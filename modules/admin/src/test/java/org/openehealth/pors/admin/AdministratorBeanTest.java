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
package org.openehealth.pors.admin;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openehealth.pors.admin.AdministratorBean;
import org.openehealth.pors.admin.IAdministrator;
import org.openehealth.pors.database.entities.Address;
import org.openehealth.pors.database.entities.ImportResult;
import org.openehealth.pors.database.entities.LocalId;
import org.openehealth.pors.database.entities.Organisation;
import org.openehealth.pors.database.entities.Provider;

import org.openehealth.pors.core.common.Task;
import org.openehealth.pors.core.dto.PorsCsv;

/**
 * Test class for import / export provider and organisation
 * 
 * @author tb, ck
 * @author mbirkle
 * 
 */
@Ignore
public class AdministratorBeanTest {

	private IAdministrator admin;

	@Before
	public void setUp() {
		admin = new AdministratorBean();
	}

	/**
	 * Test import provider
	 */
	@Test
	public void testImportProvider() throws Exception {
		String path = this
				.getClass()
				.getClassLoader()
				.getResource(
						"../test-classes/de/uni_heidelberg/ise/pors/admin/test.csv")
				.getPath();

		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
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
		fieldList.add("id");
		fieldList.add("lanr");
		fieldList.add("oid");
		fieldList.add("firstname");
		fieldList.add("lastname");
		fieldList.add("middleName");
		fieldList.add("namePrefix");
		fieldList.add("nameSuffix");
		fieldList.add("genderCode");
		fieldList.add("birthday");
		fieldList.add("email");
		fieldList.add("telephone");
		fieldList.add("fax");
		fieldList.add("deactivationDate");
		fieldList.add("deactivationReasonCode");
		fieldList.add("reactivationDate");
		fieldList.add("reactivationReasonCode");
		fieldList.add("lastUpdateDate");
		fieldList.add("addresses");
		fieldList.add("localids");

		ImportResult res = new ImportResult();
		List<Task> taskList = admin.importProviders(porsCsv, fieldList, res);

		Task task = taskList.get(0);

		Provider p = task.getProvider();
		assertEquals(p.getId(), new Long(9166));
		assertEquals(p.getFirstName(), "Hansi");
		assertEquals(p.getLastName(), "Beckenbauri");
		assertEquals(p.getGenderCode(), "m");

		Address a = p.getAddresses().get(0);

		assertEquals(a.getCity(), "Heidelberg");
		assertEquals(a.getStreet(), "Teststr.");
		assertEquals(a.getCountry(), "DE");
		assertEquals(a.getState(), "BaW");
		assertEquals(a.getHouseNumber(), "10");
		assertEquals(a.getZipCode(), "69119");

		Address a2 = p.getAddresses().get(1);

		assertEquals(a2.getCity(), "Heidelberg");
		assertEquals(a2.getStreet(), "Teststr.");
		assertEquals(a2.getCountry(), "DE");
		assertEquals(a2.getState(), "BaW");
		assertEquals(a2.getHouseNumber(), "11");
		assertEquals(a2.getZipCode(), "69119");

		LocalId l1 = p.getLocalIds().get(0);
		assertEquals(l1.getLocalId(), "1234");
		assertEquals(l1.getFacility(), "facility");
		assertEquals(l1.getApplication(), "application");

		LocalId l2 = p.getLocalIds().get(1);
		assertEquals(l2.getLocalId(), "67483");
		assertEquals(l2.getFacility(), "einrichtung");
		assertEquals(l2.getApplication(), "system");
	}

