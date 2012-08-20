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

import org.openehealth.pors.database.entities.LocalId;

/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.LocalId LocalId} 
 * objects.
 * </p>
 * 
 * @author jr
 * 
 * @see org.openehealth.pors.database.entities.LocalId LocalId
 */
public interface ILocalIdDao extends IDao<Long, LocalId>
{
	/**
	 * <p>
	 * Selects the local id composed of the unique combination of a 
	 * <code>localId</code> and the <code>facility</code> as well as the 
	 * <code>application</code> where this <code>localId</code> is designated 
	 * to.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no matching local id could be found
	 * </p>
	 * 
	 * @param localId
	 * 		Local id to search for
	 * @param facility
	 * 		Facility to define a unique local id
	 * @param application
	 * 		Application to define a unique local id
	 * @return Local id defined by the given three attributes
	 */
	LocalId getByUniqueCombination(String localId, String facility, 
			String application);
}
