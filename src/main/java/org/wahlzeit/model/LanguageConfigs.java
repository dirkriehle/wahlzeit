/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.util.*;

import org.wahlzeit.services.*;

/**
 * LanguageConfigs provides a simple access points to different language-based configurations. For every available
 * language, there is a model configuration, which captures local specific stuff.
 */

public class LanguageConfigs {

	/**
	 * 
	 */
	protected static Map<Language, ModelConfig> configurations = new HashMap<Language, ModelConfig>();
	
	/**
	 * 
	 */
	public static ModelConfig get(Language language) {
		return configurations.get(language);
	}
	
	/**
	 * 
	 */
	public static void put(Language language, ModelConfig config) {
		configurations.put(language, config);
	}

}
