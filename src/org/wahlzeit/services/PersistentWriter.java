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

package org.wahlzeit.services;

import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Gender;
import org.wahlzeit.model.PersistentType;
import org.wahlzeit.model.PhotoId;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.UserStatus;
import org.wahlzeit.utils.StringUtil;

/**
 * the PersistentWriter implements the "write" role in the serializer pattern of the
 * persistent class hierarchy
 * 
 * @author pwa
 *
 */
public class PersistentWriter {
	
	/**
	 * read the attributes from the result set
	 * in the attribute list of the persistent object 
	 */
	@SuppressWarnings("rawtypes")
	public static boolean readResultSet(Persistent p, ResultSet rset)
	{
		if(rset == null) return false;

		PersistentType pt = new PersistentType(p.getClass());
		
		// Iterator<String> iter = pt.getSqlAttributeNamesIterator();
		
		Map<String, Class> m = pt.getSqlAttributeTypeMap();
		Iterator<Entry<String, Class>> iter = m.entrySet().iterator();
		
		while(iter.hasNext()){
			Map.Entry<String, Class> pairs = iter.next();
			String camelCasedAttributeName = pairs.getKey();
			String sqlCaption = StringUtil.camelCaseToSqlCaption(camelCasedAttributeName);
			Class clazz = pairs.getValue();
			Object value = null;
			
			try {
				value = getAttributeValueFromName(rset, sqlCaption, clazz);
			} catch (SQLException e) {
				SysLog.logError(e.getMessage());
			}
			
			p.setAttributeValue(camelCasedAttributeName, value);
		}
		
		return true;
	}

	
	/**
	 * writes on the result set the attributes 
	 * stored in the Persistent object  
	 */
	public static boolean writeResultSet(Persistent p, ResultSet rset)
	{
		if(rset == null) return false;
		
		PersistentType pt = new PersistentType(p.getClass());
		
		Iterator<String> iter = pt.getSqlAttributeNamesIterator();
		
		while(iter.hasNext()){
			String attributeName = iter.next();
			Object o = p.getAttributeValue(attributeName);
			String sqlCaption = StringUtil.camelCaseToSqlCaption(attributeName);
			try {
				setAttributeValue(rset, sqlCaption, o);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return true;
	}

	private static void setAttributeValue(ResultSet rset, String sqlCaption, Object o) throws IllegalArgumentException, SQLException{
		Class<? extends Object> clazz = o.getClass();
		if(clazz == Integer.class){
			rset.updateInt(sqlCaption, (Integer)o);
		}else if(clazz == Long.class){
			rset.updateLong(sqlCaption, (Long)o);
		}else if(clazz == String.class){
			rset.updateString(sqlCaption, (String)o);
		}else if(clazz == Boolean.class){
			rset.updateBoolean(sqlCaption, (Boolean)o);
		}else if(clazz == boolean.class){
			rset.updateBoolean(sqlCaption, (Boolean)o);
		}else if(clazz == int.class){
			rset.updateInt(sqlCaption, (Integer)o);
		}else if(clazz == long.class){
			rset.updateLong(sqlCaption, (Long)o);
		}else{
			// other types are not supported !!!
			throw new IllegalArgumentException();
		}
	}

	private static Object getAttributeValueFromName(ResultSet rset,
			String attributeName, Class<? extends Object> clazz)
					throws SQLException {
		Object o = null;
		if (clazz == Integer.class) {
			o = rset.getInt(attributeName);
		} else if (clazz == Long.class) {
			o = rset.getLong(attributeName);
		} else if (clazz == String.class) {
			o = rset.getString(attributeName);
		} else if (clazz == Boolean.class) {
			o = rset.getBoolean(attributeName);
		} else if (clazz == boolean.class) {
			o = rset.getBoolean(attributeName);
		} else if (clazz == int.class) {
			o = rset.getInt(attributeName);
		} else if (clazz == long.class) {
			o = rset.getLong(attributeName);
		} else {
			// other types are not supported !!!
			throw new IllegalArgumentException();
		}

		return o;
	}

}
