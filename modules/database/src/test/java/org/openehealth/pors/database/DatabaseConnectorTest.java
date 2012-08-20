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
package org.openehealth.pors.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

import junit.framework.Assert;

import org.apache.lucene.LucenePackage;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.util.plugin.ResourceLoaderAware;
import org.hibernate.search.Search;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.pors.database.connector.IDatabaseConnector;
import org.openehealth.pors.database.dao.IDao;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.AddressLog;
import org.openehealth.pors.database.entities.DuplicateRecognition;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.LocalIdLog;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.PorsUser;
import org.openehealth.pors.database.entities.Provider;
import org.openehealth.pors.database.entities.ProviderLog;
import org.openehealth.pors.database.exception.DataException;
import org.openehealth.pors.database.exception.DatabaseException;
import org.openehealth.pors.database.util.EntityUtil;
import org.postgresql.Driver;

@RunWith(Arquillian.class)
public class DatabaseConnectorTest {
	protected static Provider insertProviderData001;
	protected final static String QUERY_INSERT_PROVIDER_001 = "INSERT INTO Provider (RegionalProviderId, PorsUserId, LANR, OID, FirstName, LastName, MiddleName, NamePrefix, NameSuffix, GenderCode, Birthday, Email, Telephone, Fax, DeactivationDate, DeactivationReasonCode, ReactivationDate, ReactivationReasonCode, LastUpdateDate) VALUES (1, 3, 'TestNo001', 'Test.Provider.001', 'Heino', 'Testmensch', 'Julius', 'Dr.', 'Jr.', 'm', '1975-12-12', 'test@test.de', '01234-123456', '01234-123457', NULL, NULL, NULL, NULL, '2010-11-23 11:12:56.034')";
	protected final static String QUERY_SELECT_INSERT_PROVIDER_001 = "SELECT RegionalProviderId, PorsUserId, LANR, OID, FirstName, LastName, MiddleName, NamePrefix, NameSuffix, GenderCode, Birthday, Email, Telephone, Fax, DeactivationDate, DeactivationReasonCode, ReactivationDate, ReactivationReasonCode, LastUpdateDate FROM Provider WHERE RegionalProviderId = 1";
	protected final static String QUERY_SELECT_INSERT_PROVIDER_001_LOG = "SELECT ProviderLogId, PorsUserId, LogTime, SessionId, IPAddress, TriggerType, TableName, RegionalProviderId, OldPorsUserId, OldLANR, OldOID, OldFirstName, OldLastName, OldMiddleName, OldNamePrefix, OldNameSuffix, OldGenderCode, OldBirthday, OldEmail, OldTelephone, OldFax, OldDeactivationDate, OldDeactivationReasonCode, OldReactivationDate, OldReactivationReasonCode, OldLastUpdateDate, NewPorsUserId, NewLANR, NewOID, NewFirstName, NewLastName, NewMiddleName, NewNamePrefix, NewNameSuffix, NewGenderCode, NewBirthday, NewEmail, NewTelephone, NewFax, NewDeactivationDate, NewDeactivationReasonCode, NewReactivationDate, NewReactivationReasonCode, NewLastUpdateDate FROM ProviderLog WHERE RegionalProviderId = 1";
	
	protected static Provider insertProviderData002;
	protected final static String QUERY_INSERT_PROVIDER_002 = "INSERT INTO Provider (RegionalProviderId, PorsUserId, LANR, OID, FirstName, LastName, MiddleName, NamePrefix, NameSuffix, GenderCode, Birthday, Email, Telephone, Fax, DeactivationDate, DeactivationReasonCode, ReactivationDate, ReactivationReasonCode, LastUpdateDate) VALUES (2, 3, 'TestNo002', 'Test.Provider.002', 'Berta', 'Testmensch', 'Marlene', 'Frau', NULL, 'f', NULL, 'berta@test.de', '01234-123456', '01234-123457', '2010-09-05', 'XZ-5', NULL, NULL, '2010-11-23 11:12:56.034')";
	
	/**
	 * Provider log entry of insertProviderData001
	 */
	protected static ProviderLog insertProviderLogData001;
	protected final static String QUERY_INSERT_INSERT_PROVIDER_LOG_001 = "INSERT INTO ProviderLog ()";
	
	/**
	 * Simple organisation
	 */
	protected static Organisation insertOrganisationData001;
	protected final static String QUERY_INSERT_ORGANISATION_001 = "INSERT INTO Organisation (RegionalOrganisationId, PorsUserId, OID, EstablishmentId, Name, SecondName, Description, Email, Telephone, Fax, LastUpdateDate) VALUES (1, 3, 'Test.Organisation.001', 'Test-Establishment-Organisation-001', 'Uni HD Organisation No. 001', 'Uni HD Orga 001', 'A data set for testing purposes', 'test001@hd.de', '12345-12234', '0234-234562', '2010-11-23 11:12:56.034')";
	
	/**
	 * Local id referencing insertProviderData001
	 */
	protected static LocalId insertLocalIdData001;
	protected final static String QUERY_INSERT_LOCALID_001 = "INSERT INTO LocalId (LocalIdId, RegionalOrganisationId, RegionalProviderId, LocalId, Facility, Application) VALUES (1, NULL, 1, 'LocalId001', 'UniHD001', 'UniHDSys001')";
	protected final static String QUERY_SELECT_INSERT_LOCALID_001 = "SELECT LocalIdId, RegionalOrganisationId, RegionalProviderId, LocalId, Facility, Application FROM LocalId WHERE LocalIdId = 1";
	
	/**
	 * Local id referencing insertProviderData002
	 */
	protected static LocalId insertLocalIdData002;
	protected final static String QUERY_INSERT_LOCALID_002 = "INSERT INTO LocalId (LocalIdId, RegionalOrganisationId, RegionalProviderId, LocalId, Facility, Application) VALUES (2, NULL, 2, 'LocalId002', 'UniHD002', 'UniHDSys002')";
	
	/**
	 * Local id referencing insertProviderData001
	 */
	protected static LocalId insertLocalIdData003;
	protected final static String QUERY_INSERT_LOCALID_003 = "INSERT INTO LocalId (LocalIdId, RegionalOrganisationId, RegionalProviderId, LocalId, Facility, Application) VALUES (3, NULL, 1, 'LocalId003', 'UniHD003', 'UniHDSys003')";
	
	/**
	 * Local id referencing insertOrganisationData001
	 */
	protected static LocalId insertLocalIdData004;
	protected final static String QUERY_INSERT_LOCALID_004 = "INSERT INTO LocalId (LocalIdId, RegionalOrganisationId, RegionalProviderId, LocalId, Facility, Application) VALUES (4, 1, NULL, 'LocalId004', 'UniHD004', 'UniHDSys004')";
	
	/**
	 * Local id logging entry for insertLocalIdData001
	 */
	protected static LocalIdLog insertLocalIdLogData001;
	protected final static String QUERY_INSERT_INSERT_LOCALID_LOG = "INSERT INTO LocalIdLog (LocalIdLogId, PorsUserId, LogTime, SessionId, IPAddress, TriggerType, TableName, LocalIdId, OldRegionalOrganisationId, OldLocalId, OldFacility, OldApplication, NewRegionalOrganisationId, NewRegionalProviderId, NewLocalId, NewFacility, NewApplication) VALUES (1, 3, '2010-11-23 11:12:56.034', 'Sess-Test-Address-Log-001', '127.0.0.1', 'Insert', 'LocalId', 1, NULL, NULL, NULL, NULL, NULL, 1, 'LocalId001', 'UniHD001', 'UniHDSys001')";
	
	/**
	 * Simple address in database where all fields are set
	 */
	protected static Address insertAddressData001;
	protected final static String QUERY_INSERT_ADDRESS_001 = "INSERT INTO Address (AddressId, Additional, Street, HouseNumber, ZipCode, City, State, Country) VALUES (1, 'Postfach 205', 'Wilhelmsgasse', '5', '12345', 'Berlin', 'Berlin', 'DE')";
	protected final static String QUERY_IS_SET_INSERT_ADDRESS_001 = "SELECT COUNT(*) FROM Address WHERE AddressId = 1";
	
	/**
	 * Simple address in database where field state is null
	 */
	protected static Address insertAddressData002;
	protected final static String QUERY_INSERT_ADDRESS_002 = "INSERT INTO Address (AddressId, Additional, Street, HouseNumber, ZipCode, City, State, Country) VALUES (2, 'Postfach 111', 'Meierstraße', '2b', '12345', 'Berlin', NULL, 'DE')";
	protected final static String QUERY_IS_SET_INSERT_ADDRESS_002 = "SELECT COUNT(*) FROM Address WHERE Additional = 'Postfach 111' AND Street = 'Meierstraße' AND HouseNumber = '2b' AND ZipCode = '12345' AND City = 'Berlin' AND State IS NULL AND Country = 'DE'";
	
	/**
	 * Same content as fullAddress002 but having state set
	 */
	protected static Address insertAddressData003;
	protected final static String QUERY_INSERT_ADDRESS_003 = "INSERT INTO Address (AddressId, Additional, Street, HouseNumber, ZipCode, City, State, Country) VALUES (3, '', 'La rue', '234', '12645', 'Paris', 'Ile De France', 'FR')";
	protected final static String QUERY_IS_SET_INSERT_ADDRESS_003 = "SELECT COUNT(*) FROM Address WHERE additional = '' AND city = 'Paris' AND country = 'FR' AND houseNumber = '234' AND state = 'Ile De France' AND street = 'La rue' AND ZipCode = '12645'";
	
	/**
	 * Simple address in database where all fields are set
	 */
	protected static Address insertAddressData004;
	protected final static String QUERY_INSERT_ADDRESS_004 = "INSERT INTO Address (AddressId, Additional, HouseNumber, ZipCode, City, State, Country) VALUES (4, 'Postfach 333', 'Winandweg', '34', '44388', 'Dortmund', 'Nordrhein-Westfalen', 'DE')";
	protected final static String QUERY_IS_SET_INSERT_ADDRESS_004 = "SELECT COUNT(*) FROM Addresss WHERE Additional = 'Postfach 333' AND Street = 'Winandweg' AND HouseNumber = '34' AND ZipCode = '44388' AND City = 'Dortmund' AND State = 'Nordrhein-Westfalen' AND Country = 'DE'";
	
	/**
	 * History entry for insertAddressData001
	 */
	protected static AddressLog insertAddressLogData001;
	protected final static String QUERY_INSERT_INSERT_ADDRESS_LOG = "INSERT INTO AddressLog (AddressLogId, PorsUserId, LogTime, SessionId, IPAddress, TriggerType, TableName, AddressId, NewStreet, NewHouseNumber, NewZIPCode, NewCity, NewState, NewCountry, NewAdditional) VALUES (1, 3, '2010-11-23 11:12:56.034', 'Sess-Test-Address-Log-001', '127.0.0.1', 'Insert', 'Address', 1, 'Wilhelmsgasse', '5', '12345', 'Berlin', 'Berlin', 'DE', 'Postfach 205')";
	
