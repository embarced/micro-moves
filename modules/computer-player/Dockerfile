FROM ubuntu:14.04
FROM python:3.6

RUN apt-get update && apt-get install -y stockfish

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY logging.ini .
COPY stockfish.py .
COPY stockfish_listener.py .

CMD ["python", "stockfish_listener.py"]
