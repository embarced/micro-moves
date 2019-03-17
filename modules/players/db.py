import tinydb

database = tinydb.TinyDB('users.json')


def all_users():
    return database.all()


def user_by_userid(userid):
    user_query = tinydb.Query()
    result = database.search(user_query.userid == userid)
    return result


def create(user):
    database.insert(user)
