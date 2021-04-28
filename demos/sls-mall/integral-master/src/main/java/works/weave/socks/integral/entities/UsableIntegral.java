package works.weave.socks.integral.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class UsableIntegral implements Serializable {

    private static final long serialVersionUID = -6763623416448160609L;

    private Long id;

    private Long recordId;

    private Float value;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expireTime;

    private String userId;

    private int deleted;

    private Date createTime;

    private Date updateTime;

    public UsableIntegral(){}

    public UsableIntegral(IntegralRecord integralRecord) {
        this.expireTime = integralRecord.getExpireTime();
        this.userId = integralRecord.getUserId();
        this.value = integralRecord.getValue();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
