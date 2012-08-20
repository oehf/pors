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

import java.util.Date;
import java.util.List;

import org.openehealth.pors.database.dao.IDaoFactory;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.AddressLog;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.LocalIdLog;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.OrganisationHasAddressLog;
import org.openehealth.pors.database.entities.OrganisationHasProviderLog;
import org.openehealth.pors.database.entities.OrganisationLog;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.entities.ProviderHasAddressLog;
import org.openehealth.pors.database.entities.ProviderLog;
import org.openehealth.pors.database.util.EntityUtil;


/**
 * <p>
 * Provides logging functionalities for all database domains which may be 
 * logged.
 * </p>
 * 
 * @author jr
 *
 */
public class DatabaseLogger 
{
	/**
	 * Value of the logging action defining an update.
	 */
	public static final String LOG_ACTION_UPDATE = "Update";
	
	/**
	 * Value of the logging action defining an insert.
	 */
	public static final String LOG_ACTION_INSERT = "Insert";
	
	/**
	 * Value of the logging action defining a deletion.
	 */
	public static final String LOG_ACTION_DELETE = "Delete";
	
	/**
	 * Id of a provider as master domain.
	 */
	public static final int MASTER_DOMAIN_PROVIDER = 1;
	
	/**
	 * Id of an organisation as master domain.
	 */
	public static final int MASTER_DOMAIN_ORGANISATION = 2;
	
	private IDaoFactory daos;
	private Date logTime;
	private PorsUser editingUser;
	private String sessionId;
	private String ipAddress;
	
	protected DatabaseLogger(final IDaoFactory daoFactory, final Date logTime, final PorsUser editingUser, String sessionId, String ipAddress)
	{
		this.daos = daoFactory;
		this.logTime = logTime;
		this.editingUser = editingUser;
		this.sessionId = sessionId;
		this.ipAddress = ipAddress;
	}
	
	protected void logProvider(String action, Provider oldProvider, Provider newProvider)
	{
		this.verifyLogAction(action);
			
		if (oldProvider == null && newProvider == null)
		{
			throw new IllegalArgumentException(
				"At least one of both providers must be set.");
		}
		
		ProviderLog log = new ProviderLog();
		log.setIPAddress(this.ipAddress);
		log.setLogTime(this.logTime);
		log.setSessionId(this.sessionId);
		log.setTriggerType(action);
		log.setUser(this.editingUser);
		
		if (newProvider != null)
		{
			log.setRegionalProviderId(newProvider.getId());
			log.setNewBirthday(newProvider.getBirthday());
			log.setNewDeactivationDate(newProvider.getDeactivationDate());
			log.setNewDeactivationReasonCode(newProvider.getDeactivationReasonCode());
			log.setNewEmail(newProvider.getEmail());
			log.setNewFax(newProvider.getFax());
			log.setNewFirstName(newProvider.getFirstName());
			log.setNewGenderCode(newProvider.getGenderCode());
			log.setNewLanr(newProvider.getLanr());
			log.setNewLastName(newProvider.getLastName());
			log.setNewLastUpdateDate(newProvider.getLastUpdateDate());
			log.setNewMiddleName(newProvider.getMiddleName());
			log.setNewNamePrefix(newProvider.getNamePrefix());
			log.setNewNameSuffix(newProvider.getNameSuffix());
			log.setNewOid(newProvider.getOid());
			log.setNewReactivationDate(newProvider.getReactivationDate());
			log.setNewReactivationReasonCode(newProvider.getReactivationReasonCode());
			log.setNewSpecialisation(newProvider.getSpecialisation());
			log.setNewTelephone(newProvider.getTelephone());
		}
		
		if (oldProvider != null)
		{
			log.setRegionalProviderId(oldProvider.getId());
			log.setOldBirthday(oldProvider.getBirthday());
			log.setOldDeactivationDate(oldProvider.getDeactivationDate());
			log.setOldDeactivationReasonCode(oldProvider.getDeactivationReasonCode());
			log.setOldEmail(oldProvider.getEmail());
			log.setOldFax(oldProvider.getFax());
			log.setOldFirstName(oldProvider.getFirstName());
			log.setOldGenderCode(oldProvider.getGenderCode());
			log.setOldLanr(oldProvider.getLanr());
			log.setOldLastName(oldProvider.getLastName());
			log.setOldLastUpdateDate(oldProvider.getLastUpdateDate());
			log.setOldMiddleName(oldProvider.getMiddleName());
			log.setOldNamePrefix(oldProvider.getNamePrefix());
			log.setOldNameSuffix(oldProvider.getNameSuffix());
			log.setOldOid(oldProvider.getOid());
			log.setOldReactivationDate(oldProvider.getReactivationDate());
			log.setOldReactivationReasonCode(oldProvider.getReactivationReasonCode());
			log.setOldSpecialisation(oldProvider.getSpecialisation());
			log.setOldTelephone(oldProvider.getTelephone());
		}
		
		this.addProviderLog(log);
	}
	
