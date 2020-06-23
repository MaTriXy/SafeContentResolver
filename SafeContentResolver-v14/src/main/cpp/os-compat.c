/*
 * Copyright (C) 2016 cketti
 * Copyright (C) 2009 The Android Open Source Project
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

#include <jni.h>
#include <sys/stat.h>
#include <errno.h>


#define TEMP_FAILURE_RETRY(exp) ({         \
    __typeof__(exp) _rc;                   \
    do {                                   \
        _rc = (exp);                       \
    } while (_rc == -1 && errno == EINTR); \
    _rc; })


JNIEXPORT jint JNICALL
Java_de_cketti_safecontentresolver_Os_nativeFstat(JNIEnv *env, jclass type, jint fileDescriptor) {
    struct stat sb;

    int rc = TEMP_FAILURE_RETRY(fstat(fileDescriptor, &sb));
    if (rc == -1) {
        int error = errno;
        jclass errnoExceptionClass = (*env)->FindClass(env, "de/cketti/safecontentresolver/ErrnoException");
        jmethodID constructor = (*env)->GetMethodID(env, errnoExceptionClass, "<init>", "(Ljava/lang/String;I)V");
        jstring functionName = (*env)->NewStringUTF(env, "fstat");
        jobject exception = (*env)->NewObject(env, errnoExceptionClass, constructor, functionName, error);
        (*env)->Throw(env, exception);
        return 0;
    }

    return sb.st_uid;
}
