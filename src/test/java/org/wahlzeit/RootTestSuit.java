package org.wahlzeit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.wahlzeit.handlers.TellFriendTest;
import org.wahlzeit.model.AccessRightsTest;
import org.wahlzeit.model.FlagReasonTest;
import org.wahlzeit.model.GenderTest;
import org.wahlzeit.model.GuestTest;
import org.wahlzeit.model.LocationTest;
import org.wahlzeit.model.PhotoFilterTest;
import org.wahlzeit.model.TagsTest;
import org.wahlzeit.model.UserStatusTest;
import org.wahlzeit.model.ValueTest;
import org.wahlzeit.model.persistence.AbstractAdapterTest;
import org.wahlzeit.model.persistence.DatastoreAdapterTest;
import org.wahlzeit.services.EmailAddressTest;
import org.wahlzeit.services.LogBuilderTest;
import org.wahlzeit.services.mailing.AllTestsForEmailService;
import org.wahlzeit.utils.StringUtilTest;
import org.wahlzeit.utils.VersionTest;


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


