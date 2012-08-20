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
package org.openehealth.pors.admin_frontend.guiengine.dialogpresentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Pattern;
import org.openehealth.pors.admin_frontend.guiengine.dialogcore.DialogCore;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.AddressBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.DuplicateDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.DuplicateDetails;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.HistoryDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.ImportBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.LocalIdBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.LogTableBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.OrganisationDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.ProviderDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.SearchBean;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import org.openehealth.pors.admin_client.DuplicateConfigurationDTO;
import org.openehealth.pors.admin_client.DuplicateEntryDTO;
import org.openehealth.pors.admin_client.ImportResultDTO;
import org.openehealth.pors.admin_client.LoggingEntryDTO;
import org.openehealth.pors.admin_client.OrganisationDTO;
import org.openehealth.pors.admin_client.PermissionDTO;
import org.openehealth.pors.admin_client.ProviderDTO;
import org.openehealth.pors.admin_client.UserDTO;
import org.openehealth.pors.admin_client.UserRoleDTO;

public class Presenter implements IPresenter{

	private DialogCore dialogCore;
	private boolean userLoggedIn;
	private boolean addressModified = false;
	private boolean localIdModified = false;
	
	private boolean editProviderDisabled = true;
	private boolean editOrganizationDisabled = true;
	
	private String localIdProviderSelected = "";
	private String localIdOrgSelected = "";
	
	private Logger logger;
	
	/**
	 * initialize {@link Presenter} 
	 */
	public Presenter() {
		userLoggedIn = false;
		dialogCore = new DialogCore(this);
		logger = Logger.getLogger(Presenter.class);
	}
	
	/**
	 * @return true if address is modified, false otherwise
	 */
	public boolean isAddressModified() {
		return addressModified;
	}

	/**
	 * sets addressModified to given value
	 * @param addressModified
	 */
	public void setAddressModified(boolean addressModified) {
		this.addressModified = addressModified;
	}

	/**
	 * @return true if localId is modified, false otherwise
	 */
	public boolean isLocalIdModified() {
		return localIdModified;
	}

	/**
	 * sets localIdModified to given value
	 * @param localIdModified
	 */
	public void setLocalIdModified(boolean localIdModified) {
		this.localIdModified = localIdModified;
	}

	/**
	 * @return permissions of current user
	 */
	public PermissionDTO getPermissions(){
		return dialogCore.getPermissions();
	}

	/**
	 * @return true if editing of organisation is disabled, false otherwise
	 */
	public boolean isEditOrganizationDisabled() {
		return editOrganizationDisabled;
	}

	/**
	 * sets editOrganizationDisabled to given value
	 * @param editOrganizationDisabled
	 */
	public void setEditOrganizationDisabled(boolean editOrganizationDisabled) {
		this.editOrganizationDisabled = editOrganizationDisabled;
	}

	/**
	 * @return true if editing of provider is disabled, false otherwise
	 */
	public boolean isEditProviderDisabled() {
		return editProviderDisabled;
	}

	/**
	 * sets editProviderDisabled to given value
	 * @param editProviderDisabled
	 */
	public void setEditProviderDisabled(boolean editProviderDisabled) {
		this.editProviderDisabled = editProviderDisabled;
	}

	/**
	 * @return list of possible country codes
	 */
	public SelectItem[] getCountries(){
		return dialogCore.getCountries();
	}
	
	/**
	 * @return dd.mm.yyyy of the current provider's birthday, null if unknown
	 */
	public String getBirthdayString(){
		return dialogCore.getBirthdayString();
	}

	/**
	 * @return list of the last 100 years
	 */
	public SelectItem[] getBirthYears(){
		return dialogCore.getYears();
	}
	
	/**
	 * replaces the list of possible birthday years with given list
	 * @param years
	 */
	public void setBirthYears(SelectItem[] years){
		dialogCore.setYears(years);
	}

	/**
	 * @return list of the 12 months
	 */
	public SelectItem[] getBirthMonths(){
		return dialogCore.getMonths();
	}
	
	/**
	 * replaces the list of possible birthday months with given list
	 * @param years
	 */
	public void setBirthMonths(SelectItem[] months){
		dialogCore.setMonths(months);
	}

	/**
	 * @return list of 31 days
	 */
	public SelectItem[] getBirthDays(){
		return dialogCore.getDays();
	}
	
	/**
	 * replaces the list of possible birthday days with given list
	 * @param years
	 */
	public void setBirthDays(SelectItem[] days){
		dialogCore.setDays(days);
	}
	
	/**
	 * @see IPresenter
	 */
	public String login() {
		return dialogCore.login();
	}

	/**
	 * @see IPresenter
	 */
	public String logout() {
		return dialogCore.logout();
	}
	
	/**
	 * gets the current user
	 * @return
	 */
	public UserDTO getCurrentUser(){
		return dialogCore.getCurrentUser();
	}
	
	/**
	 * gets userLoggedIn
	 * @return
	 */
	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	/**
	 * sets userLoggedIn
	 * @param userLoggedIn
	 */
	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	/**
	 * sets localIdProviderSelected
	 * @param id
	 */
	public void setLocalIdProviderSelected(String id) {
		this.localIdProviderSelected = id;
	}
	
	/**
	 * gets localIdProviderSelected
	 * @return
	 */
	public String getLocalIdProviderSelected() {
		return this.localIdProviderSelected;
	}

	/**
	 * sets localIdOrgSelected
	 * @param id
	 */
	public void setLocalIdOrgSelected(String id) {
		this.localIdOrgSelected = id;
	}
	
	/**
	 * gets localIdOrgSelected
	 * @return
	 */
	public String getLocalIdOrgSelected() {
		return this.localIdOrgSelected;
	}

	/**
	 * sets gendercode
	 * @param sex
	 */
	public void setSexProviderSelected(String sex) {
		setGendercode(sex);
	}
	
	/**
	 * gets gendercode
	 * @return
	 */
	public String getSexProviderSelected() {
		return getGendercode();
	}

