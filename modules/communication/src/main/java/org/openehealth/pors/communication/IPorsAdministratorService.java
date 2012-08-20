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
package org.openehealth.pors.communication;

import java.util.List;

import javax.jws.WebService;

import org.openehealth.pors.communication.exception.PORSException;

import org.openehealth.pors.core.dto.AddressDTO;
import org.openehealth.pors.core.dto.DuplicateConfigurationDTO;
import org.openehealth.pors.core.dto.DuplicateDetailDTO;
import org.openehealth.pors.core.dto.DuplicateEntryDTO;
import org.openehealth.pors.core.dto.DuplicateProcessingInfoDTO;
import org.openehealth.pors.core.dto.ImportResultDTO;
import org.openehealth.pors.core.dto.LocalIdDTO;
import org.openehealth.pors.core.dto.LoggingDetailDTO;
import org.openehealth.pors.core.dto.LoggingEntryDTO;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.PermissionDTO;
import org.openehealth.pors.core.dto.PorsCsv;
import org.openehealth.pors.core.dto.ProviderDTO;
import org.openehealth.pors.core.dto.SearchCriteriaDTO;
import org.openehealth.pors.core.dto.UserDTO;
import org.openehealth.pors.core.dto.UserRoleDTO;

/**
 * Represents the PORS administration Web Service. This Web Service can be used
 * to add provider, request stored providers and to request the history.
 * 
 * @author mf
 * 
 */
@WebService
public interface IPorsAdministratorService {

	/**
	 * Checks if the user data is correct.
	 * 
	 * @param user
	 *            the user credentials
	 * @throws PORSException
	 *             if the user is not found or the password is incorrect
	 */
	void authUser(UserDTO user) throws PORSException;

	/**
	 * Adds a new provider to the database.
	 * 
	 * @param provider
	 *            the provider to add
	 * @param user
	 *            the user credentials
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if there is
	 *             a database error.
	 */
	 void addProvider(ProviderDTO provider, UserDTO user)
			throws PORSException;

	/**
	 * Returns the list of all providers in the system.
	 * 
	 * @param user
	 *            the user credentials.
	 * @param maxResults
	 *            the number of maximum results, 0 = all
	 * @param offset
	 *            the offset for the retrieval, 0 = no offset
	 * @return the list of providers.
	 * @throws PORSException
	 *             if the login fails or the permission is missing.
	 */
	List<ProviderDTO> getProviders(UserDTO user, int maxResults,
			int offset) throws PORSException;

	/**
	 * Returns the current history entries.
	 * 
	 * @param user
	 *            the user credentials.
	 * @param maxResults
	 *            the number of maximum results, 0 = all
	 * @param offset
	 *            the offset for the retrieval, 0 = no offset
	 * @return the list of history entries.
	 * @throws PORSException
	 *             if the login fails or the permission is missing
	 */
	List<LoggingEntryDTO> getHistory(UserDTO user, int maxResults,
			int offset) throws PORSException;

	/**
	 * Returns the history detail for a special history entry.
	 * 
	 * @param user
	 *            the user credentials.
	 * @param entry
	 *            the entry for which the detail information is requested.
	 * @return details for the history entry.
	 * @throws PORSException
	 *             if the login fails or the permission is missing
	 */
	LoggingDetailDTO getHistoryDetail(UserDTO user, LoggingEntryDTO entry)
			throws PORSException;

	/**
	 * Returns the user detail for a special user.
	 * 
	 * @param user
	 *            the user credentials.
	 * @param userId
	 *            the id of the user for which the detail information is
	 *            requested.
	 * @return the user detail information
	 * @throws PORSException
	 *             if the login fails or the permission is missing
	 */
	UserDTO getUserDetail(UserDTO user, Integer userId)
			throws PORSException;

	/**
	 * Returns a list of all organisations in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @param maxResults
	 *            the number of maximum results, 0 = all
	 * @param offset
	 *            the offset for the retrieval, 0 = no offset
	 * @return List of all organisations
	 * @throws PORSException
	 *             if the login fails or the permission is missing
	 */
	List<OrganisationDTO> getOrganisations(UserDTO user, int maxResults,
			int offset) throws PORSException;

	/**
	 * Adds a new organisation to the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @param organisation
	 *            the organisation to add
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	void addOrganisation(UserDTO user, OrganisationDTO organisation)
			throws PORSException;

	/**
	 * Returns the permissions of a given user.
	 * 
	 * @param user
	 *            the user credentials
	 * @param requestUser
	 *            the user for which the permissions are requested
	 * @return a permission dto containg all permissions of the user
	 * @throws PORSException
	 */
	PermissionDTO getUserPermissions(UserDTO user, UserDTO requestUser)
			throws PORSException;

