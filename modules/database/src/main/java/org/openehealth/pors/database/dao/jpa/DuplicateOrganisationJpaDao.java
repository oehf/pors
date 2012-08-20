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

import org.openehealth.pors.database.dao.IDuplicateOrganisationDao;
import org.openehealth.pors.database.entities.DuplicateOrganisation;
import org.openehealth.pors.database.entities.DuplicateOrganisationId;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IDuplicateOrganisationDao IDuplicateOrganisationDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IDuplicateOrganisationDao IDuplicateOrganisationDao
 *
 */
public class DuplicateOrganisationJpaDao extends
		AbstractJpaDao<DuplicateOrganisationId, DuplicateOrganisation>
		implements IDuplicateOrganisationDao 
{
	protected DuplicateOrganisationJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	@Override
	public List<DuplicateOrganisation> getAll() 
	{
		@SuppressWarnings("unchecked")
		List<DuplicateOrganisation> lst = 
				this.em.createNamedQuery(DuplicateOrganisation.QUERY_NAME_ALL).
				getResultList();
		
		return lst;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDuplicateOrganisationDao#deleteByOrganisationId(long)
	 */
	@Override
	public void deleteByOrganisationId(long organisationId) 
	{
		Query q = this.em.createNamedQuery(
				DuplicateOrganisation.QUERY_NAME_DELETE_BY_ORGANISATION_ID);
		
		q.setParameter(DuplicateOrganisation.PARAM_ORGANISATION_ID, 
				organisationId);
		
		q.executeUpdate();
	}
}
