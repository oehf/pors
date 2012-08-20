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
package org.openehealth.pors.external_system;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import org.openehealth.pors.pors_service_client.IPorsExternalService;
import org.openehealth.pors.pors_service_client.PORSExternalService;
import org.openehealth.pors.pors_service_client.UserDTO;

public class ExternalSystem {
	
	private static Logger logger = Logger.getRootLogger();
	
	/**
	 * @param args user password hl7message
	 */
	public static void main(String[] args) {
	    logger.addAppender(new ConsoleAppender(new SimpleLayout()));
	    logger.setLevel(Level.ALL);

		if (args == null || args.length == 0) {
			logger.error("Missing arguments! Required arguments: user password hl7message");
			return;
		}
		logger.debug("Creating service and port object");
		PORSExternalService service = new PORSExternalService();
		logger.debug("service object created");
		IPorsExternalService port = service.getPorsExternalServiceBeanPort();
		logger.debug("port object created");
		
		logger.info("User: " + args[0]);
		logger.info("Password: " + args[1]);
		
		UserDTO dto = new UserDTO();
		dto.setUsername(args[0]);
		dto.setPassword(args[1]);
		
		String msg = args[2];
		
		
		logger.info("HL7 Message:\n" + msg);
		
		logger.debug("calling service");
		try {
			String result = port.sendHL7Message(msg, dto);
			logger.debug("service called");
			logger.info("Result:\n" + result);
		} catch (SOAPFaultException ex) {
			logger.error("Exception was: " + ex.getMessage());
		}
	}

}
