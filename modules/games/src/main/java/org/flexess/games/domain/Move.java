package org.flexess.games.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Persistent move within a game.
 *
 * @author stefanz
 */
@Entity
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private Long number;

    @ManyToOne
    private Game game;

    private Date created;

    protected Move() {
        this.created = new Date();
    }

    /**
     * Create a move with a string representation.
     *
     * @param text move as a string, e.g. "e2e4"
     */
    public Move(String text) {
        this();
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("Given move is null.");
        }
        Pattern pattern = Pattern.compile("[a-h][1-8][a-h][1-8][qkbr]?");
        if (pattern.matcher(text).matches()) {
        this.text = text;} else {
            throw new IllegalArgumentException(text + " is not a valid move. Use 'e2e4' format.");
        }
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

    /**
     * Source square of the move.
     *
     * @return name of source square, e.g. "e2"
     */
    public String getFrom() {
        return text.substring(0, 2);
    }

    /**
     * Target square of the move.
     *
     * @return name of target square, e.g. "e4"
     */
    public String getTo() {
        return text.substring(2, 4);
    }

    public String getPromotion() {
        if (text.length() == 5) {
            return text.substring(4, 5);
        } else return "";
    }

    @Override
    public String toString() {
        return "Move #" + id + ", " + text;
    }
}
