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

import org.apache.log4j.Logger;
import org.openehealth.pors.database.connector.DatabaseConnectorBean;


/**
 * <p>
 * Utility class for composed (action + message) logging actions concerning 
 * class
 * {@link org.openehealth.pors.database.connector.DatabaseConnectorBean DatabaseConnectorBean}.
 * </p>
 * 
 * @author jr
 * @see org.openehealth.pors.database.connector.DatabaseConnectorBean DatabaseConnectorBean
 *
 */
public final class LoggingUtil 
{
	private static final Logger LOGGER = Logger.getLogger(DatabaseConnectorBean.class);

	public static final String LOG_ACTION_START = "Starting Action";
	
	public static final String LOG_ACTION_COMPLETE = "Completed Action";
	
	public static final String LOG_ACTION_FAIL = "Action Failed";
	
	private LoggingUtil()
	{
		
	}
	
	/**
	 * <p>
	 * Logs a composed information message.
	 * </p>
	 * 
	 * @param action
	 * 		Action of the composed logging message
	 * @param message
	 * 		Message of the composed logging message
	 */
	public static void info(String action, String message)
	{
		LOGGER.info(buildLoggingString(action, message));
	}
	
	/**
	 * <p>
	 * Logs a composed warning message.
	 * </p>
	 * 
	 * @param action
	 * 		Action of the composed logging message
	 * @param message
	 * 		Message of the composed logging message
	 */
	public static void warn(String action, String message)
	{
		LOGGER.warn(buildLoggingString(action, message));
	}
	
	/**
	 * <p>
	 * Logs a composed warning message with a Throwable attached.
	 * </p>
	 * 
	 * @param action
	 * 		Action of the composed logging message
	 * @param message
	 * 		Message of the composed logging message
	 * @param e
	 * 		Throwable to attach to the message
	 */
	public static void warn(String action, String message, Throwable e)
	{
		LOGGER.warn(buildLoggingString(action, message), e);
	}
	
	/**
	 * <p>
	 * Logs a composed error message with a Throwable attached.
	 * </p>
	 * 
	 * @param action
	 * 		Action of the composed logging message
	 * @param message
	 * 		Message of the composed logging message
	 * @param e
	 * 		Throwable to attach to the message
	 */
	public static void error(String action, String message, Throwable e)
	{
		LOGGER.error(buildLoggingString(action, message), e);
	}
	
	/**
	 * <p>
	 * Combines both parts of a composed logging message to the final message.
	 * </p>
	 * 
	 * @param action
	 * 		Action of the composed logging message
	 * @param message
	 * 		Message of the composed logging message
	 * @return Final logging message
	 */
	private static String buildLoggingString(String action, String message)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(action);
		builder.append(" - ");
		builder.append(message);
		
		return builder.toString();
	}
}
