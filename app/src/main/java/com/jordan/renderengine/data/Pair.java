package com.jordan.renderengine.data;

import java.io.Serializable;

public class Pair <T, R> implements Serializable {
    public T fst;
    public R snd;

    public Pair() {
    }

    public Pair(T fst, R snd){
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public String toString() {
        return fst.toString() + " -> " + snd.toString();
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public void setSnd(R snd) {
        this.snd = snd;
    }
}
