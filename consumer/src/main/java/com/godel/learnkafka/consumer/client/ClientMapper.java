package com.godel.learnkafka.consumer.client;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "clientId")
    @Mapping(target = "email", source = "email")
    ClientEntity toEntity(final Client dto);

}
