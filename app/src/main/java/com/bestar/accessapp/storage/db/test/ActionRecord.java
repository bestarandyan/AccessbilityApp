package com.bestar.accessapp.storage.db.test;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

/**
 * Created by ted on 2018/4/20.
 * in com.bestar.accessapp.storage.db
 */
@Entity(indexes = {
        @Index(value = "date DESC", unique = true)
})
public class ActionRecord {
    @Id
    private Long id;

    @NotNull
    private String record;
    private String comment;
    private Integer num;
    private Date date;
@Generated(hash = 953913859)
public ActionRecord(Long id, @NotNull String record, String comment,
        Integer num, Date date) {
    this.id = id;
    this.record = record;
    this.comment = comment;
    this.num = num;
    this.date = date;
}
@Generated(hash = 1411865167)
public ActionRecord() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getRecord() {
    return this.record;
}
public void setRecord(String record) {
    this.record = record;
}
public String getComment() {
    return this.comment;
}
public void setComment(String comment) {
    this.comment = comment;
}
public Integer getNum() {
    return this.num;
}
public void setNum(Integer num) {
    this.num = num;
}
public Date getDate() {
    return this.date;
}
public void setDate(Date date) {
    this.date = date;
}

}
