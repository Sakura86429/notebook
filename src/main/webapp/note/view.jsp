<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon-cloud-upload"></span>&nbsp;
            <%--如果noteInfo为空说明是发布云记，不为空说明是在修改云记--%>
            <c:if test="${empty noteInfo}">
                发布云记
            </c:if>
            <c:if test="${!empty noteInfo}">
                修改云记
            </c:if>
        </div>
        <div class="container-fluid">
            <div class="container-fluid">
                <div class="row" style="padding-top: 20px;">
                    <div class="col-md-12">
                        <%--判断类型列表是否为空，如果为空，提示用户先添加类型--%>
                        <c:if test="${empty typeList}">
                            <h2>暂未查询到云记类型！</h2>
                            <h4><a href="type?actionName=list">添加类型</a></h4>
                        </c:if>
                            <%--如果不为空就从数据库中拿--%>
                        <c:if test="${!empty typeList}">
                            <%--提交方式是post，提交地址note--%>
                            <form class="form-horizontal" method="post" action="note">
                                <%--下面两个隐藏域都使用action="note"--%>
                                <%--设置隐藏域 存放用户行为 actionName--%>
                                <%--此处的addOrUpdate是通过上面的<form action=“note”和Servlet中的@Web...注解找到UserServlet--%>
                                <input type="hidden" name="actionName" value="addOrUpdate">
                                <%--因为修改后提交的时候会用到noteId，所以这里设置隐藏域 存放noteId--%>
                                <input type="hidden" name="noteId" value="${noteInfo.noteId}">

                                    <%--设置隐藏域：用来存放用户发布云记时所在地区的经纬度--%>
                                <%-- 存放用户发布云记时所在地区的经纬度 --%>
                                    <%--id选择器可以在此页面最后给值，然后name属性给后台获取--%>
                                    <%--经度--%>
                                <input type="hidden" name="lon" id="lon">
                                    <%--维度--%>
                                <input type="hidden" name="lat" id="lat">

                                <%--！<div类别--%>
                                <div class="form-group">
                                    <label for="typeId" class="col-sm-2 control-label">类别:</label>
                                    <div class="col-sm-8">
                                        <%--select有id有name--%>
                                        <%--select是下拉框--%>
                                        <select id="typeId" class="form-control" name="typeId">
                                            <option value="">请选择云记类别...</option>
                                            <%--使用c标签写一个forEach循环，循环的值items就是我们查到的typeList--%>
                                            <c:forEach var="item" items="${typeList}">
                                                <c:choose>
                                                    <c:when test="${!empty resultInfo}">
                                                        <%--这里可以加一个c便签，判断（test），如果resultInfo中的typeId和item中的相等就选中--%>
                                                        <option <c:if test="${resultInfo.result.typeId == item.typeId}">selected</c:if>
                                                                value="${item.typeId}">${item.typeName}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option <c:if test="${noteInfo.typeId == item.typeId}">selected</c:if>
                                                                         value="${item.typeId}">${item.typeName}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <%--!<div标题--%>
                                <div class="form-group">
                                    <%--<div下面的-lebel是小表--%>
                                    <label for="title" class="col-sm-2 control-label">标题:</label>
                                    <div class="col-sm-8"><%--8表示标题占8格--%>
                                        <c:choose>
                                            <c:when test="${resultInfo}">
                                                <input class="form-control" name="title" id="title" placeholder="云记标题" value="${resultInfo.result.title}">
                                            </c:when>
                                            <c:otherwise>
                                                <input class="form-control" name="title" id="title" placeholder="云记标题" value="${noteInfo.title}">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <%--！div云记内容，富文本编辑器--%>
                                <div class="form-group">
                                    <label for="title" class="col-sm-2 control-label">内容:</label>
                                    <div class="col-sm-8">
                                        <c:choose>
                                            <c:when test="${resultInfo}">
                                                <%--准备容器 加载富文本编辑器  加载失败--%>
                                                <textarea id="noteEditor" name="noteEditor">${resultInfo.result.content}</textarea>
                                            </c:when>
                                            <c:otherwise>
                                                <%--准备容器 加载富文本编辑器  加载失败--%>
                                                <textarea id="noteEditor" name="noteEditor">${noteInfo.content}</textarea>
                                            </c:otherwise>
                                        </c:choose>

                                    </div>
                                </div>
                                <%--！div保存按钮--%>
                                <div class="form-group">
                                    <div class="col-sm-offset-4 col-sm-4">
                                        <%--这个<input是一个submit按钮--%>
                                        <%--有的按钮是<button--%>
                                        <input type="submit" class="btn btn-primary" onclick="return checkForm()" value="保存">
                                            <%--此处显示出错信息--%>
                                        &nbsp;<span id="msg" style="font-size: 12px;color: red">${resultInfo.msg}</span>
                                    </div>
                                </div>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        <%--加载富文本编辑器--%>
        /*noteEditor是我们容器的id*/
        var ue = UE.getEditor('noteEditor');
    });


    /**
     * 表单校验函数（后端会传回一个resultInfo，前端使用EL表达式取它的值）
     * @returns {boolean}
     */
    function checkForm() {

        /*1.获取表单元素的值*/
        /*获取下拉框中的选项 ，云记类型 .val*/
        var typeId = $("#typeId").val();
        /*获取文本框的值，云记标题  .val()*/
        /*<input中的name就是title*/
        var title = $("#title").val();
        /*获取富文本编辑器的内容，云记内容 ue.getContent()*/
        // var noteEditor = ue.getContent();
        var noteEditor = $("#noteEditor").val();

        /*2.参数的非空判断*/
        if(isEmpty(typeId)){
            $("#msg").html("请选择云记类型！");
            return false;
        }
        if(isEmpty(title)){
            $("#msg").html("云记标题不能为空！");
            return false;
        }
        if(isEmpty(noteEditor)){
            $("#msg").html("云记内容不能为空！");
            return false;
        }
        return true;
    }

</script>

<%--引入百度地图api文件，需要申请百度地图对应ak密钥--%>
<%--没有用组件，所以把src中的type去掉--%>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=odMf9zrQoudBkMiYG7c73lZwwIpoEgeu"></script>
<script type="text/javascript">
        <%--百度地图获取当前地址位置的经纬度--%>
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function (r) {
        //判断是否获取到
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
            console.log("您的位置：" + r.point.lng+"," + r.point.lat);
            //使用id选择器将坐标设置给隐藏域
            $("#lon").val(r.point.lng);
            $("#lat").val(r.point.lat);
        }else {
            console.log("failed:"+ this.getStatus());
        }
    });
</script>