package works.weave.socks.antiCheating.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


public class IntegralRecord implements Serializable {

    private static final long serialVersionUID = 77471777883288200L;
    private int type;

    private String originalId;

    private Float value;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expireTime;

    private String reason;

    private String userId;

    public IntegralRecord() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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

    @Override
    public String toString() {
        return "IntegralRecord{" +
                "type=" + type +
                ", originalId='" + originalId + '\'' +
                ", value=" + value +
                ", expireTime=" + expireTime +
                ", reason='" + reason + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
