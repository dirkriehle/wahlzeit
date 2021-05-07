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
 * A model configuration for the English language.
 */
public class EnglishModelConfig extends AbstractModelConfig {

	/**
	 * 
	 */
	public EnglishModelConfig() {
		DecimalFormat praiseFormatter = new DecimalFormat("##.##");
		praiseFormatter.setMinimumFractionDigits(2);
		
		initialize(Language.ENGLISH, new SimpleDateFormat("MMM d, yyyy"), praiseFormatter);
	}
	
	/**
	 * 
	 */
	public String asYesOrNoString(boolean yes) {
		if (yes) {
			return "yes";
		} else {
			return "no";
		}
	}
	
	/**
	 * 
	 */
	public String asPhotoSummary(String un) {
		String result = "Photo";
		result += " by " + un;				
		return result;
	}

	/**
	 * 
	 */
	public String asPhotoCaption(String un, URL url) {
		String result = "Photo";
		if (url != null) {
			result += " by " + HtmlUtil.asHref(url.toString(), un);
		} else {
			result += " by " + un;				
		}
		return result;
	}

	/**
	 * 
	 */
	public String getNewPhotoSizeSet(PhotoSize ss) {
		String size = HtmlUtil.asBold(asValueString(ss));
		return "We set the photo size to " + size + ".";
	}

}
