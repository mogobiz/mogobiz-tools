/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools

import groovy.util.logging.Log4j
import rx.Subscription
import rx.subscriptions.Subscriptions

/**
 *
 * Created by stephane.manciot@ebiznext.com on 25/01/2015.
 */
@Log4j
final class Reader {
    private Reader(){}

    static final Closure<String> identity = {String l -> l}
    static final Closure<String> trim = {String l -> l.trim()}

    static Map<String, String> convertLineToMap(String[] keys, String line, String separator = ';', Closure<String> transformation = identity){
        def tokens = transformation(line).split(separator)
        def map = [:]
        if(tokens.size() == keys.size()){
            final len = tokens.size() - 1
            (0..len).each {i ->
                final k = keys[i].trim().replaceAll("^\"", "").replaceAll("\"\$", "")
                final v = tokens[i].trim().replaceAll("^\"", "").replaceAll("\"\$", "")
                map[k] = v.length() > 0 ? v : null
            }
        }
        else{
            log.warn("${tokens.size()} != keys size ${keys.size()} -> $line")
        }
        return map
    }

    static rx.Observable<Map> parseText(
            final String text,
            String separator = ';',
            Closure<String> keysTransformation = identity,
            Closure<String> tokensTransformation = identity){
        rx.Observable.create(new rx.Observable.OnSubscribeFunc<Map>() {
            @Override
            Subscription onSubscribe(rx.Observer observer) {
                try{
                    String[] keys = null
                    text.eachLine {String line, int count ->
                        if(count == 0){
                            keys = keysTransformation(line).split(separator)
                        }
                        else if(line.trim().length() > 0){
                            observer.onNext(convertLineToMap(keys, line, separator, tokensTransformation))
                        }
                    }
                }
                catch(Throwable th){
                    observer.onError(th)
                }

                observer.onCompleted()

                return Subscriptions.empty()
            }
        })
    }

    static rx.Observable<CsvLine> parseCsvFile(final String filename, final String charset = 'UTF-8', final String separator = ';', Closure<String> transformation = identity){
        parseCsvFile(new File(filename), charset, separator, transformation)
    }

    static rx.Observable<CsvLine> parseCsvFile(final File file, final String charset = 'UTF-8', final String separator = ';', Closure<String> transformation = identity){
        rx.Observable.create(new rx.Observable.OnSubscribeFunc<CsvLine>() {
            @Override
            Subscription onSubscribe(rx.Observer observer) {
                try{
                    file.eachLine(charset, {String line, lineNumber ->
                        if(line.trim().length() > 0){
                            observer.onNext(
                                    new CsvLine(
                                            fields: transformation(line).split(separator),
                                            number: lineNumber
                                    )
                            )
                        }
                    })
                }
                catch(Throwable th){
                    observer.onError(th)
                }

                observer.onCompleted()

                return Subscriptions.empty()
            }
        })
    }
}

class CsvLine{
    String[] fields
    int number
}
