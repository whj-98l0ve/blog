package com.whj.blog.web;


import com.whj.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){


        //这里其实我挺好奇的 这个controller里 blogService获取了一个Map<String, List<Blog>>
        //获取了countBlog数据
        //那我之前获取查询出来的分页数据的时候能不能整和在一起应该是page类
        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogCount",blogService.countBlog());

        return "archives";
    }
}

