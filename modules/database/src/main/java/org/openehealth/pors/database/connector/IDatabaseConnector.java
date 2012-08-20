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

import javax.ejb.Local;

import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.DuplicateAddress;
import org.openehealth.pors.database.entities.DuplicateEntry;
import org.openehealth.pors.database.entities.DuplicateOrganisation;
import org.openehealth.pors.database.entities.DuplicateRecognition;
import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.ImportResult;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.entities.UserHistory;
import org.openehealth.pors.database.entities.UserRole;
import org.openehealth.pors.database.exception.DatabaseException;

/**
 * <p>
 * Interface providing several methods for database interaction.
 * </p>
 * 
 * @author jr
 *
 */
@Local
public interface IDatabaseConnector 
{
	/**
	 * <p>
	 * Adds <code>result</code>.
	 * </p>
	 * 
	 * @param result
	 * 		Import result to add
	 */
	void addImportResult(ImportResult result);
	
	/**
	 * <p>
	 * Adds <code>organisation</code>.
	 * </p>
	 * 
	 * @param organisation
	 * 		Organisation to add
	 * @throws DatabaseException 
	 * 		If organisation could not be added (unique constraints, etc.)
	 */
	void addOrganisation(Organisation organisation) throws DatabaseException;
	
	/**
	 * <p>
	 * Adds <code>user</code>.
	 * </p>
	 * 
	 * @param user
	 * 		The PORS user to add
	 * @throws DatabaseException
	 * 		If user could not be added by any reason
	 */
	void addPorsUser(PorsUser user) throws DatabaseException;
	
	/**
	 * <p>
	 * Adds <code>provider</code>.
	 * </p>
	 * 
	 * @param provider 
	 * 		Provider to add
	 * @throws DatabaseException 
	 * 		If provider could not be added by any reason
	 */
	void addProvider(Provider provider) throws DatabaseException;
	
	/**
	 * <p>
	 * Irrecoverable deletes the address having <code>addressId</code> as ID.
	 * </p>
	 * 
	 * @param addressId
	 * 		ID of the address to delete
	 * @param editingUser
	 * 		The PORS user performing the deletion
	 * @param IP address
	 * 		IP address which is used
	 * @param sessionId
	 * 		ID of the current session
	 */
	void deleteAddress(long addressId, PorsUser editingUser, String ipAddress, 
			String sessionId);
	
	/**
	 * <p>
	 * Deletes all duplicate entries related to the address having 
	 * <code>addressId</code> as ID.
	 * </p>
	 * 
	 * @param addressId
	 * 		ID of the address whose duplicate entries should be deleted
	 */
	void deleteAddressDuplicates(long addressId);
	
	/**
	 * <p>
	 * Irrecoverable deletes the organisation having 
	 * <code>organisationId</code> as ID.
	 * </p>
	 * 
	 * @param organisationId
	 * 		ID of the organisation to delete
	 * @param editingUser
	 * 		The PORS user performing the deletion
	 * @param ipAddress
	 * 		IP address which is used
	 * @param sessionId
	 * 		ID of the current session
	 */
	void deleteOrganisation(long organisationId, PorsUser editingUser, 
			String ipAddress, String sessionId);
	
	/**
	 * <p>
	 * Deletes all duplicate entries related to the provider having 
	 * <code>organisationId</code> as ID.
	 * </p>
	 * 
	 * @param organisationId
	 * 		ID of the organisation whose duplicate entries should be deleted
	 */
	void deleteOrganisationDuplicates(long organisationId);
	
	/**
	 * <p>
	 * Irrecoverable deletes the provider having <code>providerId</code> as ID.
	 * </p>
	 * 
	 * @param providerId
	 * 		ID of the provider to delete
	 * @param editingUser
	 * 		The PORS user performing the deletion
	 * @param ipAddress
	 * 		IP address which is used
	 * @param sessionId
	 * 		ID of the current session
	 */
	void deleteProvider(long providerId, PorsUser editingUser, 
			String ipAddress, String sessionId);
	
	/**
	 * <p>
	 * Deletes all duplicate entries related to the provider having 
	 * <code>providerId</code> as ID.
	 * </p>
	 * 
	 * @param providerId
	 * 		ID of the provider whose duplicate entries should be deleted
	 */
	void deleteProviderDuplicates(long providerId);
	
