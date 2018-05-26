package com.jxyq.model.watch;

public class P3Lbs {
    int mcc;
    int mnc;
    int lac1;
    int cell1;
    int signal1;
    int lac2;
    int cell2;
    int signal2;
    int lac3;
    int cell3;
    int signal3;

    public P3Lbs(int mcc, int mnc, int lac1, int cell1, int signal1, int lac2, int cell2, int signal2, int lac3, int cell3, int signal3) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac1 = lac1;
        this.cell1 = cell1;
        this.signal1 = signal1;
        this.lac2 = lac2;
        this.cell2 = cell2;
        this.signal2 = signal2;
        this.lac3 = lac3;
        this.cell3 = cell3;
        this.signal3 = signal3;
    }

    public int getMcc() {
        return this.mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return this.mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getLac1() {
        return this.lac1;
    }

    public void setLac1(int lac1) {
        this.lac1 = lac1;
    }

    public int getCell1() {
        return this.cell1;
    }

    public void setCell1(int cell1) {
        this.cell1 = cell1;
    }

    public int getSignal1() {
        return this.signal1;
    }

    public void setSignal1(int signal1) {
        this.signal1 = signal1;
    }

    public int getLac2() {
        return this.lac2;
    }

    public void setLac2(int lac2) {
        this.lac2 = lac2;
    }

    public int getCell2() {
        return this.cell2;
    }

    public void setCell2(int cell2) {
        this.cell2 = cell2;
    }

    public int getSignal2() {
        return this.signal2;
    }

    public void setSignal2(int signal2) {
        this.signal2 = signal2;
    }

    public int getLac3() {
        return this.lac3;
    }

    public void setLac3(int lac3) {
        this.lac3 = lac3;
    }

    public int getCell3() {
        return this.cell3;
    }

    public void setCell3(int cell3) {
        this.cell3 = cell3;
    }

    public int getSignal3() {
        return this.signal3;
    }

    public void setSignal3(int signal3) {
        this.signal3 = signal3;
    }
}
