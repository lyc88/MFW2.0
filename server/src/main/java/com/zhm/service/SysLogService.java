package com.zhm.service;

import com.zhm.dao.SysLogDao;
import com.zhm.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 系统日志
 *
 * @author zhao.hongming
 * @email 1183483051@qq.com
 * @date 2019-09-11 09:52:06
 */
@Service
public class SysLogService {

    private static Logger logger= LoggerFactory.getLogger(SysLogService.class);

    @Autowired
    private SysLogDao sysLogDao;

    public Page<SysLog> findAll(Specification<SysLog> specification, Pageable pageable) {
        return sysLogDao.findAll(specification, pageable);
    }
    public List<SysLog> findAll(Specification<SysLog> specification) {
        return sysLogDao.findAll(specification);
    }

    public SysLog findOne(Integer id) {
        return sysLogDao.findById(id).get();
    }


    /**
    * 新增/保存
    * @param sysLog
    * @return
    */
    @Transactional(rollbackFor = Exception.class)
    public void save(SysLog sysLog)throws Exception{
        sysLog.setCreateTime(new Date());
        sysLogDao.save(sysLog);
    }
}

