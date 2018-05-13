const GAME_ID_REQUEST_PARAM = 'game_id';

let data={
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
        const game_id = this.gameIdFromRequestParam();
        const currentGame = await getGame(game_id);
        Vue.set(data, 'game', currentGame);
        connect(game_id);
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
        sendMove: async function () {
            try {
                await sendMove(this.nextMove, this.game.gameId);
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
            }
        }
    },
    computed: {
        gameFenLink: function() {
            return diagramUrlForFen(this.game.fen)
        }
    }
});
