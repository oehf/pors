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
package org.openehealth.pors.database.connector.sync;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openehealth.pors.database.dao.IAddressDao;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.exception.DataException;
import org.openehealth.pors.database.exception.DoubleAddressException;
import org.openehealth.pors.database.exception.InsufficientDataException;

/**
* <p>
* Synchronises addresses to a new database state.
* </p>
* <p>
* Purges data from a list of addresses and inserts or updates them into 
* database.
* </p>
* 
* @author jr
*/
public class AddressSynchronisation 
{
	private IAddressDao dao;
	private List<Address> addresses;
	private List<Address> synchronisedAddresses;
	
	/**
	 * <p>
	 * Initialises the synchronisation with the DAO which should be used for 
	 * database access and the list of addresses to synchronise. 
	 * </p>
	 * 
	 * @param dao
	 * 		Data access object for addresses
	 * @param addresses
	 * 		Addresses to synchronise
	 * @throws IllegalArgumentException
	 * 		If any of the parameters is <code>null</code>
	 */
	public AddressSynchronisation(final IAddressDao dao, 
			final List<Address> addresses)
	{
		this.dao = dao;
		this.addresses = addresses;
		
		if (!isValidAttributes())
		{
			throw new IllegalArgumentException(
					"No parameter is allowed to be null."
					);
		}
	}
	
	/**
	 * <p>
	 * Synchronises this data if not already done and returns the resulting 
	 * list.
	 * </p>
	 * 
	 * @return
	 * 		List of synchronised addresses
	 * @throws DataException
	 * 		
	 */
	public List<Address> getSynchronisedAddresses() throws DataException
	{
		if (this.synchronisedAddresses == null)
		{
			this.synchroniseData();
		}
		
		return this.synchronisedAddresses;
	}
	
	/**
	 * <p>
	 * Creates a copy of <code>a</code> containing additional, city, country, 
	 * house number, street and ZIP code of <code>a</code>.
	 * </p>
	 * 
	 * @param a
	 * 		Address to copy
	 * @return
	 * 		Copy of <code>a</code>
	 */
	private Address createDoubleAddress(Address a)
	{
		Address doubleAddress = new Address();
		doubleAddress.setAdditional(a.getAdditional());
		doubleAddress.setCity(a.getCity());
		doubleAddress.setCountry(a.getCountry());
		doubleAddress.setHouseNumber(a.getHouseNumber());
		doubleAddress.setStreet(a.getStreet());
		doubleAddress.setZipCode(a.getZipCode());
		
		return doubleAddress;
	}
	
