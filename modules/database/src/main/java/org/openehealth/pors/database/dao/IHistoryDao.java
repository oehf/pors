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

import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.HistoryId;

/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.History History} 
 * objects.
 * </p>
 * 
 * @author jr
 * 
 * @see org.openehealth.pors.database.entities.History History
 */
public interface IHistoryDao extends IDao<HistoryId, History> 
{
	/**
	 * <p>
	 * Returns <code>max</code> history entries starting at 
	 * <code>position</code>.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of results
	 * @param position
	 * 		Start position of the results (beginning with 0)
	 * @return
	 * 		The history entries
	 */
	List<History> getAllMaxResultsFromPosition(int max, int position);
	
	/**
	 * <p>
	 * Returns the total number of all history entries.
	 * </p>
	 * 
	 * @return
	 * 		Number of history entries
	 */
	long getCount();
	
	/**
	 * <p>
	 * Returns the total number of all history entries created by the PORS user
	 * having <code>userId</code> as ID.
	 * </p>
	 * 
	 * @param userId
	 * 		ID of the user who created the history entries
	 * @return
	 * 		Total number of history entries by the PORS user
	 */
	long getCountByUserId(int userId);
}
