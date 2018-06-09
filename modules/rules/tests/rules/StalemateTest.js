const assert = require('assert');

const domain = require("../../domain.js");
const rules = require("../../rules.js");

const ChessRules = rules.ChessRules;
const Position = domain.Position;

const Colour = domain.Colour;
const Square = domain.Square;




describe('ChessRules unit tests', () => {

    describe('isStalemate', () => {

        it('setup position (no stalemate)', () => {
            const pos = new Position();
            assert.ok(! ChessRules.isStalemate(pos));
        });

        it('Checkmate (no stalemate)', () => {
            const pos = new Position('4k2R/8/4K3/8/8/8/8/8 b - - 0 1');
            assert.ok(ChessRules.isCheckmate(pos));
            assert.ok(! ChessRules.isStalemate(pos));
        });

        it('stalemate with king and pawn, black to move is in stalemate.', () => {
            const pos = new Position('5k2/5P2/5K2/8/8/8/8/8 b - - 0 1');
            assert.ok(ChessRules.isStalemate(pos));
        });

        it('stalemate with king and rook, white to move is in stalemate.', () => {
            const pos = new Position('K7/1r6/2k5/8/8/8/8/8 w - - 0 1');
            assert.ok(ChessRules.isStalemate(pos));
        });


    })

});