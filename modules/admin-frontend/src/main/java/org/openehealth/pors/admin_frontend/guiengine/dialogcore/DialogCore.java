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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.AddressBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.DuplicateDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.DuplicateDetails;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.HistoryDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.ImportBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.ImportWrapper;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.LocalIdBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.LogTableBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.OrganisationDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.ProviderDataModel;
import org.openehealth.pors.admin_frontend.guiengine.dialogdata.SearchBean;
import org.openehealth.pors.admin_frontend.guiengine.dialogpresentation.Presenter;
import org.openehealth.pors.admin_frontend.guiengine.util.Fields;
import org.openehealth.pors.admin_frontend.guiengine.util.MessageHandler;
import org.openehealth.pors.admin_frontend.guiengine.util.ResourceHandler;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import org.openehealth.pors.admin_client.AddressDTO;
import org.openehealth.pors.admin_client.DuplicateConfigurationDTO;
import org.openehealth.pors.admin_client.DuplicateEntryDTO;
import org.openehealth.pors.admin_client.DuplicateProcessingInfoDTO;
import org.openehealth.pors.admin_client.IPorsAdministratorService;
import org.openehealth.pors.admin_client.ImportResultDTO;
import org.openehealth.pors.admin_client.LocalIdDTO;
import org.openehealth.pors.admin_client.LoggingDetailDTO;
import org.openehealth.pors.admin_client.LoggingEntryDTO;
import org.openehealth.pors.admin_client.OrganisationDTO;
import org.openehealth.pors.admin_client.PORSException_Exception;
import org.openehealth.pors.admin_client.PermissionDTO;
import org.openehealth.pors.admin_client.PorsCsv;
import org.openehealth.pors.admin_client.ProviderDTO;
import org.openehealth.pors.admin_client.SearchCriteriaDTO;
import org.openehealth.pors.admin_client.UserDTO;
import org.openehealth.pors.admin_client.UserRoleDTO;
import org.openehealth.pors.admin_client.custom.PORSAdministratorServiceConnector;

@SuppressWarnings("restriction")
public class DialogCore implements IDialogCore {

	private Presenter presenter;
	private Logger logger;
	private String sessionId;
	private String sessionIp;

	// general data
	private SelectItem[] years = new SelectItem[100];
	private SelectItem[] months = new SelectItem[12];
	private SelectItem[] days = new SelectItem[31];
	private int startYear;

	private ArrayList<OrganisationDTO> organizationList;
	
	private List<OrganisationDTO> providerOrganisations;
	private ArrayList<OrganisationDTO> organizationListSelected;
	private List<String> result = new ArrayList<String>();

	private UserDTO currentUser = new UserDTO();
	private PermissionDTO permissions = new PermissionDTO();
	
	private AddressBean newAddress = new AddressBean();
	private AddressBean newAddressTemp = new AddressBean();
	private ArrayList<AddressBean> addresses = new ArrayList<AddressBean>();

	private LocalIdBean newLocalId = new LocalIdBean();
	private LocalIdBean newLocalIdTemp;
	private ArrayList<LocalIdBean> localIds = new ArrayList<LocalIdBean>();

	private String selectedLocalId;
	private String selectedAddressString;
	private String newPassword;
	private SelectItem[] providerLocalIds = new SelectItem[0];
	private SelectItem[] countryCodes = new SelectItem[0];

	private ProviderDTO provider = new ProviderDTO();
	private OrganisationDTO org = new OrganisationDTO();
	private UserDTO newUser = new UserDTO();
	private LoggingEntryDTO history = new LoggingEntryDTO();
	private LoggingDetailDTO historyDetail = new LoggingDetailDTO();
	private DuplicateConfigurationDTO duplicateConfiguration;
	
	// for admin service
	private static final String WSDL_LOCATION_KEY = "wsdl.location";
	private String wsdlLocation;
	
	PorsCsv exportResult;
	HttpServletResponse response;
	
	private List<String> availableProviderFields;
	private List<String> selectedProviderFields;
	
	private List<String> availableOrganisationFields;
	private List<String> selectedOrganisationFields;
	
	private ImportBean importProviderBean;
	private ImportBean importOrganisationBean;	
	
	private SearchBean searchBean;
	
	private DuplicateDetails duplicateDetails;
	
	private ProviderDataModel providerDataModel;
	private HistoryDataModel historyDataModel;
	private OrganisationDataModel organisationDataModel;
	private DuplicateDataModel duplicateDataModel;
	
	private List<SelectItem> availableRoles;
	private String selectedRole;
	
	private List<SelectItem> availableUsers;
	private Integer selectedUserId;
	

	{
		init();
	}

	/**
	 * gets providerOrganisations
	 * @return
	 */
	public List<OrganisationDTO> getProviderOrganisations() {
		return providerOrganisations;
	}

	/**
	 * sets providerOrganisations
	 */
	public void setProviderOrganisations(
			List<OrganisationDTO> providerOrganisations) {
		this.providerOrganisations = providerOrganisations;
	}

	/**
	 * gets organizationList
	 * @return
	 */
	public ArrayList<OrganisationDTO> getOrganizationList() {
		return organizationList;
	}

	/**
	 * sets organizationList
	 */
	public void setOrganizationList(ArrayList<OrganisationDTO> organizationList) {
		this.organizationList = organizationList;
	}

	/**
	 * constructor to bind presenter object to {@link DialogCore}
	 * @param presenter
	 */
	public DialogCore(Presenter presenter) {
		this.presenter = presenter;
	}
	
	
	/**
	 * initialises basic data structures.
	 * <ul>
	 *  <li>loads country codes</li>
	 *  <li>loads possible birthday years, months and days</li>
	 *  <li>loads wsdl properties</li>
	 *  <li>initialises logger</li>
	 * </ul>
	 */
	private void init() {
		String[] codes = Locale.getISOCountries();
		countryCodes = new SelectItem[codes.length];
		for (int i = 0; i < codes.length; i++) {
			countryCodes[i] = new SelectItem(codes[i]);
		}
		logger = Logger.getLogger(DialogCore.class);

		startYear = Calendar.getInstance().get(Calendar.YEAR) - years.length;
		for (int i = 0; i < years.length; i++) {
			years[i] = new SelectItem(startYear + i);
		}

		for (int i = 0; i < months.length; i++) {
			months[i] = new SelectItem(i + 1);
		}

		for (int i = 0; i < days.length; i++) {
			days[i] = new SelectItem(i + 1);
		}

		try {
			Properties wsdlProperties = new Properties();
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream("wsdl.properties");
			wsdlProperties.load(in);
			wsdlLocation = wsdlProperties.getProperty(WSDL_LOCATION_KEY);
			in.close();
		} catch (IOException e) {
			logger.warn("Could not load wsdl properties file", e);
		}
		
		availableProviderFields = Fields.getInstance().getProviderFields(false);
		availableOrganisationFields = Fields.getInstance().getOrganisationFields(false);
	}
	
