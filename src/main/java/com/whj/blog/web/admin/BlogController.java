package com.whj.blog.web.admin;


import com.whj.blog.po.Blog;
import com.whj.blog.po.User;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;



@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    private static final String INPUT ="admin/blogs-input";
    private static final String EDIT ="admin/blogs-edit";
    private static final String LIST ="admin/blogs";
    private static final String REDIRECT_INPUT ="redirect:/admin/blogs";



    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 8,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog,
                        Model model) {

        model.addAttribute("types",typeService.listType());
        blog.setRecommend(true);
        model.addAttribute("page",blogService.listBlog(pageable,blog));




        return LIST;
    }


    //ajax thymeleaf模板做到局部动态刷新
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog,
                         Model model) {
        model.addAttribute("page",blogService.listBlog(pageable,blog));



        return "admin/blogs :: blogList"; //blogList局部片段，有点像html页面的body这种
    }

    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    //提取的博客设置属性的公共方法
    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }


    //博客编辑方法
    @GetMapping("/blogs/{id}/edit")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return EDIT;
    }


    //博客删除方法
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addAttribute("message","删除成功");
        return REDIRECT_INPUT;



    }



    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){


        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId())); //这里前后端交互要好号理解 博客新增2 07:43
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b ;


        //这里理解一下
        if (blog.getId() == null){
            b =blogService.saveBlog(blog);
        }
        else {
            b=blogService.updateBlog(blog.getId(),blog);
        }



        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }

        return REDIRECT_INPUT;

    }

    @GetMapping("/blogs/testweb")    //这个类测试用
    public String test() {

        return "admin/testweb";
    }


//    @GetMapping("/blogs/test")    //这个类测试用
//    public String editInput() {
//
//
//        long id =21;
//        System.out.println(blogService.getBlog(id));
//        System.out.println(blogService.getBlog(id).getTags());
//
//        return "admin/testweb";
//    }
//
}