	/**
	 * <p>
	 * Logs two versions of an organisation with matching action.
	 * </p>
	 * <p>
	 * Depending on whether <code>oldOrganisation</code> or <code>newOrganisation</code> 
	 * or both are set (the other parameter may be <code>null</code>), the logging 
	 * action defined in <code>action</code> can be set to {@link #LOG_ACTION_DELETE}, 
	 * {@link #LOG_ACTION_INSERT} or {@link #LOG_ACTION_UPDATE}.
	 * </p>
	 * 
	 * @param oldOrganisation 
	 * 		Version of an organisation before changes where done
	 * @param newOrganisation 
	 * 		Version of an organisation after changes where done
	 * @throws IllegalArgumentException 
	 * 		If both organisation versions where set to <code>null</code> or value of 
	 * 		<code>action</code> is unknown
	 */
	protected void logOrganisation(final String action, final Organisation oldOrganisation, final Organisation newOrganisation)
	{
		this.verifyLogAction(action);
			
		if (oldOrganisation == null && newOrganisation == null)
		{
			throw new IllegalArgumentException(
					"At least one of both organisations must be set.");
		}
		
		OrganisationLog log = new OrganisationLog();
		log.setIPAddress(this.ipAddress);
		log.setLogTime(this.logTime);
		log.setSessionId(this.sessionId);
		log.setTriggerType(action);
		log.setUser(this.editingUser);
		
		if (newOrganisation != null)
		{
			log.setRegionalOrganisationId(newOrganisation.getId());
			log.setNewDeactivationDate(newOrganisation.getDeactivationDate());
			log.setNewDeactivationReasonCode(newOrganisation.getDeactivationReasonCode());
			log.setNewDescription(newOrganisation.getDescription());
			log.setNewEMail(newOrganisation.getEmail());
			log.setNewEstablishmentId(newOrganisation.getEstablishmentId());
			log.setNewFax(newOrganisation.getFax());
			log.setNewLastUpdateDate(newOrganisation.getLastUpdateDate());
			log.setNewName(newOrganisation.getName());
			log.setNewOid(newOrganisation.getOid());
			log.setNewPorsUserId(newOrganisation.getUser().getId());
			log.setNewReactivationDate(newOrganisation.getReactivationDate());
			log.setNewReactivationReasonCode(newOrganisation.getReactivationReasonCode());
			log.setNewSecondName(newOrganisation.getSecondName());
			log.setNewTelephone(newOrganisation.getTelephone());
		}
		
		if (oldOrganisation != null)
		{
			log.setRegionalOrganisationId(oldOrganisation.getId());
			log.setOldDeactivationDate(oldOrganisation.getDeactivationDate());
			log.setOldDeactivationReasonCode(oldOrganisation.getDeactivationReasonCode());
			log.setOldDescription(oldOrganisation.getDescription());
			log.setOldEMail(oldOrganisation.getEmail());
			log.setOldEstablishmentId(oldOrganisation.getEstablishmentId());
			log.setOldFax(oldOrganisation.getFax());
			log.setOldLastUpdateDate(oldOrganisation.getLastUpdateDate());
			log.setOldName(oldOrganisation.getName());
			log.setOldOid(oldOrganisation.getOid());
			log.setOldPorsUserId(oldOrganisation.getUser().getId());
			log.setOldReactivationDate(oldOrganisation.getReactivationDate());
			log.setOldReactivationReasonCode(oldOrganisation.getReactivationReasonCode());
			log.setOldSecondName(oldOrganisation.getSecondName());
			log.setOldTelephone(oldOrganisation.getTelephone());
		}
		
		this.addOrganisationLog(log);
	}
	
