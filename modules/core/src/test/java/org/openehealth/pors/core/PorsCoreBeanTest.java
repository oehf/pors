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

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.pors.core.IPorsCore;
import org.openehealth.pors.core.PorsCoreBean;
import org.openehealth.pors.core.dto.AddressDTO;
import org.openehealth.pors.core.dto.AddressLogDTO;
import org.openehealth.pors.core.dto.DuplicateConfigurationDTO;
import org.openehealth.pors.core.dto.DuplicateEntryDTO;
import org.openehealth.pors.core.dto.LocalIdDTO;
import org.openehealth.pors.core.dto.LocalIdLogDTO;
import org.openehealth.pors.core.dto.LoggingEntryDTO;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.OrganisationHasAddressLogDTO;
import org.openehealth.pors.core.dto.OrganisationHasProviderLogDTO;
import org.openehealth.pors.core.dto.OrganisationLogDTO;
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
import org.openehealth.pors.database.entities.UserRole;
import org.openehealth.pors.database.util.SearchCriteria;

/**
 * The component test class of the module core with tests whether the assembling
 * of entity to data transfer object is working (and vice versa) and validating
 * of the several attributes of the data transfer objects.
 * 
 * @author tb, mf
 * 
 */
public class PorsCoreBeanTest {

	private IPorsCore porsCore;
	private Provider provider;
	private Address address;
	private LocalId localIdProvider;
	private LocalId localIdOrganisation;
	private Organisation organisation;
	private ProviderDTO providerDTO;
	private OrganisationDTO organisationDTO;
	private LocalIdDTO localIdOrganisationDTO;
	private LocalIdDTO localIdProviderDTO;
	private LocalIdLog lidLog;
	private AddressDTO addressDTO;
	private AddressLog addressLog;
	private ProviderLog providerLog;
	private OrganisationLog organisationLog;
	private PorsUser user;
	private PorsUser editingUser;
	private UserDTO userDTO;
	private ProviderHasAddressLog phaLog;
	private OrganisationHasAddressLog ohaLog;
	private OrganisationHasProviderLog ohpLog;
	private History log;
	private LoggingEntryDTO logDTO;
	private DuplicateEntryId duplicateEntry;
	private DuplicateEntry duplicate;
	private DuplicateEntryDTO duplicateDTO;
	private SearchCriteria search;
	private SearchCriteriaDTO searchDTO;
	private DuplicateConfigurationDTO configurationDTO;
	private Map<String, Integer> configurationMap;
	private UserRole role;
	private UserRoleDTO roleDTO;

	private DateFormat dfmt = new SimpleDateFormat("HH:mm:ss:SS");

