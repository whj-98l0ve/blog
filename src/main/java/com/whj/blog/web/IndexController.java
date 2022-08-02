package com.whj.blog.web;


import com.whj.blog.NotFoundException;
import com.whj.blog.service.BlogService;
import com.whj.blog.service.TagService;
import com.whj.blog.service.TypeService;
import com.whj.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String  index(@PageableDefault(size = 8,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        //这里是右边types标签，从博客多少从上到下排序
       // System.out.println("------------index------------");
        model.addAttribute("tags",tagService.listTagTop(10));
        //右边tags标签，从博客多少从上到下排序
        //这里没有满10个会怎么样？出BUG吗？
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));

        return "index";


    }


    //这里做的搜索只做搜索标题和内容  title=或者content=其他不管标签这种
//    表单用post方式提交 前台数据给的是submit
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                         Model model,
                         @RequestParam String query//这是从前台来的数值
    ){


        //这里自己处理query给后端数据库
        model.addAttribute("page",blogService.listBlog("%"+query+"%", pageable));
        //返回给前端让你不然刷新掉了，让你知道你查了什么ddx
        model.addAttribute("query",query);


        return "search";

    }



    @GetMapping("/blog/{id}")
    public String  blog(@PathVariable Long id,Model model){

        model.addAttribute("blog",blogService.getAndConvert(id));

        return "blog";
    }
}
