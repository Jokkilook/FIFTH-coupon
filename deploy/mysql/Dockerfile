FROM mysql:latest
ENV MYSQL_DATABASE=user
ENV MYSQL_ROOT_PASSWORD=1234
COPY setup.sql /docker-entrypoint-initdb.d/
