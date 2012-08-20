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
package org.openehealth.pors.database.dao;

import java.util.Date;
import java.util.List;

import org.openehealth.pors.database.entities.Provider;


/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.Provider Provider} 
 * objects.
 * </p>
 * 
 * @author jr
 * 
 * @see org.openehealth.pors.database.entities.Provider Provider
 */
public interface IProviderDao extends IDao<Long, Provider> 
{
	/**
	 * <p>
	 * Selects the providers linked to the organisation having 
	 * <code>organisationId</code> as ID.
	 * </p>
	 * 
	 * @param organisationId
	 * 		ID of the organisations
	 * @return
	 * 		Providers linked to <code>organisationId</code>
	 */
	List<Provider> getAllForOrganisationId(Long organisationId);
	
	/**
	 * <p>
	 * Selects the providers linked to the organisation having the ID 
	 * <code>organisationId</code> and owned by the PORS user having ID 
	 * <code>userId</code>.
	 * </p>
	 * 
	 * @param organisationId
	 * 		ID of the organisation
	 * @param userId
	 * 		ID of the user
	 * @return
	 * 		Providers linked to <code>organisationId</code> and owned by 
	 * 		<code>userId</code>
	 */
	List<Provider> getAllForOrganisationIdOfPorsUserId(Long organisationId, Integer userId);
	
	/**
	 * <p>
	 * Selects the providers linked to the organisation having ID 
	 * <code>organisationId</code> and owned by the PORS user having name 
	 * <code>userName</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such entity was found.
	 * </p>
	 * 
	 * @param organisationId
	 * 		ID of the organisation
	 * @param userName
	 * 		Name of the user
	 * @return
	 * 		Providers linked to <code>organisationId</code> and owned by 
	 * 		<code>userId</code>
	 */
	List<Provider> getAllForOrganisationIdOfPorsUserName(Long organisationId, String userName);
	
	/**
	 * <p>
	 * Selects <code>max</code> providers starting from <code>position</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such entity was found.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of data sets
	 * @param position
	 * 		Start position of the results (beginning with 0)
	 * @return
	 * 		The providers
	 */
	List<Provider> getAllMaxResultsFromPosition(int max, int position);
	
	/**
	 * <p>
	 * Selects <code>max</code> providers linked to the organisation having 
	 * <code>organisationId</code> as ID starting at <code>position</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such entry was found.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Position where to start the list
	 * @param organisatioId
	 * 		ID of the organisation the provider is linked to
	 * @return
	 * 		Matching list of providers
	 */
	List<Provider> getAllMaxResultsFromPositionForOrganisationId(
			int max, int position, long organisationId);
	
	/**
	 * <p>
	 * Selects <code>max</code> entries linked to the organisation having
	 * <code>organisationId</code> as ID and owned by the user having 
	 * <code>userId</code> as ID starting at <code>position</code>. 
	 * </p>
	 * <p>
	 * Returns an empty list if no matching entry exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position
	 * @param organisationId
	 * 		ID of the linked organisation
	 * @param userId
	 * 		ID of the owning user
	 * @return
	 * 		Matching list of providers
	 */
	List<Provider> getAllMaxResultsForOrganisationIdOfPorsUserId(int max, 
			int position, long organisationId, int userId);
	
	/**
	 * <p>
	 * Selects <code>max</code> providers linked to the organisation having 
	 * <code>organisationId</code> as ID and owned by the user having 
	 * <code>userName</code> as name starting at <code>position</code>
	 * </p>
	 * <p>
	 * Returns an empty list if no such provider exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the list
	 * @param organisationId
	 * 		ID of the linked organisation
	 * @param userName
	 * 		Name of the owning user
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getAllMaxResultsFromPositionForOrganisationIdOfPorsUserName(
			int max, int position, long organisationId, String userName);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> providers owned by the user having 
	 * <code>userId</code> as ID starting at <code>position</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such provider exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the list
	 * @param userId
	 * 		ID of the user owning the providers
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getAllMaxResultsFromPositionOfUserId(int max, int position, 
			int userId);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> providers owned by the user having 
	 * <code>userName</code> as name starting at <code>position</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such provider exists.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the first result
	 * @param userName
	 * 		Name of the owning user
	 * @return
	 * 		List of matching providers
	 */
	List<Provider> getAllMaxResultsFromPositionOfUserName(int max, 
			int position, String userName);
	
	/**
	 * <p>
	 * Selects the providers owned by the PORS user having ID 
	 * <code>userId</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such entity was found.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the owning PORS user
	 * @return
	 * 		Providers owned by <code>userId</code>
	 */
	List<Provider> getAllOfUserId(Integer userId);
	
	/**
	 * <p>
	 * Selects the providers owned by the PORS user having name 
	 * <code>userName</code>.
	 * </p>
	 * <p>
	 * Returns an empty list if no such entity was found.
	 * </p>
	 * 
	 * @param userName
	 * 		Name of the owning PORS user
	 * @return
	 * 		Providers owned by <code>userName</code>
	 */
	List<Provider> getAllOfUserName(String userName);
	
	/**
	 * <p>
	 * Selects the total number of providers.
	 * </p>
	 * 
	 * @return
	 * 		Total number of providers
	 */
	long getCount();
	
