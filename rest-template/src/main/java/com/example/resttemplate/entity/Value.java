package com.example.resttemplate.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 它带有@JsonIgnoreProperties来自 Jackson JSON 处理库的注释，以指示应忽略未绑定到此类型的任何属性
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    private Long id;
    private String quote;

    public Value() {
    }

    public Long getId() {
        return this.id;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
