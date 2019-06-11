package cn.dcxy.demo.controller;

import cn.dcxy.demo.repository.BgmRepository;
import cn.dcxy.demo.utils.DcJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="背景音乐业务的接口", tags= {"背景音乐业务的controller"})
@RequestMapping("/bgm")
public class BgmController {
	
	@Autowired
	private BgmRepository bgmRepository;
	
	@ApiOperation(value="获取背景音乐列表", notes="获取背景音乐列表的接口")
	@PostMapping("/list")
	public DcJSONResult list() {
		return DcJSONResult.ok(bgmRepository.findAll());
	}
	
}
