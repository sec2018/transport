package com.example.transport.api;

import com.example.transport.dao.CompanyBillMapper;
import com.example.transport.dao.CompanyLinesMapper;
import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.model.CompanyLinesExample;
import com.example.transport.model.SysCompanyExample;
import com.example.transport.pojo.*;
import com.example.transport.service.*;
import com.example.transport.service.Constant;
import com.example.transport.util.ImageUtil;
import com.example.transport.util.JSONUtil;
import com.example.transport.util.JsonResult;
import com.example.transport.util.graphicsutils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

@RequestMapping("api")
@Controller
public class CompanyApi {

    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    Semaphore companysemaphore = new Semaphore(1);

    @Autowired
    private RedisService redisService;

    @Autowired
    private CompanyLinesMapper companyLinesMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private CompanyBillMapper companyBillMapper;

    @Autowired
    private CompanyBillService companyBillService;

//    private static Map<String,Map<Integer,String>> linemap = new HashMap();
//   public static List<LineMap> linemaplist = new ArrayList<>();


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
        List<Integer> companylist = sysCompanyMapper.selectCheckIdList();
        if(companylist.size()==0){
            return;
        }
        List<LineMap> linemaplist = new ArrayList<>();
        CompanyLinesExample example = new CompanyLinesExample();
        example.createCriteria().andCompanyIdIn(companylist);
        List<CompanyLines> line = companyLinesMapper.selectByExample(example);
        Map<Integer,String> companylistmap;
        String companyname;
        LineMap lp;
        for (CompanyLines cl : line){
            String key = cl.getBeginAddr()+"-->"+cl.getArriveAddr();
            companyname = sysCompanyMapper.selectByPrimaryKey(cl.getCompanyId()).getCompanyName();
            companylistmap = new HashMap<Integer,String>();
            companylistmap.put(cl.getId(),companyname);
            if(linemaplist.size() == 0){
                lp = new LineMap();
                lp.setKey(key);
                lp.setValuemap(companylistmap);
                linemaplist.add(lp);
            }else{
                for(int i=0;i<linemaplist.size();i++){
                    if(linemaplist.get(i).getKey().equals(key)){
                        if(!linemaplist.get(i).getValuemap().containsKey(cl.getCompanyId())){
                            linemaplist.get(i).getValuemap().put(cl.getId(),companyname);
                            break;
                        }
                    }else{
                        companylistmap = new HashMap<Integer,String>();
                        companylistmap.put(cl.getId(),companyname);
                        lp = new LineMap();
                        lp.setKey(key);
                        lp.setValuemap(companylistmap);
                        linemaplist.add(lp);
                        break;
                    }
                }
            }
        }
        try {
            String linemapliststr = JSONUtil.listToJson(linemaplist);
            redisService.remove("linemaplist");
            redisService.setpersist("linemaplist", linemapliststr);
        } catch (Exception e) {
            e.printStackTrace();
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
            @ApiImplicitParam(name = "begin_addr", value = "默认线路出发地", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "arrive_addr", value = "默认线路到站地址", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "arrive_tel", value = "默认线路到达电话", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "arrive_detail_addr", value = "默认线路到站详细地址", required = false, dataType = "String",paramType = "query"),
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
                                                 @RequestParam(value = "begin_addr",required = false)String begin_addr,
                                                 @RequestParam(value = "arrive_addr",required = false)String arrive_addr,
                                                 @RequestParam(value = "arrive_tel",required = false)String arrive_tel,
                                                 @RequestParam(value = "arrive_detail_addr",required = false)String arrive_detail_addr,
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
                r = AddOrUpdateCompany(token,"",companyid,companyname,company_tel,company_procity,
                        company_detailarea,licence_url,complain_tel,service_tel,
                        evaluation,begin_addr,arrive_addr,arrive_tel,arrive_detail_addr,line_id);
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
            r = AddOrUpdateCompany("",tokenvalue,companyid,companyname,company_tel,company_procity,
                    company_detailarea,licence_url,complain_tel,service_tel,
                    evaluation,begin_addr,arrive_addr,arrive_tel,arrive_detail_addr,line_id);
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult AddOrUpdateCompany(String token,String tokenvalue,Integer companyid,String companyname,String company_tel,String company_procity,
                                                 String company_detailarea,String licence_url,String complain_tel,String service_tel,
                                                 Integer evaluation,String begin_addr,String arrive_addr,String arrive_tel,String arrive_detail_addr,
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
            sysCompany.setComplainTel(complain_tel);
            sysCompany.setCompanycheckstatus(0);

            CompanyLines companyLines = new CompanyLines();
            companyLines.setBeginAddr(begin_addr);
            companyLines.setArriveAddr(arrive_addr);
            companyLines.setArriveTel(arrive_tel);
            companyLines.setArriveDetailAddr(arrive_detail_addr);
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



    //角色1,2，3,4(无权限)
//    @ApiOperation(value = "查询所有物流公司(暂不要用)", notes = "查询所有物流公司(暂不要用)")
//    @RequestMapping(value="getcompanies",method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<JsonResult> GetCompanies(){
//        JsonResult r = new JsonResult();
//        try {
//            SysCompanyExample example = new SysCompanyExample();
//            List<SysCompany> companylist = sysCompanyMapper.selectByExample(example);
//            r.setCode("200");
//            r.setMsg("获取物流公司成功！");
//            r.setData(companylist);
//            r.setSuccess(true);
//        } catch (Exception e) {
//            r.setCode(Constant.COMPANY_GETFAILURE.getCode()+"");
//            r.setData(e.getClass().getName() + ":" + e.getMessage());
//            r.setMsg(Constant.COMPANY_GETFAILURE.getMsg());
//            r.setSuccess(false);
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok(r);
//    }

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



    //角色1,4
    @ApiOperation(value = "查询用户物流公司", notes = "查询物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="searchcompany",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> SearchCompany(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("4")){
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
                //查询某公司
            }
        } else {
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
                    sysCompany.setLicenceUrl("/companyimages/"+sysCompany.getLicenceUrl());
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
        }
        return ResponseEntity.ok(r);
    }


