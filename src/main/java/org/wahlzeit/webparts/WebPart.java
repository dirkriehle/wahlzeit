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

import org.wahlzeit.utils.EnumValue;
import org.wahlzeit.utils.HtmlUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * A WebPart is a Writable formatted as HTML, working off a template.
 * A WebPart has its data set from a client using key/value pairs.
 * A WebPart has a recursive structure; it may contain further WebParts.
 *
 * @author dirkriehle
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
        for (int i = 0; i < keys.length; i++) {
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
