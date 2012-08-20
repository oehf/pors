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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * <p>
 * Entity bean mapping entries of table "DupliateRecognition" and thus 
 * representing allocations of columns of several tables to their weights in 
 * duplicate recognition algorithms.
 * </p>
 * <p>
 * Supports the following named queries:
 * <ul>
 * <li><b>{@link #QUERY_NAME_ALL}</b><br />Selects all possible duplicates.
 * </li>
 * </ul>
 * </p>
 * 
 * @author jr
 *
 */
@Entity
@NamedQueries( value = { 
		@NamedQuery( name = "getDuplicateRecognitionList", query = "SELECT dr FROM DuplicateRecognition dr") 
		} )
public class DuplicateRecognition implements Serializable 
{
	private static final long serialVersionUID = -7940544102702067678L;
	
	public static final String QUERY_NAME_ALL = "getDuplicateRecognitionList";
	
	public static final String ADDRESS_PREFIX = "a.";
	public static final String PROVIDER_PREFIX = "p.";
	public static final String ORGANISATION_PREFIX = "o.";
	
	public static final String LOWERTHRESHOLD = "lowerthreshold";
	public static final String UPPERTHRESHOLD = "upperthreshold";
	
	public static final int NOTHING_CHANGED = 0;
	public static final int ALL_CHANGED = 1;
	public static final int PROVIDER_CHANGED = 2;
	public static final int ORGANISATION_CHANGED = 3;

	public DuplicateRecognition(){
	}
	
	
	public DuplicateRecognition(String name, int value){
		this.name = name;
		this.value = value;
	}
	
	
	@Id
	private String name;
	
	private Integer value;

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public Integer getValue() 
	{
		return value;
	}

	public void setValue(Integer value) 
	{
		this.value = value;
	}
}
