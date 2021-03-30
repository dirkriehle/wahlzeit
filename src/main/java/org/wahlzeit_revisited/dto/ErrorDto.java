package org.wahlzeit_revisited.dto;

public class ErrorDto {

    private String msg;

    public ErrorDto(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
