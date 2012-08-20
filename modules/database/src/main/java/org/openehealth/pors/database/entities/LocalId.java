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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.NamedQuery;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * <p>
 * Entity bean mapping entries of table "LocalId" and thus representing a local
 * id which can be explicitly defined by its id or a combination of the fields 
 * localId, facility and application.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>{@link #QUERY_NAME_ALL}</b><br />Selects all available local IDs.
 * </li>
 * <li><b>{@link #QUERY_NAME_BY_UNIQUE_COMBINATION}</b><br />Selects the local 
 * ID data set, which has a specific combination of local ID, application and 
 * facility. These attributes can be set using parameters 
 * <code>PARAM_LOCALID</code>, <code>PARAM_APPLICATION</code> and 
 * <code>PARAM_FACILITY</code>.</li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = { "LocalId", "Facility", "Application" })
		})
@NamedQueries(value = {
		@NamedQuery(name = "getLocalIdByLocalIdFacilityApplication", query = "SELECT lid FROM LocalId lid WHERE lid.localId = :localid AND lid.facility = :facility AND lid.application = :application"),
		@NamedQuery(name = "getLocalIdList", query = "SELECT lid FROM LocalId lid")
		})
@Indexed
public class LocalId implements Serializable 
{
	private static final long serialVersionUID = -8369427748798737937L;
	private static final int HASH_PRIME = 31;
	
	public static final String QUERY_NAME_ALL = "getLocalIdList";
	public static final String QUERY_NAME_BY_UNIQUE_COMBINATION = "getLocalIdByLocalIdFacilityApplication";
	
	public static final String PARAM_LOCALID = "localid";
	public static final String PARAM_FACILITY = "facility";
	public static final String PARAM_APPLICATION = "application";
	
	@Transient
	private boolean newlyInserted;
	
	@Transient
	private boolean printRelations = false;

	@Id
	@GeneratedValue
	@Column(name="LocalIdId")
	private Long id;
	
	@ManyToOne(optional = true)
	@ContainedIn
	@JoinColumn(name="RegionalOrganisationId", nullable=true)
	private Organisation organisation;
	
	@ManyToOne(optional = true)
	@ContainedIn
	@JoinColumn(name="RegionalProviderId", nullable=true)
	private Provider provider;
	
	@Field
	private String localId;
	
	@Field
	private String facility;
	
	@Field
	private String application;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}

	public Organisation getOrganisation() 
	{
		return organisation;
	}

	public void setOrganisation(Organisation organisation) 
	{
		this.organisation = organisation;
	}

	public Provider getProvider() 
	{
		return provider;
	}

	public void setProvider(Provider provider) 
	{
		this.provider = provider;
	}

	public String getLocalId() 
	{
		return localId;
	}

	public void setLocalId(String localId) 
	{
		this.localId = localId;
	}

	public void setFacility(String facility) 
	{
		this.facility = facility;
	}

	public String getFacility() 
	{
		return facility;
	}

	public void setNewlyInserted(boolean newlyInserted) 
	{
		this.newlyInserted = newlyInserted;
	}

	public boolean isNewlyInserted() 
	{
		return newlyInserted;
	}
	
	public void setApplication(String application) 
	{
		this.application = application;
	}

	public String getApplication() 
	{
		return application;
	}
	
	public boolean isPrintRelations() 
	{
		return this.printRelations;
	}

	public void setPrintRelations(boolean printRelations) 
	{
		this.printRelations = printRelations;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this local id.
	 * </p>
	 * <p>
	 * An object is equal to this local id if and only if it is not null, an 
	 * instance of {@link LocalId} and its attributes local id, facility and 
	 * application are equal to the corresponding attributes in this local id. 
	 * Relations to other entities do not have relevance for equality.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this local id, else 
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
		
		if (!(obj instanceof LocalId))
		{
			return false;
		}
		
		LocalId that = (LocalId) obj;
		
		// XOR
		if ((that.localId == null) != (this.localId == null))
		{
			return false;
		}
		
		if (that.localId != null && !that.localId.equals(this.localId))
		{
			return false;
		}
		
		// XOR
		if ((that.facility == null) != (this.facility == null))
		{
			return false;
		}
		
		if (that.facility != null && !that.facility.equals(this.facility))
		{
			return false;
		}
		
		// XOR
		if ((that.application == null) != (this.application == null))
		{
			return false;
		}
		
		if (that.application != null && 
				!that.application.equals(this.application))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this local id.
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
				((this.localId == null) ? 0 : this.localId.hashCode());
		hashCode = hashCode * HASH_PRIME + 
				((this.application == null) ? 0 : this.application.hashCode());
		hashCode = hashCode * HASH_PRIME + 
				((this.facility == null) ? 0 : this.facility.hashCode());
		
		return hashCode;
	}

	/**
	 * <p>
	 * Returns a string representation of this object and its content having 
	 * the following format:
	 * </p>
	 * <p>
	 * [LocalId: id = ?; localId = ?; facility = ?; application = ?]
	 * </p>
	 * <p>
	 * Set {@link #setPrintRelations(boolean)} to <code>true</code> for 
	 * also printing additional information about relations to 
	 * {@link org.openehealth.pors.database.entities.Organisation Organisation}
	 * and 
	 * {@link org.openehealth.pors.database.entities.Provider Provider}
	 * objects.
	 * </p>
	 * 
	 * @return String representation of this object
	 * @see Object#toString(Object)
	 */
	@Override
	public String toString()
	{
		String nullStr = "null";
		StringBuilder builder = new StringBuilder();
		
		builder.append("[LocalId: id = ");
		
		if (this.id == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append(String.valueOf(this.id));
		}
		
		builder.append("; localId = ");
		
		if (this.localId == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.localId);
			builder.append("\"");
		}
		
		builder.append("; facility = ");
		
		if (this.facility == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.facility);
			builder.append("\"");
		}
		
		builder.append("; application = ");
		
		if (this.application == null)
		{
			builder.append(nullStr);
		}
		else
		{
			builder.append("\"");
			builder.append(this.application);
			builder.append("\"");
		}
		
		if (this.printRelations)
		{
			builder.append("; provider ");
			
			if (provider != null)
			{
				builder.append("!");
			}
			
			builder.append("= null; provider.id = ");
			
			if (this.provider == null || this.provider.getId() == null)
			{
				builder.append(nullStr);
			}
			else
			{
				builder.append(this.provider.getId().toString());
			}
			
			builder.append("; organisation ");
			
			if (this.organisation != null)
			{
				builder.append("!");
			}
			
			builder.append("= null; organisation.id = ");
			
			if (this.organisation == null || this.organisation.getId() == null)
			{
				builder.append(nullStr);
			}
			else
			{
				builder.append(this.organisation.getId().toString());
			}
		}
		
		builder.append("]");
		
		return builder.toString();
	}
}
