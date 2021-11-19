package com.example.resttemplate.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 它带有@JsonIgnoreProperties来自 Jackson JSON 处理库的注释，以指示应忽略未绑定到此类型的任何属性
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

    private String type;
    private Value value;

    public Quote() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}