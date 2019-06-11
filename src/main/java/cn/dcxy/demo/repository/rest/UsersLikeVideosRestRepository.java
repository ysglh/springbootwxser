package cn.dcxy.demo.repository.rest;
import cn.dcxy.demo.entry.UsersFans;
import cn.dcxy.demo.entry.UsersLikeVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UsersLikeVideosRestRepository extends JpaRepository<UsersLikeVideos,String>{
    @Query(value = "select ulv.id,ulv.user_id, ulv.video_id from users_like_videos ulv where ulv.user_id =?1 and ulv.video_id=?2",nativeQuery = true)
    UsersLikeVideos queryUsersLikeVideos(String userId, String video_id);

    @Query(value = "select COUNT(ulv.user_id) from users_like_videos ulv where ulv.video_id =?1",nativeQuery = true)
    Long queryVideosLikeCount(String video_id);
}
