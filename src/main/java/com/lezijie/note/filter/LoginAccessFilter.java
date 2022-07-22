package com.lezijie.note.filter;

import com.lezijie.note.po.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 非法访问拦截
 *  拦截的资源：
 *      所有的资源   /*
 *      （默认拦截所有资源，之后再针对具体的放行）
 *
 *      需要被放行的资源
 *          1. 指定页面，放行 （用户无需登录的即可访问的页面；例如：登录页面login.jsp、注册页面register.jsp等）
 *          2. 静态资源，放行 （存放在statics目录下的资源；例如：js、css、images等）
 *          3. 指定行为，放行 （用户无需登录即可执行的操作；例如：登录操作actionName=login等）
 *          4. 登录状态，放行 （判断session作用域中是否存在user对象；存在则放行，不存在，则拦截跳转到登录页面）
 *          //因为登陆后会把用户信息存到session中，所以根据登陆状态决定放行
 *
 *  免登录（自动登录）
 *      通过Cookie和Session对象实现
 *
 *      什么时候使用免登录：
 *          当用户处于未登录状态，且去请求需要登录才能访问的资源时，调用自动登录功能
 *
 *      目的：
 *          让用户处于登录状态（自动调用登录方法）
 *
 *      实现：
 *          从Cookie对象中获取用户的姓名与密码，自动执行登录操作
 *              1. 获取Cookie数组  request.getCookies()
 *              2. 判断Cookie数组
 *              3. 遍历Cookie数组，获取指定的Cookie对象 （name为user的cookie对象）
 *              4. 得到对应的cookie对象的value （姓名与密码：userName-userPwd）
 *              5. 通过split()方法将value字符串分割成数组
 *              6. 从数组中分别得到对应的姓名与密码值
 *              7. 请求转发到登录操作  user?actionName=login&userName=姓名&userPwd=密码
 *              8. return
 *
 *     如果以上判断都不满足，则拦截跳转到登录页面
 *
 */
@WebFilter("/*")
public class LoginAccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 基于HTTP
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 得到访问的路径
        String path = request.getRequestURI(); // 格式：项目路径/资源路径

        // 1. 指定页面，放行 （用户无需登录的即可访问的页面；例如：登录页面login.jsp、注册页面register.jsp等）
        if (path.contains("/login.jsp")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 静态资源，放行 （存放在statics目录下的资源；例如：js、css、images等，放行首页不放行静态资源---图像、样式等）
        if (path.contains("/statics")) {
            filterChain.doFilter(request, response);
            return;
        }

        //放行执行的登录动作
        // 3. 指定行为！！！（例如这里是登陆行为就放行），放行 （用户无需登录即可执行的操作；例如：登录操作actionName=login等）
//        联系到：<%-- actionName表示用户行为，通过这个参数后端可以在UserServlet中判断用户当前想要操作的功能 --%>
//                    <!--// 隐藏域，value中如果是登陆的话给个1也是可以的-->
//                <input type="hidden" name="actionName" value="login"/>
        // user就是web目录下UserServlet这个模块，是：@WebServlet("/user")中的/user，然后再访问里面的actionName行为
        if (path.contains("/user")) {
            // 得到用户行为
            String actionName = request.getParameter("actionName");
            // 判断是否是登录操作
            if ("login".equals(actionName)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 4. 登录状态，放行
        // 用户登陆成功之后会把用户信息存在session里面，判断session中有用户信息，我就认为你是一个登陆状态；如果session信息没有了，我就认为你是一个未登录状态
        // 是登陆状态就放行
        // （判断session作用域中是否存在user对象；存在则放行，不存在，则拦截跳转到登录页面）
        // 获取Session作用域中的user对象
        User user = (User) request.getSession().getAttribute("user");
        // 判断user对象是否为空
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }
        /**
         * 免登录 （自动登录）
         * 假设不是前面的指定页面、静态资源、指定行为和登录状态，例如：index.jsp这样，我们选择了记住密码就不应该被拦截走，应该做一个自动登录操作
         *      从Cookie对象中获取用户的姓名与密码，自动执行登录操作
         *
         *      什么时候使用免登录：
         *          当用户处于未登录状态，且去请求需要登录才能访问的资源时，调用自动登录功能
         *
         *      目的：
         *          让用户处于登陆状态（自动调用登陆方法）
         *
         *      实现：
         *          从Cookie对象中获取用户的姓名和密码，自动执行登陆操作
         *
         */
        // 1. 获取Cookie数组  request.getCookies()
        Cookie[] cookies = request.getCookies();
        // 2. 判断Cookie数组
        if (cookies != null && cookies.length > 0) {
            // 3. 遍历Cookie数组，获取指定的Cookie对象 （name为user的cookie对象）
            for (Cookie cookie: cookies) {
                if ("user".equals(cookie.getName())) {
                    // 4. 得到对应的cookie对象的value （姓名与密码：userName-userPwd）
                    String value = cookie.getValue(); // admin-123456
                    // 5. 通过split()方法将value字符串分割成数组
                    String[] val = value.split("-");
                    // 6. 从数组中分别得到对应的姓名与密码值
                    String userName = val[0];
                    String userPwd = val[1];
                    // 7. 请求转发到登录操作  user?actionName=login&userName=姓名&userPwd=密码
//                    String url = "user?actionName=login&userName="+userName+"&userPwd="+userPwd;
                    String url = "user?actionName=login&rem=1&userName="+userName+"&userPwd="+userPwd;
                    request.getRequestDispatcher(url).forward(request,response);
                    // 8. return
                    return;
                }
            }
        }
        // 拦截请求，重定向跳转到登录页面
        response.sendRedirect("login.jsp");

    }

    @Override
    public void destroy() {

    }
}
