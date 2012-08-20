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
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * <p>
 * Entity bean mapping entries in database table "AddressLog" and thus representing 
 * logging entries for actions done in table "Address".
 * </p>
 * <p>
 * The following fields are required for successful persisting:
 * <ul>
 * <li>User</li>
 * <li>Log Time</li>
 * <li>Session ID</li>
 * <li>Trigger Type</li>
 * <li>IP Address</li>
 * <li>Address ID</li>
 * </ul>
 * </p>
 * <p>
 * The entity supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all available address logs.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@NamedQueries( value = { 
		@NamedQuery( name = "getAddressLogList", query = "SELECT al FROM AddressLog al" ) 
		} )
public class AddressLog implements Serializable 
{
	private static final long serialVersionUID = -1015709069151921852L;
	
	public static final String QUERY_NAME_ALL = "getAddressLogList";

	@Id
	@GeneratedValue
	@Column(name="AddressLogId")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PorsUserId")
	private PorsUser user;
	
	private Date logTime;
	
	private String sessionId;
	
	private String IPAddress;
	
	private String triggerType;
	
	private String tableName = "Address";
	
	private Long addressId;
	
	@Column(nullable=true)
	private String oldStreet;
	
	@Column(nullable=true)
	private String oldHouseNumber;
	
	@Column(name="OldZIPCode", nullable=true)
	private String oldZipCode;
	
	@Column(nullable=true)
	private String oldCity;
	
	@Column(nullable=true)
	private String oldCountry;
	
	@Column(nullable=true)
	private String oldState;
	
	@Column(nullable=true)
	private String oldAdditional;
	
	@Column(nullable=true)
	private String newStreet;
	
	@Column(nullable=true)
	private String newHouseNumber;
	
	@Column(name="NewZIPCode", nullable=true)
	private String newZipCode;
	
	@Column(nullable=true)
	private String newCity;
	
	@Column(nullable=true)
	private String newCountry;
	
	@Column(nullable=true)
	private String newState;
	
	@Column(nullable=true)
	private String newAdditional;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getOldStreet() {
		return oldStreet;
	}

	public void setOldStreet(String oldStreet) {
		this.oldStreet = oldStreet;
	}

	public String getOldHouseNumber() {
		return oldHouseNumber;
	}

	public void setOldHouseNumber(String oldHouseNumber) {
		this.oldHouseNumber = oldHouseNumber;
	}

	public String getOldZipCode() {
		return oldZipCode;
	}

	public void setOldZipCode(String oldZipCode) {
		this.oldZipCode = oldZipCode;
	}

	public String getOldCity() {
		return oldCity;
	}

	public void setOldCity(String oldCity) {
		this.oldCity = oldCity;
	}

	public String getOldCountry() {
		return oldCountry;
	}

	public void setOldCountry(String oldCountry) {
		this.oldCountry = oldCountry;
	}

	public String getOldAdditional() {
		return oldAdditional;
	}

	public void setOldAdditional(String oldAdditional) {
		this.oldAdditional = oldAdditional;
	}

	public String getNewStreet() {
		return newStreet;
	}

	public void setNewStreet(String newStreet) {
		this.newStreet = newStreet;
	}

	public String getNewHouseNumber() {
		return newHouseNumber;
	}

	public void setNewHouseNumber(String newHouseNumber) {
		this.newHouseNumber = newHouseNumber;
	}

	public String getNewZipCode() {
		return newZipCode;
	}

	public void setNewZipCode(String newZipCode) {
		this.newZipCode = newZipCode;
	}

	public String getNewCity() {
		return newCity;
	}

	public void setNewCity(String newCity) {
		this.newCity = newCity;
	}

	public String getNewCountry() {
		return newCountry;
	}

	public void setNewCountry(String newCountry) {
		this.newCountry = newCountry;
	}

	public String getNewAdditional() {
		return newAdditional;
	}

	public void setNewAdditional(String newAdditional) {
		this.newAdditional = newAdditional;
	}

	public String getOldState() {
		return oldState;
	}

	public void setOldState(String oldState) {
		this.oldState = oldState;
	}

	public String getNewState() {
		return newState;
	}

	public void setNewState(String newState) {
		this.newState = newState;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}
}
