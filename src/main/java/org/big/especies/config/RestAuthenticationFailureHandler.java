package org.big.especies.config;

import org.big.especies.service.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private LocaleService localeService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String thisLanguage=localeService.getLanguage(request,response);
        String loginErrorMsg="error";
        try{
            if(request.getSession().getAttribute("loginError").equals("token")){
                if(thisLanguage.equals("zh"))
                    loginErrorMsg="验证码错误";
                else if(thisLanguage.equals("en"))
                    loginErrorMsg="Verification code is error";
                else
                    loginErrorMsg="Verification code is error";
            }
            if(request.getSession().getAttribute("loginError").equals("name")){
                if(thisLanguage.equals("zh"))
                    loginErrorMsg="无此用户名";
                else if(thisLanguage.equals("en"))
                    loginErrorMsg="No this Username";
                else
                    loginErrorMsg="No this Username";
            }
            else if(request.getSession().getAttribute("loginError").equals("password")){
                if(thisLanguage.equals("zh"))
                    loginErrorMsg="密码错误";
                else if(thisLanguage.equals("en"))
                    loginErrorMsg="Password is error";
                else
                    loginErrorMsg="Password is error";
            }
            else if(request.getSession().getAttribute("loginError").equals("status")){
                request.getSession().setAttribute("adminEmail",fromEmail);
                if(thisLanguage.equals("zh"))
                    loginErrorMsg="该账户未激活";
                else if(thisLanguage.equals("en"))
                    loginErrorMsg="This account was not activated";
                else
                    loginErrorMsg="This account was not activated";
            }
            else if(request.getSession().getAttribute("loginError").equals("disable")){
                if(thisLanguage.equals("zh"))
                    loginErrorMsg="该账户已禁用";
                else if(thisLanguage.equals("en"))
                    loginErrorMsg="This account has been disabled";
                else
                    loginErrorMsg="This account has been disabled";
            }
        }catch(Exception e){
        }

        // TODO Auto-generated method stub
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\":\""+loginErrorMsg+"\"}");
        response.getWriter().flush();
    }

}