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

import java.io.Serializable;
import java.util.Date;

/**
 * The DTO for LoggingEntry
 * 
 * @author mf
 * 
 */
public class LoggingEntryDTO implements Serializable {

	private static final long serialVersionUID = 7755238825234250071L;
	private Integer porsUserId;
	private String userName;
	private Date logTime;
	private String domain;
	private String action;
	private Long logEntryId;
	private String logTimeString;
	private String logDateString;

	public void setPorsUserId(Integer porsUserId) {
		this.porsUserId = porsUserId;
	}

	public Integer getPorsUserId() {
		return porsUserId;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public void setLogEntryId(Long logEntryId) {
		this.logEntryId = logEntryId;
	}

	public Long getLogEntryId() {
		return logEntryId;
	}

	public String getLogTimeString() {
		return logTimeString;
	}

	public void setLogTimeString(String logTimeString) {
		this.logTimeString = logTimeString;
	}

	public String getLogDateString() {
		return logDateString;
	}

	public void setLogDateString(String logDateString) {
		this.logDateString = logDateString;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

}
