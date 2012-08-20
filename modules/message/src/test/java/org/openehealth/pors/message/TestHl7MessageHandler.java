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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;

import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.exception.InvalidHL7MessageException;
import org.openehealth.pors.core.exception.MissingRightsException;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.message.exception.HL7ContentException;

import ca.uhn.hl7v2.HL7Exception;

public class TestHl7MessageHandler extends TestCase {
	
	private IHl7MessageHandler messageHandler;
	
	private DateFormat dateFormat;
	
	private String mfnProviderTest = "MSH|^~\\&|SAP-ISH^sapr3t^002|UKHD^0999|PORS||200912151600||MFN^M02^MFN_M02|1234|P|2.5.1\r" +
	"MFI|PRO^Practitioner Master File^HL70175||UPD|||AL\r" + 
	//PRA...:Table identification, UPD:Update entry, AL: Always send acknowledgement
	"MFE|MAD|||0000001234|PL\r" +
	//MAD: Add record to master-file,0000001234: Primary key value,PL: Primary Key Value Type: Person Location
	"STF|0000001234|179999900|Beckenbauer^Franz^Josef^JR^Dr|Internist|M|19501101||" +
	// |Primary Key Value|Staff Identifier List|Staff Name|Staff Type|Sex|DateTimeBirth|InActiveFlag|
	"|999^UKHD^Universitätsklinikum Heidelberg^1.2.276.0.76.3.1.78|" +
	//Department|Hopital Service - STF|
	"00496221566736^00496221562000^franz@beckenbauer.de|StreetOrMailing&StreetAddress&99^OtherDesignation^City^State^Zip^CY|" +
	//Phone|OfficeHomeAddressBirthplace|
	"20091211|20081101||||" +
	//InstitutionActivationDate|InactivationDate|BackupPersonID|EMailAddress|PreferredMethodContact|
	"|||||" +
	//MaritalStatus|JobTitle|JobCode|EmploymentStatus|AdditionalInsuredOnAuto|
	"|||||||" +
	//DriversLicenseNumber|CopyAutoIns|AutoInsExpire|DateLastDMVReview|DateNextDMVReview|Race|EthnicGroup|
	"|||||" +
	//ReActivationApprovalInd|Citizenship|DeathDateUTime|DeathIndicator|InstitutionRelationshipCode|
	"|||R|1"; //Added last value "|1"
	//InstitutionRelationshipPeriod|ExpectedReturnDate|CostCenterCode|GenericClassificationIndicator|InactiveReasonCode
			
	private String mfnOrganisationTest = "MSH|^~\\&|SAP-ISH^sapr3t^002|UKHD^0999|PORS||200912151600||MFN^M02^MFN_M02|1234|P|2.5.1\r" +
	"MFI|ORG^Practitioner Master File^HL70175||UPD|||AL\r" + 
	//PRA...:Table identification, UPD:Update entry, AL: Always send acknowledgement
	"MFE|MAD|||0000001234|PL\r" +
	//MAD: Add record to master-file,0000001234: Primary key value,PL: Primary Key Value Type: Person Location
	"STF|0000002345|179999999|OrgName1^OrgName2|Fachrichtung||||" +
	// |Primary Key Value|Staff Identifier List|Staff Name|Staff Type|Sex|DateTimeBirth|InActiveFlag|
	"|999^UKHD^Universitätsklinikum Heidelberg^1.2.276.0.76.3.1.78|" +
	//Department|Hopital Service - STF|
	"Telefon^Fax^Mail|StreetOrMailing&StreetAddress&99^OtherDesignation^City^State^Zip^DE|" +
	//Phone|OfficeHomeAddressBirthplace|
	"20091211|20081101||||" +
	//InstitutionActivationDate|InactivationDate|BackupPersonID|EMailAddress|PreferredMethodContact|
	"|||||" +
	//MaritalStatus|JobTitle|JobCode|EmploymentStatus|AdditionalInsuredOnAuto|
	"|||||||" +
	//DriversLicenseNumber|CopyAutoIns|AutoInsExpire|DateLastDMVReview|DateNextDMVReview|Race|EthnicGroup|
	"|||||" +
	//ReActivationApprovalInd|Citizenship|DeathDateUTime|DeathIndicator|InstitutionRelationshipCode|
	"|||R|1"; //Added last value "|1"
	//InstitutionRelationshipPeriod|ExpectedReturnDate|CostCenterCode|GenericClassificationIndicator|InactiveReasonCode
			

	protected void setUp() throws Exception {
		super.setUp();
		messageHandler = new Hl7MessageHandlerBean();
		((Hl7MessageHandlerBean) messageHandler).initLogging();
		dateFormat = new SimpleDateFormat("yyyyMMdd");
	}

