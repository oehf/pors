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
 * The DTO for AddressLogs
 * 
 * @author tb
 * 
 */
public class AddressLogDTO {

	private Long id;
	private Integer porsuserID;
	private Date logTime;
	private String sessionId;
	private String IPAddress;
	private String triggerType;
	private String tableName = "Address";
	private Long addressId;
	private String oldStreet;
	private String oldHouseNumber;
	private String oldZipCode;
	private String oldCity;
	private String oldCountry;
	private String oldState;
	private String oldAdditional;
	private String newStreet;
	private String newHouseNumber;
	private String newZipCode;
	private String newCity;
	private String newCountry;
	private String newState;
	private String newAdditional;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPorsuserID() {
		return porsuserID;
	}

	public void setPorsuserID(Integer porsuserID) {
		this.porsuserID = porsuserID;
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

	public String getOldState() {
		return oldState;
	}

	public void setOldState(String oldState) {
		this.oldState = oldState;
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

	public String getNewState() {
		return newState;
	}

	public void setNewState(String newState) {
		this.newState = newState;
	}

	public String getNewAdditional() {
		return newAdditional;
	}

	public void setNewAdditional(String newAdditional) {
		this.newAdditional = newAdditional;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getIPAddress() {
		return IPAddress;
	}
}
