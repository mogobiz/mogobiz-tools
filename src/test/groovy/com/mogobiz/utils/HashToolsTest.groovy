/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.utils

import static com.mogobiz.tools.Base64Tools.*
import static com.mogobiz.tools.ImageTools.encodeBase64
import static com.mogobiz.tools.HashTools.*

/**
 *
 */
class HashToolsTest extends GroovyTestCase {

    final tmp = System.getProperty('java.io.tmpdir') ?: '/tmp'

    def encoded
    def decoded
    File copy

    void setUp() {
        File logo = new File(ImageToolsTest.class.getResource("logoPDF.png").path)
        assertNotNull(logo)
        encoded = encodeBase64(logo, true)
        decoded = decodeBase64(encoded)
        copy = new File(tmp, logo.getName())
        FileOutputStream fos = new FileOutputStream(copy)
        fos.write(decoded)
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
