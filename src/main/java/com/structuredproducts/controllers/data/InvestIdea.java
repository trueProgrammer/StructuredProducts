package com.structuredproducts.controllers.data;

import java.util.Date;

/**
 * Created by Vlad on 10.01.2016.
 */
public class InvestIdea {

    private long id;
    private String company;
    private Date date;
    private boolean showOnMainPage;
    private String header;
    private String preview;
    private String content;

    public InvestIdea(){}

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isShowOnMainPage() {
        return showOnMainPage;
    }

    public void setShowOnMainPage(boolean showOnMainPage) {
        this.showOnMainPage = showOnMainPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvestIdea that = (InvestIdea) o;

        if (id != that.id) return false;
        if (showOnMainPage != that.showOnMainPage) return false;
        if (company != null ? !company.equals(that.company) : that.company != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (header != null ? !header.equals(that.header) : that.header != null) return false;
        if (preview != null ? !preview.equals(that.preview) : that.preview != null) return false;
        return !(content != null ? !content.equals(that.content) : that.content != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (showOnMainPage ? 1 : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (preview != null ? preview.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InvestIdea{" +
                "id=" + id +
                ", company='" + company + '\'' +
                ", date=" + date +
                ", showOnMainPage=" + showOnMainPage +
                ", header='" + header + '\'' +
                ", preview='" + preview + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
