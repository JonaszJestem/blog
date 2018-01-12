import java.sql.Date;

public class Post {
    public int id;
    public String title;
    public String author;
    public Date date;
    public String text;
    int views;

    public Post(int id, String title, String author, Date date, String text) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.text = text;
    }

    public Post(int id, String title, int views) {
        this.id = id;
        this.title = title;
        this.views = views;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
