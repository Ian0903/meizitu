package com.example.ian.meizitu.data;

import com.example.ian.meizitu.data.entity.Gank;

import java.util.List;

/**
 * Created by Ian on 2018/5/19.
 */

public class Categorydata extends Basedata {

    private List<Gank> results;

    public List<Gank> getResults() {
        return results;
    }

    public void setResults(List<Gank> results) {
        this.results = results;
    }
}
