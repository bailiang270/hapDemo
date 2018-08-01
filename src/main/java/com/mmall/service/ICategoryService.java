package com.mmall.service;

import com.mmall.common.ServiceResponse;
import org.springframework.stereotype.Service;

/**
 * Created by 12996 on 2018/6/7.
 */
@Service
public interface ICategoryService {

    /**
     * 添加品类
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServiceResponse addCategory(String categoryName, Integer parentId);

    /**
     * 更新品类
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServiceResponse updateCategoryName(Integer categoryId,String categoryName);

    /**
     * 获取categoryId的所有子分类
     * @param categoryId
     * @return
     */
    public ServiceResponse getChildrenParallelCategory(Integer categoryId);

    /**
     * 递归查询本节点id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServiceResponse selectCategoryAndChildrenById(Integer categoryId);
}
