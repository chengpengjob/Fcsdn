package com.cp.fcsdn.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.cp.fcsdn.entity.ArcType;
import com.cp.fcsdn.service.ArcTypeService;
import com.cp.fcsdn.entity.Link;
import com.cp.fcsdn.service.LinkService;


/**
 * @author chengpeng
 * @company 趋势自家游
 * @create 2019-03-20 9:45
 */
@Component("initSystem")
public class InitSystem implements ServletContextListener,ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Map<Integer,ArcType> arcTyprMap = new HashMap<Integer,ArcType>();

    /**
     * 加载数据到application缓存中
     * @param application
     */
    public void loadDate(ServletContext application){
        ArcTypeService arcTypeService=(ArcTypeService) applicationContext.getBean("arcTypeService");
        LinkService linkService=(LinkService) applicationContext.getBean("linkService");
        List<ArcType> allArcTypeList = arcTypeService.listAll(Sort.Direction.ASC, "sort");
        List<Link> allLinkList = linkService.listAll(Sort.Direction.ASC, "sort");
        for(ArcType arcType:allArcTypeList){
            arcTyprMap.put(arcType.getId(), arcType);
        }
        application.setAttribute("allArcTypeList", allArcTypeList);
        application.setAttribute("allLinkList", allLinkList);

    }



    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.loadDate(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