	protected static Provider fullProvider001;
	protected final static String QUERY_IS_SET_FULL_PROVIDER_001 = "SELECT COUNT(*) FROM Provider WHERE birthday = '1981-07-23' AND porsUserId = 3 AND eMail = 'test@test.de' AND fax = '0123-456821' AND firstName = 'Maria' AND genderCode = 'f' AND LANR = 'FullPr001' AND lastName = 'Testfrau' AND OID = 'Test.Full.Provider.001' AND telephone = '03445-43547568'";
	protected final static String QUERY_SELECT_ID_FULL_PROVIDER_001 = "SELECT RegionalProviderId FROM Provider WHERE birthday = '1981-07-23' AND porsUserId = 3 AND eMail = 'test@test.de' AND fax = '0123-456821' AND firstName = 'Maria' AND genderCode = 'f' AND LANR = 'FullPr001' AND lastName = 'Testfrau' AND OID = 'Test.Full.Provider.001' AND telephone = '03445-43547568'";
	
	/**
	 * <p>
	 * Simple local id.
	 * </p>
	 */
	protected static LocalId fullLocalId001;
	protected final static String QUERY_IS_SET_FULL_LOCALID_001 = "SELECT COUNT(*) FROM LocalId WHERE LocalId = 'FullLid001' AND Facility = 'UniHD001' AND Application = 'UniHDSys001'";
	protected final static String QUERY_SELECT_ID_FULL_LOCALID_001 = "SELECT LocalIdId, RegionalProviderId, RegionalOrganisationId FROM LocalId WHERE LocalId = 'FullLid001' AND Facility = 'UniHD001' AND Application = 'UniHDSys001'";
	/**
	 * Simple address entity where all fields are set
	 */
	protected static Address fullAddress001;
	protected final static String QUERY_IS_SET_FULL_ADDRESS_001 = "SELECT COUNT(*) FROM Address WHERE additional = 'Postfach 101' AND city = 'Heidelberg' AND country = 'DE' AND houseNumber = '27a' AND state = 'Baden-Württemberg' AND street = 'Im Neuenheimer Feld' AND zipCode = '65547'";
	protected final static String QUERY_SELECT_ID_FULL_ADDRESS_001 = "SELECT AddressId FROM Address WHERE additional = 'Postfach 101' AND city = 'Heidelberg' AND country = 'DE' AND houseNumber = '27a' AND state = 'Baden-Württemberg' AND street = 'Im Neuenheimer Feld' AND zipCode = '65547'";
	
	/**
	 * Simple address entity where state is set to null
	 */
	protected static Address fullAddress002;
	protected final static String QUERY_IS_SET_FULL_ADDRESS_002 = "SELECT COUNT(*) FROM Address WHERE additional = '' AND city = 'Paris' AND country = 'FR' AND houseNumber = '234' AND state IS NULL AND street = 'La rue' AND ZipCode = '12645'";
	protected final static String QUERY_SELECT_ID_FULL_ADDRESS_002 = "SELECT AddressId FROM Address WHERE additional = '' AND city = 'Paris' AND country = 'FR' AND houseNumber = '234' AND state IS NULL AND street = 'La rue' AND ZipCode = '12645'";
	
	/**
	 * Same content as insertAddressData002, but having state set
	 */
	protected static Address fullAddress003;
	protected final static String QUERY_IS_SET_FULL_ADDRESS_003 = "SELECT COUNT(*) FROM Address WHERE Additional = 'Postfach 111' AND Street = 'Meierstraße' AND HouseNumber = '2b' AND ZipCode = '12345' AND City = 'Berlin' AND State = 'Hessen' AND Country = 'DE'";
	protected final static String QUERY_SELECT_ID_FULL_ADDRESS_003 = "SELECT AddressId FROM Address WHERE Additional = 'Postfach 111' AND Street = 'Meierstraße' AND HouseNumber = '2b' AND ZipCode = '12345' AND City = 'Berlin' AND State = 'Hessen' AND Country = 'DE'";
	
	protected final static String QUERY_COUNT_HISTORY_ENTRIES = "SELECT COUNT(*) FROM HistoryView";
	
	protected final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	protected final static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
	
	@Deployment
	public static EnterpriseArchive createTestArchive() {
		final JavaArchive ejbJar = ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addPackages(true, IDatabaseConnector.class.getPackage(), IDao.class.getPackage(), 
						Provider.class.getPackage(), DatabaseException.class.getPackage(), 
						EntityUtil.class.getPackage(), DatabaseConnectorTest.class.getPackage(),
						Driver.class.getPackage())
				.addPackages(true, StandardAnalyzer.class.getPackage(), Analyzer.class.getPackage(), 
						MultiFieldQueryParser.class.getPackage(), LucenePackage.class.getPackage(), 
						Search.class.getPackage(), LowerCaseFilterFactory.class.getPackage(),
						ResourceLoader.class.getPackage(), Dictionary.class.getPackage(), 
						ResourceLoaderAware.class.getPackage())
				.addManifestResource("persistence.xml",
						ArchivePaths.create("persistence.xml"))
				.addManifestResource("jndi.properties",
						ArchivePaths.create("jndi.properties"));
		return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
				.addModule(ejbJar);
	}
	
	private IDatabaseConnector connector;
	private InitialContext ctx;
	protected Connection con;
	
	@Before
	public void initTest() throws Exception
	{
		if (con == null)
		{
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://wega.ifi.uni-heidelberg.de:5432/ise1011_5";
			this.con = DriverManager.getConnection(url, "ise1011", "rel144tup");
			this.con.setAutoCommit(false); 
		}
		// Clean database
		Statement stmt1 = this.con.createStatement();
		stmt1.execute("SELECT setupdb()");

		this.con.commit();
		
		initTestEntities();
	}
	
