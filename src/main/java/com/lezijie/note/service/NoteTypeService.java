package com.lezijie.note.service;

import cn.hutool.core.util.StrUtil;
import com.lezijie.note.dao.NoteTypeDao;
import com.lezijie.note.po.NoteType;
import com.lezijie.note.vo.ResultInfo;

import java.util.List;

/**
 * Topic
 * Description
 *
 * @author zhouh
 * @version 1.0
 * Create by 2022/2/8 19:24
 */
public class NoteTypeService {
    private NoteTypeDao typeDao = new NoteTypeDao();

    /**
     * @Method
     * @Description 查询类型列表
     *                  1.调用Dao层的查询方法，通过用户ID查询类型集合
     *                  2.返回类型集合
     * @author Mr
     * @date 2022/2/8 19:37
     * @param userId
     * @return List<NoteType>
     */
    public List<NoteType> findTypeList(Integer userId) {
        List<NoteType> typeList = typeDao.findTypeListByUserId(userId);
        return typeList;

    }

    /**
     * @Method 删除类型
     * @Description
     * @author Mr
     * @date 2022/2/12 16:43
     * @param typeId
     * @return ResultInfo<NoteType>
     */
    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();
        //1.判断参数是否为空
        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常，请重试！");
            return resultInfo;
        }
        //2.调用Dao层，通过类型ID查询云记记录的数量
        long noteCount = typeDao.findNoteCountByTypeId(typeId);
        //3.如果云记数量大于0，说明存在子记录，不可删除
        if (noteCount > 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("该类型存在子记录，不可删除！！");
            return resultInfo;
        }
        //4.如果不存在子记录，调用Dao层的更新方法，通过类型ID删除指定的类型记录，返回受影响的行数
        int row = typeDao.deleteTypeById(typeId);
        //5.判断受影响的行数是否大于0
        if (row > 0) {
            resultInfo.setCode(1);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败！");
        }
        return resultInfo;
    }

    /**
     * @Method 添加或修改类型
     * @Description
     * @author Mr
     * @date 2022/2/12 17:20
     * @param typeName
     * @param userId
     * @param typeId
     * @return ResultInfo<Integer>
     */
    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId, String typeId) {
        ResultInfo<Integer> resultInfo = new ResultInfo<>();
        //1.判断参数是否为空（类型名称）
        if (StrUtil.isBlank(typeName)) {
            resultInfo.setResult(0);
            resultInfo.setMsg("类型名称不能为空！");
            return resultInfo;
        }
        // 2.(添加操作，类型名称都不能与当前用户下的类型名称重复)
        // ！！！这一步实现添加的时候如果类型名重复，弹出类型名称已存在
        // 类型名称不为空之后，调用Dao层，查询当前登录用户下，类型名称是否唯一，返回0或1（1=可用；0=不可用）
        Integer code = typeDao.checkTypeName(typeName, userId, typeId);
        if (code == 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，请重新输入！");
            return resultInfo;
        }
        //3.判断类型Id是否为空
        //返回的结果
        Integer key = null;   //主键或受影响的行数
        if (StrUtil.isBlank(typeId)) {
            //如果为空，说明是添加操作，调用Dao层的添加方法，返回主键（前台页面需要显示成功之后的类型Id）
            key = typeDao.addType(typeName, userId);   //key为主键1-...从1开始
        } else {
            //如果不为空，说明是修改操作，调用Dao层的修改方法，返回受影响的行数(更新操作不需要主键就能更新，只需要id和name)
            key = typeDao.updateType(typeName, typeId);   //key返回的受影响的行数
        }
        //4.判断主键受影响的行数 是否大于0
        if (key > 0) {
            resultInfo.setCode(1);
            //成功设置key
            resultInfo.setResult(key);
        } else {
            resultInfo.setCode(0);
            //失败会弹出msg
            resultInfo.setMsg("更新失败！");
            //失败不传结果
        }
        return resultInfo;
    }
}
