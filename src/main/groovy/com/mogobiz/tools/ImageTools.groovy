/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools

import com.mortennobel.imagescaling.AdvancedResizeOp
import com.mortennobel.imagescaling.ResampleOp
import groovy.util.logging.Log4j
import org.apache.commons.codec.binary.Base64

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.util.regex.Matcher
import java.util.regex.Pattern

import static MimeTypeTools.*

/**
 *
 * Created by stephane.manciot@ebiznext.com on 25/01/15.
 */
@Log4j
final class ImageTools {

    def static final Pattern DATA_ENCODED = ~/data:(.*);base64,(.*)/

    private ImageTools(){}

    def static String encodeBase64(File f, boolean encodeAsURI = false){
        def encoded = null
        if(f?.exists()){
            ByteArrayOutputStream bos = new ByteArrayOutputStream()
            try {
                final mimeType = detectMimeType(f)
                ImageIO.write(ImageIO.read(f), toFormat(mimeType), bos)
                encoded = (encodeAsURI ? "data:$mimeType;base64," : "") + Base64.encodeBase64String(bos.toByteArray())
                bos.close()
            } catch (IOException e) {
                log.error(e.message, e)
            }
        }
        return encoded
    }

    def static byte[] decodeBase64(String encoded, boolean encodedAsURI = false){
        def ret = null
        if(encodedAsURI){
            Matcher matcher = DATA_ENCODED.matcher(encoded)
            if (matcher.find() && matcher.groupCount() > 1){
                final type = matcher.group(1)
                log.debug("extract mime type $type from $encoded")
                ret = Base64.decodeBase64(matcher.group(2))
            }
        }
        else{
            ret = Base64.decodeBase64(encoded)
        }
        ret
    }

    def static Collection<File> resizeImage(File file){
        def files = []
        if(file?.exists()){
            final mimeType = detectMimeType(file)
            if(mimeType.startsWith("image/")){
                final format = toFormat(mimeType)
                for (size in ImageSize.values()){
                    files << resizeImage(file, format, size.width(), size.height())
                }
            }
        }
        files
    }

    def static File resizeImage(File file, String format = toFormat(detectMimeType(file)), int width, int height){
        File out = null
        if(file?.exists() && format && width > 0 && height > 0){
            out = new File("${file.absolutePath}.${width}x$height.$format")
            if(!out.exists()){
                BufferedImage src = ImageIO.read(file)
                int originalWidth = src.width
                int originalHeight = src.height
                if(width == originalWidth && height == originalHeight){
                    InputStream is = new FileInputStream(file)
                    OutputStream os = new FileOutputStream(out)
                    byte[] buffer = new byte[1024]
                    int len
                    while ((len = is.read(buffer)) > 0) {
                        os.write(buffer, 0, len)
                    }
                    is.close()
                    os.close()
                }
                else{
                    log.info("resize image ${originalWidth}x$originalHeight to ${out.absolutePath}")
                    ResampleOp resampleOp = new ResampleOp (width, height)
                    resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp)
                    BufferedImage dest = resampleOp.filter(src, null)
                    ImageIO.write(dest, format, out)
                }
            }
        }
        out
    }
    public static File getFile(File resFile, ImageSize size, boolean create) {
        File file = null
        if(resFile?.exists()){
            final format = toFormat(detectMimeType(resFile))
            file = new File("${resFile.absolutePath}.${size.width()}x${size.height()}.$format");
            if (!file.exists() && create) {
                resizeImage(resFile)
            }
        }
        return file
    }

    public static void deleteAll(File resFile) {
        if(resFile?.exists()){
            final format = toFormat(detectMimeType(resFile))
            resFile.delete()
            for (size in ImageSize.values()) {
                File file = new File("${resFile.absolutePath}.${size.width()}x${size.height()}.$format")
                file.delete()
            }
        }
    }

}