	/**
	 * <p>
	 * Flushes and clears the current connection.
	 * </p>
	 * <p>
	 * Should be used when adding a huge amount of data in one transaction to 
	 * improve performance.
	 * </p>
	 */
	void flushAndClearConnection();
	
	/**
	 * <p>
	 * Gets the address whose ID is <code>id</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such address exists.
	 * </p>
	 * 
	 * @param id
	 * 		ID of the address
	 * @return
	 * 		Address with ID <code>id</code>
	 */
	Address getAddressById(final long id);
	
	/**
	 * <p>
	 * Returns the address which matches the given unique combination of 
	 * additional, street, house number, ZIP code, city and country.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such address exists.
	 * </p>
	 * 
	 * @param additional
	 * 		Additional to the address
	 * @param street
	 * 		Street value of the address
	 * @param houseNumber
	 * 		House number value of the address
	 * @param zipCode
	 * 		ZIP code value of the address
	 * @param city
	 * 		City value of the address
	 * @param country
	 * 		Country value of the address
	 * @return 
	 * 		Matching address
	 */
	Address getAddressByUniqueCombination(String additional, 
			String street, String houseNumber, String zipCode, String city, 
			String country);
	
	/**
	 * <p>
	 * Gets the matching duplicate which was aggregated in 
	 * <code>entry</code>.
	 * </p>
	 * <p>
	 * The type of the returned object depends on the domain of 
	 * <code>entry</code>. The domain {@link DuplicateEntry#DOMAIN_ADDRESS} 
	 * will lead to a returned {@link DuplicateAddress} object, 
	 * {@link DuplicateEntry#DOMAIN_ORGANISATION} to an 
	 * {@link DuplicateOrganisation} object and 
	 * {@link DuplicateEntry#DOMAIN_PROVIDER} to an {@link DuplicateProvider} 
	 * object. 
	 * </p>
	 * <p>
	 * Returns <code>null</code>, if no matching duplicate exists.
	 * </p>
	 * 
	 * @param entry
	 * 		Entry of the duplicates list
	 * @return
	 * 		Detailed object depending on the domain of <code>entry</code>
	 */
	Object getDetailedDuplicateEntry(DuplicateEntry entry);
	
	/**
	 * <p>
	 * Loads a detailed logging entry by domain and ID specified in 
	 * <code>logEntry</code>.
	 * </p>
	 * <p>
	 * The particular domains specify entities as follows:
	 * <br />
	 * <ul>
	 * <li>
	 * {@link History#DOMAIN_PROVIDER} returns an object of type 
	 * {@link org.openehealth.pors.database.entities.ProviderLog ProviderLog}.
	 * </li>
	 * <li>
	 * {@link History#DOMAIN_ORGANISATION} returns an object of type
	 * {@link org.openehealth.pors.database.entities.OrganisationLog OrganisationLog}.
	 * </li>
	 * <li>
	 * {@link History#DOMAIN_ADDRESS} returns an object of type 
	 * {@link org.openehealth.pors.database.entities.AddressLog AddressLog}.
	 * </li>
	 * <li>
	 * {@link History#DOMAIN_LOCALID} returns an object of type 
	 * {@link org.openehealth.pors.database.entities.LocalIdLog LocalIdLog}.
	 * </li>
	 * <li>
	 * {@link History#DOMAIN_ORGANISATION_HAS_ADDRESS} returns an object of 
	 * type 
	 * {@link org.openehealth.pors.database.entities.OrganisationHasAddressLog OrganisationHasAddressLog}.
	 * </li>
	 * <li>
	 * {@link History#DOMAIN_ORGANISATION_HAS_PROVIDER} returns an object of 
	 * type
	 * {@link org.openehealth.pors.database.entities.OrganisationHasProviderLog OrganisationHasProviderLog}.
	 * </li>
	 * <li>
	 * {@link History#DOMAIN_PROVIDER_HAS_ADDRESS} returns an object of type 
	 * {@link org.openehealth.pors.database.entities.ProviderHasAddress ProviderHasAddress}.
	 * </li>
	 * </ul>
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no matching entry exists.
	 * </p>
	 * 
	 * @param logEntry 
	 * 		Log entry whose details are requested
	 * @return 
	 * 		Detailed logging entry depending on the domain of <code>logEntry</code>
	 */
	Object getDetailedHistory(History logEntry);
	
