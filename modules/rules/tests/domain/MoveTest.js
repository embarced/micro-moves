const assert = require('assert');
const domain = require("../../domain.js");

const Move = domain.Move;
const PieceType = domain.PieceType;

const boardGeometry = require("../../geometry.js");
const Square = boardGeometry.Square;

describe('Move unit tests', () => {

    describe('constructor', () => {

        it('by String, e2e4', () => {
            const move = new Move('e2e4');
            assert.equal(Square.e2, move.from);
            assert.equal(Square.e4, move.to);
        });

        it('by String with promotion, g7g8q', () => {
            const move = new Move('g7g8q');
            assert.equal(Square.g7, move.from);
            assert.equal(Square.g8, move.to);
            assert.equal('q', move.promotion);
        });

    });

    describe('toString', () => {
        it('simple, e2e4', () => {
            const move = new Move(Square.e2, Square.e4);
            assert.equal('e2e4', move.toString());
        });
        it('with promotion, g7g8n', () => {
            const move = new Move(Square.g7, Square.g8, PieceType.KNIGHT);
            assert.equal('g7g8n', move.toString());
        });
    });

});