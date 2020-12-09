package com.example.dc.service;

import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.common.vo.PageResult;
import com.example.dc.mapper.JotterArticleMapper;
import com.example.dc.pojo.JotterArticle;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class JotterArticleService {

    @Autowired
    private JotterArticleMapper jotterArticleMapper;



    public int insert(JotterArticle article) {
        JotterArticle jotterArticle = findById(article.getId());
        if (jotterArticle==null){
            article.setArticleDate(new Date());
            return jotterArticleMapper.insert(article);
        }
        return jotterArticleMapper.updateByPrimaryKeySelective(article);
    }

    public PageResult<JotterArticle> list(int page, int size) {
        PageHelper.startPage(page, size);
        Example example=new Example(JotterArticle.class);
        example.setOrderByClause("id DESC ");
        List<JotterArticle> list = jotterArticleMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.ARTICLE_NOT_FOUND);
        }
        PageInfo<JotterArticle> info=new PageInfo<>(list);

        return new PageResult<JotterArticle>(info.getTotal(),info.getPages(),list);
    }

    public JotterArticle findById(Integer id) {
        return jotterArticleMapper.selectByPrimaryKey(id);
    }

    public void delete(Integer id) {
        int i = jotterArticleMapper.deleteByPrimaryKey(id);
        if (i!=1){
            throw new LyException(ExceptionEnum.ARTICLE_DELETE_ERROR);
        }
    }
}
