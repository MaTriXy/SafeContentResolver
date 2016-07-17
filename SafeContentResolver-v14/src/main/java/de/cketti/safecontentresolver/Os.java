/*
 * Copyright (C) 2016 cketti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cketti.safecontentresolver;


import android.content.Context;

import com.getkeepsafe.relinker.MissingLibraryException;
import com.getkeepsafe.relinker.ReLinker;


class Os {
    private static final String LIBRARY_NAME = "os-compat";

    private static Context context;
    private static boolean libraryNeedsLoading = true;
    private static UnsupportedOperationException loadFailedException;


    synchronized static void init(Context context) {
        if (context == null) {
            throw new NullPointerException("Argument 'context' must not be null");
        }

        // Only get the context here. Load the library before doing the actual work (hopefully in a background thread).
        if (Os.context == null) {
            Os.context = context.getApplicationContext();
        }
    }

    static int fstat(int fileDescriptor) throws ErrnoException, UnsupportedOperationException {
        synchronized (Os.class) {
            if (context == null) {
                throw new IllegalStateException("Call Os.init(Context) before attempting to call Os.fstat()");
            }

            if (libraryNeedsLoading) {
                loadLibrary();
            } else if (loadFailedException != null) {
                throw loadFailedException;
            }
        }

        return nativeFstat(fileDescriptor);
    }

    private static void loadLibrary() {
        libraryNeedsLoading = false;
        try {
            ReLinker.loadLibrary(context, LIBRARY_NAME);
        } catch (MissingLibraryException | UnsatisfiedLinkError e) {
            loadFailedException = new UnsupportedOperationException("Failed to load native library " + LIBRARY_NAME, e);
            throw loadFailedException;
        }
    }

    private static native int nativeFstat(int fileDescriptor) throws ErrnoException;
}
