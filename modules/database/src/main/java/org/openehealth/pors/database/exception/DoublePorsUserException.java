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

import org.openehealth.pors.database.entities.PorsUser;

/**
 * <p>
 * Thrown if trying to insert / update an PORS user having a value which has to 
 * be unique and already exists.
 * </p>
 * 
 * @author jr
 *
 */
public class DoublePorsUserException extends Exception 
{
	private static final long serialVersionUID = 2706775791182531541L;
	private final PorsUser porsUser;

	/**
	 * <p>
	 * Initialises this exception with an PORS user object containing the 
	 * duplicate values.
	 * </p>
	 * 
	 * @param user
	 * 		Duplicate PORS user
	 */
	public DoublePorsUserException(final PorsUser user) 
	{
		this.porsUser = user;
	}

	/**
	 * <p>
	 * Initialises this exception with an PORS user object containing the 
	 * duplicate values and a corresponding message.
	 * </p>
	 * 
	 * @param user
	 * 		Duplicate PORS user
	 * @param msg
	 * 		Exception message
	 */
	public DoublePorsUserException(final PorsUser user, String msg) 
	{
		super(msg);
		this.porsUser = user;
	}

	/**
	 * <p>
	 * Initialises this exception with an PORS user object containing the 
	 * duplicate values and its cause.
	 * </p>
	 * 
	 * @param user
	 * 		Duplicate user
	 * @param cause
	 * 		Cause of this exception
	 */
	public DoublePorsUserException(final PorsUser user, Throwable cause) 
	{
		super(cause);
		this.porsUser = user;
	}

	/**
	 * <p>
	 * Initialises this exception with an PORS user object containing the 
	 * duplicate values, an exception message and the cause of this exception. 
	 * </p>
	 * 
	 * @param user
	 * 		Duplicate user
	 * @param msg
	 * 		Exception message
	 * @param cause
	 * 		Cause of this exception
	 */
	public DoublePorsUserException(PorsUser user, String msg, Throwable cause) 
	{
		super(msg, cause);
		this.porsUser = user;
	}

	/**
	 * <p>
	 * Returns an PORS user object containing the duplicate content.
	 * </p>
	 * 
	 * @return
	 * 		Duplicate PORS user
	 */
	public PorsUser getDoublePorsUser()
	{
		return this.porsUser;
	}
}
