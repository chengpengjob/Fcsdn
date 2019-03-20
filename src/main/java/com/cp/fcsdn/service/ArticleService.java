package com.cp.fcsdn.service;

import com.cp.fcsdn.entity.Article;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 资源Service接口
 * @author Administrator
 *
 */
public interface ArticleService {

	/**
	 * 根据条件分页查询资源信息
	 * @param s_article
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Article> list(Article s_article, Integer page, Integer pageSize, Direction direction, String... properties);
	
	/**
	 * 根据条件查询总记录数
	 * @param s_article
	 * @return
	 */
	public Long getTotal(Article s_article);
	
	/**
	 * 根据id获取实体
	 * @param id
	 * @return
	 */
	public Article get(Integer id);
}
