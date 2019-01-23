package com.example.transport.api;

import com.example.transport.dao.CompanyLinesMapper;
import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.model.CompanyLinesExample;
import com.example.transport.model.SysCompanyExample;
import com.example.transport.pojo.CompanyLines;
import com.example.transport.pojo.LineMap;
import com.example.transport.pojo.SysCompany;
import com.example.transport.pojo.User;
import com.example.transport.service.CompanyService;
import com.example.transport.service.Constant;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RequestMapping("api")
@Controller
public class CompanyApi {

    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private CompanyLinesMapper companyLinesMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;



//    private static Map<String,Map<Integer,String>> linemap = new HashMap();
   private static List<LineMap> linemaplist = new ArrayList<>();



    @PostConstruct
    public void init(){
        //初始化得到所有路线
        GetAllCompanyLines();
    }

//    public void GetAllCompanyLines(){
//        CompanyLinesExample example = new CompanyLinesExample();
//        List<CompanyLines> line = companyLinesMapper.selectByExample(example);
//        Map<Integer,String> companylistmap;
//        String companyname;
//        for (CompanyLines cl : line){
//            String key = cl.getBeginAddr()+"-->"+cl.getArriveAddr();
//            if(linemap.containsKey(key)){
//                if(!linemap.get(key).containsKey(cl.getCompanyId())){
//                    companyname = sysCompanyMapper.selectByPrimaryKey(cl.getCompanyId()).getCompanyName();
//                    linemap.get(key).put(cl.getCompanyId(),companyname);
//                }
//            }else{
//                companyname = sysCompanyMapper.selectByPrimaryKey(cl.getCompanyId()).getCompanyName();
//                companylistmap = new HashMap<Integer,String>();
//                companylistmap.put(cl.getCompanyId(),companyname);
//                linemap.put(key,companylistmap);
//            }
//        }
//    }

