package com.whj.blog.service;


import com.whj.blog.po.Blog;
import com.whj.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    //前端专用获取转换方法
    Blog getAndConvert(Long id);


    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    //根据查询条件返回blog
    Page<Blog> listBlog(String query,Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    //根据recommend属性，blog访问最高的数据返回，参数size是几条
    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();
    //归档页面的数据查询方法，返回一个MAP

    Long countBlog();
    //返回博客条数


    Blog saveBlog(Blog blog);



    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);



   // List<BlogQuery> getBlogBySearch(SearchBlog searchBlog);



//修改recommend,因为recommend从前台接收只能接收字符串，但数据库中的Integer类型，所以转一下
//


//    void transformRecommend(SearchBlog searchBlog);
//
//    List<FirstPageBlog> getAllFirstPageBlog();
//
//    List<RecommendBlog> getRecommendedBlog();
//
//    List<FirstPageBlog> getSearchBlog(String query);
//
//    DetailedBlog getDetailedBlog(Long id);


//根据TypeId获取博客列表，在分类页进行的操作

//    List<FirstPageBlog> getByTypeId(Long typeId);
//
//    List<FirstPageBlog> getByTagId(Long tagId);

}
