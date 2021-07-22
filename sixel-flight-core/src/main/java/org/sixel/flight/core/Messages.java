package org.sixel.flight.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author marcos
 *
 */
public class Messages {
	
	/**
	 * 
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(
			Messages.class.getPackageName() + "." + Messages.class.getSimpleName().toLowerCase());

	/**
	 * 
	 */
	private Messages() {
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
