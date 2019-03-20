package com.cp.fcsdn.service;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Sort.Direction;

import com.cp.fcsdn.entity.ArcType;

import java.util.List;

/**
 * @author chengpeng
 * @company 趋势自家游
 * @create 2019-03-20 9:30
 */
public interface ArcTypeService {

    /**
     * 查询所有资源类别
     * @param direction
     * @param properties
     * @return
     */

    public List<ArcType> listAll(Sort.Direction direction, String...properties);
}
