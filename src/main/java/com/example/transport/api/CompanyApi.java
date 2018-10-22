package com.example.transport.api;

import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.model.SysCompanyExample;
import com.example.transport.pojo.SysCompany;
import com.example.transport.service.Constant;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("api")
@Controller
public class CompanyApi {

    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private UserService userService;


    @Autowired
    private RedisService redisService;

    //角色1,4
    @ApiOperation(value = "添加物流公司", notes = "根据物流公司名称添加物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyname", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_tel", value = "物流公司电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_procity", value = "物流公司省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_detailarea", value = "物流公司详细名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="addcompany",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> AddCompany(@RequestParam(value = "companyname")String companyname,
                                                 @RequestParam(value = "company_tel")String company_tel,
                                                 @RequestParam(value = "company_procity")String company_procity,
                                                 @RequestParam(value = "company_detailarea")String company_detailarea,HttpServletRequest request){
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }

        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = r.getData().toString();
        SysCompany sysCompany = new SysCompany();
        sysCompany.setCompanyName(companyname);
        sysCompany.setCompanyTel(company_tel);
        sysCompany.setCompanyProcity(company_procity);
        sysCompany.setCompanyDetailarea(company_detailarea);
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                sysCompany.setWxuserId(wxuserid);
                boolean flag = sysCompanyMapper.insert(sysCompany)==1?true:false;
                if(flag){
                    r.setCode("200");
                    r.setMsg("添加物流公司成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }else{
                    r.setCode(Constant.COMPANY_ADDFAILURE.getCode()+"");
                    r.setMsg(Constant.COMPANY_ADDFAILURE.getMsg());
                    r.setData(null);
                    r.setSuccess(true);
                }
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.COMPANY_ADDFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_ADDFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //角色1,2，3,4
    @ApiOperation(value = "查询所有物流公司", notes = "查询所有物流公司")
    @RequestMapping(value="getcompanies",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetCompanies(){
        JsonResult r = new JsonResult();
        try {
            SysCompanyExample example = new SysCompanyExample();
            List<SysCompany> companylist = sysCompanyMapper.selectByExample(example);
            r.setCode("200");
            r.setMsg("获取物流公司成功！");
            r.setData(companylist);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.COMPANY_GETFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_GETFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //角色1,2，3,4
    @ApiOperation(value = "查询所有物流公司个数", notes = "查询所有物流公司个数")
    @RequestMapping(value="getcompaniescount",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetCompaniesNum(){
        JsonResult r = new JsonResult();
        try {
            SysCompanyExample sysCompanyExample = new SysCompanyExample();
            int count = sysCompanyMapper.countByExample(sysCompanyExample);
            r.setCode("200");
            r.setMsg("获取物流公司数量成功！");
            r.setData(count);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.COMPANY_GETFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_GETFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //角色1,2
    @ApiOperation(value = "查询用户物流公司", notes = "查询物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="searchcompany",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> SearchShop(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = r.getData().toString();
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                SysCompany sysCompany = sysCompanyMapper.selectByWxuserid(wxuserid);
                r.setCode("200");
                r.setMsg("查询物流公司成功！");
                r.setData(sysCompany);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult ConnectRedisCheckToken(String token){
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry<=3){
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
}
