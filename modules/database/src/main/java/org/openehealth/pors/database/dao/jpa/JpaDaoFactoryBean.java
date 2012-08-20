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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.openehealth.pors.database.dao.IAddressDao;
import org.openehealth.pors.database.dao.IAddressLogDao;
import org.openehealth.pors.database.dao.IDaoFactory;
import org.openehealth.pors.database.dao.IDuplicateAddressDao;
import org.openehealth.pors.database.dao.IDuplicateEntryDao;
import org.openehealth.pors.database.dao.IDuplicateOrganisationDao;
import org.openehealth.pors.database.dao.IDuplicateProviderDao;
import org.openehealth.pors.database.dao.IDuplicateRecognitionDao;
import org.openehealth.pors.database.dao.IHistoryDao;
import org.openehealth.pors.database.dao.IImportResultDao;
import org.openehealth.pors.database.dao.ILocalIdDao;
import org.openehealth.pors.database.dao.ILocalIdLogDao;
import org.openehealth.pors.database.dao.IOrganisationDao;
import org.openehealth.pors.database.dao.IOrganisationHasAddressLogDao;
import org.openehealth.pors.database.dao.IOrganisationHasProviderLogDao;
import org.openehealth.pors.database.dao.IOrganisationLogDao;
import org.openehealth.pors.database.dao.IPorsUserDao;
import org.openehealth.pors.database.dao.IProviderDao;
import org.openehealth.pors.database.dao.IProviderHasAddressLogDao;
import org.openehealth.pors.database.dao.IProviderLogDao;
import org.openehealth.pors.database.dao.IUserHistoryDao;
import org.openehealth.pors.database.dao.IUserRoleDao;


@Stateless
public class JpaDaoFactoryBean implements IDaoFactory 
{
	@PersistenceContext(unitName="manager1")
	private EntityManager em;
	
