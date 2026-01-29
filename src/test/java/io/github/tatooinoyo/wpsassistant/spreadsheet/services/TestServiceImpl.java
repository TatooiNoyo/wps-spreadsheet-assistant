package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Tatooi Noyo
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public boolean saveBatch(Collection<TestPO> entityList) {
        return true;
    }

    @Override
    public List<TestPO> listByIds(Collection<? extends Serializable> idList) {
        return List.of();
    }
}
