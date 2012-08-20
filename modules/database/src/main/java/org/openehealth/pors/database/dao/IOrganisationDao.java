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
package org.openehealth.pors.database.dao;

import java.util.List;

import org.openehealth.pors.database.entities.Organisation;


/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.Organisation Organisation} 
 * objects.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.Organisation Organisation
 */
public interface IOrganisationDao extends IDao<Long, Organisation> 
{
	/**
	 * <p>
	 * Returns all organisations linked with the provider having the given 
	 * <code>providerId</code>.
	 * </p>
	 * 
	 * @param providerId
	 * 		ID of the provider linked to the organisations
	 * @return
	 * 		Organisations linked to the provider having given ID
	 */
	List<Organisation> getAllForProviderId(Long providerId);
	
	/**
	 * <p>
	 * Selects the organisations linked to the provider having the given 
	 * <code>providerId</code> and owned by the PORS user having the given
	 * <code>userId</code>.
	 * </p>
	 * 
	 * @param providerId
	 * 		ID of the provider linked to the organisations
	 * @param userId
	 * 		ID of the PORS user owning the organisations
	 * @return
	 * 		Organisations owned by given <code>userId</code> and linked to
	 * 		given <code>providerId</code>
	 */
	List<Organisation> getAllForProviderIdOfPorsUserId(Long providerId, Integer userId);
	
	/**
	 * <p>
	 * Selects the organisations linked to the provider having the given 
	 * <code>providerId</code> and owned by the PORS user having the given 
	 * <code>userName</code>.
	 * </p>
	 * 
	 * @param providerId
	 * 		ID of the provider linked to the organisations
	 * @param userName
	 * 		Name of the PORS user owning the organisations
	 * @return
	 * 		Organisations owned by given <code>userName</code> and linked to 
	 * 		given <code>providerId</code>
	 */
	List<Organisation> getAllForProviderIdOfPorsUserName(Long providerId, String userName);
	
	/**
	 * <p>
	 * Returns <code>max</code> organisations starting at 
	 * <code>position</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Start position of the results (beginning with 0)
	 * @return
	 * 		The organisations
	 */
	List<Organisation> getAllMaxResultsFromPosition(int max, int position);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> organisations linked to the provider 
	 * having <code>providerId</code> as ID starting from 
	 * <code>position</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting positions of the resulting list (beginning with 0)
	 * @param providerId
	 * 		ID of the provider the organisations are linked to
	 * @return
	 * 		Corresponding list of organisations
	 */
	List<Organisation> getAllMaxResultsFromPositionForProviderId(int max, 
			int position, Long providerId);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> organisations starting from 
	 * <code>position</code> which are linked to the provider having the given
	 * <code>providerId</code> and being owned by the user having the given 
	 * <code>userId</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the list (beginning with 0)
	 * @param providerId
	 * 		ID of the provider linked to the organisations
	 * @param userId
	 * 		ID of the PORS user owning the organisations
	 * @return
	 * 		The corresponding organisations
	 */
	List<Organisation> getAllMaxResultsFromPositionForProviderIdOfPorsUserId(
			int max, int position, long providerId, int userId);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> organisations starting from 
	 * <code>position</code> which are linked to the provider having the given
	 * <code>providerId</code> and being owned by the user having the given 
	 * <code>userName</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the result list (beginning at 0)
	 * @param providerId
	 * 		ID of the provider linked to the organisations
	 * @param userName
	 * 		Name of the user owning the organisations
	 * @return
	 * 		The corresponding organisations
	 */
	List<Organisation> getAllMaxResultsFromPositionForProviderIdOfPorsUserName(
			int max, int position, long providerId, String userName);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> organisations starting from 
	 * <code>position</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the results (beginning with 0)
	 * @param userId
	 * 		ID of the user owning the organisations
	 * @return
	 * 		The organisations
	 */
	List<Organisation> getAllMaxResultsFromPositionOfPorsUserId(int max, 
			int position, int userId);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> organisations starting from 
	 * <code>position</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Starting position of the results (beginning with 0)
	 * @param userName
	 * 		Name of the user
	 * @return
	 * 		Corresponding list of organisations
	 */
	List<Organisation> getAllMaxResultsFromPositionOfPorsUserName(int max, 
			int position, String userName);
	
	/**
	 * <p>
	 * Selects the organisations owned by the PORS user having given 
	 * <code>userId</code>.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the user owning the organisations
	 * @return
	 * 		Organisations owned by given <code>userId</code>
	 */
	List<Organisation> getAllOfPorsUserId(Integer userId);
	
	/**
	 * <p>
	 * Selects the organisations owned by the PORS user having the given 
	 * <code>name</code>.
	 * </p>
	 * 
	 * @param name
	 * 		Name of the PORS user owning the organisations
	 * @return
	 * 		Organisations owned by the PORS user aoving given <code>name</code>
	 */
	List<Organisation> getAllOfPorsUserName(String name);
	
	/**
	 * <p>
	 * Selects the organisation having the given <code>establishmentId</code>.
	 * </p>
	 * 
	 * @param establishmentId
	 * 		Establishment ID of the organisation
	 * @return
	 * 		Organisation having given establishment ID
	 */
	Organisation getByEstablishmentId(String establishmentId);
	
	/**
	 * <p>
	 * Selects the organisation having <code>establishmentId</code> as 
	 * establishment ID and <b>not</b> having the given 
	 * <code>organisationId</code>. 
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param establishmentId
	 * 		Establishment ID of the organisation
	 * @param organisationId
	 * 		ID which the organisation does <b>not</b> have
	 * @return
	 * 		Matchig organisation
	 */
	Organisation getByEstablishmentIdExcludeOrganisationId(String establishmentId, long organisationId);
	
