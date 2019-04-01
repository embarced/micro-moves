import tinydb

database = tinydb.TinyDB('users.json')


def all_users():
    """ returns all users. """

    return database.all()


def user_by_userid(userid):
    """ Finds a user by its user id. Returns None, if it not exists. """

    user_query = tinydb.Query()
    user = None
    result = database.search(user_query.userid == userid)
    if len(result) == 1:
        user = result[0]

    return user


def create_user(user):
    """ Add a new user. """

    database.insert(user)
