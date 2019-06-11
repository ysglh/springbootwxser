package cn.dcxy.demo.entry;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Book {
    @Id
    private Long id;

    /**
     * 作者
     */
    private String author;

    /**
     * 描述
     */
    private String description;

    /**
     * 书名
     */
    private String name;

    /**
     * 是否存在
     */
    private Boolean status;

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
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取书名
     *
     * @return name - 书名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置书名
     *
     * @param name 书名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取是否存在
     *
     * @return status - 是否存在
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置是否存在
     *
     * @param status 是否存在
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }
}