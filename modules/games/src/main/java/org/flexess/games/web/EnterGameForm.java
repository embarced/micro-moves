package org.flexess.games.web;

import javax.validation.constraints.Positive;

public class EnterGameForm {

    @Positive
    public long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
