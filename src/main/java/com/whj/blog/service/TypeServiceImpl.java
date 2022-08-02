package com.whj.blog.service;


import com.whj.blog.NotFoundException;
import com.whj.blog.dao.TypeRepository;
import com.whj.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class TypeServiceImpl implements TypeService {



    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
       return  typeRepository.findById(id).get();//spring 1.0版本有findone id返回值 2.0变成getOneId getone(id)不知道怎么用
                                                  //用的是findById返回的是一个optional对象通过get（）方法获得原本的对象，optional对象是为了解除
                                                  //空指针问题

    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t =typeRepository.findById(id).get();
        if(t == null){
            throw new NotFoundException("不存在该类型");
                    }

        BeanUtils.copyProperties(type,t);//? BeanUtils.copyProperties(a,b) a给b springframework是这样的


        return typeRepository.save(t);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }


    @Override
    public List<Type> listTypeTop(Integer size) {
        Pageable pageable =PageRequest.of(0,size,Sort.by(Sort.Direction.DESC,"blogs.size"));
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);

    }


}
