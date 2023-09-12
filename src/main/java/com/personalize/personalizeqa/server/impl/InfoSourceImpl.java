package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.InfoSource;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.mapper.InfoSourceMapper;
import com.personalize.personalizeqa.mapper.RelationMapper;
import com.personalize.personalizeqa.server.IInfoSourceService;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.InfoSourceListVO;
import com.personalize.personalizeqa.vo.TaskGetInfoSourcesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class InfoSourceImpl extends ServiceImpl<InfoSourceMapper, InfoSource> implements IInfoSourceService {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private InfoSourceMapper infoSourceMapper;

    @Override
    public boolean insert(String infoSourceName, String infoSourceUrl, String infoSourceRule, String infoSourceDesc) {
        String id = idGenerate.generate().toString();
        LocalDateTime now = LocalDateTime.now();
        UserDTO user = UserHolder.getUser();
        String username = user.getUsername();
        InfoSource infoSource = InfoSource.builder().id(id).infoSourceName(infoSourceName).infoSourceUrl(infoSourceUrl).infoSourceRule(infoSourceRule).infoSourceDesc(infoSourceDesc).createTime(now).createUser(username).updateTime(now).updateUser(username).build();
        boolean save = save(infoSource);
        return save;
    }

    @Override
    public R<Page<InfoSource>> findAll(Integer curPage, Integer maxPage, String keyword) {
        QueryWrapper<InfoSource> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("info_source_name",keyword);
        Page<InfoSource> infoSourcePage = new Page<>(curPage,maxPage);
        Page<InfoSource> result = baseMapper.selectPage(infoSourcePage, queryWrapper);
        return R.success(result);
    }

    @Override
    public Boolean deleteById(String id) {
        boolean b = removeById(id);
        return b;
    }

    @Override
    public Boolean updateById(String id, String infoSourceName, String infoSourceUrl, String infoSourceRule, String infoSourceDesc) {
        InfoSource infoSource = getById(id);
        infoSource.setInfoSourceName(infoSourceName);
        infoSource.setInfoSourceUrl(infoSourceUrl);
        infoSource.setInfoSourceRule(infoSourceRule);
        infoSource.setInfoSourceDesc(infoSourceDesc);
        //更新用户和时间
        UserDTO user = UserHolder.getUser();
        LocalDateTime now = LocalDateTime.now();
        infoSource.setUpdateTime(now);
        infoSource.setUpdateUser(user.getUsername());
        boolean b = updateById(infoSource);
        return b;
    }

    @Override
    public InfoSourceListVO infoSourceList() {
        List<Map<String, String>> infoSourceNames = infoSourceMapper.selectInfoSourceNames();
        InfoSourceListVO infoSourceListVO = new InfoSourceListVO();
        infoSourceListVO.setInfoSourceList(infoSourceNames);
        return infoSourceListVO;
    }

    @Override
    public boolean isNotExist(String infoSourceName) {
        LambdaQueryWrapper<InfoSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InfoSource::getInfoSourceName,infoSourceName);
        int count = count(queryWrapper);
        return count==0;
    }

    @Override
    public List<TaskGetInfoSourcesVO> getInfoSources() {
        List<TaskGetInfoSourcesVO> infoSources = infoSourceMapper.getInfoSources();
        return infoSources;
    }

    @Override
    public InfoSource getByName(String infoSourceName) {
        InfoSource infoSourceName1 = getOne(new QueryWrapper<InfoSource>().eq("info_source_name", infoSourceName));
        return infoSourceName1;
    }
}
