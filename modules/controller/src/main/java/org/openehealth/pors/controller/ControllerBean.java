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
package org.openehealth.pors.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.openehealth.pors.controller.mdb.ImportRequest;
import org.openehealth.pors.message.IHl7MessageHandler;
import org.openehealth.pors.message.exception.HL7ContentException;

import org.openehealth.pors.admin.IAdministrator;
import org.openehealth.pors.auth.IAuthentificator;
import org.openehealth.pors.core.IPorsCore;
import org.openehealth.pors.core.common.Permission;
import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.dto.AddressDTO;
import org.openehealth.pors.core.dto.AddressLogDTO;
import org.openehealth.pors.core.dto.DuplicateConfigurationDTO;
import org.openehealth.pors.core.dto.DuplicateDetailDTO;
import org.openehealth.pors.core.dto.DuplicateEntryDTO;
import org.openehealth.pors.core.dto.DuplicateProcessingInfoDTO;
import org.openehealth.pors.core.dto.ImportResultDTO;
import org.openehealth.pors.core.dto.LocalIdDTO;
import org.openehealth.pors.core.dto.LocalIdLogDTO;
import org.openehealth.pors.core.dto.LoggingDetailDTO;
import org.openehealth.pors.core.dto.LoggingEntryDTO;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.OrganisationHasAddressLogDTO;
import org.openehealth.pors.core.dto.OrganisationHasProviderLogDTO;
import org.openehealth.pors.core.dto.OrganisationLogDTO;
import org.openehealth.pors.core.dto.PermissionDTO;
import org.openehealth.pors.core.dto.PorsCsv;
import org.openehealth.pors.core.dto.ProviderDTO;
import org.openehealth.pors.core.dto.ProviderHasAddressLogDTO;
import org.openehealth.pors.core.dto.ProviderLogDTO;
import org.openehealth.pors.core.dto.SearchCriteriaDTO;
import org.openehealth.pors.core.dto.UserDTO;
import org.openehealth.pors.core.dto.UserRoleDTO;
import org.openehealth.pors.core.exception.AddressNotFoundException;
import org.openehealth.pors.core.exception.AuthentificationException;
import org.openehealth.pors.core.exception.CsvExportException;
import org.openehealth.pors.core.exception.CsvImportException;
import org.openehealth.pors.core.exception.ImportException;
import org.openehealth.pors.core.exception.InvalidHL7MessageException;
import org.openehealth.pors.core.exception.MissingFieldsException;
import org.openehealth.pors.core.exception.MissingRightsException;
import org.openehealth.pors.core.exception.OrganisationNotFoundException;
import org.openehealth.pors.core.exception.ProviderNotFoundException;
import org.openehealth.pors.core.exception.UserNotFoundException;
import org.openehealth.pors.core.exception.WrongValueException;
import org.openehealth.pors.database.connector.IDatabaseConnector;
import org.openehealth.pors.database.connector.IEntitySearcher;
import org.openehealth.pors.database.connector.IImportLogger;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.AddressLog;
import org.openehealth.pors.database.entities.DuplicateAddress;
import org.openehealth.pors.database.entities.DuplicateEntry;
import org.openehealth.pors.database.entities.DuplicateOrganisation;
import org.openehealth.pors.database.entities.DuplicateProvider;
import org.openehealth.pors.database.entities.History;
import org.openehealth.pors.database.entities.ImportResult;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.LocalIdLog;
import org.openehealth.pors.database.entities.LoggingEntry;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.OrganisationHasAddressLog;
import org.openehealth.pors.database.entities.OrganisationHasProviderLog;
import org.openehealth.pors.database.entities.OrganisationLog;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.entities.ProviderHasAddressLog;
import org.openehealth.pors.database.entities.ProviderLog;
import org.openehealth.pors.database.entities.UserHistory;
import org.openehealth.pors.database.entities.UserRole;
import org.openehealth.pors.database.exception.DatabaseException;
import org.openehealth.pors.database.exception.SearchException;
import org.openehealth.pors.database.util.SearchCriteria;

