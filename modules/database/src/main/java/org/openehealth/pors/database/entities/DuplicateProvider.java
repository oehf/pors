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
 * Entity bean mapping entries of table "duplicate_provider" and thus 
 * representing a hint on two entries in table "Provider" which could be 
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
 * <li><b>{@link #QUERY_NAME_DELETE_BY_PROVIDER_ID}</b><br />Deletes all 
 * entries where first or second provider id equals <code>PARAM_PROVIDER_ID
 * </code>.
 * </li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table( name = "duplicate_provider" )
@NamedQueries( value = { 
		@NamedQuery( name = "getDuplicateProviderList", query = "SELECT dp FROM DuplicateProvider dp"),
		@NamedQuery( name = "deleteByProviderId1OrProviderId2", query = "DELETE FROM DuplicateProvider dp WHERE dp.id.providerId1 = :providerId OR dp.id.providerId2 = :providerId" )
		} )
public class DuplicateProvider implements Serializable 
{
	private static final long serialVersionUID = -1741520322239152608L;
	
	public static final String QUERY_NAME_ALL = "getDuplicateProviderList";
	public static final String QUERY_NAME_DELETE_BY_PROVIDER_ID = "deleteByProviderId1OrProviderId2";
	
	public static final String PARAM_PROVIDER_ID = "providerId";

	@EmbeddedId
	private DuplicateProviderId id;
	
	private Double value;

	public DuplicateProviderId getId() 
	{
		return id;
	}

	public void setId(DuplicateProviderId id) 
	{
		this.id = id;
	}

	public Double getValue() 
	{
		return value;
	}

	public void setValue(Double value) 
	{
		this.value = value;
	}
}
