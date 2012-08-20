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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * <p>
 * Entity bean mapping entries in table "Address" and thus representing an address which 
 * can be explicitly defined by its id or a combination of the fields street, 
 * houseNumber, zipCode, city, country and additional.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>{@link #QUERY_NAME_ALL}</b><br />Selects all available addresses.
 * </li>
 * <li><b>{@link #QUERY_NAME_BY_UNIQUE_COMBINATION}</b><br />Selects an address
 * by the unique combination of additional, street, house number, ZIP code, city 
 * and country. These attributes can be set using parameters 
 * <code>PARAM_ADDITIONAL</code>, <code>PARAM_STREET</code>, 
 * <code>PARAM_HOUSE_NUMBER</code>, <code>PARAM_ZIP_CODE</code>, 
 * <code>PARAM_CITY</code> and <code>PARAM_COUNTRY</code>.</li>
 * <li><b>{@link #QUERY_NAME_UPDATE_ALL_DUPLICATES_CALCULATED}</b><br />Sets the 
 * "duplicates calculated" flag of all addresses to the boolean value of 
 * <code>PARAM_CALCULATED</code>.</li>
 * <li><b>{@link #QUERY_NAME_UPDATE_DUPLICATES_CALCULATED}</b><br />Sets the 
 * "duplicates calculated" flag of the address having the long value of 
 * <code>PARAM_ID</code> as ID to the boolean value of 
 * <code>PARAM_CALCULATED</code>.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name="Address", 
		uniqueConstraints={
			@UniqueConstraint(columnNames={"Street","HouseNumber", "ZIPCode", "City", "Country", "Additional"})})
@NamedQueries(value = {
		@NamedQuery(name = "getAddressList", query = "SELECT a FROM Address a"),
		@NamedQuery(name = "updateDuplicatesCalculatedAddress", query = "UPDATE Address a SET a.duplicatesCalculated = :isCalculated"),
		@NamedQuery(name = "getAddressByAllFields", query = "SELECT a FROM Address a WHERE a.street = :street AND a.houseNumber = :housenumber AND a.zipCode = :zipcode AND a.city = :city AND a.country = :country AND a.additional = :additional"),
		@NamedQuery(name = "updateDuplicatesCalculatedForAddressId", query = "UPDATE Address a SET a.duplicatesCalculated = :isCalculated WHERE a.id = :id")
		})
@Indexed
public class Address implements Serializable 
{
	private static final long serialVersionUID = 2532284691145917204L;
	private static final int HASH_PRIME = 31;
	
	public static final String QUERY_NAME_ALL = "getAddressList";
	public static final String QUERY_NAME_BY_UNIQUE_COMBINATION = "getAddressByAllFields";
	public static final String QUERY_NAME_UPDATE_ALL_DUPLICATES_CALCULATED = "updateDuplicatesCalculatedAddress";
	public static final String QUERY_NAME_UPDATE_DUPLICATES_CALCULATED = "updateDuplicatesCalculatedForAddressId";
	
	public static final String PARAM_ADDITIONAL = "additional";
	public static final String PARAM_CITY = "city";
	public static final String PARAM_COUNTRY = "country";
	public static final String PARAM_HOUSE_NUMBER = "housenumber";
	public static final String PARAM_STREET = "street";
	public static final String PARAM_ZIP_CODE = "zipcode";
	public static final String PARAM_CALCULATED = "isCalculated";
	public static final String PARAM_ID = "id";
	
	@Transient
	private boolean newlyInserted;

	@Id
	@GeneratedValue
	@DocumentId
	@Column(name="AddressId")
	private Long id;
	
	@Field
	private String street;
	
	@Field
	private String houseNumber;
	
	@Field
	@Column(name="ZIPCode")
	private String zipCode;
	
	@Field
	private String city;
	
	@Field
	private String country;
	
	@Field
	@Column(nullable=true)
	private String state;
	
	@Column(nullable=false)
	private String additional = "";
	
	private boolean duplicatesCalculated;
	
	@ContainedIn
	@ManyToMany(mappedBy="addresses", cascade = {CascadeType.REFRESH})
	private List<Provider> providers;
	
	@ContainedIn
	@ManyToMany(mappedBy="addresses", cascade = {CascadeType.REFRESH})
	private List<Organisation> organisations;

	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public List<Provider> getProviders() {
		return providers;
	}

	public void setProviders(List<Provider> providers) {
		this.providers = providers;
	}

	public List<Organisation> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<Organisation> organisations) {
		this.organisations = organisations;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public void setNewlyInserted(boolean newlyInserted) {
		this.newlyInserted = newlyInserted;
	}

	public boolean isNewlyInserted() {
		return newlyInserted;
	}

	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this address.
	 * </p>
	 * <p>
	 * An object is equal to this Address, if and only if it is not 
	 * <code>null</code>, an instance of {@link Address} object and if the 
	 * values of street, house number, zip code, city, country and additional 
	 * are equal. Relations to other entities and the database id do not have 
	 * relevance for equality.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this address, else 
	 * 		<code>false</code>
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj)
	{	
		if (obj == this)
		{
			return false;
		}
		
		if (!(obj instanceof Address))
		{
			return false;
		}
		
		Address a = (Address) obj;
		
		// XOR
		if ((a.additional == null) != (this.additional == null))
		{
			return false;
		}
		
		if (a.additional != null && !a.additional.equals(this.additional))
		{
			return false;
		}
		
		// XOR
		if ((a.street == null) != (this.street == null))
		{
			return false;
		}
		
		if (a.street != null && !a.street.equals(this.street))
		{
			return false;
		}
		
		// XOR
		if ((a.houseNumber == null) != (this.houseNumber == null))
		{
			return false;
		}
		
		if (a.houseNumber != null && !a.houseNumber.equals(this.houseNumber))
		{
			return false;
		}
		
		// XOR
		if ((a.zipCode == null) != (this.zipCode == null))
		{
			return false;
		}
		
		if (a.zipCode != null && !a.zipCode.equals(this.zipCode))
		{
			return false;
		}
		
		// XOR
		if ((a.city == null) != (this.city == null))
		{
			return false;
		}
		
		if (a.city != null && !a.city.equals(this.city))
		{
			return false;
		}
		
		// XOR
		if ((a.country == null) != (this.country == null))
		{
			return false;
		}
		
		if (a.country != null && !a.country.equals(this.country))
		{
			return false;
		}
	
		return true;
	}
	
	/**
	 * <p>
	 * Alternative implementation to {@link #equals(Object)} which also takes
	 * the state field into consideration.
	 * </p>
	 * 
	 * @param that
	 * 		Address to compare to this
	 * @return
	 * 		True, if both addresses are equal in all data fields
	 * @see #equals(Object)
	 */
	public boolean equalsInAllDataFields(Address that)
	{
		if (!this.equals(that))
		{
			return false;
		}
		
		// XOR
		if ((this.state == null) != (that.state == null))
		{
			return false;
		}
		
		if (that.state != null && !that.state.equals(this.state))
		{
			return false;
		}
		
		return true;
	}

	/**
	 * <p>
	 * Generates a hash code value for this address.
	 * </p>
	 * 
	 * @return Hash code
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
	    int hashCode = 1;
	    
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.additional == null) ? 0 : this.additional.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.street == null) ? 0 : this.street.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.houseNumber == null) ? 0 : this.houseNumber.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.zipCode == null) ? 0 : this.zipCode.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.city == null) ? 0 : this.city.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.country == null) ? 0 : this.country.hashCode());
	    
		return hashCode;
	}
	
	/**
	 * <p>
	 * Returns a string representation of this object and its content having 
	 * the following format:
	 * </p>
	 * <p>
	 * [Address: id = ?; additional = ?; street = ?; houseNumber = ?; 
	 * zipCode = ?; city = ?; state = ?; country = ?]
	 * </p>
	 * 
	 * @return String representation of this object
	 * @see Object#toString(Object)
	 */
	@Override
	public String toString()
	{
		String nullStr = "null";
		StringBuilder builder = new StringBuilder();
		
		builder.append("[Address: id = ");
		
		if (this.id == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append(this.id.toString());
		}
		
		builder.append("; additional = ");
		if (this.additional == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.additional);
			builder.append("\"");
		}
		
		builder.append("; street = ");
		if (this.street == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.street);
			builder.append("\"");
		}
		
		builder.append("; houseNumber = ");
		if (this.houseNumber == null)
		{
			builder.append(nullStr);
		}
		else 
		{
			builder.append("\"");
			builder.append(this.houseNumber);
			builder.append("\"");
		}
		
		builder.append("; zipCode = ");
		if (this.zipCode == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.zipCode);
			builder.append("\"");
		}
		
		builder.append("; city = ");
		if (this.city == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.city);
			builder.append("\"");
		}
		
		builder.append("; state = ");
		if (this.state == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.state);
			builder.append("\"");
		}
		
		builder.append("; country = ");
		if (this.country == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.country);
			builder.append("\"");
		}
		
		builder.append("]");
		
		return builder.toString();
	}

	public boolean isDuplicatesCalculated() {
		return duplicatesCalculated;
	}

	public void setDuplicatesCalculated(boolean duplicatesCalculated) {
		this.duplicatesCalculated = duplicatesCalculated;
	}
}
