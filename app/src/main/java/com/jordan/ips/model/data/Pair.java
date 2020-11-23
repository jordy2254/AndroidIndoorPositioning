package com.jordan.ips.model.data;

public class Pair <T, R> {
    public T fst;
    public R snd;

    public Pair(T fst, R snd){
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public String toString() {
        return fst.toString() + " -> " + snd.toString();
    }

}
