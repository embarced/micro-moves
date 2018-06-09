"use strict";

/**
 * Colours of the two opponents as enumeration.
 *
 * @enum {string}
 */
const Colour = {
    WHITE: 'w',
    BLACK: 'b',

    /**
     * Returns the other colour.
     *
     * @param {Colour} colour
     * @returns {Colour}
     * @static
     */
    other: function (colour) {
        if (colour === Colour.WHITE) {
            return Colour.BLACK;
        } else {
            return Colour.WHITE;
        }
    }
};

/**
 * Piece types in chess, of which there are six.
 *
 * @enum {string}
 */
const PieceType = {
    KING:   'k',
    QUEEN:  'q',
    ROOK:   'r',
    BISHOP: 'b',
    KNIGHT: 'n',
    PAWN:   'p',

    fromChar: function (c) {
        let result = undefined;
        const cLower = c.toLowerCase();
        if ('kqrbnp'.indexOf(cLower) >= 0) {
            result = cLower;
        }
        return result;
    }
};

/**
 * Castlings still available in a game as an enumeration.
 */
const CastlingType = {
    WHITE_KINGSIDE: 'K',
    WHITE_QUEENSIDE: 'Q',
    BLACK_KINGSIDE: 'k',
    BLACK_QUEENSIDE: 'q'
};

/**
 * A chessman, a single piece in the game of chess.
 */
class Piece {

    /**
     * Createa a piece.
     *
     * @param {Colour} colour the colour, e.g. white
     * @param {PieceType} type the type, e.g. pawn
     */
    constructor (colour, type) {
        this.colour = colour;
        this.type = type;
    }

    /**
     * Create a piece from a single char
     *
     * @param c char, uppercase white piece, lowercase black, 'p' is black pawn.
     * @returns {Piece} resulting piece.
     */
    static fromChar(c) {
        const type = PieceType.fromChar(c);
        const colour = (c === c.toUpperCase()) ? Colour.WHITE : Colour.BLACK;
        return new Piece(colour, type);
    }

    /**
     * Returns a string representation of the piece.
     * Uppercase is white, lowercase black. E.g. 'Q' for a white queen.
     *
     * @returns {string}
     */
    toString() {
        return this.colour === Colour.WHITE ? this.type.toUpperCase() : this.type;
    }
}

/*  The squares of the board are represented by numbers 0 - 63. Here are some helper functions.
 */
const Square = {

    // Constants for the squares, for easy use e.g. in unit Tests
    a8: 0,
    b8: 1,
    c8: 2,
    d8: 3,
    e8: 4,
    f8: 5,
    g8: 6,
    h8: 7,
    a7: 8,
    b7: 9,
    c7: 10,
    d7: 11,
    e7: 12,
    f7: 13,
    g7: 14,
    h7: 15,
    a6: 16,
    b6: 17,
    c6: 18,
    d6: 19,
    e6: 20,
    f6: 21,
    g6: 22,
    h6: 23,
    a5: 24,
    b5: 25,
    c5: 26,
    d5: 27,
    e5: 28,
    f5: 29,
    g5: 30,
    h5: 31,
    a4: 32,
    b4: 33,
    c4: 34,
    d4: 35,
    e4: 36,
    f4: 37,
    g4: 38,
    h4: 39,
    a3: 40,
    b3: 41,
    c3: 42,
    d3: 43,
    e3: 44,
    f3: 45,
    g3: 46,
    h3: 47,
    a2: 48,
    b2: 49,
    c2: 50,
    d2: 51,
    e2: 52,
    f2: 53,
    g2: 54,
    h2: 55,
    a1: 56,
    b1: 57,
    c1: 58,
    d1: 59,
    e1: 60,
    f1: 61,
    g1: 62,
    h1: 63,


    /**
     * Converts a square from a string, e.g. "e4" to a number.
     *
     * @param {string} name name of the square
     * @returns {number} Number of the field, or undefined if no valid name
     */
    nameToNumber: function (name) {
        let nr = undefined;

        if (typeof name === "string" && name.match(/[a-h][1-8]/)) {
            const file = name.charAt(0);
            const rank = name.charAt(1);
            nr = "abcdefgh".indexOf(file) + (8 - rank) * 8;
        }
        return nr;
    },


    /**
     * Converts from a number the name of the square.
     *
     * @param {number} nr number of the square
     * @returns {string}
     */
    numberToName: function (nr) {
        const column = nr % 8;
        const row    = (nr - column) / 8;
        return "abcdefgh".charAt(column) + (8 - row);
    },


    /**
     * Calculates the square number from coordinates (row and column).
     *
     * @param {number} row Row, 0-7
     * @param {number} column Column, 0-7
     * @return {number} the field number (0-63), or undefined, if the coordinates are out of the permitted range
     */
    fromCoordinates: function (row, column) {
        let nr = undefined;
        if (row >= 0 && row <= 7 && column >= 0 && column <= 7) {
            nr = row * 8 + column;
        }
        return nr;
    },


    /**
     * Determines a new square from a start square and a direction vector (dx, dy).
     *
     * @param {number} start start square
     * @param {number} dx direction x
     * @param {number} dy  direction y
     * @returns {number} Number of square, or undefined if outside the board
     */
    fromDirection: function (start, dx, dy) {
        let square = undefined;

        let col = start % 8;
        let row = ((start - col)) / 8;
        col += dx;
        row += dy;

        if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
            square = row * 8 + col;
        }
        return square;
    },

    /**
     * Returns the column number to a square.
     *
     * @param {number} squareNumber
     * @returns {number}
     */
    column: function (squareNumber) {
        return squareNumber % 8;
    },

    /**
     * Returns the row number to a square.
     *
     * @param {number} squareNumber
     * @returns {number}
     */
    row: function (squareNumber) {
        return ((squareNumber - (squareNumber % 8))) / 8;
    }
};

