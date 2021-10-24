package com.seanco.bfcm.mapper;

import com.seanco.bfcm.pojo.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seanco.bfcm.vo.ProductVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
public interface ProductMapper extends BaseMapper<Product> {

    List<ProductVo> findProductVo();

    ProductVo findProductVoById(Long productId);
}