	public void testProcessMessage() throws HL7ContentException, ParseException {
		
		// #### Add Provider Test ####
		List<Task> tasks = messageHandler.processMessage(this.mfnProviderTest);
		
		assertTrue(tasks.size() == 1);
		
		// - Test task attributes
		Task task = tasks.get(0);
		assertTrue(task.getAction() == Task.CREATE);
		assertTrue(task.getDomain() == Task.PROVIDER);
		assertTrue(task.getActionDomain() == (Task.CREATE + Task.PROVIDER));
		
		// - Test Provider attributes
		Provider provider = task.getProvider();
		assertTrue(dateFormat.parse("19501101").equals(provider.getBirthday()));
		assertTrue(dateFormat.parse("20081101").equals(provider.getDeactivationDate()));
		assertTrue("1".equals(provider.getDeactivationReasonCode()));
		assertEquals("franz@beckenbauer.de", provider.getEmail());
		assertEquals("00496221562000", provider.getFax());
		assertEquals("Franz", provider.getFirstName());
		assertEquals("m", provider.getGenderCode());
		assertEquals("179999900", provider.getLanr());
		assertEquals("Beckenbauer", provider.getLastName());
		assertEquals("Josef", provider.getMiddleName());
		assertEquals("Dr", provider.getNamePrefix());
		assertEquals("JR", provider.getNameSuffix());
		assertTrue(dateFormat.parse("20091211").equals(provider.getReactivationDate()));
		assertEquals("00496221566736", provider.getTelephone());
		
		// - Test Provider's address
		List <Address> addresses = provider.getAddresses();
		Address address = addresses.get(0);
		assertEquals("StreetOrMailing StreetAddress",address.getStreet());
		assertEquals("99", address.getHouseNumber());
		assertEquals("OtherDesignation", address.getAdditional());
		assertEquals("State", address.getState());
		assertEquals("City", address.getCity());
		assertEquals("Zip", address.getZipCode());
		assertEquals("CY", address.getCountry());
		
		// - Get message accept answer
		
		try {
			System.out.println(messageHandler.getAcceptMFAcknowledgement(mfnProviderTest));
		} catch (InvalidHL7MessageException e) {
			System.out.println("Provider accept answer: InvalidHL7Exception");
		}
		
		
		// #### Add organisation test ####
		tasks = messageHandler.processMessage(this.mfnOrganisationTest);
		
		
		// - Test task attributes
		task = tasks.get(0);
		assertTrue(task.getAction() == Task.CREATE);
		assertTrue(task.getDomain() == Task.ORGANISATION);
		assertTrue(task.getActionDomain() == (Task.CREATE + Task.ORGANISATION));
		
		// - Test Organisation attributes
		Organisation organisation = task.getOrganisation();
		assertTrue(dateFormat.parse("20081101").equals(organisation.getDeactivationDate()));
		assertTrue("1".equals(organisation.getDeactivationReasonCode()));
		assertEquals("Mail", organisation.getEmail());
		assertEquals("Fax", organisation.getFax());
		assertEquals("OrgName1", organisation.getName());
		assertEquals("OrgName2", organisation.getSecondName());
		assertEquals("179999999", organisation.getEstablishmentId());
		//assertEquals("Beckenbauer", provider.getLastName());
		assertTrue(dateFormat.parse("20091211").equals(organisation.getReactivationDate()));
		assertEquals("Telefon", organisation.getTelephone());
		
		// - Test Organisation's address
		addresses = organisation.getAddresses();
		address = addresses.get(0);
		assertEquals("StreetOrMailing StreetAddress",address.getStreet());
		assertEquals("99", address.getHouseNumber());
		assertEquals("OtherDesignation", address.getAdditional());
		assertEquals("State", address.getState());
		assertEquals("City", address.getCity());
		assertEquals("Zip", address.getZipCode());
		assertEquals("DE", address.getCountry());
		
		// - Test Organisation accept answer
		try {
			System.out.println(messageHandler.getAcceptMFAcknowledgement(mfnOrganisationTest));
		} catch (InvalidHL7MessageException e) {
			System.out.println("Provider accept answer: InvalidHL7Exception");
		}
	}
	
	public void testGetRejectMFAcknowledgement(){
		MissingRightsException mre = new MissingRightsException("");
		
		String ack = messageHandler.getRejectMFAcknowledgement(mfnProviderTest, mre);
		
		System.out.println(ack);
	}

	public void testGetErrorMFAcknowledgement(){
		HL7ContentException hl7 = new HL7ContentException("Hallo Welt", HL7Exception.UNSUPPORTED_MESSAGE_TYPE);
		
		String test = "test\rtest\rtest";
		
		String ack = messageHandler.getErrorMFAcknowledgement(test, hl7);
		
		System.out.println(ack);
	}

	public void testGetAcceptMFAcknowledgement(){
		
		String ack;
		try {
			ack = messageHandler.getAcceptMFAcknowledgement(mfnProviderTest);
		} catch (InvalidHL7MessageException e) {
			ack = e.getHl7Message();
		}
		
		System.out.println(ack);
	}
	

}
