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
 * {@link org.openehealth.pors.database.entities.DuplicateOrganisation DuplicateOrganisation} 
 * entity bean.
 * </p>
 * <p>
 * A {@link DuplicateOrganisation} object can be uniquely defined by both IDs of the 
 * organisations which may be duplicates of each other.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entiites.DuplicateOrganisation DuplicateOrganisation
 *
 */
@Embeddable
public class DuplicateOrganisationId implements Serializable 
{
	private static final long serialVersionUID = 8978433270051871299L;
	private static final int HASH_PRIME = 31;

	private Long organisationId1;
	private Long organisationId2;
	
	/**
	 * <p>
	 * The standard constructor.
	 * </p>
	 * <p>
	 * All attributes will be <code>null</code> after initialisation.
	 * </p>
	 */
	public DuplicateOrganisationId()
	{
		
	}
	
	/**
	 * <p>
	 * Initialises this duplicate organisation ID with the IDs of two 
	 * organisations which may be duplicates of each other.
	 * </p>
	 * 
	 * @param organisationId1
	 * 		ID of the first organisation
	 * @param organisationId2
	 * 		ID of the second organisation
	 */
	public DuplicateOrganisationId(Long organisationId1, Long organisationId2)
	{
		this.organisationId1 = organisationId1;
		this.organisationId2 = organisationId2;
	}
	
	public Long getOrganisationId1() {
		return organisationId1;
	}
	public void setOrganisationId1(Long organisationId1) {
		this.organisationId1 = organisationId1;
	}
	public Long getOrganisationId2() {
		return organisationId2;
	}
	public void setOrganisationId2(Long organisationId2) {
		this.organisationId2 = organisationId2;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this duplicate organisation ID.
	 * </p>
	 * <p>
	 * An object is equal to this duplicate organisation ID, if and only if it 
	 * is not <code>null</code>, an instance of {@link DuplicateOrganisationId} 
	 * and if the values of all of their attributes are equal.
	 * </p>
	 * 
	 * @param obj 
	 * 		Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this duplicate 
	 * 		organisation ID, else <code>false</code>
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		
		if (!(obj instanceof DuplicateOrganisationId))
		{
			return false;
		}
		
		DuplicateOrganisationId that = (DuplicateOrganisationId) obj;
		
		// XOR
		if ((this.organisationId1 == null) != (that.organisationId1 == null))
		{
			return false;
		}
		
		if (that.organisationId1 != null && 
				!that.organisationId1.equals(this.organisationId1))
		{
			return false;
		}
		
		// XOR
		if ((that.organisationId2 == null) != (this.organisationId2 == null))
		{
			return false;
		}
		
		if (that.organisationId2 != null && 
				!that.organisationId2.equals(this.organisationId2))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this duplicate organisation ID.
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
				((this.organisationId1 == null) ? 0 : this.organisationId1.hashCode());
		hashCode = hashCode * HASH_PRIME +
				((this.organisationId2 == null) ? 0 : this.organisationId2.hashCode());
		
		return hashCode;
	}
}
