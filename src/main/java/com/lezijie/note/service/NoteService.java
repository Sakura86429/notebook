package com.lezijie.note.service;

import cn.hutool.core.util.StrUtil;
import com.lezijie.note.dao.NoteDao;
import com.lezijie.note.po.Note;
import com.lezijie.note.util.Page;
import com.lezijie.note.vo.NoteVo;
import com.lezijie.note.vo.ResultInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Topic
 * Description
 *
 * @author zhouh
 * @version 1.0
 * Create by 2022/2/9 19:36
 */
public class NoteService {
    private NoteDao noteDao = new NoteDao();

    /**
     * @Method 添加或修改操作
     * @Description
     * @author Mr
     * @date 2022/2/9 21:43
     * @param typeId
     * @param title
     * @param content
     * @param noteId
     * @param lon
     * @param lat
     * @return ResultInfo<Note>
     */
    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content, String noteId, String lon, String lat) {
        //结果为Note类型的resultInfo
        ResultInfo<Note> resultInfo = new ResultInfo<>();
        //1.参数的非空判断
        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择云记类型！");
            return resultInfo;
        }
        if (StrUtil.isBlank(title)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记标题不能为空！");
            return resultInfo;
        }
        if (StrUtil.isBlank(content)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记内容不能为空！");
            return resultInfo;
        }

        //设置经纬度的默认值，默认设置为北京：116，404，39，915
        if (lon == null || lat == null) {
            lon = "116.404";
            lat = "39.915";
        }

        //2.设置回显对象 Note对象
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        //typeId转成int放进来
        note.setTypeId(Integer.parseInt(typeId));
        note.setLon(Float.parseFloat(lon));
        note.setLat(Float.parseFloat(lat));

        //判断云记Id是否为空
        if (!StrUtil.isBlank(noteId)) {
            note.setNoteId(Integer.parseInt(noteId));
        }
        resultInfo.setResult(note);

        //3.调用Dao层，添加云记记录，返回受影响的行数
        //只有一个函数的时候，和Web层Service层中的函数名一样
        int row = noteDao.addOrUpdate(note);

        //4.判断受影响的行数
        if (row > 0) {
            resultInfo.setCode(1);
        } else {
            resultInfo.setCode(0);
            resultInfo.setResult(note);
            resultInfo.setMsg("更新失败！");
        }
        return resultInfo;

    }

    /**
     * @Method 分页查询云记列表
     * @Description
     * @author Mr
     * @date 2022/2/10 22:21
     * @param pageNumStr
     * @param pageSizeStr
     * @param userId
     * @param title
     * @param date
     * @param typeId
     * @return Page<Note>
     */
    //为了方便加上Str
    public Page<Note> findNoteListByPage(String pageNumStr, String pageSizeStr, Integer userId, String title, String date, String typeId) {
        //设置分页的默认值
        Integer pageNum = 1;   //默认当前页是第一页
        Integer pageSize = 3;   //默认每页显示10条数据
        //1.参数的非空校验
        if (!StrUtil.isBlank(pageNumStr)) {
            //设置当前页
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (!StrUtil.isBlank(pageSizeStr)) {
            //设置每页显示的数量
            pageSize = Integer.parseInt(pageSizeStr);
        }
        //2.查询当前登录用户的云记数量，返回总记录（long类型）
        long count = noteDao.findNoteCount(userId, title, date, typeId);
        //3.判断总记录是否大于0
        if (count < 1) {
            return null;
        }
        //4.如果总记录大于0，调用Page类的带参构造，得到其他分页参数的值，返回Page对象
        Page<Note> page = new Page<>(pageNum, pageSize, count);
        //得到数据库中分页查询的开始下标(注意：pageNum（值）下标从1开始)
        Integer index = (pageNum - 1) * pageSize;
        //5.查询当前登录用户下当前页的数据列表，返回note集合（index为0-n的List数据索引下标）
        List<Note> noteList = noteDao.findNoteListByPage(userId, index, pageSize, title, date, typeId);
        //6.将note集合设置到page对象中(set的变量名自动首字母大写)
        page.setDataList(noteList);
        //7.返回Page对象
        return null;
    }

    /**
     * @Method 通过日期分组查询当前登录用户下的云记数量
     * @Description
     * @author Mr
     * @date 2022/2/11 16:23
     * @param userId
     * @return List<NoteVo>
     */
    public List<NoteVo> findNoteCountByDate(Integer userId) {
        return noteDao.findNoteCountByDate(userId);
    }

    /**
     * @Method 通过类别分组查询当前登录用户下的云记数量
     * @Description
     * @author Mr
     * @date 2022/2/11 16:52
     * @param userId
     * @return List<NoteVo>
     */
    public List<NoteVo> findNoteCountByType(Integer userId) {
        return noteDao.findNoteCountByType(userId);
    }

    /**
     * @Method 查询云记详情
     * @Description
     * @author Mr
     * @date 2022/2/11 20:14
     * @param noteId
     * @return Note
     */
    public Note findNoteById(String noteId) {
        //1.参数的非空判断
        if (StrUtil.isBlank(noteId)) {
            return null;
        }
        //2.调用Dao层的查询，通过noteId查询note对象
        Note note = noteDao.findNoteById(noteId);
        //3.返回note对象
        return note;
    }

    /**
     * @Method 通过用户id从detail中删除云记
     * @Description
     * @author Mr
     * @date 2022/2/11 20:55
     * @param noteId
     * @return Integer
     */
    public Integer deleteNote(String noteId) {
        //1.判断参数,StrUtil.isBlank只针对字符串
        if (StrUtil.isBlank(noteId)) {
            return 0;
        }
        //2.调用Dao层的更新方法，返回受影响的行数
        int row = noteDao.deleteNoteById(noteId);
        //3.判断受影响的行数是否大于0
        if (row > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * @Method 通过月份查询对应的云记数量
     * @Description
     * @author Mr
     * @date 2022/2/11 23:01
     * @param userId
     * @return ResultInfo<Map<Object>>
     */
    public ResultInfo<Map<String, Object>> queryNoteCountByMonth(Integer userId) {
        ResultInfo<Map<String, Object>> resultInfo = new ResultInfo<>();
        //通过月份分类查询云记数量
        List<NoteVo> noteVos = noteDao.findNoteCountByDate(userId);
        //判断集合是否存在
        if (noteVos != null && noteVos.size() > 0) {
            //得到月份
            List<String> monthList = new ArrayList<>();
            //得到云集集合
            List<Integer> noteCountList = new ArrayList<>();
            //遍历月份分组集合
            for (NoteVo noteVo: noteVos) {
                monthList.add(noteVo.getGroupName());
                noteCountList.add((int) noteVo.getNoteCount());
            }
            //准备Map对象，封装对应的月份与云记数量
            Map<String , Object> map = new HashMap<>();
            map.put("monthArray", monthList);
            map.put("dataArray", noteCountList);
            //将map对象设置到ResultInfo对象中
            resultInfo.setCode(1);
            resultInfo.setResult(map);
        }

        return resultInfo;
    }

    /**
     * @Method 查询用户发布云记时的经纬度坐标
     * @Description
     * @author Mr
     * @date 2022/2/12 13:34
     * @param userId
     * @return ResultInfo<List<Note>>
     */
    public ResultInfo<List<Note>> queryNoteLonAndLat(Integer userId) {
        ResultInfo<List<Note>> resultInfo = new ResultInfo<>();
        //通过用户Id查询云记记录
        List<Note> noteList = noteDao.queryNoteList(userId);
        //判断是否为空
        if (noteList != null && noteList.size() > 0) {
            resultInfo.setCode(1);
            resultInfo.setResult(noteList);
        }

        return resultInfo;
    }
}
