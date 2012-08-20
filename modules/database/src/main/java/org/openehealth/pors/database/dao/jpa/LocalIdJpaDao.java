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
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.openehealth.pors.database.dao.ILocalIdDao;
import org.openehealth.pors.database.entities.LocalId;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.ILocalIdDao ILocalIdDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.ILocalIdDao ILocalIdDao
 *
 */
public class LocalIdJpaDao extends AbstractJpaDao<Long, LocalId> implements ILocalIdDao 
{
	protected LocalIdJpaDao(EntityManager manager)
	{
		super(manager);
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	public List<LocalId> getAll()
	{
		@SuppressWarnings("unchecked")
		List<LocalId> lst = this.em.createNamedQuery(LocalId.QUERY_NAME_ALL).
			getResultList();
		
		return lst;
	}
	
	/**
	 * @throws IllegalArgumentException
	 * 		If any of the parameters is null
	 * @see org.openehealth.pors.database.dao.ILocalIdDao#getByUniqueCombination(String, String, String)
	 */
	public LocalId getByUniqueCombination(String localId, String facility, 
			String application)
	{
		if (localId == null || facility == null || application == null)
		{
			throw new IllegalArgumentException(
					"All parameters have to have a value other than null.");
		}
		
		Query q = this.em.createNamedQuery(LocalId.QUERY_NAME_BY_UNIQUE_COMBINATION);
		
		q.setParameter(LocalId.PARAM_LOCALID, localId);
		q.setParameter(LocalId.PARAM_FACILITY, facility);
		q.setParameter(LocalId.PARAM_APPLICATION, application);
		
		try
		{
			return (LocalId) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}
}
