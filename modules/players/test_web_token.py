import web_token


def test_both_directions_with_secret():

    userOrig = {
        'userid': 'mmuster',
        'name': 'Monika Mustermann'
    }
    secret = 'dsd6sjadRRg'

    token = web_token.user_and_secret_to_jwt(userOrig, secret)
    userNew = web_token.jwt_and_secret_to_user(token, secret)

    assert len(userOrig.keys()) == 2
    assert userOrig['userid'] == userNew['userid']
    assert userOrig['name'] == userNew['name']


def test_both_directions_with_env():

    userOrig = {
        'userid': 'ppanther',
        'name': 'Paul Panther'
    }

    token = web_token.user_to_jwt(userOrig)
    userNew = web_token.jwt_to_user(token)

    assert len(userOrig.keys()) == 2
    assert userOrig['userid'] == userNew['userid']
    assert userOrig['name'] == userNew['name']

