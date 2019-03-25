package com.cp.fcsdn.controller;

import com.cp.fcsdn.entity.User;
import com.cp.fcsdn.entity.VaptchaMessage;
import com.cp.fcsdn.service.UserService;
import com.cp.fcsdn.util.CryptographyUtil;
import com.cp.fcsdn.util.DateUtil;
import com.cp.fcsdn.util.StringUtil;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * @author chengpeng
 * @company 趋势自家游
 * @create 2019-03-25 9:17
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private JavaMailSender mailSender;

    @Value("${userImageFilePath}")
    private String userImageFilePath;


    /**
     * 人机验证结果判断
     * @param token
     * @param ip
     * @return
     * @throws Exception
     */
    private boolean vaptchaCheck(String token,String ip)throws Exception{
        String body="";
        CloseableHttpClient httpClient=HttpClients.createDefault();
        HttpPost httpPost=new HttpPost("http://api.vaptcha.com/v2/validate");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("id", "5c9834b8fc650e73d4434734"));
        nvps.add(new BasicNameValuePair("secretkey", "150d242036aa45bcb649505579e6bc54"));
        nvps.add(new BasicNameValuePair("scene", ""));
        nvps.add(new BasicNameValuePair("token", token));
        nvps.add(new BasicNameValuePair("ip", ip));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        CloseableHttpResponse r = httpClient.execute(httpPost);
        HttpEntity entity = r.getEntity();

        if(entity!=null){
            body = EntityUtils.toString(entity, "utf-8");
            System.out.println(body);
        }
        r.close();
        httpClient.close();
        Gson gson = new Gson();
        VaptchaMessage message=gson.fromJson(body, VaptchaMessage.class);
        if(message.getSuccess()==1){
            return true;
        }else{
            return false;
        }

    }
    /**
     * 用户注册
     */
    @ResponseBody
    @RequestMapping("/register")
    public Map<String,Object> register(@Valid User user, BindingResult bindingResult, String vaptcha_token, HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();
        if(bindingResult.hasErrors()){
            map.put("success", false);
            map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
        }else if(userService.findByUserName(user.getUserName())!=null){
            map.put("success", false);
            map.put("errorInfo", "用户名已存在，请更换！");
        }else if(userService.findByEmail(user.getEmail())!=null){
            map.put("success", false);
            map.put("errorInfo", "邮箱已存在，请更换！");
        }else if(!vaptchaCheck(vaptcha_token,request.getRemoteHost())){
            map.put("success", false);
            map.put("errorInfo", "人机验证失败！");
        }else{
            user.setPassword(CryptographyUtil.md5(user.getPassword(), CryptographyUtil.SALT));
            user.setRegisterDate(new Date());
            user.setImageName("default.jpg");
            userService.save(user);
            map.put("success", true);
        }
        return map;
    }

    /**
     * 用户登录请求
     * @param user
     * @param vaptcha_token
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/login")
    public Map<String,Object> login(User user,String vaptcha_token,HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtil.isEmpty(user.getUserName().trim())){
            map.put("success", false);
            map.put("errorInfo", "请输入用户名！");
        }else if(StringUtil.isEmpty(user.getPassword().trim())){
            map.put("success", false);
            map.put("errorInfo", "请输入密码！");
        }else if(!vaptchaCheck(vaptcha_token,request.getRemoteHost())){
            map.put("success", false);
            map.put("errorInfo", "人机验证失败！");
        }else{
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token=new UsernamePasswordToken(user.getUserName(), CryptographyUtil.md5(user.getPassword(), CryptographyUtil.SALT));
            try{
                subject.login(token);
                String userName = (String) SecurityUtils.getSubject().getPrincipal();
                User currentUser = userService.findByUserName(userName);
                if(currentUser.isOff()){
                    map.put("success", false);
                    map.put("errorInfo", "该用户已经被封禁，请联系管理员！");
                    subject.logout();
                }else{
                    request.getSession().setAttribute("currentUser", currentUser);
                    map.put("success", true);
                }

            }catch(Exception e){
                e.printStackTrace();
                map.put("success", false);
                map.put("errorInfo", "用户名或者密码错误！");
            }
        }
        return map;
    }

    /**
     * 发送邮件
     * @param email
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/sendEmail")
    public Map<String,Object> sendEmail(String email,HttpSession session)throws Exception{
        Map<String,Object> resulMap=new HashMap<String,Object>();
        if(StringUtil.isEmpty(email)){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "邮箱不能为空！");
            return resulMap;
        }
        User u = userService.findByEmail(email);
        if(u==null){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "这个邮箱不存在！");
            return resulMap;
        }
        String mailCode = StringUtil.genSixRandomNum();
        System.out.println("mailCode:"+mailCode);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("554605804@qq.com"); // 发件人
        message.setTo(email);
        message.setSubject("飞鹏下载站点-用户找回密码"); // 主题
        message.setText("验证码："+mailCode);
        mailSender.send(message);

        // 验证码存到session中
        session.setAttribute("mailCode", mailCode);
        session.setAttribute("userId", u.getId());
        resulMap.put("success", true);
        return resulMap;
    }

    /**
     * 邮件验证码判断 重置
     * @param yzm
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/checkYzm")
    public Map<String,Object> checkYzm(String yzm,HttpSession session)throws Exception{
        Map<String,Object> resulMap=new HashMap<String,Object>();
        if(StringUtil.isEmpty(yzm)){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "验证码不能为空！");
            return resulMap;
        }
        String mailCode=(String) session.getAttribute("mailCode");
        Integer userId=(Integer) session.getAttribute("userId");
        if(!yzm.equals(mailCode)){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "验证码错误！");
            return resulMap;
        }
        User user = userService.getById(userId);
        user.setPassword(CryptographyUtil.md5("123456", CryptographyUtil.SALT));
        userService.save(user);
        resulMap.put("success", true);
        return resulMap;
    }

    /**
     * 修改密码
     * @param oldpassword
     * @param password
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/modifyPassword")
    public Map<String,Object> modifyPassword(String oldpassword,String password,HttpSession session)throws Exception{
        User user = (User) session.getAttribute("currentUser");
        Map<String,Object> resulMap=new HashMap<String,Object>();
        if(!user.getPassword().equals(CryptographyUtil.md5(oldpassword, CryptographyUtil.SALT))){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "原密码错误！");
            return resulMap;
        }
        User oldUser = userService.getById(user.getId());
        oldUser.setPassword(CryptographyUtil.md5(password, CryptographyUtil.SALT));
        userService.save(oldUser);
        resulMap.put("success", true);
        return resulMap;
    }

    /**
     * 上传头像
     * @param file
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadImage")
    public Map<String,Object> uploadImage(MultipartFile file, HttpSession session)throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();
        if(!file.isEmpty()){
            String fileName=file.getOriginalFilename(); // 获取文件名
            String suffixName=fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
            String newFileName=DateUtil.getCurrentDateStr()+suffixName; // 新文件名
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(userImageFilePath+newFileName));
            map.put("code", 0);
            map.put("msg", "上传成功");
            Map<String,Object> map2=new HashMap<String,Object>();
            map2.put("src", "/userImage/"+newFileName);
            map2.put("title", newFileName);
            map.put("data", map2);

            User user=(User) session.getAttribute("currentUser");
            user.setImageName(newFileName);
            userService.save(user);
            session.setAttribute("currentUser", user);
        }
        return map;
    }



}
