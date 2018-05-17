/*
 *
 * Copyright 2015 TedXiong xiong-wei@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bestar.accessapp.storage.network;

import android.text.TextUtils;

/**
 * Created by Ted on 2016/10/25.
 */

public class ServerConfig {
    public static final String API_PRE = "http://10.0.16.95:5000/";
    public static final String API_SUFFIX = "rent/58city";

    private static String API_PRE_CUSTOM = API_PRE;

    public static String getApiPre() {
        if (TextUtils.isEmpty(API_PRE_CUSTOM)) return API_PRE;
        return API_PRE_CUSTOM;
    }

    public static void setApiPreCustom(String apiPreCustom) {
        if (TextUtils.isEmpty(apiPreCustom)) return;
        if (!apiPreCustom.endsWith("/")) return;
        API_PRE_CUSTOM = apiPreCustom;
    }
}
