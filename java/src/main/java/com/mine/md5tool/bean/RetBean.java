package com.mine.md5tool.bean;

/**
 * @author Teng
 * @create 2019-12-12
 */
public class RetBean<T> {
    public int num = -1;
    public T obj = null;
    public String tips = "";

    public RetBean() {
    }

    public RetBean(int num , T obj , String tips) {
        this.num = num;
        this.obj = obj;
        this.tips = tips;
    }

    public RetBean(int num , String tips) {
        this.num = num;
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "RetBean{" +
                "num=" + num +
                ", obj=" + obj +
                ", tips='" + tips + '\'' +
                '}';
    }
}
