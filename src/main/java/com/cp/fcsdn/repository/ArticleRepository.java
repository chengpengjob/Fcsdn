package com.cp.fcsdn.repository;

import com.cp.fcsdn.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 资源Respository接口
 * @author Administrator
 *
 */
public interface ArticleRepository extends JpaRepository<Article, Integer>,JpaSpecificationExecutor<Article> {

}
