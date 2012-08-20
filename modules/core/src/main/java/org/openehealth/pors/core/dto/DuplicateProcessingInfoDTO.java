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
 * The DTO for DuplicateProcessingInfo
 * 
 * @author mf
 * 
 */
public class DuplicateProcessingInfoDTO {

	public static final int DSKEEPFIRST = 1;
	public static final int DSKEEPSECOND = 2;
	public static final int DSREPLACEBOTH = 3;
	public static final int DSKEEPBOTH = 4;
	private int duplicateStrategy;

	private ProviderDTO mergedProvider;
	private OrganisationDTO mergedOrganisation;
	private AddressDTO mergedAddress;
	private String ip;
	private String sessionId;

	public int getDuplicateStrategy() {
		return duplicateStrategy;
	}

	public void setDuplicateStrategy(int duplicateStrategy) {
		this.duplicateStrategy = duplicateStrategy;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
