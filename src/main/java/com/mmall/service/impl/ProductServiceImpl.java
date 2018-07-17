package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2018/7/17.
 */

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse saveOrUpdateProduct(Product product){
        if(product != null){
            if(org.apache.commons.lang3.StringUtils.isNoneBlank(product.getSubImages())){
                String[] subImageList = product.getSubImages().split(",");
                if(subImageList.length>0){
                    product.setMainImage(subImageList[0]);
                }
            }
            if(product.getId()==null){
                int rowCount = productMapper.insert(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMessage("产品添加成功");
                }
                return ServerResponse.createByErrorMessage("产品添加失败");
            }else{
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMessage("产品更新成功");
                }else{
                    return ServerResponse.createByErrorMessage("产品更新失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("新增产品参数不正确");
    }

    public ServerResponse setProductStatus(Integer productId,Integer status){
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("产品状态更新成功");
        }
        return ServerResponse.createByErrorMessage("产品状态更新失败");
    }

}
