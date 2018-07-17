package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import com.sun.xml.internal.ws.api.server.SDDocument;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by geely
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类名称错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> childrenCategroy = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(childrenCategroy.isEmpty()){
            logger.info("未能找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(childrenCategroy);

    }

    public ServerResponse getCategoryAndChildrenCategory(Integer categoryId){
        Set<Category>  categorySet = Sets.newHashSet();
        if(categoryId != null){
            findChildCategory(categorySet,categoryId);
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        for(Category categoryEx:categorySet){
            categoryIdList.add(categoryEx.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    public Set<Category> findChildCategory(Set<Category> categorySet , Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        List<Category> categoryChildren = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryEx :categoryChildren){
            findChildCategory(categorySet,categoryEx.getId());
        }
        return categorySet;

    }








}