	@Before
	public void setUp() {
		porsCore = new PorsCoreBean();

		// PorsUser
		user = new PorsUser();
		user.setId(1234);
		user.setName("test123");
		user.setPassword("test");
		editingUser = new PorsUser();
		editingUser.setId(12345678);
		editingUser.setName("test");
		editingUser.setPassword("test");

		// Provider
		provider = new Provider();
		provider.setId(new Long(1234));
		provider.setFirstName("test");
		provider.setLastName("test");
		provider.setLanr("123456789");
		provider.setSpecialisation("fachrichtung");
		provider.setVersion(new Long(2));
		provider.setBirthday(new Date(1));
		provider.setDeactivationDate(new Date(1));
		provider.setDeactivationReasonCode("1");
		provider.setReactivationDate(new Date(1));
		provider.setReactivationReasonCode("1");
		provider.setLastUpdateDate(new Date(1));
		provider.setMiddleName("middle");
		provider.setNamePrefix("prefix");
		provider.setNameSuffix("suffix");
		provider.setSessionId("session");
		provider.setOid("oidoidoid");
		provider.setGenderCode("m");
		provider.setEmail("test@test.de");
		provider.setTelephone("+49622112345678");
		provider.setFax("+496221123456789");

		provider.setUser(user);
		provider.setEditingUser(editingUser);

		// Organisation
		organisation = new Organisation();
		organisation.setId(new Long(2));
		organisation.setName("test");
		organisation.setSecondName("secondname");
		organisation.setDescription("description");
		organisation.setEmail("test@test.de");
		organisation.setTelephone("+496221123456");
		organisation.setFax("+49622112345678");
		organisation.setEstablishmentId("establishmentID");
		organisation.setIpAddress("127.0.0.1");
		organisation.setSessionId("sessionID");
		organisation.setVersion(new Long(3));
		organisation.setOid("oid");
		organisation.setDeactivationDate(new Date(1));
		organisation.setDeactivationReasonCode("1");
		organisation.setReactivationDate(new Date(1));
		organisation.setReactivationReasonCode("1");
		organisation.setLastUpdateDate(new Date(1));

		organisation.setUser(user);
		organisation.setEditingUser(editingUser);

		// UserDTO
		userDTO = new UserDTO();
		userDTO.setId(123456);
		userDTO.setPassword("pass");
		userDTO.setUsername("user");

		// Role
		role = new UserRole();
		role.setDescription("rollenbeschreibung");
		role.setId(2);
		role.setName("rollenname");

		// RoleDTO
		roleDTO = new UserRoleDTO();
		roleDTO.setDescription("roledescription");
		roleDTO.setId(3);
		roleDTO.setName("rolename");

		// ProviderDTO
		providerDTO = new ProviderDTO();
		providerDTO.setId(new Long(2));
		providerDTO.setFirstname("firstname");
		providerDTO.setLastname("Lastname");
		providerDTO.setLanr("123456789");
		providerDTO.setSpecialisation("fach");
		providerDTO.setVersion(new Long(1));
		providerDTO.setTelephone("tel");
		providerDTO.setFax("fax");
		providerDTO.setEmail("email");
		providerDTO.setGenderCode("m");
		providerDTO.setIp("ipipip");
		providerDTO.setMiddleName("mname");
		providerDTO.setNamePrefix("prefix");
		providerDTO.setNameSuffix("suffix");
		providerDTO.setOid("123.456.789");
		providerDTO.setSessionId("session");
		providerDTO.setDeactivationDate(new Date(1));
		providerDTO.setDeactivationReasonCode("1");
		providerDTO.setReactivationDate(new Date(1));
		providerDTO.setReactivationReasonCode("1");
		providerDTO.setBirthday(new Date(1));
		providerDTO.setLastUpdateDate(new Date(1));

		providerDTO.setPorsuserid(user.getId());
		providerDTO.setEditingUserID(editingUser.getId());

		// OrganisationDTO
		organisationDTO = new OrganisationDTO();
		organisationDTO.setId(new Long(2));
		organisationDTO.setName("test");
		organisationDTO.setSecondName("secondname");
		organisationDTO.setDescription("description");
		organisationDTO.setEmail("test@test.de");
		organisationDTO.setTelephone("+496221123456");
		organisationDTO.setFax("+49622112345678");
		organisationDTO.setEstablishmentID("establishmentID");
		organisationDTO.setIp("127.0.0.1");
		organisationDTO.setSessionId("sessionID");
		organisationDTO.setVersion(new Long(4));
		organisationDTO.setOID("oid");
		organisationDTO.setDeactivationDate(new Date(1));
		organisationDTO.setDeactivationReasonCode("1");
		organisationDTO.setReactivationDate(new Date(1));
		organisationDTO.setReactivationReasonCode("1");
		organisationDTO.setLastUpdateDate(new Date(1));
		organisationDTO.setPorsuserID(user.getId());
		organisationDTO.setEditingUserID(editingUser.getId());

		// ProviderLog
		providerLog = new ProviderLog();
		providerLog.setId(new Long(2));
		providerLog.setIPAddress("ipaddr");
		providerLog.setLogTime(new Date(1));
		providerLog.setNewBirthday(new Date(1));
		providerLog.setNewDeactivationDate(new Date(1));
		providerLog.setNewDeactivationReasonCode("1");
		providerLog.setNewEmail("newEmail");
		providerLog.setNewFax("newFax");
		providerLog.setNewFirstName("newFirstname");
		providerLog.setNewGenderCode("newGender");
		providerLog.setNewLanr("123456789");
		providerLog.setNewSpecialisation("newSpec");
		providerLog.setNewLastName("NewLastname");
		providerLog.setNewLastUpdateDate(new Date(1));
		providerLog.setNewMiddleName("newMName");
		providerLog.setNewNamePrefix("newNPrefix");
		providerLog.setNewNameSuffix("newNSuffix");
		providerLog.setNewOid("newOID");
		providerLog.setNewReactivationDate(new Date(2));
		providerLog.setNewReactivationReasonCode("1");
		providerLog.setNewTelephone("newTel");
		providerLog.setOldBirthday(new Date(2));
		providerLog.setOldDeactivationDate(new Date(2));
		providerLog.setOldDeactivationReasonCode("2");
		providerLog.setOldEmail("oldEmail");
		providerLog.setOldFax("oldFax");
		providerLog.setOldFirstName("oldFirstname");
		providerLog.setOldGenderCode("oldGender");
		providerLog.setOldLanr("987654321");
		providerLog.setOldSpecialisation("oldSpec");
		providerLog.setOldLastName("oldLastname");
		providerLog.setOldLastUpdateDate(new Date(2));
		providerLog.setOldMiddleName("oldMname");
		providerLog.setOldNamePrefix("oldNPrefix");
		providerLog.setOldNameSuffix("oldNSuffix");
		providerLog.setOldOid("oldOid");
		providerLog.setOldReactivationDate(new Date(2));
		providerLog.setOldReactivationReasonCode("2");
		providerLog.setOldTelephone("oldTel");
		providerLog.setRegionalProviderId(provider.getId());
		providerLog.setSessionId("session");
		providerLog.setTableName("table");
		providerLog.setTriggerType("triggert");
		providerLog.setUser(user);

		// OrganisationLog
		organisationLog = new OrganisationLog();
		organisationLog.setId(new Long(4));
		organisationLog.setIPAddress("nipa");
		organisationLog.setLogTime(new Date(8));
		organisationLog.setNewDeactivationDate(new Date(6));
		organisationLog.setNewDeactivationReasonCode("1");
		organisationLog.setNewDescription("ndesc");
		organisationLog.setNewEMail("nmail");
		organisationLog.setNewEstablishmentId("neid");
		organisationLog.setNewFax("nfax");
		organisationLog.setNewLastUpdateDate(new Date(5));
		organisationLog.setNewName("nname");
		organisationLog.setNewOid("noid");
		organisationLog.setNewPorsUserId(user.getId());
		organisationLog.setNewReactivationDate(new Date(1));
		organisationLog.setNewReactivationReasonCode("2");
		organisationLog.setNewSecondName("nsn");
		organisationLog.setNewTelephone("ntel");
		organisationLog.setOldDeactivationDate(new Date(1));
		organisationLog.setOldDeactivationReasonCode("2");
		organisationLog.setOldDescription("odesc");
		organisationLog.setOldEMail("omail");
		organisationLog.setOldEstablishmentId("oeid");
		organisationLog.setOldFax("ofax");
		organisationLog.setOldLastUpdateDate(new Date(3));
		organisationLog.setOldName("oname");
		organisationLog.setOldOid("ooid");
		organisationLog.setOldPorsUserId(editingUser.getId());
		organisationLog.setOldReactivationDate(new Date(2));
		organisationLog.setOldReactivationReasonCode("1");
		organisationLog.setOldSecondName("osname");
		organisationLog.setOldTelephone("otel");
		organisationLog.setRegionalOrganisationId(organisation.getId());
		organisationLog.setSessionId("sessionid");
		organisationLog.setTableName("table");
		organisationLog.setTriggerType("type");
		organisationLog.setUser(user);

		// LocalIDs
		localIdProvider = new LocalId();
		localIdProvider.setId(new Long(45));
		localIdProvider.setLocalId("localId1");
		localIdProvider.setApplication("testapplication");
		localIdProvider.setFacility("testfacility");
		localIdProvider.setOrganisation(organisation);
		localIdProvider.setProvider(provider);

		List<LocalId> localIDsProvider = new ArrayList<LocalId>();
		localIDsProvider.add(localIdProvider);
		provider.setLocalIds(localIDsProvider);

		localIdOrganisation = new LocalId();
		localIdOrganisation.setId(new Long(456));
		localIdOrganisation.setLocalId("localId2");
		localIdOrganisation.setApplication("testapplication1");
		localIdOrganisation.setFacility("testfacility1");
		localIdOrganisation.setOrganisation(organisation);
		localIdOrganisation.setProvider(provider);

		List<LocalId> localIDsOrganisation = new ArrayList<LocalId>();
		localIDsOrganisation.add(localIdOrganisation);
		organisation.setLocalIds(localIDsOrganisation);

		// LocalIdDTOs
		localIdProviderDTO = new LocalIdDTO();
		localIdProviderDTO.setId(new Long(24));
		localIdProviderDTO.setLocalId("loc2");
		localIdProviderDTO.setApplication("app2");
		localIdProviderDTO.setFacility("fac2");
		localIdProviderDTO.setProviderFirstname(providerDTO.getFirstname());
		localIdProviderDTO.setProviderLastname(providerDTO.getLastname());
		localIdProviderDTO.setProviderId(providerDTO.getId());

		List<LocalIdDTO> localIDsProviderDTO = new ArrayList<LocalIdDTO>();
		localIDsProviderDTO.add(localIdProviderDTO);
		providerDTO.setLocalId(localIDsProviderDTO);

		localIdOrganisationDTO = new LocalIdDTO();
		localIdOrganisationDTO.setId(new Long(32));
		localIdOrganisationDTO.setLocalId("loc3");
		localIdOrganisationDTO.setApplication("app3");
		localIdOrganisationDTO.setFacility("fac3");
		localIdOrganisationDTO.setOrganisationId(organisationDTO.getId());
		localIdOrganisationDTO.setOrganisationName(organisationDTO.getName());

		List<LocalIdDTO> localIDsOrganisationDTO = new ArrayList<LocalIdDTO>();
		localIDsOrganisationDTO.add(localIdOrganisationDTO);
		organisationDTO.setLocalId(localIDsOrganisationDTO);

		// LocalIdLog
		lidLog = new LocalIdLog();
		lidLog.setAction("action");
		lidLog.setId(Long.valueOf(2));
		lidLog.setIPAddress("ipa");
		lidLog.setLocalIdId(localIdProvider.getId());
		lidLog.setLogTime(new Date(1));
		lidLog.setNewApplication("napplicationlog");
		lidLog.setNewFacility("nfacilitylog");
		lidLog.setNewLocalId("nlocallog");
		lidLog.setNewRegionalOrganisationId(organisation.getId());
		lidLog.setNewRegionalProviderId(provider.getId());
		lidLog.setOldApplication("oapplicationlog");
		lidLog.setOldFacility("ofacilitylog");
		lidLog.setOldLocalId("olocalidlog");
		lidLog.setOldRegionalOrganisationId(Long.valueOf(4));
		lidLog.setOldRegionalProviderId(Long.valueOf(3));
		lidLog.setSessionId("sessionid");
		lidLog.setTableName("nameTable");
		lidLog.setUser(user);

		// Addresses
		address = new Address();
		address.setId(new Long(1));
		address.setCity("Heidelberg");
		address.setCountry("DE");
		address.setHouseNumber("10");
		address.setState("BW");
		address.setStreet("musterstr.");
		address.setZipCode("69121");
		address.setAdditional("additional");

		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		provider.setAddresses(addresses);

		List<Provider> providers = new ArrayList<Provider>();
		providers.add(provider);

		organisation.setAddresses(addresses);
		organisation.setProviders(providers);

		// AddressDTOs
		addressDTO = new AddressDTO();
		addressDTO.setId(new Long(1));
		addressDTO.setCity("Stuttgart");
		addressDTO.setCountry("DE");
		addressDTO.setHouseNumber("2");
		addressDTO.setState("BW");
		addressDTO.setStreet("stra�e");
		addressDTO.setZipCode("12345");
		addressDTO.setAdditional("add");

		List<AddressDTO> addressesDTO = new ArrayList<AddressDTO>();
		addressesDTO.add(addressDTO);
		providerDTO.setAddresses(addressesDTO);
		organisationDTO.setAddresses(addressesDTO);

		// AddressLog
		addressLog = new AddressLog();
		addressLog.setAddressId(address.getId());
		addressLog.setId(Long.valueOf(2));
		addressLog.setIPAddress("ipa");
		addressLog.setLogTime(new Date(2));
		addressLog.setNewAdditional("nadd");
		addressLog.setNewCity("ncity");
		addressLog.setNewCountry("ncountry");
		addressLog.setNewHouseNumber("nhouse");
		addressLog.setNewState("nstreet");
		addressLog.setNewStreet("nstreet");
		addressLog.setNewZipCode("nzip");
		addressLog.setOldAdditional("oadd");
		addressLog.setOldCity("ocity");
		addressLog.setOldCountry("ocountry");
		addressLog.setOldHouseNumber("ohouse");
		addressLog.setOldState("ostate");
		addressLog.setOldStreet("ostreet");
		addressLog.setOldZipCode("ozip");
		addressLog.setSessionId("session");
		addressLog.setTableName("tabl");
		addressLog.setTriggerType("trigger");
		addressLog.setUser(editingUser);

		// ProviderHasAddressLog
		phaLog = new ProviderHasAddressLog();
		phaLog.setId(Long.valueOf(1));
		phaLog.setIPAddress("ip");
		phaLog.setLogTime(new Date(1));
		phaLog.setNewAddressId(address.getId());
		phaLog.setNewRegionalProviderId(provider.getId());
		phaLog.setOldAddressId(Long.valueOf(2));
		phaLog.setOldRegionalProviderId(Long.valueOf(2));
		phaLog.setSessionId("session");
		phaLog.setTableName("tname");
		phaLog.setTriggerType("ttype");
		phaLog.setUser(user);

		// OganisationHasAddressLog
		ohaLog = new OrganisationHasAddressLog();
		ohaLog.setId(Long.valueOf(1));
		ohaLog.setIPAddress("ipa");
		ohaLog.setLogTime(new Date(2));
		ohaLog.setNewAddressId(address.getId());
		ohaLog.setNewRegionalOrganisationId(organisation.getId());
		ohaLog.setOldAddressId(Long.valueOf(3));
		ohaLog.setOldRegionalOrganisationId(Long.valueOf(3));
		ohaLog.setSessionId("sessionid");
		ohaLog.setTableName("tablen");
		ohaLog.setTriggerType("triggern");
		ohaLog.setUser(editingUser);

		// OrganisationHasProviderLog
		ohpLog = new OrganisationHasProviderLog();
		ohpLog.setId(Long.valueOf(1));
		ohpLog.setIPAddress("ipadr");
		ohpLog.setLogTime(new Date(1));
		ohpLog.setNewRegionalOrganisationId(organisation.getId());
		ohpLog.setNewRegionalProviderId(provider.getId());
		ohpLog.setOldRegionalOrganisationId(Long.valueOf(2));
		ohpLog.setOldRegionalProviderId(Long.valueOf(2));
		ohpLog.setSessionId("session");
		ohpLog.setTableName("table");
		ohpLog.setTriggerType("trigger");
		ohpLog.setUser(editingUser);

		// Logging Entry
		log = new History();
		log.setAction("action");
		log.setDomain("domain");
		log.setLogId(Long.valueOf(2));
		log.setLogTime(new Date(1));
		log.setUserId(user.getId());
		log.setUserName("name");

		// LoggingEntryDTO
		logDTO = new LoggingEntryDTO();
		logDTO.setAction("act");
		logDTO.setDomain("dom");
		logDTO.setLogTime(new Date(2));
		logDTO.setLogDateString(DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(logDTO.getLogTime()));
		logDTO.setLogTimeString(dfmt.format(logDTO.getLogTime()));
		logDTO.setLogEntryId(Long.valueOf(3));
		logDTO.setPorsUserId(user.getId());
		logDTO.setUserName("user");

		// DuplicatEntry
		duplicateEntry = new DuplicateEntryId();
		duplicateEntry.setId1(Long.valueOf(2));
		duplicateEntry.setId2(Long.valueOf(1));
		duplicateEntry.setDomain("domain");

		duplicate = new DuplicateEntry();
		duplicate.setId(duplicateEntry);
		duplicate.setTimestamp(new Date(2));
		duplicate.setValue(Double.valueOf(2));

		// DuplicateEntryDTO
		duplicateDTO = new DuplicateEntryDTO();
		duplicateDTO.setDomain("domain");
		duplicateDTO.setId1(new Long(1));
		duplicateDTO.setId2(new Long(2));
		duplicateDTO.setLogTime(new Date(2));
		duplicateDTO.setLogDateString(DateFormat.getDateInstance(
				DateFormat.MEDIUM).format(duplicateDTO.getLogTime()));
		duplicateDTO.setLogTimeString(dfmt.format(duplicateDTO.getLogTime()));
		duplicateDTO.setPercentage("1");

		// SearchCriteria
		search = new SearchCriteria();
		search.setField("feld");
		search.setOperator("operator");
		search.setValue("wert");

		// SearchCriteriaDTO
		searchDTO = new SearchCriteriaDTO();
		searchDTO.setField("fff");
		searchDTO.setOperator("ooo");
		searchDTO.setValue("www");

		// DuplicateConfigurationDTO
		configurationDTO = new DuplicateConfigurationDTO();
		configurationMap = new HashMap<String, Integer>();

		configurationMap.put("a.city", 10);
		configurationDTO.setAddressCityWeight(10);
		configurationMap.put("a.country", 10);
		configurationDTO.setAddressCountryWeight(10);
		configurationMap.put("a.housenumber", 10);
		configurationDTO.setAddressHousenumberWeight(10);
		configurationMap.put("a.state", 10);
		configurationDTO.setAddressStateWeight(10);
		configurationMap.put("a.street", 10);
		configurationDTO.setAddressStreetWeight(10);
		configurationMap.put("a.zipcode", 10);
		configurationDTO.setAddressZipCodeWeight(10);

		configurationMap.put("o.address", 10);
		configurationDTO.setOrganisationAddressWeight(10);
		configurationMap.put("o.email", 10);
		configurationDTO.setOrganisationEmailWeight(10);
		configurationMap.put("o.fax", 10);
		configurationDTO.setOrganisationFaxWeight(10);
		configurationMap.put("o.name", 10);
		configurationDTO.setOrganisationNameWeight(10);
		configurationMap.put("o.secondname", 10);
		configurationDTO.setOrganisationSecondnameWeight(10);
		configurationMap.put("o.telephone", 10);
		configurationDTO.setOrganisationTelephoneWeight(10);

		configurationMap.put("timer.hour", 10);
		configurationDTO.setTimerHour(10);
		configurationMap.put("timer.minutes", 10);
		configurationDTO.setTimerMinute(10);
		configurationMap.put("timer.seconds", 10);
		configurationDTO.setTimerSecond(10);

		configurationMap.put("p.address", 10);
		configurationDTO.setProviderAddressWeight(10);
		configurationMap.put("p.birthday", 10);
		configurationDTO.setProviderBirthdayWeight(10);
		configurationMap.put("p.email", 10);
		configurationDTO.setProviderEmailWeight(10);
		configurationMap.put("p.fax", 10);
		configurationDTO.setProviderFaxWeight(10);
		configurationMap.put("p.firstname", 10);
		configurationDTO.setProviderFirstnameWeight(10);
		configurationMap.put("p.gendercode", 10);
		configurationDTO.setProviderGenderCodeWeight(10);
		configurationMap.put("p.lastname", 10);
		configurationDTO.setProviderLastnameWeight(10);
		configurationMap.put("p.middlename", 10);
		configurationDTO.setProviderMiddlenameWeight(10);
		configurationMap.put("p.nameprefix", 10);
		configurationDTO.setProviderNameprefixWeight(10);
		configurationMap.put("p.namesuffix", 10);
		configurationDTO.setProviderNamesuffixWeight(10);
		configurationMap.put("p.oid", 10);
		configurationDTO.setProviderOIDWeight(10);
		configurationMap.put("p.specialisation", 10);
		configurationDTO.setProviderSpecialisationWeight(10);
		configurationMap.put("p.telephone", 10);
		configurationDTO.setProviderTelephoneWeight(10);

		configurationMap.put("lowerthreshold", 10);
		configurationDTO.setLowerthreshold(10);
		configurationMap.put("upperthreshold", 10);
		configurationDTO.setUpperthreshold(10);
	}

