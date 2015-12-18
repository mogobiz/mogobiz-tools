/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.utils

import org.junit.Test

import static com.mogobiz.tools.MimeTypeTools.*
/**
 *
 */
class MimeTypeToolsTest extends GroovyTestCase {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testDetectMimeType(){
        File image1 = new File(MimeTypeToolsTest.class.getResource("image1.jpg").path)
        assertNotNull(image1)
        assertEquals("image/jpeg", detectMimeType(image1))
        File image2 = new File(MimeTypeToolsTest.class.getResource("image2.png").path)
        assertNotNull(image2)
        assertEquals("image/png", detectMimeType(image2))
        File pdf = new File(MimeTypeToolsTest.class.getResource('document.pdf')?.path)
        assertNotNull(pdf)
        assertEquals("application/pdf", detectMimeType(pdf))
    }

    @Test
    void testToFormat(){
        String format1 = toFormat("image/jpg")
        assertNotNull(format1)
        assertEquals("jpg", format1)
        String format2 = toFormat("image/png")
        assertNotNull(format2)
        assertEquals("png", format2)
        String format3 = toFormat("application/pdf")
        assertNotNull(format3)
        assertEquals("pdf", format3)
    }
}
