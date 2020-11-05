import java.util.Objects;

public class LuceneModel {
    private String id;
    private String uri_s;
    private String title_pl;
    private String article_pl;
    private String last_modified_date_ss;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri_s() {
        return uri_s;
    }

    public void setUri_s(String uri_s) {
        this.uri_s = uri_s;
    }

    public String getTitle_pl() {
        return title_pl;
    }

    public void setTitle_pl(String title_pl) {
        this.title_pl = title_pl;
    }

    public String getArticle_pl() {
        return article_pl;
    }

    public void setArticle_pl(String article_pl) {
        this.article_pl = article_pl;
    }

    public String getLast_modified_date_ss() {
        return last_modified_date_ss;
    }

    public void setLast_modified_date_ss(String last_modified_date_ss) {
        this.last_modified_date_ss = last_modified_date_ss;
    }

    @Override
    public String toString() {
        return "LuceneModel{" +
                "id='" + id + '\'' +
                ", uri_s='" + uri_s + '\'' +
                ", title_pl='" + title_pl + '\'' +
                ", article_pl='" + article_pl + '\'' +
                ", last_modified_date_ss='" + last_modified_date_ss + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LuceneModel))
            return false;
        LuceneModel that = (LuceneModel) o;
        return id.equals(that.id) &&
                uri_s.equals(that.uri_s) &&
                title_pl.equals(that.title_pl) &&
                article_pl.equals(that.article_pl) &&
                last_modified_date_ss.equals(that.last_modified_date_ss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uri_s, title_pl, article_pl, last_modified_date_ss);
    }
}
