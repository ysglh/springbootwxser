package cn.dcxy.demo.repository.rest;
import cn.dcxy.demo.entry.Users;
import cn.dcxy.demo.entry.Videos;
import cn.dcxy.demo.entry.vo.VideosVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VideosRestRepository extends PagingAndSortingRepository<Videos,String>{
    @Query( value = "select " +
            "v.id," +
            "v.user_id," +
            " v.audio_id," +
            "v.video_desc," +
            "v.video_path," +
            "v.video_seconds," +
            "v.video_width," +
            "v.video_height," +
            "v.cover_path," +
            "v.like_counts," +
            "v.status," +
            "v.create_time " +
            "from videos v where v.user_id = ?1",nativeQuery = true )
    List<Videos> getAllVideos(String userId);
    @Query( value = "select " +
            "v.id," +
            " v.audio_id," +
            "v.cover_path," +
            "v.create_time " +
            "u.face_image " +
            "v.like_counts," +
            "u.nickname," +
            "v.status," +
            "v.user_id," +
            "v.video_desc," +
            "v.video_height," +
            "v.video_path," +
            "v.video_seconds," +
            "v.video_width," +
            "from videos v left on users u on v.user_id = u.user_id where v.user_id = ?1 and v.video_desc=?2",nativeQuery = true )
    List<VideosVO> queryAllVideos(String desc, String userId);

    @Query( value = "select " +
            "v.id," +
            " v.audio_id," +
            "v.cover_path," +
            "v.create_time " +
            "u.face_image " +
            "v.like_counts," +
            "u.nickname," +
            "v.status," +
            "v.user_id," +
            "v.video_desc," +
            "v.video_height," +
            "v.video_path," +
            "v.video_seconds," +
            "v.video_width," +
            "from videos v left on users u on v.user_id = u.user_id where v.user_id = ?1",nativeQuery = true )
    List<VideosVO> queryMyFollowVideos(String userId);

   @Query(value = "SELECT sr.content FROM search_records sr GROUP BY sr.content ORDER BY COUNT(sr.content) DESC",nativeQuery = true)
    List<String> getHotwords();
}
