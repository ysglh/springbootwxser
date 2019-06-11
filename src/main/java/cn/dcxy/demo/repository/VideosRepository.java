package cn.dcxy.demo.repository;
import cn.dcxy.demo.entry.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VideosRepository extends JpaRepository<Videos,String>{
}
