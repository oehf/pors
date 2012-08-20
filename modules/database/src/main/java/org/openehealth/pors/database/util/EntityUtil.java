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
package org.openehealth.pors.database.util;

import java.util.ArrayList;
import java.util.List;

import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.IMasterDomain;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.entities.Provider;


/**
 * <p>
 * Provides several methods for working with entities.
 * </p>
 * 
 * @author jr
 * 
 * @see org.openehealth.pors.database.entities.Address Address
 * @see org.openehealth.pors.database.entities.IMasterDomain IMasterDomain
 * @see org.openehealth.pors.database.entites.LocalId LocalId
 * @see org.openehealth.pors.database.entities.Organisation Organisation
 * @see org.openehealth.pors.database.entities.PorsUser PorsUser
 * @see org.openehealth.pors.database.entities.Provider Provider
 *
 */
public final class EntityUtil 
{
	private EntityUtil()
	{
		
	}
	
	/**
	 * <p>
	 * Copies all attributes except of id from 
	 * {@link org.openehealth.pors.database.entities.Address Address} 
	 * <code>from</code> to 
	 * {@link org.openehealth.pors.database.entities.Address Address} 
	 * <code>to</code>. List references will remain the same!
	 * </p>
	 * 
	 * @param from 
	 * 		Source address
	 * @param to 
	 * 		Destination address
	 */
	public static void copyFromTo(final Address from, final Address to)
	{
		to.setAdditional(from.getAdditional());
		to.setCity(from.getCity());
		to.setCountry(from.getCountry());
		to.setHouseNumber(from.getHouseNumber());
		to.setNewlyInserted(from.isNewlyInserted());
		to.setOrganisations(from.getOrganisations());
		to.setProviders(from.getProviders());
		to.setState(from.getState());
		to.setStreet(from.getStreet());
		to.setZipCode(from.getZipCode());
	}

	/**
	 * <p>
	 * Copies all attributes except of id from 
	 * {@link org.openehealth.pors.database.entities.Organisation Organisation} 
	 * <code>from</code> to 
	 * {@link org.openehealth.pors.database.entities.Organisation Organisation} 
	 * <code>to</code>. List references will remain the same!
	 * </p>
	 * 
	 * @param from 
	 * 		Source organisation
	 * @param to 
	 * 		Destination organisation
	 */
	public static void copyFromTo(final Organisation from, final Organisation to)
	{
		to.setAddresses(from.getAddresses());
		to.setDeactivationDate(from.getDeactivationDate());
		to.setDeactivationReasonCode(from.getReactivationReasonCode());
		to.setDescription(from.getDescription());
		to.setEditingUser(from.getEditingUser());
		to.setEmail(from.getEmail());
		to.setEstablishmentId(from.getEstablishmentId());
		to.setFax(from.getFax());
		to.setId(from.getId());
		to.setIpAddress(from.getIpAddress());
		to.setLastUpdateDate(from.getLastUpdateDate());
		to.setLocalIds(from.getLocalIds());
		to.setName(from.getName());
		to.setOid(from.getOid());
		to.setProviders(from.getProviders());
		to.setReactivationDate(from.getReactivationDate());
		to.setReactivationReasonCode(from.getReactivationReasonCode());
		to.setSecondName(from.getSecondName());
		to.setSessionId(from.getSessionId());
		to.setTelephone(from.getTelephone());
		to.setUser(from.getUser());
	}

	/**
	 * <p>
	 * Copies all attributes except of id and version from 
	 * {@link org.openehealth.pors.database.entities.Provider Provider} 
	 * <code>from</code> to 
	 * {@link org.openehealth.pors.database.entities.Provider Provider} 
	 * <code>to</code>. List references will remain the same!
	 * </p>
	 * 
	 * @param from 
	 * 		Source provider
	 * @param to 
	 * 		Destination provider
	 */
	public static void copyFromTo(final Provider from, final Provider to)
	{
		to.setAddresses(from.getAddresses());
		to.setBirthday(from.getBirthday());
		to.setDeactivationDate(from.getDeactivationDate());
		to.setDeactivationReasonCode(from.getDeactivationReasonCode());
		to.setDuplicatesCalculated(from.isDuplicatesCalculated());
		to.setEditingUser(from.getEditingUser());
		to.setEmail(from.getEmail());
		to.setFax(from.getFax());
		to.setFirstName(from.getFirstName());
		to.setGenderCode(from.getGenderCode());
		to.setIpAddress(from.getIpAddress());
		to.setLanr(from.getLanr());
		to.setLastName(from.getLastName());
		to.setLastUpdateDate(from.getLastUpdateDate());
		to.setLocalIds(from.getLocalIds());
		to.setMiddleName(from.getMiddleName());
		to.setNamePrefix(from.getNamePrefix());
		to.setNameSuffix(from.getNameSuffix());
		to.setOid(from.getOid());
		to.setOrganisations(from.getOrganisations());
		to.setReactivationDate(from.getReactivationDate());
		to.setReactivationReasonCode(from.getReactivationReasonCode());
		to.setSessionId(from.getSessionId());
		to.setTelephone(from.getTelephone());
		to.setUser(from.getUser());
	}

