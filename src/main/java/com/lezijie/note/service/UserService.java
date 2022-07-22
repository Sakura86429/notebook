package com.lezijie.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lezijie.note.dao.UserDao;
import com.lezijie.note.po.User;
import com.lezijie.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;

// service层相对来说是比较复杂的，代码量也是比较多的
public class UserService {
    // 新建一个类，方便调用类中的方法
    private UserDao userDao = new UserDao();

    /**
     * 用户登录
     1. 判断参数是否为空
     如果为空
     设置ResultInfo对象的状态码和提示信息
     返回resultInfo对象
     2. 如果不为空，通过用户名查询用户对象
     3. 判断用户对象是否为空
     如果为空
     设置ResultInfo对象的状态码和提示信息
     返回resultInfo对象
     4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较 （将密码加密后再比较）
     如果密码不正确
     设置ResultInfo对象的状态码和提示信息
     返回resultInfo对象
     5. 如果密码正确
     设置ResultInfo对象的状态码和提示信息
     6. 返回resultInfo对象
     * @param userName
     * @param userPwd
     * @return
     */
    // 此方法返回一个ResultInfo对象
    public ResultInfo<User> userLogin(String userName, String userPwd) {
        ResultInfo<User> resultInfo = new ResultInfo<>();

        // 数据回显：当登录实现时，将登录信息返回给页面显示，而不是会被全部清除
        User u = new User();
        u.setUname(userName);
        u.setUpwd(userPwd);
        // 设置到resultInfo对象中
        resultInfo.setResult(u);   //如果失败将会拿到这个resultInfo，在前台用EL表达式显示出来

        //  1. 判断参数是否为空
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(userPwd)) {
            // 如果为空 设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("用户姓名或密码不能为空！");
            // 返回resultInfo对象
            return resultInfo;
        }

        // 2. 如果不为空，通过用户名查询用户对象
        User user = userDao.queryUserByName(userName);

        // 3. 判断用户对象是否为空
        if (user == null) {
            // 如果为空,设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在！");
            // 返回resultInfo对象
            return resultInfo;
        }

        //  4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较 （将密码加密后再比较）
        // 将前台传递的密码按照MD5算法的方式加密
        userPwd = DigestUtil.md5Hex(userPwd);
        // 判断加密后的密码是否与数据库中的一致
        if (!userPwd.equals(user.getUpwd())) {
            // 如果密码不正确
            resultInfo.setCode(0);
            resultInfo.setMsg("用户密码不正确！");
            return resultInfo;
        }

        resultInfo.setCode(1);
        // 将查到的对象封装到ResultInfo类中的result私有属性中
        resultInfo.setResult(user);
        return resultInfo;
    }

    /**
     * 验证昵称唯一性
     * Service层：
     *             1. 判断昵称是否为空
     *                 如果为空，返回0
     *             2. 调用Dao层，通过用户ID和昵称查询用户对象
     *             3. 判断用户对象存在
     *                 存在，返回“0”
     *                 不存在，返回"1"
     * @param nick
     * @param userId
     * @return
     */
    public Integer checkNick(String nick, Integer userId) {
        // 1. 判断昵称是否为空（此处不会起作用，因为info.jsp中前端前面已经写了if语句可以走进去了）
        if (StrUtil.isBlank(nick)) {
            return 0;
        }
        // 2. （也是通过service中函数的参数用户的nick和Id参数）调用Dao层，通过用户ID和昵称查询用户对象
        User user = userDao.queryUserByNickAndUserId(nick, userId);

        // 3. 判断用户对象存在
        if (user != null) {
            return 0;
        }
        return 1;
    }

    /**
     * 修改用户信息
     * Service层：
     *             1. 获取参数（昵称、心情）（因为头像是一个文件传过来的，所以应该是一个文件域）
     *             2. 参数的非空校验（判断必填参数非空，这里是昵称）
     *                 如果昵称为空，将状态码和错误信息设置到resultInfo对象中，返回resultInfo对象
     *             3. 从session作用域中获取用户对象（目的是当不修改头像的时候，获取用户对象中的默认头像）
     *             4. 实现上传文件
     *                 1. 获取Part对象（因为servlet3.0中会将我们的文件封装成一个Part对象） request.fetPart("name");name代表的是file文件域的name属性值（其实就是里面那么属性值中的img）
     *                 2.通过Part对象获取上传文件的文件名（键值对格式）
     *                 3.判断文件名是否为空
     *                 4.获取文件存放的路径WEB -INF /upload/目录中
     *                 5.上传文件到指定目录
     *             5.更新用户头像(将原本用户 对象中的默认头像设置为上传的文件名)
     *             6.调用Dao层的更新方法，返回受影响的行数
     *             7.判断受影响的行数
     *                 如果大于0，则修改成功;否则修改失败
     *             8.返回resultInfo对象
     * @param request
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest request) throws IOException, ServletException {

        // 因为1和3都用到request了，所以要是不单独把request传过来的话就拿不到这个对象了
        ResultInfo<User> resultInfo = new ResultInfo<>();
        // 1. 获取参数（昵称、心情）(因为参数是通过表单传过来的，所以这里面需要写的参数名要看表单元素里面对应的name属性)
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");

        // 2. 参数的非空校验（判断必填参数非空，这里是昵称）
        if (StrUtil.isBlank(nick)) {
            // 如果昵称为空，将状态码和错误信息设置到resultInfo对象中，返回resultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }

        // 3. 从session作用域中获取用户对象（目的是当不修改头像的时候，获取用户对象中的默认头像）
        User user = (User) request.getSession().getAttribute("user");
        // 设置修改的昵称和头像
        user.setNick(nick);
        user.setMood(mood);

        // 4. 实现上传文件
        try {
            // 1. 获取Part对象（因为servlet3.0中会将我们的文件封装成一个Part对象） request.fetPart("name");name代表的是file文件域的name属性值（其实就是里面那么属性值中的img）
            Part part = request.getPart("img");
            // 2.通过Part对象获取上传文件的文件名（键值对格式）
            String header = part.getHeader("Content-Disposition");
            // 获取具体的请求头对应的值
            String str = header.substring(header.lastIndexOf("=") + 2);
            // 获取上传的文件名
            String fileName = str.substring(0, str.length() - 1);
            // 3.判断文件名是否为空
            if (!StrUtil.isBlank(fileName)) {
                // 如果用户上传了头像，则更新用户对象中的头像
                user.setHead(fileName);
                // 4.获取文件存放的路径WEB -INF /upload/目录中
                String filePath = request.getServletContext().getRealPath("/WEB-INF/upload/");
                // 5.上传文件到指定目录
                part.write(filePath + "/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 6.调用Dao层的更新方法，返回受影响的行数
        int row = userDao.updateUser(user);
        // 7.判断受影响的行数
        if (row > 0) {
            resultInfo.setCode(1);
            // 更新session中用户对象
            request.getSession().setAttribute("user", user);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
