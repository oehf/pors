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

import java.util.List;

/**
 * The DTO for addresses
 * 
 * @author mf
 * 
 */
public class AddressDTO {

	private Long id;
	private String street;
	private String houseNumber;
	private String zipCode;
	private String city;
	private String state;
	private String country;
	private String additional;
	private List<Long> providerIds;
	private List<Long> organisationIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public void setProviderIds(List<Long> providerIds) {
		this.providerIds = providerIds;
	}

	public List<Long> getProviderIds() {
		return providerIds;
	}

	public void setOrganisationIds(List<Long> organisationIds) {
		this.organisationIds = organisationIds;
	}

	public List<Long> getOrganisationIds() {
		return organisationIds;
	}

}
