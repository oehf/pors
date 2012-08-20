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
package org.openehealth.pors.database.connector;

import java.util.List;

import javax.ejb.Local;

import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.exception.SearchException;
import org.openehealth.pors.database.util.SearchCriteria;

/**
 * <p>
 * Provides methods for searching entities by several criteria.
 * </p>
 * 
 * @author jr
 *
 */
@Local
public interface IEntitySearcher 
{
	/**
	 * <p>
	 * Searches for an organisation by the given list of <code>criteria</code> 
	 * which are concatenated by the given <code>operator</code>.
	 * </p>
	 * 
	 * @param criteria
	 * 		List of search criteria
	 * @param operator
	 * 		Operator for concatenating all criteria
	 * @return
	 * 		List of searching results
	 * @throws SearchException
	 * 		If any of the input values is invalid
	 */
	List<Organisation> searchOrganisation(List<SearchCriteria> criteria, String operator) throws SearchException;
	
	/**
	 * <p>
	 * Searches for a provider by the given list of <code>criteria</code> which 
	 * are concatenated by the given <code>operator</code>.
	 * </p>
	 * 
	 * @param criteria
	 * 		List of search criteria
	 * @param operator
	 * 		Operator for concatenating all criteria
	 * @return
	 * 		List of searching results
	 * @throws SearchException
	 * 		If any of the input values is invalid
	 */
	List<Provider> searchProvider(List<SearchCriteria> criteria, String operator) throws SearchException;
	
	/**
	 * <p>
	 * Searches for a provider by the given list of <code>criteria</code> which 
	 * are concatenated by the given <code>operator</code>.
	 * </p>
	 * 
	 * @param criteria
	 * 		List of search criteria
	 * @param operator
	 * 		Operator for concatenating all criteria
	 * @return
	 * 		List of searching results
	 * @throws SearchException
	 * 		If any of the input values is invalid
	 */
	List<History> searchLoggingEntry(List<SearchCriteria> criteria, String operator) throws SearchException;
	
	void rebuildSearchindex();
}
