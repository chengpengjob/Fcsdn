package com.cp.fcsdn.service.impl;

import com.cp.fcsdn.entity.Link;
import com.cp.fcsdn.repository.LinkRepository;
import com.cp.fcsdn.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友情链接Service实现类
 * @author Administrator
 *
 */
@Service("linkService")
public class LinkServiceImpl implements LinkService {

	@Autowired
	private LinkRepository linkRepository;
	
	@Override
	public List<Link> listAll(Direction direction, String... properties) {
		Sort sort=new Sort(direction,properties);
		return linkRepository.findAll(sort);
	}

}
