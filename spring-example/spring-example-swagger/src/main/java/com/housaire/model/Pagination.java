package com.housaire.model;

/**
 * Created by liuli on 2018/5/26
 */
public class Pagination<E>
{
    // 当前页数
    private int pageNum = 1;
    // 每页显示记录的条数
    private int pageSize = 10;

    private String keywords;

    private E data;

    public Pagination() {
    }

    public Pagination(int pageNum, int pageSize, E data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
