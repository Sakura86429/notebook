<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!--
保留第一行，将html中的页面复制到jsp中
拷贝所有静态资源（前端页面不用操心，我们只用写jsp和后端代码），修正目录地址（css目录和img目录）
举例：如果login.css目录不对，打开网页后会发现排版和样式没有加载进去
前端login.jsp用的是表单提交，如果没有表单则需要我们手动加一个表单
-->
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>云R记</title>
    <link href="statics/css/login.css" rel="stylesheet" type="text/css" />
    <script src="statics/js/jquery-1.11.3.js" type=text/javascript></script>
    <!--要调用isEmpty这个判断方法需要在jsp中引用（相当于util中是我们定义的一个类，把这个类在下一行引用一下，原理是差不多的）-->
    <script src="statics/js/util.js" type=text/javascript></script>
    <script src="statics/js/config.js" type=text/javascript></script>
</head>
<body>
<!--head-->
<div id="head">
    <div class="top">
        <div class="fl yahei18">开启移动办公新时代！</div>
    </div>
</div>

<!--login box-->
<div class="wrapper">
    <div id="box">
        <div class="loginbar">用户登录</div>
        <div id="tabcon">
            <div class="box show">
                <!--// copy进来的文件没有表单，手动加表单（按钮提交+校验）-->
                <!--// action提交到哪，假设后台有个叫user的；method请求方式登录用post；给个id方便提交-->
                <form action="user" method="post" id="loginForm">
                    <%-- actionName表示用户行为，通过这个参数后端可以在UserServlet中判断用户当前想要操作的功能 --%>
                    <!--// 隐藏域，value中如果是登陆的话给个1也是可以的-->
                    <input type="hidden" name="actionName" value="login"/>
                    <!--// 在提交表单的时候一定要有name属性，否则是拿不到值的。通常我们让它与我们的用户id一样-->
                    <input type="text" class="user yahei16" id="userName" name="userName" value="${resultInfo.result.uname}" /><br /><br />
                    <input type="password" class="pwd yahei16" id="userPwd" name="userPwd" value="${resultInfo.result.upwd}" /><br /><br />
                    <!--复选框，其默认值是1，作用：勾选了我们就能拿到value，不勾选就拿不到value值-->
                    <input name="rem" type="checkbox" value="1"  class="inputcheckbox"/> <label>记住我</label>&nbsp; &nbsp;
                    <!--
                    span如果没有成功提交个人信息就提醒用户
                    用el表达式来取后台的值{resultInfo.msg}，且在最前面设置不忽略el表达式isELIgnored="false"
                    -->

                    <span id="msg" style="color: #ff0000;font-size: 12px;">${resultInfo.msg}</span><br /><br />
                    <!--// 一个普通按钮，会调用方法checkLogin()，所以我们要准备checkLogin()方法让它绑定事件
                    // 也就是说，我们点击按钮的时候会拿到上面这些值，如果不为空就提交表单，如果为空就提醒它-->
                    <input type="button" class="log jc yahei16" value="登 录" onclick="checkLogin()" />&nbsp; &nbsp; &nbsp; <input type="reset" value="取 消" class="reg jc yahei18" />
                    <!--// 用<>把表单元素给括起来

                    // 最后这个checkLogin()函数写在config.js中！-->
                </form>
            </div>
        </div>
    </div>
</div>

<div id="flash">
    <div class="pos">
        <a bgUrl="statics/images/banner-bg1.jpg" id="flash1" style="display:block;"><img src="statics/images/banner_pic1.png"></a>
        <a bgUrl="statics/images/banner-bg2.jpg" id="flash2"                       ><img src="statics/images/banner-pic2.jpg"></a>
    </div>
    <div class="flash_bar">
        <div class="dq" id="f1" onclick="changeflash(1)"></div>
        <div class="no" id="f2" onclick="changeflash(2)"></div>
    </div>
</div>

<!--bottom-->
<div id="bottom">
    <div id="copyright">
        <div class="quick">
            <ul>
                <li><input type="button" class="quickbd iphone" onclick="location.href='http://lezijie.com'" /></li>
                <li><input type="button" class="quickbd android" onclick="location.href='http://lezijie.com'" /></li>
                <li><input type="button" class="quickbd pc" onclick="location.href='http://lezijie.com'" /></li>
                <div class="clr"></div>
            </ul>
            <div class="clr"></div>
        </div>
        <div class="text">
            Copyright © 2006-2026  <a href="http://www.shsxt.com">上海乐字节</a>  All Rights Reserved
        </div>
    </div>
</div>
</body>

</html>

