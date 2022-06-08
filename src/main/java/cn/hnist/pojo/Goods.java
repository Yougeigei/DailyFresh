package cn.hnist.pojo;

import com.gitee.sunchenbin.mybatis.actable.annotation.Table;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 商品SPU模型类
 */
@Entity
@Table(name="goods")
public class Goods implements Serializable {

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String name;    // 商品名称
    private String detail;  // 商品详情


    @Id
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
