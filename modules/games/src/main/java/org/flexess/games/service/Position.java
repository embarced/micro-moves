package org.flexess.games.service;

/**
 * Represents the game situation. Pieces on the board, active colour etc.
 *
 * @author stefanz
 */
public class Position {

    private char board[][];
    private char activeColour;
    private String castlingAvailability;
    private String enPassantTargetSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    /**
     * Default constructor.
     * Creates initial position according to the chess rules.
     */
    public Position() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    /**
     * Creates the position according to a String in FEN (Forsyth-Edwards-Notation).
     * See https://en.wikipedia.org/wiki/Forsyth–Edwards_Notation
     *
     * @param fen String in Forsyth-Edwards-Notation
     */
    public Position(String fen) {
        String[] fields = fen.split(" ");

        this.board = new char[8][8];
        String[] rows = fields[0].split("/");

        int rowNo = 0;
        int lineNo = 0;

        for (String row : rows) {
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    int count = Integer.parseInt(c + "");
                    for (int i = 0; i < count; ++i) {
                        board[rowNo][lineNo] = ' ';
                        lineNo++;
                    }
                } else {
                    board[rowNo][lineNo] = c;
                    lineNo++;
                }
            }
            lineNo = 0;
            rowNo++;
        }

        this.activeColour = fields[1].charAt(0);
        this.castlingAvailability = fields[2];
        this.enPassantTargetSquare = fields[3];
        this.halfmoveClock = Integer.parseInt(fields[4]);
        this.fullmoveNumber = Integer.parseInt(fields[5]);
    }

    /**
     * Active colour. w means White moves next, b means Black.
     *
     * @return w or b
     */
    public char getActiveColour() {
        return this.activeColour;
    }

    /**
     * Set active colour.
     *
     * @param activeColour
     */
    public void setActiveColour(char activeColour) {
        this.activeColour = activeColour;
    }

    /**
     * Castling availability.
     * If neither side can castle, this is -.
     * Otherwise, this has one or more letters: K (White can castle kingside),
     * Q (White can castle queenside), k (Black can castle kingside),
     * and/or q (Black can castle queenside).
     *
     * @return castling availability
     */
    public String getCastlingAvailability() {
        return this.castlingAvailability;
    }

    /**
     * En passant target square in algebraic notation. If there's no en passant target square, this is -.
     * If a pawn has just made a two-square move, this is the position "behind" the pawn.
     * This is recorded regardless of whether there is a pawn in position to make an en passant capture.
     *
     * @return en Passant target square
     */
    public String getEnPassantTargetSquare() {
        return enPassantTargetSquare;
    }

    /**
     * This is the number of halfmoves since the last capture or pawn advance.
     * This is used to determine if a draw can be claimed under the fifty-move rule.
     *
     * @return halfmove clock
     */
    public int getHalfmoveClock() {
        return this.halfmoveClock;
    }

    /**
     * The number of the full move.
     * It starts at 1, and is incremented after Black's move.
     *
     * @return fullmove number
     */
    public int getFullmoveNumber() {
        return this.fullmoveNumber;
    }

    /**
     * Set number of the full move.
     *
     * @param fullmoveNumber
     */
    public void setFullmoveNumber(int fullmoveNumber) {
        this.fullmoveNumber = fullmoveNumber;
    }

    /**
     * Returns the piece at a given square.
     *
     * @param square name of square on board, e.g. "e4"
     * @return piece at this square, or ' ' if empty.
     */
    public char getPiece(String square) {
        int squareNo = Square.toNumber(square);
        return board[squareNo/8][squareNo%8];
    }

    /**
     * Set the piece at a sqaure.
     *
     * @param square name of square on board, e.g. "g1"
     * @param piece piece, e.g. 'Q' for white queen.
     */
    public void setPiece(String square, char piece) {
        int squareNo = Square.toNumber(square);
        board[squareNo/8][squareNo%8] = piece;
    }

    /**
     * Returns the position in in FEN (Forsyth-Edwards-Notation).
     *
     * @return Position in FEN
     */
    @Override
    public String toString() {
        return boardAsFenString() +
                ' ' +
                activeColour +
                ' ' +
                castlingAvailability +
                ' ' +
                enPassantTargetSquare +
                ' ' +
                halfmoveClock +
                ' ' +
                fullmoveNumber;
    }

    private String boardAsFenString() {
        StringBuilder result = new StringBuilder();

        int count = 0;
        for (int row = 0; row < 8; ++row) {
            for (int line = 0; line < 8; ++line) {
                char c = board[row][line];

                if (c == ' ') {
                    count++;
                } else {
                    if (count > 0) {
                        result.append(count);
                        count = 0;
                    }
                    result.append(c);
                }
            }
            if (count > 0) {
                result.append(count);
                count = 0;
            }
            if (row <= 6) {
                result.append('/');
            }
        }

        return result.toString();
    }


}
