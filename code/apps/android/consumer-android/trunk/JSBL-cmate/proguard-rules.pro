# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#
-dontwarn org.xmlpull.v1.**
-dontwarn org.bouncycastle.**

-dontwarn android.support.v7.**
-dontwarn android.support.v4.**

-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.opencv.**


-keep class android.support.v7.**{*;}
-keep class  android.support.v4.**{*;}
-keep class  org.opencv.**{*;}
-dontwarn  com.paysyslabs.instascan.**
-keep class com.paysyslabs.instascan.**{*;}
-keep class android.support.v7.widget.AppCompatImageView**{*;}

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}



