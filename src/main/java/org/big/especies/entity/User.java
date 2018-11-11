package org.big.especies.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName User
 * @Author WangTianshan (王天山)
 * @Date 18-11-8 22:40</p>
 * @Version: 0.1
 * <p>Copyright: WangTianshan - 王天山</p>
 * @Since JDK 1.80_171
 */
@Entity
public class User {
    private String id;
    private String username;
    private String pwd;
    private String email;
    private String countryCode;
    private String mobile;
    private int status;
    private String nickname;
    private String realName;
    private String profilePicture;
    private Timestamp signUpTime;
    private Timestamp lastSignInTime;
    private String verificationCode;
    private Date verificationCodeExpiryTime;

    public User() {
    }

    public User(User user){
        this.id = user.getId();
        this.username = user.getUsername();
//        this.role = user.getRole();
        this.email = user.getEmail();
        this.pwd = user.getPwd();
        this.nickname = user.getNickname();
        this.lastSignInTime = user.getLastSignInTime();
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "pwd")
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "country_code")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Basic
    @Column(name = "profile_picture")
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Basic
    @Column(name = "sign_up_time")
    public Timestamp getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(Timestamp signUpTime) {
        this.signUpTime = signUpTime;
    }

    @Basic
    @Column(name = "last_sign_in_time")
    public Timestamp getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Timestamp lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    @Basic
    @Column(name = "verification_code")
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Basic
    @Column(name = "verification_code_expiry_time")
    public Date getVerificationCodeExpiryTime() {
        return verificationCodeExpiryTime;
    }

    public void setVerificationCodeExpiryTime(Date verificationCodeExpiryTime) {
        this.verificationCodeExpiryTime = verificationCodeExpiryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return status == user.status &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(pwd, user.pwd) &&
                Objects.equals(email, user.email) &&
                Objects.equals(countryCode, user.countryCode) &&
                Objects.equals(mobile, user.mobile) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(realName, user.realName) &&
                Objects.equals(profilePicture, user.profilePicture) &&
                Objects.equals(signUpTime, user.signUpTime) &&
                Objects.equals(lastSignInTime, user.lastSignInTime) &&
                Objects.equals(verificationCode, user.verificationCode) &&
                Objects.equals(verificationCodeExpiryTime, user.verificationCodeExpiryTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username, pwd, email, countryCode, mobile, status, nickname, realName, profilePicture, signUpTime, lastSignInTime, verificationCode, verificationCodeExpiryTime);
    }
}
