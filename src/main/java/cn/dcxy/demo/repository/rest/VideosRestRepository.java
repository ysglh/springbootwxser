package cn.dcxy.demo.repository.rest;
import cn.dcxy.demo.entry.Users;
import cn.dcxy.demo.entry.Videos;
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
}
