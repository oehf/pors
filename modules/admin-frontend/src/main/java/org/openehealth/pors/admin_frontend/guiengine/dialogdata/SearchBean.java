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
package org.openehealth.pors.admin_frontend.guiengine.dialogdata;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.openehealth.pors.admin_frontend.guiengine.util.Fields;

public class SearchBean {
	
	private List<SelectItem> operators;
	
	private String field1;
	private String field2;
	private String field3;
	
	private String value1;
	private String value2;
	private String value3;
	
	private String selectedOperator;
	
	private List<SelectItem> availableFieldsProvider;
	private List<SelectItem> availableFieldsOrganisation;
	private List<SelectItem> availableFieldsHistory;
	
	
	public SearchBean() {
		availableFieldsProvider = Fields.getInstance().getProviderSearchFieldsSelectItem(true);
		availableFieldsOrganisation = Fields.getInstance().getOrganisationSearchFieldsSelectItem(true);
		availableFieldsHistory = Fields.getInstance().getHistorySearchFieldsSelectItem(true);
		operators = new ArrayList<SelectItem>();
		operators.add(new SelectItem("AND"));
		operators.add(new SelectItem("OR"));
	}


	
	public List<SelectItem> getOperators() {
		return operators;
	}



	public void setOperators(List<SelectItem> operators) {
		this.operators = operators;
	}



	public String getSelectedOperator() {
		return selectedOperator;
	}



	public void setSelectedOperator(String selectedOperator) {
		this.selectedOperator = selectedOperator;
	}



	public String getField1() {
		return field1;
	}


	public void setField1(String field1) {
		this.field1 = field1;
	}


	public String getField2() {
		return field2;
	}


	public void setField2(String field2) {
		this.field2 = field2;
	}


	public String getField3() {
		return field3;
	}


	public void setField3(String field3) {
		this.field3 = field3;
	}


	public List<SelectItem> getAvailableFieldsProvider() {
		return availableFieldsProvider;
	}


	public void setAvailableFieldsProvider(List<SelectItem> availableFieldsProvider) {
		this.availableFieldsProvider = availableFieldsProvider;
	}


	public List<SelectItem> getAvailableFieldsOrganisation() {
		return availableFieldsOrganisation;
	}


	public void setAvailableFieldsOrganisation(
			List<SelectItem> availableFieldsOrganisation) {
		this.availableFieldsOrganisation = availableFieldsOrganisation;
	}


	public String getValue1() {
		return value1;
	}


	public void setValue1(String value1) {
		this.value1 = value1;
	}


	public String getValue2() {
		return value2;
	}


	public void setValue2(String value2) {
		this.value2 = value2;
	}


	public String getValue3() {
		return value3;
	}


	public void setValue3(String value3) {
		this.value3 = value3;
	}



	public List<SelectItem> getAvailableFieldsHistory() {
		return availableFieldsHistory;
	}



	public void setAvailableFieldsHistory(List<SelectItem> availableFieldsHistory) {
		this.availableFieldsHistory = availableFieldsHistory;
	}
	
	public void reset() {
		field1 = null;
		field2 = null;;
		field3 = null;;
		
		value1 = null;
		value2 = null;
		value3 = null;
		
		selectedOperator = null;
	}
	
}
