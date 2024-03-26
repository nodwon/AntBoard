FROM redis:6.2.7-alpine

ENV REDIS_PASSWORD 1234

RUN echo "maxmemory  128mb" >> /etc/redis.conf
RUN echo "maxclients 100" >> /etc/redis.conf
RUN echo "requirepass $REDIS_PASSWORD" >> /etc/redis.conf

CMD ["redis-server", "/etc/redis.conf"]
LABEL name=friendly_napier

