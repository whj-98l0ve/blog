package com.whj.blog.web;


import com.whj.blog.po.Blog;
import com.whj.blog.po.Tag;
import com.whj.blog.service.BlogService;
import com.whj.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


//springboot 里面不允许有两个一样名字的类 admin里面有一个TagController了所以要避免名字重复
@Controller
public class TagShowController {


    @Autowired
    private TagService TagService;
    @Autowired
    private BlogService blogService;


    @GetMapping("/tags/{id}")
    public String Tags(@PathVariable Long id,
                        @PageableDefault(size = 8,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        //这里数目指定无限大就不会超越你查询的数目
        List<Tag> tags=TagService.listTagTop(1000);
        if (id==-1){
            //这里的Tags是已经经过top排序的get0就是第一条
            id=tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        //这里之后可以细细了解
        model.addAttribute("page", blogService.listBlog(id,pageable));
        //这里是活跃ID传给前段判断哪个标签选中变色
        model.addAttribute("activeTagId",id);



        return "tags";
    }
}
