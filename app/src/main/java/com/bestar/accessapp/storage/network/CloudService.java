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


import com.bestar.accessapp.storage.network.module.RecordModule;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ted on 2016/5/9.
 *
 */
public interface CloudService {

    //-------------------POST----------------------------
    @POST("rent/58city")
    Call<BaseResult> postHouseInfo(@Body RecordModule record);
}
