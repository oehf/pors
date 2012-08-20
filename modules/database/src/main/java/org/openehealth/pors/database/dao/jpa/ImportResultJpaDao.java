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
package org.openehealth.pors.database.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.openehealth.pors.database.dao.IImportResultDao;
import org.openehealth.pors.database.entities.ImportResult;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IImportResultDao IImportResultDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author mf
 * @see org.openehealth.pors.database.dao.IImportResultDao IImportResultDao
 *
 */
public class ImportResultJpaDao extends AbstractJpaDao<Long, ImportResult> implements IImportResultDao {

	protected ImportResultJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openehealth.pors.pors.database.dao.IDao#getAll()
	 */
	public List<ImportResult> getAll() 
	{
		@SuppressWarnings("unchecked")
		List<ImportResult> results = this.em.
				createNamedQuery(ImportResult.QUERY_NAME_ALL).getResultList();
		return results;
	}

}
