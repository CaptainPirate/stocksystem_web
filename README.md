# StockSystem Server
股票拟购的后台服务源码
<br/>
前端项目源码见 https://github.com/zackzhou915/stocksystem

<br />
架构基于SpringMvc，持久层使用MyBatis作为ORM框架。
<br/>
基于拦截器实现了access_token免登陆机制。
<br/>
推送服务使用 [极光推送](https://www.jiguang.cn) 

####References
*SpringMvc* http://spring.oschina.mopaas.com/
<br/>
*MyBatis* http://www.mybatis.org/mybatis-3/zh/java-api.html
<br/>
*DBCP* http://commons.apache.org/proper/commons-dbcp/configuration.html

####License

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
