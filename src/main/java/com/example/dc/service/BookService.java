package com.example.dc.service;

import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.mapper.BookMapper;
import com.example.dc.mapper.CategoryMapper;
import com.example.dc.pojo.Book;
import com.example.dc.pojo.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Book> queryBooks() {
        List<Book> books = bookMapper.selectAll();
        for (Book book : books) {
            //根据cid查询,把分类名称记住
            Category category = categoryMapper.queryCategoryById(book.getCid());
            book.setCname(category.getName());
        }
        if (CollectionUtils.isEmpty(books)){
            throw new LyException(ExceptionEnum.BOOKS_NOT_FOUND);
        }
        return books;
    }

    public List<Book> queryByCategory(int cid) {
        if (cid==0) {
            List<Book> books = bookMapper.selectAll();
            for (Book book : books) {
                Category category = categoryMapper.queryCategoryById(book.getCid());
                book.setCname(category.getName());
            }
            return books;
        }else {
            List<Book> books = bookMapper.queryByCategory(cid);
            for (Book book : books) {
                Category category = categoryMapper.queryCategoryById(book.getCid());
                book.setCname(category.getName());
            }
            return books;
        }
    }

    public void addOrEdit(Book book) {
        if (book.getId()!=null){

            int i = bookMapper.updateByPrimaryKey(book);
            if (i!=1){
                throw new LyException(ExceptionEnum.BOOKS_UPDATE_ERROR);
            }
        }else {
            book.setId(null);
            int i = bookMapper.insert(book);
            if (i!=1){
                throw new LyException(ExceptionEnum.BOOKS_SAVE_ERROR);
            }
        }
    }

    public void deleteById(int id) {
        bookMapper.deleteByPrimaryKey(id);
    }

    public List<Book> Search(String keywords) {
        //过滤
        Example example=new Example(Book.class);
        if (StringUtils.isNotBlank(keywords)){
            example.createCriteria()
                    .orLike("author","%"+keywords+"%")
                    .orLike("title","%"+keywords+"%");
        }
        String orderByClause="id DESC";
        example.setOrderByClause(orderByClause);
        List<Book> books = bookMapper.selectByExample(example);
        return books;
    }
}
