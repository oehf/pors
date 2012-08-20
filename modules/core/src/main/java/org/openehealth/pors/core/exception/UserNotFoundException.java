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
 * The user can't be found in the database
 * @author ms
 */
public class UserNotFoundException extends Exception{

	private static final long serialVersionUID = -4189427074409020649L;

	/**
	 * Creates a new UserNotFoundException
	 * @param s the message
	 */
	public UserNotFoundException(String s)
	{
		super (s);
	}
}
