package com.lezijie.note.web;

import com.lezijie.note.po.Note;
import com.lezijie.note.po.User;
import com.lezijie.note.service.NoteService;
import com.lezijie.note.util.Page;
import com.lezijie.note.vo.NoteVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置首页导航高亮
        request.setAttribute("menu_page", "index");

        /* 1 第一种，下面是另外两种和前后端的交流，
           感觉和前面的@WebServlet("/index")注解有关系
        // 2 重定向跳转到index页面（不让其进入到index.jsp中，而是让其进入到一个控制器中，所以此处应该再准备一个servlet）
        // @WebServlet("/index"),所以定位到index？？
//            response.sendRedirect("index.jsp");
        response.sendRedirect("index");
        */

        //得到用户行为（判断是什么条件查询：左上角标题查询、日期查询、类型查询）
        String actionName = request.getParameter("actionName");
        //将用户行为设置到request作用域中（分页导航中需要获取）
        request.setAttribute("action", actionName);
        //判断用户行为
        if ("searchTitle".equals(actionName)) {
            //得到查询条件：标题
            String title = request.getParameter("title");
            //将查询条件设置到request请求域中（查询条件的回显）
            request.setAttribute("title", title);
            //标题搜索
            noteList(request, response, title, null, null);
        } else if ("searchDate".equals(actionName)) {   //日期查询
            //得到查询条件：日期
            String date = request.getParameter("date");
            //将查询条件设置到request请求域中（查询条件的回显）
            request.setAttribute("date", date);
            //日期搜索（没有标题，只有日期，多添加一个参数String类型过去）
            noteList(request, response, null, date, null);
        } else if ("searchType".equals(actionName)) {   //类型查询
            //得到查询条件：日期
            String typeId = request.getParameter("typeId");
            //将查询条件设置到request请求域中（查询条件的回显）
            request.setAttribute("typeId", typeId);
            //日期搜索（没有标题，只有日期，多添加一个参数String类型过去）
            noteList(request, response, null, null, typeId);
        }
        else {
            //分页查询云记列表
            //(需要穿三个参数的函数，如果不需要第三个参数可以传一个null，但是必须要传)
            noteList(request, response, null, null, null);
        }
/*

        //分页查询云记列表
        noteList(request, response);*/

        // 设置首页动态包含的页面
        // 为什么要在这里设置，因为我们在UserServlet中设置的是重定向调整，如果在UserServlet中设置了在这里拿不到
        // 参数s："changePage"要与前端保持一致
        // note目录下的list.jsp页面
        request.setAttribute("changePage","note/list.jsp");

        /*
        // 3 请求转发到登录操作  user?actionName=login&userName=姓名&userPwd=密码
        String url = "user?actionName=login&rem=1&userName="+userName+"&userPwd="+userPwd;
        request.getRequestDispatcher(url).forward(request,response);
        */
        // 请求转发到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * @Method 分页查询云记列表
     * @Description
     * @author Mr
     * @date 2022/2/10 22:16
     * @param request
     * @param response
     * @param title

     */
    private void noteList(HttpServletRequest request, HttpServletResponse response, String title, String date, String typeId) {
        //1.接收参数（当前页、每页显示的数量）
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        //2.获取Session作用域中的user对象
        User user = (User) request.getSession().getAttribute("user");
        //3.调用Service层查询方法，返回Page对象
        Page<Note> page = new NoteService().findNoteListByPage(pageNum, pageSize, user.getUserId(), title, date, typeId);
        //4.将Page对象设置到request作用域中
        request.setAttribute("page", page);

        //通过日期分组查询当前登录用户下的云记数量
        List<NoteVo> dateInfo = new NoteService().findNoteCountByDate(user.getUserId());
        //设置及和存放在requset-Session作用域中前台好取
        request.getSession().setAttribute("dateInfo",dateInfo);

        //通过类型分组查询当前登录用户下的云记数量
        List<NoteVo> typeInfo = new NoteService().findNoteCountByType(user.getUserId());
        //设置及和存放在requset-Session作用域中前台好取
        request.getSession().setAttribute("typeInfo",typeInfo);
    }

}
