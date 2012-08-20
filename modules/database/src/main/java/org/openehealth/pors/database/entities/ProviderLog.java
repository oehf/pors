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
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p>
 * Entity bean mapping entries in database table "ProviderLog" and thus 
 * representing logging entries for actions done in table "Provider".
 * </p>
 * <p>
 * The following fields are required for successful persisting:
 * <ul>
 * <li>User</li>
 * <li>Log Time</li>
 * <li>Session ID</li>
 * <li>Trigger Type</li>
 * <li>IP Address</li>
 * <li>Provider ID</li>
 * </ul>
 * </p>
 * <p>
 * The entity supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all available provider logs.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name="ProviderLog")
@NamedQueries( value = { 
		@NamedQuery( name = "getProviderLogList", query = "SELECT pl FROM ProviderLog pl") 
		} )
public class ProviderLog implements Serializable 
{
	private static final long serialVersionUID = -1868963544461405400L;
	
	public static final String QUERY_NAME_ALL = "getProviderLogList";

	@Id
	@GeneratedValue
	@Column(name="ProviderLogId")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	private Date logTime;
	
	private String sessionId;
	
	private String IPAddress;
	
	@Transient
	private String triggerName = "";
	
	private String triggerType;
	
	private String tableName = "Provider";
	
	private Long regionalProviderId;
	
	@Column(name="OldLANR", nullable=true)
	private String oldLanr;
	
	@Column(name="OldOID", nullable=true)
	private String oldOid;
	
	private String oldFirstName;
	
	private String oldLastName;
	
	@Column(nullable=true)
	private String oldMiddleName;
	
	@Column(nullable=true)
	private String oldNamePrefix;
	
	@Column(nullable=true)
	private String oldNameSuffix;
	
	private String oldGenderCode;
	
	private Date oldBirthday;
	
	@Column(nullable = true)
	private String oldSpecialisation;
	
	@Column(nullable=true)
	private String oldEmail;
	
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
	
	private Date oldLastUpdateDate;
	
	@Column(name="NewLANR", nullable=true)
	private String newLanr;
	
	@Column(name="NewOID", nullable=true)
	private String newOid;
	
	private String newFirstName;
	
	private String newLastName;
	
	@Column(nullable=true)
	private String newMiddleName;
	
	@Column(nullable=true)
	private String newNamePrefix;
	
	@Column(nullable=true)
	private String newNameSuffix;
	
	private String newGenderCode;
	
	@Column(nullable=true)
	private Date newBirthday;
	
	@Column(nullable = true)
	private String newSpecialisation;
	
	@Column(nullable=true)
	private String newEmail;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id)
	{
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

	@Deprecated
	public String getTriggerName() {
		return triggerName;
	}

	@Deprecated
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
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

	public String getOldLanr() {
		return oldLanr;
	}

	public void setOldLanr(String oldLanr) {
		this.oldLanr = oldLanr;
	}

	public String getOldFirstName() {
		return oldFirstName;
	}

	public void setOldFirstName(String oldFirstName) {
		this.oldFirstName = oldFirstName;
	}

	public String getOldLastName() {
		return oldLastName;
	}

	public void setOldLastName(String oldLastName) {
		this.oldLastName = oldLastName;
	}

	public String getOldMiddleName() {
		return oldMiddleName;
	}

	public void setOldMiddleName(String oldMiddleName) {
		this.oldMiddleName = oldMiddleName;
	}

	public String getOldNamePrefix() {
		return oldNamePrefix;
	}

	public void setOldNamePrefix(String oldNamePrefix) {
		this.oldNamePrefix = oldNamePrefix;
	}

	public String getOldNameSuffix() {
		return oldNameSuffix;
	}

	public void setOldNameSuffix(String oldNameSuffix) {
		this.oldNameSuffix = oldNameSuffix;
	}

	public String getOldGenderCode() {
		return oldGenderCode;
	}

	public void setOldGenderCode(String oldGenderCode) {
		this.oldGenderCode = oldGenderCode;
	}

	public Date getOldBirthday() {
		return oldBirthday;
	}

	public void setOldBirthday(Date oldBirthday) {
		this.oldBirthday = oldBirthday;
	}

	public String getOldEmail() {
		return oldEmail;
	}

	public void setOldEmail(String oldEmail) {
		this.oldEmail = oldEmail;
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

	public Date getOldLastUpdateDate() {
		return oldLastUpdateDate;
	}

	public void setOldLastUpdateDate(Date oldLastUpdateDate) {
		this.oldLastUpdateDate = oldLastUpdateDate;
	}

	public String getNewLanr() {
		return newLanr;
	}

	public void setNewLanr(String newLanr) {
		this.newLanr = newLanr;
	}

	public String getNewFirstName() {
		return newFirstName;
	}

	public void setNewFirstName(String newFirstName) {
		this.newFirstName = newFirstName;
	}

	public String getNewLastName() {
		return newLastName;
	}

	public void setNewLastName(String newLastName) {
		this.newLastName = newLastName;
	}

	public String getNewMiddleName() {
		return newMiddleName;
	}

	public void setNewMiddleName(String newMiddleName) {
		this.newMiddleName = newMiddleName;
	}

	public String getNewNamePrefix() {
		return newNamePrefix;
	}

	public void setNewNamePrefix(String newNamePrefix) {
		this.newNamePrefix = newNamePrefix;
	}

	public String getNewNameSuffix() {
		return newNameSuffix;
	}

	public void setNewNameSuffix(String newNameSuffix) {
		this.newNameSuffix = newNameSuffix;
	}

	public String getNewGenderCode() {
		return newGenderCode;
	}

	public void setNewGenderCode(String newGenderCode) {
		this.newGenderCode = newGenderCode;
	}

	public Date getNewBirthday() {
		return newBirthday;
	}

	public void setNewBirthday(Date newBirthday) {
		this.newBirthday = newBirthday;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
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

	public Date getNewLastUpdateDate() {
		return newLastUpdateDate;
	}

	public void setNewLastUpdateDate(Date newLastUpdateDate) {
		this.newLastUpdateDate = newLastUpdateDate;
	}

	public Long getRegionalProviderId() {
		return regionalProviderId;
	}

	public void setRegionalProviderId(Long regionalProviderId) {
		this.regionalProviderId = regionalProviderId;
	}

	public String getOldOid() {
		return oldOid;
	}

	public void setOldOid(String oldOid) {
		this.oldOid = oldOid;
	}

	public String getNewOid() {
		return newOid;
	}

	public void setNewOid(String newOid) {
		this.newOid = newOid;
	}

	public void setUser(PorsUser user) {
		this.user = user;
	}

	public PorsUser getUser() {
		return user;
	}

	public void setNewReactivationReasonCode(String newReactivationReasonCode) {
		this.newReactivationReasonCode = newReactivationReasonCode;
	}

	public String getNewReactivationReasonCode() {
		return newReactivationReasonCode;
	}

	public void setOldReactivationReasonCode(String oldReactivationReasonCode) {
		this.oldReactivationReasonCode = oldReactivationReasonCode;
	}

	public String getOldReactivationReasonCode() {
		return oldReactivationReasonCode;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public String getOldSpecialisation() {
		return oldSpecialisation;
	}

	public void setOldSpecialisation(String oldSpecialisation) {
		this.oldSpecialisation = oldSpecialisation;
	}

	public String getNewSpecialisation() {
		return newSpecialisation;
	}

	public void setNewSpecialisation(String newSpecialisation) {
		this.newSpecialisation = newSpecialisation;
	}
}
