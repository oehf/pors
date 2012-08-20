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
 * The DTO for ImportResult
 * 
 * @author mf
 * 
 */
public class ImportResultDTO implements Serializable {

	private static final long serialVersionUID = -4933782071750558714L;

	private Long jobId;

	private String domain;
	private int addEntries;
	private int updateEntries;
	private int processed;
	private String statusMessage;
	private String errorMessage;
	private Date startDate;
	private Date endDate;
	private double progress;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getAddEntries() {
		return addEntries;
	}

	public void setAddEntries(int addEntries) {
		this.addEntries = addEntries;
	}

	public int getUpdateEntries() {
		return updateEntries;
	}

	public void setUpdateEntries(int updateEntries) {
		this.updateEntries = updateEntries;
	}

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

}
