## 接口限流
SpringBoot基于RateLimiter+AOP动态的为不同接口限流
### 一. 接口限流算法[^1]
[^1]:[SpringBoot基于RateLimiter+AOP动态的为不同接口限流](https://blog.csdn.net/qq_39816039/article/details/83988517)
- 网关层:
   1. Nginx 限流
- 服务接口层:
   1. 计数器算法
   2. 令牌桶
   3. 漏桶
  
