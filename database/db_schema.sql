-----------------------------------------------------------------------------------------------------
---	PORSDB database schema for PostgreSQL                                                         ---
-----------------------------------------------------------------------------------------------------

--- ATTENTION: All already existing views and tables will be deleted!

--- USE PORSDB;

---
--- Remove already existing views
---

DROP VIEW IF EXISTS historyview;
DROP VIEW IF EXISTS UserHistoryView;
DROP VIEW IF EXISTS duplicateview;
DROP VIEW IF EXISTS rolerights;

---
---	Remove already existing tables
---

DROP TABLE IF EXISTS Duplicate_Address;
DROP TABLE IF EXISTS Duplicate_Organisation;
DROP TABLE IF EXISTS Duplicate_Provider;
DROP TABLE IF EXISTS DuplicateRecognition;

DROP TABLE IF EXISTS OrganisationHasProvider;
DROP TABLE IF EXISTS OrganisationHasAddress;
DROP TABLE IF EXISTS ProviderHasAddress;

DROP TABLE IF EXISTS LocalId;

DROP TABLE IF EXISTS Organisation;
DROP TABLE IF EXISTS Provider;
DROP TABLE IF EXISTS Address;

DROP TABLE IF EXISTS LocalIdLog;
DROP TABLE IF EXISTS OrganisationLog;
DROP TABLE IF EXISTS ProviderLog;
DROP TABLE IF EXISTS OrganisationHasProviderLog;
DROP TABLE IF EXISTS AddressLog;
DROP TABLE IF EXISTS ProviderHasAddressLog;
DROP TABLE IF EXISTS OrganisationHasAddressLog;

DROP TABLE IF EXISTS RoleHasRightForDomain;
DROP TABLE IF EXISTS PorsUser;
DROP TABLE IF EXISTS UserRole;
DROP TABLE IF EXISTS UserDomain;
DROP TABLE IF EXISTS UserRight;

DROP TABLE IF EXISTS ImportResult;


----------------------------------------------------------------------------------------------------
--- Create right management                                                                      ---
----------------------------------------------------------------------------------------------------

CREATE TABLE UserRight (
	UserRightId SERIAL,
	Name VARCHAR(31) NOT NULL,
	Description TEXT NULL,
	PRIMARY KEY (UserRightId),
	UNIQUE (Name)
);

CREATE TABLE UserDomain (
	UserDomainId SERIAL,
	Name VARCHAR(31) NOT NULL,
	Description TEXT NULL,
	PRIMARY KEY (UserDomainId),
	UNIQUE (Name)
);

CREATE TABLE UserRole (
	UserRoleId SERIAL,
	Name VARCHAR(31) NOT NULL,
	Description TEXT NULL,
	PRIMARY KEY (UserRoleId),
	UNIQUE (Name)
);

CREATE TABLE PorsUser (
	PorsUserId SERIAL,
	UserRoleId INT NOT NULL,
	Name VARCHAR(255) NOT NULL,
	Password VARCHAR(127) NOT NULL,
	IsActive BOOLEAN NOT NULL,
	PRIMARY KEY (PorsUserId),
	FOREIGN KEY (UserRoleId) REFERENCES UserRole (UserRoleId),
	UNIQUE(Name)
);

CREATE TABLE RoleHasRightForDomain (
	UserRoleId INT NOT NULL,
	UserRightId INT NOT NULL,
	UserDomainId INT NOT NULL,
	PRIMARY KEY (UserRoleId, UserRightId, UserDomainId),
	FOREIGN KEY (UserRoleId) REFERENCES UserRole (UserRoleId),
	FOREIGN KEY (UserRightId) REFERENCES UserRight (UserRightId),
	FOREIGN KEY (UserDomainId) REFERENCES UserDomain (UserDomainId)
);

------------------------------------------------------------------------------------------------------
--- Create domain data tables                                                                      ---
------------------------------------------------------------------------------------------------------

CREATE TABLE Organisation (
	RegionalOrganisationId BIGSERIAL,
	PorsUserId INT NOT NULL,
	OID VARCHAR(255) NULL,
	EstablishmentId VARCHAR(255) NULL,
	Name VARCHAR(255) NOT NULL,
	SecondName VARCHAR(255) NULL,
	Description TEXT NULL,
	Email VARCHAR(40) NULL,
	Telephone VARCHAR(32) NULL,
	Fax VARCHAR(32) NULL,
	DeactivationDate DATE NULL,
	DeactivationReasonCode VARCHAR(255) NULL,
	ReactivationDate DATE NULL,
	ReactivationReasonCode VARCHAR(255) NULL,
	LastUpdateDate TIMESTAMP NOT NULL,
	DuplicatesCalculated BOOLEAN NOT NULL,
	"version" BIGINT NOT NULL,
	PRIMARY KEY (RegionalOrganisationId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId),
	UNIQUE (Name),
	UNIQUE (SecondName),
	UNIQUE(OID),
	UNIQUE(EstablishmentId)
);


CREATE TABLE Provider (
	RegionalProviderId BIGSERIAL,
	PorsUserId INT NOT NULL,
	LANR CHAR(9) NULL,
	OID VARCHAR(255) NULL,
	Specialisation VARCHAR(255),
	FirstName VARCHAR(255) NOT NULL,
	LastName VARCHAR(194) NOT NULL,
	MiddleName VARCHAR(30) NULL,
	NamePrefix VARCHAR(30) NULL,
	NameSuffix VARCHAR(30) NULL,
	GenderCode CHAR(1) NOT NULL,
	Birthday DATE NULL,
	Email VARCHAR(40) NULL,
	Telephone VARCHAR(32) NULL,
	Fax VARCHAR(32) NULL,
	DeactivationDate DATE NULL,
	DeactivationReasonCode VARCHAR(255) NULL,
	ReactivationDate DATE NULL,
	ReactivationReasonCode VARCHAR(255) NULL,
	LastUpdateDate TIMESTAMP NOT NULL,
	DuplicatesCalculated BOOLEAN NOT NULL,
	"version" BIGINT NOT NULL,
	PRIMARY KEY (RegionalProviderId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId),
	UNIQUE (LANR),
	UNIQUE (OID),
	UNIQUE (FirstName, LastName, GenderCode, Birthday)
);


CREATE TABLE Address (
	AddressId BIGSERIAL,
	Street VARCHAR(50) NOT NULL,
	HouseNumber VARCHAR(7) NOT NULL,
	ZIPCode VARCHAR(12) NOT NULL,
	City VARCHAR(50) NOT NULL,
	State VARCHAR(255) NULL,
	Country CHAR(2) NOT NULL,
	Additional VARCHAR(127) NOT NULL DEFAULT '',
	DuplicatesCalculated BOOLEAN NOT NULL DEFAULT FALSE,
	PRIMARY KEY (AddressId),
	UNIQUE(Street, HouseNumber, ZIPCode, City, Country, Additional)
);

CREATE TABLE LocalId (
	LocalIdId BIGSERIAL,
	RegionalOrganisationId BIGINT NULL,
	RegionalProviderId BIGINT NULL,
	LocalId VARCHAR(10) NOT NULL,
	Facility VARCHAR(255) NOT NULL,
	Application VARCHAR(255) NOT NULL,
	PRIMARY KEY (LocalIdId),
	FOREIGN KEY (RegionalOrganisationId) REFERENCES Organisation (RegionalOrganisationId),
	FOREIGN KEY (RegionalProviderId) REFERENCES Provider (RegionalProviderId),
	UNIQUE (LocalId, Facility, Application)
);

CREATE TABLE OrganisationHasProvider(
	RegionalOrganisationId BIGINT NOT NULL,
	RegionalProviderId BIGINT NOT NULL,
	PRIMARY KEY (RegionalOrganisationId, RegionalProviderId),
	FOREIGN KEY (RegionalOrganisationId) REFERENCES Organisation (RegionalOrganisationId),
	FOREIGN KEY (RegionalProviderId) REFERENCES Provider (RegionalProviderId)
);

