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
 * The composed id of an 
 * {@link org.openehealth.pors.database.entities.DuplicateEntry DuplicateEntry} entity 
 * bean.
 * </p>
 * <p>
 * A {@link DuplicateEntry} object can be uniquely defined by the domain where the 
 * logging entry was created and both ids which are unique within this domain.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entiites.DuplicateEntry DuplicateEntry
 *
 */
@Embeddable
public class DuplicateEntryId implements Serializable
{
	private static final long serialVersionUID = 4827099458736146508L;
	private static final int HASH_PRIME = 31;
	
	private Long id1;
	private Long id2;
	private String domain;
	
	public Long getId1() {
		return id1;
	}
	public void setId1(Long id1) {
		this.id1 = id1;
	}
	public Long getId2() {
		return id2;
	}
	public void setId2(Long id2) {
		this.id2 = id2;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this duplicate entry id.
	 * </p>
	 * <p>
	 * An object is equal to this duplicate entry id, if and only if it is not 
	 * <code>null</code>, an instance of {@link DuplicateEntryId} object and if
	 * the values of all of their attributes are equal.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this duplicate 
	 * 		entry id, else <code>false</code>
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		
		if (!(this instanceof DuplicateEntryId))
		{
			return false;
		}
		
		DuplicateEntryId that = (DuplicateEntryId) obj;
		
		// XOR
		if ((that.id1 == null) != (that.id1 == null))
		{
			return false;
		}
		
		if (that.id1 != null && !that.id1.equals(this.id1))
		{
			return false;
		}
		
		//XOR 
		if ((that.id2 == null) != (that.id2 == null))
		{
			return false;
		}
		
		if (that.id2 != null && !that.id2.equals(this.id2))
		{
			return false;
		}
		
		// XOR
		if ((that.domain == null) != (that.id2 == null))
		{
			return false;
		}
		
		if (that.domain != null && !that.domain.equals(this.domain))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this duplicate entry id.
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
			((this.id1 == null) ? 0 : this.id1.hashCode());
		hashCode = hashCode * HASH_PRIME + 
			((this.id2 == null) ? 0 : this.id2.hashCode());
		hashCode = hashCode * HASH_PRIME + 
			((this.domain == null) ? 0 : this.domain.hashCode());
		
		return hashCode;
	}
}
