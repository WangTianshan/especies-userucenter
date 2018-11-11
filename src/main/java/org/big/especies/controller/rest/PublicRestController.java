package org.big.especies.controller.rest;

import com.alibaba.fastjson.JSON;
import org.big.especies.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
public class PublicRestController {
    private String uploadPath = "upload/";

    @Autowired
    private UserService userService;

    /**
     *<b>远程登陆</b>
     *<p> 远程登陆</p>
     * @author WangTianshan (王天山)
     * @param request
     * @return java.lang.String
     */
    @RequestMapping(value = "/rest/remoteLogin", method = RequestMethod.POST)
    public JSON Login(HttpServletRequest request){
        return userService.remoteLogin(request);
    }
}