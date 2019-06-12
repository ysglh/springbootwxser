package cn.dcxy.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.dcxy.demo.entry.*;
import cn.dcxy.demo.entry.vo.CommentsVO;
import cn.dcxy.demo.entry.vo.VideosVO;
import cn.dcxy.demo.org.n3r.idworker.Sid;
import cn.dcxy.demo.controller.BasicController;
import cn.dcxy.demo.enums.VideoStatusEnum;
import cn.dcxy.demo.repository.*;
import cn.dcxy.demo.repository.rest.UsersLikeVideosRestRepository;
import cn.dcxy.demo.repository.rest.VideosRestRepository;
import cn.dcxy.demo.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Api(value = "视频相关业务的接口", tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {
    @Autowired
    private BgmRepository bgmRepository;
    @Autowired
    private VideosRepository videosRepository;
    @Autowired
    private VideosRestRepository videosRestRepository;
    @Autowired
    private Sid sid;
    @Autowired
    private SearchRecordsRepository searchRecordsRepository;
    @Autowired
    private UsersLikeVideosRestRepository usersLikeVideosRestRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private CommentsVORepository commentsVORepository;

    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public DcJSONResult upload(String userId,
                               String bgmId, double videoSeconds,
                               int videoWidth, int videoHeight,
                               String desc,
                               @ApiParam(value = "短视频", required = true)
                                       MultipartFile file) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return DcJSONResult.errorMsg("用户id不能为空...");
        }

        // 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";
        String coverPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalVideoPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                // abc.mp4
                String arrayFilenameItem[] = fileName.split("\\.");
                String fileNamePrefix = "";
                for (int i = 0; i < arrayFilenameItem.length - 1; i++) {
                    fileNamePrefix += arrayFilenameItem[i];
                }
                // fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
//				String fileNamePrefix = fileName.split("\\.")[0];

                if (StringUtils.isNotBlank(fileName)) {

                    finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";

                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
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

        // 判断bgmId是否为空，如果不为空，
        // 那就查询bgm的信息，并且合并视频，生产新的视频
        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmRepository.getOne(bgmId);
            String mp3InputPath = FILE_SPACE + bgm.getPath();
            System.out.println("mp3InputPath===" + mp3InputPath);
            MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
            String videoInputPath = finalVideoPath;
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDB = "/" + userId + "/video" + "/" + videoOutputName;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            tool.convertor(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
        }
        System.out.println("uploadPathDB=" + uploadPathDB);
        System.out.println("finalVideoPath=" + finalVideoPath);

        // 对视频进行截图
        FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
        videoInfo.getCover(finalVideoPath, FILE_SPACE + coverPathDB);

        // 保存视频信息到数据库
        Videos video = new Videos();
        String id = sid.nextShort();
        video.setId(id);
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
        videosRepository.save(video);
        return DcJSONResult.ok(id);
    }

    @ApiOperation(value = "上传封面", notes = "上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频主键id", required = true,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
    public DcJSONResult uploadCover(String userId,
                                    String videoId,
                                    @ApiParam(value = "视频封面", required = true)
                                            MultipartFile file) throws Exception {
        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
            return DcJSONResult.errorMsg("视频主键id和用户id不能为空...");
        }
        // 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalCoverPath = "";
        try {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
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
        Videos video = videosRepository.getOne(videoId);
        video.setCoverPath(uploadPathDB);
        videosRepository.save(video);
        return DcJSONResult.ok();
    }

    /**
     * @Description: 分页和搜索查询视频列表
     * isSaveRecord：1 - 需要保存
     * 0 - 不需要保存 ，或者为空的时候
     */
    @PostMapping(value = "/showAll")
    public DcJSONResult showAll(@RequestBody Videos video, Integer isSaveRecord,
                                Integer page, Integer pageSize) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        // 保存热搜词
        String desc = video.getVideoDesc();
        String userId = video.getUserId();
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);
            searchRecordsRepository.save(record);
        }
        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosRestRepository.queryAllVideos(desc, userId);
        PageInfo<VideosVO> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());
        return DcJSONResult.ok(pagedResult);
    }

    /**
     * @Description: 我关注的人发的视频
     */
    @PostMapping("/showMyFollow")
    public DcJSONResult showMyFollow(String userId, Integer page) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return DcJSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        int pageSize = 6;
        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosRestRepository.queryMyFollowVideos(userId);
        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return DcJSONResult.ok(pagedResult);
    }

    /**
     * @Description: 我收藏(点赞)过的视频列表
     */
    @PostMapping("/showMyLike")
    public DcJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return DcJSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }
        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosRestRepository.queryMyFollowVideos(userId);
        PageInfo<VideosVO> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());
        return DcJSONResult.ok(pagedResult);
    }

    @PostMapping(value = "/hot")
    public DcJSONResult hot() throws Exception {
        return DcJSONResult.ok(videosRestRepository.getHotwords());
    }

    @PostMapping(value = "/userLike")
    public DcJSONResult userLike(String userId, String videoId, String videoCreaterId)
            throws Exception {
        // 1. 保存用户和视频的喜欢点赞关联关系表
        String likeId = sid.nextShort();
        UsersLikeVideos ulv = new UsersLikeVideos();
        ulv.setId(likeId);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        usersLikeVideosRestRepository.save(ulv);

        // 2. 视频喜欢数量累加
        Videos videos = videosRepository.getOne(videoId);
        Long setLikeCounts=videos.getLikeCounts()+1;
        videos.setLikeCounts(setLikeCounts);
        videosRepository.save(videos);

        // 3. 用户受喜欢数量的累加
        Users users = usersRepository.getOne(videoCreaterId);
        Integer receiveLikeCounts=users.getReceiveLikeCounts()+1;
        users.setReceiveLikeCounts(receiveLikeCounts);
        usersRepository.save(users);

        return DcJSONResult.ok();
    }

    @PostMapping(value = "/userUnLike")
    public DcJSONResult userUnLike(String userId, String videoId, String videoCreaterId) throws Exception {
        // 1. 删除用户和视频的喜欢点赞关联关系表
        UsersLikeVideos usersLikeVideos = usersLikeVideosRestRepository.queryUsersLikeVideos(userId,videoId);
        if(usersLikeVideos != null){
            usersLikeVideosRestRepository.delete(usersLikeVideos);
        }

        // 2. 视频喜欢数量累减
        Videos videos = videosRepository.getOne(videoId);
        Long setLikeCounts=videos.getLikeCounts()-1;
        videos.setLikeCounts(setLikeCounts);
        videosRepository.save(videos);

        // 3. 用户受喜欢数量的累减

        Users users = usersRepository.getOne(videoCreaterId);
        Integer receiveLikeCounts=users.getReceiveLikeCounts()-1;
        users.setReceiveLikeCounts(receiveLikeCounts);
        usersRepository.save(users);
        return DcJSONResult.ok();
    }

    @PostMapping("/saveComment")
    public DcJSONResult saveComment(@RequestBody Comments comment,
                                    String fatherCommentId, String toUserId) throws Exception {

        comment.setFatherCommentId(fatherCommentId);
        comment.setToUserId(toUserId);
        String id = sid.nextShort();
        comment.setId(id);
        comment.setCreateTime(new Date());
        commentsRepository.save(comment);

        return DcJSONResult.ok();
    }

    @PostMapping("/getVideoComments")
    public DcJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {
        if (StringUtils.isBlank(videoId)) {
            return DcJSONResult.ok();
        }
        // 分页查询视频列表，时间顺序倒序排序
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageHelper.startPage(page, pageSize);
        List<CommentsVO> list = commentsVORepository.queryComments(videoId);
        for (CommentsVO c : list) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }
        PageInfo<CommentsVO> pageList = new PageInfo<>(list);
        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(list);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());
        return DcJSONResult.ok(grid);
    }

}
