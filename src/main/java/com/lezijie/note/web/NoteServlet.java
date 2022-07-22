package com.lezijie.note.web;

import cn.hutool.core.util.StrUtil;
import com.lezijie.note.po.Note;
import com.lezijie.note.po.NoteType;
import com.lezijie.note.po.User;
import com.lezijie.note.service.NoteService;
import com.lezijie.note.service.NoteTypeService;
import com.lezijie.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Topic
 * Description
 *
 * @author zhouh
 * @version 1.0
 * Create by 2022/2/9 19:37
 */
@WebServlet("/note")
public class NoteServlet extends HttpServlet {
    private NoteService noteService = new NoteService();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置首页导航栏的高亮值
        request.setAttribute("menu_page", "note");

        //得到用户行为
        String actionName = request.getParameter("actionName");
        //判断用户行为
        if ("view".equals(actionName)) {
            //进入发布云记页面
            noteView(request, response);
        } else if ("addOrUpdate".equals(actionName)) {
            //添加或修改云记
            addOrUpdate(request, response);
        } else if ("detail".equals(actionName)) {
            noteDetail(request, response);
        } else if ("delete".equals(actionName)) {
            //通过用户id从detail中删除云记
            noteDelete(request, response);
        }
    }

    /**
     * @Method 通过用户id从detail中删除云记
     * @Description
     * @author Mr
     * @date 2022/2/11 20:50
     * @param request
     * @param response
     */
    private void noteDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接收参数
        String noteId = request.getParameter("noteId");
        //2.调用Service层删除方法，返回状态码（1=成功，0=失败）
        Integer code = noteService.deleteNote(noteId);
        //3.通过流将结果响应给ajax的回调函数(+""将Integer转字符串)
        response.getWriter().write(code + "");
        response.getWriter().close();
    }

    /**
     * @Method 查询云记详情
     * @Description
     * @author Mr
     * @date 2022/2/11 20:09
     * @param request
     * @param response
     */
    private void noteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收参数
        String noteId = request.getParameter("noteId");
        //2.调用Service层的查询方法，返回Note对象
        Note note = noteService.findNoteById(noteId);
        //3.将Note对象设置到request请求域中
        request.setAttribute("note", note);
        //4.设置首页动态包含的页面值
        request.setAttribute("changePage", "note/detail.jsp");
        //5.请求转发跳转到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }

    /**
     * @Method 添加或修改云记操作
     * @Description
     * @author Mr
     * @date 2022/2/9 21:28
     * @param request
     * @param response

     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //1.接收参数（前台传过来的类型Id、标题、内容）
        String typeId = request.getParameter("typeId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        //获取经纬度
        String lon = request.getParameter("lon");
        String lat = request.getParameter("lat");

        //如果是修改操作，需要接受noteId
        String noteId = request.getParameter("noteId");

        //2.调用Service层方法，返回ResultInfo对象
        //！！！一般Service层中的方法和Web层名字一样
        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId, title, content, noteId, lon, lat);
        //3.判断resultInfo的code值
        if (resultInfo.getCode() == 1) {   //成功
            //重定向跳转到首页 index
            response.sendRedirect("index");
        } else {   //失败的话（actionName=view说明进入了发布云记的界面）
            //将resultInfo对象设置到request作用域
            request.setAttribute("resultInfo", resultInfo);
            //请求转发跳转到note?actionName=view
            String url = "note?actionName=view";
            //如果是修改操作，需要传递noteId
            if (!StrUtil.isBlank(noteId)) {
                url += "&noteId="+noteId;
            }
            request.getRequestDispatcher("note?actionName=view").forward(request, response);

        }
    }

    /**
     * @Method 进入发表云记页面
     * @Description
     * @author Mr
     * @date 2022/2/9 19:41
     * @param request
     * @param response

     */
    private void noteView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*点击修改按钮的修改操作，也进入发布云记的页面*/
        //得到要修改的云记Id
        String noteId = request.getParameter("noteId");
        //通过noteId查询云记对象
        Note note = noteService.findNoteById(noteId);
        //将note对象设置到请求域中
        request.setAttribute("noteInfo",note);
        /*修改操作*/


        //1.从Session对象中获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        //2.通过用户Id查询对应的类型列表
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        //3.将类型列表设置到request请求域中
        request.setAttribute("typeList", typeList);
        //4.设置首页动态包含的页面值
        request.setAttribute("changePage", "note/view.jsp");
        //5.请求转发跳转到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