	// address

	/**
	 * gets street of new address
	 * @return
	 */
	@NotEmpty(message="street must not be empty")
	public String getStreet() {
		return dialogCore.getNewAddress().getStreet();
	}

	/**
	 * sets street of new address
	 */
	public void setStreet(String street) {
		dialogCore.getNewAddress().setStreet(street.equals("")?null:street);
	}
	
	/**
	 * gets house number of new address
	 * @return
	 */
	@NotEmpty(message="number must not be empty")
	@Length(min = 1, max = 7, message="maximum size of house number is 7")
	public String getNumber() {
		return dialogCore.getNewAddress().getHouseNumber();
	}

	/**
	 * sets house number of new address
	 */
	public void setNumber(String number) {
		dialogCore.getNewAddress().setHouseNumber(number.equals("")?null:number);
	}
	
	/**
	 * gets zip code of new address
	 * @return
	 */
	@NotEmpty(message="zip code must not be empty")
	public String getZipCode() {
		return dialogCore.getNewAddress().getZipCode();
	}

	/**
	 * sets zip code of new address
	 */
	public void setZipCode(String zipCode) {
		dialogCore.getNewAddress().setZipCode(zipCode.equals("")?null:zipCode);
	}
	
	/**
	 * gets duplicate strategy
	 * @return
	 */
	public String getDuplicateStrategy(){
		return dialogCore.getDuplicateDetails().getDuplicateStrategy();
	}
	
	/**
	 * sets duplicate strategy 
	 */
	public void setDuplicateStrategy(String duplicateStrategy){
		dialogCore.getDuplicateDetails().setDuplicateStrategy(duplicateStrategy);
	}
	
	/**
	 * gets city of new address
	 * @return
	 */
	@NotEmpty(message="city must not be empty")
	public String getCity() {
		return dialogCore.getNewAddress().getCity();
	}

	/**
	 * sets city of new address
	 */
	public void setCity(String city) {
		dialogCore.getNewAddress().setCity(city.equals("")?null:city);
	}
	
	/**
	 * gets additional of new address
	 * @return
	 */
	public String getAdditional() {
		return dialogCore.getNewAddress().getAdditional();
	}

	/**
	 * sets additional of new address
	 */
	public void setAdditional(String additional) {
		dialogCore.getNewAddress().setAdditional(additional.equals("")?null:additional);
	}
	
	/**
	 * gets state of new address
	 * @return
	 */
	public String getState() {
		return dialogCore.getNewAddress().getState();
	}

	/**
	 * sets state of new address
	 */
	public void setState(String state) {
		dialogCore.getNewAddress().setState(state.equals("")?null:state);
	}
	
	/**
	 * gets country of new address
	 * @return
	 */
	public String getCountry() {
		return dialogCore.getNewAddress().getCountry();
	}

	/**
	 * sets country of new address
	 */
	public void setCountry(String country) {
		dialogCore.getNewAddress().setCountry(country);
	}

	// LocalId

	/**
	 * gets localId
	 * @return
	 */
	@NotEmpty(message="{validator.localid.notempty}")
	@Length(min = 1, max = 10, message="{validator.localid.length}")
	public String getLocalId() {
		return dialogCore.getNewLocalId().getLocalId();
	}

	/**
	 * sets localId
	 */
	public void setLocalId(String localId) {
		dialogCore.getNewLocalId().setLocalId(localId);
	}

	/**
	 * gets application of new localId
	 * @return
	 */
	@NotEmpty(message="{validator.localid.application}")
	public String getApplication() {
		return dialogCore.getNewLocalId().getApplication();
	}

	/**
	 * sets application of new localId
	 */
	public void setApplication(String application) {
		dialogCore.getNewLocalId().setApplication(application);
	}

	/**
	 * gets facility of new localId
	 * @return
	 */
	@NotEmpty(message="{validator.localid.facility}")
	public String getFacility() {
		return dialogCore.getNewLocalId().getFacility();
	}

	/**
	 * sets facility of new localId
	 */
	public void setFacility(String facility) {
		dialogCore.getNewLocalId().setFacility(facility);
	}
	
	
	
	/*
	 *   N e w   P r o v i d e r
	 */
	
	/**
	 * gets lanr of new provider
	 * @return
	 */
	@Pattern(regex="(.{9}|.{0})", message="{validator.lanr}")
	public String getLanr() {
		return dialogCore.getProvider().getLanr();
	}

	/**
	 * sets lanr of new provider
	 */
	public void setLanr(String lanr) {
		dialogCore.getProvider().setLanr(lanr.equals("")?null:lanr);
	}

	/**
	 * gets oid of new provider
	 * @return
	 */
	public String getOid() {
		return dialogCore.getProvider().getOid();
	}

	/**
	 * sets oid of new provider
	 */
	public void setOid(String oid) {
		dialogCore.getProvider().setOid(oid.equals("")?null:oid);
	}

	/**
	 * gets firstname of new provider
	 * @return
	 */
	@Pattern(regex="^\\S+.*$", message="{validator.firstname}")
	@NotEmpty(message="{validator.firstname}")
	public String getFirstname() {
		return dialogCore.getProvider().getFirstname();
	}

	/**
	 * sets firstname of new provider
	 */
	public void setFirstname(String firstname) {
		dialogCore.getProvider().setFirstname(firstname);
	}

	/**
	 * gets lastname of new provider
	 * @return
	 */
	@Pattern(regex="^\\S+.*$", message="{validator.lastname}")
	@NotEmpty(message="{validator.lastname}")
	public String getLastname() {
		return dialogCore.getProvider().getLastname();
	}

	/**
	 * sets firstname of new provider
	 */
	public void setLastname(String lastname) {
		dialogCore.getProvider().setLastname(lastname);
	}

	/**
	 * gets middlename of new provider
	 * @return
	 */
	public String getMiddlename() {
		return dialogCore.getProvider().getMiddleName();
	}

