package com.herokuapp.restfulbooker;

import com.herokuapp.restfulbooker.RestfulBooker;
import org.junit.platform.suite.api.*;

@SelectPackages("com.herokuapp.restfulbooker")
@IncludeTags("Tag1")
@SelectClasses(RestfulBooker.class)
@Suite
//@DisableParentConfigurationParameters
class BookingSuite {
}
