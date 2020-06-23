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


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.os.Bundle;


/**
 * Stores a (lazily built) list of all of the app's content providers that are not explicitly allowed to be accessed
 * using SafeContentResolver.
 */
class DisallowedProviders {
    private static final String META_DATA_KEY_ALLOW_INTERNAL_ACCESS =
            "de.cketti.safecontentresolver.ALLOW_INTERNAL_ACCESS";


    private final Context context;
    private Set<String> disallowedAuthorities;


    DisallowedProviders(Context context) {
        this.context = context;
    }

    synchronized boolean isDisallowed(String authority) {
        if (disallowedAuthorities == null) {
            disallowedAuthorities = findDisallowedContentProviderAuthorities();
        }

        return disallowedAuthorities.contains(authority);
    }

    private Set<String> findDisallowedContentProviderAuthorities() {
        ProviderInfo[] providers = getProviderInfo(context);

        Set<String> disallowedAuthorities = new HashSet<>(providers.length);
        for (ProviderInfo providerInfo : providers) {
            if (!isContentProviderAllowed(providerInfo)) {
                String[] authorities = providerInfo.authority.split(";");
                Collections.addAll(disallowedAuthorities, authorities);
            }
        }

        return disallowedAuthorities;
    }

    private ProviderInfo[] getProviderInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_PROVIDERS | PackageManager.GET_META_DATA);

            ProviderInfo[] providers = packageInfo.providers;
            return providers != null ? providers : new ProviderInfo[0];
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isContentProviderAllowed(ProviderInfo providerInfo) {
        Bundle metaData = providerInfo.metaData;
        return metaData != null && metaData.getBoolean(META_DATA_KEY_ALLOW_INTERNAL_ACCESS, false);
    }
}
