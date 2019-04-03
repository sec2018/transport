package com.example.transport.service.impl;

import com.example.transport.dao.SysTranMapper;
import com.example.transport.pojo.SysTran;
import com.example.transport.pojo.WxUser;
import com.example.transport.service.SysTranService;
import com.example.transport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * Created by WangZJ on 2019/4/3.
 */
@Service("sysTranService")
public class SysTranServiceImpl implements SysTranService{


    @Autowired
    private SysTranMapper sysTranMapper;

    @Autowired
    private UserService userService;


    @Transactional
    @Override
    public boolean updateByPrimaryKey(SysTran record) {
        try{
            boolean flag1 =  sysTranMapper.updateByPrimaryKey(record)==1?true:false;
            WxUser wxUser = userService.getWxUserById(record.getWxuserId());
            wxUser.setTrancheckstatus(2);                             //需重新审核
            boolean flag2 = userService.updateWxUser(wxUser);
            if(flag1 && flag2){
                return true;
            }else{
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
