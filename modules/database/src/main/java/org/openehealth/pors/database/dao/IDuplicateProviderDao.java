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

import org.openehealth.pors.database.entities.DuplicateProvider;
import org.openehealth.pors.database.entities.DuplicateProviderId;

/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.DuplicateProvider DuplicateProvider} 
 * objects.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.DuplicateProvider DuplicateProvider
 */
public interface IDuplicateProviderDao extends
		IDao<DuplicateProviderId, DuplicateProvider> 
{
	/**
	 * <p>
	 * Deletes all entries where <code>id</code> is associated either as first
	 * or as second provider id.
	 * </p>
	 * 
	 * @param id Id of the provider where associated entries should be deleted
	 */
	void deleteByProviderId(Long id);
}