	/**
	 * sets middlename of new provider
	 */
	public void setMiddlename(String middlename) {
		dialogCore.getProvider().setMiddleName(middlename);
	}

	/**
	 * gets nameprefix of new provider
	 * @return
	 */
	public String getNameprefix() {
		return dialogCore.getProvider().getNamePrefix();
	}

	/**
	 * sets nameprefix of new provider
	 */
	public void setNameprefix(String nameprefix) {
		dialogCore.getProvider().setNamePrefix(nameprefix);
	}

	/**
	 * gets namesuffix of new provider
	 * @return
	 */
	public String getNamesuffix() {
		return dialogCore.getProvider().getNameSuffix();
	}

	/**
	 * sets namesuffix of new provider
	 */
	public void setNamesuffix(String namesuffix) {
		dialogCore.getProvider().setNameSuffix(namesuffix);
	}
	
	/**
	 * gets specialisation of new provider
	 * @return
	 */
	public String getSpecialisation() {
		return dialogCore.getProvider().getSpecialisation();
	}
	
	/**
	 * sets specialisation of new provider
	 */
	public void setSpecialisation(String specialisation) {
		dialogCore.getProvider().setSpecialisation(specialisation);
	}

	/**
	 * gets gendercode of new provider
	 * @return
	 */
	public String getGendercode() {
		String genderCode = dialogCore.getProvider().getGenderCode();
		return (genderCode == null || genderCode.equals("")) ? "u" : genderCode.toLowerCase();
	}

	/**
	 * sets specialisation of new provider
	 */
	public void setGendercode(String gendercode) {
		dialogCore.getProvider().setGenderCode(gendercode);
	}

	/**
	 * gets day of date of birth of new provider
	 * @return
	 */
	public String getProviderBirthdayDateSelected(){
		if(dialogCore.getProvider().getBirthday() != null){
			return dialogCore.getProvider().getBirthday().getDay()+"";
		} else{
			return "1";
		}
	}
	
	/**
	 * sets day of date of birth of new provider
	 */
	public void setProviderBirthdayDateSelected(String date){
		if(dialogCore.getProvider().getBirthday() == null){
			try {
				dialogCore.getProvider().setBirthday(DatatypeFactory.newInstance()
				        .newXMLGregorianCalendar(new GregorianCalendar()));
			} catch (DatatypeConfigurationException ignored) {}
		}
		dialogCore.getProvider().getBirthday().setDay(Integer.parseInt(date));
	}

	/**
	 * gets month of date of birth of new provider
	 * @return
	 */
	public String getProviderBirthdayMonthSelected(){
		if(dialogCore.getProvider().getBirthday() != null){
			return (dialogCore.getProvider().getBirthday().getMonth())+"";
		} else{
			return "1";
		}
	}
	
	/**
	 * sets month of date of birth of new provider
	 */
	public void setProviderBirthdayMonthSelected(String month){
		if(dialogCore.getProvider().getBirthday() == null){
			try {
				dialogCore.getProvider().setBirthday(DatatypeFactory.newInstance()
				        .newXMLGregorianCalendar(new GregorianCalendar()));
			} catch (DatatypeConfigurationException ignored) {}
		}
		dialogCore.getProvider().getBirthday().setMonth(Integer.parseInt(month));
	}

	/**
	 * gets year of date of birth of new provider
	 * @return
	 */
	public String getProviderBirthdayYearSelected(){
		if(dialogCore.getProvider().getBirthday() != null){
			return dialogCore.getProvider().getBirthday().getYear()+"";
		} else{
			return dialogCore.getStartYear()+"";
		}
	}
	
	/**
	 * gets year of date of birth of new provider
	 * @return
	 */
	public void setProviderBirthdayYearSelected(String year){
		if(dialogCore.getProvider().getBirthday() == null){
			try {
				dialogCore.getProvider().setBirthday(DatatypeFactory.newInstance()
				        .newXMLGregorianCalendar(new GregorianCalendar()));
			} catch (DatatypeConfigurationException ignored) {}
		}
		dialogCore.getProvider().getBirthday().setYear(Integer.parseInt(year));
	}

	/**
	 * gets email of new provider
	 * @return
	 */
	@Email
	public String getEmail() {
		return dialogCore.getProvider().getEmail();
	}

	/**
	 * sets email of new provider
	 */
	public void setEmail(String email) {
		dialogCore.getProvider().setEmail(email);
	}
	
	/**
	 * gets deactivate flag of new provider
	 * @return
	 */
	public boolean isDeactive(){
		return dialogCore.getDeactive();
	}
	
	/**
	 * sets deactivate flag of new provider
	 */
	public void setDeactive(boolean deactive){
		try {
			dialogCore.setDeactive(deactive);
		} catch (DatatypeConfigurationException ignore) {}
	}
	
	/**
	 * gets deactivate flag of new organisation
	 * @return
	 */
	public boolean isOrgdeactive(){
		return dialogCore.getOrgdeactive();
	}
	
	/**
	 * sets deactivate flag of new organisation
	 */
	public void setOrgdeactive(boolean orgdeactive){
		try {
			dialogCore.setOrgdeactive(orgdeactive);
		} catch (DatatypeConfigurationException ignore) {}
	}
	
	/**
	 * gets deactivate reason of new provider
	 * @return
	 */
	@NotEmpty(message="{validator.deactivation.reason}")
	public String getDeactivationreason(){
		return dialogCore.getProvider().getDeactivationReasonCode();
	}
	
	/**
	 * sets deactivate reason of new provider
	 */
	public void setDeactivationreason(String deactivationreason){
		dialogCore.getProvider().setDeactivationReasonCode(deactivationreason);
	}
	
	/**
	 * gets deactivate reason of new organisationn
	 * @return
	 */
	@NotEmpty(message="{validator.deactivation.reason}")
	public String getOrgdeactivationreason(){
		return dialogCore.getOrg().getDeactivationReasonCode();
	}
	
