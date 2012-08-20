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
package org.openehealth.pors.message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;
import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.ProviderDTO;
import org.openehealth.pors.core.exception.InvalidHL7MessageException;
import org.openehealth.pors.message.exception.HL7ContentException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.MFN_M02;
import ca.uhn.hl7v2.model.v251.message.QBP_Q21;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * Implementation of {@link IHl7MessageHandler}
 * 
 * @see IHl7MessageHandler
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class Hl7MessageHandlerBean implements IHl7MessageHandler {

	// HL7 Message constants
	private static final String MESSAGEVERSION251 = "2.5.1";
	private static final String STANDARDFIELDSEPARATOR = "|";
	private static final String STANDARDCODINGCHAR = "^~\\&";
	private static final String PROCESSINGIDP = "P";
	private static final int FAILUREMESSAGECONTROLID = 999999999;
	private static final String APPLICATIONNAME = "PORS";
	private static final String FACILITYNAME = "UKHD";
	private static final String MESSAGETYPEMFKM02 = "MFK^M02^MFK_M02";
	//private static final String MESSAGETYPERSPQ11 = "RSP^Z80^QBP_Q11";

	//private static final String PERSONALLOCATION = "PL";

	// Referenced Tables
	private static final String PROVIDER = "PRO";
	private static final String ORGANISATION = "ORG";

	// Severities
	//private static final String SEVERITYWARNING = "W";
	private static final String SEVERITYERROR = "E";
	private static final String SEVERITYINFORMATION = "I";

	// Response Level Codes
	//private static final String RESPONSENEVER = "NE";
	private static final String RESPONSEALWAYS = "AL";
	//private static final String RESPONSEBYERROR = "ER";
	//private static final String RESPONSEBYSUCCESS = "SU";

	// Record Level Event Code
	private static final String RECORDADD = "MAD";
	private static final String RECORDDELETE = "MDL";
	private static final String RECORDUPDATE = "MUP";
	private static final String RECORDDEACTIVATE = "MDC";
	private static final String RECORDACTIVATE = "MAC";

	// Query Response Status
	private static final String DATAFOUNDNOERRORS = "OK";
	private static final String NODATAFOUNDNOERRORS = "NF";

	// Response Codes
	private static final String COMMITACCEPT = "CA";
	private static final String COMMITREJECT = "CR";
	private static final String COMMITERROR = "CE";

	private Hl7DataAssembler hl7DataAssembler = new Hl7DataAssembler();
	private PipeParser pipeParser = new PipeParser();
	private SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyyMMddhhmmss");

	private Logger logger;

	@PostConstruct
	public void initLogging() {
		logger = Logger.getLogger(Hl7MessageHandlerBean.class);
	}

	/**
	 * @see IHl7MessageHandler#processMessage(String)
	 */
	public List<Task> processMessage(String hl7Message)
			throws HL7ContentException {

		try {
			// parse the message string into a Message object
			Message message = pipeParser.parse(hl7Message);

			// Check version of the message
			if (MESSAGEVERSION251.equals(message.getVersion())) {

				logger.info("Message has the right version");

				if (message instanceof MFN_M02) {

					logger.info("processMessage: Message is of type MFN:M02");

					return this.parseMFN_M02Message((MFN_M02) message);

				} else if (message instanceof QBP_Q21) {

					logger.info("processMessage: Message is of type QBP_Q21");

					ArrayList<Task> tList = new ArrayList<Task>();
					tList.add(this.parseQBP_Q21Message((QBP_Q21) message));
					return tList;

				} else {
					throw new HL7ContentException(
							"The message type is not supported",
							HL7Exception.UNSUPPORTED_MESSAGE_TYPE);
				}

			} else {
				throw new HL7ContentException(
						"Please use messages compatible with HL7 V2.5.1 Standard",
						HL7Exception.UNSUPPORTED_VERSION_ID);
			}

		} catch (EncodingNotSupportedException e) {
			throw new HL7ContentException("EncodingNotSupportedException",
					HL7Exception.APPLICATION_INTERNAL_ERROR);
		} catch (DataTypeException dte) {
			throw new HL7ContentException(dte.getMessage(),
					HL7Exception.DATA_TYPE_ERROR);
		} catch (HL7Exception hle) {
			throw new HL7ContentException(hle.getMessage(), hle.getErrorCode());
		}

	}

	/**
	 * Parsing Messages of Message Type QBP_Q21 (QBP - Query by parameter
	 * requesting an RSP segment pattern response)
	 * 
	 * @author ck
	 * @param qbpQ21Message
	 *            Message of type MFN_M02
	 * @throws IOException
	 * @throws HL7Exception
	 * 
	 **/
	private Task parseQBP_Q21Message(QBP_Q21 qbpQ21Message) throws HL7Exception {

		int domain;
		Task task = null;

		if (null != qbpQ21Message.getMSH()
				&& null != qbpQ21Message.getMSH().getSendingFacility()
				&& null != qbpQ21Message.getMSH().getSendingApplication()) {

			// Check referenced Table
			if (qbpQ21Message.getQPD().getQpd1_MessageQueryName()
					.getCe1_Identifier().getValue().equals("Q81")) {
				logger.info("domain is provider");
				domain = Task.PROVIDER;
			} else if (qbpQ21Message.getQPD().getQpd1_MessageQueryName()
					.getCe1_Identifier().getValue().equals("Q82")) {
				logger.info("domain is organisation");
				domain = Task.ORGANISATION;
			} else {
				throw new HL7Exception("Query not supportet!",
						HL7Exception.DATA_TYPE_ERROR);
			}

			task = new Task(Task.QUERY, domain);
			task.setLogicalOperator(Task.LOGICAL_OPERATOR_AND);

			if (domain == Task.PROVIDER) {
				// Assembling ProviderDTO from STF Element and set it in task
				task.setProvider(hl7DataAssembler.assembleProvider(
						qbpQ21Message.getQPD(), qbpQ21Message.getMSH()
								.getSendingFacility().encode(), qbpQ21Message
								.getMSH().getSendingApplication().encode()));
			} else if (domain == Task.ORGANISATION) {
				task.setOrganisation(hl7DataAssembler.assembleOrganisation(
						qbpQ21Message.getQPD(), qbpQ21Message.getMSH()
								.getSendingFacility().encode(), qbpQ21Message
								.getMSH().getSendingApplication().encode()));
			}

			return task;

		} else {
			throw new HL7Exception(
					"MSH Segment not complete; Missing Facility or Application",
					HL7Exception.SEGMENT_SEQUENCE_ERROR);
		}
	}

	/**
	 * Parsing Messages of Message Type MFN_M02 (Master File Notification _Staff
	 * Practitioner) These messages will invoke an action on provider or
	 * organisation.
	 * 
	 * @author ck
	 * @param mfnm02Message
	 *            Message of type MFN_M02
	 * @return List<Task> a list of tasks, which will be executed by the
	 *         controller
	 * @throws DataTypeException
	 * @throws HL7Exception
	 **/
	private List<Task> parseMFN_M02Message(MFN_M02 mfnm02Message)
			throws DataTypeException, HL7Exception {

		int domain;
		ArrayList<Task> taskList = new ArrayList<Task>();

		if (null != mfnm02Message.getMSH()
				&& null != mfnm02Message.getMSH().getSendingFacility()
				&& null != mfnm02Message.getMSH().getSendingApplication()) {

			// Check referenced Table
			if (mfnm02Message.getMFI().getMfi1_MasterFileIdentifier()
					.getCe1_Identifier().toString().startsWith(PROVIDER)) {

				logger.info("domain is provider");

				domain = Task.PROVIDER;
			} else if (mfnm02Message.getMFI().getMfi1_MasterFileIdentifier()
					.getCe1_Identifier().toString().startsWith(ORGANISATION)) {

				domain = Task.ORGANISATION;
			} else {
				throw new HL7Exception("No such Masterfile",
						HL7Exception.DATA_TYPE_ERROR);
			}

			// Reading MFE STF Segments
			String rlec;
			Task task;
			for (int i = 0; i < mfnm02Message.getMF_STAFFReps(); i++) {

				// Checking Action (Record Level Event Code) to be performed
				if (null != mfnm02Message.getMF_STAFF(i).getMFE()
						&& null != mfnm02Message.getMF_STAFF(i).getMFE()
								.getMfe1_RecordLevelEventCode()
						&& null != mfnm02Message.getMF_STAFF(i).getMFE()
								.getMfe1_RecordLevelEventCode().toString()) {
					rlec = mfnm02Message.getMF_STAFF(i).getMFE()
							.getMfe1_RecordLevelEventCode().toString();
					logger.info("RECORD LEVEL EVENT: " + rlec);
					if (RECORDACTIVATE.equals(rlec)) {
						task = new Task(Task.REACTIVATE, domain);
					} else if (RECORDADD.equals(rlec)) {
						task = new Task(Task.CREATE, domain);
						logger.info("action is create");
					} else if (RECORDDEACTIVATE.equals(rlec)) {
						task = new Task(Task.DEACTIVATE, domain);
					} else if (RECORDDELETE.equals(rlec)) {
						task = new Task(Task.DELETE, domain);
					} else if (RECORDUPDATE.equals(rlec)) {
						task = new Task(Task.UPDATE, domain);
					} else {
						throw new HL7Exception(
								"No such record level event code",
								HL7Exception.UNSUPPORTED_EVENT_CODE);
					}
				} else {
					throw new HL7Exception(
							"MFE Segment not correct or not present",
							HL7Exception.SEGMENT_SEQUENCE_ERROR);
				}

				if (domain == Task.PROVIDER) {
					// Assembling ProviderDTO from STF Element and set it in
					// task
					task.setProvider(hl7DataAssembler.assembleProvider(
							mfnm02Message.getMF_STAFF(i).getSTF(),
							mfnm02Message.getMSH().getSendingFacility()
									.encode(), mfnm02Message.getMSH()
									.getSendingApplication().encode()));
				} else if (domain == Task.ORGANISATION) {
					task.setOrganisation(hl7DataAssembler.assembleOrganisation(
							mfnm02Message.getMF_STAFF(i).getSTF(),
							mfnm02Message.getMSH().getSendingFacility()
									.encode(), mfnm02Message.getMSH()
									.getSendingApplication().encode()));
				}
				taskList.add(task);
			}

			if (taskList.size() > 0) {
				return taskList;
			} else {
				throw new HL7Exception(
						"MFE Segment not correct or not present",
						HL7Exception.SEGMENT_SEQUENCE_ERROR);
			}
		} else {
			throw new HL7Exception(
					"MSH Segment not complete; Missing Facility or Application",
					HL7Exception.SEGMENT_SEQUENCE_ERROR);
		}
	}

	/**
	 * @see IHl7MessageHandler#getRejectMFAcknowledgement(String, Exception)
	 **/
	public String getRejectMFAcknowledgement(String hl7Message, Exception e) {
		try {

			Message message = pipeParser.parse(hl7Message);

			String mfa = message.generateACK(
					COMMITREJECT,
					new HL7Exception(e.getMessage(),
							HL7Exception.APPLICATION_INTERNAL_ERROR)).encode();

			String sfs = STANDARDFIELDSEPARATOR;
			return this.getMFASegmentsMSHaMSAaMFI(hl7Message, COMMITREJECT)
					+ mfa.substring(mfa.indexOf("ERR|"), mfa.length() - 1)
					+ sfs + sfs + HL7Exception.APPLICATION_INTERNAL_ERROR + sfs
					+ SEVERITYERROR + sfs + sfs + sfs + sfs + sfs + sfs + sfs
					+ sfs;

		} catch (EncodingNotSupportedException ensex) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^" + HL7Exception.APPLICATION_INTERNAL_ERROR
					+ "&ERROR&hl70357&&" + escapedMessage(ensex.getMessage())
					+ sfs + sfs + HL7Exception.APPLICATION_INTERNAL_ERROR + sfs
					+ SEVERITYERROR + sfs + sfs + sfs + sfs
					+ escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			return MSHaMSAaMFI + ERR;
		} catch (HL7Exception he) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^" + he.getErrorCode() + "&ERROR&hl70357&&"
					+ escapedMessage(he.getMessage()) + sfs + sfs
					+ he.getErrorCode() + sfs + SEVERITYERROR + sfs + sfs + sfs
					+ sfs + escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			return MSHaMSAaMFI + ERR;
		} catch (Exception ex) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^207&ERROR&hl70357&&"
					+ escapedMessage(ex.getMessage()) + sfs + sfs + "207" + sfs
					+ SEVERITYERROR + sfs + sfs + sfs + sfs
					+ escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			return MSHaMSAaMFI + ERR;
		}
	}

	/**
	 * @see IHl7MessageHandler#getAcceptMFAcknowledgement(String)
	 **/
	public String getAcceptMFAcknowledgement(String hl7Message)
			throws InvalidHL7MessageException {

		try {
			Message message = pipeParser.parse(hl7Message);

			String mfa = message.generateACK(
					COMMITACCEPT,
					new HL7Exception("Message Accepted",
							HL7Exception.MESSAGE_ACCEPTED)).encode();

			String sfs = STANDARDFIELDSEPARATOR;

			return this.getMFASegmentsMSHaMSAaMFI(hl7Message, COMMITACCEPT)
					+ mfa.substring(mfa.indexOf("ERR|"), mfa.length() - 1)
					+ sfs + sfs + HL7Exception.MESSAGE_ACCEPTED + sfs
					+ SEVERITYINFORMATION + sfs + sfs + sfs + sfs + sfs + sfs
					+ sfs + sfs;

		} catch (EncodingNotSupportedException ensex) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^" + HL7Exception.APPLICATION_INTERNAL_ERROR
					+ "&ERROR&hl70357&&" + escapedMessage(ensex.getMessage())
					+ sfs + sfs + HL7Exception.APPLICATION_INTERNAL_ERROR + sfs
					+ SEVERITYERROR + sfs + sfs + sfs + sfs
					+ escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			throw new InvalidHL7MessageException(ensex.getMessage(),
					MSHaMSAaMFI + ERR);
		} catch (HL7Exception he) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^" + he.getErrorCode() + "&ERROR&hl70357&&"
					+ escapedMessage(he.getMessage()) + sfs + sfs
					+ he.getErrorCode() + sfs + SEVERITYERROR + sfs + sfs + sfs
					+ sfs + escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			throw new InvalidHL7MessageException(he.getMessage(), MSHaMSAaMFI
					+ ERR);
		} catch (Exception ex) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^207&ERROR&hl70357&&"
					+ escapedMessage(ex.getMessage()) + sfs + sfs + "207" + sfs
					+ SEVERITYERROR + sfs + sfs + sfs + sfs
					+ escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			throw new InvalidHL7MessageException(ex.getMessage(), MSHaMSAaMFI
					+ ERR);
		}

	}

	/**
	 * @see IHl7MessageHandler#getErrorMFAcknowledgement(String,
	 *      HL7ContentException)
	 **/
	public String getErrorMFAcknowledgement(String hl7Message,
			HL7ContentException h7e) {

		try {

			Message message = pipeParser.parse(hl7Message);

			String mfa = message.generateACK(
					COMMITERROR,
					new HL7Exception(escapedMessage(h7e.getMessage()), h7e
							.getErrorCode())).encode();
			String sfs = STANDARDFIELDSEPARATOR;

			return this.getMFASegmentsMSHaMSAaMFI(hl7Message, COMMITERROR)
					+ mfa.substring(mfa.indexOf("ERR|"), mfa.length() - 1)
					+ sfs + sfs + h7e.getErrorCode() + sfs + SEVERITYERROR
					+ sfs + sfs + sfs + sfs + sfs + sfs + sfs + sfs;

		} catch (EncodingNotSupportedException ensex) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^" + HL7Exception.APPLICATION_INTERNAL_ERROR
					+ "&ERROR&hl70357&&" + escapedMessage(ensex.getMessage())
					+ sfs + sfs + HL7Exception.APPLICATION_INTERNAL_ERROR + sfs
					+ SEVERITYERROR + sfs + sfs + sfs + sfs
					+ escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			return MSHaMSAaMFI + ERR;
		} catch (HL7Exception he) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^" + he.getErrorCode() + "&ERROR&hl70357&&"
					+ escapedMessage(he.getMessage()) + sfs + sfs
					+ he.getErrorCode() + sfs + SEVERITYERROR + sfs + sfs + sfs
					+ sfs + escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			return MSHaMSAaMFI + ERR;
		} catch (Exception ex) {
			String MSHaMSAaMFI = this.getMFASegmentsMSHaMSAaMFI(hl7Message,
					COMMITERROR);
			String sfs = STANDARDFIELDSEPARATOR;
			String ERR = "ERR|^^^207&ERROR&hl70357&&"
					+ escapedMessage(ex.getMessage()) + sfs + sfs + "207" + sfs
					+ SEVERITYERROR + sfs + sfs + sfs + sfs
					+ escapedMessage(hl7Message) + sfs + sfs + sfs + sfs;
			return MSHaMSAaMFI + ERR;
		}

	}

	/**
	 * This method does escape the standard coding characters of the original
	 * hl7 message and returns the message with the hidden characters
	 * 
	 * @param hl7Message
	 * @return Message in which the StandardCodingCharacters are escaped
	 */
	private String escapedMessage(String hl7Message) {
		String message = hl7Message.replace("^", "\\^");
		message = message.replace("~", "\\~");
		message = message.replace("\\", "\\\\");
		message = message.replaceAll("\\n", "\\\\n");
		message = message.replaceAll("\\r", "\\\\r");
		message = message.replaceAll("|", "");
		return message.replace("&", "\\&");
	}

	/**
	 * This method builds the MSH and MSA segment of the answer message and is
	 * called, if the standard method is not convenient or throws an exception.
	 * 
	 * @param message
	 *            , the request
	 * @param String
	 *            , the ack code for the answer
	 * @return String, containing the MSH, MSA segment and line separators.
	 **/
	private String getMFASegmentsMSHaMSAaMFI(String message, String ackCode) {
		String encodingChar = STANDARDCODINGCHAR;
		int messageControlId = FAILUREMESSAGECONTROLID;
		String processingId = PROCESSINGIDP;
		String versionId = MESSAGEVERSION251;
		String receivingApplication = "";
		String receivingFacility = "";
		String masterFile = "";

		try {
			MFN_M02 m = (MFN_M02) pipeParser.parse(message);
			if (null != m.getMSH()) {
				if (null != m.getMSH().getEncodingCharacters()
						&& null != m.getMSH().getEncodingCharacters()
								.toString()
						&& 0 < m.getMSH().getEncodingCharacters().toString()
								.length()) {
					encodingChar = m.getMSH().getEncodingCharacters()
							.toString();

				}
				if (null != m.getMSH().getMsh3_SendingApplication()
						&& null != m.getMSH().getSendingApplication().encode()
						&& 0 < m.getMSH().getSendingApplication().encode()
								.length()) {
					receivingApplication = m.getMSH().getSendingApplication()
							.encode();
				}
				if (null != m.getMSH().getMsh4_SendingFacility()
						&& null != m.getMSH().getMsh4_SendingFacility()
								.encode()
						&& 0 < m.getMSH().getMsh4_SendingFacility().encode()
								.length()) {
					receivingFacility = m.getMSH().getMsh4_SendingFacility()
							.encode();
				}
				if (null != m.getMSH().getMsh10_MessageControlID()
						&& null != m.getMSH().getMsh10_MessageControlID()
								.toString()
						&& 0 < m.getMSH().getMsh10_MessageControlID()
								.toString().length()) {
					messageControlId = Integer.valueOf(
							m.getMSH().getMsh10_MessageControlID().toString())
							.intValue();
				}
				if (null != m.getMSH().getMsh11_ProcessingID()
						&& null != m.getMSH().getMsh11_ProcessingID().encode()
						&& 0 < m.getMSH().getMsh11_ProcessingID().encode()
								.length()) {
					processingId = m.getMSH().getMsh11_ProcessingID().encode();
				}
				if (null != m.getMSH().getMsh12_VersionID()
						&& null != m.getMSH().getMsh12_VersionID().encode()
						&& 0 < m.getMSH().getMsh12_VersionID().encode()
								.length()) {
					versionId = m.getMSH().getMsh12_VersionID().encode();
				}
			}
			if (null != m.getMFI()) {
				if (null != m.getMFI().getMfi1_MasterFileIdentifier()
						&& null != m.getMFI().getMfi1_MasterFileIdentifier()
								.encode()
						&& 0 < m.getMFI().getMfi1_MasterFileIdentifier()
								.encode().length()) {
					masterFile = m.getMFI().getMfi1_MasterFileIdentifier()
							.encode();
				}
			}

		} catch (Exception e) {
			logger.warn("Returning Standard Acknowledgement", e);
		}

		String msg="MSH|"+encodingChar+"|"+APPLICATIONNAME+"|"+FACILITYNAME+"|"+receivingApplication+"|"+receivingFacility+"|"+sDateFormat.format(new Date())+"||"+MESSAGETYPEMFKM02+"|"+(messageControlId + 1)+"|"+processingId+"|"+versionId + "\r" + 
				   "MSA|"+ackCode+"|"+messageControlId + "\r" + 
				   "MFI|"+masterFile+"||"+
				   "UPD"+"|||"+RESPONSEALWAYS + "\r";
	
		return msg;

	}

	/**
	 * @see IHl7MessageHandler#getOrganisationsRSP_Q11(String,
	 *      List<OrganisationDTO>)
	 **/
	public String getOrganisationsRSP_K25(String hl7Message,
			List<OrganisationDTO> orgList) {

		String[] businessPartnerIdAndSTFs = hl7DataAssembler
				.assembleSTFsFromOrganisations(orgList);
		String RSP_Q11WithoutSTF = this.getRSP_K25WithoutSTF(hl7Message,
				DATAFOUNDNOERRORS, businessPartnerIdAndSTFs[0]);

		return RSP_Q11WithoutSTF + businessPartnerIdAndSTFs[1];
	}

	/**
	 * @see IHl7MessageHandler#getProvidersRSP_Q11(String, List<ProviderDTO>)
	 **/
	public String getProvidersRSP_K25(String hl7Message,
			List<ProviderDTO> proList) {

		String[] businessPartnerIdAndSTFs = hl7DataAssembler
				.assembleSTFsFromProviders(proList);
		String RSP_K25WithoutSTF = this.getRSP_K25WithoutSTF(hl7Message,
				DATAFOUNDNOERRORS, businessPartnerIdAndSTFs[0]);

		return RSP_K25WithoutSTF + businessPartnerIdAndSTFs[1];

	}

	/**
	 * @see IHl7MessageHandler#getRSP_K25NoDataFound(String)
	 **/
	public String getRSP_K25NoDataFound(String hl7Message)
			throws InvalidHL7MessageException {
		return this.getRSP_K25WithoutSTF(hl7Message, NODATAFOUNDNOERRORS, "");
	}

	/**
	 * @see IHl7MessageHandler#getRSP_Q11WithoutSTF(String, String, String)
	 **/
	private String getRSP_K25WithoutSTF(String hl7Message,
			String queryResponseStatus, String internalBusinessIds) {

		String processingId = PROCESSINGIDP;
		String versionId = MESSAGEVERSION251;
		String receivingApplication = "";
		String receivingFacility = "";
		String queryTag = "";
		String messageQueryName = "";
		String qpd = "";
		String rcp = "";

		try {
			Message message = pipeParser.parse(hl7Message);
			QBP_Q21 qbp = (QBP_Q21) message;
			
			logger.info("Original QBP: "+qbp.encode());
			
			if (null != qbp.getMSH()) {
				if (null != qbp.getMSH().getMsh3_SendingApplication()
						&& null != qbp.getMSH().getSendingApplication()
								.encode()
						&& 0 < qbp.getMSH().getSendingApplication().encode()
								.length()) {
					receivingApplication = qbp.getMSH().getSendingApplication()
							.encode();
				}
				if (null != qbp.getMSH().getMsh4_SendingFacility()
						&& null != qbp.getMSH().getMsh4_SendingFacility()
								.encode()
						&& 0 < qbp.getMSH().getMsh4_SendingFacility().encode()
								.length()) {
					receivingFacility = qbp.getMSH().getMsh4_SendingFacility()
							.encode();
				}
				if (null != qbp.getMSH().getMsh11_ProcessingID()
						&& null != qbp.getMSH().getMsh11_ProcessingID()
								.encode()
						&& 0 < qbp.getMSH().getMsh11_ProcessingID().encode()
								.length()) {
					processingId = qbp.getMSH().getMsh11_ProcessingID()
							.encode();
				}
				if (null != qbp.getMSH().getMsh12_VersionID()
						&& null != qbp.getMSH().getMsh12_VersionID().encode()
						&& 0 < qbp.getMSH().getMsh12_VersionID().encode()
								.length()) {
					versionId = qbp.getMSH().getMsh12_VersionID().encode();
				}
				if (null != qbp.getQPD()
						&& null != qbp.getQPD().getQpd2_QueryTag()
						&& 0 < qbp.getQPD().getQpd2_QueryTag().encode()
								.length()) {
					queryTag = qbp.getQPD().getQpd2_QueryTag().encode();
				}
				if (null != qbp.getQPD()
						&& null != qbp.getQPD().getQpd1_MessageQueryName()
						&& 0 < qbp.getQPD().getQpd1_MessageQueryName().encode()
								.length()) {
					messageQueryName = qbp.getQPD().getQpd1_MessageQueryName()
							.encode();
				}
				if (null != qbp.getQPD() && null != qbp.getQPD().encode()
						&& 0 < qbp.getQPD().encode().length()) {
					qpd = qbp.getQPD().encode();
				}
				if (null != qbp.getRCP()) {
					rcp = qbp.getRCP().encode();
				}
			}

		} catch (Exception e) {
			logger.warn("Returning Standard Acknowledgement", e);
		}
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = sd.format(new Date());
		long controllid = new Date().getTime();
		String msgControllID = new StringBuffer(String.valueOf(controllid)).reverse().toString();
		
		String msg = "MSH|^~\\&|"+APPLICATIONNAME+"|"+FACILITYNAME+"|"+receivingApplication+"|"+receivingFacility+"|"+timestamp+"||RSP^K25^RSP_K25|"+msgControllID+"|"+processingId+"|"+versionId +"\r"+ 
		             "MSA|CA|"+msgControllID+"\r"+
		             "QAK|"+queryTag+"|"+queryResponseStatus+"|"+messageQueryName+"\r"+
		             qpd+"\r"+
		             rcp+"\r";		             ;

		return msg;
	}
}
