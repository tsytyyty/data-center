## 平台介绍

该平台是一套基于国产化核心中间件的数据中台，采用SpringCloud微服务架构。

于2023年8月获得中国软件杯国二。

- 前端采用Vue、Element UI。
- 后端采用SpringCloud、SpringBoot、Mybatis-plus + jdbc框架
- jdk：1.8
- 数据库：达梦8
- 网关：SpringCloud GateWay
- 注册中心：nacos1.4.1
- web容器：TongWeb（替代Tomcat）
- 分布式缓存：TongRDS（替代Redis）
- 远程调用：OpenFeign
- 可视化平台：达梦启智大数据可视化系统



## 演示视频

[第十二届中国软件杯国二——基于全信创环境下的物流信息数据可视化中台_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1mGTveaE2x/?spm_id_from=333.999.0.0&vd_source=b1b5c022061b0115270885e6bb98e9a4)



## 功能介绍

- 数据采集：多种数据源（mysql、minio）
- 数据清洗：过滤不合规数据
- 数据分析：吞吐量、出口量、入口量、周转时间数据聚合归纳
- 数据预测：港口货物吞吐量预测
- 数据可视化：报表展示



## 技术分析

平台设计多采用多线程异步提高效率，运用单例、工厂模式，

- 数据源连接池缓存多数据源连接对象，提高效率
- 多线程数据采集，异步发送
- 工厂模式管理多数据源（mysql、minio），提高可扩展性
- Redission分布式锁，保证数据安全
- JDBC设计SQL工作台，配合单例连接池，提高并发量