	/**
	 * sets deactivate reason of new organisationn
	 */
	public void setOrgdeactivationreason(String deactivationreason){
		dialogCore.getOrg().setDeactivationReasonCode(deactivationreason);
	}

	/**
	 * gets telephone of new provider
	 * @return
	 */
	public String getTelephone() {
		return dialogCore.getProvider().getTelephone();
	}

	/**
	 * sets telephone of new provider
	 */
	public void setTelephone(String telephone) {
		dialogCore.getProvider().setTelephone(telephone);
	}

	/**
	 * gets fax of new provider
	 * @return
	 */
	public String getFax() {
		return dialogCore.getProvider().getFax();
	}

	/**
	 * sets fax of new provider
	 */
	public void setFax(String fax) {
		dialogCore.getProvider().setFax(fax);
	}
	
	/*
	 *   N e w   U s e r
	 */
	
	
	/**
	 * gets new user
	 * @return
	 */
	public UserDTO getNewUser() {
		return dialogCore.getNewUser();
	}
	
	/**
	 * sets new user
	 */
	public void setNewUser(UserDTO  user) {
		dialogCore.setNewUser(user);
		dialogCore.setSelectedRole(user.getRole().getName());
	}

	/**
	 * gets name of new user
	 * @return
	 */
	@Pattern(regex="^\\S+.*$", message="{validator.username}")
	@NotEmpty(message="{validator.username}")
	public String getNewUserName() {
		return dialogCore.getNewUser().getUsername();
	}
	
	/**
	 * sets name of new user
	 */
	public void setNewUserName(String name) {
		dialogCore.getNewUser().setUsername(name);
	}
	
	/**
	 * gets password of new user
	 * @return
	 */
	@Pattern(regex="^\\S+.*$", message="{validator.password}")
	@NotEmpty(message="{validator.password}")
	public String getNewPassword() {
		return dialogCore.getNewUser().getPassword();
	}
	
	/**
	 * sets password of new user
	 */
	public void setNewPassword(String password) {
		dialogCore.getNewUser().setPassword(password);
	}
	
	/*
	 *   N e w   O r g a n i z a t i o n
	 */

	/**
	 * gets id of new organisation
	 * @return
	 */
	public String getOrgOid(){
		return dialogCore.getOrg().getOID();
	}
	
	/**
	 * sets id of new organisation
	 */
	public void setOrgOid(String oid){
		dialogCore.getOrg().setOID(oid);
	}
	
	/**
	 * gets name of new organisation
	 * @return
	 */
	@Pattern(regex="^\\S+.*$", message="{validator.org.name}")
	@NotEmpty(message="{validator.org.name}")
	public String getOrgName(){
		return dialogCore.getOrg().getName();
	}
	
	/**
	 * sets name of new organisation
	 */
	public void setOrgName(String name){
		dialogCore.getOrg().setName(name);
	}
	
	/**
	 * gets name2 of new organisation
	 * @return
	 */
	public String getOrgName2(){
		return dialogCore.getOrg().getSecondName();
	}
	
	/**
	 * sets name2 of new organisation
	 */
	public void setOrgName2(String name){
		dialogCore.getOrg().setSecondName(name);
	}
	
	/**
	 * gets description of new organisation
	 * @return
	 */
	public String getOrgDesc(){
		return dialogCore.getOrg().getDescription();
	}
	
	/**
	 * gets description of new organisation
	 * @return
	 */
	public void setOrgDesc(String desc){
		dialogCore.getOrg().setDescription(desc);
	}
	
	/**
	 * gets email of new organisation
	 * @return
	 */
	@Email
	public String getOrgEmail(){
		return dialogCore.getOrg().getEmail();
	}
	
	/**
	 * sets email of new organisation
	 */
	public void setOrgEmail(String email){
		dialogCore.getOrg().setEmail(email);
	}
	
	/**
	 * gets telephone of new organisation
	 * @return
	 */
	public String getOrgTelephone(){
		return dialogCore.getOrg().getTelephone();
	}
	
	/**
	 * sets telephone of new organisation
	 */
	public void setOrgTelephone(String telephone){
		dialogCore.getOrg().setTelephone(telephone);
	}
	
	/**
	 * gets fax of new organisation
	 * @return
	 */
	public String getOrgFax(){
		return dialogCore.getOrg().getFax();
	}
	
	/**
	 * sets fax of new organisation
	 */
	public void setOrgFax(String fax){
		dialogCore.getOrg().setFax(fax);
	}
	
	/**
	 * gets new password of user
	 * @return
	 */
	public String getNewUserPassword() {
		return dialogCore.getNewPassword();
	}

	/**
	 * sets new password of user
	 */
	public void setNewUserPassword(String pw) {
		dialogCore.setNewPassword(pw);
	}
	
	/**
	 * gets selected localId of new provider
	 * @return
	 */
	public String getSelectedLocalIdProvider() {
		return dialogCore.getSelectedLocalIdProvider();
	}

	/**
	 * sets selected localId of new provider
	 */
	public void setSelectedLocalIdProvider(String selectedLocalIdProvider) {
		dialogCore.setSelectedLocalIdProvider(selectedLocalIdProvider);
	}
	
	/**
	 * gets selected address of new provider
	 * @return
	 */
	public String getSelectedAddress() {
		return dialogCore.getSelectedAddressString();
	}

	/**
	 * sets selected address of new provider
	 */
	public void setSelectedAddress(String selectedAddress) {
		dialogCore.setSelectedAddressString(selectedAddress);
	}
	
	/**
	 * gets selected localId
	 * @return
	 */
	public String getSelectedLocalId() {
		return dialogCore.getSelectedLocalId();
	}

	/**
	 * sets selected localId
	 */
	public void setSelectedLocalId(String selectedLocalId) {
		dialogCore.setSelectedLocalId(selectedLocalId);
	}
	
	/**
	 * gets list of addresses
	 * @return
	 */
	public List<SelectItem> getSiAddresses(){
		return dialogCore.getSiAddresses();
	}
	
