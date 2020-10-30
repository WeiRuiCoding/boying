package com.tongji.boying.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.tongji.boying.dto.ResourceParam;
import com.tongji.boying.mapper.ResourceMapper;
import com.tongji.boying.model.Resource;
import com.tongji.boying.model.ResourceExample;
import com.tongji.boying.service.UmsAdminCacheService;
import com.tongji.boying.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源管理Service实现类
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService
{
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private UmsAdminCacheService adminCacheService;
    @Override
    public int create(ResourceParam param) {
        Resource resource = new Resource();
        resource.setCreateTime(new Date());
        resource.setResourceCategoryId(param.getResourceCategoryId());
        resource.setName(param.getName());
        resource.setUrl(param.getUrl());
        resource.setDescription(param.getDescription());
        return resourceMapper.insert(resource);
    }

    @Override
    public int update(Integer id, ResourceParam param) {
        Resource resource = new Resource();
        resource.setResourceCategoryId(param.getResourceCategoryId());
        resource.setName(param.getName());
        resource.setUrl(param.getUrl());
        resource.setDescription(param.getDescription());
        resource.setResourceId(id);
        int count = resourceMapper.updateByPrimaryKeySelective(resource);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public Resource getItem(Integer id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Integer id) {
        int count = resourceMapper.deleteByPrimaryKey(id);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public List<Resource> list(Integer categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        ResourceExample example = new ResourceExample();
        ResourceExample.Criteria criteria = example.createCriteria();
        if(categoryId!=null){
            criteria.andResourceCategoryIdEqualTo(categoryId);
        }
        if(StrUtil.isNotEmpty(nameKeyword)){
            criteria.andNameLike('%'+nameKeyword+'%');
        }
        if(StrUtil.isNotEmpty(urlKeyword)){
            criteria.andUrlLike('%'+urlKeyword+'%');
        }
        return resourceMapper.selectByExample(example);
    }

    @Override
    public List<Resource> listAll() {
        return resourceMapper.selectByExample(new ResourceExample());
    }
}