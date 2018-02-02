package org.wahlzeit;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.services.mailing.*;
import org.wahlzeit.handlers.*;
import org.wahlzeit.model.persistence.*;
import org.wahlzeit.utils.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({

//Email  service suit 
AllTestsForEmailService.class,
	
//Handler package

TellFriendTest.class,

//Model package 

AccessRightsTest.class,
FlagReasonTest.class,
LocationTest.class,
PhotoFilterTest.class,
TagsTest.class,
UserStatusTest.class,
ValueTest.class,
GenderTest.class,
GuestTest.class,
CartesianCoordinateTest.class,
SphericCoordinateTest.class,




//persistence  package 

AbstractAdapterTest.class,
DatastoreAdapterTest.class,

//services package 

EmailAddressTest.class,
LogBuilderTest.class,

//utils package 

StringUtilTest.class,
VersionTest.class


})
public class RootTestSuit {
	

}


