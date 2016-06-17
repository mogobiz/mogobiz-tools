package com.mogobiz.utils

import scala.Option

import static com.mogobiz.tools.ScalaTools.*

/**
 *
 * Created by smanciot on 17/06/16.
 */
class ScalaToolsTest  extends GroovyTestCase{

    void testNone(){
        def none = toScalaOption(null) as Option<String>
        assertTrue(none instanceof Option<String>)
    }
}
