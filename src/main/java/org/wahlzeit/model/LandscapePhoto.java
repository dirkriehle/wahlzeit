package org.wahlzeit.model;

import java.sql.*;

public class LandscapePhoto extends Photo {

    /**
	 * 
	 * @methodtype constructor
	 */
    public LandscapePhoto() {
        super();
    }

    /**
	 * 
	 * @methodtype constructor
	 */
    public LandscapePhoto(PhotoId myId) {
        super(myId);
    }

    /**
	 * 
	 * @methodtype constructor
	 */
    public LandscapePhoto(PhotoId myId, Location location) {
        super(myId, location);
    }

    /**
	 * 
	 * @methodtype constructor
	 */
    public LandscapePhoto(ResultSet rset) throws SQLException {
        super(rset);
    }
    
}
