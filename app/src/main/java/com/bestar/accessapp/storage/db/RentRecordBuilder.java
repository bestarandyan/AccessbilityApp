package com.bestar.accessapp.storage.db;

import com.bestar.accessapp.storage.db.module.RentHouseRecord;
import com.bestar.accessapp.storage.network.module.RecordModule;
import com.bestar.accessapp.util.DateUtil;

/**
 * Created by ted on 2018/4/23.
 * in com.bestar.accessapp.storage.db
 */
public class RentRecordBuilder {
    private String title;//标题
    private String rental;//租金
    private String type;//户型
    private String area;//面积
    private String phone;//电话号码 如果是转接号，则为空
    private String cityName;//城市名称

    public RentRecordBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public RentRecordBuilder setRental(String rental) {
        this.rental = rental;
        return this;
    }

    public RentRecordBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public RentRecordBuilder setArea(String area) {
        this.area = area;
        return this;
    }

    public RentRecordBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public RentRecordBuilder setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public RentHouseRecord create() {
        RentHouseRecord record = new RentHouseRecord();
        record.setId(System.currentTimeMillis());
        record.setDayTime(DateUtil.getDayTime());
        record.setDate(DateUtil.getToday());
        record.setTitle(title);
        record.setArea(area);
        record.setPhone(phone);
        record.setRental(rental);
        record.setType(type);
        record.setCityName(cityName);
        return record;
    }

    public RecordModule createModule() {
        RecordModule recordModule = new RecordModule();
        RecordModule.Record record = new RecordModule.Record();
        record.cityName = cityName;
        record.title = title;
        record.houseType = type;
        record.spaceArea = area;
        record.rentPrice = rental;
        record.landlord_phone = phone;
        recordModule.setData(record);
        return recordModule;
    }
}
