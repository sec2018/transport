package com.example.transport.api;

import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import com.example.transport.service.Constant;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by WangZJ on 2018/10/20.
 */
@Component
public class Common {

    @Resource
    private BillService billService;

    public static List<SysBill> unbilllist;

    @PostConstruct
    public void init() {
        this.unbilllist = billService.selectAllUnBills();
    }

    public static JsonResult TokenError(){
        JsonResult r = new JsonResult();
        r.setCode(Constant.TOKEN_ERROR.getCode()+"");
        r.setData(null);
        r.setMsg(Constant.TOKEN_ERROR.getMsg());
        r.setSuccess(false);
        return r;
    }

    public static JsonResult TokenError(Exception e){
        JsonResult r = new JsonResult();
        r.setCode(Constant.TOKEN_ERROR.getCode()+"");
        r.setData(e.getClass().getName() + ":" + e.getMessage());
        r.setMsg(Constant.TOKEN_ERROR.getMsg());
        r.setSuccess(false);
        return r;
    }

    public static JsonResult RoleError(){
        JsonResult r = new JsonResult();
        r.setCode(Constant.ROLE_ERROR.getCode()+"");
        r.setData(null);
        r.setMsg(Constant.ROLE_ERROR.getMsg());
        r.setSuccess(false);
        return r;
    }

    public static JsonResult SearchError(Exception e){
        JsonResult r = new JsonResult();
        r.setCode("500");
        r.setData(e.getClass().getName() + ":" + e.getMessage());
        r.setMsg("查询失败！");
        r.setSuccess(false);
        return r;
    }

    public static JsonResult BillUpdateSuccess(){
        JsonResult r = new JsonResult();
        r.setCode("200");
        r.setMsg("更新成功！");
        r.setData(null);
        r.setSuccess(true);
        return r;
    }

    public static JsonResult BillUpdateFailure(){
        JsonResult r = new JsonResult();
        r.setCode("500");
        r.setMsg("更新失败！");
        r.setData(null);
        r.setSuccess(false);
        return r;
    }


    public static JsonResult BillUpdateError(Exception e){
        JsonResult r = new JsonResult();
        r.setCode(Constant.BILL_UPDATEFAILURE.getCode()+"");
        r.setData(e.getClass().getName() + ":" + e.getMessage());
        r.setMsg(Constant.BILL_UPDATEFAILURE.getMsg());
        r.setSuccess(false);
        return r;
    }

    public static JsonResult DeleteSuccess(){
        JsonResult r = new JsonResult();
        r.setCode("200");
        r.setMsg("删除成功！");
        r.setData(null);
        r.setSuccess(true);
        return r;
    }

    public static JsonResult DeleteFailure(){
        JsonResult r = new JsonResult();
        r.setCode("500");
        r.setMsg("删除失败！");
        r.setData(null);
        r.setSuccess(false);
        return r;
    }
}
