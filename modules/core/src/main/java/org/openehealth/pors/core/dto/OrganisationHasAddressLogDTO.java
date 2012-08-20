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
 * The DTO for logging details of organisationHassAddress
 * 
 * @author tb
 * 
 */
public class OrganisationHasAddressLogDTO {

	private Long id;
	private Integer porsUSerId;
	private Date logTime;
	private String sessionId;
	private String IPAddress;
	private String triggerType;
	private String tableName = "OrganisationHasAddress";
	private Long oldRegionalOrganisationId;
	private Long oldAddressId;
	private Long newRegionalOrganisationId;
	private Long newAddressId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPorsUSerId() {
		return porsUSerId;
	}

	public void setPorsUSerId(Integer porsUSerId) {
		this.porsUSerId = porsUSerId;
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

	public Long getOldAddressId() {
		return oldAddressId;
	}

	public void setOldAddressId(Long oldAddressId) {
		this.oldAddressId = oldAddressId;
	}

	public Long getNewRegionalOrganisationId() {
		return newRegionalOrganisationId;
	}

	public void setNewRegionalOrganisationId(Long newRegionalOrganisationId) {
		this.newRegionalOrganisationId = newRegionalOrganisationId;
	}

	public Long getNewAddressId() {
		return newAddressId;
	}

	public void setNewAddressId(Long newAddressId) {
		this.newAddressId = newAddressId;
	}
}
