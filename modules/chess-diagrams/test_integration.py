import chess_diagrams

# setup for all tests. See https://docs.pytest.org/en/2.7.3/xunit_setup.html
#
def setup_method(self, method):
    chess_diagrams.app.testing = True

# Test for a single response. See http://flask.pocoo.org/docs/1.0/testing/
#
def test_index_page():
    app = chess_diagrams.app.test_client()
    response = app.get('/')
    assert response.status_code == 200
    assert b'<html>' in response.data


def test_board_image_no_param():
    app = chess_diagrams.app.test_client()
    response = app.get('/board.png')
    assert response.status_code == 200
    assert response.mimetype == 'image/png'


def test_board_image_with_param():
    app = chess_diagrams.app.test_client()
    fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
    response = app.get('/board.png?fen='+fen)
    assert response.status_code == 200
    assert response.mimetype == 'image/png'
