package cn.dcxy.demo.controller;

import java.util.UUID;

import cn.dcxy.demo.entry.Users;
import cn.dcxy.demo.entry.vo.UsersVO;
import cn.dcxy.demo.repository.UsersRepository;
import cn.dcxy.demo.repository.rest.UsersRestRepository;
import cn.dcxy.demo.utils.DcJSONResult;
import cn.dcxy.demo.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
public class RegistLoginController extends BasicController {

	private final UsersRepository usersRepository;
	private final UsersRestRepository usersRestRepository;
	@Autowired
	public RegistLoginController(UsersRepository usersRepository, UsersRestRepository usersRestRepository) {
		this.usersRepository = usersRepository;
		this.usersRestRepository = usersRestRepository;
	}


	@ApiOperation(value="用户注册", notes="用户注册的接口")
	@PostMapping("/regist")
	public DcJSONResult regist(@RequestBody Users user) throws Exception {
		
		// 1. 判断用户名和密码必须不为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return DcJSONResult.errorMsg("用户名和密码不能为空");
		}
		// 2. 判断用户名是否存在
		Users username = usersRestRepository.findUsersByName(user.getUsername());
		// 3. 保存用户，注册信息
		if (username == null ) {
			user.setNickname(user.getUsername());
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			user.setFansCounts(0);
			user.setReceiveLikeCounts(0);
			user.setFollowCounts(0);
			usersRepository.save(user);
		} else {
			return DcJSONResult.errorMsg("用户名已经存在，请换一个再试");
		}
		user.setPassword("");
		UsersVO userVO = setUserRedisSessionToken(user);
		
		return DcJSONResult.ok(userVO);
	}
	
	public UsersVO setUserRedisSessionToken(Users userModel) {
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userModel, userVO);
		userVO.setUserToken(uniqueToken);
		return userVO;
	}
	
	@ApiOperation(value="用户登录", notes="用户登录的接口")
	@PostMapping("/login")
	public DcJSONResult login(@RequestBody Users user) throws Exception {
		String username = user.getUsername();
		String password = user.getPassword();
//		Thread.sleep(3000);
		// 1. 判断用户名和密码必须不为空
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return DcJSONResult.ok("用户名或密码不能为空...");
		}
		
		// 2. 判断用户是否存在
		Users userResult = usersRestRepository.queryUserForLogin(username,
				MD5Utils.getMD5Str(user.getPassword()));
		
		// 3. 返回
		if (userResult != null) {
			userResult.setPassword("");
			UsersVO userVO = setUserRedisSessionToken(userResult);
			return DcJSONResult.ok(userVO);
		} else {
			return DcJSONResult.errorMsg("用户名或密码不正确, 请重试...");
		}
	}
	
	@ApiOperation(value="用户注销", notes="用户注销的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true, 
						dataType="String", paramType="query")
	@PostMapping("/logout")
	public DcJSONResult logout(String userId) throws Exception {
		redis.del(USER_REDIS_SESSION + ":" + userId);
		return DcJSONResult.ok();
	}
	
}
