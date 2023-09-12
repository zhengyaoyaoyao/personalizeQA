package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.Entity;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Relation;
import com.personalize.personalizeqa.mapper.EntityMapper;
import com.personalize.personalizeqa.mapper.RelationMapper;
import com.personalize.personalizeqa.server.IRelationService;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.EntityListVO;
import com.personalize.personalizeqa.vo.RelationListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements IRelationService {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private RelationMapper relationMapper;
    @Override
    public boolean insert(String relationName, String description, String annotationName) {
        String id = idGenerate.generate().toString();
        LocalDateTime now = LocalDateTime.now();
        UserDTO user = UserHolder.getUser();
        String username = user.getUsername();
        Relation relation = Relation.builder().id(id).relationName(relationName).annotationName(annotationName).description(description).createTime(now).createUser(username).updateTime(now).updateUser(username).build();
        boolean save = save(relation);
        return save;
    }

    @Override
    public R<Page<Relation>> findAll(Integer curPage, Integer maxPage, String keyword) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("rel_name",keyword);
        Page<Relation> dataSetPage = new Page<>(curPage,maxPage);
        Page<Relation> result = baseMapper.selectPage(dataSetPage, queryWrapper);
        return R.success(result);
    }

    @Override
    public Boolean deleteById(String id) {
        boolean isDelete = removeById(id);
        return isDelete;
    }

    @Override
    public Boolean updateById(String id, String description, String relationName, String annotationName) {
        Relation byId = getById(id);
        byId.setRelationName(relationName);
        byId.setDescription(description);
        byId.setAnnotationName(annotationName);
        UserDTO user = UserHolder.getUser();
        byId.setUpdateUser(user.getUsername());
        LocalDateTime now = LocalDateTime.now();
        byId.setUpdateTime(now);
        boolean isUpdate = updateById(byId);
        return isUpdate;
    }

    @Override
    public RelationListVO relationList() {
        List<Map<String,String>> entityNames = relationMapper.selectRelationNames();
        RelationListVO relationListVO = new RelationListVO();
        relationListVO.setRelationList(entityNames);
        return relationListVO;
    }

    @Override
    public boolean isNotExist(String relationName) {
        //判断是否存在，
        LambdaQueryWrapper<Relation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Relation::getRelationName, relationName);

        // 使用 count 方法来获取匹配记录的数量
        int count = count(queryWrapper);

        // 如果记录数量大于 0，则说明存在
        return (count == 0);
    }
}
