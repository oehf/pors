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
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * <p>
 * Entity bean mapping entries in view "UserHistoryView" and thus representing an 
 * history entry which can be explicitly defined by its id. This id consists of a 
 * logging domain and the domain id uniquely defined within this domain.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>getUserHistoryByUserId</b><br />Selects a list of all available history 
 * entries for the user having the id defined in parameter 1</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name="UserHistoryView")
@NamedQueries(value = {
		@NamedQuery(name = "getUserHistoryByUserId", query = "SELECT uh FROM UserHistory uh WHERE uh.owningUserId = ?1"),
		@NamedQuery(name = "getUserHistoryList", query = "SELECT uh FROM UserHistory uh")})
public class UserHistory implements Serializable 
{
	private static final long serialVersionUID = 2071220320139622220L;

	@EmbeddedId
	private UserHistoryId id;
	
	@Column(name="EditingPorsUserId")
	private Integer editingUserId;
	
	@Column(name="EditingPorsUserName")
	private String editingUserName;
	
	@Column(name="OwningPorsUserId")
	private Integer owningUserId;
	
	@Column(name="OwningPorsUserName")
	private String owningUserName;
	
//	@Lob
	private String action;
	
	private Date logTime;

	private String sessionId;
	
	private String IPAddress;

	public UserHistoryId getId() {
		return id;
	}

	public void setId(UserHistoryId id) {
		this.id = id;
	}

	public Integer getEditingUserId() {
		return editingUserId;
	}

	public void setEditingUserId(Integer editingUserId) {
		this.editingUserId = editingUserId;
	}

	public String getEditingUserName() {
		return editingUserName;
	}

	public void setEditingUserName(String editingUserName) {
		this.editingUserName = editingUserName;
	}

	public Integer getOwningUserId() {
		return owningUserId;
	}

	public void setOwningUserId(Integer owningUserId) {
		this.owningUserId = owningUserId;
	}

	public String getOwningUserName() {
		return owningUserName;
	}

	public void setOwningUserName(String owningUserName) {
		this.owningUserName = owningUserName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
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
			this.id = new UserHistoryId();
		}
		
		this.id.setDomain(domain);
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
		return (this.id == null) ? null : this.id.getDomain();
	}
	
	/**
	 * <p>
	 * Sets the domain ID in this ID class.
	 * </p>
	 * <p>
	 * Creates a new {@link HistoryId} object as ID if this ID is 
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param logId
	 * 		The log ID to set
	 */
	public void setDomainId(Long domainId)
	{
		if (this.id == null)
		{
			this.id = new UserHistoryId();
		}
		
		this.id.setDomainId(domainId);
	}
	
	/**
	 * <p>
	 * Returns the domain ID stored in this ID. Or <code>null</code> if the 
	 * ID or the domain ID was not set.
	 * </p>
	 * 
	 * @return
	 * 		The log ID in this ID
	 */
	public Long getDomainId()
	{
		return (this.id == null) ? null : this.id.getDomainId();
	}
}
