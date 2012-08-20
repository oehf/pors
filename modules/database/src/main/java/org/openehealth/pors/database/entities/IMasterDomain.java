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
package org.openehealth.pors.database.entities;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Interface representing a master domain which is used for cumulative inserting 
 * and updating processes in 
 * {@link de.uni_heidelberg.de.ise.pors.database.connector.IDatabaseConnector IDatabaseConnector}.
 * </p>
 * 
 * @author jr
 * @see de.uni_heidelberg.de.ise.pors.database.connector.IDatabaseConnector IDatabaseConnector
 * @see de.uni_heidelberg.de.ise.pors.database.entities.Organisation Organisation
 * @see de.uni_heidelberg.de.ise.pors.database.entities.Provider Provider
 *
 */
public interface IMasterDomain 
{
	/**
	 * <p>
	 * Getter method for the database ID.
	 * </p>
	 * 
	 * @return
	 * 		Database ID
	 */
	Long getId();
	
	/**
	 * <p>
	 * Getter method for the ID of the current session for inserting or 
	 * updating progresses.
	 * </p>
	 * 
	 * @return
	 * 		Session ID
	 */
	String getSessionId();
	
	/**
	 * <p>
	 * Getter method for the IP address of the current user for inserting or 
	 * updating progresses.
	 * </p>
	 * 
	 * @return
	 * 		IP Address
	 */
	String getIpAddress();
	
	/**
	 * <p>
	 * Getter method for the user performing an inserting or updating progress.
	 * </p>
	 * 
	 * @return
	 * 		User editing this master domain
	 */
	PorsUser getEditingUser();
	
	/**
	 * <p>
	 * Setter method for the user performing an inserting or updating progress.
	 * </p>
	 * 
	 * @param user
	 * 		The editing user
	 */
	void setEditingUser(PorsUser user);
	
	/**
	 * <p>
	 * Getter method for the user owning this master domain (should be the 
	 * user who creates/created this master domain).
	 * </p>
	 * 
	 * @return
	 * 		Owning user
	 */
	PorsUser getUser();
	
	/**
	 * <p>
	 * Setter method for the user owning this master domain (should be the user
	 * who creates/created this master domain).
	 * </p>
	 * 
	 * @param user
	 * 		Owning user
	 */
	void setUser(PorsUser user);
	
	/**
	 * <p>
	 * Getter method for the addresses linked to this master domain.
	 * </p>
	 * 
	 * @return
	 * 		Addresses belonging to this master domain
	 */
	List<Address> getAddresses();
	
	/**
	 * <p>
	 * Setter method for the addresses which should be linked to this master 
	 * domain.
	 * </p>
	 * 
	 * @param addresses
	 * 		Addresses which should be linked to this master domain
	 */
	void setAddresses(List<Address> addresses);
	
	/**
	 * <p>
	 * Getter method for the local IDs which belong to this master domain.
	 * </p>
	 * 
	 * @return
	 * 		Local IDs of this master domain
	 */
	List<LocalId> getLocalIds();
	
	/**
	 * <p>
	 * Setter method for local IDs which should be linked to this master 
	 * domain.
	 * </p>
	 * @param localIds
	 * 		Local IDs to link to this master domain
	 */
	void setLocalIds(List<LocalId> localIds);
	
	/**
	 * <p>
	 * Setter method for the last update date of this master domain.
	 * </p>
	 * 
	 * @param lastUpdateDate
	 * 		The date of last update
	 */
	void setLastUpdateDate(Date lastUpdateDate);
	
	/**
	 * <p>
	 * Setter method for information if there where duplicates calculated 
	 * for this master domain.
	 * </p>
	 * 
	 * @param duplicatesCalculated
	 * 		If there where duplicates calculated for this master domain
	 */
	void setDuplicatesCalculated(boolean duplicatesCalculated);
}
