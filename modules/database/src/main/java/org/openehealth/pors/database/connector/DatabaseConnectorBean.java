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
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.OptimisticLockException;

import org.apache.log4j.Logger;
import org.openehealth.pors.database.connector.sync.AddressSynchronisation;
import org.openehealth.pors.database.connector.sync.LocalIdSynchronisation;
import org.openehealth.pors.database.dao.IDaoFactory;
import org.openehealth.pors.database.dao.IOrganisationDao;
import org.openehealth.pors.database.dao.IProviderDao;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.DuplicateAddressId;
import org.openehealth.pors.database.entities.DuplicateEntry;
import org.openehealth.pors.database.entities.DuplicateOrganisationId;
import org.openehealth.pors.database.entities.DuplicateProviderId;
import org.openehealth.pors.database.entities.DuplicateRecognition;
import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.IMasterDomain;
import org.openehealth.pors.database.entities.ImportResult;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.LoggingEntry;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.entities.UserHistory;
import org.openehealth.pors.database.entities.UserRole;
import org.openehealth.pors.database.exception.DataException;
import org.openehealth.pors.database.exception.DatabaseException;
import org.openehealth.pors.database.exception.DoubleEstablishmentIdException;
import org.openehealth.pors.database.exception.DoubleLanrException;
import org.openehealth.pors.database.exception.DoubleMasterDomainException;
import org.openehealth.pors.database.exception.DoubleNameException;
import org.openehealth.pors.database.exception.DoubleOidException;
import org.openehealth.pors.database.exception.DoublePorsUserException;
import org.openehealth.pors.database.util.EntityUtil;
import org.openehealth.pors.database.util.LoggingUtil;