	/**
	 * Missing street
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress1() throws Exception {
		address.setStreet(null);
		porsCore.validateAddress(address);
	}

	/**
	 * Missing street
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress1_1() throws Exception {
		address.setStreet("");
		porsCore.validateAddress(address);
	}

	/**
	 * Missing country
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress2() throws Exception {
		address.setCountry(null);
		porsCore.validateAddress(address);
	}

	/**
	 * Missing country
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress2_1() throws Exception {
		address.setCountry("");
		porsCore.validateAddress(address);
	}

	/**
	 * Wrong country format
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateAddress3() throws Exception {
		address.setCountry("DEU");
		porsCore.validateAddress(address);
	}

	/**
	 * Missing house number
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress4() throws Exception {
		address.setHouseNumber(null);
		porsCore.validateAddress(address);
	}

	/**
	 * Missing house number
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress4_1() throws Exception {
		address.setHouseNumber("");
		porsCore.validateAddress(address);
	}

	/**
	 * Missing city
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress5() throws Exception {
		address.setCity(null);
		porsCore.validateAddress(address);
	}

	/**
	 * Missing city
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateAddress5_1() throws Exception {
		address.setCity("");
		porsCore.validateAddress(address);
	}

	/**
	 * Everything fine
	 */
	@Test
	public void testValidateAddress6() throws Exception {
		porsCore.validateAddress(address);
	}

