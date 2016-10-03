FROM i686/ubuntu
RUN apt-get update -q && apt-get install redis-server -y
CMD redis-server
EXPOSE 6379
