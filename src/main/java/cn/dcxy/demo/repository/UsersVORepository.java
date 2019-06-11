package cn.dcxy.demo.repository;
import cn.dcxy.demo.entry.Comments;
import cn.dcxy.demo.entry.vo.UsersVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersVORepository extends JpaRepository<UsersVO,String>{
}
