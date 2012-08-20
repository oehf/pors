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
 * The DTO for OrganisationHasProviderLog
 * 
 * @author tb
 * 
 */
public class OrganisationHasProviderLogDTO {

	private Long id;
	private Integer porsUserId;
	private Date logTime;
	private String sessionId;
	private String IPAddress;
	private String triggerType;
	private String tableName = "OrganisationHasProvider";
	private Long oldRegionalOrganisationId;
	private Long oldRegionalProviderId;
	private Long newRegionalOrganisationId;
	private Long newRegionalProviderId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPorsUserId() {
		return porsUserId;
	}

	public void setPorsUserId(Integer porsUserId) {
		this.porsUserId = porsUserId;
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

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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
}