CREATE TABLE OrganisationHasAddress (
	RegionalOrganisationId BIGINT NOT NULL,
	AddressId BIGINT NOT NULL,
	PRIMARY KEY (RegionalOrganisationId, AddressId),
	FOREIGN KEY (RegionalOrganisationId) REFERENCES Organisation (RegionalOrganisationId),
	FOREIGN KEY (AddressId) REFERENCES Address (AddressId)
);

CREATE TABLE ProviderHasAddress (
	RegionalProviderId BIGINT NOT NULL,
	AddressId BIGINT NOT NULL,
	PRIMARY KEY (RegionalProviderId, AddressId),
	FOREIGN KEY (RegionalProviderId) REFERENCES Provider (RegionalProviderId),
	FOREIGN KEY (AddressId) REFERENCES Address (AddressId)
);

------------------------------------------------------------------------------------------------------
--- Create duplicate recognition tables															   ---
------------------------------------------------------------------------------------------------------

CREATE TABLE Duplicate_Address
(
  AddressId1 INT NOT NULL,
  AddressId2 INT NOT NULL,
  "value" double precision NOT NULL,
  "timestamp" TIMESTAMP NOT NULL,
  PRIMARY KEY (addressid1, addressid2)
);

CREATE TABLE Duplicate_Organisation
(
  OrganisationId1 INT NOT NULL,
  OrganisationId2 INT NOT NULL,
  "value" double precision NOT NULL,
  "timestamp" TIMESTAMP NOT NULL,
  PRIMARY KEY (organisationid1, organisationid2)
);

CREATE TABLE Duplicate_Provider
(
  ProviderId1 INT NOT NULL,
  ProviderId2 INT NOT NULL,
  "value" double precision NOT NULL,
  "timestamp" TIMESTAMP NOT NULL,
  PRIMARY KEY (providerid1, providerid2)
);

CREATE TABLE DuplicateRecognition
(
  "name" VARCHAR(30) NOT NULL,
  "value" INT NOT NULL,
  PRIMARY KEY (name)
);

------------------------------------------------------------------------------------------------------
--- Create import tables                                                                           ---
------------------------------------------------------------------------------------------------------

CREATE TABLE ImportResult
(
  JobId BIGSERIAL,
  AddEntries INT NOT NULL,
  "domain" VARCHAR(255) NOT NULL,
  EndDate TIMESTAMP NOT NULL,
  ErrorMessage VARCHAR(255) NULL,
  Processed INT NOT NULL,
  StartDate TIMESTAMP NOT NULL,
  StatusMessage VARCHAR(255),
  UpdateEntries INT NOT NULL,
  PRIMARY KEY (JobId)
);

------------------------------------------------------------------------------------------------------
--- Create logging and history                                                                     ---
------------------------------------------------------------------------------------------------------

CREATE TABLE LocalIdLog (
	LocalIdLogId BIGSERIAL,
	PorsUserId INT NOT NULL,
	LogTime TIMESTAMP NOT NULL,
	SessionId VARCHAR(255) NOT NULL,
	IPAddress VARCHAR(255) NOT NULL,
	TriggerType text NOT NULL,
	TableName name NOT NULL,
	LocalIdId BIGINT NOT NULL,
	OldRegionalOrganisationId BIGINT NULL,
	OldRegionalProviderId BIGINT NULL,
	OldLocalId VARCHAR(10) NULL,
	OldFacility VARCHAR(255) NULL,
	OldApplication VARCHAR(255) NULL,
	NewRegionalOrganisationId BIGINT NULL,
	NewRegionalProviderId BIGINT NULL,
	NewLocalId VARCHAR(10) NULL,
	NewFacility VARCHAR(255) NULL,
	NewApplication VARCHAR(255) NULL,
	PRIMARY KEY (LocalIdLogId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId)
);

CREATE TABLE OrganisationLog (
	OrganisationLogId BIGSERIAL,
	PorsUserId INT NOT NULL,
	LogTime TIMESTAMP NOT NULL,
	SessionId VARCHAR(255) NOT NULL,
	IPAddress VARCHAR(255) NOT NULL,
	TriggerType text NOT NULL,
	TableName name NOT NULL,
	RegionalOrganisationId BIGINT NOT NULL,
	OldPorsUserId INT NULL,
	OldOID varchar(255) NULL,
	OldEstablishmentId varchar(255) NULL,
	OldName VARCHAR(255) NULL,
	OldSecondName VARCHAR(255) NULL,
	OldDescription Text NULL,
	OldEMail VARCHAR(40) NULL,
	OldTelephone VARCHAR(32) NULL,
	OldFax VARCHAR(32) NULL,
	OldDeactivationDate DATE NULL,
	OldDeactivationReasonCode VARCHAR(255) NULL,
	OldReactivationDate DATE NULL,
	OldReactivationReasonCode VARCHAR(255) NULL,
	OldLastUpdateDate TIMESTAMP NULL,
	NewPorsUserId INT NULL,
	NewOID varchar(255) NULL,
	NewEstablishmentId varchar(255) NULL,
	NewName VARCHAR(255) NULL,
	NewSecondName VARCHAR(255) NULL,
	NewDescription Text NULL,
	NewEmail VARCHAR(40) NULL,
	NewTelephone VARCHAR(32) NULL,
	NewFax VARCHAR(32) NULL,
	NewDeactivationDate DATE NULL,
	NewDeactivationReasonCode VARCHAR(255) NULL,
	NewReactivationDate DATE NULL,
	NewReactivationReasonCode VARCHAR(255) NULL,
	NewLastUpdateDate TIMESTAMP NULL,
	PRIMARY KEY (OrganisationLogId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId)
);

CREATE TABLE ProviderLog (
	ProviderLogId BIGSERIAL,
	PorsUserId INT NOT NULL,
	LogTime TIMESTAMP NOT NULL,
	SessionId VARCHAR(255) NOT NULL,
	IPAddress VARCHAR(255) NOT NULL,
	TriggerType text NOT NULL,
	TableName name NOT NULL,
	RegionalProviderId BIGINT NOT NULL,
	OldPorsUserId INT NULL,
	OldLANR CHAR(9) NULL,
	OldOID VARCHAR(255) NULL,
	OldSpecialisation VARCHAR(255) NULL,
	OldFirstName VARCHAR(255) NULL,
	OldLastName VARCHAR(194) NULL,
	OldMiddleName VARCHAR(30) NULL,
	OldNamePrefix VARCHAR(30) NULL,
	OldNameSuffix VARCHAR(30) NULL,
	OldGenderCode CHAR(1) NULL,
	OldBirthday DATE NULL,
	OldEmail VARCHAR(40) NULL,
	OldTelephone VARCHAR(32) NULL,
	OldFax VARCHAR(32) NULL,
	OldDeactivationDate DATE NULL,
	OldDeactivationReasonCode VARCHAR(255) NULL,
	OldReactivationDate DATE NULL,
	OldReactivationReasonCode VARCHAR(255) NULL,
	OldLastUpdateDate TIMESTAMP NULL,
	NewPorsUserId INT NULL,
	NewLANR CHAR(9) NULL,
	NewOID varchar(255) NULL,
	NewSpecialisation VARCHAR(255) NULL,
	NewFirstName VARCHAR(255) NULL,
	NewLastName VARCHAR(194) NULL,
	NewMiddleName VARCHAR(30) NULL,
	NewNamePrefix VARCHAR(30) NULL,
	NewNameSuffix VARCHAR(30) NULL,
	NewGenderCode CHAR(1) NULL,
	NewBirthday DATE NULL,
	NewEmail VARCHAR(40) NULL,
	NewTelephone VARCHAR(32) NULL,
	NewFax VARCHAR(32) NULL,
	NewDeactivationDate DATE NULL,
	NewDeactivationReasonCode VARCHAR(255) NULL,
	NewReactivationDate DATE NULL,
	NewReactivationReasonCode VARCHAR(255) NULL,
	NewLastUpdateDate TIMESTAMP NULL,
	PRIMARY KEY (ProviderLogId)	
);

