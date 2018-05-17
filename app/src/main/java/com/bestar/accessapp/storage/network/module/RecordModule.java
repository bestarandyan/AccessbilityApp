package com.bestar.accessapp.storage.network.module;

/**
 * Created by ted on 2018/4/27.
 * in com.bestar.accessapp.storage.network.module
 */
public class RecordModule {
    private Record data;

    public Record getData() {
        return data;
    }

    public void setData(Record data) {
        this.data = data;
    }

    public static class Record {
        public String title;//标题
        public String rentPrice;//租金
        public String houseType;//户型
        public String spaceArea;//面积
        public String landlord_phone;//电话号码 如果是转接号，则为空
        public String cityName;//城市名称
    }

}




