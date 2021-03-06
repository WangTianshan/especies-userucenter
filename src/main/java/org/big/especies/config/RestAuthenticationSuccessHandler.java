package org.big.especies.config;

import org.big.especies.entity.User;
import org.big.especies.entity.UserDetail;
import org.big.especies.service.LocaleService;
import org.big.especies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: WangTianshan(王天山)
 * @Description:
 * @Created Date: 2017/11/6 15:22
 * @Modified By:
 * @Last Modified Date:
 */
@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private LocaleService localeService;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String thisLanguage=localeService.getLanguage(request,response);
        String successMessage="Login Success";

        if(thisLanguage.equals("zh"))
            successMessage="登录成功";
        else if(thisLanguage.equals("en"))
            successMessage="Login Success";
        else
            successMessage="Login Success";
//
//        System.out.println("--------------");
//        User thisUser=(UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println("u="+thisUser.getId());
//        System.out.println("--------------");

//        this.userService.updataLastSignInTime((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\":\""+successMessage+"\"}");
        response.getWriter().flush();

    }

}
