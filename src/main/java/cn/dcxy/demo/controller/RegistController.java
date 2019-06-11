package cn.dcxy.demo.controller;

import cn.dcxy.demo.entry.Users;
import cn.dcxy.demo.repository.UsersRepository;
import cn.dcxy.demo.repository.rest.UsersRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistController {

	private final UsersRepository usersRepository;
	private final UsersRestRepository usersRestRepository;
    @Autowired
	public RegistController(UsersRepository usersRepository, UsersRestRepository usersRestRepository) {
		this.usersRepository = usersRepository;
		this.usersRestRepository = usersRestRepository;
	}

	@ResponseBody
	@GetMapping("/hello")
	public HttpEntity<?> index(){
		Users us = usersRepository.getOne("180518CKMAAM5TXP");
		System.out.println(us);
		Users uss = usersRestRepository.findUsersByName("abc123");
		System.out.println(uss);
		return new ResponseEntity<>(uss, HttpStatus.OK);
	}
//
//	@PostMapping("/regist")
//	public DcJSONResult Hello(Users users) {
//		//判断用户名和密码必需不为null
//		if(StringUtils.isEmpty(users.getUsername())||StringUtils.isEmpty(users.getPassword())){
//			return DcJSONResult.errorMsg("用户名和密码不能为空");
//		}
//		Users us = usersRestRepository.findUsersByName(users.getUsername());
//		if(us == null){
//			return DcJSONResult.errorMsg("用户不存在请注册用户");
//		}else {
//			return DcJSONResult.ok();
//		}
//
//	}
	
}
