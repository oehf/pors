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
package org.openehealth.pors.communication;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.openehealth.pors.controller.IController;
import org.openehealth.pors.core.dto.UserDTO;
import org.openehealth.pors.core.exception.InvalidHL7MessageException;

/**
 * Implementation for the PORS external Web Service.
 * @see IPorsExternalService
 * @author mf
 *
 */
@Stateless
@WebService(endpointInterface="org.openehealth.pors.communication.IPorsExternalService",serviceName="PORSExternalService")
@SOAPBinding(style=javax.jws.soap.SOAPBinding.Style.DOCUMENT)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PorsExternalServiceBean implements IPorsExternalService {
	
	@EJB
	private IController controller;
	
	@Resource
	private SessionContext context;
	
	@Resource
    private WebServiceContext wsContext;

	/**
	 * @see IPorsExternalService#sendHL7Message(String, UserDTO)
	 */
	@WebMethod
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String sendHL7Message(String msg, UserDTO userDTO) {
		try {
			if (userDTO.getIp() == null || userDTO.getIp().length() == 0) {
				HttpServletRequest request = (HttpServletRequest) wsContext
						.getMessageContext()
						.get(MessageContext.SERVLET_REQUEST);
				userDTO.setIp(request.getRemoteAddr());
			}
			return controller.processMessage(msg, userDTO);
		} catch (InvalidHL7MessageException e) {
			context.setRollbackOnly();
			return e.getHl7Message();
		}
	}

}
