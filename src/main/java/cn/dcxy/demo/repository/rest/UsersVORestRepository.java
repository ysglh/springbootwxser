package cn.dcxy.demo.repository.rest;
import cn.dcxy.demo.entry.vo.UsersVO;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersVORestRepository extends PagingAndSortingRepository<UsersVO,String> {
}
