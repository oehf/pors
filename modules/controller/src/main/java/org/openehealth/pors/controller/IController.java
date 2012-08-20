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
package org.openehealth.pors.controller;

import java.util.List;

import javax.ejb.Local;

import org.openehealth.pors.core.common.Task;
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
import org.openehealth.pors.core.exception.AddressNotFoundException;
import org.openehealth.pors.core.exception.AuthentificationException;
import org.openehealth.pors.core.exception.CsvExportException;
import org.openehealth.pors.core.exception.CsvImportException;
import org.openehealth.pors.core.exception.ImportException;
import org.openehealth.pors.core.exception.InvalidHL7MessageException;
import org.openehealth.pors.core.exception.MissingFieldsException;
import org.openehealth.pors.core.exception.MissingRightsException;
import org.openehealth.pors.core.exception.OrganisationNotFoundException;
import org.openehealth.pors.core.exception.ProviderNotFoundException;
import org.openehealth.pors.core.exception.UserNotFoundException;
import org.openehealth.pors.core.exception.WrongValueException;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.exception.DatabaseException;
import org.openehealth.pors.database.exception.SearchException;

/**
 * The Controller is the central component. It receives method calls from the communication
 * component and distributes the responsibility to the other components, while 
 * calling their methods.
 * 
 * @author ck
 * 
 */
@Local
public interface IController {
/**
 * Receives a UserDTO object, assembles the UserDTO into an user entity
 * and calls Authenticator for authenticating the user
 * @param UserDTO user , who wants to login
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 * @throws MissingFieldsException if there is no username
 */
 void authUser(UserDTO userDto) throws AuthentificationException, MissingFieldsException;

 /**
 * Receives a ProviderDTO object, assembles the ProviderDTO into a Provider entity.
 * The same is done with the UserDTO. The Controller checks the rights of the user using authentication.
 * Then if the user is allowed to do so, it will add the Provider using the DatabaseManager.
 * @param UserDTO user , who wants to add a Provider
 * @param ProviderDTO provider, who is added
 * @throws DatabaseException is thrown by DatabaseManager and forwarded here.
 * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 */
 void addProvider(ProviderDTO providerDto, UserDTO userDto) throws DatabaseException, MissingRightsException, AuthentificationException, MissingFieldsException, WrongValueException;

 /**
 * Receives a UserDTO object, assembles the UserDTO into a User entity.
 * After checking the rights (authenticator) it receives a List of Provider entities from the DatabaseManager.
 * The Provider entities are assembled into ProviderDTOs and the List is returned.
 * @param UserDTO user , who wants to list all providers
 * @param maxResults the maximum results
 * @param offset the offset
 * @return List<ProviderDTO> the list of all providers
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
 */
 List<ProviderDTO> getAllProviders(UserDTO userDto, int maxResults, int offset) throws MissingRightsException, AuthentificationException, MissingFieldsException;

 /**
 * Receives a UserDTO object, assembles the UserDTO into a User entity.
 * After checking the rights (authenticator) it receives a List of LoggingEntry entities from the DatabaseManager.
 * The LoggingEntry entities are assembled into LoggingEntryDTO and the List is returned.
 * @param UserDTO user , who wants to get the history
 * @param maxResults the number of maximum results, 0 = all
 * @param offset the offset for the retrieval, 0 = no offset
 * @return List<LoggingEntryDTO> list of LoggingEntries
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
 */
 List<LoggingEntryDTO> getHistory(UserDTO userDto, int maxResults, int offset) throws MissingRightsException, AuthentificationException, MissingFieldsException;

 /**
 * Receives a LoggingEntryDTO, for which more detail is required.
 * The rights for the task is checked and the LoggingDetailDTO is returned.
 * @param UserDTO user , who wants the Logging detail.
 * @param LoggingEntryDTO entry, for which the detail is required
 * @return LoggingDetailDTO the detail of a logging entry
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
 */
 LoggingDetailDTO getHistoryDetail(UserDTO userDto, LoggingEntryDTO entry) throws MissingRightsException, AuthentificationException, MissingFieldsException;
 
