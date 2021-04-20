# sls-mall


## 项目介绍

`sls`是一套微服务商城系统，采用了RestTemplate，redis，rabbit等基础技术 

## 系统架构图

![系统架构图](https://victor1.oss-cn-hangzhou.aliyuncs.com/mall/mall.png)

## 组织结构

``` lua
sls-mall
├── front-end -- 前端页面
├── user -- 用户登陆注册
├── payment -- 支付模块
├── carts-master -- 购物车模块
├── anti-cheating -- 积分反作弊
├── catalogue -- 商品图片模块
├── orders-master -- 订单模块
├── integral-master -- 积分模块
├── shipping-master -- 快递模块（伪模块）
└── queue-master-master -- 消费消息
```
