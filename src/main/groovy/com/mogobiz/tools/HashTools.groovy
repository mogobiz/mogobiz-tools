/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */
package com.mogobiz.tools

import groovy.util.logging.Log4j
import org.apache.commons.codec.binary.Base64

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import static com.mogobiz.tools.Base64Tools.decodeBase64

/**
 *
 */
@Log4j
final class HashTools {

    private HashTools(){}

    def static String generateMD5(String message){
        hashString(message, 'MD5')
    }

    def static String generateSHA1(String message){
        hashString(message, 'SHA-1')
    }

    def static String generateSHA256(String message){
        hashString(message, 'SHA-256')
    }

    def static String generateFileMD5(File file){
        hashFile(file, 'MD5')
    }

    def static String generateFileSHA1(File file){
        hashFile(file, 'SHA-1')
    }

    def static String generateFileSHA256(File file){
        hashFile(file, 'SHA-256')
    }

    private static String hashString(String message = '', String algorithm = 'MD5')
            throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = MessageDigest.getInstance(algorithm)
        byte[] hashedBytes = digest.digest(decodeBase64(message))
        return Base64.encodeBase64String(hashedBytes)
    }

    private static String hashFile(File file, String algorithm = 'MD5')
            throws NoSuchAlgorithmException, IOException {
        def encoded = null
        if(file?.exists()){
            FileInputStream inputStream = new FileInputStream(file)
            MessageDigest digest = MessageDigest.getInstance(algorithm)

            byte[] bytesBuffer = new byte[1024]
            int bytesRead = -1

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead)
            }

            byte[] hashedBytes = digest.digest()

            encoded = Base64.encodeBase64String(hashedBytes)
        }
        encoded
    }
}
