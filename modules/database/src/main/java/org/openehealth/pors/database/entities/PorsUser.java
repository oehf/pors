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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;

/**
 * <p>
 * Entity bean mapping entries of table "PorsUser" and thus representing a user
 * of the PORS system who can be explicitly defined by his id or name.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>getPorsUserByName</b><br />Returns the PORS user who has the name
 * specified in parameter 1</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name="PorsUser",
		uniqueConstraints= {
			@UniqueConstraint(columnNames = { "Name" }) 
		})
@NamedQueries(value = {
		@NamedQuery(name = "getPorsUserByName", query = "SELECT u FROM PorsUser u WHERE u.name = :name"),
		@NamedQuery(name = "getPorsUserList", query = "SELECT u FROM PorsUser u")
		})
public class PorsUser implements Serializable 
{
	private static final long serialVersionUID = -2668616922176632647L;
	private static final int HASH_PRIME = 31;
	
	public static final String QUERY_NAME_ALL = "getPorsUserList";
	public static final String QUERY_NAME_BY_NAME = "getPorsUserByName";
	
	public static final String PARAM_NAME = "name";

	@Id
	@GeneratedValue
	@Column(name="PorsUserId")
	private Integer id;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, 
			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="UserRoleId")
	private UserRole role;
	
	private String name;
	
	private String password;
	
	@Column(name="IsActive")
	private boolean active;

	public Integer getId() {
		return id;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this PORS user.
	 * </p>
	 * <p>
	 * An object is equal to this PORS user, if and only if it is not 
	 * <code>null</code>, an instance of {@link PorsUser} and the value of its 
	 * name attribute equals the one of this PORS user.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this PORS user, else 
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
		
		if (!(obj instanceof PorsUser))
		{
			return false;
		}
		
		PorsUser u = (PorsUser) obj;
		
		if ((u.name == null) != (this.name == null))
		{
			return false;
		}
		
		if (u.name != null && !u.name.equals(this.name))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this PORS user.
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
