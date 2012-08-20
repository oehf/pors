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
package org.openehealth.pors.communication.exception;

import javax.xml.ws.WebFault;

/**
 * PORSException represents an exception that may occur by calling the 
 * Web Service Methods of PORS.
 * @author mf
 *
 */
@WebFault
public class PORSException extends Exception {
	
	/**
	 * ID for Serializable.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Error code: user does not have enough rights for the action.
	 */
	public static final String MISSING_RIGHTS = "10";
	/**
	 * Error code: authentification failed for the user
	 */
	public static final String AUTH_FAILED 	= "20";
	/**
	 * Error code: internal database error
	 */
	public static final String DATABASE_ERROR = "30";
	/**
	 * Error code: the requested entity was not found
	 */
	public static final String ENTITY_NOT_FOUND = "40";
	
	/**
	 * Error code: required fields are missing
	 */
	public static final String MISSING_FIELDS = "50";
	
	/**
	 * Error code: wrong value
	 */
	public static final String WRONG_VALUE = "60";
	
	/**
	 * Error code: provider not found
	 */
	public static final String PROVIDER_NOT_FOUND = "70";
	
	/**
	 * Error code: organisation not found
	 */
	public static final String ORGANISATION_NOT_FOUND = "80";
	
	/**
	 * Error code: exort failed
	 */
	public static final String EXPORT_FAILED = "90";
	
	/**
	 * Error code: search failed
	 */
	public static final String SEARCH_FAILED = "100";
	
	/**
	 * Error code: search failed
	 */
	public static final String IMPORT_FAILED = "100";
	
	/**
	 * Error code: address not found
	 */
	public static final String ADDRESS_NOT_FOUND = "110";

	private String   errorCode;
	private String   errorDescription;
	
	/**
	 * Creates a new PorsException
	 */
	public PORSException() {
		super();
	}
	
	/**
	 * Creates a new PORSException with a given error code and a message.
	 * @param errorCode the error code, one of the constants of this class.
	 * @param errorDescription the error message
	 * @param variables 
	 */
	public PORSException(String errorCode, Exception ex) {
		this.errorCode = errorCode;
		this.errorDescription = ex.getMessage();
	}


	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public String getErrorDescription() {
		return errorDescription;
	}


	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}


}
