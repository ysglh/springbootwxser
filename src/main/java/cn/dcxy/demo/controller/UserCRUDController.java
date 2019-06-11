package cn.dcxy.demo.controller;

import cn.dcxy.demo.entry.Users;
import cn.dcxy.demo.utils.DcJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/crud")
public class UserCRUDController {
	
	@RequestMapping("/save")
	public DcJSONResult save() {

		return DcJSONResult.ok();
	}
	
	@RequestMapping("/update")
	public DcJSONResult update() {
		

		
		return DcJSONResult.ok();
	}
	
	@RequestMapping("/update2")
	public DcJSONResult update2() {

		
		return DcJSONResult.ok();
	}
	
	@RequestMapping("/delUser")
	public DcJSONResult delUser() {

		
		return DcJSONResult.ok();
	}
}
