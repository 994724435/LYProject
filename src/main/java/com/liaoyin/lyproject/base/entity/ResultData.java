package com.liaoyin.lyproject.base.entity;

/**
 * 作者：
 * 时间：2018/8/9 15:40
 * 描述：
 */
public class ResultData {
    private long count;//总条数
    private int pages;//总页
    private int pageNum;//当前页
    private int pageSize;//显示条数
    private Object data;//数据包


    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
