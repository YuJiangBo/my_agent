package com.yu.my_agent.mapper.postgresql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.my_agent.entity.postgresql.DocumentVector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
//@Repository
public interface DocumentVectorMapper extends BaseMapper<DocumentVector> {
    
    // 向量相似性搜索
    @Select("SELECT * FROM my_agent.document_vectors ORDER BY content_vector <=> CAST(:vector AS BINARY) LIMIT :limit")
    List<DocumentVector> searchSimilarDocuments(@Param("vector") float[] vector,
                                               @Param("limit") int limit);
    
//    // 按元数据搜索
//    @Select("SELECT * FROM my_agent.document_vectors WHERE metadata::jsonb @> $1::jsonb")
//    List<DocumentVector> findByMetadata(@Param("metadata") String metadata);
    
    // 范围搜索
    @Select("SELECT * FROM my_agent.document_vectors WHERE created_at BETWEEN #{startTime} AND #{endTime} ORDER BY created_at DESC")
    List<DocumentVector> findByCreatedAtRange(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}