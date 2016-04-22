/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools

import com.mortennobel.imagescaling.AdvancedResizeOp
import com.mortennobel.imagescaling.ResampleOp
import groovy.util.logging.Log4j

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.image.BufferedImage

import static Base64Tools.encodeBase64
import static MimeTypeTools.*

/**
 *
 */
@Log4j
final class ImageTools {

    private ImageTools(){}

    def static String encodeImageBase64(File f, boolean encodeAsURI = false){
        def encoded = null
        if(f?.exists()){
            ByteArrayOutputStream bos = new ByteArrayOutputStream()
            try {
                final mimeType = detectMimeType(f)
                ImageIO.write(ImageIO.read(f), toFormat(mimeType), bos)
                encoded = encodeBase64(bos.toByteArray(), encodeAsURI ? mimeType:null)
                bos.close()
            } catch (IOException e) {
                log.error(e.message, e)
            }
        }
        return encoded
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
                int topMargin = 0
                int leftMargin = 0
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
                else {
                    if (originalWidth > originalHeight) {
                        height = originalHeight * width /originalWidth
                        topMargin = (width - height)/2
                    }
                    else {
                        width = originalWidth * height /originalHeight
                        leftMargin = (height - width)/2
                    }
                    log.info("resize image ${originalWidth}x$originalHeight to ${out.absolutePath}")
                    BufferedImage dest = Scalr.resize(src, Scalr.Method.ULTRA_QUALITY, width, height);
                    BufferedImage dest2 = Scalr.move(dest, leftMargin, topMargin, Color.WHITE);
                    ImageIO.write(dest2, format, out)
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
