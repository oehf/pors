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

import org.openehealth.pors.database.dao.IOrganisationHasProviderLogDao;
import org.openehealth.pors.database.entities.OrganisationHasProviderLog;


/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.OrganisationHasProviderLog OrganisationHasProviderLog} 
 * objects.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.OrganisationHasProviderLog OrganisationHasProviderLog
 */
public class OrganisationHasProviderLogJpaDao extends
		AbstractJpaDao<Long, OrganisationHasProviderLog> implements
		IOrganisationHasProviderLogDao 
{

	protected OrganisationHasProviderLogJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<OrganisationHasProviderLog> getAll() 
	{
		Query q = this.em.createNamedQuery(
				OrganisationHasProviderLog.QUERY_NAME_ALL);
		
		return q.getResultList();
	}

}
