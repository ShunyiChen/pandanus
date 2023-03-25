import { Prisma, PrismaClient } from '@prisma/client'
import express from 'express'
const Eureka = require('eureka-js-client').Eureka;
const prisma = new PrismaClient()
const app = express()

const session = require('express-session');
const Keycloak = require('keycloak-connect');

const memoryStore = new session.MemoryStore();
app.use(session({
  secret: 'some secret',
  resave: false,
  saveUninitialized: true,
  store: memoryStore
}));
const kcConfig = {
    clientId: 'app-authz-rest-springboot',
    enabled: true,
    bearerOnly: true,
    realm: 'spring-boot-quickstart',
    authServerUrl: 'http://101.43.164.174:8888/auth',
    sslRequired: 'none',
    resource: 'service-nodejs'
};
//     clientId: 'app-authz-rest-springboot',
//     bearerOnly: true,
//     serverUrl: 'http://localhost:3000',
//     realm: 'spring-boot-quickstart',
//     realmPublicKey: 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAurPWIZI3Lc9nFl/ToowpUpmZLstYJ7l09+fpJ0rmQMHEI6jKpTYtbcD3YQum1tG+2JDmhuUfnh5dmIWYx0tn2SZwnSClzXu+QHRYtwB5P5Kbg2PXEc92yRWMOQHj+8Woss4PGSo8IYlq+3kQKrvDefQDD1+a9/tbc6IOHxZ/uDSnN/hodwGVD39zstr/OgOE28HI6qhYrG320biD7GvAVpAoLvet4Jnn7XRn3ovI0GTxwo5UhsQoyeXHep6dHKEmfXtXTCnkmngOdbqrUqIcmlR8/+bQ/nHSiShNPWmuY1jFa33VcWgSdjjgnZU/Zh52Mo8f/9F99UDa3USEXRLk9wIDAQAB'

const keycloak = new Keycloak({ store: memoryStore }, kcConfig);

app.use(express.json())
app.set( 'trust proxy', true );
app.use( keycloak.middleware() );

// app.post(`/signup`, async (req, res) => {
//   const { name, email, posts } = req.body
//
//   const postData = posts?.map((post: Prisma.PostCreateInput) => {
//     return { title: post?.title, content: post?.content }
//   })
//
//   const result = await prisma.user.create({
//     data: {
//       name,
//       email,
//       posts: {
//         create: postData,
//       },
//     },
//   })
//   res.json(result)
// })
//
// app.post(`/post`, async (req, res) => {
//   const { title, content, authorEmail } = req.body
//   const result = await prisma.post.create({
//     data: {
//       title,
//       content,
//       author: { connect: { email: authorEmail } },
//     },
//   })
//   res.json(result)
// })
//
// app.put('/post/:id/views', async (req, res) => {
//   const { id } = req.params
//
//   try {
//     const post = await prisma.post.update({
//       where: { id: Number(id) },
//       data: {
//         viewCount: {
//           increment: 1,
//         },
//       },
//     })
//
//     res.json(post)
//   } catch (error) {
//     res.json({ error: `Post with ID ${id} does not exist in the database` })
//   }
// })
//
// app.put('/publish/:id', async (req, res) => {
//   const { id } = req.params
//
//   try {
//     const postData = await prisma.post.findUnique({
//       where: { id: Number(id) },
//       select: {
//         published: true,
//       },
//     })
//
//     const updatedPost = await prisma.post.update({
//       where: { id: Number(id) || undefined },
//       data: { published: !postData?.published },
//     })
//     res.json(updatedPost)
//   } catch (error) {
//     res.json({ error: `Post with ID ${id} does not exist in the database` })
//   }
// })
//
// app.delete(`/post/:id`, async (req, res) => {
//   const { id } = req.params
//   const post = await prisma.post.delete({
//     where: {
//       id: Number(id),
//     },
//   })
//   res.json(post)
// })
//
// app.get('/users', async (req, res) => {
//   const users = await prisma.user.findMany()
//   res.json(users)
// })
//
// app.get('/user/:id/drafts', async (req, res) => {
//   const { id } = req.params
//
//   const drafts = await prisma.user
//     .findUnique({
//       where: {
//         id: Number(id),
//       },
//     })
//     .posts({
//       where: { published: false },
//     })
//
//   res.json(drafts)
// })
//
// app.get(`/post/:id`, async (req, res) => {
//   const { id }: { id?: string } = req.params
//
//   const post = await prisma.post.findUnique({
//     where: { id: Number(id) },
//   })
//   res.json(post)
// })


// #########################################
// console.log('------keycloak', keycloak)
// 单个角色授权
// keycloak.protect( 'realm:admin' )

// 多个角色授权
function pants(token:any, request:object) {
   return token.hasRole( 'realm:message_read11') || token.hasRole( 'realm:user');
}

app.get('/feed', keycloak.protect(pants), async (req, res) => {
  // console.log('token = ', req.header("Authorization"))
  
  const { searchString, skip, take, orderBy } = req.query
  const or: Prisma.PostWhereInput = searchString
    ? {
        OR: [
          { title: { contains: searchString as string } },
          { content: { contains: searchString as string } },
        ],
      }
    : {}

  const posts = await prisma.post.findMany({
    where: {
      published: true,
      ...or,
    },
    include: { author: true },
    take: Number(take) || undefined,
    skip: Number(skip) || undefined,
    orderBy: {
      updatedAt: orderBy as Prisma.SortOrder,
    },
  })

  res.json(posts)
})


// Create an endpoint for checking if the instance was alive at Eureka end
app.get('/info', async (req, res) => {
  res.json('alive')
})

// #########################################
// Register it to Eureka
const client = new Eureka({
  filename: 'eureka-client',
  cwd: `${__dirname}/config`,
})

// Using debug level
// client.logger.level('debug');

// Start to register
client.start((err: Error) => {
    console.log(err || 'Registered successfully')
})

const server = app.listen(3000, () =>
  console.log(`
🚀 Server ready at: http://localhost:3000
⭐️ See sample requests: http://pris.ly/e/ts/rest-express#3-using-the-rest-api`),
)
