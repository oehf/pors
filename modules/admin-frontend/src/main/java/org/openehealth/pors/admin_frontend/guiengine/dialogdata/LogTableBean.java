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

import java.io.Serializable;

/**
 * The DTO for ProviderLogTable
 * 
 * @author ms
 * 
 */
public class LogTableBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String old = "";
	private String newValue = "";
	private String color = "black";

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public LogTableBean(String title, String old, String newValue) {
		super();
		this.title = title;
		this.old = old;
		this.newValue = newValue;

		if(this.old==null && this.newValue==null){
			this.color = "black";
			return;
		}
		if((this.old==null&&this.newValue!=null)||(!this.old.equals(this.newValue)))
			this.color = "red";
		}

	public LogTableBean(String title, Long old, Long newValue) {
		super();
		this.title = title;
		if (old != null) {
			this.old = old.toString();
		} else {
			this.old = "";
		}
		if (newValue != null) {
			this.newValue = newValue.toString();
		} else {
			this.newValue = "";
		}
	}

	public void setTitle(String titel) {
		title = titel;
	}

	public String getTitle() {
		return title;
	}

	public void setOld(String old) {
		this.old = old;
	}

	public String getOld() {
		return old;
	}

	public void setNew(String _new) {
		newValue = _new;
	}

	public String getNew() {
		return newValue;
	}
}
