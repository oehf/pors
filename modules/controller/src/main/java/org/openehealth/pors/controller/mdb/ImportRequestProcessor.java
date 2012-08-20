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
package org.openehealth.pors.controller.mdb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.openehealth.pors.controller.IController;

import org.openehealth.pors.admin.IAdministrator;
import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.database.connector.IEntitySearcher;
import org.openehealth.pors.database.connector.IImportLogger;
import org.openehealth.pors.database.entities.ImportResult;

@MessageDriven(
	name="ImportRequestProcessor",
	activationConfig = {
		@ActivationConfigProperty(
			propertyName="destinationType",
			propertyValue="javax.jms.Queue"
		),
		@ActivationConfigProperty(
			propertyName="destination",
			propertyValue="jms/ImportQueue"
		),
		@ActivationConfigProperty(
			propertyName="transactionTimeout", 
			propertyValue="0"
		)
	}
)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ImportRequestProcessor implements MessageListener {
	
	@Resource
	private MessageDrivenContext context;
	
	@EJB
	private IAdministrator admin;
	
	@EJB
	private IImportLogger importLogger;
	
	@EJB
	private IController controller;
	
	@EJB
	private IEntitySearcher entitySearcher;
	
	private Logger logger;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void initLogging() {
		logger = Logger.getLogger(ImportRequestProcessor.class);
	}

	
	public void onMessage(Message message) {
		try {
			logger.info("onMessage called");
			ObjectMessage objectMessage = (ObjectMessage) message;
			ImportRequest request = (ImportRequest) objectMessage.getObject();
			
			ImportResult dbResult = importLogger.getImportResult(request.getResult().getJobId());
			if (dbResult.getStatusMessage() == null || dbResult.getStatusMessage().length() == 0) {
				processImportRequest(request);
			} else {
				logger.info("Message already failed! Skipping message");
			}
		} catch (JMSException e) {
			logger.error("Error processing message", e);
			context.setRollbackOnly();
		} catch (Exception e) {
			logger.info("onMessage caught exception");
			context.setRollbackOnly();
		}
	}
	
	private void processImportRequest(ImportRequest request) throws Exception {
		ImportResult result = request.getResult();
		try {
			result.setStatusMessage("Processing CSV data");
			result.setDomain(request.getDomain());
			importLogger.saveImportResult(result);
			if (request.getDomain().equals(Task.PROVIDER_STRING)) {
				List<Task> tasks = admin.importProviders(request.getCsvData(), request.getFieldList(), result);
				result.setStatusMessage("Saving changes to database for providers");
				handleTaskList(tasks, result, request);
			} else if (request.getDomain().equals(Task.ORGANISATION_STRING)) {
				List<Task> tasks = admin.importOrganisations(request.getCsvData(), request.getFieldList(), result);
				result.setStatusMessage("Saving changes to database for organisations");
				handleTaskList(tasks, result, request);
			}
			result.setStatusMessage("Complete");
			result.setEndDate(new Date());
			importLogger.saveImportResult(result);
			entitySearcher.rebuildSearchindex();
		} catch (Exception e) {
			logger.error("Error processing import", e);
			result.setStatusMessage("Failed");
			result.setException(e);
			result.setErrorMessage(e.getMessage());
			importLogger.saveImportResult(result);
			throw e;
		}
	}
	
	private void handleTaskList(List<Task> tasks, ImportResult result, ImportRequest request) throws Exception {
		importLogger.saveImportResult(result);
		boolean flushAndClear = false;
		for (int i = 0; i < tasks.size(); i++) {
			flushAndClear = false;
			if (i != 0 && i % 25 == 0) {
				flushAndClear = true;
			}
			Task task = tasks.get(i);
			controller.handleTask(task, request.getUser(), request.getIp(), request.getSessionId(), flushAndClear);
			result.setProcessed(result.getProcessed() + 1);
			importLogger.saveImportResult(result);
		}
	}
}
