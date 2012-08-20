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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * <p>
 * The composed id of a 
 * {@link org.openehealth.pors.database.entities.PermissionMapping PermissionMapping} 
 * object.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.PermissionMapping PermissionMapping
 */
@Embeddable
public class PermissionMappingPk implements Serializable 
{
	private static final long serialVersionUID = -8529057628425457846L;
	private static final int HASH_PRIME = 31;

	@ManyToOne
	@JoinColumn(name="UserRoleId")
	private UserRole role;
	
	@ManyToOne
	@JoinColumn(name="UserRightId")
	private UserRight right;
	
	@ManyToOne
	@JoinColumn(name="UserDomainId")
	private UserDomain domain;

	public UserRole getRole() 
	{
		return role;
	}

	public void setRole(UserRole role) 
	{
		this.role = role;
	}

	public UserRight getRight() 
	{
		return right;
	}

	public void setRight(UserRight right) 
	{
		this.right = right;
	}

	public UserDomain getDomain() 
	{
		return domain;
	}

	public void setDomain(UserDomain domain) 
	{
		this.domain = domain;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this permission mapping primary 
	 * key.
	 * </p>
	 * <p>
	 * An object is equal to this primary key, if and only if it is not 
	 * <code>null</code>, an instance of {@link PermissionMappingPk} class and 
	 * if their attributes are equal.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this primary key, 
	 * 		else <code>false</code>
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		  if (obj == this)
		  {
			  return true;
		  }
		  
		  if (!(obj instanceof PermissionMappingPk))
		  {
			  return false;
		  }
		    
		  PermissionMappingPk pk = (PermissionMappingPk) obj;
		  
		  // XOR
		  if ((pk.role == null) != (this.role == null))
		  {
			  return false;
		  }
		  
		  if (pk.role != null && !pk.role.equals(this.role))
		  {
			  return false;
		  }
		  
		  // XOR
		  if ((pk.right == null) != (this.right == null))
		  {
			  return false;
		  }
		  
		  if (pk.right != null && !pk.right.equals(this.right))
		  {
			  return false;
		  }
		  
		  // XOR
		  if ((pk.domain == null) != (this.domain == null))
		  {
			  return false;
		  }
		  
		  if (pk.domain != null && !pk.domain.equals(this.domain))
		  {
			  return false;
		  }
		  
		  return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this permission mapping primary key.
	 * </p>
	 * 
	 * @return Hash code
	 * @see Object#hashCode()
	 */
	public int hashCode() 
	{
	    int hashCode = 1;
	    
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.role == null) ? 0 : this.role.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.right == null) ? 0 : this.right.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.domain == null) ? 0 : this.domain.hashCode());
	    
	    return hashCode;
	}
}
