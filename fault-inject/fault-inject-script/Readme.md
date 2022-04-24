# 异常注入脚本
- 异常注入的信息存储在 logstore：
https://sls.console.aliyun.com/lognext/project/k8s-log-cde0358f8f8264072bcb8e96e374ea20b/logsearch/sls-mall-fault-injection-record
- 目前部署在集群的192.168.32.65 机器上。
  - /root/sls-mall-fault-inject.py 为异常注入脚本
  - /root/fault_injection_log.txt 为注入过程中出错时的日志
- 修改集群内机器的root密码可能会导致脚本在machine上的异常注入失效
- 由于目前脚本内包含敏感信息，暂时不上传

## TODO
- 将脚本中的用户名密码等设置为环境变量，由用户配置
- 将脚本封装为镜像，部署到集群，相关信息作为secret存储到集群中