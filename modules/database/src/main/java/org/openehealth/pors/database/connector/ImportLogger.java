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
package org.openehealth.pors.database.connector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;
import org.openehealth.pors.database.entities.ImportResult;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImportLogger implements IImportLogger {
	
	@EJB
	IDatabaseConnector db;
	
	private Logger logger;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void initLogging() {
		logger = Logger.getLogger(ImportLogger.class);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveImportResult(ImportResult result) {
		logger.info("saving import result...");
		if (result.getJobId() != null) {
			db.updateImportResult(result);
		} else {
			db.addImportResult(result);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImportResult getImportResult(Long id) {
		ImportResult result = new ImportResult();
		result.setJobId(id);
		return db.getImportResult(result);
	}

}
