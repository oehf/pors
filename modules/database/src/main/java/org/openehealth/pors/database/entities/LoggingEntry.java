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
package org.openehealth.pors.database.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * A logging entry which is part of a larger history consisting of all logging 
 * entries in all logging tables.
 * </p>
 * 
 * @author jr
 *
 */
public class LoggingEntry implements Serializable 
{
	private static final long serialVersionUID = 4622098633872240353L;

	public static final String DOMAIN_PROVIDER = "Provider";
	
	public static final String DOMAIN_ORGANISATION = "Organisation";
	
	public static final String DOMAIN_ADDRESS = "Address";
	
	public static final String DOMAIN_ORGANISATION_HAS_ADDRESS = "OrganisationHasAddress";
		
	public static final String DOMAIN_PROVIDER_HAS_ADDRESS = "ProviderHasAddress";
	
	public static final String DOMAIN_ORGANISATION_HAS_PROVIDER = "OrganisationHasProvider";
	
	public static final String DOMAIN_LOCALID = "LocalID";
	
	private String domain;
	
	private Long logId;
	
	private Integer porsUserId;
	
	private String userName;
	
	private Date logTime;
	
	private String action;

	public Integer getPorsUserId() {
		return porsUserId;
	}

	public void setPorsUserId(Integer porsUserId) {
		this.porsUserId = porsUserId;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public Long getLogId()
	{
		return this.logId;
	}
	
	public void setLogId(Long logId)
	{
		this.logId = logId;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
