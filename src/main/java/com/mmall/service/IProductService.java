package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;
import org.springframework.stereotype.Service;

/**
 * Created by 12996 on 2018/6/20.
 */
@Service
public interface IProductService {

    /**
     * 新增或更新产品
     * @param product
     * @return
     */
    public ServiceResponse saveOrUpdateProduct(Product product);

    /**
     * 修改销售状态
     * @param productId
     * @param status
     * @return
     */
    public ServiceResponse<String> setSaleStatus(Integer productId,Integer status);

    public ServiceResponse<ProductDetailVo> manageProductDetail(Integer productId);

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServiceResponse<PageInfo> getProductList(int pageNum, int pageSize);

    /**
     * 商品的模糊搜索（后台）
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,Integer pageNum,Integer pageSize);
}