	public void initTestEntities() throws Exception
	{
		if (insertProviderData001 == null)
		{
			PorsUser usr = new PorsUser();
			usr.setId(3);
			
			insertProviderData001 = new Provider();
			insertProviderData001.setId(1L);
			insertProviderData001.setUser(usr);
			insertProviderData001.setLanr("TestNo001");
			insertProviderData001.setOid("Test.Provider.001");
			insertProviderData001.setFirstName("Heino");
			insertProviderData001.setLastName("Testmensch");
			insertProviderData001.setNamePrefix("Dr.");
			insertProviderData001.setNameSuffix("Jr.");
			insertProviderData001.setGenderCode("m");
			insertProviderData001.setBirthday(DATE_FORMAT.parse("12.12.1975"));
			insertProviderData001.setEmail("test@test.de");
			insertProviderData001.setTelephone("01234-123456");
			insertProviderData001.setFax("01234-123457");
			insertProviderData001.setLastUpdateDate(DATETIME_FORMAT.parse("23.11.2010 11:12:56:034"));
		}
		
		if (insertProviderData002 == null)
		{
			PorsUser usr = new PorsUser();
			usr.setId(3);
			
			insertProviderData002 = new Provider();
			insertProviderData002.setBirthday(null);
			insertProviderData002.setId(2L);
			insertProviderData002.setUser(usr);
			insertProviderData002.setLanr("TestNo002");
			insertProviderData002.setOid("Test.Provider.002");
			insertProviderData002.setFirstName("Berta");
			insertProviderData002.setLastName("Testmensch");
			insertProviderData002.setMiddleName("Marlene");
			insertProviderData002.setNamePrefix("Frau");
			insertProviderData002.setGenderCode("f");
			insertProviderData002.setEmail("berta@test.de");
			insertProviderData002.setTelephone("01234-123456");
			insertProviderData002.setFax("01234-123457");
			insertProviderData002.setDeactivationDate(DATE_FORMAT.parse("05.09.2010"));
			insertProviderData002.setDeactivationReasonCode("XZ-5");
			insertProviderData002.setLastUpdateDate(DATETIME_FORMAT.parse("23.11.2010 11:12:56:034"));
		}
		
		if (insertOrganisationData001 == null)
		{
			PorsUser user = new PorsUser();
			user.setId(3);
			
			insertOrganisationData001 = new Organisation();
			insertOrganisationData001.setDescription("A data set for testing purposes");
			insertOrganisationData001.setEmail("test001@hd.de");
			insertOrganisationData001.setEstablishmentId("Test-Establishment-Organisation-001");
			insertOrganisationData001.setFax("0234-234562");
			insertOrganisationData001.setId(1L);
			insertOrganisationData001.setLastUpdateDate(DATETIME_FORMAT.parse("23.11.2010 11:12:56:034"));
			insertOrganisationData001.setName("Uni HD Organisation No. 001");
			insertOrganisationData001.setOid("Test.Organisation.001");
			insertOrganisationData001.setSecondName("Uni HD Orga 001");
			insertOrganisationData001.setTelephone("12345-12234");
			insertOrganisationData001.setUser(user);
		}
		
		if (insertLocalIdData001 == null)
		{
			Provider p = new Provider();
			p.setId(1L);
			
			insertLocalIdData001 = new LocalId();
			insertLocalIdData001.setId(1L);
			insertLocalIdData001.setApplication("UniHDSys001");
			insertLocalIdData001.setFacility("UniHD001");
			insertLocalIdData001.setLocalId("LocalId001");
			insertLocalIdData001.setProvider(p);
		}
		
		if (insertLocalIdData002 == null)
		{
			Provider p = new Provider();
			p.setId(2L);
			
			insertLocalIdData002 = new LocalId();
			insertLocalIdData002.setId(2L);
			insertLocalIdData002.setApplication("UniHDSys002");
			insertLocalIdData002.setFacility("UniHD002");
			insertLocalIdData002.setLocalId("LocalId002");
			insertLocalIdData002.setProvider(p);
		}
		
		if (insertLocalIdData003 == null)
		{
			Provider p = new Provider();
			p.setId(1L);
			
			insertLocalIdData003 = new LocalId();
			insertLocalIdData003.setId(3L);
			insertLocalIdData003.setApplication("UniHDSys003");
			insertLocalIdData003.setFacility("UniHD003");
			insertLocalIdData003.setLocalId("LocalId003");
			insertLocalIdData003.setProvider(p);
		}
		
		if (insertLocalIdData004 == null)
		{
			Organisation o = new Organisation();
			o.setId(1L);
			
			insertLocalIdData004 = new LocalId();
			insertLocalIdData004.setId(4L);
			insertLocalIdData004.setApplication("UniHDSys004");
			insertLocalIdData004.setFacility("UniHD004");
			insertLocalIdData004.setLocalId("LocalId004");
			insertLocalIdData004.setOrganisation(o);
		}
		
		if (insertLocalIdLogData001 == null)
		{
			PorsUser user = new PorsUser();
			user.setId(3);
			
			insertLocalIdLogData001 = new LocalIdLog();
			insertLocalIdLogData001.setId(1L);
			insertLocalIdLogData001.setUser(user);
			insertLocalIdLogData001.setLogTime(DATETIME_FORMAT.parse("23.11.2010 11:12:56:034"));
			insertLocalIdLogData001.setSessionId("Sess-Test-Address-Log-001");
			insertLocalIdLogData001.setIPAddress("127.0.0.1");
			insertLocalIdLogData001.setAction("Insert");
			insertLocalIdLogData001.setTableName("LocalId");
			insertLocalIdLogData001.setLocalIdId(1L);
			insertLocalIdLogData001.setNewRegionalProviderId(1L);
			insertLocalIdLogData001.setNewLocalId("LocalId001");
			insertLocalIdLogData001.setNewFacility("UniHD001");
			insertLocalIdLogData001.setNewApplication("UniHDSys001");
		}
		
		if (insertAddressData001 == null)
		{
			insertAddressData001 = new Address();
			insertAddressData001.setId(1L);
			insertAddressData001.setAdditional("Postfach 205");
			insertAddressData001.setStreet("Wilhelmsgasse");
			insertAddressData001.setHouseNumber("5");
			insertAddressData001.setZipCode("12345");
			insertAddressData001.setCity("Berlin");
			insertAddressData001.setState("Berlin");
			insertAddressData001.setCountry("DE");
		}
		
		if (insertAddressData002 == null)
		{
			insertAddressData002 = new Address();
			insertAddressData002.setId(2L);
			insertAddressData002.setAdditional("Postfach 111");
			insertAddressData002.setStreet("Meierstraße");
			insertAddressData002.setHouseNumber("2b");
			insertAddressData002.setZipCode("12345");
			insertAddressData002.setCity("Berlin");
			insertAddressData002.setState(null);
			insertAddressData002.setCountry("DE");
		}
		
		if (insertAddressData003 == null)
		{
			insertAddressData003 = new Address();
			insertAddressData003.setId(3L);
			insertAddressData003.setAdditional("");
			insertAddressData003.setCity("Paris");
			insertAddressData003.setCountry("FR");
			insertAddressData003.setHouseNumber("234");
			insertAddressData003.setState("Ile De France");
			insertAddressData003.setStreet("La rue");
			insertAddressData003.setZipCode("12645");
		}
		
		if (insertAddressData004 == null)
		{
			insertAddressData004 = new Address();
			insertAddressData004.setId(4L);
			insertAddressData004.setAdditional("Postfach 333");
			insertAddressData004.setStreet("Winandweg");
			insertAddressData004.setHouseNumber("34");
			insertAddressData004.setZipCode("44388");
			insertAddressData004.setCity("Dortmund");
			insertAddressData004.setState("Nordrhein-Westfalen");
			insertAddressData004.setCountry("DE");
		}
		
		if (insertAddressLogData001 == null)
		{
			//"INSERT INTO AddressLog (AddressLogId, PorsUserId, LogTime, SessionId, IPAddress, TriggerType, TableName, AddressId, NewStreet, NewHouseNumber, NewZIPCode, NewCity, NewState, NewCountry, NewAdditional) VALUES (1, 3, '2010-11-23 11:12:56.034', 'Sess-Test-Address-Log-001', '127.0.0.1', 'Insert', 'Address', 1, 'Wilhelmsgasse', '5', '12345', 'Berlin', 'Berlin', 'DE', 'Postfach 205')";
			PorsUser user = new PorsUser();
			user.setId(3);
			
			insertAddressLogData001 = new AddressLog();
			insertAddressLogData001.setId(1L);
			insertAddressLogData001.setUser(user);
			insertAddressLogData001.setLogTime(DATETIME_FORMAT.parse("23.11.2010 11:12:56:034"));
			insertAddressLogData001.setSessionId("Sess-Test-Address-Log-001");
			insertAddressLogData001.setIPAddress("127.0.0.1");
			insertAddressLogData001.setTriggerType("Insert");
			insertAddressLogData001.setTableName("Address");
			insertAddressLogData001.setAddressId(1L);
			insertAddressLogData001.setNewStreet("Wilhelmsgasse");
			insertAddressLogData001.setNewHouseNumber("5");
			insertAddressLogData001.setNewZipCode("12345");
			insertAddressLogData001.setNewCity("Berlin");
			insertAddressLogData001.setNewState("Berlin");
			insertAddressLogData001.setNewCountry("DE");
			insertAddressLogData001.setNewAdditional("Postfach 205");
		}
		
		if (fullProvider001 == null)
		{
			PorsUser usr = new PorsUser();
			usr.setId(3);
			
			fullProvider001 = new Provider();
			fullProvider001.setBirthday(DATE_FORMAT.parse("23.07.1981"));
			fullProvider001.setEditingUser(usr);
			fullProvider001.setEmail("test@test.de");
			fullProvider001.setFax("0123-456821");
			fullProvider001.setFirstName("Maria");
			fullProvider001.setGenderCode("f");
			fullProvider001.setIpAddress("127.0.0.1");
			fullProvider001.setLanr("FullPr001");
			fullProvider001.setLastName("Testfrau");
			fullProvider001.setOid("Test.Full.Provider.001");
			fullProvider001.setSessionId("test-full-prov-001");
			fullProvider001.setTelephone("03445-43547568");
			fullProvider001.setUser(usr);
		}
		
		if (fullLocalId001 == null)
		{
			fullLocalId001 = new LocalId();
			fullLocalId001.setLocalId("FullLid001");
			fullLocalId001.setFacility("UniHD001");
			fullLocalId001.setApplication("UniHDSys001");
		}
			
		
		if (fullAddress001 == null)
		{
			fullAddress001 = new Address();
			fullAddress001.setAdditional("Postfach 101");
			fullAddress001.setCity("Heidelberg");
			fullAddress001.setCountry("DE");
			fullAddress001.setHouseNumber("27a");
			fullAddress001.setState("Baden-Württemberg");
			fullAddress001.setStreet("Im Neuenheimer Feld");
			fullAddress001.setZipCode("65547");
		}
		
		if (fullAddress002 == null)
		{
			fullAddress002 = new Address();
			fullAddress002.setAdditional("");
			fullAddress002.setCity("Paris");
			fullAddress002.setCountry("FR");
			fullAddress002.setHouseNumber("234");
			fullAddress002.setState(null);
			fullAddress002.setStreet("La rue");
			fullAddress002.setZipCode("12645");
		}
		
		if (fullAddress003 == null)
		{
			fullAddress003 = new Address();
			fullAddress003.setAdditional("Postfach 111");
			fullAddress003.setStreet("Meierstraße");
			fullAddress003.setHouseNumber("2b");
			fullAddress003.setZipCode("12345");
			fullAddress003.setCity("Berlin");
			fullAddress003.setState("Hessen");
			fullAddress003.setCountry("DE");
		}
	}
	
	
	private void initConnector() throws Exception {
		if (ctx == null) {
			ctx = new InitialContext();
		}
		if (connector == null) {
			connector = (IDatabaseConnector) ctx.lookup("test/DatabaseConnectorBean/local");
		}
	}
	