	/**
	 * <p>
	 * Selects the organisation connected to the local ID <code>localId</code> 
	 * of the <code>application</code> in <code>facility</code>. 
	 * </p>
	 * 
	 * @param localId
	 * 		The local ID
	 * @param application
	 * 		Application the local ID belongs to
	 * @param facility
	 * 		Facility the local ID belongs to
	 * 
	 * @return
	 * 		Organisation connected to given local ID
	 */
	Organisation getByLocalId(String localId, String application, String facility);
	
	/**
	 * <p>
	 * Selects the organisation having the given <code>name</code>.
	 * </p>
	 * 
	 * @param name
	 * 		Name of the organisation
	 * @return
	 * 		Organisation having given <code>name</code>.
	 */
	Organisation getByName(String name);
	
	/**
	 * <p>
	 * Selects the organisation having the given <code>name</code> and 
	 * <b>not</b> having the given <code>organisationId</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param name
	 * 		Name of the organisation
	 * @param organsiationId
	 * 		ID the organisation does <b>not</b> have
	 * @return
	 * 		Matching organisation
	 */
	Organisation getByNameExcludeOrganisationId(String name, long organsiationId);
	
	/**
	 * <p>
	 * Selects the organisation having <code>name</code> as name or second 
	 * name.
	 * </p>
	 * 
	 * @param name
	 * 		Name or second name of the organisation
	 * @return
	 * 		Organisation having <code>name</code> as name or second name
	 */
	Organisation getByNameOrSecondName(String name);
	
	/**
	 * <p>
	 * Selects the organisation having the given <code>oid</code>.
	 * </p>
	 * 
	 * @param oid
	 * 		OID of the organisation
	 * @return
	 * 		Organisation having the given OID
	 */
	Organisation getByOid(String oid);
	
	/**
	 * <p>
	 * Selects the organisation having the given <code>oid</code> and 
	 * <b>not</b> having the given <code>organisationId</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param oid
	 * 		OID of the organisation
	 * @param organisationId
	 * 		ID the organisation does <b>not</b> have
	 * @return
	 * 		Matching organisation
	 */
	Organisation getByOidExcludeOrganisationId(String oid, long organisationId);
	
	/**
	 * <p>
	 * Selects the organisation having the given <code>secondName</code>.
	 * </p>
	 * 
	 * @param secondName
	 * 		Second name of the organisation
	 * @return
	 * 		Organisation having given <code>secondName</code>
	 */
	Organisation getBySecondName(String secondName);
	
	/**
	 * <p>
	 * Selects the organisation having the given <code>secondName</code> and 
	 * <b>not</b> having the given <code>organisationId</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no such organisation exists.
	 * </p>
	 * 
	 * @param secondName
	 * 		Second name of the organisation
	 * @param organisationId
	 * 		ID the organisation does <b>not</b> have
	 * @return
	 * 		Matching organisation
	 */
	Organisation getBySecondNameExcludeOrganisationId(String secondName, long organisationId);
	
	/**
	 * <p>
	 * Selects the total number of organisations.
	 * </p>
	 * 
	 * @return
	 * 		Total number of organisations
	 */
	long getCount();
	
	/**
	 * <p>
	 * Selects the total number of organisations owned by the user having 
	 * <code>userId</code> as ID.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the user whose organisations are counted
	 * @return
	 * 		Total number of organisations owned by the PORS user
	 */
	long getCountByUserId(int userId);
	
	/**
	 * <p>
	 * Checks, if any organisation is owned by the PORS user having the given 
	 * <code>userId</code> and linked to the address having the given 
	 * <code>addressId</code>.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the PORS user
	 * @param addressId
	 * 		ID of the address
	 * @return
	 * 		True, if there exists such an organisation, else false
	 */
	boolean isAnyOwnedByPorsUserIdReferencingAddressId(Integer userId, Long addressId);
	
	/**
	 * <p>
	 * Checks, if any organisation is owned by the PORS user having the given 
	 * <code>userId</code> and linked to the local ID having the given 
	 * <code>localIdId</code>.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the PORS user
	 * @param localIdId
	 * 		ID of the local ID
	 * @return
	 * 		True, if there exists such an organisation, else false
	 */
	boolean isAnyOwnedByPorsUserIdReferencingLocalIdId(Integer userId, Long localIdId);
	
	/**
	 * <p>
	 * Checks, if the organisation having the given <code>organisationId</code>
	 * is linked to the provider having the given <code>providerId</code>.
	 * </p>
	 * 
	 * @param organisationId
	 * 		ID of the organisation
	 * @param providerId
	 * 		ID of the provider
	 * @return
	 * 		True, if organisation an provider are linked to each other, else 
	 * 		false
	 */
	boolean isOrganisationIdRelatedToProviderId(Long organisationId, Long providerId);
	
	/**
	 * <p>
	 * For all addresses, the flag for calculated duplicates is set to 
	 * <code>calculated</code>.
	 * </p>
	 * 
	 * @param calculated
	 * 		True, if duplicates where calculated, else false.
	 */
	void updateDuplicatesCalculated(boolean calculated);
	
	/**
	 * <p>
	 * For the organisation having <code>id</code> as ID, the flag for calculated 
	 * duplicates is set to <code>calculated</code>.
	 * </p>
	 * 
	 * @param id
	 * 		ID of the organisation to update
	 * @param calculated
	 * 		True, if duplicates where calculated, else false.
	 */
	void updateDuplicatesCalculatedById(long id, boolean calculated);
}
