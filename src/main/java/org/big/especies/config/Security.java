package org.big.especies.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>SpringSecurity配置类</b></p>
 *<p> 配置SpringSecurity</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/10/31 14:50</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private RestAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private RestAuthenticationSuccessHandler successHandler;

    /**
     *<b>配置详情</b>
     *<p> 配置参数详情</p>
     * @author WangTianshan (王天山)
     * @param http
     * @return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().and()
                .headers()
                .frameOptions().sameOrigin()
                .xssProtection()
                .block(true)
                .and();

        http
                .headers()
                .cacheControl()
                .and()
                .contentTypeOptions()
                .and()
                .httpStrictTransportSecurity()
                .and()
                .xssProtection();
        http
                .authorizeRequests()
                //忽略静态文件
                .antMatchers("/images/**").permitAll()
                //忽略静态文件
                .antMatchers("/plugins/**").permitAll()
                //忽略静态文件
                .antMatchers("/css/**").permitAll()
                //忽略静态文件
                .antMatchers("/js/**").permitAll()
                //忽略静态文件
                .antMatchers("/bowerComponents").permitAll()
                //验证码
                .antMatchers("/captchaImg").permitAll()
                //所有人可访问的
                .antMatchers("/login").permitAll()
                //ROLE_USER、ROLE_SUPER权限可以访问的请求
                .antMatchers("/user/**").hasAnyAuthority("ROLE_USER","ROLE_TAXONOMIST","ROLE_SUPER")
                //ROLE_SUPER权限可以访问的请求
                .antMatchers("/super/**").hasAnyAuthority("ROLE_SUPER")
                .and()

                .formLogin() // 登陆表单
                .loginPage("/").permitAll()
                .failureHandler(authenticationFailureHandler) // failure handler
                .successHandler(successHandler) // success handler
                .loginProcessingUrl("/login").permitAll() // premit all authority
                .authenticationDetailsSource(authenticationDetailsSource)

//                //指定登录页面的请求路径
//                .formLogin().loginPage("/login_page").permitAll()
//                // 登陆处理路径
//                .loginProcessingUrl("/login").permitAll()
//                .defaultSuccessUrl("/", true)
//                .authenticationDetailsSource(authenticationDetailsSource)
//                .and()

                .and()
                .logout()
                //退出请求的默认路径为logout
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                //成功退出登录
                .logoutSuccessUrl("/")
                .and()
                .csrf();

        http.csrf().ignoringAntMatchers("/login");
    }

    /**
     *<b>全局应用</b>
     *<p> 全局应用</p>
     * @author WangTianshan (王天山)
     * @param auth
     * @return void
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
