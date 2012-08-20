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
 * Entity bean mapping entries of table "duplicate_organisation" and thus 
 * representing a hint on two entries in table "Organisation" which could be 
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
 * <li><b>{@link #QUERY_NAME_DELETE_BY_ORGANISATION_ID}</b><br />Deletes all 
 * entries where first or second organisation id equals 
 * <code>PARAM_ORGANISATION_ID</code>.
 * </li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name = "duplicate_organisation")
@NamedQueries( value = { 
		@NamedQuery( name = "getDuplicateOrganisationList", query = "SELECT do FROM DuplicateOrganisation do"),
		@NamedQuery( name = "deleteDuplicateOrganisationByOrganisationId1OrOrganisationId2", query = "DELETE FROM DuplicateOrganisation do WHERE do.id.organisationId1 = :organisationId OR do.id.organisationId2 = :organisationId")
		} )
public class DuplicateOrganisation implements Serializable 
{
	private static final long serialVersionUID = 476769778575826899L;
	
	public static final String QUERY_NAME_ALL = "getDuplicateOrganisationList";
	public static final String QUERY_NAME_DELETE_BY_ORGANISATION_ID = "deleteDuplicateOrganisationByOrganisationId1OrOrganisationId2";
	
	public static final String PARAM_ORGANISATION_ID = "organisationId";
	
	@EmbeddedId
	private DuplicateOrganisationId id;
	
	private Double value;

	public DuplicateOrganisationId getId() {
		return id;
	}

	public void setId(DuplicateOrganisationId id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
