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
package org.openehealth.pors.core.common;

/**
 * @author ck and sm
 */
public class Permission{
	private int action;
	private int domain;
	
	/**In the constructor the actiontype and the domain is set
	 * 
	 * @param action
	 * @param domain
	 */
	public Permission(int action, int domain){
		this.action = action;
		this.domain = domain;
	}
	
	/**
	 * Sets a new action
	 * @param action the action to set
	 */
	public void setAction(int action) {
		this.action = action;
	}
	
	/**
	 * @return the action
	 */
	public int getAction() {
		return action;
	}
	
	/**
	 * Sets a new domain
	 * @param domain the domain to set
	 */
	public void setDomain(int domain) {
		this.domain = domain;
	}
	
	/**
	 * @return the domain
	 */
	public int getDomain() {
		return domain;
	}
	
}
