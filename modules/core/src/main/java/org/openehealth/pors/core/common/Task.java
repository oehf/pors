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

import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;

/**
 * @author ck
 *
 */
public class Task {
	
	/**Following attributes defining the actiontype**/
	public static final int CREATE = 1;
	public static final String CREATE_STRING = "create";
	public static final int READ = 2;
	public static final String READ_STRING = "read";
	public static final int UPDATE = 3;
	public static final String UPDATE_STRING = "update";
	public static final int DELETE = 4;
	public static final String DELETE_STRING = "delete";
	public static final int REACTIVATE = 5;
	public static final String REACTIVATE_STRING = "reactivate";
	public static final int DEACTIVATE = 6;
	public static final String DEACTIVATE_STRING = "deactivate";
	public static final int READ_ALL = 7;
	public static final String READ_ALL_STRING = "read_all";
	public static final int UPDATE_ALL = 8;
	public static final String UPDATE_ALL_STRING = "update_all";
	public static final int DELETE_ALL = 9;
	public static final String DELETE_ALL_STRING = "delete_all";
	public static final int REACTIVATE_ALL = 10;
	public static final String REACTIVATE_ALL_STRING = "reactivate_all";
	public static final int DEACTIVATE_ALL = 11;
	public static final String DEACTIVATE_ALL_STRING = "deactivate_all";
	public static final int QUERY = 12;
	public static final String CONFIGURE_STRING = "configure";
	public static final int CONFIGURE = 13;
	/**Following attributes defining the domain
	 * sum of action + domain represents method-id
	 **/
	public static final int PROVIDER = 100;
	public static final String PROVIDER_STRING = "provider";
	public static final int ORGANISATION = 200;
	public static final String ORGANISATION_STRING = "organisation";
	public static final int LOGGING = 300;
	public static final String LOGGING_STRING = "logging";
	public static final int USER = 400;
	public static final String USER_STRING = "user";
	public static final int LOCALID = 500;
	public static final String LOCALID_STRING = "localid";
	public static final int DUPLICATES = 600;
	public static final String DUPLICATES_STRING = "duplicates";
	public static final int ADDRESS = 700;
	public static final String ADDRESS_STRING = "address";
	public static final int SYSTEM = 800;
	public static final String SYSTEM_STRING = "system";
	
	/**Logical Operator**/
	public static final String LOGICAL_OPERATOR_AND = "AND";
	public static final String LOGICAL_OPERATOR_OR = "OR";
	
	/**A Task has an action and a domain specified**/
	private int action;
	private String actionString;
	private int domain;
	private String domainString;
	
	/**A Task CAN contain a providerDto and/or a hl7 return message**/
	private Provider provider;
	private Organisation organisation;
	
	/**For Queries**/
	private String logicalOperator;

	/**In the constructor the actiontype and the domain is set
	 * 
	 * @param action
	 * @param domain
	 */
	public Task(int action, int domain){
		this.action = action;
		this.actionString = getActionStringFromInt();
		this.domain = domain;
		this.domainString = getDomainStringFromInt();
	}
	
	/**
	 * Creates a new Task with a special action and domain, both are strings
	 * @param actionString the string representation of the action
	 * @param domainString the string representation of the domain
	 */
	public Task(String actionString, String domainString) {
		this.actionString = actionString;
		this.action = getActionIntFromString();
		this.domainString = domainString;
		this.domain = getDomainIntFromString();
	}
	
	/**
	 * @return the provider
	 */
	public Provider getProvider() {
		return provider;
	}

	/**
	 * Sets a new provider
	 * @param provider the provider to set
	 */
	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	/**
	 * @return the action
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets a new action.
	 * @param action the action to set
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * @return the domain
	 */
	public int getDomain() {
		return domain;
	}

	/**
	 * Sets a new domain
	 * @param domain the domain to set
	 */
	public void setDomain(int domain) {
		this.domain = domain;
	}
	
	/**
	 * @return the combination of action and domain
	 */
	public int getActionDomain(){
		return this.action + this.domain;
	}
	
	/**
	 * @return the string representation of the action
	 */
	public String getActionString() {
		return actionString;
	}