    //角色1,4
    @ApiOperation(value = "查询所有物流公司", notes = "查询所有物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getallcompany",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetAllCompany(HttpServletRequest request){
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
                r = GetAllCompanies();
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = GetAllCompanies();
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult GetAllCompanies(){
        JsonResult r = new JsonResult();
        try {
            SysCompanyExample example = new SysCompanyExample();
            List<SysCompany> companylist = sysCompanyMapper.selectByExample(example);
            List<Map<String,Object>> cmaplist = new ArrayList<>();
            Map<String,Object> cmap = null;
            String companycheckstatus = "进行中";
            CompanyLines companyLines = null;
            for(SysCompany sc : companylist){
                cmap = new HashMap<String,Object>();
                cmap.put("company_id",sc.getCompanyId());
                cmap.put("company_name",sc.getCompanyName());
                cmap.put("company_procity",sc.getCompanyProcity());
                cmap.put("company_detailarea",sc.getCompanyDetailarea());
                cmap.put("wxuser_id",sc.getWxuserId());
                cmap.put("nickname",userService.getWxUserById(sc.getWxuserId()).getNickname());
                cmap.put("company_tel",sc.getCompanyTel());
                cmap.put("licence_url",sc.getLicenceUrl());
                cmap.put("complain_tel",sc.getComplainTel());
                cmap.put("service_tel",sc.getServiceTel());
                cmap.put("evaluation",sc.getEvaluation());
                cmap.put("default_lineid",sc.getDefaultLineid());

                switch (sc.getCompanycheckstatus()){
                    case 0:
                        companycheckstatus = "进行中";
                        break;
                    case 1:
                        companycheckstatus = "通过";
                        break;
                    case 2:
                        companycheckstatus = "不通过";
                        break;
                }
                cmap.put("companycheckstatus",companycheckstatus);
                companyLines = companyLinesMapper.selectByPrimaryKey(sc.getDefaultLineid());
                cmap.put("line",companyLines.getBeginAddr()+"-->"+companyLines.getArriveAddr());
                cmap.put("arrive_detail_addr",companyLines.getArriveDetailAddr());
                cmap.put("arrive_tel",companyLines.getArriveTel());
                cmaplist.add(cmap);
            }
            r.setCode("200");
            r.setMsg("获取物流公司成功！");
            r.setData(cmaplist);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.COMPANY_GETFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_GETFAILURE.getMsg());
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
                if(retry == 6){
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
        String unbillstr = redisService.get("linemaplist");
        if(unbillstr==null || unbillstr==""){
            GetAllCompanyLines();
            unbillstr = redisService.get("linemaplist");
        }
        List<LineMap> linemaplist = JSONUtil.jsonToList(unbillstr,LineMap.class);
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
                        String filefinalname = UUID.randomUUID().toString()+".png";
                        String finalimagePath = ResourceUtils.getURL("classpath:").getPath()+"companyimages/"+ filefinalname;
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
//                        r.setData(outFile.getAbsolutePath());
                        r.setData(filefinalname);
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


    //角色0,1
    @Transactional
    @ApiOperation(value = "审核物流公司", notes = "审核物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyid", value = "物流公司id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "ispass", value = "是否通过审核", required = true, dataType = "Boolean",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="admincheckcompany",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> adminCheckCompany(@RequestParam(value = "companyid",required = true)Integer companyid,@RequestParam(value = "ispass",required = true)Boolean ispass,HttpServletRequest request){
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
                SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(companyid);
                WxUser wxUser = userService.getWxUserById(sysCompany.getWxuserId());
                if(ispass){
                    sysCompany.setCompanycheckstatus(1);    //1代表审核通过
                    sysCompanyMapper.updateByPrimaryKey(sysCompany);
                    wxUser.setTrancheckstatus(1);    //1代表审核通过
                    userService.updateWxUser(wxUser);
                    r.setCode("200");
                    r.setMsg("审核物流公司通过！");
                    r.setSuccess(true);
                }else{
                    sysCompany.setCompanycheckstatus(2);    //2代表审核不通过，被驳回
                    sysCompanyMapper.updateByPrimaryKey(sysCompany);
                    wxUser.setTrancheckstatus(2);    //2代表审核不通过，被驳回
                    userService.updateWxUser(wxUser);
                    r.setCode(Constant.COMPANY_CHECKFAILURE.getCode()+"");
                    r.setMsg(Constant.COMPANY_CHECKFAILURE.getMsg());
                    r.setSuccess(true);
                }
                boolean flag = sysCompanyMapper.updateByPrimaryKey(sysCompany)==1?true:false;
                if(flag){
                    r.setData(null);

                }else{
                    r.setCode(Constant.COMPANY_UPDATEFAILURE.getCode()+"");
                    r.setMsg(Constant.COMPANY_UPDATEFAILURE.getMsg());
                    r.setData(null);
                    r.setSuccess(false);
                }
            }
        } catch (Exception e) {
            r.setCode(Constant.COMPANY_UPDATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANY_UPDATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //删除物流公司
    @ApiOperation(value = "根据id获取物流公司", notes = "根据id获取物流公司")
    @RequestMapping(value="searchcompanybyid",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_id", value = "公司id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> GetCompanyById(@RequestParam(value = "company_id")Integer company_id,
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
                SysCompany company = sysCompanyMapper.selectByPrimaryKey(company_id);
                r.setCode("200");
                r.setMsg("获取物流公司成功！");
                r.setData(company);
                r.setSuccess(true);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    SysCompany company = sysCompanyMapper.selectByPrimaryKey(company_id);
                    r.setCode("200");
                    r.setMsg("获取物流公司成功！");
                    r.setData(company);
                    r.setSuccess(true);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }

    //角色1,4
    @ApiOperation(value = "物流公司下单接口", notes = "物流公司下单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_code", value = "运单号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "shop_name", value = "商铺名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_lat", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_lng", value = "维度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsname", value = "商品名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsnum", value = "商品数量", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "billinfo", value = "运单备注", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_name", value = "收件人姓名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_tel", value = "收件人电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_procity", value = "收件人省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_detailarea", value = "收件人详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "price", value = "总价", required = true, dataType = "Double",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "createcompanybill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> createCompanyBill(@RequestParam(value = "company_code")String company_code,@RequestParam(value = "id")Long id, @RequestParam(value = "shop_name")String shop_name,@RequestParam(value = "company_lat")String company_lat,
                                                 @RequestParam(value = "company_lng")String company_lng,@RequestParam(value = "goodsname")String goodsname, @RequestParam(value = "goodsnum")Integer goodsnum,
                                                 @RequestParam(value = "billinfo")String billinfo,@RequestParam(value = "rec_name")String rec_name,
                                                 @RequestParam(value = "rec_tel")String rec_tel,@RequestParam(value = "rec_procity")String rec_procity,@RequestParam(value = "rec_detailarea")String rec_detailarea,@RequestParam(value = "price")double price,HttpServletRequest request) {
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
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
            if(tokenvalue != ""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);
                SysCompany sysCompany = sysCompanyMapper.selectByWxuserid(id);
                //创建订单
                CompanyBill companyBill = new CompanyBill();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                String bill_code = df.format(new Date()) + UUID.randomUUID().toString().substring(0,5);
                companyBill.setBill_code(bill_code);
                companyBill.setCompany_id(sysCompany.getCompanyId());
                companyBill.setCompany_name(sysCompany.getCompanyName());
                companyBill.setCompany_billcode(company_code);
                companyBill.setShop_name(shop_name);
                companyBill.setTrans_id(-1);
                companyBill.setBill_status(1);
                companyBill.setCompany_lat(company_lat);
                companyBill.setCompany_lng(company_lng);
                companyBill.setGoodsname(goodsname);
                companyBill.setGoodsnum(goodsnum);
                companyBill.setBillinfo(billinfo);
                companyBill.setCompany_procity(sysCompany.getCompanyProcity());
                companyBill.setCompany_detailarea(sysCompany.getCompanyDetailarea());
                companyBill.setRec_name(rec_name);
                companyBill.setRec_tel(rec_tel);
                companyBill.setRec_procity(rec_procity);
                companyBill.setRec_detailarea(rec_detailarea);
                companyBill.setPrice(price);
                companyBill.setCreate_time(new Date());
                boolean flag = companyBillMapper.insert(companyBill)==0?false:true;
                if (flag) {
                    String unbillstr = redisService.get("companyunbilllist");
                    if(unbillstr==null || unbillstr==""){
                        List<CompanyBill> unbilllist = companyBillMapper.selectAllCompanyUnBills();
                        try {
                            unbillstr = JSONUtil.listToJson(unbilllist);
                            redisService.remove("companyunbilllist");
                            redisService.set("companyunbilllist", unbillstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    List<CompanyBill> unbilllist = JSONUtil.jsonToList(unbillstr,CompanyBill.class);
                    unbilllist.add(companyBill);
                    String unbillliststr = JSONUtil.listToJson(unbilllist);
                    redisService.remove("companyunbilllist");
                    redisService.set("companyunbilllist", unbillliststr);
                    r.setCode("200");
                    r.setMsg("下单成功！");
                    r.setData(companyBill);
                    r.setSuccess(true);
                }else {
                    r.setCode("500");
                    r.setMsg("下单失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_CREATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_CREATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //角色1,4
    @ApiOperation(value = "物流公司修改下单接口", notes = "物流公司修改下单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_code", value = "运单号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "bill_id", value = "订单id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "shop_name", value = "商铺名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_lat", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_lng", value = "维度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsname", value = "商品名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsnum", value = "商品数量", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "billinfo", value = "运单备注", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_name", value = "收件人姓名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_tel", value = "收件人电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_procity", value = "收件人省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_detailarea", value = "收件人详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "price", value = "总价", required = true, dataType = "Double",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "updatecompanybill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> updateCompanyBill(@RequestParam(value = "company_code")String company_code,@RequestParam(value = "bill_id")Integer bill_id, @RequestParam(value = "shop_name")String shop_name,@RequestParam(value = "company_lat")String company_lat,
                                                        @RequestParam(value = "company_lng")String company_lng,@RequestParam(value = "goodsname")String goodsname, @RequestParam(value = "goodsnum")Integer goodsnum,
                                                        @RequestParam(value = "billinfo")String billinfo,@RequestParam(value = "company_procity")String company_procity,@RequestParam(value = "company_detailarea")String company_detailarea,@RequestParam(value = "rec_name")String rec_name,
                                                        @RequestParam(value = "rec_tel")String rec_tel,@RequestParam(value = "rec_procity")String rec_procity,@RequestParam(value = "rec_detailarea")String rec_detailarea,@RequestParam(value = "price")double price,HttpServletRequest request) {
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
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
            if(tokenvalue != ""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);

                //更新订单
                CompanyBill companyBill = companyBillMapper.selectByPrimaryKey(bill_id);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                String bill_code = df.format(new Date()) + UUID.randomUUID().toString().substring(0,5);
                companyBill.setBill_code(bill_code);
                companyBill.setCompany_billcode(company_code);
                companyBill.setShop_name(shop_name);
                companyBill.setTrans_id(-1);
                companyBill.setBill_status(1);
                companyBill.setCompany_lat(company_lat);
                companyBill.setCompany_lng(company_lng);
                companyBill.setGoodsname(goodsname);
                companyBill.setGoodsnum(goodsnum);
                companyBill.setBillinfo(billinfo);
                companyBill.setCompany_procity(company_procity);
                companyBill.setCompany_detailarea(company_detailarea);
                companyBill.setRec_name(rec_name);
                companyBill.setRec_tel(rec_tel);
                companyBill.setRec_procity(rec_procity);
                companyBill.setRec_detailarea(rec_detailarea);
                companyBill.setPrice(price);
                companyBill.setCreate_time(new Date());
                boolean flag = companyBillMapper.updateCompanyBill(companyBill)==0?false:true;
                if (flag) {
                    List<CompanyBill> unbilllist = companyBillMapper.selectAllCompanyUnBills();
                    try {
                        String unbillstr = JSONUtil.listToJson(unbilllist);
                        redisService.remove("companyunbilllist");
                        redisService.set("companyunbilllist", unbillstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    r.setCode("200");
                    r.setMsg("修改订单成功！");
                    r.setData(companyBill);
                    r.setSuccess(true);
                }else {
                    r.setCode("500");
                    r.setMsg("修改订单失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_UPDATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_UPDATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //1，2,3,4
    @ApiOperation(value = "查询物流公司订单接口(tab查询统一接口，分页)", notes = "查询物流公司订单接口(用tab查询统一接口，分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startitem", value = "startitem", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pagesize", value = "pagesize", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "isfinishflag", value = "是否为完成的订单标志（0：未完成，1：完成）", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getcompanytabbill",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchCompanyUnfinishBillByUserId(@RequestParam(value = "startitem") int startitem,@RequestParam(value = "pagesize") int pagesize,@RequestParam(value = "isfinishflag") int isfinishflag,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            //超级管理员,PC端
            if(roleid.equals("0")){
                tokenvalue = getAdminToken();
                if(!token.equals(tokenvalue)){
                    r = Common.TokenError();
                    return ResponseEntity.ok(r);
                }else{
                    if(isfinishflag == 0){
                        //超级管理员查询未完成订单
                        Map<String, Object> map = companyBillService.adminSelectCompanyUnfinishedBill(startitem,pagesize);
                        r.setData(map);
                    }else if(isfinishflag == 1){
                        //超级管理员查询已完成订单
                        Map<String, Object> map = companyBillService.adminSelectCompanyfinishedBill(startitem,pagesize);
                        r.setData(map);
                    }
                }
                //其他角色
            }else{
                tokenvalue = r.getData().toString();
                try {
                    if(tokenvalue!=""){
                        redisService.expire(token, Constant.expire.getExpirationTime());
                        String openid = tokenvalue.split("\\|")[0];
                        long wxuserid = userService.getWxUserId(openid);
                        try {
                            if(tokenvalue!=""){
                                redisService.expire(token, Constant.expire.getExpirationTime());
                                if(isfinishflag == 0){
                                    if(roleid.equals("3")){
                                        //承运员未完成订单
                                        Map<String, Object> map = companyBillService.selectCompanyUnfinishedBillByTransId(wxuserid,startitem,pagesize);
                                        r.setData(map);
                                    }else if(roleid.equals("4")){
                                        //物流公司未完成订单
                                        int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                                        Map<String, Object> map = companyBillService.selectCompanyUnfinishedBillByCompanyId(companyid,startitem,pagesize);
                                        r.setData(map);
                                    }else{
                                        r = Common.RoleError();
                                        return ResponseEntity.ok(r);
                                    }
                                }else if(isfinishflag == 1){
                                    if(roleid.equals("3")){
                                        //承运员查询所下已完成订单
                                        Map<String, Object> map = companyBillService.selectCompanyfinishedBillByTransId(wxuserid,startitem,pagesize);
                                        r.setData(map);
                                    }else if(roleid.equals("4")){
                                        //物流公司查询所下已完成订单
                                        int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                                        Map<String, Object> map = companyBillService.selectCompanyfinishedBillByCompanyId(companyid,startitem,pagesize);
                                        r.setData(map);
                                    }else{
                                        r = Common.RoleError();
                                        return ResponseEntity.ok(r);
                                    }
                                }
                                r.setCode("200");
                                r.setMsg("查询成功！");
                                r.setSuccess(true);
                            }else{
                                r = Common.TokenError();
                            }
                        } catch (Exception e) {
                            r = Common.SearchError(e);
                            e.printStackTrace();
                        }
                    }else{
                        r = Common.TokenError();
                    }
                } catch (Exception e) {
                    r = Common.TokenError(e);
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            r = Common.TokenError();
            System.out.println(e.getMessage());
            return ResponseEntity.ok(r);
        }
        return ResponseEntity.ok(r);
    }

    //1,2,3,4
    @ApiOperation(value = "根据名称或电话统一查询物流公司订单接口(分页)", notes = "根据名称或电话统一查询物流公司订单接口(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startitem", value = "startitem", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pagesize", value = "pagesize", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "isfinishflag", value = "是否为完成的订单标志（0：未完成，1：完成）", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "sender_param", value = "名称或电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getcompanybillbynameortel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBillByNameOrTel(@RequestParam(value = "startitem") int startitem,@RequestParam(value = "pagesize") int pagesize,@RequestParam(value = "isfinishflag") int isfinishflag,@RequestParam(value = "sender_param") String sender_param,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
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
                Map<String, Object> map = null;
                if(isfinishflag == 0){
                    if(roleid.equals("3")){
                        //承运员
                        map = companyBillService.selectTransUnfinishCompanyBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }else if(roleid.equals("4")){
                        //物流公司
                        int company_id = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                        map = companyBillService.selectUnfinishCompanyBillByTelOrName(company_id,sender_param,startitem,pagesize);
                    }
                }else if(isfinishflag == 1){
                    if(roleid.equals("3")){
                        //承运员
                        map = companyBillService.selectTransFinishCompanyBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }else if(roleid.equals("4")){
                        //物流公司
                        int company_id = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                        map = companyBillService.selectFinishCompanyBillByTelOrName(company_id,sender_param,startitem,pagesize);
                    }
                }
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(map);
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



    //1，2,3,4
    @ApiOperation(value = "用户查询未完成公司订单(用tab查询统一接口替代)", notes = "用户查询未完成公司订单(用tab查询统一接口替代)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getcompanyunfinishbill",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchunfinishCompanyBillByUserId(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
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
                try {
                    if(tokenvalue!=""){
                        redisService.expire(token, Constant.expire.getExpirationTime());
                        if(roleid.equals("3")){
                            //承运员未完成订单
                            List<CompanyBill> billList = companyBillService.selectunfinishedCompanyBillByTransId(wxuserid);
                            r.setData(billList);
                        }else if(roleid.equals("4")){
                            //物流公司未完成订单
                            int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                            List<CompanyBill> billList = companyBillService.selectunfinishedCompanyBillByCompanyId(companyid);
                            r.setData(billList);
                        }else if(roleid.equals("1")){
                            //超级管理员查询未完成订单

                        }else{
                            r = Common.RoleError();
                            return ResponseEntity.ok(r);
                        }
                        r.setCode("200");
                        r.setMsg("查询成功！");
                        r.setSuccess(true);
                    }else{
                        r = Common.TokenError();
                    }
                } catch (Exception e) {
                    r = Common.SearchError(e);
                    e.printStackTrace();
                }
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.TokenError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1,2，3,4
    @ApiOperation(value = "查询所下已完成公司订单(用tab查询统一接口替代)", notes = "查询所下已完成公司订单(用tab查询统一接口替代)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getcompanyfinishedbill",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchFinishedCompanyBillByUserId(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
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
                try {
                    if(tokenvalue!=""){
                        redisService.expire(token, Constant.expire.getExpirationTime());
                        if(roleid.equals("3")){
                            //承运员未完成订单
                            List<CompanyBill> billList = companyBillService.selectfinishedCompanyBillByTransId(wxuserid);
                            r.setData(billList);
                        }else if(roleid.equals("4")){
                            //物流公司未完成订单
                            int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                            List<CompanyBill> billList = companyBillService.selectfinishedCompanyBillByCompanyId(companyid);
                            r.setData(billList);
                        }else if(roleid.equals("1")){
                            //超级管理员查询未完成订单

                        }else{
                            r = Common.RoleError();
                            return ResponseEntity.ok(r);
                        }
                        r.setCode("200");
                        r.setMsg("查询成功！");
                        r.setSuccess(true);
                    }else{
                        r = Common.TokenError();
                    }
                } catch (Exception e) {
                    r = Common.SearchError(e);
                    e.printStackTrace();
                }
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.TokenError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //查询所有最近2公里未接订单
    @ApiOperation(value = "查询所有最近2公里未接公司订单", notes = "查询所有最近2公里未接公司订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "维度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="get2companyunbills",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> search2Unbills(@RequestParam(value = "lng") String lng,@RequestParam(value = "lat") String lat,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("3")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
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
//                List<SysBill> billList = billService.selectBillsByLnglat(lng,lat); // 方法1
                List<CompanyBill> billList = companyBillService.selectCompanyBillsIn2Mills(lng,lat);   // 方法2
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(billList);
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

    //1,4
    @ApiOperation(value = "公司运单完成", notes = "公司运单完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="finishcompanybill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> finishBill(@RequestParam(value = "id") long id, HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("3")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
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
                Date date = new Date();
                boolean flag =  companyBillMapper.finishCompanyBill(date,id)==1?true:false;
                if(flag){
                    List<CompanyBill> unbilllist = companyBillService.selectAllCompanyUnBills();
                    try {
                        String unbillstr = JSONUtil.listToJson(unbilllist);
                        redisService.remove("companyunbilllist");
                        redisService.set("companyunbilllist", unbillstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    r.setCode("200");
                    r.setMsg("运单完成！");
                    r.setData(null);
                    r.setSuccess(true);
                }else{
                    r.setCode("500");
                    r.setMsg("完成运单失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
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


    //1,3
    @ApiOperation(value = "承运员接公司运单接口", notes = "承运员接公司运单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="receivecompanybill",method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public ResponseEntity<JsonResult> updateBillSetTrans_id(@RequestParam(value = "id") long id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("3")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        int availablePermits = companysemaphore.availablePermits();
        if(availablePermits>0){
//            System.out.println("抢单成功！");
        }else{
//            System.out.println("抢单失败！");
            r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
            r.setData(null);
            r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
            r.setSuccess(false);
            return ResponseEntity.ok(r);
        }
        try {
            companysemaphore.acquire(1);
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                CompanyBill bill = companyBillService.selectSingleCompanyBill(id);
                if(bill.getTrans_id()!=-1){
                    r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
                    r.setData(null);
                    r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
                    r.setSuccess(false);
                }
                Date datetime = new Date();
                WxUser wxuser = userService.getWxUser(openid);
                boolean flag = companyBillService.updateCompanyBillSetTrans_id(id,datetime,wxuser.getId(),wxuser.getNickname());
                if (flag) {
                    String unbillstr = redisService.get("companyunbilllist");
                    if(unbillstr==null || unbillstr==""){
                        List<CompanyBill> unbilllist = companyBillService.selectAllCompanyUnBills();
                        try {
                            unbillstr = JSONUtil.listToJson(unbilllist);
                            redisService.remove("companyunbilllist");
                            redisService.set("companyunbilllist", unbillstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    List<CompanyBill> unbilllist = JSONUtil.jsonToList(unbillstr,CompanyBill.class);
                    for(CompanyBill sb : unbilllist){
                        if(sb.getId() == id){
                            sb.setBill_status(2);
                        }
                    }
                    String unbillliststr = JSONUtil.listToJson(unbilllist);
                    redisService.remove("companyunbilllist");
                    redisService.set("companyunbilllist", unbillliststr);
                    System.out.println("抢单成功！");
                    r.setCode("200");
                    r.setMsg("抢单成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
            r.setSuccess(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }finally {
            companysemaphore.release(1);
        }
        return ResponseEntity.ok(r);
    }

    //1
    @ApiOperation(value = "删除未接公司订单接口", notes = "根据id删除公司订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="deletecompanybill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> deleteSenderbill(@RequestParam(value = "id") long id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
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
//                String openid = tokenvalue.split("\\|")[0];
//                long wxuserid = userService.getWxUserId(openid);
                boolean flag = companyBillService.deleteCompanyUnRecBill(id);
                if (flag) {
                    List<CompanyBill> unbilllist = companyBillService.selectAllCompanyUnBills();
                    try {
                        String unbillstr = JSONUtil.listToJson(unbilllist);
                        redisService.remove("companyunbilllist");
                        redisService.set("companyunbilllist", unbillstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    r = Common.DeleteSuccess();
                }else{
                    r = Common.DeleteFailure();
                }
            }else{
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_DELETEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_DELETEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1,4
    @ApiOperation(value = "物流公司确认揽收接口", notes = "物流公司确认揽收接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="confirmcompanybill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> payBill(@RequestParam(value = "id") long id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
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
                //先支付
                Date datetime = new Date();
                boolean flag =  companyBillService.confirmCompanyBill(datetime,id);
                if(flag){
                    String unbillstr = redisService.get("companyunbilllist");
                    if(unbillstr==null || unbillstr==""){
                        List<CompanyBill> unbilllist = companyBillService.selectAllCompanyUnBills();
                        try {
                            unbillstr = JSONUtil.listToJson(unbilllist);
                            redisService.remove("companyunbilllist");
                            redisService.set("companyunbilllist", unbillstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    List<CompanyBill> unbilllist = JSONUtil.jsonToList(unbillstr,CompanyBill.class);
                    for(CompanyBill sb : unbilllist){
                        if(sb.getId() == id){
                            sb.setBill_status(3);
                        }
                    }
                    String unbillliststr = JSONUtil.listToJson(unbilllist);
                    redisService.remove("CompanyBill");
                    redisService.set("CompanyBill", unbillliststr);
                    r.setCode("200");
                    r.setMsg("揽收成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }else{
                    r.setCode("500");
                    r.setMsg("揽收错误！");
                    r.setData(null);
                    r.setSuccess(false);
                }
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


    //得到img
    @ApiOperation(value = "获取运单图片", notes = "获取运单图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getcompanybillimg",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getImg(@RequestParam(value = "id") Integer id,HttpServletRequest request){
        JsonResult r = new JsonResult();
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
                redisService.expire(token, Constant.expire.getExpirationTime());
                CompanyBill bill = companyBillMapper.selectByPrimaryKey(id);
                SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(bill.getCompany_id());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String create_time = formatter.format(bill.getCreate_time());
                String finish_time = formatter.format(bill.getFinish_time());
                String bigtitle = bill.getCompany_name()+"托运单";
                List<List<List<String>>> allValue = new ArrayList<>();
                List<String> content1 = Arrays.asList(new String[]{bill.getRec_name(),bill.getRec_tel(),bill.getRec_procity(),bill.getRec_detailarea()});
                List<String> content2 = Arrays.asList(new String[]{bill.getCompany_name(),sysCompany.getCompanyName(),bill.getCompany_procity(),bill.getCompany_detailarea()});
                List<String> content3 = Arrays.asList(new String[]{bill.getCompany_billcode(),bill.getGoodsname(),bill.getShop_name(),bill.getBillinfo(),create_time});
                List<String> h4 = Arrays.asList(new String[]{"承运员","数量","代收运费","托运号","完成时间"});
                List<String> content4 = Arrays.asList(new String[]{bill.getTrans_name(),bill.getGoodsnum()+"",bill.getPrice()+"",bill.getBill_code(),finish_time});
                List<List<String>> contentArray1 = new ArrayList<>();
                contentArray1.add(content1);
                List<List<String>> contentArray2 = new ArrayList<>();
                contentArray2.add(content2);
                List<List<String>> contentArray3 = new ArrayList<>();
                contentArray3.add(content3);
                contentArray3.add(h4);
                contentArray3.add(content4);

                allValue.add(contentArray1);
                allValue.add(contentArray2);
                allValue.add(contentArray3);

                List<String[]> headTitles = new ArrayList<>();
                String[] h1 = new String[]{"姓名","电话","省市区","详细地址"};
                String[] h2 = new String[]{"物流","电话","省市区","详细地址"};
                String[] h3 = new String[]{"运单号","品名","店铺名称","备注","下单时间"};
                headTitles.add(h1);
                headTitles.add(h2);
                headTitles.add(h3);

                List<String> titles = new ArrayList<>();
                titles.add("收件人信息");
                titles.add("寄件人信息");
                titles.add("运单信息");
                BufferedImage image = graphicsutils.graphicsGeneration(allValue,titles,headTitles ,"",4,bigtitle);
                //旋转90度
                image = ImageUtil.rotateImage(image,90);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
                ImageIO.write(image, "png", baos);//写入流中
                byte[] bytes = baos.toByteArray();//转换成字节
                BASE64Encoder encoder = new BASE64Encoder();
                String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
                png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n

                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(png_base64);
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

}
