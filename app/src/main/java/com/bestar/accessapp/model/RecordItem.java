package com.bestar.accessapp.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by ted on 2018/4/17.
 * in com.tedxiong.helper.data
 */
public class RecordItem {
    private String houseTitle;
    private String housePhone;
    private int callType = 0;//0代表直接拨打 1代表跳转拨号页面

    public String getHouseTitle() {
        return houseTitle;
    }

    public void setHouseTitle(String houseTitle) {
        this.houseTitle = houseTitle;
    }

    public String getHousePhone() {
        return housePhone;
    }

    public void setHousePhone(String housePhone) {
        this.housePhone = housePhone;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public boolean sameAs(@NonNull RecordItem item) {
        if (TextUtils.isEmpty(item.getHouseTitle()) || TextUtils.isEmpty(getHouseTitle())) {
            return item.getHousePhone().equals(getHousePhone());
        } else return item.getHousePhone().equals(getHousePhone())
                && item.getHouseTitle().equals(getHouseTitle());
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(getHousePhone());
    }
}
