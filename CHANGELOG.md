Change Log
==========

## Version 1.0.0

_2020-06-24_

 * Removed dependency on com.android.support:support-annotations so the library doesn't have to be jetified
   when used in an app with AndroidX.
 * Updated to ReLinker 1.4.1
 * Supported ABIs: armeabi-v7a, arm64-v8a, x86, x86_64

## Version 0.9.0

_2016-08-04_

 * Use [ReLinker](https://github.com/KeepSafe/ReLinker) to load the os-compat library. This should avoid
   `UnsatisfiedLinkError` crashes.

## Version 0.1.0

_2016-04-12_

 * In addition to files belonging to the app `SafeContentResolver` now blocks access to content providers belonging to
   the app.  
   To allow access to a specific content provider, add the following `<meta-data>` element to the appropriate
   `<provider>` entry in your manifest:

   ```xml
   <provider … >
       <meta-data
           android:name="de.cketti.safecontentresolver.ALLOW_INTERNAL_ACCESS"
           android:value="true" />
   </provider>
   ```

## Version 0.0.1

_2016-04-04_

 * Initial release
