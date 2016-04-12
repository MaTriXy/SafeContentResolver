Change Log
==========

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

 * Initital release