	/**
	 * <p>
	 * Creates a flat copy of <code>a</code>
	 * </p>
	 * <p>
	 * All attributes being objects of class Long or String will be copied. The 
	 * remaining attributes will become <code>null</code>.
	 * </p>
	 * 
	 * @param a 
	 * 		Address to copy.
	 * @return Copy of <code>a</code>
	 */
	public static Address flatCopyAddress(final Address a)
	{
		Address copy = new Address();
		copy.setAdditional(a.getAdditional());
		copy.setCity(a.getCity());
		copy.setCountry(a.getCountry());
		copy.setHouseNumber(a.getHouseNumber());
		copy.setId(a.getId());
		copy.setState(a.getState());
		copy.setStreet(a.getStreet());
		copy.setZipCode(a.getZipCode());
		
		return copy;
	}

	/**
	 * <p>
	 * Copies <code>lst</code> by creating flat copies of contained addresses
	 * using {@link #flatCopyAddress(Address)} and adding them to a new list.
	 * </p>
	 * 
	 * @param lst 
	 * 		List of addresses to copy
	 * @return List containing flat copied addresses
	 * @see #flatCopyAddress(Address)
	 */
	public static List<Address> flatCopyAddressList(final List<Address> lst)
	{
		List<Address> cLst = new ArrayList<Address>();
		
		for(Address a : lst)
		{
			cLst.add(flatCopyAddress(a));
		}
		
		return cLst;
	}

	/**
	 * <p>
	 * Creates a flat copy of <code>lid</code>.
	 * </p>
	 * <p>
	 * All attributes being objects of class Long or String will be copied. All
	 * attributes being objects of class 
	 * {@link org.openehealth.pors.database.entities.Organisation Organisation} 
	 * or 
	 * {@link org.openehealth.pors.database.entities.Provider Provider}
	 * will be flat copied by {@link #flatCopyOrganisation(Organisation)} 
	 * respectively {@link #flatCopyProvider(Provider). The remaining 
	 * attributes will become <code>null</code>.
	 * </p>
	 * 
	 * @param lid 
	 * 		Local id to copy.
	 * @return Copy of <code>lid</code>
	 * @see #flatCopyOrganisation(Organisation)
	 * @see #flatCopyProvider(Provider)
	 */
	public static LocalId flatCopyLocalId(final LocalId lid)
	{
		LocalId copy = new LocalId();
		
		copy.setFacility(lid.getFacility());
		copy.setId(lid.getId());
		copy.setLocalId(lid.getLocalId());
		copy.setOrganisation((lid.getOrganisation() == null) ? null : flatCopyOrganisation(lid.getOrganisation()));
		copy.setProvider((lid.getProvider() == null) ? null : flatCopyProvider(lid.getProvider()));
		
		return copy;
	}

	/**
	 * <p>
	 * Copies <code>lst</code> by creating flat copies of contained local ids
	 * using {@link #flatCopyLocalId(LocalId)} and adding them to a new list.
	 * </p>
	 * 
	 * @param lst 
	 * 		List of local ids to copy
	 * @return List containing flat copied local ids
	 * @see #flatCopyLocalId(LocalId)
	 */
	public static List<LocalId> flatCopyLocalIdList(final List<LocalId> lst)
	{
		List<LocalId> clst = new ArrayList<LocalId>();
		
		for (LocalId lid : lst)
		{
			clst.add(flatCopyLocalId(lid));
		}
		
		return clst;
	}

