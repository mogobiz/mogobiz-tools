/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools

import groovy.util.logging.Log4j
import rx.Subscriber
import rx.Subscription

/**
 *
 */
@Log4j
final class Reader {
    private Reader(){}

    static final Closure<String> identity = {String l -> l}
    static final Closure<String> trim = {String l -> l?.trim()}
    static final Closure<String> unDoubleQuotes = { String l -> l?.replaceAll("^\"", "")?.replaceAll("\"\$", "")}
    static final Closure<String> trimAndUnDoubleQuotes = (trim >> unDoubleQuotes)

    static Map<String, String> convertLineToMap(
            String[] keys,
            String line,
            String separator = ';',
            Closure<String> transformation = identity){
        String[] tokens = transformation(line).split(separator)
        def map = [:]
        if(tokens.size() == keys.size()){
            final len = tokens.size() - 1
            (0..len).each {i ->
                final k = keys[i]
                final v = trimAndUnDoubleQuotes.call(tokens[i])
                map[k] = v.length() > 0 ? v : null
            }
        }
        else{
            log.warn("${tokens.size()} != keys size ${keys.size()} -> $line")
        }
        return map
    }

    static rx.Observable<CsvLine> parseText(
            final String text,
            String separator = ';',
            Closure<String> keysTransformation = identity,
            Closure<String> tokensTransformation = identity){
        rx.Observable.create(new rx.Observable.OnSubscribe<CsvLine>() {
            @Override
            void call(Subscriber<? super CsvLine> subscriber) {
                def subscription = new InnerSubscription()
                subscriber.add(subscription)
                try{
                    String[] keys = null
                    subscriber.onStart()
                    text.eachLine {String line, int count ->
                        if(count == 0){
                            keys = keysTransformation(line).split(separator).collect {trimAndUnDoubleQuotes(it)}
                        }
                        else if(line.trim().length() > 0){
                            if(!subscription.isUnsubscribed()) {
                                subscriber.onNext(
                                        new CsvLine(
                                                keys: keys,
                                                fields: convertLineToMap(keys, line, separator, tokensTransformation),
                                                number: count+1
                                        )
                                )
                            }
                        }
                    }
                }
                catch(Throwable th){
                    subscriber.onError(th)
                }

                subscriber.onCompleted()

            }
        })
    }

    static rx.Observable<CsvLine> parseCsvFile(
            final String filename,
            final String charset = 'UTF-8',
            final String separator = ';',
            Closure<String> keysTransformation = identity,
            Closure<String> tokensTransformation = identity){
        parseCsvFile(new File(filename), charset, separator, keysTransformation, tokensTransformation)
    }

    static rx.Observable<CsvLine> parseCsvFile(
            final File file,
            final String charset = 'UTF-8',
            final String separator = ';',
            Closure<String> keysTransformation = identity,
            Closure<String> tokensTransformation = identity){
        rx.Observable.create(new rx.Observable.OnSubscribe<CsvLine>() {
            @Override
            void call(Subscriber<? super CsvLine> subscriber) {
                try{
                    def subscription = new InnerSubscription()
                    subscriber.add(subscription)
                    String[] keys = null
                    subscriber.onStart()
                    file.eachLine(charset, {String line, lineNumber ->
                        if(line.trim().length() > 0){
                            if(lineNumber == 1){
                                keys = keysTransformation(line).split(separator).collect {trimAndUnDoubleQuotes(it)}
                            }
                            else if(!subscription.isUnsubscribed()){
                                subscriber.onNext(
                                        new CsvLine(
                                                keys: keys,
                                                fields: convertLineToMap(keys, line, separator, tokensTransformation),
                                                number: lineNumber
                                        )
                                )
                            }
                        }
                    })
                }
                catch(Throwable th){
                    subscriber.onError(th)
                }
                subscriber.onCompleted()
            }
        })
    }
}

class CsvLine{
    String[] keys
    Map<String, String> fields = [:]
    int number
}

class InnerSubscription implements Subscription{
    private boolean unsubscribed = false;
    @Override
    void unsubscribe() {
        unsubscribed = true
    }

    @Override
    boolean isUnsubscribed() {
        return unsubscribed
    }
}
