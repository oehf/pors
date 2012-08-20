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
 * <p>
 * The composed ID of an 
 * {@link org.openehealth.pors.database.entities.DuplicateProvider DuplicateProvider} 
 * entity bean.
 * </p>
 * <p>
 * A {@link DuplicateProvider} object can be uniquely defined by both IDs of the 
 * providers which may be duplicates of each other.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entiites.DuplicateProvider DuplicateProvider
 *
 */
@Embeddable
public class DuplicateProviderId implements Serializable 
{
	private static final long serialVersionUID = -7474976136177732697L;
	private static final int HASH_PRIME = 31;
	
	private Long providerId1;
	private Long providerId2;
	
	/**
	 * <p>
	 * The standard constructor.
	 * </p>
	 * <p>
	 * All attributes will be <code>null</code> after initialisation.
	 * </p>
	 */
	public DuplicateProviderId()
	{
		
	}
	
	/**
	 * <p>
	 * Initialises this duplicate provider ID with the IDs of two providers 
	 * which may be duplicates of each other.
	 * </p>
	 * 
	 * @param providerId1
	 * 		ID of the first provider
	 * @param providerId2
	 * 		ID of the second provider
	 */
	public DuplicateProviderId(Long providerId1, Long providerId2)
	{
		this.providerId1 = providerId1;
		this.providerId2 = providerId2;
	}
	
	public Long getProviderId1() 
	{
		return providerId1;
	}
	
	public void setProviderId1(Long providerId1) 
	{
		this.providerId1 = providerId1;
	}
	
	public Long getProviderId2() 
	{
		return providerId2;
	}
	
	public void setProviderId2(Long providerId2) 
	{
		this.providerId2 = providerId2;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this duplicate provider ID.
	 * </p>
	 * <p>
	 * An object is equal to this duplicate provider ID, if and only if it is not 
	 * <code>null</code>, an instance of {@link DuplicateProviderId} and if
	 * the values of all of their attributes are equal.
	 * </p>
	 * 
	 * @param obj 
	 * 		Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this duplicate 
	 * 		provider ID, else <code>false</code>
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		
		if (!(obj instanceof DuplicateProviderId))
		{
			return false;
		}
		
		DuplicateProviderId that = (DuplicateProviderId) obj;
		
		// XOR
		if ((that.providerId1 == null) != (this.providerId1 == null))
		{
			return false;
		}
		
		if (that.providerId1 != null && !that.providerId1.equals(this.providerId1))
		{
			return false;
		}
		
		// XOR
		if ((that.providerId2 == null) != (this.providerId2 == null))
		{
			return false;
		}
		
		if (that.providerId2 != null && !that.providerId2.equals(this.providerId2))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this duplicate provider ID.
	 * </p>
	 * 
	 * @return Hash code
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		
		hashCode = hashCode * HASH_PRIME + 
				((this.providerId1 == null) ? 0 : this.providerId1.hashCode());
		hashCode = hashCode * HASH_PRIME +
				((this.providerId2 == null) ? 0 : this.providerId2.hashCode());
		
		return hashCode;
	}
}
