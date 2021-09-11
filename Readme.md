# 标注工具的翻译程序
* 该小工具的目的：使用破解的google翻译工具，来对数据库中，
主要是俄文内容进行翻译，以方便之后的分类工作。

* 使用的框架：Spring-boot框架。

* 为了防止过多调用Google Translate 接口而被封掉IP，使用的是芝麻HTTP代理。
程序开始前，需要在./src/java/com.example.annotatedTranslation/ZhimaAPI.java
文件中的setIpParams()修改参数选项。
> 参数的修改需要登录“http://www.zhimaruanjian.com/”注册账号，领取每日的免费IP或者
> 实名注册领取一日一万可用IP。