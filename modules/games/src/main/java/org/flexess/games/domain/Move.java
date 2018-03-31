package org.flexess.games.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.regex.Pattern;

@Entity
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    private Long number;

    @ManyToOne
    private Game game;

    private Date created;

    public Move() {
        this.created = new Date();
    }

    public Move(String text) {
        super();
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public Long getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Date getCreated() {
        return created;
    }


    @Override
    public String toString() {
        return "Move #" + id + ", " + text;
    }


}
