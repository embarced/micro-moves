const assert = require('assert');

const boardGeometry = require("../../geometry.js");
const Square = boardGeometry.Square;

const domain = require("../../domain.js");

const Position = domain.Position;
const Colour = domain.Colour;
const PieceType = domain.PieceType;

const Piece = domain.Piece;
const Move = domain.Move;

describe('Position unit tests', function () {
    describe('construction', function () {

        it('default constructor', function () {
            const startPos = new Position();

            assert.equal(startPos.activeColour, Colour.WHITE);
            assert.equal(startPos.castlingRights, "KQkq");
            assert.equal(startPos.enPassant, undefined);
            assert.equal(startPos.halfmoveClock, 0);
            assert.equal(startPos.fullmoveCounter, 1);

            const whitePawn = new Piece(Colour.WHITE, PieceType.PAWN);
            const blackRook = Piece.fromChar('r');

            assert.deepEqual(startPos.onSquare(Square.e2), whitePawn);
            assert.deepEqual(startPos.onSquare(Square.a8), blackRook);
        });

        it('by FEN string, initial Position', function () {
            const fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1';
            const startPos = new Position(fen);

            assert.equal(startPos.toString(), fen);
        });

        it('by FEN string, Fool\'s mate ', function () {
            const fen = 'rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 0 3';
            const startPos = new Position(fen);

            assert.equal(startPos.toString(), fen);
        });

    });

    describe('movement', function () {

        it('capture en passant', function () {

            const pos = new Position('rnbqkbnr/ppp1p1pp/8/8/3pPp2/2N2N2/PPPP1PPP/R1BQKB1R b KQkq e3 0 1');

            // pawn captures en passant
            const move1 = new Move("f4e3");
            const pos1 = pos.performMove(move1);
            assert.equal(pos1.enPassant, undefined);
            assert.ok(pos1.isEmpty(Square.e4));

            // pawn captures en passant
            const move2 = new Move("d4e3");
            const pos2 = pos.performMove(move2);
            assert.equal(pos2.enPassant, undefined);
            assert.ok(pos2.isEmpty(Square.e4));

            // pawn captures other piece
            const move3 = new Move("d4c3");
            const pos3 = pos.performMove(move3);
            assert.equal(pos3.enPassant, undefined);
            assert.ok(!pos3.isEmpty(Square.e4));
        });


        it('promotion black', function () {
            const pos = new Position('r8/1P6/2K2p2/8/8/5k2/6p1/8 b - - 0 1');

            // plack pawn promotes to queen
            const move1 = new Move("g2g1Q");
            const pos1 = pos.performMove(move1);
            assert.deepEqual(pos1.onSquare(Square.g1), Piece.fromChar('q'));

            // plack pawn promotes to knight
            const move2 = new Move("g2g1N");
            const pos2 = pos.performMove(move2);
            assert.deepEqual(pos2.onSquare(Square.g1), Piece.fromChar('n'));
        });


        it('promotion white', function () {
            const pos = new Position('8/1P6/2K2p2/8/8/5k2/6p1/8 w - - 0 67');

            // white pawn promotes to rook
            const move1 = new Move("b7b8r");
            const pos1 = pos.performMove(move1);
            assert.deepEqual(pos1.onSquare(Square.b8), Piece.fromChar('R'));

            // white pawn promotes to bishop
            const move2 = new Move("b7b8b");
            const pos2 = pos.performMove(move2);
            assert.deepEqual(pos2.onSquare(Square.b8), Piece.fromChar('B'));
        });


        it('fullmove counter', function () {

            let pos = new Position();

            // First Move white, pawn e2e4
            pos = pos.performMove(new Move("e2e4"));
            assert.equal(pos.fullmoveCounter, 1);
            assert.equal(pos.activeColour, Colour.BLACK);

            // First Move black, pawn e7e5
            pos = pos.performMove(new Move("e7e5"));
            assert.equal(pos.fullmoveCounter, 2);
            assert.equal(pos.activeColour, Colour.WHITE);

            // Second Move white, knight b1c3
            pos = pos.performMove(new Move("b1c3"));
            assert.equal(pos.fullmoveCounter, 2);
            assert.equal(pos.activeColour, Colour.BLACK);

            // Second Move black, knight g8f6
            pos = pos.performMove(new Move("g8f6"));
            assert.equal(pos.fullmoveCounter, 3);
            assert.equal(pos.activeColour, Colour.WHITE);
        });

        it('halfmove clock', function () {
            let pos = new Position();

            // First Move white, knight b1c3
            pos = pos.performMove(new Move("b1c3"));
            assert.equal(pos.halfmoveClock, 1);

            // First Move black, knight g8f6
            pos = pos.performMove(new Move("g8f6"));
            assert.equal(pos.halfmoveClock, 2);

            // Second move white, pawn e2e4
            pos = pos.performMove(new Move("e2e4"));
            assert.equal(pos.halfmoveClock, 0);

            // Second move black, knight captures e2e4
            pos = pos.performMove(new Move("f6e4"));
            assert.equal(pos.halfmoveClock, 0);
        });
    });
});