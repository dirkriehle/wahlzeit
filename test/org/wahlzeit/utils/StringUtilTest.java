/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit.utils;

import java.io.File;

import junit.framework.*;

/**
 *
 * @author dirkriehle
 *
 */
public class StringUtilTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(StringUtilTest.class);
	}

	public StringUtilTest(String name) {
		super(name);
	}

	public void testPathAsUrlString() {
		String path1 = "folder" + File.separator + "subfolder" + File.separator + "file.test";
		String url1 = StringUtil.pathAsUrlString(path1);
		assertEquals("folder/subfolder/file.test", url1);

		String path2 = File.separator + "folder" + File.separator + "sub123 fol_der_" + File.separator;
		String url2 = StringUtil.pathAsUrlString(path2);
		assertEquals("/folder/sub123 fol_der_/", url2);

		assertEquals("/", StringUtil.pathAsUrlString(File.separator));
	}
	
	public void testValidCamelCase(){
		assertTrue(StringUtil.isValidCamelCase("thisIsCamelCase"));
		assertTrue(StringUtil.isValidCamelCase("this"));
		assertTrue(StringUtil.isValidCamelCase("thisI"));
		assertTrue(StringUtil.isValidCamelCase("thisIsAnotherValidCamelCaseString"));
		
		assertFalse(StringUtil.isValidCamelCase("s"));
		assertFalse(StringUtil.isValidCamelCase("IssNotValid"));
		assertFalse(StringUtil.isValidCamelCase("IssNotValid"));
		assertFalse(StringUtil.isValidCamelCase("issNotValid23"));
		assertFalse(StringUtil.isValidCamelCase("iss_not_valid"));
	}
	
	public void testValidSqlCaption(){
		assertTrue(StringUtil.isValidSqlCaption("this"));
		assertTrue(StringUtil.isValidSqlCaption("this_is_valid"));
		assertTrue(StringUtil.isValidSqlCaption("thisis_another_valid_string"));
		assertTrue(StringUtil.isValidSqlCaption("tt"));

		assertFalse(StringUtil.isValidSqlCaption("s"));
		assertFalse(StringUtil.isValidSqlCaption("sP"));
		assertFalse(StringUtil.isValidSqlCaption("P"));
		assertFalse(StringUtil.isValidSqlCaption("PP"));
		assertFalse(StringUtil.isValidSqlCaption("issNotValid"));
		assertFalse(StringUtil.isValidSqlCaption("IssNotValid"));
		assertFalse(StringUtil.isValidSqlCaption("issNotValid23"));
		assertFalse(StringUtil.isValidSqlCaption("iss__not_valid"));
		assertFalse(StringUtil.isValidSqlCaption("_not_valid"));
		assertFalse(StringUtil.isValidSqlCaption("_not_valid_"));
		assertFalse(StringUtil.isValidSqlCaption("not__valid_"));
	}
	
	public void testSqlCaptionToCamelCase(){
		assertTrue(StringUtil.camelCaseToSqlCaption("testString").equals("test_string"));
		assertTrue(StringUtil.camelCaseToSqlCaption("otherTestString").equals("other_test_string"));
		assertTrue(StringUtil.camelCaseToSqlCaption("other").equals("other"));
		assertTrue(StringUtil.camelCaseToSqlCaption("otherABC").equals(""));
		assertTrue(StringUtil.camelCaseToSqlCaption("other23").equals(""));
		
		
	}
	
	public void testSqlCaptionToCamelCase2(){
		// PhotoCase:
		assertTrue(StringUtil.camelCaseToSqlCaption("wasDecided").equals("was_decided"));
		assertTrue(StringUtil.camelCaseToSqlCaption("decisionTime").equals("decision_time"));
		assertTrue(StringUtil.camelCaseToSqlCaption("creationTime").equals("creation_time"));
		assertTrue(StringUtil.camelCaseToSqlCaption("explanation").equals("explanation"));
		
		// Photo:
		assertTrue(StringUtil.camelCaseToSqlCaption("width").equals("width"));
		assertTrue(StringUtil.camelCaseToSqlCaption("height").equals("height"));
		assertTrue(StringUtil.camelCaseToSqlCaption("ownerId").equals("owner_id"));
		assertTrue(StringUtil.camelCaseToSqlCaption("ownerName").equals("owner_name"));
		assertTrue(StringUtil.camelCaseToSqlCaption("praiseSum").equals("praise_sum"));
		assertTrue(StringUtil.camelCaseToSqlCaption("noVotes").equals("no_votes"));
		assertTrue(StringUtil.camelCaseToSqlCaption("creationTime").equals("creation_time"));
		assertTrue(StringUtil.camelCaseToSqlCaption("ownerNotifyAboutPraise").equals("owner_notify_about_praise"));
		assertTrue(StringUtil.camelCaseToSqlCaption("ownerEmailAddress").equals("owner_email_address"));
		
		// User:
		assertTrue(StringUtil.camelCaseToSqlCaption("id").equals("id"));
		assertTrue(StringUtil.camelCaseToSqlCaption("name").equals("name"));
		assertTrue(StringUtil.camelCaseToSqlCaption("nameAsTag").equals("name_as_tag"));
		assertTrue(StringUtil.camelCaseToSqlCaption("password").equals("password"));
		assertTrue(StringUtil.camelCaseToSqlCaption("notifyAboutPraise").equals("notify_about_praise"));
		assertTrue(StringUtil.camelCaseToSqlCaption("confirmationCode").equals("confirmation_code"));
		assertTrue(StringUtil.camelCaseToSqlCaption("creationTime").equals("creation_time"));
	}
	

}