/**
 * A Movement of a chess piece.
 */
class Move {

    constructor (a, b, c) {
        if (arguments.length === 3) {
            this.from = a;
            this.to = b;
            this.promotion = c;
        } else if (arguments.length === 2) {
            this.from = a;
            this.to = b;
        } else if (arguments.length === 1 && typeof a === "string") {
            if (a.match(/[a-h][1-8][a-h][1-8][qbnrQBNR]?/)) {
                this.from = Square.nameToNumber(a.substr(0, 2));
                this.to = Square.nameToNumber(a.substr(2, 2));
                if (a.length === 5) {
                    this.promotion = PieceType.fromChar(a.substr(4));
                }
            }
        }
    }

    /**
     * Returns the move as a string, e.g. "e2e4"
     *
     * @returns {String}
     */
    toString () {
        const sfrom = Square.numberToName(this.from);
        const sto = Square.numberToName(this.to);
        const spromo = (this.promotion === undefined) ? '' : this.promotion;

        return sfrom + sto + spromo;
    }
}

/**
 * A position represents the game situation.
 */
class Position {

    /**
     * @param s other position (will be copied), a string in FEN, or nothing
     */
    constructor (s) {
        this.board = new Array(64);

        if (arguments.length === 0) {
            const startingPosition = [ "rnbqkbnr", "pppppppp", "", "", "", "", "PPPPPPPP", "RNBQKBNR" ];
            this.activeColour = Colour.WHITE;
            this.castlingRights = "KQkq";
            this.halfmoveClock = 0;
            this.fullmoveCounter = 1;

            for (let row = 0; row < startingPosition.length; row += 1) {
                for (let i = 0; i < startingPosition[row].length; i += 1) {
                    this.board[i + 8 * row] = Piece.fromChar(startingPosition[row].charAt(i));
                }
            }
        } else if (typeof s === "object") {
            this.activeColour = s.activeColour;
            this.castlingRights = s.castlingRights;
            for (let i = 0; i < 64; i += 1) {
                this.board[i] = s.board[i];
            }
            this.enPassant = s.enPassant;
            this.halfmoveClock = s.halfmoveClock;
            this.fullmoveCounter = s.fullmoveCounter;
        } else if (typeof s === "string") {

            const groups = s.split(" ");
            const rows = groups[0].split("/");
            const colour = groups[1];
            const castling = groups[2];
            const passant = groups[3];
            const halfmove = groups[4];
            const fullmove = groups[5];

            this.activeColour = colour === 'w' ? Colour.WHITE : Colour.BLACK;
            this.castlingRights = castling;
            this.halfmoveClock = parseInt(halfmove);
            this.fullmoveCounter = parseInt(fullmove);

            this.enPassant = Square.nameToNumber(passant);

            for (let rowNr = 0; rowNr < 8; rowNr += 1) {
                const row = rows[rowNr];
                let colNr = 0;

                for (let i = 0; i < row.length; i += 1) {
                    const c = row[i];
                    if (c.match(/[1-8]/)) {
                        for (let j=0; j < c; j += 1) {

                            const square = Square.fromCoordinates(rowNr,colNr);
                            this.board[square] = undefined;
                            colNr++;
                        }
                    } else {
                        const square = Square.fromCoordinates(rowNr,colNr);
                        this.board[square] = Piece.fromChar(c);
                        colNr++;
                    }
                }
            }
        }
    }

    /**
     * Returns the piece standing on the square. Or undefined if the square is empty.
     *
     * @param square, number 0..63
     * @returns {Piece} the piece on the square or undefined
     */
    onSquare(square) {
        return this.board[square];
    }


