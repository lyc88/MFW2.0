package com.zhm.controller;

import com.zhm.dto.AreaDto;
import com.zhm.dto.MenuDto;
import com.zhm.entity.CnArea;
import com.zhm.entity.SysMenu;
import com.zhm.essential.domain.PageableFactory;
import com.zhm.essential.jpa.search.DynamicSpecifications;
import com.zhm.essential.jpa.search.Operator;
import com.zhm.essential.jpa.search.SearchFilter;
import com.zhm.service.CnAreaService;
import com.zhm.util.Servlets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 赵红明 on 2019/11/21.
 */
@Api(value = "中国行政区域")
@RestController
@RequestMapping("/api/cnArea")
public class CnAreaController {

    @Autowired
    private CnAreaService cnAreaService;

    /**
     *
     *  分页查询省級信息
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询省級信息")
    @RequestMapping(name = "分页查询省級信息", value = {""}, method = RequestMethod.GET)
    public Page<CnArea> pageForList(WebRequest request, Pageable pageable) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<CnArea> specification = DynamicSpecifications.bySearchFilter(filters);
        pageable = PageableFactory.create(pageable, "createTime", Sort.Direction.DESC);
        Page<CnArea> page = cnAreaService.findAll(specification, pageable);

        return page;
    }
    @ApiOperation(value = "行政区划（省市县）")
    @RequestMapping(name = "行政区划", value = {"/query"}, method = RequestMethod.GET)
    public List<AreaDto> query(@SortDefault(value = "areaLevel", direction = Sort.Direction.ASC) Sort sort)throws Exception {
        return cnAreaService.getAreaList(sort,null);
    }
    @ApiOperation(value = "行政区划（下面两级）")
    @RequestMapping(name = "行政区划", value = {"/querySecondArea/{areaCode}"}, method = RequestMethod.GET)
    public List<AreaDto> querySecondArea(@PathVariable String areaCode,@SortDefault(value = "areaLevel", direction = Sort.Direction.ASC) Sort sort)throws Exception {
        return cnAreaService.getAreaList(sort,areaCode);
    }


}