CREATE TABLE OrganisationHasProviderLog (
	OrganisationHasProviderLogId BIGSERIAL,
	PorsUserId INT NOT NULL,
	LogTime TIMESTAMP NOT NULL,
	SessionId VARCHAR(255) NOT NULL,
	IPAddress VARCHAR(255) NOT NULL,
	TriggerType text NOT NULL,
	TableName name NOT NULL,
	OldRegionalOrganisationId BIGINT NULL,
	OldRegionalProviderId BIGINT NULL,
	NewRegionalOrganisationId BIGINT NULL,
	NewRegionalProviderId BIGINT NULL,
	PRIMARY KEY (OrganisationHasProviderLogId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId)
);

CREATE TABLE AddressLog (
	AddressLogId BIGSERIAL,
	PorsUserId INT NULL,
	LogTime TIMESTAMP NOT NULL,
	SessionId VARCHAR(255) NOT NULL,
	IPAddress VARCHAR(255) NOT NULL,
	TriggerType text NOT NULL,
	TableName name NOT NULL,
	AddressId BIGINT NOT NULL,
	OldStreet VARCHAR(50) NULL,
	OldHouseNumber VARCHAR(7) NULL,
	OldZIPCode VARCHAR(12) NULL,
	OldCity VARCHAR(50) NULL,
	OldState VARCHAR(255) NULL,
	OldCountry CHAR(2) NULL,
	OldAdditional VARCHAR(127) NULL,
	NewStreet VARCHAR(50) NULL,
	NewHouseNumber VARCHAR(7) NULL,
	NewZIPCode VARCHAR(12) NULL,
	NewCity VARCHAR(50) NULL,
	NewState VARCHAR(255) NULL,
	NewCountry CHAR(2) NULL,
	NewAdditional VARCHAR(127) NULL,
	PRIMARY KEY (AddressLogId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId)
);

CREATE TABLE ProviderHasAddressLog (
	ProviderHasAddressLogId BIGSERIAL,
	PorsUserId INT NOT NULL,
	LogTime TIMESTAMP NOT NULL,
	SessionId VARCHAR(255) NOT NULL,
	IPAddress VARCHAR(255) NOT NULL,
	TriggerType text NOT NULL,
	TableName name NOT NULL,
	OldRegionalProviderId BIGINT NULL,
	OldAddressId BIGINT NULL,
	NewRegionalProviderId BIGINT NULL,
	NewAddressId BIGINT NULL,
	PRIMARY KEY (ProviderHasAddressLogId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId)
);

CREATE TABLE OrganisationHasAddressLog (
	OrganisationHasAddressLogId BIGSERIAL,
	PorsUserId INT NOT NULL,
	LogTime TIMESTAMP NOT NULL,
	SessionId VARCHAR(255) NOT NULL,
	IPAddress VARCHAR(255) NOT NULL,
	TriggerType text NOT NULL,
	TableName name NOT NULL,
	OldRegionalOrganisationId BIGINT NULL,
	OldAddressId BIGINT NULL,
	NewRegionalOrganisationId BIGINT NULL,
	NewAddressId BIGINT NULL,
	PRIMARY KEY (OrganisationHasAddressLogId),
	FOREIGN KEY (PorsUserId) REFERENCES PorsUser (PorsUserId)
);

------------------------------------------------------------------------------------------------------
--- Create history view                                                                            ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW historyview AS 
((((( SELECT p.porsuserid, u.name, p.triggertype, p.logtime, 'Provider' AS domain, p.providerlogid AS id, p.sessionid, p.ipaddress
   FROM providerlog p
   JOIN porsuser u ON p.porsuserid = u.porsuserid
UNION 
 SELECT p.porsuserid, u.name, p.triggertype, p.logtime, 'Organisation' AS domain, p.organisationlogid AS id, p.sessionid, p.ipaddress
   FROM organisationlog p
   JOIN porsuser u ON p.porsuserid = u.porsuserid)
UNION 
 SELECT a.porsuserid, u.name, a.triggertype, a.logtime, 'Address' AS domain, a.addresslogid AS id, a.sessionid, a.ipaddress
   FROM addresslog a
   JOIN porsuser u ON a.porsuserid = u.porsuserid)
UNION 
 SELECT id.porsuserid, u.name, id.triggertype, id.logtime, 'LocalID' AS domain, id.localidlogid AS id, id.sessionid, id.ipaddress
   FROM localidlog id
   JOIN porsuser u ON id.porsuserid = u.porsuserid)
UNION
 SELECT pha.porsuserid, u.name, pha.triggertype, pha.logtime, 'ProviderHasAddress' AS domain, pha.providerhasaddresslogid AS id, pha.sessionid, pha.ipaddress
   FROM providerhasaddresslog pha
   JOIN porsuser u on pha.porsuserid = u.porsuserid)
UNION
 SELECT oha.porsuserid, u.name, oha.triggertype, oha.logtime, 'OrganisationHasAddress' AS domain, oha.organisationhasaddresslogid AS id, oha.sessionid, oha.ipaddress
   FROM organisationhasaddresslog oha
   JOIN porsuser u on oha.porsuserid = u.porsuserid)
UNION
 SELECT ohp.porsuserid, u.name, ohp.triggertype, ohp.logtime, 'OrganisationHasProvider' AS domain, ohp.organisationhasproviderlogid AS id, ohp.sessionid, ohp.ipaddress
   FROM organisationhasproviderlog ohp
   JOIN porsuser u on ohp.porsuserid = u.porsuserid;
   
   
   
