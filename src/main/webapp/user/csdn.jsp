<%--
  Created by IntelliJ IDEA.
  User: 01
  Date: 2021/12/22
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
// 个人中心
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="user-profile-head-info" data-v-d1dbb6f8="">
    <div class="user-profile-head-info-t" data-v-d1dbb6f8="">
        <div class="user-profile-head-info-l" data-v-d1dbb6f8="">
            <div class="user-profile-head-info-ll" data-v-d1dbb6f8="">
                <div class="user-profile-avatar" data-v-d1dbb6f8="">img src="https://profile.csdnimg.cn/0/3/4/1_m0_46378271" alt="" data-v-d1dbb6f8="">
                    <a href="https://www.csdn.net/vip" target="_blank" class="vip-icon" data-v-d1dbb6f8="">
                        <!---->
                    </a>
                </div>
            </div>
            <div class="user-profile-head-info-rr" data-v-d1dbb6f8="">
                <div class="user-profile-head-info-r-t" data-v-d1dbb6f8="">
                    <div class="user-profile-head-name" data-v-d1dbb6f8="">
                        <div data-v-d1dbb6f8="" class="">m0_46378271</div>
                        <div title="已加入 CSDN 2年" class="person-code-age" style="background-color:#D1DDF1;color:#455165;" data-v-d1dbb6f8="">
                            <img src="https://img-home.csdnimg.cn/images/20210108035944.gif" alt="" data-v-d1dbb6f8="">
                            <span data-v-d1dbb6f8="">码龄2年</span>
                        </div>


                        <div class="data_list">
                            <div class="data_list_title"><span class="glyphicon glyphicon-edit"></span> 个人中心 </div>
                            <div class="container-fluid">
                                <div class="row" style="background-color:#D1DDF1;color:#455165;">
                                    <div class="col-md-8">
                                        <form class="form-horizontal" method="post" action="user?act=save" enctype="multipart/form-data" onsubmit="return checkUser();">
                                            <div class="form-group">
                                                <input type="hidden" name="act" value="save">
                                                <label for="nickName" class="col-sm-2 control-label">昵称</label>
                                                <div class="col-sm-3">
                                                    <input class="form-control" name="nick" id="nickName" placeholder="昵称" value="我思故我在">
                                                </div>
                                                <label for="img" class="col-sm-2 control-label">头像：</label>
                                                <div class="col-sm-5">
                                                    <input type="file" id="img" name="img">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="mood" class="col-sm-2 control-label">心情:</label>
                                                <div class="col-sm-10">
                                                    <textarea class="form-control" name="mood" id="mood" rows="3">以后的你会感谢现在努力的你</textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <button type="submit" id="btn" class="btn btn-success">修改</button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-md-4"><img style="width: 260px;height: 200px" src="https://avatar.csdnimg.cn/0/3/4/1_m0_46378271_1631243828.jpg"></div>
                                </div>
                            </div>
                        </div>



                        <div class="user-profile-icon" data-v-d1dbb6f8="">
                            <a href="https://blog.csdn.net/blogdevteam/article/details/103478461" target="_blank" data-v-d1dbb6f8="">
                                <img src="https://csdnimg.cn/identity/blog2.png" alt="" title="博客等级" data-v-d1dbb6f8="">
                            </a>
                            <%--                            ！！！jsp中Ctrl+/也可以注释掉一行--%>
                            <%--                            <input type="text" class="user yahei16" id="userName" placeholder="昵称" name="userName" value="${resultInfo.result.uname}" />--%>
                            <input type="text" class="user yahei16" id="userName" placeholder="昵称" value="${resultInfo.result.uname}" />
                            <a href="https://i.csdn.net/#/user-center/auth" target="_blank" data-v-d1dbb6f8="">
                                <!---->
                            </a>
                            <a href="https://i.csdn.net/#/user-center/auth" target="_blank" data-v-d1dbb6f8="">
                                <!---->
                            </a>
                            <i class="user-gender-male" data-v-d1dbb6f8="">

                            </i>
                        </div>
                    </div>
                </div>
                <div class="user-profile-head-info-r-c" data-v-d1dbb6f8="">
                    <ul data-v-d1dbb6f8=""><li data-v-d1dbb6f8="">
                        <div class="user-profile-statistics-num" data-v-d1dbb6f8="">5,513</div>
                        <div class="user-profile-statistics-name" data-v-d1dbb6f8="">被访问</div>
                        <div class="user-profile-statistics-bar" data-v-d1dbb6f8=""></div>
                    </li>
                        <li data-v-d1dbb6f8="">
                            <a href="javascript:;" data-v-d1dbb6f8="">
                                <div class="user-profile-statistics-num" data-v-d1dbb6f8="">18</div>
                                <div class="user-profile-statistics-name" data-v-d1dbb6f8="">原创</div>
                                <div class="user-profile-statistics-bar" data-v-d1dbb6f8=""></div>
                            </a>
                        </li>
                        <li data-v-d1dbb6f8="">
                            <a href="https://blog.csdn.net/rank/list/total" target="_blank" data-report-click="{&quot;spm&quot;:&quot;3001.5476&quot;}" data-report-query="spm=3001.5476" data-v-d1dbb6f8="">
                                <div class="user-profile-statistics-num" data-v-d1dbb6f8="">91,459</div>
                                <div class="user-profile-statistics-name" data-v-d1dbb6f8="">排名</div>
                                <div class="user-profile-statistics-bar" data-v-d1dbb6f8=""></div>
                            </a>
                        </li>
                        <li data-v-d1dbb6f8="">
                            <a href="javascript:;" data-v-d1dbb6f8="">
                                <div class="user-profile-statistics-num" data-v-d1dbb6f8="">5</div>
                                <div class="user-profile-statistics-name" data-v-d1dbb6f8="">粉丝</div>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="user-profile-head-info-r" data-v-d1dbb6f8="">
            <div class="user-profile-operate-btn" data-v-d1dbb6f8="">
                <span data-v-d1dbb6f8="">
                    <div role="tooltip" id="el-popover-1926" aria-hidden="true" class="el-popover el-popper White" style="width:348px;display:none;" tabindex="0">
                        <!---->
                        <div class="change-version-popover" data-v-d1dbb6f8="">
                            <div class="change-version-popover-content" data-v-d1dbb6f8="">
                                <div class="change-version-popover-t" data-v-d1dbb6f8="">您即将切换至旧版个人主页</div>
                                <div class="change-version-popover-des" data-v-d1dbb6f8="">
                                    <ul data-v-d1dbb6f8="">
                                        <li data-v-d1dbb6f8="">1. 新版持续迭代中，用以满足大家的需求和体验；</li>
                                        <li data-v-d1dbb6f8="">2. 若对新版有想法或建议，欢迎随时进行反馈！</li>
                                    </ul>
                                </div>
                            </div> <!---->
                        </div>
                    </div>
                    <span class="el-popover__reference-wrapper">
                        <a href="javascript:;" data-report-click="{&quot;spm&quot;:&quot;3001.5706&quot;}" class="user-profile-black-btn el-popover__reference" data-v-d1dbb6f8="" aria-describedby="el-popover-1926" tabindex="0">切换旧版</a>
                    </span>
                </span>
                <a href="https://i.csdn.net/#/user-center/profile" target="_blank" data-report-click="{&quot;spm&quot;:&quot;3001.5516&quot;}" data-report-query="spm=3001.5516" class="user-profile-black-btn" data-v-d1dbb6f8="">编辑资料</a>
                <!---->
            </div>
        </div>
    </div>
    <div class="user-profile-head-info-b" data-v-d1dbb6f8="">
        <!---->
        <div class="user-profile-head-info-b-r" data-v-d1dbb6f8="">
            <div class="user-general-info" style="" data-v-d1dbb6f8="">
                <ul data-v-d1dbb6f8="">
                    <li class="user-general-info-edu" data-v-d1dbb6f8="">
                        <div class="user-general-info-left" data-v-d1dbb6f8="">
                            <span data-v-d1dbb6f8="">毕业院校：</span>
                            <span class="user-general-info-key-word" data-v-d1dbb6f8="">xx</span>
                            <!---->
                        </div>
                        <a href="https://i.csdn.net/#/user-center/setting" target="_blank" data-report-click="{&quot;spm&quot;:&quot;3001.5708&quot;}" data-report-query="spm=3001.5708" class="user-general-info-hide" data-v-d1dbb6f8="">隐藏</a>
                    </li> <!---->
                    <li class="user-general-info-join-csdn" data-v-d1dbb6f8="">
                        <span data-v-d1dbb6f8="">加入CSDN时间：</span>
                        <span class="user-general-info-key-word" data-v-d1dbb6f8="">2020-02-21</span>
                    </li>
                </ul>
                <div class="user-profile-wrapper" data-v-d1dbb6f8="">
                    <div class="user-profile-wrapper-box" data-v-d1dbb6f8="">
                        <span data-v-d1dbb6f8="">博客简介：</span>
                        <h1 class="user-profile-title" data-v-d1dbb6f8="">m0_46378271的博客</h1>
                    </div>
                    <!---->
                </div>
            </div>
            <span data-report-click="{&quot;spm&quot;:&quot;3001.6736&quot;}" class="show-more-introduction-fold" data-v-d1dbb6f8="">收起详细资料<i class="el-icon-arrow-up" data-v-d1dbb6f8=""></i>
            </span>
        </div>
    </div>
