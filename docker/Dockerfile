FROM mysql:8.0.31
LABEL maintainer="jujin1324@daum.net"
LABEL dockerhub="https://hub.docker.com/_/mysql"

EXPOSE 3909

ADD mybatis-starter.cnf /etc/mysql/conf.d/
ENV MYSQL_ROOT_PASSWORD root
ENV MYSQL_DATABASE mybatis_starter
ENV MYSQL_USER mybatis_starter
ENV MYSQL_PASSWORD mybatis_starter