------------------------------------------------------------------------------------------------------
--- Create user history view                                                                            ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW userhistoryview AS 
(((((((((( SELECT pl.porsuserid AS editingporsuserid, u1.name AS editingporsusername, plx.porsuserid AS owningporsuserid, plx.name AS owningporsusername, pl.triggertype AS action, pl.logtime, 'Provider' AS domain, pl.providerlogid AS domainid, pl.sessionid, pl.ipaddress
   FROM providerlog pl
   JOIN porsuser u1 ON pl.porsuserid = u1.porsuserid, ( SELECT p.regionalproviderid, p.porsuserid, u.name
      FROM provider p
   JOIN porsuser u ON p.porsuserid = u.porsuserid) plx
  WHERE pl.regionalproviderid = plx.regionalproviderid
UNION 
 SELECT ol.porsuserid AS editingporsuserid, u1.name AS editingporsusername, olx.porsuserid AS owningporsuserid, olx.name AS owningporsusername, ol.triggertype AS action, ol.logtime, 'Organisation' AS domain, ol.organisationlogid AS domainid, ol.sessionid, ol.ipaddress
   FROM organisationlog ol
   JOIN porsuser u1 ON ol.porsuserid = u1.porsuserid, ( SELECT o.regionalorganisationid, o.porsuserid, u.name
      FROM organisation o
   JOIN porsuser u ON o.porsuserid = u.porsuserid) olx
  WHERE ol.regionalorganisationid = olx.regionalorganisationid)
UNION 
 SELECT al.porsuserid AS editingporsuserid, u1.name AS editingporsusername, alp.porsuserid AS owningporsuserid, alp.name AS owningporsusername, al.triggertype AS action, al.logtime, 'Address' AS domain, al.addresslogid AS domainid, al.sessionid, al.ipaddress
   FROM addresslog al
   JOIN porsuser u1 ON al.porsuserid = u1.porsuserid, ( SELECT p.porsuserid, u.name, a.addressid
      FROM providerhasaddress a
   JOIN provider p ON a.regionalproviderid = p.regionalproviderid
   JOIN porsuser u ON p.porsuserid = u.porsuserid) alp
  WHERE al.addressid = alp.addressid)
UNION 
 SELECT al.porsuserid AS editingporsuserid, u1.name AS editingporsusername, alo.porsuserid AS owningporsuserid, alo.name AS owningporsusername, al.triggertype AS action, al.logtime, 'Address' AS domain, al.addresslogid AS domainid, al.sessionid, al.ipaddress
   FROM addresslog al
   JOIN porsuser u1 ON al.porsuserid = u1.porsuserid, ( SELECT o.porsuserid, u.name, a.addressid
      FROM organisationhasaddress a
   JOIN organisation o ON a.regionalorganisationid = o.regionalorganisationid
   JOIN porsuser u ON o.porsuserid = u.porsuserid) alo
  WHERE al.addressid = alo.addressid)
UNION 
 SELECT id.porsuserid AS editingporsuserid, u1.name AS editingporsusername, lp.porsuserid AS owningporsuserid, lp.name AS owningporsusername, id.triggertype AS action, id.logtime, 'LocalID' AS domain, id.localidlogid AS domainid, id.sessionid, id.ipaddress
   FROM localidlog id
   JOIN porsuser u1 ON id.porsuserid = u1.porsuserid, ( SELECT l.regionalproviderid, p.porsuserid, u.name, l.localidid
      FROM localid l
   JOIN provider p ON l.regionalproviderid = p.regionalproviderid
   JOIN porsuser u ON p.porsuserid = u.porsuserid) lp
  WHERE id.localidid = lp.localidid)
UNION 
 SELECT id.porsuserid AS editingporsuserid, u1.name AS editingporsusername, lo.porsuserid AS owningporsuserid, lo.name AS owningporsusername, id.triggertype AS action, id.logtime, 'LocalID' AS domain, id.localidlogid AS domainid, id.sessionid, id.ipaddress
   FROM localidlog id
   JOIN porsuser u1 ON id.porsuserid = u1.porsuserid, ( SELECT l.regionalproviderid, o.porsuserid, u.name, l.localidid
      FROM localid l
   JOIN organisation o ON l.regionalorganisationid = o.regionalorganisationid
   JOIN porsuser u ON o.porsuserid = u.porsuserid) lo
  WHERE id.localidid = lo.localidid)
UNION
 SELECT pha.porsuserid AS editingporsuserid, u1.name AS editingporsusername, phax.porsuserid AS owningporsuserid, phax.name AS owningporsusername, pha.triggertype AS action, pha.logtime, 'ProviderHasAddress' AS domain, pha.providerhasaddresslogid AS domainid, pha.sessionid, pha.ipaddress
   FROM providerhasaddresslog pha JOIN porsuser u1 
   ON pha.porsuserid = u1.porsuserid,
   (
	SELECT pa.regionalproviderid, p.porsuserid, u.name 
	FROM (providerhasaddress pa  JOIN provider p
	ON pa.regionalproviderid = p.regionalproviderid)
	JOIN porsuser u ON p.porsuserid = u.porsuserid
   ) phax
 WHERE pha.oldregionalproviderid = phax.regionalproviderid)
UNION
 SELECT pha.porsuserid AS editingporsuserid, u1.name AS editingporsusername, phax.porsuserid AS owningporsuserid, phax.name AS owningporsusername, pha.triggertype AS action, pha.logtime, 'ProviderHasAddress' AS domain, pha.providerhasaddresslogid AS domainid, pha.sessionid, pha.ipaddress
   FROM providerhasaddresslog pha JOIN porsuser u1 
   ON pha.porsuserid = u1.porsuserid,
   (
	SELECT pa.regionalproviderid, p.porsuserid, u.name 
	FROM (providerhasaddress pa  JOIN provider p
	ON pa.regionalproviderid = p.regionalproviderid)
	JOIN porsuser u ON p.porsuserid = u.porsuserid
   ) phax
 WHERE pha.newregionalproviderid = phax.regionalproviderid)
UNION 
 SELECT oha.porsuserid AS editingporsuserid, u1.name AS editingporsusername, ohax.porsuserid AS owningporsuserid, ohax.name AS owningporsusername, oha.triggertype AS action, oha.logtime, 'OrganisationHasAddress' AS domain, oha.organisationhasaddresslogid AS domainid, oha.sessionid, oha.ipaddress
   FROM OrganisationHasAddressLog oha JOIN porsuser u1 
   ON oha.porsuserid = u1.porsuserid,
   (
	SELECT oa.regionalorganisationid, o.porsuserid, u.name 
	FROM (OrganisationHasAddress oa  JOIN organisation o
	ON oa.regionalorganisationid = o.regionalorganisationid) 
	JOIN porsuser u ON o.porsuserid = u.porsuserid
   ) ohax
 WHERE oha.oldregionalorganisationid = ohax.regionalorganisationid) 
UNION 
 SELECT oha.porsuserid AS editingporsuserid, u1.name AS editingporsusername, ohax.porsuserid AS owningporsuserid, ohax.name AS owningporsusername, oha.triggertype AS action, oha.logtime, 'OrganisationHasAddress' AS domain, oha.organisationhasaddresslogid AS domainid, oha.sessionid, oha.ipaddress
   FROM OrganisationHasAddressLog oha JOIN porsuser u1 
   ON oha.porsuserid = u1.porsuserid,
   (
	SELECT oa.regionalorganisationid, o.porsuserid, u.name 
	FROM (OrganisationHasAddress oa  JOIN organisation o
	ON oa.regionalorganisationid = o.regionalorganisationid) 
	JOIN porsuser u ON o.porsuserid = u.porsuserid
   ) ohax
 WHERE oha.newregionalorganisationid = ohax.regionalorganisationid)
UNION
 SELECT ohp.porsuserid AS editingporsuserid, u1.name AS editingporsusername, ohpx.porsuserid AS owningporsuserid, ohpx.name AS owningporsusername, ohp.triggertype AS action, ohp.logtime, 'OrganisationHasProvider' AS domain, ohp.organisationhasproviderlogid AS domainid, ohp.sessionid, ohp.ipaddress
   FROM OrganisationHasProviderLog ohp JOIN porsuser u1 
   ON ohp.porsuserid = u1.porsuserid,
   (
	SELECT op.regionalorganisationid, o.porsuserid, u.name 
	FROM (OrganisationHasProvider op  JOIN organisation o
	ON op.regionalorganisationid = o.regionalorganisationid)  
	JOIN porsuser u ON o.porsuserid = u.porsuserid
   ) ohpx
 WHERE ohp.newregionalorganisationid = ohpx.regionalorganisationid)
UNION
 SELECT ohp.porsuserid AS editingporsuserid, u1.name AS editingporsusername, ohpx.porsuserid AS owningporsuserid, ohpx.name AS owningporsusername, ohp.triggertype AS action, ohp.logtime, 'OrganisationHasProvider' AS domain, ohp.organisationhasproviderlogid AS domainid, ohp.sessionid, ohp.ipaddress
   FROM OrganisationHasProviderLog ohp JOIN porsuser u1 
   ON ohp.porsuserid = u1.porsuserid,
   (
	SELECT op.regionalorganisationid, o.porsuserid, u.name 
	FROM (OrganisationHasProvider op  JOIN organisation o
	ON op.regionalorganisationid = o.regionalorganisationid)  
	JOIN porsuser u ON o.porsuserid = u.porsuserid
   ) ohpx
 WHERE ohp.oldregionalorganisationid = ohpx.regionalorganisationid;
 
 
------------------------------------------------------------------------------------------------------
--- Create duplicate view                                                                            ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW duplicateview AS 
( SELECT duplicate_address.addressid1 AS id1, duplicate_address.addressid2 AS id2, 'Address' AS domain, CAST (ROUND(CAST (duplicate_address.value AS numeric), 2) AS double precision) AS value, duplicate_address."timestamp"
   FROM duplicate_address
UNION 
 SELECT duplicate_organisation.organisationid1 AS id1, duplicate_organisation.organisationid2 AS id2, 'Organisation' AS domain, CAST (Round(CAST (duplicate_organisation.value AS numeric), 2) AS double precision) AS value, duplicate_organisation."timestamp"
   FROM duplicate_organisation)
