package com.cp.fcsdn.service.impl;

import com.cp.fcsdn.repository.ArcTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cp.fcsdn.entity.ArcType;
import com.cp.fcsdn.service.ArcTypeService;

import java.util.List;

/**
 * @author chengpeng
 * @company 趋势自家游
 * @create 2019-03-20 9:37
 */
@Service("arcTypeService")
public class ArcTypeServiceImpl implements ArcTypeService{

    @Autowired
    private ArcTypeRepository arcTypeRespository;

    @Override
    public List<ArcType> listAll(Sort.Direction direction, String... properties) {
        Sort sort=new Sort(direction, properties);
        return arcTypeRespository.findAll(sort);
    }




}
