package com.whj.blog.web;


import com.whj.blog.po.Blog;
import com.whj.blog.po.Type;
import com.whj.blog.service.BlogService;
import com.whj.blog.service.TypeService;
import com.whj.blog.vo.BlogQuery;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;


//springboot 里面不允许有两个一样名字的类 admin里面有一个typeController了所以要避免名字重复
@Controller
public class TypeShowController {


    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;


    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id,
                        @PageableDefault(size = 8,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        //这里数目指定无限大就不会超越你查询的数目
        List<Type> types=typeService.listTypeTop(1000);
        if (id==-1){
            //这里的types是已经经过top排序的get0就是第一条
            id=types.get(0).getId();
        }
        BlogQuery blogQuery =new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        //这里之后可以细细了解

        //默认是查询推荐的因为查询的那个方法，recommend 是布尔形，默认为空是false不好施加判断，也不想改了
        //这里解决还是要从Page<Blog>集合入手，看Page属性怎么整和
        blogQuery.setRecommend(true);
        model.addAttribute("page", blogService.listBlog(pageable,blogQuery));
        //这里是活跃ID传给前段判断哪个标签选中变色
        model.addAttribute("activeTypeId",id);



        return "types";
    }
}
