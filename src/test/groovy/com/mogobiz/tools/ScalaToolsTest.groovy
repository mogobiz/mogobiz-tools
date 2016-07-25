/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */
package com.mogobiz.tools

import scala.Option

import static com.mogobiz.tools.ScalaTools.*

/**
 *
 */
class ScalaToolsTest  extends GroovyTestCase{

    void testNone(){
        def none = toScalaOption(null) as Option<String>
        assertTrue(none instanceof Option<String>)
    }
}
