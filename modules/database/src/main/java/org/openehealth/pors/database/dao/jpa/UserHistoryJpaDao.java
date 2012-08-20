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

import org.openehealth.pors.database.dao.IUserHistoryDao;
import org.openehealth.pors.database.entities.UserHistory;
import org.openehealth.pors.database.entities.UserHistoryId;
import org.openehealth.pors.database.exception.UnsupportedActionException;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IUserHistoryDao IUserHistoryDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IUserHistoryDao IUserHistoryDao
 *
 */
public class UserHistoryJpaDao extends
		AbstractJpaDao<UserHistoryId, UserHistory> implements IUserHistoryDao 
{
	/**
	 * 
	 * @param manager
	 */
	protected UserHistoryJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<UserHistory> getAll() 
	{
		Query q = this.em.createNamedQuery("getUserHistoryList");
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IUserHistoryDao#getAllByUserId(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserHistory> getAllByUserId(Integer userId) 
	{
		final Query q = this.em.createNamedQuery(
				"getUserHistoryByUserId");
		
		q.setParameter(1, userId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.jpa.AbstractJpaDao#delete(java.lang.Object)
	 * @throws UnsupportedActionException
	 * 		Always!
	 */
	@Override
	public void delete(UserHistory history)
	{
		throw new UnsupportedActionException(
				"Cannot delete entity from database view. Use source entities instead."
				);
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.jpa.AbstractJpaDao#insert(java.lang.Object)
	 * @throws UnsupportedActionException
	 * 		Always!
	 */
	@Override
	public void insert(UserHistory history)
	{
		throw new UnsupportedActionException(
				"Can not insert entity in database view. Use source entities instead."
				);
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.jpa.AbstractJpaDao#update(java.lang.Object)
	 * @throws UnsupportedActionException
	 * 		Always!
	 */
	@Override
	public void update(UserHistory history)
	{
		throw new UnsupportedActionException(
				"Can not update entity in database view. Use source entities instead."
				);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IUserHistoryDao#getAllMaxResultsFromPositionByUserId(int, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserHistory> getAllMaxResultsFromPositionByUserId(int max,
			int position, int userId) 
	{
		final Query q = this.em.createNamedQuery(
				"getUserHistoryByUserId");
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(1, userId);
		
		return q.getResultList();
	}
}
