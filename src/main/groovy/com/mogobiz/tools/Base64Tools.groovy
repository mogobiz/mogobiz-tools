/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools

import groovy.util.logging.Log4j
import org.apache.commons.codec.binary.Base64

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 */
@Log4j
final class Base64Tools {

    def static final Pattern DATA_ENCODED = ~/data:(.*);base64,(.*)/

    private Base64Tools(){}

    def static String encodeBase64(byte[] bytes, String mimeType){
        (mimeType ? "data:$mimeType;base64," : "") + Base64.encodeBase64String(bytes)
    }

    def static byte[] decodeBase64(String encoded = '') {
        def ret = encoded
        def encodedBase64 = false
        Matcher matcher = DATA_ENCODED.matcher(encoded)
        if (matcher.find() && matcher.groupCount() > 1) {
            final type = matcher.group(1)
            log.debug("extract mime type $type from $encoded")
            ret = matcher.group(2)
            encodedBase64 = true
        }
        if(encodedBase64 || Base64.isBase64(ret)){
            Base64.decodeBase64(ret)
        }
        else{
            ret.getBytes('UTF-8')
        }
    }

}
