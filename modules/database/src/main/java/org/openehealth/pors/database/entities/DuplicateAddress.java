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
package org.openehealth.pors.database.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * <p>
 * Entity bean mapping entries of table "duplicate_address" and thus 
 * representing a hint on two entries in table "Address" which could be 
 * duplicates of each other.
 * </p>
 * <p>
 * The ids of both entries in question are also forming the id of this entity.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>{@link #QUERY_NAME_ALL}</b><br />Selects all possible duplicates.
 * </li>
 * <li><b>{@link #QUERY_NAME_DELETE_BY_ADDRESS_ID}</b><br />Deletes all entries 
 * where first or second address id equals <code>PARAM_ADDRESS_ID</code>.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name = "duplicate_address")
@NamedQueries( value = { 
		@NamedQuery( name = "getDuplicateAddressList", query = "SELECT da FROM DuplicateAddress da"),
		@NamedQuery( name = "deleteByAddressId1OrAddressId2", query = "DELETE FROM DuplicateAddress da WHERE da.id.addressId1 = :addressId OR da.id.addressId2 = :addressId")
		} )
public class DuplicateAddress implements Serializable
{
	private static final long serialVersionUID = -3177698726634853480L;
	
	public static final String QUERY_NAME_ALL = "getDuplicateAddressList";
	public static final String QUERY_NAME_DELETE_BY_ADDRESS_ID = "deleteByAddressId1OrAddressId2";
	
	public static final String PARAM_ADDRESS_ID = "addressId";
	
	@EmbeddedId
	private DuplicateAddressId id;
	
	private Double value;

	public DuplicateAddressId getId() 
	{
		return id;
	}

	public void setId(DuplicateAddressId id) 
	{
		this.id = id;
	}

	public Double getValue() 
	{
		return value;
	}

	public void setValue(Double value) 
	{
		this.value = value;
	}
}
