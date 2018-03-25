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
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String fromField;

    private String toField;

    private String promotion;

    private Long number;

    @ManyToOne
    private Game game;

    private Date created;


    public Move() {
    }

    public Move(String move) {
        if (move == null || move.length() == 0) {
            throw new IllegalArgumentException("Given move is null.");
        }
        Pattern pattern = Pattern.compile("[a-h][1-8][a-h][1-8][qknr]?");
        if (pattern.matcher(move).matches()) {
            this.fromField = move.substring(0, 2);
            this.toField = move.substring(2, 4);
            if (move.length() > 4) {
                this.promotion = move.substring(4, 5);
            }
        } else {
            throw new IllegalArgumentException(move + " is not a valid move. Use 'e2e4' format.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getFromField() {
        return fromField;
    }

    public void setFromField(String fromField) {
        this.fromField = fromField;
    }

    public String getToField() {
        return toField;
    }

    public void setToField(String toField) {
        this.toField = toField;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    public Date getCreated() {
        return created;
    }

    public String getAsString() {
        String result = fromField + toField;
        if (promotion != null) {
            result += promotion;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Move "+fromField+"-"+toField;
    }
}
