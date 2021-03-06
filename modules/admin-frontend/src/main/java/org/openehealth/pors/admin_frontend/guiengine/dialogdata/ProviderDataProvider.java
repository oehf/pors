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

import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.openehealth.pors.admin_frontend.guiengine.util.MessageHandler;

import org.openehealth.pors.admin_client.IPorsAdministratorService;
import org.openehealth.pors.admin_client.PORSException_Exception;
import org.openehealth.pors.admin_client.ProviderDTO;
import org.openehealth.pors.admin_client.UserDTO;

@SuppressWarnings("restriction")
public class ProviderDataProvider extends AbstractDataProvider<ProviderDTO> {

	private static final long serialVersionUID = -8728025367953265370L;

	public ProviderDataProvider(IPorsAdministratorService connector, UserDTO user) {
		super(connector, user);
	}
	
	@Override
	protected void initLogger() {
		logger = Logger.getLogger(ProviderDataProvider.class);
	}

	@Override
	protected int getWSRowCount() {
		try {
			return (int) connector.getProviderCount(user);
		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(e.getFaultInfo().getErrorDescription());
		} catch (SOAPFaultException e) {
			MessageHandler.getInstance().addErrorMessage(e.getMessage());
		}
		return 0;
	}

	@Override
	protected List<ProviderDTO> getWSItemsByRange(int firstRow, int numberOfRows) {
		try {
			return connector.getProviders(user, numberOfRows, firstRow);
		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(e.getFaultInfo().getErrorDescription());
		} catch (SOAPFaultException e) {
			MessageHandler.getInstance().addErrorMessage(e.getMessage());
		}
		return null;
	}

	@Override
	public Object getKey(ProviderDTO item) {
		return item.getId();
	}
	
}