	/**
	 * Updates an existing provider.
	 * 
	 * @param user
	 *            the user credentials
	 * @param provider
	 *            the provider to update
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	void updateProvider(UserDTO user, ProviderDTO provider)
			throws PORSException;

	/**
	 * Updates an existing organisation.
	 * 
	 * @param user
	 *            the user credentials
	 * @param organisation
	 *            the organisation to update
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	void updateOrganisation(UserDTO user, OrganisationDTO organisation)
			throws PORSException;

	/**
	 * Returns the list of providers of an organisation.
	 * 
	 * @param user
	 *            the user credentials
	 * @param organisation
	 *            the organisation for which the providers get retrieved
	 * @return list of providers for the given organisation
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<ProviderDTO> getProvidersOfOrganisation(UserDTO user,
			OrganisationDTO organisation) throws PORSException;

	/**
	 * Returns the list of organisations of a provider.
	 * 
	 * @param user
	 *            the user credentials
	 * @param provider
	 *            the provider for which the organisations get retrieved
	 * @return list of providers for the given provider
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<OrganisationDTO> getOrganisationsOfProvider(UserDTO user,
			ProviderDTO provider) throws PORSException;

	/**
	 * Returns the details of a given providerId.
	 * 
	 * @param user
	 *            the user credentials
	 * @param provider
	 *            the provider for which the details get retrieved
	 * @return provider
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	ProviderDTO getProviderDetails(UserDTO user, ProviderDTO provider)
			throws PORSException;

	/**
	 * Returns the details of a given organisationId.
	 * 
	 * @param user
	 *            the user credentials
	 * @param organisation
	 *            the organisation for which the details get retrieved
	 * @return organisation
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	OrganisationDTO getOrganisationDetails(UserDTO user,
			OrganisationDTO organisation) throws PORSException;

	/**
	 * Returns the details of a given addressId.
	 * 
	 * @param user
	 *            the user credentials
	 * @param address
	 *            the address for which the details get retrieved
	 * @return address
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	AddressDTO getAddressDetails(UserDTO user, AddressDTO address)
			throws PORSException;

	/**
	 * Returns the details of a given localIdId.
	 * 
	 * @param user
	 *            the user credentials
	 * @param localId
	 *            the localId for which the details get retrieved
	 * @return localId
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	LocalIdDTO getLocalIdDetails(UserDTO user, LocalIdDTO localId)
			throws PORSException;

	/**
	 * Exports the given providers to csv file.
	 * 
	 * @param user
	 *            the user credentials
	 * @param fields
	 *            the fields of a provider to export
	 * @return PorsCsv
	 * @throws PORSException
	 *             if the login fails, the permission is missing
	 */
	PorsCsv exportProviders(UserDTO user, List<String> fields)
			throws PORSException;

	/**
	 * Imports providers from a given csv file.
	 * 
	 * @param user
	 *            the user credentials
	 * @param csvData
	 *            the given csv file
	 * @param fieldList
	 *            the fields of the providers to import
	 * @return ImportResultDTO
	 * @throws PORSException
	 */
	ImportResultDTO importProviders(UserDTO user, PorsCsv csvData,
			List<String> fieldList) throws PORSException;

	/**
	 * Imports organisations from a given csv file.
	 * 
	 * @param user
	 *            the user credentials
	 * @param csvData
	 *            the given csv file
	 * @param fieldList
	 *            the fields of the organisations to import
	 * @return ImportResultDTO
	 * @throws PORSException
	 */
	ImportResultDTO importOrganisations(UserDTO user, PorsCsv csvData,
			List<String> fieldList) throws PORSException;

	/**
	 * Exports the given organisations to csv file.
	 * 
	 * @param user
	 *            the user credentials
	 * @param fields
	 *            the fields of a organisation to export
	 * @return PorsCsv
	 * @throws PORSException
	 *             if the login fails, the permission is missing
	 */
	PorsCsv exportOrganisations(UserDTO user, List<String> fields)
			throws PORSException;

