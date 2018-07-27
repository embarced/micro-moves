const assert = require('assert');

const domain = require("../../domain.js");
const rules = require("../../rules.js");

const ChessRules = rules.ChessRules;
const Position = domain.Position;


describe('ChessRules unit tests', () => {

    describe('isDrawnByFiftyMoveRule', () => {

        it('setup position (no draw)', () => {
            const pos = new Position();
            assert.ok(! ChessRules.isDrawnByFiftyMoveRule(pos));
        });

        it('long game (drawn)', () => {
            const fen = "8/8/8/8/6k1/8/3K1B2/8 b - - 150 155";
            const pos = new Position(fen);
            assert.ok(ChessRules.isDrawnByFiftyMoveRule(pos));
        });

    })

});