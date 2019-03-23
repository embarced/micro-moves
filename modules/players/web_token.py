import jwt
import flask
import os


JWT_COOKIE_NAME = os.environ.get('JWT_COOKIE_NAME')
JWT_SECRET = os.environ.get('JWT_SECRET')


def user_to_jwt(user):
    """ Converts a user object to a token.
    The secret is used from an environment variable. """

    assert JWT_SECRET is not None
    return user_and_secret_to_jwt(user, JWT_SECRET)


def jwt_to_user(token):
    """ Converts a JWT token to a user object.
    The secret is used from an environment variable. """

    assert JWT_SECRET is not None
    return jwt_and_secret_to_user(token, JWT_SECRET)


def jwt_cookie_to_user():
    """ Converts a JWT token from a cookie value to a user object.
    The secret and the cookie name are used from an environment variable. """

    flexess_jwt = flask.request.cookies.get(JWT_COOKIE_NAME)
    if flexess_jwt is None:
        return None
    else:
        return jwt_to_user(flexess_jwt)


def user_and_secret_to_jwt(user, secret):
    """ Converts a user object and a secret to a token. """

    jwt_payload = {'sub': user['userid'], 'name': user['name'], 'roles': user['roles'] }
    encoded = jwt.encode(jwt_payload, secret, algorithm='HS256')
    return encoded


def jwt_and_secret_to_user(token, secret):
    """ Converts a JWT token and a secret to a user object. """

    decoded = jwt.decode(token, secret, algorithms=['HS256']);
    user = {}
    user['userid'] = decoded['sub']
    user['name'] = decoded['name']
    user['roles'] = decoded['roles']

    return user


