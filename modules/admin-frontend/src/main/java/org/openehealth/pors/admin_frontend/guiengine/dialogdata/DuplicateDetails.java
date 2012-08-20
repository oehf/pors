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
package org.openehealth.pors.admin_frontend.guiengine.dialogdata;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.openehealth.pors.admin_client.AddressDTO;
import org.openehealth.pors.admin_client.DuplicateDetailDTO;
import org.openehealth.pors.admin_client.LocalIdDTO;
import org.openehealth.pors.admin_client.OrganisationDTO;
import org.openehealth.pors.admin_client.ProviderDTO;

public class DuplicateDetails {
	
	private DuplicateDetailDTO duplicateDetailDTO;
	private String duplicateStrategy;
	public static final String DSKEEPFIRST = "1";
	public static final String DSKEEPSECOND = "2";
	public static final String DSREPLACEBOTH = "3";
	public static final String DSKEEPBOTH = "4";
	
	private ProviderDTO mergedProvider;
	private OrganisationDTO mergedOrganisation;
	private AddressDTO mergedAddress;
	
	private List<LocalIdDTO> selectedLocalIds;
	private List<AddressDTO> selectedAddresses;
	private List<OrganisationDTO> selectedOrganisations;
	
	@SuppressWarnings("unused")
	private boolean addListContainsAdd = false;
	@SuppressWarnings("unused")
	private boolean orgListContainsOrg = false;
	@SuppressWarnings("unused")
	private boolean lidListContainsLid = false;
	
	public DuplicateDetails() {
		selectedLocalIds = new ArrayList<LocalIdDTO>();
		selectedAddresses = new ArrayList<AddressDTO>();
		selectedOrganisations = new ArrayList<OrganisationDTO>();
		
		mergedProvider = new ProviderDTO();
		mergedOrganisation = new OrganisationDTO();
		mergedAddress = new AddressDTO();
	}
	
	/**
	 * 
	 * @return duplicateStrategy represents the strategy, how to solve the duplicates
	 */
	public String getDuplicateStrategy(){
		return duplicateStrategy;
	}
	
	/**
	 * 
	 * @param ds represents the strategy, how to solve the duplicates
	 */
	public void setDuplicateStrategy(String ds){
		duplicateStrategy = ds;
	}

	public DuplicateDetailDTO getDuplicateDetail(){
		return duplicateDetailDTO;
	}
	
	public void setDuplicateDetail(DuplicateDetailDTO ddDTO){
		duplicateDetailDTO = ddDTO;
	}
	
	/**
	 * Invoked by Checkbox, adds and removes LocalIds from List of selected LocalIds
	 * @param lid
	 */
	public void mergeLocalId(LocalIdDTO lid){
		if(selectedLocalIds.contains(lid)){
			selectedLocalIds.remove(lid);
		} else {
			selectedLocalIds.add(lid);
		}
	}
	
	public List<LocalIdDTO> getSelectLocalIds(){
		return selectedLocalIds;
	}
	
	/**
	 * Invoked by Checkbox, adds and removes Addresses from List of selected Addresses
	 * @param add
	 */
	public void mergeAddress(AddressDTO add){
		if(selectedAddresses.contains(add)){
			selectedAddresses.remove(add);
		} else {
			selectedAddresses.add(add);
		}
	}
	
	public List<AddressDTO> getSelectAddresses(){
		return selectedAddresses;
	}
	
	/**
	 * Invoked by Checkbox, adds and removes Organisation from List of selected Organisation
	 * @param add
	 */
	public void mergeOrganisation(OrganisationDTO org){
		if(selectedOrganisations.contains(org)){
			selectedOrganisations.remove(org);
		} else {
			selectedOrganisations.add(org);
		}
	}
	
	public List<OrganisationDTO> getSelectOrganisations(){
		return selectedOrganisations;
	}
	
	public List<OrganisationDTO> getProviderOrganisations(){
		List <OrganisationDTO> orgs = new ArrayList<OrganisationDTO>();
		orgs.addAll(duplicateDetailDTO.getProvider1().getOrganisations());
		
		for(int i=0;i<duplicateDetailDTO.getProvider2().getOrganisations().size();i++){
			if(!this.getOrgListContainsOrg(orgs, duplicateDetailDTO.getProvider2().getOrganisations().get(i))){
				orgs.add(duplicateDetailDTO.getProvider2().getOrganisations().get(i));
			}
		}
		return orgs;
	}
	
