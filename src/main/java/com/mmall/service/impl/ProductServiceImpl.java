package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.controller.backend.ProductManageController;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 12996 on 2018/6/20.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;


    /**
     * 新增或更新产品
     * @param product
     * @return
     */
    @Override
    public ServiceResponse saveOrUpdateProduct(Product product){
        if (product!=null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length>0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId()!=null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount>0){
                    return ServiceResponse.createBySuccess("更新产品成功");
                }
                return ServiceResponse.createBySuccess("更新产品失败");
            }else {
                int rowCount = productMapper.insert(product);
                if (rowCount>0){
                    return ServiceResponse.createBySuccess("新增产品成功");
                }
                return ServiceResponse.createBySuccess("新增产品失败");
            }        }
        return ServiceResponse.createByErrorMessage("新增或更新产品参数不正确");
    }


    /**
     * 修改销售状态
     * @param productId
     * @param status
     * @return
     */
    public ServiceResponse<String> setSaleStatus(Integer productId,Integer status){
        if (productId==null || status==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount>0){
            return ServiceResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServiceResponse.createByErrorMessage("修改产品销售状态失败");
    }


    public ServiceResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if (productId==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServiceResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);


        return ServiceResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCagegoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

//        productDetailVo.setImageHost(PropertiesUtil.getProperty(""));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null){
            productDetailVo.setParentCategoryId(category.getParentId());
        }else {
            productDetailVo.setParentCategoryId(0);
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServiceResponse<PageInfo> getProductList(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product product: productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVos.add(productListVo);
        }
        PageInfo pageResult  = new PageInfo(productList);
        pageResult.setList(productListVos);
        return ServiceResponse.createBySuccess(pageResult);
    }

    /**
     * 初始化ProductListVo对象
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        //tupian
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        return productListVo;
    }


    /**
     * 对商品惊醒模糊查询
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuffer().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.selectListByNameAndProductId(productName,productId);
        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product product: products){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVos.add(productListVo);
        }
        PageInfo pageResult  = new PageInfo(products);
        pageResult.setList(productListVos);
        return ServiceResponse.createBySuccess(pageResult);
    }



}