	/**
	 * Sets the the string representation of the action
	 * @param actionString the action to set
	 */
	public void setActionString(String actionString) {
		this.actionString = actionString;
	}

	/**
	 * @return the string representation of the domain
	 */
	public String getDomainString() {
		return domainString;
	}

	/**
	 * Sets the string representation of the domain
	 * @param domainString the domain to set
	 */
	public void setDomainString(String domainString) {
		this.domainString = domainString;
	}

	/**
	 * @return the string representation of an action from the integer representation
	 */
	private String getActionStringFromInt() {
		switch (action) {
		case CREATE:
			return CREATE_STRING;
		case READ:
			return READ_STRING;
		case UPDATE:
			return UPDATE_STRING;
		case DELETE:
			return DELETE_STRING;
		case REACTIVATE:
			return REACTIVATE_STRING;
		case DEACTIVATE:
			return DEACTIVATE_STRING;
		case READ_ALL:
			return READ_ALL_STRING;
		case UPDATE_ALL:
			return UPDATE_ALL_STRING;
		case DELETE_ALL:
			return DELETE_ALL_STRING;
		case REACTIVATE_ALL:
			return REACTIVATE_ALL_STRING;
		case DEACTIVATE_ALL:
			return DEACTIVATE_ALL_STRING;
		case CONFIGURE:
			return CONFIGURE_STRING;

		default:
			return null;
		}
	}
	
	/**
	 * @return the string representation of a domain from the integer representation
	 */
	private String getDomainStringFromInt() {
		switch (domain) {
		case PROVIDER:
			return PROVIDER_STRING;
		case ORGANISATION:
			return ORGANISATION_STRING;
		case LOGGING:
			return LOGGING_STRING;
		case USER:
			return USER_STRING;
		case LOCALID:
			return LOCALID_STRING;
		case ADDRESS:
			return ADDRESS_STRING;
		case DUPLICATES:
			return DUPLICATES_STRING;
		case SYSTEM:
			return SYSTEM_STRING;
		default:
			return null;
		}
	}
	
	/**
	 * @return the integer representation of the domain from the string representation
	 */
	private int getDomainIntFromString() {
		if (domainString.equals(PROVIDER_STRING)) {
			return PROVIDER;
		} else if (domainString.equals(ORGANISATION_STRING)) {
			return ORGANISATION;
		} else if (domainString.equals(LOGGING_STRING)) {
			return LOGGING;
		} else if (domainString.equals(USER_STRING)) {
			return USER;
		} else if (domainString.equals(LOCALID_STRING)) {
			return LOCALID;
		} else if (domainString.equals(ADDRESS_STRING)) {
			return ADDRESS;
		} else if (domainString.equals(DUPLICATES_STRING)) {
			return DUPLICATES;
		} else if (domainString.equals(SYSTEM_STRING)) {
			return SYSTEM;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return the integer representation of an action from the string representation
	 */
	private int getActionIntFromString() {
		if (actionString.equals(CREATE_STRING)) {
			return CREATE;
		} else if (actionString.equals(READ_STRING)) {
			return READ;
		} else if (actionString.equals(UPDATE_STRING)) {
			return UPDATE;
		} else if (actionString.equals(DEACTIVATE_STRING)) {
			return DEACTIVATE;
		} else if (actionString.equals(REACTIVATE_STRING)) {
			return REACTIVATE;
		} else if (actionString.equals(READ_ALL_STRING)) {
			return READ_ALL;
		} else if (actionString.equals(UPDATE_ALL_STRING)) {
			return UPDATE_ALL;
		} else if (actionString.equals(DEACTIVATE_ALL_STRING)) {
			return DEACTIVATE_ALL;
		} else if (actionString.equals(REACTIVATE_ALL_STRING)) {
			return REACTIVATE_ALL;
		} else if (actionString.equals(CONFIGURE_STRING)) {
			return CONFIGURE;
		} else {
			return -1;
		}
	}

	/**
	 * @return the organisation for the task.
	 */
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 * Sets a new organisation for the task.
	 * @param organisation the organisation to set
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

}