	/**
	 * gets list of organisations
	 * @return
	 */
	public List<SelectItem> getSiOrgs(){
		return dialogCore.getSiOrgs();
	}
	
	/**
	 * gets list of roles
	 * @return
	 */
	public List<SelectItem> getRoles(){
		return dialogCore.getAvailableRoles();
	}
	
	/**
	 * gets selected role
	 * @return
	 */
	public String getSelectedRole() {
		return dialogCore.getSelectedRole();
	}
	
	/**
	 * sets selected role
	 */
	public void setSelectedRole(String role) {
		dialogCore.setSelectedRole(role);
	}
	
	/**
	 * gets list of localIds
	 * @return
	 */
	public List<SelectItem> getSiLocalIds(){
		return dialogCore.getSiLocalIds();
	}

	/**
	 * gets list of organisations
	 * @return
	 */
	public List<String> getResult() {
		return dialogCore.getResult();
	}

	/**
	 * sets list of organisations
	 */
	public void setResult(List<String> result) {
		List<String> list = new ArrayList<String>();
		for (String s : result) {
			list.add(s);
		}
		dialogCore.setResult(list);
	}

	/**
	 * @see IPresenter
	 */
	public void resetAddress() {
		dialogCore.setNewAddress(new AddressBean());
		this.setAddressModified(false);
	}

	/**
	 * @see IPresenter
	 */
	public void addAddress() {
		dialogCore.addAddress();
	}

	/**
	 * @see IPresenter
	 */
	public void resetLocalId() {
		dialogCore.setNewLocalId(new LocalIdBean());
		this.setLocalIdModified(false);
	}

	/**
	 * @see IPresenter
	 */
	public void addLocalId() {
		dialogCore.addLocalId();
	}

	/**
	 * @see IPresenter
	 */
	public void addOrganization() {
		dialogCore.addOrganization();
	}

	/**
	 * @see IPresenter
	 */
	public ArrayList<OrganisationDTO> getOrganisationList() {
		return dialogCore.getOrganizationList();
	}

	/**
	 * gets list of selected organisations
	 * @return
	 */
	public ArrayList<OrganisationDTO> getOrganizationListSelected() {
		return dialogCore.getOrganizationListSelected();
	}

	/**
	 * sets list of selected organisations
	 */
	public void setOrganizationListSelected(ArrayList<OrganisationDTO> organizationListSelected) {
		dialogCore.setOrganizationListSelected(organizationListSelected);
	}

	/**
	 * @see IPresenter
	 */
	public void dummy() { }

	/**
	 * @see IPresenter
	 */
	public void prepareEditAddress(AddressBean add){
		dialogCore.setNewAddressTemp(add);
		dialogCore.setNewAddress(new AddressBean(add));
		this.setAddressModified(false);
	}
	
	/**
	 * @see IPresenter
	 */
	public void editAddress(){
		AddressBean add = dialogCore.getNewAddressTemp();
		AddressBean newAddress = dialogCore.getNewAddress();
		
		add.setStreet(newAddress.getStreet());
		add.setHouseNumber(newAddress.getHouseNumber());
		add.setZipCode(newAddress.getZipCode());
		add.setCity(newAddress.getCity());
		add.setAdditional(newAddress.getAdditional());
		add.setState(newAddress.getState());
		add.setCountry(newAddress.getCountry());
		
		resetAddress();
		this.setAddressModified(true);
	}
	
	/**
	 * @see IPresenter
	 */
	public void editUser(){
		UserDTO x = dialogCore.getNewUser();
		logger.info("name=" + x.getUsername() + ", active=" + x.isActive() + ", role=" + x.getRole().getName());
		dialogCore.updateUser(dialogCore.getNewUser());
	}

	/**
	 * @see IPresenter
	 */
	public void removeAddress() {
		dialogCore.removeAddress();
	}

	/**
	 * @see IPresenter
	 */
	public void removeAddress(AddressBean add) {
		dialogCore.removeAddress(add);
	}

	/**
	 * @see IPresenter
	 */
	public void prepareEditLocalId(LocalIdBean id){
		dialogCore.setNewLocalIdTemp(id);
		dialogCore.setNewLocalId(new LocalIdBean(id));
		this.setLocalIdModified(false);
	}
	
	/**
	 * @see IPresenter
	 */
	public void editLocalId(){
		LocalIdBean localId = dialogCore.getNewLocalIdTemp();
		LocalIdBean newLocalId = dialogCore.getNewLocalId();
		
		localId.setApplication(newLocalId.getApplication());
		localId.setFacility(newLocalId.getFacility());
		localId.setLocalId(newLocalId.getLocalId());
		localId.setOrganisationId(newLocalId.getOrganisationId());
		localId.setProviderId(newLocalId.getProviderId());
		
		resetLocalId();
		this.setLocalIdModified(true);
	}

	/**
	 * @see IPresenter
	 */
	public void removeLocalId() {
		dialogCore.removeLocalId();
	}

	/**
	 * @see IPresenter
	 */
	public void removeLocalId(LocalIdBean id) {
		dialogCore.removeLocalId(id);
	}
	
	/**
	 * @see IPresenter
	 */
	public void enableEditProvider(){
		this.setEditProviderDisabled(false);
	}
	
	/**
	 * @see IPresenter
	 */
	public void enableEditOrg(){
		this.setEditOrganizationDisabled(false);
	}
	
	/**
	 * @see IPresenter
	 */
	public String addProvider() {
		return dialogCore.addProvider();
	}
	
	/**
	 * @see IPresenter
	 */
	public void saveProvider(){
		dialogCore.updateProvider();
	}
	
	/**
	 * @see IPresenter
	 */
	public void saveOrg(){
		dialogCore.updateOrg();
	}

	/**
	 * @see IPresenter
	 */
	public String showSingleProvider(ProviderDTO provider) {
		dialogCore.showSingleProvider(provider);

		return "success";
	}
	