 /**
  * Calls the hl7MessageHandler to interpret the message and gets a list
  * of tasks. A task can be "adding Provider x" for example. Then it runs each task in the taskList.
  * Finally it returns the hl7 return message, which is build by the HL7 component.
  * @param hl/Message contains the hl7 formated message, userDto contains the user data
  * @return String contains the hl7 return message
  * @throws InvalidHL7MessageException , if an error occured, needed for transactions 
  */
 String processMessage(String hl7Message, UserDTO userDto) throws InvalidHL7MessageException;

 /**
 * Receives a UserDTO, which represents the user, who wants to see the detail of another user
 * with the Integer userId. The detailed User is returned.
 * @param UserDTO user , who wants the user detail.
 * @param userId, the id of the requested detailed user
 * @return UserDTO the detail of the user.
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 * @throws UserNotFoundException is thrown if the user isn't found
 * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
 */
 UserDTO getUserDetail(UserDTO user, Integer userId) throws MissingRightsException, UserNotFoundException, AuthentificationException, MissingFieldsException;

 /**
 * Receives a UserDTO object, assembles the UserDTO into a User entity.
 * After checking the rights (authenticator) it receives a List of Organisations entities from the DatabaseManager.
 * The Organisation entities are assembled into OrganisationDTOs and the List is returned.
 * @param UserDTO user , who wants to list all organisations
 * @param maxResults the number of maximum results, 0 = all
 * @param offset the offset for the retrieval, 0 = no offset
 * @return List<OrganisationDTO> the list of all OrgansationDTO
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
 */
 List<OrganisationDTO> getAllOrganisations(UserDTO userDto, int maxResults, int offset) throws MissingRightsException, AuthentificationException, MissingFieldsException;

 /**
 * Receives a OrgansiationDTO object, assembles the OrganisationDTO into a Organsiation entity.
 * The same is done with the UserDTO. The Controller checks the rights of the user using authenticator.
 * Then if the user is allowed to do so, it will add the Provider using the DatabaseManager.
 * @param UserDTO user , who wants to add a Organsiation
 * @param OrganisationDTO provider, who is added
 * @throws DatabaseException is thrown by DatabaseManager and forwarded here.
 * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
 * @throws AuthentificationException is thrown if the password or username is inccorrect
 */
 void addOrganisation(OrganisationDTO organisation, UserDTO userDto) throws MissingRightsException, AuthentificationException, DatabaseException, MissingFieldsException, WrongValueException;
 
 /**
  * Returns the permissions of a given user.
  * @param userDTO the user who wants to request the permissions
  * @param requestUser the user for which the permissions are requested
  * @return a PermissionDTO object which contains all permission information
  * @throws MissingRightsException if there are not enough rights to retrieve the permissions for the user
  * @throws UserNotFoundException if the user was not found
  * @throws AuthentificationException is thrown if the password or username is inccorrect
  * @throws MissingFieldsException if there are missing fields in the user object
  */
 PermissionDTO getUserPermissions(UserDTO userDTO, UserDTO requestUser) throws MissingRightsException, UserNotFoundException, AuthentificationException, MissingFieldsException;
 
 /**
  * Updates an existing provider.
  * @param userDTO the user who wants to execute the update
  * @param providerDTO the provider to update
  * @throws AuthentificationException is thrown if the password or username is inccorrect
  * @throws MissingRightsException if there are not enough rights
  * @throws MissingFieldsException if there are missing fields in the user or provider object
  * @throws WrongValueException if there is a wrong value within the provider object
  * @throws DatabaseException if a database error occurs
  */
 void updateProvider(UserDTO userDTO, ProviderDTO providerDTO) throws AuthentificationException, MissingRightsException, MissingFieldsException, WrongValueException, DatabaseException, ProviderNotFoundException;
 
