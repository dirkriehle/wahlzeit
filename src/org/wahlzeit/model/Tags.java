

package org.wahlzeit.model;

import java.util.*;

import org.wahlzeit.utils.*;

/**
 * A Tags instance represents a set of tags; each tag ist just a string. All
 * tags are maintained lowercase and without whitespace. I.e. "Captain America"
 * turns into "captainamerica"
 * 
 * @author dirkriehle
 * 
 */
public class Tags {

	/**
	 * 
	 */
	public static final char SEPARATOR_CHAR = ',';

	/**
	 * 
	 */
	public static final int MAX_NO_TAGS = 32;

	/**
	 * 
	 */
	public static final Tags EMPTY_TAGS = new Tags();

	/**
	 * 
	 */
	private final char separator;

	/**
	 * 
	 */
	protected ArrayList<String> tags = new ArrayList<String>();

	/**
	 * 
	 */
	public Tags() {
		// do nothing
		this.separator = SEPARATOR_CHAR;
	}

	/**
	 * 
	 */
	public Tags(String myTags) {
		this.separator = SEPARATOR_CHAR;
		this.tags = getTagListFromString(myTags);
	}

	/**
	 * 
	 */
	public Tags(String myTags, char separator) {
		this.separator = separator;
		this.tags = getTagListFromString(myTags, separator);
	}

	/**
	 * 
	 */
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	/**
	 * 
	 */
	public int getSize() {
		return tags.size();
	}

	/**
	 * 
	 */
	public String asString() {
		return asString(false, separator);
	}

	/**
	 * 
	 */
	public String asString(boolean lead, char sep) {
		StringBuffer result = new StringBuffer();
		String seps = (lead ? " " : "") + sep + " ";
		String[] myTags = asArray();
		for (int i = 0; i < myTags.length; i++) {
			if (i != 0)
				result.append(seps);
			result.append(myTags[i]);
		}
		return result.toString();
	}

	/**
	 * 
	 */
	public String[] asArray() {
		return (String[]) tags.toArray(new String[0]);
	}

	/**
	 * 
	 */
	public static ArrayList<String> getTagListFromString(String tags) {
		return getTagListFromString(tags, SEPARATOR_CHAR);
	}

	/**
	 * 
	 */
	public static ArrayList<String> getTagListFromString(String tags, char separator) {
		ArrayList<String> result = new ArrayList<String>(8);

		if (tags != null) {
			int i = 0;
			int j = 0;
			for (; i < tags.length(); i = j) {
				for (; ((i < tags.length()) && (tags.charAt(i) == separator));) {
					i++;
				}

				for (j = i; ((j < tags.length()) && (tags.charAt(j) != separator));) {
					j++;
				}

				if (i != j) {
					String tag = asTag(tags.substring(i, j));
					if (!result.contains(tag)
							&& !StringUtil.isNullOrEmptyString(tag)) {
						result.add(tag);
					}
				}
			}
		}

		return result;
	}

	/**
	 * 
	 */
	public static String asTag(String n) {
		StringBuffer result = new StringBuffer(n.length());

		for (int i = 0; i < n.length(); i++) {
			char c = n.charAt(i);
			if (Character.isLetter(c)) {
				result.append(Character.toLowerCase(c));
			} else if (Character.isDigit(c)) {
				result.append(c);
			}
		}

		return result.toString();
	}

}

<<<<<<< HEAD
=======
package org.wahlzeit.model;

import java.util.*;

import org.wahlzeit.utils.*;

/**
 * A Tags instance represents a set of tags; each tag ist just a string.
 * All tags are maintained lowercase and without whitespace.
 * I.e. "Captain America" turns into "captainamerica"
 * 
 * @author dirkriehle
 *
 */
public class Tags {
	
	/**
	 * 
	 */
	public static final char SEPARATOR_CHAR = ',';
	
	/**
	 * 
	 */
	public static final int MAX_NO_TAGS = 32;
	
	/**
	 * 
	 */
	public static final Tags EMPTY_TAGS = new Tags();
	
	/**
	 * 
	 */
	protected ArrayList<String> tags = new ArrayList<String>();
	
	/**
	 * 
	 */
	public Tags() {
		// do nothing
	}
	
	/**
	 * 
	 */
	public Tags(String myTags) {
		tags = getTagListFromString(myTags);
	}
	
	/**
	 * 
	 */
	public Tags(String myTags, char separator) {
		tags = getTagListFromString(myTags, separator);
	}
	
	/**
	 * 
	 */
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	/**
	 * 
	 */
	public boolean isEqual(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		if(this.getClass() != obj.getClass()) return false;
		Tags object = (Tags) obj;
		//TreeSet nimmt die natuerliche Sortierung der Elemente um sicherzustellen
		//dass isEqual auch bei verschieden sortierten Tags true liefert
		TreeSet<String> treeset = new TreeSet<String>(tags);
		if(tags == null) {
			if(object != null) {
				return false;
			}
		} else if (!treeset.equals(new TreeSet<String>(object.tags))) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return isEqual(obj);
	}

	/**
	 * 
	 */
	public int getSize() {
		return tags.size();
	}
	
	/**
	 * 
	 */
	public String asString() {
		return asString(false, SEPARATOR_CHAR);
	}

	/**
	 * 
	 */
	public String asString(boolean lead, char sep) {
		StringBuffer result = new StringBuffer();
		String seps = (lead ? " " : "") + sep + " ";
		String[] myTags = asArray();
		for (int i = 0; i < myTags.length; i++) {
			if (i != 0 ) result.append(seps);
			result.append(myTags[i]);
		}
		return result.toString();
	}
	
	/**
	 * 
	 */
	public String[] asArray() {
		return (String[]) tags.toArray(new String[0]);
	}
	
	/**
	 * 
	 */
	public static ArrayList<String> getTagListFromString(String tags) {
		return getTagListFromString(tags, SEPARATOR_CHAR);
	}
	
	/**
	 * 
	 */
	public static ArrayList<String> getTagListFromString(String tags, char separator) {
		ArrayList<String> result = new ArrayList<String>(8);

		if (tags != null) {
			int i = 0;
			int j = 0;
			for(; i < tags.length(); i = j) {
				for (; ((i < tags.length()) && (tags.charAt(i) == separator)); ) {
					i++;
				}
				
				for (j = i; ((j < tags.length()) && (tags.charAt(j) != separator)); ) {
					j++;
				}
				
				if (i != j) {
					String tag = asTag(tags.substring(i, j));
					if (!result.contains(tag) && !StringUtil.isNullOrEmptyString(tag)) {
						result.add(tag);
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	public static String asTag(String n) {
		StringBuffer result = new StringBuffer(n.length());

		for (int i = 0; i < n.length(); i++) {
			char c = n.charAt(i);
			if (Character.isLetter(c)) {
				result.append(Character.toLowerCase(c));
			} else if (Character.isDigit(c)) {
				result.append(c);
			}
		}
		
		return result.toString();
	}
	
}
>>>>>>> TagsEqual
