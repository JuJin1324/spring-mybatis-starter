docker build --tag move-saas/mysql-local:1.0 .
docker run -d \
-p 3909:3909 \
--name mybatis-starter-mysql \
mybatis-starter/mysql-local:1.0
