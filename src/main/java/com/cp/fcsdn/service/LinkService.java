package com.cp.fcsdn.service;

import com.cp.fcsdn.entity.Link;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 友情链接Service接口
 * @author Administrator
 *
 */
public interface LinkService {

	/**
	 * 查询所有友情链接
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Link> listAll(Direction direction, String... properties);
	
}
