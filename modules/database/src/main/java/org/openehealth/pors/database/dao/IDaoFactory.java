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

import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * <p>
 * Interface representing a factory for accessing all available DAO objects.
 * </p>
 * 
 * @author jr
 *
 */
@Local
public interface IDaoFactory 
{
	/**
	 * <p>
	 * Returns an address DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IAddressDao getAddressDao();
	
	/**
	 * <p>
	 * Returns an address log DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IAddressLogDao getAddressLogDao();
	
	/**
	 * <p>
	 * Returns a duplicate address DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IDuplicateAddressDao getDuplicateAddressDao();
	
	/**
	 * <p>
	 * Returns a duplicate entry DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IDuplicateEntryDao getDuplicateEntryDao();
	
	/**
	 * <p>
	 * Returns a duplicate organisation DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IDuplicateOrganisationDao getDuplicateOrganisationDao();
	
	/**
	 * <p>
	 * Returns a duplicate provider DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IDuplicateProviderDao getDuplicateProviderDao();
	
	/**
	 * <p>
	 * Returns a duplicate recognition DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IDuplicateRecognitionDao getDuplicateRecognitionDao();
	
	/**
	 * <p>
	 * Getter method for a history DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IHistoryDao getHistoryDao();
	
	/**
	 * <p>
	 * Getter method for a local ID DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	ILocalIdDao getLocalIdDao();
	
	/**
	 * <p>
	 * Getter method for a local ID log DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	ILocalIdLogDao getLocalIdLogDao();
	
	/**
	 * <p>
	 * Getter method for an organisation DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IOrganisationDao getOrganisationDao();
	
	/**
	 * <p>
	 * Getter method for an organisation has address DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IOrganisationHasAddressLogDao getOrganisationHasAddressLogDao();
	
	/**
	 * <p>
	 * Getter method for an organisation has provider DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IOrganisationHasProviderLogDao getOrganisationHasProviderLogDao();
	
	/**
	 * <p>
	 * Getter method for an organisation log DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IOrganisationLogDao getOrganisationLogDao();
	
	/**
	 * <p>
	 * Getter method for an PORS user DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IPorsUserDao getPorsUserDao();
	
	/**
	 * <p>
	 * Getter method for a provider has address log DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IProviderHasAddressLogDao getProviderHasAddressLogDao();
	
	/**
	 * <p>
	 * Getter method for a provider DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IProviderDao getProviderDao();
	
	/**
	 * <p>
	 * Getter method for a provider log DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IProviderLogDao getProviderLogDao();
	
	/**
	 * <p>
	 * Getter method for an user history DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IUserHistoryDao getUserHistoryDao();
	
	/**
	 * <p>
	 * Getter method for an import result DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IImportResultDao getImportResultDao();
	
	/**
	 * <p>
	 * Getter method for an user role DAO.
	 * </p>
	 * 
	 * @return
	 * 		The DAO
	 */
	IUserRoleDao getUserRoleDao();
	
	/**
	 * <p>
	 * Gets the entity manager of this DAO factory, if any exists. Else
	 * returns <code>null</code>.
	 * </p>
	 * 
	 * @return
	 * 		Entity manager of this DAO factory or <code>null</code> if none exists
	 */
	EntityManager getEntityManager();
}
