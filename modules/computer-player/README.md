# computer-player
Integrates the open source chess engine Stockfish via AMQP / RabbitMQ.

## Technologies used
* Stockfish
* Python
* Python-Libraries: pika
* Tests with pytest and tox

## Running the tests
* pip install tox
* tox

## Starting the service standalone
* pip install -r requirements.txt
* python stockfish_listener.py

## Resources
* pika - a pure-Python implementation of the AMQP, https://pika.readthedocs.io
* Stockfish - Homepage, https://stockfishchess.org
* tox - automate and standardize testing in Python, https://tox.readthedocs.io/en/latest/
