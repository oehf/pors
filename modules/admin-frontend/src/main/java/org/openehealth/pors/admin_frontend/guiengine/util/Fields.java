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
package org.openehealth.pors.admin_frontend.guiengine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

public class Fields {
	
	public static final String NO_SELECTION = "- None -";
	
	private static final List<String> FIELDS_PROVIDER = new ArrayList<String>(
			Arrays.asList(new String[] { "id", "lanr",
					"oid", "firstname", "lastname", "middleName", "namePrefix", "nameSuffix", 
					"genderCode", "birthday", "specialisation", "email", "telephone", "fax", 
					"deactivationDate", "deactivationReasonCode",
					"reactivationDate", "reactivationReasonCode",
					"lastUpdateDate", "addresses", "localids" }));
	
	private static final List<String> FIELDS_PROVIDER_REQUIRED = new ArrayList<String>(
			Arrays.asList(new String[] { "firstname", "lastname" } ));
	
	private static final List<String> FIELDS_ORGANISATION = new ArrayList<String>(
			Arrays.asList(new String[] { "id", "oid", "establishmentid", "name",
					"secondname", "description", "email", "telephone", "fax",
					"deactivationDate", "deactivationReasonCode",
					"reactivationDate", "reactivationReasonCode",
					"lastUpdateDate", "addresses", "localids" }));
	
	private static final List<String> FIELDS_ORGANISATION_REQUIRED = new ArrayList<String>(
			Arrays.asList(new String[] { "name" } ));
	
	private static final List<String> FIELDS_PROVIDER_SEARCH = new ArrayList<String>(
			Arrays.asList(new String[] { "lanr", "oid", "firstName",
					"lastName", "middleName", "namePrefix", "nameSuffix",
					"genderCode", "birthday", "specialisation", "email", "telephone", "fax",
					"addresses.street", "addresses.houseNumber",
					"addresses.zipCode", "addresses.city", "addresses.state",
					"addresses.country", "localIds.localId", "localIds.facility",
					"localIds.application" }));
	
	private static final List<String> FIELDS_ORGANISATION_SEARCH = new ArrayList<String>(
			Arrays.asList(new String[] { "oid", "establishmentId", "name",
					"secondName", "description", "email", "telephone", "fax",
					"addresses.street", "addresses.houseNumber",
					"addresses.zipCode", "addresses.city", "addresses.state",
					"addresses.country", "localIds.localId", "localIds.facility",
					"localIds.application" }));
	
	private static final List<String> FIELDS_HISTORY_SEARCH = new ArrayList<String>(
			Arrays.asList(new String[] { "domain", "userName", "action", 
					"sessionId", "ipAddress" }));
	
	private static Fields instance;
	
	private Fields() {
	}
	
	public static Fields getInstance() {
		if (instance == null) {
			instance = new Fields();
		}
		return instance;
	}
	
	public List<String> getProviderFields(boolean includeNoneEntry) {
		List<String> fields = new ArrayList<String>();
		handleNoneEntryString(fields, includeNoneEntry);
		initializeStringList(FIELDS_PROVIDER, fields);
		return fields;
	}
	
	public List<String> getOrganisationFields(boolean includeNoneEntry) {
		List<String> fields = new ArrayList<String>();
		handleNoneEntryString(fields, includeNoneEntry);
		initializeStringList(FIELDS_ORGANISATION, fields);
		return fields;
	}
	
	public List<SelectItem> getProviderFieldsSelectItems(boolean includeNoneEntry) {
		List<SelectItem> fieldsSelectItem = new ArrayList<SelectItem>();
		handleNoneEntrySelectItem(fieldsSelectItem, includeNoneEntry);
		initializeSelectItemList(FIELDS_PROVIDER, fieldsSelectItem);
		return fieldsSelectItem;
	}
	
	public List<SelectItem> getOrganisationFieldsSelectItems(boolean includeNoneEntry) {
		List<SelectItem> fieldsSelectItem = new ArrayList<SelectItem>();
		handleNoneEntrySelectItem(fieldsSelectItem, includeNoneEntry);
		initializeSelectItemList(FIELDS_ORGANISATION, fieldsSelectItem);
		return fieldsSelectItem;
	}
	
	public List<String> getProviderSearchFields(boolean includeNoneEntry) {
		List<String> fields = new ArrayList<String>();
		handleNoneEntryString(fields, includeNoneEntry);
		initializeStringList(FIELDS_PROVIDER_SEARCH, fields);
		return fields;
	}
	
	public List<SelectItem> getProviderSearchFieldsSelectItem(boolean includeNoneEntry) {
		List<SelectItem> fields = new ArrayList<SelectItem>();
		handleNoneEntrySelectItem(fields, includeNoneEntry);
		initializeSelectItemList(FIELDS_PROVIDER_SEARCH, fields);
		return fields;
	}
	
	public List<String> getOrganisationSearchFields(boolean includeNoneEntry) {
		List<String> fields = new ArrayList<String>();
		handleNoneEntryString(fields, includeNoneEntry);
		initializeStringList(FIELDS_ORGANISATION_SEARCH, fields);
		return fields;
	}
	
	public List<SelectItem> getOrganisationSearchFieldsSelectItem(boolean includeNoneEntry) {
		List<SelectItem> fields = new ArrayList<SelectItem>();
		handleNoneEntrySelectItem(fields, includeNoneEntry);
		initializeSelectItemList(FIELDS_ORGANISATION_SEARCH, fields);
		return fields;
	}
	
	public List<String> getHistorySearchFields(boolean includeNoneEntry) {
		List<String> fields = new ArrayList<String>();
		handleNoneEntryString(fields, includeNoneEntry);
		initializeStringList(FIELDS_HISTORY_SEARCH, fields);
		return fields;
	}
	
	public List<SelectItem> getHistorySearchFieldsSelectItem(boolean includeNoneEntry) {
		List<SelectItem> fields = new ArrayList<SelectItem>();
		handleNoneEntrySelectItem(fields, includeNoneEntry);
		initializeSelectItemList(FIELDS_HISTORY_SEARCH, fields);
		return fields;
	}
	
	public boolean requiredFieldsMappedProvider(List<String> selectedFields) {
		boolean found = false;
		for (String requiredField : FIELDS_PROVIDER_REQUIRED) {
			found = false;
			for (String selectedField : selectedFields) {
				if (requiredField.equals(selectedField)) {
					found = true;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	public boolean requiredFieldsMappedOrganisation(List<String> selectedFields) {
		boolean found = false;
		for (String requiredField : FIELDS_ORGANISATION_REQUIRED) {
			found = false;
			for (String selectedField : selectedFields) {
				if (requiredField.equals(selectedField)) {
					found = true;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	private void initializeStringList(List<String> inputList, List<String> targetList) {
		for (String string : inputList) {
			targetList.add(string);
		}
	}
	
	private void initializeSelectItemList(List<String> inputList, List<SelectItem> targetList) {
		for (String string : inputList) {
			targetList.add(new SelectItem(string));
		}
	}
	
	private void handleNoneEntryString(List<String> inputList, boolean addEntry) {
		if (addEntry) {
			inputList.add(NO_SELECTION);
		}
	}
	
	private void handleNoneEntrySelectItem(List<SelectItem> inputList, boolean addEntry) {
		if (addEntry) {
			inputList.add(new SelectItem(NO_SELECTION));
		}
	}

}
