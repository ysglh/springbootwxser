package cn.dcxy.demo.repository.rest;
import cn.dcxy.demo.entry.vo.UsersVO;
import cn.dcxy.demo.entry.vo.VideosVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VideosVORestRepository extends PagingAndSortingRepository<VideosVO,String> {

    @Query(value = "SELECT videos.id,videos.audio_id, videos.video_desc,videos.video_path," +
            "videos.video_seconds,videos.video_width,videos.video_height,videos.cover_path," +
            "videos.like_counts,videos.status,videos.create_time,videos.user_id,users.face_image," +
            "users.nickname,users.face_image FROM  users RIGHT JOIN videos ON videos.user_id = users.id",nativeQuery = true)
    VideosVO queryAll();
}
