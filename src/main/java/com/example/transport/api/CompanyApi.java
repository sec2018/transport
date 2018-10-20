package com.example.transport.api;

import com.example.transport.model.SysCompanyExample;
import com.example.transport.pojo.SysCompany;
import com.example.transport.service.Constant;
import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.service.SysCompanyService;
import com.example.transport.util.JsonResult;
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
    private SysCompanyService sysCompanyService;

    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    //角色1,4
    @ApiOperation(value = "添加物流公司", notes = "根据物流公司名称添加物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyname", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="addcompany",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> AddCompany(@RequestParam(value = "companyname")String companyname, HttpServletRequest request){
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(roleid != "1" && roleid != "4"){
            r.setCode(Constant.ROLE_ERROR.getCode()+"");
            r.setData(null);
            r.setMsg(Constant.ROLE_ERROR.getMsg());
            r.setSuccess(false);
            return ResponseEntity.ok(r);
        }
        SysCompany sysCompany = new SysCompany();
        sysCompany.setCompany_name(companyname);
        try {
            boolean flag = sysCompanyService.insertCompany(sysCompany);
            if(flag){
                r.setCode("200");
                r.setMsg("添加物流公司成功！");
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
        return ResponseEntity.ok(r);
    }

    //角色1,2，3,4
    @ApiOperation(value = "查询所有物流公司", notes = "查询所有物流公司")
    @RequestMapping(value="getcompanies",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetCompanies(){
        JsonResult r = new JsonResult();
        try {
            List<SysCompany> companylist = sysCompanyService.getCompanies();
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
    public ResponseEntity<JsonResult> GetCompanies1(){
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
}