 /**
  * Updates an existing organisation.
  * @param userDTO the user who wants to execute the update
  * @param organisationDTO the organisation to update
  * @throws AuthentificationException is thrown if the password or username is inccorrect
  * @throws MissingRightsException if there are not enough rights
  * @throws MissingFieldsException if there are missing fields in the user or organisation object
  * @throws WrongValueException if there is a wrong value within the organisation object
  * @throws DatabaseException if a database error occurs
  */
 void updateOrganisation(UserDTO userDTO, OrganisationDTO organisationDTO) throws AuthentificationException, MissingRightsException, MissingFieldsException, WrongValueException, DatabaseException, OrganisationNotFoundException;
 
 /**
  * Receives a UserDTO object, assembles the UserDTO into a User entity.
  * After checking the rights (authenticator) it receives a List of Provider entities from the DatabaseManager
  * for the given organisation. The Provider entities are assembled into ProviderDTOs and the List is returned.
  * @param UserDTO the user
  * @param OrganisationDTO the organisation
  * @return List<ProviderDTO> the list of the providers for the given organisation
  * @throws AuthentificationException is thrown if the password or username is inccorrect
  * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
  **/
  List<ProviderDTO> getProvidersOfOrganisation(UserDTO userDto, OrganisationDTO organisation) throws MissingRightsException, AuthentificationException, MissingFieldsException;
  
  /**
   * Receives a UserDTO object, assembles the UserDTO into a User entity.
   * After checking the rights (authenticator) it receives a List of Organisation entities from the DatabaseManager
   * for the given provider. The Organisation entities are assembled into OrganisationDTOs and the List is returned.
   * @param UserDTO the user
   * @param ProviderDTO the provider
   * @return List<OrganisationDTO> the list of the organisations for the given provider
   * @throws AuthentificationException is thrown if the password or username is inccorrect
   * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
   **/
   List<OrganisationDTO> getOrganisationsOfProvider(UserDTO userDto, ProviderDTO provider) throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Receives a UserDTO object, assembles the UserDTO into a User entity.
    * After checking the rights (authenticator) it receives a Provider entity from the DatabaseManager
    * for the given Id of the provider. The Provider entity is assembled into a ProviderDTO and is returned.
    * @param UserDTO the user
    * @param ProviderDTO the provider
    * @return ProviderDTO the details for the given providerId
    * @throws MissingFieldsException
    * @throws AuthentificationException is thrown if the password or username is inccorrect
    * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
    **/
   ProviderDTO getProviderDetails(UserDTO user, ProviderDTO provider) throws MissingRightsException, AuthentificationException, MissingFieldsException;
	
   /**
    * Receives a UserDTO object, assembles the UserDTO into a User entity.
    * After checking the rights (authenticator) it receives a Organisation entity from the DatabaseManager
    * for the given Id of the Organisation. The Organisation entity is assembled into a OrganisationDTO and is returned.
    * @param UserDTO the user
    * @param ProviderDTO the Organisation
    * @return ProviderDTO the details for the given OrganisationId
    * @throws MissingFieldsException
    * @throws AuthentificationException is thrown if the password or username is inccorrect
    * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
    **/
   OrganisationDTO getOrganisationDetails(UserDTO user, OrganisationDTO organisation) throws MissingRightsException, AuthentificationException, MissingFieldsException;
	
   /**
    * Receives a UserDTO object, assembles the UserDTO into a User entity.
    * After checking the rights (authenticator) it receives a Address entity from the DatabaseManager
    * for the given Id of the Address. The Address entity is assembled into a AddressDTO and is returned.
    * @param UserDTO the user
    * @param ProviderDTO the Address
    * @return ProviderDTO the details for the given AddressId
    * @throws MissingFieldsException
    * @throws AuthentificationException is thrown if the password or username is inccorrect
    * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
    **/
   AddressDTO getAddressDetails(UserDTO user, AddressDTO address) throws MissingRightsException, AuthentificationException, MissingFieldsException;
	
