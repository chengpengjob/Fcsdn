package com.cp.fcsdn.repository;

import com.cp.fcsdn.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 友情链接Respository接口
 * @author Administrator
 *
 */
public interface LinkRepository extends JpaRepository<Link, Integer>,JpaSpecificationExecutor<Link> {

}
