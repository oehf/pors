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
 * Exception thrown if trying to insert an already existing OID.
 * </p>
 * 
 * @author jr
 *
 */
public class DoubleOidException extends Exception {
	private static final long serialVersionUID = 8946259133053502254L;
	
	private String oid;

	/**
	 * 
	 */
	public DoubleOidException(String oid) 
	{
		this.init(oid);
	}

	/**
	 * @param message
	 */
	public DoubleOidException(String message, String oid) 
	{
		super(message);
		this.init(oid);
	}

	/**
	 * @param cause
	 */
	public DoubleOidException(Throwable cause, String oid) 
	{
		super(cause);
		this.init(oid);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DoubleOidException(String message, Throwable cause, String oid) 
	{
		super(message, cause);
		this.init(oid);
	}

	private void init(String oid)
	{
		this.oid = oid;
	}
	
	public String getOid()
	{
		return this.oid;
	}
}
