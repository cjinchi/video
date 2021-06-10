### 运行流程

1. 启动MinIo对象存储服务

   ```shell
   docker run -p 9000:9000 --name minio -e "MINIO_ACCESS_KEY=admin" -e "MINIO_SECRET_KEY=12345678" -v /mnt/minio/data:/data -v /mnt/minio/config:/root/.minio minio/minio server /data
   ```

   （请注意该命令会将相关数据挂载到/mnt/minio上）

2. 启动RabbitMQ服务

   ```shell
   docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```

3. 分别修改三个子系统的`application.properties`文件，尤其是其中的IP地址、bucket名称等相关信息。

4. 分别在`upload`、`encode`和`download`目录下执行`mvn spring-boot:run`，`8080`端口、`8081`端口分别提供视频上传、下载服务。