package com.example.annotatedTranslation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Yu Shaoqing
 * @date 2021/7/18/15:56
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocEntity {
    private int docId;
    private String title;
    private String abst;
    private String content;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
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
        return "DocEntity{" +
                "docId=" + docId +
                ", title='" + title + '\'' +
                ", abst='" + abst + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
