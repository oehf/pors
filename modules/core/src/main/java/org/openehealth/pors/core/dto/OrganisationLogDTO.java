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
 * The DTO for OrganisationLog
 * 
 * @author mf
 * 
 */
public class OrganisationLogDTO {

	private Long id;
	private Date logTime;
	private String sessionId;
	private String IPAddress;
	private String triggerName;
	private String triggerType;
	private String tableName;

	private Integer porsuserID;

	private String oldOID;
	private String oldEstablishmentID;
	private String oldName;
	private String oldSecondname;
	private String oldDescription;
	private String oldEmail;
	private String oldFax;
	private String oldTelephone;
	private Date oldDeactivationDate;
	private String oldDeactivationReasonCode;
	private Date oldReactivationDate;
	private String oldReactivationReasonCode;
	private Date oldLastUpdateDate;
	private Integer oldPorsuserID;
	private String newOID;
	private String newEstablishmentID;
	private String newName;
	private String newSecondname;
	private String newDescription;
	private String newEmail;
	private String newFax;
	private String newTelephone;
	private Date newDeactivationDate;
	private String newDeactivationReasonCode;
	private Date newReactivationDate;
	private String newReactivationReasonCode;
	private Date newLastUpdateDate;
	private Integer newPorsuserID;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setPorsuserID(Integer porsuserID) {
		this.porsuserID = porsuserID;
	}

	public Integer getPorsuserID() {
		return porsuserID;
	}

	public void setOldOID(String oldOID) {
		this.oldOID = oldOID;
	}

	public String getOldOID() {
		return oldOID;
	}

	public void setOldEstablishmentID(String oldEstablishmentID) {
		this.oldEstablishmentID = oldEstablishmentID;
	}

	public String getOldEstablishmentID() {
		return oldEstablishmentID;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldSecondname() {
		return oldSecondname;
	}

	public void setOldSecondname(String oldSecondname) {
		this.oldSecondname = oldSecondname;
	}

	public String getOldDescription() {
		return oldDescription;
	}

	public void setOldDescription(String oldDescription) {
		this.oldDescription = oldDescription;
	}

	public String getOldEmail() {
		return oldEmail;
	}

	public void setOldEmail(String oldEmail) {
		this.oldEmail = oldEmail;
	}

	public String getOldFax() {
		return oldFax;
	}

	public void setOldFax(String oldFax) {
		this.oldFax = oldFax;
	}

	public String getOldTelephone() {
		return oldTelephone;
	}

	public void setOldTelephone(String oldTelephone) {
		this.oldTelephone = oldTelephone;
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

	public void setOldDeactivationDate(Date oldDeactivationDate) {
		this.oldDeactivationDate = oldDeactivationDate;
	}

	public Date getOldDeactivationDate() {
		return oldDeactivationDate;
	}

	public void setNewOID(String newOID) {
		this.newOID = newOID;
	}

	public String getNewOID() {
		return newOID;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewName() {
		return newName;
	}

	public String getNewSecondname() {
		return newSecondname;
	}

	public void setNewSecondname(String newSecondname) {
		this.newSecondname = newSecondname;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewEstablishmentID(String newEstablishmentID) {
		this.newEstablishmentID = newEstablishmentID;
	}

	public String getNewEstablishmentID() {
		return newEstablishmentID;
	}

	public void setNewReactivationReasonCode(String newReactivationReasonCode) {
		this.newReactivationReasonCode = newReactivationReasonCode;
	}

	public String getNewReactivationReasonCode() {
		return newReactivationReasonCode;
	}

	public void setNewLastUpdateDate(Date newLastUpdateDate) {
		this.newLastUpdateDate = newLastUpdateDate;
	}

	public Date getNewLastUpdateDate() {
		return newLastUpdateDate;
	}

	public void setNewReactivationDate(Date newReactivationDate) {
		this.newReactivationDate = newReactivationDate;
	}

	public Date getNewReactivationDate() {
		return newReactivationDate;
	}

	public void setNewDeactivationReasonCode(String newDeactivationReasonCode) {
		this.newDeactivationReasonCode = newDeactivationReasonCode;
	}

	public String getNewDeactivationReasonCode() {
		return newDeactivationReasonCode;
	}

	public void setNewDeactivationDate(Date newDeactivationDate) {
		this.newDeactivationDate = newDeactivationDate;
	}

	public Date getNewDeactivationDate() {
		return newDeactivationDate;
	}

	public void setNewTelephone(String newTelephone) {
		this.newTelephone = newTelephone;
	}

	public String getNewTelephone() {
		return newTelephone;
	}

	public void setNewFax(String newFax) {
		this.newFax = newFax;
	}

	public String getNewFax() {
		return newFax;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public Integer getOldPorsuserID() {
		return oldPorsuserID;
	}

	public void setOldPorsuserID(Integer oldPorsuserID) {
		this.oldPorsuserID = oldPorsuserID;
	}

	public Integer getNewPorsuserID() {
		return newPorsuserID;
	}

	public void setNewPorsuserID(Integer newPorsuserID) {
		this.newPorsuserID = newPorsuserID;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}

}
