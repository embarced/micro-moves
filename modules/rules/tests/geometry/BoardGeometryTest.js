const assert = require('assert');
const domain = require("../../domain.js");
const Square = domain.Square;


const geometry = require("../..//geometry.js");
const BoardGeometry = geometry.BoardGeometry;

describe('BoardGeometry unit tests', () => {

    describe('reachable by king', () => {
        it('h8', () => {
            const squares = BoardGeometry.squaresReachableByKing(Square.h8);
            assert.equal(3, squares.length);
            assert.ok(squares.includes(Square.g8));
            assert.ok(squares.includes(Square.g7));
            assert.ok(squares.includes(Square.h7));
        });
        it('e7', () => {
            const squares = BoardGeometry.squaresReachableByKing(Square.e7);
            assert.equal(8, squares.length);
        });
    });


    describe('reachable by knight', () => {
        it('a1', () => {
            const squares = BoardGeometry.squaresReachableByKnight(Square.a1);
            assert.equal(2, squares.length);
            assert.ok(squares.includes(Square.c2));
            assert.ok(squares.includes(Square.b3));
        });
        it('d4', () => {
            const squares = BoardGeometry.squaresReachableByKnight(Square.d4);
            assert.equal(8, squares.length);
        });
    });
});

