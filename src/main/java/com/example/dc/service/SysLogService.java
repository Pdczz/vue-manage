package com.example.dc.service;

import com.example.dc.mapper.SysLogMapper;
import com.example.dc.pojo.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;
    public void save(SysLog sysLog) throws Exception{
        sysLogMapper.save(sysLog);
    }

    List<SysLog> findAll() throws Exception{
        return sysLogMapper.findAll();
    }
}