	public List<LocalIdDTO> getProviderLocalIds(){
		List <LocalIdDTO> lids = new ArrayList<LocalIdDTO>();
		lids.addAll(duplicateDetailDTO.getProvider1().getLocalId());
		
		for(int i=0;i<duplicateDetailDTO.getProvider2().getLocalId().size();i++){
			if(!this.getLidListContainsLid(lids, duplicateDetailDTO.getProvider2().getLocalId().get(i))){
				lids.add(duplicateDetailDTO.getProvider2().getLocalId().get(i));
			}
		}
		return lids;
	}
	
	public List<AddressDTO> getProviderAddresses(){
		List <AddressDTO> adds = new ArrayList<AddressDTO>();
		adds.addAll(duplicateDetailDTO.getProvider1().getAddresses());
		
		for(int i=0;i<duplicateDetailDTO.getProvider2().getAddresses().size();i++){
			if(!this.addListContainsAdd(adds, duplicateDetailDTO.getProvider2().getAddresses().get(i))){
				adds.add(duplicateDetailDTO.getProvider2().getAddresses().get(i));
			}
		}
		return adds;
	}
	
	/**
	 * Checks if the List of Organisations contains the Organisation
	 * @param orgList
	 * @param org
	 * @return if organisation is in the Organisation List
	 */
	public boolean getOrgListContainsOrg(List<OrganisationDTO> orgList, OrganisationDTO org){
		boolean contains = false;
		for(int k=0;k<orgList.size();k++){
			if(orgList.get(k).getName().equals(org.getName())){
				contains = true;
			}
		}
		return contains;
	}
	
	public boolean orgListContainsOrg(List<OrganisationDTO> orgList, OrganisationDTO org){
		return getOrgListContainsOrg(orgList, org);
	}
	
	public void setOrgListContainsOrg(boolean test) {
		orgListContainsOrg = test;
	}
	
	/**
	 * Checks if List of LocalIds contains LocalId
	 * @param lidList
	 * @param lid
	 * @return boolean if the LocalId List contains the LocalId
	 */
	public boolean getLidListContainsLid(List<LocalIdDTO> lidList, LocalIdDTO lid){
		boolean contains = false;
		for(int k=0;k<lidList.size();k++){
			if(lidList.get(k).getId().equals(lid.getId())){
				contains = true;
			}
		}
		return contains;
	}
	
	public boolean lidListContainsLid(List<LocalIdDTO> lidList, LocalIdDTO lid){
		return getLidListContainsLid(lidList, lid);
	}
	
	public void setLidListContainsLid(boolean test) {
		lidListContainsLid = test;
	}
	
	/**
	 * Checks if List of Address contains Address
	 * @param addList
	 * @param add
	 * @return boolean if the Address List contains the Address
	 */
	public boolean getAddListContainsAdd(List<AddressDTO> addList, AddressDTO add){
		boolean contains = false;
		for(int k=0;k<addList.size();k++){
			if(addList.get(k).getId().equals(add.getId())){
				contains = true;
			}
		}
		return contains;
	}
	
	public void setAddListContainsAdd(boolean test){
		addListContainsAdd=test;
	}
	
	/**
	 * Checks if List of Address contains Address
	 * @param addList
	 * @param add
	 * @return boolean if the Address List contains the Address
	 */
	public boolean addListContainsAdd(List<AddressDTO> addList, AddressDTO add){
		return getAddListContainsAdd(addList, add);
	}

	/**
	 * Method to request the merged Object, which could be a Organisation, Provider or Address
	 * @return Object which represents the mergedObject
	 */
	public Object getMergedObject() {
		if(null!=this.getDuplicateDetail().getProvider1()){
			mergedProvider.getAddresses().addAll(this.selectedAddresses);
			mergedProvider.getLocalId().addAll(this.selectedLocalIds);
			mergedProvider.getOrganisations().addAll(this.selectedOrganisations);
			return mergedProvider;
		} else if(null!=this.getDuplicateDetail().getAddress1()){
				
			return mergedAddress;
		} else if(null!=this.getDuplicateDetail().getOrganisation1()){
			mergedOrganisation.getAddresses().addAll(this.selectedAddresses);
			mergedOrganisation.getLocalId().addAll(this.selectedLocalIds);
			return mergedOrganisation;
		}
		return null;
	}

