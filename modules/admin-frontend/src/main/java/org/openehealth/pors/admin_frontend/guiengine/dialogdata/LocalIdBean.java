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

import org.openehealth.pors.admin_client.LocalIdDTO;

public class LocalIdBean extends LocalIdDTO {

	public LocalIdBean(Long id, String facility, String localId, Long organisation, Long provider, String application) {
		this.id = id;
		this.facility = facility;
		this.localId = localId;
		this.organisationId = organisation;
		this.providerId = provider;
		this.application = application;
	}
	
	public LocalIdBean(LocalIdDTO localId){
		this.id = localId.getId();
		this.facility = localId.getFacility();
		this.localId = localId.getLocalId();
		this.organisationId = localId.getOrganisationId();
		this.providerId = localId.getProviderId();
		this.application = localId.getApplication();
	}
	
	public LocalIdBean(LocalIdBean localId){
		this();
		this.id = localId.getId();
		this.facility = localId.getFacility();
		this.localId = localId.getLocalId();
		this.organisationId = localId.getOrganisationId();
		this.providerId = localId.getProviderId();
		this.application = localId.getApplication();
	}

	
	public LocalIdBean() {
		super();
	}

	@Override
	public String toString() {
		return String.format("%s %s %s", this.getLocalId(), this.getFacility(), this.getApplication());
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(((LocalIdBean)obj).toString());
	}
}