/**
 * Implementation of {@link IDatabaseConnector}
 * @see IDatabaseConnector
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DatabaseConnectorBean implements IDatabaseConnector 
{	
	@EJB
	private IDaoFactory daos;
	
	private Logger logger;

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#addImportResult(org.openehealth.pors.database.entities.ImportResult)
	 */
	@Override
	public void addImportResult(ImportResult result) {
		this.daos.getImportResultDao().insert(result);
	}
	
	/**
	 * @throws IllegalArgumentException 
	 * 		If organisation does not contain a session ID, IP address or 
	 * 		editing user
	 *  @see IDatabaseConnector#addOrganisation(Organisation)
	 */
	@Override
	public void addOrganisation(Organisation organisation)
			throws DatabaseException 
	{
		EntityUtil.verifyLoggingData(organisation);
		
		String loggingAction = "Add Organisation";
		
		LoggingUtil.info(loggingAction, LoggingUtil.LOG_ACTION_START);
		
		try
		{	
			this.checkUniqueConstraintsOfOrganisation(organisation);
			
			Date logTime = new Date();
			
			DatabaseLogger dbLogger = new DatabaseLogger(this.daos, logTime, 
					organisation.getEditingUser(), organisation.getSessionId(), 
					organisation.getIpAddress());
			
			/*
			 * Store providers to a variable and set the list in 
			 * organisation to null if this functionality is needed any time.
			 */
			
			this.addMasterDomain(organisation, loggingAction, dbLogger);
			
			/*
			 * Synchronise providers, add them to organisation and log them 
			 * here, if this functionality is needed any time
			 */
			
			LoggingUtil.info(loggingAction, "Logging Organisation");
			dbLogger.logOrganisation(DatabaseLogger.LOG_ACTION_INSERT, 
					null, organisation);
			
			LoggingUtil.info(loggingAction, LoggingUtil.LOG_ACTION_COMPLETE);
		}
		catch (DataException e)
		{
			LoggingUtil.warn(loggingAction, LoggingUtil.LOG_ACTION_FAIL, e);
			throw new DatabaseException(e.getMessage(), e);
		}
		catch (Exception e)
		{
			LoggingUtil.error(loggingAction, LoggingUtil.LOG_ACTION_FAIL, e);
			throw new DatabaseException(
					"Could not add organisation due to an unexpected error.", e);
		}
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#addPorsUser(org.openehealth.pors.database.entities.PorsUser)
	 */
	@Override
	public void addPorsUser(PorsUser user) throws DatabaseException 
	{
		if (user.getName() == null)
		{
			throw new DatabaseException("A valid user name has to be set.");
		}
		
		if (user.getRole() == null || user.getRole().getId() == null ||
				user.getRole().getId().longValue() <= 0)
		{
			throw new DatabaseException(
					"A existing user role including its ID has to be set.");
		}
		
		try
		{
			this.checkUniqueConstraintsOfUser(user);
		}
		catch (DataException e)
		{
			throw new DatabaseException(e.getMessage(), e);
		}
		
		UserRole role = user.getRole();
		user.setRole(null);
		
		UserRole roleRef = this.daos.getUserRoleDao().getReferenceById(
				role.getId());

		if (roleRef == null)
		{
			throw new DatabaseException(
					"Could not find a user role with given ID: " + 
					user.getRole().getId());
		}
		
		user.setRole(roleRef);
		
		this.daos.getPorsUserDao().insert(user);
	}
	
	/**
	 * @throws IllegalArgumentException 
	 * 		If session id or editing user were not set in provider
	 *  @see IDatabaseConnector#addProvider(Provider)
	 */
	public void addProvider(Provider provider) throws DatabaseException 
	{
		EntityUtil.verifyLoggingData(provider);
		
		final String loggingAction = "Add Provider";
		
		LoggingUtil.info(loggingAction, LoggingUtil.LOG_ACTION_START);
		
		try
		{
			this.checkUniqueConstraintsOfProvider(provider);
			
			Date logTime = new Date();
			
			DatabaseLogger dbLogger = new DatabaseLogger(this.daos, logTime, 
					provider.getEditingUser(), provider.getSessionId(), 
					provider.getIpAddress());
			
			List<Organisation> organisations = provider.getOrganisations();
			provider.setOrganisations(null);
			
			this.addMasterDomain(provider, loggingAction, dbLogger);
			
			if (organisations != null && organisations.size() > 0)
			{
				List<Organisation> syncOrganisations = this.synchroniseOrganisations(provider, organisations, null);
				provider.setOrganisations(syncOrganisations);
			}
			
			LoggingUtil.info(loggingAction, "Logging Provider");
			dbLogger.logProvider(DatabaseLogger.LOG_ACTION_INSERT, null, 
					provider);
			
			LoggingUtil.info(loggingAction, "Logging Relations to Organisations");
			if (provider.getOrganisations() != null && provider.getOrganisations().size() > 0)
			{
				dbLogger.logOrganisationToProviderRelations(provider.getId(), 
						null, provider.getOrganisations());
			}
		}
		catch (DataException e)
		{
			LoggingUtil.warn(loggingAction, LoggingUtil.LOG_ACTION_FAIL, e);
			throw new DatabaseException(e.getMessage(), e);
		}
		catch (Exception e)
		{
			LoggingUtil.error(loggingAction, LoggingUtil.LOG_ACTION_FAIL, e);
			throw new DatabaseException(
					"Could not add provider due to an unexpected error.", e);
		}
		
		LoggingUtil.info(loggingAction, LoggingUtil.LOG_ACTION_COMPLETE);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#deleteAddress(long)
	 */
	@Override
	public void deleteAddress(long addressId, PorsUser editingUser, String ipAddress, String sessionId) 
	{
		Address a = this.daos.getAddressDao().getById(addressId);
		
		DatabaseLogger logger = new DatabaseLogger(this.daos, new Date(), 
				editingUser, sessionId, ipAddress);
		logger.logAddressDelete(a);
		
		this.daos.getAddressDao().delete(a);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#deleteAddressDuplicates(long)
	 */
	@Override
	public void deleteAddressDuplicates(long addressId) 
	{
		this.daos.getDuplicateAddressDao().deleteByAddressId(addressId);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#deleteOrganisation(long)
	 */
	@Override
	public void deleteOrganisation(long organisationId, PorsUser editingUser, 
			String ipAddress, String sessionId) 
	{
		Organisation o = this.daos.getOrganisationDao().getById(
				organisationId);
		
		DatabaseLogger logger = new DatabaseLogger(this.daos, new Date(),
				editingUser, sessionId, ipAddress);
		logger.logOrganisation(DatabaseLogger.LOG_ACTION_DELETE, o, null);
		
		this.daos.getOrganisationDao().delete(o);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#deleteOrganisationDuplicates(long)
	 */
	@Override
	public void deleteOrganisationDuplicates(long organisationId) 
	{
		this.daos.getDuplicateOrganisationDao().
				deleteByOrganisationId(organisationId);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#deleteProvider(long)
	 */
	@Override
	public void deleteProvider(long providerId, PorsUser editingUser, 
			String ipAddress, String sessionId) 
	{
		Provider p = this.daos.getProviderDao().getById(providerId);
		List<Organisation> orgs = p.getOrganisations();
		for (Organisation organisation : orgs) {
			int toRemove = 0;
			for (int i = 0; i < organisation.getProviders().size(); i++) {
				 if (!EntityUtil.haveDifferences(organisation.getProviders().get(i), p)) {
					toRemove = i;
				}
			}
			if (toRemove == 0) {
				logger.warn("provider not found for organisation");
			} else {
				organisation.getProviders().remove(toRemove);
				this.daos.getOrganisationDao().update(organisation);
			}
		}
		p.setOrganisations(null);
		
		DatabaseLogger logger = new DatabaseLogger(this.daos, new Date(), 
				editingUser, sessionId, ipAddress);
		logger.logProvider(DatabaseLogger.LOG_ACTION_DELETE, p, null);
		
		this.daos.getProviderDao().delete(p);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#deleteProviderDuplicates(long)
	 */
	@Override
	public void deleteProviderDuplicates(long providerId) 
	{
		this.daos.getDuplicateProviderDao().deleteByProviderId(providerId);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#flushAndClearConnection()
	 */
	@Override
	public void flushAndClearConnection() 
	{
		if (this.daos.getEntityManager() != null)
		{
			this.daos.getEntityManager().flush();
			this.daos.getEntityManager().clear();
		}
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getAddressById(long)
	 */
	@Override
	public Address getAddressById(long id) 
	{
		return this.daos.getAddressDao().getById(id);
	}

	/**
	 * @see IDatabaseConnector#getAddressByUniqueCombination(String, String, String, String, String, String)
	 */
	public Address getAddressByUniqueCombination(String additional, 
			String street, String houseNumber, String zipCode, String city, 
			String country)
	{
		return this.daos.getAddressDao().getByUniqueCombination(additional, 
				street, houseNumber, zipCode, city, country);
	}
	
	/**
	 * @throws IllegalArgumentException
	 * 		If the argument is null
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getDetailedDuplicateEntry(org.openehealth.pors.database.entities.DuplicateEntry)
	 */
	@Override
	public Object getDetailedDuplicateEntry(DuplicateEntry entry) 
	{
		if (entry == null)
		{
			throw new IllegalArgumentException(
					"Argument is not allowed to be null.");
		}
		
		String domain = entry.getId().getDomain();
		
		if (domain == null)
		{
			return null;
		}
		
		if (domain.equals(DuplicateEntry.DOMAIN_ADDRESS))
		{
			return this.daos.getDuplicateAddressDao().getById(
					new DuplicateAddressId(entry.getId().getId1(), 
							entry.getId().getId2()));
		}
		else if (domain.equals(DuplicateEntry.DOMAIN_ORGANISATION))
		{
			return this.daos.getDuplicateOrganisationDao().getById(
					new DuplicateOrganisationId(entry.getId().getId1(), 
							entry.getId().getId2()));
		}
		else if (domain.equals(DuplicateEntry.DOMAIN_PROVIDER))
		{
			return this.daos.getDuplicateProviderDao().getById(
					new DuplicateProviderId(entry.getId().getId1(), 
							entry.getId().getId2()));
		}
		else
		{
			return null;
		}
	}

	/**
	 * @throws IllegalArgumentException
	 * 		If argument is null
	 *  @see IDatabaseConnector#getDetailedHistory(History)
	 */
	@Override
	public Object getDetailedHistory(History logEntry)
	{	
		if (logEntry == null)
		{
			throw new IllegalArgumentException(
					"Log entry is not allowed to be null.");
		}
		
		if(logEntry.getLogId() == 0 || logEntry.getDomain() == null)
		{
			return null;
		}
		
		if (logEntry.getDomain().equals(LoggingEntry.DOMAIN_PROVIDER))
		{
			return this.daos.getProviderLogDao().getById(logEntry.getLogId());
		}
		else if (logEntry.getDomain().equals(LoggingEntry.DOMAIN_ORGANISATION))
		{
			return this.daos.getOrganisationLogDao().getById(
					logEntry.getLogId());
		}
		else if (logEntry.getDomain().equals(LoggingEntry.DOMAIN_ADDRESS))
		{
			return this.daos.getAddressLogDao().getById(logEntry.getLogId());
		}
		else if (logEntry.getDomain().equals(LoggingEntry.DOMAIN_LOCALID))
		{
			return this.daos.getLocalIdLogDao().getById(logEntry.getLogId());
		}
		else if (logEntry.getDomain().equals(LoggingEntry.DOMAIN_ORGANISATION_HAS_ADDRESS))
		{
			return this.daos.getOrganisationHasAddressLogDao().getById(
					logEntry.getLogId());
		}
		else if (logEntry.getDomain().equals(LoggingEntry.DOMAIN_PROVIDER_HAS_ADDRESS))
		{
			return this.daos.getProviderHasAddressLogDao().getById(
					logEntry.getLogId());
		}
		else if (logEntry.getDomain().equals(LoggingEntry.DOMAIN_ORGANISATION_HAS_PROVIDER))
		{
			return this.daos.getOrganisationHasProviderLogDao().getById(
					logEntry.getLogId());
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getDuplicateConfiguration()
	 */
	@Override
	public List<DuplicateRecognition> getDuplicateConfiguration() 
	{
		return this.daos.getDuplicateRecognitionDao().getAll();
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getDuplicateEntryList()
	 */
	@Override
	public List<DuplicateEntry> getDuplicateEntryList() 
	{
		return this.daos.getDuplicateEntryDao().getAll();
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getDuplicateEntryNumber()
	 */
	@Override
	public long getDuplicateEntryNumber() 
	{
		return this.daos.getDuplicateEntryDao().getCount();
	}

	/**
	 *  @see IDatabaseConnector#getHistory()
	 */
	@Override
	public List<History> getHistory() 
	{
		return this.daos.getHistoryDao().getAll();
	}
	
	/**
	 * @see IDatabaseConnector#getHistoryForUser(PorsUser)
	 */
	@Override
	public List<UserHistory> getHistoryForUser(PorsUser user) 
	{
		final PorsUser dbUser;
		
		if (user.getId() == null || user.getId().intValue() <= 0)
		{
			dbUser = this.getUser(user);
		}
		else
		{
			dbUser = user;
		}
		
		List<UserHistory> histories = this.daos.getUserHistoryDao().
				getAllByUserId(dbUser.getId());
		
		return histories;
	}
	
	/**
	 * @throws IllegalArgumentException
	 * 		If any of the parameters is negative
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getHistoryHavingMaxEntriesFromPosition(int, int)
	 */
	@Override
	public List<History> getHistoryHavingMaxEntriesFromPosition(int max,
			int position) 
	{
		List<History> histories;
		
		if (max == 0 && position == 0)
		{
			histories = this.daos.getHistoryDao().
					getAll();
		}
		else
		{
			histories = this.daos.getHistoryDao().
					getAllMaxResultsFromPosition(max, position);
		}
		
		return histories;
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getHistoryNumber()
	 */
	@Override
	public long getHistoryNumber() 
	{
		return this.daos.getHistoryDao().getCount();
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getHistoryNumber(org.openehealth.pors.database.entities.PorsUser)
	 * @throws IllegalArgumentException 
	 * 		If neither a valid ID nor a valid name was set for <code>user</code>
	 */
	@Override
	public long getHistoryNumber(PorsUser user) 
	{
		if (user == null)
		{
			return this.getHistoryNumber();
		}
		
		PorsUser dbUser = this.getUser(user);
		
		if (dbUser == null)
		{
			return 0;
		}
		
		return this.daos.getHistoryDao().getCountByUserId(user.getId());
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getImportResult(org.openehealth.pors.database.entities.ImportResult)
	 */
	@Override
	public ImportResult getImportResult(ImportResult result) 
	{
		return this.daos.getImportResultDao().getById(result.getJobId());
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedDuplicateEntryList(int, int)
	 */
	@Override
	public List<DuplicateEntry> getLimitedDuplicateEntryList(int max,
			int position) 
	{
		if (max == 0 && position == 0)
		{
			return this.getDuplicateEntryList();
		}
		
		return this.daos.getDuplicateEntryDao().
				getAllMaxResultsFromPosition(max, position);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedHistoryForUser(int, int, org.openehealth.pors.database.entities.PorsUser)
	 */
	@Override
	public List<UserHistory> getLimitedHistoryForUser(int max, int position,
			PorsUser user) 
	{
		if (max == 0 && position == 0)
		{
			return this.getHistoryForUser(user);
		}
		
		final PorsUser dbUser;
		
		if (user.getId() == null || user.getId().intValue() <= 0)
		{
			dbUser = this.getUser(user);
		}
		else
		{
			dbUser = user;
		}
		
		return this.daos.getUserHistoryDao().
				getAllMaxResultsFromPositionByUserId(max, position, dbUser.getId());
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedOrganisationListForProvider(int, int, org.openehealth.pors.database.entities.Provider)
	 */
	@Override
	public List<Organisation> getLimitedOrganisationListForProvider(int max,
			int position, Provider provider) 
	{
		if (max == 0 && position == 0)
		{
			return this.getOrganisationListForProvider(provider);
		}
		
		Long providerId;
		
		if (provider.getId() != null && provider.getId() > 0)
		{
			providerId = provider.getId();
		}
		else
		{
			Provider dbProvider = this.getProvider(provider);
			
			if (dbProvider == null)
			{
				throw new IllegalArgumentException(
						"Given provider does not exist in database.");
			}
			
			providerId = dbProvider.getId();
		}
		
		return this.daos.getOrganisationDao().
				getAllMaxResultsFromPositionForProviderId(max, position, 
						providerId);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedOrganisationsListForProviderOfUser(int, int, org.openehealth.pors.database.entities.Provider, org.openehealth.pors.database.entities.PorsUser)
	 */
	@Override
	public List<Organisation> getLimitedOrganisationListForProviderOfUser(int max,
			int position, Provider provider, PorsUser user) 
	{
		if (max == 0 && position == 0)
		{
			return this.getOrganisationListForProviderOfUser(provider, user);
		}
		
		if ((user.getId() == null || user.getId().longValue() <= 0) && user.getName() == null)
		{
			throw new IllegalArgumentException(
					"Need id or name to identify a single user.");
		}
		
		Long providerId;
		if (provider.getId() != null && provider.getId().longValue() > 0)
		{
			providerId = provider.getId();
		}
		else
		{
			Provider dbProvider = this.getProvider(provider);
			
			if (dbProvider == null)
			{
				throw new IllegalArgumentException(
						"Given provider does not exist in database.");
			}
			
			providerId = dbProvider.getId();
		}
		
		List<Organisation> dbOrganisations;
		if (user.getId() != null)
		{
			dbOrganisations = this.daos.getOrganisationDao().
				getAllMaxResultsFromPositionForProviderIdOfPorsUserId(
						max, position, providerId, user.getId());
		}
		else //if (user.getName() != null)
		{
			dbOrganisations = this.daos.getOrganisationDao().
			getAllMaxResultsFromPositionForProviderIdOfPorsUserName(
					max, position, providerId, user.getName());
		}
		
		return dbOrganisations;
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedOrganisationListOfUser(int, int, org.openehealth.pors.database.entities.PorsUser)
	 */
	@Override
	public List<Organisation> getLimitedOrganisationListOfUser(int max,
			int position, PorsUser user) 
	{
		if (position == 0 && max == 0)
		{
			return this.getOrganisationListOfUser(user);
		}
		
		if(user.getId() != null && user.getId().intValue() > 0)
		{
			return this.daos.getOrganisationDao().
					getAllMaxResultsFromPositionOfPorsUserId(max, position, 
							user.getId());
		}
		else if (user.getName() != null)
		{
			return this.daos.getOrganisationDao().
					getAllMaxResultsFromPositionOfPorsUserName(max, position, 
							user.getName());
		}
		else
		{
			throw new IllegalArgumentException(
					"Either id or name of the user has to be specified.");
		}
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedProviderListForOrganisation(int, int, org.openehealth.pors.database.entities.Organisation)
	 */
	@Override
	public List<Provider> getLimitedProviderListForOrganisation(int max,
			int position, Organisation organisation) 
	{
		if (max == 0 && position == 0)
		{
			return this.getProviderListForOrganisation(organisation);
		}
		
		Long organisationId;
		if (organisation.getId() != null && organisation.getId().longValue() > 0)
		{
			organisationId = organisation.getId();
		}
		else
		{
			Organisation dbOrganisation = this.getOrganisation(organisation);
			
			if (dbOrganisation == null)
			{
				throw new IllegalArgumentException(
						"Given organisation does not exist in database.");
			}
			
			organisationId = dbOrganisation.getId();
		}
		
		return this.daos.getProviderDao().
				getAllMaxResultsFromPositionForOrganisationId(max, position, 
						organisationId);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedProviderListForOrganisationOfUser(int, int, org.openehealth.pors.database.entities.Organisation, org.openehealth.pors.database.entities.PorsUser)
	 */
	@Override
	public List<Provider> getLimitedProviderListForOrganisationOfUser(int max,
			int position, Organisation organisation, PorsUser user) 
	{
		if ((user.getId() == null || user.getId().longValue() <= 0) && 
				user.getName() == null)
		{
			throw new IllegalArgumentException(
					"Need id or name to identify a single user.");
		}
		
		if (max == 0 && position == 0)
		{
			return this.getProviderListForOrganisationOfUser(organisation, 
					user);
		}
		
		Long organisationId;
		
		if (organisation.getId() != null)
		{
			organisationId = organisation.getId();
		}
		else
		{
			Organisation dbOrganisation = this.getOrganisation(organisation);
			
			if (dbOrganisation == null)
			{
				throw new IllegalArgumentException("Given organisation does not exist in database.");
			}
			
			organisationId = organisation.getId();
		}
		
		List<Provider> dbProviders;
		if (user.getId() != null)
		{
			dbProviders = this.daos.getProviderDao().
					getAllMaxResultsForOrganisationIdOfPorsUserId(max, 
							position, organisationId, user.getId());
		}
		else //if (user.getName() != null)
		{
			dbProviders = this.daos.getProviderDao().
					getAllMaxResultsFromPositionForOrganisationIdOfPorsUserName(
							max, position, organisationId, user.getName());
		}
		
		return dbProviders;
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLimitedProviderListOfUser(int, int, org.openehealth.pors.database.entities.PorsUser)
	 */
	@Override
	public List<Provider> getLimitedProviderListOfUser(int max, int position,
			PorsUser user) 
	{
		if (max == 0 && position == 0)
		{
			return this.getProviderListOfUser(user);
		}
		
		if(user.getId() != null && user.getId().intValue() > 0)
		{
			return this.daos.getProviderDao().
					getAllMaxResultsFromPositionOfUserId(max, position, 
							user.getId());
		}
		else if (user.getName() != null)
		{
			return this.daos.getProviderDao().
					getAllMaxResultsFromPositionOfUserName(max, position, 
							user.getName());
		}
		else
		{
			throw new IllegalArgumentException(
					"Either id or name of the user has to be specified.");
		}
	}
	
	/**
	 * @see IDatabaseConnector#getLocalId(LocalId)
	 */
	public LocalId getLocalId(LocalId lid)
	{
		LocalId dbLid = null;
		
		if (lid.getId() != null && lid.getId().longValue() > 0)
		{
			this.daos.getLocalIdDao().getById(lid.getId());
		}
		else if (lid.getLocalId() != null && lid.getFacility() != null)
		{
			this.daos.getLocalIdDao().getByUniqueCombination(lid.getLocalId(), 
					lid.getFacility(), lid.getApplication());
		}
		else
		{
			throw new IllegalArgumentException("Attribute id or the combination of local id and facility have to be set in order to select a single local id entry.");
		}
		
		return dbLid;
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLocalIdById(long)
	 */
	@Override
	public LocalId getLocalIdById(long id) 
	{
		return this.daos.getLocalIdDao().getById(id);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getLocalIdByUniqueCombination(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public LocalId getLocalIdByUniqueCombination(String localId, 
			String facility, String application)
	{
		return this.daos.getLocalIdDao().getByUniqueCombination(
				localId, facility, application);
	}
	
	/**
	 * @see IDatabaseConnector#getOrganisationByEstablishmentId(String)
	 */
	public Organisation getOrganisationByEstablishmentId(
			String establishmentId) 
	{
		return this.daos.getOrganisationDao().getByEstablishmentId(
				establishmentId);
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getOrganisationById(long)
	 */
	@Override
	public Organisation getOrganisationById(long id) 
	{
		return this.daos.getOrganisationDao().getById(id);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getOrganisationByLocalId(org.openehealth.pors.database.entities.LocalId)
	 */
	@Override
	public Organisation getOrganisationByLocalId(LocalId localId) 
	{
		return this.daos.getOrganisationDao().getByLocalId(
				localId.getLocalId(), localId.getApplication(), 
				localId.getFacility());
	}
	
	/**
	 * @see IDatabaseConnector#getOrganisationByName(String)
	 */
	@Override
	public Organisation getOrganisationByName(String name) 
	{
		return this.daos.getOrganisationDao().getByName(name);
	}
	
	/**
	 * @see IDatabaseConnector#getOrganisationByOid(String)
	 */
	@Override
	public Organisation getOrganisationByOid(String oid) 
	{
		return this.daos.getOrganisationDao().getByOid(oid);
	}

	/**
	 *  @see IDatabaseConnector#getOrganisationList()
	 */
	@Override
	public List<Organisation> getOrganisationList() 
	{	
		return this.daos.getOrganisationDao().getAll();
	}

	/**
	 *  @see IDatabaseConnector#getOrganisationListForProvider(Provider)
	 */
	@Override
	public List<Organisation> getOrganisationListForProvider(Provider provider) 
	{
		Long providerId;
		
		if (provider.getId() != null && provider.getId() > 0)
		{
			providerId = provider.getId();
		}
		else
		{
			Provider dbProvider = this.getProvider(provider);
			
			if (dbProvider == null)
			{
				throw new IllegalArgumentException(
						"Given provider does not exist in database.");
			}
			
			providerId = dbProvider.getId();
		}
		
		return this.daos.getOrganisationDao().getAllForProviderId(providerId);
	}

	/**
	 *  @see IDatabaseConnector#getOrganisationListForProviderOfUser(Provider, PorsUser)
	 */
	@Override
	public List<Organisation> getOrganisationListForProviderOfUser(
			Provider provider, PorsUser user) {
		if ((user.getId() == null || user.getId().longValue() <= 0) && user.getName() == null)
		{
			throw new IllegalArgumentException(
					"Need id or name to identify a single user.");
		}
		
		Long providerId;
		if (provider.getId() != null && provider.getId().longValue() > 0)
		{
			providerId = provider.getId();
		}
		else
		{
			Provider dbProvider = this.getProvider(provider);
			
			if (dbProvider == null)
			{
				throw new IllegalArgumentException(
						"Given provider does not exist in database.");
			}
			
			providerId = dbProvider.getId();
		}
		
		List<Organisation> dbOrganisations;
		if (user.getId() != null)
		{
			dbOrganisations = this.daos.getOrganisationDao().
				getAllForProviderIdOfPorsUserId(providerId, user.getId());
		}
		else //if (user.getName() != null)
		{
			dbOrganisations = this.daos.getOrganisationDao().
				getAllForProviderIdOfPorsUserName(providerId, user.getName());
		}
		
		return dbOrganisations;
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getOrganisationListHavingMaxEntriesFromPosition(int, int)
	 */
	@Override
	public List<Organisation> getOrganisationListHavingMaxEntriesFromPosition(
			int max, int position) 
	{
		List<Organisation> organisations;
		
		if (max == 0 && position == 0)
		{
			organisations = this.daos.getOrganisationDao().getAll();
		}
		else
		{
			organisations = this.daos.getOrganisationDao().
					getAllMaxResultsFromPosition(max, position);
		}

		return organisations;
	}
	
	/**
	 * @see IDatabaseConnector#getOrganisationListOfUser(PorsUser)
	 */
	@Override
	public List<Organisation> getOrganisationListOfUser(PorsUser user) 
	{
		if(user.getId() != null && user.getId().intValue() > 0)
		{
			return this.daos.getOrganisationDao().getAllOfPorsUserId(
					user.getId());
		}
		else if (user.getName() != null)
		{
			return this.daos.getOrganisationDao().
					getAllOfPorsUserName(user.getName());
		}
		else
		{
			throw new IllegalArgumentException(
					"Either id or name of the user has to be specified.");
		}
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getOrganisationNumber()
	 */
	@Override
	public long getOrganisationNumber() 
	{
		return this.daos.getOrganisationDao().getCount();
	}
	
	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getOrganisationNumber(org.openehealth.pors.database.entities.PorsUser)
	 * @throws IllegalArgumentException 
	 * 		If neither a valid ID nor a valid name was set for <code>user</code>
	 */
	@Override
	public long getOrganisationNumber(PorsUser user) 
	{
		if (user == null)
		{
			return this.getOrganisationNumber();
		}
		
		PorsUser dbUser = this.getUser(user);
		
		if (dbUser == null)
		{
			return 0;
		}
		
		return this.daos.getOrganisationDao().getCountByUserId(user.getId());
	}
	
	/**
	 * @see IDatabaseConnector#getProviderById(long)
	 */
	@Override
	public Provider getProviderById(final long id) 
	{
		return this.daos.getProviderDao().getById(id);
	}

	/**
	 * @see IDatabaseConnector#getProviderByLocalId(LocalId)
	 */
	@Override
	public Provider getProviderByLocalId(final LocalId localId) {
		return this.daos.getProviderDao().getByLocalId(localId.getLocalId(), 
				localId.getApplication(), localId.getFacility());
	}

	/**
	 * @see IDatabaseConnector#getProviderByUniqueCombination(String, String, String, Date)
	 */
	@Override
	public Provider getProviderByUniqueCombination(final String firstName,
			final String lastName, final String genderCode, final Date birthday) 
	{
		return this.daos.getProviderDao().getByUniqueCombination(firstName, 
				lastName, genderCode, birthday);
	}

	/**
	 *  @see IDatabaseConnector#getProviderList()
	 */
	@Override
	public List<Provider> getProviderList() 
	{
		return this.daos.getProviderDao().getAll();
	}

	/**
	 * @see IDatabaseConnector#getProviderListForOrganisation(Organisation)
	 */
	@Override
	public List<Provider> getProviderListForOrganisation(
			Organisation organisation) 
	{
		Long organisationId;
		if (organisation.getId() != null && organisation.getId().longValue() > 0)
		{
			organisationId = organisation.getId();
		}
		else
		{
			Organisation dbOrganisation = this.getOrganisation(organisation);
			
			if (dbOrganisation == null)
			{
				throw new IllegalArgumentException(
						"Given organisation does not exist in database.");
			}
			
			organisationId = dbOrganisation.getId();
		}
		
		return this.daos.getProviderDao().getAllForOrganisationId(
				organisationId);
	}
	
	/**
	 * @see IDatabaseConnector#getProviderListForOrganisationOfUser(Organisation, PorsUser)
	 */
	@Override
	public List<Provider> getProviderListForOrganisationOfUser(
			Organisation organisation, PorsUser user) {
		if ((user.getId() == null || user.getId().longValue() <= 0) && user.getName() == null)
		{
			throw new IllegalArgumentException("Need id or name to identify a single user.");
		}
		
		Long organisationId;
		
		if (organisation.getId() != null)
		{
			organisationId = organisation.getId();
		}
		else
		{
			Organisation dbOrganisation = this.getOrganisation(organisation);
			
			if (dbOrganisation == null)
			{
				throw new IllegalArgumentException("Given organisation does not exist in database.");
			}
			
			organisationId = organisation.getId();
		}
		
		List<Provider> dbProviders;
		if (user.getId() != null)
		{
			dbProviders = this.daos.getProviderDao().
				getAllForOrganisationIdOfPorsUserId(
						organisationId, user.getId());
		}
		else //if (user.getName() != null)
		{
			dbProviders = this.daos.getProviderDao().
				getAllForOrganisationIdOfPorsUserName(organisationId, 
						user.getName());
		}
		
		return dbProviders;
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getProviderListHavingMaxEntriesFromPosition(int, int)
	 */
	@Override
	public List<Provider> getProviderListHavingMaxEntriesFromPosition(int max,
			int position) 
	{
		List<Provider> providers;
		
		if (max == 0 && position == 0)
		{
			providers = this.daos.getProviderDao().getAll();
		}
		else
		{
			providers = this.daos.getProviderDao().
					getAllMaxResultsFromPosition(max, position);
		}
		
		return providers;
	}

	/**
	 * @see IDatabaseConnector#getProviderListOfUser(PorsUser)
	 */
	@Override
	public List<Provider> getProviderListOfUser(PorsUser user) 
	{	
		if(user.getId() != null && user.getId().intValue() > 0)
		{
			return this.daos.getProviderDao().getAllOfUserId(user.getId());
		}
		else if (user.getName() != null)
		{
			return this.daos.getProviderDao().getAllOfUserName(user.getName());
		}
		else
		{
			throw new IllegalArgumentException(
					"Either id or name of the user has to be specified.");
		}
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getProviderNumber()
	 */
	@Override
	public long getProviderNumber() 
	{
		return this.daos.getProviderDao().getCount();
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getProviderNumber(org.openehealth.pors.database.entities.PorsUser)
	 * @throws IllegalArgumentException
	 * 		If neither a valid ID nor a valid name was set for <code>user</code>
	 */
	@Override
	public long getProviderNumber(PorsUser user) 
	{
		if (user == null)
		{
			return this.getProviderNumber();
		}
		
		PorsUser dbUser = this.getUser(user);
		
		if (dbUser == null)
		{
			return 0;
		}
		
		return this.daos.getProviderDao().getCountByUserId(user.getId());
	}

	/**
	 *  @see IDatabaseConnector#getUser(PorsUser)
	 */
	@Override
	public PorsUser getUser(PorsUser user) 
	{
		final PorsUser dbUser;
		
		if(user.getId() != null && user.getId().intValue() > 0)
		{
			dbUser = this.daos.getPorsUserDao().getById(user.getId());
		}
		else if (user.getName() != null)
		{
			dbUser = this.daos.getPorsUserDao().getByName(user.getName());
		}
		else
		{
			throw new IllegalArgumentException("Need unique identifiers \"id\" or \"name\" to load a single PORS user.");
		}
		
		return dbUser;
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getUserList()
	 */
	@Override
	public List<PorsUser> getUserList() 
	{
		return this.daos.getPorsUserDao().getAll();
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#getUserRoleList()
	 */
	@Override
	public List<UserRole> getUserRoleList() 
	{
		return this.daos.getUserRoleDao().getAll();
	}

	/**
	 * @see IDatabaseConnector#isOwningUser(PorsUser, Address)
	 */
	@Override
	public boolean isOwningUser(PorsUser user, Address address) 
	{
		if ((user.getId() == null || user.getId() <= 0) && user.getName() == null)
		{
			throw new IllegalArgumentException(
					"Need id or name to identify user.");
		}
		
		Integer userId;
		if (user.getId() != null)
		{
			userId = user.getId();
		}
		else //if (user.getName() != null)
		{
			PorsUser dbUser = this.getUser(user);
			
			if (dbUser == null)
			{
				throw new IllegalArgumentException(
						"Given user does not exist in database.");
			}
			
			userId = dbUser.getId();
		}
		
		Long addressId;
		if (address.getId() != null && address.getId() > 0)
		{
			addressId = address.getId();
		}
		else
		{
			Address dbAddress = this.getAddress(address);
			
			if (dbAddress == null)
			{
				throw new IllegalArgumentException(
						"Given address does not exist in database.");
			}
			
			addressId = dbAddress.getId();
		}
		
		if (this.daos.getProviderDao().
				isAnyOwnedByPorsUserIdReferencingAddressId(userId, addressId))
		{
			return true;
		}
		
		if (this.daos.getOrganisationDao().
				isAnyOwnedByPorsUserIdReferencingAddressId(userId, addressId))
		{
			return true;
		}
		
		return false;
	}

	/**
	 * @see IDatabaseConnector#isOwningUser(PorsUser, LocalId)
	 */
	@Override
	public boolean isOwningUser(PorsUser user, LocalId localId) {
		if ((user.getId() == null || user.getId() <= 0) && user.getName() == null)
		{
			throw new IllegalArgumentException(
					"Need id or name to identify user.");
		}
		
		Integer userId;
		if (user.getId() != null)
		{
			userId = user.getId();
		}
		else //if (user.getName() != null)
		{
			PorsUser dbUser = this.getUser(user);
			
			if (dbUser == null)
			{
				throw new IllegalArgumentException(
						"Given user does not exist in database.");
			}
			
			userId = dbUser.getId();
		}
		
		Long lidId;
		if (localId.getId() != null && localId.getId() > 0)
		{
			lidId = localId.getId();
		}
		else
		{
			LocalId dbLid = this.getLocalId(localId);
			
			if (dbLid == null)
			{
				throw new IllegalArgumentException(
						"Given address does not exist in database.");
			}
			
			lidId = dbLid.getId();
		}
		
		if (this.daos.getProviderDao().
				isAnyOwnedByPorsUserIdReferencingLocalIdId(userId, lidId))
		{
			return true;
		}
		
		if (this.daos.getOrganisationDao().
				isAnyOwnedByPorsUserIdReferencingLocalIdId(userId, lidId))
		{
			return true;
		}
		
		return false;
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#setAddressDuplicatesCalculated(long, boolean)
	 */
	@Override
	public void setAddressDuplicatesCalculated(long addressId,
			boolean isDuplicatesCalculated)
	{
		this.daos.getAddressDao().updateDuplicatesCalculatedById(addressId, 
				isDuplicatesCalculated);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#setOrganisationDuplicatesCalculated(long, boolean)
	 */
	@Override
	public void setOrganisationDuplicatesCalculated(long organisationId,
			boolean isDuplicatesCalculated) 
	{
		this.daos.getOrganisationDao().updateDuplicatesCalculatedById(
				organisationId, isDuplicatesCalculated);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#setProviderDuplicatesCalculated(long, boolean)
	 */
	@Override
	public void setProviderDuplicatesCalculated(long providerId,
			boolean isDuplicatesCalculated) 
	{
		this.daos.getProviderDao().updateDuplicatesCalculatedById(providerId, 
				isDuplicatesCalculated);
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#updateDuplicateConfiguration(java.util.List)
	 */
	@Override
	public void updateDuplicateConfiguration(List<DuplicateRecognition> entries) {
		int configurationChanged = duplicateConfigurationChanged(entries);
		for (DuplicateRecognition entry : entries) {
			this.daos.getDuplicateRecognitionDao().update(entry);
		}
		
		switch (configurationChanged) {
			case DuplicateRecognition.ALL_CHANGED:
				this.daos.getAddressDao().updateDuplicatesCalculated(false);
				this.daos.getProviderDao().updateDuplicatesCalculated(false);
				this.daos.getOrganisationDao().updateDuplicatesCalculated(false);
				break;
			case DuplicateRecognition.PROVIDER_CHANGED + DuplicateRecognition.ORGANISATION_CHANGED:
				this.daos.getProviderDao().updateDuplicatesCalculated(false);
				this.daos.getOrganisationDao().updateDuplicatesCalculated(false);
				break;
			case DuplicateRecognition.ORGANISATION_CHANGED:
				this.daos.getOrganisationDao().updateDuplicatesCalculated(false);
				break;
			case DuplicateRecognition.PROVIDER_CHANGED:
				this.daos.getProviderDao().updateDuplicatesCalculated(false);
				break;
			default:
		}
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#updateImportResult(org.openehealth.pors.database.entities.ImportResult)
	 */
	@Override
	public void updateImportResult(ImportResult result) {
		this.daos.getImportResultDao().update(result);		
	}

	/**
	 * @throws IllegalArgumentException
	 * 		If <code>organisation</code> does not contain IP address, session id or editing user
	 * @see IDatabaseConnector#updateOrganisation(Organisation)
	 */
	@Override
	public void updateOrganisation(Organisation organisation) throws DatabaseException 
	{
		if (organisation.getSessionId() == null || organisation.getEditingUser() == null || organisation.getIpAddress() == null)
		{
			throw new IllegalArgumentException(
					"Session id, IP address and editing user need to be set in organisation in order to log all changes."
					);
		}
		
		this.logger.info("Update Organisation - Starting Action");
		
		try
		{
			if (organisation.getId() == null || organisation.getId().longValue() <= 0)
			{
				throw new DataException(
						"Id must be set and greater than 0 in order to identify the organisation to update.");
			}
			
			this.checkUniqueConstraintsOfOrganisation(organisation);
			
			// Check if users are in persistence context
			organisation.setUser(this.synchroniseUser(organisation.getUser()));
			organisation.setEditingUser(this.synchroniseUser(
					organisation.getEditingUser()));
			
			final Organisation dbOrganisation = this.getOrganisationById(organisation.getId());
			
			if (dbOrganisation == null)
			{
				throw new DataException("Organisation having id " + 
						organisation.getId().toString() + 
						" does not exist in database and thus cannot be updated."
						);
			}
			
			// Copy old lists; A null value will result in an empty List
			List<Address> oldAddresses = (dbOrganisation.getAddresses() == null) ? new ArrayList<Address>() : EntityUtil.flatCopyAddressList(dbOrganisation.getAddresses());
			List<LocalId> oldLocalIds = (dbOrganisation.getLocalIds() == null) ? new ArrayList<LocalId>() : EntityUtil.flatCopyLocalIdList(dbOrganisation.getLocalIds());
			// Use this list if provider synchronisation may be needed any time
			// List<Provider> oldProviders = (dbOrganisation.getProviders() == null) ? new ArrayList<Provider>() : EntityUtil.flatCopyProviderList(dbOrganisation.getProviders());
			
			Organisation dbOrganisationCopy = EntityUtil.flatCopyOrganisation(
					dbOrganisation);
			
			Date logTime = new Date();
			boolean isOrganisationUpd = EntityUtil.haveDifferences(
					organisation, dbOrganisation);
			
			if (isOrganisationUpd)
			{
				organisation.setLastUpdateDate(logTime);
				organisation.setDuplicatesCalculated(false);
				this.daos.getDuplicateOrganisationDao().deleteByOrganisationId(
						organisation.getId());
			}
			else
			{
				organisation.setLastUpdateDate(
						dbOrganisation.getLastUpdateDate());
				organisation.setDuplicatesCalculated(
						dbOrganisation.isDuplicatesCalculated());
			}
			
			if(organisation.getAddresses() != null && organisation.getAddresses().size() > 0)
			{
				final AddressSynchronisation addressSync = 
					new AddressSynchronisation(this.daos.getAddressDao(), 
							organisation.getAddresses());
				organisation.setAddresses(
						addressSync.getSynchronisedAddresses());
			}
			
			List<LocalId> localIds = (organisation.getLocalIds() == null) ? 
					new ArrayList<LocalId>() : organisation.getLocalIds();
			organisation.setLocalIds(null);
			
			this.daos.getOrganisationDao().update(organisation);
			
			Organisation organisationRef = this.daos.getOrganisationDao().
					getReferenceById(organisation.getId());

			// Synchronise local IDs
			LocalIdSynchronisation sync = new LocalIdSynchronisation(
					this.daos.getLocalIdDao(), organisationRef, localIds, 
					oldLocalIds);
			organisation.setLocalIds(sync.getSynchronisedLocalIds());
			
			// ~~~~~~~~~~~ Start logging
			DatabaseLogger dbLogger = new DatabaseLogger(this.daos, logTime, organisation.getEditingUser(), organisation.getSessionId(), organisation.getIpAddress());
			
			this.logger.info("Update Organisation - Log organisation");			
			if (isOrganisationUpd)
			{
				dbLogger.logOrganisation(DatabaseLogger.LOG_ACTION_UPDATE, dbOrganisationCopy, organisation);
			}
			
			// TODO: Log providers if needed
			
			this.logger.info("Update Organisation - Logging Local Ids");
			if (organisation.getLocalIds() != null && organisation.getLocalIds().size() > 0)
			{
				dbLogger.logLocalIds(oldLocalIds, organisation.getLocalIds());
			}
			
			this.logger.info("Update Organisation - Log addresses");
			if ((organisation.getAddresses() != null && organisation.getAddresses().size() > 0) || 
					oldAddresses.size() > 0)
			{
				dbLogger.logAddresses(DatabaseLogger.MASTER_DOMAIN_ORGANISATION, 
						organisation.getId(), oldAddresses, organisation.getAddresses());
			}
		}
		catch (OptimisticLockException e)
		{
			this.logger.warn("Update Organisation - Action failed", e);
			throw new DatabaseException(
					"Organisation was edited by another user during the updating progress.", 
					e);
		}
		catch (DataException e)
		{
			this.logger.warn("Update Organisation - Action failed", e);
			throw new DatabaseException(e.getMessage(), e);
		}
		catch (Exception e)
		{
			this.logger.warn("Update Organisation - Action failed", e);
			throw new DatabaseException("An unexpected error occurred.", e);
		}
		
		this.logger.info("Update Organisation - Action Complete");
	}

	/**
	 * @throws IllegalArgumentException If <code>provider</code> does not contain IP address, session id or editing user
	 * @see IDatabaseConnector#updateProvider(Provider)
	 */
	@Override
	public void updateProvider(Provider provider) throws DatabaseException 
	{
		if (provider.getSessionId() == null || 
				provider.getEditingUser() == null || 
				provider.getIpAddress() == null)
		{
			throw new IllegalArgumentException(
					"Session id, IP address and editing user are needed in order to log all changes."
					);
		}
		
		this.logger.info("Update Provider - Starting Action");
		
		try
		{
			
			if (provider.getId() == null || provider.getId().longValue() <= 0)
			{
				throw new DataException(
						"Id must be set and greater than 0 in order to identify a provider to update."
						);
			}
			
			this.checkUniqueConstraintsOfProvider(provider);
			
			// Check if users are in persistence context
			provider.setUser(this.synchroniseUser(provider.getUser()));
			provider.setEditingUser(this.synchroniseUser(
					provider.getEditingUser()));
			
			final Provider dbProvider = this.getProviderById(provider.getId());
			
			if (dbProvider == null)
			{
				throw new DataException("Provider having id " + 
						provider.getId().toString() + 
						" does not exist in database and thus cannot be updated."
						);
			}
		
			// Copy old lists; A null value will result in an empty List
			List<Address> oldAddresses = (dbProvider.getAddresses() == null) ? new ArrayList<Address>() : EntityUtil.flatCopyAddressList(dbProvider.getAddresses());
			List<LocalId> oldLocalIds = (dbProvider.getLocalIds() == null) ? new ArrayList<LocalId>() : EntityUtil.flatCopyLocalIdList(dbProvider.getLocalIds());
			List<Organisation> oldOrganisations = (dbProvider.getOrganisations() == null) ? new ArrayList<Organisation>() : EntityUtil.flatCopyOrganisationList(dbProvider.getOrganisations());
			
			Provider dbProviderCopy = EntityUtil.flatCopyProvider(dbProvider);
			
			final Date logTime = new Date();
			final boolean isProviderUpdate = EntityUtil.haveDifferences(provider, 
					dbProvider);
			
			if (isProviderUpdate)
			{
				provider.setLastUpdateDate(logTime);
				provider.setDuplicatesCalculated(false);
				this.daos.getDuplicateProviderDao().deleteByProviderId(
						provider.getId());
			}
			else
			{
				provider.setLastUpdateDate(dbProvider.getLastUpdateDate());
				provider.setDuplicatesCalculated(
						dbProvider.isDuplicatesCalculated());
			}
		
			if (provider.getAddresses() != null && provider.getAddresses().size() > 0)
			{
				AddressSynchronisation addressSync = 
					new AddressSynchronisation(this.daos.getAddressDao(), 
							provider.getAddresses());
				provider.setAddresses(addressSync.getSynchronisedAddresses());
			}
			
			List<LocalId> localIds = (provider.getLocalIds() == null) ? 
					new ArrayList<LocalId>() : provider.getLocalIds();
			provider.setLocalIds(null);
			
			List<Organisation> organisations = provider.getOrganisations();
			provider.setOrganisations(null);
	
			this.daos.getProviderDao().update(provider);
			
			Provider providerRef = this.daos.getProviderDao().
				getReferenceById(provider.getId());
			
			// Synchronise local IDs
			LocalIdSynchronisation sync = new LocalIdSynchronisation(
					this.daos.getLocalIdDao(), providerRef, localIds, 
					oldLocalIds);
			provider.setLocalIds(sync.getSynchronisedLocalIds());
			if (organisations != null && organisations.size() > 0)
			{
				final List<Organisation> syncOrganisations = this.
					synchroniseOrganisations(provider, organisations, 
							oldOrganisations);
				provider.setOrganisations(syncOrganisations);
			}

			// ~~~~~~~~~~~ Start logging
			DatabaseLogger dbLogger = new DatabaseLogger(this.daos, logTime, provider.getEditingUser(), provider.getSessionId(), provider.getIpAddress());
		
			this.logger.info("Update Provider - Logging Provider");
			if (isProviderUpdate)
			{
				dbLogger.logProvider(DatabaseLogger.LOG_ACTION_UPDATE, 
						dbProviderCopy, provider);
			}
			
			this.logger.info("Update Provider - Logging Relations to Organisations");
			if ((provider.getOrganisations() != null && provider.getOrganisations().size() > 0) || 
					oldLocalIds.size() > 0)
			{
				dbLogger.logOrganisationToProviderRelations(provider.getId(), 
						oldOrganisations, provider.getOrganisations());
			}
			
			this.logger.info("Update Provider - Logging Local Ids");
			if ((provider.getLocalIds() != null && provider.getLocalIds().size() > 0) || 
					oldLocalIds.size() > 0)
			{
				dbLogger.logLocalIds(oldLocalIds, provider.getLocalIds());
			}
		
			this.logger.info("Update Provider - Logging Addresses");
			if ((provider.getAddresses() != null && provider.getAddresses().size() > 0) || 
					oldAddresses.size() > 0)
			{
				dbLogger.logAddresses(DatabaseLogger.MASTER_DOMAIN_PROVIDER, 
						provider.getId(), oldAddresses, provider.getAddresses());
			}
		}
		catch (OptimisticLockException e)
		{
			this.logger.warn("Update Provider - Action failed", e);
			throw new DatabaseException(
					"Provider was edited by another user during your updating progress.", 
					e);
		}
		catch (DataException e)
		{
			this.logger.warn("Update Provider - Action failed", e);
			throw new DatabaseException(e.getMessage(), e);
		}
		catch (Exception e)
		{
			this.logger.warn("Update Provider - Action failed", e);
			throw new DatabaseException("An unexpected error occurred.", e);
		}
		
		this.logger.info("Update Provider - Action Complete");
	}

	/**
	 * @see org.openehealth.pors.database.connector.IDatabaseConnector#updateUser(org.openehealth.pors.database.entities.PorsUser)
	 */
	@Override
	public void updateUser(PorsUser user) 
	{
		this.daos.getPorsUserDao().update(user);
	}

	private void addMasterDomain(final IMasterDomain domain, final String loggingAction, final DatabaseLogger dbLogger) throws DataException
	{		
		// Synchronise Users
		domain.setUser(this.synchroniseUser(domain.getUser()));
		domain.setEditingUser(this.synchroniseUser(domain.getEditingUser()));
		
		// Synchronise Addresses
		if(domain.getAddresses() != null && domain.getAddresses().size() > 0)
		{
			AddressSynchronisation addressSync = new AddressSynchronisation(
					this.daos.getAddressDao(), domain.getAddresses());
			
			domain.setAddresses(addressSync.getSynchronisedAddresses());
		}
		
		List<LocalId> localIds = domain.getLocalIds();
		domain.setLocalIds(null);
		
		Date logTime = dbLogger.getLogTime();
		domain.setLastUpdateDate(logTime);
		domain.setDuplicatesCalculated(false);
		
		if (domain instanceof Provider)
		{
			this.daos.getProviderDao().insert((Provider) domain);
		}
		else if (domain instanceof Organisation)
		{
			this.daos.getOrganisationDao().insert((Organisation) domain);
		}
		
		// Synchronise Local Ids
		if (localIds != null && localIds.size() > 0)
		{
			LocalIdSynchronisation sync = new LocalIdSynchronisation(
					this.daos.getLocalIdDao(), domain, localIds);
			
			domain.setLocalIds(sync.getSynchronisedLocalIds());
		}
		
		// ~~~~~~~~~~~ Start Logging
		LoggingUtil.info(loggingAction, "Logging Local Ids");
		if (domain.getLocalIds() != null && domain.getLocalIds().size() > 0)
		{
			dbLogger.logLocalIds(null, domain.getLocalIds());
		}
		
		LoggingUtil.info(loggingAction, "Logging Addresses");
		if (domain.getAddresses() != null && domain.getAddresses().size() > 0)
		{
			if (domain instanceof Provider)
			{
				dbLogger.logAddresses(DatabaseLogger.MASTER_DOMAIN_PROVIDER, 
						domain.getId(), null, domain.getAddresses());
			}
			
			if (domain instanceof Organisation)
			{
				dbLogger.logAddresses(
						DatabaseLogger.MASTER_DOMAIN_ORGANISATION, 
						domain.getId(), null, domain.getAddresses());
			}
		}
	}

	/**
	 * 
	 * @param organisation
	 * @return
	 * @throws DataException
	 */
	private void checkUniqueConstraintsOfOrganisation(Organisation organisation)
			throws DataException 
	{
		try
		{
			if (organisation.getId() == null || organisation.getId().longValue() <= 0)
			{
				logger.info("id was null, checking unique constraint on insert");
				this.checkUniqueConstraintsOfOrganisationOnInsert(organisation);
			}
			else
			{
				logger.info("id was not null, checking unique constraint on update");
				this.checkUniqueConstraintsOfOrganisationOnUpdate(organisation);
			}
		}
		catch (DoubleOidException e)
		{
			throw new DataException("OID already exists: " + e.getOid());
		}
		catch (DoubleNameException e)
		{
			throw new DataException(e.getMessage());
		} catch (DoubleEstablishmentIdException e) 
		{
			throw new DataException("Establishment ID already exists: " + 
					e.getDoubleEstablishmentId());
		}
	}

	private void checkUniqueConstraintsOfOrganisationOnInsert(final Organisation organisation) throws DoubleOidException, DoubleNameException, DoubleEstablishmentIdException
	{
		IOrganisationDao dao = this.daos.getOrganisationDao();
		
		if (organisation.getEstablishmentId() != null && 
				dao.getByEstablishmentId(organisation.getEstablishmentId()) != null)
		{
			throw new DoubleEstablishmentIdException(
					organisation.getEstablishmentId(), 
					"Establishment ID already exists: " + 
					organisation.getEstablishmentId());
		}
		
		if (organisation.getName() != null && 
				dao.getByName(organisation.getName()) != null)
		{
			throw new DoubleNameException("Name already exists: " + 
					organisation.getName());
		}
		
		if (organisation.getOid() != null && 
				dao.getByOid(organisation.getOid()) != null)
		{
			throw new DoubleOidException(organisation.getOid(), 
					"OID already exists: " + organisation.getOid());
		}
		
		if (organisation.getSecondName() != null && 
				dao.getBySecondName(organisation.getSecondName()) != null)
		{
			throw new DoubleNameException("Second name already exists: " + 
					organisation.getSecondName());
		}
	}

	private void checkUniqueConstraintsOfOrganisationOnUpdate(final Organisation organisation) throws DoubleEstablishmentIdException, DoubleNameException, DoubleOidException
	{
		logger.info("Name: " + organisation.getName());
		logger.info("Name 2: " + organisation.getSecondName());
		IOrganisationDao dao = this.daos.getOrganisationDao();
		
		if (organisation.getEstablishmentId() != null && 
				dao.getByEstablishmentIdExcludeOrganisationId(
						organisation.getEstablishmentId(), organisation.getId()) 
						!= null)
		{
			throw new DoubleEstablishmentIdException(organisation.getEstablishmentId(),
					"Establishment ID already exists: " + 
					organisation.getEstablishmentId());
		}
		
		if (organisation.getName() != null && 
				dao.getByNameExcludeOrganisationId(organisation.getName(), 
						organisation.getId()) != null)
		{
			throw new DoubleNameException(organisation.getName(), 
					"Name already exists: " + organisation.getName());
		}
		
		if (organisation.getOid() != null && 
				dao.getByOidExcludeOrganisationId(organisation.getOid(), 
						organisation.getId()) != null)
		{
			throw new DoubleOidException(organisation.getOid(), 
					"OID already exists: " + organisation.getOid());
		}
		
		if (organisation.getSecondName() != null && 
				dao.getBySecondNameExcludeOrganisationId(
						organisation.getSecondName(), organisation.getId()) 
						!= null)
		{
			throw new DoubleNameException(organisation.getSecondName(), 
					"Second name already exists: " + 
					organisation.getSecondName());
		}
	}

	/**
	 * 
	 * @param provider
	 * @return
	 * @throws DataException
	 */
	private void checkUniqueConstraintsOfProvider(final Provider provider)
			throws DataException 
	{
		try
		{
			if (provider.getId() == null || provider.getId().intValue() <= 0)
			{
				this.checkUniqueConstraintsOfProviderOnInsert(provider);
			}
			else
			{
				this.checkUniqueConstraintsOfProviderOnUpdate(provider);
			}
		}
		catch (DoubleMasterDomainException e)
		{
			Provider p = (Provider) e.getExistingDomain();
			throw new DataException(
					"Unique combination of first name, last name, gender code and birthday for a provider already exists in database: " + 
					p.getFirstName() + " " + p.getLastName() + ", " + 
					p.getGenderCode(), e);
		}
		catch (DoubleLanrException e)
		{
			throw new DataException("LANR already exists in database: " + 
					e.getLanr(), e);
		}
		catch (DoubleOidException e)
		{
			throw new DataException("OID already exists in database: " + 
					e.getOid(), e);
		}
	}

	private void checkUniqueConstraintsOfProviderOnInsert(Provider provider) 
			throws DoubleOidException, DoubleMasterDomainException, DoubleLanrException
	{
		IProviderDao dao = this.daos.getProviderDao();

		if (provider.getBirthday() != null)
		{
			final Provider uniCombProvider = dao.getByUniqueCombination(
					provider.getFirstName(), provider.getLastName(), 
					provider.getGenderCode(), provider.getBirthday());
			
			
			if (uniCombProvider != null)
			{
				throw new DoubleMasterDomainException(
						"Provider already exists in database.", 
						uniCombProvider, provider);
			}
		}
		
		if (provider.getLanr() != null && 
				dao.getByLanr(provider.getLanr()) != null)
		{
			throw new DoubleLanrException(
					"LANR already exists in database.", 
					provider.getLanr());
		}
		
		if (provider.getOid() != null &&
				dao.getByOid(provider.getOid()) != null)
		{
			throw new DoubleOidException("OID already exists in database.", 
					provider.getOid());
		}
	}

	private void checkUniqueConstraintsOfProviderOnUpdate(Provider provider) 
			throws DoubleMasterDomainException, DoubleLanrException, DoubleOidException
	{
		IProviderDao dao = this.daos.getProviderDao();
		
		if (provider.getBirthday() != null)
		{
			final Provider uniCombProvider = dao.
				getByUniqueCombinationExcludeProviderId(
					provider.getFirstName(), provider.getLastName(), 
					provider.getGenderCode(), provider.getBirthday(), 
					provider.getId());
			
			if (uniCombProvider != null)
			{
				throw new DoubleMasterDomainException(
						"Provider already exists in database.", 
						uniCombProvider, provider);
			}
		}
		
		if (provider.getLanr() != null &&
				dao.getByLanrExcludeProviderId(provider.getLanr(), 
				provider.getId()) != null)
		{
			throw new DoubleLanrException(
					"LANR already exists in database.", 
					provider.getLanr());
		}
		
		if (provider.getOid() != null &&
				dao.getByOidExcludeProviderId(provider.getOid(), 
						provider.getId()) != null)
		{
			throw new DoubleOidException("OID already exists in database.", 
					provider.getOid());
		}
	}

	private void checkUniqueConstraintsOfUser(PorsUser user) throws DataException
	{
		PorsUser dbUser = this.daos.getPorsUserDao().getByName(user.getName());
		
		try
		{
			if (dbUser != null && 
					(user.getId() == null || 
							!user.getId().equals(dbUser.getId())))
			{
				PorsUser doubleUser = new PorsUser();
				doubleUser.setName(dbUser.getName());
				
				throw new DoublePorsUserException(doubleUser, 
						"The chosen user name already exists: " + 
						doubleUser.getName());
			}
		}
		catch (DoublePorsUserException e)
		{
			throw new DataException(
					"The chosen user name already exists: " + 
					e.getDoublePorsUser().getName(), e);
		}
	}
	
	private int duplicateConfigurationChanged(List<DuplicateRecognition> entries) {
		boolean allChanged = false;
		boolean providerChanged = false;
		boolean organisationChanged = false;
		
		List<DuplicateRecognition> oldValues = getDuplicateConfiguration();
		for (DuplicateRecognition oldEntry : oldValues) {
			for (DuplicateRecognition newEntry : entries) {
				if (oldEntry.getName().equals(newEntry.getName())
						&& oldEntry.getValue().intValue() != newEntry
								.getValue().intValue()) {
					if (oldEntry.getName().startsWith(DuplicateRecognition.ADDRESS_PREFIX) || 
							oldEntry.getName().equals(DuplicateRecognition.LOWERTHRESHOLD) ||
							oldEntry.getName().equals(DuplicateRecognition.UPPERTHRESHOLD)) {
						allChanged = true;
						break;
					} else if (oldEntry.getName().startsWith(DuplicateRecognition.PROVIDER_PREFIX)) {
						providerChanged = true;
					} else if (oldEntry.getName().startsWith(DuplicateRecognition.ORGANISATION_PREFIX)) {
						organisationChanged = true;
					}
				}
			}
		}
		
		if (allChanged) {
			return DuplicateRecognition.ALL_CHANGED;
		}
		if (providerChanged && organisationChanged) {
			return DuplicateRecognition.PROVIDER_CHANGED + DuplicateRecognition.ORGANISATION_CHANGED;
		}
		if (providerChanged) {
			return DuplicateRecognition.PROVIDER_CHANGED;
		}
		if (organisationChanged) {
			return DuplicateRecognition.ORGANISATION_CHANGED;
		}
		return DuplicateRecognition.NOTHING_CHANGED;
	}

	/**
	 * @see IDatabaseConnector#getAddress(Address)
	 */
	private Address getAddress(Address address)
	{
		Address dbAddress = null;
		if (address.getId() != null && address.getId().longValue() > 0)
		{
			dbAddress = this.daos.getAddressDao().getById(address.getId());
		}
		else if (address.getAdditional() != null && address.getCity() != null && address.getCountry() != null && address.getHouseNumber() != null && address.getStreet() != null && address.getZipCode() != null)
		{
			dbAddress = this.daos.getAddressDao().getByUniqueCombination(
					address.getAdditional(), address.getStreet(), 
					address.getHouseNumber(), address.getZipCode(), 
					address.getCity(), address.getCountry());
		}
		else
		{
			throw new IllegalArgumentException("An id or the combination of the attributes additional, street, houseNumber, zipCode and city has to be set in order to select a single address.");
		}
		
		return dbAddress;
	}

	/**
	 * @throws IllegalArgumentException
	 *  @see IDatabaseConnector#getOrganisation(Organisation)
	 */
	private Organisation getOrganisation(Organisation organisation) 
	{
		final Organisation dbOrganisation;
		
		if(organisation.getId() != null && organisation.getId().longValue() > 0)
		{
			dbOrganisation = this.daos.getOrganisationDao().getById(organisation.getId());
		}
		else if (organisation.getLocalIds() != null && organisation.getLocalIds().size() == 1)
		{
			dbOrganisation = this.daos.getOrganisationDao().getByLocalId(
					organisation.getLocalIds().get(0).getLocalId(), 
					organisation.getLocalIds().get(0).getApplication(), 
					organisation.getLocalIds().get(0).getFacility());
		}
		else if (organisation.getOid() != null)
		{
			dbOrganisation = this.daos.getOrganisationDao().getByOid(
					organisation.getOid());
		}
		else if (organisation.getEstablishmentId() != null)
		{
			dbOrganisation = this.daos.getOrganisationDao().
				getByEstablishmentId(
					organisation.getEstablishmentId());
		}
		else if (organisation.getName() != null)
		{
			dbOrganisation = this.daos.getOrganisationDao().getByName(
					organisation.getName());
		}
		else
		{
			throw new IllegalArgumentException(
					"Id, a single local id, OID, establishment id or name need to be specified in order to select a single organisation."
					);
		}
		
		return dbOrganisation;
	}

	private Provider getProvider(Provider provider) {
		
		Provider dbProvider;
		
		if(provider.getId() != null && provider.getId().longValue() > 0)
		{
			dbProvider = this.daos.getProviderDao().getById(provider.getId());
		}
		else if (provider.getLocalIds() != null && provider.getLocalIds().size() == 1 &&
				provider.getLocalIds().get(0).getApplication() != null && 
				provider.getLocalIds().get(0).getFacility() != null && 
				provider.getLocalIds().get(0).getLocalId() != null)
		{
			dbProvider = this.daos.getProviderDao().getByLocalId(
					provider.getLocalIds().get(0).getLocalId(), 
					provider.getLocalIds().get(0).getApplication(),
					provider.getLocalIds().get(0).getFacility());
		}
		else if(provider.getFirstName() != null && 
				provider.getLastName() != null && 
				provider.getGenderCode() != null)
		{
			dbProvider = this.daos.getProviderDao().getByUniqueCombination(
					provider.getFirstName(),
					provider.getLastName(), 
					provider.getGenderCode(), 
					provider.getBirthday());
		}
		else
		{
			throw new IllegalArgumentException("Need unique identifier \"id\" or a single \"localId\" (with fields \"localId\", \"facility\" and \"application\" set) or a combination of \"firstName\", \"lastName\", \"genderCode\" and \"birthday\" to load a single Provider.");
		}
		
		return dbProvider;
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void initLogging() 
	{
		this.logger = Logger.getLogger(DatabaseConnectorBean.class);
	}

	private List<Organisation> synchroniseOrganisations(Provider provider, final List<Organisation> newOrganisations, final List<Organisation> oldOrganisations) throws DataException
	{
		List<Organisation> syncOrganisations = new ArrayList<Organisation>(newOrganisations.size());
		List<Long> syncIds = new ArrayList<Long>(newOrganisations.size());
		
		for (Organisation o : newOrganisations)
		{
			if (o.getId() == null || o.getId().longValue() <= 0)
			{
				throw new DataException(
						"Tried to reference organisation not having an id.");
			}
			
			if (!syncIds.contains(o.getId()))
			{
				Organisation dbOrganisation = this.getOrganisationById(o.getId());
				
				boolean isRelated = this.daos.getOrganisationDao().
					isOrganisationIdRelatedToProviderId(
						o.getId().longValue(), provider.getId().longValue());
				if (!isRelated)
				{
					List<Provider> dbProviders;
					if (dbOrganisation.getProviders() == null)
					{
						dbProviders = new ArrayList<Provider>();
					}
					else
					{
						dbProviders = dbOrganisation.getProviders();
					}
					
					dbProviders.add(provider);
					
					dbOrganisation.setProviders(dbProviders);
					this.daos.getOrganisationDao().update(dbOrganisation);
				}
				
				syncOrganisations.add(dbOrganisation);
				syncIds.add(o.getId());
			}
		}
		
		if (oldOrganisations != null && oldOrganisations.size() > 0)
		{
			for (Organisation oo : oldOrganisations)
			{
				boolean removed = true;
				for (Organisation no : newOrganisations)
				{
					if (no.getId().equals(oo.getId()))
					{
						removed = false;
						break;
					}
				}
				
				if (removed)
				{
					Organisation dbOrganisation = this.getOrganisationById(oo.getId());
					
					if (dbOrganisation != null)
					{
						if (dbOrganisation.getProviders() != null)
						{
							int index = 0;
							for (Provider p : dbOrganisation.getProviders())
							{
								if (p.getId().equals(provider.getId()))
								{
									List<Provider> dbProviders = dbOrganisation.getProviders();
									dbProviders.remove(index);
									dbOrganisation.setProviders(dbProviders);
									this.daos.getOrganisationDao().
										update(dbOrganisation);
									break;
								}
								
								index++;
							}
						}
					}
				}
			}
		}
		
		return syncOrganisations;
	}

	/**
	 * 
	 * @param user
	 * @return
	 * @throws DataException
	 */
	private PorsUser synchroniseUser(PorsUser user) throws DataException
	{
		PorsUser syncUser = user;
		
		try
		{
			syncUser = this.getUser(user);
		}
		catch (IllegalArgumentException e)
		{
			throw new DataException(
					"Received insufficient data to select a user.", e);
		}
			
		if (syncUser == null)
		{
			throw new DataException(
					"The given user does not exist in database.");
		}
		
		return syncUser;
	}

	@Override
	public void deleteLocalIDs(List<LocalId> localIDs) {
		for (LocalId localId : localIDs) {
			if (localId.getId() != 0) {
				LocalId temp = this.getLocalIdById(localId.getId());
				this.daos.getLocalIdDao().delete(temp);
			}
		}
	}
}
