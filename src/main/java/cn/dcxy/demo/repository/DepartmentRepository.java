package cn.dcxy.demo.repository;
import cn.dcxy.demo.entry.Bgm;
import cn.dcxy.demo.entry.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer>{
}
