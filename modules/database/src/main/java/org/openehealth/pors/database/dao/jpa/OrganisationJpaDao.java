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

import org.openehealth.pors.database.dao.IOrganisationDao;
import org.openehealth.pors.database.entities.Organisation;


/**
 * <p>
 * Concrete implementation of the 
 * {@link org.openehealth.pors.database.dao.IOrganisationDao IOrganisationDao} 
 * interface using the standard JPA framework for data access.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.dao.IOrganisationDao IOrganisationDao
 *
 */
public class OrganisationJpaDao extends AbstractJpaDao<Long, Organisation>
		implements IOrganisationDao 
{

	/**
	 * @param manager
	 */
	public OrganisationJpaDao(EntityManager manager) 
	{
		super(manager);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDao#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAll() 
	{
		return this.em.createNamedQuery(Organisation.QUERY_NAME_ALL).
				getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllForProviderId(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllForProviderId(final Long providerId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_PROVIDER_ID);
		
		q.setParameter(Organisation.PARAM_PROVIDER_ID, providerId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllForProviderIdOfPorsUserId(java.lang.Long, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllForProviderIdOfPorsUserId(
			final Long providerId, final Integer userId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_PROVIDER_ID_AND_USER_ID);
		
		q.setParameter(Organisation.PARAM_PROVIDER_ID, providerId);
		q.setParameter(Organisation.PARAM_USER_ID, userId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllForProviderIdOfPorsUserName(java.lang.Long, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Organisation> getAllForProviderIdOfPorsUserName(
			final Long providerId, final String userName) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_PROVIDER_ID_AND_USER_NAME);
		
		q.setParameter(Organisation.PARAM_PROVIDER_ID, providerId);
		q.setParameter(Organisation.PARAM_USER_NAME, userName);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllMaxResultsFromPosition(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllMaxResultsFromPosition(final int max,
			final int position) 
	{
		final Query q = this.em.createNamedQuery(Organisation.QUERY_NAME_ALL);
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllMaxResultsFromPositionForProviderId(int, int, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllMaxResultsFromPositionForProviderId(
			final int max, final int position, final Long providerId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_PROVIDER_ID);
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(Organisation.PARAM_PROVIDER_ID, providerId);
		
		return q.getResultList();
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllMaxResultsFromPositionForProviderIdOfPorsUserId(int, int, long, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllMaxResultsFromPositionForProviderIdOfPorsUserId(
			final int max, final int position, final long providerId, 
			final int userId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_PROVIDER_ID_AND_USER_ID);
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(Organisation.PARAM_PROVIDER_ID, providerId);
		q.setParameter(Organisation.PARAM_USER_ID, userId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllMaxResultsFromPositionForProviderIdOfPorsUserName(int, int, long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllMaxResultsFromPositionForProviderIdOfPorsUserName(
			final int max, final int position, final long providerId, 
			final String userName) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_PROVIDER_ID_AND_USER_NAME);
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(Organisation.PARAM_PROVIDER_ID, providerId);
		q.setParameter(Organisation.PARAM_USER_NAME, userName);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllMaxResultsFromPositionOfPorsUserId(int, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllMaxResultsFromPositionOfPorsUserId(
			final int max, final int position, final int userId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_USER_ID);
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(Organisation.PARAM_USER_ID, userId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllMaxResultsFromPositionOfPorsUserName(int, int, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllMaxResultsFromPositionOfPorsUserName(
			final int max, final int position, final String userName) 
	{
		final Query q = em.createNamedQuery(
				Organisation.QUERY_NAME_BY_USER_NAME);
		q.setMaxResults(max);
		q.setFirstResult(position);
		
		q.setParameter(Organisation.PARAM_USER_NAME, userName);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllOfPorsUserId(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllOfPorsUserId(final Integer userId) 
	{
		final Query q = em.createNamedQuery(
				Organisation.QUERY_NAME_BY_USER_ID);
		
		q.setParameter(Organisation.PARAM_USER_ID, userId);
		
		return q.getResultList();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getAllOfPorsUserName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organisation> getAllOfPorsUserName(final String name) 
	{
		final Query q = em.createNamedQuery(Organisation.QUERY_NAME_BY_USER_NAME);
		
		q.setParameter(Organisation.PARAM_USER_NAME, name);
		
		return q.getResultList();
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByEstablishmentId(java.lang.String)
	 */
	@Override
	public Organisation getByEstablishmentId(final String establishmentId) 
	{
		if (establishmentId == null)
		{
			throw new IllegalArgumentException(
					"Establishment ID must not be null.");
		}
		
		final Organisation dbOrganisation;
		
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_ESTABLISHMENT_ID);
		
		q.setParameter(Organisation.PARAM_ESTABLISHMENT_ID, establishmentId);
		
		try
		{
			dbOrganisation = (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
		
		return dbOrganisation;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByEstablishmentIdExcludeOrganisationId(java.lang.String, long)
	 */
	@Override
	public Organisation getByEstablishmentIdExcludeOrganisationId(
			final String establishmentId, final long organisationId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_ESTABLISHMENT_ID_EXCLUDE_ORGANISATION_ID
				);
		
		q.setParameter(Organisation.PARAM_ESTABLISHMENT_ID, establishmentId);
		q.setParameter(Organisation.PARAM_ORGANISATION_ID, organisationId);
		
		final Organisation result;
		
		try
		{
			result = (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
		
		return result;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByLocalId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Organisation getByLocalId(final String localId, 
			final String application, final String facility) 
	{
		if (application == null || facility == null || localId == null)
		{
			throw new IllegalArgumentException(
					"Fields application, facility and localId need to be set in order to define a unique local ID."
					);
		}
		
		final Organisation dbOrganisation;
		
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_LOCAL_ID_FACILITY_APPLICATION);
		
		q.setParameter(Organisation.PARAM_LOCAL_ID, localId);
		q.setParameter(Organisation.PARAM_FACILITY, facility);
		q.setParameter(Organisation.PARAM_APPLICATION, application);
		
		try
		{
			dbOrganisation = (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
		
		return dbOrganisation;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByName(java.lang.String)
	 */
	@Override
	public Organisation getByName(final String name) 
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name must not be null.");
		}
		
		final Organisation dbOrganisation;
		
		final Query q = em.createNamedQuery(Organisation.QUERY_NAME_BY_NAME);
		
		q.setParameter(Organisation.PARAM_NAME, name);
		
		try
		{
			dbOrganisation = (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}

		return dbOrganisation;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByNameExcludeOrganisationId(java.lang.String, long)
	 */
	@Override
	public Organisation getByNameExcludeOrganisationId(final String name,
			final long organisationId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_NAME_EXCLUDE_ORGANISATION_ID);
		
		q.setParameter(Organisation.PARAM_NAME, name);
		q.setParameter(Organisation.PARAM_ORGANISATION_ID, organisationId);
		
		final Organisation result;
		
		try
		{
			result = (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
		
		return result;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByNameOrSecondName(java.lang.String)
	 */
	@Override
	public Organisation getByNameOrSecondName(final String name) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_NAME_OR_SECOND_NAME);
		
		q.setParameter(Organisation.PARAM_NAME, name);
		
		try
		{
			return (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByOid(java.lang.String)
	 */
	@Override
	public Organisation getByOid(final String oid) 
	{
		if (oid == null)
		{
			throw new IllegalArgumentException("Oid must not be null.");
		}
		
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_OID);
		
		q.setParameter(Organisation.PARAM_OID, oid);
		
		try
		{
			return (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getByOidExcludeOrganisationId(java.lang.String, long)
	 */
	@Override
	public Organisation getByOidExcludeOrganisationId(String oid,
			long organisationId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_OID_EXCLUDE_ORGANISATION_ID);
		
		q.setParameter(Organisation.PARAM_OID, oid);
		q.setParameter(Organisation.PARAM_ORGANISATION_ID, organisationId);
		
		try
		{
			return (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getBySecondName(java.lang.String)
	 */
	@Override
	public Organisation getBySecondName(final String secondName) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_SECOND_NAME);
		
		q.setParameter(Organisation.PARAM_SECOND_NAME, secondName);
		
		try 
		{
			return (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getBySecondNameExcludeOrganisationId(java.lang.String, long)
	 */
	@Override
	public Organisation getBySecondNameExcludeOrganisationId(final String secondName,
			final long organisationId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_BY_SECOND_NAME_EXCLUDE_ORGANISATION_ID);
		
		q.setParameter(Organisation.PARAM_SECOND_NAME, secondName);
		q.setParameter(Organisation.PARAM_ORGANISATION_ID, organisationId);
		
		final Organisation result;
		
		try
		{
			result = (Organisation) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return null;
		}
		
		return result;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getCount()
	 */
	@Override
	public long getCount() 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_COUNT_ALL);
		
		final Number n = (Number) q.getSingleResult();
		
		return n.longValue();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#getCountByUserId(int)
	 */
	@Override
	public long getCountByUserId(final int userId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_COUNT_BY_USER_ID);
		
		q.setParameter(Organisation.PARAM_USER_ID, userId);
		
		Number n = (Number) q.getSingleResult();
		
		return n.longValue();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#isAnyOwnedByPorsUserIdReferencingAddressId(java.lang.Integer, java.lang.Long)
	 */
	@Override
	public boolean isAnyOwnedByPorsUserIdReferencingAddressId(
			final Integer userId, final Long addressId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_IS_ADDRESS_OWNING_USER_VIA_ORGANISATION
				);
		
		q.setParameter(Organisation.PARAM_USER_ID, userId);
		q.setParameter(Organisation.PARAM_ADDRESS_ID, addressId);
		
		final Number n = (Number) q.getSingleResult();
		
		return n.intValue() > 0;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#isAnyOwnedByPorsUserIdReferencingLocalIdId(java.lang.Integer, java.lang.Long)
	 */
	@Override
	public boolean isAnyOwnedByPorsUserIdReferencingLocalIdId(
			final Integer userId, final Long localIdId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_IS_LOCAL_ID_OWNING_USER_VIA_ORGANISATION
				);
		
		q.setParameter(Organisation.PARAM_USER_ID, userId);
		q.setParameter(Organisation.PARAM_LOCAL_ID_ID, localIdId);
		
		final Number n = (Number) q.getSingleResult();
		
		return n.intValue() > 0;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#isOrganisationIdRelatedToProviderId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean isOrganisationIdRelatedToProviderId(
			final Long organisationId, final Long providerId) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_IS_ORGANISATION_ID_RELATED_TO_PROVIDER_ID
				);
		
		q.setParameter(Organisation.PARAM_ORGANISATION_ID, organisationId);
		q.setParameter(Organisation.PARAM_PROVIDER_ID, providerId);
		
		final Number n = (Number) q.getSingleResult();

		return (n.intValue() > 0);
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#updateDuplicatesCalculated(boolean)
	 */
	@Override
	public void updateDuplicatesCalculated(final boolean calculated) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_UPDATE_ALL_DUPLICATES_CALCULATED);
		
		q.setParameter(Organisation.PARAM_CALCULATED, calculated);
		
		q.executeUpdate();
	}

	/**
	 * @see org.openehealth.pors.database.dao.IOrganisationDao#updateDuplicatesCalculatedById(long, boolean)
	 */
	@Override
	public void updateDuplicatesCalculatedById(final long id, 
			final boolean calculated) 
	{
		final Query q = this.em.createNamedQuery(
				Organisation.QUERY_NAME_UPDATE_DUPLICATES_CALCULATED);
		
		q.setParameter(Organisation.PARAM_ORGANISATION_ID, id);
		q.setParameter(Organisation.PARAM_CALCULATED, calculated);
		
		q.executeUpdate();
	}
}