	public ProviderDTO getMergedProvider() {
		return mergedProvider;
	}

	public void setMergedProvider(ProviderDTO mergedProvider) {
		this.mergedProvider = mergedProvider;
	}

	public OrganisationDTO getMergedOrganisation() {
		return mergedOrganisation;
	}

	public void setMergedOrganisation(OrganisationDTO mergedOrganisation) {
		this.mergedOrganisation = mergedOrganisation;
	}

	public AddressDTO getMergedAddress() {
		return mergedAddress;
	}

	public void setMergedAddress(AddressDTO mergedAddress) {
		this.mergedAddress = mergedAddress;
	}
	
	/** SelectItems for Provider **/
	
	public List<SelectItem> getSelectProviderLanr(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getLanr()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getLanr()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getLanr() &&
				!duplicateDetailDTO.getProvider2().getLanr().equals(duplicateDetailDTO.getProvider1().getLanr())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getLanr()));
		}
		if(!si.isEmpty()){
			this.mergedProvider.setLanr(si.get(0).getValue().toString());
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderOid(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getOid()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getOid()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getOid() &&
				!duplicateDetailDTO.getProvider2().getOid().equals(duplicateDetailDTO.getProvider1().getOid())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getOid()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderFirstname(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getFirstname()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getFirstname()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getFirstname() &&
				!duplicateDetailDTO.getProvider2().getFirstname().equals(duplicateDetailDTO.getProvider1().getFirstname())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getFirstname()));
		}
		if(!si.isEmpty()){
			this.mergedProvider.setFirstname(si.get(0).getValue().toString());
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderLastname(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getLastname()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getLastname()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getLastname() &&
				!duplicateDetailDTO.getProvider2().getLastname().equals(duplicateDetailDTO.getProvider1().getLastname())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getLastname()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderMiddlename(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getMiddleName()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getMiddleName()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getMiddleName() &&
				!duplicateDetailDTO.getProvider2().getMiddleName().equals(duplicateDetailDTO.getProvider1().getMiddleName())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getMiddleName()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderNameprefix(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getNamePrefix()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getNamePrefix()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getNamePrefix() &&
				!duplicateDetailDTO.getProvider2().getNamePrefix().equals(duplicateDetailDTO.getProvider1().getNamePrefix())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getNamePrefix()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderNamesuffix(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getNameSuffix()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getNameSuffix()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getNameSuffix() &&
				!duplicateDetailDTO.getProvider2().getNameSuffix().equals(duplicateDetailDTO.getProvider1().getNameSuffix())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getNameSuffix()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderGendercode(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getGenderCode()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getGenderCode()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getGenderCode() &&
				!duplicateDetailDTO.getProvider2().getGenderCode().equals(duplicateDetailDTO.getProvider1().getGenderCode())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getGenderCode()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderSpecialisation(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getSpecialisation()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getSpecialisation()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getSpecialisation() &&
				!duplicateDetailDTO.getProvider2().getSpecialisation().equals(duplicateDetailDTO.getProvider1().getSpecialisation())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getSpecialisation()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderBirthday(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getBirthday()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getBirthday()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getBirthday() &&
				!duplicateDetailDTO.getProvider2().getBirthday().equals(duplicateDetailDTO.getProvider1().getBirthday())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getBirthday()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderTelephone(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getTelephone()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getTelephone()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getTelephone() &&
				!duplicateDetailDTO.getProvider2().getTelephone().equals(duplicateDetailDTO.getProvider1().getTelephone())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getTelephone()));
		}
		
		return si;
	}
	
	public List<SelectItem> getSelectProviderEmail(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getEmail()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getEmail()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getEmail() &&
				!duplicateDetailDTO.getProvider2().getEmail().equals(duplicateDetailDTO.getProvider1().getEmail())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getEmail()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderFax(){
		List<SelectItem> si = new ArrayList<SelectItem>();

		if(null!=duplicateDetailDTO.getProvider1().getFax()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getFax()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getFax() &&
				!duplicateDetailDTO.getProvider2().getFax().equals(duplicateDetailDTO.getProvider1().getFax())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getFax()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderDeactivationDate(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getDeactivationDate()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getDeactivationDate()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getDeactivationDate() &&
				!duplicateDetailDTO.getProvider2().getDeactivationDate().equals(duplicateDetailDTO.getProvider1().getDeactivationDate())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getDeactivationDate()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderDeactivationReasonCode(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getDeactivationReasonCode()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getDeactivationReasonCode()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getDeactivationReasonCode() &&
				!duplicateDetailDTO.getProvider2().getDeactivationReasonCode().equals(duplicateDetailDTO.getProvider1().getDeactivationReasonCode())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getDeactivationReasonCode()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderReactivationDate(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getProvider1().getReactivationDate()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getReactivationDate()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getReactivationDate() &&
				!duplicateDetailDTO.getProvider2().getReactivationDate().equals(duplicateDetailDTO.getProvider1().getReactivationDate())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getReactivationDate()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectProviderReactivationReasonCode(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		if(null!=duplicateDetailDTO.getProvider1().getReactivationReasonCode()){
			si.add(new SelectItem(duplicateDetailDTO.getProvider1().getReactivationReasonCode()));
		}
		if(null!=duplicateDetailDTO.getProvider2().getReactivationReasonCode() &&
				!duplicateDetailDTO.getProvider2().getReactivationReasonCode().equals(duplicateDetailDTO.getProvider1().getReactivationReasonCode())){
			si.add(new SelectItem(duplicateDetailDTO.getProvider2().getReactivationReasonCode()));
		}
		return si;
	}
		
	/** SelectItems for Organisation **/

	public List<SelectItem> getSelectOrganisationOid(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getOID()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getOID()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getOID() &&
				!duplicateDetailDTO.getOrganisation2().getOID().equals(duplicateDetailDTO.getOrganisation1().getOID())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getOID()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationEstablishmentId(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getEstablishmentID()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getEstablishmentID()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getEstablishmentID() &&
				!duplicateDetailDTO.getOrganisation2().getEstablishmentID().equals(duplicateDetailDTO.getOrganisation1().getEstablishmentID())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getEstablishmentID()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationName(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getName()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getName()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getName() &&
				!duplicateDetailDTO.getOrganisation2().getName().equals(duplicateDetailDTO.getOrganisation1().getName())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getName()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationSecondName(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getSecondName()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getSecondName()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getSecondName() &&
				!duplicateDetailDTO.getOrganisation2().getSecondName().equals(duplicateDetailDTO.getOrganisation1().getSecondName())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getSecondName()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationDescription(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getDescription()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getDescription()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getDescription() &&
				!duplicateDetailDTO.getOrganisation2().getDescription().equals(duplicateDetailDTO.getOrganisation1().getDescription())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getDescription()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationEmail(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getEmail()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getEmail()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getEmail() &&
				!duplicateDetailDTO.getOrganisation2().getEmail().equals(duplicateDetailDTO.getOrganisation1().getEmail())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getEmail()));
		}
		return si;
	}

	public List<SelectItem> getSelectOrganisationFax(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getFax()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getFax()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getFax() &&
				!duplicateDetailDTO.getOrganisation2().getFax().equals(duplicateDetailDTO.getOrganisation1().getFax())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getFax()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationTelephone(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getTelephone()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getTelephone()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getTelephone() &&
				!duplicateDetailDTO.getOrganisation2().getTelephone().equals(duplicateDetailDTO.getOrganisation1().getTelephone())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getTelephone()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationDeactivationDate(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getDeactivationDate()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getDeactivationDate()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getDeactivationDate() &&
				!duplicateDetailDTO.getOrganisation2().getDeactivationDate().equals(duplicateDetailDTO.getOrganisation1().getDeactivationDate())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getDeactivationDate()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationReactivationDate(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getReactivationDate()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getReactivationDate()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getReactivationDate() &&
				!duplicateDetailDTO.getOrganisation2().getReactivationDate().equals(duplicateDetailDTO.getOrganisation1().getReactivationDate())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getReactivationDate()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationReactivationReasonCode(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getReactivationReasonCode()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getReactivationReasonCode()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getReactivationReasonCode() &&
				!duplicateDetailDTO.getOrganisation2().getReactivationReasonCode().equals(duplicateDetailDTO.getOrganisation1().getReactivationReasonCode())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getReactivationReasonCode()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectOrganisationDeactivationReasonCode(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getOrganisation1().getDeactivationReasonCode()){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation1().getDeactivationReasonCode()));
		}
		if(null!=duplicateDetailDTO.getOrganisation2().getDeactivationReasonCode() &&
				!duplicateDetailDTO.getOrganisation2().getDeactivationReasonCode().equals(duplicateDetailDTO.getOrganisation1().getDeactivationReasonCode())){
			si.add(new SelectItem(duplicateDetailDTO.getOrganisation2().getDeactivationReasonCode()));
		}
		return si;
	}
	
	/** SelectItems for Address **/
	
	public List<SelectItem> getSelectAddressStreet(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getAddress1().getStreet()){
			si.add(new SelectItem(duplicateDetailDTO.getAddress1().getStreet()));
		}
		if(null!=duplicateDetailDTO.getAddress2().getStreet() &&
				!duplicateDetailDTO.getAddress2().getStreet().equals(duplicateDetailDTO.getAddress1().getStreet())){
			si.add(new SelectItem(duplicateDetailDTO.getAddress2().getStreet()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectAddressHouseNumber(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getAddress1().getHouseNumber()){
			si.add(new SelectItem(duplicateDetailDTO.getAddress1().getHouseNumber()));
		}
		if(null!=duplicateDetailDTO.getAddress2().getHouseNumber() &&
				!duplicateDetailDTO.getAddress2().getHouseNumber().equals(duplicateDetailDTO.getAddress1().getHouseNumber())){
			si.add(new SelectItem(duplicateDetailDTO.getAddress2().getHouseNumber()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectAddressZipCode(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getAddress1().getZipCode()){
			si.add(new SelectItem(duplicateDetailDTO.getAddress1().getZipCode()));
		}
		if(null!=duplicateDetailDTO.getAddress2().getZipCode() &&
				!duplicateDetailDTO.getAddress2().getZipCode().equals(duplicateDetailDTO.getAddress1().getZipCode())){
			si.add(new SelectItem(duplicateDetailDTO.getAddress2().getZipCode()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectAddressCity(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getAddress1().getCity()){
			si.add(new SelectItem(duplicateDetailDTO.getAddress1().getCity()));
		}
		if(null!=duplicateDetailDTO.getAddress2().getCity() &&
				!duplicateDetailDTO.getAddress2().getCity().equals(duplicateDetailDTO.getAddress1().getCity())){
			si.add(new SelectItem(duplicateDetailDTO.getAddress2().getCity()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectAddressState(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getAddress1().getState()){
			si.add(new SelectItem(duplicateDetailDTO.getAddress1().getState()));
		}
		if(null!=duplicateDetailDTO.getAddress2().getState() &&
				!duplicateDetailDTO.getAddress2().getState().equals(duplicateDetailDTO.getAddress1().getState())){
			si.add(new SelectItem(duplicateDetailDTO.getAddress2().getState()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectAddressCountry(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getAddress1().getCountry()){
			si.add(new SelectItem(duplicateDetailDTO.getAddress1().getCountry()));
		}
		if(null!=duplicateDetailDTO.getAddress2().getCountry() &&
				!duplicateDetailDTO.getAddress2().getCountry().equals(duplicateDetailDTO.getAddress1().getCountry())){
			si.add(new SelectItem(duplicateDetailDTO.getAddress2().getCountry()));
		}
		return si;
	}
	
	public List<SelectItem> getSelectAddressAdditional(){
		List<SelectItem> si = new ArrayList<SelectItem>();
		
		if(null!=duplicateDetailDTO.getAddress1().getAdditional()){
			si.add(new SelectItem(duplicateDetailDTO.getAddress1().getAdditional()));
		}
		if(null!=duplicateDetailDTO.getAddress2().getAdditional() &&
				!duplicateDetailDTO.getAddress2().getAdditional().equals(duplicateDetailDTO.getAddress1().getAdditional())){
			si.add(new SelectItem(duplicateDetailDTO.getAddress2().getAdditional()));
		}
		return si;
	}
	
}