	/**
	 * <p>
	 * Gets the full list of duplicate configuration settings.
	 * </p>
	 * <p>
	 * Returns an empty list if no setting was found.
	 * </p>
	 * 
	 * @return
	 * 		List of settings for duplicate configuration
	 */
	List<DuplicateRecognition> getDuplicateConfiguration();
	
	/**
	 * <p>
	 * Gets a list of all duplicate entries.
	 * </p>
	 * <p>
	 * Returns an empty list, of no entry was found.
	 * </p>
	 * 
	 * @return
	 * 		List of all duplicate entries
	 */
	List<DuplicateEntry> getDuplicateEntryList();
	
	/**
	 * <p>
	 * Returns the total number of all duplicate entries.
	 * </p>
	 * <p>
	 * Returns an empty list if no duplicate entry was found.
	 * </p>
	 * 
	 * @return
	 * 		Number of all duplicate entries
	 */
	long getDuplicateEntryNumber();
	
	/**
	 * <p>
	 * Loads a list of all history entries in database.
	 * </p>
	 * <p>
	 * Returns an empty list if no history entries where found.
	 * </p>
	 * 
	 * @return List of all history entries in database
	 */
	List<History> getHistory();
	
	/**
	 * <p>
	 * Returns a list of all logging entries for data sets created by 
	 * <code>user</code> or related to data sets created by <code>user</code>.
	 * </p>
	 * <p>
	 * The user can be defined by its ID or name, where ID has the higher 
	 * priority.
	 * </p>
	 * <p>
	 * Returns an empty list, if no matching history entry could be found.
	 * </p>
	 * 
	 * @param user 
	 * 		User whose history entries have to be returned
	 * @return 
	 * 		All history entries for <code>user</code>
	 */
	List<UserHistory> getHistoryForUser(PorsUser user);
	
	/**
	 * <p>
	 * Gets a sublist of all history entries starting at 
	 * <code>position</code> and having at most <code>max</code> entries.
	 * </p>
	 * <p>
	 * Returns all available history entries in the case that both 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list, if no matching history entry could be found.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position of the sublist (beginning with 0)
	 * @return
	 * 		Sublist of all history entries
	 */
	List<History> getHistoryHavingMaxEntriesFromPosition(int max, int position);
	
	/**
	 * <p>
	 * Gets the total number of all history entries.
	 * </p>
	 * 
	 * @return
	 * 		Total number of history entries
	 */
	long getHistoryNumber();
	
	/**
	 * <p>
	 * Gets the total number of all history entries created by 
	 * <code>user</code>. The user can be either defined by its ID or name 
	 * where the ID has the higher priority.
	 * </p>
	 * <p>
	 * Returns the total number of all history entries in the case 
	 * <code>user</code> is <code>null</code>.
	 * </p>
	 * <p>
	 * Returns <code>0</code> if the given user does not exist.
	 * </p>
	 * 
	 * @param user
	 * 		User whose history entries have to be counted
	 * @return
	 * 		Number of all histories entries created by <code>user</code>
	 */
	long getHistoryNumber(PorsUser user);
	
	/**
	 * <p>
	 * Gets the import result by the job ID in <code>result</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such import result exists.
	 * </p>
	 * 
	 * @param result
	 * 		The result to get
	 * @return
	 * 		The import result
	 */
	ImportResult getImportResult(ImportResult result);
	
	/**
	 * <p>
	 * Gets a sublist of all duplicate entries having at most <code>max</code> 
	 * entries and starting at <code>position</code>.
	 * </p>
	 * <p>
	 * Returns a full list of all duplicate entries in the case that 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list, if no duplicate entry was found.
	 * </p>
	 *  
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position of the sublist
	 * @return
	 * 		Sublist of all duplicate entries
	 */
	List<DuplicateEntry> getLimitedDuplicateEntryList(int max, int position);
	
	/**
	 * <p>
	 * Gets at most <code>max</code> history entries starting from 
	 * <code>position</code> created by <code>user</code>.
	 * </p>
	 * <p>
	 * Gets all history entries created by user if both 
	 * <code>max</code> and <code>position</code> have the value 
	 * <code>0</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such user history exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position of the entries in the total list of all entries
	 * @param user
	 * 		PORS user whose history entries have to be returned
	 * @return 
	 * 		Matching history entries
	 */
	List<UserHistory> getLimitedHistoryForUser(int max, int position, PorsUser user);
	
