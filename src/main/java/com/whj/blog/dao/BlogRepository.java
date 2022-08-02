package com.whj.blog.dao;

import com.whj.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {



    //有点感觉了，从Blog数据库中查询数据 查询出来的数据交给service层，service层处理成分页，多少页多少个，什么排序，然后交给前端显示，有点意思
    @Query("select b from Blog b where b.recommend = true ")
    List<Blog> findTop(Pageable pageable);


    //1代表参数1 query 不得不说数据库语言看着还挺简单的 如果想判断是否等于pageable参数 用参数2 ==？2有趣
    //正常sql语句 select * from t_blog where title like '%内容%'
    //所以传递query的时候要加上'%%'自己加铁铁
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    //这是一个查询要修改+Modifying，并且要加事务，如果不加事务也不行？好好理解铁子
    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    //查询year和数据库中的console_1语句一样，但是用的jpa自己的数据库语言，可能这就是比mybatis方便的地方吧
    //后面order by  year desc 通过year倒叙排序被我删了，暂时没有实现的想法以后来补
    //因为query注解不能实现查询+分页+排序会报错
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y')")
    List<String> findGroupYear();

    //根据年份从数据库中查询
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') =?1 ")
    List<Blog> findByYear(String year);






}
