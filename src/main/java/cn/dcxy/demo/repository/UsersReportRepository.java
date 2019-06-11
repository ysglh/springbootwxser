package cn.dcxy.demo.repository;
import cn.dcxy.demo.entry.Book;
import cn.dcxy.demo.entry.UsersReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersReportRepository extends JpaRepository<UsersReport,String>{
}
