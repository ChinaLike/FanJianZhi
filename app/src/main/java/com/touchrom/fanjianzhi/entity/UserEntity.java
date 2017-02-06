package com.touchrom.fanjianzhi.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2016/6/2.
 */
public class UserEntity extends BaseEntity {
    int id;
    String imgUrl;
    String nikeName;
    int money;
    int realMoney;  //真贱币
    int exp;    //经验
    @SerializedName("signState")
    boolean isSigned;  //签到状态
    int msgNum;
    String level;
    int signDay;    //连续签到天数

    public void setSignDay(int signDay) {
        this.signDay = signDay;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public int getSignDay() {
        return signDay;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getRealMoney() {
        return realMoney;
    }

    public int getExp() {
        return exp;
    }

    public int getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getNikeName() {
        return nikeName;
    }

    public int getMoney() {
        return money;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public String getLevel() {
        return level;
    }
}