	/**
	 * <p>
	 * Gets a sublist of all organisations linked to <code>provider</code> 
	 * having at most <code>max</code> entries and starting at 
	 * <code>position</code>.
	 * </p>
	 * <p>
	 * Returns a full list of all matching organisations in the case that 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list, if no matching organisation was found.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position (beginning at 0)
	 * @param provider
	 * 		Provider linked to organisations
	 * @return
	 * 		Corrsponding list of organisations
	 */
	List<Organisation> getLimitedOrganisationListForProvider(
			int max, int position, Provider provider);
	
	/**
	 * <p>
	 * Gets a sublist of all organisations owned by <code>user</code> and 
	 * linked to <code>provider</code> having at most <code>max</code> entries
	 * and starting at <code>position</code>.
	 * </p>
	 * <p>
	 * Returns a full list of all matching organisations in the case that 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list, if no matching organisation was found.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position	
	 * 		Starting position (beginning at 0)
	 * @param provider
	 * 		Provider the organisations are linked to
	 * @param user
	 * 		PORS user owning the organisations
	 * @return
	 * 		Corresponding list of organisations
	 */
	List<Organisation> getLimitedOrganisationListForProviderOfUser(
			int max, int position, Provider provider, PorsUser user);
	
	/**
	 * <p>
	 * Gets a sublist of all organisations owned by <code>user</code> having 
	 * at most <code>max</code> entries and starting at <code>position</code>
	 * </p>
	 * <p>
	 * Returns a full list of all matching organisations in the case that 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list, if no matching organisation was found.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position (beginning at 0)
	 * @param user
	 * 		Owning user of organsations
	 * @return
	 * 		Corresponding list of organisations
	 */
	List<Organisation> getLimitedOrganisationListOfUser(
			int max, int position, PorsUser user);
	
	/**
	 * <p>
	 * Gets at most <code>max</code> providers linked to 
	 * <code>organisation</code> starting from <code>position</code>.
	 * </p>
	 * <p>
	 * Returns a full list of all matching providers in the case that 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list if no matching provider exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position of the result list
	 * @param organisation
	 * 		Organisation the providers are linked to
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getLimitedProviderListForOrganisation(int max, int position,
			Organisation organisation);
	
	/**
	 * <p>
	 * Gets ar most <code>max</code> providers linked to 
	 * <code>organisation</code> and owned by <code>user</code> starting 
	 * from <code>position</code>.
	 * </p>
	 * <p>
	 * Returns a full list of all matching providers in the case that 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list if no matching provider exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the result list
	 * @param organisation
	 * 		Organisation the providers are linked to
	 * @param user
	 * 		PORS user owning the providers
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getLimitedProviderListForOrganisationOfUser(int max, 
			int position, Organisation organisation, PorsUser user);
	
	/**
	 * <p>
	 * Gets at most <code>max</code> providers owned by <code>user</code> 
	 * starting at <code>position</code>.
	 * </p>
	 * <p>
	 * Returns a full list of all matching providers in the case that 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list if not matching provider exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of result list
	 * @param user
	 * 		User owning the providers
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getLimitedProviderListOfUser(int max, int position, PorsUser user);
	
	/**
	 * Checks if Id is set and then returns the LocalId by the id, otherwise
	 * the application system localid combination is searched.
	 * @param lid
	 * @return LocalId
	 */
	@Deprecated
	LocalId getLocalId(LocalId lid);
	
	/**
	 * <p>
	 * Gets the local ID having <code>id</code> as ID.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no matching local ID could be found.
	 * </p>
	 * 
	 * @param id
	 * 		ID of the local ID to select
	 * @return
	 * 		Matching local ID
	 */
	LocalId getLocalIdById(long id);
	
	/**
	 * <p>
	 * Gets the local ID by the unique combination of local ID, facility and 
	 * application.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such local ID exists.
	 * </p>
	 * 
	 * @param localId
	 * 		The local ID
	 * @param facility
	 * 		The facility the local ID belongs to
	 * @param application
	 * 		The application the local ID belongs to
	 * @return
	 * 		Matching local ID
	 */
	LocalId getLocalIdByUniqueCombination(String localId, String facility, 
			String application);
	
