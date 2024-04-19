package com.personalize.personalizeqa.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.annotationEntity.OperationLogging;
import com.personalize.personalizeqa.dto.LoginFormDTO;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Relation;
import com.personalize.personalizeqa.entity.TaskType;
import com.personalize.personalizeqa.entity.User;
import com.personalize.personalizeqa.enumeration.OperationType;
import com.personalize.personalizeqa.server.IUserService;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.vo.UserInfoVO;
import com.personalize.personalizeqa.vo.UserLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin // 允许来自指定源的请求
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginFormDTO loginFormDTO, HttpServletRequest request){
        log.info("{}",loginFormDTO);
        // TODO 实现登录功能
        R<String> loginState =  userService.login(loginFormDTO,request);
        return loginState;
    }
    @GetMapping("/profile")
    public R<UserLoginInfoVO> profile(){
        UserLoginInfoVO userLoginInfoVO = userService.profile();
        return R.success(userLoginInfoVO,"用户信息获取成功");
    }
    @GetMapping("/users")
    public R<Page<UserInfoVO>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        R<Page<UserInfoVO>> usersList = userService.findAll(page,perPage,keyword);
        return usersList;
    }
    @GetMapping("/isNotExist/{username}")
    public R<Boolean> isNotExist(@PathVariable("username")String username){
        boolean noExist = userService.isNotExist(username);
        return R.success(noExist);
    }
    @OperationLogging(description = "新建用户",type = OperationType.INSERT)
    @PostMapping("/user")
    public R<Boolean> insertUser(@RequestParam("username")String username,@RequestParam("nickName")String nickName,@RequestParam("phone")String phone,@RequestParam("password")String password,
                                 @RequestParam("organization")String organization,@RequestParam("authority")String authority,@RequestParam("status")Boolean status){
        Boolean isInsert = userService.insert(username,nickName,phone,password,organization,authority,status);
        if (isInsert){
            return R.success(isInsert,"新建成功");
        }else {
            return R.fail("新建任务失败,请重新再试");
        }
    }
    @GetMapping("/users/{id}")
    public R<User>  searchUserById(@PathVariable("id")String id){
        User user = userService.searchUserById(id);
        return  R.success(user);
    }
    @OperationLogging(description = "更新用户信息",type =OperationType.UPDATE)
    @PutMapping("/user")
    public R<Boolean> updateUser(@RequestParam("id")String id,@RequestParam("username")String username,@RequestParam("nickName")String nickName,@RequestParam("phone")String phone,@RequestParam("password")String password,
                                 @RequestParam("organization")String organization,@RequestParam("authority")String authority,@RequestParam("status")Boolean status){
        Boolean isUpdate =  userService.updateUser(id,username,nickName,phone,password,organization,authority,status);
        if (isUpdate){
            return R.success(isUpdate,"更新成功");
        }else {
            return R.fail("更新任务失败,请重新再试");
        }
    }
    @OperationLogging(description = "删除用户信息",type = OperationType.DELETE)
    @DeleteMapping("/users/{id}")
    public R<Boolean> deleteById(@PathVariable("id")String id){
        Boolean isDelete =  userService.deleteById(id);
        return R.success(isDelete);
    }
    @GetMapping("/organizations")
    public R<List<Map<String, List<String>>>> organizations(){
        List<Map<String, List<String>>>  organizationUser = userService.organizations();
        return R.success(organizationUser);
    }
}
