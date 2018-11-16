package org.big.especies.controller;

import org.big.especies.entity.User;
import org.big.especies.entity.UserDetail;
import org.big.especies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>User相关的Controller类</b></p>
 *<p> User相关的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/1 10:07</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private String uploadPath = "upload/";

    /**
     *<b>我的数据中心</b>
     *<p> 我的数据中心</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/info", method = {RequestMethod.GET})
    public String Info(Model model) {
        //获取当前登录用户
        model.addAttribute("thisUser", (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/info";
    }

    /**
     *<b>我的个人中心</b>
     *<p> 我的个人中心</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/myinfo", method = {RequestMethod.GET})
    public String MyInfo(Model model) {
        //获取当前登录用户
        User thisUser=(UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("thisUser",this.userService.findbyID(thisUser.getId()));
        return "user/myinfo";
    }

    /**
     *<b>编辑我的信息</b>
     *<p> 编辑我的信息</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/editmyinfo", method = {RequestMethod.GET})
    public String EditMyInfo(Model model) {
        //获取当前登录用户
        User thisUser=(UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("thisUser",this.userService.findbyID(thisUser.getId()));
        return "user/editMyinfo";
    }

    /**
     *<b>添加</b>
     *<p> 添加新的实体的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/add", method = {RequestMethod.GET})
    public String Add(Model model) {
        User thisUser=new User();
        model.addAttribute("thisUser", thisUser);
        return "user/add";
    }

    /**
     *<b>编辑</b>
     *<p> 对已有的实体进行编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @param id 被编辑实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/edit/{id}", method = {RequestMethod.GET})
    public String Edit(Model model, @PathVariable String id) {
        User thisUser=this.userService.findbyID(id);
        model.addAttribute("thisUser", thisUser);
        return "user/edit";
    }

    /**
     *<b>保存</b>
     *<p> 将传入的实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisUser 传入的实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/save", method = {RequestMethod.POST})
    public String Save(@ModelAttribute("thisUser") User thisUser) {
        this.userService.saveOne(thisUser);
        return "redirect:/user";
    }

    /**
     *<b>保存</b>
     *<p> 将传入的实体保存</p>
     * @author WangTianshan (王天山)
     * @param request 传入的
     * @return java.lang.String
     */
    @RequestMapping(value="/myinfo/save", method = {RequestMethod.POST})
    public String SaveMyinfo(Model model, MultipartFile file, HttpServletRequest request) {
        //获取当前登录用户
        User thisUser=(UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String msg=this.userService.saveMyinfo(this.userService.findbyID(thisUser.getId()),file,request,uploadPath+"profile/");
        if(msg.equals("success")){
            return "redirect:/user/myinfo";
        }
        else {
            if(msg.equals("size_error")){
                model.addAttribute("errorMsg","图片大小请小于50Mb");
            }
            else if(msg.equals("type_error")){
                model.addAttribute("errorMsg","文件类型错误");
            }
            else if(msg.equals("file_error")){
                model.addAttribute("errorMsg","图片转化错误");
            }
            else {
                model.addAttribute("errorMsg","ERROE");
            }
            thisUser.setNickname(request.getParameter("nickname"));
            model.addAttribute("thisUser",thisUser);
            return "user/editMyinfo";
        }
    }


}