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
    @Query(value = "SELECT " +
            "c.id," +
            "c.comment," +
            "c.create_time," +
            "u.face_image AS face_image," +
            "c.from_user_id," +
            "c.to_user_id AS time_ago_str," +
            "c.video_id," +
            "u.nickname," +
            "tu.nickname AS to_nickname " +
            "from comments c " +
            "LEFT JOIN users u ON c.from_user_id = u.id " +
            "left JOIN users tu ON c.to_user_id = tu.id " +
            "where c.video_id =?1 ORDER BY c.create_time DESC",nativeQuery = true)
    List<CommentsVO> queryComments(String videoId);


}
