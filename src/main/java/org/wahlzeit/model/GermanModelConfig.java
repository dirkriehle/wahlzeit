/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.net.*;
import java.text.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

/**
 * A model configuration for the German language.
 */
public class GermanModelConfig extends AbstractModelConfig {

	/**
	 * 
	 */
	public GermanModelConfig() {
		DecimalFormat praiseFormatter = new DecimalFormat("##,##");
		praiseFormatter.setMinimumFractionDigits(2);
		
		super.initialize(Language.GERMAN, new SimpleDateFormat("d. MMM yyyy"), praiseFormatter);
	}
	
	/**
	 * 
	 */
	public String asYesOrNoString(boolean yes) {
		if (yes) {
			return "ja";
		} else {
			return "nein";
		}
	}
	
	/**
	 * 
	 */
	public String asPhotoSummary(String un) {
		String result = "Foto";
		result += " von " + un;				
		return result;
	}

	/**
	 * 
	 */
	public String asPhotoCaption(String un, URL url) {
		String result = "Foto";
		if (url != null) {
			result += " von " + HtmlUtil.asHref(url.toString(), un);
		} else {
			result += " von " + un;
		}
		return result;
	}
	
	/**
	 * 
	 */
	public String getNewPhotoSizeSet(PhotoSize ss) {
		String size = HtmlUtil.asBold(asValueString(ss));
		return "Wir haben die Fotogröße auf " + size + " gesetzt.";
	}

}
