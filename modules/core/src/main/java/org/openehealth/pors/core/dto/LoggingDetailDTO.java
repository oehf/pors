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
 * The DTO for logging detail
 * 
 * @author mf
 * 
 */
public class LoggingDetailDTO {

	private LoggingEntryDTO loggingEntry;
	private ProviderLogDTO providerLog;
	private OrganisationLogDTO organisationLog;
	private AddressLogDTO addressLog;
	private LocalIdLogDTO localidLog;
	private ProviderHasAddressLogDTO providerHasAddressLog;
	private OrganisationHasAddressLogDTO organisationHasAddressLog;
	private OrganisationHasProviderLogDTO organisationHasProviderLog;

	public LoggingEntryDTO getLoggingEntry() {
		return loggingEntry;
	}

	public void setLoggingEntry(LoggingEntryDTO loggingEntry) {
		this.loggingEntry = loggingEntry;
	}

	public ProviderLogDTO getProviderLog() {
		return providerLog;
	}

	public void setProviderLog(ProviderLogDTO providerLog) {
		this.providerLog = providerLog;
	}

	public OrganisationLogDTO getOrganisationLog() {
		return organisationLog;
	}

	public void setOrganisationLog(OrganisationLogDTO organisationLog) {
		this.organisationLog = organisationLog;
	}

	public AddressLogDTO getAddressLog() {
		return addressLog;
	}

	public void setAddressLog(AddressLogDTO addressLog) {
		this.addressLog = addressLog;
	}

	public LocalIdLogDTO getLocalidLog() {
		return localidLog;
	}

	public void setLocalidLog(LocalIdLogDTO localidLog) {
		this.localidLog = localidLog;
	}

	public void setProviderHasAddressLog(ProviderHasAddressLogDTO phaLog) {
		this.providerHasAddressLog = phaLog;
	}

	public ProviderHasAddressLogDTO getProviderHasAddressLog() {
		return providerHasAddressLog;
	}

	public void setOrganisationHasAddressLog(
			OrganisationHasAddressLogDTO organisationHasAddressLog) {
		this.organisationHasAddressLog = organisationHasAddressLog;
	}

	public OrganisationHasAddressLogDTO getOrganisationHasAddressLog() {
		return organisationHasAddressLog;
	}

	public void setOrganisationHasProviderLog(
			OrganisationHasProviderLogDTO organisationHasProviderLog) {
		this.organisationHasProviderLog = organisationHasProviderLog;
	}

	public OrganisationHasProviderLogDTO getOrganisationHasProviderLog() {
		return organisationHasProviderLog;
	}

}
