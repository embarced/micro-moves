FROM python:3.6

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY gunicorn_config.py ./
COPY chess_diagrams.py draw.py ./
COPY templates ./templates/
COPY images ./images/
COPY fonts ./fonts/

EXPOSE 8000
EXPOSE 8080

CMD ["gunicorn", "--config", "./gunicorn_config.py", "--bind", "0.0.0.0:8000", "--workers", "4", "chess_diagrams:app"]
