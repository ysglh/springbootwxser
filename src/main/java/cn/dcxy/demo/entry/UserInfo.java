package cn.dcxy.demo.entry;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密钥
     */
    private String appkey;

    private Long phone;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取密钥
     *
     * @return appkey - 密钥
     */
    public String getAppkey() {
        return appkey;
    }

    /**
     * 设置密钥
     *
     * @param appkey 密钥
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    /**
     * @return phone
     */
    public Long getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(Long phone) {
        this.phone = phone;
    }
}