   /**
    * Receives a UserDTO object, assembles the UserDTO into a User entity.
    * After checking the rights (authenticator) it receives a LocalId entity from the DatabaseManager
    * for the given Id of the LocalId. The LocalId entity is assembled into a LocalIdDTO and is returned.
    * @param UserDTO the user
    * @param ProviderDTO the LocalId
    * @return ProviderDTO the details for the given LocalIdId
    * @throws MissingFieldsException
    * @throws AuthentificationException is thrown if the password or username is inccorrect
    * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
    **/
   LocalIdDTO getLocalIdDetails(UserDTO user, LocalIdDTO localId) throws MissingRightsException, AuthentificationException, MissingFieldsException;
    
   /**
    * Exports the current list of providers to a CSV File and exports only the given fields.
    * @param user the user
    * @param fields fields selected for export
    * @return CSV file conent
    * @throws MissingRightsException if rights are missing for this action
    * @throws AuthentificationException if authentification fails
    * @throws MissingFieldsException if password or username is missing
    * @throws CsvExportException if an export error occurs
    */
   PorsCsv exportProviders(UserDTO user, List<String> fields) throws MissingRightsException, AuthentificationException, MissingFieldsException, CsvExportException;
   
   /**
    * Exports the current list of organisation to a CSV File and exports only the given fields.
    * @param user the user
    * @param fields fields selected for export
    * @return CSV file content
    * @throws MissingRightsException if rights are missing for this action
    * @throws AuthentificationException if authentification fails
    * @throws MissingFieldsException if password or username is missing
    * @throws CsvExportException if an export error occurs
    */
   PorsCsv exportOrganisations(UserDTO user, List<String> fields) throws MissingRightsException, AuthentificationException, MissingFieldsException, CsvExportException;
   
   /**
    * Imports a list of providers from the given CSV file content. The fieldList is used 
    * to map CSV fields to providers fields. The complete process is asynchronous, so the method 
    * returns a ImportResultDTO, which can be used to query the system for the current import status.
    * @param user the user
    * @param csvData csv file content which includes the providers
    * @param fieldList list of fields for mapping from csv fields to provider fields
    * @return ImportResultDTO for query of status
    * @throws CsvImportException If there is a problem with the CSV file content
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   ImportResultDTO importProviders(UserDTO user, PorsCsv csvData, List<String> fieldList) throws CsvImportException, AuthentificationException, MissingFieldsException;
   
   /**
    * Imports a list of organisation from the given CSV file content. The fieldList is used 
    * to map CSV fields to organisation fields. The complete process is asynchronous, so the method 
    * returns a ImportResultDTO, which can be used to query the system for the current import status.
    * @param user the user
    * @param csvData csv file content which includes the organisation
    * @param fieldList list of fields for mapping from csv fields to organisation fields
    * @return ImportResultDTO for query of status
    * @throws CsvImportException If there is a problem with the CSV file content
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   ImportResultDTO importOrganisations(UserDTO user, PorsCsv csvData, List<String> fieldList) throws CsvImportException, MissingFieldsException, AuthentificationException;
   
   /**
    * Search for providers by using the given search criteria and operator.
    * @param user the user
    * @param criteriaDTO List of criteria used for the search, includes names of fields and search values
    * @param operator AND or OR
    * @return list of providers which match the given criteria
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is not set
    * @throws SearchException if an error occurs at search time
    * @throws MissingRightsException if rights are missing
    */
   List<ProviderDTO> searchProviders(UserDTO user, List<SearchCriteriaDTO> criteriaDTO, String operator) throws AuthentificationException, MissingFieldsException, SearchException, MissingRightsException;
   
   /**
    * Search for organisation by using the given search criteria and operator.
    * @param user the user
    * @param criteriaDTO List of criteria used for the search, includes names of fields and search values
    * @param operator AND or OR
    * @return list of organisation which match the given criteria
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is not set
    * @throws SearchException if an error occurs at search time
    * @throws MissingRightsException if rights are missing
    */
   List<OrganisationDTO> searchOrganisations(UserDTO user, List<SearchCriteriaDTO> criteriaDTO, String operator) throws AuthentificationException, MissingFieldsException, SearchException, MissingRightsException;
   
