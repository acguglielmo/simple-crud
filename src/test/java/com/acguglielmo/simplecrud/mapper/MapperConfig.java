package com.acguglielmo.simplecrud.mapper;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CustomerMapper.class)
public interface MapperConfig {

}
