package com.mogobiz.utils

import com.mogobiz.tools.FileTools
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.junit.Test

/**
 *
 * Created by smanciot on 11/04/15.
 */
class FileToolsTest extends GroovyTestCase{

    @Test
    void testEncodeBase64(){
        File logo = new File(FileToolsTest.class.getResource("document.pdf").path)
        def encoded = FileTools.encodeBase64(logo)
        assertNotNull(encoded)
        byte[] bytes = Base64.decodeBase64(encoded)
        File out = new File("/tmp/test.pdf")
        FileOutputStream fos = new FileOutputStream(out)
        fos.write(bytes)
        fos.close()
        def digest = DigestUtils.md5Hex(new FileInputStream(out))
        log.info(digest)
    }

}
