/*
* Copyright 2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.openehealth.pors.database.connector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.exception.SearchException;
import org.openehealth.pors.database.util.SearchCriteria;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EntitySearcherBean implements IEntitySearcher {
	private static final String[] SPECIALS = new String[] { "+", "-", "&&",
			"||", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~",
			":", "\\" };

	private static final Set<String> PROVIDER_FIELDS = new HashSet<String>(
			Arrays.asList(new String[] { "id", "lanr", "oid", "firstName",
					"lastName", "middleName", "namePrefix", "nameSuffix",
					"genderCode", "birthday", "specialisation", "email",  
					"telephone", "fax", "deactivationDate", 
					"deactivationReasonCode", "reactivationDate", 
					"reactivationReasonCode", "lastUpdateDate",
					"addresses.street", "addresses.houseNumber", 
					"addresses.zipCode", "addresses.city", "addresses.state", 
					"addresses.country", "localIds.localId", 
					"localIds.facility", "localIds.application" }));
	
	private static final Set<String> ORGANISATION_FIELDS = new HashSet<String>(
			Arrays.asList(new String[] { "id", "oid", "establishmentId", "name", 
					"secondName", "description", "email", "telephone", "fax", 
					"deactivationDate", "deactivationReasonCode", 
					"reactivationDate", "reactivationReasonCode",
					"lastUpdateDate",
					"addresses.street", "addresses.houseNumber", 
					"addresses.zipCode", "addresses.city", "addresses.state", 
					"addresses.country", "localIds.localId", 
					"localIds.facility", "localIds.application" }));
	
	private static final Set<String> HISTORY_FIELDS = new HashSet<String>(
			Arrays.asList(new String[] { "domain", "userName", "action", 
					"sessionId", "ipAddress" }));
	
	private static final int DOMAIN_PROVIDER = 1;
	private static final int DOMAIN_ORGANISATION = 2;
	
	private Logger logger;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initLogging() 
	{
		this.logger = Logger.getLogger(EntitySearcherBean.class);
		this.ftem = Search.getFullTextEntityManager(emFac.createEntityManager());
	}

	@PersistenceContext(unitName = "manager1")
	private EntityManager em;
	@PersistenceUnit(unitName = "manager1")
	private EntityManagerFactory emFac;
	private FullTextEntityManager ftem;

	/**
	 * @see org.openehealth.pors.database.connector.IEntitySearcher#searchProvider(java.util.List, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Provider> searchProvider(List<SearchCriteria> criteria,
			String operator) throws SearchException 
	{
		return this.searchDomain(DOMAIN_PROVIDER, criteria, operator);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IEntitySearcher#searchOrganisation(java.util.List, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Organisation> searchOrganisation(List<SearchCriteria> criteria, 
			String operator) throws SearchException 
	{
		return this.searchDomain(DOMAIN_ORGANISATION, criteria, operator);
	}

	@SuppressWarnings("rawtypes")
	private List searchDomain(int domain, List<SearchCriteria> criteria, 
			String operator) throws SearchException
	{
		if (criteria == null || criteria.size() == 0) {
			return null;
		}

		if (operator == null
				|| !(operator.equals(SearchCriteria.OPERATORAND) || operator
						.equals(SearchCriteria.OPERATOROR))) {
			throw new SearchException(
					"Operator was null or is not a valid operator, valid operators are: "
							+ SearchCriteria.OPERATORAND + " or "
							+ SearchCriteria.OPERATOROR);
		}

		List<String> fields = new ArrayList<String>();
		List<String> queries = new ArrayList<String>();

		for (Iterator<SearchCriteria> iterator = criteria.iterator(); iterator
				.hasNext();) {
			SearchCriteria searchCriteria = iterator.next();

			if (!(domain == DOMAIN_ORGANISATION && checkOrganisationCriteria(searchCriteria)) && 
					!(domain == DOMAIN_PROVIDER && checkProviderCriteria(searchCriteria))) 
			{
				throw new SearchException(
						"Criteria is invalid, field is not valid or value is missing");
			}

			fields.add(searchCriteria.getField());
			queries.add(escapeSpecials(searchCriteria.getValue()));
		}

		String[] strFields = new String[fields.size()];
		strFields = fields.toArray(strFields);
		
		logger.info("strFields size: " + strFields.length);
		for (int i = 0; i < strFields.length; i++) {
			logger.info("field: " + strFields[i]);
		}

		String[] strQueries = new String[queries.size()];
		strQueries = queries.toArray(strQueries);
		
		logger.info("strQueries size: " + strQueries.length);
		for (int i = 0; i < strQueries.length; i++) {
			logger.info("query: " + strQueries[i]);
		}

		Occur[] operators = new Occur[strFields.length];
		for (int i = 0; i < operators.length; i++) {
			if (operator.equals(SearchCriteria.OPERATORAND)) {
				operators[i] = Occur.MUST;
			} else if (operator.equals(SearchCriteria.OPERATOROR)) {
				operators[i] = Occur.SHOULD;
			}
		}

		Query luceneQuery;
		
		try {
			luceneQuery = MultiFieldQueryParser.parse(Version.LUCENE_29,
					strQueries, strFields, operators, new StandardAnalyzer(
							Version.LUCENE_29));
		} catch (ParseException e) {
			throw new IllegalArgumentException(
					"Search string is not parseable.", e);
		}
		
		logger.info("Query was: " + luceneQuery.toString());

		javax.persistence.Query query;
		
		if (domain == DOMAIN_ORGANISATION)
		{
			query = this.ftem.createFullTextQuery(
					luceneQuery, Organisation.class);
		}
		else
		{
			query = this.ftem.createFullTextQuery(
					luceneQuery, Provider.class);
		}

		@SuppressWarnings("unchecked")
		List<Organisation> result = query.getResultList();
		return result;
	}
	
	
	private boolean checkProviderCriteria(SearchCriteria criteria) {
		if (!checkCriteria(criteria)) {
			return false;
		}
		return PROVIDER_FIELDS.contains(criteria.getField());
	}
	
	private boolean checkOrganisationCriteria(SearchCriteria criteria)
	{
		if (!checkCriteria(criteria))
		{
			return false;
		}
		
		return ORGANISATION_FIELDS.contains(criteria.getField());
	}
	
	private boolean checkHistoryCriteria(SearchCriteria criteria)
	{
		if (!checkCriteria(criteria))
		{
			return false;
		}
		
		return HISTORY_FIELDS.contains(criteria.getField());
	}

	private boolean checkCriteria(SearchCriteria criteria) {
		if (!checkFieldAndValue(criteria.getField())
				|| !checkFieldAndValue(criteria.getValue())) {
			return false;
		}
		return true;
	}

	private boolean checkFieldAndValue(String string) {
		if (string == null) {
			return false;
		}
		String trimString = string.trim();
		return trimString.length() > 0;
	}

	private String escapeSpecials(String clientQuery) {
		String regexOr = "";
		for (String special : SPECIALS) {
			regexOr += (special.equals(SPECIALS[0]) ? "" : "|") + "\\"
					+ special.substring(0, 1);
		}
		clientQuery = clientQuery.replaceAll("(?<!\\\\)(" + regexOr + ")",
				"\\\\$1");
		return clientQuery.trim();
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IEntitySearcher#searchOrganisation(java.util.List, java.lang.String)
	 */
	@Override
	public List<History> searchLoggingEntry(List<SearchCriteria> criteria,
			String operator) throws SearchException 
	{
		if (criteria == null || criteria.size() == 0) {
			return null;
		}

		if (operator == null
				|| !(operator.equals(SearchCriteria.OPERATORAND) || operator
						.equals(SearchCriteria.OPERATOROR))) {
			throw new SearchException(
					"Operator was null or is not a valid operator, valid operators are: "
							+ SearchCriteria.OPERATORAND + " or "
							+ SearchCriteria.OPERATOROR);
		}

//		List<String> fields = new ArrayList<String>();
//		List<String> queries = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder("SELECT h FROM History h WHERE ");
		
		boolean first = true;
		for (SearchCriteria criterion : criteria)
		{
			if (!checkHistoryCriteria(criterion))
			{
				throw new SearchException(
						"Criterion is invalid, field is not valid or value is missing."
						);
			}
			
			if (first)
			{
				first = false;
			}
			else
			{
				builder.append((operator.equals(SearchCriteria.OPERATORAND)) ? "AND " : "OR ");
			}
			
			builder.append("LOWER(");
			builder.append(criterion.getField());
			builder.append(") LIKE '");
			String value = criterion.getValue().toLowerCase();
			value = value.replace('*', '%');
			value = value.replace('?', '_');
			builder.append(value);
			builder.append("' ");
		}
		
		logger.info("Query for history search was: " + builder.toString());
		
		TypedQuery<History> query = this.em.createQuery(builder.toString(), 
				History.class);
		
		List<History> histories = query.getResultList();
//		List<LoggingEntry> entries = new ArrayList<LoggingEntry>(
//				histories.size());
		
//		for (History history : histories)
//		{
//			LoggingEntry le = new LoggingEntry();
//			le.setDomain(history.getId().getDomain());
//			le.setLogId(history.getId().getLogId());
//			le.setLogTime(history.getLogTime());
//			le.setPorsUserId(history.getUserId());
//			le.setAction(history.getAction());
//			le.setUserName(history.getUserName());
//			
//			entries.add(le);
//		}
//		
//		return entries;
		return histories;
	}

	@Override
	public void rebuildSearchindex() {
		MassIndexer indexer = ftem.createIndexer(Provider.class, Organisation.class, Address.class, LocalId.class);
		try {
			indexer.startAndWait();
		} catch (InterruptedException e) {
			logger.error("Error rebuilding index", e);
		}
	}
}
