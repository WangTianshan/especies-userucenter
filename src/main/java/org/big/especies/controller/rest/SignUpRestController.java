package org.big.especies.controller.rest;

import com.alibaba.fastjson.JSON;
import org.big.especies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>超级管理员Controller的Rest风格类</b></p>
 *<p> 超级管理员Controller的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/1 10:20</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/signup/rest")
public class SignUpRestController {
    @Autowired
    private UserService userService;

    /**
     *<b>用户名重复</b>
     *<p> 用户名重复</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return Boolean
     */
    @RequestMapping(value="/isReName", method = {RequestMethod.GET})
    public Boolean isReName(HttpServletRequest request) {
        if(userService.findOneByName(request.getParameter("name"))!=null)
            return true;
        else
            return false;
    }

    /**
     *<b>邮箱重复</b>
     *<p> 邮箱重复</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return Boolean
     */
    @RequestMapping(value="/isReEmail", method = {RequestMethod.GET})
    public Boolean isReEmail(HttpServletRequest request) {
        if(userService.findOneByEmail(request.getParameter("email"))!=null)
            return true;
        else
            return false;
    }
}