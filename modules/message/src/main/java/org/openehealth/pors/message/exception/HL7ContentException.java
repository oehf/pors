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
package org.openehealth.pors.message.exception;

public class HL7ContentException extends Exception{
	
	private static final long serialVersionUID = 5664482778605694918L;

	public static final int UNSUPPORTEDEVENTCODE = 201;
	
	public static final int DATATYPEERROR = 102;
	
	public static final int REQUIREDFIELDMISSING = 101;
	
	private int errorCode;
	
	public HL7ContentException(String s, int i){
		super(s);
		this.errorCode = i;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
