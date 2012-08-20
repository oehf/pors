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
 * The DTO for DuplicateDetail
 * 
 * @author tb
 * 
 */
public class DuplicateDetailDTO {

	private DuplicateEntryDTO duplicateEntry;

	private ProviderDTO provider1;
	private ProviderDTO provider2;

	private OrganisationDTO organisation1;
	private OrganisationDTO organisation2;

	private AddressDTO address1;
	private AddressDTO address2;

	public DuplicateEntryDTO getDuplicateEntry() {
		return duplicateEntry;
	}

	public void setDuplicateEntry(DuplicateEntryDTO duplicateEntry) {
		this.duplicateEntry = duplicateEntry;
	}

	public ProviderDTO getProvider1() {
		return provider1;
	}

	public void setProvider1(ProviderDTO provider1) {
		this.provider1 = provider1;
	}

	public ProviderDTO getProvider2() {
		return provider2;
	}

	public void setProvider2(ProviderDTO provider2) {
		this.provider2 = provider2;
	}

	public OrganisationDTO getOrganisation1() {
		return organisation1;
	}

	public void setOrganisation1(OrganisationDTO organisation1) {
		this.organisation1 = organisation1;
	}

	public OrganisationDTO getOrganisation2() {
		return organisation2;
	}

	public void setOrganisation2(OrganisationDTO organisation2) {
		this.organisation2 = organisation2;
	}

	public AddressDTO getAddress1() {
		return address1;
	}

	public void setAddress1(AddressDTO address1) {
		this.address1 = address1;
	}

	public AddressDTO getAddress2() {
		return address2;
	}

	public void setAddress2(AddressDTO address2) {
		this.address2 = address2;
	}

}