    /**
     * Performs the move and returns the new position.
     * The position itself remains unchanged.
     *
     * @param move
     * @returns {Position}
     */
    performMove(move) {
        const newPos = new Position(this);
        const piece = newPos.board[move.from];

        newPos.activeColour = Colour.other(this.activeColour);
        if (newPos.activeColour === Colour.WHITE) {
            newPos.fullmoveCounter += 1;
        }

        newPos.halfmoveClock += 1;
        if (newPos.board[move.to] !== undefined) {
            newPos.halfmoveClock = 0;
        } else if (piece.type === PieceType.PAWN) {
            newPos.halfmoveClock = 0;
        }

        newPos.board[move.to] = piece;
        newPos.board[move.from] = undefined;

        newPos.enPassant = undefined;
        if (piece.type === PieceType.PAWN) {

            // set en passant square
            if (Math.abs(Square.row(move.from) - Square.row(move.to)) === 2) {
                const dy = (this.activeColour === Colour.WHITE) ? -1 : 1;
                newPos.enPassant = Square.fromCoordinates(Square.row(move.from) + dy, Square.column(move.from));
            }

            // enPassant capture
            if (Math.abs(Square.column(move.from) - Square.column(move.to)) === 1) {
                if (move.to === this.enPassant) {
                    const dy = (this.activeColour === Colour.WHITE) ? 1 : -1;
                    newPos.board[Square.fromDirection(this.enPassant, 0, dy)] = undefined;
                }
            }
        }

        // Castling
        if (this.castlingRights !== '-' ) {

            if (piece.type === PieceType.KING) {

                if (Math.abs(Square.column(move.from) - Square.column(move.to)) === 2) {
                    // perform castling
                    if (this.activeColour === Colour.WHITE) {
                        if(move.to === Square.g1) {
                            newPos.board[Square.f1] = newPos.board[Square.h1];
                            newPos.board[Square.h1] = undefined;
                        } else {
                            newPos.board[Square.d1] = newPos.board[Square.a1];
                            newPos.board[Square.a1] = undefined;
                        }
                        newPos.castlingRights =  newPos.castlingRights.replace('K', '');
                        newPos.castlingRights =  newPos.castlingRights.replace('Q', '');
                    } else {
                        if(move.to === Square.g8) {
                            newPos.board[Square.f8] = newPos.board[Square.h8];
                            newPos.board[Square.h8] = undefined;
                        } else {
                            newPos.board[Square.d8] = newPos.board[Square.a8];
                            newPos.board[Square.a8] = undefined;
                        }
                        newPos.castlingRights =  newPos.castlingRights.replace('k', '');
                        newPos.castlingRights =  newPos.castlingRights.replace('q', '');
                    }
                } else {
                    if (this.activeColour === Colour.WHITE) {
                        newPos.castlingRights =  newPos.castlingRights.replace('K', '');
                        newPos.castlingRights =  newPos.castlingRights.replace('Q', '');
                    } else {
                        newPos.castlingRights =  newPos.castlingRights.replace('k', '');
                        newPos.castlingRights =  newPos.castlingRights.replace('q', '');
                    }
                }
            } else if (piece.type === PieceType.ROOK) {
                if (this.activeColour === Colour.WHITE) {
                    if (move.from === Square.h1) {
                        newPos.castlingRights =  newPos.castlingRights.replace('K', '');
                    } else  if (move.from === Square.h8){
                        newPos.castlingRights =  newPos.castlingRights.replace('Q', '');
                    }
                } else {
                    if (move.from === Square.a1) {
                        newPos.castlingRights =  newPos.castlingRights.replace('k', '');
                    } else  if (move.from === Square.a8){
                        newPos.castlingRights =  newPos.castlingRights.replace('q', '');
                    }

                }
            }
            if (newPos.castlingRights === '') {
                newPos.castlingRights = '-';
            }
        }

        return newPos;
    }

    /**
     * Check if the square is empty, so there is no piece.
     *
     * @param square
     * @returns {boolean}
     */
    isEmpty(square) {
        return this.board[square] === undefined;
    }

    /**
     * Returns the square on which the king of the given colour is.
     *
     * @param {Colour} colour
     * @returns square (number 0..63), or undefines)
     */
    findKing(colour) {
        for (let square = 0; square < 64; square += 1) {
            const piece = this.board[square];
            if (piece !== undefined && piece.type === PieceType.KING && piece.colour === colour) {
                return square;
            }
        }
        return undefined;
    }

    /**
     * Returns the position as a string in FEN notation.
     *
     * @returns {string}
     */
    toString() {
        let result = '';
        let empty = 0;

        for (let row = 0; row < 8; row += 1) {
            for (let col = 0; col < 8; col += 1) {
                const square = Square.fromCoordinates(row,col);
                const piece = this.board[square];
                if (piece === undefined) {
                    empty += 1;
                } else {
                    if (empty > 0) {
                        result += empty;
                        empty = 0;
                    }
                    result += piece.toString();
                }
            }
            if (empty > 0) {
                result += empty;
                empty = 0;
            }
            if (row < 7) {
                result += '/'
            }
        }

        result += ' ';
        result += this.activeColour;
        result += ' ';
        result += this.castlingRights;
        result += ' ';
        result += this.enPassant === undefined ? '-' : Square.numberToName(this.enPassant);
        result += ' ';
        result += this.halfmoveClock;
        result += ' ';
        result += this.fullmoveCounter;

        return result;
    }
}

if (typeof module !== 'undefined' && module.exports) {
    exports.Colour = Colour;
    exports.PieceType = PieceType;
    exports.Piece = Piece;
    exports.Square = Square;
    exports.Move = Move;
    exports.Position = Position;
}