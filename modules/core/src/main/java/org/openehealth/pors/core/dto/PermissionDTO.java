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
package org.openehealth.pors.core.dto;

/**
 * The DTO for Permission
 * 
 * @author mf
 * 
 */
public class PermissionDTO {
	/**
	 * Create
	 */
	private boolean createProvider;
	private boolean createOrganisation;
	private boolean createUser;

	/**
	 * Read
	 */
	private boolean readProvider;
	private boolean readOrganisation;
	private boolean readUser;
	private boolean readLogging;
	private boolean readAddress;
	private boolean readDuplicate;

	/**
	 * update
	 */
	private boolean updateProvider;
	private boolean updateOrganisation;
	private boolean updateUser;
	private boolean updateAddress;
	private boolean updateDuplicate;

	/**
	 * Deactivate
	 */
	private boolean deactivateProvider;
	private boolean deactivateOrganisation;
	private boolean deactivateUser;

	/**
	 * Reactivate
	 */
	private boolean reactivateProvider;
	private boolean reactivateOrganisation;
	private boolean reactivateUser;

	/**
	 * Read all
	 */
	private boolean readAllProvider;
	private boolean readAllOrganisation;
	private boolean readAllUser;
	private boolean readAllLogging;

	/**
	 * update all
	 */
	private boolean updateAllProvider;
	private boolean updateAllOrganisation;
	private boolean updateAllUser;

	/**
	 * Deactivate
	 */
	private boolean deactivateAllProvider;
	private boolean deactivateAllOrganisation;
	private boolean deactivateAllUser;

	/**
	 * Reactivate
	 */
	private boolean reactivateAllProvider;
	private boolean reactivateAllOrganisation;
	private boolean reactivateAllUser;
	
	/**
	 * Configure
	 */
	private boolean configureDuplicate;
	private boolean configureUser;
	private boolean configureSystem;

	public boolean isCreateProvider() {
		return createProvider;
	}

	public void setCreateProvider(boolean createProvider) {
		this.createProvider = createProvider;
	}

	public boolean isCreateOrganisation() {
		return createOrganisation;
	}

	public void setCreateOrganisation(boolean createOrganisation) {
		this.createOrganisation = createOrganisation;
	}

	public boolean isCreateUser() {
		return createUser;
	}

	public void setCreateUser(boolean createUser) {
		this.createUser = createUser;
	}

	public boolean isReadProvider() {
		return readProvider;
	}

	public void setReadProvider(boolean readProvider) {
		this.readProvider = readProvider;
	}

	public boolean isReadOrganisation() {
		return readOrganisation;
	}

	public void setReadOrganisation(boolean readOrganisation) {
		this.readOrganisation = readOrganisation;
	}

	public boolean isReadUser() {
		return readUser;
	}

	public void setReadUser(boolean readUser) {
		this.readUser = readUser;
	}

	public boolean isReadLogging() {
		return readLogging;
	}

	public void setReadLogging(boolean readLogging) {
		this.readLogging = readLogging;
	}

	public boolean isUpdateProvider() {
		return updateProvider;
	}

	public void setUpdateProvider(boolean updateProvider) {
		this.updateProvider = updateProvider;
	}

	public boolean isUpdateOrganisation() {
		return updateOrganisation;
	}

	public void setUpdateOrganisation(boolean updateOrganisation) {
		this.updateOrganisation = updateOrganisation;
	}

	public boolean isUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(boolean updateUser) {
		this.updateUser = updateUser;
	}

	public boolean isDeactivateProvider() {
		return deactivateProvider;
	}

	public void setDeactivateProvider(boolean deactivateProvider) {
		this.deactivateProvider = deactivateProvider;
	}

	public boolean isDeactivateOrganisation() {
		return deactivateOrganisation;
	}

	public void setDeactivateOrganisation(boolean deactivateOrganisation) {
		this.deactivateOrganisation = deactivateOrganisation;
	}

	public boolean isDeactivateUser() {
		return deactivateUser;
	}

	public void setDeactivateUser(boolean deactivateUser) {
		this.deactivateUser = deactivateUser;
	}

	public boolean isReactivateProvider() {
		return reactivateProvider;
	}

	public void setReactivateProvider(boolean reactivateProvider) {
		this.reactivateProvider = reactivateProvider;
	}

	public boolean isReactivateOrganisation() {
		return reactivateOrganisation;
	}

