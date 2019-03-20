package com.cp.fcsdn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cp.fcsdn.entity.ArcType;
/**
 * @author chengpeng
 * @company 趋势自家游
 * @create 2019-03-20 9:27
 */
public interface ArcTypeRepository extends JpaRepository<ArcType, Integer>,JpaSpecificationExecutor<ArcType>{

}
