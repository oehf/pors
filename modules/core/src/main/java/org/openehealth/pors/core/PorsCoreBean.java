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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.validator.EmailValidator;
import org.openehealth.pors.core.common.Permission;
import org.openehealth.pors.core.common.Task;
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
import org.openehealth.pors.database.entities.DuplicateEntryId;
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
 * Implementation of IPorsCore
 * 
 * @author mf, tb
 * 
 * @see IPorsCore
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PorsCoreBean implements IPorsCore {

	/**
	 * @see IPorsCore#assembleDuplicateConfiguration(DuplicateConfigurationDTO)
	 */
	public List<DuplicateRecognition> assembleDuplicateConfiguration(
			DuplicateConfigurationDTO dto) {
		List<DuplicateRecognition> drlist = new ArrayList<DuplicateRecognition>();
		drlist.add(new DuplicateRecognition("p.address", dto
				.getProviderAddressWeight()));
		drlist.add(new DuplicateRecognition("p.firstname", dto
				.getProviderFirstnameWeight()));
		drlist.add(new DuplicateRecognition("p.lastname", dto
				.getProviderLastnameWeight()));
		drlist.add(new DuplicateRecognition("p.middlename", dto
				.getProviderMiddlenameWeight()));
		drlist.add(new DuplicateRecognition("p.nameprefix", dto
				.getProviderNameprefixWeight()));
		drlist.add(new DuplicateRecognition("p.namesuffix", dto
				.getProviderNamesuffixWeight()));
		drlist.add(new DuplicateRecognition("p.email", dto
				.getProviderEmailWeight()));
		drlist.add(new DuplicateRecognition("p.fax", dto.getProviderFaxWeight()));
		drlist.add(new DuplicateRecognition("p.telephone", dto
				.getProviderTelephoneWeight()));
		drlist.add(new DuplicateRecognition("p.specialisation", dto
				.getProviderSpecialisationWeight()));
		drlist.add(new DuplicateRecognition("p.oid", dto.getProviderOIDWeight()));
		drlist.add(new DuplicateRecognition("p.birthday", dto
				.getProviderBirthdayWeight()));
		drlist.add(new DuplicateRecognition("p.gendercode", dto
				.getProviderGenderCodeWeight()));

		drlist.add(new DuplicateRecognition("o.name", dto
				.getOrganisationNameWeight()));
		drlist.add(new DuplicateRecognition("o.secondname", dto
				.getOrganisationSecondnameWeight()));
		drlist.add(new DuplicateRecognition("o.telephone", dto
				.getOrganisationTelephoneWeight()));
		drlist.add(new DuplicateRecognition("o.fax", dto
				.getOrganisationFaxWeight()));
		drlist.add(new DuplicateRecognition("o.email", dto
				.getOrganisationEmailWeight()));
		drlist.add(new DuplicateRecognition("o.address", dto
				.getOrganisationAddressWeight()));

		drlist.add(new DuplicateRecognition("a.street", dto
				.getAddressStreetWeight()));
		drlist.add(new DuplicateRecognition("a.housenumber", dto
				.getAddressHousenumberWeight()));
		drlist.add(new DuplicateRecognition("a.zipcode", dto
				.getAddressZipCodeWeight()));
		drlist.add(new DuplicateRecognition("a.state", dto
				.getAddressStateWeight()));
		drlist.add(new DuplicateRecognition("a.country", dto
				.getAddressCountryWeight()));
		drlist.add(new DuplicateRecognition("a.city", dto
				.getAddressCityWeight()));

		drlist.add(new DuplicateRecognition("timer.hour", dto.getTimerHour()));
		drlist.add(new DuplicateRecognition("timer.minutes", dto
				.getTimerMinute()));
		drlist.add(new DuplicateRecognition("timer.seconds", dto
				.getTimerSecond()));

		drlist.add(new DuplicateRecognition("lowerthreshold", dto
				.getLowerthreshold()));
		drlist.add(new DuplicateRecognition("upperthreshold", dto
				.getUpperthreshold()));

		return drlist;
	}

	/**
	 * @see IPorsCore#assembleDuplicateConfigurationDTO()
	 */
	public DuplicateConfigurationDTO assembleDuplicateConfigurationDTO(
			List<DuplicateRecognition> drlist) {
		DuplicateConfigurationDTO dto = new DuplicateConfigurationDTO();
		for (DuplicateRecognition item : drlist) {
			if (item.getName().equalsIgnoreCase("p.address")) {
				dto.setProviderAddressWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.birthday")) {
				dto.setProviderBirthdayWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.email")) {
				dto.setProviderEmailWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.fax")) {
				dto.setProviderFaxWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.firstname")) {
				dto.setProviderFirstnameWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.gendercode")) {
				dto.setProviderGenderCodeWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.lastname")) {
				dto.setProviderLastnameWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.middlename")) {
				dto.setProviderMiddlenameWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.nameprefix")) {
				dto.setProviderNameprefixWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.namesuffix")) {
				dto.setProviderNamesuffixWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.oid")) {
				dto.setProviderOIDWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.specialisation")) {
				dto.setProviderSpecialisationWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("p.telephone")) {
				dto.setProviderTelephoneWeight(item.getValue());
				continue;
			}

			if (item.getName().equalsIgnoreCase("o.address")) {
				dto.setOrganisationAddressWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("o.email")) {
				dto.setOrganisationEmailWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("o.fax")) {
				dto.setOrganisationFaxWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("o.name")) {
				dto.setOrganisationNameWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("o.secondname")) {
				dto.setOrganisationSecondnameWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("o.telephone")) {
				dto.setOrganisationTelephoneWeight(item.getValue());
				continue;
			}

			if (item.getName().equalsIgnoreCase("a.city")) {
				dto.setAddressCityWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("a.country")) {
				dto.setAddressCountryWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("a.housenumber")) {
				dto.setAddressHousenumberWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("a.state")) {
				dto.setAddressStateWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("a.street")) {
				dto.setAddressStreetWeight(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("a.zipcode")) {
				dto.setAddressZipCodeWeight(item.getValue());
				continue;
			}

			if (item.getName().equalsIgnoreCase("timer.hour")) {
				dto.setTimerHour(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("timer.minute")) {
				dto.setTimerMinute(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("timer.seconds")) {
				dto.setTimerSecond(item.getValue());
				continue;
			}

			if (item.getName().equalsIgnoreCase("lowerthreshold")) {
				dto.setLowerthreshold(item.getValue());
				continue;
			}
			if (item.getName().equalsIgnoreCase("upperthreshold")) {
				dto.setUpperthreshold(item.getValue());
				continue;
			}
		}
		return dto;
	}

	/**
	 * @see IPorsCore#assembleProviderDTO(Provider)
	 */
	public ProviderDTO assembleProviderDTO(Provider provider) {
		ProviderDTO dto = new ProviderDTO();
		dto.setId(provider.getId());
		dto.setOid(provider.getOid());
		dto.setLanr(provider.getLanr());
		dto.setFirstname(provider.getFirstName());
		dto.setLastname(provider.getLastName());
		dto.setMiddleName(provider.getMiddleName());
		dto.setNamePrefix(provider.getNamePrefix());
		dto.setNameSuffix(provider.getNameSuffix());
		dto.setGenderCode(provider.getGenderCode());
		dto.setSpecialisation(provider.getSpecialisation());
		dto.setBirthday(provider.getBirthday());
		dto.setVersion(provider.getVersion());
		dto.setEmail(provider.getEmail());
		dto.setTelephone(provider.getTelephone());
		dto.setFax(provider.getFax());
		dto.setDeactivationDate(provider.getDeactivationDate());
		dto.setDeactivationReasonCode(provider.getDeactivationReasonCode());
		dto.setReactivationDate(provider.getReactivationDate());
		dto.setReactivationReasonCode(provider.getReactivationReasonCode());
		dto.setLastUpdateDate(provider.getLastUpdateDate());
		dto.setIp(provider.getIpAddress());
		if (provider.getUser() != null) {
			dto.setPorsuserid(provider.getUser().getId());
		}
		if (provider.getEditingUser() != null) {
			dto.setEditingUserID(provider.getEditingUser().getId());
		}
		dto.setSessionId(provider.getSessionId());

		if (provider.getAddresses() != null) {
			dto.setAddresses(new ArrayList<AddressDTO>());
			for (Address address : provider.getAddresses()) {
				dto.getAddresses().add(assembleAddressDTO(address));
			}
		}

		if (provider.getOrganisations() != null) {
			dto.setOrganisations(new ArrayList<OrganisationDTO>());
			for (Organisation organisation : provider.getOrganisations()) {
				dto.getOrganisations().add(
						assembleOrganisationDTO(organisation));
			}
		}

		if (provider.getLocalIds() != null) {
			dto.setLocalId(new ArrayList<LocalIdDTO>());
			for (LocalId localId : provider.getLocalIds()) {
				dto.getLocalId().add(assembleLocalIdDTO(localId));
			}
		}

		return dto;
	}

	/**
	 * @see IPorsCore#assembleProvider(ProviderDTO)
	 */
	public Provider assembleProvider(ProviderDTO providerDTO) {
		Provider provider = new Provider();
		provider.setId(providerDTO.getId());
		provider.setLanr(providerDTO.getLanr());
		provider.setOid(providerDTO.getOid());
		provider.setFirstName(providerDTO.getFirstname());
		provider.setLastName(providerDTO.getLastname());
		provider.setMiddleName(providerDTO.getMiddleName());
		provider.setNamePrefix(providerDTO.getNamePrefix());
		provider.setNameSuffix(providerDTO.getNameSuffix());
		provider.setGenderCode(providerDTO.getGenderCode());
		provider.setSpecialisation(providerDTO.getSpecialisation());
		provider.setBirthday(providerDTO.getBirthday());
		provider.setVersion(providerDTO.getVersion());
		provider.setEmail(providerDTO.getEmail());
		provider.setTelephone(providerDTO.getTelephone());
		provider.setFax(providerDTO.getFax());
		provider.setDeactivationDate(providerDTO.getDeactivationDate());
		provider.setDeactivationReasonCode(providerDTO
				.getDeactivationReasonCode());
		provider.setReactivationDate(providerDTO.getReactivationDate());
		provider.setReactivationReasonCode(providerDTO
				.getReactivationReasonCode());
		provider.setLastUpdateDate(providerDTO.getLastUpdateDate());
		PorsUser editingUser = new PorsUser();
		editingUser.setId(providerDTO.getEditingUserID());
		PorsUser user = new PorsUser();
		user.setId(providerDTO.getPorsuserid());
		provider.setUser(user);
		provider.setEditingUser(editingUser);
		provider.setSessionId(providerDTO.getSessionId());
		provider.setIpAddress(providerDTO.getIp());

		/*
		 * todo: user.setId(providerDTO.getPorsuserid());
		 * provider.setUser(user);
		 */

		if (providerDTO.getAddresses() != null) {
			provider.setAddresses(new ArrayList<Address>());
			for (AddressDTO addressDto : providerDTO.getAddresses()) {
				provider.getAddresses().add(assembleAddress(addressDto));
			}
		}
		if (providerDTO.getOrganisations() != null) {
			provider.setOrganisations(new ArrayList<Organisation>());
			for (OrganisationDTO organisationDTO : providerDTO
					.getOrganisations()) {
				provider.getOrganisations().add(
						assembleOrganisation(organisationDTO));
			}
		}
		if (providerDTO.getLocalId() != null) {
			provider.setLocalIds(new ArrayList<LocalId>());
			for (LocalIdDTO localIdDto : providerDTO.getLocalId()) {
				provider.getLocalIds().add(assembleLocalId(localIdDto));
			}
		}

		return provider;
	}

	/**
	 * @see IPorsCore#assembleUserDTO(PorsUser)
	 */
	public UserDTO assembleUserDTO(PorsUser user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getName());
		userDTO.setPassword(user.getPassword());
		userDTO.setActive(user.isActive());
		if (user.getRole() != null) {
			userDTO.setRole(assembleRoleDTO(user.getRole()));
		}
		return userDTO;
	}

	/**
	 * @see IPorsCore#assemblePorsUser(UserDTO)
	 */
	public PorsUser assemblePorsUser(UserDTO userDTO) {
		PorsUser user = new PorsUser();
		user.setId(userDTO.getId());
		user.setName(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setActive(userDTO.isActive());
		if (userDTO.getRole() != null) {
			user.setRole(assembleRole(userDTO.getRole()));
		}
		return user;
	}

	/**
	 * @see IPorsCore#assembleRoleDTO(UserRole)
	 */
	public UserRoleDTO assembleRoleDTO(UserRole role) {
		UserRoleDTO roleDTO = new UserRoleDTO();
		roleDTO.setDescription(role.getDescription());
		roleDTO.setId(role.getId());
		roleDTO.setName(role.getName());
		return roleDTO;
	}

	/**
	 * @see IPorsCore#assembleRole(UserRoleDTO)
	 */
	public UserRole assembleRole(UserRoleDTO roleDTO) {
		UserRole role = new UserRole();
		role.setDescription(roleDTO.getDescription());
		role.setId(roleDTO.getId());
		role.setName(roleDTO.getName());
		return role;
	}

	/**
	 * @see IPorsCore#assembleLoggingEntry(LoggingEntryDTO)
	 */
	public History assembleLoggingEntry(LoggingEntryDTO loggingEntryDto) {
		History loggingEntry = new History();
		loggingEntry.setUserId(loggingEntryDto.getPorsUserId());
		loggingEntry.setUserName(loggingEntryDto.getUserName());
		loggingEntry.setLogTime(loggingEntryDto.getLogTime());
		loggingEntry.setDomain(loggingEntryDto.getDomain());
		loggingEntry.setAction(loggingEntryDto.getAction());
		loggingEntry.setLogId(loggingEntryDto.getLogEntryId());

		return loggingEntry;
	}

	/**
	 * @see IPorsCore#assembleLoggingEntryDTO(History)
	 */
	public LoggingEntryDTO assembleLoggingEntryDTO(History loggingEntry) {
		LoggingEntryDTO loggingEntryDto = new LoggingEntryDTO();
		loggingEntryDto.setPorsUserId(loggingEntry.getUserId());
		loggingEntryDto.setUserName(loggingEntry.getUserName());
		loggingEntryDto.setLogTime(loggingEntry.getLogTime());
		loggingEntryDto.setLogDateString(DateFormat.getDateInstance(
				DateFormat.MEDIUM).format(loggingEntry.getLogTime()));

		DateFormat dfmt = new SimpleDateFormat("HH:mm:ss:SS");

		loggingEntryDto
				.setLogTimeString(dfmt.format(loggingEntry.getLogTime()));
		loggingEntryDto.setDomain(loggingEntry.getDomain());
		loggingEntryDto.setAction(loggingEntry.getAction());
		loggingEntryDto.setLogEntryId(loggingEntry.getLogId());

		return loggingEntryDto;
	}

	/**
	 * @see IPorsCore#assembleDuplicateEntry(DuplicateEntryDTO)
	 */
	public DuplicateEntry assembleDuplicateEntry(DuplicateEntryDTO duplicateDto) {
		DuplicateEntry duplicate = new DuplicateEntry();
		duplicate.setTimestamp(duplicateDto.getLogTime());
		duplicate.setValue(Double.valueOf(duplicateDto.getPercentage().replace(
				',', '.')));
		DuplicateEntryId entryId = new DuplicateEntryId();
		entryId.setDomain(duplicateDto.getDomain());
		entryId.setId1(duplicateDto.getId1());
		entryId.setId2(duplicateDto.getId2());
		duplicate.setId(entryId);
		return duplicate;
	}

	/**
	 * @see IPorsCore#assembleDuplicateEntryDTO(DuplicateEntry)
	 */
	public DuplicateEntryDTO assembleDuplicateEntryDTO(DuplicateEntry duplicate) {
		DuplicateEntryDTO duplicateDTO = new DuplicateEntryDTO();
		duplicateDTO.setDomain(duplicate.getId().getDomain());
		duplicateDTO.setLogTime(duplicate.getTimestamp());
		duplicateDTO.setLogDateString(DateFormat.getDateInstance(
				DateFormat.MEDIUM).format(duplicate.getTimestamp()));
		DateFormat dfmt = new SimpleDateFormat("HH:mm:ss:SS");

		duplicateDTO.setLogTimeString(dfmt.format(duplicate.getTimestamp()));
		NumberFormat formatter = new DecimalFormat("#0.00");
		duplicateDTO
				.setPercentage(formatter.format(duplicate.getValue() * 100.0));
		duplicateDTO.setId1(duplicate.getId().getId1());
		duplicateDTO.setId2(duplicate.getId().getId2());
		return duplicateDTO;
	}

	/**
	 * @see IPorsCore#assembleProviderLogDTO(ProviderLog)
	 */
	public ProviderLogDTO assembleProviderLogDTO(ProviderLog providerLog) {
		ProviderLogDTO providerLogDTO = new ProviderLogDTO();

		providerLogDTO.setId(providerLog.getId());
		providerLogDTO.setLogTime(providerLog.getLogTime());
		providerLogDTO.setNewBirthday(providerLog.getNewBirthday());
		providerLogDTO.setNewDeactivationDate(providerLog
				.getNewDeactivationDate());
		providerLogDTO.setNewDeactivationReasonCode(providerLog
				.getNewDeactivationReasonCode());
		providerLogDTO.setNewEmail(providerLog.getNewEmail());
		providerLogDTO.setNewFax(providerLog.getNewFax());
		providerLogDTO.setNewFirstName(providerLog.getNewFirstName());
		providerLogDTO.setOldSpecialisation(providerLog.getOldSpecialisation());
		providerLogDTO.setNewSpecialisation(providerLog.getNewSpecialisation());
		providerLogDTO.setNewGenderCode(providerLog.getNewGenderCode());
		providerLogDTO.setNewLanr(providerLog.getNewLanr());
		providerLogDTO.setNewLastName(providerLog.getNewLastName());
		providerLogDTO.setNewLastUpdateDate(providerLog.getNewLastUpdateDate());
		providerLogDTO.setNewMiddleName(providerLog.getNewMiddleName());
		providerLogDTO.setNewNamePrefix(providerLog.getNewNamePrefix());
		providerLogDTO.setNewNameSuffix(providerLog.getNewNameSuffix());
		providerLogDTO.setNewOid(providerLog.getNewOid());
		providerLogDTO.setNewReactivationDate(providerLog
				.getNewReactivationDate());
		providerLogDTO.setNewReactivationReasonCode(providerLog
				.getNewReactivationReasonCode());
		providerLogDTO.setNewTelephone(providerLog.getNewTelephone());
		providerLogDTO.setOldBirthday(providerLog.getOldBirthday());
		providerLogDTO.setOldDeactivationDate(providerLog
				.getOldDeactivationDate());
		providerLogDTO.setOldDeactivationReasonCode(providerLog
				.getOldDeactivationReasonCode());
		providerLogDTO.setOldEmail(providerLog.getOldEmail());
		providerLogDTO.setOldFax(providerLog.getOldFax());
		providerLogDTO.setOldFirstName(providerLog.getOldFirstName());
		providerLogDTO.setOldGenderCode(providerLog.getOldGenderCode());
		providerLogDTO.setOldLanr(providerLog.getOldLanr());
		providerLogDTO.setOldLastName(providerLog.getOldLastName());
		providerLogDTO.setOldLastUpdateDate(providerLog.getOldLastUpdateDate());
		providerLogDTO.setOldMiddleName(providerLog.getOldMiddleName());
		providerLogDTO.setOldNamePrefix(providerLog.getOldNamePrefix());
		providerLogDTO.setOldNameSuffix(providerLog.getOldNameSuffix());
		providerLogDTO.setOldOid(providerLog.getOldOid());
		providerLogDTO.setOldReactivationDate(providerLog
				.getOldReactivationDate());
		providerLogDTO.setOldReactivationReasonCode(providerLog
				.getOldReactivationReasonCode());
		providerLogDTO.setOldTelephone(providerLog.getOldTelephone());
		providerLogDTO.setRegionalProviderId(providerLog
				.getRegionalProviderId());
		providerLogDTO.setSessionId(providerLog.getSessionId());
		providerLogDTO.setIPAddress(providerLog.getIPAddress());
		providerLogDTO.setTableName(providerLog.getTableName());
		providerLogDTO.setTriggerType(providerLog.getTriggerType());

		return providerLogDTO;
	}

	/**
	 * @see IPorsCore#assembleAddressDTO(Address)
	 */
	public AddressDTO assembleAddressDTO(Address address) {
		AddressDTO dto = new AddressDTO();
		dto.setId(address.getId());
		dto.setAdditional(address.getAdditional());
		dto.setCity(address.getCity());
		dto.setCountry(address.getCountry());
		dto.setHouseNumber(address.getHouseNumber());
		dto.setStreet(address.getStreet());
		dto.setZipCode(address.getZipCode());
		dto.setState(address.getState());
		return dto;
	}

	/**
	 * @see IPorsCore#assembleAddress(AddressDTO)
	 */
	public Address assembleAddress(AddressDTO addressDTO) {
		Address address = new Address();
		address.setId(addressDTO.getId());
		address.setAdditional(addressDTO.getAdditional());
		address.setCity(addressDTO.getCity());
		address.setCountry(addressDTO.getCountry());
		address.setHouseNumber(addressDTO.getHouseNumber());
		address.setStreet(addressDTO.getStreet());
		address.setZipCode(addressDTO.getZipCode());
		address.setState(addressDTO.getState());
		return address;
	}

	/**
	 * @see IPorsCore#assembleAddressLogDTO(AddressLog)
	 */
	public AddressLogDTO assembleAddressLogDTO(AddressLog addresslog) {
		AddressLogDTO dto = new AddressLogDTO();
		dto.setId(addresslog.getId());
		dto.setPorsuserID(addresslog.getUser().getId());
		dto.setSessionId(addresslog.getSessionId());
		dto.setIPAddress(addresslog.getIPAddress());
		dto.setLogTime(addresslog.getLogTime());
		dto.setTriggerType(addresslog.getTriggerType());
		dto.setTableName(addresslog.getTableName());
		dto.setAddressId(addresslog.getAddressId());

		dto.setNewAdditional(addresslog.getNewAdditional());
		dto.setNewCity(addresslog.getNewCity());
		dto.setNewCountry(addresslog.getNewCountry());
		dto.setNewHouseNumber(addresslog.getNewHouseNumber());
		dto.setNewState(addresslog.getNewState());
		dto.setNewStreet(addresslog.getNewStreet());
		dto.setNewZipCode(addresslog.getNewZipCode());

		dto.setOldAdditional(addresslog.getOldAdditional());
		dto.setOldCity(addresslog.getOldCity());
		dto.setOldCountry(addresslog.getOldCountry());
		dto.setOldHouseNumber(addresslog.getOldHouseNumber());
		dto.setOldState(addresslog.getOldState());
		dto.setOldStreet(addresslog.getOldStreet());
		dto.setOldZipCode(addresslog.getOldZipCode());

		return dto;
	}

	/**
	 * @see IPorsCore#assembleAddressLog(AddressDTOLog)
	 */
	public AddressLog assembleAddressLog(AddressLogDTO addressLogDTO) {
		AddressLog addressLog = new AddressLog();
		addressLog.setId(addressLogDTO.getId());
		addressLog.setAddressId(addressLogDTO.getAddressId());
		PorsUser user = new PorsUser();
		user.setId(addressLogDTO.getPorsuserID());
		addressLog.setUser(user);
		addressLog.setSessionId(addressLogDTO.getSessionId());
		addressLog.setIPAddress(addressLogDTO.getIPAddress());
		addressLog.setLogTime(addressLogDTO.getLogTime());
		addressLog.setTriggerType(addressLogDTO.getTriggerType());
		addressLog.setTableName(addressLogDTO.getTableName());

		addressLog.setNewAdditional(addressLogDTO.getNewAdditional());
		addressLog.setNewCity(addressLogDTO.getNewCity());
		addressLog.setNewCountry(addressLogDTO.getNewCountry());
		addressLog.setNewHouseNumber(addressLogDTO.getNewHouseNumber());
		addressLog.setNewState(addressLogDTO.getNewState());
		addressLog.setNewStreet(addressLogDTO.getNewStreet());
		addressLog.setNewZipCode(addressLogDTO.getNewZipCode());

		addressLog.setOldAdditional(addressLogDTO.getOldAdditional());
		addressLog.setOldCity(addressLogDTO.getOldCity());
		addressLog.setOldCountry(addressLogDTO.getOldCountry());
		addressLog.setOldHouseNumber(addressLogDTO.getOldHouseNumber());
		addressLog.setOldState(addressLogDTO.getOldState());
		addressLog.setOldStreet(addressLogDTO.getOldStreet());
		addressLog.setOldZipCode(addressLogDTO.getOldZipCode());
		return addressLog;
	}

	/**
	 * @see IPorsCore#assembleLocalIdDTO(localid)
	 */
	public LocalIdDTO assembleLocalIdDTO(LocalId localid) {
		LocalIdDTO dto = new LocalIdDTO();
		dto.setId(localid.getId());
		dto.setLocalId(localid.getLocalId());
		dto.setFacility(localid.getFacility());
		dto.setApplication(localid.getApplication());
		if (localid.getOrganisation() != null) {
			dto.setOrganisationId(localid.getOrganisation().getId());
			dto.setOrganisationName(localid.getOrganisation().getName());
		}
		if (localid.getProvider() != null) {
			dto.setProviderFirstname(localid.getProvider().getFirstName());
			dto.setProviderLastname(localid.getProvider().getLastName());
			dto.setProviderId(localid.getProvider().getId());
		}

		return dto;
	}

	/**
	 * @see IPorsCore#assembleLocalId(LocalIdDTO)
	 */
	public LocalId assembleLocalId(LocalIdDTO localIdDTO) {
		LocalId localid = new LocalId();
		localid.setId(localIdDTO.getId());
		if ((localIdDTO.getOrganisationId() != null && localIdDTO
				.getOrganisationId() != 0)
				|| (localIdDTO.getOrganisationName() != null && localIdDTO
						.getOrganisationName().length() > 0)) {
			Organisation organisation = new Organisation();
			organisation.setId(localIdDTO.getOrganisationId());
			organisation.setName(localIdDTO.getOrganisationName());
			localid.setOrganisation(organisation);
		}
		if ((localIdDTO.getProviderId() != null && localIdDTO.getProviderId() != 0)
				|| (localIdDTO.getProviderFirstname() != null && localIdDTO
						.getProviderFirstname().length() > 0)
				|| (localIdDTO.getProviderLastname() != null && localIdDTO
						.getProviderLastname().length() > 0)) {
			Provider provider = new Provider();
			provider.setId(localIdDTO.getProviderId());
			provider.setFirstName(localIdDTO.getProviderFirstname());
			provider.setLastName(localIdDTO.getProviderLastname());
			localid.setProvider(provider);
		}
		localid.setLocalId(localIdDTO.getLocalId());
		localid.setApplication(localIdDTO.getApplication());
		localid.setFacility(localIdDTO.getFacility());
		return localid;
	}

	/**
	 * @see IPorsCore#assembleLocalIdLogDTO(LocalIdLog)
	 */
	public LocalIdLogDTO assembleLocalIDLogDTO(LocalIdLog localid) {
		LocalIdLogDTO dto = new LocalIdLogDTO();
		dto.setId(localid.getId());
		dto.setAction(localid.getAction());
		dto.setIPAddress(localid.getIPAddress());
		dto.setLogTime(localid.getLogTime());
		dto.setSessionId(localid.getSessionId());
		dto.setPorsuserID(localid.getUser().getId());
		dto.setTableName(localid.getTableName());
		dto.setLocalIdId(localid.getLocalIdId());
		dto.setNewLocalId(localid.getNewLocalId());
		dto.setOldLocalId(localid.getOldLocalId());
		dto.setNewRegionalOrganisationId(localid.getNewRegionalOrganisationId());
		dto.setOldRegionalOrganisationId(localid.getOldRegionalOrganisationId());
		dto.setNewRegionalProviderId(localid.getNewRegionalProviderId());
		dto.setOldRegionalProviderId(localid.getOldRegionalProviderId());
		dto.setOldFacility(localid.getOldFacility());
		dto.setOldApplication(localid.getOldApplication());
		dto.setNewFacility(localid.getNewFacility());
		dto.setNewApplication(localid.getNewApplication());
		return dto;
	}

	/**
	 * @see IPorsCore#assembleLocalIdLog(LocalIdLogDTO)
	 */
	public LocalIdLog assembleLocalIdLog(LocalIdLogDTO localIdDTO) {
		LocalIdLog localIdLog = new LocalIdLog();
		localIdLog.setId(localIdDTO.getId());
		localIdLog.setAction(localIdDTO.getAction());
		localIdLog.setIPAddress(localIdDTO.getIPAddress());
		localIdLog.setLogTime(localIdDTO.getLogTime());
		localIdLog.setSessionId(localIdDTO.getSessionId());
		PorsUser user = new PorsUser();
		user.setId(localIdDTO.getPorsuserID());
		localIdLog.setUser(user);
		localIdLog.setTableName(localIdDTO.getTableName());
		localIdLog.setLocalIdId(localIdDTO.getLocalIdId());
		localIdLog.setNewLocalId(localIdDTO.getNewLocalId());
		localIdLog.setOldLocalId(localIdDTO.getOldLocalId());
		localIdLog.setNewRegionalOrganisationId(localIdDTO
				.getNewRegionalOrganisationId());
		localIdLog.setOldRegionalOrganisationId(localIdDTO
				.getOldRegionalOrganisationId());
		localIdLog.setNewRegionalProviderId(localIdDTO
				.getNewRegionalProviderId());
		localIdLog.setOldRegionalProviderId(localIdDTO
				.getOldRegionalProviderId());
		return localIdLog;
	}

	/**
	 * @see IPorsCore#assembleOrganisationDTO(Organisation)
	 */
	public OrganisationDTO assembleOrganisationDTO(Organisation organisation) {
		OrganisationDTO orga = new OrganisationDTO();
		orga.setId(organisation.getId());
		if (organisation.getUser() != null) {
			orga.setPorsuserID(organisation.getUser().getId());
		}
		orga.setOID(organisation.getOid());
		orga.setEstablishmentID(organisation.getEstablishmentId());
		orga.setName(organisation.getName());
		orga.setSecondName(organisation.getSecondName());
		orga.setDescription(organisation.getDescription());
		orga.setEmail(organisation.getEmail());
		orga.setFax(organisation.getFax());
		orga.setVersion(organisation.getVersion());
		orga.setTelephone(organisation.getTelephone());
		orga.setDeactivationDate(organisation.getDeactivationDate());
		orga.setDeactivationReasonCode(organisation.getDeactivationReasonCode());
		orga.setReactivationDate(organisation.getReactivationDate());
		orga.setReactivationReasonCode(organisation.getReactivationReasonCode());
		orga.setLastUpdateDate(organisation.getLastUpdateDate());
		orga.setIp(organisation.getIpAddress());
		if (organisation.getEditingUser() != null) {
			orga.setEditingUserID(organisation.getEditingUser().getId());
		}
		orga.setSessionId(organisation.getSessionId());

		if (organisation.getAddresses() != null) {
			orga.setAddresses(new ArrayList<AddressDTO>());
			for (Address address : organisation.getAddresses()) {
				orga.getAddresses().add(assembleAddressDTO(address));
			}
		}
		if (organisation.getLocalIds() != null) {
			orga.setLocalId(new ArrayList<LocalIdDTO>());
			for (LocalId localId : organisation.getLocalIds()) {
				orga.getLocalId().add(assembleLocalIdDTO(localId));
			}
		}
		return orga;
	}

	/**
	 * @see IPorsCore#assembleOrganisation(OrganisationDTO)
	 */
	public Organisation assembleOrganisation(OrganisationDTO organisationDTO) {
		Organisation orga = new Organisation();
		PorsUser user = new PorsUser();
		user.setId(organisationDTO.getPorsuserID());
		orga.setId(organisationDTO.getId());
		orga.setUser(user);
		orga.setOid(organisationDTO.getOID());
		orga.setEstablishmentId(organisationDTO.getEstablishmentID());
		orga.setName(organisationDTO.getName());
		orga.setSecondName(organisationDTO.getSecondName());
		orga.setDescription(organisationDTO.getDescription());
		orga.setVersion(organisationDTO.getVersion());
		orga.setEmail(organisationDTO.getEmail());
		orga.setFax(organisationDTO.getFax());
		orga.setTelephone(organisationDTO.getTelephone());
		orga.setDeactivationDate(organisationDTO.getDeactivationDate());
		orga.setDeactivationReasonCode(organisationDTO
				.getDeactivationReasonCode());
		orga.setReactivationDate(organisationDTO.getReactivationDate());
		orga.setReactivationReasonCode(organisationDTO
				.getReactivationReasonCode());
		orga.setLastUpdateDate(organisationDTO.getLastUpdateDate());
		PorsUser editingUser = new PorsUser();
		editingUser.setId(organisationDTO.getEditingUserID());
		orga.setEditingUser(editingUser);
		orga.setSessionId(organisationDTO.getSessionId());
		orga.setIpAddress(organisationDTO.getIp());

		if (organisationDTO.getAddresses() != null) {
			orga.setAddresses(new ArrayList<Address>());
			for (AddressDTO addressDto : organisationDTO.getAddresses()) {
				orga.getAddresses().add(assembleAddress(addressDto));
			}
		}
		if (organisationDTO.getLocalId() != null) {
			orga.setLocalIds(new ArrayList<LocalId>());
			for (LocalIdDTO localIdDto : organisationDTO.getLocalId()) {
				orga.getLocalIds().add(assembleLocalId(localIdDto));
			}
		}
		return orga;
	}

	/**
	 * @see IPorsCore#assembleOrganisationLogDTO(OrganisationLog)
	 */
	public OrganisationLogDTO assembleOrganisationLogDTO(
			OrganisationLog organisationLog) {
		OrganisationLogDTO logDTO = new OrganisationLogDTO();

		logDTO.setId(organisationLog.getId());
		if (organisationLog.getUser() != null) {
			logDTO.setPorsuserID(organisationLog.getUser().getId());
		}
		logDTO.setLogTime(organisationLog.getLogTime());
		logDTO.setSessionId(organisationLog.getSessionId());
		logDTO.setIPAddress(organisationLog.getIPAddress());
		logDTO.setTriggerType(organisationLog.getTriggerType());
		logDTO.setTableName(organisationLog.getTableName());

		logDTO.setOldPorsuserID(organisationLog.getOldPorsUserId());
		logDTO.setOldOID(organisationLog.getOldOid());
		logDTO.setOldEstablishmentID(organisationLog.getOldEstablishmentId());
		logDTO.setOldName(organisationLog.getOldName());
		logDTO.setOldSecondname(organisationLog.getOldSecondName());
		logDTO.setOldDescription(organisationLog.getOldDescription());
		logDTO.setOldEmail(organisationLog.getOldEMail());
		logDTO.setOldTelephone(organisationLog.getOldTelephone());
		logDTO.setOldFax(organisationLog.getOldFax());
		logDTO.setOldDeactivationDate(organisationLog.getOldDeactivationDate());
		logDTO.setOldDeactivationReasonCode(organisationLog
				.getOldDeactivationReasonCode());
		logDTO.setOldReactivationDate(organisationLog.getOldReactivationDate());
		logDTO.setOldReactivationReasonCode(organisationLog
				.getOldReactivationReasonCode());
		logDTO.setOldLastUpdateDate(organisationLog.getOldLastUpdateDate());

		logDTO.setNewPorsuserID(organisationLog.getNewPorsUserId());
		logDTO.setNewOID(organisationLog.getNewOid());
		logDTO.setNewEstablishmentID(organisationLog.getNewEstablishmentId());
		logDTO.setNewName(organisationLog.getNewName());
		logDTO.setNewSecondname(organisationLog.getNewSecondName());
		logDTO.setNewDescription(organisationLog.getNewDescription());
		logDTO.setNewEmail(organisationLog.getNewEMail());
		logDTO.setNewTelephone(organisationLog.getNewTelephone());
		logDTO.setNewFax(organisationLog.getNewFax());
		logDTO.setNewDeactivationDate(organisationLog.getNewDeactivationDate());
		logDTO.setNewDeactivationReasonCode(organisationLog
				.getNewDeactivationReasonCode());
		logDTO.setNewReactivationDate(organisationLog.getNewReactivationDate());
		logDTO.setNewReactivationReasonCode(organisationLog
				.getNewReactivationReasonCode());
		logDTO.setNewLastUpdateDate(organisationLog.getNewLastUpdateDate());

		return logDTO;
	}

	/**
	 * @see IPorsCore#assemblePersmissionDTO(List)
	 */
	public PermissionDTO assemblePersmissionDTO(List<Permission> permissions) {
		PermissionDTO dto = new PermissionDTO();

		for (Permission permission : permissions) {
			switch (permission.getAction()) {
			case Task.CREATE:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setCreateProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setCreateOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setCreateUser(true);
				}
				break;
			case Task.READ:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setReadProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setReadOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setReadUser(true);
				} else if (permission.getDomain() == Task.LOGGING) {
					dto.setReadLogging(true);
				} else if (permission.getDomain() == Task.ADDRESS) {
					dto.setReadAddress(true);
				} else if (permission.getDomain() == Task.DUPLICATES) {
					dto.setReadDuplicate(true);
				}
				break;
			case Task.UPDATE:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setUpdateProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setUpdateOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setUpdateUser(true);
				} else if (permission.getDomain() == Task.ADDRESS) {
					dto.setUpdateAddress(true);
				} else if (permission.getDomain() == Task.DUPLICATES) {
					dto.setUpdateDuplicate(true);
				}
				break;
			case Task.DEACTIVATE:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setDeactivateProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setDeactivateOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setDeactivateUser(true);
				}
				break;
			case Task.REACTIVATE:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setReactivateProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setReactivateOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setReactivateUser(true);
				}
				break;
			case Task.READ_ALL:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setReadAllProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setReadAllOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setReadAllUser(true);
				} else if (permission.getDomain() == Task.LOGGING) {
					dto.setReadAllLogging(true);
				}
				break;
			case Task.UPDATE_ALL:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setUpdateAllProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setUpdateAllOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setUpdateAllUser(true);
				}
				break;
			case Task.DEACTIVATE_ALL:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setDeactivateAllProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setDeactivateAllOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setDeactivateAllUser(true);
				}
				break;
			case Task.REACTIVATE_ALL:
				if (permission.getDomain() == Task.PROVIDER) {
					dto.setReactivateAllProvider(true);
				} else if (permission.getDomain() == Task.ORGANISATION) {
					dto.setReactivateAllOrganisation(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setReactivateAllUser(true);
				}
				break;
			case Task.CONFIGURE:
				if (permission.getDomain() == Task.DUPLICATES) {
					dto.setConfigureDuplicate(true);
				} else if (permission.getDomain() == Task.USER) {
					dto.setConfigureUser(true);
				} else if (permission.getDomain() == Task.SYSTEM) {
					dto.setConfigureSystem(true);
				}
				break;
			default:
				break;
			}
		}

		return dto;
	}

	/**
	 * @see IPorsCore#validateProvider(Provider)
	 */
	public void validateProvider(Provider provider)
			throws MissingFieldsException, WrongValueException {
		// TODO: check editing user
		// TODO: check session id
		if (provider.getUser() == null) {
			throw new MissingFieldsException("User not set");
		} else if (provider.getUser().getId() == null
				&& isEmpty(provider.getUser().getName())) {
			throw new MissingFieldsException("No name or id found for user!");
		}

		if (!isEmpty(provider.getLanr()) && provider.getLanr().length() != 9) {
			throw new WrongValueException("LANr must be exactly 9 characters");
		}

		if (isEmpty(provider.getFirstName())) {
			throw new MissingFieldsException("No value for firstname found!");
		}
		if (isEmpty(provider.getLastName())) {
			throw new MissingFieldsException("No value for lastname found");
		}

		if (isEmpty(provider.getGenderCode())) {
			throw new MissingFieldsException("Gender code not defined!");
		} else if (!provider.getGenderCode().toUpperCase().equals("M")
				&& !provider.getGenderCode().toUpperCase().equals("F")
				&& !provider.getGenderCode().toUpperCase().equals("U")) {
			throw new WrongValueException(
					"Gender code must be \"M\", \"F\" or \"U\"!");
		}

		if (!isEmpty(provider.getEmail())
				&& !EmailValidator.getInstance().isValid(provider.getEmail())) {
			throw new WrongValueException("Email address is not valid!");
		}

		// TODO: telephone & fax: buchstaben checken

		if (provider.getAddresses() != null) {
			for (Address a : provider.getAddresses()) {
				validateAddress(a);
			}
		}

		if (provider.getLocalIds() != null) {
			for (LocalId l : provider.getLocalIds()) {
				validateLocalId(l);
			}
		}
	}

	/**
	 * @see IPorsCore#validateAddress(Address)
	 */
	public void validateAddress(Address address) throws MissingFieldsException,
			WrongValueException {
		if (isEmpty(address.getStreet())) {
			throw new MissingFieldsException("Street is missing in address");
		}
		if (isEmpty(address.getHouseNumber())) {
			throw new MissingFieldsException(
					"House number is missing in address");
		} else if (address.getHouseNumber().length() > 7) {
			throw new WrongValueException(
					"House number is too long, only 7 characters allowed!");
		}

		if (isEmpty(address.getCity())) {
			throw new MissingFieldsException("City is missing in address");
		}
		if (isEmpty(address.getCountry())) {
			throw new MissingFieldsException("Country is missing in address");
		} else {
			String[] countries = Locale.getISOCountries();
			boolean countryOk = false;
			for (String c : countries) {
				if (address.getCountry().equalsIgnoreCase(c)) {
					countryOk = true;
				}
			}
			if (!countryOk) {
				throw new WrongValueException(
						"Country must be valid to ISO 639, value was: "
								+ address.getCountry());
			}
		}
	}

	/**
	 * @see IPorsCore#validateOrganisation(Organisation)
	 */
	public void validateOrganisation(Organisation organisation)
			throws MissingFieldsException, WrongValueException {
		if (organisation.getUser() == null) {
			throw new MissingFieldsException("User not set");
		} else if (organisation.getUser().getId() == null
				&& isEmpty(organisation.getUser().getName())) {
			throw new MissingFieldsException("No name or id found for user!");
		}

		if (isEmpty(organisation.getName())) {
			throw new MissingFieldsException("Name is not set");
		}

		if (!isEmpty(organisation.getEmail())
				&& !EmailValidator.getInstance().isValid(
						organisation.getEmail())) {
			throw new WrongValueException("Email address is not valid!");
		}

		// TODO: telephone & fax: buchstaben checken

		if (organisation.getAddresses() != null) {
			for (Address a : organisation.getAddresses()) {
				validateAddress(a);
			}
		}

		if (organisation.getProviders() != null) {
			for (Provider p : organisation.getProviders()) {
				validateProvider(p);
			}
		}

		if (organisation.getLocalIds() != null) {
			for (LocalId l : organisation.getLocalIds()) {
				validateLocalId(l);
			}
		}
	}

	/**
	 * @see IPorsCore#validateLocalId(LocalId)
	 */
	public void validateLocalId(LocalId localId) throws MissingFieldsException,
			WrongValueException {
		if (isEmpty(localId.getLocalId())) {
			throw new MissingFieldsException("LocalId is required!");
		} else if (localId.getLocalId().length() > 10) {
			throw new WrongValueException("maximum size of LocalId is 10");
		}
		if (isEmpty(localId.getApplication())) {
			throw new MissingFieldsException(
					"Application of LocalId is not set!");
		}
		if (isEmpty(localId.getFacility())) {
			throw new MissingFieldsException("Facility of LocalId is not set!");
		}
		// if (localId.getProvider() != null && localId.getOrganisation() !=
		// null) {
		// throw new MissingFieldsException(
		// "Provider and Organisation is set for LocalId but only one can be set!");
		// }
		// if (localId.getProvider() == null && localId.getOrganisation() ==
		// null) {
		// throw new MissingFieldsException(
		// "Provider and Organisation is not set for LocalId but one is required!");
		// }
	}

	private boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * @see IPorsCore#assembleProviderHasAddressLogDTO(ProviderHasAddressLog)
	 */
	public ProviderHasAddressLogDTO assembleProviderHasAddressLogDTO(
			ProviderHasAddressLog phaLog) {
		ProviderHasAddressLogDTO pha = new ProviderHasAddressLogDTO();
		pha.setId(phaLog.getId());
		pha.setIPAddress(phaLog.getIPAddress());
		pha.setLogTime(phaLog.getLogTime());
		pha.setNewAddressId(phaLog.getNewAddressId());
		pha.setNewRegionalProviderId(phaLog.getNewRegionalProviderId());
		pha.setOldAddressId(phaLog.getOldAddressId());
		pha.setOldRegionalProviderId(phaLog.getOldRegionalProviderId());
		pha.setPorsUserId(phaLog.getUser().getId());
		pha.setSessionId(phaLog.getSessionId());
		pha.setTableName(phaLog.getTableName());
		pha.setTriggerType(phaLog.getTriggerType());
		return pha;
	}

	/**
	 * @see IPorsCore#assembleOrganisationHasAddressLogDTO(OrganisationHasAddressLog)
	 */
	public OrganisationHasAddressLogDTO assembleOrganisationHasAddressLogDTO(
			OrganisationHasAddressLog orgLog) {
		OrganisationHasAddressLogDTO org = new OrganisationHasAddressLogDTO();
		org.setId(orgLog.getId());
		org.setIPAddress(orgLog.getIPAddress());
		org.setLogTime(orgLog.getLogTime());
		org.setNewAddressId(orgLog.getNewAddressId());
		org.setNewRegionalOrganisationId(orgLog.getNewRegionalOrganisationId());
		org.setOldAddressId(orgLog.getOldAddressId());
		org.setOldRegionalOrganisationId(orgLog.getOldRegionalOrganisationId());
		org.setPorsUSerId(orgLog.getUser().getId());
		org.setSessionId(orgLog.getSessionId());
		org.setTableName(orgLog.getTableName());
		org.setTriggerType(orgLog.getTriggerType());
		return org;
	}

	/**
	 * @see IPorsCore#assembleOrganisationHasProviderLogDTO(OrganisationHasProviderLog)
	 */
	public OrganisationHasProviderLogDTO assembleOrganisationHasProviderLogDTO(
			OrganisationHasProviderLog ohpLog) {
		OrganisationHasProviderLogDTO org = new OrganisationHasProviderLogDTO();
		org.setIPAddress(ohpLog.getIPAddress());
		org.setId(ohpLog.getId());
		org.setLogTime(ohpLog.getLogTime());
		org.setNewRegionalOrganisationId(ohpLog.getNewRegionalOrganisationId());
		org.setNewRegionalProviderId(ohpLog.getNewRegionalProviderId());
		org.setOldRegionalOrganisationId(ohpLog.getOldRegionalOrganisationId());
		org.setOldRegionalProviderId(ohpLog.getOldRegionalProviderId());
		org.setPorsUserId(ohpLog.getUser().getId());
		org.setSessionId(ohpLog.getSessionId());
		org.setTableName(ohpLog.getTableName());
		org.setTriggerType(ohpLog.getTriggerType());
		return org;
	}

	/**
	 * @see IPorsCore#assembleSearchCriteria(SearchCriteriaDTO)
	 */
	public SearchCriteria assembleSearchCriteria(SearchCriteriaDTO searchDTO) {
		SearchCriteria search = new SearchCriteria();
		search.setField(searchDTO.getField());
		search.setValue(searchDTO.getValue());
		search.setOperator(searchDTO.getOperator());
		return search;
	}

	/**
	 * @see IPorsCore#assembleSearchCriteriaDTO(SearchCriteria)
	 */
	public SearchCriteriaDTO assembleSearchCriteriaDTO(SearchCriteria search) {
		SearchCriteriaDTO searchDTO = new SearchCriteriaDTO();
		searchDTO.setField(search.getField());
		searchDTO.setValue(search.getValue());
		searchDTO.setOperator(search.getOperator());
		return searchDTO;
	}

	/**
	 * @see IPorsCore#assembleImportResultDTO(ImportResult)
	 */
	public ImportResultDTO assembleImportResultDTO(ImportResult result) {
		ImportResultDTO dto = new ImportResultDTO();
		dto.setJobId(result.getJobId());
		dto.setAddEntries(result.getAddEntries());
		dto.setUpdateEntries(result.getUpdateEntries());
		dto.setDomain(result.getDomain());
		dto.setEndDate(result.getEndDate());
		dto.setErrorMessage(result.getErrorMessage());
		dto.setProcessed(result.getProcessed());
		dto.setStartDate(result.getStartDate());
		dto.setStatusMessage(result.getStatusMessage());
		double progress = 0.0;
		int toProcess = result.getAddEntries() + result.getUpdateEntries();
		if (result.getProcessed() > 0 && toProcess > 0) {
			progress = (result.getProcessed() / toProcess) * 100;
		}
		dto.setProgress(progress);

		return dto;
	}

	/**
	 * @see IPorsCore#assembleImportResult(ImportResultDTO)
	 */
	public ImportResult assembleImportResult(ImportResultDTO dto) {
		ImportResult result = new ImportResult();
		result.setJobId(dto.getJobId());
		result.setAddEntries(dto.getAddEntries());
		result.setUpdateEntries(dto.getUpdateEntries());
		result.setDomain(dto.getDomain());
		result.setEndDate(dto.getEndDate());
		result.setErrorMessage(dto.getErrorMessage());
		result.setProcessed(dto.getProcessed());
		result.setStartDate(dto.getStartDate());
		result.setStatusMessage(dto.getStatusMessage());

		return result;
	}

	/**
	 * @see IPorsCore#validateUser(PorsUser)
	 */
	public void validateUser(PorsUser newUser) throws MissingFieldsException,
			WrongValueException {

		if (isEmpty(newUser.getName())) {
			throw new MissingFieldsException("No value for name found!");
		}

		if (isEmpty(newUser.getPassword())) {
			throw new MissingFieldsException("No value for password found!");
		}

	}

	/**
	 * @see IPorsCore#assembleDuplicateDetailDTO(Provider, Provider)
	 */
	public DuplicateDetailDTO assembleDuplicateDetailDTO(Provider p1,
			Provider p2) {
		DuplicateDetailDTO dto = new DuplicateDetailDTO();
		dto.setProvider1(assembleProviderDTO(p1));
		dto.setProvider2(assembleProviderDTO(p2));
		return dto;
	}

	/**
	 * @see IPorsCore#assembleDuplicateDetailDTO(Address, Address)
	 */
	public DuplicateDetailDTO assembleDuplicateDetailDTO(Address a1, Address a2) {
		DuplicateDetailDTO dto = new DuplicateDetailDTO();
		dto.setAddress1(assembleAddressDTO(a1));
		dto.setAddress2(assembleAddressDTO(a2));
		return dto;
	}

	/**
	 * @see IPorsCore#assembleDuplicateDetailDTO(Organisation, Organisation)
	 */
	public DuplicateDetailDTO assembleDuplicateDetailDTO(Organisation o1,
			Organisation o2) {
		DuplicateDetailDTO dto = new DuplicateDetailDTO();
		dto.setOrganisation1(assembleOrganisationDTO(o1));
		dto.setOrganisation2(assembleOrganisationDTO(o2));
		return dto;
	}

	/**
	 * @see IPorsCore#assembleLoggingEntryDTO(UserHistory)
	 */
	public LoggingEntryDTO assembleLoggingEntryDTO(UserHistory loggingEntry) {
		LoggingEntryDTO loggingEntryDto = new LoggingEntryDTO();
		loggingEntryDto.setPorsUserId(loggingEntry.getEditingUserId());
		loggingEntryDto.setUserName(loggingEntry.getEditingUserName());
		loggingEntryDto.setLogTime(loggingEntry.getLogTime());
		loggingEntryDto.setLogDateString(DateFormat.getDateInstance(
				DateFormat.MEDIUM).format(loggingEntry.getLogTime()));

		DateFormat dfmt = new SimpleDateFormat("HH:mm:ss:SS");

		loggingEntryDto
				.setLogTimeString(dfmt.format(loggingEntry.getLogTime()));
		loggingEntryDto.setDomain(loggingEntry.getDomain());
		loggingEntryDto.setAction(loggingEntry.getAction());
		loggingEntryDto.setLogEntryId(loggingEntry.getDomainId());

		return loggingEntryDto;
	}
}
