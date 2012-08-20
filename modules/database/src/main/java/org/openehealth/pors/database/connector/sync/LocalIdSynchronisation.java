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
package org.openehealth.pors.database.connector.sync;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openehealth.pors.database.dao.ILocalIdDao;
import org.openehealth.pors.database.entities.IMasterDomain;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.exception.DataException;
import org.openehealth.pors.database.exception.DoubleLocalIdException;
import org.openehealth.pors.database.exception.InsufficientDataException;

/**
* <p>
* Synchronises local IDs to a new database state.
* </p>
* <p>
* Purges data from a list of new local IDs, links them to a master domain and 
* inserts or updates them into database. If also a list of old local IDs is 
* defined, it removes all data set which are in this list but not in the list 
* of new local IDs.
* </p>
* 
* @author jr
*/
public class LocalIdSynchronisation 
{
	private ILocalIdDao dao;
	private List<LocalId> oldLocalIds;
	private List<LocalId> newLocalIds;
	private List<LocalId> synchronisedLocalIds;
	private IMasterDomain masterDomain;
	
	/**
	 * <p>
	 * Initialises the synchronisation with the DAO which should be used for 
	 * database access, the master domain all local IDs should be linked to as 
	 * well as the list of new local IDs and old local IDs. 
	 * </p>
	 * <p>
	 * This should be usually used for updating processes of the master domain, 
	 * but may also be used for inserts. For this application, 
	 * <code>oldLocalIds</code> can be set to <code>null</code>. Alternatively 
	 * use the constructor 
	 * {@link #LocalIdSynchronisation(ILocalIdDao, IMasterDomain, List)}.
	 * </p>
	 * 
	 * @param dao
	 * 		Data access object for local IDs
	 * @param domain
	 * 		Master domain, the new local IDs should be linked to
	 * @param newLocalIds
	 * 		The new list of local IDs
	 * @param oldLocalIds
	 * 		The previous list of local IDs
	 */
	public LocalIdSynchronisation(ILocalIdDao dao, IMasterDomain domain, 
			List<LocalId> newLocalIds, List<LocalId> oldLocalIds)
	{
		this.dao = dao;
		this.masterDomain = domain;
		this.oldLocalIds = oldLocalIds;
		this.newLocalIds = newLocalIds;
		
		if (!this.isValidAttributes())
		{
			throw new IllegalArgumentException(
					"Null values for parameters not allowed.");
		}
	}
	
	/**
	 * <p>
	 * Initialises the synchronisation with the DAO which should be used for 
	 * database access, the master domain all local IDs should be linked to as 
	 * well as the new list of local IDs. 
	 * </p>
	 * <p>
	 * This can be used for inserting processes of the master domain.
	 * </p>
	 * 
	 * @param dao
	 * 		Data access object for local IDs
	 * @param domain
	 * 		Master domain, the new local IDs should be linked to
	 * @param newLocalIds
	 * 		The new list of local IDs
	 */
	public LocalIdSynchronisation(ILocalIdDao dao, IMasterDomain domain, 
			List<LocalId> newLocalIds)
	{
		this(dao, domain, newLocalIds, null);
	}

	/**
	 * <p>
	 * Purges this lists of old and new local IDs, synchronises them with 
	 * the current database state and returns the resulting list of data sets. 
	 * </p>
	 * 
	 * @return
	 * 		Synchronised local IDs
	 * @throws DataException
	 * 		If given data is invalid
	 */
	public List<LocalId> getSynchronisedLocalIds() throws DataException
	{
		if (this.synchronisedLocalIds == null)
		{
			this.synchroniseData();
		}
		
		return this.synchronisedLocalIds;
	}
	
	/**
	 * <p>
	 * Checks, if all attributes of this object are valid.
	 * </p>
	 * 
	 * @return
	 * 		True, if all attributes are valid, else false.
	 */
	private boolean isValidAttributes()
	{
		if (this.dao == null)
		{
			return false;
		}
		
		if (this.masterDomain == null)
		{
			return false;
		}
		
		if (this.newLocalIds == null)
		{
			return false;
		}
		
		return true;
	}

