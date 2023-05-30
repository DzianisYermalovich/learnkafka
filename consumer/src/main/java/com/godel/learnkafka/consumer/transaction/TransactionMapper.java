package com.godel.learnkafka.consumer.transaction;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "bank", source = "dto.bank")
    @Mapping(target = "orderType", source = "dto.orderType")
    @Mapping(target = "total", source = "total")
    @Mapping(target = "createdAt", source = "dto.createdAt")
    TransactionEntity toEntity(final Transaction dto, final Double total);

}
