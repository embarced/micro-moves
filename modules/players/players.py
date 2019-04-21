import flask

import web_token
import db


app = flask.Flask(__name__)
app.config['SECRET_KEY'] = 'Geheimnis123'

def redirect_uri():

    proto = 'http'
    if flask.request.headers.has_key('X-FLEXess-Proto'):
        proto = flask.request.headers['X-FLEXess-Proto']

    host = '0.0.0.0'
    if flask.request.headers.has_key('X-FLEXess-Host'):
        host = flask.request.headers['X-FLEXess-Host']

    uri = '/players/'
    if flask.request.headers.has_key('X-FLEXess-Uri'):
        uri = flask.request.headers['X-FLEXess-Uri']

    token = uri.split('/')
    url = proto + "://" + host + "/" + token[1] + "/"
    return url


@app.route('/about.html')
def index():
    """ Displays the about page of the players submodule. """

    user = web_token.jwt_cookie_to_user()
    return flask.render_template('about.html', user=user)


@app.route('/')
def all_players():
    """ Displays the index page of the players submodule which lists all players. """

    user = web_token.jwt_cookie_to_user()
    players = db.all_users()
    resp = flask.make_response(flask.render_template('allPlayers.html', players=players, user=user))
    return resp


@app.route('/login')
def login_form():
    resp = flask.make_response(flask.render_template('login_form.html', user=None))
    return resp


@app.route('/login', methods=["POST"])
def login():
    form = flask.request.form
    user = form.get('user')
    password = form.get('password')

    player = db.user_by_userid(user)
    # TODO: Check password

    if player is not None:
        flask.flash('Logged in to FLEXess.', category='info')

        url = redirect_uri()
        resp = flask.redirect(url, 302)

        encoded = web_token.user_to_jwt(player)
        resp.set_cookie(web_token.JWT_COOKIE_NAME, encoded)
        return resp
    else:
        flask.flash('Login failed.', category='danger')
        resp = flask.make_response(flask.render_template('login_form.html', user=None), 401)
        return resp


@app.route('/logoff')
def logoff():
    """ Logs the the user off. Therefore removes the JWT token"""

    flask.flash('Logged off.', category='info')

    url = redirect_uri()
    resp = flask.redirect(url, 302)
    resp.set_cookie(web_token.JWT_COOKIE_NAME, '', expires=0)

    return resp


@app.route('/profile_<userid>')
def profile(userid):
    auth_user = web_token.jwt_cookie_to_user()

    profile_user = db.user_by_userid(userid)

    resp = flask.make_response(flask.render_template('profile.html', user=auth_user, profile_user=profile_user))
    return resp


@app.route('/register')
def register_form():
    player = {}
    resp = flask.make_response(flask.render_template('register_form.html', user=None, player=player))
    return resp


@app.route('/register', methods=["POST"])
def register():

    form = flask.request.form
    userid = form.get('userid')
    name = form.get('name')
    player = { 'userid': userid, 'name': name, 'roles': 'user'}

    success = True

    if userid == '':
        flask.flash('User ID required.', category='warning')
        success = False

    if name == '':
        flask.flash('Name required.', category='warning')
        success = False

    if success:
        result = db.user_by_userid(userid)
        if result is not None :
            flask.flash('User ID not available.', category='warning')
            success = False

    if success:
        db.create_user(player)
        flask.flash('Registration of ' + name + ' to FLEXess was successful.', category='success')

        encoded = web_token.user_to_jwt(player)
        url = redirect_uri()
        resp = flask.redirect(url, 302)
        resp.set_cookie(web_token.JWT_COOKIE_NAME, encoded)

    else:
        resp = flask.make_response(flask.render_template('register_form.html', user=None, player=player))

    return resp


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=False)