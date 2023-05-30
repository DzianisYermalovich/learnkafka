package com.godel.learnkafka.consumer.transaction;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "bank", source = "bank")
    @Mapping(target = "orderType", source = "orderType")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "createdAt", source = "createdAt")
    TransactionEntity toEntity(final Transaction dto);

}