    public void GetAllCompanyLines(){
        CompanyLinesExample example = new CompanyLinesExample();
        List<CompanyLines> line = companyLinesMapper.selectByExample(example);
        Map<Integer,String> companylistmap;
        String companyname;
        LineMap lp;
        for (CompanyLines cl : line){
            String key = cl.getBeginAddr()+"-->"+cl.getArriveAddr();
            companyname = sysCompanyMapper.selectByPrimaryKey(cl.getCompanyId()).getCompanyName();
            companylistmap = new HashMap<Integer,String>();
            companylistmap.put(cl.getCompanyId(),companyname);
            if(linemaplist.size() == 0){
                lp = new LineMap();
                lp.setKey(key);
                lp.setValuemap(companylistmap);
                linemaplist.add(lp);
            }else{
                for(int i=0;i<linemaplist.size();i++){
                    if(linemaplist.get(i).getKey().equals(key)){
                        if(!linemaplist.get(i).getValuemap().containsKey(cl.getCompanyId())){
                            linemaplist.get(i).getValuemap().put(cl.getCompanyId(),companyname);
                            break;
                        }
                    }else{
                        companylistmap = new HashMap<Integer,String>();
                        companylistmap.put(cl.getCompanyId(),companyname);
                        lp = new LineMap();
                        lp.setKey(key);
                        lp.setValuemap(companylistmap);
                        linemaplist.add(lp);
                        break;
                    }
                }
            }
        }
    }


//    //角色1,4
//    @ApiOperation(value = "添加或修改物流公司", notes = "根据物流公司名称添加或修改物流公司")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "companyid", value = "物流公司id", required = false, dataType = "Integer",paramType = "query"),
//            @ApiImplicitParam(name = "companyname", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
//            @ApiImplicitParam(name = "company_tel", value = "物流公司电话", required = true, dataType = "String",paramType = "query"),
//            @ApiImplicitParam(name = "company_procity", value = "物流公司省市", required = true, dataType = "String",paramType = "query"),
//            @ApiImplicitParam(name = "company_detailarea", value = "物流公司详细名称", required = true, dataType = "String",paramType = "query"),
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
//            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
//    })
//    @RequestMapping(value="addorupdatecompany",method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<JsonResult> AddCompany(@RequestParam(value = "companyid",required = false)Integer companyid,
//            @RequestParam(value = "companyname")String companyname,
//                                                 @RequestParam(value = "company_tel")String company_tel,
//                                                 @RequestParam(value = "company_procity")String company_procity,
//                                                 @RequestParam(value = "company_detailarea")String company_detailarea,HttpServletRequest request){
//        String roleid = request.getHeader("roleid");
//        JsonResult r = new JsonResult();
//        if(!roleid.equals("1") && !roleid.equals("4")){
//            r = Common.RoleError();
//            return ResponseEntity.ok(r);
//        }
//
//        String token = request.getHeader("token");
//        r = ConnectRedisCheckToken(token);
//        String tokenvalue = "";
//        try{
//            tokenvalue = r.getData().toString();
//        }catch (Exception e) {
//            r = Common.TokenError();
//            e.printStackTrace();
//            return ResponseEntity.ok(r);
//        }
//        if(companyid == null) {
//            //插入
//            SysCompany sysCompany = new SysCompany();
//            sysCompany.setCompanyName(companyname);
//            sysCompany.setCompanyTel(company_tel);
//            sysCompany.setCompanyProcity(company_procity);
//            sysCompany.setCompanyDetailarea(company_detailarea);
//            try {
//                if(tokenvalue!=""){
//                    redisService.expire(token, Constant.expire.getExpirationTime());
//                    String openid = tokenvalue.split("\\|")[0];
//                    long wxuserid = userService.getWxUserId(openid);
//                    sysCompany.setWxuserId(wxuserid);
//                    boolean flag = sysCompanyMapper.insert(sysCompany)==1?true:false;
//                    if(flag){
//                        r.setCode("200");
//                        r.setMsg("添加物流公司成功！");
//                        r.setData(null);
//                        r.setSuccess(true);
//                    }else{
//                        r.setCode(Constant.COMPANY_ADDFAILURE.getCode()+"");
//                        r.setMsg(Constant.COMPANY_ADDFAILURE.getMsg());
//                        r.setData(null);
//                        r.setSuccess(true);
//                    }
//                }else{
//                    r = Common.TokenError();
//                    ResponseEntity.ok(r);
//                }
//            } catch (Exception e) {
//                r.setCode(Constant.COMPANY_ADDFAILURE.getCode()+"");
//                r.setData(e.getClass().getName() + ":" + e.getMessage());
//                r.setMsg(Constant.COMPANY_ADDFAILURE.getMsg());
//                r.setSuccess(false);
//                e.printStackTrace();
//            }
//        }else{
//            //更新
//            try {
//                if(tokenvalue!=""){
//                    redisService.expire(token, Constant.expire.getExpirationTime());
//
//                    SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(companyid);
//                    sysCompany.setCompanyName(companyname);
//                    sysCompany.setCompanyTel(company_tel);
//                    sysCompany.setCompanyProcity(company_procity);
//                    sysCompany.setCompanyDetailarea(company_detailarea);
//                    boolean flag = sysCompanyMapper.updateByPrimaryKey(sysCompany)==1?true:false;
//                    if(flag){
//                        r.setCode("200");
//                        r.setMsg("修改物流公司成功！");
//                        r.setData(null);
//                        r.setSuccess(true);
//                    }else{
//                        r.setCode(Constant.COMPANY_UPDATEFAILURE.getCode()+"");
//                        r.setMsg(Constant.COMPANY_UPDATEFAILURE.getMsg());
//                        r.setData(null);
//                        r.setSuccess(true);
//                    }
//                }else{
//                    r = Common.TokenError();
//                    ResponseEntity.ok(r);
//                }
//            } catch (Exception e) {
//                r.setCode(Constant.COMPANY_UPDATEFAILURE.getCode()+"");
//                r.setData(e.getClass().getName() + ":" + e.getMessage());
//                r.setMsg(Constant.COMPANY_UPDATEFAILURE.getMsg());
//                r.setSuccess(false);
//                e.printStackTrace();
//            }
//        }
//        return ResponseEntity.ok(r);
//    }


