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
package org.openehealth.pors.database.dao;

import org.openehealth.pors.database.entities.Address;

/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.Address Address} 
 * objects.
 * </p>
 * 
 * @author jr
 * 
 * @see org.openehealth.pors.database.entities.Address Address
 */
public interface IAddressDao extends IDao<Long, Address> 
{
	/**
	 * <p>
	 * Selects the address composed of the unique combination of  
	 * <code>additional</code>, <code>street</code>, <code>houseNumber</code>,
	 * <code>zipCode</code>, <code>city</code> and <code>country</code>.
	 * </p>
	 * <p>
	 * Returns <code>null</code> if no matching address could be found
	 * </p>
	 * 
	 * @param additional
	 * 		Additional address information
	 * @param street
	 * 		Street of the address
	 * @param houseNumber
	 * 		House number of the address
	 * @param zipCode
	 * 		ZIP code of the address
	 * @param city
	 * 		City of the address
	 * @param country
	 * 		Country of the address
	 * @return
	 */
	Address getByUniqueCombination(String additional, String street, 
			String houseNumber, String zipCode, String city, String country);
	
	/**
	 * <p>
	 * For all addresses, the flag for calculated duplicates is set to 
	 * <code>calculated</code>.
	 * </p>
	 * 
	 * @param calculated
	 * 		True, if duplicates where calculated, else false.
	 */
	void updateDuplicatesCalculated(boolean calculated);
	
	/**
	 * <p>
	 * For the address having <code>id</code> as ID, the flag for calculated 
	 * duplicates is set to <code>calculated</code>.
	 * </p>
	 * 
	 * @param id
	 * 		ID of the address to update
	 * @param calculated
	 * 		True, if duplicates where calculated, else false.
	 */
	void updateDuplicatesCalculatedById(long id, boolean calculated);
}
