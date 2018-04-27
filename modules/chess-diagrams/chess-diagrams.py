import io
import flask

import draw

app = flask.Flask(__name__)


@app.route('/')
def index():
    return flask.render_template('index.html')


@app.route('/board.png')
def board_png():

    fen = flask.request.args.get("fen")
    if fen == '' or fen is None:
        fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

    image = draw.draw_diagram_for_fen(fen)

    image_bytes = io.BytesIO()
    image.save(image_bytes, format='png')
    response = flask.make_response(image_bytes.getvalue())
    response.mimetype = 'image/png'

    return response


if __name__ == '__main__':
    app.run(debug=False)