	public void solve(){
		dialogCore.solveDuplicate();
	}   
	
	/**
	 * @see IPresenter
	 */
	public void addUser() {
		dialogCore.addUser();
	}	
	
	/**
	 * @see IPresenter
	 */
	public String showSingleHistory(LoggingEntryDTO history) {
		dialogCore.setHistory(history);
	    return dialogCore.getLoggingDetail();
	}
	
	
	/**
	 * @see IPresenter
	 */
	public String showSingleOrg(OrganisationDTO org) {
		dialogCore.setOrg(org);
		this.setEditOrganizationDisabled(true);
		
		return "success";
	}
	
	/**
	 * @see IPresenter
	 */
	public String showSingleDuplicate(DuplicateEntryDTO deDTO) {
		dialogCore.loadDuplicateDetails(deDTO);
		
		return "success";
	}

	/**
	 * @see IPresenter
	 */
	public void resetProvider() {
		dialogCore.setProvider(new ProviderDTO());
		dialogCore.showAllOrganizations();
		dialogCore.getResult().clear();
	}

	/**
	 * @see IPresenter
	 */
	public void resetOrg() {
		dialogCore.setOrg(new OrganisationDTO());
	}

	/*
	 * HistoryDetails
	 */
	
	/**
	 * gets logging id of history
	 * @return
	 */
	public String getHistoryLoggingID(){
		return dialogCore.getHistory().getLogEntryId().toString();
	}
	
	/**
	 * gets history
	 * @return
	 */
	public LoggingEntryDTO getHistory(){
		return dialogCore.getHistory();
	}
	
	/**
	 * checks if history is provider
	 * @return
	 */
	public boolean isProviderAvailable(){
		return dialogCore.getHistoryDetail().getProviderLog() != null;
	}
	
	/**
	 * checks if history is organisation
	 * @return
	 */
	public boolean isOrganisationAvailable(){
		return dialogCore.getHistoryDetail().getOrganisationLog() != null;
	}
	
	/**
	 * checks if history is localId
	 * @return
	 */
	public boolean isLocalIdAvailable(){
		return dialogCore.getHistoryDetail().getLocalidLog() != null;
	}
	
	/**
	 * checks if history is address
	 * @return
	 */
	public boolean isAddressAvailable(){
		return dialogCore.getHistoryDetail().getAddressLog() != null;
	}
	
	/**
	 * checks if history is provider - address relation
	 * @return
	 */
	public boolean isProviderHasAddressAvailable(){
		return dialogCore.getHistoryDetail().getProviderHasAddressLog() != null;
	}
	
	/**
	 * checks if history is organisation - address relation
	 * @return
	 */
	public boolean isOrganisationHasAddressAvailable(){
		return dialogCore.getHistoryDetail().getOrganisationHasAddressLog() != null;
	}
	
	/**
	 * checks if history is organisation - provider relation
	 * @return
	 */
	public boolean isOrganisationHasProviderAvailable(){
		return dialogCore.getHistoryDetail().getOrganisationHasProviderLog() != null;
	}
	
	/**
	 * gets ip of history
	 * @return
	 */
	public String getHistoryIpAddress() {
		String result = "No data available";
		if (dialogCore.getHistoryDetail().getProviderLog() != null) {
			result = dialogCore.getHistoryDetail().getProviderLog()
					.getIPAddress();
		} else if (dialogCore.getHistoryDetail().getOrganisationLog() != null) {
			result = dialogCore.getHistoryDetail().getOrganisationLog()
					.getIPAddress();
		} else if (dialogCore.getHistoryDetail().getAddressLog() != null) {
			result = dialogCore.getHistoryDetail().getAddressLog()
					.getIPAddress();
		} else if (dialogCore.getHistoryDetail().getLocalidLog() != null) {
			result = dialogCore.getHistoryDetail().getLocalidLog()
					.getIPAddress();
		} else if (dialogCore.getHistoryDetail().getProviderHasAddressLog() != null) {
			result = dialogCore.getHistoryDetail().getProviderHasAddressLog()
					.getIPAddress();
		} else if (dialogCore.getHistoryDetail().getOrganisationHasAddressLog() != null) {
			result = dialogCore.getHistoryDetail()
					.getOrganisationHasAddressLog().getIPAddress();
		} else if (dialogCore.getHistoryDetail().getOrganisationHasProviderLog() != null) {
			result = dialogCore.getHistoryDetail().getOrganisationHasProviderLog()
			.getIPAddress();
		}

		return result;
	}
	
	/**
	 * gets session id of history
	 * @return
	 */
	public String getHistorySessionID() {
		String result = "No data available";
		if (dialogCore.getHistoryDetail().getProviderLog() != null) {
			result = dialogCore.getHistoryDetail().getProviderLog()
					.getSessionId();
		} else if (dialogCore.getHistoryDetail().getOrganisationLog() != null) {
			result = dialogCore.getHistoryDetail().getOrganisationLog()
					.getSessionId();
		} else if (dialogCore.getHistoryDetail().getAddressLog() != null) {
			result = dialogCore.getHistoryDetail().getAddressLog()
					.getSessionId();
		} else if (dialogCore.getHistoryDetail().getLocalidLog() != null) {
			result = dialogCore.getHistoryDetail().getLocalidLog()
					.getSessionId();
		} else if (dialogCore.getHistoryDetail().getProviderHasAddressLog() != null) {
			result = dialogCore.getHistoryDetail().getProviderHasAddressLog()
			.getSessionId();
		} else if (dialogCore.getHistoryDetail().getOrganisationHasAddressLog() != null) {
			result = dialogCore.getHistoryDetail().getOrganisationHasAddressLog()
			.getSessionId();
		} else if (dialogCore.getHistoryDetail().getOrganisationHasProviderLog() != null) {
			result = dialogCore.getHistoryDetail().getOrganisationHasProviderLog()
			.getSessionId();
		}

		return result;
	}
	
	
	
	
	/**
	 * gets list of related organisations of provider
	 * @return
	 */
	public List<OrganisationDTO> getProviderOrganisations() {
		return dialogCore.getProviderOrganisations();
	}

