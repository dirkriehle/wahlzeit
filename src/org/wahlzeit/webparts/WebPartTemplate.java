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

package org.wahlzeit.webparts;

import java.util.*;

/**
 * A WebPartTmpl is a template for a WebPart. 
 * It gets initialized once and is read-only after that.
 * 
 * @author dirkriehle
 *
 */
public class WebPartTemplate {

	/**
	 * 
	 */
	protected String name;
	
	/**
	 * 
	 */
	protected String template;
	
	/**
	 * 
	 */
	protected String[] keys = null;
	protected int[] slots = null;
		
	/**
	 * 
	 */
	public WebPartTemplate(String myName) {
		name = myName;
	}
	
	/**
	 * @methodtype initialization
	 */
	public void initialize(String source) {
		List<String> keyList = new LinkedList<String>();
		List<Integer> slotList = new LinkedList<Integer>();
		
		StringBuffer buffer = new StringBuffer(source);
		
		for (int index = 0; index != -1; ) {
			int nextSlot = buffer.indexOf("{$", index);
			if (nextSlot != -1) {
				int endSlot = buffer.indexOf("}", nextSlot);
				if (endSlot != -1) {
					String key = buffer.substring(nextSlot + 2, endSlot);
	
					keyList.add(key);
					slotList.add(nextSlot);
	
					buffer.replace(nextSlot, endSlot + 1, "");
					index = nextSlot;
				}
				index = endSlot;
			} else {
				index = -1;
			}
		}
		
		int length = keyList.size();
		keys = new String[length];
		slots = new int[length];
		for (int i = 0; i < length; i++) {
			keys[i] = (String) keyList.get(i);
			slots[i] = slotList.get(i);
		}
		
		template = buffer.toString();
	}
	
	/**
	 * 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 */
	public String asString() {
		return template;
	}
	
	/**
	 * 
	 */
	public String[] getKeys() {
		return keys; // @FIXME needs cloning?
	}

	/**
	 * 
	 */
	public int[] getSlots() {
		return slots; // @FIXME needs cloning?
	}

}
