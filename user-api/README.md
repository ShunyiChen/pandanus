# Pandanus(露兜树)-USER-API

```java
用户端微服务
```

### Implement a REST API with TypeScript using Express and Prisma Client.

```yaml
#1.Install pnpm dependencies:
cd \user-api\src\Services
pnpm install
#2.Create and seed the database
npx prisma migrate dev --name init
#3.Start the REST API server
pnpm dev
```

```javascript
Below is what dependencies we used in user-api module:
 "@prisma/client": "4.11.0",
 "body-parser": "^1.20.2",
 "eureka-js-client": "^4.5.0",
 "express": "4.18.2",
 "express-session": "^1.17.3",
 "keycloak-connect": "^21.0.1"
```