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
package org.openehealth.pors.core;

import java.util.List;

import javax.ejb.Local;

import org.openehealth.pors.core.common.Permission;
import org.openehealth.pors.core.dto.AddressDTO;
import org.openehealth.pors.core.dto.AddressLogDTO;
import org.openehealth.pors.core.dto.DuplicateConfigurationDTO;
import org.openehealth.pors.core.dto.DuplicateDetailDTO;
import org.openehealth.pors.core.dto.DuplicateEntryDTO;
import org.openehealth.pors.core.dto.ImportResultDTO;
import org.openehealth.pors.core.dto.LocalIdDTO;
import org.openehealth.pors.core.dto.LocalIdLogDTO;
import org.openehealth.pors.core.dto.LoggingEntryDTO;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.OrganisationHasAddressLogDTO;
import org.openehealth.pors.core.dto.OrganisationHasProviderLogDTO;
import org.openehealth.pors.core.dto.OrganisationLogDTO;
import org.openehealth.pors.core.dto.PermissionDTO;
import org.openehealth.pors.core.dto.ProviderDTO;
import org.openehealth.pors.core.dto.ProviderHasAddressLogDTO;
import org.openehealth.pors.core.dto.ProviderLogDTO;
import org.openehealth.pors.core.dto.SearchCriteriaDTO;
import org.openehealth.pors.core.dto.UserDTO;
import org.openehealth.pors.core.dto.UserRoleDTO;
import org.openehealth.pors.core.exception.MissingFieldsException;
import org.openehealth.pors.core.exception.WrongValueException;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.AddressLog;
import org.openehealth.pors.database.entities.DuplicateEntry;
import org.openehealth.pors.database.entities.DuplicateRecognition;
import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.ImportResult;
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
import org.openehealth.pors.database.entities.UserHistory;
import org.openehealth.pors.database.entities.UserRole;
import org.openehealth.pors.database.util.SearchCriteria;

/**
 * The PorsCore is used to convert between Data Transfer Objects and Entities.
 * For each data element two assemble methods are provided, which assemble DTOs
 * out of entities and vice versa.
 */
@Local
public interface IPorsCore {

	/**
	 * This method assembles a ProviderDTO from a given Provider Entity
	 * 
	 * @param Provider
	 *            , the entity to read
	 * @return ProviderDTO The assembled Provider Data Transfer Object
	 **/
	ProviderDTO assembleProviderDTO(Provider provider);

	/**
	 * This method assembles a Provider from a given ProviderDTO
	 * 
	 * @param ProviderDTO
	 *            , the data transfer object to read
	 * @return Provider The assembled Provider Entity
	 **/
	Provider assembleProvider(ProviderDTO providerDTO);

	/**
	 * This method assembles a AddressDTO from a given Address Entity
	 * 
	 * @param Address
	 *            , the entity
	 * @return AddressDTO, the assembled Data Transfer Object
	 **/
	AddressDTO assembleAddressDTO(Address address);

	/**
	 * This method assembles a Address from a given AddressDTO
	 * 
	 * @param AddressDTO
	 *            , the data transfer object to read
	 * @return Address, the assembled Address entity
	 **/
	Address assembleAddress(AddressDTO addressDTO);

	/**
	 * This method assembles a AddressLogDTO from a given AddressLog Entity
	 * 
	 * @param AddressLog
	 *            , the entity
	 * @return AddressLogDTO, the assembled Data Transfer Object
	 **/
	AddressLogDTO assembleAddressLogDTO(AddressLog addressLog);

	/**
	 * This method assembles a AddressLog from a given AddressLogDTO
	 * 
	 * @param AddressLogDTO
	 *            , the data transfer object to read
	 * @return AddressLog, the assembled AddressLog entity
	 **/
	AddressLog assembleAddressLog(AddressLogDTO addressDTOLog);

	/**
	 * This method assembles a UserDTO from a given PorsUser
	 * 
	 * @param PorsUser
	 *            , the entity
	 * @return UserDTO, the assembled data transfer object
	 **/
	LocalIdDTO assembleLocalIdDTO(LocalId localid);

	/**
	 * This method assembles a LocalId from a given LocalIdDTO
	 * 
	 * @param LocalId
	 *            , the entity
	 * @return localId, the assembled data transfer object
	 **/
	LocalId assembleLocalId(LocalIdDTO localIdDTO);

	/**
	 * This method assembles a LocalIdLog from a given LocalIdLogDTO
	 * 
	 * @param LocalIdLog
	 *            , the entity
	 * @return localIdLogDTO, the assembled data transfer object
	 **/
	LocalIdLogDTO assembleLocalIDLogDTO(LocalIdLog localid);

	/**
	 * This method assembles a LocalIdLoc from a given LocalIdLocDTO
	 * 
	 * @param LocalIdLogDTO
	 *            , the data transfer object to read
	 * @return localIdLoc, the assembled data transfer object
	 **/
	LocalIdLog assembleLocalIdLog(LocalIdLogDTO localIdDTO);

