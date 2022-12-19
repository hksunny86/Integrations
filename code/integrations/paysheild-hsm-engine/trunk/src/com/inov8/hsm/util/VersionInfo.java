package com.inov8.hsm.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// on class level
public @interface VersionInfo {

	public enum Priority {
		LOW, MEDIUM, HIGH
	}

	Priority priority() default Priority.MEDIUM;

	String[] tags() default "";
	
	String version() default "1.0";

	String createdBy() default "";

	String lastModified() default "20/01/2014";
	
	String releaseVersion() default "";
	
	String patchVersion() default "";
	
	String notes() default "";

}