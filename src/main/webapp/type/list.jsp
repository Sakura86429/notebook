<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="col-md-9">
    <div class="data_list">
        <%--类型列表、添加类别--%>
        <div class="data_list_title">
            <span class="glyphicon glyphicon-list"></span>类型列表
            <span class="noteType_add">
            <button class="btn btn-sm btn-success" type="button" id="addBtn">添加类别</button>
        </span>
        </div>
            <%--编号、类型、操作--%>
        <div id="myDiv">

            <%--通过JSTL的if标签，判断类型集合是否存在
            //typeList与NoteTypeServlet中的get...是一一对应的名字--%>
            <%--如果不存在，则显示对应的提示信息（暂未查询到类型数据！）--%>
            <c:if test="${empty typeList}">
                <h2>暂未查询到类型数据！</h2>
            </c:if>
                <%--如果存在，通过JSTL的forEach标签进行遍历--%>
            <c:if test="${!empty typeList}">
            <table id="myTable" class="table table-hover table-striped">
                <tbody>
<%--                表头留着--%>
                <tr>
                    <th>编号</th>
                    <th>类型</th>
                    <th>操作</th>
                </tr>
                <%--遍历类型集合--%>
                <c:forEach items="${typeList}" var="item">
                    <%--//加上id之后，每一个tr都有一个id tr_1、tr_2...--%>
                    <%--加上的id是分别是各个遍历的框的id--%>
                    <tr id="tr_${item.typeId}">
                        <%--上面th对应是表头，这里td对应是表格子--%>
                        <td>${item.typeId}</td>
                        <td>${item.typeName}</td>
                        <td>
                            <button class="btn btn-primary" type="button" onclick="openUpdateDialog(${item.typeId})">修改</button>&nbsp;
                            <button class="btn btn-danger del" type="button" onclick="deleteType(${item.typeId})">删除</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            </c:if>
        </div>
    </div>
    <!-- 模态框（Modal） 添加和修改 -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <%--左上方小标题--%>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <%--在type.js中.html修改--%>
                    <h4 class="modal-title" id="myModalLabel">新增</h4>
                </div>
                    <%--中间内容--%>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="typename">类型名称</label>
                        <input type="hidden" name="typeId" id="typeId">
                        <input type="text" name="typename" class="form-control" id="typeName" placeholder="类型名称">
                    </div>
                </div>
                <%--模态框中的关闭、保存按钮div--%>
                <div class="modal-footer">
                    <span id="msg" style="font-size: 12px; color: red"></span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span class="glyphicon glyphicon-remove"></span>关闭</button>
                    <button type="button" class="btn btn-primary" id="btn_submit">
                        <span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
<%--    <script type="text/javascript" src="statics/js/type.js">--%>

<%--    </script>--%>
</div>
<script type="text/javascript" src="statics/js/type.js"></script>











