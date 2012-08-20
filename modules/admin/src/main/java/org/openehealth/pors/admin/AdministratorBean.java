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
package org.openehealth.pors.admin;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.ImportResult;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.dto.PorsCsv;
import org.openehealth.pors.core.exception.CsvExportException;
import org.openehealth.pors.core.exception.CsvImportException;

/**
 * Implementation of IAdministrator
 * 
 * @author mf, ck, tb
 * @author mbirkle
 * 
 * @see IAdministrator
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AdministratorBean implements IAdministrator {

	private static final String PROVIDER_FIELD_ID = "id";
	private static final String PROVIDER_FIELD_LANR = "lanr";
	private static final String PROVIDER_FIELD_OID = "oid";
	private static final String PROVIDER_FIELD_FIRSTNAME = "firstname";
	private static final String PROVIDER_FIELD_LASTNAME = "lastname";
	private static final String PROVIDER_FIELD_MIDDLENAME = "middleName";
	private static final String PROVIDER_FIELD_NAMEPREFIX = "namePrefix";
	private static final String PROVIDER_FIELD_NAMESUFFIX = "nameSuffix";
	private static final String PROVIDER_FIELD_GENDERCODE = "genderCode";
	private static final String PROVIDER_FIELD_BIRTHDAY = "birthday";
	private static final String PROVIDER_FIELD_SPECIALISATION = "specialisation";
	private static final String PROVIDER_FIELD_EMAIL = "email";
	private static final String PROVIDER_FIELD_TELEPHONE = "telephone";
	private static final String PROVIDER_FIELD_FAX = "fax";
	private static final String PROVIDER_FIELD_DEACTIVATION_DATE = "deactivationDate";
	private static final String PROVIDER_FIELD_DEACTIVATION_REASONCODE = "deactivationReasonCode";
	private static final String PROVIDER_FIELD_REACTIVATION_DATE = "reactivationDate";
	private static final String PROVIDER_FIELD_REACTIVATION_REASONCODE = "reactivationReasonCode";
	private static final String PROVIDER_FIELD_LASTUPDATE_DATE = "lastUpdateDate";
	private static final String PROVIDER_FIELD_ADDRESSES = "addresses";
	private static final String PROVIDER_FIELD_LOCALIDS = "localids";

	private static final String ORGANISATION_FIELD_ID = "id";
	private static final String ORGANISATION_FIELD_OID = "oid";
	private static final String ORGANISATION_FIELD_ESTABLISHMENTID = "establishmentid";
	private static final String ORGANISATION_FIELD_NAME = "name";
	private static final String ORGANISATION_FIELD_SECONDNAME = "secondname";
	private static final String ORGANISATION_FIELD_DESCRIPTION = "description";
	private static final String ORGANISATION_FIELD_EMAIL = "email";
	private static final String ORGANISATION_FIELD_TELEPHONE = "telephone";
	private static final String ORGANISATION_FIELD_FAX = "fax";
	private static final String ORGANISATION_FIELD_DEACTIVATION_DATE = "deactivationDate";
	private static final String ORGANISATION_FIELD_DEACTIVATION_REASONCODE = "deactivationReasonCode";
	private static final String ORGANISATION_FIELD_REACTIVATION_DATE = "reactivationDate";
	private static final String ORGANISATION_FIELD_REACTIVATION_REASONCODE = "reactivationReasonCode";
	private static final String ORGANISATION_FIELD_LASTUPDATE_DATE = "lastUpdateDate";
	private static final String ORGANISATION_FIELD_ADDRESSES = "addresses";
	private static final String ORGANISATION_FIELD_LOCALIDS = "localids";

	private static final String DESCRIPTION_HEADER = "// FIELD DESCRIPTION";

	private Set<String> providerAllFields;
	private Set<String> organisationAllFields;
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	private Logger logger;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initialize() {
		logger = Logger.getLogger(AdministratorBean.class);

		providerAllFields = new LinkedHashSet<String>();
		providerAllFields.add(PROVIDER_FIELD_ID);
		providerAllFields.add(PROVIDER_FIELD_LANR);
		providerAllFields.add(PROVIDER_FIELD_OID);
		providerAllFields.add(PROVIDER_FIELD_FIRSTNAME);
		providerAllFields.add(PROVIDER_FIELD_LASTNAME);
		providerAllFields.add(PROVIDER_FIELD_MIDDLENAME);
		providerAllFields.add(PROVIDER_FIELD_NAMEPREFIX);
		providerAllFields.add(PROVIDER_FIELD_NAMESUFFIX);
		providerAllFields.add(PROVIDER_FIELD_GENDERCODE);
		providerAllFields.add(PROVIDER_FIELD_BIRTHDAY);
		providerAllFields.add(PROVIDER_FIELD_SPECIALISATION);
		providerAllFields.add(PROVIDER_FIELD_EMAIL);
		providerAllFields.add(PROVIDER_FIELD_TELEPHONE);
		providerAllFields.add(PROVIDER_FIELD_FAX);
		providerAllFields.add(PROVIDER_FIELD_DEACTIVATION_DATE);
		providerAllFields.add(PROVIDER_FIELD_DEACTIVATION_REASONCODE);
		providerAllFields.add(PROVIDER_FIELD_REACTIVATION_DATE);
		providerAllFields.add(PROVIDER_FIELD_REACTIVATION_REASONCODE);
		providerAllFields.add(PROVIDER_FIELD_LASTUPDATE_DATE);
		providerAllFields.add(PROVIDER_FIELD_ADDRESSES);
		providerAllFields.add(PROVIDER_FIELD_LOCALIDS);

		organisationAllFields = new LinkedHashSet<String>();
		organisationAllFields.add(ORGANISATION_FIELD_ID);
		organisationAllFields.add(ORGANISATION_FIELD_OID);
		organisationAllFields.add(ORGANISATION_FIELD_ESTABLISHMENTID);
		organisationAllFields.add(ORGANISATION_FIELD_NAME);
		organisationAllFields.add(ORGANISATION_FIELD_SECONDNAME);
		organisationAllFields.add(ORGANISATION_FIELD_DESCRIPTION);
		organisationAllFields.add(ORGANISATION_FIELD_EMAIL);
		organisationAllFields.add(ORGANISATION_FIELD_TELEPHONE);
		organisationAllFields.add(ORGANISATION_FIELD_FAX);
		organisationAllFields.add(ORGANISATION_FIELD_DEACTIVATION_DATE);
		organisationAllFields.add(ORGANISATION_FIELD_DEACTIVATION_REASONCODE);
		organisationAllFields.add(ORGANISATION_FIELD_REACTIVATION_DATE);
		organisationAllFields.add(ORGANISATION_FIELD_REACTIVATION_REASONCODE);
		organisationAllFields.add(ORGANISATION_FIELD_LASTUPDATE_DATE);
		organisationAllFields.add(ORGANISATION_FIELD_ADDRESSES);
		organisationAllFields.add(ORGANISATION_FIELD_LOCALIDS);
	}

	/**
	 * @see IAdministrator#importProviders(PorsCsv, List<String>, ImportResult)
	 */
	public List<Task> importProviders(PorsCsv csvData, List<String> fieldList,
			ImportResult result) throws CsvImportException {

		StringReader stringReader = new StringReader(csvData.getContent());
		CsvListReader csvReader = new CsvListReader(stringReader,
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		ArrayList<Task> taskList = new ArrayList<Task>();

		try {

			csvReader.read();
			List<String> csvFieldList = csvReader.read();
			Provider provider;
			String field;
			Task task;
			boolean isUpdate;

			while (null != csvFieldList) {
				isUpdate = false;
				provider = new Provider();
				for (int i = 0; i < csvFieldList.size(); i++) {
					if (null != csvFieldList.get(i)
							&& 0 < csvFieldList.get(i).length()) {
						if (i == 0) {
							if (csvFieldList.get(i).equals(DESCRIPTION_HEADER)) {
								return taskList;
							}
						}
						field = fieldList.get(i);
						if (field.equalsIgnoreCase(PROVIDER_FIELD_ID)) {
							provider.setId(new Long(csvFieldList.get(i)));
							isUpdate = true;
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_LANR)) {
							provider.setLanr(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_OID)) {
							provider.setOid(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_FIRSTNAME)) {
							provider.setFirstName(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_LASTNAME)) {
							provider.setLastName(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_MIDDLENAME)) {
							provider.setMiddleName(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_NAMEPREFIX)) {
							provider.setNamePrefix(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_NAMESUFFIX)) {
							provider.setNameSuffix(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_GENDERCODE)) {
							provider.setGenderCode(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_BIRTHDAY)) {
							provider.setBirthday(dateFormat.parse(csvFieldList
									.get(i)));
							continue;
						}
						if (field
								.equalsIgnoreCase(PROVIDER_FIELD_SPECIALISATION)) {
							provider.setSpecialisation(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_EMAIL)) {
							provider.setEmail(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_TELEPHONE)) {
							provider.setTelephone(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_FAX)) {
							provider.setFax(csvFieldList.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(PROVIDER_FIELD_DEACTIVATION_DATE)) {
							provider.setDeactivationDate(dateFormat
									.parse(csvFieldList.get(i)));
							continue;
						}
						if (field
								.equalsIgnoreCase(PROVIDER_FIELD_DEACTIVATION_REASONCODE)) {
							provider.setDeactivationReasonCode(csvFieldList
									.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(PROVIDER_FIELD_REACTIVATION_DATE)) {
							provider.setReactivationDate(dateFormat
									.parse(csvFieldList.get(i)));
							continue;
						}
						if (field
								.equalsIgnoreCase(PROVIDER_FIELD_REACTIVATION_REASONCODE)) {
							provider.setReactivationReasonCode(csvFieldList
									.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(PROVIDER_FIELD_LASTUPDATE_DATE)) {
							provider.setLastUpdateDate(dateFormat
									.parse(csvFieldList.get(i)));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_ADDRESSES)) {
							logger.info("Administrator Bean, getAddressFromString, for Provider:"
									+ provider.getFirstName()
									+ " "
									+ provider.getLastName());
							provider.setAddresses(getAddressesFromString(csvFieldList
									.get(i)));
							continue;
						}
						if (field.equalsIgnoreCase(PROVIDER_FIELD_LOCALIDS)) {
							provider.setLocalIds(getLocalIdsFromString(csvFieldList
									.get(i)));
							continue;
						}
					}
				}

				if (isUpdate) {
					task = new Task(Task.UPDATE, Task.PROVIDER);
					result.setUpdateEntries(result.getUpdateEntries() + 1);
				} else {
					task = new Task(Task.CREATE, Task.PROVIDER);
					result.setAddEntries(result.getAddEntries() + 1);
				}
				task.setProvider(provider);
				taskList.add(task);
				csvFieldList = csvReader.read();
			}
		} catch (IOException e) {
			throw new CsvImportException(e.getMessage());
		} catch (ParseException e) {
			throw new CsvImportException(e.getMessage());
		}
		return taskList;
	}

	/**
	 * @see IAdministrator#importOrganisations(PorsCsv, List<String>,
	 *      ImportResult)
	 */
	public List<Task> importOrganisations(PorsCsv csvData,
			List<String> fieldList, ImportResult result)
			throws CsvImportException {

		StringReader stringReader = new StringReader(csvData.getContent());
		CsvListReader csvReader = new CsvListReader(stringReader,
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		ArrayList<Task> taskList = new ArrayList<Task>();

		try {

			csvReader.read();
			List<String> csvFieldList = csvReader.read();
			Organisation organisation;
			String field;
			Task task;
			boolean isUpdate;

			while (null != csvFieldList) {
				isUpdate = false;
				organisation = new Organisation();
				for (int i = 0; i < csvFieldList.size(); i++) {
					if (null != csvFieldList.get(i)
							&& 0 < csvFieldList.get(i).length()) {
						if (i == 0) {
							if (csvFieldList.get(i).equals(DESCRIPTION_HEADER)) {
								return taskList;
							}
						}
						field = fieldList.get(i);
						if (field.equalsIgnoreCase(ORGANISATION_FIELD_ID)) {
							organisation.setId(new Long(csvFieldList.get(i)));
							isUpdate = true;
							continue;
						}
						if (field.equalsIgnoreCase(ORGANISATION_FIELD_OID)) {
							organisation.setOid(csvFieldList.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_ESTABLISHMENTID)) {
							organisation
									.setEstablishmentId(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(ORGANISATION_FIELD_NAME)) {
							organisation.setName(csvFieldList.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_SECONDNAME)) {
							organisation.setSecondName(csvFieldList.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_DESCRIPTION)) {
							organisation.setDescription(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(ORGANISATION_FIELD_EMAIL)) {
							organisation.setEmail(csvFieldList.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_TELEPHONE)) {
							organisation.setTelephone(csvFieldList.get(i));
							continue;
						}
						if (field.equalsIgnoreCase(ORGANISATION_FIELD_FAX)) {
							organisation.setFax(csvFieldList.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_DEACTIVATION_DATE)) {
							organisation.setDeactivationDate(dateFormat
									.parse(csvFieldList.get(i)));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_DEACTIVATION_REASONCODE)) {
							organisation.setDeactivationReasonCode(csvFieldList
									.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_REACTIVATION_DATE)) {
							organisation.setReactivationDate(dateFormat
									.parse(csvFieldList.get(i)));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_REACTIVATION_REASONCODE)) {
							organisation.setReactivationReasonCode(csvFieldList
									.get(i));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_LASTUPDATE_DATE)) {
							organisation.setLastUpdateDate(dateFormat
									.parse(csvFieldList.get(i)));
							continue;
						}
						if (field
								.equalsIgnoreCase(ORGANISATION_FIELD_ADDRESSES)) {
							organisation
									.setAddresses(getAddressesFromString(csvFieldList
											.get(i)));
							continue;
						}
						if (field.equalsIgnoreCase(ORGANISATION_FIELD_LOCALIDS)) {
							organisation
									.setLocalIds(getLocalIdsFromString(csvFieldList
											.get(i)));
							continue;
						}
					}
				}

				if (isUpdate) {
					task = new Task(Task.UPDATE, Task.ORGANISATION);
					result.setUpdateEntries(result.getUpdateEntries() + 1);
				} else {
					task = new Task(Task.CREATE, Task.ORGANISATION);
					result.setAddEntries(result.getAddEntries() + 1);
				}
				task.setOrganisation(organisation);
				taskList.add(task);
				csvFieldList = csvReader.read();
			}
		} catch (IOException e) {
			throw new CsvImportException(e.getMessage());
		} catch (ParseException e) {
			throw new CsvImportException(e.getMessage());
		}
		return taskList;
	}

	/**
	 * @see IAdministrator#exportProviders(List<Provider>, List<String>)
	 */
	public StringWriter exportProviders(List<Provider> providers,
			List<String> fields) throws CsvExportException {

		StringWriter outWriter = new StringWriter();
		String[] header = null;
		if (fields == null || fields.size() == 0) {
			logger.info("no fields found => using all fields for export of providers");
			header = providerAllFields.toArray(new String[providerAllFields
					.size()]);
		} else {
			logger.info("found " + fields.size() + " fields!");
			if (checkProviderFields(fields)) {
				header = fields.toArray(new String[fields.size()]);
			} else {
				throw new CsvExportException(
						"The given fields are not valid for the export!");
			}
		}

		List<CellProcessor> processors = new ArrayList<CellProcessor>();
		for (int i = 0; i < header.length; i++) {
			processors.add(new ConvertNullTo(""));
		}
		CellProcessor[] arrProc = processors
				.toArray(new CellProcessor[header.length]);

		try {
			CsvListWriter writer = new CsvListWriter(outWriter,
					CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			writer.writeHeader(header);
			for (Provider provider : providers) {
				writer.write(getProviderLine(header, provider), arrProc);
			}
			for (int i = 0; i < getCaption().size(); i++) {
				writer.write(getCaption().get(i));
			}
			writer.close();
		} catch (IOException e) {
			throw new CsvExportException(
					"Could not write the provider data to the stream! Message was: "
							+ e.getMessage());
		}

		return outWriter;
	}

	/**
	 * @see IAdministrator#exportOrganisations(List<Organisation>, List<String>)
	 */
	public StringWriter exportOrganisations(List<Organisation> organisations,
			List<String> fields) throws CsvExportException {
		StringWriter outWriter = new StringWriter();

		String[] header = null;
		if (fields == null || fields.size() == 0) {
			header = organisationAllFields
					.toArray(new String[organisationAllFields.size()]);
		} else {
			if (checkOrganisationFields(fields)) {
				header = fields.toArray(new String[fields.size()]);
			} else {
				throw new CsvExportException(
						"The given fields are not valid for the export!");
			}
		}

		List<CellProcessor> processors = new ArrayList<CellProcessor>();
		for (int i = 0; i < header.length; i++) {
			processors.add(new ConvertNullTo(""));
		}
		CellProcessor[] arrProc = processors
				.toArray(new CellProcessor[header.length]);

		try {
			CsvListWriter writer = new CsvListWriter(outWriter,
					CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			writer.writeHeader(header);
			for (Organisation organisation : organisations) {
				writer.write(getOrganisationLine(header, organisation), arrProc);
			}
			for (int i = 0; i < getCaption().size(); i++) {
				writer.write(getCaption().get(i));
			}
			writer.close();
		} catch (IOException e) {
			throw new CsvExportException(
					"Could not write the organisation data to the stream! Message was: "
							+ e.getMessage());
		}

		return outWriter;
	}

	/**
	 * This method returns a caption to insert at the bottom of the entries in
	 * the csv
	 * 
	 * @param --
	 * @return ArrayList<String>, the caption entries
	 **/
	private ArrayList<String> getCaption() {
		ArrayList<String> caption = new ArrayList<String>();
		caption.add(DESCRIPTION_HEADER);
		caption.add("");
		caption.add("ADDRESSES");
		caption.add("Street&Housenumber^Additional^City^State^ZIP^Country");
		caption.add("Multiple address records are separated by ~");
		caption.add("");
		caption.add("LOCALIDS");
		caption.add("LocalId^Facility^Application");
		caption.add("Multiple localid records are separated by ~");
		return caption;
	}

	/**
	 * This method returns a assembled address string (containing street,
	 * housenumber, city, zip, country, state)
	 * 
	 * @param List<Address>, the entities to assemble
	 * @return String, the assembled address attributes
	 **/
	private String getAddressesString(List<Address> addresses) {
		String addressesString = "";
		if (addresses != null) {
			for (int i = 0; i < addresses.size(); i++) {
				Address address = addresses.get(i);
				addressesString += address.getStreet() + "&"
						+ address.getHouseNumber() + "^"
						+ address.getAdditional() + "^" + address.getCity()
						+ "^";
				if (address.getState() != null) {
					addressesString += address.getState();
				}
				addressesString += "^" + address.getZipCode() + "^"
						+ address.getCountry();
				if (i + 1 < addresses.size()) {
					addressesString += "~";
				}
			}
		}
		return addressesString;
	}

	/**
	 * This method reassemble a address string and returns a list of Address
	 * entities
	 * 
	 * @param String
	 *            , the address string with Address entity data (containing
	 *            street, housenumber, city, zip, country, state)
	 * @return List<Address>, several Address entities
	 **/
	private List<Address> getAddressesFromString(String addressString) {
		ArrayList<Address> addresses = new ArrayList<Address>();
		logger.info("AdministratorBean, getAddressesFromString :"
				+ addressString);
		Address address;
		String addressCString = addressString;
		for (int i = 0; i < (countOccuranceOfStringInString("~", addressString) + 1); i++) {

			address = new Address();
			String currentAddressString;
			if (0 < addressCString.indexOf("~")) {
				currentAddressString = addressCString.substring(0,
						addressCString.indexOf("~"));
			} else {
				currentAddressString = addressCString;
			}
			addressCString = addressCString.substring(currentAddressString
					.length());
			addressCString = addressCString.replaceAll("~", "");

			logger.info("AdministratorBean, parsing Address"
					+ currentAddressString);

			String streetHouseNr = currentAddressString.substring(0,
					currentAddressString.indexOf("^"));
			logger.info("AdministratorBean, parsing Address, streetHouseNr : "
					+ streetHouseNr);
			address.setStreet(streetHouseNr.substring(0,
					streetHouseNr.indexOf("&")));
			address.setHouseNumber(streetHouseNr.substring(address.getStreet()
					.length() + 1));
			currentAddressString = currentAddressString.substring(streetHouseNr
					.length() + 1);

			address.setAdditional(currentAddressString.substring(0,
					currentAddressString.indexOf("^")));
			currentAddressString = currentAddressString.substring(address
					.getAdditional().length() + 1);

			address.setCity(currentAddressString.substring(0,
					currentAddressString.indexOf("^")));
			currentAddressString = currentAddressString.substring(address
					.getCity().length() + 1);

			address.setState(currentAddressString.substring(0,
					currentAddressString.indexOf("^")));
			currentAddressString = currentAddressString.substring(address
					.getState().length() + 1);

			address.setZipCode(currentAddressString.substring(0,
					currentAddressString.indexOf("^")));
			currentAddressString = currentAddressString.substring(address
					.getZipCode().length() + 1);

			address.setCountry(currentAddressString);

			addresses.add(address);
		}
		return addresses;
	}

	/**
	 * This method counts the occurance of a distinct substring in the base
	 * string
	 * 
	 * @param String
	 *            , the string to search
	 * @param String
	 *            , the base string to search in
	 * @return int, the count of occurance
	 **/
	private int countOccuranceOfStringInString(String searchFor, String base) {
		int len = searchFor.length();
		int result = 0;
		if (len > 0) {
			int start = base.indexOf(searchFor);
			while (start != -1) {
				result++;
				start = base.indexOf(searchFor, start + len);
			}
		}
		return result;
	}

	/**
	 * This method returns a assembled localid string (containing facility,
	 * application, localid)
	 * 
	 * @param List<LocalId>, the entities to assemble
	 * @return String, the assembled localid attributes
	 **/
	private String getLocalIdString(List<LocalId> localIds) {
		String localidString = "";
		if (localIds != null) {
			for (int i = 0; i < localIds.size(); i++) {
				LocalId localId = localIds.get(i);
				localidString += localId.getLocalId() + "^"
						+ localId.getFacility() + "^"
						+ localId.getApplication();
				if (i + 1 < localIds.size()) {
					localidString += "~";
				}
			}
		}
		return localidString;
	}

	/**
	 * This method reassemble a localid string and returns a list of LocalId
	 * entities
	 * 
	 * @param String
	 *            , the localid string with LocalId entity data (containing
	 *            localid, facility, application)
	 * @return List<LocalId>, several LocalId entities
	 **/
	private List<LocalId> getLocalIdsFromString(String localIdsString) {
		ArrayList<LocalId> localIds = new ArrayList<LocalId>();

		LocalId localId;
		String localIdsCString = localIdsString;
		for (int i = 0; i < countOccuranceOfStringInString("~", localIdsCString) + 1; i++) {
			localId = new LocalId();
			String currentLocalIdString;
			if (0 < localIdsString.indexOf("~")) {
				currentLocalIdString = localIdsString.substring(0,
						localIdsString.indexOf("~"));
			} else {
				currentLocalIdString = localIdsString;
			}
			localIdsString = localIdsString.substring(currentLocalIdString
					.length());
			localIdsString = localIdsString.replaceAll("~", "");

			localId.setLocalId(currentLocalIdString.substring(0,
					currentLocalIdString.indexOf("^")));
			currentLocalIdString = currentLocalIdString.substring(localId
					.getLocalId().length() + 1);

			localId.setFacility(currentLocalIdString.substring(0,
					currentLocalIdString.indexOf("^")));
			currentLocalIdString = currentLocalIdString.substring(localId
					.getFacility().length() + 1);

			localId.setApplication(currentLocalIdString);
			localIds.add(localId);
		}
		return localIds;
	}

	/**
	 * This method returns a list of all exported provider attributes
	 * 
	 * @param String
	 *            , the exported provider fields
	 * @param Provider
	 *            , the Provider entity
	 * @return List<String>, the strings to write in the header of the csv table
	 **/
	private List<String> getProviderLine(String[] fields, Provider provider) {
		List<String> values = new ArrayList<String>();

		for (String field : fields) {
			if (field.equalsIgnoreCase(PROVIDER_FIELD_ID)) {
				values.add(provider.getId().toString());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_LANR)) {
				values.add(provider.getLanr());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_OID)) {
				values.add(provider.getOid());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_FIRSTNAME)) {
				values.add(provider.getFirstName());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_LASTNAME)) {
				values.add(provider.getLastName());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_MIDDLENAME)) {
				values.add(provider.getMiddleName());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_NAMEPREFIX)) {
				values.add(provider.getNamePrefix());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_NAMESUFFIX)) {
				values.add(provider.getNameSuffix());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_GENDERCODE)) {
				values.add(provider.getGenderCode());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_BIRTHDAY)) {
				String birthday = null;
				if (provider.getBirthday() != null) {
					birthday = getDateString(provider.getBirthday());
				}
				values.add(birthday);
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_SPECIALISATION)) {
				values.add(provider.getSpecialisation());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_EMAIL)) {
				values.add(provider.getEmail());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_TELEPHONE)) {
				values.add(provider.getTelephone());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_FAX)) {
				values.add(provider.getFax());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_DEACTIVATION_DATE)) {
				String deactivationDate = null;
				if (provider.getDeactivationDate() != null) {
					deactivationDate = getDateString(provider
							.getDeactivationDate());
				}
				values.add(deactivationDate);
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_DEACTIVATION_REASONCODE)) {
				values.add(provider.getDeactivationReasonCode());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_REACTIVATION_DATE)) {
				String reactivationDate = null;
				if (provider.getReactivationDate() != null) {
					reactivationDate = getDateString(provider
							.getReactivationDate());
				}
				values.add(reactivationDate);
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_REACTIVATION_REASONCODE)) {
				values.add(provider.getReactivationReasonCode());
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_LASTUPDATE_DATE)) {
				String lastUpdateDate = null;
				if (provider.getLastUpdateDate() != null) {
					lastUpdateDate = getDateString(provider.getLastUpdateDate());
				}
				values.add(lastUpdateDate);
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_ADDRESSES)) {
				values.add(getAddressesString(provider.getAddresses()));
				continue;
			}
			if (field.equalsIgnoreCase(PROVIDER_FIELD_LOCALIDS)) {
				values.add(getLocalIdString(provider.getLocalIds()));
				continue;
			}
		}

		return values;
	}

	private boolean checkProviderFields(List<String> fields) {
		for (String fieldName : fields) {
			if (!providerAllFields.contains(fieldName)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method returns a list of all exported organisation attributes
	 * 
	 * @param String
	 *            , the exported organisation fields
	 * @param Organisation
	 *            , the Organisation entity
	 * @return List<String>, the strings to write in the header of the csv table
	 **/
	private List<String> getOrganisationLine(String[] fields,
			Organisation organisation) {
		List<String> values = new ArrayList<String>();

		for (String field : fields) {
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_ID)) {
				values.add(organisation.getId().toString());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_OID)) {
				values.add(organisation.getOid());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_ESTABLISHMENTID)) {
				values.add(organisation.getEstablishmentId());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_NAME)) {
				values.add(organisation.getName());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_SECONDNAME)) {
				values.add(organisation.getSecondName());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_DESCRIPTION)) {
				values.add(organisation.getDescription());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_EMAIL)) {
				values.add(organisation.getEmail());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_TELEPHONE)) {
				values.add(organisation.getTelephone());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_FAX)) {
				values.add(organisation.getFax());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_DEACTIVATION_DATE)) {
				String deactivationDate = null;
				if (organisation.getDeactivationDate() != null) {
					deactivationDate = getDateString(organisation
							.getDeactivationDate());
				}
				values.add(deactivationDate);
				continue;
			}
			if (field
					.equalsIgnoreCase(ORGANISATION_FIELD_DEACTIVATION_REASONCODE)) {
				values.add(organisation.getDeactivationReasonCode());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_REACTIVATION_DATE)) {
				String reactivationDate = null;
				if (organisation.getReactivationDate() != null) {
					reactivationDate = getDateString(organisation
							.getReactivationDate());
				}
				values.add(reactivationDate);
				continue;
			}
			if (field
					.equalsIgnoreCase(ORGANISATION_FIELD_REACTIVATION_REASONCODE)) {
				values.add(organisation.getReactivationReasonCode());
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_LASTUPDATE_DATE)) {
				String lastUpdateDate = null;
				if (organisation.getLastUpdateDate() != null) {
					lastUpdateDate = getDateString(organisation
							.getLastUpdateDate());
				}
				values.add(lastUpdateDate);
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_ADDRESSES)) {
				values.add(getAddressesString(organisation.getAddresses()));
				continue;
			}
			if (field.equalsIgnoreCase(ORGANISATION_FIELD_LOCALIDS)) {
				values.add(getLocalIdString(organisation.getLocalIds()));
				continue;
			}
		}

		return values;
	}

	private boolean checkOrganisationFields(List<String> fields) {
		for (String fieldName : fields) {
			if (!organisationAllFields.contains(fieldName)) {
				return false;
			}
		}
		return true;
	}

	private String getDateString(Date date) {
		return dateFormat.format(date);
	}
}