	public void setReactivateOrganisation(boolean reactivateOrganisation) {
		this.reactivateOrganisation = reactivateOrganisation;
	}

	public boolean isReactivateUser() {
		return reactivateUser;
	}

	public void setReactivateUser(boolean reactivateUser) {
		this.reactivateUser = reactivateUser;
	}

	public boolean isReadAllProvider() {
		return readAllProvider;
	}

	public void setReadAllProvider(boolean readAllProvider) {
		this.readAllProvider = readAllProvider;
	}

	public boolean isReadAllOrganisation() {
		return readAllOrganisation;
	}

	public void setReadAllOrganisation(boolean readAllOrganisation) {
		this.readAllOrganisation = readAllOrganisation;
	}

	public boolean isReadAllUser() {
		return readAllUser;
	}

	public void setReadAllUser(boolean readAllUser) {
		this.readAllUser = readAllUser;
	}

	public boolean isReadAllLogging() {
		return readAllLogging;
	}

	public void setReadAllLogging(boolean readAllLogging) {
		this.readAllLogging = readAllLogging;
	}

	public boolean isUpdateAllProvider() {
		return updateAllProvider;
	}

	public void setUpdateAllProvider(boolean updateAllProvider) {
		this.updateAllProvider = updateAllProvider;
	}

	public boolean isUpdateAllOrganisation() {
		return updateAllOrganisation;
	}

	public void setUpdateAllOrganisation(boolean updateAllOrganisation) {
		this.updateAllOrganisation = updateAllOrganisation;
	}

	public boolean isUpdateAllUser() {
		return updateAllUser;
	}

	public void setUpdateAllUser(boolean updateAllUser) {
		this.updateAllUser = updateAllUser;
	}

	public boolean isDeactivateAllProvider() {
		return deactivateAllProvider;
	}

	public void setDeactivateAllProvider(boolean deactivateAllProvider) {
		this.deactivateAllProvider = deactivateAllProvider;
	}

	public boolean isDeactivateAllOrganisation() {
		return deactivateAllOrganisation;
	}

	public void setDeactivateAllOrganisation(boolean deactivateAllOrganisation) {
		this.deactivateAllOrganisation = deactivateAllOrganisation;
	}

	public boolean isDeactivateAllUser() {
		return deactivateAllUser;
	}

	public void setDeactivateAllUser(boolean deactivateAllUser) {
		this.deactivateAllUser = deactivateAllUser;
	}

	public boolean isReactivateAllProvider() {
		return reactivateAllProvider;
	}

	public void setReactivateAllProvider(boolean reactivateAllProvider) {
		this.reactivateAllProvider = reactivateAllProvider;
	}

	public boolean isReactivateAllOrganisation() {
		return reactivateAllOrganisation;
	}

	public void setReactivateAllOrganisation(boolean reactivateAllOrganisation) {
		this.reactivateAllOrganisation = reactivateAllOrganisation;
	}

	public boolean isReactivateAllUser() {
		return reactivateAllUser;
	}

	public void setReactivateAllUser(boolean reactivateAllUser) {
		this.reactivateAllUser = reactivateAllUser;
	}

	public boolean isReadAddress() {
		return readAddress;
	}

	public void setReadAddress(boolean readAddress) {
		this.readAddress = readAddress;
	}

	public boolean isReadDuplicate() {
		return readDuplicate;
	}

	public void setReadDuplicate(boolean readDuplicate) {
		this.readDuplicate = readDuplicate;
	}

	public boolean isUpdateAddress() {
		return updateAddress;
	}

	public void setUpdateAddress(boolean updateAddress) {
		this.updateAddress = updateAddress;
	}

	public boolean isUpdateDuplicate() {
		return updateDuplicate;
	}

	public void setUpdateDuplicate(boolean updateDuplicate) {
		this.updateDuplicate = updateDuplicate;
	}

	public boolean isConfigureDuplicate() {
		return configureDuplicate;
	}

	public void setConfigureDuplicate(boolean configureDuplicate) {
		this.configureDuplicate = configureDuplicate;
	}

	public boolean isConfigureUser() {
		return configureUser;
	}

	public void setConfigureUser(boolean configureUser) {
		this.configureUser = configureUser;
	}

	public boolean isConfigureSystem() {
		return configureSystem;
	}

	public void setConfigureSystem(boolean configureSystem) {
		this.configureSystem = configureSystem;
	}
	
	

}
