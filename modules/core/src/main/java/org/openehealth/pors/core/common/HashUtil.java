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
package org.openehealth.pors.core.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author jr
 * 
 */
public class HashUtil 
{
	private static final String HEXES = "0123456789ABCDEF";
	
	private HashUtil()
	{
		
	}
	
	/**
	 * <p>
	 * Creates the SHA hash of <code>inputString</code> and returns it as a
	 * hex string.
	 * </p>
	 * 
	 * @param inputString
	 * 		String to hash
	 * @return
	 * 		Hashed string
	 */
	public static String createShaHashHexString(String inputString)
	{
		MessageDigest md;
		
		try
		{
			md = MessageDigest.getInstance("SHA");
		}
		catch (NoSuchAlgorithmException e)
		{
			md = null;
		}
		
		byte[] digest = md.digest(inputString.getBytes());
		
		return getHex(digest);
	}
	
	/**
	 * <p>
	 * Creates a hash string from the given byte array.
	 * </p>
	 * @param bytes
	 * 		Byte array to convert to hex string
	 * @return
	 */
	public static String getHex(byte[] bytes) 
	{
		if (bytes == null)
		{
			return null;
		}
		
		final StringBuilder hex = new StringBuilder(2 * bytes.length);
		
		for (final byte b : bytes)
		{
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).
				append(HEXES.charAt((b & 0x0F)));
		}
		
	    return hex.toString();
	}

}