	/**
	 * Missing User
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateProvider1() throws Exception {
		provider.setUser(null);
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing username
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateProvider1_1() throws Exception {
		provider.getUser().setName(null);
		provider.getUser().setId(null);
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing username
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateProvider1_2() throws Exception {
		provider.getUser().setName("");
		provider.getUser().setId(null);
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing firstname
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateProvider2() throws Exception {
		provider.setFirstName(null);
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing firstname
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateProvider2_1() throws Exception {
		provider.setFirstName("");
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing lastname
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateProvider3() throws Exception {
		provider.setLastName(null);
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing lastname
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateProvider3_1() throws Exception {
		provider.setLastName("");
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing gender code
	 */
	@Test
	public void testValidateProvider4() throws Exception {
		provider.setGenderCode(null);
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing gender code
	 */
	@Test
	public void testValidateProvider4_1() throws Exception {
		provider.setGenderCode("");
		porsCore.validateProvider(provider);
	}

	/**
	 * Invalid gender code
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateProvider5() throws Exception {
		provider.setGenderCode("Z");
		porsCore.validateProvider(provider);
	}

	/**
	 * invalid E-Mail Address
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateProvider6() throws Exception {
		provider.setEmail("@test.de");
		porsCore.validateProvider(provider);
	}

	/**
	 * invalid E-Mail Address
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateProvider6_1() throws Exception {
		provider.setEmail("test@.de");
		porsCore.validateProvider(provider);
	}

	/**
	 * invalid E-Mail Address
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateProvider6_2() throws Exception {
		provider.setEmail("test@testde");
		porsCore.validateProvider(provider);
	}

	/**
	 * invalid E-Mail Address
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateProvider6_3() throws Exception {
		provider.setEmail("testtest.de");
		porsCore.validateProvider(provider);
	}

	/**
	 * Validation error in address
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateProvider7() throws Exception {
		provider.getAddresses().get(0).setCountry("GERMANY");
		porsCore.validateProvider(provider);
	}

	/**
	 * Everything fine
	 */
	@Test
	public void testValidateProvider8() throws Exception {
		porsCore.validateProvider(provider);
	}

	/**
	 * Missing user or username
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateOrganisation1() throws Exception {
		organisation.setUser(null);
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Missing user or username
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateOrganisation1_1() throws Exception {
		organisation.getUser().setName(null);
		organisation.getUser().setId(null);
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Missing user or username
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateOrganisation1_2() throws Exception {
		organisation.getUser().setName("");
		organisation.getUser().setId(null);
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Missing organisation name
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateOrganisation2() throws Exception {
		organisation.setName(null);
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Missing organisation name
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateOrganisation2_1() throws Exception {
		organisation.setName("");
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Wrong email
	 */
	@Test(expected = WrongValueException.class)
	public void testValidateOrganisation3() throws Exception {
		organisation.setEmail("test");
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Validation error in address
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateOrganisation4() throws Exception {
		organisation.getAddresses().get(0).setHouseNumber("");
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Validation error in provider
	 */
	@Test(expected = MissingFieldsException.class)
	public void testValidateOrganisation5() throws Exception {
		organisation.getProviders().get(0).setFirstName("");
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Everything fine
	 */
	@Test
	public void testValidateOrganisation6() throws Exception {
		porsCore.validateOrganisation(organisation);
	}

	/**
	 * Test the assembling of a data transfer object User to an entity PorsUser
	 */
	@Test
	public void testAssembleUser1() {
		PorsUser porsuser = porsCore.assemblePorsUser(userDTO);
		Assert.assertEquals(porsuser.getId(), userDTO.getId());
		Assert.assertEquals(porsuser.getName(), userDTO.getUsername());
		Assert.assertEquals(porsuser.getPassword(), userDTO.getPassword());
	}

	/**
	 * Test the assembling of a entity PorsUser to an data transfer object User
	 */
	@Test
	public void testAssembleUserDTO1() {
		UserDTO userDTO = porsCore.assembleUserDTO(user);
		Assert.assertEquals(userDTO.getId(), user.getId());
		Assert.assertEquals(userDTO.getUsername(), user.getName());
		Assert.assertEquals(userDTO.getPassword(), user.getPassword());
	}

	/**
	 * Test the assembling of a entity Provider to an data transfer object
	 * ProviderDTO
	 */
	@Test
	public void testAssembleProviderDTO1() {
		ProviderDTO dto = porsCore.assembleProviderDTO(provider);
		Assert.assertEquals(dto.getId(), provider.getId());
		Assert.assertEquals(dto.getLanr(), provider.getLanr());
		Assert.assertEquals(dto.getMiddleName(), provider.getMiddleName());
		Assert.assertEquals(dto.getNamePrefix(), provider.getNamePrefix());
		Assert.assertEquals(dto.getNameSuffix(), provider.getNameSuffix());
		Assert.assertEquals(dto.getFirstname(), provider.getFirstName());
		Assert.assertEquals(dto.getSessionId(), provider.getSessionId());
		Assert.assertEquals(dto.getOid(), provider.getOid());
		Assert.assertEquals(dto.getLastname(), provider.getLastName());
		Assert.assertEquals(dto.getMiddleName(), provider.getMiddleName());
		Assert.assertEquals(dto.getLanr(), provider.getLanr());
		Assert.assertEquals(dto.getSpecialisation(),
				provider.getSpecialisation());
		Assert.assertEquals(dto.getVersion(), provider.getVersion());
		Assert.assertEquals(dto.getOid(), provider.getOid());
		if (dto.getPorsuserid() == null && provider.getUser() != null
				|| dto.getPorsuserid() != null && provider.getUser() == null) {
			Assert.fail();
		} else if (provider.getUser() != null) {
			Assert.assertEquals(dto.getPorsuserid(), provider.getUser().getId());
		}
		Assert.assertEquals(dto.getNamePrefix(), provider.getNamePrefix());
		Assert.assertEquals(dto.getNameSuffix(), provider.getNameSuffix());
		Assert.assertEquals(dto.getGenderCode(), provider.getGenderCode());
		if (dto.getBirthday() == null && provider.getBirthday() != null
				|| dto.getBirthday() != null && provider.getBirthday() == null) {
			Assert.fail();
		} else if (dto.getBirthday() != null && provider.getBirthday() != null) {
			Assert.assertEquals(dto.getBirthday().getTime(), provider
					.getBirthday().getTime());
		}
		Assert.assertEquals(dto.getEmail(), provider.getEmail());
		Assert.assertEquals(dto.getTelephone(), provider.getTelephone());
		Assert.assertEquals(dto.getFax(), provider.getFax());
		if (dto.getDeactivationDate() == null
				&& provider.getDeactivationDate() != null
				|| dto.getDeactivationDate() != null
				&& provider.getDeactivationDate() == null) {
			Assert.fail();
		} else if (dto.getDeactivationDate() != null
				&& provider.getDeactivationDate() != null) {
			Assert.assertEquals(dto.getDeactivationDate().getTime(), provider
					.getDeactivationDate().getTime());
		}
		Assert.assertEquals(dto.getDeactivationReasonCode(),
				provider.getDeactivationReasonCode());
		if (dto.getReactivationDate() == null
				&& provider.getReactivationDate() != null
				|| dto.getReactivationDate() != null
				&& provider.getReactivationDate() == null) {
			Assert.fail();
		} else if (dto.getReactivationDate() != null
				&& provider.getReactivationDate() != null) {
			Assert.assertEquals(dto.getReactivationDate().getTime(), provider
					.getReactivationDate().getTime());
		}
		Assert.assertEquals(dto.getReactivationReasonCode(),
				provider.getReactivationReasonCode());
		if (dto.getLastUpdateDate() == null
				&& provider.getLastUpdateDate() != null
				|| dto.getLastUpdateDate() != null
				&& provider.getLastUpdateDate() == null) {
			Assert.fail();
		} else if (dto.getLastUpdateDate() != null
				&& provider.getLastUpdateDate() != null) {
			Assert.assertEquals(dto.getLastUpdateDate().getTime(), provider
					.getLastUpdateDate().getTime());
		}

		if (dto.getEditingUserID() == null && provider.getEditingUser() != null
				|| dto.getEditingUserID() != null
				&& provider.getEditingUser() == null) {
			Assert.fail();
		} else if (provider.getEditingUser() != null) {
			Assert.assertEquals(dto.getEditingUserID(), provider
					.getEditingUser().getId());
		}

		Assert.assertEquals(dto.getSessionId(), provider.getSessionId());
		Assert.assertEquals(dto.getIp(), provider.getIpAddress());

		if (dto.getAddresses() != null && provider.getAddresses() == null
				|| dto.getAddresses() == null
				&& provider.getAddresses() != null) {
			Assert.fail();
		} else if (dto.getAddresses() != null
				&& provider.getAddresses() != null) {
			Assert.assertTrue(dto.getAddresses().size() == provider
					.getAddresses().size());
		}

		if (dto.getLocalId() != null && provider.getLocalIds() == null
				|| dto.getLocalId() == null && provider.getLocalIds() != null) {
			Assert.fail();
		} else if (dto.getLocalId() != null && provider.getLocalIds() != null) {
			Assert.assertTrue(dto.getLocalId().size() == provider.getLocalIds()
					.size());
		}

		if (dto.getOrganisations() != null
				&& provider.getOrganisations() == null
				|| dto.getOrganisations() == null
				&& provider.getOrganisations() != null) {
			Assert.fail();
		} else if (dto.getOrganisations() != null
				&& provider.getOrganisations() != null) {
			Assert.assertTrue(dto.getOrganisations().size() == provider
					.getOrganisations().size());
		}
	}

	/**
	 * Test the assembling of a data transfer object Provider to an entity
	 * ProviderDTO
	 */
	@Test
	public void testAssembleProvider1() {
		Provider prov = porsCore.assembleProvider(providerDTO);
		Assert.assertEquals(prov.getId(), providerDTO.getId());
		Assert.assertEquals(prov.getLanr(), providerDTO.getLanr());
		Assert.assertEquals(prov.getMiddleName(), providerDTO.getMiddleName());
		Assert.assertEquals(prov.getNamePrefix(), providerDTO.getNamePrefix());
		Assert.assertEquals(prov.getNameSuffix(), providerDTO.getNameSuffix());
		Assert.assertEquals(prov.getFirstName(), providerDTO.getFirstname());
		Assert.assertEquals(prov.getSessionId(), providerDTO.getSessionId());
		Assert.assertEquals(prov.getOid(), providerDTO.getOid());
		Assert.assertEquals(prov.getLastName(), providerDTO.getLastname());
		Assert.assertEquals(prov.getMiddleName(), providerDTO.getMiddleName());
		Assert.assertEquals(prov.getLanr(), providerDTO.getLanr());
		Assert.assertEquals(prov.getSpecialisation(),
				providerDTO.getSpecialisation());
		Assert.assertEquals(prov.getVersion(), providerDTO.getVersion());
		Assert.assertEquals(prov.getOid(), providerDTO.getOid());
		if (prov.getUser().getId() == null
				&& providerDTO.getPorsuserid() != null
				|| prov.getUser().getId() != null
				&& providerDTO.getPorsuserid() == null) {
			Assert.fail();
		} else if (providerDTO.getPorsuserid() != null) {
			Assert.assertEquals(prov.getUser().getId(),
					providerDTO.getPorsuserid());
		}
		Assert.assertEquals(prov.getNamePrefix(), providerDTO.getNamePrefix());
		Assert.assertEquals(prov.getNameSuffix(), providerDTO.getNameSuffix());
		Assert.assertEquals(prov.getGenderCode(), providerDTO.getGenderCode());
		if (prov.getBirthday() == null && providerDTO.getBirthday() != null
				|| prov.getBirthday() != null
				&& providerDTO.getBirthday() == null) {
			Assert.fail();
		} else if (prov.getBirthday() != null
				&& providerDTO.getBirthday() != null) {
			Assert.assertEquals(prov.getBirthday().getTime(), providerDTO
					.getBirthday().getTime());
		}
		Assert.assertEquals(prov.getEmail(), providerDTO.getEmail());
		Assert.assertEquals(prov.getTelephone(), providerDTO.getTelephone());
		Assert.assertEquals(prov.getFax(), providerDTO.getFax());
		if (prov.getDeactivationDate() == null
				&& providerDTO.getDeactivationDate() != null
				|| prov.getDeactivationDate() != null
				&& providerDTO.getDeactivationDate() == null) {
			Assert.fail();
		} else if (prov.getDeactivationDate() != null
				&& providerDTO.getDeactivationDate() != null) {
			Assert.assertEquals(prov.getDeactivationDate().getTime(),
					providerDTO.getDeactivationDate().getTime());
		}
		Assert.assertEquals(prov.getDeactivationReasonCode(),
				providerDTO.getDeactivationReasonCode());
		if (prov.getReactivationDate() == null
				&& providerDTO.getReactivationDate() != null
				|| prov.getReactivationDate() != null
				&& providerDTO.getReactivationDate() == null) {
			Assert.fail();
		} else if (prov.getReactivationDate() != null
				&& providerDTO.getReactivationDate() != null) {
			Assert.assertEquals(prov.getReactivationDate().getTime(),
					providerDTO.getReactivationDate().getTime());
		}
		Assert.assertEquals(prov.getReactivationReasonCode(),
				providerDTO.getReactivationReasonCode());
		if (prov.getLastUpdateDate() == null
				&& providerDTO.getLastUpdateDate() != null
				|| prov.getLastUpdateDate() != null
				&& providerDTO.getLastUpdateDate() == null) {
			Assert.fail();
		} else if (prov.getLastUpdateDate() != null
				&& providerDTO.getLastUpdateDate() != null) {
			Assert.assertEquals(prov.getLastUpdateDate().getTime(), providerDTO
					.getLastUpdateDate().getTime());
		}

		if (prov.getEditingUser().getId() == null
				&& providerDTO.getEditingUserID() != null
				|| prov.getEditingUser().getId() != null
				&& providerDTO.getEditingUserID() == null) {
			Assert.fail();
		} else if (providerDTO.getEditingUserID() != null) {
			Assert.assertEquals(prov.getEditingUser().getId(),
					providerDTO.getEditingUserID());
		}

		Assert.assertEquals(prov.getSessionId(), providerDTO.getSessionId());
		Assert.assertEquals(prov.getIpAddress(), providerDTO.getIp());

		if (prov.getAddresses() != null && providerDTO.getAddresses() == null
				|| prov.getAddresses() == null
				&& providerDTO.getAddresses() != null) {
			Assert.fail();
		} else if (prov.getAddresses() != null
				&& providerDTO.getAddresses() != null) {
			Assert.assertTrue(prov.getAddresses().size() == providerDTO
					.getAddresses().size());
		}

		if (prov.getLocalIds() != null && providerDTO.getLocalId() == null
				|| prov.getLocalIds() == null
				&& providerDTO.getLocalId() != null) {
			Assert.fail();
		} else if (prov.getLocalIds() != null
				&& providerDTO.getLocalId() != null) {
			Assert.assertTrue(prov.getLocalIds().size() == providerDTO
					.getLocalId().size());
		}

		if (prov.getOrganisations() != null
				&& providerDTO.getOrganisations() == null
				|| prov.getOrganisations() == null
				&& providerDTO.getOrganisations() != null) {
			Assert.fail();
		} else if (prov.getOrganisations() != null
				&& providerDTO.getOrganisations() != null) {
			Assert.assertTrue(prov.getOrganisations().size() == providerDTO
					.getOrganisations().size());
		}
	}

	/**
	 * Test the assembling of a entity ProviderLog to an data transfer object
	 * ProviderLogDTO
	 */
	@Test
	public void testAssembleProviderLogDTO() {
		ProviderLogDTO plDTO = porsCore.assembleProviderLogDTO(providerLog);
		Assert.assertEquals(plDTO.getIPAddress(), providerLog.getIPAddress());
		Assert.assertEquals(plDTO.getId(), providerLog.getId());
		Assert.assertEquals(plDTO.getLogTime(), providerLog.getLogTime());
		Assert.assertEquals(plDTO.getNewBirthday(),
				providerLog.getNewBirthday());
		Assert.assertEquals(plDTO.getNewDeactivationDate(),
				providerLog.getNewDeactivationDate());
		Assert.assertEquals(plDTO.getNewDeactivationReasonCode(),
				providerLog.getNewDeactivationReasonCode());
		Assert.assertEquals(plDTO.getNewEmail(), providerLog.getNewEmail());
		Assert.assertEquals(plDTO.getNewFax(), providerLog.getNewFax());
		Assert.assertEquals(plDTO.getNewFirstName(),
				providerLog.getNewFirstName());
		Assert.assertEquals(plDTO.getNewGenderCode(),
				providerLog.getNewGenderCode());
		Assert.assertEquals(plDTO.getNewLanr(), providerLog.getNewLanr());
		Assert.assertEquals(plDTO.getNewLastName(),
				providerLog.getNewLastName());
		Assert.assertEquals(plDTO.getNewLastUpdateDate(),
				providerLog.getNewLastUpdateDate());
		Assert.assertEquals(plDTO.getNewMiddleName(),
				providerLog.getNewMiddleName());
		Assert.assertEquals(plDTO.getNewNamePrefix(),
				providerLog.getNewNamePrefix());
		Assert.assertEquals(plDTO.getNewNameSuffix(),
				providerLog.getNewNameSuffix());
		Assert.assertEquals(plDTO.getNewOid(), providerLog.getNewOid());
		Assert.assertEquals(plDTO.getNewReactivationDate(),
				providerLog.getNewReactivationDate());
		Assert.assertEquals(plDTO.getNewReactivationReasonCode(),
				providerLog.getNewReactivationReasonCode());
		Assert.assertEquals(plDTO.getNewSpecialisation(),
				providerLog.getNewSpecialisation());
		Assert.assertEquals(plDTO.getNewTelephone(),
				providerLog.getNewTelephone());
		Assert.assertEquals(plDTO.getOldBirthday(),
				providerLog.getOldBirthday());
		Assert.assertEquals(plDTO.getOldDeactivationDate(),
				providerLog.getOldDeactivationDate());
		Assert.assertEquals(plDTO.getOldDeactivationReasonCode(),
				providerLog.getOldDeactivationReasonCode());
		Assert.assertEquals(plDTO.getOldEmail(), providerLog.getOldEmail());
		Assert.assertEquals(plDTO.getOldFax(), providerLog.getOldFax());
		Assert.assertEquals(plDTO.getOldFirstName(),
				providerLog.getOldFirstName());
		Assert.assertEquals(plDTO.getOldGenderCode(),
				providerLog.getOldGenderCode());
		Assert.assertEquals(plDTO.getOldLanr(), providerLog.getOldLanr());
		Assert.assertEquals(plDTO.getOldLastName(),
				providerLog.getOldLastName());
		Assert.assertEquals(plDTO.getOldLastUpdateDate(),
				providerLog.getOldLastUpdateDate());
		Assert.assertEquals(plDTO.getOldMiddleName(),
				providerLog.getOldMiddleName());
		Assert.assertEquals(plDTO.getOldNamePrefix(),
				providerLog.getOldNamePrefix());
		Assert.assertEquals(plDTO.getOldNameSuffix(),
				providerLog.getOldNameSuffix());
		Assert.assertEquals(plDTO.getOldOid(), providerLog.getOldOid());
		Assert.assertEquals(plDTO.getOldReactivationDate(),
				providerLog.getOldReactivationDate());
		Assert.assertEquals(plDTO.getOldReactivationReasonCode(),
				providerLog.getOldReactivationReasonCode());
		Assert.assertEquals(plDTO.getOldSpecialisation(),
				providerLog.getOldSpecialisation());
		Assert.assertEquals(plDTO.getOldTelephone(),
				providerLog.getOldTelephone());
		// Assert.assertEquals(plDTO.getPorsuserID(), providerLog.getUser());
		Assert.assertEquals(plDTO.getRegionalProviderId(),
				providerLog.getRegionalProviderId());
		Assert.assertEquals(plDTO.getSessionId(), providerLog.getSessionId());
		Assert.assertEquals(plDTO.getTableName(), providerLog.getTableName());
		Assert.assertEquals(plDTO.getTriggerType(),
				providerLog.getTriggerType());
	}

	/**
	 * Test the assembling of a entity Organisation to an data transfer object
	 * OrganisationDTO
	 */
	@Test
	public void testAssembleOrganisationDTO1() {
		OrganisationDTO dto = porsCore.assembleOrganisationDTO(organisation);
		Assert.assertEquals(dto.getId(), organisation.getId());
		Assert.assertEquals(dto.getName(), organisation.getName());
		Assert.assertEquals(dto.getSecondName(), organisation.getSecondName());
		Assert.assertEquals(dto.getOID(), organisation.getOid());
		Assert.assertEquals(dto.getEstablishmentID(),
				organisation.getEstablishmentId());
		Assert.assertEquals(dto.getEmail(), organisation.getEmail());
		Assert.assertEquals(dto.getTelephone(), organisation.getTelephone());
		Assert.assertEquals(dto.getFax(), organisation.getFax());
		Assert.assertEquals(dto.getVersion(), organisation.getVersion());

		if (dto.getPorsuserID() == null && organisation.getUser() != null
				|| dto.getPorsuserID() != null
				&& organisation.getUser() == null) {
			Assert.fail();
		} else if (organisation.getUser() != null) {
			Assert.assertEquals(dto.getPorsuserID(), organisation.getUser()
					.getId());
		}
		if (dto.getEditingUserID() == null
				&& organisation.getEditingUser() != null
				|| dto.getEditingUserID() != null
				&& organisation.getEditingUser() == null) {
			Assert.fail();
		} else if (organisation.getEditingUser() != null) {
			Assert.assertEquals(dto.getEditingUserID(), organisation
					.getEditingUser().getId());
		}

		if (dto.getDeactivationDate() == null
				&& organisation.getDeactivationDate() != null
				|| dto.getDeactivationDate() != null
				&& organisation.getDeactivationDate() == null) {
			Assert.fail();
		} else if (dto.getDeactivationDate() != null
				&& organisation.getDeactivationDate() != null) {
			Assert.assertEquals(dto.getDeactivationDate().getTime(),
					organisation.getDeactivationDate().getTime());
		}
		Assert.assertEquals(dto.getDeactivationReasonCode(),
				organisation.getDeactivationReasonCode());

		if (dto.getReactivationDate() == null
				&& organisation.getReactivationDate() != null
				|| dto.getReactivationDate() != null
				&& organisation.getReactivationDate() == null) {
			Assert.fail();
		} else if (dto.getReactivationDate() != null
				&& organisation.getReactivationDate() != null) {
			Assert.assertEquals(dto.getReactivationDate().getTime(),
					organisation.getReactivationDate().getTime());
		}
		Assert.assertEquals(dto.getReactivationReasonCode(),
				organisation.getReactivationReasonCode());

		if (dto.getLastUpdateDate() == null
				&& organisation.getLastUpdateDate() != null
				|| dto.getLastUpdateDate() != null
				&& organisation.getLastUpdateDate() == null) {
			Assert.fail();
		} else if (dto.getLastUpdateDate() != null
				&& organisation.getLastUpdateDate() != null) {
			Assert.assertEquals(dto.getLastUpdateDate().getTime(), organisation
					.getLastUpdateDate().getTime());
		}

		Assert.assertEquals(dto.getSessionId(), organisation.getSessionId());
		Assert.assertEquals(dto.getIp(), organisation.getIpAddress());

		if (dto.getAddresses() != null && organisation.getAddresses() == null
				|| dto.getAddresses() == null
				&& organisation.getAddresses() != null) {
			Assert.fail();
		} else if (dto.getAddresses() != null
				&& organisation.getAddresses() != null) {
			Assert.assertTrue(dto.getAddresses().size() == organisation
					.getAddresses().size());
		}

		if (dto.getLocalId() != null && organisation.getLocalIds() == null
				|| dto.getLocalId() == null
				&& organisation.getLocalIds() != null) {
			Assert.fail();
		} else if (dto.getLocalId() != null
				&& organisation.getLocalIds() != null) {
			Assert.assertTrue(dto.getLocalId().size() == organisation
					.getLocalIds().size());
		}
	}

	/**
	 * Test the assembling of a data transfer object Organisation to an entity
	 * OrganisationDTO
	 */
	@Test
	public void testAssembleOrganisation1() {
		Organisation orga = porsCore.assembleOrganisation(organisationDTO);
		Assert.assertEquals(orga.getId(), organisationDTO.getId());
		Assert.assertEquals(orga.getName(), organisationDTO.getName());
		Assert.assertEquals(orga.getSecondName(),
				organisationDTO.getSecondName());
		Assert.assertEquals(orga.getOid(), organisationDTO.getOID());
		Assert.assertEquals(orga.getEstablishmentId(),
				organisationDTO.getEstablishmentID());
		Assert.assertEquals(orga.getEmail(), organisationDTO.getEmail());
		Assert.assertEquals(orga.getTelephone(), organisationDTO.getTelephone());
		Assert.assertEquals(orga.getFax(), organisationDTO.getFax());
		Assert.assertEquals(orga.getVersion(), organisationDTO.getVersion());

		if (orga.getUser().getId() == null
				&& organisationDTO.getPorsuserID() != null
				|| orga.getUser().getId() != null
				&& organisationDTO.getPorsuserID() == null) {
			Assert.fail();
		} else if (organisationDTO.getPorsuserID() != null) {
			Assert.assertEquals(orga.getUser().getId(),
					organisationDTO.getPorsuserID());
		}
		if (orga.getEditingUser().getId() == null
				&& organisationDTO.getEditingUserID() != null
				|| orga.getEditingUser().getId() != null
				&& organisationDTO.getEditingUserID() == null) {
			Assert.fail();
		} else if (organisationDTO.getEditingUserID() != null) {
			Assert.assertEquals(orga.getEditingUser().getId(),
					organisationDTO.getEditingUserID());
		}

		if (orga.getDeactivationDate() == null
				&& organisationDTO.getDeactivationDate() != null
				|| orga.getDeactivationDate() != null
				&& organisationDTO.getDeactivationDate() == null) {
			Assert.fail();
		} else if (orga.getDeactivationDate() != null
				&& organisationDTO.getDeactivationDate() != null) {
			Assert.assertEquals(orga.getDeactivationDate().getTime(),
					organisationDTO.getDeactivationDate().getTime());
		}
		Assert.assertEquals(orga.getDeactivationReasonCode(),
				organisationDTO.getDeactivationReasonCode());

		if (orga.getReactivationDate() == null
				&& organisationDTO.getReactivationDate() != null
				|| orga.getReactivationDate() != null
				&& organisationDTO.getReactivationDate() == null) {
			Assert.fail();
		} else if (orga.getReactivationDate() != null
				&& organisationDTO.getReactivationDate() != null) {
			Assert.assertEquals(orga.getReactivationDate().getTime(),
					organisationDTO.getReactivationDate().getTime());
		}
		Assert.assertEquals(orga.getReactivationReasonCode(),
				organisationDTO.getReactivationReasonCode());

		if (orga.getLastUpdateDate() == null
				&& organisationDTO.getLastUpdateDate() != null
				|| orga.getLastUpdateDate() != null
				&& organisationDTO.getLastUpdateDate() == null) {
			Assert.fail();
		} else if (orga.getLastUpdateDate() != null
				&& organisationDTO.getLastUpdateDate() != null) {
			Assert.assertEquals(orga.getLastUpdateDate().getTime(),
					organisationDTO.getLastUpdateDate().getTime());
		}

		Assert.assertEquals(orga.getSessionId(), organisationDTO.getSessionId());
		Assert.assertEquals(orga.getIpAddress(), organisationDTO.getIp());

		if (orga.getAddresses() != null
				&& organisationDTO.getAddresses() == null
				|| orga.getAddresses() == null
				&& organisationDTO.getAddresses() != null) {
			Assert.fail();
		} else if (orga.getAddresses() != null
				&& organisationDTO.getAddresses() != null) {
			Assert.assertTrue(orga.getAddresses().size() == organisationDTO
					.getAddresses().size());
		}

		if (orga.getLocalIds() != null && organisationDTO.getLocalId() == null
				|| orga.getLocalIds() == null
				&& organisationDTO.getLocalId() != null) {
			Assert.fail();
		} else if (orga.getLocalIds() != null
				&& organisationDTO.getLocalId() != null) {
			Assert.assertTrue(orga.getLocalIds().size() == organisationDTO
					.getLocalId().size());
		}
	}

	/**
	 * Test the assembling of a entity OrganisationLog to an data transfer
	 * object OrganisationLogDTO
	 */
	@Test
	public void testAssembleOrganisationLogDTO() {
		OrganisationLogDTO olDTO = porsCore
				.assembleOrganisationLogDTO(organisationLog);
		Assert.assertEquals(Long.valueOf(olDTO.getId()),
				Long.valueOf(organisationLog.getId()));
		Assert.assertEquals(olDTO.getIPAddress(),
				organisationLog.getIPAddress());
		Assert.assertEquals(olDTO.getNewDeactivationReasonCode(),
				organisationLog.getNewDeactivationReasonCode());
		Assert.assertEquals(olDTO.getNewDescription(),
				organisationLog.getNewDescription());
		Assert.assertEquals(olDTO.getNewEmail(), organisationLog.getNewEMail());
		Assert.assertEquals(olDTO.getNewEstablishmentID(),
				organisationLog.getNewEstablishmentId());
		Assert.assertEquals(olDTO.getNewFax(), organisationLog.getNewFax());
		Assert.assertEquals(olDTO.getNewName(), organisationLog.getNewName());
		Assert.assertEquals(olDTO.getNewOID(), organisationLog.getNewOid());
		Assert.assertEquals(olDTO.getNewReactivationReasonCode(),
				organisationLog.getNewReactivationReasonCode());
		Assert.assertEquals(olDTO.getNewSecondname(),
				organisationLog.getNewSecondName());
		Assert.assertEquals(olDTO.getNewTelephone(),
				organisationLog.getNewTelephone());
		Assert.assertEquals(olDTO.getOldDeactivationReasonCode(),
				organisationLog.getOldDeactivationReasonCode());
		Assert.assertEquals(olDTO.getOldDescription(),
				organisationLog.getOldDescription());
		Assert.assertEquals(olDTO.getOldEmail(), organisationLog.getOldEMail());
		Assert.assertEquals(olDTO.getOldEstablishmentID(),
				organisationLog.getOldEstablishmentId());
		Assert.assertEquals(olDTO.getOldFax(), organisationLog.getOldFax());
		Assert.assertEquals(olDTO.getOldName(), organisationLog.getOldName());
		Assert.assertEquals(olDTO.getOldOID(), organisationLog.getOldOid());
		Assert.assertEquals(olDTO.getOldReactivationReasonCode(),
				organisationLog.getOldReactivationReasonCode());
		Assert.assertEquals(olDTO.getOldSecondname(),
				organisationLog.getOldSecondName());
		Assert.assertEquals(olDTO.getOldTelephone(),
				organisationLog.getOldTelephone());
		Assert.assertEquals(olDTO.getSessionId(),
				organisationLog.getSessionId());
		Assert.assertEquals(olDTO.getTableName(),
				organisationLog.getTableName());
		Assert.assertEquals(olDTO.getTriggerType(),
				organisationLog.getTriggerType());
		Assert.assertEquals(olDTO.getLogTime(), organisationLog.getLogTime());
		Assert.assertEquals(olDTO.getNewDeactivationDate(),
				organisationLog.getNewDeactivationDate());
		Assert.assertEquals(olDTO.getNewLastUpdateDate(),
				organisationLog.getNewLastUpdateDate());
		Assert.assertEquals(Integer.valueOf(olDTO.getNewPorsuserID()),
				Integer.valueOf(organisationLog.getNewPorsUserId()));
		Assert.assertEquals(olDTO.getNewReactivationDate(),
				organisationLog.getNewReactivationDate());
		Assert.assertEquals(olDTO.getOldDeactivationDate(),
				organisationLog.getOldDeactivationDate());
		Assert.assertEquals(olDTO.getOldLastUpdateDate(),
				organisationLog.getOldLastUpdateDate());
		Assert.assertEquals(Integer.valueOf(olDTO.getOldPorsuserID()),
				Integer.valueOf(organisationLog.getOldPorsUserId()));
		Assert.assertEquals(olDTO.getOldReactivationDate(),
				organisationLog.getOldReactivationDate());
	}

	/**
	 * Test the assembling of a entity Address to an data transfer object
	 * AddressDTO
	 */
	@Test
	public void testAssembleAddressDTO1() {
		AddressDTO dto = porsCore.assembleAddressDTO(address);
		Assert.assertEquals(dto.getId(), address.getId());
		Assert.assertEquals(dto.getCity(), address.getCity());
		Assert.assertEquals(dto.getStreet(), address.getStreet());
		Assert.assertEquals(dto.getHouseNumber(), address.getHouseNumber());
		Assert.assertEquals(dto.getState(), address.getState());
		Assert.assertEquals(dto.getCountry(), address.getCountry());
		Assert.assertEquals(dto.getAdditional(), address.getAdditional());
		Assert.assertEquals(dto.getZipCode(), address.getZipCode());
	}

	/**
	 * Test the assembling of a data transfer object Address to an entity
	 * AddressDTO
	 */
	@Test
	public void testAssembleAddress1() {
		Address adr = porsCore.assembleAddress(addressDTO);
		Assert.assertEquals(adr.getId(), addressDTO.getId());
		Assert.assertEquals(adr.getCity(), addressDTO.getCity());
		Assert.assertEquals(adr.getStreet(), addressDTO.getStreet());
		Assert.assertEquals(adr.getHouseNumber(), addressDTO.getHouseNumber());
		Assert.assertEquals(adr.getState(), addressDTO.getState());
		Assert.assertEquals(adr.getCountry(), addressDTO.getCountry());
		Assert.assertEquals(adr.getAdditional(), addressDTO.getAdditional());
		Assert.assertEquals(adr.getZipCode(), addressDTO.getZipCode());
	}

	/**
	 * Test the assembling of a entity AddressLog to an data transfer object
	 * AddressLogDTO
	 */
	@Test
	public void testAssembleAcdressLogDTO() {
		AddressLogDTO aLog = porsCore.assembleAddressLogDTO(addressLog);
		Assert.assertEquals(aLog.getIPAddress(), addressLog.getIPAddress());
		Assert.assertEquals(aLog.getNewAdditional(),
				addressLog.getNewAdditional());
		Assert.assertEquals(aLog.getNewCity(), addressLog.getNewCity());
		Assert.assertEquals(aLog.getNewCountry(), addressLog.getNewCountry());
		Assert.assertEquals(aLog.getNewHouseNumber(),
				addressLog.getNewHouseNumber());
		Assert.assertEquals(aLog.getNewState(), addressLog.getNewState());
		Assert.assertEquals(aLog.getNewStreet(), addressLog.getNewStreet());
		Assert.assertEquals(aLog.getNewZipCode(), addressLog.getNewZipCode());
		Assert.assertEquals(aLog.getOldAdditional(),
				addressLog.getOldAdditional());
		Assert.assertEquals(aLog.getOldCity(), addressLog.getOldCity());
		Assert.assertEquals(aLog.getOldCountry(), addressLog.getOldCountry());
		Assert.assertEquals(aLog.getOldHouseNumber(),
				addressLog.getOldHouseNumber());
		Assert.assertEquals(aLog.getOldState(), addressLog.getOldState());
		Assert.assertEquals(aLog.getOldStreet(), addressLog.getOldStreet());
		Assert.assertEquals(aLog.getOldZipCode(), addressLog.getOldZipCode());
		Assert.assertEquals(aLog.getSessionId(), addressLog.getSessionId());
		Assert.assertEquals(aLog.getTableName(), addressLog.getTableName());
		Assert.assertEquals(aLog.getTriggerType(), addressLog.getTriggerType());
		Assert.assertEquals(aLog.getAddressId(), addressLog.getAddressId());
		Assert.assertEquals(aLog.getId(), addressLog.getId());
		Assert.assertEquals(aLog.getLogTime(), addressLog.getLogTime());
		Assert.assertEquals(aLog.getPorsuserID(), addressLog.getUser().getId());
	}

	/**
	 * Test the assembling of a entity LocalId to an data transfer object
	 * LocalIdDTO
	 */
	@Test
	public void testAssembleLocalIdDTO1() {
		LocalIdDTO dto = porsCore.assembleLocalIdDTO(localIdProvider);
		Assert.assertEquals(dto.getId(), localIdProvider.getId());
		Assert.assertEquals(dto.getLocalId(), localIdProvider.getLocalId());
		Assert.assertEquals(dto.getApplication(),
				localIdProvider.getApplication());
		Assert.assertEquals(dto.getFacility(), localIdProvider.getFacility());
		Assert.assertEquals(dto.getProviderFirstname(), localIdProvider
				.getProvider().getFirstName());
		Assert.assertEquals(dto.getProviderLastname(), localIdProvider
				.getProvider().getLastName());
		Assert.assertEquals(dto.getProviderId(), localIdProvider.getProvider()
				.getId());

		if (dto.getOrganisationId() == null
				&& localIdProvider.getOrganisation().getId() != null
				|| dto.getOrganisationId() != null
				&& localIdProvider.getOrganisation().getId() == null) {
			Assert.fail();
		} else if (dto.getOrganisationId() != null
				&& localIdProvider.getOrganisation().getId() != null) {
			Assert.assertEquals(dto.getOrganisationId(), localIdProvider
					.getOrganisation().getId());
		}

		if (dto.getOrganisationName() == null
				&& localIdProvider.getOrganisation().getName() != null
				|| dto.getOrganisationName() != null
				&& localIdProvider.getOrganisation().getName() == null) {
			Assert.fail();
		} else if (dto.getOrganisationName() != null
				&& localIdProvider.getOrganisation().getName() != null) {
			Assert.assertEquals(dto.getOrganisationName(), localIdProvider
					.getOrganisation().getName());
		}
	}

	/**
	 * Test the assembling of a entity LocalId to an data transfer object
	 * LocalIdDTO
	 */
	@Test
	public void testAssembleLocalIdDTO2() {
		LocalIdDTO dto = porsCore.assembleLocalIdDTO(localIdOrganisation);
		Assert.assertEquals(dto.getId(), localIdOrganisation.getId());
		Assert.assertEquals(dto.getLocalId(), localIdOrganisation.getLocalId());
		Assert.assertEquals(dto.getApplication(),
				localIdOrganisation.getApplication());
		Assert.assertEquals(dto.getFacility(),
				localIdOrganisation.getFacility());
		Assert.assertEquals(dto.getOrganisationName(), localIdOrganisation
				.getOrganisation().getName());
		Assert.assertEquals(dto.getOrganisationId(), localIdOrganisation
				.getOrganisation().getId());

		if (dto.getProviderId() == null
				&& localIdOrganisation.getProvider().getId() != null
				|| dto.getProviderId() != null
				&& localIdOrganisation.getProvider().getId() == null) {
			Assert.fail();
		} else if (dto.getProviderId() != null
				&& localIdOrganisation.getProvider().getId() != null) {
			Assert.assertEquals(dto.getProviderId(), localIdOrganisation
					.getProvider().getId());
		}

		if (dto.getProviderFirstname() == null
				&& localIdOrganisation.getProvider().getFirstName() != null
				|| dto.getProviderFirstname() != null
				&& localIdOrganisation.getProvider().getFirstName() == null) {
			Assert.fail();
		} else if (dto.getProviderFirstname() != null
				&& localIdOrganisation.getProvider().getFirstName() != null) {
			Assert.assertEquals(dto.getProviderFirstname(), localIdOrganisation
					.getProvider().getFirstName());
		}

		if (dto.getProviderLastname() == null
				&& localIdOrganisation.getProvider().getLastName() != null
				|| dto.getProviderLastname() != null
				&& localIdOrganisation.getProvider().getLastName() == null) {
			Assert.fail();
		} else if (dto.getProviderLastname() != null
				&& localIdOrganisation.getProvider().getLastName() != null) {
			Assert.assertEquals(dto.getProviderLastname(), localIdOrganisation
					.getProvider().getLastName());
		}
	}

	/**
	 * Test the assembling of a entity LocalIdLog to an data transfer object
	 * LocalIdLogDTO
	 */
	@Test
	public void testAssembleLocalIdLogDTO() {
		LocalIdLogDTO lLog = porsCore.assembleLocalIDLogDTO(lidLog);
		Assert.assertEquals(lLog.getAction(), lidLog.getAction());
		Assert.assertEquals(lLog.getIPAddress(), lidLog.getIPAddress());
		Assert.assertEquals(lLog.getNewApplication(),
				lidLog.getNewApplication());
		Assert.assertEquals(lLog.getNewFacility(), lidLog.getNewFacility());
		Assert.assertEquals(lLog.getNewLocalId(), lidLog.getNewLocalId());
		Assert.assertEquals(lLog.getOldApplication(),
				lidLog.getOldApplication());
		Assert.assertEquals(lLog.getOldFacility(), lidLog.getOldFacility());
		Assert.assertEquals(lLog.getOldLocalId(), lidLog.getOldLocalId());
		Assert.assertEquals(lLog.getSessionId(), lidLog.getSessionId());
		Assert.assertEquals(lLog.getTableName(), lidLog.getTableName());
		Assert.assertEquals(lLog.getId(), lidLog.getId());
		Assert.assertEquals(lLog.getLocalIdId(), lidLog.getLocalIdId());
		Assert.assertEquals(lLog.getLogTime(), lidLog.getLogTime());
		Assert.assertEquals(lLog.getNewRegionalOrganisationId(),
				lidLog.getNewRegionalOrganisationId());
		Assert.assertEquals(lLog.getNewRegionalProviderId(),
				lidLog.getNewRegionalProviderId());
		Assert.assertEquals(lLog.getOldRegionalOrganisationId(),
				lidLog.getOldRegionalOrganisationId());
		Assert.assertEquals(lLog.getOldRegionalProviderId(),
				lidLog.getOldRegionalProviderId());
		Assert.assertEquals(lLog.getPorsuserID(), lidLog.getUser().getId());
	}

	/**
	 * Test the assembling of a entity ProviderHasAddressLog to an data transfer
	 * object ProviderHasAddressLogDTO
	 */
	@Test
	public void testAssembleProviderHasAddressLogDTO() {
		ProviderHasAddressLogDTO lLog = porsCore
				.assembleProviderHasAddressLogDTO(phaLog);
		Assert.assertEquals(lLog.getIPAddress(), phaLog.getIPAddress());
		Assert.assertEquals(lLog.getSessionId(), phaLog.getSessionId());
		Assert.assertEquals(lLog.getTableName(), phaLog.getTableName());
		Assert.assertEquals(lLog.getTriggerType(), phaLog.getTriggerType());
		Assert.assertEquals(Long.valueOf(lLog.getId()),
				Long.valueOf(phaLog.getId()));
		Assert.assertEquals(lLog.getLogTime(), phaLog.getLogTime());
		Assert.assertEquals(lLog.getNewAddressId(), phaLog.getNewAddressId());
		Assert.assertEquals(lLog.getNewRegionalProviderId(),
				phaLog.getNewRegionalProviderId());
		Assert.assertEquals(lLog.getOldAddressId(), phaLog.getOldAddressId());
		Assert.assertEquals(lLog.getOldRegionalProviderId(),
				phaLog.getOldRegionalProviderId());
		Assert.assertEquals(lLog.getPorsUserId(), phaLog.getUser().getId());
	}

	/**
	 * Test the assembling of a entity OrganisationHasAddressLog to an data transfer
	 * object OrganisationHasAddressLogDTO
	 */
	@Test
	public void testAssembleOrganisationHasAddressLogDTO() {
		OrganisationHasAddressLogDTO oLog = porsCore
				.assembleOrganisationHasAddressLogDTO(ohaLog);
		Assert.assertEquals(oLog.getIPAddress(), ohaLog.getIPAddress());
		Assert.assertEquals(oLog.getSessionId(), ohaLog.getSessionId());
		Assert.assertEquals(oLog.getTableName(), ohaLog.getTableName());
		Assert.assertEquals(oLog.getTriggerType(), ohaLog.getTriggerType());
		Assert.assertEquals(oLog.getId(), ohaLog.getId());
		Assert.assertEquals(oLog.getLogTime(), ohaLog.getLogTime());
		Assert.assertEquals(oLog.getNewAddressId(), ohaLog.getNewAddressId());
		Assert.assertEquals(oLog.getNewRegionalOrganisationId(),
				ohaLog.getNewRegionalOrganisationId());
		Assert.assertEquals(oLog.getOldAddressId(), ohaLog.getOldAddressId());
		Assert.assertEquals(oLog.getOldRegionalOrganisationId(),
				ohaLog.getOldRegionalOrganisationId());
		Assert.assertEquals(oLog.getPorsUSerId(), ohaLog.getUser().getId());
	}

	/**
	 * Test the assembling of a entity OrganisationHasProviderLog to an data transfer
	 * object OrganisationHasProviderLogDTO
	 */
	@Test
	public void testAssembleOrganisationHasProviderLogDTO() {
		OrganisationHasProviderLogDTO opLog = porsCore
				.assembleOrganisationHasProviderLogDTO(ohpLog);
		Assert.assertEquals(opLog.getIPAddress(), ohpLog.getIPAddress());
		Assert.assertEquals(opLog.getSessionId(), ohpLog.getSessionId());
		Assert.assertEquals(opLog.getTableName(), ohpLog.getTableName());
		Assert.assertEquals(opLog.getTriggerType(), ohpLog.getTriggerType());
		Assert.assertEquals(opLog.getId(), ohpLog.getId());
		Assert.assertEquals(opLog.getLogTime(), ohpLog.getLogTime());
		Assert.assertEquals(opLog.getNewRegionalOrganisationId(),
				ohpLog.getNewRegionalOrganisationId());
		Assert.assertEquals(opLog.getNewRegionalProviderId(),
				ohpLog.getNewRegionalProviderId());
		Assert.assertEquals(opLog.getOldRegionalOrganisationId(),
				ohpLog.getOldRegionalOrganisationId());
		Assert.assertEquals(opLog.getOldRegionalProviderId(),
				ohpLog.getOldRegionalProviderId());
		Assert.assertEquals(opLog.getPorsUserId(), ohpLog.getUser().getId());
	}

	/**
	 * Test the assembling of a entity LoggingEntry to an data transfer
	 * object LoggingEntryDTO
	 */
	@Test
	public void testAssembleLoggingEntryDTO() {
		LoggingEntryDTO entry = porsCore.assembleLoggingEntryDTO(log);
		Assert.assertEquals(entry.getAction(), log.getAction());
		Assert.assertEquals(entry.getDomain(), log.getDomain());
		Assert.assertEquals(entry.getLogDateString(), DateFormat
				.getDateInstance(DateFormat.MEDIUM).format(log.getLogTime()));
		Assert.assertEquals(entry.getLogTimeString(),
				dfmt.format(log.getLogTime()));
		Assert.assertEquals(entry.getUserName(), log.getUserName());
		Assert.assertEquals(entry.getLogEntryId(), log.getLogId());
		Assert.assertEquals(entry.getLogTime(), log.getLogTime());
		Assert.assertEquals(entry.getPorsUserId(), log.getUserId());
	}

	/**
	 * Test the assembling of a data transfer object LoggingEntry to an entity
	 * LoggingEntryDTO
	 */
	@Test
	public void testAssembleLoggingEntry() {
		History entry = porsCore.assembleLoggingEntry(logDTO);
		Assert.assertEquals(entry.getAction(), logDTO.getAction());
		Assert.assertEquals(entry.getDomain(), logDTO.getDomain());
		Assert.assertEquals(DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(entry.getLogTime()), logDTO.getLogDateString());
		Assert.assertEquals(dfmt.format(entry.getLogTime()),
				logDTO.getLogTimeString());
		Assert.assertEquals(entry.getUserName(), logDTO.getUserName());
		Assert.assertEquals(entry.getLogId(), logDTO.getLogEntryId());
		Assert.assertEquals(entry.getLogTime(), logDTO.getLogTime());
		Assert.assertEquals(entry.getUserId(), logDTO.getPorsUserId());
	}

	/**
	 * Test the assembling of a entity DuplicateEntry to an data transfer
	 * object DuplicateEntryDTO
	 */
	@Test
	public void testAssembleDuplicateEntryDTO() {
		DuplicateEntryDTO entry = porsCore.assembleDuplicateEntryDTO(duplicate);
		Assert.assertEquals(entry.getDomain(), duplicate.getId().getDomain());
		// Assert.assertEquals(entry.getDuplicateId(), duplicate.getId());
		// Assert.assertEquals(entry.getPercentage(),
		// Double.valueOf(duplicate.getValue()*100));
		Assert.assertEquals(entry.getLogTime(), duplicate.getTimestamp());
		Assert.assertEquals(
				entry.getLogDateString(),
				DateFormat.getDateInstance(DateFormat.MEDIUM).format(
						duplicate.getTimestamp()));
		Assert.assertEquals(entry.getLogTimeString(),
				dfmt.format(duplicate.getTimestamp()));
	}

	/**
	 * Test the assembling of a data transfer object DuplicateEntry to an entity
	 * DuplicateEntryDTO
	 */
	@Test
	public void testAssembleDuplicateEntry() {
		DuplicateEntry entry = porsCore.assembleDuplicateEntry(duplicateDTO);
		// Assert.assertEquals(entry.getId(), duplicateDTO.getDuplicateId());
		Assert.assertEquals(entry.getTimestamp(), duplicateDTO.getLogTime());
		Assert.assertEquals(entry.getValue(),
				Double.valueOf(duplicateDTO.getPercentage()));
		// Assert.assertEquals(entry.getId().getDomain(),
		// duplicateDTO.getDomain());
		Assert.assertEquals(DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(entry.getTimestamp()), duplicateDTO.getLogDateString());
		Assert.assertEquals(dfmt.format(entry.getTimestamp()),
				duplicateDTO.getLogTimeString());
	}

	/**
	 * Test the assembling of a entity SearchCriteria to an data transfer
	 * object SearchCriteriaDTO
	 */
	@Test
	public void testAssembleSearchCriteriaDTO() {
		SearchCriteriaDTO searchDTO = porsCore
				.assembleSearchCriteriaDTO(search);
		Assert.assertEquals(searchDTO.getField(), search.getField());
		Assert.assertEquals(searchDTO.getOperator(), search.getOperator());
		Assert.assertEquals(searchDTO.getValue(), search.getValue());
	}

	/**
	 * Test the assembling of a data transfer object SearchCriteria to an entity
	 * SearchCriteriaDTO
	 */
	@Test
	public void testAssembleSearchCriteria() {
		SearchCriteria search = porsCore.assembleSearchCriteria(searchDTO);
		Assert.assertEquals(search.getField(), searchDTO.getField());
		Assert.assertEquals(search.getOperator(), searchDTO.getOperator());
		Assert.assertEquals(search.getValue(), searchDTO.getValue());
	}

	/**
	 * Test the assembling of a data transfer object Configuration to an entity
	 * ConfigurationDTO
	 */
	@Test
	public void testAssembleConfigurationDTO() {
		List<DuplicateRecognition> dto = porsCore
				.assembleDuplicateConfiguration(configurationDTO);
		for (DuplicateRecognition entry : dto) {
			Assert.assertTrue(configurationMap.containsKey(entry.getName()));
			int value = configurationMap.get(entry.getName());
			Assert.assertTrue(value == 10);
			configurationMap.remove(entry.getName());
		}
		Assert.assertTrue(configurationMap.size() == 0);
	}

	/**
	 * Test the assembling of a data transfer object UserRole to an entity
	 * UserRoleDTO
	 */
	@Test
	public void testAssembleUserRoleDTO() {
		UserRole role = porsCore.assembleRole(roleDTO);
		Assert.assertEquals(role.getDescription(), roleDTO.getDescription());
		Assert.assertEquals(role.getName(), roleDTO.getName());
		Assert.assertEquals(role.getId(), roleDTO.getId());
	}

	/**
	 * Test the assembling of a entity UserRole to an data transfer
	 * object UserRoleDTO
	 */
	@Test
	public void testAssembleUserRole() {
		UserRoleDTO roleDTO = porsCore.assembleRoleDTO(role);
		Assert.assertEquals(roleDTO.getDescription(), role.getDescription());
		Assert.assertEquals(roleDTO.getName(), role.getName());
		Assert.assertEquals(roleDTO.getId(), role.getId());
	}
}
