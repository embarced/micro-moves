const REST_URL = 'http://' + window.location.host + '/games-api/games';
const WEBSOCKET_URL = 'http://' + window.location.host + '/games-websocket';

async function restGetGame(gameId) {
    const gameUrl = `${REST_URL}/${gameId}`;
    const rs = await axios.get(gameUrl);
    return rs.data;
}

async function restSendMove(nextMove, gameId) {
    const mv = {
        text: nextMove
    };
    try {
        const gameUrl = `${REST_URL}/${gameId}/moves/`;
        await axios.post(gameUrl, mv);
    } catch (error) {
        if (error.response) {
            console.log(error.response.data);
            console.log(error.response.status);
            console.log(error.response.headers);
            throw {
                type: 'warning',
                text: error.response.data.toString()
            };
        } else {
            throw {
                type: 'error',
                text: error.toString()
            };
        }
    }
}

/* Functions for WebSocket
*/

let stompClient = null;

function webSocketConnect(gameId) {
    const socket = new SockJS(WEBSOCKET_URL);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        const topic = `/topic/game_${gameId}`;
        stompClient.subscribe(topic, function (game) {
            webSocketGameReceived(JSON.parse(game.body));
        });
    });
}

function webSocketDisconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function webSocketGameReceived(message) {
    Vue.set(data, 'game', message);
}