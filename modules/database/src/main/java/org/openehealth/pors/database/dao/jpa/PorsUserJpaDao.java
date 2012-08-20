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

import org.openehealth.pors.database.dao.IPorsUserDao;
import org.openehealth.pors.database.entities.PorsUser;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IPorsUserDao IPorsUserDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IPorsUserDao IPorsUserDao
 *
 */
public class PorsUserJpaDao extends AbstractJpaDao<Integer, PorsUser> implements
		IPorsUserDao {

	PorsUserJpaDao(EntityManager manager)
	{
		super(manager);
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	public List<PorsUser> getAll() 
	{
		@SuppressWarnings("unchecked")
		List<PorsUser> lst = this.em.createNamedQuery(PorsUser.QUERY_NAME_ALL).
				getResultList();
		
		return lst;
	}

	/**
	 * @throws IllegalArgumentException
	 * 		If <code>name</code> is null
	 * @see org.openehealth.pors.database.dao.IPorsUserDao#getByName(java.lang.String)
	 */
	public PorsUser getByName(String name) 
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name must not be null.");
		}
		
		Query q = this.em.createNamedQuery(PorsUser.QUERY_NAME_BY_NAME);
		q.setParameter(PorsUser.PARAM_NAME, name);
		
		try
		{
			return (PorsUser) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

}