	/**
	 * <p>
	 * Gets the organisation having the given establishment ID.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param establishmentId
	 * 		Establishment ID of the organisation
	 * @return
	 * 		Matching organisation
	 */
	Organisation getOrganisationByEstablishmentId(
			final String establishmentId);
	
	/**
	 * <p>
	 * Gets the organisation entity having the given ID.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param id
	 * 		ID of the organisations
	 * @return
	 * 		Matching organisation
	 */
	Organisation getOrganisationById(final long id);
	
	/**
	 * <p>
	 * Gets the organisation linked to <code>localId</code>. Needs all 
	 * values except the ID of the local ID set.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param localId
	 * 		Local ID the organisation is linked to
	 * @return
	 * 		Matching organisation
	 */
	Organisation getOrganisationByLocalId(final LocalId localId);
	
	/**
	 * <p>
	 * Gets the organisation where the name equals <code>name</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param name
	 * 		Name of the organisation
	 * @return 
	 * 		Organisation having <code>name</code> as name
	 */
	Organisation getOrganisationByName(final String name);
	
	/**
	 * <p>
	 * Gets the organisation having the given OID.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no matching organisation exists.
	 * </p>
	 * 
	 * @param oid
	 * 		OID of the organisation
	 * @return 
	 * 		Matching organisation
	 */
	Organisation getOrganisationByOid(final String oid);
	
	/**
	 * <p>
	 * Gets all organisations.
	 * </p>
	 * 
	 * @return 
	 * 		List of all organisations
	 */
	List<Organisation> getOrganisationList();
	
	/**
	 * <p>
	 * Gets all organisations linked to <code>provider</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no matching organisation exists.
	 * </p>
	 * 
	 * @param provider
	 * 		The provider the organisations are linked to
	 * @return 
	 * 		List of matching organisations
	 */
	List<Organisation> getOrganisationListForProvider(Provider provider);
	
	/**
	 * <p>
	 * Gets all organisations linked to <code>provider</code> and owned by 
	 * <code>user</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such organisation exists.
	 * </p>
	 * 
	 * @param provider
	 * 		Provider linked to the organisations
	 * @param user
	 * 		PORS user owning the organisations
	 * @return 
	 * 		Matching organisations
	 */
	List<Organisation> getOrganisationListForProviderOfUser(Provider provider, 
			PorsUser user);

	/**
	 * <p>
	 * Gets a sublist of all organisations starting at 
	 * <code>position</position> and having at most <code>max</code> entries.
	 * </p>
	 * <p>
	 * Returns all available organisations in the case that both 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list if no matching organisation exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position of the sublist (beginning with 0)
	 * @return
	 * 		Sublist of all organisations
	 */
	List<Organisation> getOrganisationListHavingMaxEntriesFromPosition(int max, 
			int position);
	
	/**
	 * <p>
	 * Loads all organisations from database owned by <code>user</code>.
	 * </p>
	 * <p>
	 * The user can be specified by its id or name, where id has the higher 
	 * priority.
	 * </p>
	 * <p>
	 * Returns an empty list if no matching organisation exists.
	 * </p>
	 * 
	 * @param user 
	 * 		PORS user owning the organisations
	 * @return 
	 * 		Organisations owned by <code>user</code>
	 */
	List<Organisation> getOrganisationListOfUser(PorsUser user);
	
	/**
	 * <p>
	 * Gets the total number of all organisations.
	 * </p>
	 * 
	 * @return
	 * 		Total number of organisations
	 */
	long getOrganisationNumber();
	
	/**
	 * <p>
	 * Gets the total number of all organisations owned by <code>user</code>. 
	 * The user can be either defined by its ID or name where the ID has the 
	 * higher priority.
	 * </p>
	 * <p>
	 * Returns the total number of all organisations in the case 
	 * <code>user</code> is <code>null</code>.
	 * </p>
	 * <p>
	 * Returns <code>0</code> if the given user does not exist.
	 * </p>
	 * 
	 * @param user
	 * 		User whose organisations have to be counted
	 * @return
	 * 		Number of all organisations created by <code>user</code>
	 */
	long getOrganisationNumber(PorsUser user);
	
	/**
	 * <p>
	 * Gets the provider having the given ID.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider exists.
	 * </p>
	 * 
	 * @param id
	 * 		ID of the provider
	 * @return
	 * 		Matching provider
	 */
	Provider getProviderById(final long id);
	
