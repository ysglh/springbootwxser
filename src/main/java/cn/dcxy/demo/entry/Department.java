package cn.dcxy.demo.entry;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Department {
    @Id
    private Integer id;

    /**
     * 部门
     */
    private String departmentname;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取部门
     *
     * @return departmentname - 部门
     */
    public String getDepartmentname() {
        return departmentname;
    }

    /**
     * 设置部门
     *
     * @param departmentname 部门
     */
    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }
}