/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.webparts;

import java.util.*;

/**
 * A WebPartTmpl is a template for a WebPart.
 * It gets initialized once and is read-only after that.
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
