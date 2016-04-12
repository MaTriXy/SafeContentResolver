# SafeContentResolver

A replacement for Android's [`ContentResolver`](https://developer.android.com/reference/android/content/ContentResolver.html)
that protects against the *Surreptitious Sharing* attack.

## Surreptitious Sharing

Read all about it in the [corresponding blog post](https://www.ibr.cs.tu-bs.de/news/ibr/surreptitious-sharing-2016-04-04.xml).

## Usage

Replace all occurrences of [`ContentResolver.openInputStream()`](https://developer.android.com/reference/android/content/ContentResolver.html#openInputStream(android.net.Uri))
where URIs provided by other apps are opened with `SafeContentResolver.openInputStream()`.

`SafeContentResolver` will refuse to open `file://` URIs pointing to files belonging to your app and `content://` URIs
belonging to content providers of your app.  
If you wish to allow access to certain content providers, add the following `<meta-data>` element to the appropriate
`<provider>` entries in your manifest:
```xml
<provider … >
    <meta-data
        android:name="de.cketti.safecontentresolver.ALLOW_INTERNAL_ACCESS"
        android:value="true" />
</provider>
```

The library comes in two flavors `safe-content-resolver-v14` and `safe-content-resolver-v21`. The former includes
native code to be able to invoke the `fstat` system call that is used to retrieve the owner of a file. Starting with
Lollipop (API 21) the framework includes the class [`Os`](https://developer.android.com/reference/android/system/Os.html)
to access this functionality. So `safe-content-resolver-v21` is free of native code and thus much smaller.

To retrieve an instance of `SafeContentResolver` use:
```java
SafeContentResolver safeContentResolver = SafeContentResolverCompat.newInstance(context);
```

If your `minSdkVersion` is 21 or higher you only need to include `safe-content-resolver-v21` and use the following code:
```java
SafeContentResolver safeContentResolver = SafeContentResolver.newInstance(context);
```

## Include the library

```groovy
compile 'de.cketti.safecontentresolver:safe-content-resolver-v14:0.1.0'
```

Or, if you're using `minSdkVersion` 21 or higher:

```groovy
compile 'de.cketti.safecontentresolver:safe-content-resolver-v21:0.1.0'
```

## Native code

`safe-content-resolver-v14` contains native code for the following ABIs:
* armeabi
* armeabi-v7a
* arm64-v8a
* x86
* x86_64
* mips
* mips64

If you don't want to include all of them in your APK you might want to look into
[ABIs Splits](http://tools.android.com/tech-docs/new-build-system/user-guide/apk-splits#TOC-ABIs-Splits).


## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
