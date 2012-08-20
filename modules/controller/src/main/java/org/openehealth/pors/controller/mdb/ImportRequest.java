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
package org.openehealth.pors.controller.mdb;

import java.io.Serializable;
import java.util.List;

import org.openehealth.pors.core.dto.PorsCsv;
import org.openehealth.pors.database.entities.ImportResult;
import org.openehealth.pors.database.entities.PorsUser;

public class ImportRequest  implements Serializable {
	
	private static final long serialVersionUID = -1930170912611530436L;
	
	private String domain;
	private PorsCsv csvData;
	private List<String> fieldList;
	private PorsUser user;
	private String ip;
	private String sessionId;
	private ImportResult result;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public PorsCsv getCsvData() {
		return csvData;
	}
	public void setCsvData(PorsCsv csvData) {
		this.csvData = csvData;
	}
	public List<String> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}
	public PorsUser getUser() {
		return user;
	}
	public void setUser(PorsUser user) {
		this.user = user;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public ImportResult getResult() {
		return result;
	}
	public void setResult(ImportResult result) {
		this.result = result;
	}
}
