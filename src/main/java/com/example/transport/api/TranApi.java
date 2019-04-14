package com.example.transport.api;

import com.example.transport.dao.SysTranMapper;
import com.example.transport.pojo.SysTran;
import com.example.transport.pojo.User;
import com.example.transport.service.Constant;
import com.example.transport.service.SysTranService;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by WangZJ on 2019/4/3.
 */
@RequestMapping("api")
@Controller
public class TranApi {


    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysTranMapper sysTranMapper;

    @Autowired
    private SysTranService sysTranService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    //角色1,2
    @ApiOperation(value = "添加或修改承运员", notes = "添加或修改承运员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "承运员id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tran_name", value = "承运员名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tran_tel", value = "承运员电话", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "front_url", value = "身份证正面", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "back_url", value = "身份证背面", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "addorupdatetran", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> AddShop(@RequestParam(value = "id", required = false) Integer id,
                                              @RequestParam(value = "tran_name") String tran_name, @RequestParam(value = "tran_tel") String tran_tel,
                                              @RequestParam(value = "front_url") String front_url, @RequestParam(value = "back_url") String back_url, HttpServletRequest request) {
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("3")) {
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }

        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try {
            tokenvalue = r.getData().toString();
        } catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        if (id == null) {
            //插入
            try {
                if (tokenvalue != "") {
                    SysTran sysTran = new SysTran();
                    sysTran.setTranName(tran_name);
                    sysTran.setTranTel(tran_tel);
                    sysTran.setIdFrontUrl(front_url);
                    sysTran.setIdBackUrl(back_url);
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    sysTran.setWxuserId(wxuserid);
                    boolean flag = sysTranMapper.insert(sysTran) == 1 ? true : false;
                    if (flag) {
                        r.setCode("200");
                        r.setMsg("添加承运员成功！");
                        r.setData(sysTran);
                        r.setSuccess(true);
                    } else {
                        r.setCode(Constant.TRAN_ADDFAILURE.getCode() + "");
                        r.setMsg(Constant.TRAN_ADDFAILURE.getMsg());
                        r.setData(null);
                        r.setSuccess(true);
                    }
                } else {
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r.setCode(Constant.TRAN_ADDFAILURE.getCode() + "");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.TRAN_ADDFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        } else {
            //修改
            try {
                if (tokenvalue != "" && roleid.equals("3")) {
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    SysTran sysTran = sysTranMapper.selectByPrimaryKey(id);


                    sysTran.setTranName(tran_name);
                    sysTran.setTranTel(tran_tel);
                    sysTran.setIdFrontUrl(front_url);
                    sysTran.setIdBackUrl(back_url);



                    boolean flag = sysTranService.updateByPrimaryKey(sysTran);
                    if (flag) {
                        r.setCode("200");
                        r.setMsg("修改承运员成功！");
                        r.setData(null);
                        r.setSuccess(true);
                    } else {
                        r.setCode(Constant.SHOP_UPDATEFAILURE.getCode() + "");
                        r.setMsg(Constant.SHOP_UPDATEFAILURE.getMsg());
                        r.setData(null);
                        r.setSuccess(true);
                    }
                } else {
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r.setCode(Constant.SHOP_UPDATEFAILURE.getCode() + "");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.SHOP_UPDATEFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(r);
    }


    //角色1,3
    @ApiOperation(value = "查询承运员", notes = "查询承运员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "searchtran", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> SearchShop(HttpServletRequest request) {
        JsonResult r = new JsonResult();
        String roleid = request.getHeader("roleid");
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("3")) {
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");

        //超级管理员,PC端
        if (roleid.equals("0")) {
            token = getAdminToken();
            if (!token.equals(token)) {
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            } else {
                //查询所有？
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
            try {
                if (tokenvalue != "") {
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    SysTran sysTran = sysTranMapper.selectByWxuserid(wxuserid);
                    r.setCode("200");
                    r.setMsg("查询承运员成功！");
                    r.setData(sysTran);
                    r.setSuccess(true);
                } else {
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r = Common.SearchError(e);
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(r);
    }


    //点击上传图片按钮，返回路径接口
    @ApiOperation(value = "承运员上传身份证正面", notes = "承运员上传身份证正面")
    @PostMapping(value="/tranfrontimage",headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> TranUploadFrontImage(@ApiParam(value="imagefile",required=true)
                                                              MultipartFile imagefile, HttpServletRequest request){

        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("3")) {
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");

        //超级管理员,PC端
        if (roleid.equals("0")) {
            token = getAdminToken();
            if (!token.equals(token)) {
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            } else {
                r = TranUploadImage(imagefile,1);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = TranUploadImage(imagefile,1);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }

    //点击上传图片按钮，返回路径接口
    @ApiOperation(value = "承运员上传身份证反面", notes = "承运员上传身份证反面")
    @PostMapping(value="/tranbackimage",headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> TranUploadBackImage(@ApiParam(value="imagefile",required=true)
                                                                   MultipartFile imagefile, HttpServletRequest request){

        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("3")) {
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");

        //超级管理员,PC端
        if (roleid.equals("0")) {
            token = getAdminToken();
            if (!token.equals(token)) {
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            } else {
                r = TranUploadImage(imagefile,2);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = TranUploadImage(imagefile,2);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult TranUploadImage(MultipartFile imagefile,Integer flag){
        JsonResult r = new JsonResult();
        try {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                if (imagefile != null) {
                    String fileName = imagefile.getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        // 文件上传的最终保存路径
                        String filefinalname = "";
                        if(flag == 1){
                            //正面
                            filefinalname = "front_"+UUID.randomUUID().toString()+".png";
                            r.setMsg("上传身份证正面成功！");
                        }else if(flag == 2){
                            filefinalname = "back_"+UUID.randomUUID().toString()+".png";
                            r.setMsg("上传身份证反面成功！");
                        }
                        String finalimagePath = "D:/transportimage/tranimages/"+ filefinalname;
                        File outFile = new File(finalimagePath);
                        if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                            // 创建父文件夹
                            outFile.getParentFile().mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(outFile);
                        inputStream = imagefile.getInputStream();
                        IOUtils.copy(inputStream, fileOutputStream);
                        r.setCode("200");
//                        r.setData(outFile.getAbsolutePath());
                        r.setData(filefinalname);
                        r.setSuccess(true);
                    }
                } else {
                    r.setCode(Constant.TRAN_UPLOADIMAGEFAILURE.getCode()+"");
                    r.setMsg(Constant.TRAN_UPLOADIMAGEFAILURE.getMsg());
                    r.setSuccess(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                r.setCode(Constant.TRAN_UPLOADIMAGEFAILURE.getCode()+"");
                r.setMsg(Constant.TRAN_UPLOADIMAGEFAILURE.getMsg());
                r.setSuccess(false);
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            r.setCode(Constant.TRAN_UPLOADIMAGEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.TRAN_UPLOADIMAGEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }


    public JsonResult ConnectRedisCheckToken(String token) {
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry <= 5) {
            try {
                //业务代码
                tokenvalue = redisService.get(token);
                r.setCode(200 + "");
                r.setData(tokenvalue);
                r.setMsg("连接成功！");
                r.setSuccess(true);
                break;
            } catch (Exception ex) {
                //重试
                retry++;
                if (retry == 4) {
                    //记录错误
                    r.setCode(Constant.Redis_TIMEDOWN.getCode() + "");
                    r.setData("");
                    r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
                    r.setSuccess(false);
                    return r;
                }
            }
        }
        return r;
    }

    public String getAdminToken() {
        User user = userService.getUserByLoginName("system");
        String admintoken = sysUserTokenService.getToken(user.getId());
        return admintoken;
    }
}
