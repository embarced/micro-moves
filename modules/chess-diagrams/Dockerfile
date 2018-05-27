FROM python:3.6

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY chess_diagrams.py draw.py ./
COPY templates ./templates/
COPY images ./images/
COPY fonts ./fonts/

CMD ["gunicorn", "--bind", "0.0.0.0:8000", "--workers", "4", "chess_diagrams:app"]
