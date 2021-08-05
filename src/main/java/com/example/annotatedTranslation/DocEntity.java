package com.example.annotatedTranslation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Yu Shaoqing
 * @date 2021/7/18/15:56
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocEntity {
    private int id;
    private String title;
    private String abst;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", abst='" + abst + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