	/**
	 * sets deactivate flag for organisation and sets [re|de]activation date
	 * @param deactive
	 * @throws DatatypeConfigurationException
	 */
	public void setOrgdeactive(boolean deactive) throws DatatypeConfigurationException{
		logger.warn("setDeactive called : "+deactive);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date(System.currentTimeMillis()));
		XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		if(deactive){
			getOrg().setDeactivationDate(date);
		}else{
			getOrg().setReactivationDate(date);
		}
	}
	
	/**
	 * gets deactivate flag of organisation
	 * @return
	 */
	public boolean getOrgdeactive(){
		if(null!=getOrg().getReactivationDate() && null!=getOrg().getDeactivationDate()){
			int comparison = getOrg().getReactivationDate().compare(getOrg().getDeactivationDate());
			switch(comparison){
				case DatatypeConstants.LESSER:
					return true;
				case DatatypeConstants.GREATER:
					return false;
				case DatatypeConstants.EQUAL:
					return true;
				default:
					return true;
			}
		} else if(null!=getOrg().getDeactivationDate()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * sets deactivate flag for provider and sets [re|de]activation date
	 * @param deactive
	 * @throws DatatypeConfigurationException
	 */
	public void setDeactive(boolean deactive) throws DatatypeConfigurationException{
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date(System.currentTimeMillis()));
		XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		if(deactive){
			getProvider().setDeactivationDate(date);
		}else{
			getProvider().setReactivationDate(date);
		}
	}
	
	/**
	 * gets deactivate flag of organisation
	 * @return
	 */
	public boolean getDeactive(){
		if(null!=getProvider().getReactivationDate() && null!=getProvider().getDeactivationDate()){
			int comparison = getProvider().getReactivationDate().compare(getProvider().getDeactivationDate());
			switch(comparison){
				case DatatypeConstants.LESSER:
					return true;
				case DatatypeConstants.GREATER:
					return false;
				case DatatypeConstants.EQUAL:
					return true;
				default:
					return true;
			}
		} else if(null!=getProvider().getDeactivationDate()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * gets newAddressTemp
	 * @return
	 */
	public AddressBean getNewAddressTemp() {
		return newAddressTemp;
	}

	/**
	 * sets newAddressTemp
	 */
	public void setNewAddressTemp(AddressBean newAddressTemp) {
		this.newAddressTemp = newAddressTemp;
	}

	/**
	 * gets organisation list
	 * @return
	 */
	public List<String> getResult() {
		return result;
	}

	/**
	 * sets organisation list
	 */
	public void setResult(List<String> result) {
		this.result = result;
	}

	/**
	 * @see DialogCore
	 */
	public String login() {
		try {
			FacesContext fCtx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fCtx.getExternalContext()
					.getSession(true);
			sessionId = session.getId();
			HttpServletRequest request = (HttpServletRequest) fCtx
					.getExternalContext().getRequest();
			sessionIp = request.getRemoteHost();

			IPorsAdministratorService port = connectWS();
			port.authUser(currentUser);
			permissions = port.getUserPermissions(currentUser, currentUser);
			presenter.setUserLoggedIn(true);
			return "success";
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_login_error", new Object[] { pex
									.getFaultInfo().getErrorDescription() }));
			logger.error("PORSException caught ("
					+ pex.getFaultInfo().getErrorDescription() + ")", pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_login_error", new Object[] { sfex.getMessage() }));
			logger.error("SOAPFaultException caught", sfex);
		}
		return null;
	}

	/**
	 * adds current address to address list
	 */
	public void addAddress() {
		this.addresses.add(new AddressBean(newAddress));
		presenter.setAddressModified(true);
	}

	/**
	 * adds current localId to address list
	 */
	public void addLocalId() {
		this.localIds.add(new LocalIdBean(newLocalId));
		presenter.setLocalIdModified(true);
	}

	/**
	 * removes current address from address list
	 */
	public void removeAddress() {
		String selected = getSelectedAddressString();
		ArrayList<AddressBean> temp = new ArrayList<AddressBean>();
		for (AddressBean add : this.addresses) {
			if (!add.toString().equals(selected)) {
				temp.add(add);
			}
		}
		this.addresses = temp;
	}

	/**
	 * removes given address from address list
	 * @param removeAdd
	 */
	public void removeAddress(AddressBean removeAdd) {
		ArrayList<AddressBean> temp = new ArrayList<AddressBean>();
		for (AddressBean add : this.addresses) {
			if (!add.equals(removeAdd)) {
				temp.add(add);
			}
		}
		this.addresses = temp;
	}

	/**
	 * removes current localId from localId list
	 * @param removeAdd
	 */
	public void removeLocalId() {
		String selected = getSelectedLocalId();
		ArrayList<LocalIdBean> temp = new ArrayList<LocalIdBean>();
		for (LocalIdBean localId : this.localIds) {
			if (!localId.toString().equals(selected)) {
				temp.add(localId);
			}
		}
		this.localIds = temp;
	}

	/**
	 * removes given localId from localId list
	 * @param removeAdd
	 */
	public void removeLocalId(LocalIdBean id) {
		ArrayList<LocalIdBean> temp = new ArrayList<LocalIdBean>();
		for (LocalIdBean localId : this.localIds) {
			if (!localId.equals(id)) {
				temp.add(localId);
			}
		}
		this.localIds = temp;
	}

	/**
	 * gets newLocalIdTemp
	 * @return
	 */
	public LocalIdBean getNewLocalIdTemp() {
		return newLocalIdTemp;
	}

	/**
	 * sets newLocalIdTemp
	 */
	public void setNewLocalIdTemp(LocalIdBean newLocalIdTemp) {
		this.newLocalIdTemp = newLocalIdTemp;
	}

	/**
	 * gets addresses
	 * @return
	 */
	public ArrayList<AddressBean> getAddresses() {
		return addresses;
	}
	
	/**
	 * sets addresses
	 */
	public void setAddresses(List<AddressDTO> addresses) {
		ArrayList<AddressBean> adds = new ArrayList<AddressBean>();
		for (AddressDTO addressDTO : addresses) {
			adds.add(new AddressBean(addressDTO));
		}
		this.addresses = adds;
	}

	/**
	 * gets localIds
	 * @return
	 */
	public ArrayList<LocalIdBean> getLocalIds() {
		return localIds;
	}

	/**
	 * sets localIds
	 */
	public void setLocalIds(List<LocalIdDTO> localIdDTOs) {
		ArrayList<LocalIdBean> localIds = new ArrayList<LocalIdBean>();
		for (LocalIdDTO localIdDTO : localIdDTOs) {
			localIds.add(new LocalIdBean(localIdDTO));
		}
		this.localIds = localIds;
	}

	/**
	 * gets permissions
	 * @return
	 */
	public PermissionDTO getPermissions() {
		return permissions;
	}

	/**
	 * sets permissions
	 */
	public void setPermissions(PermissionDTO permissions) {
		this.permissions = permissions;
	}

	/**
	 * gets startYear
	 * @return
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * gets sessionId
	 * @return
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * sets sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * gets day of birthday of provider
	 * @return
	 */
	public String getProviderBirthdateDay() {
		GregorianCalendar gregorianCalendar = this.provider.getBirthday()
				.toGregorianCalendar();
		return gregorianCalendar.get(GregorianCalendar.DATE) + "";

	}

	/**
	 * gets month of birthday of provider
	 * @return
	 */
	public String getProviderBirthdateMonth() {
		GregorianCalendar gregorianCalendar = this.provider.getBirthday()
				.toGregorianCalendar();
		return (gregorianCalendar.get(GregorianCalendar.MONTH) + 1) + "";

	}

	/**
	 * gets year of birthday of provider
	 * @return
	 */
	public String getProviderBirthdateYear() {
		GregorianCalendar gregorianCalendar = this.provider.getBirthday()
				.toGregorianCalendar();
		return gregorianCalendar.get(GregorianCalendar.YEAR) + "";

	}

	/**
	 * gets selectedLocalId
	 * @return
	 */
	public String getSelectedLocalIdProvider() {
		return selectedLocalId;
	}

	/**
	 * sets selectedLocalId
	 */
	public void setSelectedLocalIdProvider(String selectedLocalIdProvider) {
		this.selectedLocalId = selectedLocalIdProvider;
	}

	/**
	 * gets selectedAddressString
	 * @return
	 */
	public String getSelectedAddressString() {
		return selectedAddressString;
	}

	/**
	 * sets selectedAddressString
	 */
	public void setSelectedAddressString(String selectedAddress) {
		this.selectedAddressString = selectedAddress;
	}

	/**
	 * gets selectedLocalId
	 * @return
	 */
	public String getSelectedLocalId() {
		return selectedLocalId;
	}

	/**
	 * sets selectedLocalId
	 */
	public void setSelectedLocalId(String selectedLocalId) {
		this.selectedLocalId = selectedLocalId;
	}

	/**
	 * gets corresponding AddressDTO for given string
	 * @return
	 */
	public AddressDTO getAddressDTOForString(String selectedAdd) {
		for (AddressDTO add : getProvider().getAddresses()) {
			AddressBean addressBean = new AddressBean(add);
			if (selectedAdd.equals(addressBean.toString())) {
				return add;
			}
		}
		return null;
	}

	/**
	 * gets corresponding AddressBean for given string
	 * @return
	 */
	public AddressBean getAddressBeanForString(String selectedAdd) {
		for (AddressBean add : this.addresses) {
			if (selectedAdd.equals(add.toString())) {
				return add;
			}
		}
		return new AddressBean();
	}

	/**
	 * gets corresponding LocalIdDTO for given string
	 * @return
	 */
	public LocalIdDTO getLocalIdDTOForString(String selectedLocalId) {
		for (LocalIdDTO localId : getProvider().getLocalId()) {
			LocalIdBean localIdBean = new LocalIdBean(localId);
			if (selectedLocalId.equals(localIdBean.toString())) {
				return localId;
			}
		}
		return null;
	}

	/**
	 * gets corresponding LocalIdBean for given string
	 * @return
	 */
	public LocalIdBean getLocalIdBeanForString(String selectedLocalId) {
		for (LocalIdBean localId : this.localIds) {
			if (selectedLocalId.equals(localId.toString())) {
				return localId;
			}
		}
		return new LocalIdBean();
	}

	/**
	 * removes given localId from providerLocalIds[]
	 */
	public void removeLocalId(String value) {
		ArrayList<SelectItem> temp = new ArrayList<SelectItem>();
		for (int i = 0; i < providerLocalIds.length; i++) {
			if (!value.equals(providerLocalIds[i].getLabel())) {
				temp.add(providerLocalIds[i]);
			}
		}
		providerLocalIds = new SelectItem[temp.size()];
		int i = 0;
		for (SelectItem selectItem : temp) {
			providerLocalIds[i++] = selectItem;
		}
	}

	/**
	 * adds given localId to providerLocalIds[]
	 */
	public void addLocalId(String value) {
		SelectItem[] temp = new SelectItem[providerLocalIds.length + 1];
		for (int i = 0; i < providerLocalIds.length; i++) {
			if (providerLocalIds[i].getLabel().equals(value)) {
				return;
			}
			temp[i] = providerLocalIds[i];
		}
		temp[providerLocalIds.length] = new SelectItem(value);
		providerLocalIds = temp;
	}

	/**
	 * gets newAddress
	 * @return
	 */
	public AddressBean getNewAddress() {
		return newAddress;
	}

	/**
	 * sets newAddress
	 */
	public void setNewAddress(AddressBean newAddress) {
		this.newAddress = newAddress;
	}

	/**
	 * gets newLocalId
	 * @return
	 */
	public LocalIdBean getNewLocalId() {
		return newLocalId;
	}

	/**
	 * sets newLocalId
	 */
	public void setNewLocalId(LocalIdBean newLocalId) {
		this.newLocalId = newLocalId;
	}

	/**
	 * connects to server via WSDL
	 * @return
	 */
	private IPorsAdministratorService connectWS() {
		PORSAdministratorServiceConnector connector;
		if (wsdlLocation == null) {
			connector = new PORSAdministratorServiceConnector();
		} else {
			try {
				connector = new PORSAdministratorServiceConnector(wsdlLocation);
			} catch (MalformedURLException e) {
				logger.error("WSDL url is malformed!", e);
				return null;
			}
		}
		return connector.getPorsAdministratorServiceBeanPort();
	}

	/**
	 * @see DialogCore
	 */
	public DuplicateConfigurationDTO getDuplicateConfiguration()
	{
		try {
			
			if (duplicateConfiguration == null)
			{	
				IPorsAdministratorService port = connectWS();
				duplicateConfiguration = port.getDuplicateConfiguration(currentUser);
			}	
			
			return duplicateConfiguration;
				
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_getduplicateconfiguration_error", new Object[] { pex
									.getFaultInfo().getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_getduplicateconfiguration_error", new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
		return null;
	}

	/**
	 * @see DialogCore
	 */
	public String getLoggingDetail() {
		try {
			if (getHistory() == null) {
				logger.warn("History object was null!");
			}

			IPorsAdministratorService port = connectWS();

			historyDetail.setLoggingEntry(null);
			historyDetail.setAddressLog(null);
			historyDetail.setLocalidLog(null);
			historyDetail.setOrganisationLog(null);
			historyDetail.setProviderLog(null);
			historyDetail.setOrganisationHasProviderLog(null);
			historyDetail.setProviderHasAddressLog(null);
			historyDetail.setOrganisationHasAddressLog(null);

			historyDetail = port.getHistoryDetail(currentUser, getHistory());
			return "success";
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_getloggingdetail_error",
							new Object[] { pex.getFaultInfo()
									.getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_getloggingdetail_error",
							new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
		return null;
	}

	/**
	 * @see DialogCore
	 */
	public ArrayList<LogTableBean> getProviderHistoryData() {
		ArrayList<LogTableBean> providerHistoryData = new ArrayList<LogTableBean>();
		if (getHistoryDetail().getProviderLog() != null) {

			LogTableBean oid = new LogTableBean("Oid", getHistoryDetail()
					.getProviderLog().getOldOid(), getHistoryDetail()
					.getProviderLog().getNewOid());
			LogTableBean lanr = new LogTableBean("Lanr", getHistoryDetail()
					.getProviderLog().getOldLanr(), getHistoryDetail()
					.getProviderLog().getNewLanr());
			LogTableBean prename = new LogTableBean("Name Prefix",
					getHistoryDetail().getProviderLog().getOldNamePrefix(),
					getHistoryDetail().getProviderLog().getNewNamePrefix());
			LogTableBean fname = new LogTableBean("First Name",
					getHistoryDetail().getProviderLog().getOldFirstName(),
					getHistoryDetail().getProviderLog().getNewFirstName());
			LogTableBean mname = new LogTableBean("Middle Name",
					getHistoryDetail().getProviderLog().getOldMiddleName(),
					getHistoryDetail().getProviderLog().getNewMiddleName());
			LogTableBean lname = new LogTableBean("Last Name",
					getHistoryDetail().getProviderLog().getOldLastName(),
					getHistoryDetail().getProviderLog().getNewLastName());
			LogTableBean sufname = new LogTableBean("Name Suffix",
					getHistoryDetail().getProviderLog().getOldNameSuffix(),
					getHistoryDetail().getProviderLog().getNewNameSuffix());

			LogTableBean spec = new LogTableBean("Specialisation",
					getHistoryDetail().getProviderLog().getOldSpecialisation(),
					getHistoryDetail().getProviderLog().getNewSpecialisation());

			LogTableBean gender = new LogTableBean("Gender", getHistoryDetail()
					.getProviderLog().getOldGenderCode(), getHistoryDetail()
					.getProviderLog().getNewGenderCode());
			LogTableBean birth = new LogTableBean("Date of birth",
					getDateString(getHistoryDetail().getProviderLog()
							.getOldBirthday()),
					getDateString(getHistoryDetail().getProviderLog()
							.getNewBirthday()));
			LogTableBean email = new LogTableBean("Email", getHistoryDetail()
					.getProviderLog().getOldEmail(), getHistoryDetail()
					.getProviderLog().getNewEmail());
			LogTableBean tele = new LogTableBean("Telephone",
					getHistoryDetail().getProviderLog().getOldTelephone(),
					getHistoryDetail().getProviderLog().getNewTelephone());
			LogTableBean fax = new LogTableBean("Fax", getHistoryDetail()
					.getProviderLog().getOldFax(), getHistoryDetail()
					.getProviderLog().getNewFax());
			LogTableBean deactivate = new LogTableBean("Deactivation Date",
					getDateString(getHistoryDetail().getProviderLog()
							.getOldDeactivationDate()),
					getDateString(getHistoryDetail().getProviderLog()
							.getNewDeactivationDate()));
			LogTableBean deReason = new LogTableBean("Deactivation Reason",
					getHistoryDetail().getProviderLog()
							.getOldDeactivationReasonCode(), getHistoryDetail()
							.getProviderLog().getNewDeactivationReasonCode());
			LogTableBean reactivate = new LogTableBean("Reactivation Date",
					getDateString(getHistoryDetail().getProviderLog()
							.getOldReactivationDate()),
					getDateString(getHistoryDetail().getProviderLog()
							.getNewReactivationDate()));
			LogTableBean reReason = new LogTableBean("Reactivation Reason",
					getHistoryDetail().getProviderLog()
							.getOldReactivationReasonCode(), getHistoryDetail()
							.getProviderLog().getNewReactivationReasonCode());

			providerHistoryData.add(oid);
			providerHistoryData.add(lanr);
			providerHistoryData.add(prename);
			providerHistoryData.add(fname);
			providerHistoryData.add(mname);
			providerHistoryData.add(lname);
			providerHistoryData.add(sufname);
			providerHistoryData.add(spec);
			providerHistoryData.add(gender);
			providerHistoryData.add(birth);
			providerHistoryData.add(email);
			providerHistoryData.add(tele);
			providerHistoryData.add(fax);
			providerHistoryData.add(deactivate);
			providerHistoryData.add(deReason);
			providerHistoryData.add(reactivate);
			providerHistoryData.add(reReason);
		}

		return providerHistoryData;
	}

	/**
	 * @see DialogCore
	 */
	public ArrayList<LogTableBean> getOrganisationHistoryData() {
		ArrayList<LogTableBean> organisationHistoryData = new ArrayList<LogTableBean>();
		if (getHistoryDetail().getOrganisationLog() != null) {
			LogTableBean oid = new LogTableBean("Oid", getHistoryDetail()
					.getOrganisationLog().getOldOID(), getHistoryDetail()
					.getOrganisationLog().getOldOID());
			LogTableBean name = new LogTableBean("Name", getHistoryDetail()
					.getOrganisationLog().getOldName(), getHistoryDetail()
					.getOrganisationLog().getNewName());
			LogTableBean name2 = new LogTableBean("Second Name",
					getHistoryDetail().getOrganisationLog().getOldSecondname(),
					getHistoryDetail().getOrganisationLog().getNewSecondname());
			LogTableBean description = new LogTableBean(
					"Description",
					getHistoryDetail().getOrganisationLog().getOldDescription(),
					getHistoryDetail().getOrganisationLog().getNewDescription());
			LogTableBean email = new LogTableBean("Email", getHistoryDetail()
					.getOrganisationLog().getOldEmail(), getHistoryDetail()
					.getOrganisationLog().getNewEmail());
			LogTableBean tele = new LogTableBean("Telephone",
					getHistoryDetail().getOrganisationLog().getOldTelephone(),
					getHistoryDetail().getOrganisationLog().getNewTelephone());
			LogTableBean fax = new LogTableBean("Fax", getHistoryDetail()
					.getOrganisationLog().getOldFax(), getHistoryDetail()
					.getOrganisationLog().getNewFax());
			LogTableBean deactivate = new LogTableBean("Deactivation Date",
					getDateString(getHistoryDetail().getOrganisationLog()
							.getOldDeactivationDate()),
					getDateString(getHistoryDetail().getOrganisationLog()
							.getNewDeactivationDate()));
			LogTableBean deReason = new LogTableBean("Deactivation Reason",
					getHistoryDetail().getOrganisationLog()
							.getOldDeactivationReasonCode(), getHistoryDetail()
							.getOrganisationLog()
							.getNewDeactivationReasonCode());
			LogTableBean reactivate = new LogTableBean("Reactivation Date",
					getDateString(getHistoryDetail().getOrganisationLog()
							.getOldReactivationDate()),
					getDateString(getHistoryDetail().getOrganisationLog()
							.getNewReactivationDate()));
			LogTableBean reReason = new LogTableBean("Reactivation Reason",
					getHistoryDetail().getOrganisationLog()
							.getOldReactivationReasonCode(), getHistoryDetail()
							.getOrganisationLog()
							.getNewReactivationReasonCode());

			organisationHistoryData.add(oid);
			organisationHistoryData.add(name);
			organisationHistoryData.add(name2);
			organisationHistoryData.add(description);
			organisationHistoryData.add(email);
			organisationHistoryData.add(tele);
			organisationHistoryData.add(fax);
			organisationHistoryData.add(deactivate);
			organisationHistoryData.add(deReason);
			organisationHistoryData.add(reactivate);
			organisationHistoryData.add(reReason);

		}
		return organisationHistoryData;
	}

	/**
	 * forms dd.mm.yyyy out of a given XMLGregorianCalendar
	 * 
	 * @param cal
	 * @return
	 */
	private String getDateString(XMLGregorianCalendar cal) {
		if (cal != null) {
			return cal.getDay() + "." + cal.getMonth() + "." + cal.getYear();
		} else
			return "";
	}

	/**
	 * @see DialogCore
	 */
	public ArrayList<LogTableBean> getAddressHistoryData() {
		ArrayList<LogTableBean> addressHistoryData = new ArrayList<LogTableBean>();
		if (getHistoryDetail().getAddressLog() != null) {
			LogTableBean street = new LogTableBean("Street", getHistoryDetail()
					.getAddressLog().getOldStreet(), getHistoryDetail()
					.getAddressLog().getNewStreet());
			LogTableBean number = new LogTableBean("Housenumber",
					getHistoryDetail().getAddressLog().getOldHouseNumber(),
					getHistoryDetail().getAddressLog().getNewHouseNumber());
			LogTableBean zip = new LogTableBean("Zip code", getHistoryDetail()
					.getAddressLog().getOldZipCode(), getHistoryDetail()
					.getAddressLog().getNewZipCode());
			LogTableBean city = new LogTableBean("City", getHistoryDetail()
					.getAddressLog().getOldCity(), getHistoryDetail()
					.getAddressLog().getNewCity());
			LogTableBean state = new LogTableBean("State", getHistoryDetail()
					.getAddressLog().getOldState(), getHistoryDetail()
					.getAddressLog().getNewState());
			LogTableBean country = new LogTableBean("Country",
					getHistoryDetail().getAddressLog().getOldCountry(),
					getHistoryDetail().getAddressLog().getNewCountry());
			LogTableBean additional = new LogTableBean("Additional",
					getHistoryDetail().getAddressLog().getOldAdditional(),
					getHistoryDetail().getAddressLog().getNewAdditional());

			addressHistoryData.add(street);
			addressHistoryData.add(number);
			addressHistoryData.add(zip);
			addressHistoryData.add(city);
			addressHistoryData.add(state);
			addressHistoryData.add(country);
			addressHistoryData.add(additional);
		}
		return addressHistoryData;
	}

	/**
	 * gets list of history data of localIds
	 */
	public ArrayList<LogTableBean> getLocalIdHistoryData() {
		ArrayList<LogTableBean> localIdHistoryData = new ArrayList<LogTableBean>();
		if (getHistoryDetail().getLocalidLog() != null) {

			LogTableBean localid = new LogTableBean("LocalId",
					getHistoryDetail().getLocalidLog().getOldLocalId(),
					getHistoryDetail().getLocalidLog().getNewLocalId());
			LogTableBean application = new LogTableBean("Application",
					getHistoryDetail().getLocalidLog().getOldApplication(),
					getHistoryDetail().getLocalidLog().getNewApplication());
			LogTableBean facility = new LogTableBean("Facility",
					getHistoryDetail().getLocalidLog().getOldFacility(),
					getHistoryDetail().getLocalidLog().getNewFacility());

			localIdHistoryData.add(localid);
			localIdHistoryData.add(application);
			localIdHistoryData.add(facility);
		}
		return localIdHistoryData;
	}

	/**
	 * gets list of history data of providerHasAddress
	 */
	public ArrayList<LogTableBean> getProviderHasAddressHistoryData() {
		ArrayList<LogTableBean> providerHasAddressHistoryData = new ArrayList<LogTableBean>();
		if (getHistoryDetail().getProviderHasAddressLog() != null) {

			LogTableBean phaProvider = new LogTableBean("RegionalProviderID",
					getHistoryDetail().getProviderHasAddressLog()
							.getOldRegionalProviderId(), getHistoryDetail()
							.getProviderHasAddressLog()
							.getNewRegionalProviderId());
			LogTableBean phaAddress = new LogTableBean("AddressID",
					getHistoryDetail().getProviderHasAddressLog()
							.getOldAddressId(), getHistoryDetail()
							.getProviderHasAddressLog().getNewAddressId());

			providerHasAddressHistoryData.add(phaProvider);
			providerHasAddressHistoryData.add(phaAddress);
		}
		return providerHasAddressHistoryData;
	}

	/**
	 * gets list of history data of organisationHasAddress
	 */
	public ArrayList<LogTableBean> getOrganisationHasAddressHistoryData() {
		ArrayList<LogTableBean> organisationHasAddressHistoryData = new ArrayList<LogTableBean>();
		if (getHistoryDetail().getOrganisationHasAddressLog() != null) {

			LogTableBean phaOrganisation = new LogTableBean(
					"RegionalOrganisationID", getHistoryDetail()
							.getOrganisationHasAddressLog()
							.getOldRegionalOrganisationId(), getHistoryDetail()
							.getOrganisationHasAddressLog()
							.getNewRegionalOrganisationId());
			LogTableBean phaAddress = new LogTableBean("AddressID",
					getHistoryDetail().getOrganisationHasAddressLog()
							.getOldAddressId(), getHistoryDetail()
							.getOrganisationHasAddressLog().getNewAddressId());

			organisationHasAddressHistoryData.add(phaOrganisation);
			organisationHasAddressHistoryData.add(phaAddress);
		}
		return organisationHasAddressHistoryData;
	}

	/**
	 * gets list of history data of organisationHasProvider
	 */
	public ArrayList<LogTableBean> getOrganisationHasProviderHistoryData() {
		ArrayList<LogTableBean> organisationHasProviderHistoryData = new ArrayList<LogTableBean>();
		if (getHistoryDetail().getOrganisationHasProviderLog() != null) {

			LogTableBean phaOrganisation = new LogTableBean(
					"RegionalOrganisationID", getHistoryDetail()
							.getOrganisationHasProviderLog()
							.getOldRegionalOrganisationId(), getHistoryDetail()
							.getOrganisationHasProviderLog()
							.getNewRegionalOrganisationId());
			LogTableBean phaProvider = new LogTableBean("RegionalProviderID",
					getHistoryDetail().getOrganisationHasProviderLog()
							.getOldRegionalProviderId(), getHistoryDetail()
							.getOrganisationHasProviderLog()
							.getNewRegionalProviderId());

			organisationHasProviderHistoryData.add(phaOrganisation);
			organisationHasProviderHistoryData.add(phaProvider);
		}
		return organisationHasProviderHistoryData;
	}

	/**
	 * @see DialogCore
	 */
	public String addProvider() {
		try {
			IPorsAdministratorService port = connectWS();
			try {
				provider.setLastUpdateDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(new GregorianCalendar()));
				if (provider.getBirthday().compare(
						DatatypeFactory.newInstance().newXMLGregorianCalendar(
								new GregorianCalendar(1, 1, startYear))) == DatatypeConstants.EQUAL) {

					provider.setBirthday(null);
				}
			} catch (DatatypeConfigurationException dcex) {
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString(
								"message_addprovider_error",
								new Object[] { dcex.getMessage() }));
				logger.error("DatatypeConfigurationException caught", dcex);
				return null;
			}
			provider.setSessionId(sessionId);
			provider.setIp(sessionIp);
			provider.getAddresses().clear();
			provider.getAddresses().addAll(this.addresses);
			provider.getLocalId().clear();
			provider.getLocalId().addAll(this.localIds);
			provider.getOrganisations().clear();
			provider.getOrganisations().addAll(this.getSelectedOrganizations());
			for (OrganisationDTO orgs : provider.getOrganisations()) {
				logger.info("provider.getOrganisations(): " + orgs);
			}
			nullEmptyFieldsProvider();
			port.addProvider(provider, currentUser);
			provider = new ProviderDTO();
			this.addresses.clear();
			this.result.clear();
			this.localIds.clear();
			MessageHandler.getInstance().addInfoMessage(
					ResourceHandler.getMessageResourceString(
							"message_addprovider", null));
			return "success";
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_addprovider_error", new Object[] { pex
									.getFaultInfo().getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_addprovider_error",
							new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
		return null;
	}

	/**
	 * @see DialogCore
	 */
	public void addUser() {
		try {
			IPorsAdministratorService port = connectWS();
			logger.info("Username: " + newUser.getUsername());
			logger.info("Password: " + newUser.getPassword());
			newUser.setSessionId(sessionId);
			newUser.setIp(sessionIp);
			List<UserRoleDTO> roles = requestRoleList();
			for (UserRoleDTO userRoleDTO : roles) {
				if (userRoleDTO.getName().equals(selectedRole)) {
					newUser.setRole(userRoleDTO);
				}
			}
			port.addUser(newUser, currentUser);
			MessageHandler.getInstance().addInfoMessage(
					ResourceHandler.getMessageResourceString("message_adduser",
							null));
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_adduser_error", new Object[] { pex
									.getFaultInfo().getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_adduser_error",
							new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
	}
	/**
	 * returns the {@link UserRoleDTO} for a given name
	 * @param roleName
	 * @return
	 */
	private UserRoleDTO getRoleForName(String roleName){
		List<UserRoleDTO> roles = requestRoleList();
		for (UserRoleDTO userRoleDTO : roles) {
			if (userRoleDTO.getName().equals(roleName)) {
				return userRoleDTO;
			}
		}
		return new UserRoleDTO();
	}
	

	/**
	 * @see DialogCore
	 */
	public String logout() {
		currentUser = new UserDTO();
		presenter.setUserLoggedIn(false);
		return "success";
	}

	/**
	 * @see DialogCore
	 */
	public void loadDuplicateDetails(DuplicateEntryDTO deDTO) {
		IPorsAdministratorService port = connectWS();
		/** reload Duplicatedetails **/
		duplicateDetails = new DuplicateDetails();
		try {
			this.getDuplicateDetails().setDuplicateDetail(
					port.getDuplicateDetail(currentUser, deDTO));
		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_loadduplicatedetails_error", new Object[] { e.getFaultInfo().getErrorDescription() }));
			logger.error("PORSException caught ("
					+ e.getFaultInfo().getErrorDescription() + ")", e);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_loadduplicatedetails_error", new Object[] { sfex.getMessage() }));
			logger.error("SOAPFaultException caught", sfex);
		}
	}

	/**
	 * @see DialogCore
	 */
	public void showAllOrganizations() {
		try {
			organizationList = new ArrayList<OrganisationDTO>();
			organizationList = requestOrganisationList();
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_showorganisations_error",
							new Object[] { pex.getFaultInfo()
									.getErrorDescription() }));
			logger.error("PORSException caught ("
					+ pex.getFaultInfo().getErrorDescription() + ")", pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_showorganisations_error",
							new Object[] { sfex.getMessage() }));
			logger.error("SOAPFaultException caught", sfex);
		}
	}

	/**
	 * gets list of all organisations
	 * 
	 * @return
	 * @throws PORSException_Exception
	 * @throws SOAPFaultException
	 */
	private ArrayList<OrganisationDTO> requestOrganisationList()
			throws PORSException_Exception, SOAPFaultException {
		ArrayList<OrganisationDTO> orgList = new ArrayList<OrganisationDTO>();
		IPorsAdministratorService port = connectWS();
		for (OrganisationDTO org : port.getOrganisations(currentUser, 0, 0)) {
			orgList.add(org);
		}
		return orgList;
	}

	/**
	 * gets list of all user roles
	 * 
	 * @return
	 */
	private List<UserRoleDTO> requestRoleList() {
		try {
			List<UserRoleDTO> roleList = new ArrayList<UserRoleDTO>();
			IPorsAdministratorService port = connectWS();
			for (UserRoleDTO role : port.getRoles(currentUser)) {
				roleList.add(role);
			}
			return roleList;
		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_requestrolelist_error", new Object[] { e
									.getFaultInfo().getErrorDescription() }));
			logger.error("Loading roles failed ("
					+ e.getFaultInfo().getErrorDescription() + ")", e);
		} catch (SOAPFaultException e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_requestrolelist_error", new Object[] { e
									.getMessage() }));
			logger.error("Loading roles failed (" + e.getMessage() + ")", e);
		}

		return null;
	}

	/**
	 * gets organizationListSelected
	 */
	public ArrayList<OrganisationDTO> getOrganizationListSelected() {
		return organizationListSelected;
	}

	/**
	 * sets organizationListSelected
	 */
	public void setOrganizationListSelected(
			ArrayList<OrganisationDTO> organizationListSelected) {
		this.organizationListSelected = organizationListSelected;
	}

	/**
	 * gets org
	 * 
	 * @return
	 */
	public OrganisationDTO getOrg() {
		return this.org;
	}

	/**
	 * sets org
	 */
	public void setOrg(OrganisationDTO org) {
		this.org = org;
		this.setAddresses(org.getAddresses());
		this.setLocalIds(org.getLocalId());
	}

	/**
	 * gets provider
	 * 
	 * @return
	 */
	public ProviderDTO getProvider() {
		return this.provider;
	}

	/**
	 * sets provider
	 */
	public void setProvider(ProviderDTO newProvider) {
		this.provider = newProvider;
		this.setAddresses(newProvider.getAddresses());
		this.setLocalIds(newProvider.getLocalId());
	}

	/**
	 * gets history
	 * 
	 * @return
	 */
	public LoggingEntryDTO getHistory() {
		return history;
	}

	/**
	 * sets history
	 */
	public void setHistory(LoggingEntryDTO history) {
		this.history = history;
	}

	/**
	 * gets historyDetail
	 * 
	 * @return
	 */
	public LoggingDetailDTO getHistoryDetail() {
		return historyDetail;
	}

	/**
	 * sets historyDetail
	 */
	public void setHistoryDetail(LoggingDetailDTO historyDetail) {
		this.historyDetail = historyDetail;
	}

	/**
	 * gets sorted countryCodes
	 * 
	 * @return
	 */
	public SelectItem[] getCountries() {
		Arrays.sort(countryCodes, new SelectItemComparator());
		return countryCodes;
	}

	/**
	 * sets providerLocalIds
	 * 
	 * @return
	 */
	public void setLocalIds(SelectItem[] localIds) {
		this.providerLocalIds = localIds;
	}

	/**
	 * gets siLocalIds
	 * 
	 * @return
	 */
	public List<SelectItem> getSiLocalIds() {
		List<SelectItem> siLocalIds = new ArrayList<SelectItem>();
		for (LocalIdBean localId : this.localIds) {
			siLocalIds.add(new SelectItem(localId.toString(), localId
					.toString(), localId.toString()));
		}
		Collections.sort(siLocalIds, new SelectItemComparator());
		return siLocalIds;
	}

	/**
	 * gets siAddresses
	 * 
	 * @return
	 */
	public List<SelectItem> getSiAddresses() {
		List<SelectItem> siAddresses = new ArrayList<SelectItem>();
		for (AddressBean add : this.addresses) {
			siAddresses.add(new SelectItem(add.toString(), add.toString(), add
					.toString()));
		}
		Collections.sort(siAddresses, new SelectItemComparator());
		return siAddresses;
	}

	/**
	 * gets siOrgs
	 * 
	 * @return
	 */
	public List<SelectItem> getSiOrgs() {
		List<SelectItem> siOrgs = new ArrayList<SelectItem>();
		for (OrganisationDTO org : organizationList) {
			siOrgs.add(new SelectItem(org.getName(), org.getName(), org
					.getName()));
		}
		Collections.sort(siOrgs, new SelectItemComparator());
		return siOrgs;
	}

	/**
	 * gets years
	 * 
	 * @return
	 */
	public SelectItem[] getYears() {
		return years;
	}

	/**
	 * sets years
	 */
	public void setYears(SelectItem[] years) {
		this.years = years;
	}

	/**
	 * gets months
	 * 
	 * @return
	 */
	public SelectItem[] getMonths() {
		return months;
	}

	/**
	 * sets months
	 */
	public void setMonths(SelectItem[] months) {
		this.months = months;
	}

	/**
	 * gets days
	 * 
	 * @return
	 */
	public SelectItem[] getDays() {
		return days;
	}

	/**
	 * sets days
	 */
	public void setDays(SelectItem[] days) {
		this.days = days;
	}

	/**
	 * gets selected organisations
	 * 
	 * @return
	 */
	private ArrayList<OrganisationDTO> getSelectedOrganizations() {
		logger.info("getSelectedOrganizations: " + this.getResult().size());
		for (String itemLabel : this.getResult()) {
			logger.info("name: " + itemLabel);
		}
		ArrayList<OrganisationDTO> returnList = new ArrayList<OrganisationDTO>();
		showAllOrganizations();
		for (String itemLabel : this.getResult()) {
			for (OrganisationDTO orgItem : organizationList) {
				if (itemLabel.equals(orgItem.getName())) {
					returnList.add(orgItem);
					logger.info("Org: " + orgItem.getName());
				}
			}
		}
		return returnList;
	}

	/**
	 * gets currentUser
	 * 
	 * @return
	 */
	public UserDTO getCurrentUser() {
		return currentUser;
	}

	/**
	 * sets currentUser
	 */
	public void setCurrentUser(UserDTO currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @see DialogCore
	 */
	public void addOrganization() {
		try {
			IPorsAdministratorService port = connectWS();
			org.setSessionId(sessionId);
			org.setIp(sessionIp);
			org.getAddresses().clear();
			org.getAddresses().addAll(this.addresses);
			org.getLocalId().clear();
			org.getLocalId().addAll(this.localIds);

			nullEmptyFields();

			port.addOrganisation(currentUser, org);
			org = new OrganisationDTO();
			this.addresses.clear();
			this.localIds.clear();
			MessageHandler.getInstance().addInfoMessage(
					ResourceHandler.getMessageResourceString(
							"message_addorganisation", null));
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_addorganisation_error", new Object[] { pex
									.getFaultInfo().getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_addorganisation_error",
							new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
	}

	/**
	 * sets fields of organisations null if they are empty strings
	 */
	private void nullEmptyFields() {
		if (org.getName() != null && org.getName().length() == 0) {
			org.setName(null);
		}
		if (org.getSecondName() != null && org.getSecondName().length() == 0) {
			org.setSecondName(null);
		}
		if (org.getOID() != null && org.getOID().length() == 0) {
			org.setOID(null);
		}
		if (org.getDescription() != null && org.getDescription().length() == 0) {
			org.setDescription(null);
		}
		if (org.getEmail() != null && org.getEmail().length() == 0) {
			org.setEmail(null);
		}
		if (org.getTelephone() != null && org.getTelephone().length() == 0) {
			org.setTelephone(null);
		}
		if (org.getFax() != null && org.getFax().length() == 0) {
			org.setFax(null);
		}
	}

	/**
	 * sets fields of provider null if they are empty strings
	 */
	private void nullEmptyFieldsProvider() {
		if (provider.getNamePrefix() != null
				&& provider.getNamePrefix().length() == 0) {
			provider.setNamePrefix(null);
		}
		if (provider.getFirstname() != null
				&& provider.getFirstname().length() == 0) {
			provider.setFirstname(null);
		}
		if (provider.getMiddleName() != null
				&& provider.getMiddleName().length() == 0) {
			provider.setMiddleName(null);
		}
		if (provider.getLastname() != null
				&& provider.getLastname().length() == 0) {
			provider.setLastname(null);
		}
		if (provider.getNameSuffix() != null
				&& provider.getNameSuffix().length() == 0) {
			provider.setNameSuffix(null);
		}
		if (provider.getSpecialisation() != null
				&& provider.getSpecialisation().length() == 0) {
			provider.setSpecialisation(null);
		}
		if (provider.getTelephone() != null
				&& provider.getTelephone().length() == 0) {
			provider.setTelephone(null);
		}
		if (provider.getFax() != null && provider.getFax().length() == 0) {
			provider.setFax(null);
		}
		if (provider.getEmail() != null && provider.getEmail().length() == 0) {
			provider.setEmail(null);
		}
		if (provider.getGenderCode() != null
				&& provider.getGenderCode().length() == 0) {
			provider.setGenderCode(null);
		}
		if (provider.getOid() != null && provider.getOid().length() == 0) {
			provider.setOid(null);
		}
		if (provider.getLanr() != null && provider.getLanr().length() == 0) {
			provider.setLanr(null);
		}
	}

	/**
	 * @see DialogCore
	 */
	public void updateOrg() {
		presenter.setEditOrganizationDisabled(true);
		try {
			IPorsAdministratorService port = connectWS();
			org.setSessionId(sessionId);
			org.setIp(sessionIp);
			org.getAddresses().clear();
			org.getAddresses().addAll(this.addresses);
			org.getLocalId().clear();
			org.getLocalId().addAll(this.localIds);

			nullEmptyFields();

			port.updateOrganisation(currentUser, org);
			this.addresses.clear();
			this.localIds.clear();
			org = port.getOrganisationDetails(currentUser, org);
			MessageHandler.getInstance().addInfoMessage(
					ResourceHandler.getMessageResourceString(
							"message_updateorganisation", null));
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_updateorganisation_error", new Object[] { pex.getFaultInfo().getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_updateorganisation_error", new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
	}

	/**
	 * @see DialogCore
	 */
	public void updateProvider() {
		presenter.setEditProviderDisabled(true);
		try {
			IPorsAdministratorService port = connectWS();
			provider.setSessionId(sessionId);
			provider.setIp(sessionIp);
			provider.getAddresses().clear();
			provider.getAddresses().addAll(this.addresses);
			provider.getLocalId().clear();
			provider.getLocalId().addAll(this.localIds);
			provider.getOrganisations().clear();
			provider.getOrganisations().addAll(this.getSelectedOrganizations());

			nullEmptyFieldsProvider();

			port.updateProvider(currentUser, provider);
			this.addresses.clear();
			this.result.clear();
			this.localIds.clear();
			showSingleProvider(provider);
			MessageHandler.getInstance().addInfoMessage(
					ResourceHandler.getMessageResourceString(
							"message_updateprovider", null));
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_updateprovider_error", new Object[]{ pex.getFaultInfo().getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_updateprovider_error", new Object[]{ sfex.getMessage() }));
			logger.error(sfex);
		}
	}

	/**
	 * gets birthday of current provider as dd.mm.yyyy or '' if birthday is null
	 * 
	 * @return
	 */
	public String getBirthdayString() {
		if (this.getProvider().getBirthday() == null) {
			return "";
		} else {
			XMLGregorianCalendar cal = this.getProvider().getBirthday();
			return cal.getDay() + "." + cal.getMonth() + "." + cal.getYear();
		}
	}

	/**
	 * loads provider details from database
	 * 
	 * @param provider2
	 */
	public void showSingleProvider(ProviderDTO provider2) {
		try {
			IPorsAdministratorService port = connectWS();
			ProviderDTO detailProvider = port.getProviderDetails(currentUser,
					provider2);
			setProvider(detailProvider);
			setAddresses(detailProvider.getAddresses());
			setLocalIds(detailProvider.getLocalId());
			setProviderOrganisations(detailProvider.getOrganisations());
			// initialize organisation list
			showAllOrganizations();
			ArrayList<String> organisations = new ArrayList<String>();
			for (OrganisationDTO orgs : detailProvider.getOrganisations()) {
				organisations.add(orgs.getName());
				this.organizationList.remove(orgs);
			}
			Collections.sort(organisations);
			setResult(organisations);
			presenter.setEditProviderDisabled(true);
		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_showsingleprovider_error", new Object[] { e.getFaultInfo().getErrorDescription() }));
		}
	}

	/**
	 * solve duplicates
	 */
	public void solveDuplicate() {
		IPorsAdministratorService port = connectWS();

		int duplicateStrategy = new Integer(this.getDuplicateDetails()
				.getDuplicateStrategy()).intValue();

		DuplicateProcessingInfoDTO dpiDto = new DuplicateProcessingInfoDTO();
		dpiDto.setDuplicateStrategy(duplicateStrategy);

		if (this.getDuplicateDetails().getMergedObject() instanceof ProviderDTO) {
			ProviderDTO mergedProv = (ProviderDTO) this.getDuplicateDetails()
					.getMergedObject();
			logger.info("Merged provider:");
			logger.info(mergedProv.getFirstname());
			logger.info(mergedProv.getLastname());
			logger.info(mergedProv.getGenderCode());
			logger.info(mergedProv.getAddresses().size());
			logger.info(mergedProv.getLocalId().size());
			dpiDto.setMergedProvider(mergedProv);
		} else if (this.getDuplicateDetails().getMergedObject() instanceof OrganisationDTO) {
			dpiDto.setMergedOrganisation((OrganisationDTO) this
					.getDuplicateDetails().getMergedObject());
		} else if (this.getDuplicateDetails().getMergedObject() instanceof AddressDTO) {
			dpiDto.setMergedAddress((AddressDTO) this.getDuplicateDetails()
					.getMergedObject());
		} else {
			logger.info("DialogCore : DuplicateDetailsDTO no merged object set");
		}
		dpiDto.setSessionId(this.sessionId);
		dpiDto.setIp(this.sessionIp);
		try {
			port.clearDuplicateEntry(currentUser, this.getDuplicateDetails()
					.getDuplicateDetail(), dpiDto);
			this.duplicateDetails = null;
		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_solveduplicate_error", new Object[] {e.getFaultInfo().getErrorDescription() }));
			logger.error("Solve Duplicate failed", e);
		}
	}

	/**
	 * @see DialogCore
	 */
	public void exportProviders() {
		if (getSelectedProviderFields() == null
				|| getSelectedProviderFields().size() == 0) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_checkfields", null));
			return;
		}

		IPorsAdministratorService port = connectWS();
		try {
			logger.info("number of selected provider fields: "
					+ getSelectedProviderFields().size());
			for (String field : getSelectedProviderFields()) {
				logger.info("Found field: " + field);
			}
			PorsCsv exportResult = port.exportProviders(currentUser,
					getSelectedProviderFields());

			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/csv");
			response.addHeader(
					"Content-Disposition",
					"attachment; filename=\"PORSProviders_"
							+ new SimpleDateFormat("yyyyMMdd_HHmm")
									.format(new Date()) + ".csv\"");

			if (exportResult == null) {
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString(
								"message_noexportresult", null));
				logger.error("Export result was null!");
				return;
			}
			try {
				ServletOutputStream out = response.getOutputStream();
				BufferedOutputStream bufOut = new BufferedOutputStream(out);
				response.setContentLength(exportResult.getContent().length());

				ByteArrayInputStream in = new ByteArrayInputStream(exportResult
						.getContent().getBytes("UTF-8"));

				byte[] buf = new byte[2048];
				int length = -1;
				while ((in != null) && ((length = in.read(buf)) != -1)) {
					bufOut.write(buf, 0, (int) length);
				}
				in.close();
				bufOut.close();
				FacesContext.getCurrentInstance().responseComplete();
			} catch (IOException e) {
				logger.error(e);
			}

		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_exportproviders_error", new Object[] { e
									.getFaultInfo().getErrorDescription() }));
			logger.error("Export Providers failed", e);
		}
	}

	/**
	 * @see DialogCore
	 */
	public void exportOrganisations() {
		if (getSelectedOrganisationFields() == null
				|| getSelectedOrganisationFields().size() == 0) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_checkfields", null));
			return;
		}

		IPorsAdministratorService port = connectWS();
		try {
			PorsCsv exportResult = port.exportOrganisations(currentUser,
					getSelectedOrganisationFields());

			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/csv");
			response.addHeader(
					"Content-Disposition",
					"attachment; filename=\"PORSOrganisations_"
							+ new SimpleDateFormat("yyyyMMdd_HHmm")
									.format(new Date()) + ".csv\"");

			if (exportResult == null) {
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString(
								"message_noexportresult", null));
				logger.error("Export result was null!");
				return;
			}
			try {
				ServletOutputStream out = response.getOutputStream();
				BufferedOutputStream bufOut = new BufferedOutputStream(out);
				response.setContentLength(exportResult.getContent().length());

				ByteArrayInputStream in = new ByteArrayInputStream(exportResult
						.getContent().getBytes("UTF-8"));

				byte[] buf = new byte[2048];
				int length = -1;
				while ((in != null) && ((length = in.read(buf)) != -1)) {
					bufOut.write(buf, 0, (int) length);
				}
				in.close();
				bufOut.close();
				FacesContext.getCurrentInstance().responseComplete();
			} catch (IOException e) {
				logger.error(e);
			}

		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_exportorganisations_error",
							new Object[] { e.getFaultInfo()
									.getErrorDescription() }));
			logger.error("Export Organisations failed", e);
		}
	}

	/**
	 * initialises provider import
	 */
	public void initProviderImport() {
		this.importProviderBean = new ImportBean(Fields.getInstance()
				.getProviderFieldsSelectItems(true));
	}

	/**
	 * checks import file for organisations
	 */
	public String checkImportOrganisationFile() {
		if (importOrganisationBean.getUploadedFile() == null) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_checkimportfile", null));
			return null;
		}
		return "success";
	}

	/**
	 * checks import file for provider
	 */
	public String checkImportProviderFile() {
		if (importProviderBean.getUploadedFile() == null) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_checkimportfile", null));
			return null;
		}
		return "success";
	}

	/**
	 * imports provider from csv file
	 */
	public String importProviders() {
		try {
			IPorsAdministratorService port = connectWS();

			FileInputStream fis = new FileInputStream(
					this.importProviderBean.getUploadedFile());
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			Reader in = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
			PorsCsv porsCsv = new PorsCsv();
			porsCsv.setContent(buffer.toString());

			ArrayList<String> fieldList = new ArrayList<String>();

			boolean mappingSet = false;
			for (ImportWrapper wrapper : this.importProviderBean.getFields()) {
				if (!wrapper.getSelectedField().getValue().toString()
						.equals(Fields.NO_SELECTION)) {
					mappingSet = true;
				}
				fieldList.add(wrapper.getSelectedField().getValue().toString());
			}

			if (!mappingSet
					|| !Fields.getInstance().requiredFieldsMappedProvider(
							fieldList)) {
				MessageHandler
						.getInstance()
						.addErrorMessage(
								ResourceHandler.getMessageResourceString("message_importproviders_error_mapping", null));
				return null;
			}

			currentUser.setIp(sessionIp);

			importProviderBean.setResult(port.importProviders(currentUser,
					porsCsv, fieldList));
			return "success";
		} catch (PORSException_Exception pex) {
			logger.error("PORSException caught ("
					+ pex.getFaultInfo().getErrorDescription() + ")", pex);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error", new Object[] { pex
									.getFaultInfo().getErrorDescription() }));
		} catch (SOAPFaultException sfex) {
			logger.error("SOAPFaultException caught", sfex);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error", new Object[] { sfex.getMessage() }));
		} catch (FileNotFoundException e) {
			logger.error("Import File not found", e);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error_filenotfound", null));
		} catch (IOException e) {
			logger.error("IOException caught", e);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error_loading", new Object[] { e.getMessage() }));
		}
		return null;
	}

	/**
	 * imports organisation from csv file
	 */
	public String importOrganisations() {
		try {
			IPorsAdministratorService port = connectWS();

			FileInputStream fis = new FileInputStream(
					this.importOrganisationBean.getUploadedFile());
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			Reader in = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
			PorsCsv porsCsv = new PorsCsv();
			porsCsv.setContent(buffer.toString());

			ArrayList<String> fieldList = new ArrayList<String>();

			boolean mappingSet = false;
			for (ImportWrapper wrapper : this.importOrganisationBean
					.getFields()) {
				if (!wrapper.getSelectedField().getValue().toString()
						.equals(Fields.NO_SELECTION)) {
					mappingSet = true;
				}
				fieldList.add(wrapper.getSelectedField().getValue().toString());
			}

			if (!mappingSet
					|| !Fields.getInstance().requiredFieldsMappedOrganisation(
							fieldList)) {
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString(
								"message_importorganisations_error_mapping",
								null));
				return null;
			}

			currentUser.setIp(sessionIp);
			importProviderBean.setResult(port.importOrganisations(currentUser,
					porsCsv, fieldList));
			return "success";
		} catch (PORSException_Exception pex) {
			logger.error("PORSException caught ("
					+ pex.getFaultInfo().getErrorDescription() + ")", pex);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error", new Object[] { pex
									.getFaultInfo().getErrorDescription() }));
		} catch (SOAPFaultException sfex) {
			logger.error("SOAPFaultException caught", sfex);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error", new Object[] { sfex.getMessage() }));
		} catch (FileNotFoundException e) {
			logger.error("Import File not found", e);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error_filenotfound", null));
		} catch (IOException e) {
			logger.error("IOException caught", e);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_import_error_loading", new Object[] { e.getMessage() }));
		}
		return null;
	}

	/**
	 * gets availableProviderFields
	 * 
	 * @return
	 */
	public List<String> getAvailableProviderFields() {
		return availableProviderFields;
	}

	/**
	 * gets selectedProviderFields
	 * 
	 * @return
	 */
	public List<String> getSelectedProviderFields() {
		if (selectedProviderFields == null) {
			selectedProviderFields = new ArrayList<String>();
		}
		return selectedProviderFields;
	}

	/**
	 * sets initial importOrganisationBean
	 */
	public void initOrganisationImport() {
		this.importOrganisationBean = new ImportBean(Fields.getInstance()
				.getOrganisationFieldsSelectItems(true));
	}

	/**
	 * gets availableOrganisationFields
	 * 
	 * @return
	 */
	public List<String> getAvailableOrganisationFields() {
		return availableOrganisationFields;
	}

	/**
	 * gets selectedOrganisationFields
	 * 
	 * @return
	 */
	public List<String> getSelectedOrganisationFields() {
		if (selectedOrganisationFields == null) {
			selectedOrganisationFields = new ArrayList<String>();
		}
		return selectedOrganisationFields;
	}

	/**
	 * gets importProviderBean
	 * 
	 * @return
	 */
	public ImportBean getImportProviderBean() {
		return importProviderBean;
	}

	/**
	 * reads and processes the header of the import csv file
	 * 
	 * @param file
	 * @param uploadType
	 */
	public void processHeader(File file, String uploadType) {
		try {
			CsvListReader reader = new CsvListReader(new FileReader(file),
					CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			String[] header = reader.getCSVHeader(true);

			if (uploadType.equals("PROVIDER")) {
				importProviderBean.initializeFields(Arrays.asList(header));
			} else if (uploadType.equals("ORGANISATION")) {
				importOrganisationBean.initializeFields(Arrays.asList(header));
			}
		} catch (IOException e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_processheader_error", new Object[] { e.getMessage() }));
			logger.error(e);
		}

	}

	/**
	 * gets importOrganisationBean
	 * 
	 * @return
	 */
	public ImportBean getImportOrganisationBean() {
		return importOrganisationBean;
	}

	/**
	 * gets list of search fields of provider
	 * 
	 * @return
	 */
	public List<String> getProviderSearchFields() {
		return Fields.getInstance().getProviderSearchFields(true);
	}

	/**
	 * gets list of search fields of organisation
	 * 
	 * @return
	 */
	public List<String> getOrganisationSearchFields() {
		return Fields.getInstance().getOrganisationSearchFields(true);
	}

	/**
	 * gets duplicateDetails
	 * 
	 * @return
	 */
	public DuplicateDetails getDuplicateDetails() {
		if (duplicateDetails == null) {
			duplicateDetails = new DuplicateDetails();
		}
		return duplicateDetails;
	}

	/**
	 * gets searchBean
	 * 
	 * @return
	 */
	public SearchBean getSearchBean() {
		if (searchBean == null) {
			searchBean = new SearchBean();
		}
		return searchBean;
	}

	/**
	 * searchs corresponding provider from database and pushes the results into
	 * providerDataModel
	 */
	public void startProviderSearch() {
		List<SearchCriteriaDTO> criteria = collectSearchCriteria();
		if (criteria.size() == 0) {
			MessageHandler
			.getInstance()
			.addErrorMessage(
					ResourceHandler.getMessageResourceString("message_search_error_fields", null));
		} else {
			IPorsAdministratorService port = connectWS();
			try {
				List<ProviderDTO> result = port.searchProviders(currentUser,
						criteria, searchBean.getSelectedOperator());
				providerDataModel.getDataProvider().setSearchMode(true);
				providerDataModel.getDataProvider().setSearchResult(result);
			} catch (PORSException_Exception e) {
				logger.error(e);
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString("message_search_error_failed", new Object[] {e.getFaultInfo().getErrorDescription() }));
			} catch (SOAPFaultException e) {
				logger.error(e);
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString("message_search_error_failed", new Object[] {e.getMessage() }));
			}
		}
	}

	/**
	 * searchs corresponding organisation from database and pushes the results
	 * into organizationList
	 */
	public void startOrganisationSearch() {
		List<SearchCriteriaDTO> criteria = collectSearchCriteria();
		if (criteria.size() == 0) {
			MessageHandler
			.getInstance()
			.addErrorMessage(
					ResourceHandler.getMessageResourceString("message_search_error_fields", null));
		} else {
			IPorsAdministratorService port = connectWS();
			try {
				if (organizationList != null) {
					organizationList.clear();
				} else {
					organizationList = new ArrayList<OrganisationDTO>();
				}
				for (OrganisationDTO org : port
						.searchOrganisations(currentUser, criteria,
								searchBean.getSelectedOperator())) {
					organizationList.add(org);
				}
				organisationDataModel.getDataProvider().setSearchMode(true);
				organisationDataModel.getDataProvider().setSearchResult(
						organizationList);
			} catch (PORSException_Exception e) {
				logger.error(e);
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString("message_search_error_failed", new Object[] {e.getFaultInfo().getErrorDescription() }));
			} catch (SOAPFaultException e) {
				logger.error(e);
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString("message_search_error_failed", new Object[] {e.getMessage() }));
			}
		}
	}

	/**
	 * searchs corresponding history from database for ip or sessionId
	 */
	public void startHistorySearch(String ip, String sessionId) {
		if ((ip != null && ip.length() >= 0)
				|| (sessionId != null && sessionId.length() >= 0)) {
			getSearchBean().reset();
			if (ip != null && ip.length() > 0) {
				getSearchBean().setField1("ipAddress");
				getSearchBean().setValue1(ip);
			} else if (sessionId != null && sessionId.length() > 0) {
				getSearchBean().setField1("sessionId");
				getSearchBean().setValue1(sessionId);
			}
			getSearchBean().setField2(Fields.NO_SELECTION);
			getSearchBean().setField3(Fields.NO_SELECTION);
			getSearchBean().setSelectedOperator("AND");
		}

		List<SearchCriteriaDTO> criteria = collectSearchCriteria();

		if (criteria.size() == 0) {
			MessageHandler
					.getInstance()
					.addErrorMessage(
							ResourceHandler.getMessageResourceString("message_search_error_fields", null));
		} else {
			IPorsAdministratorService port = connectWS();
			try {
				List<LoggingEntryDTO> result = port.searchHistory(currentUser,
						criteria, searchBean.getSelectedOperator());
				historyDataModel.getDataProvider().setSearchMode(true);
				historyDataModel.getDataProvider().setSearchResult(result);
			} catch (PORSException_Exception e) {
				logger.error(e);
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString("message_search_error_failed", new Object[] {e.getFaultInfo().getErrorDescription() }));
			} catch (SOAPFaultException e) {
				logger.error(e);
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString("message_search_error_failed", new Object[] {e.getMessage() }));
			}
		}
	}

	/**
	 * gets import status of provider
	 * 
	 * @return
	 */
	public ImportResultDTO getProviderImportStatus() {
		return getImportStatus("provider");
	}

	/**
	 * gets import status of organisation
	 * 
	 * @return
	 */
	public ImportResultDTO getOrganisationImportStatus() {
		return getImportStatus("organisation");
	}

	/**
	 * gets import status for given domain
	 * 
	 * @param domain
	 * @return
	 */
	private ImportResultDTO getImportStatus(String domain) {
		try {
			logger.info("import status refresh");
			IPorsAdministratorService port = connectWS();
			ImportResultDTO result = null;
			if (domain.equals("provider")) {
				result = port.getImportResult(currentUser,
						importProviderBean.getResult());
				importProviderBean.setResult(result);
			} else if (domain.equals("organisation")) {
				result = port.getImportResult(currentUser,
						importOrganisationBean.getResult());
				importOrganisationBean.setResult(result);
			}

			logger.info("processed: " + result.getProcessed());
			if (result.getProcessed() == result.getAddEntries()
					+ result.getUpdateEntries()) {
				Object[] params = new Object[] { result.getProcessed() };
				MessageHandler.getInstance().addInfoMessage(
						ResourceHandler.getMessageResourceString(
								"message_importstatus", params));
			} else if (result.getErrorMessage() != null
					&& result.getErrorMessage().length() > 0) {
				MessageHandler.getInstance().addErrorMessage(
						ResourceHandler.getMessageResourceString(
								"message_importstatus_error",
								new Object[] { result.getErrorMessage() }));
			}
			return result;
		} catch (PORSException_Exception e) {
			logger.error(e);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_importstatus_error",
							new Object[] { e.getFaultInfo().getErrorDescription() }));
		} catch (SOAPFaultException e) {
			logger.error(e);
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString(
							"message_importstatus_error",
							new Object[] { e.getMessage() }));
		}
		return importProviderBean.getResult();
	}

	/**
	 * reads the search criteria and fills a SearchCriteriaDTO
	 * 
	 * @return
	 */
	private List<SearchCriteriaDTO> collectSearchCriteria() {
		List<SearchCriteriaDTO> criteria = new ArrayList<SearchCriteriaDTO>();
		String[] fields = { searchBean.getField1(), searchBean.getField2(),
				searchBean.getField3() };
		String[] values = { searchBean.getValue1(), searchBean.getValue2(),
				searchBean.getValue3() };

		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].equals(Fields.NO_SELECTION) && values[i] != null) {
				String value = (values[i]).trim();
				if (value.length() > 0) {
					SearchCriteriaDTO c = new SearchCriteriaDTO();
					c.setField(fields[i]);
					c.setValue(value);
					criteria.add(c);
				}
			}
		}

		return criteria;
	}

	/**
	 * @see DialogCore
	 */
	public void updateUser(UserDTO user) {
		try {
			newUser.setRole(getRoleForName(selectedRole));
			if(!getNewPassword().equals("")){
				newUser.setPassword(getNewPassword());
			}
			IPorsAdministratorService port = connectWS();
			port.updateUser(currentUser, newUser);
			MessageHandler.getInstance().addInfoMessage(ResourceHandler.getMessageResourceString("message_updateuser", null));
		} catch (PORSException_Exception pex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_updateuser_error", new Object[] { pex.getFaultInfo().getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_updateuser_error", new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
	}

	/**
	 * @see DialogCore
	 */
	public void updateDuplicateConfiguration() {
		try {
			IPorsAdministratorService port = connectWS();
			if (duplicateConfiguration.getLowerthreshold() >= duplicateConfiguration
					.getUpperthreshold()) {
				MessageHandler
						.getInstance()
						.addErrorMessage(
								ResourceHandler.getMessageResourceString("message_updateduplicateconfiguration_error_threshold", null));
			} else {
				port.updateDuplicateConfiguration(duplicateConfiguration,
						currentUser);
				MessageHandler.getInstance().addInfoMessage(
						ResourceHandler.getMessageResourceString(
								"message_updateduplicateconfiguration", null));
			}
		} catch (PORSException_Exception pex) {
			MessageHandler
					.getInstance()
					.addErrorMessage(
							ResourceHandler
									.getMessageResourceString(
											"message_updateduplicateconfiguration_error_failed",
											new Object[] { pex.getFaultInfo()
													.getErrorDescription() }));
			logger.error(pex);
		} catch (SOAPFaultException sfex) {
			MessageHandler
			.getInstance()
			.addErrorMessage(
					ResourceHandler
							.getMessageResourceString(
									"message_updateduplicateconfiguration_error_failed",
									new Object[] { sfex.getMessage() }));
			logger.error(sfex);
		}
	}

	/**
	 * sets newUser
	 */
	public void setNewUser(UserDTO newUser) {
		this.newUser = newUser;
	}

	/**
	 * gets newUser
	 * 
	 * @return
	 */
	public UserDTO getNewUser() {
		return newUser;
	}

	/**
	 * gets providerDataModel
	 * 
	 * @return
	 */
	public ProviderDataModel getProviderDataModel() {
		if (providerDataModel == null) {
			providerDataModel = new ProviderDataModel(connectWS(), currentUser);
		}
		return providerDataModel;
	}

	/**
	 * sets providerDataModel
	 */
	public void setProviderDataModel(ProviderDataModel dataModel) {
		this.providerDataModel = dataModel;
	}

	/**
	 * gets historyDataModel
	 * 
	 * @return
	 */
	public HistoryDataModel getHistoryDataModel() {
		if (historyDataModel == null) {
			historyDataModel = new HistoryDataModel(connectWS(), currentUser);
		}
		return historyDataModel;
	}

	/**
	 * sets historyDataModel
	 */
	public void setHistoryDataModel(HistoryDataModel dataModel) {
		this.historyDataModel = dataModel;
	}

	/**
	 * gets organisationDataModel
	 * 
	 * @return
	 */
	public OrganisationDataModel getOrganisationDataModel() {
		if (organisationDataModel == null) {
			organisationDataModel = new OrganisationDataModel(connectWS(),
					currentUser);
		}
		return organisationDataModel;
	}

	/**
	 * sets organisationDataModel
	 */
	public void setOrganisationDataModel(OrganisationDataModel dataModel) {
		this.organisationDataModel = dataModel;
	}

	/**
	 * gets duplicateDataModel
	 * 
	 * @return
	 */
	public DuplicateDataModel getDuplicateDataModel() {
		if (duplicateDataModel == null) {
			duplicateDataModel = new DuplicateDataModel(connectWS(),
					currentUser);
		}
		return duplicateDataModel;
	}

	/**
	 * sets duplicateDataModel
	 */
	public void setDuplicateDataModel(DuplicateDataModel dataModel) {
		this.duplicateDataModel = dataModel;
	}

	/**
	 * gets availableRoles
	 * 
	 * @return
	 */
	public List<SelectItem> getAvailableRoles() {
		if (availableRoles == null) {
			availableRoles = new ArrayList<SelectItem>();
			for (UserRoleDTO role : requestRoleList()) {
				availableRoles.add(new SelectItem(role.getName()));
			}
		}
		return availableRoles;
	}

	/**
	 * sets availableRoles
	 */
	public void setAvailableRoles(List<SelectItem> availableRoles) {
		this.availableRoles = availableRoles;
	}

	/**
	 * gets selectedRole
	 * 
	 * @return
	 */
	public String getSelectedRole() {
		return selectedRole;
	}

	/**
	 * gets selectedRole
	 * 
	 * @return
	 */
	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	/**
	 * loads list of user from database
	 * 
	 * @return
	 */
	public List<UserDTO> requestUserList() {
		try {
			IPorsAdministratorService port = connectWS();
			return port.getUsers(currentUser);
		} catch (PORSException_Exception e) {
			MessageHandler.getInstance().addErrorMessage(
					ResourceHandler.getMessageResourceString("message_requestuserlist_error", new Object[] { e.getFaultInfo().getErrorDescription() }));
		}
		return null;
	}

	/**
	 * gets availableUsers
	 * 
	 * @return
	 */
	public List<SelectItem> getAvailableUsers() {
		if (availableUsers == null) {
			availableUsers = new ArrayList<SelectItem>();
			for (UserDTO user : requestUserList()) {
				availableUsers.add(new SelectItem(user.getId(), user
						.getUsername()));
			}
		}
		return availableUsers;
	}

	/**
	 * sets availableUsers
	 */
	public void setAvailableUsers(List<SelectItem> availableUsers) {
		this.availableUsers = availableUsers;
	}

	/**
	 * gets selectedUserId
	 * 
	 * @return
	 */
	public Integer getSelectedUserId() {
		return selectedUserId;
	}

	/**
	 * sets selectedUserId
	 */
	public void setSelectedUserId(Integer selectedUserId) {
		this.selectedUserId = selectedUserId;
		logger.info("user id selected: " + selectedUserId);
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}
}