	/**
	 * <p>
	 * Gets the provider linked to <code>localId</code>. For this, all fields 
	 * except of the ID (which will be ignored) of the local ID have to be set. 
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider exists.
	 * </p>
	 * 
	 * @param localId
	 * 		Local ID, the provider is linked to
	 * @return
	 * 		Matching provider
	 */
	Provider getProviderByLocalId(final LocalId localId);
	
	/**
	 * <p>
	 * Gets the provider having the given unique combination of first name, 
	 * last name, gender code and birthday.
	 * </p>
	 * <p>
	 * Birthday is allowed to be <code>null</code>. In that case there will 
	 * be returned a provider where besides of the other fields the birthday 
	 * is set to <code>null</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider exists.
	 * </p>
	 * 
	 * @param firstName
	 * 		First name of the provider
	 * @param lastName
	 * 		Last name of the provider
	 * @param genderCode
	 * 		Gender code of the provider
	 * @param birthday
	 * 		Birthday of the provider
	 */
	Provider getProviderByUniqueCombination(final String firstName, 
			final String lastName, final String genderCode, 
			final Date birthday);
	
	/**
	 * <p>
	 * Gets all available providers.
	 * </p>
	 * 
	 * @return 
	 * 		List of all providers
	 */
	List<Provider> getProviderList();
	
	/**
	 * <p>
	 * Gets the providers linked to <code>organisation</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such organisation exists.
	 * </p>
	 * 
	 * @param organisation
	 * 		Organisation the providers are linked to
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getProviderListForOrganisation(Organisation organisation);
	
	/**
	 * <p>
	 * Gets the providers linked to <code>organisation</code> and owned by 
	 * <code>user</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such provider exists.
	 * </p>
	 * 
	 * @param organisation
	 * 		Organisation linked to the providers
	 * @param user
	 * 		PORS user owning the organisations
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getProviderListForOrganisationOfUser(
			Organisation organisation, PorsUser user);
	
	/**
	 * <p>
	 * Gets a sublist of all providers starting at <code>position</position> 
	 * and having at most <code>max</code> entries.
	 * </p>
	 * <p>
	 * Returns all available providers in the case that both 
	 * <code>max</code> and <code>position</code> are 0.
	 * </p>
	 * <p>
	 * Returns an empty list if no provider was found.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position of the sublist (beginning with 0)
	 * @return
	 * 		Sublist of all providers
	 */
	List<Provider> getProviderListHavingMaxEntriesFromPosition(int max, 
			int position);
	
	/**
	 * <p>
	 * Loads all providers from database which have been created by 
	 * <code>user</code>.
	 * </p>
	 * <p>
	 * The user can be specified by its ID or name, where id has the higher 
	 * priority.
	 * </p>
	 * <p>
	 * Returns an empty list if no such provider exists.
	 * </p>
	 * 
	 * @param user 
	 * 		PORS user owning the providers
	 * @return 
	 * 		Providers owned by <code>user</code>
	 */
	List<Provider> getProviderListOfUser(PorsUser user);
	
	/**
	 * <p>
	 * Gets the total number of all providers.
	 * </p>
	 * 
	 * @return
	 * 		Total number of providers
	 */
	long getProviderNumber();
	
	/**
	 * <p>
	 * Gets the total number of all providers owned by <code>user</code>. 
	 * The user can be either defined by its ID or name where the ID has the 
	 * higher priority.
	 * </p>
	 * <p>
	 * Returns the total number of all provider in the case 
	 * <code>user</code> is <code>null</code>.
	 * </p>
	 * <p>
	 * Returns <code>0</code> if the given user does not exist.
	 * </p>
	 * 
	 * @param user
	 * 		User whose providers have to be counted
	 * @return
	 * 		Number of all providers created by <code>user</code>
	 */
	long getProviderNumber(PorsUser user);
	
	/**
	 * <p>
	 * Loads the PORS user from database having the same id or name as 
	 * <code>user</code> or returns <code>null</code> if no matching user was 
	 * found.
	 * </p>
	 * <p>
	 * The id has a higher priority than the name. So an object will only be 
	 * selected by the name value of <code>user</code> if no id was set.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such PORS user exists.
	 * </p>
	 * 
	 * @param user 
	 * 		Contains either id or name of user object to load
	 * @return
	 * 		Matching user
	 */
	PorsUser getUser(PorsUser user);
	