	@Test
	public void testGetUser() throws Exception {
		initConnector();
		Assert.assertNotNull("Verify injection", connector);
		PorsUser qUser = new PorsUser();
		qUser.setName("Testuser");
		PorsUser user = connector.getUser(qUser);
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getName(), qUser.getName());
		Assert.assertNotNull(user.getId());
	}
	
	@Test
	public void testGetProviders() throws Exception {
		initConnector();
		// TODO: insert at least one provider...
		List<Provider> p = connector.getProviderList();
		Assert.assertNotNull(p);
	}
	
	@Test
	public void testUpdateDuplicateConfiguration() throws Exception {
		initConnector();
		List<DuplicateRecognition> settings = new ArrayList<DuplicateRecognition>();
		DuplicateRecognition d1 = new DuplicateRecognition();
		d1.setName("p.address");
		d1.setValue(9);
		DuplicateRecognition d2 = new DuplicateRecognition();
		d2.setName("p.oid");
		d2.setValue(1);
		settings.add(d1);
		settings.add(d2);
		connector.updateDuplicateConfiguration(settings);
		List<DuplicateRecognition> configuration = connector.getDuplicateConfiguration();
		Assert.assertNotNull(configuration);
		for (DuplicateRecognition entry : configuration) {
			if (entry.getName().equals("p.address")) {
				Assert.assertTrue(9 == entry.getValue().intValue());
			} else if (entry.getName().equals("p.oid")) {
				Assert.assertTrue(1 == entry.getValue().intValue());
			}
		}
	}
	
	@Test
	public void testAddProvider() throws Exception
	{
		this.initConnector();
		Provider p = new Provider();
		p.setBirthday(fullProvider001.getBirthday());
		p.setEditingUser(fullProvider001.getEditingUser());
		p.setEmail(fullProvider001.getEmail());
		p.setFax(fullProvider001.getFax());
		p.setFirstName(fullProvider001.getFirstName());
		p.setGenderCode(fullProvider001.getGenderCode());
		p.setIpAddress(fullProvider001.getIpAddress());
		p.setLanr(fullProvider001.getLanr());
		p.setLastName(fullProvider001.getLastName());
		p.setLastUpdateDate(fullProvider001.getLastUpdateDate());
		p.setOid(fullProvider001.getOid());
		p.setSessionId(fullProvider001.getSessionId());
		p.setTelephone(fullProvider001.getTelephone());
		p.setUser(fullProvider001.getUser());
		
		try 
		{
			this.connector.addProvider(p);
			
			this.con.setAutoCommit(true);
			
			Statement stmt1 = this.con.createStatement();
			Statement stmt2 = this.con.createStatement();
			
			ResultSet res1 = stmt1.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
			res1.next();
			int res1_val = res1.getInt(1);
			
			ResultSet res2 = stmt2.executeQuery(QUERY_COUNT_HISTORY_ENTRIES);
			res2.next();
			int res2_val = res2.getInt(1);
			
			this.con.setAutoCommit(false);
			
			assert(res1_val == 1 && res2_val == 1);
		} 
		catch (DatabaseException e) 
		{
			e.printStackTrace();
			Assert.fail("Could not add provider due to a database exception: " + e.getMessage());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Assert.fail("Could not check result.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Assert.fail("Could not add provider due to exception: " + e.getClass().getName());
		}
	}
	
	@Test
	public void testAddExistingProvider() throws Exception
	{
		this.initConnector();
		try
		{
			Statement stmt = this.con.createStatement();
			stmt.executeUpdate(QUERY_INSERT_PROVIDER_001);
			this.con.commit();
		}
		catch (SQLException e)
		{
			Assert.fail("Could not set up database.");
		}
		
		PorsUser user = new PorsUser();
		user.setId(3);
		
		Provider p = new Provider();
		p.setBirthday(insertProviderData001.getBirthday());
		p.setDeactivationDate(insertProviderData001.getDeactivationDate());
		p.setDeactivationReasonCode(insertProviderData001.getDeactivationReasonCode());
		p.setEditingUser(user);
		p.setEmail(insertProviderData001.getEmail());
		p.setFax(insertProviderData001.getFax());
		p.setFirstName(insertProviderData001.getFirstName());
		p.setGenderCode(insertProviderData001.getGenderCode());
		p.setIpAddress("127.0.0.1");
		p.setLanr(insertProviderData001.getLanr());
		p.setLastName(insertProviderData001.getLastName());
		p.setMiddleName(insertProviderData001.getMiddleName());
		p.setNamePrefix(insertProviderData001.getNamePrefix());
		p.setNameSuffix(insertProviderData001.getNameSuffix());
		p.setOid(insertProviderData001.getOid());
		p.setReactivationDate(insertProviderData001.getReactivationDate());
		p.setReactivationReasonCode(insertProviderData001.getReactivationReasonCode());
		p.setSessionId("Sess-Test-Insert-Double-Provider");
		p.setTelephone(insertProviderData001.getTelephone());
		p.setUser(user);
		
		try
		{
			this.connector.addProvider(p);
			Assert.fail("No exception thrown.");
		}
		catch(DatabaseException e)
		{
			Throwable cause = e.getCause();
			
			if (cause == null || !(cause instanceof DataException))
				Assert.fail("Wrong cause of database exception: " + ((cause == null) ? "null" : cause.getClass().getName()));
		}
	}
	
	/**
	 * <p>
	 * Adds a provider having a relation to an address having same content as 
	 * an already existing address, but not using its database id.
	 * </p>
	 * <p>
	 * Expected result: A relation between the already existing address and 
	 * the new provider will be created.
	 * </p>
	 * @throws Exception 
	 */
	@Test
	public void testAddProviderExistingAddress() throws Exception
	{
		this.initConnector();
		try
		{
			Statement stmt = this.con.createStatement();
			stmt.executeUpdate(QUERY_INSERT_ADDRESS_001);
			this.con.commit();
			
			Address a = new Address();
			a.setAdditional(insertAddressData001.getAdditional());
			a.setCity(insertAddressData001.getCity());
			a.setCountry(insertAddressData001.getCountry());
			a.setHouseNumber(insertAddressData001.getHouseNumber());
			a.setState(insertAddressData001.getState());
			a.setStreet(insertAddressData001.getStreet());
			a.setZipCode(insertAddressData001.getZipCode());
			
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(a);
			
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setLastUpdateDate(fullProvider001.getLastUpdateDate());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			p.setAddresses(addresses);
			
			this.connector.addProvider(p);
			
			this.con.setAutoCommit(true);
			Statement stmt1 = this.con.createStatement();
//			Statement stmt2 = this.con.createStatement();
			Statement stmt3 = this.con.createStatement();
			Statement stmt4 = this.con.createStatement();
			Statement stmt5 = this.con.createStatement();
			
			ResultSet res1 = stmt1.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
			res1.next();
			int res1_val = res1.getInt(1);
			
			if (res1_val != 1)
				Assert.fail("Provider was not inserted into database.");
			
//			ResultSet res2 = stmt2.executeQuery(QUERY_COUNT_HISTORY_ENTRIES);
//			res2.next();
//			int res2_val = res2.getInt(1);
			
			ResultSet res3 = stmt3.executeQuery(QUERY_IS_SET_INSERT_ADDRESS_001);
			res3.next();
			int res3_val = res3.getInt(1);
			
			ResultSet res4 = stmt4.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
			res4.next();
			long providerId = res4.getLong(1);
			
			ResultSet res5 = stmt5.executeQuery("SELECT COUNT(*) FROM ProviderHasAddress WHERE RegionalProviderId = " + String.valueOf(providerId) + " AND AddressId = " + String.valueOf(insertAddressData001.getId()));
			res5.next();
			int res5_val = res5.getInt(1);
			
			this.con.setAutoCommit(false);
			
			assert(res1_val == 1 /*&& res2_val == 3*/ && res3_val == 1 && res5_val == 1);
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
			Assert.fail("Could not add provider due to a database exception: " + e.getMessage());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Assert.fail("Could not execute SQL statements.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Assert.fail("Could not add provider due to an exception: " + e.getClass().getName());
		}
	}
	
	@Test
	public void testAddProviderExistingAddressDifferingState() throws Exception
	{
		this.initConnector();
		try
		{
			Statement stmt = this.con.createStatement();
			stmt.executeUpdate(QUERY_INSERT_ADDRESS_001);
			this.con.commit();
			
			Address a = new Address();
			a.setAdditional(insertAddressData001.getAdditional());
			a.setCity(insertAddressData001.getCity());
			a.setCountry(insertAddressData001.getCountry());
			a.setHouseNumber(insertAddressData001.getHouseNumber());
			a.setState(insertAddressData001.getState() + "-ABC");
			a.setStreet(insertAddressData001.getStreet());
			a.setZipCode(insertAddressData001.getZipCode());
			
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(a);
			
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			p.setAddresses(addresses);
			
			this.connector.addProvider(p);
			
			Assert.fail("No exception thrown.");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Assert.fail("Could not execute SQL statements.");
		}
		catch (DatabaseException e)
		{
			if (!(e.getCause() instanceof DataException))
				Assert.fail("Wrong exception thrown.");
		}
		catch (Exception e)
		{
			Assert.fail("Wrong exception thrown.");
		}
	}
	
	@Test
	public void testAddProviderExistingAddressEntityStateIsNull() throws Exception
	{
		this.initConnector();
		try
		{
			Statement stmt = this.con.createStatement();
			stmt.executeUpdate(QUERY_INSERT_ADDRESS_003);
			this.con.commit();
		}
		catch (SQLException e)
		{
			Assert.fail("Could not set up database.");
		}
		
		Address a1 = new Address();
		a1.setAdditional(fullAddress002.getAdditional());
		a1.setCity(fullAddress002.getCity());
		a1.setCountry(fullAddress002.getCountry());
		a1.setHouseNumber(fullAddress002.getHouseNumber());
		a1.setState(fullAddress002.getState());
		a1.setStreet(fullAddress002.getStreet());
		a1.setZipCode(fullAddress002.getZipCode());
		a1.setState(fullAddress002.getState());
		
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(a1);
		
		Provider p = new Provider();
		p.setBirthday(fullProvider001.getBirthday());
		p.setEditingUser(fullProvider001.getEditingUser());
		p.setEmail(fullProvider001.getEmail());
		p.setFax(fullProvider001.getFax());
		p.setFirstName(fullProvider001.getFirstName());
		p.setGenderCode(fullProvider001.getGenderCode());
		p.setIpAddress(fullProvider001.getIpAddress());
		p.setLanr(fullProvider001.getLanr());
		p.setLastName(fullProvider001.getLastName());
		p.setOid(fullProvider001.getOid());
		p.setSessionId(fullProvider001.getSessionId());
		p.setTelephone(fullProvider001.getTelephone());
		p.setUser(fullProvider001.getUser());
		p.setAddresses(addresses);
		
		try
		{	
			this.connector.addProvider(p);
			Assert.fail("No exception thrown.");
		}
		catch (DatabaseException e)
		{
			Throwable cause = e.getCause();
			
			if (!(cause instanceof DataException))
				Assert.fail("Wrong exception thrown. Expected DataException, but caught " + cause.getClass().getName());
		}
	}

	@Test
	public void testAddProviderDoubleAddressInEntity() throws Exception
	{
		this.initConnector();
		
		Address a1 = new Address();
		a1.setAdditional(fullAddress001.getAdditional());
		a1.setCity(fullAddress001.getCity());
		a1.setCountry(fullAddress001.getCountry());
		a1.setHouseNumber(fullAddress001.getHouseNumber());
		a1.setState(fullAddress001.getState());
		a1.setStreet(fullAddress001.getStreet());
		a1.setZipCode(fullAddress001.getZipCode());
		
		Address a2 = new Address();
		a2.setAdditional(fullAddress001.getAdditional());
		a2.setCity(fullAddress001.getCity());
		a2.setCountry(fullAddress001.getCountry());
		a2.setHouseNumber(fullAddress001.getHouseNumber());
		a2.setState(fullAddress001.getState());
		a2.setStreet(fullAddress001.getStreet());
		a2.setZipCode(fullAddress001.getZipCode());
		
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(a1);
		addresses.add(a2);
		
		Provider p = new Provider();
		p.setBirthday(fullProvider001.getBirthday());
		p.setEditingUser(fullProvider001.getEditingUser());
		p.setEmail(fullProvider001.getEmail());
		p.setFax(fullProvider001.getFax());
		p.setFirstName(fullProvider001.getFirstName());
		p.setGenderCode(fullProvider001.getGenderCode());
		p.setIpAddress(fullProvider001.getIpAddress());
		p.setLanr(fullProvider001.getLanr());
		p.setLastName(fullProvider001.getLastName());
		p.setOid(fullProvider001.getOid());
		p.setSessionId(fullProvider001.getSessionId());
		p.setTelephone(fullProvider001.getTelephone());
		p.setUser(fullProvider001.getUser());
		p.setAddresses(addresses);
		
		try
		{
			this.connector.addProvider(p);
			
			this.con.setAutoCommit(true);
			Statement stmt1 = this.con.createStatement();
//			Statement stmt2 = this.con.createStatement();
			Statement stmt3 = this.con.createStatement();
			Statement stmt4 = this.con.createStatement();
			Statement stmt5 = this.con.createStatement();
			Statement stmt6 = this.con.createStatement();
			
			ResultSet res1 = stmt1.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
			res1.next();
			int res1_val = res1.getInt(1);
			
			if (res1_val != 1)
				Assert.fail("Provider was not inserted into database.");
			
//			ResultSet res2 = stmt2.executeQuery(QUERY_COUNT_HISTORY_ENTRIES);
//			res2.next();
//			int res2_val = res2.getInt(1);
//			
//			if (res2_val != 3)
//				fail("Incorrect number of history entries: Should be 3, but is " + String.valueOf(res2_val) + ".");
			
			ResultSet res3 = stmt3.executeQuery(QUERY_IS_SET_FULL_ADDRESS_001);
			res3.next();
			int res3_val = res3.getInt(1);
			
			if (res3_val != 1)
				Assert.fail("Address was not inserted correctly: Inserted " + res3_val + " data sets.");
			
			ResultSet res4 = stmt4.executeQuery(QUERY_SELECT_ID_FULL_ADDRESS_001);
			res4.next();
			long addressId = res4.getLong(1);
			
			ResultSet res5 = stmt5.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
			res5.next();
			long providerId = res5.getLong(1);
			
			ResultSet res6 = stmt6.executeQuery("SELECT COUNT(*) FROM ProviderHasAddress WHERE RegionalProviderId = " + String.valueOf(providerId) + " AND AddressId = " + String.valueOf(addressId));
			res6.next();
			int res6_val = res6.getInt(1);
			
			if (res6_val != 1)
				Assert.fail("Relation between provider and address was not set");
			
			this.con.setAutoCommit(false);
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
			Assert.fail("Could not add provider due to a database exception: " + e.getMessage());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Assert.fail("Could not check result.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Assert.fail("Could not add provider due to an exception: " + e.getClass().getName());
		}
	}

	/**
		 * <p>
		 * Tests adding a state including a new address which equals an 
		 * existing address. The <code>state</code> field of the address in
		 * database has value <code>null</code> the field in the entity is 
		 * filled.
		 * </p>
	 * @throws Exception 
		 */
	@Test
		public void testAddProviderExistingAddressDbStateIsNull() throws Exception
		{
		this.initConnector();
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_ADDRESS_002);
				this.con.commit();
			}
			catch (SQLException e)
			{
				Assert.fail("Could not set up database.");
			}
			
			try
			{	
				Address a1 = new Address();
				a1.setAdditional(fullAddress003.getAdditional());
				a1.setCity(fullAddress003.getCity());
				a1.setCountry(fullAddress003.getCountry());
				a1.setHouseNumber(fullAddress003.getHouseNumber());
				a1.setState(fullAddress003.getState());
				a1.setStreet(fullAddress003.getStreet());
				a1.setZipCode(fullAddress003.getZipCode());
				a1.setState(fullAddress003.getState());
				
				List<Address> addresses = new ArrayList<Address>();
				addresses.add(a1);
				
				Provider p = new Provider();
				p.setBirthday(fullProvider001.getBirthday());
				p.setEditingUser(fullProvider001.getEditingUser());
				p.setEmail(fullProvider001.getEmail());
				p.setFax(fullProvider001.getFax());
				p.setFirstName(fullProvider001.getFirstName());
				p.setGenderCode(fullProvider001.getGenderCode());
				p.setIpAddress(fullProvider001.getIpAddress());
				p.setLanr(fullProvider001.getLanr());
				p.setLastName(fullProvider001.getLastName());
				p.setOid(fullProvider001.getOid());
				p.setSessionId(fullProvider001.getSessionId());
				p.setTelephone(fullProvider001.getTelephone());
				p.setUser(fullProvider001.getUser());
				p.setAddresses(addresses);
				
				this.connector.addProvider(p);
				
				this.con.setAutoCommit(true);
				Statement stmt1 = this.con.createStatement();
	//			Statement stmt2 = this.con.createStatement();
				Statement stmt3 = this.con.createStatement();
				Statement stmt4 = this.con.createStatement();
				Statement stmt5 = this.con.createStatement();
				Statement stmt6 = this.con.createStatement();
				Statement stmt7 = this.con.createStatement();
				
				// Provider was inserted
				ResultSet res1 = stmt1.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
				res1.next();
				int res1_val = res1.getInt(1);
				
				if (res1_val != 1)
					Assert.fail("Provider was not inserted into database.");
				
				// Insert provider & insert relation & update address
	//			ResultSet res2 = stmt2.executeQuery(QUERY_COUNT_HISTORY_ENTRIES);
	//			res2.next();
	//			int res2_val = res2.getInt(1);
	//			
	//			if (res2_val != 3)
	//				fail("Incorrect number of history entries: Should be 3, but is " + String.valueOf(res2_val) + ".");
				
				// Insert address 002 should not be available any longer
				ResultSet res7 = stmt7.executeQuery(QUERY_IS_SET_INSERT_ADDRESS_002);
				res7.next();
				int res7_val = res7.getInt(1);
				
				if (res7_val != 0)
					Assert.fail("Previous address is still in database.");
				
				// Full address 003 should be set now
				ResultSet res3 = stmt3.executeQuery(QUERY_IS_SET_FULL_ADDRESS_003);
				res3.next();
				int res3_val = res3.getInt(1);
				
				if (res3_val != 1)
					Assert.fail("Address was not updated.");
				
				ResultSet res4 = stmt4.executeQuery(QUERY_SELECT_ID_FULL_ADDRESS_003);
				res4.next();
				long addressId = res4.getLong(1);
				
				if (addressId != 2L)
					Assert.fail("Address id has changed");
				
				ResultSet res5 = stmt5.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
				res5.next();
				long providerId = res5.getLong(1);
				
				ResultSet res6 = stmt6.executeQuery("SELECT COUNT(*) FROM ProviderHasAddress WHERE RegionalProviderId = " + String.valueOf(providerId) + " AND AddressId = " + String.valueOf(addressId));
				res6.next();
				int res6_val = res6.getInt(1);
				
				if (res6_val != 1)
					Assert.fail("Relation between provider and address was not set");
				
				this.con.setAutoCommit(false);
			}
			catch (SQLException e)
			{
				Assert.fail("Could not check database content.");
			}
			catch (DatabaseException e)
			{
				Assert.fail("Failed to add provider due to a DatabaseException: " + e.getMessage());
			}
			catch (Exception e)
			{
				Assert.fail("Failed due to an unexpected exception: " + e.getClass().getName());
			}
		}
		
		/**
		 * <p>
		 * Updates an existing address to the values of another already 
		 * existing address not being in the address list of the provider.
		 * </p>
		 * <p>
		 * Expected result: Both address will stay in their old version
		 * and a relation to the already existing address in database will be 
		 * created.
		 * </p>
		 * @throws Exception 
		 * 
		 */
	@Test
		public void testAddProviderAddressIdUpdatedToExistingAddress() throws Exception
		{
		this.initConnector();
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_ADDRESS_001);
				stmt.executeUpdate(QUERY_INSERT_ADDRESS_002);
				this.con.commit();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				Assert.fail("Could not set up database.");
			}
			
			Address a1 = new Address();
			a1.setId(insertAddressData002.getId());
			a1.setAdditional(insertAddressData001.getAdditional());
			a1.setCity(insertAddressData001.getCity());
			a1.setCountry(insertAddressData001.getCountry());
			a1.setHouseNumber(insertAddressData001.getHouseNumber());
			a1.setState(insertAddressData001.getState());
			a1.setStreet(insertAddressData001.getStreet());
			a1.setZipCode(insertAddressData001.getZipCode());
			
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(a1);
			
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			p.setAddresses(addresses);
			
			try
			{
				this.connector.addProvider(p);
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
				Assert.fail("Failed to add provider due to a DatabaseException.");
			}
			catch (Exception e)
			{
				Assert.fail("Unexpected exception caught: " + e.getClass().getName());
			}
			
			try
			{
				this.con.setAutoCommit(true);
				Statement stmt = this.con.createStatement();
				
				ResultSet res1 = stmt.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
				res1.next();
				int res1_val = res1.getInt(1);
				
				if (res1_val != 1)
					Assert.fail("Could not insert provider. Expected 1 data set but counted " + String.valueOf(res1_val));
				
				ResultSet res2 = stmt.executeQuery(QUERY_IS_SET_INSERT_ADDRESS_001);
				res2.next();
				int res2_val = res2.getInt(1);
				
				if (res2_val != 1)
					Assert.fail("Existing address data changed. Expected 1 data set, but countetd " + String.valueOf(res2_val));
				
				ResultSet res3 = stmt.executeQuery(QUERY_IS_SET_INSERT_ADDRESS_002);
				res3.next();
				int res3_val = res3.getInt(1);
				
				if (res3_val != 1)
					Assert.fail("Updated address data changed. Expected 1 data set, but counted " + String.valueOf(res3_val));
						
				ResultSet res4 = stmt.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
				res4.next();
				long providerId = res4.getLong(1);
				
				ResultSet res5 = stmt.executeQuery("SELECT COUNT(*) FROM ProviderHasAddress WHERE RegionalProviderId = " + String.valueOf(providerId) + " AND AddressId = " + insertAddressData001.getId().toString());
				res5.next();
				int res5_val = res5.getInt(1);
				
				if (res5_val != 1)
					Assert.fail("Relation from provider to address was not inserted properly. Expected 1 data set, but counted " + String.valueOf(res5_val));
				
				this.con.setAutoCommit(false);
			}
			catch (SQLException e)
			{
				Assert.fail("Could not verify results.");
			}
		}
		
		public void testAddProviderDifferentAddressIdsUpdatedToSameAddress()
		{
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_ADDRESS_001);
				stmt.executeUpdate(QUERY_INSERT_ADDRESS_003);
				this.con.commit();
			}
			catch (SQLException e)
			{
				Assert.fail("Failed to set up database.");
			}
			
			Address a1 = new Address();
			a1.setId(insertAddressData001.getId());
			a1.setAdditional(fullAddress001.getAdditional());
			a1.setCity(fullAddress001.getCity());
			a1.setCountry(fullAddress001.getCountry());
			a1.setHouseNumber(fullAddress001.getHouseNumber());
			a1.setState(fullAddress001.getState());
			a1.setStreet(fullAddress001.getStreet());
			a1.setZipCode(fullAddress001.getZipCode());
			
			Address a2 = new Address();
			a2.setId(insertAddressData003.getId());
			a2.setAdditional(fullAddress001.getAdditional());
			a2.setCity(fullAddress001.getCity());
			a2.setCountry(fullAddress001.getCountry());
			a2.setHouseNumber(fullAddress001.getHouseNumber());
			a2.setState(fullAddress001.getState());
			a2.setStreet(fullAddress001.getStreet());
			a2.setZipCode(fullAddress001.getZipCode());
			
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(a1);
			addresses.add(a2);
			
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			p.setAddresses(addresses);
			
			try
			{
				this.connector.addProvider(p);
				Assert.fail("No exception thrown.");
			}
			catch (DatabaseException e)
			{
				Throwable cause = e.getCause();
				if (!(cause instanceof DataException))
				{
					e.printStackTrace();
					Assert.fail("Wrong exception thrown. DataException expected, " + cause.getClass().getName() + " received.");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Assert.fail("Unexpected exception thrown.");
			}
		}
		
		@Test
		public void testAddProviderNewLocalId() throws Exception
		{
			this.initConnector();
			LocalId lid1 = new LocalId();
			lid1.setApplication(fullLocalId001.getApplication());
			lid1.setFacility(fullLocalId001.getFacility());
			lid1.setLocalId(fullLocalId001.getLocalId());
			
			List<LocalId> localIds = new ArrayList<LocalId>();
			localIds.add(lid1);
			
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			p.setLocalIds(localIds);
			
			try
			{
				this.connector.addProvider(p);
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
				Assert.fail("Failed to add provider.");
			}
			
			try
			{
				this.con.setAutoCommit(true);
				Statement stmt = this.con.createStatement();
				
				ResultSet res1 = stmt.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
				res1.next();
				int res1_val = res1.getInt(1);
				
				if (res1_val != 1)
					Assert.fail("Provider was not inserted properly. Expected 1 data set, but received " + String.valueOf(res1_val) + ".");
				
				ResultSet res2 = stmt.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
				res2.next();
				long providerId = res2.getLong(1);
				
				ResultSet res3 = stmt.executeQuery(QUERY_IS_SET_FULL_LOCALID_001);
				res3.next();
				int res3_val = res3.getInt(1);
				
				if (res3_val != 1)
					Assert.fail("Local id was not inserted propertly. Expected 1 data set, but ereceived " + String.valueOf(res3_val) + ".");
				
				ResultSet res4 = stmt.executeQuery(QUERY_SELECT_ID_FULL_LOCALID_001);
				res4.next();
				long lidProviderId = res4.getLong(2);
				long lidOrganisationId = res4.getLong(3);
				
				if (lidProviderId == 0)
					Assert.fail("Provider id was not set in local id.");
				
				if (lidProviderId != providerId)
					Assert.fail("Wrong provider id was set in local id. Excpected " + providerId + ", but selected " + String.valueOf(lidProviderId) + ".");
				
				if (lidOrganisationId != 0)
					Assert.fail("Organisation id was set in local id: "  + String.valueOf(lidOrganisationId));
				
				this.con.setAutoCommit(false);
			}
			catch (SQLException e)
			{
				Assert.fail("Could not verify results.");
			}
		}
		
		@Test
		public void testAddProviderExistingLocalId() throws Exception
		{
			this.initConnector();
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_PROVIDER_002);
				stmt.executeUpdate(QUERY_INSERT_LOCALID_002);
				this.con.commit();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				Assert.fail("Could not set up database.");
			}
			
			LocalId lid1 = new LocalId();
			lid1.setApplication(insertLocalIdData002.getApplication());
			lid1.setFacility(insertLocalIdData002.getFacility());
			lid1.setLocalId(insertLocalIdData002.getLocalId());
			
			List<LocalId> localIds = new ArrayList<LocalId>();
			localIds.add(lid1);
			
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			p.setLocalIds(localIds);
			
			try
			{
				this.connector.addProvider(p);
				Assert.fail("No exception thrown.");
			}
			catch (DatabaseException e)
			{
				Throwable cause = e.getCause();
				if (!(cause instanceof DataException))
				{
					Assert.fail("Wrong exception thrown. Expected DataException, but caught " + cause.getClass().getName());
				}
			}
			catch (Exception e)
			{
				Assert.fail("Unexpected exception thrown: " + e.getClass().getName());
			}
		}
		
		@Test
		public void testAddProviderDoubleLocalIdInEntity() throws Exception
		{
			this.initConnector();
			LocalId lid1 = new LocalId();
			lid1.setApplication(fullLocalId001.getApplication());
			lid1.setFacility(fullLocalId001.getFacility());
			lid1.setLocalId(fullLocalId001.getLocalId());
			
			LocalId lid2 = new LocalId();
			lid2.setApplication(fullLocalId001.getApplication());
			lid2.setFacility(fullLocalId001.getFacility());
			lid2.setLocalId(fullLocalId001.getLocalId());
			
			List<LocalId> localIds = new ArrayList<LocalId>();
			localIds.add(lid1);
			localIds.add(lid2);
			
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			p.setLocalIds(localIds);
			
			try
			{
				this.connector.addProvider(p);
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
				Assert.fail("Failed to add provider due to an exception.");
			}
			
			try
			{
				this.con.setAutoCommit(true);
				Statement stmt = this.con.createStatement();
				
				ResultSet res1 = stmt.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
				res1.next();
				int res1_val = res1.getInt(1);
				
				if (res1_val != 1)
					Assert.fail("Provider was not inserted properly. Expected 1 entry, selected " + String.valueOf(res1_val) + ".");
				
				ResultSet res2 = stmt.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
				res2.next();
				long providerId = res2.getLong(1);
				
				ResultSet res3 = stmt.executeQuery(QUERY_IS_SET_FULL_LOCALID_001);
				res3.next();
				int res3_val = res3.getInt(1);
				
				if (res3_val != 1)
					Assert.fail("Local id was not inserted properly. Expected 1 entry, selected " + String.valueOf(res3_val) + ".");
				
				ResultSet res4 = stmt.executeQuery(QUERY_SELECT_ID_FULL_LOCALID_001);
				res4.next();
				long res4_providerId = res4.getLong(2);
				long res4_organisationId = res4.getLong(3);
				
				if (res4_providerId != providerId)
					Assert.fail("Provider relation was not set properly. Expected value " + String.valueOf(providerId) + ", but selected " + String.valueOf(res4_providerId) + ".");
				
				if (res4_organisationId != 0)
					Assert.fail("Organisation relation was set: " + String.valueOf(res4_organisationId));
				
				this.con.setAutoCommit(false);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				Assert.fail("Could not verify results.");
			}
		}
		
