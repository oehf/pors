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

/**
 * The DTO for DuplicateConfiguration
 * 
 * @author ms
 * 
 */
public class DuplicateConfigurationDTO {

	private int providerNameprefixWeight;
	private int providerFirstnameWeight;
	private int providerMiddlenameWeight;
	private int providerLastnameWeight;
	private int providerNamesuffixWeight;
	private int providerAddressWeight;
	private int providerEmailWeight;
	private int providerTelephoneWeight;
	private int providerFaxWeight;
	private int providerGenderCodeWeight;
	private int providerOIDWeight;
	private int providerSpecialisationWeight;
	private int providerBirthdayWeight;

	private int addressCityWeight;
	private int addressCountryWeight;
	private int addressHousenumberWeight;
	private int addressStateWeight;
	private int addressStreetWeight;
	private int addressZipCodeWeight;

	private int organisationAddressWeight;
	private int organisationEmailWeight;
	private int organisationTelephoneWeight;
	private int organisationFaxWeight;
	private int organisationNameWeight;
	private int organisationSecondnameWeight;

	private int timerHour;
	private int timerMinute;
	private int timerSecond;

	private int lowerthreshold;
	private int upperthreshold;

	public int getLowerthreshold() {
		return lowerthreshold;
	}

	public void setLowerthreshold(int lowerthreshold) {
		this.lowerthreshold = lowerthreshold;
	}

	public int getUpperthreshold() {
		return upperthreshold;
	}

	public void setUpperthreshold(int upperthreshold) {
		this.upperthreshold = upperthreshold;
	}

	public int getProviderNameprefixWeight() {
		return providerNameprefixWeight;
	}

	public void setProviderNameprefixWeight(int providerNameprefixWeight) {
		this.providerNameprefixWeight = providerNameprefixWeight;
	}

	public int getProviderFirstnameWeight() {
		return providerFirstnameWeight;
	}

	public void setProviderFirstnameWeight(int providerFirstnameWeight) {
		this.providerFirstnameWeight = providerFirstnameWeight;
	}

	public int getProviderMiddlenameWeight() {
		return providerMiddlenameWeight;
	}

	public void setProviderMiddlenameWeight(int providerMiddlenameWeight) {
		this.providerMiddlenameWeight = providerMiddlenameWeight;
	}

	public int getProviderLastnameWeight() {
		return providerLastnameWeight;
	}

	public void setProviderLastnameWeight(int providerLastnameWeight) {
		this.providerLastnameWeight = providerLastnameWeight;
	}

	public int getProviderNamesuffixWeight() {
		return providerNamesuffixWeight;
	}

	public void setProviderNamesuffixWeight(int providerNamesuffixWeight) {
		this.providerNamesuffixWeight = providerNamesuffixWeight;
	}

	public int getProviderAddressWeight() {
		return providerAddressWeight;
	}

	public void setProviderAddressWeight(int providerAddressWeight) {
		this.providerAddressWeight = providerAddressWeight;
	}

	public int getProviderEmailWeight() {
		return providerEmailWeight;
	}

	public void setProviderEmailWeight(int providerEmailWeight) {
		this.providerEmailWeight = providerEmailWeight;
	}

	public int getProviderTelephoneWeight() {
		return providerTelephoneWeight;
	}

	public void setProviderTelephoneWeight(int providerTelephoneWeight) {
		this.providerTelephoneWeight = providerTelephoneWeight;
	}

	public int getProviderFaxWeight() {
		return providerFaxWeight;
	}

	public void setProviderFaxWeight(int providerFaxWeight) {
		this.providerFaxWeight = providerFaxWeight;
	}

	public int getProviderGenderCodeWeight() {
		return providerGenderCodeWeight;
	}

	public void setProviderGenderCodeWeight(int providerGenderCodeWeight) {
		this.providerGenderCodeWeight = providerGenderCodeWeight;
	}

	public int getProviderOIDWeight() {
		return providerOIDWeight;
	}

	public void setProviderOIDWeight(int providerOIDWeight) {
		this.providerOIDWeight = providerOIDWeight;
	}

	public int getProviderSpecialisationWeight() {
		return providerSpecialisationWeight;
	}

	public void setProviderSpecialisationWeight(int providerSpecialisationWeight) {
		this.providerSpecialisationWeight = providerSpecialisationWeight;
	}

	public int getProviderBirthdayWeight() {
		return providerBirthdayWeight;
	}

	public void setProviderBirthdayWeight(int providerBirthdayWeight) {
		this.providerBirthdayWeight = providerBirthdayWeight;
	}

	public int getAddressCityWeight() {
		return addressCityWeight;
	}

	public void setAddressCityWeight(int addressCityWeight) {
		this.addressCityWeight = addressCityWeight;
	}

	public int getAddressCountryWeight() {
		return addressCountryWeight;
	}

	public void setAddressCountryWeight(int addressCountryWeight) {
		this.addressCountryWeight = addressCountryWeight;
	}

	public int getAddressHousenumberWeight() {
		return addressHousenumberWeight;
	}

	public void setAddressHousenumberWeight(int addressHousenumberWeight) {
		this.addressHousenumberWeight = addressHousenumberWeight;
	}

	public int getAddressStateWeight() {
		return addressStateWeight;
	}

	public void setAddressStateWeight(int addressStateWeight) {
		this.addressStateWeight = addressStateWeight;
	}

	public int getAddressStreetWeight() {
		return addressStreetWeight;
	}

	public void setAddressStreetWeight(int addressStreetWeight) {
		this.addressStreetWeight = addressStreetWeight;
	}

	public int getAddressZipCodeWeight() {
		return addressZipCodeWeight;
	}

	public void setAddressZipCodeWeight(int addressZipCodeWeight) {
		this.addressZipCodeWeight = addressZipCodeWeight;
	}

	public int getOrganisationAddressWeight() {
		return organisationAddressWeight;
	}

	public void setOrganisationAddressWeight(int organisationAddressWeight) {
		this.organisationAddressWeight = organisationAddressWeight;
	}

	public int getOrganisationEmailWeight() {
		return organisationEmailWeight;
	}

	public void setOrganisationEmailWeight(int organisationEmailWeight) {
		this.organisationEmailWeight = organisationEmailWeight;
	}

	public int getOrganisationTelephoneWeight() {
		return organisationTelephoneWeight;
	}

	public void setOrganisationTelephoneWeight(int organisationTelephoneWeight) {
		this.organisationTelephoneWeight = organisationTelephoneWeight;
	}

	public int getOrganisationFaxWeight() {
		return organisationFaxWeight;
	}

	public void setOrganisationFaxWeight(int organisationFaxWeight) {
		this.organisationFaxWeight = organisationFaxWeight;
	}

	public int getOrganisationNameWeight() {
		return organisationNameWeight;
	}

	public void setOrganisationNameWeight(int organisationNameWeight) {
		this.organisationNameWeight = organisationNameWeight;
	}

	public int getOrganisationSecondnameWeight() {
		return organisationSecondnameWeight;
	}

	public void setOrganisationSecondnameWeight(int organisationSecondnameWeight) {
		this.organisationSecondnameWeight = organisationSecondnameWeight;
	}

	public int getTimerHour() {
		return timerHour;
	}

	public void setTimerHour(int timerHour) {
		this.timerHour = timerHour;
	}

	public int getTimerMinute() {
		return timerMinute;
	}

	public void setTimerMinute(int timerMinute) {
		this.timerMinute = timerMinute;
	}

	public int getTimerSecond() {
		return timerSecond;
	}

	public void setTimerSecond(int timerSecond) {
		this.timerSecond = timerSecond;
	}
}