	/**
	 * <p>
	 * Creates a flat copy of <code>o</code>.
	 * </p>
	 * <p>
	 * All attributes being objects of class Long, String or Date will be 
	 * copied. All attributes being objects of class 
	 * {@link org.openehealth.pors.database.entities.PorsUser PorsUser} 
	 * will be flat copied by {@link #flatCopyPorsUser(PorsUser)}. The 
	 * remaining attributes will become <code>null</code>.
	 * </p>
	 * 
	 * @param o 
	 * 		Organisation to copy
	 * @return Copy of <code>o</code>
	 * @see #flatCopyPorsUser(PorsUser)
	 */
	public static Organisation flatCopyOrganisation(final Organisation o)
	{
		Organisation copy = new Organisation();
		
		copy.setDeactivationDate(o.getDeactivationDate());
		copy.setDeactivationReasonCode(o.getDeactivationReasonCode());
		copy.setDescription(o.getDescription());
		copy.setEditingUser((o.getEditingUser() == null) ? null : flatCopyPorsUser(o.getEditingUser()));
		copy.setEmail(o.getEmail());
		copy.setEstablishmentId(o.getEstablishmentId());
		copy.setFax(o.getFax());
		copy.setId(o.getId());
		copy.setIpAddress(o.getIpAddress());
		copy.setLastUpdateDate(o.getLastUpdateDate());
		copy.setName(o.getName());
		copy.setOid(o.getOid());
		copy.setReactivationDate(o.getReactivationDate());
		copy.setReactivationReasonCode(o.getReactivationReasonCode());
		copy.setSecondName(o.getSecondName());
		copy.setSessionId(o.getSessionId());
		copy.setTelephone(o.getTelephone());
		copy.setUser((o.getUser() == null) ? null : flatCopyPorsUser(o.getUser()));
		
		return copy;
	}

	/**
	 * <p>
	 * Copies <code>lst</code> by creating flat copies of contained 
	 * organisations using {@link #flatCopyOrganisation(Organisation)} and 
	 * adding them to a new list.
	 * </p>
	 * 
	 * @param lst 
	 * 		List of organisations to copy
	 * @return List containing flat copied organisations
	 * @see #flatCopyOrganisation(LocalId)
	 */
	public static List<Organisation> flatCopyOrganisationList(final List<Organisation> lst)
	{
		List<Organisation> clst = new ArrayList<Organisation>();
		
		for (Organisation o : lst)
		{
			clst.add(flatCopyOrganisation(o));
		}
		
		return clst;
	}

	/**
	 * <p>
	 * Creates a flat copy of <code>u</code>.
	 * </p>
	 * <p>
	 * All attributes being objects of class Integer, String or of type boolean 
	 * will be copied. The remaining attributes will become <code>null</code>.
	 * </p>
	 * 
	 * @param u 
	 * 		PorsUser to copy
	 * @return Copy of <code>u</code>
	 * @see #flatCopyPorsUser(PorsUser)
	 */
	public static PorsUser flatCopyPorsUser(final PorsUser u)
	{
		PorsUser copy = new PorsUser();
		copy.setActive(u.isActive());
		copy.setId(u.getId());
		copy.setName(u.getName());
		copy.setPassword(u.getPassword());
		
		return copy;
	}

	/**
	 * <p>
	 * Creates a flat copy of <code>p</code>.
	 * </p>
	 * <p>
	 * All attributes being objects of class Long, String or Date will be 
	 * copied. All attributes being objects of class 
	 * {@link org.openehealth.pors.database.entities.PorsUser PorsUser} 
	 * will be flat copied by {@link #flatCopyPorsUser(PorsUser)}. The 
	 * remaining attributes will become <code>null</code>.
	 * </p>
	 * 
	 * @param p 
	 * 		Provider to copy
	 * @return Copy of <code>p</code>
	 * @see #flatCopyPorsUser(PorsUser)
	 */
	public static Provider flatCopyProvider(final Provider p)
	{
		final Provider copy = new Provider();
		
		copy.setBirthday(p.getBirthday());
		copy.setDeactivationDate(p.getDeactivationDate());
		copy.setDeactivationReasonCode(p.getDeactivationReasonCode());
		copy.setEditingUser((p.getEditingUser() == null) ? null : flatCopyPorsUser(p.getEditingUser()));
		copy.setEmail(p.getEmail());
		copy.setFax(p.getFax());
		copy.setFirstName(p.getFirstName());
		copy.setGenderCode(p.getGenderCode());
		copy.setId(p.getId());
		copy.setIpAddress(p.getIpAddress());
		copy.setLanr(p.getLanr());
		copy.setLastName(p.getLastName());
		copy.setLastUpdateDate(p.getLastUpdateDate());
		copy.setMiddleName(p.getMiddleName());
		copy.setNamePrefix(p.getNamePrefix());
		copy.setNameSuffix(p.getNameSuffix());
		copy.setOid(p.getOid());
		copy.setReactivationDate(p.getReactivationDate());
		copy.setReactivationReasonCode(p.getReactivationReasonCode());
		copy.setSessionId(p.getSessionId());
		copy.setSpecialisation(p.getSpecialisation());
		copy.setTelephone(p.getTelephone());
		copy.setUser((p.getEditingUser() == null) ? null : flatCopyPorsUser(p.getUser()));
		
		return copy;
	}
	
