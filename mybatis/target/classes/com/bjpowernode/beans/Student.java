package com.bjpowernode.beans;

public class Student {
    //以下为成员变量，不是属性,属性是getXxx()方法去掉get，Xxx首字母小写
    private Integer id;
    private String name;
    private Integer age;
    private Double score;

    public Student() {
        super();
    }

    public Student(String name, Integer age, Double score) {
        super();
        this.name = name;
        this.age = age;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", score=" + score +
                '}';
    }
}
