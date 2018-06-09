const assert = require('assert');

const domain = require("../../domain.js");
const rules = require("../../rules.js");

const ChessRules = rules.ChessRules;
const Position = domain.Position;

function checkMovesWithFEN(fen, expectedMoves) {
    const pos = new Position(fen);
    const moves = ChessRules.getAllValidMoves(pos);

    assert.equal(moves.length, expectedMoves.length);

    for (let i = 0; i < moves.length; i += 1) {
        moves[i] = moves[i].toString();
    }

    expectedMoves.forEach(function (move) {
        assert.ok(moves.includes(move), "expected move " + move + " contained?");
    });

    moves.forEach(function (move) {
        assert.ok(expectedMoves.includes(move), "contained move " + move + " expected?");
    });
}

describe('ChessRules unit tests', () => {
    describe('getAllValidMoves', () => {

        describe('pawn moves', () => {

            it('white player, move and capture', () => {
                const fen = "2k5/8/8/8/8/3p4/2P5/7K w - - 0 1";
                const expectedMoves = ["c2c3", "c2c4", "c2d3", "h1g1", "h1g2", "h1h2"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player, move and capture', () => {
                const fen = "k7/p7/2p5/4p1p1/5P2/8/8/K7 b - - 0 1";
                const expectedMoves = ["a7a5", "a7a6", "a8b7", "a8b8", "c6c5", "e5e4", "e5f4", "g5f4", "g5g4"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('white player, en passant', () => {
                const fen = "4k3/8/8/3Pp3/8/8/8/K7 w - e6 0 1";
                const expectedMoves = ["a1a2", "a1b1", "a1b2", "d5d6", "d5e6"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player, en passant', () => {
                const fen = "4k3/8/8/8/5pPp/8/8/7K b - g3 0 1";
                const expectedMoves = ["e8d7", "e8d8", "e8e7", "e8f7", "e8f8", "f4f3", "f4g3", "h4g3", "h4h3"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('white player, promotion', () => {
                const fen = "3rk3/P3P3/8/8/8/8/8/K7 w - - 0 1";
                const expectedMoves = ["a7a8q", "a7a8r", "a7a8b", "a7a8n", "e7d8q", "e7d8r", "e7d8b", "e7d8n", "a1a2", "a1b1", "a1b2"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player, promotion', () => {
                const fen = "k7/8/8/8/8/8/4p3/K2R1R2 b - - 0 1";
                const expectedMoves = ["a8a7", "a8b7", "a8b8", "e2d1b", "e2d1n", "e2d1q", "e2d1r", "e2e1b", "e2e1n", "e2e1q",
                    "e2e1r", "e2f1b", "e2f1n", "e2f1q", "e2f1r"];
                checkMovesWithFEN(fen, expectedMoves);
            });
        });

        describe('knight moves', () => {

            it('white player', () => {
                const fen = "k7/8/8/8/3p4/5N2/7P/7K w - - 0 1";
                const expectedMoves = ["h2h3", "h2h4", "f3d2", "f3d4", "f3e1", "f3e5", "f3g1", "f3g5", "f3h4", "h1g1", "h1g2"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player', () => {
                const fen = "k7/n7/8/1P6/8/8/8/7K b - - 0 1";
                const expectedMoves = ["a7b5", "a7c6", "a7c8", "a8b7", "a8b8"];
                checkMovesWithFEN(fen, expectedMoves);
            });
        });

        describe('bishop moves', () => {

            it('white player', () => {
                const fen = "k7/2p5/8/4B3/8/8/8/K7 w - - 0 1";
                const expectedMoves = ["e5b2", "e5c3", "e5c7", "e5d4", "e5d6", "e5f4", "e5f6", "e5g3", "e5g7", "e5h2", "e5h8", "a1a2", "a1b1", "a1b2"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player', () => {
                const fen = "k7/8/8/6b1/8/4P3/8/7K b - - 0 1";
                const expectedMoves = ["g5d8", "g5e3", "g5e7", "g5f4", "g5f6", "g5h4", "g5h6", "a8a7", "a8b7", "a8b8"];
                checkMovesWithFEN(fen, expectedMoves);
            });
        });

        describe('rook moves', () => {

            it('white player', () => {
                const fen = "k7/3p4/8/8/8/8/3R4/3K4 w - - 0 1";
                const expectedMoves = ["d2a2", "d2b2", "d2c2", "d2d3", "d2d4", "d2d5", "d2d6", "d2d7", "d2e2", "d2f2", "d2g2",
                    "d2h2", "d1c1", "d1c2", "d1e1", "d1e2"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player', () => {
                const fen = "k2r4/8/8/8/3r2P1/8/8/K2R4 b - - 0 1";
                const expectedMoves = ["d4a4", "d4b4", "d4c4", "d4d1", "d4d2", "d4d3", "d4d5", "d4d6", "d4d7", "d4e4", "d4f4",
                    "d4g4", "d8b8", "d8c8", "d8d5", "d8d6", "d8d7", "d8e8", "d8f8", "d8g8", "d8h8", "a8a7", "a8b7", "a8b8"];
                checkMovesWithFEN(fen, expectedMoves);
            });
        });

        describe('queen moves', () => {

            it('white player', () => {
                const fen = "1k6/1p6/2p3p1/8/4Q3/8/8/4K3 w - - 0 1";
                const expectedMoves = ["e4a4", "e4b1", "e4b4", "e4c2", "e4c4", "e4c6", "e4d3", "e4d4", "e4d5", "e4e2", "e4e3",
                    "e4e5", "e4e6", "e4e7", "e4e8", "e4f3", "e4f4", "e4f5", "e4g2", "e4g4", "e4g6", "e4h1", "e4h4", "e1d1",
                    "e1d2", "e1e2", "e1f1", "e1f2"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player', () => {
                const fen = "8/1k6/8/8/4q3/8/6P1/K3R3 b - - 0 1";
                const expectedMoves = ["e4a4", "e4b1", "e4b4", "e4c2", "e4c4", "e4c6", "e4d3", "e4d4", "e4d5", "e4e1", "e4e2",
                    "e4e3", "e4e5", "e4e6", "e4e7", "e4e8", "e4f3", "e4f4", "e4f5", "e4g2", "e4g4", "e4g6", "e4h4", "e4h7", "b7a6",
                    "b7a7", "b7a8", "b7b6", "b7b8", "b7c6", "b7c7", "b7c8"];
                checkMovesWithFEN(fen, expectedMoves);
            });
        });

        describe('king moves', () => {

            it('white player, move and capture', () => {
                const fen = "8/6k1/8/4r3/3KP3/8/8/8 w - - 0 1";
                const expectedMoves = ["d4c3", "d4c4", "d4d3", "d4e3", "d4e5"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player, move and capture', () => {
                const fen = "8/8/p7/k7/N7/8/8/7K b - - 0 1";
                const expectedMoves = ["a5a4", "a5b4", "a5b5"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('white player, kings in direct opposition', () => {
                const fen = "8/8/8/3k4/8/3K4/7p/8 w - - 0 1";
                const expectedMoves = ["d3c2", "d3c3", "d3d2", "d3e2", "d3e3"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player, only two squares left', () => {
                const fen = "8/8/7R/3k4/8/3P4/7B/7K b - - 0 1";
                const expectedMoves = ["d5d4", "d5c5"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('white player, only a single square left', () => {
                const fen = "7k/8/5pp1/8/7K/r7/8/8 w - - 0 1";
                const expectedMoves = ["h4g4"];
                checkMovesWithFEN(fen, expectedMoves);
            });


        });

        describe('castling', () => {

            it('white player, king side', () => {
                const fen = "8/8/8/8/8/4k3/P6P/R3K2R w K - 0 1";
                const expectedMoves = ["a1b1", "a1c1", "a1d1", "a2a3", "a2a4", "e1d1", "e1f1", "e1g1", "h1f1", "h1g1", "h2h3", "h2h4"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player, queen side', () => {
                const fen = "r3k2r/p6p/4K3/8/8/8/8/8 b q - 0 1";
                const expectedMoves = ["a7a5", "a7a6", "a8b8", "a8c8", "a8d8", "e8c8", "e8d8", "e8f8", "h7h5", "h7h6",
                    "h8f8", "h8g8"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('white player, occupied square', () => {
                const fen = "8/8/8/8/8/4k3/P6P/R3K1NR w KQ - 0 1";
                const expectedMoves = ["a1b1", "a1c1", "a1d1", "a2a3", "a2a4", "e1c1", "e1d1", "e1f1", "g1e2", "g1f3", "g1h3",
                    "h2h3", "h2h4"];
                checkMovesWithFEN(fen, expectedMoves);
            });

            it('black player, attacked square', () => {
                const fen = "r3k2r/p6p/4K3/8/8/8/8/2Q5 b kq - 0 1";
                const expectedMoves = ["a7a5", "a7a6", "a8b8", "a8c8", "a8d8", "e8d8", "e8f8", "e8g8", "h7h5", "h7h6", "h8f8",
                    "h8g8"];
                checkMovesWithFEN(fen, expectedMoves);
            });
        });

    })
});