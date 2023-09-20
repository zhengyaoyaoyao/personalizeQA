package com.personalize.personalizeqa.server.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.LoginFormDTO;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.User;
import com.personalize.personalizeqa.mapper.UserMapper;
import com.personalize.personalizeqa.server.IUserService;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.UserInfoVO;
import com.personalize.personalizeqa.vo.UserLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.personalize.personalizeqa.utils.RedisConstants.LOGIN_USER_KEY;
import static com.personalize.personalizeqa.utils.RedisConstants.LOGIN_USER_TTL;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private UserMapper userMapper;
    private static final String USER_CODE="user:code:";
    @Override
    public R<String> login(LoginFormDTO loginFormDTO, HttpServletRequest request) {
        String curUsername = loginFormDTO.getUsername();
        //判断用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,curUsername);
        int isExist = count(queryWrapper);
        if (isExist==0){
            return R.fail("用户不存在");
        }
        User user = getOne(queryWrapper);
        String curPassword = loginFormDTO.getPassword();
        if (!StringUtils.equals(curPassword,user.getPassword())){
            return R.fail("密码错误");
        }
        if (!user.isStatus()){
            return R.fail("用户不可操作,请联系管理员");
        }
        //登录成功，更新其ip信息和登录时间
        String loginIp =request.getRemoteAddr();
        LocalDateTime loginUpdate = LocalDateTime.now();
        user.setLoginIP(loginIp);
        user.setLoginTime(loginUpdate);
        updateById(user);

        //7. 保存用户信息到redis中
        // 7.1 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
        Map<String,Object> userMap = BeanUtil.beanToMap(userDTO,new HashMap<>(), CopyOptions.create().setIgnoreNullValue(true)
                .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString()));
        //7.3 存储
        String tokenKey = LOGIN_USER_KEY+token;
        stringRedisTemplate.opsForHash().putAll(tokenKey,userMap);
        //7.4设置token有效期
        stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL, TimeUnit.MINUTES);
        return R.success(token,"登录成功");
    }

    @Override
    public UserLoginInfoVO profile() {
        UserDTO userDTO = UserHolder.getUser();
        String authority  = userDTO.getAuthority();
        List<String> authorities = userMapper.getAuthorities(authority);
        UserLoginInfoVO userLoginInfoVO = UserLoginInfoVO.builder().id(userDTO.getId()).username(userDTO.getUsername()).nickName(userDTO.getNickName()).authority(authorities).build();
        return userLoginInfoVO;
    }

    @Override
    public R<Page<UserInfoVO>> findAll(Integer curPage, Integer maxPage, String keyword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username",keyword);
        Page<User> userPage = new Page<>(curPage,maxPage);
        Page<User> resultUser = baseMapper.selectPage(userPage,queryWrapper);
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        List<User> records = resultUser.getRecords();
        for (User user:records){
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setId(user.getId());
            userInfoVO.setUsername(user.getUsername());
            userInfoVO.setUserCode(user.getUserCode());
            userInfoVO.setNickName(user.getNickName());
            userInfoVO.setAuthority(user.getAuthority());
            userInfoVO.setOrganization(user.getOrganization());
            userInfoVO.setPhone(user.getPhone());
            userInfoVO.setCreateTime(user.getCreateTime());
            userInfoVO.setLoginTime(user.getLoginTime());
            userInfoVO.setLoginIp(user.getLoginIP());
            userInfoVO.setStatus(user.isStatus());
            userInfoVOList.add(userInfoVO);
        }
        Page<UserInfoVO> userInfoVOPage = new Page<>(curPage,maxPage);
        userInfoVOPage.setRecords(userInfoVOList);
        userInfoVOPage.setTotal(resultUser.getTotal());
        userInfoVOPage.setCurrent(resultUser.getCurrent());
        userInfoVOPage.setSize(resultUser.getSize());
        return R.success(userInfoVOPage);
    }

    @Override
    public boolean isNotExist(String username) {
        //判断是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        int count = count(queryWrapper);
        return (count==0);
    }

    @Override
    public Boolean insert(String username, String nickName, String phone, String password, String organization, String authority, Boolean status) {
        String userId = idGenerate.generate().toString();
        LocalDateTime now = LocalDateTime.now();
        UserDTO createUser = UserHolder.getUser();//创建和更新的用户
        //生成人物代码
        organization = StringUtils.upperCase(organization);
        String userCode = codeGenerate(organization);
        authority = StringUtils.lowerCase(authority.trim());
        User userInsert = User.builder().id(userId).username(username).userCode(userCode).nickName(nickName).phone(phone).password(password).organization(organization).authority(authority).createTime(now)
                .createUser(createUser.getUsername()).updateUser(createUser.getUsername()).updateTime(now).status(status).build();
        boolean save = save(userInsert);
        return save;
    }
    private String codeGenerate(String organization){
        LocalDateTime now = LocalDateTime.now();
        String time = DateUtil.format(now,"yyyyMM");
        organization = StringUtils.lowerCase(organization.trim());
        long count = stringRedisTemplate.opsForValue().increment(USER_CODE+organization);
        DecimalFormat df = new DecimalFormat("0000");
        String formatted = df.format(count%10000);
        String result = StringUtils.upperCase(organization)+time+formatted;
        return result;
    }

    @Override
    public Boolean updateUser(String id, String username, String nickName, String phone, String password
            , String organization, String authority, Boolean status) {
        LocalDateTime updateTime = LocalDateTime.now();
        UserDTO user = UserHolder.getUser();
        User byId = getById(id);
        byId.setUsername(username);
        byId.setNickName(nickName);
        byId.setPhone(phone);
        byId.setPassword(password);
        byId.setOrganization(organization);
        byId.setAuthority(authority);
        byId.setStatus(status);
        byId.setUpdateTime(updateTime);
        byId.setUpdateUser(user.getUsername());
        boolean isUpdate = updateById(byId);
        return isUpdate;
    }

    @Override
    public User searchUserById(String id) {
        User byId = getById(id);
        return byId;
    }
    /**
     * 删除用户
     */
    @Override
    public Boolean deleteById(String id) {
        boolean b = removeById(id);
        return b;
    }

    @Override
    public List<Map<String, List<String>>> organizations() {
        List<Map<String, List<String>>> maps = userMapper.selectUsersByOrganization();
        return maps;
    }
}
