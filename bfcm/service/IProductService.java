package com.seanco.bfcm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seanco.bfcm.pojo.Product;
import com.seanco.bfcm.vo.ProductVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
public interface IProductService extends IService<Product> {

    List<ProductVo> findProductVo();

    ProductVo findProductVoById(Long productId);
}
