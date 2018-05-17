package com.bestar.accessapp.storage.db.module;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ted on 2018/4/23.
 * in com.bestar.accessapp.storage.db
 */
@Entity(indexes = {
        @Index(value = "date DESC", unique = true)
})
public class RentHouseRecord {
    @Id
    private Long id;

    @NotNull
    private String title;//标题
    private String rental;//租金
    private String type;//户型
    private String area;//面积
    private String phone;//电话号码 如果是转接号，则为空
    private Date date;//爬取时间
    @NotNull
    private Long dayTime;//爬取时间当天的日期
    private String cityName;//城市名称
@Generated(hash = 180588057)
public RentHouseRecord(Long id, @NotNull String title, String rental,
        String type, String area, String phone, Date date,
        @NotNull Long dayTime, String cityName) {
    this.id = id;
    this.title = title;
    this.rental = rental;
    this.type = type;
    this.area = area;
    this.phone = phone;
    this.date = date;
    this.dayTime = dayTime;
    this.cityName = cityName;
}
@Generated(hash = 1390932868)
public RentHouseRecord() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getTitle() {
    return this.title;
}
public void setTitle(String title) {
    this.title = title;
}
public String getRental() {
    return this.rental;
}
public void setRental(String rental) {
    this.rental = rental;
}
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public String getArea() {
    return this.area;
}
public void setArea(String area) {
    this.area = area;
}
public String getPhone() {
    return this.phone;
}
public void setPhone(String phone) {
    this.phone = phone;
}
public Date getDate() {
    return this.date;
}
public void setDate(Date date) {
    this.date = date;
}
public Long getDayTime() {
    return this.dayTime;
}
public void setDayTime(Long dayTime) {
    this.dayTime = dayTime;
}
public String getCityName() {
    return this.cityName;
}
public void setCityName(String cityName) {
    this.cityName = cityName;
}
}