    //角色1,4
    @ApiOperation(value = "添加或修改物流公司", notes = "根据物流公司名称添加或修改物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyid", value = "物流公司id", required = false, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "companyname", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_tel", value = "物流公司电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_procity", value = "物流公司省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_detailarea", value = "物流公司详细名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "licence_url", value = "营业执照url", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "complain_tel", value = "投诉电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "service_tel", value = "服务电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "evaluation", value = "评价", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "begin_addr", value = "默认线路出发地", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "arrive_addr", value = "默认线路到站地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "arrive_tel", value = "默认线路到达电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "line_id", value = "默认线路id", required = false, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="addorupdatecompany",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> AddCompany(@RequestParam(value = "companyid",required = false)Integer companyid,
                                                 @RequestParam(value = "companyname")String companyname,
                                                 @RequestParam(value = "company_tel")String company_tel,
                                                 @RequestParam(value = "company_procity")String company_procity,
                                                 @RequestParam(value = "company_detailarea")String company_detailarea,
                                                 @RequestParam(value = "licence_url")String licence_url,
                                                 @RequestParam(value = "complain_tel")String complain_tel,
                                                 @RequestParam(value = "service_tel")String service_tel,
                                                 @RequestParam(value = "evaluation")Integer evaluation,
                                                 @RequestParam(value = "begin_addr")String begin_addr,
                                                 @RequestParam(value = "arrive_addr")String arrive_addr,
                                                 @RequestParam(value = "arrive_tel")String arrive_tel,
                                                 @RequestParam(value = "line_id",required = false)Integer line_id,
                                                 HttpServletRequest request){
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");

        //超级管理员,PC端
        if(roleid.equals("0")) {
            token = getAdminToken();
            if (!token.equals(token)) {
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            } else {
                r = AddCompany(token,"",companyid,companyname,company_tel,company_procity,
                        company_detailarea,licence_url,complain_tel,service_tel,
                        evaluation,begin_addr,arrive_addr,arrive_tel,line_id);
            }
        }else{
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try{
                tokenvalue = r.getData().toString();
            }catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
            r = AddCompany("",tokenvalue,companyid,companyname,company_tel,company_procity,
                    company_detailarea,licence_url,complain_tel,service_tel,
                    evaluation,begin_addr,arrive_addr,arrive_tel,line_id);
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult AddCompany(String token,String tokenvalue,Integer companyid,String companyname,String company_tel,String company_procity,
                                                 String company_detailarea,String licence_url,String complain_tel,String service_tel,
                                                 Integer evaluation,String begin_addr,String arrive_addr,String arrive_tel,
                                                 Integer line_id){
        JsonResult r = new JsonResult();
        if(companyid == null) {
            //插入
            SysCompany sysCompany = new SysCompany();
            sysCompany.setCompanyName(companyname);
            sysCompany.setCompanyTel(company_tel);
            sysCompany.setCompanyProcity(company_procity);
            sysCompany.setCompanyDetailarea(company_detailarea);
            sysCompany.setLicenceUrl(licence_url);
            sysCompany.setServiceTel(service_tel);
            sysCompany.setEvaluation(evaluation);

            CompanyLines companyLines = new CompanyLines();
            companyLines.setBeginAddr(begin_addr);
            companyLines.setArriveAddr(arrive_addr);
            companyLines.setArriveTel(arrive_tel);
            try {
                if(tokenvalue != "" && token == "") {
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    sysCompany.setWxuserId(wxuserid);
                }else if(tokenvalue == "" && token != ""){
                    sysCompany.setWxuserId(-1L);
                }else{
                    r = Common.TokenError();
                    return r;
                }
                boolean flag = companyService.insert(sysCompany,companyLines);
                if(flag){
                    //修改营业执照图片名称

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
            } catch (Exception e) {
                r.setCode(Constant.COMPANY_ADDFAILURE.getCode()+"");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.COMPANY_ADDFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        }else{
            //更新
            try {
                if(tokenvalue != "" && token == "") {
                    redisService.expire(token, Constant.expire.getExpirationTime());
                }else{
                    r = Common.TokenError();
                    return r;
                }
                SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(companyid);
                sysCompany.setCompanyName(companyname);
                sysCompany.setCompanyTel(company_tel);
                sysCompany.setCompanyProcity(company_procity);
                sysCompany.setCompanyDetailarea(company_detailarea);
                sysCompany.setLicenceUrl(licence_url);
                sysCompany.setServiceTel(service_tel);
                sysCompany.setEvaluation(evaluation);

                CompanyLines companyLines = companyLinesMapper.selectByPrimaryKey(line_id);
                companyLines.setBeginAddr(begin_addr);
                companyLines.setArriveAddr(arrive_addr);
                companyLines.setArriveTel(arrive_tel);
                boolean flag = companyService.updateByPrimaryKey(sysCompany,companyLines);
                if(flag){
                    r.setCode("200");
                    r.setMsg("修改物流公司成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }else{
                    r.setCode(Constant.COMPANY_UPDATEFAILURE.getCode()+"");
                    r.setMsg(Constant.COMPANY_UPDATEFAILURE.getMsg());
                    r.setData(null);
                    r.setSuccess(true);
                }
            } catch (Exception e) {
                r.setCode(Constant.COMPANY_UPDATEFAILURE.getCode()+"");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.COMPANY_UPDATEFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        }
        return r;
    }


    //删除物流公司
    @ApiOperation(value = "删除物流公司线路", notes = "删除物流公司线路")
    @RequestMapping(value="deletecompany",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_id", value = "公司id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> DeleteCompany(@RequestParam(value = "company_id")Integer company_id,
                                                         HttpServletRequest request) {

        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("4")) {
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
                r = DeleteCompany(company_id);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = DeleteCompany(company_id);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult DeleteCompany(Integer company_id){
        JsonResult r = new JsonResult();
        try {
            boolean issuccess = companyService.delete(company_id);
            if(issuccess){
                r.setCode("200");
                r.setMsg("删除物流公司成功！");
                r.setData(null);
                r.setSuccess(true);
            }else{
                r.setCode("500");
                r.setMsg("删除物流公司失败！");
                r.setData(null);
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.COMPANY_DELETEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_DELETEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
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
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
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


    //角色1,2，3,4
    @ApiOperation(value = "获取所有线路接口", notes = "获取所有线路接口")
    @RequestMapping(value="getalllines",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetAllLines(){
        JsonResult r = new JsonResult();
        r.setCode("200");
        r.setMsg("获取物流公司数量成功！");
        r.setData(linemaplist);
        r.setSuccess(true);
        return ResponseEntity.ok(r);
    }


    public String getAdminToken(){
        User user = userService.getUserByLoginName("system");
        String admintoken  = sysUserTokenService.getToken(user.getId());
        return admintoken;
    }

    //点击上传图片按钮，返回路径接口
    @ApiOperation(value = "点击上传图片按钮，返回路径（应该用不到）", notes = "点击上传图片按钮，返回路径（应该用不到）")
    @RequestMapping(value="getimagepath",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> GetCompanyImagePath(HttpServletRequest request) {

        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("4")) {
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
                r = GetCompanyLicence();
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = GetCompanyLicence();
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }

    public JsonResult GetCompanyLicence(){
        JsonResult r = new JsonResult();
        try {
            String path = UUID.randomUUID().toString()+".png";
            r.setCode("200");
            r.setMsg("生成物流公司营业执照路径成功！");
            r.setData(path);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.COMPANY_LICENCEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_LICENCEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }


    //点击上传图片按钮，返回路径接口
    @ApiOperation(value = "上传图片接口", notes = "上传图片接口")
    @PostMapping(value="/uploadimage",headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> UploadImage(@ApiParam(value="imagefile",required=true)
            MultipartFile imagefile,HttpServletRequest request){

        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("4")) {
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
                r = UploadLicenceImage(imagefile);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = UploadLicenceImage(imagefile);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult UploadLicenceImage(MultipartFile imagefile){
        JsonResult r = new JsonResult();
        try {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                if (imagefile != null) {
                    String fileName = imagefile.getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        // 文件上传的最终保存路径
                        //target/class里面
                        String finalimagePath = ResourceUtils.getURL("classpath:").getPath()+"companyimages/"+UUID.randomUUID().toString()+".png";
                        File outFile = new File(finalimagePath);
                        if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                            // 创建父文件夹
                            outFile.getParentFile().mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(outFile);
                        inputStream = imagefile.getInputStream();
                        IOUtils.copy(inputStream, fileOutputStream);
                        r.setCode("200");
                        r.setMsg("上传物流公司营业执照成功！");
                        r.setData(outFile.getAbsolutePath());
                        r.setSuccess(true);
                    }
                } else {
                    r.setCode(Constant.COMPANY_UPLOADIMAGEFAILURE.getCode()+"");
                    r.setMsg(Constant.COMPANY_UPLOADIMAGEFAILURE.getMsg());
                    r.setSuccess(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                r.setCode(Constant.COMPANY_UPLOADIMAGEFAILURE.getCode()+"");
                r.setMsg(Constant.COMPANY_UPLOADIMAGEFAILURE.getMsg());
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
            r.setCode(Constant.COMPANY_UPLOADIMAGEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_UPLOADIMAGEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }
}
