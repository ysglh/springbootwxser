package cn.dcxy.demo.repository;
import cn.dcxy.demo.entry.Bgm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BgmRepository extends JpaRepository<Bgm,String>{

}
