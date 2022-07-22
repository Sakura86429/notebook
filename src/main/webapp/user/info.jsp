<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon-edit"></span>个人中心</div>
        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <div class="col-md-8">
                    <form class="form-horizontal" method="post" action="user" enctype="multipart/form-data" >
                        <div class="form-group">
                            <%--设置隐藏域存放用户行为，name="actionName"前后保持一致--%>
                            <input type="hidden" name="actionName" value="updateUser">
                            <label for="nickName" class="col-sm-2 control-label">昵称:</label>
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
                                <button type="submit" id="btn" class="btn btn-success" onclick="return updateUser()">修改</button>
                                <span style="color: red;font-size: 12px;" id="msg"></span>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-4"><img style="width: 240px;height: 180px" src="user?actionName=userHead&imageName=${user.head}"></div>
            </div>
        </div>
    </div>
</div>

<script>
    $("#nickName").blur(function () {
        //1.使用id选择器获取昵称文本框的值
        var nickName = $("#nickName").val();
        //引入util.js使用它的isEmpty方法
        //2.判断值是否为空
        if(isEmpty(nickName)){
            //设置span字体为红色，.html说用户名称不能为空
            $("#msg").html("用户昵称不能为空！")
            //.prop、disabled表示禁用按钮
            $("#btn").prop('disabled', true);
            return;
        }
        //3.判断昵称是否做了修改
        //使用EL表达式从session作用域中获取用户昵称
        var nick = '${user.nick}';
        //如果用户昵称与session中的一致，就return
        if(nickName == nick){
            return;
        }
        //4.如果昵称做了修改
        //发送ajax请求后台，验证昵称是否可用
        $.ajax({
            type:"get",
            url:"user",
            data:{
                //传给后台actionName值为checkNick
                actionName:"checkNick",
                nick:nickName
            },
            success:function (code) {
                //如果可用，清空提示信息，按钮可用
                if(code==1){
                    //1.清空提示信息
                    $("#msg").html("")
                    //2.按钮可用
                    $("#btn").prop('disabled', false);
                }//如果不可用，提示用户，并禁用按钮
                else{
                    $("#msg").html("该昵称已存在！")
                    $("#btn").prop('disabled', true);
                }
            }
        });
        //聚焦清空提示信息+按钮可用
    }).focus(function () {
        $("#msg").html("")
        $("#btn").prop('disabled', false);
    });

    /**
     *表单提交的校验（因为昵称是必填项，只校验昵称，其他两个是非必填就不校验了）
     *     满足条件，返回true，表示提交表单
     *     不满足条件，返回false，表示不提交表单
     * @returns {boolean}
     */
    function updateUser() {
        var nickName = $("#nickName").val();
        if(isEmpty(nickName)){
            $("#msg").html("用户昵称不能为空！")
            $("#btn").prop('disabled', true);
            return false;
        }

        //可扩充
        //唯一性 TO DO
        return true;
    }
</script>