</div>

<%--<script type="text/javascript">
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
    // id选择器
    $("#nickName").blur(function () {
        // 1. 获取昵称文本框的值
        var nickName = $("#nickName").val();
        // 2. 判断值是否为空，在index.jsp中引入util.js子模块，使用isEmpty函数
        if (isEmpty(nickName)) {
            // 如果为空，（设置提示信息红色）msg提示用户，禁用按钮(disable设置为true)，并return（不让其往下走了）
            $("#msg").html("用户昵称不能为空！")
            $("#btn").prop("disable", true);
            return;
        }
        // 3. 判断昵称是否做了修改
        // 从session作用域中获取用户昵称
        var nick = '${user.nick}';
        // 如果用户昵称与session中的昵称一致，则return
        if (nickName == nick) {
            return;
        }
        // 4. 如果昵称做了修改
        $.ajax({
            type:"get",
            url:"user", // 路径为user
            data:{
                actionName:"checkNick", // 用户行为：验证昵称的唯一性
                nick:nickName
            },
            // success:function (result) {
            success:function (code) {
                // 如果可用，清空提示信息，按钮可用(假设1就是可用)
                // 这个返回的是code而不是resultInfo对象，所以改为code==1
                if (code == 1) {
                    // 1. 清空提示信息
                    $("#msg").html()
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
        $("#msg").html()
        // 2. 按钮可用
        $("#btn").prop("disabled", false);

    });

    /**
     * 表单提交校验
     *  满足条件，返回true，表示提交表单
     *  不满足条件，返回false，表示不提交表单
     */
    function updataUser() {
        // 1. 获取昵称文本框的值
        var nickName = $("#nickName").val();
        // 2. 判断值是否为空，在index.jsp中引入util.js子模块，使用isEmpty函数
        if (isEmpty(nickName)) {
            // 如果为空，（设置提示信息红色）msg提示用户，禁用按钮(disable设置为true)，并return（不让其往下走了）
            $("#msg").html("用户昵称不能为空！")
            $("#btn").prop("disable", true);
            // 这里是return false不能是return，如果是return的话表单还是会提交走
            return false;
        }
        // 唯一性 TODO
        // 因为我们这里做了失去焦点的校验所以这里就不用写了

        return true;
    }
</script>--%>
