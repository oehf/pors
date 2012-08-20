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
package org.openehealth.pors.core.dto;

import java.util.Date;

/**
 * The DTO for localIDs
 * 
 * @author tb
 * 
 */
public class LocalIdLogDTO {

	private Long id;
	private Integer porsuserID;
	private Date logTime;
	private String sessionId;
	private String IPAddress;
	private String action;
	private String tableName = "LocalId";
	private Long localIdId;
	private Long oldRegionalOrganisationId;
	private Long oldRegionalProviderId;
	private String oldLocalId;
	private Long newRegionalOrganisationId;
	private Long newRegionalProviderId;
	private String oldFacility;
	private String newFacility;
	private String oldApplication;
	private String newApplication;
	private String newLocalId;

	public Integer getPorsuserID() {
		return porsuserID;
	}

	public void setPorsuserID(Integer porsuserID) {
		this.porsuserID = porsuserID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getLocalIdId() {
		return localIdId;
	}

	public void setLocalIdId(Long localIdId) {
		this.localIdId = localIdId;
	}

	public Long getOldRegionalOrganisationId() {
		return oldRegionalOrganisationId;
	}

	public void setOldRegionalOrganisationId(Long oldRegionalOrganisationId) {
		this.oldRegionalOrganisationId = oldRegionalOrganisationId;
	}

	public Long getOldRegionalProviderId() {
		return oldRegionalProviderId;
	}

	public void setOldRegionalProviderId(Long oldRegionalProviderId) {
		this.oldRegionalProviderId = oldRegionalProviderId;
	}

	public String getOldLocalId() {
		return oldLocalId;
	}

	public void setOldLocalId(String oldLocalId) {
		this.oldLocalId = oldLocalId;
	}

	public Long getNewRegionalOrganisationId() {
		return newRegionalOrganisationId;
	}

	public void setNewRegionalOrganisationId(Long newRegionalOrganisationId) {
		this.newRegionalOrganisationId = newRegionalOrganisationId;
	}

	public Long getNewRegionalProviderId() {
		return newRegionalProviderId;
	}

	public void setNewRegionalProviderId(Long newRegionalProviderId) {
		this.newRegionalProviderId = newRegionalProviderId;
	}

	public String getNewLocalId() {
		return newLocalId;
	}

	public void setNewLocalId(String newLocalId) {
		this.newLocalId = newLocalId;
	}

	public String getOldFacility() {
		return oldFacility;
	}

	public void setOldFacility(String oldFacility) {
		this.oldFacility = oldFacility;
	}

	public String getNewFacility() {
		return newFacility;
	}

	public void setNewFacility(String newFacility) {
		this.newFacility = newFacility;
	}

	public String getOldApplication() {
		return oldApplication;
	}

	public void setOldApplication(String oldApplication) {
		this.oldApplication = oldApplication;
	}

	public String getNewApplication() {
		return newApplication;
	}

	public void setNewApplication(String newApplication) {
		this.newApplication = newApplication;
	}

}
