package com.touchrom.fanjianzhi.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2016/6/2.
 */
public class TabEntity extends BaseEntity implements Parcelable {
    int id;
    String title;

    public TabEntity(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
    }

    public TabEntity() {
    }

    protected TabEntity(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<TabEntity> CREATOR = new Parcelable.Creator<TabEntity>() {
        @Override
        public TabEntity createFromParcel(Parcel source) {
            return new TabEntity(source);
        }

        @Override
        public TabEntity[] newArray(int size) {
            return new TabEntity[size];
        }
    };
}
