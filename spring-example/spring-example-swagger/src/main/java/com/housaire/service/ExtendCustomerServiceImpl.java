package com.housaire.service;

import com.housaire.model.CustomerModel;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/1/29 16:12
 * @see
 * @since 1.0.0
 */
@Service
public class ExtendCustomerServiceImpl extends CustomerServiceImpl
{
    @Override
    public void addCustomer(CustomerModel CustomerModel)
    {
        System.out.println("Extend Add Customer");
    }
}
