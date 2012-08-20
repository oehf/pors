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
package org.openehealth.pors.communication_servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openehealth.pors.communication.IPorsExternalService;
import org.openehealth.pors.core.dto.UserDTO;

/**
 * Represents a servlet which can be used to send HL7 Messages
 * to the system.
 * 
 * @author mf
 *
 */
public class PorsServlet extends HttpServlet {

	/**
	 * default UID
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MSG_PARAM = "msg";
	private static final String USER_PARAM = "user";
	private static final String PW_PARAM = "password";
	
	@EJB
	private IPorsExternalService porsService;
	
	/**
	 * Implements HTTP Get
	 * @param request the http request
	 * @param response the http response
	 * @throws ServletException for a servlet error, e.g. wrong parameters
	 * @throws IOException 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = request.getParameter(MSG_PARAM);
		String user = request.getParameter(USER_PARAM);
		String password = request.getParameter(PW_PARAM);
		
		if (msg == null || user == null || password == null) {
			throw new ServletException("Parameters missing: msg, user or password not found.");
		}
		
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(user);
		userDTO.setPassword(password);
		userDTO.setIp(request.getRemoteAddr());
		
		String returnMsg = porsService.sendHL7Message(msg, userDTO);
		
		PrintWriter writer = response.getWriter();
		writer.print(returnMsg);
		writer.flush();
		writer.close();
	}

}
