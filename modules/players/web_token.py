import jwt
import flask
import os

JWT_COOKIE_NAME = os.environ.get('JWT_COOKIE_NAME')
JWT_SECRET = os.environ.get('JWT_SECRET')


def user_to_jwt(user):
    jwt_payload = {'sub': user['userid'], 'name': user['name'] }
    encoded = jwt.encode(jwt_payload, JWT_SECRET, algorithm='HS256')
    return encoded


def jwt_cookie_to_user():
    flexess_jwt = flask.request.cookies.get(JWT_COOKIE_NAME)
    if flexess_jwt == None:
        return None
    else:
        decoded = jwt.decode(flexess_jwt, JWT_SECRET, algorithms=['HS256']);
        user = {}
        user['userid'] = decoded['sub']
        user['name'] = decoded['name']
        return user
