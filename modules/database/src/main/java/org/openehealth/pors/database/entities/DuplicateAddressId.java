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

import javax.persistence.Embeddable;

/**
 * @author jr
 *
 */
@Embeddable
public class DuplicateAddressId implements Serializable
{
	private static final long serialVersionUID = -1252504728365007483L;
	private static final int HASH_PRIME = 31;
	
	private Long addressId1;
	private Long addressId2;
	
	public DuplicateAddressId(Long addressId1, Long addressId2)
	{
		this.addressId1 = addressId1;
		this.addressId2 = addressId2;
	}
	
	public Long getAddressId1() 
	{
		return addressId1;
	}
	
	public void setAddressId1(Long addressId1) 
	{
		this.addressId1 = addressId1;
	}
	
	public Long getAddressId2() 
	{
		return addressId2;
	}
	
	public void setAddressId2(Long addressId2) 
	{
		this.addressId2 = addressId2;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		
		if (!(obj instanceof DuplicateAddressId))
		{
			return false;
		}
		
		DuplicateAddressId that = (DuplicateAddressId) obj;
		
		// XOR
		if ((this.addressId1 == null) != (this.addressId1 == null))
		{
			return false;
		}
		
		if (that.addressId1 != null && !that.addressId1.equals(this.addressId1))
		{
			return false;
		}
		
		// XOR
		if ((this.addressId2 == null) != (that.addressId2 == null))
		{
			return false;
		}
		
		if (that.addressId2 != null && !that.addressId2.equals(this.addressId2))
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		
		hashCode = hashCode * HASH_PRIME + 
				((this.addressId1 == null) ? 0 : this.addressId1.hashCode());
		hashCode = hashCode * HASH_PRIME +
				((this.addressId2 == null) ? 0 : this.addressId2.hashCode());
		
		return hashCode;
	}
}