	/**
	 * sets list of related organisations of provider
	 */
	public void setProviderOrganisations(List<OrganisationDTO> providerOrganisations) {
		dialogCore.setProviderOrganisations(providerOrganisations);
	}
	
	/**
	 * gets list of related history of provider
	 * @return
	 */
	public ArrayList<LogTableBean> getProviderHistoryData(){
		return dialogCore.getProviderHistoryData();
	}
	
	/**
	 * gets list of related history of organisation
	 * @return
	 */
	public ArrayList<LogTableBean> getOrganisationHistoryData(){
		return dialogCore.getOrganisationHistoryData();
	}
	
	/**
	 * gets list of related history of address
	 * @return
	 */
	public ArrayList<LogTableBean> getAddressHistoryData(){
		return dialogCore.getAddressHistoryData();
	}
	
	/**
	 * gets list of related history of localId
	 * @return
	 */
	public ArrayList<LogTableBean> getLocalIdHistoryData(){
		return dialogCore.getLocalIdHistoryData();
	}
	
	/**
	 * gets list of related history of providerHasAddress
	 * @return
	 */
	public ArrayList<LogTableBean> getProviderHasAddressHistoryData(){
		return dialogCore.getProviderHasAddressHistoryData();
	}
	
	/**
	 * gets list of related history of organisationHasAddress
	 * @return
	 */
	public ArrayList<LogTableBean> getOrganisationHasAddressHistoryData(){
		return dialogCore.getOrganisationHasAddressHistoryData();
	}	
	
	/**
	 * gets list of related history of organisationHasProvider
	 * @return
	 */
	public ArrayList<LogTableBean> getOrganisationHasProviderHistoryData(){
		return dialogCore.getOrganisationHasProviderHistoryData();
	}

	/**
	 * @see IPresenter
	 */
	public void loadShowProvider() {
		dialogCore.getSearchBean().reset();
		dialogCore.setProviderDataModel(null);
	}

	/**
	 * @see IPresenter
	 */
	public void loadImportProvider() {
		dialogCore.initProviderImport();
	}

	/**
	 * @see IPresenter
	 */
	public void loadShowOrganisation() {
		dialogCore.getSearchBean().reset();
		dialogCore.setOrganisationDataModel(null);
	}

	/**
	 * @see IPresenter
	 */
	public void loadImportOrganisation() {
		dialogCore.initOrganisationImport();
	}

	/**
	 * @see IPresenter
	 */
	public void loadShowHistory() {
		dialogCore.getSearchBean().reset();
		dialogCore.setHistoryDataModel(null);
	}

	/**
	 * @see IPresenter
	 */
	public void loadAddUser() {
		dialogCore.setAvailableRoles(null);
		dialogCore.setNewUser(new UserDTO());
		dialogCore.setSelectedRole(new UserRoleDTO().getName());
	}

	/**
	 * @see IPresenter
	 */
	public void loadEditUser() {
		dialogCore.setNewPassword("");
		dialogCore.setAvailableUsers(null);
	}

	/**
	 * @see IPresenter
	 */
	public List<LocalIdBean> getLocalIDs() {
		return dialogCore.getLocalIds();
	}

	/**
	 * @see IPresenter
	 */
	public List<AddressBean> getAddresses() {
		return dialogCore.getAddresses();
	}

