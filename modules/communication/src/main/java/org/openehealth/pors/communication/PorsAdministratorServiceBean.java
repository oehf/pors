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
package org.openehealth.pors.communication;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.openehealth.pors.communication.exception.PORSException;

import org.openehealth.pors.controller.IController;
import org.openehealth.pors.core.dto.AddressDTO;
import org.openehealth.pors.core.dto.DuplicateConfigurationDTO;
import org.openehealth.pors.core.dto.DuplicateDetailDTO;
import org.openehealth.pors.core.dto.DuplicateEntryDTO;
import org.openehealth.pors.core.dto.DuplicateProcessingInfoDTO;
import org.openehealth.pors.core.dto.ImportResultDTO;
import org.openehealth.pors.core.dto.LocalIdDTO;
import org.openehealth.pors.core.dto.LoggingDetailDTO;
import org.openehealth.pors.core.dto.LoggingEntryDTO;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.PermissionDTO;
import org.openehealth.pors.core.dto.PorsCsv;
import org.openehealth.pors.core.dto.ProviderDTO;
import org.openehealth.pors.core.dto.SearchCriteriaDTO;
import org.openehealth.pors.core.dto.UserDTO;
import org.openehealth.pors.core.dto.UserRoleDTO;
import org.openehealth.pors.core.exception.AddressNotFoundException;
import org.openehealth.pors.core.exception.AuthentificationException;
import org.openehealth.pors.core.exception.CsvExportException;
import org.openehealth.pors.core.exception.CsvImportException;
import org.openehealth.pors.core.exception.MissingFieldsException;
import org.openehealth.pors.core.exception.MissingRightsException;
import org.openehealth.pors.core.exception.OrganisationNotFoundException;
import org.openehealth.pors.core.exception.ProviderNotFoundException;
import org.openehealth.pors.core.exception.UserNotFoundException;
import org.openehealth.pors.core.exception.WrongValueException;
import org.openehealth.pors.database.exception.DatabaseException;
import org.openehealth.pors.database.exception.SearchException;

/**
 * Implementation of the PORS administration Web Service.
 * 
 * @see IPorsAdministratorService
 * @author mf
 * 
 */
