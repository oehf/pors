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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EmbeddedId;
import javax.persistence.Transient;

/**
 * <p>
 * Entity representing permission mappings as defined in table "RoleHasRightDomain".
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name="RoleHasRightForDomain")
public class PermissionMapping implements Serializable {
	@Transient
	private static final long serialVersionUID = -4928718097125811780L;
	
	@EmbeddedId
	private PermissionMappingPk id;
//	@Id
//	@ManyToOne
//	@JoinColumn(name="UserRoleId")
//	private UserRole role;
//	
//	@Id
//	@ManyToOne
//	@JoinColumn(name="UserRightId")
//	private UserRight right;
//	
//	@Id
//	@ManyToOne
//	@JoinColumn(name="UserDomainId")
//	private UserDomain domain;
//
	/**
	 * <p>
	 * Pseudo-getter returning the user role of this primary key.
	 * </p>
	 */
	public UserRole getRole() {
		return this.id.getRole();
	}
//
//	public void setRole(UserRole role) {
//		this.role = role;
//	}
//
	/**
	 * <p>
	 * Pseudo-getter returning user right of this primary key.
	 * </p>
	 */
	public UserRight getRight() {
		return this.id.getRight();
	}
//
//	public void setRight(UserRight right) {
//		this.right = right;
//	}
//
	/**
	 * <p>
	 * Pseudo-getter returning domain of this primary key.
	 * </p>
	 */
	public UserDomain getDomain() {
		return this.id.getDomain();
	}

//	public void setDomain(UserDomain domain) {
//		this.domain = domain;
//	}

	public PermissionMappingPk getId() {
		return id;
	}

	public void setId(PermissionMappingPk id) {
		this.id = id;
	}
}
