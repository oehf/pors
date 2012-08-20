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

import org.openehealth.pors.database.entities.LocalId;

/**
 * <p>
 * Thrown, if trying to update or insert a local ID to a value, which 
 * already exists in any way.
 * </p>
 * 
 * @author jr
 *
 */
public class DoubleLocalIdException extends Exception 
{
	private static final long serialVersionUID = 3466394547901729471L;
	private final LocalId localId;
	/**
	 * 
	 */
	public DoubleLocalIdException(LocalId localId) 
	{
		this.localId = localId;
	}

	/**
	 * @param message
	 */
	public DoubleLocalIdException(LocalId localId, String message) 
	{
		super(message);
		this.localId = localId;
	}

	/**
	 * @param cause
	 */
	public DoubleLocalIdException(LocalId localId, Throwable cause) 
	{
		super(cause);
		this.localId = localId;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DoubleLocalIdException(LocalId localId, String message, Throwable cause) 
	{
		super(message, cause);
		this.localId = localId;
	}
	
	/**
	 * <p>
	 * Returns a local ID containing the duplicate content.
	 * </p>
	 * @return
	 */
	public LocalId getDoubleLocalId()
	{
		return this.localId;
	}

}
