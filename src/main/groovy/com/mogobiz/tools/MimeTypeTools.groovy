/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools

import groovy.util.logging.Log4j
import org.apache.tika.Tika

import java.util.regex.Matcher
import java.util.regex.Pattern

import static ExecuteShellTools.*

/**
 *
 */
@Log4j
final class MimeTypeTools {

    def static final Pattern FORMAT    = ~/(.*)\/(.*)/

    private MimeTypeTools(){}

    static String detectMimeType(File file, String filenameWithExtension = file?.name) {
        String type = null
        if(file?.exists()){
            try{
                Tika tika = new Tika();
                type = tika.detect(file)
                log.info('detect mimeType ' + type + ' for ' + filenameWithExtension)
            }
            catch(IOException io){
                log.error('could not detect mimeType for ' + filenameWithExtension + ' ' + io.message)
            }
        }
        type
    }

    static String toFormat(File file){
        toFormat(detectMimeType(file))
    }

    static String toFormat(String mimeType) {
        String format = null
        if(mimeType){
            Matcher matcher = FORMAT.matcher(mimeType)
            if (matcher.find() && matcher.groupCount() > 1){
                format = matcher.group(2)
            }
        }
        format
    }
}
