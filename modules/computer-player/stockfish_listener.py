import pika
import json
import time
import os
import logging.config

import stockfish

STOCKFISH_EXECUTABLE = '/usr/games/stockfish'

RABBITMQ_HOSTNAME = os.environ.get('RABBITMQ_HOSTNAME')
RABBITMQ_USERNAME = os.environ.get('RABBITMQ_USERNAME')
RABBITMQ_PASSWORD = os.environ.get('RABBITMQ_PASSWORD')


# load the logging configuration
logging.config.fileConfig('logging.ini')
log = logging.getLogger(__name__)

def send_best_move(move):
    channel.basic_publish(exchange='',
                          routing_key='moves',
                          body=move,
                          properties=pika.BasicProperties(
                              content_type='text/plain',
                          )
                          )

def receive_message(ch, method, properties, body):

    log.debug("Received %r" % body)
    message = json.loads(body)

    fen = message['fen']
    gameId = message['gameId']

    best_move = stockfish.calculate_move(fen, STOCKFISH_EXECUTABLE)
    log.debug("Best move calculated by stockfish %r" % best_move)

    response = {'move': best_move, 'gameId': gameId}
    response_json = json.dumps(response)

    send_best_move(response_json)


credentials = pika.PlainCredentials(RABBITMQ_USERNAME, RABBITMQ_PASSWORD)
channel = None

while True:
    connectSucceeded = False
    try:
        log.info("Connect to %r" % RABBITMQ_HOSTNAME)
        connection = pika.BlockingConnection(pika.ConnectionParameters(RABBITMQ_HOSTNAME, credentials=credentials))
        channel = connection.channel()
        connectSucceeded = True
    except Exception as e:
        log.warning(e)
        time.sleep(5)
        pass
    if connectSucceeded:
        log.info("Connected.")
        break

channel.queue_declare(queue='positions')
channel.queue_declare(queue='moves')

channel.basic_consume(receive_message,
                      queue='positions',
                      no_ack=True)

log.info('Stockfish waiting for messages ...')
channel.start_consuming()
