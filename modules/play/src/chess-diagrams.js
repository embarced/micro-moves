// const DIAGRAM_URL = 'http://' + 'localhost:9000' +'/chess-diagrams/board.png';

const DIAGRAM_URL = 'http://' + window.location.host +'/chess-diagrams/board.png';

function diagramUrlForFen (fen) {
    let link = DIAGRAM_URL;
    if (fen) {
        link += '?fen=' + fen;
    }
    return encodeURI(link);
}

function fieldNameFromCoords(x, y, boardWidth) {
    const squareSize = boardWidth / 9; // 9 instead of 8 due to the border
    const border = squareSize / 2;
    if (x > border && x < boardWidth - border && y > border && x < boardWidth - border ) {
        const line = Math.floor((x - border) / squareSize);
        const row = Math.floor((y - border) / squareSize);
        return "" + "abcdefgh".charAt(line) + (8-row);
    } else {
        return "";
    }
}