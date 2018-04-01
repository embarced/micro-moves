package org.flexess.games.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

/**
 * Data transfer object (PofEAA, page 401) for move data.
 *
 * @author stefanz
 */
@JsonPropertyOrder({"moveId", "number", "text", "created"})
public class MoveDto {

    private Long moveId;

    private String text;

    private Long number;

    private Date created;

    public Long getMoveId() {
        return moveId;
    }

    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // see https://www.firstfewlines.com/post/spring-boot-json-format-date-using-jsonserialize-and-jsonformat/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
