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
package org.openehealth.pors.database.exception;

import org.openehealth.pors.database.entities.IMasterDomain;

/**
 * @author jr
 *
 */
public class DoubleMasterDomainException extends Exception 
{
	private static final long serialVersionUID = 7775519658396063508L;
	
	private IMasterDomain existingDomain;
	private IMasterDomain receivedDomain;

	/**
	 * 
	 */
	public DoubleMasterDomainException(IMasterDomain existingDomain, IMasterDomain receivedDomain) 
	{
		super();
		this.initDomains(existingDomain, receivedDomain);
	}

	/**
	 * @param arg0
	 */
	public DoubleMasterDomainException(String msg, IMasterDomain existingDomain, IMasterDomain receivedDomain) 
	{
		super(msg);
		this.initDomains(existingDomain, receivedDomain);
	}

	/**
	 * @param arg0
	 */
	public DoubleMasterDomainException(Throwable cause, IMasterDomain existingDomain, IMasterDomain receivedDomain) 
	{
		super(cause);
		this.initDomains(existingDomain, receivedDomain);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DoubleMasterDomainException(String msg, Throwable cause, IMasterDomain existingDomain, IMasterDomain receivedDomain) 
	{
		super(msg, cause);
		this.initDomains(existingDomain, receivedDomain);
	}

	private void initDomains(IMasterDomain existingDomain, IMasterDomain receivedDomain)
	{
		this.existingDomain = existingDomain;
		this.receivedDomain = receivedDomain;
	}

	public IMasterDomain getExistingDomain() {
		return existingDomain;
	}

	public void setExistingDomain(IMasterDomain existingDomain) {
		this.existingDomain = existingDomain;
	}

	public IMasterDomain getReceivedDomain() {
		return receivedDomain;
	}

	public void setReceivedDomain(IMasterDomain receivedDomain) {
		this.receivedDomain = receivedDomain;
	}
}
