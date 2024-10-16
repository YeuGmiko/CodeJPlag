<h1>README</h1>

**启动项目**

```bash
npm install
```

```bash
npm run dev
```

**打包项目**

```bash
npm run build
```

**部署对接**

> file path: src/api/index.ts

修改Axios实例对象的BaseUrl为后端部署服务器IP

```ts
export const axios = Axios.create({
    baseURL: 'http://8.138.14.75:8080',
    withCredentials: true
})
```

**前后端项目说明**

在Fronted、Backend目录下，都有README文件进行项目的单独说明