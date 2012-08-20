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

/**
 * <p>
 * Thrown if tried to insert an already existing establishment ID.
 * </p>
 * 
 * @author jr
 *
 */
public class DoubleEstablishmentIdException extends Exception 
{
	private static final long serialVersionUID = 5482651890431506041L;
	private final String establishmentId;

	/**
	 * <p>
	 * Initialises this exception with the establishment ID whose double 
	 * occurrence caused this exception.
	 * </p>
	 * 
	 * @param establishmentId
	 * 		Establishment ID whose double occurrence caused this exception
	 */
	public DoubleEstablishmentIdException(final String establishmentId) 
	{
		this.establishmentId = establishmentId;
	}
	
	/**
	 * <p>
	 * Initialises this exception with the establishment ID whose double 
	 * occurrence caused this exception and an error message.
	 * </p>
	 * 
	 * @param establishmentId
	 * 		Establishment ID whose double occurrence caused this exception
	 * @param message
	 * 		Error message
	 */
	public DoubleEstablishmentIdException(String establishmentId, String message) 
	{
		super(message);
		this.establishmentId = establishmentId;
	}

	/**
	 * <p>
	 * Initialises this exception with the establishment ID whose double 
	 * occurrence caused this exception and a cause.
	 * </p>
	 * 
	 * @param establishmentId
	 * 		Establishment ID whose double occurrence caused this exception
	 * @param cause
	 * 		Cause of this exception
	 */
	public DoubleEstablishmentIdException(String establishmentId, Throwable cause) 
	{
		super(cause);
		this.establishmentId = establishmentId;
	}

	/**
	 * <p>
	 * Initialises this exception with the establishment ID whose double 
	 * occurrence caused this exception, an error message and a cause of this 
	 * exception.
	 * </p>
	 * 
	 * @param establishmentId
	 * 		Establishment ID whose double occurrence caused this exception
	 * @param message
	 * 		Error message
	 * @param cause
	 * 		Cause of this exception
	 */
	public DoubleEstablishmentIdException(String establishmentId, String message, Throwable cause) 
	{
		super(message, cause);
		this.establishmentId = establishmentId;
	}

	/**
	 * <p>
	 * Gets the establishment ID whose double occurence caused this exception.
	 * </p>
	 * 
	 * @return
	 * 		Double establishment ID
	 */
	public String getDoubleEstablishmentId() 
	{
		return establishmentId;
	}

}
