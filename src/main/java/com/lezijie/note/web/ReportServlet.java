package com.lezijie.note.web;

import com.lezijie.note.po.Note;
import com.lezijie.note.po.User;
import com.lezijie.note.service.NoteService;
import com.lezijie.note.util.JsonUtil;
import com.lezijie.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Topic
 * Description
 *
 * @author zhouh
 * @version 1.0
 * Create by 2022/2/11 22:07
 */
@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private NoteService noteSercive = new NoteService();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置首页导航高亮
        request.setAttribute("menu_page", "report");
        //得到用户行为
        String actionName = request.getParameter("actionName");
        //判断用户行为
        if ("info".equals(actionName)) {
            //进入报表页面
            reportInfo(request, response);
        } else if ("month".equals(actionName)) {
            //通过月份查询对应的云记数量
            queryNoteCountByMonth(request, response);
        } else if ("location".equals(actionName)) {
            queryNoteLonAndLat(request, response);
        }
    }

    /**
     * @Method 查询用户发布云记时的经纬度
     * @Description
     * @author Mr
     * @date 2022/2/12 13:30
     * @param request
     * @param response
     */
    private void queryNoteLonAndLat(HttpServletRequest request, HttpServletResponse response) {
        //从Session作用域中获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        //调用Service层的查询方法，返回ResultInfo对象
        ResultInfo<List<Note>> resultInfo = noteSercive.queryNoteLonAndLat(user.getUserId());
        //将ResultInfo对象转换成JSON对象的字符串，相应给ajax的回调函数
        JsonUtil.toJson(response, resultInfo);
    }

    /**
     * @Method 通过月份查询对应的云记数量,做报表
     * @Description
     * @author Mr
     * @date 2022/2/11 22:56
     * @param request
     * @param response
     */
    private void queryNoteCountByMonth(HttpServletRequest request, HttpServletResponse response) {
        //从Session作用域中获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        //调用Service层的查询方法，返回ResultInfo对象
        ResultInfo<Map<String, Object>> resultInfo = noteSercive.queryNoteCountByMonth(user.getUserId());
        //由于是ajax请求，所以流
        //将ResultInfo对象转换成JSON格式的字符串，相应给ajax的回调函数
        JsonUtil.toJson(response, response);
    }

    /**
     * @Method 进入报表页面
     * @Description 
     * @author Mr
     * @date 2022/2/11 22:10
     * @param request 
     * @param response 

     */
    private void reportInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置首页动态包含的页面值
        request.setAttribute("changePage", "report/info.jsp");
        //请求转发跳转到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
