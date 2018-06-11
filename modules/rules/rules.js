"use strict";

const domain = require("./domain.js");

const Colour = domain.Colour;
const BLACK = Colour.BLACK;
const WHITE = Colour.WHITE;

const PieceType = domain.PieceType;
const PAWN = PieceType.PAWN;
const KING = PieceType.KING;
const QUEEN = PieceType.QUEEN;
const ROOK = PieceType.ROOK;
const BISHOP = PieceType.BISHOP;
const KNIGHT = PieceType.KNIGHT;

const Piece = domain.Piece;
const Move = domain.Move;

const boardGeometry = require("./geometry.js");
const BoardGeometry = boardGeometry.BoardGeometry;
const Square = boardGeometry.Square;

/**
 * The rules of chess.
 */
const ChessRules = {

    /**
     * Tests for a position whether a square is attacked by a piece of the given colour.
     *
     * @param position given position
     * @param square square to check (number, 0..63)
     * @param attacker colour of the attacker, enum Colour
     * @returns {boolean} true square if attacked
     */
    isSquareAttackedByColour: function (position, square, attacker) {

        let alreadyAttacked = false;

        // straight rays (ranks and files), for rooks and queen
        //
        const straightRays = BoardGeometry.straightRaysFromSquare(square);
        // test all straight rays
        straightRays.some(ray => {
            // follow a ray until hit a piece
            ray.some(testSquare => {
                    const piece = position.onSquare(testSquare);
                    const hit = (piece !== undefined);
                    if (hit && piece.colour === attacker &&
                        (piece.type === ROOK || piece.type === QUEEN)) {
                        alreadyAttacked = true;

                    }
                    return hit;
                }
            );
            return alreadyAttacked;
        });

        if (alreadyAttacked) return true;

        // diagonal rays, for bishops and queen
        //
        const diagRays = BoardGeometry.diagonalRaysFromSquare(square);
        // test all diagonal rays
        diagRays.some(ray => {
                // follow a ray until hit a piece
                ray.some(testSquare => {
                    const piece = position.onSquare(testSquare);
                    const hit = (piece !== undefined);
                    if (hit && piece.colour === attacker &&
                        (piece.type === BISHOP || piece.type === QUEEN)) {
                        alreadyAttacked = true;
                    }
                    return hit;
                });
                return alreadyAttacked;
            }
        );

        if (alreadyAttacked) return true;

        // knight
        //
        const knightSquares = BoardGeometry.squaresReachableByKnight(square);
        knightSquares.some(testSquare => {
            const piece = position.onSquare(testSquare);
            if (piece !== undefined && piece.type === KNIGHT && piece.colour === attacker) {
                alreadyAttacked = true;
            }
            return alreadyAttacked;
        });

        if (alreadyAttacked) return true;

        // king
        //
        const kingSquares = BoardGeometry.squaresReachableByKing(square);
        kingSquares.some(testSquare => {
            const piece = position.onSquare(testSquare);
            if (piece !== undefined && piece.type === KING && piece.colour === attacker) {
                alreadyAttacked = true;
            }
            return alreadyAttacked;
        });

        // pawn
        if (!alreadyAttacked) {
            let dy = (position.activeColour === WHITE) ? 1 : -1;
            let otherSquare = Square.fromDirection(square, 1, dy);
            if (otherSquare !== undefined) {
                const piece = position.onSquare(otherSquare);
                if (piece !== undefined && piece.type === PAWN && piece.colour === attacker) {
                    alreadyAttacked = true;
                }
            }
            otherSquare = Square.fromDirection(square, -1, dy);
            if (otherSquare !== undefined) {
                const piece = position.onSquare(otherSquare);
                if (piece !== undefined && piece.type === PAWN && piece.colour === attacker) {
                    alreadyAttacked = true;
                }
            }
        }

        return alreadyAttacked;
    },

    isCheckmate: function (position) {
        let result = false;
        const validMoves = ChessRules.getAllValidMoves(position);
        const activeColour = position.activeColour;
        if (validMoves.length === 0) {
            const otherColour = Colour.other(activeColour);
            const kingSquare = position.findKing(activeColour);
            if (ChessRules.isSquareAttackedByColour(position, kingSquare, otherColour)) {
                result = true;
            }
        }
        return result;
    },

    isStalemate: function (position) {
        let result = false;
        const validMoves = ChessRules.getAllValidMoves(position);
        const activeColour = position.activeColour;
        if (validMoves.length === 0) {
            const otherColour = Colour.other(activeColour);
            const kingSquare = position.findKing(activeColour);
            if (!ChessRules.isSquareAttackedByColour(position, kingSquare, otherColour)) {
                result = true;
            }
        }
        return result;
    },



    getAllValidMoves: function (position) {

        const candidateMoves = [];
        const activeColour = position.activeColour;

        // determine all candidate move
        for (let from = 0; from < 64; from += 1) {
            const piece = position.onSquare(from);
            if (piece !== undefined && piece.colour === activeColour) {
                Movements[piece.type](position, from).forEach(m => {
                    candidateMoves.push(m)
                });
            }
        }

        // filter out moves where the king is attacked afterwards.
        return candidateMoves.filter(m => {
            const newPos = position.performMove(m);
            const kingsSquare = newPos.findKing(activeColour);

            return !ChessRules.isSquareAttackedByColour(newPos, kingsSquare, newPos.activeColour);
        });
    }
};

