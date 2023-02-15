# Pandanus(露兜树)-API-GATEWAY

```java
网关微服务
```

```java
//创建Consul
docker run --name consul-node1 --network consul-network -v J:\spmia2\consul-persistence:/bitnami -d -p 8500:8500 bitnami/consul:latest

//创建nginx容器
docker run --name test-nginx \
  -v /usr/shunyi/nginx/conf/my_server_block.conf:/opt/bitnami/nginx/conf/server_blocks/my_server_block.conf:ro \
  -v /usr/shunyi/nginx/conf/nginx-persistence/certs:/certs \
  -d -p 8080:8080 \
  bitnami/nginx:latest

//创建keycloak
docker run --name keycloak \
-v /usr/shunyi/keycloak/init-scripts:/docker-entrypoint-initdb.d \
-e KEYCLOAK_ADMIN_USER=user \
-e KEYCLOAK_ADMIN_PASSWORD=bitnami \
-e KEYCLOAK_MANAGEMENT_USER=manager \
-e KEYCLOAK_MANAGEMENT_PASSWORD=bitnami1 \
-e KEYCLOAK_DATABASE_HOST=10.0.16.10 \
-e KEYCLOAK_DATABASE_PORT=5432 \
-e KEYCLOAK_DATABASE_NAME=bitnami_keycloak \
-e KEYCLOAK_DATABASE_USER=bitnami_keycloak \
-e KEYCLOAK_DATABASE_PASSWORD=bitnami_keycloak \
-d -p 8888:8080 bitnami/keycloak:latest

问题：keycloak HTTPS required
解决：第1步.update REALM set ssl_required='NONE' where id = 'master';
     第2步.容器重启

//创建postgresDB
docker run --name keycloak-db \
    -v /usr/shunyi/keycloak/postgresql-persistence:/bitnami/postgresql \
    -e POSTGRESQL_DATABASE=bitnami_keycloak \
    -e POSTGRESQL_USERNAME=bitnami_keycloak \
    -e POSTGRESQL_PASSWORD=bitnami_keycloak \
    -d -p 5432:5432 bitnami/postgresql:latest
//解决办法
https://stackoverflow.com/questions/63924161/postgresql-container-not-starting-chmod-changing-permissions-of-bitnami-post
//创建consul
docker network create consul-network --driver bridge
docker run --name consul-node1 --network consul-network -v J:\spmia2\docker\consul-persistence:/bitnami -d -p 8500:8500 bitnami/consul:latest

//退出登陆endpoint:
http://101.43.164.174:8888/auth/realms/spring-boot-quickstart/protocol/openid-connect/logout?redirect_uri=http://localhost:9090/

```

### Linux

```java
清理内存：
echo 3 >/proc/sys/vm/drop_caches
```

### 参考资料:

```java
https://hub.docker.com/r/bitnami/keycloak
https://hub.docker.com/r/bitnami/postgresql
https://www.keycloak.org/documentation
https://refactorfirst.com/spring-cloud-gateway-keycloak-oauth2-openid-connect
https://github.com/amrutprabhu/keycloak-spring-cloud-gateway-and-resource-server/blob/main/keycloak-config/My-Realm-realm.json
https://datmt.com/backend/integrate-keycloak-with-spring-boot-step-by-step/#Quick_Keycloak_setup_with_docker_compose
https://blog.logrocket.com/implement-keycloak-authentication-react/
https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
```