	/**
	 * This method assembles a localIdDTO from a given localId
	 * 
	 * @param localIdDTO
	 *            , the data transfer object to read
	 * @return localId, the assembled LocalIdLog entity
	 **/
	UserDTO assembleUserDTO(PorsUser user);

	/**
	 * This method assembles a PorsUser from a given UserDTO
	 * 
	 * @param UserDTO
	 *            , the data transfer object to read
	 * @return PorsUser, the assembled User entity
	 **/
	PorsUser assemblePorsUser(UserDTO userDTO);

	/**
	 * This method assembles a LoggingEntryDTO from a given History entity
	 * 
	 * @param LoggingEntry
	 *            , the entity
	 * @return LoggingEntryDTO, the assembled data transfer object
	 **/
	LoggingEntryDTO assembleLoggingEntryDTO(History loggingEntry);

	/**
	 * <p>
	 * This method assembles a LoggingEntryDRO from a given UserHostory entity.
	 * </p>
	 * 
	 * @param loggingEntry
	 * @return
	 */
	LoggingEntryDTO assembleLoggingEntryDTO(UserHistory loggingEntry);

	/**
	 * This method assembles a LoggingEntry from a given LoggingEntryDTO
	 * 
	 * @param LoggingEntryDTO
	 *            , the data transfer object to read
	 * @return LoggingEntry, the assembled Logging entity
	 **/
	History assembleLoggingEntry(LoggingEntryDTO loggingEntryDto);

	/**
	 * This method assembles a ProviderLogDTO from a given ProviderLog entity
	 * 
	 * @param ProviderLog
	 *            , the entity
	 * @return ProviderLogDTO, the assembled data transfer object
	 **/
	ProviderLogDTO assembleProviderLogDTO(ProviderLog providerLog);

	/**
	 * This method assembles a OrganisationDTO from a given Organisation entity
	 * 
	 * @param Organisation
	 *            , the entity
	 * @return OrganisationDTO, the assembled data transfer object
	 **/
	OrganisationDTO assembleOrganisationDTO(Organisation organisation);

	/**
	 * This method assembles a Organsiation from a given OrganisationDTO
	 * 
	 * @param OrganisationDTO
	 *            , the data transfer object
	 * @return Organsiation, the assembled entity
	 **/
	Organisation assembleOrganisation(OrganisationDTO organisationDTO);

	/**
	 * Assembles a new data transfer object from an OrganisationLog Object.
	 * 
	 * @param organisationLog
	 *            the entity
	 * @return the OrganisationLogDTO
	 */
	OrganisationLogDTO assembleOrganisationLogDTO(
			OrganisationLog organisationLog);

	/**
	 * Assembles a permission data transfer object from a list of permissions.
	 * 
	 * @param permissions
	 *            the permission list
	 * @return the permission dto
	 */
	PermissionDTO assemblePersmissionDTO(List<Permission> permissions);

	/**
	 * Validates a provider object
	 * 
	 * @param provider
	 *            the provider to validate
	 * @throws MissingFieldsException
	 *             if required fields are missing
	 * @throws WrongValueException
	 *             if the value is not ok
	 */
	void validateProvider(Provider provider)
			throws MissingFieldsException, WrongValueException;

	/**
	 * Validates an address object
	 * 
	 * @param address
	 *            the address to validate
	 * @throws MissingFieldsException
	 *             if required fields are missing
	 * @throws WrongValueException
	 *             if the value is not ok
	 */
	void validateAddress(Address address) throws MissingFieldsException,
			WrongValueException;

	/**
	 * Validates an organisation object
	 * 
	 * @param organisation
	 *            the organisation to validate
	 * @throws MissingFieldsException
	 *             if required fields are missing
	 * @throws WrongValueException
	 *             if the value is not ok
	 */
	void validateOrganisation(Organisation organisation)
			throws MissingFieldsException, WrongValueException;

	/**
	 * Validates an localId object
	 * 
	 * @param localId
	 *            the localId to validate
	 * @throws MissingFieldsException
	 *             if required fields are missing
	 * @throws WrongValueException
	 *             if the value is not ok
	 */
	void validateLocalId(LocalId localId) throws MissingFieldsException,
			WrongValueException;

	/**
	 * This method assembles a ProviderHasAddressLogDTO from a given
	 * ProviderHasAddressLog
	 * 
	 * @param ProviderHasAddressLog
	 *            , the entity to read
	 * @return ProviderHasAddressLogDTO, the assembled data transfer object
	 **/
	ProviderHasAddressLogDTO assembleProviderHasAddressLogDTO(
			ProviderHasAddressLog phaLog);

	/**
	 * This method assembles a OrganisationHasAddressLogDTO from a given
	 * OrganisationHasAddressLog
	 * 
	 * @param OrganisationHasAddressLog
	 *            , the entity to read
	 * @return OrganisationHasAddressLogDTO, the assembled data transfer object
	 **/
	OrganisationHasAddressLogDTO assembleOrganisationHasAddressLogDTO(
			OrganisationHasAddressLog orgLog);

