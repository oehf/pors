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
package org.openehealth.pors.admin_frontend.guiengine.dialogpresentation;

import java.util.ArrayList;
import java.util.List;

import org.openehealth.pors.admin_frontend.guiengine.dialogdata.AddressBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.LocalIdBean;

import org.openehealth.pors.admin_client.DuplicateEntryDTO;
import org.openehealth.pors.admin_client.LoggingEntryDTO;
import org.openehealth.pors.admin_client.OrganisationDTO;
import org.openehealth.pors.admin_client.ProviderDTO;

public interface IPresenter {
	
	/**
	 * calls {@link DialogCore.login()}
	 */
	public String login();
	/**
	 * calls {@link DialogCore.logout()}
	 */
	public String logout();
	/**
	 * calls {@link DialogCore.getOrganisationList()}
	 */
	public ArrayList<OrganisationDTO> getOrganisationList();
	
	/**
	 * calls {@link DialogCore.addProvider()}
	 */
	public String addProvider();
	/**
	 * calls {@link DialogCore.showSingleProvider()}
	 */
	public String showSingleProvider(ProviderDTO provider);
	/**
	 * sets editProviderDisabled to false
	 */
	public void enableEditProvider();
	/**
	 * calls {@link DialogCore.updateProvider()}
	 */
	public void saveProvider();
	/**
	 * resets the organisation list and the provider
	 */
	public void resetProvider();
	
	/**
	 * calls {@link DialogCore.addOrganization()}
	 */
	public void addOrganization();
	/**
	 * calls {@link DialogCore.setOrg(OrganisationDTO)} and disables the editing
	 */
	public String showSingleOrg(OrganisationDTO org);
	/**
	 * sets editOrganizationDisabled to false
	 */
	public void enableEditOrg();
	/**
	 * calls {@link DialogCore.updateOrg()}
	 */
	public void saveOrg();
	/**
	 * resets the organisation
	 */
	public void resetOrg();
	
	/**
	 * calls {@link DialogCore.addUser()}
	 */
	public void addUser();
	
	/**
	 * calls {@link DialogCore.setHistory(HistoryDTO)}
	 * @return result of {@link DialogCore.getLoggingDetail()}
	 */
	public String showSingleHistory(LoggingEntryDTO history);
	/**
	 * calls {@link DialogCore.loadDuplicateDetails(DuplicateEntryDTO)}
	 */
	public String showSingleDuplicate(DuplicateEntryDTO deDTO);
	
	/**
	 * replaces newAddress with new Object and sets addressModified to false
	 */
	public void resetAddress();
	/**
	 * calls {@link DialogCore.addAddress()}
	 */
	public void addAddress();
	/**
	 * saves temp object into current address
	 */
	public void editAddress();
	/**
	 * loads given address into temp object
	 */
	public void prepareEditAddress(AddressBean id);
	/**
	 * calls {@link DialogCore.removeAddress()}
	 */
	public void removeAddress();
	/**
	 * calls {@link DialogCore.removeAddress(AddressBean)}
	 */
	public void removeAddress(AddressBean id);

	/**
	 * replaces newLocalId with new Object and sets localIdModified to false
	 */
	public void resetLocalId();
	/**
	 * calls {@link DialogCore.addLocalId()}
	 */
	public void addLocalId();
	/**
	 * saves temp object into current localId
	 */
	public void editLocalId();
	/**
	 * loads given localId into temp object
	 */
	public void prepareEditLocalId(LocalIdBean id);
	/**
	 * calls {@link DialogCore.removeLocalId()}
	 */
	public void removeLocalId();
	/**
	 * calls {@link DialogCore.removeLocalId(LocalIdBean)}
	 */
	public void removeLocalId(LocalIdBean id);

	/**
	 * empty method for unused actions
	 */
	public void dummy();
	
	/**
	 * calls {@link DialogCore.getLocalIDs()}
	 */
	public List<LocalIdBean> getLocalIDs();
	/**
	 * calls {@link DialogCore.getAddresses()}
	 */
	public List<AddressBean> getAddresses();
	
	/**
	 * redirects current UI-page to start.xhtml
	 */
	public String getRedirectionToMain();
	/**
	 * redirects current UI-page to showLogin.xhtml
	 */
	public String getRedirectionToLogin();
	
	/**
	 * calls {@link DialogCore.exportProviders()}
	 */
	public void exportProviders();
	/**
	 * calls {@link DialogCore.exportOrganisations()}
	 */
	public void exportOrganisations();
	
	/**
	 * calls {@link DialogCore.importProviders()}
	 */
	public String importProviders();
	/**
	 * calls {@link DialogCore.importOrganisations()}
	 */
	public String importOrganisations();

	/**
	 * calls {@link DialogCore.updateDuplicateConfiguration()}
	 */
	public void updateDuplicateConfiguration();
	
	// methods for menu
	/**
	 * prepares objects for showProvider.xhtml
	 */
	public void loadShowProvider();
	/**
	 * prepares objects for showImportProvider.xhtml
	 */
	public void loadImportProvider();
	/**
	 * prepares objects for showOrganisation.xhtml
	 */
	public void loadShowOrganisation();
	/**
	 * prepares objects for showImportOrganisation.xhtml
	 */
	public void loadImportOrganisation();
	/**
	 * prepares objects for showHistory.xhtml
	 */
	public void loadShowHistory();
	/**
	 * prepares objects for showAddUser.xhtml
	 */
	public void loadAddUser();
	/**
	 * prepares objects for showEditUser.xhtml
	 */
	public void loadEditUser();
	/**
	 * calls {@link DialogCore.updateUser()}
	 */
	public void editUser();
}