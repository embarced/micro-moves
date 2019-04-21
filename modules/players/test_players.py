import players


# setup for all tests. See https://docs.pytest.org/en/2.7.3/xunit_setup.html
#
def setup_method(self, method):
    players.app.testing = True


# Test for a single response. See http://flask.pocoo.org/docs/1.0/testing/
#
def test_about_page():
    app = players.app.test_client()
    response = app.get('/about.html')
    assert response.status_code == 200
    assert b'<html>' in response.data


def test_login_no_credentials():
    app = players.app.test_client()
    response = app.post('/login')
    assert response.status_code == 401


def test_login():
    app = players.app.test_client()
    response = app.post('/login', data=dict(
        user='paul',
        password='paul'
    ))
    assert response.status_code == 302

