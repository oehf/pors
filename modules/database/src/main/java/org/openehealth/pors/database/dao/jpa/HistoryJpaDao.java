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

import org.openehealth.pors.database.dao.IHistoryDao;
import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.HistoryId;
import org.openehealth.pors.database.exception.UnsupportedActionException;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IHistoryDao IHistoryDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IHistoryDao IHistoryDao
 *
 */
public class HistoryJpaDao extends AbstractJpaDao<HistoryId, History> implements
		IHistoryDao 
{

	protected HistoryJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see org.openehealth.pors.database.dao.jpa.AbstractJpaDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(History history)
	{
		throw new UnsupportedActionException(
				"Cannot delete entity from database view. Use source entities instead."
				);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	public List<History> getAll() 
	{
		Query q = em.createNamedQuery(History.QUERY_NAME_ALL);
		
		@SuppressWarnings("unchecked")
		List<History> lst = q.getResultList();
		
		return lst;
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IHistoryDao#getAllMaxResultsFromPosition(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<History> getAllMaxResultsFromPosition(int max, int position) 
	{
		Query q = this.em.createNamedQuery(History.QUERY_NAME_ALL);
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		return q.getResultList();
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IHistoryDao#getCount()
	 */
	@Override
	public long getCount() 
	{
		Query q = this.em.createNamedQuery(History.QUERY_NAME_COUNT_ALL);
		
		Number n = (Number) q.getSingleResult();
		
		return n.longValue();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IHistoryDao#getCountByUserId(int)
	 */
	@Override
	public long getCountByUserId(int userId) 
	{
		final Query q = this.em.createNamedQuery(
				History.QUERY_NAME_COUNT_BY_USER_ID);
		
		Number n = (Number) q.getSingleResult();
		
		return n.longValue();
	}

	/**
	 * @see org.openehealth.pors.database.dao.jpa.AbstractJpaDao#insert(java.lang.Object)
	 */
	@Override
	public void insert(History history)
	{
		throw new UnsupportedActionException(
				"Can not insert entity in database view. Use source entities instead."
				);
	}

	/**
	 * @see org.openehealth.pors.database.dao.jpa.AbstractJpaDao#update(java.lang.Object)
	 */
	@Override
	public void update(History history)
	{
		throw new UnsupportedActionException(
				"Can not update entity in database view. Use source entities instead."
				);
	}
}
