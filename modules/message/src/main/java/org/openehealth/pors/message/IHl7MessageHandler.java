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

import java.util.List;

import javax.ejb.Local;

import org.openehealth.pors.message.exception.HL7ContentException;

import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.ProviderDTO;
import org.openehealth.pors.core.exception.InvalidHL7MessageException;


/**
 * @author ck
 *
 */
@Local
public interface IHl7MessageHandler {
	/**
	 * This method is the universal entry point for parsing HL7 messages. By discovering the hl7 message string,
	 * this method decides, what the user or the external system wants to do. As a result a List of tasks is returned.
	 * A Task can contain one of different Actions and one Domain. If the external system intended to e.g. add
	 * an entry to the database/masterfile, the data is written into the task object.
	 * @throws HL7ContentException
	 * @param message, the request
	 * @return List<Task>, a list of tasks representing the intention of the Hl7message
	 **/
	List<Task> processMessage(String hl7Message) throws HL7ContentException;
	/**
	 * Building an answer message for a given HL7Message and an Exception. Some exception information will be 
	 * written into the answer message.The Answer message contains a "commit reject" ack code.
	 * @author ck
	 * @param hl7Message , the request
	 * @param Exception responsible for Rejection
	 * @return String, answer message with ack code = commit reject
	 **/
	String getRejectMFAcknowledgement(String hl7Message, Exception e);
	/**
	 * Building an answer message for a given HL7Message and a Hl7Exception. Some exception information will be 
	 * written into the answer message. The Answer message contains a "commit error" ack code.
	 * @author ck
	 * @param hl7Message , the request
	 * @param hl7ContentException, containing the reason for the Error
	 * @return String, answer message with ack code = commit error
	 **/
	String getErrorMFAcknowledgement(String hl7Message, HL7ContentException he);
	/**
	 * Building an answer message for a given HL7Message. The Answer message contains a "commit accept" ack code.
	 * @author ck
	 * @param hl7Message , the request
	 * @return String, answer message with ack code = commit accept
	 * @throws InvalidHL7MessageException 
	 **/
	String getAcceptMFAcknowledgement(String hl7Message) throws InvalidHL7MessageException;
	/**
	 * Building an answer message of type RSP_Q11 for a given HL7Message and organisations.
	 * @author ck
	 * @param hl7Message , the request
	 * @param orgList, the Organisations found for the query
	 * @return String, answer message 
	 **/
	String getOrganisationsRSP_K25(String hl7Message, List<OrganisationDTO> orgList) throws InvalidHL7MessageException;;
	/**
	 * Building an answer message of type RSP_Q11 for a given HL7Message and providers.
	 * @author ck
	 * @param hl7Message , the request
	 * @param orgList, the Providers found for the query
	 * @return String, answer message 
	 **/
	String getProvidersRSP_K25(String hl7Message, List<ProviderDTO> proList) throws InvalidHL7MessageException;;
	/**
	 * Building an answer message of type RSP_Q11 for a given HL7Message. The query had no results.
	 * @author ck
	 * @param hl7Message , the request
	 * @return String, answer message 
	 **/
	String getRSP_K25NoDataFound(String hl7Message) throws InvalidHL7MessageException;;

}
