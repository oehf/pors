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
import javax.persistence.Embeddable;

/**
 * <p>
 * The composed id of an 
 * {@link org.openehealth.pors.database.entities.History History} entity 
 * bean.
 * </p>
 * <p>
 * A {@link History} object can be uniquely defined by the domain where the 
 * logging entry was created and the log id which is unique within this domain.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entiites.History History
 *
 */
@Embeddable
public class HistoryId implements Serializable 
{
	private static final long serialVersionUID = -5865569881865703273L;
	private static final int HASH_PRIME = 31;
	
	private String domain;
	
	@Column(name="Id")
	private Long logId;

	public String getDomain() 
	{
		return domain;
	}

	public void setDomain(String domain) 
	{
		this.domain = domain;
	}

	public Long getLogId() 
	{
		return logId;
	}

	public void setLogId(Long logId) 
	{
		this.logId = logId;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this history id.
	 * </p>
	 * <p>
	 * An object is equal to this history id, if and only if it is not 
	 * <code>null</code>, an instance of {@link HistoryId} object and if the 
	 * values of all of their attributes are equal.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this history id, 
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
		  
		  if (!(obj instanceof HistoryId))
		  {
			  return false;
		  }
		    
		  HistoryId id = (HistoryId) obj;
		  
		  // XOR
		  if ((id.domain == null) != (id.domain == null))
		  {
			  return false;
		  }
		  
		  if (id.domain != null && !id.domain.equals(this.domain))
		  {
			  return false;
		  }
		  
		  // XOR
		  if ((id.logId == null) && (this.logId == null))
		  {
			  return false;
		  }
		  
		  if (id.logId != null && !id.logId.equals(this.logId))
		  {
			  return false;
		  }
		  
		  return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this history id.
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
	    		((this.domain == null) ? 0 : this.domain.hashCode());
	    hashCode = hashCode * HASH_PRIME + 
	    		((this.logId == null) ? 0 : this.logId.hashCode());
	    
	    return hashCode;
	}
}
