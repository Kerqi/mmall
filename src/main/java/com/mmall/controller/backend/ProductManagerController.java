package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/7/17.
 */

@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse saveProduct(HttpSession session, Product product){
        User currentuser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentuser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，需要强制登录");
        }

        if(iUserService.checkAdminRole(currentuser).isSuccess()){
            //填充增加产品的
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("不是管理员无法进行操作");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Product product){
        User currentuser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentuser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，需要强制登录");
        }

        if(iUserService.checkAdminRole(currentuser).isSuccess()){
            //更新产品状态
            return iProductService.setProductStatus(product.getId(),product.getStatus());

        }else{
            return ServerResponse.createByErrorMessage("不是管理员无法进行操作");
        }
    }
}
