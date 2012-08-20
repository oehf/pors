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

import java.util.List;

import javax.faces.model.SelectItem;

public class ImportWrapper {
	
	private SelectItem userField;
	private SelectItem selectedField;
	
	private List<SelectItem> available;
	
	public SelectItem getUserField() {
		return userField;
	}
	public void setUserField(SelectItem userField) {
		this.userField = userField;
	}
	public SelectItem getSelectedField() {
		return selectedField;
	}
	public void setSelectedField(SelectItem selectedField) {
		this.selectedField = selectedField;
	}
	public List<SelectItem> getAvailable() {
		return available;
	}
	public void setAvailable(List<SelectItem> available) {
		this.available = available;
	}

}
