package cn.dcxy.demo.repository;
import cn.dcxy.demo.entry.Bgm;
import cn.dcxy.demo.entry.vo.CommentsVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentsVORepository extends PagingAndSortingRepository<CommentsVO,String> {
    @Query(value = "SELECT cvo.id,cvo.comment,cvo.create_time,cvo.face_image,cvo.from_user_id,cvo.nickname,cvo.time_ago_str,cvo.to_nickname,cvo.video_id FROM commentsvo cvo WHERE cvo.video_id=?1",nativeQuery = true)
    List<CommentsVO> queryComments(String videoId);


}