	private AddressJpaDao addressDao;
	private AddressLogJpaDao addressLogDao;
	private DuplicateAddressJpaDao duplicateAddressDao;
	private DuplicateEntryJpaDao duplicateEntryDao;
	private DuplicateOrganisationJpaDao duplicateOrganisationDao;
	private DuplicateProviderJpaDao duplicateProviderDao;
	private DuplicateRecognitionJpaDao duplicateRecognitionDao;
	private HistoryJpaDao historyDao;
	private LocalIdJpaDao localIdDao;
	private LocalIdLogJpaDao localIdLogDao;
	private OrganisationJpaDao organisationDao;
	private OrganisationHasAddressLogJpaDao organisationHasAddressLogDao;
	private OrganisationHasProviderLogJpaDao organisationHasProviderLogDao;
	private OrganisationLogJpaDao organisationLogDao;
	private PorsUserJpaDao userDao;
	private ProviderJpaDao providerDao;
	private ProviderHasAddressLogJpaDao providerHasAddressDao;
	private ProviderLogJpaDao providerLogDao;
	private UserHistoryJpaDao userHistoryDao;
	private ImportResultJpaDao importResultDao;
	private UserRoleJpaDao userRoleDao;
	
	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getLocalIdDao()
	 */
	@Override
	public ILocalIdDao getLocalIdDao() 
	{
		if (this.localIdDao == null)
		{
			this.localIdDao = new LocalIdJpaDao(this.em);
		}
		
		return this.localIdDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getAddressDao()
	 */
	@Override
	public IAddressDao getAddressDao()
	{
		if (this.addressDao == null)
		{
			this.addressDao = new AddressJpaDao(this.em);
		}
		
		return this.addressDao;
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getPorsUserDao()
	 */
	@Override
	public IPorsUserDao getPorsUserDao()
	{
		if (this.userDao == null)
		{
			this.userDao = new PorsUserJpaDao(this.em);
		}
		
		return this.userDao;
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getProviderLogDao()
	 */
	@Override
	public IProviderLogDao getProviderLogDao()
	{
		if (this.providerLogDao == null)
		{
			this.providerLogDao = new ProviderLogJpaDao(this.em);
		}
		
		return this.providerLogDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getAddressLogDao()
	 */
	@Override
	public IAddressLogDao getAddressLogDao() 
	{
		if (this.addressLogDao == null)
		{
			this.addressLogDao = new AddressLogJpaDao(this.em);
		}
		
		return this.addressLogDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getLocalIdLogDao()
	 */
	@Override
	public ILocalIdLogDao getLocalIdLogDao() 
	{
		if (this.localIdLogDao == null)
		{
			this.localIdLogDao = new LocalIdLogJpaDao(this.em);
		}
		
		return this.localIdLogDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getOrganisationHasAddressLogDao()
	 */
	@Override
	public IOrganisationHasAddressLogDao getOrganisationHasAddressLogDao() 
	{
		if (this.organisationHasAddressLogDao == null)
		{
			this.organisationHasAddressLogDao = 
				new OrganisationHasAddressLogJpaDao(this.em);
		}
		
		return this.organisationHasAddressLogDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getOrganisationHasProviderLogDao()
	 */
	@Override
	public IOrganisationHasProviderLogDao getOrganisationHasProviderLogDao() 
	{
		if (this.organisationHasProviderLogDao == null)
		{
			this.organisationHasProviderLogDao = new 
			OrganisationHasProviderLogJpaDao(this.em);
		}
		
		return this.organisationHasProviderLogDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getOrganisationLogDao()
	 */
	@Override
	public IOrganisationLogDao getOrganisationLogDao() 
	{
		if (this.organisationLogDao == null)
		{
			this.organisationLogDao = new OrganisationLogJpaDao(this.em);
		}
		
		return this.organisationLogDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getProviderHasAddressLogDao()
	 */
	@Override
	public IProviderHasAddressLogDao getProviderHasAddressLogDao() 
	{
		if (this.providerHasAddressDao == null)
		{
			this.providerHasAddressDao = new ProviderHasAddressLogJpaDao(
					this.em);
		}
		
		return this.providerHasAddressDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getOrganisationDao()
	 */
	@Override
	public IOrganisationDao getOrganisationDao() 
	{
		if (this.organisationDao == null)
		{
			this.organisationDao = new OrganisationJpaDao(this.em);
		}
		
		return this.organisationDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getProviderDao()
	 */
	@Override
	public IProviderDao getProviderDao() 
	{
		if (this.providerDao == null)
		{
			this.providerDao = new ProviderJpaDao(this.em);
		}

		return this.providerDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getHistoryDao()
	 */
	@Override
	public IHistoryDao getHistoryDao() 
	{
		if (this.historyDao == null)
		{
			this.historyDao = new HistoryJpaDao(this.em);
		}
		
		return this.historyDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getUserHistoryDao()
	 */
	@Override
	public IUserHistoryDao getUserHistoryDao() 
	{
		if (this.userHistoryDao == null)
		{
			this.userHistoryDao = new UserHistoryJpaDao(this.em);
		}
		
		return this.userHistoryDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getDuplicateAddressDao()
	 */
	@Override
	public IDuplicateAddressDao getDuplicateAddressDao() 
	{
		if (this.duplicateAddressDao == null)
		{
			this.duplicateAddressDao = new DuplicateAddressJpaDao(this.em); 
		}
		
		return this.duplicateAddressDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getDuplicateOrganisationDao()
	 */
	@Override
	public IDuplicateOrganisationDao getDuplicateOrganisationDao() 
	{
		if (this.duplicateOrganisationDao == null)
		{
			this.duplicateOrganisationDao = new DuplicateOrganisationJpaDao(
					this.em);
		}
		
		return this.duplicateOrganisationDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getDuplicateProviderDao()
	 */
	@Override
	public IDuplicateProviderDao getDuplicateProviderDao() 
	{
		if (this.duplicateProviderDao == null)
		{
			this.duplicateProviderDao = new DuplicateProviderJpaDao(this.em);
		}
		
		return this.duplicateProviderDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getDuplicateRecognitionDao()
	 */
	@Override
	public IDuplicateRecognitionDao getDuplicateRecognitionDao() 
	{
		if (this.duplicateRecognitionDao == null)
		{
			this.duplicateRecognitionDao = new DuplicateRecognitionJpaDao(
					this.em);
		}
		
		return this.duplicateRecognitionDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getDuplicateEntryDao()
	 */
	@Override
	public IDuplicateEntryDao getDuplicateEntryDao() 
	{
		if (this.duplicateEntryDao == null)
		{
			this.duplicateEntryDao = new DuplicateEntryJpaDao(this.em);
		}
		
		return this.duplicateEntryDao;
	}
	
	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getImportResultDao()
	 */
	@Override
	public IImportResultDao getImportResultDao() 
	{
		if (this.importResultDao == null) 
		{
			this.importResultDao = new ImportResultJpaDao(this.em);
		}
		
		return this.importResultDao;
	}

	/**
	 * @see org.openehealth.pors.database.dao.IDaoFactory#getUserRoleDao()
	 */
	@Override
	public IUserRoleDao getUserRoleDao() 
	{
		if (this.userRoleDao == null)
		{
			this.userRoleDao = new UserRoleJpaDao(this.em);
		}
		
		return this.userRoleDao;
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}
}
