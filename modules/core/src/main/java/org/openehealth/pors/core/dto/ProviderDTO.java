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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The DTO for Provider
 * 
 * @author mf
 * 
 */
public class ProviderDTO implements Serializable {

	private static final long serialVersionUID = 1728074262578921676L;
	private Long id;
	private Integer porsuserid;
	private String lanr;
	private String oid;

	private String firstname;
	private String lastname;

	private String middleName;
	private String namePrefix;
	private String nameSuffix;
	private String genderCode;
	private String specialisation;

	private Date birthday;
	private String email;
	private String telephone;
	private String fax;

	private Long version;
	private Date deactivationDate;
	private String deactivationReasonCode;
	private Date reactivationDate;
	private String reactivationReasonCode;
	private Date lastUpdateDate;

	private Integer editingUserID;
	private String sessionId;
	private String ip;

	private List<AddressDTO> addresses;
	private List<LocalIdDTO> localId;
	private List<OrganisationDTO> organisations;

	public List<OrganisationDTO> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<OrganisationDTO> organisations) {
		this.organisations = organisations;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setLanr(String lanr) {
		this.lanr = lanr;
	}

	public String getLanr() {
		return lanr;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOid() {
		return oid;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	public String getNameSuffix() {
		return nameSuffix;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
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

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public void setPorsuserid(Integer porsuserid) {
		this.porsuserid = porsuserid;
	}

	public Integer getPorsuserid() {
		return porsuserid;
	}

	public void setReactivationReasonCode(String reactivationReasonCode) {
		this.reactivationReasonCode = reactivationReasonCode;
	}

	public String getReactivationReasonCode() {
		return reactivationReasonCode;
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

	public void setSpecialisation(String specialisation) {
		this.specialisation = specialisation;
	}

	public String getSpecialisation() {
		return specialisation;
	}

}
