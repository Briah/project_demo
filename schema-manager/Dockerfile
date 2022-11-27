FROM python:3.7.3

RUN pip install --upgrade pip
RUN pip install cassandra-driver
COPY create.py /root/create.py
RUN chmod 777 /root/create.py

COPY sql /sql

WORKDIR /root

ENTRYPOINT [ "python", "/root/create.py" ]
