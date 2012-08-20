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

import org.openehealth.pors.database.dao.IAddressDao;
import org.openehealth.pors.database.entities.Address;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IAddressDao IAddressDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IAddressDao IAddressDao
 *
 */
public class AddressJpaDao extends AbstractJpaDao<Long, Address> implements
		IAddressDao 
{
	AddressJpaDao(EntityManager manager)
	{
		super(manager);
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	public List<Address> getAll() 
	{
		@SuppressWarnings("unchecked")
		List<Address> lst = this.em.createNamedQuery(Address.QUERY_NAME_ALL).
				getResultList();
		
		return lst;
	}

	/**
	 * @throws IllegalArgumentException
	 * 		If any of the parameters is null
	 * @see org.openehealth.pors.database.dao.IAddressDao#getByUniqueCombination(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Address getByUniqueCombination(String additional,
			String street, String houseNumber, String zipCode, String city,
			String country) 
	{
		if (additional == null || street == null || houseNumber == null || 
				zipCode == null || city == null || country == null)
		{
			throw new IllegalArgumentException(
					"All parameters have to have a value other than null.");
		}
		
		Query q = this.em.createNamedQuery(
				Address.QUERY_NAME_BY_UNIQUE_COMBINATION);
		
		q.setParameter(Address.PARAM_STREET, street);
		q.setParameter(Address.PARAM_HOUSE_NUMBER, houseNumber);
		q.setParameter(Address.PARAM_ZIP_CODE, zipCode);
		q.setParameter(Address.PARAM_CITY, city);
		q.setParameter(Address.PARAM_COUNTRY, country);
		q.setParameter(Address.PARAM_ADDITIONAL, additional);
		
		try
		{
			return (Address) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IAddressDao#updateDuplicatesCalculated(boolean)
	 */
	@Override
	public void updateDuplicatesCalculated(boolean calculated) {
		Query q = this.em.createNamedQuery(
				Address.QUERY_NAME_UPDATE_ALL_DUPLICATES_CALCULATED);
		q.setParameter(Address.PARAM_CALCULATED, calculated);
		q.executeUpdate();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IAddressDao#updateDuplicatesCalculatedById(long, boolean)
	 */
	@Override
	public void updateDuplicatesCalculatedById(long id, boolean calculated) 
	{
		Query q = this.em.createNamedQuery(
				Address.QUERY_NAME_UPDATE_DUPLICATES_CALCULATED);
		q.setParameter(Address.PARAM_ID, id);
		q.setParameter(Address.PARAM_CALCULATED, calculated);
		q.executeUpdate();
	}
}
