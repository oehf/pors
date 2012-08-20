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
 * Exception which is thrown for any problem regarding database transactions.
 * </p>
 * 
 * @author jr
 *
 */
public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8063092631496763617L;

	/**
	 * 
	 */
	public DatabaseException() {}

	/**
	 * @param msg Error message
	 */
	public DatabaseException(String msg) {
		super(msg);
	}

	/**
	 * @param cause Cause of this exception
	 */
	public DatabaseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param msg Error message
	 * @param cause Cause of this exception
	 */
	public DatabaseException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
