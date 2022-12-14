<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--用于格式化日子，只保留年月日--%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="col-md-9">



    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon glyphicon-th-list"></span>&nbsp;
            云记列表
        </div>

        <%--c标签判断云记列表是否存在--%>
        <c:if test="${empty page}">
            <h2>暂未查询到云记记录！</h2>
        </c:if>
        <c:if test="${!empty page}">
            <%--遍历获取云记列表--%>
            <div class="note_datas">
                <ul>
                    <c:forEach items="${page.dataList}" var="item">
                        <li>
                            『<fmt:formatDate value="${item.pubTime}" pattern="yyyy-MM-dd"/>』
                            <a href="note?actionName=detail&noteId=${item.noteId}">${item.title}</a>
                        </li>
                        <%--<li>&nbsp;&nbsp;<a href="note?act=view&amp;noteId=28">123</a> </li>
                        <li>『 2016-08-04』&nbsp;&nbsp;<a href="note?act=view&amp;noteId=27">我们</a> </li>--%>
                    </c:forEach>
                </ul>
            </div>
            <%--设置分页导航--%>
            <nav style="text-align: center">
                <ul class="pagination  center">
                    <%--c标签判断：如果当前不是第一页，则显示上一页的按钮--%>
                    <c:if test="${page.pageNum > 1}">
                        <li>
                            <a href="index?pageNum=${page.prePage}&actionName=${action}&title=${title}&date=${date}&typeId=${typeId}">
                                <span><<</span>
                            </a>
                        </li>
                    </c:if>
                    <%--导航页数--%>
                    <c:forEach begin="${page.startNavPage}" end="${page.endNavPage}" var="p">
                        <%--c标签：当第x页被选中时，页码那里加高亮效果--%>
                        <li <c:if test="${page.pageNum == p}">class=""activate</c:if> >
                            <a href="index?pageNum=${p}&actionName=${action}&title=${title}&date=${date}&typeId=${typeId}">${p}</a>
                        </li>
                    </c:forEach>
                        <%--如果当前不是最后一页，则显示下一页的按钮--%>
                    <c:if test="${page.pageNum < page.totalPages}">
                        <li>
                            <a href="index?pageNum=${page.nextPage}&actionName=${action}&title=${title}&date=${date}&typeId=${typeId}">
                                <span>>></span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>

        </c:if>

    </div>

</div>