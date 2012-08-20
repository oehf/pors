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
 * The DTO for localIDs
 * 
 * @author tb
 * 
 */
public class LocalIdDTO {

	private Long id;
	private Long organisationId;
	private String organisationName;
	private Long providerId;
	private String providerFirstname;
	private String providerLastname;
	private String localId;
	private String facility;
	private String application;

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public String getLocalId() {
		return localId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getFacility() {
		return facility;
	}

	public Long getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Long organisationId) {
		this.organisationId = organisationId;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public String getProviderFirstname() {
		return providerFirstname;
	}

	public void setProviderFirstname(String providerFirstname) {
		this.providerFirstname = providerFirstname;
	}

	public String getProviderLastname() {
		return providerLastname;
	}

	public void setProviderLastname(String providerLastname) {
		this.providerLastname = providerLastname;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

}
