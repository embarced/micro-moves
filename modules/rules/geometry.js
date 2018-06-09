"use strict";

const domain = require("./domain.js");

const Square = domain.Square;

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
}