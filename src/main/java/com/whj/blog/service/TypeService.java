package com.whj.blog.service;


import com.whj.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

   // List<Type> getAllType();



    Type getTypeByName(String name);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);


  //  Type updateType(Type type);

    Type updateType(Long id,Type type);



    void deleteType(Long id);


}