//		public void testAddProviderDifferentLocalIdIdsUpdatedToSameLocalId()
//		{
//			try
//			{
//				Statement stmt = this.con.createStatement();
//				stmt.executeUpdate(QUERY_INSERT_PROVIDER_001);
//				stmt.executeUpdate(QUERY_INSERT_LOCALID_001);
//				stmt.executeUpdate(QUERY_INSERT_LOCALID_003);
//				this.con.commit();
//			}
//			catch (SQLException e)
//			{
//				fail("Could not set up database.");
//			}
//			
//			LocalId lid1 = new LocalId();
//			lid1.setId(insertLocalIdData001.getId());
//			lid1.setApplication(fullLocalId001.getApplication());
//			lid1.setFacility(fullLocalId001.getFacility());
//			lid1.setLocalId(fullLocalId001.getLocalId());
//			
//			LocalId lid2 = new LocalId();
//			lid2.setId(insertLocalIdData002.getId());
//			lid2.setApplication(fullLocalId001.getApplication());
//			lid2.setFacility(fullLocalId001.getFacility());
//			lid2.setLocalId(fullLocalId001.getLocalId());
//			
//			List<LocalId> localIds = new ArrayList<LocalId>();
//			localIds.add(lid1);
//			localIds.add(lid2);
//			
//			Provider p = new Provider();
//			p.setBirthday(fullProvider001.getBirthday());
//			p.setEditingUser(fullProvider001.getEditingUser());
//			p.setEmail(fullProvider001.getEmail());
//			p.setEstablishmentId(fullProvider001.getEstablishmentId());
//			p.setFax(fullProvider001.getFax());
//			p.setFirstName(fullProvider001.getFirstName());
//			p.setGenderCode(fullProvider001.getGenderCode());
//			p.setIpAddress(fullProvider001.getIpAddress());
//			p.setLanr(fullProvider001.getLanr());
//			p.setLastName(fullProvider001.getLastName());
//			p.setOid(fullProvider001.getOid());
//			p.setSessionId(fullProvider001.getSessionId());
//			p.setTelephone(fullProvider001.getTelephone());
//			p.setUser(fullProvider001.getUser());
//			p.setLocalIds(localIds);
//			
//			try
//			{
//				this.connector.addProvider(p);
//				fail("No exception thrown.");
//			}
//			catch (DatabaseException e)
//			{
//				Throwable cause = e.getCause();
//				if (!(cause instanceof DataException))
//				{
//					e.printStackTrace();
//					fail("Wrong exception thrown. Expected DataException, but caught " + cause.getClass().getName());
//				}
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				fail("Unexpected exception thrown: " + e.getClass().getName());
//			}
//		}
//	
//		public void testAddProviderLocalIdUpdatedToExistingLocalId()
//		{
//			try
//			{
//				Statement stmt = this.con.createStatement();
//				stmt.executeUpdate(QUERY_INSERT_PROVIDER_001);
//				stmt.executeUpdate(QUERY_INSERT_LOCALID_001);
//				this.con.commit();
//			}
//			catch (SQLException e)
//			{
//				fail("Could not set up database.");
//			}
//			
//			LocalId lid1 = new LocalId();
//			lid1.setApplication(insertLocalIdData001.getApplication());
//			lid1.setFacility(insertLocalIdData001.getFacility());
//			lid1.setLocalId(insertLocalIdData001.getLocalId());
//			
//			List<LocalId> localIds = new ArrayList<LocalId>();
//			localIds.add(lid1);
//			
//			Provider p = new Provider();
//			p.setBirthday(fullProvider001.getBirthday());
//			p.setEditingUser(fullProvider001.getEditingUser());
//			p.setEmail(fullProvider001.getEmail());
//			p.setEstablishmentId(fullProvider001.getEstablishmentId());
//			p.setFax(fullProvider001.getFax());
//			p.setFirstName(fullProvider001.getFirstName());
//			p.setGenderCode(fullProvider001.getGenderCode());
//			p.setIpAddress(fullProvider001.getIpAddress());
//			p.setLanr(fullProvider001.getLanr());
//			p.setLastName(fullProvider001.getLastName());
//			p.setOid(fullProvider001.getOid());
//			p.setSessionId(fullProvider001.getSessionId());
//			p.setTelephone(fullProvider001.getTelephone());
//			p.setUser(fullProvider001.getUser());
//			p.setLocalIds(localIds);
//			
//			try
//			{
//				this.connector.addProvider(p);
//				fail("No exception thrown.");
//			}
//			catch (DatabaseException e)
//			{
//				Throwable cause = e.getCause();
//				
//				if (!(cause instanceof DataException))
//					fail("Wrong exception thrown. Expected DataException, but caught " + cause.getClass().getName());
//			}
//			catch (Exception e)
//			{
//				fail("Unexpected exception thrown: " + e.getClass().getName());
//			}
//		}
		
