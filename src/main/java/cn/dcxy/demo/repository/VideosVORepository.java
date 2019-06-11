package cn.dcxy.demo.repository;

import cn.dcxy.demo.entry.vo.VideosVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VideosVORepository extends JpaRepository<VideosVO,String>{
}
