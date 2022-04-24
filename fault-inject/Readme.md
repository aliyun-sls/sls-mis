# sls-mall集群内部异常注入实现
  通过在集群内机器以及容器镜像安装命令行工具，并执行脚本定期注入异常
## 使用到的命令行工具
- stress-ng
  - https://wiki.ubuntu.com/Kernel/Reference/stress-ng
- tc (alpine中 tc与另一个iproute2 整合成了一个工具iproute2-tc ,但实际使用的仍然是tc的相关功能)
  - https://cizixs.com/2017/10/23/tc-netem-for-terrible-network/
  - https://wiki.linuxfoundation.org/networking/iproute2#introduction

## 为了实现在pod层面的异常注入，需要对镜像进行修改。
### 修改后有以下的注意事项
- 所有修改后的容器被授予特权容器权限
- 所有修改后的容器的filesystem请不要设置readOnly权限
- 注入异常的pod所属的deployment进行了资源限制。注入异常时有小概率导致pod重启
- 注入异常的容器所属的deployment如果配置了健康检查，健康检查的周期可以适当调大一些，否则容器容易频繁重启
- 所有修改后的容器都在本目录下有对应的文件夹
  - 文件夹中的before.yaml 为对应deployment修改前的yaml文件
  
### 其他/备忘
- queue-master 是初期用来测试的容器，并没有实际往其中注入异常，镜像也没有替换
- 待补充