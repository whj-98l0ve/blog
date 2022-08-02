package com.whj.blog.web.admin;


import com.whj.blog.po.Tag;
import com.whj.blog.service.TagService;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String list(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {

        model.addAttribute("page", tagService.listTag(pageable));
        //前端分装好的页面自动封装到pageable里来
        return "admin/tags";
    }


    @GetMapping("/tags/input")
    public String input(Model model) {

        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @PostMapping("/tags/add")
    public String post(@Valid Tag tag, BindingResult result,
                       RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加分类");
        }

        if (result.hasErrors()) {
            return "admin/tags-input";


        }

        Tag t = tagService.saveTag(tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/add/{id}")
    public String editPost(@Valid Tag tag,BindingResult result,
                           @PathVariable Long id,
                           RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加分类");
        }

        if (result.hasErrors()) {
            return "admin/tags-input";


        }

        Tag t = tagService.updateTag(id, tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }


    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {

        System.out.println(tagService.getTag(id).getName());
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }


    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {


        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }



//    @GetMapping("/tags/test")    //这个类测试用
//    public String editInput() {
//
//
//        System.out.println(tagService.getTagByName("感悟").getName());
//        System.out.println(tagService.getTag((long) 7).getName());
//
//        return "admin/testweb";
//    }

}
