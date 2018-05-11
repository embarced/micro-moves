const URL = 'http://localhost:8080/api/games/';

async function getGame(gameId) {
    const gameUrl = `${URL}/${gameId}`;
    const rs = await axios.get(gameUrl);
    return rs.data;
}

async function sendMove(nextMove, gameId) {
    const mv = {
        text: nextMove
    };
    try {
        const gameUrl = `${URL}/${gameId}/moves/`;
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
    const socket = new SockJS('http://localhost:8080/games-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
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
    console.log("Disconnected");
}

function gameReceived(message) {
    console.log(message);
    Vue.set(data, 'game', message);
}

