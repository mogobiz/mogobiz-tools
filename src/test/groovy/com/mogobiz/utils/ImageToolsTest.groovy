/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.utils

import org.junit.Test

import static com.mogobiz.tools.ImageTools.*
import static com.mogobiz.tools.Base64Tools.*
/**
 *
 */
class ImageToolsTest extends GroovyTestCase {
    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testEncodeBase64(){
        File logo = new File(ImageToolsTest.class.getResource("logo.png").path)
        assertNotNull(logo)
        final encoded = encodeBase64(logo)
        assertNotNull(encoded)
        log.info(encoded)
    }

    @Test
    void testDecodeBase64(){
        File logo = new File(ImageToolsTest.class.getResource("logo.png").path)
        assertNotNull(logo)
        def fis = new FileInputStream(logo)
        fis.close()
        final encoded = encodeBase64(logo)
        assertNotNull(encoded)

        byte[] bytes = decodeBase64(encoded)
        assertNotNull(bytes)

        final decoded = new File("${logo.absolutePath}-generated")

        def is = new ByteArrayInputStream(bytes)
        def os = new FileOutputStream(decoded)
        byte[] buffer = new byte[1024]
        int len
        while ((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len)
        }
        is.close()
        os.close()

        assertEquals(encoded, encodeBase64(decoded))
    }

    void testResizeImage(){
        File logo = new File(ImageToolsTest.class.getResource("logo.png").path)
        assertNotNull(logo)
        def files = resizeImage(logo)
        assertNotNull(files)
        assertEquals(2, files.size())
        files.each{log.info(it.absolutePath)}
    }
}