	private boolean isValidLocalIdForMasterDomain(LocalId localId, IMasterDomain masterDomain)
	{
		if (localId.getProvider() == null && localId.getOrganisation() == null)
		{
			return true;
		}
		
		boolean providerInstance = masterDomain instanceof Provider;
		boolean organisationInstance = masterDomain instanceof Organisation;
		
		if (localId.getProvider() != null && providerInstance)
		{
			if (localId.getProvider().getId() != null && 
					localId.getProvider().getId().equals(masterDomain.getId()))
			{
				return true;
			}
		}
		else if (localId.getOrganisation() != null && organisationInstance)
		{
			if (localId.getOrganisation().getId() != null && 
					localId.getOrganisation().getId().equals(masterDomain.getId()))
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * <p>
	 * Purges local ID data in this list of new local IDs for preparing them for 
	 * insert or update processes. 
	 * </p>
	 * <p>
	 * Merges duplicates in this list of new local IDs where possible and sorts 
	 * the entities into <code>insertLocalIds</code> if they have no ID set or 
	 * into <code>updateLocalIds</code> if they have an ID set.
	 * </p>
	 * 
	 * @param insertLocalIds
	 * 		List where new local IDs are stored which should be inserted
	 * @param updateLocalIds
	 * 		List where old local IDs are stored which should be updated
	 * @throws IllegalArgumentException
	 * 		If any local ID references another master domain but <code>masterDomain</code> or 
	 * 		if there is a double occurrence of any local ID's id or id <code>masterDomain</code>
	 * 		is not of a type supported by local IDs.
	 * @throws DoubleLocalIdException
	 * 		If two local IDs should be set to the same value
	 */
	private void purgeData(List<LocalId> insertLocalIds, 
			List<LocalId> updateLocalIds) throws DoubleLocalIdException
	{
		Set<Long> synchronisedLocalIdIds = new HashSet<Long>(
				this.newLocalIds.size());
		
		for (LocalId localId : this.newLocalIds)
		{
			if (!this.isValidLocalIdForMasterDomain(localId, this.masterDomain))
			{
				throw new IllegalArgumentException("Local ID " + 
						localId.toString() + 
						" does not reference the given master domain object.");
			}
			
			localId.setProvider(null);
			localId.setOrganisation(null);
			
			if (this.masterDomain instanceof Provider)
			{
				localId.setProvider((Provider) this.masterDomain);
			}
			else if (this.masterDomain instanceof Organisation)
			{
				localId.setOrganisation((Organisation) this.masterDomain);
			}
			else
			{
				throw new IllegalArgumentException(
						"Local IDs cannot be linked to given type of master domain: " +
						masterDomain.getClass().toString()
						);
			}
			
			if (localId.getId() != null && localId.getId() > 0)
			{
				if (synchronisedLocalIdIds.contains(localId.getId()))
				{
					throw new IllegalArgumentException(
							"Double occurence of local ID: " 
							+ localId.getId().toString());
				}
				
				if (updateLocalIds.contains(localId))
				{
					LocalId doubleLocalId = new LocalId();
					doubleLocalId.setLocalId(localId.getLocalId());
					doubleLocalId.setApplication(localId.getApplication());
					doubleLocalId.setFacility(localId.getFacility());
					
					throw new DoubleLocalIdException(doubleLocalId, 
							"Tried to update two local IDs to same value: " + 
							doubleLocalId.toString());
				}
				
				if (insertLocalIds.contains(localId))
				{
					insertLocalIds.remove(localId);
				}
				
				updateLocalIds.add(localId);
			}
			else if (!updateLocalIds.contains(localId) && 
						!insertLocalIds.contains(localId))
			{	
				insertLocalIds.add(localId);
			}
		}
	}
	
	/**
	 * <p>
	 * Purges the data of this list of new local IDs and starts the 
	 * corresponding inserting and updating processes.
	 * </p>
	 * <p>
	 * If this list of old local IDs is set, there will also all unlinked 
	 * local IDs be removed from database.
	 * </p>
	 * 
	 * @throws DataException
	 * 		If data could not be synchronised
	 */
	private void synchroniseData() throws DataException
	{
		List<LocalId> updateLocalIds = new ArrayList<LocalId>(
				this.newLocalIds.size());
		List<LocalId> insertLocalIds = new ArrayList<LocalId>(
				this.newLocalIds.size());
		
		try
		{
			this.purgeData(insertLocalIds, updateLocalIds);
		}
		catch (DoubleLocalIdException e)
		{
			LocalId doubleLid = e.getDoubleLocalId();
			throw new DataException("Two or more local IDs have the same value: Local ID \"" + 
					doubleLid.getLocalId() + "\" for application \"" + 
					doubleLid.getApplication() + "\" in facility \"" + doubleLid.getFacility() + "\".", 
					e);
		}
		catch (IllegalArgumentException e)
		{
			throw new DataException("Invalid data input for new local IDs.", e);
		}
		
		this.synchronisedLocalIds = new ArrayList<LocalId>(
				updateLocalIds.size() + insertLocalIds.size());
		
		try
		{
			this.synchronisedLocalIds.addAll(
					this.synchroniseUpdateData(updateLocalIds));
			this.synchronisedLocalIds.addAll(
					this.synchroniseInsertData(insertLocalIds));
		}
		catch(DoubleLocalIdException e)
		{
			LocalId doubleLid = e.getDoubleLocalId();
			throw new DataException("Local ID \"" + doubleLid.getLocalId() + 
					"\" of application \"" + doubleLid.getApplication() + 
					"\" in facility \"" + doubleLid.getFacility() + "\" already " +
							"exists for another provider or organisation.", e); 
		}
		catch (InsufficientDataException e)
		{
			throw new DataException("Local ID could not be processed, because " +
					"either local ID, application or facility has not been set.", 
					e);
		}
		catch (IllegalArgumentException e)
		{
			throw new DataException("Local ID has invalid data.", e);
		}
		
		try
		{
			if (this.oldLocalIds != null)
			{
				this.synchroniseDeleteData(this.synchronisedLocalIds);
			}
		}
		catch (IllegalArgumentException e)
		{
			throw new DataException(
					"Invalid data input for old local IDs.", e);
		}
	}
	
	/**
	 * <p>
	 * Removes all local IDs which have been referenced to a previous data set 
	 * defined in this list of old local IDs and are not in use any more, which 
	 * means they do not occur in this list of new local IDs. 
	 * </p>
	 * 
	 * @param synchronisedData
	 * 		The already synchronised (inserted / updated) data sets
	 * @throws IllegalArgumentException
	 * 		If it is not possible to find any old local ID in database
	 */
	private void synchroniseDeleteData(List<LocalId> synchronisedData)
	{
		for (LocalId oldLid : this.oldLocalIds)
		{
			if (oldLid.getId() != null && oldLid.getId() > 0)
			{
				boolean found = false;
				
				for (LocalId newLid : synchronisedData)
				{
					if (oldLid.getId().equals(newLid.getId()))
					{
						found = true;
						break;
					}
				}
				
				if (!found)
				{
					LocalId lidRef = this.dao.getReferenceById(
							oldLid.getId());
					
					if (lidRef == null)
					{
						throw new IllegalArgumentException(
								"Old local ID does not exist: " + 
								oldLid.toString());
					}
					
					this.dao.delete(lidRef);
				}
			}
			else
			{
				throw new IllegalArgumentException(
						"Old local ID does not have a valid ID: " +
						oldLid.toString());
			}
		}
	}

	/**
	 * <p>
	 * Checks for each new data set, if it would violate any unique constraints 
	 * and inserts it if not.
	 * </p>
	 * 
	 * @param insertLocalIds
	 * 		Local IDs to newly insert in database
	 * @return
	 * 		List of synchronised local ID inserts
	 * @throws InsufficientDataException
	 * 		If in any of the local IDs are not all required fields set
	 * @throws DoubleLocalIdException
	 * 		If any local ID would violate a unique constraint
	 */
	private List<LocalId> synchroniseInsertData(List<LocalId> insertLocalIds) throws InsufficientDataException, DoubleLocalIdException
	{
		List<LocalId> synchronisedLocalIds = 
			new ArrayList<LocalId>(insertLocalIds.size());
		
		for (LocalId localId : insertLocalIds)
		{
			LocalId dbLid;
			
			try
			{
				dbLid = this.dao.getByUniqueCombination(
						localId.getLocalId(), 
						localId.getFacility(), 
						localId.getApplication());
			}
			catch (IllegalArgumentException e)
			{
				throw new InsufficientDataException(localId,
						"Could not load local ID due to insufficient data: " + 
						localId.toString(), e);
			}
			
			if (dbLid == null)
			{		
				this.dao.insert(localId);
				
				localId.setNewlyInserted(true);
				
				synchronisedLocalIds.add(localId);
			}
			else // if (dbLid != null)
			{					
				if (!this.isValidLocalIdForMasterDomain(dbLid, 
						masterDomain))
				{
					LocalId doubleLocalId = new LocalId();
					doubleLocalId.setLocalId(localId.getLocalId());
					doubleLocalId.setApplication(localId.getApplication());
					doubleLocalId.setFacility(localId.getFacility());
					
					throw new DoubleLocalIdException(doubleLocalId, 
							"Tried to insert local ID having a value already existing in database for another provider or organisation.");
				}
				
				dbLid.setNewlyInserted(false);
				
				synchronisedLocalIds.add(dbLid);
			}
		}
		
		return synchronisedLocalIds;
	}

	/**
	 * <p>
	 * Checks for each new data set, if it would violate any unique constraints 
	 * and updates it if not.
	 * </p>
	 * 
	 * @param updateLocalIds
	 * 		List of all data sets to update
	 * @return
	 * 		List of all updated data sets
	 * @throws InsufficientDataException
	 * 		If any of the local IDs does not have all required fields set
	 * @throws DoubleLocalIdException
	 * 		If any local ID violates any unique constraint
	 * @throws IllegalArgumentException
	 * 		If any local ID could not be updated due to missing ID
	 */
	private List<LocalId> synchroniseUpdateData(List<LocalId> updateLocalIds) throws InsufficientDataException, DoubleLocalIdException
	{
		List<LocalId> synchronisedLocalIds = 
			new ArrayList<LocalId>(updateLocalIds.size());
		
		for (LocalId localId : updateLocalIds)
		{
			LocalId dbLocalId;
			
			try
			{
				dbLocalId = this.dao.getByUniqueCombination(
								localId.getLocalId(), localId.getFacility(), 
								localId.getApplication());
			}
			catch (IllegalArgumentException e)
			{
				throw new InsufficientDataException(localId,
						"Could not load local ID due to insufficient data: " + 
						localId.toString(), e);
			}
			
			if (dbLocalId != null && 
					!dbLocalId.getId().equals(localId.getId()))
			{
				LocalId doubleLocalId = new LocalId();
				doubleLocalId.setLocalId(localId.getLocalId());
				doubleLocalId.setApplication(localId.getApplication());
				doubleLocalId.setFacility(localId.getFacility());
				
				throw new DoubleLocalIdException(doubleLocalId, 
						"Tried to update local ID to a value already existing in database.");
			}
			
			this.dao.update(localId);
			
			localId.setNewlyInserted(false);
			
			synchronisedLocalIds.add(localId);
		}
		
		return synchronisedLocalIds;
	}
}
