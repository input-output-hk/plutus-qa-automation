package io.iohk.utils.listeners;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * A Marker annotation which is used to express the intent that a particular test method
 * can be executed more than one time. The number of times that a test method should be
 * iterated is governed by the JVM argument : <code>-Diteration.count</code>. The default value
 * is <code>3</code>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface CanRunMultipleTimes {
}
