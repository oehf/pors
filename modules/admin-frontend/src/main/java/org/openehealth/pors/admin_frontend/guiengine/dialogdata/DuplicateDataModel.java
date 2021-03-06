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
package org.openehealth.pors.admin_frontend.guiengine.dialogdata;

import org.openehealth.pors.admin_client.DuplicateEntryDTO;
import org.openehealth.pors.admin_client.IPorsAdministratorService;
import org.openehealth.pors.admin_client.UserDTO;


public class DuplicateDataModel extends AbstractDataModel<DuplicateEntryDTO> {

	private static final long serialVersionUID = 405829403327304771L;

	public DuplicateDataModel(IPorsAdministratorService connector, UserDTO user) {
		super(new DuplicateDataProvider(connector, user));
	}
	
}