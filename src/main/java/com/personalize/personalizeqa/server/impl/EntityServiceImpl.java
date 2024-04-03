package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.entity.DataSet;
import com.personalize.personalizeqa.entity.Entity;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.TaskType;
import com.personalize.personalizeqa.mapper.EntityMapper;
import com.personalize.personalizeqa.mapper.TaskTypeMapper;
import com.personalize.personalizeqa.server.IEntityService;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.EntityListVO;
import com.personalize.personalizeqa.vo.TaskTypeListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EntityServiceImpl extends ServiceImpl<EntityMapper, Entity> implements IEntityService {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private EntityMapper entityMapper;
    @Override
    public boolean insert(String entityName, String description,String annotationName) {
        String id = idGenerate.generate().toString();
        LocalDateTime now = LocalDateTime.now();
        Entity entity = Entity.builder().id(id).entityName(entityName).annotationName(annotationName).description(description).createTime(now).createUser("0").updateTime(now).updateUser("0").build();
        boolean save = save(entity);
        return save;
    }

    @Override
    public R<Page<Entity>> findAll(Integer curPage, Integer maxPage, String keyword) {
        QueryWrapper<Entity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",keyword);
        Page<Entity> dataSetPage = new Page<>(curPage,maxPage);
        Page<Entity> result = baseMapper.selectPage(dataSetPage, queryWrapper);
        return R.success(result);
    }

    @Override
    public Boolean deleteById(String id) {
        boolean isDelete = removeById(id);
        return isDelete;
    }

    @Override
    public Boolean updateById(String id, String description, String entityName,String annotationName) {
        Entity byId = getById(id);
        byId.setEntityName(entityName);
        byId.setDescription(description);
        byId.setAnnotationName(annotationName);
        byId.setUpdateUser("1");
        LocalDateTime now = LocalDateTime.now();
        byId.setUpdateTime(now);
        boolean isUpdate = updateById(byId);
        return isUpdate;
    }

    @Override
    public EntityListVO entityList() {
        List<Map<String,String>> entityNames = entityMapper.selectEntityNames();
        EntityListVO entityListVO = new EntityListVO();
        entityListVO.setEntityList(entityNames);
        return entityListVO;
    }
    /**
     * 确保实体唯一性
     */
    @Override
    public boolean isNotExist(String entityName) {
        //判断是否存在，
        LambdaQueryWrapper<Entity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Entity::getEntityName, entityName);

        // 使用 count 方法来获取匹配记录的数量
        long count = count(queryWrapper);

        // 如果记录数量大于 0，则说明存在
        return (count == 0);
    }
}
