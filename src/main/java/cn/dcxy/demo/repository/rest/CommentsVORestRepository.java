package cn.dcxy.demo.repository.rest;
import cn.dcxy.demo.entry.vo.CommentsVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentsVORestRepository extends PagingAndSortingRepository<CommentsVO,String> {


}