@WebService(endpointInterface = "org.openehealth.pors.communication.IPorsAdministratorService", serviceName = "PORSAdministratorService")
@SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT)
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PorsAdministratorServiceBean implements IPorsAdministratorService {

	@EJB
	private IController controller;

	@Resource
	private SessionContext context;

	@Resource
	private WebServiceContext wsContext;

	private Logger logger;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initLogging() {
		logger = Logger.getLogger(PorsAdministratorServiceBean.class);
	}

	/**
	 * @see IPorsAdministratorService#authUser(UserDTO)
	 */
	@WebMethod
	public void authUser(UserDTO user) throws PORSException {
		try {
			controller.authUser(user);
		} catch (Exception e) {
			handleException(e, false);
		}
	}

	/**
	 * @see IPorsAdministratorService#addProvider(ProviderDTO, UserDTO)
	 */
	@WebMethod
	public void addProvider(ProviderDTO provider, UserDTO user)
			throws PORSException {
		try {
			if (provider.getIp() == null || provider.getIp().length() == 0) {
				provider.setIp(getRemoteIp());
			}
			controller.addProvider(provider, user);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#getProviders(UserDTO)
	 */
	@WebMethod
	public List<ProviderDTO> getProviders(UserDTO user, int maxResults,
			int offset) throws PORSException {
		try {
			return controller.getAllProviders(user, maxResults, offset);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getHistory(UserDTO)
	 */
	@WebMethod
	public List<LoggingEntryDTO> getHistory(UserDTO user, int maxResults,
			int offset) throws PORSException {
		try {
			return controller.getHistory(user, maxResults, offset);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getHistoryDetail(UserDTO, LoggingEntryDTO)
	 */
	@WebMethod
	public LoggingDetailDTO getHistoryDetail(UserDTO user, LoggingEntryDTO entry)
			throws PORSException {
		try {
			return controller.getHistoryDetail(user, entry);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getUserDetail(UserDTO, Integer)
	 */
	@WebMethod
	public UserDTO getUserDetail(UserDTO user, Integer userId)
			throws PORSException {
		try {
			return controller.getUserDetail(user, userId);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getOrganisations(UserDTO)
	 */
	@WebMethod
	public List<OrganisationDTO> getOrganisations(UserDTO user, int maxResults,
			int offset) throws PORSException {
		try {
			return controller.getAllOrganisations(user, maxResults, offset);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getRoles(UserDTO)
	 */
	@WebMethod
	public List<UserRoleDTO> getRoles(UserDTO user) throws PORSException {
		try {
			return controller.getAllRoles(user);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#addOrganisation(UserDTO, OrganisationDTO)
	 */
	@WebMethod
	public void addOrganisation(UserDTO user, OrganisationDTO organisation)
			throws PORSException {
		try {
			if (organisation.getIp() == null
					|| organisation.getIp().length() == 0) {
				organisation.setIp(getRemoteIp());
			}
			controller.addOrganisation(organisation, user);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#getUserPermissions(UserDTO, UserDTO)
	 */
	@WebMethod
	public PermissionDTO getUserPermissions(UserDTO user, UserDTO requestUser)
			throws PORSException {
		try {
			return controller.getUserPermissions(user, requestUser);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#updateProvider(UserDTO, ProviderDTO)
	 */
	@WebMethod
	public void updateProvider(UserDTO user, ProviderDTO provider)
			throws PORSException {
		try {
			if (provider.getIp() == null || provider.getIp().length() == 0) {
				provider.setIp(getRemoteIp());
			}
			logger.info("username: " + user.getUsername());
			controller.updateProvider(user, provider);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#updateOrganisation(UserDTO,
	 *      OrganisationDTO)
	 */
	@WebMethod
	public void updateOrganisation(UserDTO user, OrganisationDTO organisation)
			throws PORSException {
		try {
			if (organisation.getIp() == null
					|| organisation.getIp().length() == 0) {
				organisation.setIp(getRemoteIp());
			}
			controller.updateOrganisation(user, organisation);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#getProvidersOfOrganisation(UserDTO,
	 *      OrganisationDTO)
	 */
	@WebMethod
	public List<ProviderDTO> getProvidersOfOrganisation(UserDTO user,
			OrganisationDTO organisation) throws PORSException {
		try {
			return controller.getProvidersOfOrganisation(user, organisation);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getOrganisationsOfProvider(UserDTO,
	 *      ProviderDTO)
	 */
	@WebMethod
	public List<OrganisationDTO> getOrganisationsOfProvider(UserDTO user,
			ProviderDTO provider) throws PORSException {
		try {
			return controller.getOrganisationsOfProvider(user, provider);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getProviderDetails(UserDTO, ProviderDTO)
	 */
	@WebMethod
	public ProviderDTO getProviderDetails(UserDTO user, ProviderDTO provider)
			throws PORSException {
		try {
			return controller.getProviderDetails(user, provider);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getOrganisationDetails(UserDTO,
	 *      OrganisationDTO)
	 */
	@WebMethod
	public OrganisationDTO getOrganisationDetails(UserDTO user,
			OrganisationDTO organisation) throws PORSException {
		try {
			return controller.getOrganisationDetails(user, organisation);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getAddressDetails(UserDTO, AddressDTO)
	 */
	@WebMethod
	public AddressDTO getAddressDetails(UserDTO user, AddressDTO address)
			throws PORSException {
		try {
			return controller.getAddressDetails(user, address);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getLocalIdDetails(UserDTO, LocalIdDTO)
	 */
	@WebMethod
	public LocalIdDTO getLocalIdDetails(UserDTO user, LocalIdDTO localId)
			throws PORSException {
		try {
			return controller.getLocalIdDetails(user, localId);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#importProviders(UserDTO, PorsCsv,
	 *      List<String>)
	 */
	@WebMethod
	public ImportResultDTO importProviders(UserDTO user, PorsCsv csvData,
			List<String> fieldList) throws PORSException {
		try {
			return controller.importProviders(user, csvData, fieldList);
		} catch (Exception e) {
			handleException(e, true);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#importOrganisations(UserDTO, PorsCsv,
	 *      List<String>)
	 */
	@WebMethod
	public ImportResultDTO importOrganisations(UserDTO user, PorsCsv csvData,
			List<String> fieldList) throws PORSException {
		try {
			return controller.importOrganisations(user, csvData, fieldList);
		} catch (Exception e) {
			handleException(e, true);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#exportProviders(UserDTO, List<String>)
	 */
	@WebMethod
	public PorsCsv exportProviders(UserDTO user, List<String> fields)
			throws PORSException {
		try {
			return controller.exportProviders(user, fields);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#exportOrganisations(UserDTO, List<String>)
	 */
	@WebMethod
	public PorsCsv exportOrganisations(UserDTO user, List<String> fields)
			throws PORSException {
		try {
			return controller.exportOrganisations(user, fields);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#searchProviders(UserDTO,
	 *      List<SearchCriteriaDTO>, String)
	 */
	@WebMethod
	public List<ProviderDTO> searchProviders(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws PORSException {
		try {
			return controller.searchProviders(user, criteriaDTO, operator);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#searchOrganisations(UserDTO,
	 *      List<SearchCriteriaDTO>, String)
	 */
	@WebMethod
	public List<OrganisationDTO> searchOrganisations(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws PORSException {
		try {
			return controller.searchOrganisations(user, criteriaDTO, operator);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#searchHistory(UserDTO,
	 *      List<SearchCriteriaDTO>, String)
	 */
	@WebMethod
	public List<LoggingEntryDTO> searchHistory(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws PORSException {
		try {
			return controller.searchHistory(user, criteriaDTO, operator);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getDuplicates(UserDTO, int, int)
	 */
	@WebMethod
	public List<DuplicateEntryDTO> getDuplicates(UserDTO user, int maxResults,
			int offset) throws PORSException {
		try {
			return controller.getDuplicates(user, maxResults, offset);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getDuplicateDetail(UserDTO,
	 *      DuplicateEntryDTO)
	 */
	@WebMethod
	public DuplicateDetailDTO getDuplicateDetail(UserDTO user,
			DuplicateEntryDTO entry) throws PORSException {
		try {
			return controller.getDuplicateDetail(user, entry);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#getDuplicateConfiguration(UserDTO)
	 */
	@WebMethod
	public DuplicateConfigurationDTO getDuplicateConfiguration(UserDTO user)
			throws PORSException {
		try {
			return controller.getDuplicateConfiguration(user);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#updateDuplicateConfiguration(DuplicateConfigurationDTO,
	 *      UserDTO)
	 */
	@WebMethod
	public void updateDuplicateConfiguration(DuplicateConfigurationDTO dto,
			UserDTO user) throws PORSException {
		try {
			controller.updateDuplicateConfiguration(dto, user);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#getImportResult(UserDTO, ImportResultDTO)
	 */
	@WebMethod
	public ImportResultDTO getImportResult(UserDTO user, ImportResultDTO result)
			throws PORSException {
		try {
			return controller.getImportResult(user, result);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}

	/**
	 * @see IPorsAdministratorService#addUser(UserDTO, UserDTO)
	 */
	@WebMethod
	public void addUser(UserDTO newUser, UserDTO user) throws PORSException {
		try {
			if (newUser.getIp() == null || newUser.getIp().length() == 0) {
				newUser.setIp(getRemoteIp());
			}
			controller.addUser(newUser, user);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#updateUser(UserDTO, UserDTO)
	 */
	@WebMethod
	public void updateUser(UserDTO user, UserDTO userUpdate)
			throws PORSException {
		try {
			controller.updateUser(user, userUpdate);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#clearDuplicateEntry(UserDTO,
	 *      DuplicateDetailDTO, DuplicateProcessingInfoDTO)
	 */
	@WebMethod
	public void clearDuplicateEntry(UserDTO user, DuplicateDetailDTO detailDTO,
			DuplicateProcessingInfoDTO infoDTO) throws PORSException {
		try {
			logger.info("AdministratorService : clearDuplicateEntry called");
			controller.clearDuplicateEntry(user, detailDTO, infoDTO);
			logger.info("AdministratorService : clearDuplicateEntry finished");
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#getProviderCount(UserDTO)
	 */
	@WebMethod
	public long getProviderCount(UserDTO user) throws PORSException {
		try {
			return controller.getProviderCount(user);
		} catch (Exception e) {
			handleException(e, false);
		}
		return 0;
	}

	/**
	 * @see IPorsAdministratorService#getOrganisationCount(UserDTO)
	 */
	@WebMethod
	public long getOrganisationCount(UserDTO user) throws PORSException {
		try {
			return controller.getOrganisationCount(user);
		} catch (Exception e) {
			handleException(e, false);
		}
		return 0;
	}

	/**
	 * @see IPorsAdministratorService#getHistoryCount(UserDTO)
	 */
	@WebMethod
	public long getHistoryCount(UserDTO user) throws PORSException {
		try {
			return controller.getHistoryCount(user);
		} catch (Exception e) {
			handleException(e, false);
		}
		return 0;
	}

	/**
	 * @see IPorsAdministratorService#rebuildSearchindex(UserDTO)
	 */
	@WebMethod
	public void rebuildSearchindex(UserDTO user) throws PORSException {
		try {
			controller.rebuildSearchindex(user);
		} catch (Exception e) {
			handleException(e, true);
		}
	}

	/**
	 * @see IPorsAdministratorService#getDuplicateCount(UserDTO)
	 */
	@WebMethod
	public long getDuplicateCount(UserDTO user) throws PORSException {
		try {
			return controller.getDuplicateCount(user);
		} catch (Exception e) {
			handleException(e, false);
		}
		return 0;
	}

	/**
	 * @see IPorsAdministratorService#getUsers(UserDTO)
	 */
	@WebMethod
	public List<UserDTO> getUsers(UserDTO user) throws PORSException {
		try {
			return controller.getUsers(user);
		} catch (Exception e) {
			handleException(e, false);
		}
		return null;
	}
	
	private void handleException(Exception e, boolean rollbackOnly) throws PORSException {
		if (rollbackOnly) {
			context.setRollbackOnly();
		}
		
		if (e instanceof MissingRightsException) {
			logger.error("MissingRightsException caught", e);
			throw new PORSException(PORSException.MISSING_RIGHTS, e);
		} else if (e instanceof UserNotFoundException) {
			logger.error("UserNotFoundException caught", e);
			throw new PORSException(PORSException.AUTH_FAILED, e);
		} else if (e instanceof AuthentificationException) {
			logger.error("AuthentificationException caught", e);
			throw new PORSException(PORSException.AUTH_FAILED, e);
		} else if (e instanceof MissingFieldsException) {
			logger.error("MissingFieldsException caught", e);
			throw new PORSException(PORSException.MISSING_FIELDS, e);
		} else if (e instanceof DatabaseException) {
			logger.error("DatabaseException caught", e);
			throw new PORSException(PORSException.DATABASE_ERROR, e);
		} else if (e instanceof WrongValueException) {
			logger.error("WrongValueException caught", e);
			throw new PORSException(PORSException.WRONG_VALUE, e);
		} else if (e instanceof AddressNotFoundException) {
			logger.error("AddressNotFoundException caught", e);
			throw new PORSException(PORSException.ADDRESS_NOT_FOUND, e);
		} else if (e instanceof ProviderNotFoundException) {
			logger.error("ProviderNotFoundException caught", e);
			throw new PORSException(PORSException.PROVIDER_NOT_FOUND, e);
		} else if (e instanceof OrganisationNotFoundException) {
			logger.error("OrganisationNotFoundException caught", e);
			throw new PORSException(PORSException.ORGANISATION_NOT_FOUND, e);
		} else if (e instanceof SearchException) {
			logger.error("SearchException caught", e);
			throw new PORSException(PORSException.SEARCH_FAILED, e);
		} else if (e instanceof CsvExportException) {
			logger.error("CsvExportException caught", e);
			throw new PORSException(PORSException.EXPORT_FAILED, e);
		} else if (e instanceof CsvImportException) {
			logger.error("CsvImportException caught", e);
			throw new PORSException(PORSException.IMPORT_FAILED, e);
		} else {
			logger.warn("Exception type unknown");
			throw new RuntimeException(e);
		}
	}
	
	private String getRemoteIp() {
		HttpServletRequest request = (HttpServletRequest) wsContext
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);
		return request.getRemoteAddr();
	}

}
