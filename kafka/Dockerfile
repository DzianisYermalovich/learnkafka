FROM eclipse-temurin:17.0.7_7-jre-focal

WORKDIR /tmp
RUN wget https://downloads.apache.org/kafka/3.4.0/kafka_2.13-3.4.0.tgz \
    && tar -xvf kafka_2.13-3.4.0.tgz \
    && mv kafka_2.13-3.4.0 /opt/kafka \
    && rm kafka_2.13-3.4.0.tgz

WORKDIR /opt/kafka
RUN adduser kafka && adduser kafka kafka \
    && chown kafka:kafka .
USER kafka:kafka

COPY entrypoint.sh .
ENTRYPOINT ["bash", "entrypoint.sh"]
