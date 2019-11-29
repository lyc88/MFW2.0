# MFW2.0
基于SpringBoot2.2的权限系统
架构MFW全名My Framework

这是基于SpringBoot+Element-UI的架构。
其中集成了如下技术
    后端涉及到技术如下：
        SpringBoot2.2.0
        Spring JPA
        java-jwt(用于acessToken的生成和校验)
        kaptcha2.3.2（校验码生成）
        Redis(用于存储系统参数，一些重要缓存数据)
        poi(用于导出excel)
        oshi-core(用于查询系统信息)
        基于springBoot2的websocket（扫码登录和聊天室）
    
    前端设计到的技术
        Element-ui 2.11.0
        vue2.0
        tinymce5.0.14（富文本编辑）
        echarts(表格展示工具)
        vue-count-to(计数小工具)
        vue-clock2(时钟)
        vue-baidu-map(百度地图组件)


过滤器
    其中准备了两个过滤器
    一个是登录过滤器（用于页面功能的处理）
    一个是接口过滤器（用于对外接口功能的处理）

功能说明
登录注册以及系统菜单页
    注册 
    
    登录
        密码登录----输入登录名和密码登录
        扫码登录----扫描二维码然后调用接口登录，由于当前没有手机客户端所以需要调用接口登录。
                    地址：http://www.zhm.com:9090/openApi/interface/qrcodelogin?uid=25f67054-6886-4a15-a35a-e800490be958
                      其中header中需要access_token ，access_token是通过jwt生成。在OpenController的获取acess_token接口里。
                      
    
    忘记密码----通过邮箱来修改
    
    修改密码
    
    
    系统菜单-----登录成功后，会显示该用户有那些系统可以操作
    
        
    菜单界面
        权限系统设置界面-----构成了用户可以处理那些系统和菜单
             系统用户管理
             系统菜单管理
             系统角色管理
             系统部门管理
        系统参数管理
             系统参数------一些系统参数管理
             国家地址信息管理-----省市县（区）镇村(其中可以根据百度地图跳转到百度地图定位)
              对于百度地图的特别说明，一定要选择
            
              应用类型： 浏览器端
              启用服务：Javascript API（这个一定要选）
              Referer白名单：*（当我们本地测试时候一定要这样，否则会出错就是【百度未授权使用地图API，
              可能是因为您提供的密钥不是有效的百度LBS开放平台密钥，或此密钥未对本应用的百度地图JavaScriptAPI授权】）
              
         日志管理
            接口日志--------谁调用了接口，调用了那些接口（开发中）
            操作日志--------记录操作人信息，以及操作参数
            
        组件---介绍一些组件信息。
           服务监控------用于监控当前系统的环境
           富文本编辑器----用于文本类型的发布
           小组件-----一些小功能演示

后续优化，涉及到工具类编写，统一工具类的编写。异常处理。
  controller层只负责数据的转换，（接受不准有Map这样比较宽泛的）
  service层涉及到数据的逻辑处理。（其中包括异常处理，在一些关键的地方打日志。其中显示关键的数据）
 
  
