const GAME_ID_REQUEST_PARAM = 'game_id';

let data={
    user: '',
    game: '',
    nextMove: '',
    message: {
        type: 'none',
        text: ""
    }
};

new Vue({
    el: '#app',
    data: data,
    mounted: async function(){

        const currentUser = this.currentUserFromCookie();
        Vue.set(data, 'user', currentUser);

        const game_id = this.gameIdFromRequestParam();
        const currentGame = await restGetGame(game_id);
        Vue.set(data, 'game', currentGame);

        webSocketConnect(game_id);
    },
    methods: {
        gameIdFromRequestParam() {
            let name = '';
            let result = 1;
            /*
            Source: https://stackoverflow.com/questions/831030/how-to-get-get-request-parameters-in-javascript
            */
            if(name=(new RegExp('[?&]'+encodeURIComponent(GAME_ID_REQUEST_PARAM)+'=([^&]*)')).exec(location.search)) {
                result = decodeURIComponent(name[1]);
            }
            return result;
        },
        currentUserFromCookie() {
            const jwt_token = getJWTfromCookie();
            if (jwt_token === '') {
                return '';
            }
            else {
                return getUserFromJWT(jwt_token);
            }
        },
        sendMove: async function () {
            try {
                await restSendMove(this.nextMove, this.game.gameId);
            } catch (error) {
                Vue.set(data, 'message', error);
                return;
            }
            Vue.set(data, 'message', {
                type: 'success',
                text: 'Move performed.'
            });
            this.nextMove = "";
        },
        boardImageClicked: function(event) {
            const boardWidth = event.target.width;
            const fieldName = fieldNameFromCoords(event.offsetX, event.offsetY, boardWidth);
            if (this.nextMove.length <= 2) {
                this.nextMove = this.nextMove + fieldName;
            } else if (this.nextMove.length == 4) {
                this.nextMove = fieldName;
            }
        }
    },
    computed: {
        gameFenLink: function() {
            return diagramUrlForFen(this.game.fen)
        }
    }
});