	/**
	 * <p>
	 * Copies <code>lst</code> by creating flat copies of contained providers 
	 * using {@link #flatCopyProvider(Provider)} and adding them to a new list.
	 * </p>
	 * 
	 * @param lst 
	 * 		List of providers to copy
	 * @return List containing flat copied providers
	 * @see #flatCopyProvider(Provider)
	 */
	public static List<Provider> flatCopyProviderList(final List<Provider> lst)
	{
		List<Provider> clst = new ArrayList<Provider>();
		
		for (Provider p : lst)
		{
			clst.add(flatCopyProvider(p));
		}
		
		return clst;
	}
	
	/**
	 * <p>
	 * Checks if there are any differences between <code>a</code> and 
	 * <code>b</code> which would affect the source table in database.
	 * </p>
	 * <p>
	 * In fact, this includes all column values, but not the references 
	 * to providers or organisations.
	 * </p>
	 * 
	 * @param a 
	 * 		First address for comparison
	 * @param b 
	 * 		Second address for comparison
	 * @return True, if there are any differences. False, else.
	 */
	public static boolean haveDifferences(final Address a, final Address b)
	{
		/* Boolean expressions:
		 * True, if and only if: Both variables are not null and have same values or 
		 * exactly one of both variables is null
		 */
		
		if (a.hashCode() != b.hashCode())
		{
			return true;
		}
		
		if (!a.equals(b))
		{
			return true;
		}
		
		// XOR
		if((a.getState() == null) != (b.getState() == null))
		{
			return true;
		}
		
		if (a.getState() != null && !a.getState().equals(b.getState()))
		{
			return true;
		}
		
		return false;
	}

