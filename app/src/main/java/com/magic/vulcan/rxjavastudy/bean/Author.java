package com.magic.vulcan.rxjavastudy.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类 作者
 */
public class Author {
    /*所有的文章*/
    public List<String> Articles = new ArrayList<>();
    /*名字*/
    public String name;


    public Author(String name) {
        this.name = name;
    }

    public List<String> getArticles() {
        return Articles;
    }

    public void setArticles(List<String> articles) {
        Articles = articles;
    }
}