	/**
	 * @see IPresenter
	 */
	public String getRedirectionToMain() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("start.xhtml");
		} catch (IOException e) {
			logger.warn("Redirection failed", e);
		}
		return null;
	}

	/**
	 * @see IPresenter
	 */
	public String getRedirectionToLogin() {
		try {
			if (!FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/showLogin.xhtml") && 
					!FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/timeout.xhtml")) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("showLogin.xhtml");
			}
		} catch (IOException e) {
			logger.warn("Redirection failed", e);
		}
		return null;
	}

	/**
	 * @see IPresenter
	 */
	public void exportProviders() {
		dialogCore.exportProviders();		
	}
	
	/**
	 * @see IPresenter
	 */
	public void exportOrganisations() {
		dialogCore.exportOrganisations();		
	}
	
	/**
	 * gets list of provider fields
	 * @return
	 */
	public List<String> getProviderFields() {
		return dialogCore.getAvailableProviderFields();
	}
	
	/**
	 * gets list of selected provider fields
	 * @return
	 */
	public List<String> getSelectedProviderFields() {
		return dialogCore.getSelectedProviderFields();
	}
	
	/**
	 * sets list of provider fields
	 */
	public void setProviderFields(List<String> fields) {
		dialogCore.getAvailableProviderFields().clear();
		dialogCore.getAvailableProviderFields().addAll(fields);
	}
	
	/**
	 * sets list of selected provider fields
	 */
	public void setSelectedProviderFields(List<String> fields) {
		dialogCore.getSelectedProviderFields().clear();
		dialogCore.getSelectedProviderFields().addAll(fields);
	}
	
	/**
	 * gets list of organisation fields
	 * @return
	 */
	public List<String> getOrganisationFields() {
		return dialogCore.getAvailableOrganisationFields();
	}
	
	/**
	 * gets list of selected organisation fields
	 * @return
	 */
	public List<String> getSelectedOrganisationFields() {
		return dialogCore.getSelectedOrganisationFields();
	}
	
	/**
	 * sets list of organisation fields
	 */
	public void setOrganisationFields(List<String> fields) {
		dialogCore.getAvailableOrganisationFields().clear();
		dialogCore.getAvailableOrganisationFields().addAll(fields);
	}
	
	/**
	 * sets list of selected organisation fields
	 */
	public void setSelectedOrganisationFields(List<String> fields) {
		dialogCore.getSelectedOrganisationFields().clear();
		dialogCore.getSelectedOrganisationFields().addAll(fields);
	}
	
	/**
	 * gets import provider bean
	 * @return
	 */
	public ImportBean getImportProviderBean() {
		return dialogCore.getImportProviderBean();
	}
	
	/**
	 * checks import provider file
	 * @return
	 */
	public String checkImportProviderFile() {
		return dialogCore.checkImportProviderFile();
	}
	
	/**
	 * checks import organisation file
	 * @return
	 */
	public String checkImportOrganisationFile() {
		return dialogCore.checkImportOrganisationFile();
	}
	
	/**
	 * gets import provider
	 * @return
	 */
	public String importProviders(){
		return dialogCore.importProviders();
	}
	
	/**
	 * gets import organisations
	 * @return
	 */
	public String importOrganisations(){
		return dialogCore.importOrganisations();
	}
	
	/**
	 * implementation of event listener for update functionality
	 * @return
	 */
	public void uploadListener(UploadEvent event) throws Exception {
        UploadItem item = event.getUploadItem();
        if (event.getComponent().getId().equals("uploadProvider")) {
        	dialogCore.getImportProviderBean().setUploadedFile(item.getFile());
        	dialogCore.processHeader(dialogCore.getImportProviderBean().getUploadedFile(), "PROVIDER");
        } else if (event.getComponent().getId().equals("uploadOrganisations")) {
        	dialogCore.getImportOrganisationBean().setUploadedFile(item.getFile());
        	dialogCore.processHeader(dialogCore.getImportOrganisationBean().getUploadedFile(), "ORGANISATION");
        }
    } 
	
	/**
	 * gets import organisation bean
	 * @return
	 */
	public ImportBean getImportOrganisationBean() {
		return dialogCore.getImportOrganisationBean();
	}
	
	/**
	 * gets list of search fields of provider
	 * @return
	 */
	public List<String> getProviderSearchFields() {
		return dialogCore.getProviderSearchFields();
	}
	
	/**
	 * gets list of search fields of organisation
	 * @return
	 */
	public List<String> getOrganisationSearchFields() {
		return dialogCore.getOrganisationSearchFields();
	}
	
	/**
	 * gets search bean
	 * @return
	 */
	public SearchBean getSearchBean() {
		return dialogCore.getSearchBean();
	}
	
	/**
	 * calls {@link DialogCore.startProviderSearch()}
	 */
	public void startProviderSearch() {
		dialogCore.startProviderSearch();
	}
	
	/**
	 * calls {@link DialogCore.startOrganisationSearch()}
	 */
	public void startOrganisationSearch() {
		dialogCore.startOrganisationSearch();
	}
	
	/**
	 * calls {@link DialogCore.startHistorySearch(null, null)}
	 */
	public void startHistorySearch() {
		dialogCore.startHistorySearch(null, null);
	}
	
	/**
	 * resets provider search
	 */
	public void resetProviderSearch() {
		dialogCore.getSearchBean().reset();
		dialogCore.setProviderDataModel(null);
	}
	
	/**
	 * resets organisation search
	 */
	public void resetOrganisationSearch() {
		dialogCore.getSearchBean().reset();
		dialogCore.setOrganisationDataModel(null);
	}
	
	/**
	 * resets history search
	 */
	public void resetHistorySearch() {
		dialogCore.getSearchBean().reset();
		dialogCore.setHistoryDataModel(null);
	}
	
	/**
	 * searchs history by ip
	 */
	public void searchHistoryByIp() {
		String ip = getHistoryIpAddress();
		dialogCore.startHistorySearch(ip, null);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("showHistory.xhtml");
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/**
	 * searchs history by session id
	 */
	public void searchHistoryBySession() {
		String sessionId = getHistorySessionID();
		dialogCore.startHistorySearch(null, sessionId);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("showHistory.xhtml");
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	
	/**
	 * gets duplicate details
	 * @return
	 */
	public DuplicateDetails getDuplicateDetails(){
		return dialogCore.getDuplicateDetails();
	}
	
	
	//------------------------
	// Duplicate Configuration
	//------------------------
	
	
	/**
	 * gets duplicate configuration
	 * @return
	 */
	public DuplicateConfigurationDTO getDuplicateConfiguration()
	{
		return dialogCore.getDuplicateConfiguration();
	}
	
	/**
	 * gets import status of provider
	 * @return
	 */
	public ImportResultDTO getProviderImportStatus() {
		return dialogCore.getProviderImportStatus();
	}

	/**
	 * gets import status of organisation
	 * @return
	 */	
	public ImportResultDTO getOrganisationImportStatus() {
		return dialogCore.getOrganisationImportStatus();
	}
	
	/**
	 * sets duplicate configuration
	 */
	public void updateDuplicateConfiguration(){
		dialogCore.updateDuplicateConfiguration();
	}
	
	/**
	 * gets provider data model
	 * @return
	 */
	public ProviderDataModel getProviderDataModel() {
		return dialogCore.getProviderDataModel();
	}
	
	/**
	 * gets history data model
	 * @return
	 */
	public HistoryDataModel getHistoryDataModel() {
		return dialogCore.getHistoryDataModel();
	}
	
	/**
	 * gets organisation data model
	 * @return
	 */
	public OrganisationDataModel getOrganisationDataModel() {
		return dialogCore.getOrganisationDataModel();
	}
	
	/**
	 * gets duplicate data model
	 * @return
	 */
	public DuplicateDataModel getDuplicateDataModel() {
		return dialogCore.getDuplicateDataModel();
	}
	
	/**
	 * gets list of available user
	 * @return
	 */
	public List<UserDTO> getUserList() {
		return dialogCore.requestUserList();
	}
	
	/**
	 * sets selected user id
	 */
	public void setSelectedUserId(Object userid) {
		dialogCore.setSelectedUserId((Integer) userid);
	}
	
	/**
	 * gets selected user id
	 * @return
	 */
	public Object getSelectedUserId() {
		return dialogCore.getSelectedUserId();
	}	
}
