# Pandanus

### Keycloak

```java
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