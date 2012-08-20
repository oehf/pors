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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * <p>
 * Entity mapping entries of table "UserRole" and thus representing a role 
 * for users which can be mapped to rights in domains via a 
 * {@link org.openehealth.pors.database.entities.PermissionMapping PermissionMapping} 
 * entity.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all available rights.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.PermissionMapping PermissionMapping
 *
 */
@Entity
@Table(name="UserRole", 
		uniqueConstraints={@UniqueConstraint(columnNames={"Name"})})
@NamedQueries(value = {
		@NamedQuery(name = "getUserRoleList", query = "SELECT ur FROM UserRole ur")
})
public class UserRole implements Serializable 
{
	private static final long serialVersionUID = 3594239099211823622L;
	private static final int HASH_PRIME = 31;
	
	public static final String QUERY_NAME_ALL = "getUserRoleList";

	@Id
	@GeneratedValue
	@Column(name="UserRoleId")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
//	@Lob
	@Column(nullable=true)
	private String description;
	
	@OneToMany(mappedBy="id.role")
	private List<PermissionMapping> permissions;

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

	public List<PermissionMapping> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionMapping> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * <p>
	 * Validates if this role has right having the name <code>rightName</code> 
	 * for the domain with name <code>domainName</code>.
	 * </p>
	 * 
	 * @param rightName 
	 * 		Name of the right to check 
	 * @param domainName
	 * 		Name of the domain where to check the right
	 * @return True, if this role has the given right for given name. False 
	 * 		otherwise.
	 */
	public boolean hasRightForDomain(final String rightName, final String domainName)
	{
		for(PermissionMapping permission : this.permissions)
		{
			if(permission.getId().getRight().getName().equals(rightName) && 
					permission.getId().getDomain().getName().equals(domainName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this role.
	 * </p>
	 * <p>
	 * An object is equal to this role, if and only if it is not 
	 * <code>null</code>, an instance of {@link UserRole} class and if the 
	 * values of name are equal. Relations to other entities and the database 
	 * id do not have relevance for equality.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this role, else 
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
		
		if (!(obj instanceof UserRole))
		{
			return false;
		}
		
		UserRole ur = (UserRole) obj;
		
		// XOR
		if ((ur.name == null) != (this.name == null))
		{
			return false;
		}
		
		if (ur.name != null && !this.name.equals(ur.name))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this role.
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