	/**
	 * Test import organisation
	 */
	@Test
	public void testImportOrganisations() throws Exception {

		String path = this
				.getClass()
				.getClassLoader()
				.getResource(
						"../test-classes/de/uni_heidelberg/ise/pors/admin/orgTest.csv")
				.getPath();

		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
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
		fieldList.add("id");
		fieldList.add("oid");
		fieldList.add("establishmentid");
		fieldList.add("name");
		fieldList.add("secondname");
		fieldList.add("description");
		fieldList.add("email");
		fieldList.add("telephone");
		fieldList.add("fax");
		fieldList.add("deactivationDate");
		fieldList.add("deactivationReasonCode");
		fieldList.add("reactivationDate");
		fieldList.add("reactivationReasonCode");
		fieldList.add("lastUpdateDate");
		fieldList.add("addresses");
		fieldList.add("localids");

		ImportResult res = new ImportResult();
		List<Task> taskList = admin
				.importOrganisations(porsCsv, fieldList, res);
		Task task = taskList.get(0);

		Organisation o = task.getOrganisation();
		assertEquals(o.getId(), new Long(4711));
		assertEquals(o.getName(), "Uniklinik");
		assertEquals(o.getSecondName(), "Heidelberg");
		Address a = o.getAddresses().get(0);

		assertEquals(a.getCity(), "Heidelberg");
		assertEquals(a.getStreet(), "Teststr.");
		assertEquals(a.getCountry(), "DE");
		assertEquals(a.getState(), "BaW");
		assertEquals(a.getHouseNumber(), "10");
		assertEquals(a.getZipCode(), "69119");

		LocalId l1 = o.getLocalIds().get(0);
		assertEquals(l1.getLocalId(), "1234");
		assertEquals(l1.getFacility(), "facility");
		assertEquals(l1.getApplication(), "application");
	}

	/**
	 * Test export provider
	 */
	@Test
	public void testExportProviders() throws Exception {
		Provider p = new Provider();
		p.setId(new Long(123));
		p.setFirstName("test");
		p.setLastName("tester");
		p.setOid("1.2.3.4.5.6");
		p.setEmail("test@test.de");
		p.setGenderCode("m");

		List<Address> addresses = new ArrayList<Address>();
		Address a = new Address();
		a.setId(new Long(1));
		a.setCity("Heidelberg");
		a.setStreet("Teststr.");
		a.setCountry("DE");
		a.setState("BaW�");
		a.setHouseNumber("10");
		a.setZipCode("69119");
		addresses.add(a);

		Address b = new Address();
		b.setId(new Long(2));
		b.setCity("Heidelberg");
		b.setStreet("Neuestr..");
		b.setCountry("DE");
		b.setHouseNumber("15");
		b.setZipCode("69121");
		addresses.add(b);

		p.setAddresses(addresses);

		List<LocalId> localIds = new ArrayList<LocalId>();
		LocalId l1 = new LocalId();
		l1.setId(new Long(1));
		l1.setLocalId("1234567");
		l1.setFacility("facility1");
		l1.setApplication("application1");
		localIds.add(l1);

		p.setLocalIds(localIds);

		List<Provider> pList = new ArrayList<Provider>();
		pList.add(p);

		List<String> fields = new ArrayList<String>();
		fields.add("id");
		fields.add("lanr");
		fields.add("addresses");
		System.out.println(admin.exportProviders(pList, fields).toString());

	}

	/**
	 * Test export organisation
	 */
	@Test
	public void testExportOrganisations() throws Exception {
		Organisation o = new Organisation();
		o.setId(new Long(1234567));
		o.setName("test");
		o.setSecondName("test2");
		List<Address> addresses = new ArrayList<Address>();
		Address b = new Address();
		b.setId(new Long(2));
		b.setCity("Heidelberg");
		b.setStreet("Neuestr..");
		b.setCountry("DE");
		b.setHouseNumber("15");
		b.setZipCode("69121");
		addresses.add(b);

		o.setAddresses(addresses);

		List<LocalId> localIds = new ArrayList<LocalId>();
		LocalId l1 = new LocalId();
		l1.setId(new Long(1));
		l1.setLocalId("1234567");
		l1.setFacility("facility1");
		l1.setApplication("application1");
		localIds.add(l1);

		o.setLocalIds(localIds);

		List<Organisation> oList = new ArrayList<Organisation>();
		oList.add(o);
		System.out.println(admin.exportOrganisations(oList, null).toString());
	}
}
