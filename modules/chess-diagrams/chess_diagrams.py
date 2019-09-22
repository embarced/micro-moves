import io
import flask

import draw
from prometheus_flask_exporter.multiprocess import GunicornPrometheusMetrics

app = flask.Flask(__name__)
metrics = GunicornPrometheusMetrics(app)


@app.route('/')
def index():
    """ Displays a simple demo page with links for diagram examples. """

    return flask.render_template('index.html')


@app.route('/board.png')
def board_png():
    """ Creates a PNG image for FEN position given as request paramaeter."""

    fen = flask.request.args.get("fen")
    if fen == '' or fen is None:
        fen = '8/8/8/8/8/8/8/8 w KQkq - 0 1'

    image = draw.draw_diagram_for_fen(fen)

    image_bytes = io.BytesIO()
    image.save(image_bytes, format='png')
    response = flask.make_response(image_bytes.getvalue())
    response.mimetype = 'image/png'

    return response


if __name__ == '__main__':
    app.run(debug=False)
