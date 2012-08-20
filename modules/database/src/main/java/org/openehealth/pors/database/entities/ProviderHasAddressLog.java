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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * <p>
 * Entity bean mapping entries in table "ProviderHasAddressLog" and thus 
 * representing logging entries of actions done in table 
 * "ProviderHasAddress".
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
 * <li><b>QUERY_NAME_ALL</b><br />Selects all provider has address logs.
 * </li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@NamedQueries( value = {
		@NamedQuery( name = "getProviderHasAddressLogList", query = "SELECT phal FROM ProviderHasAddressLog phal")
	})
public class ProviderHasAddressLog implements Serializable 
{
	private static final long serialVersionUID = -3686219401444542252L;
	
	public static final String QUERY_NAME_ALL = "getProviderHasAddressLogList";
	
	@Id
	@GeneratedValue
	@Column(name="ProviderHasAddressLogId")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	private Date logTime;
	
	private String sessionId;
	
	private String IPAddress;
	
	private String triggerType;
	
	private String tableName = "ProviderHasAddress";
	
	@Column(nullable=true)
	private Long oldRegionalProviderId;
	
	@Column(nullable=true)
	private Long oldAddressId;
	
	@Column(nullable=true)
	private Long newRegionalProviderId;
	
	@Column(nullable=true)
	private Long newAddressId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public Long getOldRegionalProviderId() {
		return oldRegionalProviderId;
	}

	public void setOldRegionalProviderId(Long oldRegionalProviderId) {
		this.oldRegionalProviderId = oldRegionalProviderId;
	}

	public Long getOldAddressId() {
		return oldAddressId;
	}

	public void setOldAddressId(Long oldAddressId) {
		this.oldAddressId = oldAddressId;
	}

	public Long getNewRegionalProviderId() {
		return newRegionalProviderId;
	}

	public void setNewRegionalProviderId(Long newRegionalProviderId) {
		this.newRegionalProviderId = newRegionalProviderId;
	}

	public Long getNewAddressId() {
		return newAddressId;
	}

	public void setNewAddressId(Long newAddressId) {
		this.newAddressId = newAddressId;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}
}