	/**
	 * <p>
	 * Checks, if this attributes where set correctly.
	 * </p>
	 * 
	 * @return
	 * 		True, if attributes are correct, false otherwise.
	 */
	private boolean isValidAttributes()
	{
		if (this.dao == null)
		{
			return false;
		}
		
		if (this.addresses == null)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Preprocesses the data in this addresses list for synchronisation.
	 * </p>
	 * <p>
	 * For each address, the additional field is set to an empty string if it 
	 * is <code>null</code>. Every address is the sorted into the 
	 * <code>insertAddresses</code> and <code>updateAddresses</code> depending
	 * if they have an ID set or not.</p>
	 * 
	 * @param insertAddresses
	 * 		List where addresses which have to be inserted are added
	 * @param updateAddresses
	 * 		List where addresses which have to be updated are added
	 * @throws IllegalArgumentException
	 * 		If any address ID occurs twice
	 */
	private void preprocessData(List<Address> insertAddresses, List<Address> updateAddresses)
	{
		Set<Long> updateAddressIds = new HashSet<Long>();
		
		for(Address a : this.addresses)
		{
			if(a.getAdditional() == null)
			{
				a.setAdditional("");
			}
			
			if (a.getId() != null && a.getId().longValue() > 0)
			{
				if (updateAddressIds.contains(a.getId()))
				{
					throw new IllegalArgumentException(
							"Tried to update an address to two values."
							);
				}
				
				updateAddressIds.add(a.getId());
				updateAddresses.add(a);
			}
			else // if (a.getId() == null || a.getId().longValue <= 0)
			{
				insertAddresses.add(a);
			}
		}
	}
	
	/**
	 * <p>
	 * Synchronises this address data by preprocessing the list and then 
	 * processing the update and insert operations.
	 * </p>
	 * 
	 * @throws DataException
	 * 		If an address ID occurs twice, an address should be updated to an 
	 * 		existing value or data of any address is incomplete
	 */
	private void synchroniseData() throws DataException
	{
		List<Address> insertAddresses = new ArrayList<Address>(
				this.addresses.size());
		List<Address> updateAddresses = new ArrayList<Address>(
				this.addresses.size());
		
		try
		{
			this.preprocessData(insertAddresses, updateAddresses);
		}
		catch (IllegalArgumentException e)
		{
			throw new DataException(
					"Tried to update an existing address to two different values.", 
					e);
		}
		
		Set<Long> synchronisedAddressIds = new HashSet<Long>();
		
		this.synchronisedAddresses = new ArrayList<Address>(
				this.addresses.size());
		
		try 
		{
			this.synchronisedAddresses.addAll(this.synchroniseUpdateData(
					synchronisedAddressIds, updateAddresses));
			this.synchronisedAddresses.addAll(this.synchroniseInsertData(
					synchronisedAddressIds, insertAddresses));
		} 
		catch (InsufficientDataException e) 
		{
			throw new DataException("Address data is incomplete.", e);
		}
		catch (DoubleAddressException e)
		{
			Address doubleAddress = e.getDoubleAddress();
			throw new DataException(
					"Double occurrence of address or address already exists: " + 
					doubleAddress.getAdditional() + ", " + 
					doubleAddress.getStreet() + " " + 
					doubleAddress.getHouseNumber() + ", " + 
					doubleAddress.getZipCode() + " " + 
					doubleAddress.getCity());
		}
	}
	
	/**
	 * <p>
	 * Updates the addresses in <code>insertAddresses</code> and tunes the
	 * IDs with the ones stored in <code>synchronisedAddressIds</code>. The 
	 * latter value can be obtained from 
	 * {@link #synchroniseUpdateData(Set, List)}.
	 * </p>
	 * 
	 * @param synchronisedAddressIds
	 * 		Set of already synchronised IDs
	 * @param insertAddresses
	 * 		Addresses to insert
	 * @return
	 * 		Synchronised addresses
	 * @throws InsufficientDataException
	 * 		If any address does not have all mandatory fields set
	 * @throws DoubleAddressException
	 * 		If any address value occurs twice in the data list or database
	 */
	private List<Address> synchroniseInsertData(
			Set<Long> synchronisedAddressIds, List<Address> insertAddresses) 
			throws InsufficientDataException, DoubleAddressException
	{
		List<Address> syncAddresses = new ArrayList<Address>(insertAddresses.size());
		
		for (Address a : insertAddresses)
		{
			Address dbAddress;
			try
			{
				dbAddress = this.dao.getByUniqueCombination(
						a.getAdditional(), a.getStreet(), a.getHouseNumber(), 
						a.getZipCode(), a.getCity(), a.getCountry());
			}
			catch (IllegalArgumentException e)
			{
				throw new InsufficientDataException(a, 
						"Tried to insert address having insufficient data: " + 
						a.toString(), e);
			}
			
			if (dbAddress == null)
			{
				this.dao.insert(a);
				
				a.setNewlyInserted(true);
				
				syncAddresses.add(a);
				synchronisedAddressIds.add(a.getId());
			}
			else if (!synchronisedAddressIds.contains(dbAddress.getId()))
			{
				if ((dbAddress.getState() != null && a.getState() != null && 
						!dbAddress.getState().equals(a.getState())) || 
						(dbAddress.getState() != null && a.getState() == null))
				{
					Address doubleAddress = this.createDoubleAddress(a);
					
					throw new DoubleAddressException(doubleAddress,
							"Tried to insert an already existing address.");
				}
				
				if (dbAddress.getState() == null && a.getState() != null)
				{
					dbAddress.setState(a.getState());
					this.dao.update(dbAddress);
				}
				
				dbAddress.setNewlyInserted(false);
				
				syncAddresses.add(dbAddress);
				synchronisedAddressIds.add(dbAddress.getId());	
			}
		}
		
		return syncAddresses;
	}
	
	/**
	 * <p>
	 * Updates the addresses in <code>updateAddresses</code> and stores their
	 * IDs in <code>updateAddresses</code>.
	 * </p>
	 * 
	 * @param synchronisedAddressIds
	 * 		Set where to store the IDs of the updated addresses
	 * @param updateAddresses
	 * 		Addresses to update
	 * @return
	 * 		Synchronised addresses
	 * @throws InsufficientDataException
	 * 		If any address does not have all mandatory fields set
	 * @throws DoubleAddressException
	 * 		If any address value occurs twice in the data list or database
	 */
	private List<Address> synchroniseUpdateData(
			Set<Long> synchronisedAddressIds, 
			final List<Address> updateAddresses) 
			throws InsufficientDataException, DoubleAddressException
	{
		List<Address> syncAddresses = new ArrayList<Address>(updateAddresses.size());
		
		int index = 0;
		for (Address a : updateAddresses)
		{
			for (int i = index + 1; i < updateAddresses.size(); i++)
			{
				if (a.hashCode() == updateAddresses.get(i).hashCode())
				{
					if (a.equals(updateAddresses.get(i)))
					{
						Address doubleAddress = this.createDoubleAddress(a);
						
						throw new DoubleAddressException(doubleAddress,
								"Tried to update two addresses to same value.");
					}
				}
			}
			
			Address dbAddress; 
			try
			{
				dbAddress = this.dao.getByUniqueCombination(a.getAdditional(), 
					a.getStreet(), a.getHouseNumber(), a.getZipCode(), 
					a.getCity(), a.getCountry());
			}
			catch (IllegalArgumentException e)
			{
				throw new InsufficientDataException(a, 
						"Tried to update address having insufficient data: " + 
						a.toString(), e);
			}
			
			if (dbAddress == null)
			{
				this.dao.update(a);
				
				a.setNewlyInserted(false);
				
				syncAddresses.add(a);
			}
			else // if (dbAddress != null)
			{
				if (dbAddress.getId().equals(a.getId()))
				{
					this.dao.update(a);
					
					a.setNewlyInserted(false);
					
					syncAddresses.add(a);
				}
				else
				{
					if (!synchronisedAddressIds.contains(dbAddress.getId()))
					{
						// If addresses can be merged, set relation to data set 
						// in database, else exception
						
						if ((a.getState() != null && 
								dbAddress.getState() != null && 
								!a.getState().equals(dbAddress.getState())) ||
								(dbAddress.getState() != null && a.getState() == null))
						{
							Address doubleAddress = this.createDoubleAddress(
									a);
							
							throw new DoubleAddressException(doubleAddress, 
									"Tried to update an address to an already existing value.");
						}
					
						if (dbAddress.getState() == null && a.getState() != null)
						{
							dbAddress.setState(a.getState());
							this.dao.update(dbAddress);
						}
						
						dbAddress.setNewlyInserted(false);
						
						synchronisedAddressIds.add(dbAddress.getId());
						syncAddresses.add(dbAddress);
					}
				}
			}
			
			index++;
		}
		
		return syncAddresses;
	}
}
