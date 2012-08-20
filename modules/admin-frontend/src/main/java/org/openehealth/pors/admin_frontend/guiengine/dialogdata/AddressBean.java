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
package org.openehealth.pors.admin_frontend.guiengine.dialogdata;

import org.openehealth.pors.admin_client.AddressDTO;

public class AddressBean extends AddressDTO{
	
	public AddressBean(AddressDTO add) {
		this.id = add.getId();
		this.street = add.getStreet();
		this.houseNumber = add.getHouseNumber();
		this.zipCode = add.getZipCode();
		this.city = add.getCity();
		this.state = add.getState();
		this.additional = add.getAdditional();
		this.country = add.getCountry();
	}
	
	public AddressBean(String street, String houseNumber, String zipCode, String city, String additional, String state, String country) {
		this.street = street;
		this.houseNumber = houseNumber;
		this.zipCode = zipCode;
		this.city = city;
		this.additional = additional;
		this.state = state;
		this.country = country;
	}
	
	public AddressBean(AddressBean add){
		this.id = add.id;
		this.street = add.getStreet();
		this.houseNumber = add.getHouseNumber();
		this.zipCode = add.getZipCode();
		this.city = add.getCity();
		this.state = add.getState();
		this.additional = add.getAdditional();
		this.country = add.getCountry();
	}
	
	public AddressBean() {
		this.street = "";
		this.houseNumber = "";
		this.zipCode = "";
		this.city = "";
		this.additional = "";
		this.state = "";
		this.country = "";
	}

	@Override
	public String toString() {
		String street = this.street!=null ? this.street : "";
		String houseNumber = this.houseNumber!=null ? this.houseNumber : "";
		String zipCode = this.zipCode!=null ? this.zipCode : "";
		String city = this.city!=null ? this.city : "";
		String state = this.state!=null ? this.state : "";
		String additional = this.additional!=null ? this.additional : "";
		String country = this.country!=null ? this.country : "";
		
		return String.format("%s %s %s %s %s %s %s", street, houseNumber, zipCode, city, additional, state, country);
	}
	
	@Override
	public boolean equals(Object obj) {
		AddressBean secondAddress = (AddressBean) obj;
		return this.toString().equals(secondAddress.toString());
	}
}