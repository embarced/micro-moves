const assert = require('assert');
const domain = require("../../domain.js");

const Piece = domain.Piece;
const PieceType = domain.PieceType;
const Colour = domain.Colour;
const BLACK = Colour.BLACK;
const WHITE = Colour.WHITE;

describe('Piece unit tests', () => {

    describe('constructor', () => {
        it('white pawn', () => {
            const whitePawn = new Piece(WHITE, PieceType.PAWN);
            assert.equal('P', whitePawn.toString());
        });
        it('black knight', () => {
            const blackKnight = new Piece(BLACK, PieceType.KNIGHT);
            assert.equal('n', blackKnight.toString());
        });
    });

    describe('fromChar', () => {
        it('black queen', () => {
            const blackQueen = Piece.fromChar('q');
            assert.equal(BLACK, blackQueen.colour);
            assert.equal(PieceType.QUEEN, blackQueen.type);
        });
        it('white king', () => {
            const whiteKing = Piece.fromChar('K');
            assert.equal(WHITE, whiteKing.colour);
            assert.equal(PieceType.KING, whiteKing.type);
        });
    });
});
