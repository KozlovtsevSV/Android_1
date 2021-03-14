package com.example.lesson_1;

import android.os.Parcel;
import android.os.Parcelable;

public class Counters implements Parcelable {
    private double number;
    private double resultOperation;
    private MainActivity.Operations currentOperation;

    Counters(double number, double resultOperation, MainActivity.Operations currentOperation) {
        this.number = number;
        this.resultOperation = resultOperation;
        this.currentOperation = currentOperation;
    }

    protected Counters(Parcel in) {
        number = in.readDouble();
        resultOperation = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(number);
        dest.writeDouble(resultOperation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Counters> CREATOR = new Creator<Counters>() {
        @Override
        public Counters createFromParcel(Parcel in) {
            return new Counters(in);
        }

        @Override
        public Counters[] newArray(int size) {
            return new Counters[size];
        }
    };

    public double getNumber() {
        return number;
    }

    public double getResultOperation() {
        return resultOperation;
    }

    public MainActivity.Operations getCurrentOperation() {
        return currentOperation;
    }
}