	/**
	 * This method assembles a OrganisationHasProviderLogDTO from a given
	 * OrganisationHasProviderLog
	 * 
	 * @param OrganisationHasProviderLog
	 *            , the entity to read
	 * @return OrganisationHasProviderLogDTO, the assembled data transfer object
	 **/
	OrganisationHasProviderLogDTO assembleOrganisationHasProviderLogDTO(
			OrganisationHasProviderLog ohpLog);

	/**
	 * This method assembles a SearchCriteria from a given SearchCriteriaDTO
	 * 
	 * @param SearchCriteriaDTO
	 *            , the data transfer object
	 * @return SearchCriteria, the assembled entity
	 **/
	SearchCriteria assembleSearchCriteria(SearchCriteriaDTO searchDTO);

	/**
	 * This method assembles a SearchCriteriaDTO from a given SearchCriteria
	 * 
	 * @param SearchCriteria
	 *            , the entity to read
	 * @return SearchCriteriaDTO, the assembled data transfer object
	 **/
	SearchCriteriaDTO assembleSearchCriteriaDTO(SearchCriteria search);

	/**
	 * This method assembles a DuplicateEntry from a given DuplicateEntryDTO
	 * 
	 * @param DuplicateEntryDTO
	 *            , the data transfer object
	 * @return DuplicateEntry, the assembled entity
	 **/
	DuplicateEntry assembleDuplicateEntry(DuplicateEntryDTO entryDTO);

	/**
	 * This method assembles a DuplicateEntryDTO from a given DuplicateEntry
	 * 
	 * @param DuplicateEntry
	 *            , the entity to read
	 * @return DuplicateEntryDTO, the assembled data transfer object
	 **/
	DuplicateEntryDTO assembleDuplicateEntryDTO(DuplicateEntry entry);

	/**
	 * This method assembles a ImportResultDTO from a given ImportResult
	 * 
	 * @param ImportResult
	 *            , the entity to read
	 * @return ImportResultDTO, the assembled data transfer object
	 **/
	ImportResultDTO assembleImportResultDTO(ImportResult result);

	/**
	 * This method assembles a ImportResult from a given ImportResultDTO
	 * 
	 * @param ImportResultDTO
	 *            , the data transfer object
	 * @return ImportResult, the assembled entity
	 **/
	ImportResult assembleImportResult(ImportResultDTO dto);

	/**
	 * This method assembles a DuplicateConfigurationDTO from a given
	 * DuplicateRecognition
	 * 
	 * @param List
	 *            <DuplicateRecognition> , the entities to read
	 * @return DuplicateConfigurationDTO, the assembled data transfer object
	 **/
	DuplicateConfigurationDTO assembleDuplicateConfigurationDTO(
			List<DuplicateRecognition> drlist);

	/**
	 * This method assembles a List of DuplicateRecognition from a given
	 * DuplicateConfigurationDTO
	 * 
	 * @param DuplicateConfigurationDTO
	 *            , the data transfer object
	 * @return List<DuplicateRecognition>, the assembled entities
	 **/
	List<DuplicateRecognition> assembleDuplicateConfiguration(
			DuplicateConfigurationDTO dto);

	/**
	 * This method assembles a DuplicateDetailDTO from two given Providers
	 * 
	 * @param Provider
	 *            , Provider , the entities to read
	 * @return DuplicateDetailDTO, the assembled data transfer object
	 **/
	DuplicateDetailDTO assembleDuplicateDetailDTO(Provider p1,
			Provider p2);

	/**
	 * This method assembles a DuplicateDetailDTO from two given Addresses
	 * 
	 * @param Address
	 *            , Address , the entities to read
	 * @return DuplicateDetailDTO, the assembled data transfer object
	 **/
	DuplicateDetailDTO assembleDuplicateDetailDTO(Address a1, Address a2);

	/**
	 * This method assembles a DuplicateDetailDTO from two given Organisations
	 * 
	 * @param Organisation
	 *            , Organisation , the entities to read
	 * @return DuplicateDetailDTO, the assembled data transfer object
	 **/
	DuplicateDetailDTO assembleDuplicateDetailDTO(Organisation o1,
			Organisation o2);

	/**
	 * Validates an user object
	 * 
	 * @param user
	 *            the user to validate
	 * @throws MissingFieldsException
	 *             if required fields are missing
	 * @throws WrongValueException
	 *             if the value is not ok
	 */
	void validateUser(PorsUser newUser) throws MissingFieldsException,
			WrongValueException;

	/**
	 * This method assembles a UserRoleDTO from a given UserRole
	 * 
	 * @param User
	 *            Role, the entity
	 * @return UserRoleDTO, the assembled Data Transfer Object
	 **/
	UserRoleDTO assembleRoleDTO(UserRole role);

	/**
	 * This method assembles a UserRole from a given UserRoleDTO
	 * 
	 * @param UserRoleDTO
	 *            , the data transfer object to read
	 * @return UserRole, the assembled role entity
	 **/
	UserRole assembleRole(UserRoleDTO roleDTO);

}
