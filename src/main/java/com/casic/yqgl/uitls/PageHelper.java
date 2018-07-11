package com.casic.yqgl.uitls;

import java.util.ArrayList;
import java.util.List;

public class PageHelper<T> {
    private List<T> rows = new ArrayList<>();

    private int total;

    public PageHelper() {
        super();
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
