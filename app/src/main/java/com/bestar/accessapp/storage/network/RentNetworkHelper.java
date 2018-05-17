package com.bestar.accessapp.storage.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bestar.accessapp.storage.network.module.RecordModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ted on 2018/4/27.
 * in com.bestar.accessapp.storage.network
 */
public class RentNetworkHelper {

    public static void insert(@NonNull RecordModule rentRecord) {

        Call<BaseResult> tickerInfoCall = WebCloudApi.getIns().postHouseInfo(rentRecord);
        tickerInfoCall.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                Log.d("xiongwei", "成功！！！！！" + response.toString());
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                Log.d("xiongwei", "onFailure" + t.toString());
            }
        });

    }
}
