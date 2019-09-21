const express = require('express');

const rules = require('./rules');
const ChessRules = rules.ChessRules;

const domain = require('./domain');
const Move = domain.Move;

const app = express();

app.get('/', function(req, res){
    res.sendFile('index.html', { root: __dirname + "/static-html"} );
});

app.get('/health', function (req, res) {
    res.send('OK');
});

app.get('/allValidMoves', function (req, res) {

    let fen = req.query.fen;
    if (fen === undefined ) {
        fen='rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1';
    }

    const pos = new domain.Position(fen);
    const moves = ChessRules.getAllValidMoves(pos);
    const movesWithNames = moves.map(m => m.toString());

    const result = {
        moveCount: movesWithNames.length,
        moves: movesWithNames
    };

    res.json(result);
});

app.get('/validateMove', function (req, res) {

    let fen = req.query.fen;
    let move = req.query.move;
    if (fen === undefined || move === undefined) {
        res.status(400).send('invalid call of /validateMove');
    }

    const pos = new domain.Position(fen);
    const moves = rules.ChessRules.getAllValidMoves(pos);
    const movesWithNames = moves.map(m => m.toString());

    const result = {
        'fen': fen,
        'move': move
    };
    if (movesWithNames.includes(move)) {
        result.valid = true;
        const newPos = pos.performMove(new Move(move));
        result.resultingFen = newPos.toString();
        result.checkmateAfterMove = ChessRules.isCheckmate(newPos);
        result.stalemateAfterMove = ChessRules.isStalemate(newPos);
        result.drawnByFiftyMoves = ChessRules.isDrawnByFiftyMoveRule(newPos);
    } else {
        result.valid = false;
    }

    res.json(result);
});


const server = app.listen(8081, function () {

    const host = server.address().address;
    const port = server.address().port;

    console.log("rules server app listening at http://%s:%s", host, port)

});