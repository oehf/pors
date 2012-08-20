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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Version;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;

/**
 * <p>
 * Entity bean representing an organisation which can be explicitly identified 
 * by its id or name.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all available organisations.</li>
 * <li><b>QUERY_NAME_BY_ESTABLISHMENT_ID</b><br />Selects an organisation by 
 * the establishment ID defined using parameter 
 * <code>PARAM_ESTABLISHMENT_ID</code>.</li>
 * <li><b>QUERY_NAME_BY_ESTABLISHMENT_ID_EXCLUDE_ORGANISATION_ID</b><br />
 * Selects an organisation by the establishment ID defined in parameter 
 * <code>PARAM_ESTABLISHMENT_ID</code> and not having the ID defined in 
 * parameter <code>PARAM_ORGANISATION_ID</code>.</li>
 * <li><b>QUERY_NAME_BY_LOCAL_ID_FACILITY_APPLICATION</b><br />Selects an 
 * organisation by the local ID defined by the parameters 
 * <code>PARAM_LOCAL_ID</code>, <code>PARAM_FACILITY</code> and 
 * <code>PARAM_APPLICATION</code>.</li>
 * <li><b>QUERY_NAME_BY_NAME</b><br />Selects an organisation by its name 
 * defined by parameter <code>PARAM_NAME</code>.</li>
 * <li><b>QUERY_NAME_BY_NAME_EXCLUDE_ORGANISATION_ID</b><br />Selects the 
 * organisation having the name defined with <code>PARAM_NAME</code> and not 
 * having the ID defined with <code>PARAM_ORGANISATION_ID</code>.</li> 
 * <li><b>QUERY_NAME_BY_NAME_OR_SECOND_NAME</b><br />Selects the organisation
 * having the name defined with <code>PARAM_NAME</code> as name or second name.
 * </li>
 * <li><b>QUERY_NAME_BY_OID</b><br />Selects an organisation by the oid defined
 * with <code>PARAM_OID</code>.</li>
 * <li><b>QUERY_NAME_BY_OID_EXCLUDE_ORGANISATION_ID</b><br />Selects an 
 * organisation by the oid defined with <code>PARAM_OID</code> and not having 
 * the ID defined with <code>PARAM_ORGANISATION_ID</code>.</li>
 * <li><b>QUERY_NAME_BY_PROVIDER_ID</b><br />Selects all organisations linked to
 * the provider having the ID defined with <code>PARAM_PROVIDER_ID</code>.</li>
 * <li><b>QUERY_NAME_BY_PROVIDER_ID_AND_USER_ID</b><br />Selects all 
 * organisations linked to the provider having the ID defined with 
 * <code>PARAM_PROVIDER_ID</code> and being owned by the user having the ID 
 * defined with parameter <code>PARAM_USER_ID</code>.</li>
 * <li><b>QUERY_NAME_BY_PROVIDER_ID_AND_USER_NAME</b><br />Selects all 
 * organisations linked to the provider having the ID defined with parameter 
 * <code>PARAM_PROVIDER_ID</code> and being owned by the user having the name 
 * defined with parameter <code>PARAM_USER_NAME</code>.</li>
 * <li><b>QUERY_NAME_BY_SECOND_NAME</b><br />Selects the organisation having 
 * the second name defined with parameter <code>PARAM_SECOND_NAME</code>.</li>
 * <li><b>QUERY_NAME_BY_SECOND_NAME_EXCLUDE_ORGANISATION_ID</b><br />Selects
 * the organisation having the second name defined with parameter 
 * <code>PARAM_SECOND_NAME</code> and not having the ID defined with parameter 
 * <code>PARAM_ORGANISATION_ID</code>.</li>
 * <li><b>QUERY_NAME_BY_USER_ID</b><br />Selects the organisations owned by the 
 * user having the ID defined with <code>PARAM_USER_ID</code>.</li>
 * <li><b>QUERY_NAME_BY_USER_NAME</b><br />Selects the organisations owned by 
 * the user having the name defined with <code>PARAM_USER_NAME</code>.</li>
 * <li><b>QUERY_NAME_COUNT_ALL</b><br />Returns the total number of 
 * all organisations.</li>
 * <li><b>QUERY_NAME_COUNT_BY_USER_ID</b><br />Counts all organisations owned 
 * by the PORS user having the ID defined with <code>PARAM_USER_ID</code>.</li>
 * <li><b>QUERY_NAME_IS_ADDRESS_OWNING_USER_VIA_ORGANISATION</b><br />Selects 
 * the number of organisations where the user having the ID defined with 
 * <code>PARAM_USER_ID</code> owns any organisation which is linked to the 
 * address having the ID defined with <code>PARAM_ADDRESS_ID</code>.
 * <li><b>QUERY_NAME_IS_LOCAL_ID_OWNING_USER_VIA_ORGANISATION</b><br />Selects 
 * the number of organisations where the user having the ID defined with 
 * <code>PARAM_USER_ID</code> owns any organisation which is linked to the 
 * local ID having the ID defined with <code>PARAM_LOCAL_ID_ID</code>.</li>
 * <li><b>QUERY_NAME_IS_ORGANISATION_ID_RELATED_TO_PROVIDER_ID</b><br />Selects
 * <code>1</code> if the organisation having the ID defined with 
 * <code>PARAM_ORGANISATION_ID</code> is linked to the provider defined with 
 * <code>PARAM_PROVIDER_ID</code>. Else selects <code>0</code>.</li>
 * <li><b>QUERY_NAME_UPDATE_ALL_DUPLICATES_CALCULATED</b><br />Updates the 
 * duplicates calculated field of all organisations to the value defined with 
 * <code>PARAM_CALCULATED</code>.</li>
 * <li><b>QUERY_NAME_UPDATE_DUPLICATES_CALCULATED</b><br />Updates the 
 * duplicates calculated field of the organisation having the ID defined with 
 * <code>PARAM_ORGANISATION_ID</code> to the value defined with 
 * <code>PARAM_CALCULATED</code>.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name="Organisation", uniqueConstraints={@UniqueConstraint(columnNames={"Name"})})
@NamedQueries(value = {@NamedQuery(name = "getOrganisationList", query = "SELECT o FROM Organisation o ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "getOrganisationByName", query = "SELECT o FROM Organisation o WHERE o.name = :name ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "getOrganisationByNameExcludingOrganisationId", query = "SELECT o FROM Organisation o WHERE o.name = :name AND o.id <> :organisationId"),
		@NamedQuery(name = "getOrganisationByOid", query = "SELECT o FROM Organisation o WHERE o.oid = :oid ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "getOrganisationByOidExcludingOrganisationId", query = "SELECT o FROM Organisation o WHERE o.oid = :oid AND o.id = :organisationId"),
		@NamedQuery(name = "getOrganisationByEstablishmentId", query = "SELECT o FROM Organisation o WHERE o.establishmentId = :establishmentId ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "getOrganisationByEstablishmentIdExcludingOrganisationId", query = "SELECT o FROM Organisation o WHERE o.establishmentId = :establishmentId AND o.id <> :organisationId"),
		@NamedQuery(name = "getOrganisationByLocalIdFacilityApplication", query = "SELECT lid.organisation FROM LocalId lid WHERE lid.organisation IS NOT NULL AND lid.localId = :localId AND lid.facility = :facility AND lid.application = :application"),
		@NamedQuery(name = "getOrganisationListOfUserId", query = "SELECT o FROM Organisation o WHERE o.user.id = :userId ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "getOrganisationListOfUserName", query = "SELECT o FROM Organisation o WHERE o.user.name = :userName ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "getOrganisationListForProviderId", query = "SELECT p.organisations FROM Provider p WHERE p.id = :providerId"), 
		@NamedQuery(name = "getOrganisationListForProviderIdOfUserId", query = "SELECT p.organisations FROM Provider p, IN (p.organisations) o WHERE p.id = :providerId AND o.user.id = :userId ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "getOrganisationBySecondname", query = "SELECT o FROM Organisation o WHERE o.secondName = :secondName"),
		@NamedQuery(name = "getOrganisationBySecondnameExcludingOrganisationId", query = "SELECT o FROM Organisation o WHERE o.secondName = :secondName AND o.id <> :organisationId"),
		@NamedQuery(name = "getOrganisationByNameOrSecondName", query = "SELECT o FROM Organisation o WHERE o.name = :name OR o.secondName = :name"),
		@NamedQuery(name = "getOrganisationListForProviderIdOfUserName", query = "SELECT p.organisations FROM Provider p, IN (p.organisations) o WHERE p.id = :providerId AND o.user.name = :userName ORDER BY o.name ASC, o.secondName ASC"),
		@NamedQuery(name = "isOrganisationOwningUserOfAddress", query = "SELECT COUNT(o.addresses) FROM Organisation o, IN (o.addresses) a WHERE o.user.id = :userId AND a.id = :addressId"), 
		@NamedQuery(name = "isOrganisationOwningUserOfLocalId", query = "SELECT COUNT(o.localIds) FROM Organisation o, IN (o.localIds) lid WHERE o.user.id = :userId AND lid.id = :localIdId"),
		@NamedQuery(name = "isOrganisationIdRelatedToProviderId", query = "SELECT COUNT(p) FROM Provider p, IN (p.organisations) o WHERE o.id = :organisationId AND p.id = :providerId"),
		@NamedQuery(name = "updateDuplicatesCalculatedOrganisation", query = "UPDATE Organisation o SET o.duplicatesCalculated = :isCalculated"),
		@NamedQuery(name = "countAllOrganisations", query = "SELECT COUNT(o) FROM Organisation o"),
		@NamedQuery(name = "updateDuplicatesCalculatedForOrganisationId", query = "UPDATE Organisation o SET o.duplicatesCalculated = :isCalculated WHERE o.id = :organisationId"),
		@NamedQuery(name = "countAllOrganisationsOfUser", query = "SELECT COUNT(o) FROM Organisation o WHERE o.user.id = :userId")
		})
@Indexed
public class Organisation implements Serializable, IMasterDomain 
{
	private static final long serialVersionUID = 875638736764000639L;
	
	public static final String PARAM_ADDRESS_ID = "addressId";
	public static final String PARAM_APPLICATION = "application";
	public static final String PARAM_CALCULATED = "isCalculated";
	public static final String PARAM_ESTABLISHMENT_ID = "establishmentId";
	public static final String PARAM_FACILITY = "facility";
	public static final String PARAM_LOCAL_ID = "localId";
	public static final String PARAM_LOCAL_ID_ID = "localIdId";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_OID = "oid";
	public static final String PARAM_ORGANISATION_ID = "organisationId";
	public static final String PARAM_PROVIDER_ID = "providerId";
	public static final String PARAM_SECOND_NAME = "secondName";
	public static final String PARAM_USER_ID = "userId";
	public static final String PARAM_USER_NAME = "userName";
	
	public static final String QUERY_NAME_ALL = "getOrganisationList";
	public static final String QUERY_NAME_BY_ESTABLISHMENT_ID = "getOrganisationByEstablishmentId";
	public static final String QUERY_NAME_BY_ESTABLISHMENT_ID_EXCLUDE_ORGANISATION_ID = "getOrganisationByEstablishmentIdExcludingOrganisationId";
	public static final String QUERY_NAME_BY_LOCAL_ID_FACILITY_APPLICATION = "getOrganisationByLocalIdFacilityApplication";
	public static final String QUERY_NAME_BY_NAME = "getOrganisationByName";
	public static final String QUERY_NAME_BY_NAME_EXCLUDE_ORGANISATION_ID = "getOrganisationByNameExcludingOrganisationId";
	public static final String QUERY_NAME_BY_NAME_OR_SECOND_NAME = "getOrganisationByNameOrSecondName";
	public static final String QUERY_NAME_BY_OID = "getOrganisationByOid";
	public static final String QUERY_NAME_BY_OID_EXCLUDE_ORGANISATION_ID = "getOrganisationByOidExcludingOrganisationId";
	public static final String QUERY_NAME_BY_PROVIDER_ID = "getOrganisationListForProviderId";
	public static final String QUERY_NAME_BY_PROVIDER_ID_AND_USER_ID = "getOrganisationListForProviderIdOfUserId";
	public static final String QUERY_NAME_BY_PROVIDER_ID_AND_USER_NAME = "getOrganisationListForProviderIdOfUserName";
	public static final String QUERY_NAME_BY_SECOND_NAME = "getOrganisationBySecondname";
	public static final String QUERY_NAME_BY_SECOND_NAME_EXCLUDE_ORGANISATION_ID = "getOrganisationBySecondnameExcludingOrganisationId";
	public static final String QUERY_NAME_BY_USER_ID = "getOrganisationListOfUserId";
	public static final String QUERY_NAME_BY_USER_NAME = "getOrganisationListOfUserName";
	public static final String QUERY_NAME_COUNT_ALL = "countAllOrganisations";
	public static final String QUERY_NAME_COUNT_BY_USER_ID = "countAllOrganisationsOfUser";
	public static final String QUERY_NAME_IS_ADDRESS_OWNING_USER_VIA_ORGANISATION = "isOrganisationOwningUserOfAddress";
	public static final String QUERY_NAME_IS_LOCAL_ID_OWNING_USER_VIA_ORGANISATION = "isOrganisationOwningUserOfLocalId";
	public static final String QUERY_NAME_IS_ORGANISATION_ID_RELATED_TO_PROVIDER_ID = "isOrganisationIdRelatedToProviderId";
	public static final String QUERY_NAME_UPDATE_ALL_DUPLICATES_CALCULATED = "updateDuplicatesCalculatedOrganisation";
	public static final String QUERY_NAME_UPDATE_DUPLICATES_CALCULATED = "updateDuplicatesCalculatedForOrganisationId";
	
	@IndexedEmbedded
	@ManyToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
    	    name="OrganisationHasAddress",
    	    joinColumns=
    	        @JoinColumn(name="RegionalOrganisationId", referencedColumnName="RegionalOrganisationId"),
    	    inverseJoinColumns=
    	        @JoinColumn(name="AddressId", referencedColumnName="AddressId")
    	    )
	private List<Address> addresses;
	
	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Column(nullable=true)
	private Date deactivationDate;
	
	@Field
	@Column(nullable=true)
	private String deactivationReasonCode;

	@Field
	@Column(nullable=true)
	private String description;
	
	private boolean duplicatesCalculated;
	
	@Transient
	private PorsUser editingUser;
	
	@Field
	@Column(nullable=true)
	private String email;
	
	@Field
	@Column(nullable=true)
	private String establishmentId;
	
	@Field
	@Column(nullable=true)
	private String fax;
	
	@Id
	@GeneratedValue
	@DocumentId
	@Field
	@Column(name="RegionalOrganisationId")
	private Long id;
	
	@Transient
	private String ipAddress;
	
	@Field
	@DateBridge(resolution = Resolution.MILLISECOND)
	private Date lastUpdateDate;
	
	@IndexedEmbedded
	@OneToMany(mappedBy = "organisation", 
			cascade = {CascadeType.DETACH, CascadeType.MERGE, 
			CascadeType.PERSIST, CascadeType.REFRESH})
	private List<LocalId> localIds;
	
	@Field
	private String name;
	
	@Field
	@Column(name="OID", nullable=true)
	private String oid;
	
	@ManyToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(
			name="OrganisationHasProvider",
			joinColumns=
				@JoinColumn(name="RegionalOrganisationId", referencedColumnName="RegionalOrganisationId"),
			inverseJoinColumns=
				@JoinColumn(name="RegionalProviderId", referencedColumnName="RegionalProviderId"))
	private List<Provider> providers;
	
	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Column(nullable = true)
	private Date reactivationDate;
	
	@Field
	@Column(nullable = true)
	private String reactivationReasonCode;
	
	@Field
	@Column(name="SecondName", nullable=true)
	private String secondName;
	
	@Transient
	private String sessionId;
	
	@Field
	@Column(nullable=true)
	private String telephone;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	@Version
	private Long version;

	public List<Address> getAddresses() {
		return addresses;
	}
	
	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public String getDeactivationReasonCode() {
		return deactivationReasonCode;
	}

	public String getDescription() {
		return description;
	}

	public PorsUser getEditingUser() {
		return editingUser;
	}

	public String getEmail() {
		return email;
	}

	/**
	 * <p>
	 * Getter method for this establishment ID.
	 * </p>
	 * 
	 * @return
	 * 		Estabilshment ID
	 */
	public String getEstablishmentId() 
	{
		return this.establishmentId;
	}

	public String getFax() {
		return fax;
	}

	public Long getId() {
		return id;
	}

	/**
	 * <p>
	 * Getter method for this IP address.
	 * </p>
	 * 
	 * @return 
	 * 		IP address
	 */
	public String getIpAddress() 
	{
		return this.ipAddress;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public List<LocalId> getLocalIds() {
		return localIds;
	}

	public String getName() {
		return name;
	}

	public String getOid() {
		return oid;
	}

	public List<Provider> getProviders() {
		return providers;
	}

	public Date getReactivationDate() {
		return reactivationDate;
	}

	public String getReactivationReasonCode() {
		return reactivationReasonCode;
	}

	public String getSecondName() {
		return secondName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getTelephone() {
		return telephone;
	}

	public PorsUser getUser() {
		return user;
	}

	public Long getVersion() {
		return version;
	}

	public boolean isDuplicatesCalculated() {
		return duplicatesCalculated;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public void setDeactivationReasonCode(String deactivationReasonCode) {
		this.deactivationReasonCode = deactivationReasonCode;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuplicatesCalculated(boolean duplicatesCalculated) {
		this.duplicatesCalculated = duplicatesCalculated;
	}

	public void setEditingUser(PorsUser editingUser) {
		this.editingUser = editingUser;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEstablishmentId(String estabilshmentId) {
		this.establishmentId = estabilshmentId;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public void setLocalIds(List<LocalId> localIds) {
		this.localIds = localIds;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public void setProviders(List<Provider> providers) {
		this.providers = providers;
	}

	public void setReactivationDate(Date reactivationDate) {
		this.reactivationDate = reactivationDate;
	}

	public void setReactivationReasonCode(String reactivationReasonCode) {
		this.reactivationReasonCode = reactivationReasonCode;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setUser(PorsUser user) {
		this.user = user;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
