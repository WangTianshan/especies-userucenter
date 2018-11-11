package org.big.especies.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

/**
 *<p><b>UserDetail的Entity类</b></p>
 *<p> 此类用于操作SpringSecurity的存储访问用户的重写</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/10/31 14:54</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class UserDetail extends User implements UserDetails {
    private int status;
    private String profilePicture;
    private String code;
    private String email;
    private String countryCode;
    private String mobile;
    private String nickname;
    private String realName;
    private Timestamp signUpTime;
    private Timestamp lastSignInTime;

    public UserDetail(User user){
        super(user);
        this.status=user.getStatus();
        this.profilePicture=user.getProfilePicture();
        this.email=user.getEmail();
        this.countryCode=user.getCountryCode();
        this.mobile=user.getMobile();
        this.nickname=user.getNickname();
        this.realName=user.getRealName();
        this.signUpTime=user.getSignUpTime();
        this.lastSignInTime=user.getLastSignInTime();
        this.code=user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("USER");
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPwd();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    //@Override
    public String getName() {
        return super.getNickname();
    }


    public int getStatus() {
        return status;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getCode() {
        return code;
    }

    public String getId(){
        return super.getId();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getMobile() {
        return mobile;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getRealName() {
        return realName;
    }

    @Override
    public Timestamp getSignUpTime() {
        return signUpTime;
    }

    @Override
    public Timestamp getLastSignInTime() {
        return lastSignInTime;
    }

    @Override
    public void setLastSignInTime(Timestamp lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }
}

