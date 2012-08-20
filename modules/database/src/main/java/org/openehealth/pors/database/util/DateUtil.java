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
package org.openehealth.pors.database.util;

import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Utility class providing operations concerning Date or Time objects.
 * </p>
 * 
 * @author jr
 *
 */
public final class DateUtil 
{
	private DateUtil()
	{
		
	}
	
	/**
	 * <p>
	 * Two dates are equal if they have the same date (differing time values will 
	 * be ignored) or if <code>d1</code> and <code>d2</code> are both null.
	 * </p>
	 * @param d1 First date for comparison
	 * @param d2 Second date for comparison
	 * @return
	 * True, if equal. False, else.
	 */
	public static boolean equalsIgnoreTime(final Date d1, final Date d2)
	{
		if ((d1 == null) && (d2 == null))
		{
			return true;
		}
		
		if (d1 != null && d2 != null)
		{
			Calendar c1 = Calendar.getInstance();
			c1.setTimeInMillis(d1.getTime());
		
			Calendar c2 = Calendar.getInstance();
			c2.setTimeInMillis(d2.getTime());
			
			if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && 
					c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
					c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
			{
				return true;
			}
		}
		
		return false;
	}
}
