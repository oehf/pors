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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * <p>
 * Entity bean mapping entries in table "UserRight" and thus representing a 
 * user right which takes effect in several domains it can be dedicated to.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.PermissionMapping PermissionMapping
 *
 */
@Entity
@Table(name="UserRight", 
		uniqueConstraints={@UniqueConstraint(columnNames={"Name"})})
public class UserRight implements Serializable 
{
	private static final long serialVersionUID = -7536651793879766555L;
	private static final int HASH_PRIME = 31;

	@Id
	@GeneratedValue
	@Column(name="UserRightId")
	private Integer id;
	
	private String name;
	
//	@Lob
	@Column(nullable=true)
	private String description;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this right.
	 * </p>
	 * <p>
	 * An object is equal to this right, if and only if it is not 
	 * <code>null</code>, an instance of {@link UserRight} class and if the 
	 * values of variable name are equal. Relations to other entities and the 
	 * database id do not have relevance for equality.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this right, else 
	 * 		<code>false</code>
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		
		if (!(obj instanceof UserRight))
		{
			return false;
		}
		
		UserRight ur = (UserRight) obj;
		
		// XOR
		if ((ur.name == null) != (this.name == null))
		{
			return false;
		}
		
		if (ur.name != null && !ur.name.equals(this.name))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this right.
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
				((this.name == null) ? 0 : this.name.hashCode());
		
		return hashCode;
	}
}
