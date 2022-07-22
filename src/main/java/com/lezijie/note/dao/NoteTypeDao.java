package com.lezijie.note.dao;

import com.lezijie.note.po.NoteType;
import com.lezijie.note.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Topic
 * Description
 *
 * @author zhouh
 * @version 1.0
 * Create by 2022/2/8 19:23
 */
public class NoteTypeDao {

    public List<NoteType> findTypeListByUserId(Integer userId) {
        //1.定义sql语句
        String sql = "select typeId,typeName,userId from note_type where userId = ?";
        //2.设置参数列表
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //3.调用BaseDao的查询方法，返回集合
        List<NoteType> list = BaseDao.queryRows(sql, params, NoteType.class);
        //4.返回集合
        return list;
    }

    /**
     * @Method 通过类型ID查询云记记录的数量，返回云记数量
     * @Description
     * @author Mr
     * @date 2022/2/8 21:18
     * @param typeId
     * @return long
     */
    public long findNoteCountByTypeId(String typeId) {
        //定义sql语句
        //count查的是云记表为count(1)
        String sql = "select count(1) from note where typeId = ?";
        //设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        //调用BaseDao
        long count = (long) BaseDao.findSingleValue(sql, params);
        return count;
    }

    /**
     * @Method 通过类型ID删除指定的类型记录，返回受影响的行数
     * @Description
     * @author Mr
     * @date 2022/2/8 21:22
     * @param typeId
     * @return int
     */
    public int deleteTypeById(String typeId) {
        //定义sql语句
        String sql = "delete from note_type where typeId = ?";
        //设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        //调用BaseDao
        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }

    /**
     * @Method 添加（该用户下的类型名称都不能相同）还是修改（类型名称可以相同），都要做这一步--->判断类型名是否唯一
     * （两种情况：查到了，查不到）
     * ！！！拿原来的Name和userId去查，如果查不到，说明添加了不同的name；如果查得到，且typeId和原来的一样，说明是修改操作，
     * 类型id与null（添加没有id）肯定不一样，说明是添加了相同的name
     * @Description 这里设置如果修改后是原来的类型Id(因为是通过userId和typeName查询的，这两项肯定相同)，即未动，还是返回1（返回0的话弹出为空了）
     * @author Mr
     * @date 2022/2/9 17:01
     * @param typeName
     * @param userId
     * @param typeId
     * @return Integer
     */
    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        //定义SQL语句(通过用户名称和类型名称查询)
        String sql = "select * from note_type where userId = ? and typeName = ?";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(typeName);
        //执行查询操作得到一个NoteType实体类对象
        //调用查询对象的方法（其中使用查询集合的方法）（此外还有查询字段：单个值，和更新操作）
        NoteType noteType = (NoteType) BaseDao.queryRow(sql, params, NoteType.class);
        //！！！没有这个用户说明不重复，否则重复保留和原类型id一样的类型name，其他的不一样的舍去0弹出
        //如果对象为空表示数据库中没有同名的类型名称，可以继续下一步，不用code0 失败返回值0return了
        if (noteType == null) {   //！！！不存在typeId，说明是添加操作
            return 1;
            //else 表示不为空，可能为原来的name或其他
        } else {   //！！！存在typeId且typeName和原来一样，说明是修改操作且修改后的类型名和修改前的一样
            //！！！如果是添加操作，typeId为空更不可能相等了
            //（因为在添加模态框的点击函数中清空掉了）
            //而修改的可以
            //如果是修改操作，则需要判断是否是当前记录本身（不能与之前typeId相同意思就是不能是同一个）
            if (typeId.equals(noteType.getTypeId().toString())) {
                return 1;
            }
        }
        return 0;   //说明查到的是原来有的id，添加的id=null肯定不相等

    }

    /**
     * @Method （如果没有查到typeId就说明是添加类型名称的方法）添加方法，返回主键
     * @Description
     * @author Mr
     * @date 2022/2/9 17:25
     * @param typeName
     * @param userId
     * @return Integer
     */
    public Integer addType(String typeName, Integer userId) {
        //首先准备null对象
        Integer key = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //手写抛异常和关闭资源
        try {
            //得到数据库连接
            connection = DBUtil.getConnetion();
            //定义sql语句,插入不用按顺序写
            String sql = "insert into note_type (typeName,userId) values (?,?)";
            //预编译，这里和前面的不一样，因为不光写sql语句，还要返回主键
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //设置数据库中每一行的单个位置参数（设置0会报错，因为除去主键之后，是从1开始的）
            preparedStatement.setString(1,typeName);
            preparedStatement.setInt(2,userId);
            //执行更新，返回受影响的行数
            int row = preparedStatement.executeUpdate();
            //判断受影响的行数
            if (row > 0) {
                //获取返回主键的结果集
                resultSet = preparedStatement.getGeneratedKeys();
                //得到主键的值
                if (resultSet.next()) {
                    //取第一column且只有一个列（0不会报错，但是不知道返回什么结果）
                    key = resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            DBUtil.close(resultSet, preparedStatement, connection);
        }
        return key;
    }

    /**
     * @Method 修改方法，返回受影响的行数
     * @Description
     * @author Mr
     * @date 2022/2/9 17:39
     * @param typeName
     * @param typeId
     * @return Integer
     */
    public Integer updateType(String typeName, String typeId) {
        //定义sql语句，更新的时候不用userId？
        String sql = "update note_type set typeName = ? where typeId = ?";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(typeName);
        params.add(typeId);
        //调用BaseDao的更新方法
        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }
}
