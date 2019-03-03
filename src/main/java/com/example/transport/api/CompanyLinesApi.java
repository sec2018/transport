package com.example.transport.api;

import com.example.transport.dao.CompanyLinesMapper;
import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.model.CompanyLinesExample;
import com.example.transport.pojo.CompanyLines;
import com.example.transport.pojo.LineMap;
import com.example.transport.pojo.User;
import com.example.transport.service.Constant;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.JSONUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api")
@Controller
public class CompanyLinesApi {

    @Autowired
    private UserService userService;

    @Autowired
    private SysCompanyMapper sysCompanyMapper;

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
            @ApiImplicitParam(name = "lines_json", value = "线路json，包括（begin_addr，arrive_addr，arrive_tel,arrive_detail_addr）", required = true, dataType = "String", paramType = "query"),
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
                r = AddOrUpdateCompanyLines(companyid, flag, lines_json, line_id);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = AddOrUpdateCompanyLines(companyid, flag, lines_json, line_id);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult AddOrUpdateCompanyLines(Integer companyid, Integer flag, String lines_json, Integer line_id) {
        JsonResult r = new JsonResult();
        String unbillstr = redisService.get("linemaplist");
        if(unbillstr==null || unbillstr==""){
            new CompanyApi().GetAllCompanyLines();
            unbillstr = redisService.get("linemaplist");
        }
        List<LineMap> linemaplist = JSONUtil.jsonToList(unbillstr,LineMap.class);
        try {
            //添加线路
            if (flag == 0) {
                List<CompanyLines> companyLinesList = new ArrayList<CompanyLines>();
                JSONArray json = JSONArray.fromObject(lines_json);//先将对象转成json数组
                if (json.size() > 0) {
                    for (int i = 0; i < json.size(); i++) {
                        JSONObject jsonObject = json.getJSONObject(i);
                        CompanyLines companyLines = new CompanyLines();
                        companyLines.setId(0);
                        companyLines.setCompanyId(companyid);
                        companyLines.setBeginAddr(jsonObject.getString("begin_addr"));
                        companyLines.setArriveAddr(jsonObject.getString("arrive_addr"));
                        companyLines.setArriveTel(jsonObject.getString("arrive_tel"));
                        companyLines.setArriveDetailAddr(jsonObject.getString("arrive_detail_addr"));
                        int issuccess = companyLinesMapper.insert(companyLines);
                        String key = companyLines.getBeginAddr() + "-->" + companyLines.getArriveAddr();
                        String companyname = sysCompanyMapper.selectByPrimaryKey(companyLines.getCompanyId()).getCompanyName();
                        Map<Integer, String> companylistmap;
                        LineMap lp;
                        if (issuccess != 0) {
                            for (int k = 0; k < linemaplist.size(); k++) {
                                if (linemaplist.get(k).getKey().equals(key)) {
                                    if (!linemaplist.get(k).getValuemap().containsKey(companyLines.getCompanyId())) {
                                        linemaplist.get(k).getValuemap().put(companyLines.getId(), companyname);
                                        break;
                                    }
                                } else {
                                    companylistmap = new HashMap<Integer, String>();
                                    companylistmap.put(companyLines.getId(), companyname);
                                    lp = new LineMap();
                                    lp.setKey(key);
                                    lp.setValuemap(companylistmap);
                                    linemaplist.add(lp);
                                    break;
                                }
                            }
                            r.setCode("200");
                            r.setMsg("添加物流公司线路成功！");
                            r.setData(null);
                            r.setSuccess(true);
                        } else {
                            r.setCode("500");
                            r.setMsg("添加物流公司线路失败！");
                            r.setData(null);
                            r.setSuccess(false);
                        }

//                        companyLinesList.add(companyLines);
                    }
                }
//                int issuccess = companyLinesMapper.insertLineList(companyLinesList);
//                if (issuccess != 0) {
//                    r.setCode("200");
//                    r.setMsg("添加物流公司线路成功！");
//                    r.setData(null);
//                    r.setSuccess(true);
//                } else {
//                    r.setCode("500");
//                    r.setMsg("添加物流公司线路失败！");
//                    r.setData(null);
//                    r.setSuccess(false);
//                }
            }
            //更新线路
            else if (flag == 1) {
                JSONArray json = JSONArray.fromObject(lines_json);//先将对象转成json数组
                if (json.size() > 0) {
                    JSONObject jsonObject = json.getJSONObject(0);
                    CompanyLines companyLines = companyLinesMapper.selectByPrimaryKey(line_id);
                    companyLines.setBeginAddr(jsonObject.getString("begin_addr"));
                    companyLines.setArriveAddr(jsonObject.getString("arrive_addr"));
                    companyLines.setArriveTel(jsonObject.getString("arrive_tel"));
                    companyLines.setArriveDetailAddr(jsonObject.getString("arrive_detail_addr"));
                    int issuccess = companyLinesMapper.updateByPrimaryKey(companyLines);
                    String key = companyLines.getBeginAddr() + "-->" + companyLines.getArriveAddr();
                    String companyname = sysCompanyMapper.selectByPrimaryKey(companyLines.getCompanyId()).getCompanyName();
                    Map<Integer, String> companylistmap;
                    LineMap lp;
                    if (issuccess != 0) {
                        for (int k = 0; k < linemaplist.size(); k++) {
                            if (linemaplist.get(k).getKey().equals(key)) {
                                if (!linemaplist.get(k).getValuemap().containsKey(companyLines.getCompanyId())) {
                                    linemaplist.get(k).getValuemap().put(companyLines.getId(), companyname);
                                    break;
                                }
                            } else {
                                companylistmap = new HashMap<Integer, String>();
                                companylistmap.put(companyLines.getId(), companyname);
                                lp = new LineMap();
                                lp.setKey(key);
                                lp.setValuemap(companylistmap);
                                linemaplist.add(lp);
                                break;
                            }
                        }
                        r.setCode("200");
                        r.setMsg("更新物流公司线路成功！");
                        r.setData(null);
                        r.setSuccess(true);
                    } else {
                        r.setCode("500");
                        r.setMsg("更新物流公司线路失败！");
                        r.setData(null);
                        r.setSuccess(false);
                    }
                } else {
                    r.setCode("500");
                    r.setMsg("更新物流公司线路失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            } else {
                r.setCode("500");
                r.setMsg("添加或更新物流公司线路失败！");
                r.setData(null);
                r.setSuccess(false);
            }
            String linemapliststr = JSONUtil.listToJson(linemaplist);
            redisService.remove("linemaplist");
            redisService.set("linemaplist", linemapliststr);
        } catch (Exception e) {
            r.setCode(Constant.COMPANYLINE_ADDORUPDATEFAILURE.getCode() + "");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANYLINE_ADDORUPDATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }

    @ApiOperation(value = "删除物流公司线路", notes = "删除物流公司线路")
    @RequestMapping(value = "deletecompanylines", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "line_id", value = "默认线路id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> DeleteCompanyLines(@RequestParam(value = "line_id") Integer line_id,
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


    public JsonResult DeleteCompanyLines(Integer line_id) {
        JsonResult r = new JsonResult();
        String unbillstr = redisService.get("linemaplist");
        if(unbillstr==null || unbillstr==""){
            new CompanyApi().GetAllCompanyLines();
            unbillstr = redisService.get("linemaplist");
        }
        List<LineMap> linemaplist = JSONUtil.jsonToList(unbillstr,LineMap.class);
        try {
            int issuccess = companyLinesMapper.deleteByPrimaryKey(line_id);
            if (issuccess != 0) {
                CompanyLinesExample example = new CompanyLinesExample();
                List<CompanyLines> line = companyLinesMapper.selectByExample(example);
                Map<Integer, String> companylistmap;
                String companyname;
                LineMap lp;
                for (CompanyLines cl : line) {
                    String key = cl.getBeginAddr() + "-->" + cl.getArriveAddr();
                    companyname = sysCompanyMapper.selectByPrimaryKey(cl.getCompanyId()).getCompanyName();
                    companylistmap = new HashMap<Integer, String>();
                    companylistmap.put(cl.getId(), companyname);
                    if (linemaplist.size() == 0) {
                        lp = new LineMap();
                        lp.setKey(key);
                        lp.setValuemap(companylistmap);
                        linemaplist.add(lp);
                    } else {
                        for (int i = 0; i < linemaplist.size(); i++) {
                            if (linemaplist.get(i).getKey().equals(key)) {
                                if (!linemaplist.get(i).getValuemap().containsKey(cl.getCompanyId())) {
                                    linemaplist.get(i).getValuemap().put(cl.getId(), companyname);
                                    break;
                                }
                            } else {
                                companylistmap = new HashMap<Integer, String>();
                                companylistmap.put(cl.getId(), companyname);
                                lp = new LineMap();
                                lp.setKey(key);
                                lp.setValuemap(companylistmap);
                                linemaplist.add(lp);
                                break;
                            }
                        }
                    }
                }
                r.setCode("200");
                r.setMsg("删除物流公司线路成功！");
                r.setData(null);
                r.setSuccess(true);
            } else {
                r.setCode("500");
                r.setMsg("删除物流公司线路失败！");
                r.setData(null);
                r.setSuccess(false);
            }
            String linemapliststr = JSONUtil.listToJson(linemaplist);
            redisService.remove("linemaplist");
            redisService.set("linemaplist", linemapliststr);
        } catch (Exception e) {
            r.setCode(Constant.COMPANYLINE_DELETEFAILURE.getCode() + "");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANYLINE_DELETEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }


    @ApiOperation(value = "根据物流公司id获取所有线路", notes = "根据物流公司id获取所有线路")
    @RequestMapping(value = "getcompanylines", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_id", value = "公司id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> getCompanyLines(@RequestParam(value = "company_id") Integer company_id,
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
                r = getCompanyLinesByCompanyId(company_id);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = getCompanyLinesByCompanyId(company_id);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult getCompanyLinesByCompanyId(Integer company_id) {
        JsonResult r = new JsonResult();
        try {
            CompanyLinesExample example = new CompanyLinesExample();
            CompanyLinesExample.Criteria criteria = example.createCriteria();
            criteria.andCompanyIdEqualTo(company_id);
            List<CompanyLines> lineslist = companyLinesMapper.selectByExample(example);
            r.setCode("200");
            r.setMsg("获取物流公司线路成功！");
            r.setData(lineslist);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.COMPANYLINE_GETFAILURE.getCode() + "");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANYLINE_GETFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }

    @ApiOperation(value = "根据线路id获取线路", notes = "根据线路id获取线路")
    @RequestMapping(value = "getcompanylinesbyid", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "line_id", value = "默认线路id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> getCompanyLinesById(@RequestParam(value = "line_id") Integer line_id,
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
                r = getCompanyLinesById(line_id);
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = getCompanyLinesById(line_id);
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }

    public JsonResult getCompanyLinesById(Integer line_id) {
        JsonResult r = new JsonResult();
        try {
            CompanyLines companyLines = companyLinesMapper.selectByPrimaryKey(line_id);
            r.setCode("200");
            r.setMsg("获取物流公司线路成功！");
            r.setData(companyLines);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.COMPANYLINE_GETFAILURE.getCode() + "");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.COMPANYLINE_GETFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return r;
    }


    public JsonResult ConnectRedisCheckToken(String token) {
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry <= 3) {
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
