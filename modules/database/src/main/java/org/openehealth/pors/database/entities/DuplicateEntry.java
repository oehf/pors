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
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * <p>
 * Entity bean mapping entries in view "DuplicateView" and thus representing a 
 * duplicate entry from tables "Duplicate_Provider" or "Duplicate_Organisation" 
 * or "Duplicate_Address" which can be explicitly defined by its two ids and 
 * the domain it is belonging to.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>QUERY_NAME_ALL</b><br />Selects all available duplicate 
 * entries.</li>
 * <li><b>QUERY_NAME_COUNT_ALL</b><br />Counts all available duplicate entries.
 * </li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(name = "DuplicateView")
@NamedQueries( value = {
		@NamedQuery( name = "getDuplicateEntryList", query = "SELECT de FROM DuplicateEntry de ORDER BY de.value DESC" ),
		@NamedQuery( name = "countAllDuplicateEntries", query = "SELECT COUNT(de) FROM DuplicateEntry de" )
})
public class DuplicateEntry implements Serializable
{
	private static final long serialVersionUID = 4425697827570081701L;
	
	public static final String QUERY_NAME_ALL = "getDuplicateEntryList";
	public static final String QUERY_NAME_COUNT_ALL = "countAllDuplicateEntries";
	
	public static final String DOMAIN_ADDRESS = "Address";
	public static final String DOMAIN_PROVIDER = "Provider";
	public static final String DOMAIN_ORGANISATION = "Organisation";
	
	@EmbeddedId
	private DuplicateEntryId id;
	
	private Double value;
	
	private Date timestamp;

	public DuplicateEntryId getId() {
		return id;
	}

	public void setId(DuplicateEntryId id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
