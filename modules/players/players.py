import flask
import tinydb

import web_token

app = flask.Flask(__name__)
app.config['SECRET_KEY'] = 'Geheimnis123'


db = tinydb.TinyDB('db.json')
if len(db) == 0:
    db.insert({'userid': 'stefanz', 'name': 'Stefan Zörner'})
    db.insert({'userid': 'peter', 'name': 'Peter Yarrow'})
    db.insert({'userid': 'paul', 'name': 'Noel Paul Stookey'})
    db.insert({'userid': 'mary', 'name': 'Mary Travers'})
    db.insert({'userid': 'pinky', 'name': 'Pinky'})
    db.insert({'userid': 'brain', 'name': 'The Brain'})
    db.insert({'userid': 'stockfish', 'name': 'Stockfish Engine'})


@app.route('/')
def index():
    user = web_token.jwt_cookie_to_user()
    return flask.render_template('index.html', user=user)


@app.route('/allplayers')
def all_players():
    user = web_token.jwt_cookie_to_user()
    players = db.all()
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

    user_query = tinydb.Query()
    result = db.search(user_query.userid == user)

    print (result)

    # TODO: Check password

    if len(result) == 1:
        player = result[0]
        flask.flash('Logged in to FLEXess.', category='info')
        encoded = web_token.user_to_jwt(player)
        resp = flask.make_response(flask.render_template('index.html', user=player))
        resp.set_cookie(web_token.JWT_COOKIE_NAME, encoded)
        return resp
    else:
        flask.flash('Login failed.', category='danger')
        resp = flask.make_response(flask.render_template('login_form.html', user=None), 401)
        return resp

@app.route('/logoff')
def logoff():
    flask.flash('Logged off.', category='info')
    resp = flask.make_response(flask.render_template('index.html', user=None))
    resp.set_cookie(web_token.JWT_COOKIE_NAME, '', expires=0)
    return resp


@app.route('/profile_<userid>')
def profile(userid):
    auth_user = web_token.jwt_cookie_to_user()

    user_query = tinydb.Query()
    result = db.search(user_query.userid == userid)

    profile_user = None
    if len(result) == 1:
        profile_user = result[0]

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
    player = { 'userid': userid, 'name': name}

    success = True

    if userid == '':
        flask.flash('User ID required.', category='warning')
        success = False

    if name == '':
        flask.flash('Name required.', category='warning')
        success = False

    if success:
        user_query = tinydb.Query()
        result = db.search(user_query.userid == userid)
        if len(result) > 0:
            flask.flash('User ID not available.', category='warning')
            success = False

    if success:
        db.insert(player)
        flask.flash('Registration of ' + name + ' to FLEXess was successful.', category='success')

        encoded = web_token.user_to_jwt(player)
        resp = flask.make_response(flask.render_template('index.html', user=player))
        resp.set_cookie(web_token.JWT_COOKIE_NAME, encoded)

    else:
        resp = flask.make_response(flask.render_template('register_form.html', user=None, player=player))

    return resp


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=False)