	protected void logOrganisationToProviderRelations(Long providerId, List<Organisation> oldOrganisations, final List<Organisation> newOrganisations)
	{
		if (newOrganisations != null && newOrganisations.size() > 0)
		{
			for (Organisation newOrga : newOrganisations)
			{
				boolean newRelation = true;
				
				if (oldOrganisations != null && oldOrganisations.size() > 0)
				{
					for (Organisation oldOrga : oldOrganisations)
					{
						if (oldOrga.getId().equals(newOrga.getId()))
						{
							newRelation = false;
							break;
						}
					}
				}
				
				if (newRelation)
				{
					this.logOrganisationHasProvider(LOG_ACTION_INSERT, null, 
							newOrga.getId(), null, providerId);
				}
			}
		}
		
		if (oldOrganisations != null && oldOrganisations.size() > 0)
		{
			for (Organisation oldOrga : oldOrganisations)
			{
				boolean deletedRelation = true;
				
				if (newOrganisations != null && newOrganisations.size() > 0)
				{
					for (Organisation newOrga : newOrganisations)
					{
						if (newOrga.getId().equals(oldOrga.getId()))
						{
							deletedRelation = false;
							break;
						}
					}
				}
				
				if (deletedRelation)
				{
					this.logOrganisationHasProvider(LOG_ACTION_DELETE, oldOrga.getId(), null, providerId, null);
				}
			}
		}
	}
	
	protected void logAddresses(final int masterDomain, final Long masterDomainId, final List<Address> oldAddresses, final List<Address> newAddresses)
	{
		/* Some hints about implementation : 
		 * In old addresses, but not in new addresses -> relation was removed
		 * In old and new addresses with same id and content not equal -> address was updated
		 * In new addresses with flag newlyInserted = true -> address inserted + address relation inserted (id has to be selected from database)
		 * In new addresses with id but not in old addresses -> address relation inserted 
		 */
		
		this.verifyMasterDomainId(masterDomain);
		
		if (oldAddresses != null && oldAddresses.size() > 0)
		{
			// Check for removed relations and updated addresses
			for (Address oldAddress : oldAddresses)
			{
				boolean found = false;
				if (newAddresses != null && newAddresses.size() > 0)
				{
					for (Address newAddress : newAddresses)
					{
						if (oldAddress.getId().equals(newAddress.getId()))
						{
							if (EntityUtil.haveDifferences(newAddress, oldAddress))
							{
								this.logAddress(LOG_ACTION_UPDATE, oldAddress, 
										newAddress);
							}
							
							found = true;
							break;
						}
					}
				}
				
				// Log deletion of address relations
				if (!found && masterDomain == MASTER_DOMAIN_PROVIDER)
				{
					this.logProviderHasAddress(LOG_ACTION_DELETE, masterDomainId, null, 
							oldAddress.getId(), null);
				}
				else if (!found && masterDomain == MASTER_DOMAIN_ORGANISATION)
				{
					this.logOrganisationHasAddress(LOG_ACTION_DELETE, masterDomainId, null, 
							oldAddress.getId(), null);
				}
			}
		}
		
		// Check for added relations and inserted addresses
		if (newAddresses != null && newAddresses.size() > 0)
		{
			for (Address newAddress : newAddresses)
			{
				boolean insertAddressRelation = false;
				
				if (newAddress.isNewlyInserted())
				{
					this.logAddress(LOG_ACTION_INSERT, null, newAddress);
					insertAddressRelation = true;
				}
				else
				{
					insertAddressRelation = true;
					if (oldAddresses != null && oldAddresses.size() > 0)
					{
						// If also in old address : Do NOT log insert of address relation
						for (Address oldAddress : oldAddresses)
						{
							if (newAddress.getId().equals(oldAddress.getId()))
							{
								// TODO: Could be checked for updates in this insert, too!
								insertAddressRelation = false;
								break;
							}
						}
					}
				}
				
				if (insertAddressRelation && masterDomain == MASTER_DOMAIN_PROVIDER)
				{
					this.logProviderHasAddress(LOG_ACTION_INSERT, null, masterDomainId, 
							null, newAddress.getId());
				}
				else if (insertAddressRelation && masterDomain == MASTER_DOMAIN_ORGANISATION)
				{
					this.logOrganisationHasAddress(LOG_ACTION_INSERT, null, masterDomainId, 
							null, newAddress.getId());
				}
			}
		}
	}
	
