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
package org.openehealth.pors.admin_frontend.guiengine.dialogcore;

import org.openehealth.pors.admin_frontend.guiengine.dialogdata.UserFormBean;

import org.openehealth.pors.admin_client.UserDTO;

public class Assembler {
	
	public UserDTO assembleUserDTO(UserFormBean user){
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(user.getUsername());
		userDTO.setPassword(user.getPassword());
		return userDTO;
	}
	
	public UserFormBean assembleUserFormBean(UserDTO user){
		UserFormBean userFormBean = new UserFormBean();
		userFormBean.setUsername(user.getUsername());
		userFormBean.setPassword(user.getPassword());
		return userFormBean;
	}
	
}