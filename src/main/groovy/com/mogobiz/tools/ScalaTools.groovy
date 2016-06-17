/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */
package com.mogobiz.tools

import scala.Option
import scala.collection.Iterable

import static scala.collection.JavaConverters.collectionAsScalaIterableConverter

/**
 *
 */
final class ScalaTools {

    private ScalaTools(){}

    static <T> scala.collection.immutable.List<T> toScalaList(List<T> list) {
        ((collectionAsScalaIterableConverter(list).asScala()) as Iterable<T>).toList()
    }

    static <T> Option<T> toScalaOption(T o){
        Option.apply(o)
    }

}
