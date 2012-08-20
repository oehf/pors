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
package org.openehealth.pors.auth;

import java.util.List;

import javax.ejb.Local;

import org.openehealth.pors.core.common.Permission;
import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.exception.AuthentificationException;
import org.openehealth.pors.core.exception.MissingFieldsException;
import org.openehealth.pors.core.exception.MissingRightsException;
import org.openehealth.pors.core.exception.UserNotFoundException;
import org.openehealth.pors.database.entities.PorsUser;

/**
 * Is called when a user logs in for validating the login parameters and is
 * responsible for comparing the tasks a user wants to perform with the
 * permissions granted.
 **/
@Local
public interface IAuthentificator {
	/**
	 * Receives a PorsUser object, which contains the user name and password. The
	 * method checks if the user exists and if the password is correct.
	 * Otherwise it throws a specific exception
	 * 
	 * @param PorsUser
	 *            the user, who wants to login
	 * @throws AuthentificationException
	 *             is thrown if the password or username is incorrect
	 * @throws MissingFieldsException
	 *             if there are fields missing in the user object
	 **/
	public PorsUser authUser(PorsUser user) throws AuthentificationException, MissingFieldsException;

	/**
	 * Receives a PorsUser object and a task the user wants to perform. After
	 * querying the DatabaseManager for the permissions of the user, the method
	 * checks if there is a permission for fullfilling the task.
	 * 
	 * @param PorsUser
	 *            the user who wants to perform the task
	 * @param Task
	 *            containing action and domain.
	 * @throws MissingRightsException
	 *             is thrown if the user isn't allowed to perform the task
	 **/
	public void checkRights(PorsUser user, Task task)
			throws MissingRightsException;


	/**
	 * Returns a list of permissions for a given user.
	 * 
	 * @param user
	 *            the user for which the permissions are requested
	 * @return a list of permissions for the user
	 * @throws MissingFieldsException
	 *             if there are missing fields for the user
	 * @throws UserNotFoundException
	 *             if the user cannot be found
	 */
	public List<Permission> getPermissions(PorsUser user)
			throws MissingFieldsException, UserNotFoundException;
	
	/**
	 * <p>
	 * Returns the hash string of <code>inputString</code>.
	 * </p>
	 * 
	 * @param inputString
	 * 		String to hash
	 * 		
	 */
	String getHashString(String inputString);
	
	String getLdapDn(String username);
	
	boolean isLdapMode();
}
