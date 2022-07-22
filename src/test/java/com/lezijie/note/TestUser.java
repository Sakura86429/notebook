package com.lezijie.note;

import com.lezijie.note.dao.BaseDao;
import com.lezijie.note.dao.UserDao;
import com.lezijie.note.po.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestUser {

    @Test
    public void testQueryUserByName() {
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }

    @Test
    public void testAdd() {
        //用户id设置为主键自增，添加的时候就不用写userId了
       String sql = "insert into user (uname,upwd,nick,head,mood) values (?,?,?,?,?)";
       List<Object> params = new ArrayList<>();
       params.add("wangwu");
       //密码要写加密过的密码
       params.add("e10adc3949ba59abbe56e057f20f883e");
       params.add("wangwu");
       params.add("404.jpg");
       params.add("Hello");
       int row = BaseDao.executeUpdate(sql,params);
       System.out.println(row);

    }
}
