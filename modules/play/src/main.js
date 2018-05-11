var data={
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
        const game_id = getRequestParameter('game_id');
        const currentGame = await getGame(game_id);
        Vue.set(data, 'game', currentGame);
        connect(game_id);
    },
    methods: {
        switchToGame: async function(givenId){
            const currentGame = await getGame(givenId);
            Vue.set(data, 'game', currentGame);
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
            if (this.game != '') {
                const link = 'http://localhost:5000/board.png?fen=' + this.game.fen;
                return encodeURI(link);
            } else {
                return '';
            }
        }
    }
});
