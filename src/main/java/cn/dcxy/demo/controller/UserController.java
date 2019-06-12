package cn.dcxy.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.dcxy.demo.entry.Users;
import cn.dcxy.demo.entry.UsersFans;
import cn.dcxy.demo.entry.UsersLikeVideos;
import cn.dcxy.demo.entry.UsersReport;
import cn.dcxy.demo.entry.vo.PublisherVideo;
import cn.dcxy.demo.entry.vo.UsersVO;
import cn.dcxy.demo.org.n3r.idworker.Sid;
import cn.dcxy.demo.repository.UsersReportRepository;
import cn.dcxy.demo.repository.UsersRepository;
import cn.dcxy.demo.repository.rest.UsersFansRestRepository;
import cn.dcxy.demo.repository.rest.UsersLikeVideosRestRepository;
import cn.dcxy.demo.utils.DcJSONResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户相关业务的接口", tags= {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController {
	private Sid sid;
	private final UsersRepository usersRepository;
	private final UsersFansRestRepository usersFansRestRepository;
	private final UsersLikeVideosRestRepository usersLikeVideosRestRepository;
	private final UsersReportRepository usersReportRepository;


	@Autowired
	public UserController(Sid sid, UsersRepository usersRepository, UsersFansRestRepository usersFansRestRepository, UsersLikeVideosRestRepository usersLikeVideosRestRepository, UsersReportRepository usersReportRepository) {
		this.sid = sid;
		this.usersRepository = usersRepository;
		this.usersFansRestRepository = usersFansRestRepository;
		this.usersLikeVideosRestRepository = usersLikeVideosRestRepository;
		this.usersReportRepository = usersReportRepository;
	}




	/**
	 * 用户头像上传
	 * @param userId
	 * @param files
	 * @return 用户头像地址
	 * @throws Exception
	 */
	@ApiOperation(value="用户上传头像", notes="用户上传头像的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true, 
						dataType="String", paramType="query")
	@PostMapping("/uploadFace")
	public DcJSONResult uploadFace(String userId,
								   @RequestParam("file") MultipartFile[] files) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return DcJSONResult.errorMsg("用户id不能为空...");
		}
		// 文件保存的命名空间
		String fileSpace = "C:/imooc_videos_dev";
		// 保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/face";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (files != null && files.length > 0) {
				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					File outFile = new File(finalFacePath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			} else {
				return DcJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DcJSONResult.errorMsg("上传出错...");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}

		Users user= usersRepository.getOne(userId);
		user.setFaceImage(uploadPathDB);
		usersRepository.save(user);
		
		return DcJSONResult.ok(uploadPathDB);
	}

	/**
	 *
	 * @param userId
	 * @param fanId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="查询用户信息", notes="查询用户信息的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true, 
						dataType="String", paramType="query")
	@PostMapping("/query")
	public DcJSONResult query(String userId, String fanId) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return DcJSONResult.errorMsg("用户id不能为空...");
		}

		Users user= usersRepository.getOne(userId);
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(user, userVO);
		UsersFans usersFans = usersFansRestRepository.queryFollow(userId, fanId);
		if (usersFans != null) {
			userVO.setFollow(true);
		}else {
			userVO.setFollow(false);
		}

		return DcJSONResult.ok(userVO);
	}
	
	
	@PostMapping("/queryPublisher")
	public DcJSONResult queryPublisher(String loginUserId, String videoId, 
			String publishUserId) throws Exception {
		if (StringUtils.isBlank(publishUserId)) {
			return DcJSONResult.errorMsg("");
		}
		// 1. 查询视频发布者的信息
		Users userInfo = usersRepository.getOne(publishUserId);
		System.out.println("publishUserId---"+publishUserId);
		System.out.println(userInfo);
		UsersVO publisher = new UsersVO();
		BeanUtils.copyProperties(userInfo, publisher);
		// 2. 查询当前登录者和视频的点赞关系
		UsersLikeVideos userLikeVideo = usersLikeVideosRestRepository.queryUsersLikeVideos(loginUserId, videoId);
		PublisherVideo bean = new PublisherVideo();
		bean.setPublisher(publisher);
		if (userLikeVideo != null) {
			bean.setUserLikeVideo(true);
		}else {
			bean.setUserLikeVideo(false);
		}
		return DcJSONResult.ok(bean);
	}


	@PostMapping("/beyourfans")
	public DcJSONResult beyourfans(String userId, String fanId) throws Exception {
		System.out.println("userId---"+userId);
		System.out.println("fanId---"+fanId);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return DcJSONResult.errorMsg("");
		}
		String id = sid.nextShort();
		UsersFans usersFans = new UsersFans();
		usersFans.setId(id);
		usersFans.setUserId(userId);
		usersFans.setFanId(fanId);
		usersFansRestRepository.save(usersFans);
		return DcJSONResult.ok("关注成功...");
	}
	
	@PostMapping("/dontbeyourfans")
	public DcJSONResult dontbeyourfans(String userId, String fanId) throws Exception {
		
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return DcJSONResult.errorMsg("");
		}
		UsersFans usersFans = usersFansRestRepository.queryFollow(userId, fanId);
		usersFansRestRepository.delete(usersFans);

		return DcJSONResult.ok("取消关注成功...");
	}
	
	@PostMapping("/reportUser")
	public DcJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {
		
		// 保存举报信息
		usersReportRepository.save(usersReport);
		return DcJSONResult.errorMsg("举报成功...有你平台变得更美好...");
	}
	
}
