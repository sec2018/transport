package com.example.transport.api;

import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.dao.SysRoleMapper;
import com.example.transport.model.SysCompanyExample;
import com.example.transport.model.SysRoleExample;
import com.example.transport.pojo.SysRole;
import com.example.transport.service.Constant;
import com.example.transport.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("api")
@Controller
public class RoleApi {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @ApiOperation(value = "查询所有角色列表", notes = "查询所有角色列表")
    @RequestMapping(value="getrolelist",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetRoleList(){
        JsonResult r = new JsonResult();
        try {
            SysRoleExample example = new SysRoleExample();
            List<SysRole> sysRoleList  = sysRoleMapper.selectByExample(example);
            r.setCode("200");
            r.setMsg("获取物流公司数量成功！");
            r.setData(sysRoleList);
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
