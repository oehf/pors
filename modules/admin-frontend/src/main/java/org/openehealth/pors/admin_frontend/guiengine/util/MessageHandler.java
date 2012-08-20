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
package org.openehealth.pors.admin_frontend.guiengine.util;

import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class MessageHandler {
	
	private static MessageHandler instance;
	
	private MessageHandler() {
	}
	
	public static MessageHandler getInstance() {
		if (instance == null) {
			instance = new MessageHandler();
		}
		return instance;
	}
	
	public void addErrorMessage(String msg) {
		addMessage(msg, FacesMessage.SEVERITY_ERROR, "dummyMessage");
	}
	
	public void addInfoMessage(String msg) {
		addMessage(msg, FacesMessage.SEVERITY_INFO, "dummyMessage");
	}
	
	private void addMessage(String message, Severity severity, String componentId) {
		FacesMessage msg = new FacesMessage();
		msg.setDetail(message);
		msg.setSeverity(severity);
		UIComponent uic = getComponent(componentId, FacesContext.getCurrentInstance().getViewRoot());
		FacesContext.getCurrentInstance().addMessage(uic.getClientId(FacesContext.getCurrentInstance()), msg);
	}
	
	private UIComponent getComponent(String componentId, UIComponent parent) {
		if(componentId.equals(parent.getId())) {
    		return parent;
    	}
    	Iterator<UIComponent> kids = parent.getFacetsAndChildren();
    	while(kids.hasNext()) {
    		UIComponent kid = kids.next();
    		UIComponent found = getComponent(componentId, kid);
    		if(found != null) {
    			return found;
    		}
    	}
    	return null;
	}

}
