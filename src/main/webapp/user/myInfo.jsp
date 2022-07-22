<%--
  Created by IntelliJ IDEA.
  User: 01
  Date: 2021/12/22
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%--// 个人中心--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>


<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon-edit"></span> 个人中心 </div>
        <div class="container-fluid">
            <%--        <div class="row" style="background-color:#D1DDF1;color:#455165;">--%>
            <div class="row" style="">
                <%--                ！！！此处style应该是什么？没有发现，可能会影响和下一行的行距--%>
                <div class="col-md-8">
                    <%--                    表单类型  enctype="multipart/form-data"--%>
                    <%--                    提交方式  method="post"--%>
                    <%--                    <form class="form-horizontal" method="post" action="user?" enctype="multipart/form-data" onsubmit="return checkUser();">--%>
                    <form class="form-horizontal" method="post" action="user?" enctype="multipart/form-data">
                        <div class="form-group">
                            <%--                            设置隐藏域（前端form中的hidden）存放用户行为actionName，所以这里name设置为actionName前后保持一致--%>
                            <input type="hidden" name="actionName" value="updateUser">
                            <label for="nickName" class="col-sm-2 control-label">昵称:</label>
                            <%--                            不管在哪种屏幕上，栅格系统都会自动的每行row分12列 col-xs-*和col-sm-* 和col-md-*后面跟的参数表示在当前的屏幕中 每个div所占列数。
                                                            例如 <div class="col-xs-6 col-md-3"> 这个div在屏幕中占的位置是： .col-xs-6 在超小屏幕中占6列 也就是屏幕的一半（12/6列=2个div） ，
                                                            .col-md-3 在中单屏幕中占3列也就是1/4（12/3列=4个div）。--%>
                            <div class="col-sm-3">
                                <input class="form-control" name="nick" id="nickName" placeholder="昵称" value="${user.nick}">
                            </div>
                            <label for="img" class="col-sm-2 control-label">头像:</label>
                            <div class="col-sm-5">
                                <input type="file" id="img" name="img">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mood" class="col-sm-2 control-label">心情:</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" name="mood" id="mood" rows="3">${user.mood}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <%--                                <button type="submit" id="btn" class="btn btn-success">修改</button> <span style="background-color:#D1DDF1;color:#455165;"></span>--%>
                                <%--                                下面的按钮是submit按钮，意味着一点按钮表单就会被提交走；所以这里要用onclick事件进行校验,这里在下面准备了一个updateUser()函数--%>
                                <button type="submit" id="btn" class="btn btn-success" onclick="return updateUser();">修改</button>
                                <span style="color: red; font-size: 12px;" id="msg"></span>
                            </div>
                        </div>
                    </form>
                </div>
                <%--                <div class="col-md-4"><img style="width: 260px;height: 200px" src="https://avatar.csdnimg.cn/0/3/4/1_m0_46378271_1631243828.jpg"></div>--%>
                <div class="col-md-4"><img style="width: 240px;height: 180px" src="user?actionName=userHead&imageName=${user.head}"></div>
            </div>
        </div>
    </div>
</div>




<script type="text/javascript">
    /**
     * 验证昵称的唯一性
     * 前台：
     昵称文本框的失焦事件 blur
     1. 获取昵称文本框的值
     2. 判断值是否为空
     如果为空，提示用户，禁用按钮，并return
     3. 判断昵称是否做了修改
     从session作用域中获取用户昵称
     （如果在js中想要使用el表达式获取域对象，js需要写在JSP页面中，无法在js文件中获取）
     如果用户昵称与session中的昵称一致，则return
     4. 如果昵称做了修改
     发送ajax请求后台，验证昵称是否可用
     如果不可用，提示用户，并禁用按钮
     如果可用，清空提示信息，按钮可用
     昵称文本框的聚焦事件 focus
     1. 清空提示信息
     2. 按钮可用
     */
    // javascript中可以用//来注释，但是在jsp中非javascript外就不能了
    // id选择器
    $("#nickName").blur(function () {
        // 1. 使用id选择器获取昵称文本框的值
        var nickName = $("#nickName").val();
        // 2. 判断值是否为空，在index.jsp中引入util.js子模块，使用isEmpty函数
        // 选择nick的时候如果有提示，是因为有浏览器缓存，可以选择在设置中清掉就没有了
        if (isEmpty(nickName)) {
            // 如果为空，（设置提示信息红色）msg提示用户，禁用按钮(disable设置为true)，并return（不让其往下走了）
            // 此处的id选择器选中的是上面span和button的
            $("#msg").html("用户昵称不能为空！")
            $("#btn").prop("disable", true);
            return;
        }
        // 3. 判断昵称是否做了修改
        // 从session作用域中获取用户昵称
        // 使用EL表达式
        var nick = '${user.nick}';
        // 如果用户昵称与session中的昵称一致，则return
        if (nickName == nick) {
            return;
        }
        // 4. 如果昵称做了修改，发送ajax请求
        // 发送ajax请求后台（会传一个checkNick过来，然后也将对应填的一个昵称名nickName传过来；
        // 然后我们拿这个名字去数据库中查这个名字有没有被别人占用），验证昵称是否可用
        $.ajax({
            type:"get",
            url:"user", // 路径为user，肯定要传参
            data:{
                actionName:"checkNick", // 用户行为：验证昵称的唯一性（后台Servlet中的checkNick方法）
                nick:nickName // 会在servlet中传过去（nick值是文本框中的昵称）
            },
            // success:function (result) {
            success:function (code) {
                // 如果可用，清空提示信息，按钮可用(假设1就是可用)
                // 这个返回的是code而不是resultInfo对象，所以改为code==1
                if (code == 1) {
                    // 1. 清空提示信息
                    $("#msg").html("")
                    // 2. 按钮可用
                    $("#btn").prop("disabled", false);
                } else { // 如果不可用，提示用户，并禁用按钮
                    // 1. 提示信息
                    $("#msg").html("该昵称已存在，请重新输入！")
                    // 2. 按钮可用
                    $("#btn").prop("disabled", true);
                }

            }
        });

    }).focus(function () {
        // 1. 清空提示信息
        $("#msg").html("")
        // 2. 按钮可用
        $("#btn").prop("disabled", false);

    });

    /**
     * 表单提交校验
     *  满足条件，返回true，表示提交表单（但是return;即后面不加东西也会提交表单）
     *  不满足条件，返回false，表示不提交表单
     */
    function updateUser() {
        // 1. 获取昵称文本框的值
        var nickName = $("#nickName").val();
        // 2. 判断值是否为空，在index.jsp中引入util.js子模块，使用isEmpty函数
        if (isEmpty(nickName)) {
            // 如果为空，（设置提示信息红色）msg提示用户，禁用按钮(disable设置为true)，并return（不让其往下走了）
            // 注意此处下一行是不加逗号的，到最后一行（下一行才加逗号）
            $("#msg").html("用户昵称不能为空！")
            $("#btn").prop("disable", true);
            // 这里是return false不能是return，如果是return的话表单还是会提交走
            return false;
        }
        // 唯一性 TODO
        // 因为我们这里做了失去焦点的校验所以这里就不用写了

        return true;
    }
</script>
