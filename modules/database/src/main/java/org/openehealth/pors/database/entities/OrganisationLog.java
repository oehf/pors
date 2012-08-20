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
 * Entity bean mapping entries in database table "OrganisationLog" and thus 
 * representing logging entries for actions done in table "Organisation".
 * </p>
 * <p>
 * The following fields are required for successful persisting:
 * <ul>
 * <li>User</li>
 * <li>Log Time</li>
 * <li>Session ID</li>
 * <li>Trigger Type</li>
 * <li>IP Address</li>
 * <li>Organisation ID</li>
 * </ul>
 * </p>
 * <p>
 * The entity supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all available organisation logs.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@NamedQueries( value = {
		@NamedQuery( name = "getOrganisationLogList", query = "SELECT ol FROM OrganisationLog ol")
		} )
public class OrganisationLog implements Serializable 
{
	private static final long serialVersionUID = -8545956599482402037L;
	
	public static final String QUERY_NAME_ALL = "getOrganisationLogList";
	
	@GeneratedValue
	@Id
	@Column(name="OrganisationLogId")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	private Date logTime;
	
	private String sessionId;
	
	private String IPAddress;
	
	private String triggerType;
	
	private String tableName = "Organisation";
	
	private long regionalOrganisationId;
	
	@Column(nullable=true)
	private int oldPorsUserId;
	
	@Column(name="OldOID", nullable=true)
	private String oldOid;
	
	@Column(nullable=true)
	private String oldEstablishmentId;
	
	@Column(nullable=true)
	private String oldName;
	
	@Column(nullable=true)
	private String oldSecondName;
	
	@Column(nullable=true)
	private String oldDescription;
	
	@Column(nullable=true)
	private String oldEMail;

	@Column(nullable=true)
	private String oldTelephone;
	
	@Column(nullable=true)
	private String oldFax;
	
	@Column(nullable=true)
	private Date oldDeactivationDate;
	
	@Column(nullable=true)
	private String oldDeactivationReasonCode;
	
	@Column(nullable=true)
	private Date oldReactivationDate;
	
	@Column(nullable=true)
	private String oldReactivationReasonCode;
	
	@Column(nullable=true)
	private Date oldLastUpdateDate;

	@Column(nullable=true)
	private int newPorsUserId;
	
	@Column(name="NewOID", nullable=true)
	private String newOid;
	
	@Column(nullable=true)
	private String newEstablishmentId;
	
	@Column(nullable=true)
	private String newName;
	
	@Column(nullable=true)
	private String newSecondName;
	
	@Column(nullable=true)
	private String newDescription;
	
	@Column(nullable=true)
	private String newEMail;

	@Column(nullable=true)
	private String newTelephone;
	
	@Column(nullable=true)
	private String newFax;
	
	@Column(nullable=true)
	private Date newDeactivationDate;
	
	@Column(nullable=true)
	private String newDeactivationReasonCode;
	
	@Column(nullable=true)
	private Date newReactivationDate;
	
	@Column(nullable=true)
	private String newReactivationReasonCode;
	
	@Column(nullable=true)
	private Date newLastUpdateDate;

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

	public long getRegionalOrganisationId() {
		return regionalOrganisationId;
	}

	public void setRegionalOrganisationId(long regionalOrganisationId) {
		this.regionalOrganisationId = regionalOrganisationId;
	}

	public int getOldPorsUserId() {
		return oldPorsUserId;
	}

	public void setOldPorsUserId(int oldPorsUserId) {
		this.oldPorsUserId = oldPorsUserId;
	}

	public String getOldOid() {
		return oldOid;
	}

	public void setOldOid(String oldOid) {
		this.oldOid = oldOid;
	}

	public String getOldEstablishmentId() {
		return oldEstablishmentId;
	}

