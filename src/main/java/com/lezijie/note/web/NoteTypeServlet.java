package com.lezijie.note.web;

import com.lezijie.note.po.NoteType;
import com.lezijie.note.po.User;
import com.lezijie.note.service.NoteTypeService;
import com.lezijie.note.util.JsonUtil;
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
 * Create by 2022/2/8 19:25
 */
@WebServlet("/type")
public class NoteTypeServlet extends HttpServlet {
    private NoteTypeService typeService = new NoteTypeService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置首页导航的高亮值
        request.setAttribute("menu_page", "type");

        //得到用户行为
        String actionName = request.getParameter("actionName");

        //判断用户行为
        if ("list".equals(actionName)) {
            typeList(request, response);
        } else if ("delete".equals(actionName)) {
            deleteType(request, response);
        } else if ("addOrUpdate".equals(actionName)) {
            addOrUpdate(request, response);
        }
    }


    /**
     * @Method 添加或修改类型
     * @Description
     * @author Mr
     * @date 2022/2/9 16:34
     * @param request
     * @param response

     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        //1.接收参数（类型名称、类型Id）
        //添加的时候id是没有值的，但是不管添加还是修改我们都先拿到
        String typeName = request.getParameter("typeName");
        String typeId = request.getParameter("typeId");
        //2.获取Session作用域中的user对象，得到用户Id（因为做类型存储的时候是需要用户Id的）
        User user = (User) request.getSession().getAttribute("user");
        //3.调用Service层中的更新方法，返回ResultInfo对象(返回Integer类型是因为用户主键是int)
        ResultInfo<Integer> resultInfo = typeService.addOrUpdate(typeName, user.getUserId(), typeId);
        //4.将ResultInfo转换成JSON格式的字符串，响应给ajax的回调函数(直接传ResultInfo对象)
        JsonUtil.toJson(response, resultInfo);
    }

    /**
     * @Method 删除类型
     * @Description
     * @author Mr
     * @date 2022/2/8 20:53
     * @param request
     * @param response

     */
    private void deleteType(HttpServletRequest request, HttpServletResponse response) {
        //1.接收参数（类型ID）
        String typeId = request.getParameter("typeId");
        //2.调用Service的更新操作，返回ResultInfo对象
        ResultInfo<NoteType> resultInfo = typeService.deleteType(typeId);
        //3.将ResultInfo对象转换成JSON格式的字符串，相应给ajax的回调函数
        //封装后
        JsonUtil.toJson(response, resultInfo);
        /*封装前：
        //设置相应类型及编码格式
        response.setContentType("application/json;charset=UTF-8");
        //得到字符输出流
        PrintWriter out = response.getWriter();
        //通过fastjson的方法，将ResultInfo对象转换成JSON格式的字符串
        String json = JSON.toJSONString(resultInfo);
        //通过输出流输出JSON格式的字符串
        out.write(json);
        //关闭资源
        out.close();
        */
    }

    /**
     * @Method 查询类型列表
     * @Description 
     * @author Mr
     * @date 2022/2/8 19:42
     * @param request 
     * @param response 

     */
    private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取Session作用域设置的user对象
        User user = (User) request.getSession().getAttribute("user");
        //2.调用Service层的查询方法，查询当前登录用户的类型集合，返回集合
        List<NoteType> typeList = typeService.findTypeList(user.getUserId());
        //3.将类型列表设置到request请求域中
        request.setAttribute("typeList", typeList);
        //4.设置首页动态包含的页面值
        request.setAttribute("changePage", "type/list.jsp");
        //5.请求转发跳转到index.jsp页面
        request.getRequestDispatcher("index.jsp").forward(request, response);
        
    }
}
