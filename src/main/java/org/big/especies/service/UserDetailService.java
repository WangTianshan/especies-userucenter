package org.big.especies.service;

//import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.big.especies.entity.User;
import org.big.especies.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>UserDetail的Service类接口</b></p>
 *<p> UserDetail的Service类接口，与Spring Security中保存用户信息有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/10/31 15:37</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service("UserDetailImpl")
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    /**
     *<b>根据用户名读取当前用户的信息</b>
     *<p> 用户登录功能的部分</p>
     * @author WangTianshan (王天山)
     * @param key 用户输入的用户名orEmail
     * @return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String key) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.findOneByEmailOrUsername(key);
        } catch (Exception e) {
            throw new UsernameNotFoundException("读取数据库失败");
        }
        if(user == null){
            request.getSession().setAttribute("loginError","name");
            throw new UsernameNotFoundException("nameError");
        }
        else {
            try {
                return new UserDetail(user);
            } catch (Exception e) {
                request.getSession().setAttribute("loginError","password");
                throw new UsernameNotFoundException("passwordError");
            }
        }
    }
}