UNION 
 SELECT duplicate_provider.providerid1 AS id1, duplicate_provider.providerid2 AS id2, 'Provider' AS domain, CAST (Round(CAST (duplicate_provider.value AS numeric), 2) AS double precision) AS value, duplicate_provider."timestamp"
   FROM duplicate_provider;
   
   
------------------------------------------------------------------------------------------------------
--- Create rolerights view                                                                           ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW rolerights AS 
SELECT urole.name AS rolename, uright.name AS rightname, udomain.name AS domainname
FROM rolehasrightfordomain AS rrd, userrole AS urole, userright AS uright, userdomain AS udomain
WHERE rrd.userroleid = urole.userroleid 
	AND rrd.userrightid = uright.userrightid 
	AND rrd.userdomainid = udomain.userdomainid
ORDER BY urole.name, uright.userrightid, udomain.userdomainid;

-- Rule to enable inserts by name in view rolerights (insert into table rolehasrightfordomain instead)
CREATE OR REPLACE RULE roleinsert AS ON INSERT
    TO rolerights
   DO INSTEAD 
		INSERT INTO rolehasrightfordomain (userroleid, userrightid, userdomainid) 
				VALUES ((SELECT userroleid FROM userrole WHERE name = new.rolename),
					(SELECT userrightid FROM userright WHERE name= new.rightname), 
					(SELECT userdomainid FROM userdomain WHERE name = new.domainname));
					
					
					
------------------------------------------------------------------------------------------------------
--- Function jaro()                                                                       ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION jaro(ying text, yang text)
  RETURNS double precision AS
'/usr/local/pgsql/pors/jaro.so', 'jaro'
  LANGUAGE 'c' VOLATILE
  COST 1;



------------------------------------------------------------------------------------------------------
--- Create Language plpgsql                                                                      ---
------------------------------------------------------------------------------------------------------

CREATE PROCEDURAL LANGUAGE plpgsql;
  
------------------------------------------------------------------------------------------------------
--- Function duplicate_address(bigint,bigint)                                                                       ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION duplicate_address(address1 bigint, address2 bigint)
  RETURNS double precision AS
$BODY$
DECLARE
   count integer;
   sum double precision;
   add1 address%ROWTYPE;
   add2 address%ROWTYPE;

   weightStreet integer;
   weightHousenumber integer;
   weightZipcode integer;
   weightCity integer;
   weightState integer;
   weightCountry integer;
   weightAdditional integer;
   
BEGIN
 SELECT INTO weightStreet value FROM duplicaterecognition WHERE name='a.street';
 SELECT INTO weightHousenumber value FROM duplicaterecognition WHERE name='a.housenumber';
 SELECT INTO weightZipcode value FROM duplicaterecognition WHERE name='a.zipcode';
 SELECT INTO weightCity value FROM duplicaterecognition WHERE name='a.city';
 SELECT INTO weightState value FROM duplicaterecognition WHERE name='a.state';
 SELECT INTO weightCountry value FROM duplicaterecognition WHERE name='a.country';
 SELECT INTO weightAdditional value FROM duplicaterecognition WHERE name='a.additional';

 SELECT INTO add1 * FROM address WHERE addressid=address1;
 SELECT INTO add2 * FROM address WHERE addressid=address2;

 sum = 0.0;
 count = 0;
 IF ((add1.street != '') AND (add2.street != '') AND (weightStreet!=0)) THEN 
	count = count + weightStreet;
 	sum = sum + (jaro(add1.street, add2.street) * weightStreet);
 END IF;

 IF ((add1.housenumber != '') AND (add2.housenumber != '') AND (weightHousenumber!=0)) THEN 
	count=count + weightHousenumber;
	sum = sum + (jaro(add1.housenumber, add2.housenumber) * weightHousenumber);
 END IF;

 IF ((add1.zipcode != '') AND (add2.zipcode != '') AND (weightZipcode!=0)) THEN 
	count=count + weightZipcode;
	sum = sum + (jaro(add1.zipcode, add2.zipcode) * weightZipcode);
 END IF;
 
 IF ((add1.city != '') AND (add2.city != '') AND (weightCity!=0)) THEN 
	count = count + weightCity;
	sum = sum + (jaro(add1.city, add2.city) * weightCity);
 END IF;

 IF ((add1.state != '') AND (add2.state != '') AND (weightState!=0)) THEN 
	count = count + weightState;
	sum = sum + (jaro(add1.state, add2.state) * weightState);
 END IF;

 IF ((add1.country != '') AND (add2.country != '') AND (weightCountry!=0)) THEN 
	count = count + weightCountry;
	sum = sum + (jaro(add1.country, add2.country) * weightCountry);
 END IF;

 IF ((add1.additional != null) AND (add2.additional != null) AND (weightAdditional!=0)) THEN 
	count = count + weightAdditional;
	sum = sum + (jaro(add1.additional, add2.additional) * weightAdditional);
 END IF;

RETURN (sum / count);

END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
 
------------------------------------------------------------------------------------------------------
--- Function duplicate_address()                                                                       ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION duplicate_address()
  RETURNS integer AS
$BODY$
DECLARE
   result double precision;
   add1 address%ROWTYPE;
   add2 address%ROWTYPE;

   thresholdUpper integer;
   thresholdLower integer;
   
BEGIN
 SELECT INTO thresholdUpper value FROM duplicaterecognition WHERE name='upperthreshold';
 SELECT INTO thresholdLower value FROM duplicaterecognition WHERE name='lowerthreshold';
 
 FOR add1 IN SELECT * FROM address WHERE duplicatescalculated = false LOOP
	FOR add2 IN SELECT * FROM address WHERE duplicatescalculated = true LOOP
	result = duplicate_address(add1.addressid, add2.addressid);
		IF (((result*100)>thresholdLower)AND((result*100)<thresholdUpper)) THEN
			INSERT INTO duplicate_address VALUES (add1.addressid, add2.addressid, result, NOW());
		END IF;
	END LOOP;
	UPDATE address SET duplicatescalculated = true WHERE addressid=add1.addressid;
 END LOOP;

return 0;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
------------------------------------------------------------------------------------------------------
--- Function duplicate_provider()                                                                       ---
------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION duplicate_provider()
  RETURNS integer AS
$BODY$
DECLARE
   sum double precision;
   count integer;
   result double precision;
   bestAddMatch double precision;
   addMatch double precision;
   prov1 provider%ROWTYPE;
   prov2 provider%ROWTYPE;
   addProv1 providerhasaddress%ROWTYPE;
   addProv2 providerhasaddress%ROWTYPE;

   weightFirstname integer;
   weightLastname integer;
   weightMiddlename integer;
   weightNameprefix integer;
   weightNamesuffix integer;
   weightGendercode integer;
   weightBirthday integer;
   weightEmail integer;
   weightTelephone integer;
   weightFax integer;
   weightAddress integer;
   weightSpecialisation integer;
   thresholdUpper integer;
   thresholdLower integer;
   
