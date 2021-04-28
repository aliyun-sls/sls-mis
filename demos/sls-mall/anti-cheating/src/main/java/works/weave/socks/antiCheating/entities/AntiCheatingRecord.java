package works.weave.socks.antiCheating.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class AntiCheatingRecord implements Serializable {


    private static final long serialVersionUID = 5812533871817454847L;
    private Long id;

    private int type;

    private String originalId;

    private String reason;

    private String userId;

    private Date createTime;

    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public AntiCheatingRecord (){}

    public AntiCheatingRecord(IntegralRecord integralRecord) {
        this.originalId = integralRecord.getOriginalId();
        this.type = integralRecord.getType();
        this.reason = integralRecord.getReason();
        this.userId = integralRecord.getUserId();
    }

}
