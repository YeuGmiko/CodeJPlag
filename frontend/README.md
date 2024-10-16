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

修改Axios实例对象的BaseUrl为部署服务器IP

```ts
export const axios = Axios.create({
    baseURL: 'http://8.138.14.75:8080',
    withCredentials: true
})
```