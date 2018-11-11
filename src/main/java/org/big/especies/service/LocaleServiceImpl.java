package org.big.especies.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.springframework.web.servlet.DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE;
import static org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME;

/**
 *<p><b>Locale的Service类</b></p>
 *<p> Locale的Service类，与Locale有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/9 15:31</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class LocaleServiceImpl implements LocaleService {

    @Autowired
    LocaleResolver localeResolver;

    @Override
    public String getLanguage(HttpServletRequest request, HttpServletResponse response) {
        String thisLocale = LocaleContextHolder.getLocale().getLanguage();
        if(request.getSession().getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME)==null){
            if(thisLocale.equals("zh_CN")||thisLocale.equals("zh")){
                localeResolver.setLocale(request,response, Locale.CHINESE);
                request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
            }
            else if(thisLocale.equals("en")) {
                localeResolver.setLocale(request, response, Locale.ENGLISH);
                request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
            }
            else{
                localeResolver.setLocale(request,response, Locale.ENGLISH);
                request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
            }
        }
        HttpSession session=request.getSession();
        return session.getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME).toString();
    }

    @Override
    public Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        Locale thisLocale = LocaleContextHolder.getLocale();
        if(request.getSession().getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME)==null){
            if(thisLocale.equals("zh_CN")||thisLocale.equals("zh")){
                localeResolver.setLocale(request,response, Locale.CHINESE);
                request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
            }
            else if(thisLocale.equals("en")) {
                localeResolver.setLocale(request, response, Locale.ENGLISH);
                request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
            }
            else{
                localeResolver.setLocale(request,response, Locale.ENGLISH);
                request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
            }
        }
        HttpSession session=request.getSession();
        return (Locale)session.getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME);
    }
}