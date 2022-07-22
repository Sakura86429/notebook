package com.lezijie.note.dao;

import cn.hutool.core.util.StrUtil;
import com.lezijie.note.po.Note;
import com.lezijie.note.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Topic
 * Description
 *
 * @author zhouh
 * @version 1.0
 * Create by 2022/2/9 19:35
 */
public class NoteDao {
    /**
     * @Method 添加或修改云记，返回受影响的行数
     * @Description
     * @author Mr
     * @date 2022/2/9 21:52
     * @param note
     * @return int
     */
    public int addOrUpdate(Note note) {
        //1.定义sql语句（可以不用insert全部字段）
        String sql = "";
        //2.设置参数
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        //判读noteId是否为空：如果为空，则为添加操作；如果不为空，则为修改操作
        if (note.getNoteId() == null) {   //添加操作
            sql = "insert into note (type,title,content,pubTime,lon,lat) values (?,?,?,now(),?,?)";
            params.add(note.getLon());
            params.add(note.getLat());
        } else {   //修改操作(不用修改经纬度)
            sql = "update note set typeId = ?, title = ?, content = ? where noteId = ?";
            params.add(note.getNoteId());
        }

        //3.调用BaseDao的更新方法
        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }

    /**
     * @Method 查询当前登陆用户的云记数量，返回总记录数
     * @Description
     * @author Mr
     * @date 2022/2/10 23:01
     * @param userId
     * @param title
     * @param date
     * @param typeId
     * @return long 所有云记记录的数量
     */
    public long findNoteCount(Integer userId, String title, String date, String typeId) {
        //定义sql语句(此处如果有title的话，应该是条件查询，有就查，没有就不查)
        String sql = "SELECT count(1) from note n INNER JOIN " +
                " note_type t on n.typeId = t.typeId " +
                " WHERE userId = ?";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //判断条件查询的参数是否为空（如果查询的参数不为空，则拼接sql语句，并设置所需要的参数）
        if (!StrUtil.isBlank(title)) {
            //拼接sql语句需要的参数
            sql += " and title like concat('%',?,'%') ";
            //设置sql语句所需要的参数
            params.add(title);
        } else if (!StrUtil.isBlank(date)) {
            //拼接sql语句需要的参数
            sql += " and date_format(pubTime, '%Y年%m月') = ? ";
            //设置sql语句所需要的参数
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) {
            //拼接sql语句需要的参数
            sql += " and n.typeId = ? ";
            //设置sql语句所需要的参数
            params.add(typeId);
        }
        //调用BaseDao的查询方法(查询单个字段：满足条件的行数)
        long count = (long) BaseDao.findSingleValue(sql, params);
        return count;
    }

    /**
     * @Method 分页查询当前登录用户下当前页的数据列表，返回note集合
     * @Description
     * @author Mr
     * @date 2022/2/10 23:02
     * @param userId
     * @param index
     * @param pageSize
     * @param title
     * @param date
     * @param typeId
     * @return List<Note> 返回存有当前页的所有数据的列表
     */
    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize, String title, String date, String typeId) {
        //定义sql语句（将上一个查询数量的sql语句count(1)换成字段就可以了）
        //加上limit才能将数据限制到当前页(且limit必须放在最后面)
        String sql = "SELECT noteId,title,pubTime from note n INNER JOIN " +
                " note_type t on n.typeId = t.typeId WHERE userId = ? ";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        //判断条件查询的参数是否为空（如果查询的参数不为空，则拼接sql语句，并设置所需要的参数）
        if (!StrUtil.isBlank(title)) {
            sql += " and title like concat('%',?,'%') ";
            //设置sql语句所需要的参数
            params.add(title);
        } else if (!StrUtil.isBlank(date)) {
            //拼接sql语句需要的参数
            sql += " and date_format(pubTime, '%Y年%m月') = ? ";
            //设置sql语句所需要的参数
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) {
            //拼接sql语句需要的参数
            sql += " and n.typeId = ? ";
            //设置sql语句所需要的参数
            params.add(typeId);
        }

        //拼接分页的sql语句（limit语句需要写在sql语句最后）
        sql += " order by pubTime desc limit ?,?";
        params.add(index);
        params.add(pageSize);
        //调用BaseDao的查询方法(查询多行)
        List<Note> noteList = BaseDao.queryRows(sql, params, Note.class);
        return noteList;
    }

    /**
     * @Method  通过日期分组查询当前登录用户下的云记数量
     * @Description
     * @author Mr
     * @date 2022/2/11 16:24
     * @param userId
     * @return List<NoteVo>
     */
    public List<NoteVo> findNoteCountByDate(Integer userId) {
        //定义sql语句
        String sql = "SELECT count(1) noteCount,DATE_FORMAT(pubTime, '%Y年%m月') groupName FROM note n " +
                "INNER JOIN note_type t ON n.typeId WHERE userId = ? " +
                "ORDER BY DATE_FORMAT(pubTime,'%Y年%m月') DESC";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    /**
     * @Method 通过类别分组查询当前登录用户下的云记数量
     * @Description
     * @author Mr
     * @date 2022/2/11 16:53
     * @param userId
     * @return List<NoteVo>
     */
    public List<NoteVo> findNoteCountByType(Integer userId) {
        String sql = "SELECT count(noteId) noteCount, t.typeId, typeName groupName FROM note n" +
                "RIGHT JOIN note_type t ON n.typeId = t.typeId WHERE userId = ?" +
                "GROUP BY t.typeId ORDER BY COUNT(noteId) DESC";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    /**
     * @Method 通过Id查询云记对象
     * @Description
     * @author Mr
     * @date 2022/2/11 20:16
     * @param noteId
     * @return Note
     */
    public Note findNoteById(String noteId) {
        //定义sql语句
        String sql = "select noteId,title,content,pubTime,typeName,n.typeId from " +
                " note n inner join note_type t on n.typeId=t.typeId where noteId = ?";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(noteId);
        //调用BaseDao的查询方法
        Note note = (Note) BaseDao.queryRow(sql, params, Note.class);

        return note;

    }

    /**
     * @Method 通过noteId删除云记录，返回受影响的行数
     * @Description
     * @author Mr
     * @date 2022/2/11 20:58
     * @param noteId
     * @return int
     */
    public int deleteNoteById(String noteId) {
        //定义sql语句，注意delete后面没有加*
        String sql = "delete from note where noteId = ?";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(noteId);
        //调用BaseDao
        int row = BaseDao.executeUpdate(sql, params);

        return row;

    }

    /**
     * @Method 通过用户Id查询云记列表
     * @Description
     * @author Mr
     * @date 2022/2/12 13:37
     * @param userId
     * @return List<Note>
     */
    public List<Note> queryNoteList(Integer userId) {
        //定义sql语句(因为note里面没有userId，所以要内联两张表)
        String sql = "select lon,lat from note n inner join note_type t on " +
                " n.typeId = t.typeId where userId = ?";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //调用BaseDao
        List<Note> list = BaseDao.queryRows(sql, params, Note.class);

        return list;
    }
}
