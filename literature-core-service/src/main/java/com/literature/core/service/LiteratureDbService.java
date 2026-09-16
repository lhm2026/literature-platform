package com.literature.core.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.literature.common.enums.LiteratureStatus;
import com.literature.core.entity.Literature;
import com.literature.core.mapper.LiteratureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiteratureDbService extends ServiceImpl<LiteratureMapper, Literature> {

    /**
     * 安全更新：update 使用主键ID（索引字段）作为where条件，防止全表锁
     */
    public boolean updateStatus(String litId, LiteratureStatus status) {
        Literature lit = new Literature();
        lit.setId(litId);
        lit.setStatus(status.getCode());
        // 主键id带索引，不会触发全表更新锁
        return baseMapper.updateById(lit) > 0;
    }
}
