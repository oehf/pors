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

import java.util.ArrayList;

import org.openehealth.pors.admin_frontend.guiengine.dialogdata.LogTableBean;

import org.openehealth.pors.admin_client.DuplicateConfigurationDTO;
import org.openehealth.pors.admin_client.DuplicateEntryDTO;
import org.openehealth.pors.admin_client.UserDTO;

public interface IDialogCore {

	/**
	 * checks the credentials and logs the current user in
	 */
	public String login();

	/**
	 * logs the current user out
	 */
	public String logout();

	/**
	 * adds current provider to database
	 * @return
	 */
	public String addProvider();

	/**
	 * loads details for chosen history entry
	 * @return
	 */
	public String getLoggingDetail();

	/**
	 * adds current organisation to database
	 * @return
	 */
	public void addOrganization();

	/**
	 * fills organisation list with database data
	 */
	public void showAllOrganizations();

	/**
	 * updates current provider with edited fields 
	 */
	public void updateOrg();

	/**
	 * updates current organisation with edited fields 
	 */
	public void updateProvider();

	/**
	 * adds current user to database
	 * @return
	 */
	public void addUser();

	/**
	 * gets history data for provider logging entry
	 * @return
	 */
	public ArrayList<LogTableBean> getProviderHistoryData();

	/**
	 * gets history data for organisation logging entry
	 * @return
	 */
	public ArrayList<LogTableBean> getOrganisationHistoryData();

	/**
	 * gets history data for address logging entry
	 * @return
	 */
	public ArrayList<LogTableBean> getAddressHistoryData();

	/**
	 * gets history data for localId logging entry
	 * @return
	 */
	public ArrayList<LogTableBean> getLocalIdHistoryData();
	
	/**
	 * gets duplicate configuration
	 * @return
	 */
	public DuplicateConfigurationDTO getDuplicateConfiguration();

	/**
	 * sets duplicate configuration
	 */
	public void updateDuplicateConfiguration();
	

	/**
	 * loads details for given DuplicateEntryDTO
	 */
	public void loadDuplicateDetails(DuplicateEntryDTO deDTO);
	

	/**
	 * exports selected provider to csv file
	 */
	public void exportProviders();

	/**
	 * exports selected organisation to csv file
	 */
	public void exportOrganisations();

	/**
	 * imports provider from csv file to database
	 */
	public String importProviders();

	/**
	 * imports organisations from csv file to database
	 */
	public String importOrganisations();

	public void updateUser(UserDTO user);
}
