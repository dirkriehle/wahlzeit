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

import java.net.*;
import java.text.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

/**
 * A model configuration for the English language.
 * 
 * @author dirkriehle
 *
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
