package com.cd.crm.vo;

import java.util.List;

/**
 * vo类，每个模块进行分页查询之后，返回的结果，最终利用这个结果处理成json数据返回给前端页面
 */

public class PaginationVo<T> {
    //数据库表中的总记录条数
    private int total;
    //查询出来的数据库表中数据的list集合
    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "PaginationVo{" +
                "total=" + total +
                ", dataList=" + dataList +
                '}';
    }
}
