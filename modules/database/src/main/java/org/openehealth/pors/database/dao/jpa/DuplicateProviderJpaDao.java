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
import javax.persistence.Query;

import org.openehealth.pors.database.dao.IDuplicateProviderDao;
import org.openehealth.pors.database.entities.DuplicateProvider;
import org.openehealth.pors.database.entities.DuplicateProviderId;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IDuplicateProviderDao IDuplicateProviderDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IDuplicateProviderDao IDuplicateProviderDao
 *
 */
public class DuplicateProviderJpaDao extends
		AbstractJpaDao<DuplicateProviderId, DuplicateProvider> implements
		IDuplicateProviderDao 
{

	protected DuplicateProviderJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see {@link org.openehealth.pors.database.dao.IDao IDao}
	 */
	@Override
	public List<DuplicateProvider> getAll() 
	{
		@SuppressWarnings("unchecked")
		List<DuplicateProvider> lst = 
				this.em.createNamedQuery(DuplicateProvider.QUERY_NAME_ALL).
				getResultList();
		
		return lst;
	}

	/**
	 * @see {@link org.openehealth.pors.database.dao.IDuplicateProviderDao IDuplicateProviderDao}
	 */
	@Override
	public void deleteByProviderId(Long id) 
	{
		Query q = this.em.
			createNamedQuery(DuplicateProvider.QUERY_NAME_DELETE_BY_PROVIDER_ID);
		q.setParameter(DuplicateProvider.PARAM_PROVIDER_ID, id);
		q.executeUpdate();
	}
}
