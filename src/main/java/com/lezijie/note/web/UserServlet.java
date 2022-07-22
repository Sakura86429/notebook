package com.lezijie.note.web;

import com.lezijie.note.po.User;
import com.lezijie.note.service.UserService;
import com.lezijie.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet("/user")
@MultipartConfig
// servlet的话需要继承HttpServlet
public class UserServlet extends HttpServlet {

    // 新建一个类，方便调用类中的方法
    private UserService userService = new UserService();

    @Override
    // 重写http的那个service方法
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置首页导航高亮
        request.setAttribute("menu_page", "user");

        // 接收用户行为，参数名name = "actionName"要前后台保持一致，不能写错
        String actionName = request.getParameter("actionName");
        // 判断用户行为，调用对应的方法
        if ("login".equals(actionName)) {

            // 功能1. 用户登录，调用类内部的userLogin方法，而不是service层的这个方法
            userLogin(request, response);

        } else if ("logout".equals(actionName)) {

            // 功能2. 用户退出（写完后使用Alt + Enter生成方法）
            userLogOut(request, response);

        } else if ("userCenter".equals(actionName)) {

            // 3. 进入个人中心
            userCenter(request, response);
        } else if ("userHead".equals(actionName)) {

            // 4. 加载头像
            userHead(request, response);
        } else if ("checkNick".equals(actionName)) {

            // 功能5. 验证昵称的唯一性
            checkNick(request, response);
        } else if ("updateUser".equals(actionName)) {

            // 修改用户信息
            updateUser(request, response);
        }
    }

    /**
     * 修改用户信息
     * Web层（接收参数，调用Service层，响应结果）：
     *         // 如果是正常的接收参数，我们可以在Web层接收，然后传给Service层；
     *         // 如果我们的Service层需要用到request对象/response对象的时候，我们就可以直接将我们的request对象传给Service层，让Service层去接收参数
     *             注：文件上传必须在Servlet类上添加和文件上传相关的注解@multipartConfig！！！否则 所有参数都拿不到
     *             1. 调用Service层的方法，传递request对象作为参数，返回resultInfo封装对象
     *             2. （我们拿到对象以后，需要把对象返回给前端，怎么返回呢？要看前端是怎么响应过来的：
     *                 如果前端使用ajax响应，那我们这里就用流，将response对象转换为json字符串输出出去
     *                 如果是一个表单提交过来的，这种情况下你的页面要跳转回去，所以你需要将你的结果传到请求域中
     *                 那我们这里是用文件上传表单来传数据的，那你最重要返回到页面中）
     *                 将resultInfo对象存到request作用域中
     *             3. 请求转发跳转到个人中心页面 （user?actionName=userCenter）
     * @param request
     * @param response
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 调用Service层的方法，传递request对象作为参数，返回resultInfo封装对象
        ResultInfo<User> resultInfo = userService.updateUser(request);
        // 2. 将resultInfo对象存到request作用域中
        request.setAttribute("resultInfo", resultInfo);
        // 3. 请求转发跳转到个人中心页面 （user?actionName=userCenter）
        request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);
    }

    /**
     * 验证昵称唯一性
     *  Web层：
     *    1. 获取参数(昵称)（参数只有一个，是ajax传过来的）
     *    （不管是通过表单传过来，还是通过ajax传过来，这里的获取方法都是一样的）
     *    2.从session作用域获取用户对象，得到用户ID
     *    3.调用Service层的方法，得到返回的结果
     *    4.通过字符输出流将结果响应给前台的ajax的回调函数
     *    5.关闭资源
     * @param request
     * @param response
     */
    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 获取参数(文本框中的昵称)（这里的s后参数名和ajax中的k值（键值对中的k，例如：nick：nickName）保持一致）
        String nick = request.getParameter("nick");
        // 2.从session作用域获取用户对象，方便下一步得到用户ID
        User user = (User) request.getSession().getAttribute("user");
        // 3.（参数为用户的nick和Id）调用Service层的方法，得到返回的结果
        Integer code = userService.checkNick(nick, user.getUserId());
        // 4.通过字符输出流将结果响应给前台的ajax的回调函数
        // （注意这里不要直接输出code，这是个字符串，所以最后要+""，不然很容易会出问题；
        // 这是个整形，我们把它转为字符串往外抛一下异常）
        response.getWriter().write(code + "");
        // 5. 关闭资源（关闭流）
        response.getWriter().close();
    }

    /**
     * 加载头像
     *  1. 获取参数 （图片名称）
     *  2. 得到图片的存放路径（request.getServletContext().getRealPathR("/")）
     *  3. 通过图片的完整路径，得到file对象
     *  4. 通过截取，得到图片的后缀（如果不得到图片类型的话就会默认二进制输出，就会输出一片乱码）
     *  5. 通过不同的图片后缀，设置不同的响应的类型
     *  6. 利用FileUtils的copyFile（）方法，将图片拷贝给浏览器
     * @param request
     * @param response
     */
    private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 获取参数 （图片名称）
        String head = request.getParameter("imageName");
        // 2. 得到图片的存放路径（request.getServletContext().getRealP
        String realPath = request.getServletContext().getRealPath("/WEB-INF/upload/");
        // 3. 通过图片的完整路径，得到file对象
        File file = new File(realPath + "/" + head);
        // 4. 通过截取，得到图片的后缀（如果不得到图片类型的话就会默认二进制输出，就会输出一片乱码）
        // 不包含”.“，所以+1
        String pic = head.substring(head.lastIndexOf(".")+1);
        // 5. 通过不同的图片后缀，设置不同的响应的类型(设置了常用的几种，并没有完全设置)
        // 由于其可能是大写的PNG也可能是小写的png，所以判断相等的时候要忽略大小写equalsIgnoreCase
        //但实测.jfif的也可以显示
        if ("PNG".equalsIgnoreCase(pic)) {
            // 默认下面方法响应的字符串，所以是图片的话还要设置一下"image/png"
            response.setContentType("image/png");
        } else if ("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)) {
            response.setContentType("image/jpeg");
            // 常见的动图GIF
        } else if ("GIF".equalsIgnoreCase(pic)) {
            response.setContentType("image/gif");
        }
        // 6. 利用工具类FileUtils的copyFile（）方法(可以拷贝文件也可以拷贝目录)，将图片拷贝给浏览器(通过我们的response拷贝给客户端往外输出)
        FileUtils.copyFile(file, response.getOutputStream());

    }

    /**
     * 进入个人中心
     * （点击中护中心的时候，网页上变为了
     * http://localhost:8080/notebook_war_exploded/user?actionName=userCenter）
     *  1. 设置首页动态包含的页面值
     *  2. 请求转发跳转到index
     * @param request
     * @param response
     */
    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 设置首页动态包含的页面值（user下的info.jsp页面）
        //名字就叫做changePage保持一致，不然前台是拿不到的
        request.setAttribute("changePage", "user/info.jsp");
        // 2. (请求回来了，再把它请求回去)请求转发跳转到index
        // 为什么用请求转发不用重定向呢？因为咱们这边有请求域，如果用了重定向，请求域就失效了
        request.getRequestDispatcher("index.jsp").forward(request, response);
        /*
        ！！！转发和重定向的区别
        request.getRequestDispatcher()是容器中控制权的转向，在客户端浏览器地址栏中不会显示出转向后的地址；服务器内部转发，整个过程处于同一个请求当中。
        response.sendRedirect()则是完全的跳转，浏览器将会得到跳转的地址，并重新发送请求链接。这样，从浏览器的地址栏中可以看到跳转后的链接地址。不在同一个请求。重定向，实际上客户端会向服务器端发送两个请求。
        所以转发中数据的存取可以用request作用域：request.setAttribute(), request.getAttribute()，重定向是取不到request中的数据的。只能用session。

        forward()更加高效，在可以满足需要时，尽量使用RequestDispatcher.forward()方法。（思考一下为什么？）

        RequestDispatcher是通过调用HttpServletRequest对象的getRequestDispatcher()方法得到的，是属于请求对象的方法。
        sendRedirect()是HttpServletResponse对象的方法，即响应对象的方法，既然调用了响应对象的方法，那就表明整个请求过程已经结束了，服务器开始向客户端返回执行的结果。

        重定向可以跨域访问，而转发是在web服务器内部进行的，不能跨域访问。
        */
    }

    /**
     * 用户退出
     *  1.销毁Session对象
     *  2.删除Cookie对象
     *  3.重定向跳转到登录页面
     * @param request
     * @param response
     */
    private void userLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 销毁Session对象
        request.getSession().invalidate();
        // 2. 删除Cookie对象(删除与其值没有关系，所以此处写null或1或2都可以，反正最后都是要被删除的)
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0); // 因为Cookie中没有提供单独的删除方法，所以这里设置它的过期时间为0，表示删除cookie
        // 3. 重定向跳转到登录页面
        response.sendRedirect("login.jsp");
    }


    /**
     * 用户登录
     1. 获取参数 （姓名、密码）
     2. 调用Service层的方法，返回ResultInfo对象
     3. 判断是否登录成功
     如果失败
     将resultInfo对象设置到request作用域中
     请求转发跳转到登录页面
     如果成功
     将用户信息设置到session作用域中
     判断用户是否选择记住密码（rem的值是1）
     如果是，将用户姓名与密码存到cookie中，设置失效时间，并响应给客户端
     如果否，清空原有的cookie对象
     重定向跳转到index页面
     * @param request
     * @param response
     */
    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 获取参数 （姓名、密码）userName和userPwd，前台是什么就写什么，不能乱写
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");

        // 2. 调用Service层的方法，返回ResultInfo对象
        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);

        // 3. 判断是否登录成功
        if (resultInfo.getCode() == 1) { // 如果成功，状态码是1
            //  将resultInfo中的用户信息属性设置到session作用域中
            request.getSession().setAttribute("user", resultInfo.getResult());
            //  判断用户是否选择记住密码（rem的值是1）
            String rem = request.getParameter("rem");
            // 如果是，将用户姓名与密码存到cookie中，设置失效时间，并响应给客户端

            // ！！！（加载头像模块的登录小bug）
            // 如果记住密码的话，会存Cookie，但是如果退出后再登录的时候，没有选择记住密码，那么当登陆成功后就会将原来的Cookie清掉
            // 所以当你下一次再做自动登陆相关操作的时候就没有Cookie了
            if ("1".equals(rem)) {
                // 得到Cookie对象
                Cookie cookie = new Cookie("user",userName +"-"+userPwd);
                // 设置失效时间，3天（* 24小时 60min 60s）
                cookie.setMaxAge(3*24*60*60);
                // 响应给客户端
                response.addCookie(cookie);
            } else {
                // 如果否，清空原有的cookie对象
                Cookie cookie = new Cookie("user", null);
                // 删除cookie，设置maxage为0
                cookie.setMaxAge(0);
                // 响应给客户端
                response.addCookie(cookie);
            }
            // 重定向跳转到index页面（跳转到首页）
//            response.sendRedirect("index.jsp");
            // 修改为index使切换页面的时候跳转到控制器（redirect重定向）
            response.sendRedirect("index");

        } else { // 失败
            // 将resultInfo对象设置到request作用域中
            request.setAttribute("resultInfo", resultInfo);
            // 请求转发跳转到登录页面
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }
}
