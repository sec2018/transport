package com.example.transport.api;

import com.example.transport.pojo.*;
import com.example.transport.service.Constant;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api")
@Controller
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getuserinfo",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetUserInfo(@RequestParam(value = "username") String username,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        try {
            User user = userService.getUserByLoginName(username);

            if(token.equals(sysUserTokenService.getToken(user.getId()))){
                r.setCode("200");
                r.setMsg("获取用户信息成功！");
                r.setData(user);
                r.setSuccess(true);
            }else{
                r.setCode(Constant.PERSONINFO_FAILURE.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.PERSONINFO_FAILURE.getMsg());
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.PERSONINFO_FAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.PERSONINFO_FAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //角色0,1
    @ApiOperation(value = "查询所有承运员", notes = "查询所有承运员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getalltrans",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetUsers(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("0") && !roleid.equals("1")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);

        //超级管理员,PC端
        if (roleid.equals("0")) {
            token = getAdminToken();
            if (!token.equals(token)) {
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            } else {
                r = getAllTrans();
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = getAllTrans();
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }




    public JsonResult getAllTrans(){
        JsonResult r = new JsonResult();
        try {
            List<WxUser>  userlist = userService.getAllTrans();
            List<Map<String,Object>> umaplist = new ArrayList<>();
            Map<String,Object> umap = null;
            for(WxUser wu : userlist){
                umap = new HashMap<String,Object>();
                umap.put("id",wu.getId());
                umap.put("nickname",wu.getNickname());
                umap.put("gender",wu.getGender()==1?"男":"女");
                umap.put("city",wu.getCity());
                umap.put("province",wu.getProvince());
                umap.put("country",wu.getCountry());
                umap.put("avatarurl",wu.getAvatarurl());
                umap.put("timestamp",wu.getTimestamp());
                String trancheckstatus = "进行中";
                if(wu.getTrancheckstatus() == null){
                    wu.setTrancheckstatus(0);
                }
                switch (wu.getTrancheckstatus()){
                    case 0:
                        trancheckstatus = "进行中";
                        break;
                    case 1:
                        trancheckstatus = "通过";
                        break;
                    case 2:
                        trancheckstatus = "不通过";
                        break;
                }
                umap.put("trancheckstatus",trancheckstatus);
                umaplist.add(umap);
            }
            r.setCode("200");
            r.setMsg("获取所有用户信息成功！");
            r.setData(umaplist);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.PERSONINFO_FAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.PERSONINFO_FAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return  r;
    }


    public JsonResult ConnectRedisCheckToken(String token){
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry<=5){
            try
            {
                //业务代码
                tokenvalue = redisService.get(token);
                r.setCode(200+"");
                r.setData(tokenvalue);
                r.setMsg("连接成功！");
                r.setSuccess(true);
                break;
            }
            catch(Exception ex)
            {
                //重试
                retry++;
                if(retry == 4){
                    //记录错误
                    r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
                    r.setData("");
                    r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
                    r.setSuccess(false);
                    return r;
                }
            }
        }
        return r;
    }

    public String getAdminToken(){
        User user = userService.getUserByLoginName("system");
        String admintoken  = sysUserTokenService.getToken(user.getId());
        return admintoken;
    }



    //角色0,1
    @ApiOperation(value = "审核承运员", notes = "审核承运员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "承运员id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "ispass", value = "是否通过审核", required = true, dataType = "Boolean",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="adminchecktran",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> adminCheckShop(@RequestParam(value = "userid",required = true)Integer userid,@RequestParam(value = "ispass",required = true)Boolean ispass,HttpServletRequest request){
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(!roleid.equals("0") && !roleid.equals("1")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }

        String token = request.getHeader("token");
        try {
            token = getAdminToken();
            if (!token.equals(token)) {
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }else{

                WxUser wxUser = userService.getWxUserById(userid);
                if(ispass){
                    wxUser.setTrancheckstatus(1);    //1代表审核通过
                    userService.updateWxUser(wxUser);
                    r.setCode("200");
                    r.setMsg("审核承运员通过！");
                    r.setSuccess(true);
                }else{
                    wxUser.setTrancheckstatus(2);    //2代表审核不通过，被驳回
                    userService.updateWxUser(wxUser);
                    r.setCode("200");
                    r.setMsg("承运员未通过审核！");
                    r.setSuccess(true);
                }
                boolean flag = userService.updateWxUser(wxUser);
                if(flag){
                    r.setData(null);
                }else{
                    r.setCode(Constant.USER_CHECKFAILURE.getCode()+"");
                    r.setMsg(Constant.USER_CHECKFAILURE.getMsg());
                    r.setData(null);
                    r.setSuccess(false);
                }
            }
        } catch (Exception e) {
            r.setCode(Constant.USER_CHECKFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.USER_CHECKFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //角色0,1
    @ApiOperation(value = "前端显示图片", notes = "前端显示图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imagename", value = "图片名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="adminshowimage",method = RequestMethod.POST,produces = "image/jpeg;chartset=UTF-8")
    @ResponseBody
    public String showPhoto(@RequestParam(value = "imagename",required = true)String imagename, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.reset();
            String path="";
            String roleid = request.getHeader("roleid");
            switch (roleid){
                case "2":
                    path = "D:/transportimage/shopimages/"+ imagename;
                    break;
                case "3":
                    path = "D:/transportimage/tranimages/"+ imagename;
                    break;
                case "4":
                    path = "D:/transportimage/companyimages/"+ imagename;
                    break;
            }
            // 以byte流的方式打开文件 d:\1.gif
            FileInputStream hFile;
            hFile = new FileInputStream(path);
            //得到文件大小
            int i=hFile.available();
            byte data[]=new byte[i];
            //读数据
            hFile.read(data);
//            String extension = FilenameUtils.getExtension(path);
//            boolean isJPG = StringUtils.isBlank(extension) || extension.equalsIgnoreCase("jpg");
//            extension = isJPG ? "jpeg" : extension;
//            response.setContentType("image/".concat(extension).concat(";charset=UTF-8"));
//            response.setHeader("Content-Type","image/jpeg");
//            //得到向客户端输出二进制数据的对象
//            OutputStream toClient=response.getOutputStream();
//            //输出数据
//            toClient.write(data);
//            toClient.flush();
//            toClient.close();
//            hFile.close();

            BASE64Encoder encoder = new BASE64Encoder();
            String png_base64 =  encoder.encodeBuffer(data);//转换成base64串
            png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
            return png_base64;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }
}
