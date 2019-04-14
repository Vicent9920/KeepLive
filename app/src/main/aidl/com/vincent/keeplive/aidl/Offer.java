package com.vincent.keeplive.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable  {

    public int salary;
    public String company;

    public Offer(int salary, String company) {
        this.salary = salary;
        this.company = company;
    }

    public Offer() {
    }

    protected Offer(Parcel in) {
        salary = in.readInt();
        company = in.readString();
    }

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(salary);
        dest.writeString(company);
    }


    @Override
    public String toString() {
        return  String.format("{salary:%s, company:%s}", salary, company);
    }
}
