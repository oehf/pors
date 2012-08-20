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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.openehealth.pors.core.dto.OrganisationDTO;
import org.openehealth.pors.core.dto.ProviderDTO;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.XAD;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.STF;

/**
 * Assemble application data classes from data Segments of HL7 messages 
 * @author ck
 *
 */
class Hl7DataAssembler {
	
	private DateFormat dateFormat;
	
	private Logger logger;
	private String serviceID="UKHD-PORS&1.2.276.0.76.3.1.78.1.0.10.30";
	
	private static final String FIELDSEPARATOR = "|";
	private static final String VALUESEPARATOR ="^";
	private static final String FIELDREPETITION = "~";
	private static final String SUBFIELDINDICATOR = "&";

	
	public Hl7DataAssembler() {
		 dateFormat = new SimpleDateFormat("yyyyMMdd");
		 logger = Logger.getLogger(Hl7DataAssembler.class);
	}
	/**
	 * Assembles a Provider out of a hl7 STF Segment
	 * @param stf
	 * @return Provider the assembled Provider
	 * @throws DataTypeException 
	 */
	Provider assembleProvider(STF stf, String sendingFacility, String sendingApplication) throws DataTypeException{
		Provider provider = new Provider();
		
		List<Address> addresses = assembleAddresses(stf.getStf11_OfficeHomeAddressBirthplace());
			
		if(null!=addresses && 
				addresses.size()>0){
			provider.setAddresses(addresses);
		}
		
		if(null!=stf.getStf6_DateTimeOfBirth() &&
				null!=stf.getStf6_DateTimeOfBirth().getTime() &&
				null!=stf.getStf6_DateTimeOfBirth().getTime().getValue() && 
				0<stf.getStf6_DateTimeOfBirth().getTime().getValue().length()){
			try {
				provider.setBirthday(dateFormat.parse(stf.getStf6_DateTimeOfBirth().getTime().getValue()));
			} catch (ParseException e) {
				throw new DataTypeException("Wrong format of birthday: " + e.getMessage());
			}
		}
		
		if(0<stf.getStf13_InstitutionInactivationDate().length &&
				null!=stf.getStf13_InstitutionInactivationDate()[0].getDate() &&
				null!=stf.getStf13_InstitutionInactivationDate()[0].getDate().getTime() &&
				null!=stf.getStf13_InstitutionInactivationDate()[0].getDate().getTime().getValue() &&
				0<stf.getStf13_InstitutionInactivationDate()[0].getDate().getTime().getValue().length()){
			try {
				provider.setDeactivationDate(dateFormat.parse(stf.getInstitutionInactivationDate()[0].getDate().getTime().getValue()));
			} catch (ParseException e) {
				throw new DataTypeException("Wrong format of deactivation date: " + e.getMessage());
			}
		}
		/**		 
		 * Here the ZIM Standard is used, which means only Integer values
		 * 1 = died
		 * 2 = closed
		 * 3 = cheating
		 * 4 = other
		 **/
		if(null!=stf.getStf38_InactiveReasonCode() &&
				null!=stf.getStf38_InactiveReasonCode().getCwe1_Identifier() &&
				null!=stf.getStf38_InactiveReasonCode().getCwe1_Identifier().getValue() &&
				0<stf.getStf38_InactiveReasonCode().getCwe1_Identifier().getValue().length()){
			provider.setDeactivationReasonCode(stf.getStf38_InactiveReasonCode().getCwe1_Identifier().getValue());
		}
		
		/**Getting the Email-Address, which can be in the phone-field (preferred) or
		 * in the Email field.
		 */
		if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType() &&
				null!=stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType().getValue() &&
				0<stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType().getValue().length()){
			provider.setEmail(stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType().getValue());
		}else if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getEmailAddress() &&
				null!=stf.getPhone()[0].getEmailAddress().getValue() &&
				0<stf.getPhone()[0].getEmailAddress().getValue().length()){
			provider.setEmail(stf.getPhone()[0].getEmailAddress().getValue());
		}else if(0<stf.getEMailAddress().length &&
				null!=stf.getEMailAddress()[0].getValue() &&
				0<stf.getEMailAddress()[0].getValue().length()){
			provider.setEmail(stf.getEMailAddress()[0].getValue());
		}
		
		/**Definition of ZIM used, where second entry of field is declared as fax**/
		if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getXtn2_TelecommunicationUseCode() &&
				null!=stf.getPhone()[0].getXtn2_TelecommunicationUseCode().getValue() &&
				0<stf.getPhone()[0].getXtn2_TelecommunicationUseCode().getValue().length()){
			provider.setFax(stf.getPhone()[0].getXtn2_TelecommunicationUseCode().getValue());
		}
		
		if(0<stf.getStf3_StaffName().length &&
				null!=stf.getStf3_StaffName()[0].getGivenName() &&
				null!=stf.getStf3_StaffName()[0].getGivenName().getValue() &&
				0<stf.getStf3_StaffName()[0].getGivenName().getValue().length()){
			provider.setFirstName(stf.getStf3_StaffName()[0].getGivenName().toString());
		}
		
		if(null!=stf.getStaffType() &&
				0<stf.getStaffTypeReps()){
			String specialisation = stf.getStaffType()[0].getValue();
			
			for(int i=1;i<stf.getStaffTypeReps();i++){
				specialisation = specialisation + "~"+stf.getStaffType()[i].getValue();
			}
			provider.setSpecialisation(specialisation);
		}	
		
		if(null!=stf.getAdministrativeSex() &&
				null!=stf.getAdministrativeSex().getValue() &&
				0<stf.getAdministrativeSex().getValue().length()){
			provider.setGenderCode(stf.getStf5_AdministrativeSex().getValue().toLowerCase());
		}
		
		if(0<stf.getStf2_StaffIdentifierList().length &&
				null!=stf.getStf2_StaffIdentifierList()[0].getIDNumber() &&
				null!=stf.getStf2_StaffIdentifierList()[0].getIDNumber().getValue() &&
				0<stf.getStf2_StaffIdentifierList()[0].getIDNumber().getValue().length()){
			provider.setLanr(stf.getStf2_StaffIdentifierList()[0].getIDNumber().getValue());
		}
		if(0<stf.getStf3_StaffName().length &&
				null!=stf.getStf3_StaffName()[0].getXpn1_FamilyName() &&
				null!=stf.getStf3_StaffName()[0].getXpn1_FamilyName().getFn1_Surname() &&
				null!=stf.getStf3_StaffName()[0].getXpn1_FamilyName().getFn1_Surname().getValue() &&
				0<stf.getStf3_StaffName()[0].getXpn1_FamilyName().getFn1_Surname().getValue().length()){
			provider.setLastName(stf.getStf3_StaffName()[0].getXpn1_FamilyName().getFn1_Surname().toString());
		}
		
		if(0<stf.getStf3_StaffName().length &&
				null!=stf.getStf3_StaffName()[0].getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof() &&
				null!=stf.getStf3_StaffName()[0].getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof().getValue() &&
				0<stf.getStf3_StaffName()[0].getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof().getValue().length()){
			provider.setMiddleName(stf.getStf3_StaffName()[0].getSecondAndFurtherGivenNamesOrInitialsThereof().getValue());
		}
		
		if(0<stf.getStf3_StaffName().length &&
				null!=stf.getStf3_StaffName()[0].getXpn5_PrefixEgDR() &&
				null!=stf.getStf3_StaffName()[0].getXpn5_PrefixEgDR().getValue() &&
				0<stf.getStf3_StaffName()[0].getXpn5_PrefixEgDR().getValue().length()){
			provider.setNamePrefix(stf.getStf3_StaffName()[0].getXpn5_PrefixEgDR().getValue());
		}
		
		if(0<stf.getStf3_StaffName().length &&
				null!=stf.getStf3_StaffName()[0].getXpn4_SuffixEgJRorIII().getValue() &&
				0<stf.getStf3_StaffName()[0].getXpn4_SuffixEgJRorIII().getValue().length()){
			provider.setNameSuffix(stf.getStf3_StaffName()[0].getXpn4_SuffixEgJRorIII().getValue());
		}
		if(null!=stf.getPrimaryKeyValueSTF() &&
				null!=stf.getPrimaryKeyValueSTF().getCe2_Text() &&
				null!=stf.getPrimaryKeyValueSTF().getCe2_Text().getValue() &&
				0<stf.getPrimaryKeyValueSTF().getCe2_Text().getValue().length()){
			provider.setId(new Long(stf.getPrimaryKeyValueSTF().getCe2_Text().getValue()).longValue());
		}
		
		if(null!=stf.getPrimaryKeyValueSTF() &&
				null!=stf.getPrimaryKeyValueSTF().getCe1_Identifier() &&
				null!=stf.getPrimaryKeyValueSTF().getCe1_Identifier().getValue() &&
				0<stf.getPrimaryKeyValueSTF().getCe1_Identifier().getValue().length()){
			LocalId localId = new LocalId();
			localId.setApplication(sendingApplication);
			localId.setFacility(sendingFacility);
			localId.setLocalId(stf.getPrimaryKeyValueSTF().getCe1_Identifier().getValue());
			ArrayList<LocalId> localIds = new ArrayList<LocalId>();
			localIds.add(localId);
			provider.setLocalIds(localIds);
			logger.info(provider.getLocalIds().get(0).toString()+" added");
		}
		
		if(0<stf.getHospitalServiceSTF().length){
			List<Organisation> olist = new ArrayList<Organisation>();
			Organisation organisation;
			for(int i=0;i<stf.getHospitalServiceSTF().length;i++){
				organisation = new Organisation();
				boolean organisationHasData =false;
				
				if(null!=stf.getHospitalServiceSTF()[i].getCe4_AlternateIdentifier() &&
					null!=stf.getHospitalServiceSTF()[i].getCe4_AlternateIdentifier().getValue() &&
					0<stf.getHospitalServiceSTF()[i].getCe4_AlternateIdentifier().getValue().length()){
					organisation.setSecondName(stf.getHospitalServiceSTF()[i].getCe4_AlternateIdentifier().getValue());
				}
				if(null!=stf.getHospitalServiceSTF()[i].getCe3_NameOfCodingSystem() &&
					null!=stf.getHospitalServiceSTF()[i].getCe3_NameOfCodingSystem().getValue() &&
					0<stf.getHospitalServiceSTF()[i].getCe3_NameOfCodingSystem().getValue().length()){
					organisationHasData = true;
					organisation.setName(stf.getHospitalServiceSTF()[i].getCe3_NameOfCodingSystem().getValue());
				}
				if(null!=stf.getHospitalServiceSTF()[i].getCe1_Identifier() &&
						null!=stf.getHospitalServiceSTF()[i].getCe1_Identifier().getValue() &&
						0<stf.getHospitalServiceSTF()[i].getCe1_Identifier().getValue().length()){
						LocalId localId = new LocalId();
						localId.setApplication(sendingApplication);
						localId.setFacility(sendingFacility);
						localId.setLocalId(stf.getHospitalServiceSTF()[i].getCe1_Identifier().getValue());
						ArrayList<LocalId> localIds = new ArrayList<LocalId>();
						localIds.add(localId);
						organisation.setLocalIds(localIds);
						organisationHasData = true;
				}
				if(null!=stf.getHospitalServiceSTF()[i].getCe2_Text() &&
						null!=stf.getHospitalServiceSTF()[i].getCe2_Text().getValue() &&
						0<stf.getHospitalServiceSTF()[i].getCe2_Text().getValue().length()){
						organisationHasData = true;
						organisation.setEstablishmentId(stf.getHospitalServiceSTF()[i].getCe2_Text().getValue());
				}
				if(organisationHasData){
					olist.add(organisation);
				}
			}
			provider.setOrganisations(olist);
		}
			
		if(0<stf.getInstitutionActivationDate().length &&
				null!=stf.getInstitutionActivationDate()[0].getDate() &&
				null!=stf.getInstitutionActivationDate()[0].getDate().getTime() &&
				null!=stf.getInstitutionActivationDate()[0].getDate().getTime().getValue() &&
				0<stf.getInstitutionActivationDate()[0].getDate().getTime().getValue().length()){
			try {
				provider.setReactivationDate(dateFormat.parse(stf.getInstitutionActivationDate()[0].getDate().getTime().getValue()));
			} catch (ParseException e) {
				throw new DataTypeException("Wrong format of reactivation date: " + e.getMessage());
			}
		}
		
		if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getTelephoneNumber() &&
				null!=stf.getPhone()[0].getTelephoneNumber().getValue() &&
				0<stf.getPhone()[0].getTelephoneNumber().getValue().length()){
			provider.setTelephone(stf.getPhone()[0].getTelephoneNumber().getValue());
		}
		
		logger.info("hl7 data converted into Provider Object");		
		return provider;
	}
	/**
	 * This method assembles an provider from a QPD Segment, which is part of a HL7 query message.
	 * The provider can then be used for creating SearchCriterias
	 * @param qpd the QPD segment
	 * @param sendingFacility 
	 * @param sendingApplication
	 * @return Provider for further processing
	 * @throws NumberFormatException if Fields which are required to be Numbers are in a different Format.
	 * @throws HL7Exception if an error occurs while parsing
	 */
	Provider assembleProvider(QPD qpd, String sendingFacility, String sendingApplication) throws NumberFormatException, HL7Exception{
		Provider provider = new Provider();
		if(null!=qpd.getField(3, 0) && 0<qpd.getField(3, 0).encode().length()){
			String field3 = qpd.getField(3, 0).encode();
			
			String[] fields = field3.split("\\~");
			
			for(int i=0; i<fields.length; i++){
				String field = fields[i];
				
				String[] parameter = field.split("\\^");
				String parameter1 = parameter[0];
				String parameter2 = parameter[1];
				
				if(parameter1.equals("@STF.3.1")){
					if(parameter2.equals("*")){
						provider.setLastName("\\[A TO Z\\]");
					}
					else{
						provider.setLastName(parameter2);
					}
				}else if(parameter1.equals("@STF.3.2")){
					if(parameter2.equals("*")){						
						provider.setFirstName("\\[A TO Z\\]");
					}
					else{
						provider.setFirstName(parameter2);
					}
				}else if(parameter1.equals("@STF.2")){
						provider.setLanr(parameter2);
				}
			}
			
			
//			LocalId localId = new LocalId();
//			
//			if(field5.contains("^")){
//				logger.info("assemble Provider: LocalId and Id set");
//				if(0<field5.substring(0,field5.indexOf("^")).length()){
//					localId.setLocalId(field5.substring(0,field5.indexOf("^")));
//				}
//				if(0<field5.substring(field5.substring(0,field5.indexOf("^")).length()+1).length()){
//					provider.setId(new Long(field5.substring(field5.substring(0,field5.indexOf("^")).length()+1)));
//				}
//			}else{
//				logger.info("assemble Provider: Id set");
//				localId.setLocalId(field5);
//			}
//
//			localId.setFacility(sendingFacility);
//			localId.setApplication(sendingApplication);
//			ArrayList<LocalId> localIds = new ArrayList<LocalId>();
//			localIds.add(localId);
//			provider.setLocalIds(localIds);
//		}
//		// LANR
//		if(null!=qpd.getField(6, 0) && 0<qpd.getField(6, 0).encode().length()){
//			String field6 = qpd.getField(6, 0).encode();
//			logger.info("assemble Provider: LANr set");
//			provider.setLanr(field6);
//		}
//		//STAFFNAME
//		if(null!=qpd.getField(7, 0) && 0<qpd.getField(7, 0).encode().length()){
//			String field7 = qpd.getField(7, 0).encode();
//			logger.info("assemble Provider: staffname field:"+field7);
//			String[] values = field7.split("\\^");
//			if(values.length>0 && values[0].length()>0){
//				logger.info("assemble Provider: lastname set:"+values[0]);
//				provider.setLastName(values[0]);
//			}
//			if(values.length>1 && values[1].length()>0){
//				logger.info("assemble Provider: firstname set:"+values[1]);
//				provider.setFirstName(values[1]);
//			}
//			if(values.length>2 && values[2].length()>0){
//				logger.info("assemble Provider: middlename set");
//				provider.setMiddleName(values[2]);
//			}
//			if(values.length>3 && values[3].length()>0){
//				provider.setNameSuffix(values[3]);
//			}
//			if(values.length>4 && values[4].length()>0){
//				provider.setNamePrefix(values[4]);
//			}
//		}
//		//Staff Type
//		if(null!=qpd.getField(8, 0) && 0<qpd.getField(8, 0).encode().length()){
//			String field8 = qpd.getField(8, 0).encode();
//			logger.info("assemble Provider: specialisation set");
//			provider.setSpecialisation(field8);
//		}
//		//Gender
//		if(null!=qpd.getField(9, 0) && 0<qpd.getField(9, 0).encode().length()){
//			String field9 = qpd.getField(9, 0).encode();
//			logger.info("assemble Provider: gendercode set");
//			provider.setGenderCode(field9.toLowerCase());
//		}
//		//Birthday
//		if(null!=qpd.getField(10, 0) && 0<qpd.getField(10, 0).encode().length()){
//			String field10 = qpd.getField(10, 0).encode();
//			try {
//				provider.setBirthday(dateFormat.parse(field10));
//				logger.info("assemble Provider: birthday set");
//			} catch (ParseException e) {
//				logger.info("assemble Provider: Error while parsing birthday");
//			}
//		}
//		//Contact
//		if(null!=qpd.getField(14, 0) && 0<qpd.getField(14, 0).encode().length()){
//			String field14 = qpd.getField(14, 0).encode();
//			String[] values14 = field14.split("\\^");
//			if(values14.length>0 && values14[0].length()>0){
//				logger.info("assemble Provider: tel set");
//				provider.setTelephone(values14[0]);
//			}
//			if(values14.length>1 && values14[1].length()>0){
//				logger.info("assemble Provider: fax set");
//				provider.setFax(values14[1]);
//			}
//			if(values14.length>2 && values14[2].length()>0){
//				logger.info("assemble Provider: email set");
//				provider.setEmail(values14[2]);
//			}
//		}
//		//Activation Date
//		if(null!=qpd.getField(16, 0) && 0<qpd.getField(16, 0).encode().length()){
//			String field16 = qpd.getField(16, 0).encode();
//			try {
//				provider.setReactivationDate(dateFormat.parse(field16));
//				logger.info("assemble Provider: activationdate set");
//			} catch (ParseException e) {
//				logger.info("assemble Provider: Error while parsing reactivationdate");
//				}
//		}
//		//Deactivation Date
//		if(null!=qpd.getField(17, 0) && 0<qpd.getField(17, 0).encode().length()){
//			String field17 = qpd.getField(17, 0).encode();
//			try {
//				provider.setDeactivationDate(dateFormat.parse(field17));
//				logger.info("assemble Provider: deactivationdate set");
//			} catch (ParseException e) {
//				logger.info("assemble Provider: Error while parsing deactivationdate");
//				}
//		}
//		//Inactivation Reason Code
//		if(null!=qpd.getField(19, 0) && 0<qpd.getField(19, 0).encode().length()){
//			String field19 = qpd.getField(19,0).encode();
//			provider.setDeactivationReasonCode(field19);
//			logger.info("assemble Provider: deactivationreason set");
//		}
//		//Address
//		if(null!=qpd.getField(15, 0) && 0<qpd.getField(15, 0).encode().length()){
//			Type[] field15 = qpd.getField(15);
//			XAD[] xad = new XAD[field15.length];
//			//Parser
//			for(int i=0;i<xad.length;i++){
//				xad[i].parse(field15[i].encode());
//			}
//			provider.setAddresses(this.assembleAddresses(xad));
//			logger.info("assemble Provider: addresses set");
//		}
//		//Organisations
//		if(null!=qpd.getField(13, 0) && 0<qpd.getField(13, 0).encode().length()){
//			Type[] field13 = qpd.getField(13);
//			
//			List<Organisation> olist = new ArrayList<Organisation>();
//			Organisation organisation;
//			for(int i=0;i<field13.length;i++){
//				organisation = new Organisation();
//				boolean organisationHasData =false;
//				String[] values = field13[i].encode().split("\\^");
//				if(values.length>3 && values[3].length()>0){
//					logger.info("assemble Provider: organisationname2 set");
//					organisation.setSecondName(values[3]);
//				}
//				
//				if(values.length>2 && values[2].length()>0){
//					organisationHasData = true;
//					organisation.setName(values[2]);
//					logger.info("assemble Provider: organisationname set");
//				}
//				
//				if(values.length>0 && values[0].length()>0){
//						LocalId localId = new LocalId();
//						localId.setApplication(sendingApplication);
//						localId.setFacility(sendingFacility);
//						localId.setLocalId(values[0]);
//						ArrayList<LocalId> localIds = new ArrayList<LocalId>();
//						localIds.add(localId);
//						organisation.setLocalIds(localIds);
//						organisationHasData = true;
//						logger.info("assemble Provider: organisationlocalId set");
//				}
//				if(values.length>1 && values[1].length()>0){
//						organisationHasData = true;
//						organisation.setEstablishmentId(values[1]);
//						logger.info("assemble Provider: organisationestablishmentId set");
//				}
//				if(organisationHasData){
//					olist.add(organisation);
//				}
//			}
//			provider.setOrganisations(olist);
		}
		
		return provider;
				
	}
	
	/**
	 * Assembles an organisation out of a hl7 STF Messsage segment
	 * @param stf the hl7 Message Segment
	 * @return Organisation the assembled organisation
	 * @throws DataTypeException
	 */
	Organisation assembleOrganisation(STF stf, String sendingFacility, String sendingApplication) throws DataTypeException{
		Organisation organisation = new Organisation();
		
		List<Address> addresses = assembleAddresses(stf.getStf11_OfficeHomeAddressBirthplace());
		
		if(null!=addresses && 
				addresses.size()>0){
			organisation.setAddresses(addresses);
		}
		
		if(0<stf.getStf13_InstitutionInactivationDate().length &&
				null!=stf.getStf13_InstitutionInactivationDate()[0].getDate() &&
				null!=stf.getStf13_InstitutionInactivationDate()[0].getDate().getTime() &&
				null!=stf.getStf13_InstitutionInactivationDate()[0].getDate().getTime().getValue() &&
				0<stf.getStf13_InstitutionInactivationDate()[0].getDate().getTime().getValue().length()){
			try {
				organisation.setDeactivationDate(dateFormat.parse(stf.getInstitutionInactivationDate()[0].getDate().getTime().getValue()));
			} catch (ParseException e) {
				throw new DataTypeException("Wrong format of deactivation date: " + e.getMessage(),e.getCause());
			}
		}
		/** 
		 * Here the ZIM Standard is used, which means only Integer values
		 * 1 = died
		 * 2 = closed
		 * 3 = cheating
		 * 4 = other
		 **/
		if(null!=stf.getStf38_InactiveReasonCode() &&
				null!=stf.getStf38_InactiveReasonCode().getCwe1_Identifier() &&
				null!=stf.getStf38_InactiveReasonCode().getCwe1_Identifier().getValue() &&
				0<stf.getStf38_InactiveReasonCode().getCwe1_Identifier().getValue().length()){
			organisation.setDeactivationReasonCode(stf.getStf38_InactiveReasonCode().getCwe1_Identifier().getValue());
		}
		
		/**Getting the Email-Address, which can be in the phone-field (preferred) or
		 * in the Email field.
		 */
		if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType() &&
				null!=stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType().getValue() &&
				0<stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType().getValue().length()){
			organisation.setEmail(stf.getPhone()[0].getXtn3_TelecommunicationEquipmentType().getValue());
		}else if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getEmailAddress() &&
				null!=stf.getPhone()[0].getEmailAddress().getValue() &&
				0<stf.getPhone()[0].getEmailAddress().getValue().length()){
			organisation.setEmail(stf.getPhone()[0].getEmailAddress().getValue());
		}else if(0<stf.getEMailAddress().length &&
				null!=stf.getEMailAddress()[0].getValue() &&
				0<stf.getEMailAddress()[0].getValue().length()){
			organisation.setEmail(stf.getEMailAddress()[0].getValue());
		}
		//Establishment ID
		if(0<stf.getStf2_StaffIdentifierList().length &&
				null!=stf.getStf2_StaffIdentifierList()[0].getIDNumber() &&
				null!=stf.getStf2_StaffIdentifierList()[0].getIDNumber().getValue() &&
				0<stf.getStf2_StaffIdentifierList()[0].getIDNumber().getValue().length()){
			organisation.setEstablishmentId(stf.getStf2_StaffIdentifierList()[0].getIDNumber().getValue());
		}
		/**Definition of ZIM used, where second entry of field is declared as fax**/
		if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getXtn2_TelecommunicationUseCode() &&
				null!=stf.getPhone()[0].getXtn2_TelecommunicationUseCode().getValue() &&
				0<stf.getPhone()[0].getXtn2_TelecommunicationUseCode().getValue().length()){
			organisation.setFax(stf.getPhone()[0].getXtn2_TelecommunicationUseCode().getValue());
		}
		
		if(0<stf.getStf3_StaffName().length){
			if(null!=stf.getStf3_StaffName()[0].getFamilyName() &&
				null!=stf.getStf3_StaffName()[0].getFamilyName().getFn1_Surname() &&
				null!=stf.getStf3_StaffName()[0].getFamilyName().getFn1_Surname().getValue()&&
				0<stf.getStf3_StaffName()[0].getFamilyName().getFn1_Surname().getValue().length()){
			organisation.setName(stf.getStf3_StaffName()[0].getFamilyName().getFn1_Surname().getValue());
			}
			if(null!=stf.getStf3_StaffName()[0].getXpn2_GivenName() &&
					null!=stf.getStf3_StaffName()[0].getXpn2_GivenName().toString() &&
					0<stf.getStf3_StaffName()[0].getXpn2_GivenName().toString().length()){
				organisation.setSecondName(stf.getStf3_StaffName()[0].getXpn2_GivenName().toString());
			}
		}
		
		if(null!=stf.getPrimaryKeyValueSTF() &&
				null!=stf.getPrimaryKeyValueSTF().getCe1_Identifier() &&
				null!=stf.getPrimaryKeyValueSTF().getCe1_Identifier().getValue() &&
				0<stf.getPrimaryKeyValueSTF().getCe1_Identifier().getValue().length()){
			LocalId localId = new LocalId();
			localId.setApplication(sendingApplication);
			localId.setFacility(sendingFacility);
			localId.setLocalId(stf.getPrimaryKeyValueSTF().getCe1_Identifier().getValue());
			ArrayList<LocalId> localIds = new ArrayList<LocalId>();
			localIds.add(localId);
			organisation.setLocalIds(localIds);
		}
		
		if(null!=stf.getPrimaryKeyValueSTF() &&
				null!=stf.getPrimaryKeyValueSTF().getCe2_Text() &&
				null!=stf.getPrimaryKeyValueSTF().getCe2_Text().getValue() &&
				0<stf.getPrimaryKeyValueSTF().getCe2_Text().getValue().length()){
			organisation.setId(new Long(stf.getPrimaryKeyValueSTF().getCe2_Text().getValue()).longValue());
		}
		
		if(0<stf.getInstitutionActivationDate().length &&
				null!=stf.getInstitutionActivationDate()[0].getDate() &&
				null!=stf.getInstitutionActivationDate()[0].getDate().getTime() &&
				null!=stf.getInstitutionActivationDate()[0].getDate().getTime().getValue() &&
				0<stf.getInstitutionActivationDate()[0].getDate().getTime().getValue().length()){
			try {
				organisation.setReactivationDate(dateFormat.parse(stf.getInstitutionActivationDate()[0].getDate().getTime().getValue()));
			} catch (ParseException e) {
				throw new DataTypeException("Wrong format of reactivation date: " + e.getMessage());
			}
		}
	
		if(0<stf.getPhone().length &&
				null!=stf.getPhone()[0].getTelephoneNumber() &&
				null!=stf.getPhone()[0].getTelephoneNumber().getValue() &&
				0<stf.getPhone()[0].getTelephoneNumber().getValue().length()){
			organisation.setTelephone(stf.getPhone()[0].getTelephoneNumber().getValue());
		}
		
		logger.info("hl7 data converted into Organisation Object");
			
		return organisation;
		
	}
	/**
	 * This method assembles an organisation from a QPD Segment, which is part of a HL7 query message.
	 * The organisation can then be used for creating SearchCriterias
	 * @param qpd the QPD segment
	 * @param sendingFacility 
	 * @param sendingApplication
	 * @return Organisation for further processing
	 * @throws NumberFormatException if Fields which are required to be Numbers are in a different Format.
	 * @throws HL7Exception if an error occurs while parsing
	 */
	Organisation assembleOrganisation(QPD qpd, String sendingFacility, String sendingApplication) throws NumberFormatException, HL7Exception{
		Organisation organisation = new Organisation();
		if(null!=qpd.getField(3, 0) && 0<qpd.getField(3, 0).encode().length()){
			String field3 = qpd.getField(3, 0).encode();
			
			String[] fields = field3.split("\\~");
			
			for(int i=0; i<fields.length; i++){
				String field = fields[i];
				
				String[] parameter = field.split("\\^");
				String parameter1 = parameter[0];
				String parameter2 = parameter[1];
				
				if(parameter1.equals("@STF.3.1")){
					if(parameter2.equals("*")){
						organisation.setName("\\[A TO Z\\]");
					}
					else{
						organisation.setName(parameter2);
					}
				}else if(parameter1.equals("@STF.3.2")){
					if(parameter2.equals("*")){						
						organisation.setSecondName("\\[A TO Z\\]");
					}
					else{
						organisation.setSecondName(parameter2);
					}
				}
				/*else if(parameter1.equals("@STF.2")){
					organisation.setOid(parameter2);				
				}*/				
			}
		}
			
			
//		if(null!=qpd.getField(5, 0) && 0<qpd.getField(5, 0).encode().length()){
//			String field5 = qpd.getField(5, 0).encode();
//			LocalId localId = new LocalId();
//			
//			if(field5.contains("^")){
//				if(0<field5.substring(0,field5.indexOf("^")).length()){
//					localId.setLocalId(field5.substring(0,field5.indexOf("^")));
//				}
//				if(0<field5.substring(field5.substring(0,field5.indexOf("^")).length()+1).length()){
//					organisation.setId(new Long(field5.substring(field5.substring(0,field5.indexOf("^")).length()+1)));
//				}
//			}else{
//				localId.setLocalId(field5);
//			}
//
//			localId.setFacility(sendingFacility);
//			localId.setApplication(sendingApplication);
//			ArrayList<LocalId> localIds = new ArrayList<LocalId>();
//			localIds.add(localId);
//			organisation.setLocalIds(localIds);
//		}
//		// LANR
//		if(null!=qpd.getField(6, 0) && 0<qpd.getField(6, 0).encode().length()){
//			String field6 = qpd.getField(6, 0).encode();
//			organisation.setEstablishmentId(field6);
//		}
//		//STAFFNAME
//		if(null!=qpd.getField(7, 0) && 0<qpd.getField(7, 0).encode().length()){
//			String field7 = qpd.getField(7, 0).encode();
//			String[] values = field7.split("\\^");
//			if(values.length>0 && values[0].length()>0){
//				organisation.setName(values[0]);
//			}
//			if(values.length>1 && values[1].length()>0){
//				organisation.setSecondName(values[1]);
//			}
//		}
//		//Contact
//		if(null!=qpd.getField(14, 0) && 0<qpd.getField(14, 0).encode().length()){
//			String field14 = qpd.getField(14, 0).encode();
//			String[] values14 = field14.split("\\^");
//			if(values14.length>0 && values14[0].length()>0){
//				organisation.setTelephone(values14[0]);
//			}
//			if(values14.length>1 && values14[1].length()>0){
//				organisation.setFax(values14[1]);
//			}
//			if(values14.length>2 && values14[2].length()>0){
//				organisation.setEmail(values14[2]);
//			}
//		}
//		//Activation Date
//		if(null!=qpd.getField(16, 0) && 0<qpd.getField(16, 0).encode().length()){
//			String field16 = qpd.getField(16, 0).encode();
//			try {
//				organisation.setReactivationDate(dateFormat.parse(field16));
//			} catch (ParseException e) {
//				logger.info("assemble Provider: Error while parsing reactivationdate");
//			}
//		}
//		//Deactivation Date
//		if(null!=qpd.getField(17, 0) && 0<qpd.getField(17, 0).encode().length()){
//			String field17 = qpd.getField(17, 0).encode();
//			try {
//				organisation.setDeactivationDate(dateFormat.parse(field17));
//			} catch (ParseException e) {
//				logger.info("assemble Provider: Error while parsing deactivationdate");
//			}
//		}
//		//Inactivation Reason Code
//		if(null!=qpd.getField(19, 0) && 0<qpd.getField(19, 0).encode().length()){
//			String field19 = qpd.getField(19,0).encode();
//			organisation.setDeactivationReasonCode(field19);
//		}
//		//Address
//		if(null!=qpd.getField(15, 0) && 0<qpd.getField(15, 0).encode().length()){
//			Type[] field15 = qpd.getField(15);
//			XAD[] xad = new XAD[field15.length];
//			//Parser
//			for(int i=0;i<xad.length;i++){
//				xad[i].parse(field15[i].encode());
//			}
//			organisation.setAddresses(this.assembleAddresses(xad));
//		}
				
		return organisation;
		
	}
	
	/**
	 * Assembles one or more addresses from hl7 XAD segments which are part of a STF Message segment
	 * @param stf11addresses the XAD[] array
	 * @return List<Address> a List of addresses
	 * @throws DataTypeException 
	 */
	private List<Address> assembleAddresses(XAD[] stf11addresses) throws DataTypeException{
		if(null!=stf11addresses &&
				0<stf11addresses.length){
			List<Address> addresses = new ArrayList<Address>();
			Address address;
			for(int i=0;i<stf11addresses.length;i++){
				address = new Address();
				if(null!=stf11addresses[i].getXad1_StreetAddress()){
					String street = "";
					if(null!=stf11addresses[i].getXad1_StreetAddress().getSad1_StreetOrMailingAddress() &&
							null!=stf11addresses[i].getXad1_StreetAddress().getSad1_StreetOrMailingAddress().getValue()){
						street = street + stf11addresses[i].getXad1_StreetAddress().getSad1_StreetOrMailingAddress().getValue()+" ";
					}
					if(null!=stf11addresses[i].getXad1_StreetAddress().getSad2_StreetName() &&
							null!=stf11addresses[i].getXad1_StreetAddress().getSad2_StreetName().getValue()){
						street = street + stf11addresses[i].getXad1_StreetAddress().getSad2_StreetName().getValue();
					}
					address.setStreet(street);
					if(null!=stf11addresses[i].getXad1_StreetAddress().getSad3_DwellingNumber() &&
							null!=stf11addresses[i].getXad1_StreetAddress().getSad3_DwellingNumber().getValue()){
						address.setHouseNumber(stf11addresses[i].getXad1_StreetAddress().getSad3_DwellingNumber().getValue());
					}
					
				}
				if(null!=stf11addresses[i].getXad2_OtherDesignation() &&
						null!=stf11addresses[i].getXad2_OtherDesignation().getValue()){
					address.setAdditional(stf11addresses[i].getXad2_OtherDesignation().getValue());
				}
				if(null!=stf11addresses[i].getXad3_City() &&
						null!=stf11addresses[i].getXad3_City().getValue()){
					address.setCity(stf11addresses[i].getXad3_City().getValue());
				}
				if(null!=stf11addresses[i].getXad4_StateOrProvince() &&
						null!=stf11addresses[i].getXad4_StateOrProvince().getValue()){
					address.setState(stf11addresses[i].getStateOrProvince().getValue());
				}
				if(null!=stf11addresses[i].getXad5_ZipOrPostalCode()&&
						null!=stf11addresses[i].getXad5_ZipOrPostalCode().getValue()){
					address.setZipCode(stf11addresses[i].getXad5_ZipOrPostalCode().getValue());
				}
				if(null!=stf11addresses[i].getXad6_Country() &&
						null!=stf11addresses[i].getXad6_Country().getValue()){
					if(isValidCountry(stf11addresses[i].getXad6_Country().getValue())){
						address.setCountry(stf11addresses[i].getXad6_Country().getValue());
					}else{
						throw new DataTypeException("No valid ISO 639 country code");
					}
				}
				addresses.add(address);
			}
			return addresses;
		}
		return null;
	}
	/**
	 * Checks if the country code is a valid one
	 * @param countryCode the code from a message
	 * @return boolean , if the country code is valid
	 */
	private boolean isValidCountry(String countryCode) {
		  String []countries = Locale.getISOCountries();
		  for (String c : countries) {
		    if (countryCode.equals(c)){
		      return true;
		    }
		  }
		  return false;
		}
	/**
	 * Returns the business Partner Ids, which means the Ids of the Providers and
	 * the STF Segments generated from the Provider List
	 * @param proList
	 * @return String[] containing the BusinessPartnerIds and the STF Segments
	 */
	public String[] assembleSTFsFromProviders(List<ProviderDTO> proList){
		String stfs = "";
		String businessPartnerId = "";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat dTDOB=new SimpleDateFormat("yyyyMMdd");
		
		for(int i=0;i<proList.size();i++){
			if(i>0){
				stfs = stfs +"\r";
			}
			stfs = stfs +"STF";
			ProviderDTO provider = proList.get(i);
			stfs = stfs+FIELDSEPARATOR;
			if(null!=provider.getId()){	
				stfs = stfs+provider.getId()+VALUESEPARATOR+VALUESEPARATOR+serviceID+SUBFIELDINDICATOR+"ISO";
				/*if(null!=provider.getId()){
					stfs = stfs+provider.getId();
					if(businessPartnerId.length()>0){
						businessPartnerId = businessPartnerId + "^";
					}
					businessPartnerId = businessPartnerId + provider.getId();
				}*/
			}
			stfs = stfs+FIELDSEPARATOR;
			if(null!=provider.getLanr()){
				stfs = stfs+provider.getLanr();
			}
			stfs = stfs+FIELDSEPARATOR;
			if(null!=provider.getLastname()){
				stfs = stfs+provider.getLastname();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=provider.getFirstname()){
				stfs = stfs+provider.getFirstname();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=provider.getMiddleName()){
				stfs = stfs+provider.getMiddleName();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=provider.getNameSuffix()){
				stfs = stfs+provider.getNameSuffix();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=provider.getNamePrefix()){
				stfs = stfs+provider.getNamePrefix();
			}
			stfs = stfs + FIELDSEPARATOR;
			if(null!=provider.getSpecialisation()){
				stfs = stfs + provider.getSpecialisation();
			}
			stfs = stfs + FIELDSEPARATOR;
			if(null!=provider.getGenderCode()){
				stfs = stfs+provider.getGenderCode().toUpperCase();
			}
			
			stfs = stfs + FIELDSEPARATOR;
			if(null!=provider.getBirthday()){
				String birthdate=dTDOB.format(provider.getBirthday());
				stfs = stfs+birthdate;
			}
			stfs = stfs + FIELDSEPARATOR;
			if((null!=provider.getDeactivationDate() && null!=provider.getReactivationDate() &&
					provider.getDeactivationDate().after(provider.getReactivationDate())) ||
					(null!=provider.getDeactivationDate() && null== provider.getReactivationDate())){
				stfs = stfs+"I";
			}else{
				stfs = stfs+"A";
			}
			stfs = stfs + FIELDSEPARATOR + FIELDSEPARATOR;
//			if((null!=provider.getOrganisations() && 0<provider.getOrganisations().size())){
//				for(int k=0;k<provider.getOrganisations().size();k++){
//					if(k>0){
//						stfs = stfs + FIELDREPETITION;
//					}
//					//!!!
//					if(null!=provider.getOrganisations().get(k).getEstablishmentID()){
//						stfs = stfs + provider.getOrganisations().get(k).getEstablishmentID();
//					}
//					stfs=stfs+VALUESEPARATOR;
//					if(null!=provider.getOrganisations().get(k).getName()){
//						stfs = stfs + provider.getOrganisations().get(k).getName();
//					}
//					stfs=stfs+VALUESEPARATOR;
//					if(null!=provider.getOrganisations().get(k).getSecondName()){
//						stfs = stfs + provider.getOrganisations().get(k).getSecondName();
//					}
//				}
//			}
			stfs = stfs + FIELDSEPARATOR;
			if(null!=provider.getTelephone()){
				stfs = stfs + provider.getTelephone();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=provider.getFax()){
				stfs = stfs + provider.getFax();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=provider.getEmail()){
				stfs = stfs + provider.getEmail();
			}
			stfs = stfs + FIELDSEPARATOR;
			if((null!=provider.getAddresses() && 0<provider.getAddresses().size())){
				for(int k=0;k<provider.getAddresses().size();k++){
					if(k>0){
						stfs = stfs + FIELDREPETITION;
					}
					stfs = stfs + SUBFIELDINDICATOR;
					if(null!=provider.getAddresses().get(k).getStreet()){
						stfs = stfs + provider.getAddresses().get(k).getStreet();
					}
					stfs=stfs+SUBFIELDINDICATOR;
					if(null!=provider.getAddresses().get(k).getHouseNumber()){
						stfs = stfs + provider.getAddresses().get(k).getHouseNumber();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=provider.getAddresses().get(k).getAdditional()){
						stfs = stfs + provider.getAddresses().get(k).getAdditional();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=provider.getAddresses().get(k).getCity()){
						stfs = stfs + provider.getAddresses().get(k).getCity();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=provider.getAddresses().get(k).getState()){
						stfs = stfs + provider.getAddresses().get(k).getState();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=provider.getAddresses().get(k).getZipCode()){
						stfs = stfs + provider.getAddresses().get(k).getZipCode();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=provider.getAddresses().get(k).getCountry()){
						stfs = stfs + provider.getAddresses().get(k).getCountry();
					}
				}
			}
			
			stfs = stfs + FIELDSEPARATOR;
			if(null!=provider.getDeactivationDate()){
				String deactivationDate=dateFormat.format(provider.getDeactivationDate());
				stfs = stfs + deactivationDate ;
			}
			stfs = stfs + FIELDSEPARATOR;
			if(null!=provider.getReactivationDate()){
				String reactivationDate=dateFormat.format(provider.getReactivationDate());
				stfs = stfs + reactivationDate;
			}
			stfs = stfs + FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR+ FIELDSEPARATOR;
			if(null!=provider.getDeactivationReasonCode()){
				stfs = stfs + provider.getDeactivationReasonCode();
			}
		}
		String[] returnValues = {businessPartnerId, stfs};
		return returnValues;
	}
	/**
	 * Returns the business Partner Ids, which means the Ids of the Organisations and
	 * the STF Segments generated from the Organisation List
	 * @param proList
	 * @return String[] containing the BusinessPartnerIds and the STF Segments
	 */
	public String[] assembleSTFsFromOrganisations(List<OrganisationDTO> orgList){
		String stfs = "";
		String businessPartnerId = "";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		for(int i=0;i<orgList.size();i++){
			if(i>0){
				stfs = stfs +"\r";
			}
			stfs = stfs +"STF";
			OrganisationDTO organisation = orgList.get(i);
			stfs = stfs+FIELDSEPARATOR;
			
			if(null!=organisation.getId()){
				stfs = stfs+organisation.getId()+VALUESEPARATOR+VALUESEPARATOR+serviceID+SUBFIELDINDICATOR+"ISO";
				/*stfs = stfs+organisation.getId();
				if(businessPartnerId.length()>0){
					businessPartnerId = businessPartnerId + "^";
				}
				businessPartnerId = businessPartnerId + organisation.getId();
				*/
			}
			stfs = stfs+FIELDSEPARATOR;
			if(null!=organisation.getEstablishmentID()){
				stfs = stfs+organisation.getEstablishmentID();
			}
			stfs = stfs+FIELDSEPARATOR;
			if(null!=organisation.getName()){
				stfs = stfs+organisation.getName();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=organisation.getSecondName()){
				stfs = stfs+organisation.getSecondName();
			}
			
			stfs = stfs + FIELDSEPARATOR;
			stfs = stfs + FIELDSEPARATOR;
			stfs = stfs + FIELDSEPARATOR;
			stfs = stfs + FIELDSEPARATOR;
			
			if((null!=organisation.getDeactivationDate() && null!=organisation.getReactivationDate() &&
					organisation.getDeactivationDate().after(organisation.getReactivationDate())) ||
					(null!=organisation.getDeactivationDate() && null== organisation.getReactivationDate())){
				stfs = stfs+"I";
			}else{
				stfs = stfs+"A";
			}
			stfs = stfs + FIELDSEPARATOR;
			stfs = stfs + FIELDSEPARATOR;
			stfs = stfs + FIELDSEPARATOR;
			if(null!=organisation.getTelephone()){
				stfs = stfs + organisation.getTelephone();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=organisation.getFax()){
				stfs = stfs + organisation.getFax();
			}
			stfs = stfs + VALUESEPARATOR;
			if(null!=organisation.getEmail()){
				stfs = stfs + organisation.getEmail();
			}
			stfs = stfs + FIELDSEPARATOR;
			if((null!=organisation.getAddresses() && 0<organisation.getAddresses().size())){
				for(int k=0;k<organisation.getAddresses().size();k++){
					if(k>0){
						stfs = stfs + FIELDREPETITION;
					}
					stfs = stfs + SUBFIELDINDICATOR;
					if(null!=organisation.getAddresses().get(k).getStreet()){
						stfs = stfs + organisation.getAddresses().get(k).getStreet();
					}
					stfs=stfs+SUBFIELDINDICATOR;
					if(null!=organisation.getAddresses().get(k).getHouseNumber()){
						stfs = stfs + organisation.getAddresses().get(k).getHouseNumber();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=organisation.getAddresses().get(k).getAdditional()){
						stfs = stfs + organisation.getAddresses().get(k).getAdditional();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=organisation.getAddresses().get(k).getCity()){
						stfs = stfs + organisation.getAddresses().get(k).getCity();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=organisation.getAddresses().get(k).getState()){
						stfs = stfs + organisation.getAddresses().get(k).getState();
					}
					stfs = stfs + VALUESEPARATOR;
					if(null!=organisation.getAddresses().get(k).getZipCode()){
						stfs = stfs + organisation.getAddresses().get(k).getZipCode();
					}
					//stfs = stfs + VALUESEPARATOR;
					/*if(null!=organisation.getAddresses().get(k).getCountry()){
						stfs = stfs + organisation.getAddresses().get(k).getCountry();
					}*/
				}
			}
			stfs = stfs + FIELDSEPARATOR;
			if(null!=organisation.getDeactivationDate()){
				String deactivationDate=dateFormat.format(organisation.getDeactivationDate());
				stfs = stfs + deactivationDate ;
			}
			stfs = stfs + FIELDSEPARATOR;
			if(null!=organisation.getReactivationDate()){
				String reactivationDate=dateFormat.format(organisation.getReactivationDate());
				stfs = stfs + reactivationDate;
			}
			stfs = stfs + FIELDSEPARATOR+ FIELDSEPARATOR;
			if(null!=organisation.getDeactivationReasonCode()){
				stfs = stfs + organisation.getDeactivationReasonCode();
			}
		}
		String[] returnValues = {businessPartnerId, stfs};
		return returnValues;
	}

}
