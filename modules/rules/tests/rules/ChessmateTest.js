const assert = require('assert');

const domain = require("../../domain.js");
const rules = require("../../rules.js");

const ChessRules = rules.ChessRules;
const Position = domain.Position;


describe('ChessRules unit tests', () => {

    describe('isCheckmate', () => {

        it('setup position (no mate)', () => {
            const pos = new Position();
            assert.ok(! ChessRules.isCheckmate(pos));
        });


        // https://en.wikipedia.org/wiki/Scholar%27s_mate
        it('Scholar\'s mate', () => {
            const fen = "r1bqkb1r/pppp1Qpp/2n2n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1";
            const pos = new Position(fen);
            assert.ok(ChessRules.isCheckmate(pos));
        });

        // https://en.wikipedia.org/wiki/Fool%27s_mate
        it('Fool\'s mate', () => {
            const fen = "rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 0 3";
            const pos = new Position(fen);
            assert.ok(ChessRules.isCheckmate(pos));
        });

    })

});