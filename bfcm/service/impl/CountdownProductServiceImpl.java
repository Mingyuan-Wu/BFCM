package com.seanco.bfcm.service.impl;

import com.seanco.bfcm.pojo.CountdownProduct;
import com.seanco.bfcm.mapper.CountdownProductMapper;
import com.seanco.bfcm.service.ICountdownProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
@Service
public class CountdownProductServiceImpl extends ServiceImpl<CountdownProductMapper, CountdownProduct> implements ICountdownProductService {

}
