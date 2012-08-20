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

import org.openehealth.pors.database.entities.UserHistory;
import org.openehealth.pors.database.entities.UserHistoryId;


/**
 * <p>
 * An {@link org.openehealth.pors.database.dao.IDao IDao} interface 
 * providing methods for accessing 
 * {@link org.openehealth.pors.database.entities.UserHistory UserHistory} 
 * objects.
 * </p>
 * 
 * @author jr
 * 
 * @see org.openehealth.pors.database.entities.PorsUser PorsUser
 */
public interface IUserHistoryDao extends IDao<UserHistoryId, UserHistory> 
{
	/**
	 * <p>
	 * Selects all user history entries which belong to the user having 
	 * <code>userId</code> as ID.
	 * </p>
	 * 
	 * @param userId
	 * 		User id by which to select user history entries
	 * @return
	 * 		User history entries of user having the given id
	 */
	List<UserHistory> getAllByUserId(Integer userId);
	
	/**
	 * <p>
	 * Selects at most <code>max</code> user history entries starting from 
	 * <code>position</code> which belong to the user having 
	 * <code>userId</code> as ID.
	 * </p>
	 * 
	 * @param max
	 * 		Maximum number of entries
	 * @param position
	 * 		Position of the first entry
	 * @param userId
	 * 		ID of the user whose user history entries have to be returned
	 * @return
	 * 		Matching user history entries
	 */
	List<UserHistory> getAllMaxResultsFromPositionByUserId(int max, int position, int userId);
}
