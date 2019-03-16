import tinydb

db = tinydb.TinyDB('users.json')
if len(db) == 0:
    db.insert({"userid": 'stefanz', "name": 'Stefan Zörner'})
    db.insert({"userid": 'peter', "name": 'Peter Yarrow'})
    db.insert({"userid": 'paul', "name": 'Noel Paul Stookey'})
    db.insert({"userid": 'mary', "name": 'Mary Travers'})
    db.insert({"userid": 'pinky', "name": 'Pinky'})
    db.insert({"userid": 'brain', "name": 'The Brain'})
    db.insert({"userid": 'stockfish', "name": 'Stockfish Engine'})


def all():
    return db.all()


def user_by_userid(userid):
    user_query = tinydb.Query()
    result = db.search(user_query.userid == userid)
    return result


def create(user):
    db.insert(user)
