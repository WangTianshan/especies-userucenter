package org.big.especies.controller;

import org.big.especies.entity.User;
import org.big.especies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;
    @Value("${spring.mail.username}") private String adminEmail;

    /**
     *<b>注册页面</b>
     *<p> 注册页面</p>
     * @author WangTianshan (王天山)
     * @param model 空的model
     * @return java.lang.String
     */
    @RequestMapping(value="", method = {RequestMethod.GET})
    public String register(Model model) {
        User newUser =new User();
        model.addAttribute("newUser", newUser);
        return "user/register";
    }

    /**
     *<b>注册</b>
     *<p> 注册</p>
     * @author WangTianshan (王天山)
     * @param newUser newUser
     * @return java.lang.String
     */
    @RequestMapping(value="/new", method = {RequestMethod.POST})
    public String newUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("newUser") User newUser, Model model) {
        String registerMsg=this.userService.registerNewOne(request,response,newUser);
        if(registerMsg.equals("success")){
            model.addAttribute("signType", "signIn");
            model.addAttribute("resultType", "signUpSuccess");
            model.addAttribute("alertMessage", registerMsg);
            model.addAttribute("newUser", newUser);
            model.addAttribute("adminEmail", adminEmail);
            return "index";
        }
        else{
            model.addAttribute("newUser", newUser);
            model.addAttribute("signType", "signUp");
            model.addAttribute("resultType", "signUpError");
            model.addAttribute("errorMessage", registerMsg);
            return "index";
        }
    }

    /**
     *<b>注册成功页面</b>
     *<p> 注册成功页面</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/success")
    public String success(Model model, HttpServletRequest request) {
        //model.addAttribute("registerEmail", request.getParameter("email"));
        return "user/registerSuccess";

    }

    /**
     *<b>激活结果页面</b>
     *<p> 激活结果页面</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/active/{username}/{mark}")
    public String active(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String username, @PathVariable String mark) {
        String activeMsg=userService.activeUser(username,mark,request,response);
        model.addAttribute("activeMsg", activeMsg);
        request.getSession().setAttribute("adminEmail",adminEmail);
        if(activeMsg.equals("此账户已激活")||activeMsg.equals("This account has been activated")){
            model.addAttribute("activeStatus", true);
        }
        else{
            model.addAttribute("activeStatus", false);
        }
        String signType="signIn";
        User newUser =new User();
        newUser.setUsername(username);
        model.addAttribute("newUser", newUser);
        model.addAttribute("signType", signType);

        return "index";
    }
}