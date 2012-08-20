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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * <p>
 * Entity bean mapping entries in database table "LocalIdLog" and thus 
 * representing logging entries for actions done in table "LocalId".
 * </p>
 * <p>
 * The following fields are required for successful persisting:
 * <ul>
 * <li>User</li>
 * <li>Log Time</li>
 * <li>Session ID</li>
 * <li>Trigger Type</li>
 * <li>IP Address</li>
 * <li>Local ID ID</li>
 * </ul>
 * </p>
 * <p>
 * The entity supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all available local ID logs.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@NamedQueries( value = { 
		@NamedQuery( name = "getLocalIdLogList", query = "SELECT lidl FROM LocalIdLog lidl" )
})
public class LocalIdLog implements Serializable 
{
	private static final long serialVersionUID = -6331537092787478553L;
	
	public static final String QUERY_NAME_ALL = "getLocalIdLogList";

	@Id
	@GeneratedValue
	@Column(name = "LocalIdLogId")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	private Date logTime;
	
	private String sessionId;
	
	private String IPAddress;
	
	@Column(name="TriggerType")
	private String action;
	
	private String tableName = "LocalId";
	
	private Long localIdId;
	
	@Column(nullable = true)
	private Long oldRegionalOrganisationId;
	
	@Column(nullable = true)
	private Long oldRegionalProviderId;
	
	@Column(nullable = true)
	private String oldLocalId;
	
	@Column(nullable = true)
	private String oldFacility;
	
	@Column(nullable = true)
	private String oldApplication;
	
	@Column(nullable = true)
	private Long newRegionalOrganisationId;
	
	@Column(nullable = true)
	private Long newRegionalProviderId;
	
	@Column(nullable = true)
	private String newLocalId;
	
	@Column(nullable = true)
	private String newFacility;
	
	@Column(nullable = true)
	private String newApplication;

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

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public String getOldFacility() {
		return oldFacility;
	}

	public void setOldFacility(String oldFacility) {
		this.oldFacility = oldFacility;
	}

	public String getOldApplication() {
		return oldApplication;
	}

	public void setOldApplication(String oldApplication) {
		this.oldApplication = oldApplication;
	}

	public String getNewFacility() {
		return newFacility;
	}

	public void setNewFacility(String newFacility) {
		this.newFacility = newFacility;
	}

	public String getNewApplication() {
		return newApplication;
	}

	public void setNewApplication(String newApplication) {
		this.newApplication = newApplication;
	}
}
