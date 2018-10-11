package com.example.transport.controller;

import com.example.transport.pojo.SysCompany;
import com.example.transport.service.Constant;
import com.example.transport.service.SysCompanyService;
import com.example.transport.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("api")
@Controller
public class CompanyController {

    @Autowired
    private SysCompanyService sysCompanyService;

    @RequestMapping("addcompany")
    @ResponseBody
    public R AddCompany(@RequestParam(value = "companyname")String companyname){
        SysCompany sysCompany = new SysCompany();
        sysCompany.setCompany_name(companyname);
        boolean flag = sysCompanyService.insertCompany(sysCompany);
        if(flag){
            return R.ok();
        }
        return R.error(Constant.COMPANY_ADDFAILURE.getCode(),Constant.COMPANY_ADDFAILURE.getMsg());
    }

}
