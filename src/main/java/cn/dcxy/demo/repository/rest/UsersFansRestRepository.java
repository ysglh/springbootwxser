package cn.dcxy.demo.repository.rest;
import cn.dcxy.demo.entry.UsersFans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface UsersFansRestRepository extends PagingAndSortingRepository<UsersFans,String> {
    @Query(value = "select uf.id,uf.user_id, uf.fan_id from users_fans uf where uf.user_id =?1 and uf.fan_id=?2",nativeQuery = true)
    UsersFans queryFollow(String userId, String fanId);

    @Query(value = "select count(uf.fan_id) from users_fans uf where uf.user_id =?1",nativeQuery = true)
    Integer queryUsersLikeCount(String userId);
}
