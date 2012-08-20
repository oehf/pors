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
package org.openehealth.pors.database.dao;

import java.util.List;

import org.openehealth.pors.database.entities.DuplicateEntry;
import org.openehealth.pors.database.entities.DuplicateEntryId;


/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.DuplicateEntry DuplicateEntry} 
 * objects.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.entities.DuplicateEntry DuplicateEntry
 */
public interface IDuplicateEntryDao extends IDao<DuplicateEntryId, DuplicateEntry> 
{
	/**
	 * <p>
	 * Selects the sublist of all duplicate entries containing at most 
	 * <code>max</code> entries starting at <code>position</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Starting position of the sublist
	 * @return
	 * 		Sublist of all duplicate entries
	 */
	List<DuplicateEntry> getAllMaxResultsFromPosition(int max, int position);
	
	/**
	 * <p>
	 * Selects the total number of all duplicate entries.
	 * </p>
	 * 
	 * @return
	 * 		Total number of duplicate entries
	 */
	long getCount();
}
