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
package org.openehealth.pors.admin;

import java.io.StringWriter;
import java.util.List;

import javax.ejb.Local;

import org.openehealth.pors.database.entities.ImportResult;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;

import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.dto.PorsCsv;
import org.openehealth.pors.core.exception.CsvExportException;
import org.openehealth.pors.core.exception.CsvImportException;

/**
 * This interface provides functionality, which is specific for the admin.
 * 
 * @author mf, ck, tb
 * @author mbirkle
 **/
@Local
public interface IAdministrator {

	/**
	 * This method imports Providers from a given CSV file
	 * 
	 * @param PorsCsv
	 *            , the given CSV file
	 * @param List
	 *            <String>, the provider fields to import
	 * @param ImportResult
	 *            , the import entity with addEntries or updateEntries
	 * @return List<Task>
	 */
	public List<Task> importProviders(PorsCsv csvData, List<String> fieldList,
			ImportResult result) throws CsvImportException;

	/**
	 * This method imports Organisations from a given CSV file
	 * 
	 * @param PorsCsv
	 *            , the given CSV file
	 * @param List
	 *            <String>, the organisation fields to import
	 * @param ImportResult
	 *            , the import entity with addEntries or updateEntries
	 * @return List<Task>
	 **/
	public List<Task> importOrganisations(PorsCsv csvData,
			List<String> fieldList, ImportResult result)
			throws CsvImportException;

	/**
	 * This method exports Providers to a CSV file
	 * 
	 * @param List
	 *            <Provider>, the providers to export
	 * @param List
	 *            <String>, the provider fields to export
	 * @return StringWriter
	 **/
	public StringWriter exportProviders(List<Provider> providers,
			List<String> fields) throws CsvExportException;

	/**
	 * This method exports Organisations to a CSV file
	 * 
	 * @param List
	 *            <Organisation>, the organisations to export
	 * @param List
	 *            <String>, the organisation fields to export
	 * @return StringWriter
	 **/
	public StringWriter exportOrganisations(List<Organisation> organisations,
			List<String> fields) throws CsvExportException;
}
