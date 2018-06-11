"use strict";

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
 * Includes (pre-)calculations for the chess board, e.g. squares reachable from a source square (independent
 * of position of other pieces) for different piece types.
 */
const BoardGeometry = {

    /**
     * Returns the list of all squares that can be reached by a knight from a source square.
     *
     * @param fromSquare source square (as number, 0..63)
     * @returns {*} list of reachable squares
     */
    squaresReachableByKnight: function (fromSquare) {
        return BoardGeometry.knightReachableSquares[fromSquare];
    },

    /**
     * Returns the list of all squares that can be reached by a king from a source square.
     *
     * @param fromSquare source square (as number, 0..63)
     * @returns {*} list of reachable squares
     */
    squaresReachableByKing: function (fromSquare) {
        return BoardGeometry.kingReachableSquares[fromSquare];
    },

    /**
     * Returns the list of straight lines (or rays) starting from a source square.
     * Each line contains a list of squares. The source sqaure is not included.
     *
     * @param fromSquare source square (as number, 0..63)
     * @returns {*} list of straight lines
     */
    straightRaysFromSquare: function (fromSquare) {
        return BoardGeometry.straightRays[fromSquare];
    },

    /**
     * Returns the list of diagonal lines (or rays) starting from a source square.
     * Each line contains a list of squares. The source sqaure is not included.
     *
     * @param fromSquare fromSquare source square (as number, 0..63)
     * @returns {*} list of diagonal lines
     */
    diagonalRaysFromSquare: function (fromSquare) {
        return BoardGeometry.diagonalRays[fromSquare];
    },

    /**
     * Returns the list of diagonal and straight lines (or rays) starting from a source square.
     * Each line contains a list of squares. The source sqaure is not included.
     *
     * @param fromSquare fromSquare source square (as number, 0..63)
     * @returns {*} list of diagonal and straight lines
     */
    allRaysFromSquare: function (fromSquare) {
        return BoardGeometry.allRays[fromSquare];
    },

    // containers for precalculated values
    //
    knightReachableSquares: new Array(64),
    straightRays: new Array(64),
    diagonalRays: new Array(64),
    allRays: new Array(64),
    kingReachableSquares: new Array(64),

    /**
     * Precalculation for all 64 squares.
     * Only necessary to call it once.
     */
    precalculate: function () {
        for (let sqNo = 0; sqNo < 64; sqNo += 1) {
            BoardGeometry.knightReachableSquares[sqNo] = Calculation.squaresReachableByKnight(sqNo);
            BoardGeometry.straightRays[sqNo] = Calculation.straightRaysFromSquare(sqNo);
            BoardGeometry.diagonalRays[sqNo] = Calculation.diagonalRaysFromSquare(sqNo);
            BoardGeometry.allRays[sqNo] = Calculation.straightAndDiogonalRaysFromSquare(sqNo);
            BoardGeometry.kingReachableSquares[sqNo] = Calculation.squaresReachableByKing(sqNo);
        }
    }
};

/**
 *  Functions for board geometry calculations.
 */
const Calculation = {

    /**
     * Calculates the squares an a board reachable by a knight on a given square.
     *
     * @param from source square (as number, 0..63)
     * @returns {Array} array with squares
     */
    squaresReachableByKnight: function (from) {
        let squares = [];
        for (let dx = 2; dx >= -2; dx -= 1) {
            for (let dy = -2; dy <= 2; dy += 1) {
                if (Math.abs(dx) + Math.abs(dy) === 3) {
                    const targetSquare = Square.fromDirection(from, dx, dy);
                    if (targetSquare !== undefined) {
                        squares.push(targetSquare);
                    }
                }
            }
        }
        return squares;
    },

    straightRaysFromSquare: function (fromSquare) {
        const rays = [];

        for (let dx = -1; dx <= 1; dx += 1) {
            for (let dy = -1; dy <= 1; dy += 1) {
                if (Math.abs(dx) + Math.abs(dy) === 1) {
                    const ray = [];
                    let targetSquare = fromSquare;
                    do {
                        targetSquare = Square.fromDirection(targetSquare, dx, dy);
                        if (targetSquare !== undefined) {
                            ray.push(targetSquare);
                        }
                    } while (targetSquare !== undefined);
                    if (ray.length > 0) {
                        rays.push(ray);
                    }
                }
            }
        }
        return rays;
    },

    diagonalRaysFromSquare: function (fromSquare) {
        const rays = [];

        for (let dx = -1; dx <= 1; dx += 1) {
            for (let dy = -1; dy <= 1; dy += 1) {
                if (Math.abs(dx) + Math.abs(dy) === 2) {
                    const ray = [];
                    let targetSquare = fromSquare;
                    do {
                        targetSquare = Square.fromDirection(targetSquare, dx, dy);
                        if (targetSquare !== undefined) {
                            ray.push(targetSquare);
                        }
                    } while (targetSquare !== undefined);
                    if (ray.length > 0) {
                        rays.push(ray);
                    }
                }
            }
        }

        return rays;
    },

    straightAndDiogonalRaysFromSquare: function (fromSquare) {
        const rays = [];

        for (let dx = -1; dx <= 1; dx += 1) {
            for (let dy = -1; dy <= 1; dy += 1) {
                if (Math.abs(dx) + Math.abs(dy) > 0) {
                    const ray = [];
                    let targetSquare = fromSquare;
                    do {
                        targetSquare = Square.fromDirection(targetSquare, dx, dy);
                        if (targetSquare !== undefined) {
                            ray.push(targetSquare);
                        }
                    } while (targetSquare !== undefined);
                    if (ray.length > 0) {
                        rays.push(ray);
                    }
                }
            }
        }

        return rays;
    },

    /**
     * Calculates the squares an a board reachable by a king on a given square.
     *
     * @param from source square (as number, 0..63)
     * @returns {Array} array with squares
     */
    squaresReachableByKing: function (from) {
        const squares = [];
        for (let dx = -1; dx <= 1; dx += 1) {
            for (let dy = -1; dy <= 1; dy += 1) {
                if (dx !== 0 || dy !== 0) {
                    const toSquare = Square.fromDirection(from, dx, dy);
                    if (toSquare !== undefined) {
                        squares.push(toSquare);
                    }
                }
            }
        }
        return squares;
    }

};

if (typeof module !== 'undefined' && module.exports) {
    BoardGeometry.precalculate();
    exports.BoardGeometry = BoardGeometry;
    exports.Square = Square;
}