//		@Test
//		public void testAddProviderRelateToOrganisation() throws Exception
//		{
//			this.initConnector();
//			try
//			{
//				Statement stmt = this.con.createStatement();
//				stmt.executeUpdate(QUERY_INSERT_ORGANISATION_001);
//				this.con.commit();
//			}
//			catch (SQLException e)
//			{
//				Assert.fail("Ould not set up database.");
//			}
//			
//			Organisation o1 = new Organisation();
//			o1.setId(insertOrganisationData001.getId());
//			o1.setAddresses(insertOrganisationData001.getAddresses());
//			o1.setDeactivationDate(insertOrganisationData001.getDeactivationDate());
//			o1.setDeactivationReasonCode(insertOrganisationData001.getDeactivationReasonCode());
//			o1.setDescription(insertOrganisationData001.getDescription());
//			o1.setDuplicatesCalculated(insertOrganisationData001.isDuplicatesCalculated());
//			o1.setEditingUser(insertOrganisationData001.getEditingUser());
//			o1.setEmail(insertOrganisationData001.getEmail());
//			o1.setEstablishmentId(insertOrganisationData001.getEstablishmentId());
//			o1.setFax(insertOrganisationData001.getFax());
//			o1.setIpAddress(insertOrganisationData001.getIpAddress());
//			o1.setLastUpdateDate(insertOrganisationData001.getLastUpdateDate());
//			o1.setLocalIds(insertOrganisationData001.getLocalIds());
//			o1.setName(insertOrganisationData001.getName());
//			o1.setOid(insertOrganisationData001.getOid());
//			o1.setProviders(insertOrganisationData001.getProviders());
//			o1.setReactivationDate(insertOrganisationData001.getReactivationDate());
//			o1.setReactivationReasonCode(insertOrganisationData001.getReactivationReasonCode());
//			o1.setSecondName(insertOrganisationData001.getSecondName());
//			o1.setSessionId(insertOrganisationData001.getSessionId());
//			o1.setTelephone(insertOrganisationData001.getTelephone());
//			o1.setUser(insertOrganisationData001.getUser());
//			
//			List<Organisation> organisations = new ArrayList<Organisation>();
//			organisations.add(o1);
//			
//			Provider p = new Provider();
//			p.setBirthday(fullProvider001.getBirthday());
//			p.setEditingUser(fullProvider001.getEditingUser());
//			p.setEmail(fullProvider001.getEmail());
//			p.setFax(fullProvider001.getFax());
//			p.setFirstName(fullProvider001.getFirstName());
//			p.setGenderCode(fullProvider001.getGenderCode());
//			p.setIpAddress(fullProvider001.getIpAddress());
//			p.setLanr(fullProvider001.getLanr());
//			p.setLastName(fullProvider001.getLastName());
//			p.setOid(fullProvider001.getOid());
//			p.setSessionId(fullProvider001.getSessionId());
//			p.setTelephone(fullProvider001.getTelephone());
//			p.setUser(fullProvider001.getUser());
//			p.setOrganisations(organisations);
//			
//			try
//			{
//				this.connector.addProvider(p);
//			}
//			catch (DatabaseException e)
//			{
//				e.printStackTrace();
//				Assert.fail("Could not add provider due to an exception.");
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				Assert.fail("Could not add provider due to an unexpected exception: " + e.getClass().getName());
//			}
//			
//			try
//			{
//				this.con.setAutoCommit(true);
//				
//				Statement stmt = this.con.createStatement();
//				
//				ResultSet res1 = stmt.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
//				res1.next();
//				int res1_val = res1.getInt(1);
//				
//				if (res1_val != 1)
//					Assert.fail("Provider was not inserted properly. Expected 1 data set, but received " + String.valueOf(res1_val) + ".");
//				
//				ResultSet res2 = stmt.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
//				res2.next();
//				long providerId = res2.getLong(1);
//				
//				ResultSet res3 = stmt.executeQuery("SELECT COUNT(*) FROM OrganisationHasProvider WHERE RegionalOrganisationId = " + insertOrganisationData001.getId().toString() + " AND RegionalProviderId = " + String.valueOf(providerId));
//				res3.next();
//				int res3_val = res3.getInt(1);
//				
//				if (res3_val != 1)
//					Assert.fail("Relation between provider and organisation was not set properly. Expected 1 data set, but received " + String.valueOf(res3_val));
//				
//				this.con.setAutoCommit(false);
//			}
//			catch (SQLException e)
//			{
//				Assert.fail("Could not verify results.");
//			}
//		}
//		
//		@Test
//		public void testAddProviderDoubleRelateToOrganisation() throws Exception
//		{
//			this.initConnector();
//			try
//			{
//				Statement stmt = this.con.createStatement();
//				stmt.executeUpdate(QUERY_INSERT_ORGANISATION_001);
//				this.con.commit();
//			}
//			catch (SQLException e)
//			{
//				Assert.fail("Ould not set up database.");
//			}
//			
//			Organisation o1 = new Organisation();
//			o1.setId(insertOrganisationData001.getId());
//			
//			Organisation o2 = new Organisation();
//			o2.setId(insertOrganisationData001.getId());
//			
//			List<Organisation> organisations = new ArrayList<Organisation>();
//			organisations.add(o1);
//			organisations.add(o2);
//			
//			Provider p = new Provider();
//			p.setBirthday(fullProvider001.getBirthday());
//			p.setEditingUser(fullProvider001.getEditingUser());
//			p.setEmail(fullProvider001.getEmail());
//			p.setFax(fullProvider001.getFax());
//			p.setFirstName(fullProvider001.getFirstName());
//			p.setGenderCode(fullProvider001.getGenderCode());
//			p.setIpAddress(fullProvider001.getIpAddress());
//			p.setLanr(fullProvider001.getLanr());
//			p.setLastName(fullProvider001.getLastName());
//			p.setOid(fullProvider001.getOid());
//			p.setSessionId(fullProvider001.getSessionId());
//			p.setTelephone(fullProvider001.getTelephone());
//			p.setUser(fullProvider001.getUser());
//			p.setOrganisations(organisations);
//			
//			try
//			{
//				this.connector.addProvider(p);
//			}
//			catch (DatabaseException e)
//			{
//				e.printStackTrace();
//				Assert.fail("Could not add provider due to an exception.");
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				Assert.fail("Could not add provider due to an unexpected exception: " + e.getClass().getName());
//			}
//			
//			try
//			{
//				this.con.setAutoCommit(true);
//				
//				Statement stmt = this.con.createStatement();
//				
//				ResultSet res1 = stmt.executeQuery(QUERY_IS_SET_FULL_PROVIDER_001);
//				res1.next();
//				int res1_val = res1.getInt(1);
//				
//				if (res1_val != 1)
//					Assert.fail("Provider was not inserted properly. Expected 1 data set, but received " + String.valueOf(res1_val) + ".");
//				
//				ResultSet res2 = stmt.executeQuery(QUERY_SELECT_ID_FULL_PROVIDER_001);
//				res2.next();
//				long providerId = res2.getLong(1);
//				
//				ResultSet res3 = stmt.executeQuery("SELECT COUNT(*) FROM OrganisationHasProvider WHERE RegionalOrganisationId = " + insertOrganisationData001.getId().toString() + " AND RegionalProviderId = " + String.valueOf(providerId));
//				res3.next();
//				int res3_val = res3.getInt(1);
//				
//				if (res3_val != 1)
//					Assert.fail("Relation between provider and organisation was not set properly. Expected 1 data set, but received " + String.valueOf(res3_val));
//				
//				this.con.setAutoCommit(false);
//			}
//			catch (SQLException e)
//			{
//				Assert.fail("Could not verify results.");
//			}
//		}
		
