package works.weave.socks.integral.entities;


import java.io.Serializable;
import java.util.Date;

public class ReduceIntegralDetail implements Serializable {

    private static final long serialVersionUID = -4008648249090183027L;

    private Long id;

    private Long reduceId;

    private Long addId;

    private Float value;

    private Date expireTime;

    private String userId;

    private int deleted;

    private Date createTime;

    private Date updateTime;

    public ReduceIntegralDetail(){}

    public ReduceIntegralDetail(UsableIntegral usableIntegral){
        this.reduceId = usableIntegral.getId();
        this.value = usableIntegral.getValue();
        this.expireTime = usableIntegral.getExpireTime();
        this.userId = usableIntegral.getUserId();
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

    public Long getReduceId() {
        return reduceId;
    }

    public void setReduceId(Long reduceId) {
        this.reduceId = reduceId;
    }

    public Long getAddId() {
        return addId;
    }

    public void setAddId(Long addId) {
        this.addId = addId;
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