	/**
	 * Search all Provider entries that match to the search criteria
	 * 
	 * @param user
	 *            the user credentials
	 * @param criteriaDTO
	 *            the data transfer object with attribute + value pairs to
	 *            search
	 * @param operator
	 *            the search operator (AND , OR)
	 * @return List<ProviderDTO>
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<ProviderDTO> searchProviders(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws PORSException;

	/**
	 * Search all Organisation entries that match to the search criteria
	 * 
	 * @param user
	 *            the user credentials
	 * @param criteriaDTO
	 *            the data transfer object with attribute + value pairs to
	 *            search
	 * @param operator
	 *            the search operator (AND , OR)
	 * @return List<OrganisationDTO>
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<OrganisationDTO> searchOrganisations(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws PORSException;

	/**
	 * Search all History entries that match to the search criteria
	 * 
	 * @param user
	 *            the user credentials
	 * @param criteriaDTO
	 *            the data transfer object with attribute + value pairs to
	 *            search
	 * @param operator
	 *            the search operator (AND , OR)
	 * @return List<LoggingEntryDTO>
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<LoggingEntryDTO> searchHistory(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws PORSException;

	/**
	 * Returns a list of all duplicates in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @param maxResults
	 *            the number of maximum results, 0 = all
	 * @param offset
	 *            the offset for the retrieval, 0 = no offset
	 * @return List of all duplicates
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<DuplicateEntryDTO> getDuplicates(UserDTO user, int maxResults,
			int offset) throws PORSException;

	/**
	 * Returns the details of a given duplicate.
	 * 
	 * @param user
	 *            the user credentials
	 * @param entry
	 *            the duplicate for which the details get retrieved
	 * @return DuplicateDetailEntry
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	DuplicateDetailDTO getDuplicateDetail(UserDTO user,
			DuplicateEntryDTO entry) throws PORSException;

	/**
	 * Returns the DuplicateConfiguration.
	 * 
	 * @param user
	 *            the user credentials
	 * @return DuplicateConfigurationDTO
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	DuplicateConfigurationDTO getDuplicateConfiguration(UserDTO user)
			throws PORSException;

	/**
	 * Update an existing DuplicateConfigurationDTO.
	 * 
	 * @param dto
	 * @param user
	 *            the user credentials
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	void updateDuplicateConfiguration(DuplicateConfigurationDTO dto,
			UserDTO user) throws PORSException;

	/**
	 * Returns the ImportResultDTO
	 * 
	 * @param user
	 *            the user credentials
	 * @param result
	 *            the ImportResultDTO
	 * @return ImportResultDTO
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	ImportResultDTO getImportResult(UserDTO user, ImportResultDTO result)
			throws PORSException;

	/**
	 * Adds a new user to the database.
	 * 
	 * @param user
	 *            the new user to add
	 * @param user
	 *            the user credentials
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if there is
	 *             a database error.
	 */
	void addUser(UserDTO newUser, UserDTO user) throws PORSException;

	/**
	 * Updates an existing user.
	 * 
	 * @param user
	 *            the user credentials
	 * @param userUpdate
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	void updateUser(UserDTO user, UserDTO userUpdate)
			throws PORSException;

	/**
	 * Returns a list of all roles in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @return List of all roles
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<UserRoleDTO> getRoles(UserDTO user) throws PORSException;

	/**
	 * Process a given duplicate entry (delete, merge, left).
	 * 
	 * @param user
	 *            the user credentials
	 * @param detailDTO
	 *            the given DuplicateDetailDTO
	 * @param infoDTO
	 *            the processing information
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	void clearDuplicateEntry(UserDTO user, DuplicateDetailDTO detailDTO,
			DuplicateProcessingInfoDTO infoDTO) throws PORSException;

	/**
	 * Returns the count of the provider entries in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @return long
	 * @throws PORSException
	 */
	long getProviderCount(UserDTO user) throws PORSException;

	/**
	 * Returns the count of the organisation entries in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @return long
	 * @throws PORSException
	 */
	long getOrganisationCount(UserDTO user) throws PORSException;

	/**
	 * Returns the count of the history entries in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @return long
	 * @throws PORSException
	 */
	long getHistoryCount(UserDTO user) throws PORSException;

	/**
	 * Rebuilds the search index
	 * 
	 * @param user
	 *            the user credentials
	 * @throws PORSException
	 */
	void rebuildSearchindex(UserDTO user) throws PORSException;

	/**
	 * Returns the count of the duplicate entries in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @return long
	 * @throws PORSException
	 */
	long getDuplicateCount(UserDTO user) throws PORSException;

	/**
	 * Returns a list of all users in the database.
	 * 
	 * @param user
	 *            the user credentials
	 * @return List of all users
	 * @throws PORSException
	 *             if the login fails, the permission is missing or if a
	 *             database error occurs.
	 */
	List<UserDTO> getUsers(UserDTO user) throws PORSException;

}
