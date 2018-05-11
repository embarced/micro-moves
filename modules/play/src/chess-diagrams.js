/*
Source: https://stackoverflow.com/questions/831030/how-to-get-get-request-parameters-in-javascript
*/
function getRequestParameter(name){
    if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
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