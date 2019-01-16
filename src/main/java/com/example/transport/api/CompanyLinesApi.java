package com.example.transport.api;

import com.example.transport.dao.CompanyLinesMapper;
import com.example.transport.pojo.CompanyLines;
import com.example.transport.pojo.User;
import com.example.transport.service.Constant;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("api")
@Controller
public class CompanyLinesApi {

    @Autowired
    private UserService userService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private CompanyLinesMapper companyLinesMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;


    @ApiOperation(value = "添加线路或修改线路", notes = "添加线路或修改线路")
    @RequestMapping(value = "addorupdatecompanylines", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyid", value = "物流公司id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "添加或更新标识（0为添加，1为更新）", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "lines_json", value = "线路json，包括（begin_addr，arrive_addr，arrive_tel）", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "line_id", value = "默认线路id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> AddOrUpdateCompanyLines(@RequestParam(value = "companyid") Integer companyid,
                                                              @RequestParam(value = "flag") Integer flag,
                                                              @RequestParam(value = "lines_json") String lines_json,
                                                              @RequestParam(value = "line_id", required = false) Integer line_id,
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
                r = AddOrUpdateCompanyLines(companyid,flag,lines_json,line_id);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if(tokenvalue!=null){
                    r = AddOrUpdateCompanyLines(companyid,flag,lines_json,line_id);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult AddOrUpdateCompanyLines(Integer companyid,Integer flag,String lines_json,Integer line_id){
        JsonResult r = new JsonResult();
        try {
            //添加线路
            if(flag == 0){
                List<CompanyLines> companyLinesList = new ArrayList<CompanyLines>();
                CompanyLines companyLines = null;
                JSONArray json = JSONArray.fromObject(lines_json);//先将对象转成json数组
                if(json.size()>0){
                    for (int i=0;i<json.size();i++){
                        JSONObject jsonObject = json.getJSONObject(i);
                        companyLines = new CompanyLines();
                        companyLines.setCompanyId(companyid);
                        companyLines.setBeginAddr(jsonObject.getString("begin_addr"));
                        companyLines.setArriveAddr(jsonObject.getString("arrive_addr"));
                        companyLines.setArriveTel(jsonObject.getString("arrive_tel"));
                        companyLinesList.add(companyLines);
                    }
                }
                int issuccess = companyLinesMapper.insertLineList(companyLinesList);
                if(issuccess!=0){
                    r.setCode("200");
                    r.setMsg("插入物流公司线路成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }else{
                    r.setCode("500");
                    r.setMsg("插入物流公司线路失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }
            //更新线路
            else if(flag == 1){
                JSONArray json = JSONArray.fromObject(lines_json);//先将对象转成json数组
                if(json.size()>0){
                    JSONObject jsonObject = json.getJSONObject(0);
                    CompanyLines companyLines = companyLinesMapper.selectByPrimaryKey(line_id);
                    companyLines.setBeginAddr(jsonObject.getString("begin_addr"));
                    companyLines.setArriveAddr(jsonObject.getString("arrive_addr"));
                    companyLines.setArriveTel(jsonObject.getString("arrive_tel"));
                    int issuccess = companyLinesMapper.updateByPrimaryKey(companyLines);
                    if(issuccess!=0){
                        r.setCode("200");
                        r.setMsg("更新物流公司线路成功！");
                        r.setData(null);
                        r.setSuccess(true);
                    }else{
                        r.setCode("500");
                        r.setMsg("更新物流公司线路失败！");
                        r.setData(null);
                        r.setSuccess(false);
                    }
                }else{
                    r.setCode("500");
                    r.setMsg("更新物流公司线路失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r.setCode("500");
                r.setMsg("添加或更新物流公司线路失败！");
                r.setData(null);
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.COMPANYLINE_ADDORUPDATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANYLINE_ADDORUPDATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }

    @ApiOperation(value = "删除物流公司线路", notes = "删除物流公司线路")
    @RequestMapping(value="deletecompanylines",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "line_id", value = "默认线路id", dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> DeleteCompanyLines(@RequestParam(value = "line_id")Integer line_id,
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
                r = DeleteCompanyLines(line_id);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = DeleteCompanyLines(line_id);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult DeleteCompanyLines(Integer line_id){
        JsonResult r = new JsonResult();
        try {
            int issuccess = companyLinesMapper.deleteByPrimaryKey(line_id);
            if(issuccess!=0){
                r.setCode("200");
                r.setMsg("删除物流公司线路成功！");
                r.setData(null);
                r.setSuccess(true);
            }else{
                r.setCode("500");
                r.setMsg("删除物流公司线路失败！");
                r.setData(null);
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.COMPANYLINE_DELETEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANYLINE_DELETEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
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

    public String getAdminToken(){
        User user = userService.getUserByLoginName("system");
        String admintoken  = sysUserTokenService.getToken(user.getId());
        return admintoken;
    }
}
