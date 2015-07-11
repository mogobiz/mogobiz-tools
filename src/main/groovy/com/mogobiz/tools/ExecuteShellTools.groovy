package com.mogobiz.tools

import groovy.util.logging.Log4j

/**
 *
 * Created by stephane.manciot@ebiznext.com on 25/01/15.
 */
@Log4j
final class ExecuteShellTools {

    private ExecuteShellTools(){}

    static String executeCommand(String command) {

        StringBuffer output = new StringBuffer()

        Process p
        try {
            p = Runtime.getRuntime().exec(command)
            p.waitFor()
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()))

            String line
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n")
            }

        } catch (Exception e) {
            log.error(e.message)
        }

        return output.toString()

    }
}
