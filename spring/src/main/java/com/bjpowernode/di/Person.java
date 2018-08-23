package com.bjpowernode.di;

public class Person {
    private String pname;
    private int page;

    public Person() {
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPname() {
        return pname;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Person{" +
                "pname='" + pname + '\'' +
                ", page=" + page +
                '}';
    }
}
