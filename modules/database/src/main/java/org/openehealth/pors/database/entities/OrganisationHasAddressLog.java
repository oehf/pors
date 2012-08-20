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
package org.openehealth.pors.database.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * <p>
 * Entity bean mapping entries in table "OrganisationHasAddressLog" and thus 
 * representing logging entries of actions done in table 
 * "OrganisationHasAddress".
 * </p>
 * <p>
 * The following fields are required for successful persisting:
 * <ul>
 * <li>User</li>
 * <li>Log Time</li>
 * <li>IP Address</li>
 * <li>Session ID</li>
 * <li>Trigger Type</li>
 * </ul>
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all organisation has address logs.
 * </li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@NamedQueries( value = {
		@NamedQuery( name = "getOrganisationHasAddressLogList", query = "SELECT ohal FROM OrganisationHasAddressLog ohal")
	})
public class OrganisationHasAddressLog implements Serializable 
{
	private static final long serialVersionUID = 3971437628479529043L;

	public static final String QUERY_NAME_ALL = "getOrganisationHasAddressLogList";
	
	@Id
	@GeneratedValue
	@Column( name = "OrganisationHasAddressLogId" )
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	private Date logTime;
	
	private String sessionId;
	
	private String ipAddress;
	
	private String triggerType;
	
	private String tableName = "OrganisationHasAddress";
	
	@Column(nullable=true)
	private Long oldRegionalOrganisationId;
	
	@Column(nullable=true)
	private Long oldAddressId;
	
	@Column(nullable=true)
	private Long newRegionalOrganisationId;
	
	@Column(nullable=true)
	private Long newAddressId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PorsUser getUser() {
		return user;
	}

	public void setUser(PorsUser user) {
		this.user = user;
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

	public void setIPAddress(String iPAddress) {
		ipAddress = iPAddress;
	}

	public String getIPAddress() {
		return ipAddress;
	}
}
