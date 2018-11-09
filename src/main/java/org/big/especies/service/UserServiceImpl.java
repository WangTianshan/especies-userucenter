package org.big.especies.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import org.apache.commons.io.FileUtils;
import org.big.especies.common.MD5Utils;
import org.big.especies.common.QueryTool;
import org.big.especies.entity.*;
import org.big.especies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.big.especies.common.ChangeImage.changeImageSize;

/**
 *<p><b>User的Service类</b></p>
 *<p> User的Service类，与User有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/10/31 15:32</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private LocaleService localeService;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Transactional
    public JSON findbyInfo(HttpServletRequest request) {
        String this_language="en";
        Locale this_locale= LocaleContextHolder.getLocale();
        if(this_locale.getLanguage().equals("zh")){
            this_language="zh";
        }
        if(this_locale.getLanguage().equals("en")){
            this_language="en";
        }
        JSON json= null;
        String searchText=request.getParameter("search");
        if(searchText==null || searchText.length()<=0){
            searchText="";
        }
        int limit_serch=Integer.parseInt(request.getParameter("limit"));
        int offset_serch=Integer.parseInt(request.getParameter("offset"));
        String sort="desc";
        String order="date";
        sort=request.getParameter("sort");
        order=request.getParameter("order");
        if(sort==null || sort.length()<=0){
            sort="dtime";
        }
        if(order==null || order.length()<=0){
            order="desc";
        }
        JSONObject thisTable= new JSONObject();
        JSONArray rows = new JSONArray();
        List<User> thisList=new ArrayList<>();
        Page<User> thisPage=this.userRepository.searchInfo(searchText, QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order));
        thisTable.put("total",thisPage.getTotalElements());
        thisList=thisPage.getContent();
        for(int i=0;i<thisList.size();i++){
            JSONObject row= new JSONObject();
            String thisSelect="<input type='checkbox' name='checkbox' id='sel_"+thisList.get(i).getId()+"' />";
            String thisEdit=
                    "<a class=\"table-edit-icon\" onclick=\"editThisObject('"+thisList.get(i).getId()+"','user')\" >" +
                            "<span class=\"glyphicon glyphicon-edit\"></span>" +
                            "</a>" +
                            "&nbsp;"+
                            "<a class=\"table-edit-icon\" onclick=\"removeThisObject('"+thisList.get(i).getId()+"','user')\" >" +
                            "<span class=\"glyphicon glyphicon-remove\"></span>" +
                            "</a>";
            int statusNum=thisList.get(i).getStatus();
            String thisStatus="未知";
            if (statusNum==1){
                thisStatus="激活";
            }
            else if (statusNum==0){
                thisStatus="未激活";
            }
            else if (statusNum==-1){
                thisStatus="禁用";
            }
            row.put("select",thisSelect);
            row.put("username",thisList.get(i).getUsername());
            row.put("status",thisStatus);
            row.put("nickname",thisList.get(i).getNickname());
            row.put("email",thisList.get(i).getEmail());
            row.put("phone",thisList.get(i).getMobile());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime="";
            try {
                addTime=formatter.format(thisList.get(i).getSignUpTime());
            } catch (Exception e) {
                //e.printStackTrace();
            }
            row.put("dtime",addTime);
            row.put("edit",thisEdit);
            rows.add(i,row);
        }
        thisTable.put("rows",rows);
        json=thisTable;
        return json;
    }

    @Override
    public User findbyID(String ID) {
        return this.userRepository.getOne(ID);
    }

    @Override
    public void saveOne(User thisUser) {
        if(thisUser.getId()==null||thisUser.getId().equals("")||thisUser.getId().length()<=0){
            thisUser.setId(UUID.randomUUID().toString());
            thisUser.setSignUpTime(new Timestamp(System.currentTimeMillis()));
        }
        this.userRepository.save(thisUser);
    }

    @Override
    public String saveMyinfo(User thisUser, MultipartFile file, HttpServletRequest request, String uploadPath) {
        String msg="";
        //图片
        if (null == file || file.isEmpty()) {
            //没有头像
        }
        else{
            if (file.getSize() > 50 * 1024 * 1024) {
               //大小有问题
                msg="size_error";
            }
            else if (!file.getContentType().contains("image")) {
               //文件类型错误
               msg="type_error";
            }
            else{
                String ext = file.getContentType().split("\\/")[1];
                String newFileName = "profile" + "." + ext;
                String realPath = request.getSession().getServletContext().getRealPath(uploadPath);
                //构造路径
                realPath=realPath+thisUser.getId()+"/";
                try {
                    // 先把文件保存到本地
                    FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, newFileName));
                    //压缩
                    changeImageSize(realPath+newFileName, realPath+newFileName,100);
                } catch (IOException e1) {
                    msg="file_error";
                }
                thisUser.setProfilePicture(uploadPath+thisUser.getId()+"/"+newFileName);
            }
        }
        //常规信息
        thisUser.setNickname(request.getParameter("nickname"));
        this.userRepository.save(thisUser);
        msg="success";
        return msg;
    }

    @Override
    public void changeStatus(User thisUser,int status) {
        thisUser.setStatus((byte)status);
        this.userRepository.save(thisUser);
    }

    @Override
    public void removeOne(String ID) {
        this.userRepository.deleteById(ID);
    }

    @Override
    public User findOneByName(String user_name) {
        return this.userRepository.findOneByUsername(user_name);
    }

    @Override
    public User findOneByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }


    @Override
    public String registerNewOne(HttpServletRequest request, HttpServletResponse response, User newUser) {
        String registerMsg="none";
        String thisLanguage=localeService.getLanguage(request,response);
        //Token判断
        if(request.getParameter("token").equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString())){
            //username判断
            if(this.findOneByName(newUser.getUsername())==null) {
                //email判断
                if (this.findOneByEmail(newUser.getEmail()) == null) {
                    //设置用户信息
                    newUser.setStatus((byte)0);
//                    newUser.setMark(UUID.randomUUID().toString());
                    newUser.setPwd(MD5Utils.MD532(newUser.getPwd()));
                    this.saveOne(newUser);

                    //设置激活流程
                    try{
                        //base_url
                        String contextPath=request.getContextPath();
                        String base_url=request.getServerName().toString()+":"+request.getServerPort();
                        if(contextPath!=null && contextPath.length()>0){
                            base_url=base_url+contextPath;
                        }
                        ///邮件的内容
                        StringBuffer sb=new StringBuffer("生物记 Notes of Life<br/>");
                        if(thisLanguage.equals("zh")){
                            sb.append("记录我们身边的生物，公民科学从这里开始！<br/>");
                            sb.append("点击下面链接激活账号，请尽快激活！<br/>");
                            sb.append("<a href=\"http://"+base_url+"/register/active/");
                            sb.append(newUser.getUsername());
                            sb.append("/");
//                            sb.append(newUser.getMark());
                            sb.append("/");
                            sb.append("\">http://"+base_url+"/register/active/");
                            sb.append(newUser.getUsername());
                            sb.append("/");
//                            sb.append(newUser.getMark());
                            sb.append("/");
                            sb.append("</a>");
                        }
                        else if(thisLanguage.equals("en")){
                            sb.append("Recording lives around us, citizen science starts from here.<br/>");
                            sb.append("Click the link below to activate the account, please activate it as soon as possible!<br/>");
                            sb.append("<a href=\"http://"+base_url+"/register/active/");
                            sb.append(newUser.getUsername());
                            sb.append("/");
//                            sb.append(newUser.getMark());
                            sb.append("/");
                            sb.append("\">http://"+base_url+"/register/active/");
                            sb.append(newUser.getUsername());
                            sb.append("/");
//                            sb.append(newUser.getMark());
                            sb.append("/");
                            sb.append("</a>");
                        }
                        else{
                            sb.append("Recording lives around us, citizen science starts from here.<br/>");
                            sb.append("Click the link below to activate the account, please activate it as soon as possible!<br/>");
                            sb.append("<a href=\"http://"+base_url+"/register/active/");
                            sb.append(newUser.getUsername());
                            sb.append("/");
//                            sb.append(newUser.getMark());
                            sb.append("/");
                            sb.append("\">http://"+base_url+"/register/active/");
                            sb.append(newUser.getUsername());
                            sb.append("/");
//                            sb.append(newUser.getMark());
                            sb.append("/");
                            sb.append("</a>");
                        }

                        //邮件信息
                        MimeMessage mimeMessage = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.setFrom(fromEmail);
                        helper.setTo(newUser.getEmail());
                        helper.setSubject("NoL 生物记");
                        helper.setText(sb.toString(), true);

                        //发送邮件
                        mailSender.send(mimeMessage);
                    }catch(Exception e){
                    }
                    registerMsg="success";
                }
                else{
                    if(thisLanguage.equals("zh"))
                        registerMsg = "邮箱不可用，请更换";
                    else if(thisLanguage.equals("en"))
                        registerMsg = "The email address is not available, please replace the email address";
                    else
                        registerMsg = "The email address is not available, please replace the email address";
                }
            }
            else{
                if(thisLanguage.equals("zh"))
                    registerMsg = "用户名不可用，请更换";
                else if(thisLanguage.equals("en"))
                    registerMsg = "The username is not available, please replace the username";
                else
                    registerMsg = "The username is not available, please replace the username";
            }
        }
        else{
            if(thisLanguage.equals("zh"))
                registerMsg = "验证码错误";
            else if(thisLanguage.equals("en"))
                registerMsg = "Verification is error";
            else
                registerMsg = "Verification is error";
        }
        return registerMsg;
    }

    @Override
    public String sendActiveEmail(HttpServletRequest request, HttpServletResponse response) {
        String thisLanguage=localeService.getLanguage(request,response);
        String sendMsg="error";
        //验证码判断
        if(request.getParameter("token").equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString())){
            //邮箱判断
            User thisUser=this.findOneByEmail(request.getParameter("email"));
            if(thisUser!=null){
                //判断是否激活
                if(thisUser.getStatus()==0){
                    try{
                        //base_url
                        String contextPath=request.getContextPath();
                        String base_url=request.getServerName().toString()+":"+request.getServerPort();
                        if(contextPath!=null && contextPath.length()>0){
                            base_url=base_url+contextPath;
                        }
                        ///邮件的内容
                        StringBuffer sb=new StringBuffer("AnimalSeeker<br/>");
                        if(thisLanguage.equals("zh")){
                            sb.append("记录身边的生物，公民科学从这里开始.<br/>");
                            sb.append("点击下面链接激活账号，请尽快激活！<br/>");
                            sb.append("<a href=\"http://"+base_url+"/register/active/");
                            sb.append(thisUser.getUsername());
                            sb.append("/");
//                            sb.append(thisUser.getMark());
                            sb.append("/");
                            sb.append("\">http://"+base_url+"/register/active/");
                            sb.append(thisUser.getUsername());
                            sb.append("/");
//                            sb.append(thisUser.getMark());
                            sb.append("/");
                            sb.append("</a>");
                        }
                        else if(thisLanguage.equals("en")){
                            sb.append("Recording lives around us, citizen science starts from here.<br/>");
                            sb.append("Click the link below to activate the account, please activate it as soon as possible!<br/>");
                            sb.append("<a href=\"http://"+base_url+"/register/active/");
                            sb.append(thisUser.getUsername());
                            sb.append("/");
//                            sb.append(thisUser.getMark());
                            sb.append("/");
                            sb.append("\">http://"+base_url+"/register/active/");
                            sb.append(thisUser.getUsername());
                            sb.append("/");
//                            sb.append(thisUser.getMark());
                            sb.append("/");
                            sb.append("</a>");
                        }
                        else{
                            sb.append("Recording lives around us, citizen science starts from here.<br/>");
                            sb.append("Click the link below to activate the account, please activate it as soon as possible!<br/>");
                            sb.append("<a href=\"http://"+base_url+"/register/active/");
                            sb.append(thisUser.getUsername());
                            sb.append("/");
//                            sb.append(thisUser.getMark());
                            sb.append("/");
                            sb.append("\">http://"+base_url+"/register/active/");
                            sb.append(thisUser.getUsername());
                            sb.append("/");
//                            sb.append(thisUser.getMark());
                            sb.append("/");
                            sb.append("</a>");
                        }

                        //邮件信息
                        MimeMessage mimeMessage = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.setFrom(fromEmail);
                        helper.setTo(thisUser.getEmail());
                        helper.setSubject("AnimalSeeker");
                        helper.setText(sb.toString(), true);

                        //发送邮件
                        mailSender.send(mimeMessage);

                        if(thisLanguage.equals("zh"))
                            sendMsg="发送成功";
                        else if(thisLanguage.equals("en"))
                            sendMsg="Send Success";
                        else
                            sendMsg="Send Success";
                    }catch(Exception e){
                        if(thisLanguage.equals("zh"))
                            sendMsg="邮件发送失败";
                        else if(thisLanguage.equals("en"))
                            sendMsg="Send email failed";
                        else
                            sendMsg="Send email failed";
                    }
                }
                else{
                    if(thisLanguage.equals("zh"))
                        sendMsg="此用户已激活，请直接登录";
                    else if(thisLanguage.equals("en"))
                        sendMsg="This user has been activated, please login directly";
                    else
                        sendMsg="This user has been activated, please login directly";
                }
            }
            else{
                if(thisLanguage.equals("zh"))
                    sendMsg="该邮箱没有注册";
                else if(thisLanguage.equals("en"))
                    sendMsg="This email is not registered";
                else
                    sendMsg="This email is not registered";
            }
        }
        else{
            if(thisLanguage.equals("zh"))
                sendMsg="验证码错误";
            else if(thisLanguage.equals("en"))
                sendMsg="Verification code is error";
            else
                sendMsg="Verification code is error";
        }
        return sendMsg;
    }

    @Override
    public String activeUser(String username, String mark, HttpServletRequest request, HttpServletResponse response) {
        String thisLanguage=localeService.getLanguage(request,response);
        String activeMsg="error";
        User thisUser=this.findOneByName(username);
        if(thisUser!=null){
            //有此用户
            if(thisUser.getStatus()==0){
                //可正常激活
                if(thisUser.getVerificationCode().equals(mark)){
                    this.changeStatus(thisUser,1);
                    if(thisLanguage.equals("zh"))
                        activeMsg="此账户已激活";
                    else if(thisLanguage.equals("en"))
                        activeMsg="This account has been activated";
                    else
                        activeMsg="This account has been activated";
                }
                else{
                    //激活失败
                    if(thisLanguage.equals("zh"))
                        activeMsg="无效激活链接";
                    else if(thisLanguage.equals("en"))
                        activeMsg="Invalid activation link";
                    else
                        activeMsg="Invalid activation link";
                }
            }
            else if(thisUser.getStatus()==1){
                //已经激活
                if(thisLanguage.equals("zh"))
                    activeMsg="此账户已激活";
                else if(thisLanguage.equals("en"))
                    activeMsg="This account has been activated";
                else
                    activeMsg="This account has been activated";
            }
            else{
                //账户异常
                if(thisLanguage.equals("zh"))
                    activeMsg="无效激活链接";
                else if(thisLanguage.equals("en"))
                    activeMsg="Invalid activation link";
                else
                    activeMsg="Invalid activation link";
            }

        }
        else{
            //无效激活链接
            if(thisLanguage.equals("zh"))
                activeMsg="无效激活链接";
            else if(thisLanguage.equals("en"))
                activeMsg="Invalid activation link";
            else
                activeMsg="Invalid activation link";
        }
        return activeMsg;
    }

    @Override
    public String sendPasswordEmail(HttpServletRequest request, HttpServletResponse response) {
        String errorMsg="none";
        String thisLanguage=localeService.getLanguage(request,response);
        //Token判断
        if(request.getParameter("token").equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString())){
            //email判断
            User thisUser=this.userRepository.findOneByEmail(request.getParameter("email"));
            if(thisUser!=null){
                //设置找回流程
                try{
                    //base_url
                    String contextPath=request.getContextPath();
                    String base_url=request.getServerName().toString()+":"+request.getServerPort();
                    if(contextPath!=null && contextPath.length()>0){
                        base_url=base_url+contextPath;
                    }
                    thisUser.setVerificationCode((UUID.randomUUID().toString().replace("-","")));
                    //过期时间
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Timestamp(System.currentTimeMillis()));
                    c.add(Calendar.MINUTE, 10);
                    thisUser.setVerificationCodeExpiryTime(c.getTime());
                    this.userRepository.save(thisUser);
                    ///邮件的内容
                    StringBuffer sb=new StringBuffer("Notes of Life<br/>");
                    if(thisLanguage.equals("zh")){
                        sb.append("记录身边的生物，公民科学从这里开始<br/>");
                        sb.append("点击下面链接重设密码，请在10分钟内完成！<br/>");
                        sb.append("<a href=\"http://"+base_url+"/password/reset/");
                        sb.append(thisUser.getUsername());
                        sb.append("/");
//                        sb.append(thisUser.getResetmark());
                        sb.append("/");
                        sb.append("\">http://"+base_url+"/password/reset/");
                        sb.append(thisUser.getUsername());
                        sb.append("/");
//                        sb.append(thisUser.getResetmark());
                        sb.append("/");
                        sb.append("</a>");
                    }
                    else if(thisLanguage.equals("en")){
                        sb.append("Recording lives around us, citizen science starts from here.<br/>");
                        sb.append("Click the link below to reset the password, please complete it in 10 minutes!<br/>");
                        sb.append("<a href=\"http://"+base_url+"/password/reset/");
                        sb.append(thisUser.getUsername());
                        sb.append("/");
//                        sb.append(thisUser.getResetmark());
                        sb.append("/");
                        sb.append("\">http://"+base_url+"/password/reset/");
                        sb.append(thisUser.getUsername());
                        sb.append("/");
//                        sb.append(thisUser.getResetmark());
                        sb.append("/");
                        sb.append("</a>");
                    }
                    else{
                        sb.append("Recording lives around us, citizen science starts from here.<br/>");
                        sb.append("Click the link below to reset the password, please complete it in 10 minutes!<br/>");
                        sb.append("<a href=\"http://"+base_url+"/password/reset/");
                        sb.append(thisUser.getUsername());
                        sb.append("/");
//                        sb.append(thisUser.getResetmark());
                        sb.append("/");
                        sb.append("\">http://"+base_url+"/password/reset/");
                        sb.append(thisUser.getUsername());
                        sb.append("/");
//                        sb.append(thisUser.getResetmark());
                        sb.append("/");
                        sb.append("</a>");
                    }

                    //邮件信息
                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setFrom(fromEmail);
                    helper.setTo(thisUser.getEmail());
                    helper.setSubject("NoL 生物记");
                    helper.setText(sb.toString(), true);

                    //发送邮件
                    mailSender.send(mimeMessage);
                }catch(Exception e){
                    errorMsg="error";
                }
            }
            else{
                errorMsg="email";
            }
        }
        else{
            errorMsg="token";
        }
        return errorMsg;
    }

    @Override
    public Boolean canRestPassword(String username,String mark) {
        try{
            User thisUser=this.userRepository.findOneByUsername(username);
            //mark验证
            if(thisUser.getVerificationCode().equals(mark)){
                //时间验证
                Date nowTime=new Date();
                Date pastTime=thisUser.getVerificationCodeExpiryTime();
                if(nowTime.compareTo(pastTime)==-1){
                    return true;
                }
            }
            return false;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean restPassword(String username,String password) {
        try{
            User thisUser=this.userRepository.findOneByUsername(username);
            thisUser.setPwd(MD5Utils.MD532(password));
            thisUser.setVerificationCode("");
            this.userRepository.save(thisUser);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

}