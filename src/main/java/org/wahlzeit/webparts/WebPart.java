/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.webparts;

import java.util.*;
import java.io.*;

import org.wahlzeit.utils.*;

/**
 * A WebPart is a Writable formatted as HTML, working off a template.
 * A WebPart has its data set from a client using key/value pairs.
 * A WebPart has a recursive structure; it may contain further WebParts.
 */
public class WebPart implements Writable {

	/**
	 * 
	 */
	protected WebPartTemplate template = null;
	
	/**
	 * 
	 */
	protected Map<String, Object> parts = new HashMap<String, Object>();
	
	/**
	 * 
	 */
	public WebPart(WebPartTemplate myTemplate) {
		template = myTemplate;
	}
	
	public Object getValue(String key) {
		return parts.get(key);
	}
	
	/**
	 * 
	 */
	public void addString(String key, String value) {
		if (value != null) {
			putValue(key, value);
		}
	}
		
	/**
	 * 
	 */
	public void maskAndAddString(String key, String value) {
		if (value != null) {
			putValue(key, HtmlUtil.maskForWeb(value));
		}
	}
		
	/**
	 * 
	 */
	public void addStringFromArgs(Map args, String key) {
		Object value = args.get(key);
		if (value != null) {
			addString(key, value.toString());
		}
	}
	
	/**
	 * 
	 */
	public void maskAndAddStringFromArgs(Map args, String key) {
		Object value = args.get(key);
		if (value != null) {
			addString(key, HtmlUtil.maskForWeb(value.toString()));
		}
	}
	
	/**
	 * 
	 */
	public void addStringFromArgsWithDefault(Map args, String key, String defval) {
		Object value = args.get(key);
		if (value != null) {
			addString(key, value.toString());
		} else {
			addString(key, defval);
		}		
	}
	
	/**
	 * 
	 */
	public void maskAndAddStringFromArgsWithDefault(Map args, String key, String defval) {
		Object value = args.get(key);
		if (value != null) {
			addString(key, HtmlUtil.maskForWeb(value.toString()));
		} else {
			addString(key, HtmlUtil.maskForWeb(defval));
		}		
	}
	
	/**
	 * 
	 */
	public void addSelect(String key, EnumValue value) {
		addSelect(key, value.getClass(), value.asString());
	}
	
	/**
	 * 
	 */
	public void addSelect(String key, Class valueClass, String value) {
		WebValueManager wvMgr = WebValueManager.getInstance();
		WebValue wv = wvMgr.getWebValue(valueClass, value);
		if (wv != null) {
			addSelect(key, wv);
		}
	}
	
	/**
	 * 
	 */
	public void addSelect(String key, Class valueClass, String value, EnumValue defval) {
		WebValueManager wvMgr = WebValueManager.getInstance();
		WebValue wv = wvMgr.getWebValue(valueClass, value);
		if (wv != null) {
			addSelect(key, wv);
		} else {
			addSelect(key, defval);
		}
	}
	
	/**
	 * 
	 */
	public void addSelect(String key, WebValue value) {
		addString(value.getSelectedKey(), HtmlUtil.asSelectSelected(true));
	}
	
	/**
	 * 
	 */
	public void addWritable(String key, Writable value) {
		if (value != null) {
			putValue(key, value);
		}
	}
	
	/**
	 * 
	 */
	protected void putValue(String key, Object value) {
		if (value != null) {
			parts.put(key, value);
		}
	}

	/**
	 * 
	 */
	public void writeOn(Writer out) throws IOException {
		String tmpl = template.asString();
		String[] keys = template.getKeys();
		int[] slots = template.getSlots();

		int start = 0;
		for (int i = 0; i < keys.length; i++ ) {
			int index = slots[i];
			out.write(tmpl, start, index - start);

			String key = keys[i];
			if (key != null) {
				Object object = parts.get(key);
				if (object != null) {
					if (object instanceof Writable) {
						Writable part = (Writable) object;
						part.writeOn(out);
					} else {
						String value = object.toString();
						out.write(value);
					}
				}
			}
			
			start = index;
		}
		
		out.write(tmpl, start, tmpl.length() - start);
	}
	
}
