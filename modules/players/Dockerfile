FROM python:3.6

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY users.json ./
COPY players.py web_token.py db.py ./
COPY templates ./templates/

EXPOSE 8000
CMD ["gunicorn", "--bind", "0.0.0.0:8000", "--workers", "4", "players:app"]
