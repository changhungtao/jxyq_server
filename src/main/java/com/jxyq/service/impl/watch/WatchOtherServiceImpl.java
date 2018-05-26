package com.jxyq.service.impl.watch;

import com.jxyq.mapper.watch_mapper.OtherMapper;
import com.jxyq.model.watch.CenterNumber;
import com.jxyq.service.inf.watch.WatchOtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchOtherServiceImpl implements WatchOtherService {
    @Autowired
    private OtherMapper otherMapper;

    @Override
    public CenterNumber selCenterNumber() {
        return otherMapper.selCenterNumber();
    }
}
