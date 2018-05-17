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

import com.bestar.accessapp.BaseApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.storage.network.module.RecordModule;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ted on 2016/4/12.
 *
 * @ com.mr.xiong.movie.network
 */
public class WebCloudApi {

    public static WebCloudApi instance;

    public static WebCloudApi getIns() {
        if (null == instance) {
            synchronized (WebCloudApi.class) {
                if (null == instance) {
                    instance = new WebCloudApi();
                }
            }
        }
        return instance;
    }

    public static String ENDPOINT = ServerConfig.getApiPre();

    private final CloudService mWebService;

    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create();

    public WebCloudApi() {
        Cache cache = null;
        OkHttpClient okHttpClient;
        try {
            File cacheDir =
                    new File(BaseApp.getContext().getCacheDir().getPath(), "wkzf_cache.json");
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (null != cache) {
            builder.cache(cache);
        }
        okHttpClient = builder.addInterceptor(new BaseRequestInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mWebService = retrofit.create(CloudService.class);
    }

    public static void reset(){
        instance = null;
    }

    /****************************************************************/

    public Call<BaseResult> postHouseInfo(RecordModule record) {
        return mWebService.postHouseInfo(record);
    }


}
