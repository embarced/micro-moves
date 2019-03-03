import jwt
import flask
import os

JWT_COOKIE_NAME = os.environ.get('JWT_COOKIE_NAME')
JWT_SECRET = os.environ.get('JWT_SECRET')


def user_to_jwt(user):
    assert JWT_SECRET != None
    return user_and_secret_to_jwt(user, JWT_SECRET)


def jwt_to_user(token):
    assert JWT_SECRET != None
    return jwt_and_secret_to_user(token, JWT_SECRET)


def jwt_cookie_to_user():
    flexess_jwt = flask.request.cookies.get(JWT_COOKIE_NAME)
    if flexess_jwt == None:
        return None
    else:
        return jwt_to_user(flexess_jwt)


def user_and_secret_to_jwt(user, secret):
    jwt_payload = {'sub': user['userid'], 'name': user['name'] }
    encoded = jwt.encode(jwt_payload, secret, algorithm='HS256')
    return encoded


def jwt_and_secret_to_user(token, secret):
    decoded = jwt.decode(token, secret, algorithms=['HS256']);
    user = {}
    user['userid'] = decoded['sub']
    user['name'] = decoded['name']
    return user


