# Pandanus(露兜树)-JCR-API

```java
JCR微服务
```

```java
//创建mongodb服务端
docker run --name mongodb \
  -v J:/spmia2/docker/mongodb:/bitnami/mongodb \
  -e MONGODB_ROOT_PASSWORD=password123 \
  -e MONGODB_USERNAME=my_user \
  -e MONGODB_PASSWORD=password123 \
  -e MONGODB_DATABASE=my_database -d -p 27017:27017 bitnami/mongodb:latest
```