	protected void logAddressDelete(Address a)
	{
		this.logAddress(LOG_ACTION_DELETE, a, null);
		
		List<Provider> providers = a.getProviders();
		List<Organisation> organisations = a.getOrganisations();
		
		if (providers != null)
		{
			for (Provider p : providers)
			{
				this.logProviderHasAddress(LOG_ACTION_DELETE, p.getId(), 
						null, a.getId(), null);
			}
		}
		
		if (organisations != null)
		{
			for (Organisation o : organisations)
			{
				this.logOrganisationHasAddress(LOG_ACTION_DELETE, o.getId(), 
						null, a.getId(), null);
			}
		}
	}
	
	protected void logLocalIds(List<LocalId> oldLocalIds, List<LocalId> newLocalIds)
	{	
		// Check for removed relations and updated local ids
		if (oldLocalIds != null && oldLocalIds.size() > 0)
		{
			for (LocalId oldLid : oldLocalIds)
			{
				boolean found = false;
				for (LocalId newLid : newLocalIds)
				{
					if (oldLid.getId().equals(newLid.getId()))
					{
						if (EntityUtil.haveDifferences(oldLid, newLid))
						{
							this.logLocalId(LOG_ACTION_UPDATE, oldLid, newLid);
						}
						
						found = true;
						break;
					}
				}
				
				if (!found)
				{
					this.logLocalId(LOG_ACTION_DELETE, oldLid, null);
				}
			}
		}
		
		// Check for inserted local ids
		if (newLocalIds != null && newLocalIds.size() > 0)
		{
			for (LocalId newLid : newLocalIds)
			{
				if (newLid.isNewlyInserted())
				{
					this.logLocalId(LOG_ACTION_INSERT, null, newLid);
				}
			}
		}
	}
	
	/**
	 * <p>
	 * Returns the log time which will be set for each logging entry generated 
	 * using this database logger.
	 * </p>
	 * 
	 * @return Log time for each generated log entry
	 */
	public Date getLogTime()
	{
		return this.logTime;
	}
	
	private void logAddress(String action, Address oldAddress, Address newAddress)
	{
		this.verifyLogAction(action);
		
		if (oldAddress == null && newAddress == null)
		{
			throw new IllegalArgumentException(
					"At least one of both addresses must be set.");
		}
		
		AddressLog log = new AddressLog();
		log.setIPAddress(this.ipAddress);
		log.setLogTime(this.logTime);
		log.setSessionId(this.sessionId);
		log.setTriggerType(action);
		log.setUser(this.editingUser);
		
		if (newAddress != null)
		{
			log.setAddressId(newAddress.getId());
			log.setNewAdditional(newAddress.getAdditional());
			log.setNewCity(newAddress.getCity());
			log.setNewCountry(newAddress.getCountry());
			log.setNewHouseNumber(newAddress.getHouseNumber());
			log.setNewState(newAddress.getState());
			log.setNewStreet(newAddress.getStreet());
			log.setNewZipCode(newAddress.getZipCode());
		}
		
		if (oldAddress != null)
		{
			log.setAddressId(newAddress.getId());
			log.setOldAdditional(oldAddress.getAdditional());
			log.setOldCity(oldAddress.getCity());
			log.setOldCountry(oldAddress.getCountry());
			log.setOldHouseNumber(oldAddress.getHouseNumber());
			log.setOldState(oldAddress.getState());
			log.setOldStreet(oldAddress.getStreet());
			log.setOldZipCode(oldAddress.getZipCode());
		}
		
		this.addAddressLog(log);
	}
	
