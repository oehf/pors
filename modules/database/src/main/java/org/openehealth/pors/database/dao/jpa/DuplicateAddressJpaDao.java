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

import org.openehealth.pors.database.dao.IDuplicateAddressDao;
import org.openehealth.pors.database.entities.DuplicateAddress;
import org.openehealth.pors.database.entities.DuplicateAddressId;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IAddressDao IDuplicateAddressDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IDuplicateAddressDao IDuplicateAddressDao
 *
 */
public class DuplicateAddressJpaDao extends 
	AbstractJpaDao<DuplicateAddressId, DuplicateAddress> 
	implements IDuplicateAddressDao 
{
	protected DuplicateAddressJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	@Override
	public List<DuplicateAddress> getAll() 
	{
		@SuppressWarnings("unchecked")
		List<DuplicateAddress> lst = this.em.createNamedQuery(DuplicateAddress.QUERY_NAME_ALL).
				getResultList();
		
		return lst;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDuplicateAddressDao#deleteByAddressId(Long)
	 */
	@Override
	public void deleteByAddressId(Long id) 
	{
		Query q = this.em.
			createNamedQuery(DuplicateAddress.QUERY_NAME_DELETE_BY_ADDRESS_ID);
		q.setParameter(DuplicateAddress.PARAM_ADDRESS_ID, id);
		q.executeUpdate();
	}
}
