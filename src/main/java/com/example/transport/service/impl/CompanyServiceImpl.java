package com.example.transport.service.impl;

import com.example.transport.dao.CompanyLinesMapper;
import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.pojo.CompanyLines;
import com.example.transport.pojo.SysCompany;
import com.example.transport.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


@Service("companyService")
public class CompanyServiceImpl implements CompanyService{

    @Autowired
    private SysCompanyMapper sysCompanyMapper;


    @Autowired
    private CompanyLinesMapper companyLinesMapper;


    @Override
    @Transactional
    public boolean insert(SysCompany record, CompanyLines companyLines) {
        try{
            int companyid = sysCompanyMapper.insert(record);
            if(companyid>0 && record.getCompanyId()!=null){
                companyLines.setCompanyId(record.getCompanyId());
                boolean flag  = companyLinesMapper.insert(companyLines)==1?true:false;
                if(flag){
                    return true;
                }else{
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return false;
                }
            }else{
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean updateByPrimaryKey(SysCompany record, CompanyLines companyLines) {
        try{
            boolean flag = sysCompanyMapper.updateByPrimaryKey(record)==1?true:false;
            if(flag){
                flag  = companyLinesMapper.updateByPrimaryKey(companyLines)==1?true:false;
                if(flag){
                    return true;
                }else{
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return false;
                }
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean delete(Integer companyId) {
        try{
            boolean flag = sysCompanyMapper.deleteByPrimaryKey(companyId)==1?true:false;
            if(flag){
                flag  = companyLinesMapper.deleteByCompanyId(companyId)>0?true:false;
                if(flag){
                    return true;
                }else{
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return false;
                }
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
