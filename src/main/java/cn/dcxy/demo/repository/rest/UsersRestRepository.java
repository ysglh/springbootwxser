package cn.dcxy.demo.repository.rest;

import cn.dcxy.demo.entry.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRestRepository extends PagingAndSortingRepository<Users,String> {
    @Query(value = "select u.id,u.username, u.password, u.face_image, u.nickname, u.fans_counts,u.follow_counts, u.receive_like_counts from users u where u.username =?1",nativeQuery = true)
    Users findUsersByName(String username);

    @Query(value = "select u.id,u.username, u.password, u.face_image, u.nickname, u.fans_counts,u.follow_counts, u.receive_like_counts from users u where u.username =?1 and u.password=?2",nativeQuery = true)
    Users queryUserForLogin(String username,String password);
}
