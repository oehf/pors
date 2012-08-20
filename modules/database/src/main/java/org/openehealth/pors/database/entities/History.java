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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.NamedQuery;

/**
 * <p>
 * Entity bean mapping entries in view "HistoryView" and thus representing an 
 * history entry which can be explicitly defined by its id. This id consists of a 
 * logging domain and the log id uniquely defined within this domain.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects a list of all available history 
 * entries.</li>
 * <li><b>QUERY_NAME_COUNT_ALL</b><br />Counts all history entries.</li>
 * <li><b>QUERY_NAME_COUNT_BY_USER_ID</b><br />Counts all history entries 
 * created by the PORS user having the ID defined with 
 * <code>PARAM_USER_ID</code>. 
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name = "HistoryView")
@NamedQueries(value = {
		@NamedQuery(name = "getHistory", query = "SELECT h FROM History h"),
		@NamedQuery(name = "countAllHistories", query = "SELECT COUNT(h) FROM History h"),
		@NamedQuery(name = "countAllHistoriesOfUser", query = "SELECT COUNT(h) FROM History h WHERE h.userId = :userId")
		})
public class History implements Serializable 
{
	private static final long serialVersionUID = -5506430777227896915L;
	
	public static final String DOMAIN_ADDRESS = "Address";
	public static final String DOMAIN_LOCALID = "LocalID";
	public static final String DOMAIN_ORGANISATION = "Organisation";
	public static final String DOMAIN_ORGANISATION_HAS_ADDRESS = "OrganisationHasAddress";
	public static final String DOMAIN_ORGANISATION_HAS_PROVIDER = "OrganisationHasProvider";
	public static final String DOMAIN_PROVIDER = "Provider";
	public static final String DOMAIN_PROVIDER_HAS_ADDRESS = "ProviderHasAddress";
	
	public static final String PARAM_USER_ID = "userId";
	
	public static final String QUERY_NAME_ALL = "getHistory";
	public static final String QUERY_NAME_COUNT_ALL = "countAllHistories";
	public static final String QUERY_NAME_COUNT_BY_USER_ID = "countAllHistoriesOfUser";

	@Column(name="TriggerType")
	private String action;
	
	@EmbeddedId
	private HistoryId id;
	
	private String IPAddress;
	
	private Date logTime;
	
	private String sessionId;
	
	@Column(name="PorsUserId")
	private Integer userId;
	
	@Column(name="Name")
	private String userName;

	public String getAction() {
		return action;
	}

	/**
	 * <p>
	 * Returns the domain stored in this ID. Or <code>null</code> if the 
	 * ID or the domain was not set.
	 * </p>
	 * 
	 * @return
	 * 		The domain in this ID
	 */
	public String getDomain()
	{
		return (this.id == null) ? null : (this.id.getDomain());
	}

	public HistoryId getId() {
		return id;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	/**
	 * <p>
	 * Returns the log ID stored in this ID. Or <code>null</code> if the 
	 * ID or the log ID was not set.
	 * </p>
	 * 
	 * @return
	 * 		The log ID in this ID
	 */
	public Long getLogId()
	{
		return (this.id == null) ? null : this.id.getLogId();
	}

	public Date getLogTime() {
		return logTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * <p>
	 * Sets the domain in this ID class.
	 * </p>
	 * <p>
	 * Creates a new {@link HistoryId} object as ID if this ID is 
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param domain
	 * 		The domain to set
	 */
	public void setDomain(String domain)
	{
		if (this.id == null)
		{
			this.id = new HistoryId();
		}
		
		this.id.setDomain(domain);
	}

	public void setId(HistoryId id) {
		this.id = id;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	/**
	 * <p>
	 * Sets the log ID in this ID class.
	 * </p>
	 * <p>
	 * Creates a new {@link HistoryId} object as ID if this ID is 
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param logId
	 * 		The log ID to set
	 */
	public void setLogId(Long logId)
	{
		if (this.id == null)
		{
			this.id = new HistoryId();
		}
		
		this.id.setLogId(logId);
	}
	
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