//	public void testGetHistory()
//	{
//		assert(this.connector.getHistory().size() > 0);
//	}
//	
//	public void testGetDetailedHistory()
//	{
////		LoggingEntry logEntry = new LoggingEntry();
////		logEntry.setId(1);
////		logEntry.setDomain("Provider");
////		assert(this.connector.getDetailedHistory(logEntry) != null);
//	}
//	
//	public void testGetUserByName() throws RemoteException {
//		PorsUser user = new PorsUser();
//		user.setName("admin");
//		PorsUser result = this.connector.getUser(user);
//		assert(result != null && result.getName().equals("admin"));
//	}
//	
//	public void testUpdateProvider() 
//	{
//		PorsUser user = new PorsUser();
//		user.setName("admin");
//		PorsUser dbUser = this.connector.getUser(user);
//		
//		// Prepare addresses
//		List<Address> addresses1 = new LinkedList<Address>();
//		List<Address> addresses2 = new LinkedList<Address>();
//		
//		for(int i = 1; i < 5; i++)
//		{
//			Address address = new Address();
//			address.setCity("Heidelberg");
//			address.setCountry("DE");
//			address.setHouseNumber(String.valueOf(i));
//			address.setStreet("Im Neuenheimer Feld");
//			address.setZipCode("69115");
//			
//			if(i < 4)
//			{
//				addresses1.add(address);
//			}
//			
//			if(i > 2)
//			{
//				addresses2.add(address);
//			}
//		}
//		
//		Date birthday = new Date();
//		Date deactivationDate = new Date();
//		Date lastUpdateDate = new Date();
//		Date reactivationDate = new Date();
//		
//		Provider providerDTO = new Provider();
//		providerDTO.setUser(dbUser);
//		providerDTO.setBirthday(birthday);
//		providerDTO.setDeactivationDate(deactivationDate);
//		providerDTO.setDeactivationReasonCode("1");
//		providerDTO.setEmail("test@test.de");
//		providerDTO.setFax("+496221-1234567");
//		providerDTO.setFirstName("Heinz");
//		providerDTO.setGenderCode("M");
//		providerDTO.setLanr("1234c4a6d");
//		providerDTO.setLastName("Becker");
//		providerDTO.setLastUpdateDate(lastUpdateDate);
//		providerDTO.setNamePrefix("Dr.");
//		providerDTO.setOid("1.2.3.4.C.6.7.F.9");
//		providerDTO.setReactivationDate(reactivationDate);
//		providerDTO.setTelephone("+496221-1234567");
//		providerDTO.setAddresses(addresses1);
//		providerDTO.setEditingUser(dbUser);
//		providerDTO.setSessionId("ab34-c-d45");
//		
//		try {
//			this.connector.addProvider(providerDTO);
//		} catch (DatabaseException e) {
//			fail("Could not insert provider.");
//		}
//		
//		Address newAddress = new Address();
//		newAddress.setCity("InsertNachUpdate-Dorf");
//		newAddress.setCountry("DE");
//		newAddress.setHouseNumber("20b");
//		newAddress.setStreet("Endstraße");
//		newAddress.setZipCode("34204");
//		
//		List<Address> oldAddresses = providerDTO.getAddresses();
//		oldAddresses.remove(0);
//		oldAddresses.add(newAddress);
//		
//		providerDTO.setAddresses(oldAddresses);
//		providerDTO.setEstablishmentId("UpdatedEstablishmentId-xyzac");
//		
//		try {
//			this.connector.updateProvider(providerDTO);
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//			fail("Could not update provider.");
//		}
//	}
	
