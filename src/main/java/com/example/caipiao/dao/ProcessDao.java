package com.example.caipiao.dao;

import com.example.caipiao.model.DoubleChromosphereInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessDao {
    void insertBatch(List<DoubleChromosphereInfo> doubleChromosphereInfos);
}