package com.example.transport.service;

import com.example.transport.pojo.CompanyLines;
import com.example.transport.pojo.SysCompany;

public interface CompanyService {
    boolean insert(SysCompany record, CompanyLines companyLines);

    boolean updateByPrimaryKey(SysCompany record, CompanyLines companyLines);

    boolean delete(Integer companyId);
}