	/**
	 * <p>
	 * Checks if there are any differences between <code>a</code> and 
	 * <code>b</code> which would affect the source table in database.
	 * </p>
	 * <p>
	 * In fact, this includes all column values as well as the references 
	 * to an organisation and provider.
	 * </p>
	 * 
	 * @param a 
	 * 		First local id for comparison
	 * @param b 
	 * 		Second local id for comparison
	 * @return True, if there are any differences. False, else.
	 */
	public static boolean haveDifferences(final LocalId a, final LocalId b)
	{
		if (a.hashCode() != b.hashCode())
		{
			return true;
		}
		
		if (!a.equals(b))
		{
			return true;
		}
		
		if ((a.getOrganisation() == null) != (b.getOrganisation() == null))
		{
			return true;
		}
		
		if ((a.getProvider() == null) != (b.getProvider() == null))
		{
			return true;
		}
		
		if (a.getOrganisation() != null)
		{
			Long oIdA = a.getOrganisation().getId();
			Long oIdB = b.getOrganisation().getId();
			
			if ((oIdA == null) != (oIdB == null))
			{
				return true;
			}
			
			if (oIdA != null && !oIdA.equals(oIdB))
			{
				return true;
			}
		}
		
		if (b.getProvider() != null)
		{
			Long pIdA = a.getProvider().getId();
			Long pIdB = b.getProvider().getId();
			
			if ((pIdA == null) != (pIdB == null))
			{
				return true;
			}
			
			if (pIdA != null && !pIdA.equals(pIdB))
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * <p>
	 * Checks if there are any differences between <code>a</code> and 
	 * <code>b</code> which would affect the source table in database.
	 * </p>
	 * <p>
	 * In fact, this includes all column values as well as the reference 
	 * to the owning user of the data set, but not the references to providers
	 * or local ids.
	 * </p>
	 * 
	 * @param a 
	 * 		First organisation for comparison
	 * @param b 
	 * 		Second organisation for comparison
	 * @return True, if there are any differences. False, else.
	 */
	public static boolean haveDifferences(final Organisation a, final Organisation b)
	{
		/* Boolean expressions:
		 * True, if and only if: Both variables are not null and have same values or 
		 * exactly one of both variables is null
		 */
		
		if (!DateUtil.equalsIgnoreTime(a.getDeactivationDate(), b.getDeactivationDate()))
		{
			return true;
		}
		
		if ((a.getDeactivationReasonCode() != null && b.getDeactivationReasonCode() != null && !a.getDeactivationReasonCode().equals(b.getDeactivationReasonCode())) || ((a.getDeactivationReasonCode() == null) != (b.getDeactivationReasonCode() == null)))
		{
			return true;
		}
		
		if ((a.getDescription() != null && b.getDescription() != null && !a.getDescription().equals(b.getDescription())) || ((a.getDescription() == null) != (b.getDescription() == null)))
		{
			return true;
		}
		
		if ((a.getEmail() != null && b.getEmail() != null && !a.getEmail().equals(b.getEmail())) || ((a.getEmail() == null) != (b.getEmail() == null)))
		{
			return true;
		}
		
		if ((a.getEstablishmentId() != null && b.getEstablishmentId() != null && !a.getEstablishmentId().equals(b.getEstablishmentId())) || ((a.getEstablishmentId() == null) != (b.getEstablishmentId() == null)))
		{
			return true;
		}
		
		if ((a.getFax() != null && b.getFax() != null && !a.getFax().equals(b.getFax())) || ((a.getFax() == null) != (b.getFax() == null)))
		{
			return true;
		}
		
		if ((a.getName() != null && b.getName() != null && !a.getName().equals(b.getName())) || ((a.getName() == null) != (b.getName() == null)))
		{
			return true;
		}
		
		if ((a.getOid() != null && b.getOid() != null && !a.getOid().equals(b.getOid())) || ((a.getOid() == null) != (b.getOid() == null)))
		{
			return true;
		}
		
		if (!DateUtil.equalsIgnoreTime(a.getReactivationDate(), b.getReactivationDate()))
		{
			return true;
		}
		
		if ((a.getReactivationReasonCode() != null && b.getReactivationReasonCode() != null && !a.getReactivationReasonCode().equals(b.getReactivationReasonCode())) || ((a.getReactivationReasonCode() == null) != (b.getReactivationReasonCode() == null)))
		{
			return true;
		}
		
		if ((a.getSecondName() != null && b.getSecondName() != null && !a.getSecondName().equals(b.getSecondName())) || ((a.getSecondName() == null) != (b.getSecondName() == null)))
		{
			return true;
		}
		
		if ((a.getTelephone() != null && b.getTelephone() != null && !a.getTelephone().equals(b.getTelephone())) || ((a.getTelephone() == null) != (b.getTelephone() == null)))
		{
			return true;
		}
		
		if ((a.getUser() == null) != (b.getUser() == null))
		{
			return true;
		}
		
		if (a.getUser() != null)
		{
			Integer uIdA = a.getUser().getId();
			Integer uIdB = b.getUser().getId();
			
			if ((uIdA == null) != (uIdB == null))
			{
				return true;
			}
			
			if (uIdA != null && !uIdA.equals(uIdB))
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * <p>
	 * Checks if there are any differences between <code>a</code> and 
	 * <code>b</code> which would affect the source table in database.
	 * </p>
	 * <p>
	 * In fact, this includes all column values as well as the reference 
	 * to the owning user of the data set, but not the references to 
	 * organisations or local ids.
	 * </p>
	 * 
	 * @param a 
	 * 		First provider for comparison
	 * @param b 
	 * 		Second provider for comparison
	 * @return True, if there are any differences. False, else.
	 */
	public static boolean haveDifferences(final Provider a, final Provider b)
	{
		/* 
		 * Only the date of birth (and not the time) is of importance here, so 
		 * the Date#equals(Object) method would be too detailed in some cases!
		 */
		if (!DateUtil.equalsIgnoreTime(a.getBirthday(), b.getBirthday()))
		{
			return true;
		}
		
		if (!DateUtil.equalsIgnoreTime(a.getDeactivationDate(), b.getDeactivationDate()))
		{
			return true;
		}
		
		if ((a.getDeactivationReasonCode() != null && b.getDeactivationReasonCode() != null && !a.getDeactivationReasonCode().equals(b.getDeactivationReasonCode())) || ((a.getDeactivationReasonCode() == null) != (b.getDeactivationReasonCode() == null)))
		{
			return true;
		}
		
		if ((a.getEmail() != null && b.getEmail() != null && !a.getEmail().equals(b.getEmail())) || ((a.getEmail() == null) != (b.getEmail() == null)))
		{
			return true;
		}
		
		if ((a.getFax() != null && b.getFax() != null && !a.getFax().equals(b.getFax())) || ((a.getFax() == null) != (b.getFax() == null)))
		{
			return true;
		}
		
		if ((a.getFirstName() != null && b.getFirstName() != null && !a.getFirstName().equals(b.getFirstName())) || ((a.getFirstName() == null) != (b.getFirstName() == null)))
		{
			return true;
		}
		
		if ((a.getGenderCode() != null && b.getGenderCode() != null && !a.getGenderCode().equals(b.getGenderCode())) || ((a.getGenderCode() == null) != (b.getGenderCode() == null)))
		{
			return true;
		}
		
		if ((a.getLanr() != null && b.getLanr() != null && !a.getLanr().equals(b.getLanr())) || ((a.getLanr() == null) != (b.getLanr() == null)))
		{
			return true;
		}
		
		if ((a.getLastName() != null && b.getLastName() != null && !a.getLastName().equals(b.getLastName())) || ((a.getLastName() == null) != (b.getLastName() == null)))
		{
			return true;
		}
		
		if ((a.getMiddleName() != null && b.getMiddleName() != null && !a.getMiddleName().equals(b.getMiddleName())) || ((a.getMiddleName() == null) != (b.getMiddleName() == null)))
		{
			return true;
		}
		
		if ((a.getNamePrefix() != null && b.getNamePrefix() != null && !a.getNamePrefix().equals(b.getNamePrefix())) || ((a.getNamePrefix() == null) != (b.getNamePrefix() == null)))
		{
			return true;
		}
		
		if ((a.getNameSuffix() != null && b.getNameSuffix() != null && !a.getNameSuffix().equals(b.getNameSuffix())) || ((a.getNameSuffix() == null) != (b.getNameSuffix() == null)))
		{
			return true;
		}
		
		if ((a.getOid() != null && b.getOid() != null && !a.getOid().equals(b.getOid())) || ((a.getOid() == null) != (b.getOid() == null)))
		{
			return true;
		}
		
		if (!DateUtil.equalsIgnoreTime(a.getReactivationDate(), b.getReactivationDate()))
		{
			return true;
		}
		
		if ((a.getSpecialisation() != null && b.getSpecialisation() != null && !a.getSpecialisation().equals(b.getSpecialisation())) || ((a.getSpecialisation() == null) != (b.getSpecialisation() == null)))
		{
			return true;
		}
		
		if ((a.getTelephone() != null && b.getTelephone() != null && !a.getTelephone().equals(b.getTelephone())) || ((a.getTelephone() == null) != (b.getTelephone() == null)))
		{
			return true;
		}
		
		if ((a.getUser() == null) != (b.getUser() == null))
		{
			return true;
		}
		
		if (a.getUser() != null)
		{
			Integer uIdA = a.getUser().getId();
			Integer uIdB = b.getUser().getId();
			
			if ((uIdA == null) != (uIdB == null))
			{
				return true;
			}
			
			if (uIdA != null && !uIdA.equals(uIdB))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * <p>
	 * Throws a {@link java.lang.IllegalArgumentException} in the case that 
	 * either the session id or the editing user or the IP address was not 
	 * set in <code>domain</code>.
	 * </p>
	 * 
	 * @param domain
	 * 		Master domain to check
	 * @throws java.lang.IllegalArgumentException 
	 * 		If any of the needed attributes was not set
	 */
	public static void verifyLoggingData(final IMasterDomain domain)
	{		
		if (domain.getSessionId() == null || domain.getEditingUser() == null ||
				domain.getIpAddress() == null)
		{
			throw new IllegalArgumentException(
					"Session id, IP address and editing user need to be set in order to log all changes."
					);
		}
	}
}
