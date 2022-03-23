package com.gproto.service;

import com.gproto.entity.ToBasisEntity;

public interface ValProtoService {
    String jsonToProto(String className, ToBasisEntity toBasisEntity) throws Exception;
}
