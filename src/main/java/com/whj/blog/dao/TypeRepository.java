package com.whj.blog.dao;

import com.whj.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {
    Type findByName(String name);


    //这里要好好理解Query注解和使用方法，以及这个select的什么sql语句

    //Query jpql语句查数据库
    @Query("select t from  Type t")
    List<Type>  findTop(Pageable pageable);

}
