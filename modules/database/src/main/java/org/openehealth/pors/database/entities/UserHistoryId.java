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

/**
 * <p>
 * The composed id of an 
 * {@link org.openehealth.pors.database.entities.UserHistory UserHistory} 
 * entity.
 * </p>
 * <p>
 * A {@link org.openehealth.pors.database.entities.UserHistory UserHistory} 
 * object can be uniquely defined by the domain where the logging entry was 
 * created and the domain id which is unique within this domain.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.UserHistory UserHistory
 *
 */
@Embeddable
public class UserHistoryId implements Serializable 
{
	private static final long serialVersionUID = 9165759570235119197L;
	private static final int HASH_PRIME = 31;
	
	private String domain;
	
	private Long domainId;

	public String getDomain() 
	{
		return domain;
	}

	public void setDomain(String domain) 
	{
		this.domain = domain;
	}

	public Long getDomainId() 
	{
		return domainId;
	}

	public void setDomainId(Long domainId) 
	{
		this.domainId = domainId;
	}
	
	/**
	 * <p>
	 * Tests if <code>obj</code> is equal to this user history id.
	 * </p>
	 * <p>
	 * An object is equal to this user history id, if and only if it is not 
	 * <code>null</code>, an instance of {@link HistoryId} object and if the 
	 * values of all of their attributes are equal.
	 * </p>
	 * 
	 * @param obj Object to test for equality
	 * @return <code>true</code>, if <code>obj</code> equals this user history 
	 * 		id, else <code>false</code>
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		  if (obj == this)
		  {
			  return true;
		  }
		  
		  if (!(obj instanceof UserHistoryId))
		  {
			  return false;
		  }
		    
		  UserHistoryId id = (UserHistoryId) obj;
		  
		  // XOR
		  if ((this.domain == null) != (id.domain == null))
		  {
			  return false;
		  }
		  
		  if (id.domain != null && !id.domain.equals(this.domain))
		  {
			  return false;
		  }
		  
		  // XOR
		  if ((this.domainId == null) != (id.domainId == null))
		  {
			  return false;
		  }
		  
		  if (id.domainId != null && !id.domainId.equals(this.domainId))
		  {
			  return false;
		  }
		  
		  return true;
	}
	
	/**
	 * <p>
	 * Generates a hash code value for this user history id.
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
	    		((this.domainId == null) ? 0 : this.domainId.hashCode());
	    
	    return hashCode;
	}
}
