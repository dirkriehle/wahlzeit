/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.webparts;

import java.util.*;
import java.io.*;

/**
 * A WritableList is a list of Writables.
 */
public class WritableList implements Writable {
	
	/**
	 * 
	 */
	protected LinkedList<Writable> writables = new LinkedList<Writable>();
	
	/**
	 * 
	 */
	public WritableList() {
		// do nothing
	}
	
	/**
	 * 
	 */
	public void writeOn(Writer out) throws IOException {
		for (Iterator<Writable> pi = writables.listIterator(); pi.hasNext(); ) {
			Writable part = pi.next();
			part.writeOn(out);
		}
	}
	
	/**
	 * 
	 */
	public WritableList prepend(Writable w) {
		writables.addFirst(w);
		return this;
	}
	
	/**
	 * 
	 */
	public WritableList insert(int i, Writable w) {
		writables.add(i, w);
		return this;
	}
	
	/**
	 * 
	 */
	public WritableList append(Writable w) {
		writables.addLast(w);
		return this;
	}	

}