BEGIN
 SELECT INTO weightFirstname value FROM duplicaterecognition WHERE name='p.firstname';
 SELECT INTO weightLastname value FROM duplicaterecognition WHERE name='p.lastname';
 SELECT INTO weightMiddlename value FROM duplicaterecognition WHERE name='p.middlename';
 SELECT INTO weightNameprefix value FROM duplicaterecognition WHERE name='p.nameprefix';
 SELECT INTO weightNamesuffix value FROM duplicaterecognition WHERE name='p.namesuffix';
 SELECT INTO weightGendercode value FROM duplicaterecognition WHERE name='p.gendercode';
 SELECT INTO weightBirthday value FROM duplicaterecognition WHERE name='p.birthday';
 SELECT INTO weightEmail value FROM duplicaterecognition WHERE name='p.email';
 SELECT INTO weightTelephone value FROM duplicaterecognition WHERE name='p.telephone';
 SELECT INTO weightFax value FROM duplicaterecognition WHERE name='p.fax';
 SELECT INTO weightAddress value FROM duplicaterecognition WHERE name='p.address';
 SELECT INTO thresholdUpper value FROM duplicaterecognition WHERE name='upperthreshold';
 SELECT INTO thresholdLower value FROM duplicaterecognition WHERE name='lowerthreshold';
 SELECT INTO weightSpecialisation value FROM duplicaterecognition WHERE name='p.specialisation';

 
 FOR prov1 IN SELECT * FROM provider WHERE duplicatescalculated = false LOOP
	FOR prov2 IN SELECT * FROM provider WHERE duplicatescalculated = true LOOP
		sum = 0.0;
		count = 0;
		addMatch = 0.0;
		bestAddMatch = 0.0;
		IF ((prov1.firstname != '') AND (prov2.firstname != '') AND (weightFirstname!=0)) THEN 
			count = count + weightFirstname;
			sum = sum + (jaro(prov1.firstname, prov2.firstname) * weightFirstname);
		END IF;
		
		IF ((prov1.lastname != '') AND (prov2.lastname != '') AND (weightLastname!=0)) THEN 
			count=count + weightLastname;
			sum = sum + (jaro(prov1.lastname, prov2.lastname) * weightLastname);
		END IF;
		
		IF ((prov1.middlename != '') AND (prov2.middlename != '') AND (weightMiddlename!=0)) THEN 
			count=count + weightMiddlename;
			sum = sum + (jaro(prov1.middlename, prov2.middlename) * weightMiddlename);
		END IF;

		IF ((NOT((prov1.nameprefix != '') AND (prov2.nameprefix != ''))) AND (weightNameprefix!=0)) THEN 
			count = count + weightNameprefix;
			sum = sum + (jaro(prov1.nameprefix, prov2.nameprefix) * weightNameprefix);
		END IF;

		IF ((prov1.namesuffix != '') AND (prov2.namesuffix != '') AND (weightNamesuffix!=0)) THEN 
			count = count + weightNamesuffix;
			sum = sum + (jaro(prov1.namesuffix, prov2.namesuffix) * weightNamesuffix);
		END IF;


		IF ((prov1.specialisation != '') AND (prov2.specialisation != '') AND (weightSpecialisation!=0)) THEN 
			count = count + weightSpecialisation;
			sum = sum + (jaro(prov1.specialisation, prov2.specialisation) * weightSpecialisation);
		END IF;


		IF ((prov1.gendercode != '') AND (prov2.gendercode != '') AND (weightGendercode!=0)) THEN 
			count = count + weightGendercode;
			sum = sum + (jaro(prov1.gendercode, prov2.gendercode) * weightGendercode);
		END IF;

		IF ((prov1.birthday != null) AND (prov2.birthday != null) AND (weightBirthday!=0)) THEN 
			count = count + weightBirthday;
			sum = sum + (jaro(prov1.birthday, prov2.birthday) * weightBirthday);
		END IF;

		IF ((prov1.email != '') AND (prov2.email != '') AND (weightEmail!=0)) THEN 
			count = count + weightEmail;
			sum = sum + (jaro(prov1.email, prov2.email) * weightEmail);
		END IF;

		IF ((prov1.telephone != '') AND (prov2.telephone != '') AND (weightTelephone!=0)) THEN 
			count = count + weightTelephone;
			sum = sum + (jaro(prov1.telephone, prov2.telephone) * weightTelephone);
		END IF;

		IF ((prov1.fax != '') AND (prov2.fax != '') AND (weightFax!=0)) THEN 
			count = count + weightFax;
			sum = sum + (jaro(prov1.fax, prov2.fax) * weightFax);
		END IF;

		FOR addProv1 IN SELECT * FROM providerhasaddress WHERE regionalproviderid=prov1.regionalproviderid LOOP
			FOR addProv2 IN SELECT * FROM providerhasaddress WHERE regionalproviderid=prov2.regionalproviderid LOOP
				addMatch = duplicate_address(addProv1.addressid, addProv2.addressid);
				IF(addMatch > bestAddMatch) THEN
					bestAddMatch = addMatch;
				END IF;
			END LOOP;
		END LOOP;

		IF ( ( bestAddMatch > 0.0) AND (weightAddress!=0)) THEN
			count = count + weightAddress;
			sum = sum + (bestAddMatch * weightAddress);
		END IF;

		IF ( count > 0) THEN
			result = sum / count;
			IF (((result*100)>thresholdLower)AND((result*100)<thresholdUpper)) THEN
				INSERT INTO duplicate_provider VALUES (prov1.regionalproviderid, prov2.regionalproviderid, result, NOW());
			END IF;
		END IF;
	END LOOP;
	UPDATE provider SET duplicatescalculated = true WHERE regionalproviderid=prov1.regionalproviderid;
 END LOOP;
return 0;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;

  
  
  
------------------------------------------------------------------------------------------------------
--- Function duplicate_organisation()                                                                       ---
------------------------------------------------------------------------------------------------------
  
  CREATE OR REPLACE FUNCTION duplicate_organisation()
  RETURNS integer AS
$BODY$
DECLARE
   sum double precision;
   count integer;
   result double precision;
   bestAddMatch double precision;
   addMatch double precision;
   org1 organisation%ROWTYPE;
   org2 organisation%ROWTYPE;
   addOrg1 organisationhasaddress%ROWTYPE;
   addOrg2 organisationhasaddress%ROWTYPE;

   weightName integer;
   weightSecondname integer;
   weightEmail integer;
   weightTelephone integer;
   weightFax integer;
   weightAddress integer;
   thresholdUpper integer;
   thresholdLower integer;
   
BEGIN
 SELECT INTO weightName value FROM duplicaterecognition WHERE name='o.name';
 SELECT INTO weightSecondname value FROM duplicaterecognition WHERE name='o.secondname';
 SELECT INTO weightEmail value FROM duplicaterecognition WHERE name='o.email';
 SELECT INTO weightTelephone value FROM duplicaterecognition WHERE name='o.telephone';
 SELECT INTO weightFax value FROM duplicaterecognition WHERE name='o.fax';
 SELECT INTO weightAddress value FROM duplicaterecognition WHERE name='o.address';
 SELECT INTO thresholdUpper value FROM duplicaterecognition WHERE name='upperthreshold';
 SELECT INTO thresholdLower value FROM duplicaterecognition WHERE name='lowerthreshold';
 
 FOR org1 IN SELECT * FROM organisation LOOP
	FOR org2 IN SELECT * FROM organisation LOOP
		sum = 0.0;
		count = 0;
		addMatch = 0.0;
		bestAddMatch = 0.0;
		count = count + weightName;
		sum = sum + (jaro(org1.name, org2.name) * weightName);

		IF ( count > 0) THEN
			result = sum / count;
			IF (((result*100)>thresholdLower)AND((result*100)<thresholdUpper)) THEN
				INSERT INTO duplicate_organisation VALUES (org1.regionalorganisationid, org2.regionalorganisationid, result, NOW());
			END IF;
		END IF;
	END LOOP;
	UPDATE organisation SET duplicatescalculated = true WHERE regionalorganisationid=org1.regionalorganisationid;
  END LOOP;
return 0;

END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  


------------------------------------------------------------------------------------------------------
--- Function duplicate_address_opt(bigint,bigint,int)                                                                       ---
------------------------------------------------------------------------------------------------------
  
  
  CREATE OR REPLACE FUNCTION duplicate_address_opt(address1 bigint, address2 bigint, minweight integer)
  RETURNS double precision AS
$BODY$
DECLARE
   count integer;
   sum double precision;
   add1 address%ROWTYPE;
   add2 address%ROWTYPE;

   weightStreet integer;
   weightHousenumber integer;
   weightZipcode integer;
   weightCity integer;
   weightState integer;
   weightCountry integer;
   weightAdditional integer;
   
