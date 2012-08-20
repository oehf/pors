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
import java.util.List;

/**
 * The DTO for Organisation
 * 
 * @author mf
 * 
 */
public class OrganisationDTO {

	private Long id;
	private Integer porsuserID;
	private String OID;
	private String establishmentID;
	private String name;
	private String secondName;
	private String description;
	private String email;
	private String fax;
	private String telephone;
	private Date deactivationDate;
	private String deactivationReasonCode;
	private Date reactivationDate;
	private String reactivationReasonCode;
	private Date lastUpdateDate;
	private Long version;

	private Integer editingUserID;
	private String sessionId;
	private String ip;

	private List<Long> addressID;
	private List<AddressDTO> addresses;
	private List<LocalIdDTO> localId;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setPorsuserID(Integer porsuserID) {
		this.porsuserID = porsuserID;
	}

	public Integer getPorsuserID() {
		return porsuserID;
	}

	public void setOID(String oID) {
		OID = oID;
	}

	public String getOID() {
		return OID;
	}

	public void setEstablishmentID(String establishmentID) {
		this.establishmentID = establishmentID;
	}

	public String getEstablishmentID() {
		return establishmentID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationReasonCode(String deactivationReasonCode) {
		this.deactivationReasonCode = deactivationReasonCode;
	}

	public String getDeactivationReasonCode() {
		return deactivationReasonCode;
	}

	public void setReactivationDate(Date reactivationDate) {
		this.reactivationDate = reactivationDate;
	}

	public Date getReactivationDate() {
		return reactivationDate;
	}

	public void setReactivationReasonCode(String reactivationReasonCode) {
		this.reactivationReasonCode = reactivationReasonCode;
	}

	public String getReactivationReasonCode() {
		return reactivationReasonCode;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setAddressID(List<Long> addressID) {
		this.addressID = addressID;
	}

	public List<Long> getAddressID() {
		return addressID;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public Integer getEditingUserID() {
		return editingUserID;
	}

	public void setEditingUserID(Integer editingUserID) {
		this.editingUserID = editingUserID;
	}

	public void setLocalId(List<LocalIdDTO> localId) {
		this.localId = localId;
	}

	public List<LocalIdDTO> getLocalId() {
		return localId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getVersion() {
		return version;
	}
	//
	// public boolean equals(Object obj){
	// if(obj instanceof OrganisationDTO){
	// OrganisationDTO dto = (OrganisationDTO) obj;
	// if(dto.getName().equals(this.getName())){
	// return true;
	// }
	// }
	// return false;
	// }
}
