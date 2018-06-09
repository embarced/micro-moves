const express = require('express');

const rules = require('./rules');
const ChessRules = rules.ChessRules;

const domain = require('./domain');
const Move = domain.Move;

const app = express();

app.get('/', function(req, res){
    res.sendFile('index.html', { root: __dirname + "/static-html"} );
});

app.get('/allValidMoves', function (req, res) {

    let fen = req.query.fen;
    if (fen === undefined ) {
        fen='rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1';
    }
    console.log(fen);

    const pos = new domain.Position(fen);
    const moves = ChessRules.getAllValidMoves(pos);
    const movesWithNames = moves.map(m => m.toString());

    const result = {
        moveCount: movesWithNames.length,
        moves: movesWithNames
    };

    const jsonResult = JSON.stringify(result);
    console.log(jsonResult);

    res.end(jsonResult);
});

app.get('/validateMove', function (req, res) {

    let fen = req.query.fen;
    let move = req.query.move;
    if (fen === undefined || move === undefined) {
        res.status(400).send('invalid call of /validateMove');
    }
    console.log(fen, move);

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
        result.isCheckmateAfterMove = ChessRules.isCheckmate(newPos);
        result.isStalemateAfterMove = ChessRules.isStalemate(newPos);
    } else {
        result.valid = false;
    }

    const jsonResult = JSON.stringify(result);
    console.log(jsonResult);

    res.end(jsonResult);
});



const server = app.listen(8081, function () {

    const host = server.address().address;
    const port = server.address().port;

    console.log("rules server app listening at http://%s:%s", host, port)

})