   /**
    * Search for history entries by using the given search criteria and operator.
    * @param user the user
    * @param criteriaDTO List of criteria used for the search, includes names of fields and search values
    * @param operator AND or OR
    * @return list of history entries which match the given criteria
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is not set
    * @throws SearchException if an error occurs at search time
    * @throws MissingRightsException if rights are missing
    */
   List<LoggingEntryDTO> searchHistory(UserDTO user, List<SearchCriteriaDTO> criteriaDTO, String operator) throws AuthentificationException, MissingFieldsException, SearchException, MissingRightsException;
   
   /**
    * Retrieves the current list of duplicates.
    * @param user the user
    * @param maxResults number of maximum results 
    * @param offset the offset to use
    * @return list of duplicates with maxResults entries and all entries from the offset
    * @throws AuthentificationException if the authentification fails
    * @throws MissingRightsException if rights are missing for this action
    * @throws MissingFieldsException if username or password is missing
    */
   List<DuplicateEntryDTO> getDuplicates(UserDTO user, int maxResults, int offset) throws AuthentificationException, MissingRightsException, MissingFieldsException;
   
   /**
    * Retrieves the details for the given duplicate entry.
    * @param user the user
    * @param entry the entry for which detailed information should be retrieved
    * @return detail information for the entry
    * @throws AuthentificationException if authentification fails
    * @throws MissingFieldsException if username or password is missing
    * @throws MissingRightsException if rights are missing for this action
    * @throws AddressNotFoundException if the address cannot be found
    * @throws ProviderNotFoundException if the provider cannot be found
    * @throws OrganisationNotFoundException if the organisation cannot be found
    */
   DuplicateDetailDTO getDuplicateDetail(UserDTO user, DuplicateEntryDTO entry) throws AuthentificationException, MissingFieldsException, MissingRightsException, AddressNotFoundException, ProviderNotFoundException, OrganisationNotFoundException;
   
   /**
    * Handles a list of domain tasks.
    * @param taskList list of tasks
    * @param user the user
    * @return List of entities, if needed
    * @throws OrganisationNotFoundException if the organisation cannot be found
    * @throws DatabaseException if there was a database problem
    * @throws MissingRightsException if the rights for this action are missing
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for user
    * @throws WrongValueException if there is a wrong value, e.g. e-mail address format is wrong
    * @throws ProviderNotFoundException if the provider cannot be found
    * @throws ImportException if the import fails
    * @throws SearchException if the search fails
    */
   @SuppressWarnings("rawtypes")
   List handleTaskList(List<Task> taskList, UserDTO user) throws OrganisationNotFoundException, DatabaseException, MissingRightsException, AuthentificationException, MissingFieldsException, WrongValueException, ProviderNotFoundException, ImportException, SearchException;
   
   /**
    * Handles one domain task
    * @param task the task to handle
    * @param porsUser the user
    * @param ip the ip address of the user
    * @param sessionId the current session id
    * @param flushAndClear if the database connection should be flushed and cleared
    * @return list of entities, if needed
    * @throws AuthentificationException if authentification fails
    * @throws MissingFieldsException if username or password is missing
    * @throws SearchException if the search fails
    * @throws OrganisationNotFoundException if the organisation cannot be found
    * @throws DatabaseException if there was a problem with the database
    * @throws MissingRightsException if the rights are missing for this task
    * @throws WrongValueException if there is a wrong value, e.g. e-mail address format is wrong
    * @throws ProviderNotFoundException if the provider cannot be found
    * @throws ImportException if the import fails
    */
   @SuppressWarnings("rawtypes")
   List handleTask(Task task, PorsUser porsUser, String ip, String sessionId, boolean flushAndClear) throws AuthentificationException, MissingFieldsException, SearchException, OrganisationNotFoundException, DatabaseException, MissingRightsException, WrongValueException, ProviderNotFoundException, ImportException;
   