//	public void testAddOrganisation()
//	{
//		// Prepare addresses
//		List<Address> addresses1 = new LinkedList<Address>();
//		List<Address> addresses2 = new LinkedList<Address>();
//		
//		for(int i = 1; i < 5; i++)
//		{
//			Address address = new Address();
//			address.setCity("Heidelberg");
//			address.setCountry("DE");
//			address.setHouseNumber(String.valueOf(i));
//			address.setStreet("Weiherweg");
//			address.setZipCode("69115");
//			
//			if(i < 4)
//			{
//				addresses1.add(address);
//			}
//			
//			if(i > 2)
//			{
//				addresses2.add(address);
//			}
//		}
//		
//		PorsUser adminSceleton = new PorsUser();
//		adminSceleton.setName("admin");
//		PorsUser admin = this.connector.getUser(adminSceleton);
//		
//		Organisation orga = new Organisation();
//		orga.setAddresses(addresses2);
//		orga.setDeactivationDate(new Date());
//		orga.setDeactivationReasonCode("F");
//		orga.setDescription("An entry for testing purposes.");
//		orga.setEditingUser(admin);
//		orga.setEmail("myMail@myMailer.de");
//		orga.setEstablishmentId("Est-abc-01");
//		orga.setFax("1234-56789");
//		orga.setLastUpdateDate(new Date());
//		orga.setName("Private Testing Organisation");
//		orga.setOid("12.334.21.67.1");
//		orga.setReactivationDate(new Date());
//		orga.setReactivationReasonCode("C");
//		orga.setSecondName("Cool Testing Organisation");
//		orga.setSessionId("dafc3445f");
//		orga.setTelephone("9875-124625");
//		orga.setUser(admin);
//		
//		try {
//			this.connector.addOrganisation(orga);
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//			fail("Could not add organisation");
//		}
//	}
//	
//	public void testUpdateOrganisation()
//	{
//		// Prepare addresses
//		List<Address> addresses1 = new LinkedList<Address>();
//		
//		for(int i = 1; i < 5; i++)
//		{
//			Address address = new Address();
//			address.setCity("IrgendeinDorf");
//			address.setCountry("DE");
//			address.setHouseNumber(String.valueOf(i));
//			address.setStreet("Eckernstraße");
//			address.setZipCode("34204");
//			
//			addresses1.add(address);
//		}
//		
//		PorsUser adminSceleton = new PorsUser();
//		adminSceleton.setName("admin");
//		PorsUser admin = this.connector.getUser(adminSceleton);
//		
//		Organisation orga = new Organisation();
//		orga.setAddresses(addresses1);
//		orga.setDeactivationDate(new Date());
//		orga.setDeactivationReasonCode("F");
//		orga.setDescription("An entry for testing purposes.");
//		orga.setEditingUser(admin);
//		orga.setEmail("mySecondMail@myMailer.de");
//		orga.setEstablishmentId("Est-def-21");
//		orga.setFax("1234-56789");
//		orga.setLastUpdateDate(new Date());
//		orga.setName("New Testing Organisation");
//		orga.setOid("12.3ab4.21c.67.1");
//		orga.setReactivationDate(new Date());
//		orga.setReactivationReasonCode("C");
//		orga.setSecondName("NTO");
//		orga.setSessionId("fc3445f");
//		orga.setTelephone("9875-124625");
//		orga.setUser(admin);
//		
//		try {
//			this.connector.addOrganisation(orga);
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//			fail("Could not add test organisation");
//		}
//		
//		orga.setEmail("MyUpdatedMail@mail.de");
//		orga.setFax("3456-12345");
//		
//		Address newAddress = new Address();
//		newAddress.setCity("InsertNachUpdate-Dorf");
//		newAddress.setCountry("DE");
//		newAddress.setHouseNumber("20a");
//		newAddress.setStreet("Endstraße");
//		newAddress.setZipCode("34204");
//		
//		List<Address> lst = orga.getAddresses();
//		lst.remove(0);
//		lst.get(0).setAdditional("UpdatedAdditional");
//		lst.add(newAddress);
//		
//		orga.setAddresses(lst);
//		
//		try
//		{
//			this.connector.updateOrganisation(orga);
//		}
//		catch (DatabaseException e)
//		{
//			e.printStackTrace();
//			fail("Could not update test organisation");
//		}
//	}
//	
//	public void testUpdateProviderRemoveLocalId()
//	{
//		PorsUser user = new PorsUser();
//		user.setName("admin");
//		PorsUser dbUser = this.connector.getUser(user);
//		
//		// Prepare addresses
//		List<Address> addresses1 = new LinkedList<Address>();
//		List<Address> addresses2 = new LinkedList<Address>();
//		
//		for(int i = 1; i < 5; i++)
//		{
//			Address address = new Address();
//			address.setCity("Heidelberg");
//			address.setCountry("DE");
//			address.setHouseNumber(String.valueOf(i));
//			address.setStreet("Im Neuenheimer Feld");
//			address.setZipCode("69115");
//			
//			if(i < 4)
//			{
//				addresses1.add(address);
//			}
//			
//			if(i > 2)
//			{
//				addresses2.add(address);
//			}
//		}
//		
//		Date birthday = new Date();
//		Date deactivationDate = new Date();
//		Date lastUpdateDate = new Date();
//		Date reactivationDate = new Date();
//		
//		Provider provider = new Provider();
//		provider.setUser(dbUser);
//		provider.setBirthday(birthday);
//		provider.setDeactivationDate(deactivationDate);
//		provider.setDeactivationReasonCode("1");
//		provider.setEmail("test@test.de");
//		provider.setFax("+496221-1234567");
//		provider.setFirstName("Hinz");
//		provider.setGenderCode("M");
//		provider.setIpAddress("1.0.0.127");
//		provider.setLanr("123ac4abd");
//		provider.setLastName("Kunz");
//		provider.setLastUpdateDate(lastUpdateDate);
//		provider.setNamePrefix("Dr.");
//		provider.setOid("1.2.3.445.C.6.7.F.9");
//		provider.setReactivationDate(reactivationDate);
//		provider.setTelephone("+496221-123457");
//		provider.setAddresses(addresses1);
//		provider.setEditingUser(dbUser);
//		provider.setSessionId("test-update-provider-remove-localid");
//		
//		List<LocalId> localIds = new ArrayList<LocalId>();
//		
//		LocalId lid_01 = new LocalId();
//		lid_01.setFacility("TestUpdProv002");
//		lid_01.setLocalId("tup_02_001");
//		
//		LocalId lid_02 = new LocalId();
//		lid_02.setFacility("TestUpdProv002");
//		lid_02.setLocalId("tup_02_002");
//	
//		localIds.add(lid_01);
//		localIds.add(lid_02);
//		
//		provider.setLocalIds(localIds);
//		
//		try {
//			this.connector.addProvider(provider);
//		} catch (DatabaseException e) {
//			fail("Could not insert provider.");
//		}
//		
//		provider.getLocalIds().remove(1);
//		
//		try 
//		{
//			this.connector.updateProvider(provider);
//		}
//		catch (DatabaseException e)
//		{
//			fail("Could not update provider.");
//		}
//	}
		
		@Test
		public void testGetPorsUserByName() throws Exception
		{
			this.initConnector();
			PorsUser user = new PorsUser();
			user.setName("Testuser3");
			
			try
			{
				PorsUser dbUser = this.connector.getUser(user);
				
				if (dbUser == null)
				{
					Assert.fail("Could not select a user.");
				}
			}
			catch (Exception e)
			{
				Assert.fail("Failed to get user due to an unexpected exception: " + e.getMessage());
			}
		}
		
		@Test
		public void testGetPorsUserById() throws Exception
		{
			this.initConnector();
			PorsUser user = new PorsUser();
			user.setId(3);
			
			try
			{
				PorsUser dbUser = this.connector.getUser(user);
				
				if (dbUser == null)
				{
					Assert.fail("Could not select a user.");
				}
			}
			catch (Exception e)
			{
				Assert.fail("Failed to get user due to an unexpected exception: " + e.getMessage());
			}
		}
		
		@Test
		public void testGetProviderById() throws Exception
		{
			this.initConnector();
			try {
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_PROVIDER_001);
				this.con.commit();
				
				Provider dbProvider = this.connector.getProviderById(1L);
				
				if (dbProvider == null)
					Assert.fail("Could not select provider.");
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				Assert.fail("Could not set up database.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Assert.fail("Could not select provider due to an exception.");
			}
		}
		
		@Test
		public void testGetProviderByLocalId() throws Exception
		{
			this.initConnector();
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_PROVIDER_001);
				stmt.executeUpdate(QUERY_INSERT_LOCALID_001);
				this.con.commit();
				
				LocalId localId = new LocalId();
				localId.setLocalId("LocalId001");
				localId.setFacility("UniHD001");
				localId.setApplication("UniHDSys001");
				
				Provider dbProvider = this.connector.getProviderByLocalId(localId);
				
				if (dbProvider == null)
					Assert.fail("Could not select provider by local id.");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				Assert.fail("Could not set up database.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Assert.fail("Could not load provider due to an exception.");
			}
		}
		
		@Test
		public void testGetProviderByUniqueFields() throws Exception
		{
			this.initConnector();
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_PROVIDER_001);
				this.con.commit();
				
				Provider dbProvider = this.connector.getProviderByUniqueCombination(insertProviderData001.getFirstName(), 
						insertProviderData001.getLastName(), 
						insertProviderData001.getGenderCode(), 
						insertProviderData001.getBirthday());
				
				if (dbProvider == null)
					Assert.fail("Could not select provider.");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				Assert.fail("Could not set up database.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Assert.fail("Could not select provider due to an exception.");
			}
		}

		@Test
		public void testGetProviderByUniqueFieldsBirthdayIsNull() throws Exception
		{
			this.initConnector();
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_PROVIDER_002);
				this.con.commit();
				
				Provider dbProvider = this.connector.getProviderByUniqueCombination(insertProviderData002.getFirstName(), 
						insertProviderData002.getLastName(), 
						insertProviderData002.getGenderCode(), 
						insertProviderData002.getBirthday());
				
				if (dbProvider == null)
					Assert.fail("Could not select provider.");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				Assert.fail("Could not set up database.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Assert.fail("Could not select provider due to an exception.");
			}
		}

		@Test
		public void testGetProviderList() throws Exception
		{
			this.initConnector();
			try
			{
				Statement stmt = this.con.createStatement();
				stmt.executeUpdate(QUERY_INSERT_PROVIDER_001);
				stmt.executeUpdate(QUERY_INSERT_PROVIDER_002);
				this.con.commit();
				
				assert(this.connector.getProviderList().size() == 2);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				Assert.fail("Could not set up database.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Assert.fail("Could not select provider list due to an exception.");
			}
		}
		
		@Test
		public void testGetProviderListEmptyResult() throws Exception
		{
			this.initConnector();
			List<Provider> lst = this.connector.getProviderList();
			assert(lst != null && lst.size() == 0);
		}
		
		/**
		 * <p>
		 * Tries to update an provider without having the provider id set.
		 * </p>
		 * <p>
		 * <b>Expected result:</b> A 
		 * {@link de.uni_heidelberg.de.ise.pors.database.exception.DatabaseException DatabaseException} 
		 * is thrown. The cause is a 
		 * {@link de.uni_heidelberg.de.ise.pors.database.exception.DataException DataException}.
		 * </p>
		 * @throws Exception 
		 */
		@Test
		public final void testUpdateProviderMissingId() throws Exception
		{
			this.initConnector();
			Provider p = new Provider();
			p.setBirthday(fullProvider001.getBirthday());
			p.setEditingUser(fullProvider001.getEditingUser());
			p.setEmail(fullProvider001.getEmail());
			p.setFax(fullProvider001.getFax());
			p.setFirstName(fullProvider001.getFirstName());
			p.setGenderCode(fullProvider001.getGenderCode());
			p.setIpAddress(fullProvider001.getIpAddress());
			p.setLanr(fullProvider001.getLanr());
			p.setLastName(fullProvider001.getLastName());
			p.setLastUpdateDate(fullProvider001.getLastUpdateDate());
			p.setOid(fullProvider001.getOid());
			p.setSessionId(fullProvider001.getSessionId());
			p.setTelephone(fullProvider001.getTelephone());
			p.setUser(fullProvider001.getUser());
			
			try 
			{
				this.connector.updateProvider(p);
				Assert.fail("No exception thrown.");
			} 
			catch (DatabaseException e) 
			{
				Throwable cause = e.getCause();
				
				if (!(cause instanceof DataException))
					Assert.fail("Wrong exception thrown. Expected DataException, but caught " + cause.getClass().getName());
			}
			catch (Exception e)
			{
				Assert.fail("Unexpected exception thrown: " + e.getClass().getName());
			}
		}
}
