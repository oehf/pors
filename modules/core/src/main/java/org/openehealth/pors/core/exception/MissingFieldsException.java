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
package org.openehealth.pors.core.exception;

/**
 * There are missing fields in the HL7-Message
 * @author ms
 */
public class MissingFieldsException extends Exception{

	private static final long serialVersionUID = -2623029406565156197L;

	/**
	 * Creates a new MissingFieldsException
	 * @param s the message
	 */
	public MissingFieldsException(String s)
	{
		super(s);
	}
	
}
