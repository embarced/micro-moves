FROM python:3.6

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY players.py web_token.py ./
COPY templates ./templates/

CMD ["gunicorn", "--bind", "0.0.0.0:8000", "--workers", "4", "players:app"]