	public void setOldEstablishmentId(String oldEstablishmentId) {
		this.oldEstablishmentId = oldEstablishmentId;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldSecondName() {
		return oldSecondName;
	}

	public void setOldSecondName(String oldSecondName) {
		this.oldSecondName = oldSecondName;
	}

	public String getOldDescription() {
		return oldDescription;
	}

	public void setOldDescription(String oldDescription) {
		this.oldDescription = oldDescription;
	}

	public String getOldEMail() {
		return oldEMail;
	}

	public void setOldEMail(String oldEMail) {
		this.oldEMail = oldEMail;
	}

	public String getOldTelephone() {
		return oldTelephone;
	}

	public void setOldTelephone(String oldTelephone) {
		this.oldTelephone = oldTelephone;
	}

	public String getOldFax() {
		return oldFax;
	}

	public void setOldFax(String oldFax) {
		this.oldFax = oldFax;
	}

	public Date getOldDeactivationDate() {
		return oldDeactivationDate;
	}

	public void setOldDeactivationDate(Date oldDeactivationDate) {
		this.oldDeactivationDate = oldDeactivationDate;
	}

	public String getOldDeactivationReasonCode() {
		return oldDeactivationReasonCode;
	}

	public void setOldDeactivationReasonCode(String oldDeactivationReasonCode) {
		this.oldDeactivationReasonCode = oldDeactivationReasonCode;
	}

	public Date getOldReactivationDate() {
		return oldReactivationDate;
	}

	public void setOldReactivationDate(Date oldReactivationDate) {
		this.oldReactivationDate = oldReactivationDate;
	}

	public String getOldReactivationReasonCode() {
		return oldReactivationReasonCode;
	}

	public void setOldReactivationReasonCode(String oldReactivationReasonCode) {
		this.oldReactivationReasonCode = oldReactivationReasonCode;
	}

	public Date getOldLastUpdateDate() {
		return oldLastUpdateDate;
	}

	public void setOldLastUpdateDate(Date oldLastUpdateDate) {
		this.oldLastUpdateDate = oldLastUpdateDate;
	}

	public int getNewPorsUserId() {
		return newPorsUserId;
	}

	public void setNewPorsUserId(int newPorsUserId) {
		this.newPorsUserId = newPorsUserId;
	}

	public String getNewOid() {
		return newOid;
	}

	public void setNewOid(String newOid) {
		this.newOid = newOid;
	}

	public String getNewEstablishmentId() {
		return newEstablishmentId;
	}

	public void setNewEstablishmentId(String newEstablishmentId) {
		this.newEstablishmentId = newEstablishmentId;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewSecondName() {
		return newSecondName;
	}

	public void setNewSecondName(String newSecondName) {
		this.newSecondName = newSecondName;
	}

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}

	public String getNewEMail() {
		return newEMail;
	}

	public void setNewEMail(String newEMail) {
		this.newEMail = newEMail;
	}

	public String getNewTelephone() {
		return newTelephone;
	}

	public void setNewTelephone(String newTelephone) {
		this.newTelephone = newTelephone;
	}

	public String getNewFax() {
		return newFax;
	}

	public void setNewFax(String newFax) {
		this.newFax = newFax;
	}

	public Date getNewDeactivationDate() {
		return newDeactivationDate;
	}

	public void setNewDeactivationDate(Date newDeactivationDate) {
		this.newDeactivationDate = newDeactivationDate;
	}

	public String getNewDeactivationReasonCode() {
		return newDeactivationReasonCode;
	}

	public void setNewDeactivationReasonCode(String newDeactivationReasonCode) {
		this.newDeactivationReasonCode = newDeactivationReasonCode;
	}

	public Date getNewReactivationDate() {
		return newReactivationDate;
	}

	public void setNewReactivationDate(Date newReactivationDate) {
		this.newReactivationDate = newReactivationDate;
	}

	public String getNewReactivationReasonCode() {
		return newReactivationReasonCode;
	}

	public void setNewReactivationReasonCode(String newReactivationReasonCode) {
		this.newReactivationReasonCode = newReactivationReasonCode;
	}

	public Date getNewLastUpdateDate() {
		return newLastUpdateDate;
	}

	public void setNewLastUpdateDate(Date newLastUpdateDate) {
		this.newLastUpdateDate = newLastUpdateDate;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}
}
