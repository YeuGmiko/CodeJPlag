<h1>README</h1>

**项目启动**

```bash
mvn clean install
```
```bash
mvn spring-boot:run
```

**项目打包**

```bash
mvn clean package
```

**项目部署**

配置文件：Application.yml

> file path: src/main/resources/application.yml

```yml
custom:
#  前端项目BaseUrl, 多个用 ";" 进行分割
  fronted_base_url: http://your/fronted/base/url;http://localhost
#  用于该项目暂存文件的目录
  base-resource-path: path/to/your/absolute/resource_path
```

**注意事项**

若该项目在Windows部署，则在接口调用之后可能会遇到“文件正在被使用”的问题，这个时候只能通过重启该项目进行恢复。建议在Linux项目上进行部署使用。