package org.big.especies.controller.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.big.especies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *<p><b>UserController的Rest风格类</b></p>
 *<p> UserController的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/1 10:19</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/user/rest")
public class UserRestController {
    @Autowired
    private UserService userService;
    /**
     *<b>统计首页上所需要的所有数量</b>
     *<p> 统计首页上所需要的所有数量</p>
     * @author WangTianshan (王天山)
     * @return long
     */
    @RequestMapping(value="/updata/signInTime",method = {RequestMethod.GET})
    public void UpdataLastSignInTime() {
        this.userService.updataLastSignInTime();
    }
//
//    /**
//     *<b>统计首页上所需要的所有数量</b>
//     *<p> 统计首页上所需要的所有数量</p>
//     * @author WangTianshan (王天山)
//     * @return long
//     */
//    @RequestMapping(value="/count/idrecord",method = {RequestMethod.GET})
//    public JSON Idrecord(HttpServletRequest request, HttpServletResponse response) {
//        JSON myGroup=this.idrecordService.countMyIdrecord(request,response);
//        return myGroup;
//    }
}