package org.big.especies.config;

import org.big.especies.entity.UserDetail;
import org.big.especies.service.LocaleService;
import org.big.especies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String thisLanguage=localeService.getLanguage(request,response);
        String loginErrorMsg="error";
        try{
            if(request.getSession().getAttribute("loginError").equals("token")){
                loginErrorMsg=messageSource.getMessage("msg_sign_in_token_error", null, localeService.getLocale(request,response));
            }
            if(request.getSession().getAttribute("loginError").equals("name")){
                loginErrorMsg=messageSource.getMessage("msg_sign_in_key_error", null, localeService.getLocale(request,response));
            }
            else if(request.getSession().getAttribute("loginError").equals("password")){
                loginErrorMsg=messageSource.getMessage("msg_sign_in_password_error", null, localeService.getLocale(request,response));
            }
            else if(request.getSession().getAttribute("loginError").equals("status")){
                request.getSession().setAttribute("adminEmail",fromEmail);
                loginErrorMsg=messageSource.getMessage("msg_sign_in_status_error", null, localeService.getLocale(request,response));
            }
            else if(request.getSession().getAttribute("loginError").equals("disable")){
                loginErrorMsg=messageSource.getMessage("msg_sign_in_status_disable", null, localeService.getLocale(request,response));
            }
        }catch(Exception e){
        }

        // TODO Auto-generated method stub
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\":\""+loginErrorMsg+"\",\"title\":\""+messageSource.getMessage("alert_warning", null, localeService.getLocale(request,response))+"\"}");
        response.getWriter().flush();
    }

}