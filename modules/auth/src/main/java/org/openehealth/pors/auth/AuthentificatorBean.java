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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import org.openehealth.pors.core.common.HashUtil;
import org.openehealth.pors.core.common.Permission;
import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.exception.AuthentificationException;
import org.openehealth.pors.core.exception.MissingFieldsException;
import org.openehealth.pors.core.exception.MissingRightsException;
import org.openehealth.pors.core.exception.UserNotFoundException;
import org.openehealth.pors.database.connector.IDatabaseConnector;
import org.openehealth.pors.database.entities.PermissionMapping;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.entities.UserRole;
import org.openehealth.pors.database.exception.DatabaseException;

/**
 * Implementation of IAuthentificator
 * 
 * @see IAuthentificator
 * @author ck, ms, mf
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AuthentificatorBean implements IAuthentificator {
	
	private static final String PORS_AUTH_MODE = "pors.authmode";
	private static final String AUTH_MODE_DB = "db";
	private static final String AUTH_MODE_LDAP = "ldap";
	private static final String LDAP_BASE_DN = "pors.ldap.basedn";
	private static final String LDAP_IP = "pors.ldap.ip";
	private static final String LDAP_PORT = "pors.ldap.port";
	private static final String LDAP_ADMINGROUP = "pors.ldap.admingroup";
	private static final String LDAP_USERGROUP = "pors.ldap.usergroup";
	
	private static final String DB_ADMIN_ROLE = "admin";
	private static final String DB_USER_ROLE = "user";
	

	@EJB
	private IDatabaseConnector databasemanager;
	
	private Logger logger;
	private String ldapUserGroup;
	private String ldapAdminGroup;
	private String ldapBase;
	private String ldapIp;
	private String ldapPort;
	private String porsAuthMode;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void initAuthentificatorBean() throws Exception {
		initLogging();
		loadAuthSettings();
	}

	private void initLogging() {
		logger = Logger.getLogger(AuthentificatorBean.class);
	}
	
	private void loadAuthSettings() throws Exception {
		Properties authProperties = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("auth.properties");
		authProperties.load(in);
		
		porsAuthMode = authProperties.getProperty(PORS_AUTH_MODE);
		ldapUserGroup = authProperties.getProperty(LDAP_USERGROUP);
    	ldapAdminGroup = authProperties.getProperty(LDAP_ADMINGROUP);
    	ldapBase = authProperties.getProperty(LDAP_BASE_DN);
    	ldapIp = authProperties.getProperty(LDAP_IP);
    	ldapPort = authProperties.getProperty(LDAP_PORT);
	}

	/**
	 * @see IAuthentificator#authUser(PorsUser)
	 */
	public PorsUser authUser(PorsUser user) throws AuthentificationException, MissingFieldsException {
		logger.info("Requesting user: " + user.getName());
		if (user.getName() == null || user.getName().length() == 0
				|| user.getPassword() == null) {
			throw new MissingFieldsException(
					"username and password is required!");
		}
		
		if (!isLdapMode()) {
			return authUserDB(user);
		} else {
			return authUserLDAP(user);
		}
	}

	/**
	 * @see IAuthentificator#checkRights(PorsUser,Task)
	 */
	public void checkRights(PorsUser user, Task task)
			throws MissingRightsException {
		PorsUser myUser = databasemanager.getUser(user);
		UserRole role = myUser.getRole();
		if (!role.hasRightForDomain(task.getActionString(),
				task.getDomainString())) {
			throw new MissingRightsException("missing rights");
		}
	}

	/**
	 * @see IAuthentificator#getPermissions(PorsUser)
	 */
	public List<Permission> getPermissions(PorsUser user)
			throws MissingFieldsException, UserNotFoundException {
		if (user.getName() == null || user.getName().length() == 0) {
			throw new MissingFieldsException("username is required!");
		}
		PorsUser requestUser = databasemanager.getUser(user);
		if (requestUser == null) {
			throw new UserNotFoundException("requested user was not found!");
		}
		List<Permission> permissions = new ArrayList<Permission>();
		List<PermissionMapping> mappings = requestUser.getRole()
				.getPermissions();
		for (PermissionMapping mapping : mappings) {
			String right = mapping.getRight().getName();
			String domain = mapping.getDomain().getName();
			Task task = new Task(right, domain);
			permissions.add(new Permission(task.getAction(), task.getDomain()));
		}

		return permissions;
	}

	/**
	 * @see IAuthentificator#getHashString(String)
	 */
	public String getHashString(String inputString) {
		return HashUtil.createShaHashHexString(inputString);
	}
	
	public String getLdapDn(String username) {
		return "cn=" + username + "," + ldapBase;
	}
	
	public boolean isLdapMode() {
		if (porsAuthMode.equalsIgnoreCase(AUTH_MODE_LDAP)) {
			return true;
		} else if (porsAuthMode.equalsIgnoreCase(AUTH_MODE_DB)) {
			return false;
		}
		throw new RuntimeException("Unknown auth mode! Known modes: " + AUTH_MODE_DB + " and " + AUTH_MODE_LDAP);
	}
	
	private PorsUser authUserDB(PorsUser user) throws AuthentificationException {
		PorsUser userdata = databasemanager.getUser(user);
		if (userdata == null) {
			logger.info("Database returned null object");
			throw new AuthentificationException("User not found");
		} else if (!userdata.getPassword().equals(
				HashUtil.createShaHashHexString(user.getPassword()))) {
			throw new AuthentificationException("Password incorrect");
		}
		return userdata;
	}
	
	private PorsUser authUserLDAP(PorsUser user) throws AuthentificationException {
		Hashtable<String,String> authEnv = new Hashtable<String,String>();
    	
    	String dn = getLdapDn(user.getName());
    	String ldapURL = "ldap://" + ldapIp + ":" + ldapPort;
    	
    	authEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
   		authEnv.put(Context.PROVIDER_URL, ldapURL);
   		authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
   		authEnv.put(Context.SECURITY_PRINCIPAL, dn);
   		authEnv.put(Context.SECURITY_CREDENTIALS, user.getPassword());
   		
   		String filter = "(uniqueMember=" + dn + ")";
    	try {
    		DirContext authContext = new InitialDirContext(authEnv);
    		SearchControls ctls = new SearchControls();
    		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    		NamingEnumeration<SearchResult> adminQuery = authContext.search(ldapAdminGroup, filter, ctls);
    		String roleName = null;
    		if (adminQuery.hasMore()) {
    			roleName = DB_ADMIN_ROLE;
    		} else {
    			NamingEnumeration<SearchResult> userQuery = authContext.search(ldapUserGroup, filter, ctls);
    			if (userQuery.hasMore()) {
    				roleName = DB_USER_ROLE;
    			}
    		}
    		
    		if (roleName == null) {
    			throw new AuthenticationException("No role found for the given user");
    		}
    		
    		user.setName(dn);
    		PorsUser tmpUser = databasemanager.getUser(user);
    		if (tmpUser == null) {
    			user.setPassword(getHashString(user.getPassword()));
    			user.setActive(true);
    			List<UserRole> roles = databasemanager.getUserRoleList();
    			for (UserRole userRole : roles) {
					if (userRole.getName().equals(roleName)) {
						user.setRole(userRole);
					}
				}
    			if (user.getRole() == null) {
    				throw new AuthenticationException("Database is missing the role: " + roleName);
    			}
    			databasemanager.addPorsUser(user);
    			return databasemanager.getUser(user);
    		}
    		return tmpUser;
    	} catch (AuthenticationException e) {
    		throw new AuthentificationException("Username or password was incorrect!");
    	} catch (NamingException e) {
    		throw new AuthentificationException("LDAP lookup failed, error was: " + e.getMessage());
    	} catch (DatabaseException e) {
    		throw new AuthentificationException("Could not add LDAP user to PORS database: " + e.getMessage());
    	}
	}
	
}
