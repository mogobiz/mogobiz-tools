package com.mogobiz.tools

import scala.Option
import scala.collection.Iterable

import static scala.collection.JavaConverters.collectionAsScalaIterableConverter

/**
 *
 * Created by smanciot on 17/06/16.
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
