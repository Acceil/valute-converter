package org.itstep.msk.app.entity;

public class Demo {
    private Integer id = 123;
    private String text = "Demo text!";

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
