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

import java.util.*;
import java.io.*;

/**
 * A WritableList is a list of Writables.
 * 
 * @author dirkriehle
 *
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
