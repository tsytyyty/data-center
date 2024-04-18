package com.data.center.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.mapper.CustomerInformationMapper;
import com.data.center.service.CustomerInformationService;
import org.springframework.stereotype.Service;


@Service
public class CustomerInformationServiceImpl extends ServiceImpl<CustomerInformationMapper, CustomerInformation> implements CustomerInformationService {

}
