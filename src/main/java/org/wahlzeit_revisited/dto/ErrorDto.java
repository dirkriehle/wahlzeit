package org.wahlzeit_revisited.dto;

/**
 * ErrorDto maps an internal for the outer world
 */
public class ErrorDto {

    private String msg;

    public ErrorDto(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
