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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

/**
 * <p>
 * Entity bean representing provider which can be explicitly defined by its id 
 * or combination of fist name, last name, gender and birthday.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>{@link #QUERY_NAME_ALL}</b><br />Returns a list of all available 
 * providers.</li>
 * <li><b>getProviderListOfUserId</b><br />Returns a list of all providers created by the user specified by its id in parameter 1.</li>
 * <li><b>getProviderListOfUserName</b><br />Returns a list of all providers created by the user specified by its name in parameter 1.</li>
 * <li><b>getProviderByFirstNameLastNameGenderBirthday</b><br />Returns a provider identified by its first name in parameter 1, last name in parameter 2, gender code in parameter 3 and birthday in parameter 4.</li>
 * <li><b>getProviderByFirstNameLastNameGenderBirthdayIsNull</b><br />Returns a provider identified by its first name in parameter 1, last name in parameter 2, gender code in parameter 3 and value <code>NULL</code> for birthday.</li>
 * <li><b>getProviderByLocalIdFacility</b><br />Returns the provider dedicated to the local id specified in parameter 1 belonging to the facility specified in parameter 2.</li>
 * <li><b>{@link #QUERY_NAME_COUNT_ALL}</b><br />Returns the total number of 
 * all providers.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name="Provider", 
		uniqueConstraints={@UniqueConstraint(columnNames={"FirstName", "LastName", "GenderCode", "Birthday"}), @UniqueConstraint(columnNames={"LANR"})})
@NamedQueries(value={
		@NamedQuery(name = "getProviderList",query = "SELECT p FROM Provider p ORDER BY p.lastName ASC, p.firstName ASC"),
		@NamedQuery(name = "getProviderByFirstNameLastNameGenderBirthday", query = "SELECT p FROM Provider p WHERE p.firstName = ?1 AND p.lastName = ?2 AND p.genderCode = ?3 AND p.birthday = ?4 ORDER BY p.lastName ASC, p.firstName ASC"),
		@NamedQuery(name = "getProviderByFirstNameLastNameGenderBirthdayIsNull", query = "SELECT p FROM Provider p WHERE p.firstName = ?1 AND p.lastName = ?2 AND p.genderCode = ?3 AND p.birthday IS NULL ORDER BY p.lastName ASC, p.firstName ASC"),
		@NamedQuery(name = "getProviderListOfUserId", query = "SELECT p FROM Provider p WHERE p.user.id = ?1 ORDER BY p.lastName ASC, p.firstName ASC"),
		@NamedQuery(name = "getProviderListOfUserName", query = "SELECT p FROM Provider p WHERE p.user.name = ?1 ORDER BY p.lastName ASC, p.firstName ASC"),
		@NamedQuery(name = "getProviderByLocalIdFacilityApplication", query = "SELECT lid.provider FROM LocalId lid WHERE lid.provider IS NOT NULL AND lid.localId = ?1 AND lid.facility = ?2 AND lid.application = ?3"),
		@NamedQuery(name = "getProviderListForOrganisationId", query = "SELECT o.providers FROM Organisation o WHERE o.id = ?1"), 
		@NamedQuery(name = "getProviderListForOrganisationIdOfUserId", query = "SELECT o.providers FROM Organisation o, IN (o.providers) p WHERE o.id = ?1 AND p.user.id = ?2 ORDER BY p.lastName ASC, p.firstName ASC"),
		@NamedQuery(name = "getProviderByLanr", query = "SELECT p FROM Provider p where lanr = ?1"),
		@NamedQuery(name = "getProviderByLanrExcludingProviderId", query = "SELECT p FROM Provider p WHERE p.lanr = ?1 AND p.id <> ?2"),
		@NamedQuery(name = "getProviderByOid", query = "SELECT p FROM Provider p where oid = ?1"),
		@NamedQuery(name = "getProviderByOidExcludingProviderId", query = "SELECT p FROM Provider p WHERE p.oid = ?1 AND p.id <> ?2"),
		@NamedQuery(name = "getProviderByFirstNameLastNameGenderBirthdayExcludingProviderId", query = "SELECT p FROM Provider p WHERE p.firstName = :firstName AND p.lastName = :lastName AND p.genderCode = :genderCode AND p.birthday = :birthday AND p.id <> :providerId"),
		@NamedQuery(name = "getProviderByFirstNameLastNameGenderBirthdayIsNullExcludingProviderId", query = "SELECT p FROM Provider p WHERE p.firstName = :firstName AND p.lastName = :lastName AND p.genderCode = :genderCode AND p.birthday IS NULL AND p.id <> :providerId"),
		@NamedQuery(name = "getProviderListForOrganisationIdOfUserName", query = "SELECT o.providers FROM Organisation o, IN (o.providers) p WHERE o.id = ?1 AND p.user.name = ?2 ORDER BY p.lastName ASC, p.firstName ASC"),
		@NamedQuery(name = "isProviderOwningUserOfAddress", query = "SELECT COUNT(p.addresses) FROM Provider p, IN (p.addresses) a WHERE p.user.id = ?1 AND a.id = ?2"),
		@NamedQuery(name = "isProviderOwningUserOfLocalId", query = "SELECT COUNT(p.localIds) FROM Provider p, IN (p.localIds) lid WHERE p.user.id = ?1 AND lid.id = ?2"),
		@NamedQuery(name = "countAllProviders", query = "SELECT COUNT(p) FROM Provider p"),
		@NamedQuery(name = "updateDuplicatesCalculatedProvider", query = "UPDATE Provider p SET p.duplicatesCalculated = ?1"),
		@NamedQuery(name = "updateDuplicatesCalculatedForProviderId", query = "UPDATE Provider p SET p.duplicatesCalculated = :isCalculated WHERE p.id = :providerId"),
		@NamedQuery(name = "countAllProvidersOfUser", query = "SELECT COUNT(p) FROM Provider p WHERE p.user.id = :userId")
		})
@Indexed
@AnalyzerDef(
		name = "applicationAnalyzer1",
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
			@TokenFilterDef(factory = StandardFilterFactory.class),
			@TokenFilterDef(factory = LowerCaseFilterFactory.class)
		}
)
public class Provider implements Serializable, IMasterDomain 
{
	private static final long serialVersionUID = 7244988455455104683L;
	
	public static final String QUERY_NAME_ALL = "getProviderList";
	public static final String QUERY_NAME_COUNT_ALL = "countAllProviders";
	public static final String QUERY_NAME_UPDATE_DUPLICATES_CALCULATED = "updateDuplicatesCalculatedForProviderId";
	public static final String QUERY_NAME_BY_UNIQUE_COMBINATION_EXCLUDE_PROVIDER_ID = "getProviderByFirstNameLastNameGenderBirthdayExcludingProviderId";
	public static final String QUERY_NAME_BY_UNIQUE_COMBINATION_BIRTHDAY_NULL_EXCLUDE_PROVIDER_ID = "getProviderByFirstNameLastNameGenderBirthdayIsNullExcludingProviderId";
	public static final String QUERY_NAME_COUNT_BY_USER_ID = "countAllProvidersOfUser";
	
	public static final String PARAM_CALCULATED = "isCalculated";
	public static final String PARAM_PROVIDER_ID = "providerId";
	public static final String PARAM_FIRST_NAME = "firstName";
	public static final String PARAM_LAST_NAME = "lastName";
	public static final String PARAM_GENDER_CODE = "genderCode";
	public static final String PARAM_BIRTHDAY = "birthday";
	public static final String PARAM_USER_ID = "userId";
	
	public static final String GENDER_CODE_MALE = "m";
	public static final String GENDER_CODE_FEMALE = "f";
	public static final String GENDER_CODE_UNKNOWN = "u";

	@Transient
	private PorsUser editingUser;
	
	@Transient
	private String sessionId;
	
	@Transient
	private String ipAddress;
	
	@Id
	@GeneratedValue
	@DocumentId
	@Field
	@Column(name="RegionalProviderId")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	@Field
	@Column(nullable=true)
	private String lanr;
	
	@Field
	@Column(name="OID", nullable=true)
	private String oid;
	
	@Field
	private String firstName;
	
	@Field
	private String lastName;
	
	@Field
	@Column(nullable=true)
	private String middleName;
	
	@Field
	@Column(nullable=true)
	private String namePrefix;
	
	@Field
	@Column(nullable=true)
	private String nameSuffix;
	
	@Field
	private String genderCode;
	
	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Column(nullable=true)
	private Date birthday;
	
	@Field
	@Column(nullable=true)
	private String specialisation;
	
	@Field
	@Column(nullable=true)
	private String email;
	
	@Field
	@Column(nullable=true)
	private String telephone;
	
	@Field
	@Column(nullable=true)
	private String fax;
	
	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Column(nullable=true)
	private Date deactivationDate;
	
	@Field
	@Column(nullable=true)
	private String deactivationReasonCode;
	
	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Column(nullable=true)
	private Date reactivationDate;
	
	@Field
	@Column(nullable=true)
	private String reactivationReasonCode;
	
	@Field
	@DateBridge(resolution = Resolution.MILLISECOND)
	private Date lastUpdateDate;
	
	private boolean duplicatesCalculated;
	
	@Version
	private Long version;
	
	@IndexedEmbedded
	@ManyToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
    	    name="ProviderHasAddress",
    	    joinColumns=
    	        @JoinColumn(name="RegionalProviderId", referencedColumnName="RegionalProviderId"),
    	    inverseJoinColumns=
    	        @JoinColumn(name="AddressId", referencedColumnName="AddressId")
    	    )
	private List<Address> addresses;
	
	@ManyToMany(mappedBy="providers",
			cascade={CascadeType.REFRESH, CascadeType.DETACH})
	private List<Organisation> organisations;
	
	@IndexedEmbedded
	@OneToMany(mappedBy = "provider", 
			cascade = { CascadeType.DETACH, CascadeType.MERGE, 
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<LocalId> localIds;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}

	public String getLanr() {
		return lanr;
	}

	public void setLanr(String lanr) {
		this.lanr = lanr;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public String getNameSuffix() {
		return nameSuffix;
	}

	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	public String getGenderCode() {
		return genderCode;
	}

	/**
	 * <p>
	 * Sets the gender code.
	 * </p>
	 * <p>
	 * If <code>genderCode</code> is <code>null</code>, this gender code 
	 * will automatically be set to value {@link #GENDER_CODE_UNKNOWN}.
	 * </p>
	 * 
	 * @param genderCode
	 * 		The gender code
	 */
	public void setGenderCode(String genderCode) 
	{
		if (genderCode == null || genderCode.equals(""))
		{
			this.genderCode = "u";
		}
		else
		{
			this.genderCode = genderCode;
		}
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public String getDeactivationReasonCode() {
		return deactivationReasonCode;
	}

	public void setDeactivationReasonCode(String deactivationReasonCode) {
		this.deactivationReasonCode = deactivationReasonCode;
	}

	public Date getReactivationDate() {
		return reactivationDate;
	}

	public void setReactivationDate(Date reactivationDate) {
		this.reactivationDate = reactivationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> adresses) {
		this.addresses = adresses;
	}

	public void setUser(PorsUser user) {
		this.user = user;
	}

	public PorsUser getUser() {
		return user;
	}

	public void setReactivationReasonCode(String reactivationReasonCode) {
		this.reactivationReasonCode = reactivationReasonCode;
	}

	public String getReactivationReasonCode() {
		return reactivationReasonCode;
	}

	public void setOrganisations(List<Organisation> organisations) {
		this.organisations = organisations;
	}

	public List<Organisation> getOrganisations() {
		return organisations;
	}

	public PorsUser getEditingUser() {
		return editingUser;
	}

	public void setEditingUser(PorsUser editingUser) {
		this.editingUser = editingUser;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setLocalIds(List<LocalId> localIds) {
		this.localIds = localIds;
	}

	public List<LocalId> getLocalIds() {
		return localIds;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public boolean isDuplicatesCalculated() {
		return duplicatesCalculated;
	}

	public void setDuplicatesCalculated(boolean duplicatesCalculated) {
		this.duplicatesCalculated = duplicatesCalculated;
	}

	public String getSpecialisation() {
		return specialisation;
	}

	public void setSpecialisation(String specialisation) {
		this.specialisation = specialisation;
	}
}
