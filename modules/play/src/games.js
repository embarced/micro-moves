const REST_URL = 'http://localhost:8080/api/games';
const WEBSOCKET_URL = 'http://localhost:8080/games-websocket';

async function getGame(gameId) {
    const gameUrl = `${REST_URL}/${gameId}`;
    const rs = await axios.get(gameUrl);
    return rs.data;
}

async function sendMove(nextMove, gameId) {
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

let stompClient = null;

function connect(gameId) {
    const socket = new SockJS(WEBSOCKET_URL);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        const topic = `/topic/game_${gameId}`;
        stompClient.subscribe(topic, function (game) {
            gameReceived(JSON.parse(game.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function gameReceived(message) {
    Vue.set(data, 'game', message);
}