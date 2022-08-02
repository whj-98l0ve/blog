package com.whj.blog.service;

import com.whj.blog.NotFoundException;
import com.whj.blog.dao.BlogRepository;
import com.whj.blog.po.Blog;
import com.whj.blog.po.Type;
import com.whj.blog.util.MarkdownUtils;
import com.whj.blog.util.MyBeanUtils;
import com.whj.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.util.*;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

//分页查询方式
    @Override
    public Page<Blog> listBlog(Pageable pageable,  BlogQuery blog) {


        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //root 代表查询的对象就是blog，blog对象映射给root，从里面获取表的字段属性名字属性值，
                //cq查询条件容器
                //cb设置具体查询条件表达式，比如两个条件相等，模糊查询alike

                List<Predicate> predicates =new ArrayList<>();


                if(!"".equals(blog.getTitle()) && blog.getTitle()!=null){
                    //字符串不为空 title不为空

                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));

                    //添加查询条件
                }

                if(blog.getTypeId() != null){
                  //  predicates.add(cb.equal(root.<Type>get("Type").get("id"),"%"+blog.getTypeId()+"%"));
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                    //教程这里id没有拼接%，到时候回来改，如果有问题
                }



                if(blog.isRecommend()){
                   predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }else{
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }

                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                //list转数组这句

                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
       return   blogRepository.findAll(new Specification<Blog>() {
           @Override
           public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
               //这里是关联查询通过blog表去关联另一个
               Join join = root.join("tags");
               //查一下criteriaQuery Api使用
               return cb.equal(join.get("id"),tagId);
           }
       },pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Pageable pageable = PageRequest.of(0,size, Sort.by(Sort.Direction.DESC,"updateTime"));
        return blogRepository.findTop(pageable);
    }


    @Override
    public Map<String, List<Blog>> archiveBlog() {
        //先拿到年份的数据
        List<String> years =blogRepository.findGroupYear();
        Map<String,List<Blog>> map=new HashMap<>();
        //循环取出年份放到map里 year作为key 通过year查询blog保存在map里
        for(String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        //返回的是blog集合里面所有的数据条数
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {


        if(blog.getId()==null){
            blog.setId((long)0);
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }


        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if (b==null){
            throw new NotFoundException("该博客不存在");

        }
        BeanUtils.copyProperties(blog,b,MyBeanUtils.getNullpropertyName(blog));
        b.setUpdateTime(new Date());


        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }


    //这里和blogRepository😀对应持久层这个Transactional注解
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog=blogRepository.findById(id).get();
        if(blog==null){
            throw new NotFoundException("该博客不存在");

        }
        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);//spring framework blog给b属性
        String content=b.getContent();
        //这里返回的是已经处理好的html，string类型的类

        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //这里拿到blog，改变content值赋值给blog，改变了blog hibernate里面的属性值

        //Transactional事务会覆盖repository里面的事务
        blogRepository.updateViews(id);

        return b;


    }
}