	private void logLocalId(String action, LocalId oldLocalId, LocalId newLocalId)
	{
		this.verifyLogAction(action);
		
		if (oldLocalId == null && newLocalId == null)
		{
			throw new IllegalArgumentException(
					"At least one of both local ids has to be set.");
		}
		
		LocalIdLog log = new LocalIdLog();
		log.setAction(action);
		log.setIPAddress(this.ipAddress);
		log.setLogTime(this.logTime);
		log.setSessionId(this.sessionId);
		log.setUser(this.editingUser);
		
		if (newLocalId != null)
		{
			log.setLocalIdId(newLocalId.getId());
			log.setNewLocalId(newLocalId.getLocalId());
			log.setNewFacility(newLocalId.getFacility());
			log.setNewApplication(newLocalId.getApplication());
			
			if (newLocalId.getOrganisation() != null)
			{
				log.setNewRegionalOrganisationId(newLocalId.getOrganisation().
						getId());
			}
			
			if (newLocalId.getProvider() != null)
			{
				log.setNewRegionalProviderId(newLocalId.getProvider().getId());
			}
		}
		
		if (oldLocalId != null)
		{
			log.setLocalIdId(oldLocalId.getId());
			log.setOldLocalId(oldLocalId.getLocalId());
			log.setOldFacility(oldLocalId.getFacility());
			log.setOldApplication(oldLocalId.getApplication());
			
			if (oldLocalId.getOrganisation() != null)
			{
				log.setOldRegionalOrganisationId(oldLocalId.getOrganisation().
						getId());
			}
			
			if (oldLocalId.getProvider() != null)
			{
				log.setOldRegionalProviderId(oldLocalId.getProvider().getId());
			}
		}
		
		this.addLocalIdLog(log);
	}
	
	private void logProviderHasAddress(String action, Long oldProviderId, Long newProviderId, Long oldAddressId, Long newAddressId)
	{
		this.verifyLogAction(action);
		
		boolean oldValuesSet = (oldProviderId != null && 
				oldProviderId.longValue() > 0 && 
				oldAddressId != null && 
				oldAddressId.longValue() > 0);
		
		boolean newValuesSet = (newProviderId != null && 
				newProviderId.longValue() > 0 && 
				newAddressId != null && 
				newAddressId.longValue() > 0);
		
		if (!oldValuesSet && !newValuesSet)
		{
			throw new IllegalArgumentException(
					"At least old organisation id and old address id or new organisation id and new address id have to be set and have a greater value than 0."
					);
		}
		
		ProviderHasAddressLog log = new ProviderHasAddressLog();
		log.setIPAddress(this.ipAddress);
		log.setLogTime(this.logTime);
		log.setSessionId(this.sessionId);
		log.setTriggerType(action);
		log.setUser(this.editingUser);
		
		if (oldValuesSet)
		{
			log.setOldAddressId(oldAddressId);
			log.setOldRegionalProviderId(oldProviderId);
		}
		
		if(newValuesSet)
		{
			log.setNewAddressId(newAddressId);
			log.setNewRegionalProviderId(newProviderId);
		}
			
		this.addProviderHasAddressLog(log);
	}
	