BEGIN
 SELECT INTO weightStreet value FROM duplicaterecognition WHERE name='a.street';
 SELECT INTO weightHousenumber value FROM duplicaterecognition WHERE name='a.housenumber';
 SELECT INTO weightZipcode value FROM duplicaterecognition WHERE name='a.zipcode';
 SELECT INTO weightCity value FROM duplicaterecognition WHERE name='a.city';
 SELECT INTO weightState value FROM duplicaterecognition WHERE name='a.state';
 SELECT INTO weightCountry value FROM duplicaterecognition WHERE name='a.country';
 SELECT INTO weightAdditional value FROM duplicaterecognition WHERE name='a.additional';

 SELECT INTO add1 * FROM address WHERE addressid=address1;
 SELECT INTO add2 * FROM address WHERE addressid=address2;

 sum = 0.0;
 count = 0;
 IF ((add1.street != '') AND (add2.street != '') AND (weightStreet!=0)) THEN 
	count = count + weightStreet;
 	sum = sum + (jaro(add1.street, add2.street) * weightStreet);
 END IF;

 IF ((add1.housenumber != '') AND (add2.housenumber != '') AND (weightHousenumber!=0)) THEN 
	count=count + weightHousenumber;
	sum = sum + (jaro(add1.housenumber, add2.housenumber) * weightHousenumber);
 END IF;

 IF ((add1.zipcode != '') AND (add2.zipcode != '') AND (weightZipcode!=0)) THEN 
	count=count + weightZipcode;
	sum = sum + (jaro(add1.zipcode, add2.zipcode) * weightZipcode);
 END IF;
 
 IF ((add1.city != '') AND (add2.city != '') AND (weightCity!=0)) THEN 
	count = count + weightCity;
	sum = sum + (jaro(add1.city, add2.city) * weightCity);
 END IF;

 IF ((add1.state != '') AND (add2.state != '') AND (weightState!=0)) THEN 
	count = count + weightState;
	sum = sum + (jaro(add1.state, add2.state) * weightState);
 END IF;

 IF ((add1.country != '') AND (add2.country != '') AND (weightCountry!=0)) THEN 
	count = count + weightCountry;
	sum = sum + (jaro(add1.country, add2.country) * weightCountry);
 END IF;

 IF ((add1.additional != null) AND (add2.additional != null) AND (weightAdditional!=0)) THEN 
	count = count + weightAdditional;
	sum = sum + (jaro(add1.additional, add2.additional) * weightAdditional);
 END IF;

RETURN (sum / count);

END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
------------------------------------------------------------------------------------------------------
--- Function duplicate_provider_opt()                                                                       ---
------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION duplicate_provider_opt()
  RETURNS integer AS
$BODY$
DECLARE
   sum double precision;
   count integer;
   result double precision;
   bestAddMatch double precision;
   addMatch double precision;
   prov1 provider%ROWTYPE;
   prov2 provider%ROWTYPE;
   addProv1 providerhasaddress%ROWTYPE;
   addProv2 providerhasaddress%ROWTYPE;

   weightFirstname integer;
   weightLastname integer;
   weightMiddlename integer;
   weightNameprefix integer;
   weightNamesuffix integer;
   weightGendercode integer;
   weightBirthday integer;
   weightEmail integer;
   weightTelephone integer;
   weightFax integer;
   weightAddress integer;
   weightSpecialisation integer;
   sumWeights integer;
   part double precision;
   thresholdUpper integer;
   thresholdLower integer;
   
BEGIN
 SELECT INTO weightFirstname value FROM duplicaterecognition WHERE name='p.firstname';
 SELECT INTO weightLastname value FROM duplicaterecognition WHERE name='p.lastname';
 SELECT INTO weightMiddlename value FROM duplicaterecognition WHERE name='p.middlename';
 SELECT INTO weightNameprefix value FROM duplicaterecognition WHERE name='p.nameprefix';
 SELECT INTO weightNamesuffix value FROM duplicaterecognition WHERE name='p.namesuffix';
 SELECT INTO weightGendercode value FROM duplicaterecognition WHERE name='p.gendercode';
 SELECT INTO weightBirthday value FROM duplicaterecognition WHERE name='p.birthday';
 SELECT INTO weightEmail value FROM duplicaterecognition WHERE name='p.email';
 SELECT INTO weightTelephone value FROM duplicaterecognition WHERE name='p.telephone';
 SELECT INTO weightFax value FROM duplicaterecognition WHERE name='p.fax';
 SELECT INTO weightAddress value FROM duplicaterecognition WHERE name='p.address';
 SELECT INTO weightSpecialisation value FROM duplicaterecognition WHERE name='p.specialisation';
 SELECT INTO thresholdUpper value FROM duplicaterecognition WHERE name='upperthreshold';
 SELECT INTO thresholdLower value FROM duplicaterecognition WHERE name='lowerthreshold';
 sumWeights = weightFirstname 
 + weightLastname 
 + weightMiddlename 
 + weightNameprefix 
 + weightNamesuffix 
 + weightGendercode 
 + weightBirthday 
 + weightEmail 
 + weightTelephone 
 + weightFax 
 + weightAddress 
 + weightSpecialisation;

 FOR prov1 IN SELECT * FROM provider WHERE duplicatescalculated = false LOOP
	<<label1>>
	FOR prov2 IN SELECT * FROM provider WHERE duplicatescalculated = true LOOP
		sum = 0.0;
		count = 0;
		addMatch = 0.0;
		bestAddMatch = 0.0;
		IF ((prov1.firstname != '') AND (prov2.firstname != '') AND (weightFirstname!=0)) THEN 
			count = count + weightFirstname;
			part = jaro(prov1.firstname, prov2.firstname);
			sum = sum + (part * weightFirstname);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;
		IF ((prov1.lastname != '') AND (prov2.lastname != '') AND (weightLastname!=0)) THEN 
			count=count + weightLastname;
			part = jaro(prov1.lastname, prov2.lastname);
			sum = sum + (part * weightLastname);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;
		
		IF ((prov1.middlename != '') AND (prov2.middlename != '') AND (weightMiddlename!=0)) THEN 
			count=count + weightMiddlename;
			part = jaro(prov1.middlename, prov2.middlename);
			sum = sum + (part * weightMiddlename);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;

		IF ((prov1.nameprefix != '') AND (prov2.nameprefix != '') AND (weightNameprefix!=0)) THEN 
			count = count + weightNameprefix;
			part = jaro(prov1.nameprefix, prov2.nameprefix);
			sum = sum + (part * weightNameprefix);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;

		IF ((prov1.namesuffix != '') AND (prov2.namesuffix != '') AND (weightNamesuffix!=0)) THEN 
			count = count + weightNamesuffix;
			part = jaro(prov1.namesuffix, prov2.namesuffix);
			sum = sum + (part * weightNamesuffix);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;


		IF ((prov1.specialisation != '') AND (prov2.specialisation != '') AND (weightSpecialisation!=0)) THEN 
			count = count + weightSpecialisation;
			part = jaro(prov1.specialisation, prov2.specialisation);
			sum = sum + (part * weightSpecialisation);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;


		IF ((prov1.gendercode != '') AND (prov2.gendercode != '') AND (weightGendercode!=0)) THEN 
			count = count + weightGendercode;
			part = jaro(prov1.gendercode, prov2.gendercode);
			sum = sum + (part * weightGendercode);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;

		IF ((prov1.birthday != null) AND (prov2.birthday != null) AND (weightBirthday!=0)) THEN 
			count = count + weightBirthday;
			part = jaro(prov1.birthday, prov2.birthday);
			sum = sum + (part * weightBirthday);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;

		IF ((prov1.email != '') AND (prov2.email != '') AND (weightEmail!=0)) THEN 
			count = count + weightEmail;
			part = jaro(prov1.email, prov2.email);
			sum = sum + (part * weightEmail);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;

		IF ((prov1.telephone != '') AND (prov2.telephone != '') AND (weightTelephone!=0)) THEN 
			count = count + weightTelephone;
			part = jaro(prov1.telephone, prov2.telephone);
			sum = sum + (part * weightTelephone);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;

		IF ((prov1.fax != '') AND (prov2.fax != '') AND (weightFax!=0)) THEN 
			count = count + weightFax;
			part = jaro(prov1.fax, prov2.fax);
			sum = sum + (part * weightFax);
			CONTINUE label1 WHEN ((sum / count * 100) < ((sumWeights-count)*count/sumWeights));
		END IF;

		FOR addProv1 IN SELECT * FROM providerhasaddress WHERE regionalproviderid=prov1.regionalproviderid LOOP
			FOR addProv2 IN SELECT * FROM providerhasaddress WHERE regionalproviderid=prov2.regionalproviderid LOOP
				addMatch = duplicate_address(addProv1.addressid, addProv2.addressid);
				IF(addMatch > bestAddMatch) THEN
					bestAddMatch = addMatch;
				END IF;
			END LOOP;
		END LOOP;

		IF ( ( bestAddMatch > 0.0) AND (weightAddress!=0)) THEN
			count = count + weightAddress;
			sum = sum + (bestAddMatch * weightAddress);
		END IF;

		IF ( count > 0) THEN
			result = sum / count;
			IF (((result*100)>thresholdLower)AND((result*100)<thresholdUpper)) THEN
				INSERT INTO duplicate_provider VALUES (prov1.regionalproviderid, prov2.regionalproviderid, result, NOW());
			END IF;
		END IF;
	END LOOP;
	UPDATE provider SET duplicatescalculated = true WHERE regionalproviderid=prov1.regionalproviderid;
 END LOOP;
