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
package org.openehealth.pors.database.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.openehealth.pors.database.dao.IProviderDao;
import org.openehealth.pors.database.entities.Provider;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IProviderDao IProviderDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IProviderDao IProviderDao
 *
 */
public class ProviderJpaDao extends AbstractJpaDao<Long, Provider> implements
		IProviderDao 
{

	protected ProviderJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	public List<Provider> getAll() 
	{
		@SuppressWarnings("unchecked")
		List<Provider> lst = this.em.createNamedQuery(Provider.QUERY_NAME_ALL)
				.getResultList();

		return lst;
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllForOrganisationId(java.lang.Long)
	 */
	public List<Provider> getAllForOrganisationId(Long organisationId) 
	{
		Query q = this.em.createNamedQuery("getProviderListForOrganisationId");
		
		q.setParameter(1, organisationId);
		
		@SuppressWarnings("unchecked")
		List<Provider> lst = q.getResultList();
		
		return lst;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllForOrganisationIdOfPorsUserId(java.lang.Long, java.lang.Integer)
	 */
	public List<Provider> getAllForOrganisationIdOfPorsUserId(
			Long organisationId, Integer userId) 
	{
		Query q = this.em.createNamedQuery("getProviderListForOrganisationIdOfUserId");
		
		q.setParameter(1, organisationId);
		q.setParameter(2, userId);
		
		@SuppressWarnings("unchecked")
		List<Provider> lst = q.getResultList();
		
		return lst;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllForOrganisationIdOfPorsUserName(java.lang.Long, java.lang.String)
	 */
	public List<Provider> getAllForOrganisationIdOfPorsUserName(
			Long organisationId, String userName) 
	{
		Query q = this.em.createNamedQuery("getProviderListForOrganisationIdOfUserName");
		
		q.setParameter(1, organisationId);
		q.setParameter(2, userName);
		
		@SuppressWarnings("unchecked")
		List<Provider> lst = q.getResultList();
		
		return lst;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllMaxResultsForOrganisationIdOfPorsUserId(int, int, long, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllMaxResultsForOrganisationIdOfPorsUserId(
			int max, int position, long organisationId, int userId) 
	{
		Query q = this.em.createNamedQuery("getProviderListForOrganisationIdOfUserId");
		
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(1, organisationId);
		q.setParameter(2, userId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllMaxResultsFromPosition(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllMaxResultsFromPosition(int max, int position) 
	{
		Query q = this.em.createNamedQuery(Provider.QUERY_NAME_ALL);
		
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllMaxResultsFromPositionForOrganisationId(int, int, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllMaxResultsFromPositionForOrganisationId(
			int max, int position, long organisationId) 
	{
		Query q = this.em.createNamedQuery("getProviderListForOrganisationId");
		
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(1, organisationId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllMaxResultsFromPositionForOrganisationIdOfPorsUserName(int, int, long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllMaxResultsFromPositionForOrganisationIdOfPorsUserName(
			int max, int position, long organisationId, String userName) 
	{
		Query q = this.em.createNamedQuery("getProviderListForOrganisationIdOfUserName");
		
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(1, organisationId);
		q.setParameter(2, userName);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllMaxResultsFromPositionOfUserId(int, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllMaxResultsFromPositionOfUserId(int max,
			int position, int userId) 
	{
		Query q = em.createNamedQuery("getProviderListOfUserId");
		
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(1, userId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllMaxResultsFromPositionOfUserName(int, int, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllMaxResultsFromPositionOfUserName(int max,
			int position, String userName) 
	{
		Query q = em.createNamedQuery("getProviderListOfUserName");
		
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(1, userName);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllOfUserId(java.lang.Integer)
	 */
	public List<Provider> getAllOfUserId(Integer userId) 
	{
		Query q = em.createNamedQuery("getProviderListOfUserId");
		q.setParameter(1, userId);
		
		@SuppressWarnings("unchecked")
		List<Provider> lst = q.getResultList();
		
		return lst;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getAllOfUserName(java.lang.String)
	 */
	public List<Provider> getAllOfUserName(String userName) 
	{
		Query q = em.createNamedQuery("getProviderListOfUserName");
		q.setParameter(1, userName);
		
		@SuppressWarnings("unchecked")
		List<Provider> lst = q.getResultList();
		
		return lst;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getByLanr(java.lang.String)
	 */
	public Provider getByLanr(String lanr) 
	{
		Query q = this.em.createNamedQuery("getProviderByLanr");
		
		q.setParameter(1, lanr);
		
		try
		{
			return (Provider) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getByLanrExcludeProviderId(java.lang.String, java.lang.Long)
	 */
	public Provider getByLanrExcludeProviderId(String lanr, Long providerId) 
	{
		Query q = this.em.createNamedQuery("getProviderByLanrExcludingProviderId");
		
		q.setParameter(1, lanr);
		q.setParameter(2, providerId);
		
		try
		{
			return (Provider) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getByLocalId(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Provider getByLocalId(String localId, String application,
			String facility) 
	{
		if (application == null || facility == null || localId == null)
			throw new IllegalArgumentException(
					"Fields application, facility and localId need to be set in order to define a unique local ID."
					);
		
		Provider dbProvider;
		
		Query q = this.em.createNamedQuery("getProviderByLocalIdFacilityApplication");
		q.setParameter(1, localId);
		q.setParameter(2, facility);
		q.setParameter(3, application);
		
		try
		{
			dbProvider = (Provider) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			dbProvider = null;
		}
		
		return dbProvider;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getByOid(java.lang.String)
	 */
	public Provider getByOid(String oid) 
	{
		Query q = this.em.createNamedQuery("getProviderByOid");
		
		q.setParameter(1, oid);
		
		try
		{
			return (Provider) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getByOidExcludeProviderId(java.lang.String, java.lang.Long)
	 */
	public Provider getByOidExcludeProviderId(String oid, Long providerId) 
	{
		Query q = this.em.createNamedQuery(
					"getProviderByOidExcludingProviderId");
		
		q.setParameter(1, oid);
		q.setParameter(2, providerId);
		
		try
		{
			return (Provider) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getByUniqueCombination(java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	public Provider getByUniqueCombination(String firstName, String lastName,
			String genderCode, Date birthday) 
	{
		if (firstName == null || lastName == null || genderCode == null)
			throw new IllegalArgumentException("First name, last name and gender code are not allowed to be null.");
		
		Provider dbProvider;
		
		Query q;
		
		if (birthday == null)
		{
			q = em.createNamedQuery("getProviderByFirstNameLastNameGenderBirthdayIsNull");
		}
		else
		{
			q = em.createNamedQuery("getProviderByFirstNameLastNameGenderBirthday");
			q.setParameter(4, birthday);
		}
		
		q.setParameter(1, firstName);
		q.setParameter(2, lastName);
		q.setParameter(3, genderCode);
		
		try
		{
			dbProvider = (Provider) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			dbProvider = null;
		}
		
		return dbProvider;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getByUniqueCombinationExcludeProviderId(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.lang.Long)
	 */
	public Provider getByUniqueCombinationExcludeProviderId(String firstName,
			String lastName, String genderCode, Date birthday, Long providerId) 
	{
		Query q;
		
		if (birthday == null)
		{
			q = this.em.createNamedQuery(Provider.
					QUERY_NAME_BY_UNIQUE_COMBINATION_BIRTHDAY_NULL_EXCLUDE_PROVIDER_ID
					);
		}
		else
		{
			q = this.em.createNamedQuery(Provider.
					QUERY_NAME_BY_UNIQUE_COMBINATION_EXCLUDE_PROVIDER_ID);
			
			q.setParameter(Provider.PARAM_BIRTHDAY, birthday);
		}
		
		q.setParameter(Provider.PARAM_FIRST_NAME, firstName);
		q.setParameter(Provider.PARAM_LAST_NAME, lastName);
		q.setParameter(Provider.PARAM_GENDER_CODE, genderCode);
		q.setParameter(Provider.PARAM_PROVIDER_ID, providerId);
		
		try
		{
			return (Provider) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getCount()
	 */
	@Override
	public long getCount() 
	{
		Query q = this.em.createNamedQuery(Provider.QUERY_NAME_COUNT_ALL);
		
		Number n = (Number) q.getSingleResult();
		
		return n.longValue();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#getCountByUserId(int)
	 */
	@Override
	public long getCountByUserId(final int userId) 
	{
		final Query q = this.em.createNamedQuery(
				Provider.QUERY_NAME_COUNT_BY_USER_ID);
		
		q.setParameter(Provider.PARAM_USER_ID, userId);
		
		Number n = (Number) q.getSingleResult();
		
		return n.longValue();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#isAnyOwnedByPorsUserIdReferencingAddressId(java.lang.Integer, java.lang.Long)
	 */
	public boolean isAnyOwnedByPorsUserIdReferencingAddressId(Integer userId,
			Long addressId) 
	{
		Query q = this.em.createNamedQuery("isProviderOwningUserOfAddress");
		
		q.setParameter(1, userId);
		q.setParameter(2, addressId);
		
		Number n = (Number) q.getSingleResult();
		
		return n.intValue() > 0;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#isAnyOwnedByPorsUserIdReferencingLocalIdId(java.lang.Integer, java.lang.Long)
	 */
	public boolean isAnyOwnedByPorsUserIdReferencingLocalIdId(Integer userId,
			Long localIdId) 
	{
		Query q = this.em.createNamedQuery("isProviderOwningUserOfLocalId");
		
		q.setParameter(1, userId);
		q.setParameter(2, localIdId);
		
		Number n = (Number) q.getSingleResult();
		
		return n.intValue() > 0;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#updateDuplicatesCalculated(boolean)
	 */
	@Override
	public void updateDuplicatesCalculated(boolean calculated) 
	{
		Query q = this.em.createNamedQuery("updateDuplicatesCalculatedProvider");
		
		q.setParameter(1, calculated);
		
		q.executeUpdate();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IProviderDao#updateDuplicatesCalculatedById(long, boolean)
	 */
	@Override
	public void updateDuplicatesCalculatedById(long id, boolean calculated) 
	{
		Query q = this.em.createNamedQuery(
				Provider.QUERY_NAME_UPDATE_DUPLICATES_CALCULATED);
		
		q.setParameter(Provider.PARAM_PROVIDER_ID, id);
		q.setParameter(Provider.PARAM_CALCULATED, calculated);
		
		q.executeUpdate();
	}
}
