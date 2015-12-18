/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools

import groovy.util.logging.Log4j
import org.apache.commons.codec.binary.Base64

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 *
 */
@Log4j
class FileTools {

    def static String encodeBase64(File file){
        def encoded = null
        if(file?.exists()){
            FileChannel inChannel = Files.newByteChannel(Paths.get(file.toURI())) as FileChannel
            ByteBuffer buffer = ByteBuffer.allocate(1024)
            ByteArrayOutputStream bos = new ByteArrayOutputStream()
            try {
                while(inChannel.read(buffer) > 0)
                {
                    buffer.flip()
                    bos.write(buffer.array())
                    buffer.clear()
                }
                encoded = Base64.encodeBase64String(bos.toByteArray())
            } catch (IOException e) {
                log.error(e.message, e)
            }
            finally{
                inChannel.close()
                bos.close()
            }
        }
        encoded
    }

    static List<File> scan(String folder, String glob = "*") throws Exception{
        List<File> files = new ArrayList<>()
        Path dir = Paths.get(folder)
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            log.warn(String.format("No such directory %s", folder))
        }
        else{
            DirectoryStream<Path> ds = null
            try {
                ds = Files.newDirectoryStream(dir, glob)
                //iterate over the content of the directory
                for (Path path : ds) {
                    files.add(path.toFile())
                }
            } catch (IOException io) {
                log.error(io.message, io)
            }
            finally {
                ds?.close()
            }
        }
        log.debug("found ${files?.size() ?: 0} file(s) matching $glob within $folder")
        return files
    }

}