/**
 * Implementation of IController
 * 
 * @see IController
 * @author ck, mf
 * 
 **/
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ControllerBean implements IController {

	@EJB
	private IAuthentificator authentificator;
	@EJB
	private IDatabaseConnector databaseManager;
	@EJB
	private IHl7MessageHandler hl7MessageHandler;
	@EJB
	private IEntitySearcher entitySearcher;

	@EJB
	private IImportLogger importLogger;

	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "jms/ImportQueue")
	private Destination destination;

	@EJB
	private IPorsCore core;

	@EJB
	private IAdministrator admin;

	private Logger logger;
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	@SuppressWarnings("unused")
	@PostConstruct
	private void initLogging() {
		logger = Logger.getLogger(ControllerBean.class);
	}

	/**
	 * @see IController#addProvider(ProviderDTO,UserDTO)
	 **/
	public void addProvider(ProviderDTO providerDto, UserDTO userDto)
			throws DatabaseException, MissingRightsException,
			AuthentificationException,
			MissingFieldsException, WrongValueException {
		Provider provider = core.assembleProvider(providerDto);
		PorsUser user = core.assemblePorsUser(userDto);

		addProvider(provider, user);
	}

	/**
	 * Adds a provider to the database.
	 * 
	 * @param provider
	 *            the provider to add
	 * @param user
	 *            the user credentials
	 * @throws DatabaseException
	 * @throws MissingRightsException
	 * @throws AuthentificationException
	 * @throws MissingFieldsException
	 */
	private void addProvider(Provider provider, PorsUser user)
			throws DatabaseException, MissingRightsException,
			AuthentificationException,
			MissingFieldsException, WrongValueException {
		logger.info("addProvider (private) called");
		PorsUser currentUser = authentificator.authUser(user);
		authentificator.checkRights(currentUser, new Task(Task.CREATE, Task.PROVIDER));

		if (provider.getSessionId() == null
				|| provider.getSessionId().length() == 0) {
			provider.setSessionId(UUID.randomUUID().toString());
		}

		provider.setUser(currentUser);
		provider.setEditingUser(currentUser);
		core.validateProvider(provider);
		databaseManager.addProvider(provider);
	}

	/**
	 * Adding provider with HL7 needs some extra functionality, because external
	 * system doen't know if provider already exists
	 * 
	 * @param provider
	 * @param user
	 * @throws WrongValueException
	 * @throws MissingFieldsException
	 * @throws AuthentificationException
	 * @throws MissingRightsException
	 * @throws DatabaseException
	 * @throws ProviderNotFoundException
	 */
	private void addProviderHL7(Provider provider, PorsUser user)
			throws DatabaseException, MissingRightsException,
			AuthentificationException,
			MissingFieldsException, WrongValueException,
			ProviderNotFoundException {

		Provider dbProvider = null;
		if (null != provider.getFirstName() && null != provider.getLastName()
				&& null != provider.getGenderCode()
				&& null != provider.getBirthday()) {
			dbProvider = databaseManager.getProviderByUniqueCombination(
					provider.getFirstName(), provider.getLastName(),
					provider.getGenderCode(), provider.getBirthday());
		}
		Provider dbProvider2 = null;
		if (null != provider.getId()) {
			dbProvider2 = databaseManager.getProviderById(provider.getId());
		}
		if (null != dbProvider || null != dbProvider2) {
			this.updateProviderHL7(provider, user);
		} else {
			this.addProvider(provider, user);
		}
	}

	/**
	 * @see IController#getAllProviders(UserDTO)
	 **/
	public List<ProviderDTO> getAllProviders(UserDTO userDto, int maxResults,
			int offset) throws MissingRightsException, AuthentificationException, MissingFieldsException {
		logger.info("getAllProviders called");
		logger.info("username: " + userDto.getUsername() + " - password: "
				+ userDto.getPassword());

		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(userDto));
		authentificator.checkRights(porsUser,
				new Task(Task.READ, Task.PROVIDER));

		List<Provider> providerList = null;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.PROVIDER));
			providerList = databaseManager
					.getProviderListHavingMaxEntriesFromPosition(maxResults,
							offset);
		} catch (MissingRightsException ex) {
			providerList = databaseManager.getLimitedProviderListOfUser(maxResults, offset, porsUser);
		}

		List<ProviderDTO> providerDtoList = new ArrayList<ProviderDTO>();

		for (int i = 0; i < providerList.size(); i++) {
			providerDtoList.add(core.assembleProviderDTO(providerList.get(i)));
		}

		return providerDtoList;
	}

	/**
	 * @see IController#getHistory(UserDTO)
	 **/
	public List<LoggingEntryDTO> getHistory(UserDTO userDto, int maxResults,
			int offset) throws MissingRightsException, AuthentificationException, MissingFieldsException {

		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(userDto));
		authentificator
				.checkRights(porsUser, new Task(Task.READ, Task.LOGGING));
		List<UserHistory> userHistoryList = null;
		List<History> historyList = null;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.LOGGING));
			historyList = databaseManager
					.getHistoryHavingMaxEntriesFromPosition(maxResults, offset);
		} catch (MissingRightsException ex) {
			userHistoryList = databaseManager.getLimitedHistoryForUser(maxResults, offset, porsUser);
		}

		List<LoggingEntryDTO> loggingDtoList = new ArrayList<LoggingEntryDTO>();

		if (userHistoryList != null) {
			for (int i = 0; i < userHistoryList.size(); i++) {
				loggingDtoList.add(core.assembleLoggingEntryDTO(userHistoryList
						.get(i)));
			}
		} else // if (historyList != null)
		{
			for (int i = 0; i < historyList.size(); i++) {
				loggingDtoList.add(core.assembleLoggingEntryDTO(historyList
						.get(i)));
			}
		}

		return loggingDtoList;
	}

	/**
	 * @see IController#processMessage(String,UserDTO)
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processMessage(String hl7Message, UserDTO userDto)
			throws InvalidHL7MessageException {
		logger.info("Message:\n" + hl7Message);

		List list = null;

		try {
			List<Task> taskList = hl7MessageHandler.processMessage(hl7Message);
			logger.info("Tasklist size: " + taskList.size());
			logger.info("First task has domain: " + taskList.get(0).getDomain()
					+ ", action: " + taskList.get(0).getAction());
			list = handleTaskList(taskList, userDto);

		} catch (DatabaseException de) {
			throw new InvalidHL7MessageException(de.getMessage(),
					hl7MessageHandler
							.getRejectMFAcknowledgement(hl7Message, de));
		} catch (MissingRightsException mre) {
			throw new InvalidHL7MessageException(mre.getMessage(),
					hl7MessageHandler.getRejectMFAcknowledgement(hl7Message,
							mre));
		} catch (HL7ContentException he) {
			throw new InvalidHL7MessageException(he.getMessage(),
					hl7MessageHandler.getErrorMFAcknowledgement(hl7Message, he));
		} catch (AuthentificationException authex) {
			throw new InvalidHL7MessageException(authex.getMessage(),
					hl7MessageHandler.getRejectMFAcknowledgement(hl7Message,
							authex));
		} catch (MissingFieldsException mEx) {
			throw new InvalidHL7MessageException(mEx.getMessage(),
					hl7MessageHandler.getErrorMFAcknowledgement(hl7Message,
							new HL7ContentException(mEx.getMessage(),
									HL7ContentException.REQUIREDFIELDMISSING)));
		} catch (WrongValueException wEx) {
			throw new InvalidHL7MessageException(wEx.getMessage(),
					hl7MessageHandler.getErrorMFAcknowledgement(hl7Message,
							new HL7ContentException(wEx.getMessage(),
									HL7ContentException.DATATYPEERROR)));
		} catch (ProviderNotFoundException pnfe) {
			throw new InvalidHL7MessageException(pnfe.getMessage(),
					hl7MessageHandler.getRejectMFAcknowledgement(hl7Message,
							pnfe));
		} catch (OrganisationNotFoundException onfe) {
			throw new InvalidHL7MessageException(onfe.getMessage(),
					hl7MessageHandler.getRejectMFAcknowledgement(hl7Message,
							onfe));
		} catch (ImportException e) {
			throw new InvalidHL7MessageException(e.getMessage(),
					hl7MessageHandler.getErrorMFAcknowledgement(hl7Message,
							new HL7ContentException(e.getMessage(),
									HL7ContentException.DATATYPEERROR)));
		} catch (SearchException e) {
			throw new InvalidHL7MessageException(e.getMessage(),
					hl7MessageHandler.getErrorMFAcknowledgement(hl7Message,
							new HL7ContentException(e.getMessage(),
									HL7ContentException.DATATYPEERROR)));
		}

		if (null != list) {
			logger.info("HL7 : List of providers or organisations for query is not null.");
			if (list.size() < 1) {
				return hl7MessageHandler.getRSP_K25NoDataFound(hl7Message);
			} else {
				logger.info("HL7 : List of providers or organisations for query is greater 0.");

				if (list.get(0) instanceof OrganisationDTO) {
					logger.info("HL7 : List for query is organisation.");

					List<OrganisationDTO> orgList = list;
					return hl7MessageHandler.getOrganisationsRSP_K25(
							hl7Message, orgList);
				}
				if (list.get(0) instanceof ProviderDTO) {
					logger.info("HL7 : List for query is provider.");

					List<ProviderDTO> proList = list;
					return hl7MessageHandler.getProvidersRSP_K25(hl7Message,
							proList);
				}
				logger.info("HL7 : Cannot cast list for query");
			}
		}
		return hl7MessageHandler.getAcceptMFAcknowledgement(hl7Message);

	}

	/**
	 * Fills the organisation objects inside the provider with data
	 * 
	 * @param provider
	 */
	private void fillOrganisationsInProvider(Provider provider)
			throws OrganisationNotFoundException {

		List<Organisation> newOrganisations = new ArrayList<Organisation>();
		if (null != provider.getOrganisations()) {
			for (int i = 0; i < provider.getOrganisations().size(); i++) {
				Organisation dbOrg = null;
				List<LocalId> localIDs = provider.getOrganisations().get(i)
						.getLocalIds();
				if (localIDs != null && localIDs.size() > 0) {
					if (localIDs.get(0) != null) {
						dbOrg = databaseManager
								.getOrganisationByLocalId(localIDs.get(0));
					}
				}
				String establishmentId = provider.getOrganisations().get(i)
						.getEstablishmentId();
				if (dbOrg == null && establishmentId != null
						&& establishmentId.length() > 0) {
					dbOrg = databaseManager
							.getOrganisationByEstablishmentId(establishmentId);
				}
				String name = provider.getOrganisations().get(i).getName();
				if (dbOrg == null && name != null && name.length() > 0) {
					dbOrg = databaseManager.getOrganisationByName(name);
				}
				if (dbOrg != null) {
					newOrganisations.add(dbOrg);
				} else {
					throw new OrganisationNotFoundException(
							"Organisation of provider could not be found! Name of organisation: "
									+ name);
				}
			}
			provider.setOrganisations(newOrganisations);
		}

	}

	/**
	 * @see IController#authUser(UserDTO)
	 **/
	public void authUser(UserDTO userDto) throws AuthentificationException, MissingFieldsException {
		authentificator.authUser(core.assemblePorsUser(userDto));
	}

	/**
	 * @see IController#getHistoryDetail(UserDTO, LoggingEntryDTO)
	 **/
	public LoggingDetailDTO getHistoryDetail(UserDTO userDto,
			LoggingEntryDTO entry) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {

		PorsUser user = authentificator.authUser(core.assemblePorsUser(userDto));

		History loggingEntry = core.assembleLoggingEntry(entry);
		Object o = databaseManager.getDetailedHistory(loggingEntry);

		LoggingDetailDTO detailDTO = new LoggingDetailDTO();
		detailDTO.setLoggingEntry(entry);
		
		authentificator.checkRights(user, new Task(Task.READ, Task.LOGGING));

		if (loggingEntry.getDomain().equals(LoggingEntry.DOMAIN_PROVIDER)) {
			ProviderLog providerLog = (ProviderLog) o;
			
			try {
				authentificator.checkRights(user, new Task(Task.READ_ALL,
						Task.LOGGING));
			} catch (MissingRightsException ex) {
				Provider providerOfLog = databaseManager
						.getProviderById(providerLog.getRegionalProviderId());
				if (!providerOfLog.getUser().getName().equals(user.getName())) {
					throw new MissingRightsException("Missing Rights");
				}
			}
			ProviderLogDTO providerLogDTO = core
					.assembleProviderLogDTO(providerLog);
			detailDTO.setProviderLog(providerLogDTO);
		} else if (loggingEntry.getDomain().equals(
				LoggingEntry.DOMAIN_ORGANISATION)) {
			OrganisationLog orgLog = (OrganisationLog) o;
			
			try {
				authentificator.checkRights(user, new Task(Task.READ_ALL,
						Task.LOGGING));
			} catch (MissingRightsException ex) {
				Organisation orgOfLog = databaseManager
						.getOrganisationById(orgLog.getRegionalOrganisationId());
				if (!orgOfLog.getUser().getName().equals(user.getName())) {
					throw new MissingRightsException("Missing Rights");
				}
			}
			OrganisationLogDTO organisationLogDTO = core
					.assembleOrganisationLogDTO(orgLog);
			detailDTO.setOrganisationLog(organisationLogDTO);
		} else if (loggingEntry.getDomain().equals(LoggingEntry.DOMAIN_ADDRESS)) {
			AddressLog adrLog = (AddressLog) o;
			
			try {
				authentificator.checkRights(user, new Task(Task.READ_ALL,
						Task.LOGGING));
			} catch (MissingRightsException e) {
				Address adr = new Address();
				adr.setId(adrLog.getAddressId());
				if (!databaseManager.isOwningUser(user, adr)) {
					throw new MissingRightsException("Missing Rights!");
				}
			}
			AddressLogDTO adrLogDTO = core.assembleAddressLogDTO(adrLog);
			detailDTO.setAddressLog(adrLogDTO);
		} else if (loggingEntry.getDomain().equals(LoggingEntry.DOMAIN_LOCALID)) {
			LocalIdLog localIdLog = (LocalIdLog) o;
			if (o == null) {
				logger.info("o was null!");
			}
			if (localIdLog == null) {
				logger.info("LocalIdLog was null!");
			}
			try {
				authentificator.checkRights(user, new Task(Task.READ_ALL,
						Task.LOGGING));
			} catch (MissingRightsException e) {
				LocalId localId = new LocalId();
				localId.setId(localIdLog.getLocalIdId());
				localId.setApplication(localIdLog.getNewApplication());
				localId.setFacility(localIdLog.getNewFacility());
				if (!databaseManager.isOwningUser(user, localId)) {
					throw new MissingRightsException("Missing Rights!");
				}
			}
			LocalIdLogDTO logDTO = core.assembleLocalIDLogDTO(localIdLog);
			detailDTO.setLocalidLog(logDTO);
		} else if (loggingEntry.getDomain().equals(
				LoggingEntry.DOMAIN_PROVIDER_HAS_ADDRESS)) {
			ProviderHasAddressLog phaLog = (ProviderHasAddressLog) o;
			if (o == null) {
				logger.info("o was null!");
			}
			if (phaLog == null) {
				logger.info("ProviderHasAddressLog was null!");
			}
			
			try {
				authentificator.checkRights(user, new Task(Task.READ_ALL,
						Task.LOGGING));
			} catch (MissingRightsException e) {
				Provider provider = databaseManager.getProviderById(phaLog.getNewRegionalProviderId());
				if (!provider.getUser().getName().equals(user.getName())) {
					throw new MissingRightsException("Missing Rights");
				}
			}
			ProviderHasAddressLogDTO phaLogDTO = core
					.assembleProviderHasAddressLogDTO(phaLog);
			detailDTO.setProviderHasAddressLog(phaLogDTO);
		} else if (loggingEntry.getDomain().equals(
				LoggingEntry.DOMAIN_ORGANISATION_HAS_ADDRESS)) {
			OrganisationHasAddressLog orgLog = (OrganisationHasAddressLog) o;
			if (o == null) {
				logger.info("o was null!");
			}
			if (orgLog == null) {
				logger.info("OrganisationHasAddressLog was null!");
			}
			
			try {
				authentificator.checkRights(user, new Task(Task.READ_ALL,
						Task.LOGGING));
			} catch (MissingRightsException e) {
				Organisation organisation = databaseManager.getOrganisationById(orgLog.getNewRegionalOrganisationId());
				if (!organisation.getUser().equals(user.getName())) {
					throw new MissingRightsException("Missing rights!");
				}
			}
			OrganisationHasAddressLogDTO orgLogDTO = core
					.assembleOrganisationHasAddressLogDTO(orgLog);
			detailDTO.setOrganisationHasAddressLog(orgLogDTO);
		} else if (loggingEntry.getDomain().equals(
				LoggingEntry.DOMAIN_ORGANISATION_HAS_PROVIDER)) {
			OrganisationHasProviderLog orgLog = (OrganisationHasProviderLog) o;
			if (o == null) {
				logger.info("o was null!");
			}
			if (orgLog == null) {
				logger.info("OrganisationHasProviderLog was null!");
			}
			
			try {
				authentificator.checkRights(user, new Task(Task.READ_ALL,
						Task.LOGGING));
			} catch (MissingRightsException e) {
				Organisation organisation = databaseManager.getOrganisationById(orgLog.getNewRegionalOrganisationId());
				if (!organisation.getUser().equals(user.getName())) {
					throw new MissingRightsException("Missing rights!");
				}
			}
			OrganisationHasProviderLogDTO orgLogDTO = core
					.assembleOrganisationHasProviderLogDTO(orgLog);
			detailDTO.setOrganisationHasProviderLog(orgLogDTO);
		}
		return detailDTO;
	}

	/**
	 * @see IController#getUserDetail(UserDTO,Integer)
	 **/
	public UserDTO getUserDetail(UserDTO user, Integer userId)
			throws MissingRightsException, UserNotFoundException,
			AuthentificationException, MissingFieldsException {

		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		PorsUser detailUser = databaseManager.getUser(porsUser);
		if (detailUser.getId() == userId) {
			authentificator.checkRights(porsUser,
					new Task(Task.READ, Task.USER));
		} else {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.USER));
		}

		PorsUser queryUser = new PorsUser();
		queryUser.setId(userId);

		return core.assembleUserDTO(databaseManager.getUser(queryUser));
	}

	/**
	 * @see IController#getAllOrganisations(UserDTO)
	 **/
	public List<OrganisationDTO> getAllOrganisations(UserDTO userDto,
			int maxResults, int offset) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(userDto));
		authentificator.checkRights(porsUser, new Task(Task.READ,
				Task.ORGANISATION));

		List<Organisation> organisations = null;

		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.ORGANISATION));
			organisations = databaseManager
					.getOrganisationListHavingMaxEntriesFromPosition(
							maxResults, offset);
		} catch (MissingRightsException ex) {
			organisations = databaseManager.getLimitedOrganisationListOfUser(maxResults, offset, porsUser);
		}

		List<OrganisationDTO> dtoList = new ArrayList<OrganisationDTO>();
		for (Organisation organisation : organisations) {
			dtoList.add(core.assembleOrganisationDTO(organisation));
		}
		return dtoList;
	}

	/**
	 * @see IController#addOrganisation(OrganisationDTO,UserDTO)
	 **/
	public void addOrganisation(OrganisationDTO organisation, UserDTO userDto)
			throws MissingRightsException, AuthentificationException, DatabaseException,
			MissingFieldsException, WrongValueException {

		logger.info("addOrganisation called");

		PorsUser user = core.assemblePorsUser(userDto);
		Organisation org = core.assembleOrganisation(organisation);

		addOrganisation(org, user);
	}

	/**
	 * Adds an organisation to the database.
	 * 
	 * @param organisation
	 *            the organisation
	 * @param user
	 *            the user credentials
	 * @throws MissingRightsException
	 * @throws AuthentificationException
	 * @throws DatabaseException
	 * @throws MissingFieldsException
	 * @throws WrongValueException
	 */
	private void addOrganisation(Organisation organisation, PorsUser user)
			throws MissingRightsException, AuthentificationException, DatabaseException,
			MissingFieldsException, WrongValueException {
		PorsUser currentUser = authentificator.authUser(user);
		authentificator.checkRights(currentUser, new Task(Task.CREATE,
				Task.ORGANISATION));

		if (organisation.getSessionId() == null
				|| organisation.getSessionId().length() == 0) {
			organisation.setSessionId(UUID.randomUUID().toString());
		}

		organisation.setUser(currentUser);
		organisation.setEditingUser(currentUser);

		core.validateOrganisation(organisation);

		databaseManager.addOrganisation(organisation);
	}

	/**
	 * Adding organisation over HL7 Messages needs an extra check if
	 * organisation already exists
	 * 
	 * @param provider
	 * @param user
	 * @throws DatabaseException
	 * @throws MissingRightsException
	 * @throws AuthentificationException
	 * @throws MissingFieldsException
	 * @throws WrongValueException
	 * @throws ProviderNotFoundException
	 * @throws OrganisationNotFoundException
	 */
	private void addOrganisationHL7(Organisation organisation, PorsUser user)
			throws DatabaseException, MissingRightsException,
			AuthentificationException,
			MissingFieldsException, WrongValueException,
			ProviderNotFoundException, OrganisationNotFoundException {
		Organisation dbOrganisation = null;
		if (null != organisation.getEstablishmentId()) {
			dbOrganisation = databaseManager
					.getOrganisationByEstablishmentId(organisation
							.getEstablishmentId());
		}
		Organisation dbOrganisation2 = null;
		if (null != organisation.getId()) {
			dbOrganisation2 = databaseManager.getOrganisationById(organisation
					.getId());
		}
		Organisation dbOrganisation3 = null;
		if (null != organisation.getName()) {
			dbOrganisation3 = databaseManager
					.getOrganisationByName(organisation.getName());
		}
		if (null != dbOrganisation || null != dbOrganisation2
				|| null != dbOrganisation3) {
			this.updateOrganisationHL7(organisation, user);
		} else {
			this.addOrganisation(organisation, user);
		}
	}

	/**
	 * @see IController#getUserPermissions(UserDTO, UserDTO)
	 */
	public PermissionDTO getUserPermissions(UserDTO userDTO, UserDTO requestUser)
			throws MissingRightsException, UserNotFoundException,
			AuthentificationException, MissingFieldsException {
		logger.info("getUserPermissions called");

		PorsUser permissionUser = core.assemblePorsUser(requestUser);
		if (authentificator.isLdapMode()) {
			permissionUser.setName(authentificator.getLdapDn(permissionUser.getName()));
		}

		PorsUser detailUser = authentificator.authUser(core.assemblePorsUser(userDTO));
		PorsUser detailPermissionUser = databaseManager.getUser(permissionUser);

		if (detailUser.getId() == detailPermissionUser.getId()) {
			authentificator.checkRights(detailUser, new Task(Task.READ,
					Task.USER));
		} else {
			authentificator.checkRights(detailUser, new Task(Task.READ_ALL,
					Task.USER));
		}

		List<Permission> permissions = authentificator
				.getPermissions(detailPermissionUser);

		return core.assemblePersmissionDTO(permissions);
	}

	/**
	 * @see IController#updateProvider(UserDTO, ProviderDTO)
	 */
	public void updateProvider(UserDTO userDTO, ProviderDTO providerDTO)
			throws AuthentificationException,
			MissingRightsException, MissingFieldsException,
			WrongValueException, DatabaseException, ProviderNotFoundException {
		PorsUser user = core.assemblePorsUser(userDTO);
		Provider provider = core.assembleProvider(providerDTO);

		updateProvider(provider, user);
	}

	/**
	 * 
	 * @param providerU
	 * @param user
	 * @throws AuthentificationException
	 * @throws MissingRightsException
	 * @throws MissingFieldsException
	 * @throws WrongValueException
	 * @throws DatabaseException
	 * @throws ProviderNotFoundException
	 */
	private void updateProviderHL7(Provider providerU, PorsUser user)
			throws AuthentificationException,
			MissingRightsException, MissingFieldsException,
			WrongValueException, DatabaseException, ProviderNotFoundException {

		Provider provider = null;
		if (null == provider && null != providerU.getLocalIds()) {
			provider = databaseManager.getProviderByLocalId(providerU
					.getLocalIds().get(0));
		}
		if (null == provider && null != providerU.getId()) {
			provider = databaseManager.getProviderById(providerU.getId());
		}
		if (null == provider && null != providerU.getFirstName()
				&& null != providerU.getLastName()
				&& null != providerU.getGenderCode()) {
			provider = databaseManager.getProviderByUniqueCombination(
					providerU.getFirstName(), providerU.getLastName(),
					providerU.getGenderCode(), providerU.getBirthday());
		}
		if (null != provider) {
			providerU.setVersion(provider.getVersion());
		}
		updateProvider(providerU, user);
	}

	/**
	 * Updates an existing provider.
	 * 
	 * @param provider
	 * @param user
	 * @throws AuthentificationException
	 * @throws MissingRightsException
	 * @throws MissingFieldsException
	 * @throws WrongValueException
	 * @throws DatabaseException
	 * @throws ProviderNotFoundException
	 */
	private void updateProvider(Provider provider, PorsUser user)
			throws AuthentificationException,
			MissingRightsException, MissingFieldsException,
			WrongValueException, DatabaseException, ProviderNotFoundException {
		logger.info("updateProvider called");

		PorsUser currentUser = authentificator.authUser(user);
		authentificator.checkRights(currentUser, new Task(Task.UPDATE, Task.PROVIDER));

		Provider currentProvider = null;
		if (null != provider.getId() && provider.getId() > 0) {
			currentProvider = databaseManager.getProviderById(provider.getId());
		}
		if (null == currentProvider && null != provider.getLocalIds()
				&& 0 < provider.getLocalIds().size()) {
			logger.info("updateProvider, Step: Getting provider by localId");
			currentProvider = databaseManager.getProviderByLocalId(provider
					.getLocalIds().get(0));
		}
		if (null == currentProvider) {
			throw new ProviderNotFoundException("There is no such provider!");
		}

		if (!currentProvider.getUser().getName().equals(currentUser.getName())) {
			authentificator.checkRights(currentUser, new Task(Task.UPDATE_ALL,
					Task.PROVIDER));
		}
		if (provider.getSessionId() == null
				|| provider.getSessionId().length() == 0) {
			provider.setSessionId(UUID.randomUUID().toString());
		}

		if (null == provider.getId() || 0 > provider.getId()) {
			provider.setId(currentProvider.getId());
		}

		provider.setEditingUser(currentUser);
		provider.setUser(currentProvider.getUser());

		core.validateProvider(provider);

		databaseManager.updateProvider(provider);
	}

	/**
	 * @see IController#updateOrganisation(UserDTO, OrganisationDTO)
	 */
	public void updateOrganisation(UserDTO userDTO,
			OrganisationDTO organisationDTO) throws AuthentificationException, MissingRightsException,
			MissingFieldsException, WrongValueException, DatabaseException,
			OrganisationNotFoundException {
		PorsUser user = core.assemblePorsUser(userDTO);
		Organisation organisation = core.assembleOrganisation(organisationDTO);

		updateOrganisation(organisation, user);
	}

	private void updateOrganisationHL7(Organisation organisationU, PorsUser user)
			throws AuthentificationException,
			MissingRightsException, MissingFieldsException,
			WrongValueException, DatabaseException,
			OrganisationNotFoundException {
		Organisation organisation = null;
		if (null != organisationU.getLocalIds()) {
			organisation = databaseManager
					.getOrganisationByLocalId(organisationU.getLocalIds()
							.get(0));
		}
		if (null == organisation && null != organisationU.getId()) {
			organisation = databaseManager.getOrganisationById(organisationU
					.getId());
		}
		if (null == organisation && null != organisationU.getName()) {
			organisation = databaseManager.getOrganisationByName(organisationU
					.getName());
		}
		if (null == organisation && null != organisationU.getOid()) {
			organisation = databaseManager.getOrganisationByOid(organisationU
					.getOid());
		}
		if (null != organisation) {
			organisationU.setVersion(organisation.getVersion());
		}
		updateOrganisation(organisationU, user);
	}

	private void updateOrganisation(Organisation organisation, PorsUser user)
			throws AuthentificationException,
			MissingRightsException, MissingFieldsException,
			WrongValueException, DatabaseException,
			OrganisationNotFoundException {
		logger.info("updateOrganisation called");

		PorsUser currentUser = authentificator.authUser(user);
		authentificator.checkRights(currentUser, new Task(Task.UPDATE,
				Task.ORGANISATION));

		Organisation currentOrganisation = null;
		if (null != organisation.getId() && organisation.getId() > 0) {
			currentOrganisation = databaseManager
					.getOrganisationById(organisation.getId());
		}
		if (null == currentOrganisation && null != organisation.getLocalIds()
				&& 0 < organisation.getLocalIds().size()) {
			currentOrganisation = databaseManager
					.getOrganisationByLocalId(organisation.getLocalIds().get(0));
		}

		if (currentOrganisation == null) {
			throw new OrganisationNotFoundException(
					"There is no such organisation!");
		}
		if (!currentOrganisation.getUser().getName().equals(currentUser.getName())) {
			authentificator.checkRights(currentUser, new Task(Task.UPDATE_ALL,
					Task.ORGANISATION));
		}
		if (organisation.getSessionId() == null
				|| organisation.getSessionId().length() == 0) {
			organisation.setSessionId(UUID.randomUUID().toString());
		}

		if (null == organisation.getId() || 0 > organisation.getId()) {
			organisation.setId(currentOrganisation.getId());
		}

		organisation.setEditingUser(currentUser);
		organisation.setUser(currentOrganisation.getUser());
		core.validateOrganisation(organisation);

		databaseManager.updateOrganisation(organisation);
	}

	/**
	 * @see IController#getProvidersOfOrganisation(UserDTO, OrganisationDTO)
	 */
	public List<ProviderDTO> getProvidersOfOrganisation(UserDTO userDto,
			OrganisationDTO organisation) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		logger.info("getProvidersOfOrganisation called");
		logger.info("username: " + userDto.getUsername() + " - password: "
				+ userDto.getPassword());
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(userDto));
		authentificator.checkRights(porsUser,
				new Task(Task.READ, Task.PROVIDER));

		Organisation selectedOrganisation = core
				.assembleOrganisation(organisation);

		List<Provider> providerList = null;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.PROVIDER));
			providerList = databaseManager
					.getProviderListForOrganisation(selectedOrganisation);
		} catch (MissingRightsException ex) {
			providerList = databaseManager
					.getProviderListForOrganisationOfUser(selectedOrganisation,
							porsUser);
		}

		List<ProviderDTO> providerDtoList = new ArrayList<ProviderDTO>();

		for (int i = 0; i < providerList.size(); i++) {
			providerDtoList.add(core.assembleProviderDTO(providerList.get(i)));
		}

		return providerDtoList;
	}

	/**
	 * @see IController#getOrganisationsOfProvider(UserDTO, ProviderDTO)
	 */
	public List<OrganisationDTO> getOrganisationsOfProvider(UserDTO userDto,
			ProviderDTO provider) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(userDto));
		authentificator.checkRights(porsUser, new Task(Task.READ,
				Task.ORGANISATION));

		Provider selectedProvider = core.assembleProvider(provider);

		List<Organisation> organisations = null;

		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.ORGANISATION));
			organisations = databaseManager
					.getOrganisationListForProvider(selectedProvider);
		} catch (MissingRightsException ex) {
			organisations = databaseManager
					.getOrganisationListForProviderOfUser(selectedProvider,
							porsUser);
		}

		List<OrganisationDTO> dtoList = new ArrayList<OrganisationDTO>();
		for (Organisation organisation : organisations) {
			dtoList.add(core.assembleOrganisationDTO(organisation));
		}
		return dtoList;
	}

	/**
	 * @see IController#getProviderDetails(UserDTO, ProviderDTO)
	 */
	public ProviderDTO getProviderDetails(UserDTO user, ProviderDTO provider)
			throws MissingRightsException, AuthentificationException, MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser,
				new Task(Task.READ, Task.PROVIDER));

		Provider selectedProvider = core.assembleProvider(provider);
		if (selectedProvider.getId() == null || selectedProvider.getId() == 0) {
			throw new MissingFieldsException("ID is required!");
		}
		Provider dbProvider = databaseManager.getProviderById(selectedProvider
				.getId());
		if (!dbProvider.getUser().getName().equals(porsUser.getName())) {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.PROVIDER));
		}
		return core.assembleProviderDTO(dbProvider);
	}

	/**
	 * @see IController#getOrganisationDetails(UserDTO, OrganisationDTO)
	 */
	public OrganisationDTO getOrganisationDetails(UserDTO user,
			OrganisationDTO organisation) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ,
				Task.ORGANISATION));

		Organisation selectedOrganisation = core
				.assembleOrganisation(organisation);
		if (selectedOrganisation.getId() == null
				|| selectedOrganisation.getId() == 0) {
			throw new MissingFieldsException("ID is required!");
		}
		Organisation dbOrganisation = databaseManager
				.getOrganisationById(selectedOrganisation.getId());
		if (!dbOrganisation.getUser().getName().equals(porsUser.getName())) {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.ORGANISATION));
		}
		return core.assembleOrganisationDTO(dbOrganisation);
	}

	/**
	 * @see IController#getOrganisationDetails(UserDTO, OrganisationDTO)
	 */
	public AddressDTO getAddressDetails(UserDTO user, AddressDTO address)
			throws MissingRightsException, AuthentificationException, MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.ADDRESS));
		Address selectedAddress = core.assembleAddress(address);
		if (selectedAddress.getId() == null || selectedAddress.getId() <= 0) {
			throw new MissingFieldsException("ID is required!");
		}
		Address dbAddress = databaseManager.getAddressById(selectedAddress
				.getId());

		return core.assembleAddressDTO(dbAddress);
	}

	/**
	 * @see IController#getOrganisationDetails(UserDTO, OrganisationDTO)
	 */
	public LocalIdDTO getLocalIdDetails(UserDTO user, LocalIdDTO localId)
			throws MissingRightsException, AuthentificationException, MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.LOCALID));
		LocalId selectedLocalId = core.assembleLocalId(localId);
		if (selectedLocalId.getId() == null || selectedLocalId.getId() == 0) {
			throw new MissingFieldsException("ID is required!");
		}
		LocalId dbLocalId = databaseManager.getLocalIdById(selectedLocalId.getId());

		return core.assembleLocalIdDTO(dbLocalId);
	}

	/**
	 * @see IController#exportProviders(UserDTO, List<String>)
	 */
	public PorsCsv exportProviders(UserDTO user, List<String> fields)
			throws MissingRightsException, AuthentificationException, MissingFieldsException,
			CsvExportException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		authentificator.checkRights(porsUser,
				new Task(Task.READ, Task.PROVIDER));

		List<Provider> providerList = null;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.PROVIDER));
			providerList = databaseManager.getProviderList();
		} catch (MissingRightsException ex) {
			providerList = databaseManager.getProviderListOfUser(porsUser);
		}

		try {
			PorsCsv csv = new PorsCsv();
			csv.setMimeType("text/csv");
			csv.setContent(admin.exportProviders(providerList, fields)
					.toString());
			return csv;
		} catch (CsvExportException e) {
			logger.error("CsvExportException caught", e);
			throw e;
		}
	}

	/**
	 * @see IController#exportOrganisations(UserDTO, List<String>)
	 */
	public PorsCsv exportOrganisations(UserDTO user, List<String> fields)
			throws MissingRightsException, AuthentificationException, MissingFieldsException,
			CsvExportException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		
		authentificator.checkRights(porsUser, new Task(Task.READ,
				Task.ORGANISATION));

		List<Organisation> organisationList = null;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL,
					Task.ORGANISATION));
			organisationList = databaseManager.getOrganisationList();
		} catch (MissingRightsException ex) {
			organisationList = databaseManager
					.getOrganisationListOfUser(porsUser);
		}

		try {
			PorsCsv csv = new PorsCsv();
			csv.setMimeType("text/csv");
			csv.setContent(admin.exportOrganisations(organisationList, fields)
					.toString());
			return csv;
		} catch (CsvExportException e) {
			logger.error("CsvExportException caught!", e);
			throw e;
		}
	}

	/**
	 * @see IController#searchProviders(UserDTO, List<SearchCriteriaDTO>,
	 *      String)
	 */
	public List<ProviderDTO> searchProviders(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws AuthentificationException,
			MissingFieldsException, SearchException, MissingRightsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		List<SearchCriteria> criteria = new LinkedList<SearchCriteria>();

		for (int i = 0; i < criteriaDTO.size(); i++) {
			criteria.add(core.assembleSearchCriteria(criteriaDTO.get(i)));
		}
		
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.PROVIDER));
		
		boolean ownProvidersOnly = false;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL, Task.PROVIDER));
		} catch (MissingRightsException e) {
			ownProvidersOnly = true;
		}
		List<Provider> providers = entitySearcher.searchProvider(criteria,
				operator);
		
		if (providers == null) {
			logger.info("search Provider: no providers found");
			return null;
		}
		logger.info("search Provider: search was successfull");
		List<ProviderDTO> dtos = new ArrayList<ProviderDTO>();
		for (Provider provider : providers) {
			if ((ownProvidersOnly && provider.getUser().getName().equals(porsUser.getName())) || !ownProvidersOnly) {
				dtos.add(core.assembleProviderDTO(provider));
			} 
		}
		return dtos;
	}

	/**
	 * @see IController#searchOrganisations(UserDTO, List<SearchCriteriaDTO>,
	 *      String)
	 */
	public List<OrganisationDTO> searchOrganisations(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws AuthentificationException,
			MissingFieldsException, SearchException, MissingRightsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		List<SearchCriteria> criteria = new LinkedList<SearchCriteria>();

		for (int i = 0; i < criteriaDTO.size(); i++) {
			criteria.add(core.assembleSearchCriteria(criteriaDTO.get(i)));
		}
		
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.ORGANISATION));
		
		boolean ownOrganisationsOnly = false;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL, Task.ORGANISATION));
		} catch (MissingRightsException e) {
			ownOrganisationsOnly = true;
		}
		
		List<Organisation> organisations = entitySearcher.searchOrganisation(
				criteria, operator);
		if (organisations == null) {
			return null;
		}
		List<OrganisationDTO> dtos = new ArrayList<OrganisationDTO>();
		for (Organisation organisation : organisations) {
			if ((ownOrganisationsOnly && organisation.getUser().getName().equals(porsUser.getName())) || !ownOrganisationsOnly) {
				dtos.add(core.assembleOrganisationDTO(organisation));
			}
		}
		return dtos;
	}

	/**
	 * @see IController#searchHistory(UserDTO, List<SearchCriteriaDTO>,
	 *      String)
	 */
	public List<LoggingEntryDTO> searchHistory(UserDTO user,
			List<SearchCriteriaDTO> criteriaDTO, String operator)
			throws AuthentificationException,
			MissingFieldsException, SearchException, MissingRightsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		List<SearchCriteria> criteria = new LinkedList<SearchCriteria>();

		for (int i = 0; i < criteriaDTO.size(); i++) {
			criteria.add(core.assembleSearchCriteria(criteriaDTO.get(i)));
		}

		authentificator.checkRights(porsUser, new Task(Task.READ, Task.LOGGING));
		
		boolean ownHistoryOnly = false;
		try {
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL, Task.LOGGING));
		} catch (MissingRightsException e) {
			ownHistoryOnly = true;
		}
		
		List<History> entries = entitySearcher.searchLoggingEntry(criteria,
				operator);
		if (entries == null) {
			return null;
		}
		List<LoggingEntryDTO> dtos = new ArrayList<LoggingEntryDTO>();
		for (History entry : entries) {
			if ((ownHistoryOnly && entry.getUserName().equals(porsUser.getName())) || !ownHistoryOnly) {
				dtos.add(core.assembleLoggingEntryDTO(entry));
			}
		}
		return dtos;
	}

	/**
	 * @throws MissingFieldsException 
	 * @throws AuthentificationException 
	 * @see IController#importProviders(UserDTO, PorsCsv, List<String>)
	 */
	public ImportResultDTO importProviders(UserDTO user, PorsCsv csvData,
			List<String> fieldList) throws CsvImportException, AuthentificationException, MissingFieldsException {
		logger.info("password: " + user.getPassword());
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		if (!authentificator.isLdapMode()) {
			porsUser = core.assemblePorsUser(user);
		}
		return core.assembleImportResultDTO(processImport(porsUser, user.getIp(), csvData,
				fieldList, Task.PROVIDER_STRING));
	}

	/**
	 * @throws MissingFieldsException 
	 * @throws AuthentificationException 
	 * @see IController#importOrganisations(UserDTO, PorsCsv, List<String>)
	 */
	public ImportResultDTO importOrganisations(UserDTO user, PorsCsv csvData,
			List<String> fieldList) throws CsvImportException, AuthentificationException, MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		if (!authentificator.isLdapMode()) {
			porsUser = core.assemblePorsUser(user);
		}
		return core.assembleImportResultDTO(processImport(porsUser, user.getIp(), csvData,
				fieldList, Task.ORGANISATION_STRING));
	}

	/**
	 * @see IController#getDuplicates(UserDTO, int, int)
	 */
	public List<DuplicateEntryDTO> getDuplicates(UserDTO user, int maxResults,
			int offset) throws AuthentificationException, MissingFieldsException, MissingRightsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.DUPLICATES));
		List<DuplicateEntry> entries = databaseManager
				.getLimitedDuplicateEntryList(maxResults, offset);

		List<DuplicateEntryDTO> dtos = new ArrayList<DuplicateEntryDTO>();
		for (DuplicateEntry entry : entries) {
			dtos.add(core.assembleDuplicateEntryDTO(entry));
		}
		return dtos;
	}

	/**
	 * @see IController#getDuplicateDetail(UserDTO, DuplicateEntryDTO)
	 */
	public DuplicateDetailDTO getDuplicateDetail(UserDTO user,
			DuplicateEntryDTO entry) throws AuthentificationException,
			MissingFieldsException, MissingRightsException, AddressNotFoundException,
			ProviderNotFoundException, OrganisationNotFoundException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		DuplicateDetailDTO returnDTO = null;
		
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.DUPLICATES));

		Object detail = databaseManager.getDetailedDuplicateEntry(core
				.assembleDuplicateEntry(entry));
		if (entry.getDomain().equals(DuplicateEntry.DOMAIN_PROVIDER)) {
			DuplicateProvider providerDetail = (DuplicateProvider) detail;
			Provider p1 = databaseManager.getProviderById(providerDetail
					.getId().getProviderId1());
			if (p1 == null) {
				throw new ProviderNotFoundException(
						"Provider not found, id was: " + entry.getId1());
			}
			Provider p2 = databaseManager.getProviderById(providerDetail
					.getId().getProviderId2());
			if (p2 == null) {
				throw new ProviderNotFoundException(
						"Provider not found, id was: " + entry.getId2());
			}
			returnDTO = core.assembleDuplicateDetailDTO(p1, p2);
		} else if (entry.getDomain().equals(DuplicateEntry.DOMAIN_ORGANISATION)) {
			DuplicateOrganisation organisationDetail = (DuplicateOrganisation) detail;
			Organisation o1 = databaseManager
					.getOrganisationById(organisationDetail.getId()
							.getOrganisationId1());
			if (o1 == null) {
				throw new OrganisationNotFoundException(
						"Organisation not found, id was: " + entry.getId1());
			}
			Organisation o2 = databaseManager
					.getOrganisationById(organisationDetail.getId()
							.getOrganisationId2());
			if (o2 == null) {
				throw new OrganisationNotFoundException(
						"Organisation not found, id was: " + entry.getId2());
			}
			returnDTO = core.assembleDuplicateDetailDTO(o1, o2);
		} else if (entry.getDomain().equals(DuplicateEntry.DOMAIN_ADDRESS)) {
			DuplicateAddress addressDetail = (DuplicateAddress) detail;
			Address a1 = databaseManager.getAddressById(addressDetail.getId()
					.getAddressId1());
			if (a1 == null) {
				throw new AddressNotFoundException(
						"Address not found, id was: " + entry.getId1());
			}
			Address a2 = databaseManager.getAddressById(addressDetail.getId()
					.getAddressId2());
			if (a2 == null) {
				throw new AddressNotFoundException(
						"Address not found, id was: " + entry.getId2());
			}
			returnDTO = core.assembleDuplicateDetailDTO(a1, a2);
		}
		returnDTO.setDuplicateEntry(entry);

		return returnDTO;
	}

	/**
	 * @see IController#handleTaskList(List<Task>, UserDTO)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List handleTaskList(List<Task> taskList, UserDTO user)
			throws OrganisationNotFoundException, DatabaseException,
			MissingRightsException, AuthentificationException, MissingFieldsException,
			WrongValueException, ProviderNotFoundException, ImportException,
			SearchException {
		PorsUser porsUser = core.assemblePorsUser(user);
		String ip = user.getIp();
		String sessionId = UUID.randomUUID().toString();
		List returnValue = new ArrayList();
		boolean forcereturn = false;
		for (int i = 0; i < taskList.size(); i++) {
			boolean flushAndClear = false;
			// 25 because of jdbc batch size
			if (taskList.get(i).getAction() != Task.QUERY
					&& taskList.get(i).getAction() != Task.READ) {
				if (i != 0 && i % 25 == 0) {
					flushAndClear = true;
				}
			}
			Task task = taskList.get(i);
			List tempList = handleTask(task, porsUser, ip, sessionId,
					flushAndClear);
			if (null != tempList) {
				forcereturn = true;
				if (tempList.size() > 0) {
					returnValue.addAll(tempList);
				}
			}
		}
		if (returnValue.size() > 0 || forcereturn) {
			return returnValue;
		}
		return null;
	}

	/**
	 * @see IController#handleTask(Task, PorsUser, String, String, boolean)
	 */
	@SuppressWarnings("rawtypes")
	public List handleTask(Task task, PorsUser porsUser, String ip,
			String sessionId, boolean flushAndClear)
			throws AuthentificationException,
			MissingFieldsException, SearchException,
			OrganisationNotFoundException, DatabaseException,
			MissingRightsException, WrongValueException,
			ProviderNotFoundException, ImportException {
		if (flushAndClear) {
			logger.info("Flush and clear connection...");
			databaseManager.flushAndClearConnection();
		}

		// decides which method to call by checking sum of action and
		// domain
		switch (task.getActionDomain()) {
		case (Task.CREATE + Task.PROVIDER):
			task.getProvider().setIpAddress(ip);
			task.getProvider().setSessionId(sessionId);
			this.fillOrganisationsInProvider(task.getProvider());
			this.addProviderHL7(task.getProvider(), porsUser);
			logger.info("handleTaskList : Create Provider");
			break;
		case (Task.CREATE + Task.ORGANISATION):
			task.getOrganisation().setIpAddress(ip);
			task.getOrganisation().setSessionId(sessionId);
			this.addOrganisationHL7(task.getOrganisation(), porsUser);
			logger.info("handleTaskList : Create Organisation");
			break;
		case (Task.UPDATE + Task.PROVIDER):
			task.getProvider().setIpAddress(ip);
			task.getProvider().setSessionId(sessionId);
			this.fillOrganisationsInProvider(task.getProvider());
			this.updateProviderHL7(task.getProvider(), porsUser);
			logger.info("handleTaskList : Update Provider");
			break;
		case (Task.UPDATE + Task.ORGANISATION):
			task.getOrganisation().setIpAddress(ip);
			task.getOrganisation().setSessionId(sessionId);
			this.updateOrganisationHL7(task.getOrganisation(), porsUser);
			logger.info("handleTaskList : Update Organisation");
			break;
		case (Task.QUERY + Task.ORGANISATION):
			// task.getOrganisation().setIpAddress(ip);
			// task.getOrganisation().setSessionId(sessionId);
			logger.info("handleTaskList : Query Organisation");
			ArrayList<SearchCriteriaDTO> criteriaList = convertOrganisationToSearchCriterias(
					task.getOrganisation(), task.getLogicalOperator());
			for(int u=0;u<criteriaList.size();u++){
				logger.info("Organisaiton query criteria:"+criteriaList.get(u).getField()+" "+criteriaList.get(u).getValue());
			}
			List<OrganisationDTO> orgList = this.searchOrganisations(
					core.assembleUserDTO(porsUser), criteriaList,
					task.getLogicalOperator());
			if (null != orgList) {
				logger.info("handleTaskList : Query Organisation has results");
				return orgList;
			} else {
				return new ArrayList<OrganisationDTO>();
			}
		case (Task.QUERY + Task.PROVIDER):
			// task.getOrganisation().setIpAddress(ip);
			// task.getOrganisation().setSessionId(sessionId);
			logger.info("handleTaskList : Query Provider");
			ArrayList<SearchCriteriaDTO> criteriaListP = convertProviderToSearchCriterias(
					task.getProvider(), task.getLogicalOperator());
			List<ProviderDTO> proList = this.searchProviders(
					core.assembleUserDTO(porsUser), criteriaListP,
					task.getLogicalOperator());
			if (null != proList) {
				logger.info("handleTaskList : Query Provider has results:");
				return proList;
			} else {
				return new ArrayList<ProviderDTO>();
			}
		default:
			throw new ImportException(
					"Error while importing, caused by unrecognised action/domain combination");
		}

		return null;
	}

	/**
	 * @see IController#updateDuplicateConfiguration(DuplicateConfigurationDTO,
	 *      UserDTO)
	 */
	public void updateDuplicateConfiguration(DuplicateConfigurationDTO dto,
			UserDTO userDto) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser user = authentificator.authUser(core.assemblePorsUser(userDto));
		authentificator.checkRights(user, new Task(Task.CONFIGURE, Task.DUPLICATES));
		
		databaseManager.updateDuplicateConfiguration(core
				.assembleDuplicateConfiguration(dto));

	}

	/**
	 * @see IController#getDuplicateConfiguration(UserDTO)
	 */
	public DuplicateConfigurationDTO getDuplicateConfiguration(UserDTO userDto)
			throws MissingRightsException, AuthentificationException, MissingFieldsException {
		PorsUser user = authentificator.authUser(core.assemblePorsUser(userDto));

		authentificator.checkRights(user, new Task(Task.CONFIGURE, Task.DUPLICATES));
		return core.assembleDuplicateConfigurationDTO(databaseManager
				.getDuplicateConfiguration());

	}

	/**
	 * @see IController#getImportResult(UserDTO, ImportResultDTO)
	 */
	public ImportResultDTO getImportResult(UserDTO user,
			ImportResultDTO resultDTO) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		ImportResult result = importLogger.getImportResult(core.assembleImportResult(resultDTO).getJobId());
		
		if (result.getDomain().equals(Task.PROVIDER_STRING)) {
			authentificator.checkRights(porsUser, new Task(Task.CREATE, Task.PROVIDER));
		} else if (result.getDomain().equals(Task.ORGANISATION_STRING)) {
			authentificator.checkRights(porsUser, new Task(Task.CREATE, Task.ORGANISATION));
		}
		
		return core.assembleImportResultDTO(result);
	}

	/**
	 * @see IController#getAllRoles(UserDTO)
	 */
	public List<UserRoleDTO> getAllRoles(UserDTO user)
			throws MissingRightsException, AuthentificationException, MissingFieldsException {
		logger.info("getAllRoles called");
		logger.info("username: " + user.getUsername() + " - password: "
				+ user.getPassword());
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		
		authentificator.checkRights(porsUser, new Task(Task.CONFIGURE, Task.USER));
		
		List<UserRole> roleList = null;

		roleList = databaseManager.getUserRoleList();

		List<UserRoleDTO> roleDtoList = new ArrayList<UserRoleDTO>();

		for (int i = 0; i < roleList.size(); i++) {
			roleDtoList.add(core.assembleRoleDTO(roleList.get(i)));
		}

		return roleDtoList;
	}

	/**
	 * @see IController#updateUser(UserDTO, UserDTO)
	 */
	public void updateUser(UserDTO user, UserDTO userUpdate)
			throws MissingRightsException, UserNotFoundException,
			AuthentificationException, MissingFieldsException,
			WrongValueException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		authentificator.checkRights(porsUser, new Task(Task.UPDATE, Task.USER));

		PorsUser updateUser = core.assemblePorsUser(userUpdate);
		PorsUser dbUser = databaseManager.getUser(updateUser);
		if (!dbUser.getPassword().equals(updateUser.getPassword())) {
			// new hash
			updateUser.setPassword(authentificator.getHashString(updateUser
					.getPassword()));
		}
		core.validateUser(updateUser);
		databaseManager.updateUser(updateUser);
	}

	/**
	 * @see IController#clearDuplicateEntry(UserDTO, DuplicateDetailDTO,
	 *      DuplicateProcessingInfoDTO)
	 */
	public void clearDuplicateEntry(UserDTO user, DuplicateDetailDTO detailDTO,
			DuplicateProcessingInfoDTO infoDTO) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException, DatabaseException {

		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		
		authentificator.checkRights(porsUser, new Task(Task.UPDATE, Task.DUPLICATES));
		
		String domain = detailDTO.getDuplicateEntry().getDomain();

		switch (infoDTO.getDuplicateStrategy()) {

		case (DuplicateProcessingInfoDTO.DSKEEPFIRST):
			removeDuplicateObject(detailDTO, domain, false, porsUser,
					infoDTO.getIp(), infoDTO.getSessionId());
			break;
		case (DuplicateProcessingInfoDTO.DSKEEPSECOND):
			removeDuplicateObject(detailDTO, domain, true, porsUser,
					infoDTO.getIp(), infoDTO.getSessionId());
			break;
		case (DuplicateProcessingInfoDTO.DSREPLACEBOTH):
			if (domain.equals(DuplicateEntry.DOMAIN_PROVIDER)) {
				// needed for deletion of duplicate entry at the end
				infoDTO.getMergedProvider().setIp(infoDTO.getIp());
				infoDTO.getMergedProvider()
						.setSessionId(infoDTO.getSessionId());
				infoDTO.getMergedProvider().setEditingUserID(user.getId());
				long duplicateId = detailDTO.getProvider1().getId();
				
				List<LocalId> p1LocalIDs = core.assembleProvider(detailDTO.getProvider1()).getLocalIds();
				if (p1LocalIDs != null && p1LocalIDs.size() > 0) {
					databaseManager.deleteLocalIDs(p1LocalIDs);
				}
				List<LocalId> p2LocalIDs = core.assembleProvider(detailDTO.getProvider2()).getLocalIds();
				if (p2LocalIDs != null && p2LocalIDs.size() > 0) {
					databaseManager.deleteLocalIDs(p2LocalIDs);
				}
				
				databaseManager.deleteProvider(
						detailDTO.getProvider1().getId(), porsUser,
						infoDTO.getIp(), infoDTO.getSessionId());
				databaseManager.deleteProvider(
						detailDTO.getProvider2().getId(), porsUser,
						infoDTO.getIp(), infoDTO.getSessionId());
				
				Provider mergedProvider = core.assembleProvider(infoDTO.getMergedProvider());
				logger.info("Firstname merged: " + mergedProvider.getFirstName());
				mergedProvider.setUser(porsUser);
				mergedProvider.setEditingUser(porsUser);
				databaseManager.addProvider(mergedProvider);
				
				databaseManager.deleteProviderDuplicates(duplicateId);
			} else if (domain.equals(DuplicateEntry.DOMAIN_ORGANISATION)) {
				// needed for deletion of duplicate entry at the end
				infoDTO.getMergedOrganisation().setIp(infoDTO.getIp());
				infoDTO.getMergedOrganisation().setSessionId(
						infoDTO.getSessionId());
				infoDTO.getMergedOrganisation().setEditingUserID(user.getId());
				long duplicateId = detailDTO.getOrganisation1().getId();
				databaseManager.deleteOrganisation(detailDTO.getOrganisation1()
						.getId(), porsUser, infoDTO.getIp(), infoDTO
						.getSessionId());
				databaseManager.deleteOrganisation(detailDTO.getOrganisation2()
						.getId(), porsUser, infoDTO.getIp(), infoDTO
						.getSessionId());
				Organisation mergedOrganisation = core.assembleOrganisation(infoDTO.getMergedOrganisation());
				mergedOrganisation.setUser(porsUser);
				mergedOrganisation.setEditingUser(porsUser);
				databaseManager.addOrganisation(mergedOrganisation);
				
				databaseManager.deleteOrganisationDuplicates(duplicateId);
			} else if (domain.equals(DuplicateEntry.DOMAIN_ADDRESS)) {
				Address a1 = databaseManager.getAddressById(detailDTO
						.getAddress1().getId());
				Address a2 = databaseManager.getAddressById(detailDTO
						.getAddress2().getId());
				Address mergedAddress = core.assembleAddress(infoDTO
						.getMergedAddress());
				// needed for deletion of duplicate entry at the end
				long duplicateId = a1.getId();

				List<Provider> a1Providers = a1.getProviders();
				for (Provider provider : a1Providers) {
					if (!provider.getAddresses().remove(a1)) {
						logger.warn("address could not be removed!");
					}
					databaseManager.updateProvider(provider);
				}
				List<Organisation> a1Organisations = a1.getOrganisations();
				for (Organisation organisation : a1Organisations) {
					if (!organisation.getAddresses().remove(a1)) {
						logger.warn("address could not be removed!");
					}
					databaseManager.updateOrganisation(organisation);
				}

				List<Provider> a2Providers = a2.getProviders();
				for (Provider provider : a2Providers) {
					if (!provider.getAddresses().remove(a2)) {
						logger.warn("address could not be removed!");
					}
					databaseManager.updateProvider(provider);
				}
				List<Organisation> a2Organisations = a2.getOrganisations();
				for (Organisation organisation : a2Organisations) {
					if (!organisation.getAddresses().remove(a2)) {
						logger.warn("address could not be removed!");
					}
					databaseManager.updateOrganisation(organisation);
				}
				// add merged address
				for (Provider provider : a1Providers) {
					provider.getAddresses().add(mergedAddress);
					databaseManager.updateProvider(provider);
				}
				for (Organisation organisation : a1Organisations) {
					organisation.getAddresses().add(mergedAddress);
					databaseManager.updateOrganisation(organisation);
				}
				for (Provider provider : a2Providers) {
					provider.getAddresses().add(mergedAddress);
					databaseManager.updateProvider(provider);
				}
				for (Organisation organisation : a2Organisations) {
					organisation.getAddresses().add(mergedAddress);
					databaseManager.updateOrganisation(organisation);
				}
				databaseManager.deleteAddressDuplicates(duplicateId);
			}
			break;
		case (DuplicateProcessingInfoDTO.DSKEEPBOTH):
			if (domain.equals(DuplicateEntry.DOMAIN_PROVIDER)) {
				databaseManager.deleteProviderDuplicates(detailDTO
						.getProvider1().getId());
			} else if (domain.equals(DuplicateEntry.DOMAIN_ORGANISATION)) {
				databaseManager.deleteOrganisationDuplicates(detailDTO
						.getOrganisation1().getId());
			} else if (domain.equals(DuplicateEntry.DOMAIN_ADDRESS)) {
				databaseManager.deleteAddressDuplicates(detailDTO.getAddress1()
						.getId());
			}
			break;
		}

	}

	/**
	 * @see IController#getProviderCount(UserDTO)
	 */
	public long getProviderCount(UserDTO user) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.PROVIDER));
		
		try { 
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL, Task.PROVIDER));
			return databaseManager.getProviderNumber();
		} catch (MissingRightsException e) {
			return databaseManager.getProviderNumber(porsUser);
		}
	}

	/**
	 * @see IController#getOrganisationCount(UserDTO)
	 */
	public long getOrganisationCount(UserDTO user)
			throws MissingRightsException, AuthentificationException, MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.ORGANISATION));
		
		try { 
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL, Task.ORGANISATION));
			return databaseManager.getOrganisationNumber();
		} catch (MissingRightsException e) {
			return databaseManager.getOrganisationNumber(porsUser);
		}
	}

	/**
	 * @see IController#getHistoryCount(UserDTO)
	 */
	public long getHistoryCount(UserDTO user) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.LOGGING));
		
		try { 
			authentificator.checkRights(porsUser, new Task(Task.READ_ALL, Task.LOGGING));
			return databaseManager.getHistoryNumber();
		} catch (MissingRightsException e) {
			return databaseManager.getHistoryNumber(porsUser);
		}
	}

	/**
	 * @see IController#rebuildSearchindex(UserDTO)
	 */
	public void rebuildSearchindex(UserDTO user) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));

		authentificator.checkRights(porsUser, new Task(Task.CONFIGURE, Task.SYSTEM));
		entitySearcher.rebuildSearchindex();
	}

	/**
	 * @see IController#addUser(UserDTO,UserDTO)
	 **/
	public void addUser(UserDTO newUserDTO, UserDTO userDTO)
			throws DatabaseException, MissingRightsException,
			AuthentificationException,
			MissingFieldsException, WrongValueException {

		logger.info("username: " + newUserDTO.getUsername());

		PorsUser newUser = core.assemblePorsUser(newUserDTO);
		if (authentificator.isLdapMode()) {
			newUser.setName(authentificator.getLdapDn(newUser.getName()));
		}
		PorsUser user = authentificator.authUser(core.assemblePorsUser(userDTO));
		authentificator.checkRights(user, new Task(Task.CREATE, Task.USER));

		logger.info("username after assembling: " + newUser.getName());

		core.validateUser(newUser);
		newUser.setActive(true);
		newUser.setPassword(authentificator.getHashString(newUser.getPassword()));
		databaseManager.addPorsUser(newUser);
	}

	/**
	 * @see IController#getDuplicateCount(UserDTO)
	 */
	public long getDuplicateCount(UserDTO user) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.DUPLICATES));

		return databaseManager.getDuplicateEntryNumber();
	}

	/**
	 * @see IController#getUsers(UserDTO)
	 */
	public List<UserDTO> getUsers(UserDTO user) throws MissingRightsException,
			AuthentificationException,
			MissingFieldsException {
		PorsUser porsUser = authentificator.authUser(core.assemblePorsUser(user));
		authentificator.checkRights(porsUser, new Task(Task.READ, Task.USER));
		List<UserDTO> users = new ArrayList<UserDTO>();
		for (PorsUser tmpUser : databaseManager.getUserList()) {
			users.add(core.assembleUserDTO(tmpUser));
		}
		return users;
	}

	private ArrayList<SearchCriteriaDTO> convertOrganisationToSearchCriterias(
			Organisation organisation, String operator) {
		ArrayList<SearchCriteriaDTO> searchList = new ArrayList<SearchCriteriaDTO>();
		if (null != organisation.getId()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("id");
			searchCriteria.setValue(organisation.getId().toString());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getDeactivationDate()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("deactivationDate");
			searchCriteria.setValue(dateFormat.format(organisation
					.getDeactivationDate()));
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getDeactivationReasonCode()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("deactivationReasonCode");
			searchCriteria.setValue(organisation.getDeactivationReasonCode());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getDescription()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("description");
			searchCriteria.setValue(organisation.getDescription());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getEmail()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("email");
			searchCriteria.setValue(organisation.getEmail());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getEstablishmentId()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("establishmentId");
			searchCriteria.setValue(organisation.getEstablishmentId());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getFax()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("fax");
			searchCriteria.setValue(organisation.getFax());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getName()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("name");
			searchCriteria.setValue(organisation.getName());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getOid()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("oid");
			searchCriteria.setValue(organisation.getOid());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getReactivationDate()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("reactivationDate");
			searchCriteria.setValue(dateFormat.format(organisation
					.getReactivationDate()));
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getSecondName()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("secondName");
			searchCriteria.setValue(organisation.getSecondName());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != organisation.getTelephone()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("telephone");
			searchCriteria.setValue(organisation.getTelephone());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}

		if (null != organisation.getAddresses()) {
			List<Address> addresses = organisation.getAddresses();
			for (int i = 0; i < addresses.size(); i++) {
				Address address = addresses.get(i);
				if (null != address.getCity()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.city");
					searchCriteria.setValue(address.getCity());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getCountry()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.country");
					searchCriteria.setValue(address.getCountry());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getHouseNumber()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.houseNumber");
					searchCriteria.setValue(address.getHouseNumber());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getState()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.state");
					searchCriteria.setValue(address.getState());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getStreet()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.street");
					searchCriteria.setValue(address.getStreet());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getZipCode()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.zipCode");
					searchCriteria.setValue(address.getZipCode());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
			}
		}

		if (null != organisation.getLocalIds()) {
			List<LocalId> localIds = organisation.getLocalIds();
			for (int i = 0; i < localIds.size(); i++) {
				LocalId localId = localIds.get(i);
				if (null != localId.getApplication()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("localIds.application");
					searchCriteria.setValue(localId.getApplication());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != localId.getFacility()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("localIds.facility");
					searchCriteria.setValue(localId.getFacility());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != localId.getLocalId()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("localIds.localId");
					searchCriteria.setValue(localId.getLocalId());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
			}
		}
		return searchList;
	}

	private ArrayList<SearchCriteriaDTO> convertProviderToSearchCriterias(
			Provider provider, String operator) {
		ArrayList<SearchCriteriaDTO> searchList = new ArrayList<SearchCriteriaDTO>();
		if (null != provider.getId()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("id");
			searchCriteria.setValue(provider.getId().toString());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getDeactivationDate()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("deactivationDate");
			searchCriteria.setValue(dateFormat.format(provider
					.getDeactivationDate()));
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getDeactivationReasonCode()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("deactivationReasonCode");
			searchCriteria.setValue(provider.getDeactivationReasonCode());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getBirthday()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("birthday");
			searchCriteria.setValue(dateFormat.format(provider.getBirthday()));
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getEmail()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("email");
			searchCriteria.setValue(provider.getEmail());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getFax()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("fax");
			searchCriteria.setValue(provider.getFax());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getFirstName()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("firstName");
			searchCriteria.setValue(provider.getFirstName());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getLastName()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("lastName");
			searchCriteria.setValue(provider.getLastName());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getGenderCode()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("genderCode");
			searchCriteria.setValue(provider.getGenderCode());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getLanr()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("lanr");
			searchCriteria.setValue(provider.getLanr());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getMiddleName()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("middleName");
			searchCriteria.setValue(provider.getMiddleName());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getNamePrefix()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("namePrefix");
			searchCriteria.setValue(provider.getNamePrefix());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getNameSuffix()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("nameSuffix");
			searchCriteria.setValue(provider.getNameSuffix());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getOid()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("oid");
			searchCriteria.setValue(provider.getOid());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getReactivationDate()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("reactivationDate");
			searchCriteria.setValue(dateFormat.format(provider
					.getReactivationDate()));
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}
		if (null != provider.getTelephone()) {
			SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
			searchCriteria.setField("telephone");
			searchCriteria.setValue(provider.getTelephone());
			searchCriteria.setOperator(operator);
			searchList.add(searchCriteria);
		}

		if (null != provider.getAddresses()) {
			List<Address> addresses = provider.getAddresses();
			for (int i = 0; i < addresses.size(); i++) {
				Address address = addresses.get(i);
				if (null != address.getCity()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.city");
					searchCriteria.setValue(address.getCity());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getCountry()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.country");
					searchCriteria.setValue(address.getCountry());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getHouseNumber()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.houseNumber");
					searchCriteria.setValue(address.getHouseNumber());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getState()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.state");
					searchCriteria.setValue(address.getState());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getStreet()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.street");
					searchCriteria.setValue(address.getStreet());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != address.getZipCode()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("addresses.zipCode");
					searchCriteria.setValue(address.getZipCode());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
			}
		}

		if (null != provider.getLocalIds()) {
			List<LocalId> localIds = provider.getLocalIds();
			for (int i = 0; i < localIds.size(); i++) {
				LocalId localId = localIds.get(i);
				if (null != localId.getApplication()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("localIds.application");
					searchCriteria.setValue(localId.getApplication());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != localId.getFacility()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("localIds.facility");
					searchCriteria.setValue(localId.getFacility());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
				if (null != localId.getLocalId()) {
					SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
					searchCriteria.setField("localIds.localId");
					searchCriteria.setValue(localId.getLocalId());
					searchCriteria.setOperator(operator);
					searchList.add(searchCriteria);
				}
			}
		}
		return searchList;
	}

	private ImportResult processImport(PorsUser user, String ip, PorsCsv csvData,
			List<String> fieldList, String domain) throws CsvImportException {
		ImportResult result = new ImportResult();
		result.setStartDate(new Date());
		importLogger.saveImportResult(result);

		ImportRequest request = new ImportRequest();
		request.setCsvData(csvData);
		request.setDomain(domain);
		request.setFieldList(fieldList);
		request.setIp(ip);
		request.setResult(result);
		request.setSessionId(UUID.randomUUID().toString());
		request.setUser(user);

		Connection connection = null;
		Session session = null;

		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(destination);
			ObjectMessage message = session.createObjectMessage();
			message.setObject(request);
			producer.send(message);
			logger.info("Message send to queue");
			return result;
		} catch (JMSException e) {
			result.setStatusMessage("Failed");
			result.setErrorMessage(e.getMessage());
			importLogger.saveImportResult(result);
			logger.error("JMS Exception found", e);
			throw new CsvImportException(e.getMessage());
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				logger.error(e);
			}
		}
	}

	private void removeDuplicateObject(DuplicateDetailDTO detailDTO,
			String domain, boolean first, PorsUser porsUser, String ip,
			String sessionId) {
		long id = 0;
		if (domain.equals(DuplicateEntry.DOMAIN_PROVIDER)) {
			if (first) {
				id = detailDTO.getProvider1().getId();
			} else {
				id = detailDTO.getProvider2().getId();
			}
			databaseManager.deleteProviderDuplicates(id);
			databaseManager.deleteProvider(id, porsUser, ip, sessionId);
		} else if (domain.equals(DuplicateEntry.DOMAIN_ORGANISATION)) {
			if (first) {
				id = detailDTO.getOrganisation1().getId();
			} else {
				id = detailDTO.getOrganisation2().getId();
			}
			databaseManager.deleteOrganisationDuplicates(id);
			databaseManager.deleteOrganisation(id, porsUser, ip, sessionId);
		} else if (domain.equals(DuplicateEntry.DOMAIN_ADDRESS)) {
			if (first) {
				id = detailDTO.getAddress1().getId();
			} else {
				id = detailDTO.getAddress2().getId();
			}
			databaseManager.deleteAddressDuplicates(id);
			databaseManager.deleteAddress(id, porsUser, ip, sessionId);
		}
	}

}