   /**
    * Retrieves the duplicate configuration.
    * @param user the user
    * @return the configuration for the duplicate recognition
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   DuplicateConfigurationDTO getDuplicateConfiguration(UserDTO user) throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Returns the current status for the given import result.
    * @param user the user
    * @param resultDTO the result for which the current information will be retrieved
    * @return the current status of the import result
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   ImportResultDTO getImportResult(UserDTO user, ImportResultDTO resultDTO) throws MissingRightsException, AuthentificationException, MissingFieldsException;

   /**
    * Receives a UserDTO object, assembles the UserDTO into a user entity.
    * The same is done with the UserDTO. The Controller checks the rights of the user using authentication.
    * Then if the user is allowed to do so, it will add the Provider using the DatabaseManager.
    * @param UserDTO user , who wants to add a user
    * @param UserDTO newUser, who is added
    * @throws DatabaseException is thrown by DatabaseManager and forwarded here.
    * @throws MissingRightsException is thrown by the Authenticator and forwarded here.
    * @throws AuthentificationException is thrown if the password or username is inccorrect
    * @throws UserNotFoundException is thrown if the user isn't found
    **/
   void addUser(UserDTO newUserDTO, UserDTO userDTO)  throws DatabaseException, MissingRightsException, AuthentificationException, MissingFieldsException, WrongValueException;

   /**
    * Updates configuration for the duplicate recognition.
    * @param dto the configuration
    * @param userDto the user
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   void updateDuplicateConfiguration(DuplicateConfigurationDTO dto, UserDTO userDto) throws MissingRightsException, AuthentificationException, MissingFieldsException;

   /**
    * Returns all roles.
    * @param user the user
    * @return the list of roles
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   List<UserRoleDTO> getAllRoles(UserDTO user)  throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Updates a user.
    * @param user the user
    * @param userUpdate the user to update 
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    * @throws UserNotFoundException if the user cannot be found
    * @throws WrongValueException if there is a wrong value
    */
   void updateUser(UserDTO user, UserDTO userUpdate) throws MissingRightsException, UserNotFoundException, AuthentificationException, MissingFieldsException, WrongValueException;
   
   /**
    * Clears a duplicate entry by using the given processing info. This includes a strategy like 
    * removing both entries, using only one, merging the two entries or just removing the duplicate status 
    * flag for the entries.
    * @param user the user
    * @param detailDTO the detail entry for this duplicate
    * @param infoDTO
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    * @throws DatabaseException if there was a problem with the database
    */
   void clearDuplicateEntry(UserDTO user, DuplicateDetailDTO detailDTO, DuplicateProcessingInfoDTO infoDTO) throws MissingRightsException, AuthentificationException, MissingFieldsException, DatabaseException;
   
   /**
    * Retrieves the number of providers in the database.
    * @param user the user
    * @return number of providers in the database
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   long getProviderCount(UserDTO user) throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Retrieves the number of organisations in the database.
    * @param user the user
    * @return number of organisations in the database
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   long getOrganisationCount(UserDTO user) throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Retrieves the number of history entries in the database.
    * @param user the user
    * @return number of history entries in the database
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   long getHistoryCount(UserDTO user) throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Rebuilds the search index
    * @param user the user
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   void rebuildSearchindex(UserDTO user) throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Retrieves the number of duplicate entries in the database.
    * @param user the user
    * @return number of duplicate entries in the database
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   long getDuplicateCount(UserDTO user) throws MissingRightsException, AuthentificationException, MissingFieldsException;
   
   /**
    * Returns the list of users.
    * @param user the user
    * @return list of all users
    * @throws MissingRightsException if the rights are missing for this action
    * @throws AuthentificationException if the authentification fails
    * @throws MissingFieldsException if username or password is missing for the user
    */
   List<UserDTO> getUsers(UserDTO user) throws MissingRightsException, AuthentificationException, MissingFieldsException;
}