	private void logOrganisationHasProvider(String action, Long oldOrganisationId, Long newOrganisationId, Long oldProviderId, Long newProviderId)
	{
		this.verifyLogAction(action);
		
		boolean oldValuesSet = (oldOrganisationId != null && 
				oldOrganisationId.longValue() > 0 && 
				oldProviderId != null && 
				oldProviderId.longValue() > 0);
		
		boolean newValuesSet = (newOrganisationId != null && 
				newOrganisationId.longValue() > 0 && 
				newProviderId != null && 
				newProviderId.longValue() > 0);
		
		if (!oldValuesSet && !newValuesSet)
		{
			throw new IllegalArgumentException(
					"At least old organisation id and old address id or new organisation id and new address id have to be set and have a greater value than 0."
					);
		}
		
		OrganisationHasProviderLog log = new OrganisationHasProviderLog();
		log.setIPAddress(this.ipAddress);
		log.setLogTime(this.logTime);
		log.setSessionId(this.sessionId);
		log.setTriggerType(action);
		log.setUser(this.editingUser);
		
		if (oldValuesSet)
		{
			log.setOldRegionalOrganisationId(oldOrganisationId);
			log.setOldRegionalProviderId(oldProviderId);
		}
		
		if (newValuesSet)
		{
			log.setNewRegionalOrganisationId(newOrganisationId);
			log.setNewRegionalProviderId(newProviderId);
		}
		
		this.addOrganisationHasProviderLog(log);
	}
	
	private void logOrganisationHasAddress(final String action, final Long oldOrganisationId, final Long newOrganisationId, Long oldAddressId, Long newAddressId)
	{
		this.verifyLogAction(action);
		
		boolean oldValuesSet = (oldOrganisationId != null && 
				oldOrganisationId.longValue() > 0 && 
				oldAddressId != null && 
				oldAddressId.longValue() > 0);
		
		boolean newValuesSet = (newOrganisationId != null && 
				newOrganisationId.longValue() > 0 && 
				newAddressId != null && 
				newAddressId.longValue() > 0);
		
		if (!oldValuesSet && !newValuesSet)
		{
			throw new IllegalArgumentException(
					"At least old organisation id and old address id or new organisation id and new address id have to be set and have a greater value than 0.");
		}
		
		OrganisationHasAddressLog log = new OrganisationHasAddressLog();
		log.setIPAddress(this.ipAddress);
		log.setLogTime(this.logTime);
		log.setSessionId(this.sessionId);
		log.setTriggerType(action);
		log.setUser(this.editingUser);
		
		if (oldValuesSet)
		{
			log.setOldAddressId(oldAddressId);
			log.setOldRegionalOrganisationId(oldOrganisationId);
		}
		
		if(newValuesSet)
		{
			log.setNewAddressId(newAddressId);
			log.setNewRegionalOrganisationId(newOrganisationId);
		}
		
		this.addOrganisationHasAddressLog(log);
	}
	
	private void addProviderLog(ProviderLog log)
	{
		this.daos.getProviderLogDao().insert(log);
	}
	
	private void addOrganisationLog(OrganisationLog log)
	{
		this.daos.getOrganisationLogDao().insert(log);
	}
	
	private void addAddressLog(AddressLog log)
	{
		this.daos.getAddressLogDao().insert(log);
	}
	
	private void addOrganisationHasAddressLog(OrganisationHasAddressLog log)
	{
		this.daos.getOrganisationHasAddressLogDao().insert(log);
	}
	
	private void addOrganisationHasProviderLog(OrganisationHasProviderLog log)
	{
		this.daos.getOrganisationHasProviderLogDao().insert(log);
	}
	
	private void addProviderHasAddressLog(ProviderHasAddressLog log)
	{
		this.daos.getProviderHasAddressLogDao().insert(log);
	}
	
	private void addLocalIdLog(LocalIdLog log)
	{
		this.daos.getLocalIdLogDao().insert(log);
	}
	
	private void verifyLogAction(String logAction)
	{
		if (logAction == null)
		{
			throw new IllegalArgumentException(
					"Logging action must not be null.");
		}
		
		if (!logAction.equals(LOG_ACTION_DELETE) && 
				!logAction.equals(LOG_ACTION_INSERT) && 
				!logAction.equals(LOG_ACTION_UPDATE))
		{
			throw new IllegalArgumentException(
					"Logging action \"" + logAction + "\" is unknown.");
		}
	}
	
	private void verifyMasterDomainId(int masterDomain)
	{
		if (masterDomain != MASTER_DOMAIN_ORGANISATION && 
				masterDomain != MASTER_DOMAIN_PROVIDER)
		{
			throw new IllegalArgumentException(
					"Received master domain is unknown.");
		}
	}
}