return weightLastname;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;


------------------------------------------------------------------------------------------------------
--- Function setupdb()                                                                       ---
------------------------------------------------------------------------------------------------------
  
CREATE OR REPLACE FUNCTION setupdb()
  RETURNS integer AS
$BODY$
begin 
	-- Delete the data records
	delete from addresslog;
	delete from localidlog;
	delete from organisationlog;
	delete from organisationhasaddresslog;
	delete from providerhasaddresslog;
	delete from organisationhasproviderlog;
	delete from providerlog;
	
	delete from providerhasaddress;
	delete from organisationhasprovider;
	delete from organisationhasaddress;

	delete from address;
	delete from localid;
	delete from organisation;
	delete from provider;

	delete from duplicate_address;
	delete from duplicate_organisation;
	delete from duplicate_provider;

	delete from rolehasrightfordomain;
	delete from porsuser;
	delete from userrole;
	delete from userright;
	delete from userdomain;

	-- User settings
	-- userrole
	insert into userrole (userroleid, name, description)
		values (1, 'admin', 'admin user role');
	insert into userrole (userroleid, name, description)
		values (2, 'user', 'standard user role');
	-- userright
	insert into userright (userrightid, name)
		values (1, 'create');
	insert into userright (userrightid, name)
			values (2, 'read');
	insert into userright (userrightid, name)
			values (3, 'update');
	insert into userright (userrightid, name)
			values (5, 'deactivate');
	insert into userright (userrightid, name)
			values (6, 'reactivate');
	insert into userright (userrightid, name)
			values (7, 'read_all');
	insert into userright (userrightid, name)
			values (8, 'update_all');
	insert into userright (userrightid, name)
			values (10, 'deactivate_all');
	insert into userright (userrightid, name)
			values (11, 'reactivate_all');		
	insert into userright (userrightid, name)
			values (12, 'configure');
	-- userdomain
	insert into userdomain (userdomainid, name)
		values (1, 'provider');
	insert into userdomain (userdomainid, name)
		values (2, 'organisation');
	insert into userdomain (userdomainid, name)
		values (3, 'user');
	insert into userdomain (userdomainid, name)
		values (4, 'logging');
	insert into userdomain (userdomainid, name)
		values (5, 'localid');
	insert into userdomain (userdomainid, name)
		values (6, 'duplicates');
	insert into userdomain (userdomainid, name)
		values (7, 'address');
	insert into userdomain (userdomainid, name)
		values (8, 'system');
	-- rolehasrightfordomain
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read', 'duplicates');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read', 'address');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update', 'duplicates');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update', 'address');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'configure', 'duplicates');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'configure', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'configure', 'system');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'create', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'create', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'create', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read', 'logging');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'deactivate', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'deactivate', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'deactivate', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'reactivate', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'reactivate', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'reactivate', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read_all', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read_all', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read_all', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'read_all', 'logging');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update_all', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update_all', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'update_all', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'deactivate_all', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'deactivate_all', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'deactivate_all', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'reactivate_all', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'reactivate_all', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('admin', 'reactivate_all', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'create', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'create', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'read', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'read', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'read', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'read', 'logging');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'update', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'update', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'deactivate', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'deactivate', 'organisation');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'deactivate', 'user');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'reactivate', 'provider');
	INSERT INTO rolerights ("rolename", "rightname", "domainname") VALUES ('user', 'reactivate', 'organisation');
	-- porsuser
	insert into porsuser (porsuserid, userroleid, name, password, isactive)
		values (1, 1,'admin', 'D033E22AE348AEB5660FC2140AEC35850C4DA997', TRUE);
	insert into porsuser (porsuserid, userroleid, name, password, isactive)
		values (2, 2,'user', '12DEA96FEC20593566AB75692C9949596833ADC9', TRUE);
	
	return 0;
end;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
 
 
 
------------------------------------------------------------------------------------------------------
--- Initialise duplicate configuration                                                             ---
------------------------------------------------------------------------------------------------------
  
INSERT INTO duplicaterecognition (name, value) VALUES ('p.nameprefix', 5);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.birthday', 5);
INSERT INTO duplicaterecognition (name, value) VALUES ('o.email', 5);
INSERT INTO duplicaterecognition (name, value) VALUES ('a.state', 5);
INSERT INTO duplicaterecognition (name, value) VALUES ('o.address', 5);
INSERT INTO duplicaterecognition (name, value) VALUES ('a.country', 0);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.firstname', 30);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.lastname', 30);
INSERT INTO duplicaterecognition (name, value) VALUES ('lowerthreshold', 97);
INSERT INTO duplicaterecognition (name, value) VALUES ('timer.seconds', 0);
INSERT INTO duplicaterecognition (name, value) VALUES ('timer.minute', 30);
INSERT INTO duplicaterecognition (name, value) VALUES ('timer.hour', 16);
INSERT INTO duplicaterecognition (name, value) VALUES ('timer.minutes', 30);
INSERT INTO duplicaterecognition (name, value) VALUES ('o.secondname', 56);
INSERT INTO duplicaterecognition (name, value) VALUES ('o.telephone', 35);
INSERT INTO duplicaterecognition (name, value) VALUES ('o.fax', 56);
INSERT INTO duplicaterecognition (name, value) VALUES ('o.name', 82);
INSERT INTO duplicaterecognition (name, value) VALUES ('upperthreshold', 100);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.oid', 3);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.middlename', 2);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.namesuffix', 2);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.email', 2);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.fax', 2);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.address', 20);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.gendercode', 2);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.telephone', 2);
INSERT INTO duplicaterecognition (name, value) VALUES ('a.housenumber', 15);
INSERT INTO duplicaterecognition (name, value) VALUES ('a.zipcode', 30);
INSERT INTO duplicaterecognition (name, value) VALUES ('a.city', 30);
INSERT INTO duplicaterecognition (name, value) VALUES ('a.street', 30);
INSERT INTO duplicaterecognition (name, value) VALUES ('p.specialisation', 3);
  
  
  
------------------------------------------------------------------------------------------------------
--- Initialise database                                                             ---
------------------------------------------------------------------------------------------------------
  
  
SELECT setupdb();
  
