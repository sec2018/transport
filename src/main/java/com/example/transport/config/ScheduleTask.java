package com.example.transport.config;

import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.model.SysCompanyExample;
import com.example.transport.pojo.SysCompany;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Component
@Configurable
@EnableScheduling
public class ScheduleTask {

    private static int count = 0;

    private static final Logger logger = LogManager.getLogger(ScheduleTask.class);

    @Autowired
    private SysCompanyMapper sysCompanyMapper;


    @Transactional
    @Scheduled(cron = "10 53 9 * * ?")   //每天17点07分10秒触发任务
//    @Scheduled(fixedRate = 50)
    public void FormatLicenceUrl() {

        SysCompanyExample example = new SysCompanyExample();
        List<SysCompany> listcompany = sysCompanyMapper.selectByExample(example);
        String licence_url = "";
        String new_licence_url = "";
        for(SysCompany sc : listcompany){
            licence_url = sc.getLicenceUrl();
            if(licence_url.length()>15){
                try {
                    licence_url = ResourceUtils.getURL("classpath:").getPath()+"companyimages/"+licence_url;
                    new_licence_url = sc.getCompanyId()+"";
                    licence_url = FixFileName(licence_url,new_licence_url);
                    System.out.println(licence_url);
                    //修改数据库
                    sc.setLicenceUrl(new_licence_url+".png");
                    sysCompanyMapper.updateByPrimaryKey(sc);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }

    }

    /**
     * 通过文件路径直接修改文件名
     * @param filePath 需要修改的文件的完整路径
     * @param newFileName 需要修改的文件的名称
     * @return
     */
    private String FixFileName(String filePath, String newFileName) {
        File f = new File(filePath);
        if (!f.exists()) { // 判断原文件是否存在
            return null;
        }

        newFileName = newFileName.trim();
        if ("".equals(newFileName) || newFileName == null) // 文件名不能为空
            return null;

        String newFilePath = null;
        if (f.isDirectory()) { // 判断是否为文件夹
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/"))+ "/"  + newFileName + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        if (!f.exists()) { // 判断需要修改为的文件是否存在（防止文件名冲突）
            return null;
        }

        try {
            f.renameTo(nf); // 修改文件名
        } catch(Exception err) {
            err.printStackTrace();
            return null;
        }
        return newFilePath;
    }
}

