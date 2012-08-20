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

import org.openehealth.pors.database.dao.IDuplicateEntryDao;
import org.openehealth.pors.database.entities.DuplicateEntry;
import org.openehealth.pors.database.entities.DuplicateEntryId;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IDuplicateEntryDao IDuplicateEntryDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IDuplicateEntryDao IDuplicateEntryDao
 *
 */
public class DuplicateEntryJpaDao extends
		AbstractJpaDao<DuplicateEntryId, DuplicateEntry> implements
		IDuplicateEntryDao {

	protected DuplicateEntryJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see {@link org.openehealth.pors.database.dao.IDao IDao}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DuplicateEntry> getAll() 
	{
		return this.em.createNamedQuery(
				DuplicateEntry.QUERY_NAME_ALL).getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDuplicateEntryDao#getAllMaxResultsFromPosition(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DuplicateEntry> getAllMaxResultsFromPosition(int max,
			int position) 
	{
		Query q = this.em.createNamedQuery(DuplicateEntry.QUERY_NAME_ALL);
		
		q.setFirstResult(position);
		q.setMaxResults(max);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDuplicateEntryDao#getCount()
	 */
	@Override
	public long getCount() 
	{
		Query q = this.em.createNamedQuery(
				DuplicateEntry.QUERY_NAME_COUNT_ALL);
		
		Number n = (Number) q.getSingleResult();
		
		return n.longValue();
	}

}