const Movements = {
    getKingCandidates: function (position, from) {
        const kingMoves = [];

        BoardGeometry.squaresReachableByKing(from).forEach(to => {
            const piece = position.onSquare(to);
            if (piece === undefined || piece.colour !== position.activeColour) {
                kingMoves.push(new Move(from, to));
            }
        });

        Movements.castling(position).forEach(m => {
            kingMoves.push(m)
        });

        return kingMoves;
    },

    getKnightCandidates: function (position, from) {
        const knightMoves = [];

        BoardGeometry.squaresReachableByKnight(from).forEach(to => {
            const pieceOnTarget = position.onSquare(to);
            if (pieceOnTarget === undefined || pieceOnTarget.colour !== position.activeColour) {
                knightMoves.push(new Move(from, to));
            }
        });

        return knightMoves;
    },


    getQueenCandidates: function (position, from) {
        const queenMoves = [];

        BoardGeometry.allRaysFromSquare(from).forEach(ray => {
            ray.some(to => {
                    const piece = position.onSquare(to);
                    const hit = piece !== undefined;
                    if (!hit || piece.colour !== position.activeColour) {
                        queenMoves.push(new Move(from, to));
                    }
                    return hit;
                }
            );
        });
        return queenMoves;
    },

    getRookCandidates: function (position, from) {
        const rookMoves = [];

        BoardGeometry.straightRaysFromSquare(from).forEach(ray => {
            ray.some(to => {
                    const piece = position.onSquare(to);
                    const hit = piece !== undefined;
                    if (!hit || piece.colour !== position.activeColour) {
                        rookMoves.push(new Move(from, to));
                    }
                    return hit;
                }
            );
        });
        return rookMoves;
    },

    getBishopCandidates: function (position, from) {
        const bishopMoves = [];

        BoardGeometry.diagonalRaysFromSquare(from).forEach(ray => {
            ray.some(to => {
                    const piece = position.onSquare(to);
                    const hit = piece !== undefined;
                    if (!hit || piece.colour !== position.activeColour) {
                        bishopMoves.push(new Move(from, to));
                    }
                    return hit;
                }
            );
        });
        return bishopMoves;
    },

    getPawnCandidates: function (position, from) {
        const pawnMoves = [];

        // pawn advances 1 squares
        const activeColour = position.activeColour;
        const dy = (activeColour === WHITE) ? -1 : 1;
        const row = Square.row(from);

        let to = Square.fromDirection(from, 0, dy);

        if (to !== undefined && position.onSquare(to) === undefined) {

            if ((activeColour === WHITE && row === 1)
                || (activeColour === BLACK && row === 6)) {
                pawnMoves.push(new Move(from, to, QUEEN));
                pawnMoves.push(new Move(from, to, ROOK));
                pawnMoves.push(new Move(from, to, KNIGHT));
                pawnMoves.push(new Move(from, to, BISHOP));
            } else {
                pawnMoves.push(new Move(from, to));
            }

            // pawn advances 2 squares
            if ((activeColour === WHITE && row === 6)
                || (activeColour === BLACK && row === 1)) {
                to = Square.fromDirection(from, 0, dy * 2);
                if (position.onSquare(to) === undefined) {
                    pawnMoves.push(new Move(from, to));
                }
            }
        }

        // pawn captures diagonal
        to = Square.fromDirection(from, -1, dy);
        if (to !== undefined) {
            const pieceOnTarget = position.onSquare(to);
            if (pieceOnTarget !== undefined && pieceOnTarget.colour !== activeColour) {
                if ((activeColour === WHITE && row === 1)
                    || (activeColour === BLACK && row === 6)) {
                    pawnMoves.push(new Move(from, to, QUEEN));
                    pawnMoves.push(new Move(from, to, ROOK));
                    pawnMoves.push(new Move(from, to, KNIGHT));
                    pawnMoves.push(new Move(from, to, BISHOP));
                } else {
                    pawnMoves.push(new Move(from, to));
                }
            } else if (to === position.enPassant) {
                pawnMoves.push(new Move(from, to));
            }
        }
        to = Square.fromDirection(from, 1, dy);
        if (to !== undefined) {
            const pieceOnTarget = position.onSquare(to);
            if (pieceOnTarget !== undefined && pieceOnTarget.colour !== activeColour) {
                if ((activeColour === WHITE && row === 1)
                    || (activeColour === BLACK && row === 6)) {
                    pawnMoves.push(new Move(from, to, QUEEN));
                    pawnMoves.push(new Move(from, to, ROOK));
                    pawnMoves.push(new Move(from, to, KNIGHT));
                    pawnMoves.push(new Move(from, to, BISHOP));
                } else {
                    pawnMoves.push(new Move(from, to));
                }
            } else if (to === position.enPassant) {
                pawnMoves.push(new Move(from, to));
            }
        }

        return pawnMoves;
    },


    castling: function (position) {
        const castlingMoves = [];

        if ((position.castlingRights !== undefined)
            && (position.castlingRights.length > 0)) {

            if (position.activeColour === WHITE) {
                // kingside white
                if (position.castlingRights.indexOf('K') >= 0) {
                    if (position.isEmpty(Square.f1)
                        && position.isEmpty(Square.g1)
                        && !ChessRules.isSquareAttackedByColour(position, Square.e1, BLACK)
                        && !ChessRules.isSquareAttackedByColour(position, Square.f1, BLACK)
                        && !ChessRules.isSquareAttackedByColour(position, Square.g1, BLACK)) {
                        castlingMoves.push(new Move("e1g1"));
                    }
                }
                // queenside white
                if (position.castlingRights.indexOf('Q') >= 0) {
                    if (position.isEmpty(Square.b1)
                        && position.isEmpty(Square.c1)
                        && position.isEmpty(Square.d1)
                        && !ChessRules.isSquareAttackedByColour(position, Square.c1, BLACK)
                        && !ChessRules.isSquareAttackedByColour(position, Square.d1, BLACK)
                        && !ChessRules.isSquareAttackedByColour(position, Square.e1, BLACK)) {
                        castlingMoves.push(new Move("e1c1"));
                    }
                }
            } else {
                // kingside black
                if (position.castlingRights.indexOf('k') >= 0) {
                    if (position.isEmpty(Square.f8)
                        && position.isEmpty(Square.g8)
                        && !ChessRules.isSquareAttackedByColour(position, Square.e8, WHITE)
                        && !ChessRules.isSquareAttackedByColour(position, Square.f8, WHITE)
                        && !ChessRules.isSquareAttackedByColour(position, Square.g8, WHITE)) {
                        castlingMoves.push(new Move("e8g8"));
                    }
                }
                // quenside black
                if (position.castlingRights.indexOf('q') >= 0) {
                    if (position.isEmpty(Square.b8)
                        && position.isEmpty(Square.c8)
                        && position.isEmpty(Square.d8)
                        && !ChessRules.isSquareAttackedByColour(position, Square.c8, WHITE)
                        && !ChessRules.isSquareAttackedByColour(position, Square.d8, WHITE)
                        && !ChessRules.isSquareAttackedByColour(position, Square.e8, WHITE)) {
                        castlingMoves.push(new Move("e8c8"));
                    }
                }
            }
        }
        return castlingMoves;
    },
};
Movements[KING] = Movements.getKingCandidates;
Movements[QUEEN] = Movements.getQueenCandidates;
Movements[ROOK] = Movements.getRookCandidates;
Movements[BISHOP] = Movements.getBishopCandidates;
Movements[KNIGHT] = Movements.getKnightCandidates;
Movements[PAWN] = Movements.getPawnCandidates;


if (typeof module !== 'undefined' && module.exports) {
    exports.ChessRules = ChessRules;
}

