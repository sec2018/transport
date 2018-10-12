package com.example.transport.service;

import com.example.transport.pojo.SysCompany;

import java.util.List;

public interface SysCompanyService {

    boolean insertCompany(SysCompany sysCompany);

    List<SysCompany> getCompanies();
}