	/**
	 * <p>
	 * Selects the total number of providers owned by the user having 
	 * <code>userId</code> as ID.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the user owning the counted provides
	 * @return
	 * 		Total number of providers owned by the PORS user
	 */
	long getCountByUserId(int userId);
	
	/**
	 * <p>
	 * Selects the provider having the given <code>lanr</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider was found.
	 * </p>
	 * 
	 * @param lanr
	 * 		LANR of the provider to select
	 * @return
	 * 		Provider having <code>lanr</code>
	 */
	Provider getByLanr(String lanr);
	
	/**
	 * <p>
	 * Selects the provider having the given <code>lanr</code> and which has
	 * <b>not</b> the given <code>providerId</code>. 
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider was found.
	 * </p>
	 * 
	 * @param lanr
	 * 		LANR of the provider
	 * @param providerId
	 * 		ID which the resulting provider should <b>not</b> have
	 * @return
	 * 		The provider 
	 */
	Provider getByLanrExcludeProviderId(String lanr, Long providerId);
	
	/**
	 * <p>
	 * Selects the provider linked to the <code>localId</code> valid in
	 * in the application <code>application</code> of the facility 
	 * <code>facility</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider was found.
	 * </p>
	 * 
	 * @param localId
	 * 		Local ID
	 * @param application
	 * 		Application where local ID is valid
	 * @param facility
	 * 		Facility where local ID is valid
	 * @return
	 * 		The matching provider
	 */
	Provider getByLocalId(String localId, String application, String facility);
	
	/**
	 * <p>
	 * Selects the provider having <code>oid</code> as OID.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider exists.
	 * </p>
	 * 
	 * @param oid
	 * 		The OID
	 * @return
	 * 		Provider having <code>oid</code> as OID
	 */
	Provider getByOid(String oid);
	
	/**
	 * <p>
	 * Selects the provider having <code>oid</code> as OID and <b>not</b> 
	 * having <code>providerId</code> as ID.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such provider exists.
	 * </p>
	 * 
	 * @param oid
	 * 		The OID
	 * @param providerId
	 * 		ID, which the provider should <b>not</b> have
	 * @return
	 */
	Provider getByOidExcludeProviderId(String oid, Long providerId);
	
	/**
	 * <p>
	 * Selects the provider having the given unique combination of first name, 
	 * last name, gender code and birthday.
	 * </p>
	 * <p>
	 * It is allowed to set <code>birthday</code> to <code>null</code>. In such 
	 * a case, there will be selected by a data set where the birthday is set 
	 * to <code>NULL</code>.
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
	 * 		Birthday of the provider or <code>null</code>
	 * @return
	 * 		The matching provider
	 */
	Provider getByUniqueCombination(String firstName, String lastName, String genderCode, Date birthday);
	
	/**
	 * <p>
	 * Selects the provider having the given first name, last name, gender code 
	 * and birthday which does <b>not</b> have the given 
	 * <code>providerId</code>.
	 * </p>
	 * <p>
	 * It is allowed to set <code>birthday</code> to <code>null</code>. In such
	 * a case, there will be selected by a data set where the birthday is set 
	 * to <code>NULL</code>.
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
	 * 		Birthday of the provider or <code>null</code>
	 * @param providerId
	 * 		ID which the provider should <b>not</b> have
	 * @return
	 * 		The matching provider
	 */
	Provider getByUniqueCombinationExcludeProviderId(String firstName, String lastName, String genderCode, Date birthday, Long providerId);
	
	/**
	 * <p>
	 * Checks, if there exists any provider owned by the user having 
	 * <code>userId</code> as ID and being linked to the local ID having 
	 * <code>localIdId</code> as ID.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the PORS user
	 * @param localIdId
	 * 		ID of the local ID
	 * @return
	 * 		True, if there exists at least one such provider, else false.
	 */
	boolean isAnyOwnedByPorsUserIdReferencingLocalIdId(Integer userId, Long localIdId);
	
	/**
	 * <p>
	 * Checks, if there exists any provider owned by the user having 
	 * <code>userId</code> as ID and being linked to the address having
	 * <code>addressId</code> as ID.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the PORS user
	 * @param addressId
	 * 		ID of the address
	 * @return
	 * 		True, if there exists at least one such provider, else false.
	 */
	boolean isAnyOwnedByPorsUserIdReferencingAddressId(Integer userId, Long addressId);
	
	/**
	 * <p>
	 * For all addresses, the flag for calculated duplicates is set to 
	 * <code>calculated</code>.
	 * </p>
	 * 
	 * @param calculated
	 * 		True, if duplicates where calculated, else false.
	 */
	void updateDuplicatesCalculated(boolean calculated);
	
	/**
	 * <p>
	 * For the provider having <code>id</code> as ID, the flag for calculated 
	 * duplicates is set to <code>calculated</code>.
	 * </p>
	 * 
	 * @param id
	 * 		ID of the provider to update
	 * @param calculated
	 * 		True, if duplicates where calculated, else false.
	 */
	void updateDuplicatesCalculatedById(long id, boolean calculated);
}
