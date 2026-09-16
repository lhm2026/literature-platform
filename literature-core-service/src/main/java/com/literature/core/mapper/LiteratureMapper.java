package com.literature.core.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.literature.core.entity.Literature;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LiteratureMapper extends BaseMapper<Literature> {
}
