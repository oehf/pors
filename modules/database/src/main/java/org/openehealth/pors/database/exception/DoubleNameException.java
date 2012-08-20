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
 * Thrown if any unique name value should be used twice.
 * </p>
 * 
 * @author jr
 */
public class DoubleNameException extends Exception 
{
	private static final long serialVersionUID = -1840981758320083886L;
	
	private final String name;
	
	/**
	 * <p>
	 * Initialises this exception with the name whose double occurrence caused 
	 * this exception.
	 * </p>
	 * 
	 * @param name
	 * 		Name whose double occurrence caused this exception
	 */
	public DoubleNameException(String name) 
	{
		this.name = name;
	}

	/**
	 * <p>
	 * Initialises this exception with the name whose double occurrence caused 
	 * this exception and an error message.
	 * </p>
	 * 
	 * @param name
	 * 		Name whose double occurrence caused this exception
	 * @param msg
	 * 		Error message
	 */
	public DoubleNameException(String name, String msg) 
	{
		super(msg);
		this.name = name;
	}

	/**
	 * <p>
	 * Initialises this exception with the name whose double occurrence caused 
	 * this exception and the <code>cause</code> of this exception.
	 * </p>
	 * 
	 * @param name
	 * 		Name whose double occurrence caused this exception
	 * @param cause
	 * 		Cause of this exception
	 */
	public DoubleNameException(String name, Throwable cause) 
	{
		super(cause);
		this.name = name;
	}

	/**
	 * <p>
	 * Initialises this exception with the name whose double occurrence caused
	 * this exception, an error message and the cause of this exception.
	 * </p>
	 * 
	 * @param name
	 * 		Name whose double occurrence caused this exception
	 * @param msg
	 * 		Error message
	 * @param cause
	 * 		Cause of this exception
	 */
	public DoubleNameException(String name, String msg, Throwable cause) 
	{
		super(msg, cause);
		this.name = name;
	}

	/**
	 * <p>
	 * Returns the name whose double occurrence cause this exception.
	 * </p>
	 * 
	 * @return
	 * 		Double name
	 */
	public String getDoubleName() 
	{
		return name;
	}
}
