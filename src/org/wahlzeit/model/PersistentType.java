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

package org.wahlzeit.model;

import java.lang.reflect.Field;
import java.util.*;
import org.wahlzeit.services.Persistent;

/**
 * A PersistentType object contains the type attributes of a persistent object
 * 
 * @author pwa
 *
 */
public class PersistentType {
	
	protected Class<? extends Persistent> clazz = null;
	
	public PersistentType(Class<? extends Persistent> clazz){
		this.clazz = clazz;
	}
	
	public Iterator<String> getSqlAttributeNamesIterator(){
		Collection<String> c = getSqlAttributeNames();
		return c.iterator();
	}

	public Collection<String> getSqlAttributeNames(){
		Field[] fields = clazz.getDeclaredFields();
		
		ArrayList<String> lst = new ArrayList<String>();
		for(int i = 0; i < fields.length; i++){
			if(hasDirectSqlAnnotation(fields[i])){
				lst.add(fields[i].getName());
			}
		}
		return lst;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Class> getSqlAttributeTypeMap(){
		Map<String,Class> m = new HashMap<String, Class>();
		Field[] fields = clazz.getDeclaredFields();
		for(int i = 0; i < fields.length; i++){
			if(hasDirectSqlAnnotation(fields[i])){
				m.put(fields[i].getName(), fields[i].getType());
			}
		}
		return m;
	}

	private boolean hasDirectSqlAnnotation(Field field) {
		SqlAnnotation ann = field.getAnnotation(SqlAnnotation.class);
		if(ann == null) return false;
		else return ann.value().equals("direct");
	}
	
}
