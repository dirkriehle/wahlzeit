package org.wahlzeit_revisited.dto;

/**
 * ErrorDto maps an internal error for the outer world
 */
public class ErrorDto {

    private final String msg;

    public ErrorDto(String msg) {
        this.msg = msg;
    }

    /**
     * @methodtype getter
     */
    public String getMsg() {
        return msg;
    }
}
