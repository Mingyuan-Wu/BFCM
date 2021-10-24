package com.seanco.bfcm.service.impl;

import com.seanco.bfcm.pojo.Product;
import com.seanco.bfcm.mapper.ProductMapper;
import com.seanco.bfcm.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seanco.bfcm.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductVo> findProductVo() {
        return productMapper.findProductVo();
    }

    @Override
    public ProductVo findProductVoById(Long productId) {
        return productMapper.findProductVoById(productId);
    }
}
