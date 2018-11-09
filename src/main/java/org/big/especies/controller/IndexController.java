package org.big.especies.controller;

import org.big.especies.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>IndexController类</b></p>
 *<p> Index的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/1 10:23</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
public class IndexController {
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private HttpServletRequest request;

    /**
     *<b>默认页面</b>
     *<p> 登录后自动跳转的主页面</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/", method = {RequestMethod.GET})
    public String Index(Model model) {
        String loginErrorMsg="";
        try{
            request.getSession().setAttribute("adminEmail",fromEmail);
            System.out.println("fromEmail="+fromEmail);
            if(request.getSession().getAttribute("loginError").equals("name")){
                model.addAttribute("loginError", 1);
                loginErrorMsg="无此用户名";
            }
            else if(request.getSession().getAttribute("loginError").equals("password")){
                model.addAttribute("loginError", 1);
                loginErrorMsg="密码错误";
            }
            else if(request.getSession().getAttribute("loginError").equals("token")){
                model.addAttribute("loginError", 1);
                loginErrorMsg="验证码错误";
            }
            else{
                model.addAttribute("loginError", 0);
                User newUser =new User();
                model.addAttribute("newUser", newUser);
            }
            model.addAttribute("loginErrorMsg", loginErrorMsg);
        }catch(Exception e){
        }

        User newUser =new User();
        model.addAttribute("newUser", newUser);
        return "index";
    }
}