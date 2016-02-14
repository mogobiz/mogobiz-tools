/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.utils

import static com.mogobiz.tools.Base64Tools.*
import static com.mogobiz.tools.ImageTools.encodeImageBase64
import static com.mogobiz.tools.HashTools.*

/**
 *
 */
class HashToolsTest extends GroovyTestCase {

    final tmp = System.getProperty('java.io.tmpdir') ?: '/tmp'

    def encoded
    File copy

    void setUp() {
        File logo = new File(ImageToolsTest.class.getResource("logo.png").path)
        assertNotNull(logo)
        encoded = encodeImageBase64(logo, true)
        copy = new File(tmp, logo.getName())
        FileOutputStream fos = new FileOutputStream(copy)
        fos.write(decodeBase64(encoded))
        fos.close()
    }

    void tearDown() {
        copy?.delete()
    }

    void testGenerateMD5(){
        assertEquals(generateFileMD5(copy), generateMD5(encoded))
    }

    void testGenerateSHA1(){
        assertEquals(generateFileSHA1(copy), generateSHA1(encoded))
    }

    void testGenerateSHA256(){
        assertEquals(generateFileSHA256(copy), generateSHA256(encoded))
    }
}
