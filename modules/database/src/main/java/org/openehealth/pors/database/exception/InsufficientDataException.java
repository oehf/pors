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
 * Exception thrown every time if data contained in an entity is insufficient
 * for inserting or updating processes.
 * </p>
 * @author jr
 *
 */
public class InsufficientDataException extends Exception 
{
	private static final long serialVersionUID = 6330340898677864182L;
	private final Object incompleteObject;

	/**
	 * 
	 */
	public InsufficientDataException(Object incompleteObject) 
	{
		this.incompleteObject = incompleteObject;
	}

	/**
	 * @param arg0
	 */
	public InsufficientDataException(Object incompleteObject, String msg) 
	{
		super(msg);
		this.incompleteObject = incompleteObject;
	}

	/**
	 * @param arg0
	 */
	public InsufficientDataException(Object incompleteObject, Throwable cause) 
	{
		super(cause);
		this.incompleteObject = incompleteObject;
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InsufficientDataException(Object incompleteObject, String msg, Throwable cause) 
	{
		super(msg, cause);
		this.incompleteObject = incompleteObject;
	}

	/**
	 * <p>
	 * Returns the object containing insufficient data.
	 * </p>
	 * 
	 * @return
	 */
	public Object getIncompleteObject()
	{
		return this.incompleteObject;
	}
}
