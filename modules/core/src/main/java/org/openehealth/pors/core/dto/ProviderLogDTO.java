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
 * The DTO for ProviderLog
 * 
 * @author mf
 * 
 */
public class ProviderLogDTO {

	private Long id;
	private Date logTime;
	private String sessionId;
	private String IPAddress;
	private String triggerType;
	private String tableName;

	private Integer porsuserID;
	private Long regionalProviderId;
	private String oldLanr;
	private String oldOid;
	private String oldFirstName;
	private String oldLastName;
	private String oldMiddleName;
	private String oldNamePrefix;
	private String oldNameSuffix;
	private String oldSpecialisation;
	private String newSpecialisation;
	private String oldGenderCode;
	private Date oldBirthday;
	private String oldEmail;
	private String oldTelephone;
	private String oldFax;
	private Date oldDeactivationDate;
	private String oldDeactivationReasonCode;
	private Date oldReactivationDate;
	private String oldReactivationReasonCode;
	private Date oldLastUpdateDate;
	private String newLanr;
	private String newOid;
	private String newFirstName;
	private String newLastName;
	private String newMiddleName;
	private String newNamePrefix;
	private String newNameSuffix;
	private String newGenderCode;
	private Date newBirthday;
	private String newEmail;
	private String newTelephone;
	private String newFax;
	private Date newDeactivationDate;
	private String newDeactivationReasonCode;
	private Date newReactivationDate;
	private String newReactivationReasonCode;
	private Date newLastUpdateDate;

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

	public Long getRegionalProviderId() {
		return regionalProviderId;
	}

	public void setRegionalProviderId(Long regionalProviderId) {
		this.regionalProviderId = regionalProviderId;
	}

	public String getOldLanr() {
		return oldLanr;
	}

	public void setOldLanr(String oldLanr) {
		this.oldLanr = oldLanr;
	}

	public String getOldOid() {
		return oldOid;
	}

	public void setOldOid(String oldOid) {
		this.oldOid = oldOid;
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

	public String getNewOid() {
		return newOid;
	}

	public void setNewOid(String newOid) {
		this.newOid = newOid;
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

	public void setOldReactivationReasonCode(String oldReactivationReasonCode) {
		this.oldReactivationReasonCode = oldReactivationReasonCode;
	}

	public String getOldReactivationReasonCode() {
		return oldReactivationReasonCode;
	}

	public void setNewReactivationReasonCode(String newReactivationReasonCode) {
		this.newReactivationReasonCode = newReactivationReasonCode;
	}

	public String getNewReactivationReasonCode() {
		return newReactivationReasonCode;
	}

	public void setPorsuserID(Integer porsuserID) {
		this.porsuserID = porsuserID;
	}

	public Integer getPorsuserID() {
		return porsuserID;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setOldSpecialisation(String oldSpecialisation) {
		this.oldSpecialisation = oldSpecialisation;
	}

	public String getOldSpecialisation() {
		return oldSpecialisation;
	}

	public void setNewSpecialisation(String newSpecialisation) {
		this.newSpecialisation = newSpecialisation;
	}

	public String getNewSpecialisation() {
		return newSpecialisation;
	}

}
