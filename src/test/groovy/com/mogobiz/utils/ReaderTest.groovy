package com.mogobiz.utils

import com.mogobiz.tools.CsvLine
import com.mogobiz.tools.Reader
import rx.functions.Action1

/**
 *
 * Created by smanciot on 25/07/16.
 */
class ReaderTest extends GroovyTestCase{

    void testReadOffers(){
        def offers = ReaderTest.class.getResource("offers.csv").path
        assertNotNull(offers)
        rx.Observable<CsvLine> lines = Reader.parseCsvFile(offers)
        def results = lines.toBlocking()
        results.forEach(new Action1<CsvLine>() {
            @Override
            void call(CsvLine csvLine) {
                log.info(csvLine.fields.toMapString())
            }
        })
        assertEquals(5, results.iterator.size())
    }

}
