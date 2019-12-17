package com.zhm.service;


import com.google.common.collect.Lists;
import com.zhm.dao.CnAreaDao;
import com.zhm.dto.AreaDto;
import com.zhm.entity.CnArea;
import com.zhm.essential.jpa.search.DynamicSpecifications;
import com.zhm.essential.jpa.search.Operator;
import com.zhm.essential.jpa.search.SearchFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by 赵红明 on 2019/6/3.
 */
@Service
public class CnAreaService {

    private static Logger logger= LoggerFactory.getLogger(CnAreaService.class);

    @Autowired
    private CnAreaDao cnAreaDao;

    /**
     * 分页查询基础
     *
     * @param specification
     * @return
     */
    public Page<CnArea> findAll(Specification<CnArea> specification, Pageable pageable) {
        return cnAreaDao.findAll(specification, pageable);
    }

    /**
     * 排序查询所有信息
     * @param specification
     * @param sort
     * @return
     */
    public List<CnArea> findAll(Specification<CnArea> specification, Sort sort) {
        return cnAreaDao.findAll(specification,sort);
    }
    /**
     * 全量查询不排序
     * @param specification
     * @return
     */
    public List<CnArea> findAll(Specification<CnArea> specification) {
        return cnAreaDao.findAll(specification);
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public CnArea findOne(Integer id) {
        return cnAreaDao.findById(id).get();
    }

    /**
     * 新增/修改
     * @param sysMenu
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CnArea add(CnArea sysMenu)throws Exception {
        sysMenu= cnAreaDao.save(sysMenu);
        return sysMenu;
    }


    public List<AreaDto> getAreaList(Sort sort,String areaCode)throws Exception{
        Collection<SearchFilter> filters = Lists.newArrayList();
        String type="area";
        if(areaCode!=null){
            areaCode=areaCode.substring(0,6);
            SearchFilter.addFilter(filters, SearchFilter.build("areaCode", Operator.RLIKE,areaCode));
            type="sunarea";
        }else{
            SearchFilter.addFilter(filters, SearchFilter.build("areaLevel", Operator.LTE,2));
        }
        Specification<CnArea> specification = DynamicSpecifications.bySearchFilter(filters);

        List<CnArea> list = cnAreaDao.findAll(specification,sort);

        return getList(list,type);
    }


    public List<AreaDto> getList(List<CnArea> list,String type){
        //封装Map
        Map<String,AreaDto> map=new HashMap<String,AreaDto>();
        List<AreaDto> parentlist=new ArrayList<AreaDto>();

        if(list!=null&&list.size()>0){
            for(CnArea pm:list){
                if(type.equals("area")){
                    if(pm.getAreaLevel()==0){
                        //根路径
                        List<AreaDto> plist=new ArrayList<AreaDto>();
                        AreaDto pmmodel=new AreaDto();
                        pmmodel.setId(pm.getId());
                        pmmodel.setName(pm.getName());
                        pmmodel.setLevel(pm.getAreaLevel());
                        pmmodel.setAreaCode(pm.getAreaCode());
                        pmmodel.setParentName("中国");
                        pmmodel.setChildren(plist);
                        map.put(pm.getAreaCode(),pmmodel);
                        parentlist.add(pmmodel);
                    }else{
                        //查询其父亲节点
                        AreaDto pmodel=map.get(pm.getParentCode());
                        if(pmodel!=null){
                            //存到父亲里面
                            List<AreaDto> pmmlist=pmodel.getChildren();
                            if(pmmlist==null){
                                pmmlist=new ArrayList<>();
                            }
                            AreaDto productModel=new AreaDto();
                            productModel.setId(pm.getId());
                            productModel.setName(pm.getName());
                            productModel.setLevel(pm.getAreaLevel());
                            productModel.setAreaCode(pm.getAreaCode());
                            productModel.setId(pm.getId());
                            productModel.setParentName(pmodel.getMergerName());
                            pmmlist.add(productModel);
                            pmodel.setChildren(pmmlist);
                            //存到map中
                            map.put(pm.getAreaCode(),productModel);
                        }
                    }
                }else{
                    if(pm.getAreaLevel()==2){
                        //根路径
                        List<AreaDto> plist=new ArrayList<AreaDto>();
                        AreaDto pmmodel=new AreaDto();
                        pmmodel.setId(pm.getId());
                        pmmodel.setName(pm.getName());
                        pmmodel.setLevel(pm.getAreaLevel());
                        pmmodel.setAreaCode(pm.getAreaCode());
                        pmmodel.setParentName("中国");
                        pmmodel.setChildren(plist);
                        map.put(pm.getAreaCode(),pmmodel);
                        parentlist.add(pmmodel);
                    }else{
                        //查询其父亲节点
                        AreaDto pmodel=map.get(pm.getParentCode());
                        if(pmodel!=null){
                            //存到父亲里面
                            List<AreaDto> pmmlist=pmodel.getChildren();
                            if(pmmlist==null){
                                pmmlist=new ArrayList<>();
                            }
                            AreaDto productModel=new AreaDto();
                            productModel.setId(pm.getId());
                            productModel.setName(pm.getName());
                            productModel.setLevel(pm.getAreaLevel());
                            productModel.setAreaCode(pm.getAreaCode());
                            productModel.setId(pm.getId());
                            productModel.setParentName(pmodel.getMergerName());
                            pmmlist.add(productModel);
                            pmodel.setChildren(pmmlist);
                            //存到map中
                            map.put(pm.getAreaCode(),productModel);
                        }
                    }
                }
            }
        }
        return parentlist;
    }

}