	/**
	 * <p>
	 * Gets all available PORS users.
	 * </p>
	 * 
	 * @return
	 * 		The PORS users
	 */
	List<PorsUser> getUserList();
	
	/**
	 * <p>
	 * Gets all available user roles.
	 * </p>
	 * 
	 * @return
	 * 		The user roles
	 */
	List<UserRole> getUserRoleList();
	
	/**
	 * <p>
	 * Checks if the <code>user</code> is owning <code>address</code> because 
	 * it is either connected to a provider or an organisation owned by 
	 * <code>user</code>.
	 * </p>
	 * 
	 * @param user
	 * 		PORS user potentially owning <code>address</code>
	 * @param address
	 * 		Address potentially owned by <code>user</code>
	 * @return 
	 * 		True, if <code>user</code> owns <code>address</code>, false otherwise.
	 */
	boolean isOwningUser(PorsUser user, Address address);
	
	/**
	 * <p>
	 * Checks, of <code>user</code> owns <code>localId</code> because it is 
	 * either connected to a provider or an organisation owned by 
	 * <code>user</code>.
	 * </p>
	 * 
	 * @param user
	 * 		PORS user potentially owning the local ID
	 * @param localId
	 * 		Local ID potentially owned by the PORS user
	 * @return 
	 * 		True, if <code>localId</code> is owned by <code>user</code>, false otherwise.
	 */
	boolean isOwningUser(PorsUser user, LocalId localId);
	
	/**
	 * <p>
	 * Sets the flag for duplicate recognition to 
	 * <code>isDuplicatesCalculated</code> for the address having 
	 * <code>addressId</code> as ID.
	 * </p>
	 * 
	 * @param addressId
	 * 		ID of the address to update
	 * @param isDupplicateCalculated
	 * 		True if duplicates have been calculated, else false
	 */
	void setAddressDuplicatesCalculated(long addressId, 
			boolean isDuplicatesCalculated);
	
	/**
	 * <p>
	 * Sets the flag for duplicate recognition to 
	 * <code>isDuplicatesCalculated</code> for the organisation having 
	 * <code>organisationId</code> as ID.
	 * </p>
	 * 
	 * @param organisationId
	 * 		ID of the organisation to update
	 * @param isDupplicatesCalculated
	 * 		True if duplicates have been calculated, else false
	 */
	void setOrganisationDuplicatesCalculated(long organisationId, 
			boolean isDuplicatesCalculated);
	
	/**
	 * <p>
	 * Sets the flag for duplicate recognition to 
	 * <code>isDuplicatesCalculated</code> for the provider having 
	 * <code>provdierId</code> as ID.
	 * </p>
	 * 
	 * @param providerId
	 * 		ID of the provider to update
	 * @param isDupplicatesCalculated
	 * 		True if duplicates have been calculated, else false
	 */
	void setProviderDuplicatesCalculated(long providerId, 
			boolean isDuplicatesCalculated);
	
	/**
	 * <p>
	 * Updates all entries of duplicate recognition in list 
	 * <code>entries</code>.
	 * </p>
	 * 
	 * @param entries
	 * 		The entries to update
	 */
	void updateDuplicateConfiguration(List<DuplicateRecognition> entries);
	
	/**
	 * <p>
	 * Updates <code>result</code>.
	 * </p>
	 * 
	 * @param result
	 * 		The import result to update
	 */
	void updateImportResult(ImportResult result);
	
	/**
	 * <p>
	 * Updates <code>organisation</code> and all related entities in
	 * database.
	 * </p>
	 * 
	 * @param organisation 
	 * 		Organisation to update
	 * @throws DatabaseException 
	 * 		If organisation could not be updated
	 */
	void updateOrganisation(Organisation organisation) throws DatabaseException;
	
	/**
	 * <p>
	 * Updates <code>provider</code> and all related entities in 
	 * database. 
	 * </p>
	 * 
	 * @param provider 
	 * 		Provider to update
	 * @throws DatabaseException 
	 * 		If provider could not be updated
	 */
	void updateProvider(Provider provider) throws DatabaseException;
	
	/**
	 * <p>
	 * Updates <code>user</code>.
	 * </p>
	 * 
	 * @param user
	 * 		PORS user to update
	 */
	void updateUser(PorsUser user);
	
	void deleteLocalIDs(List<LocalId> localIDs);
} 
