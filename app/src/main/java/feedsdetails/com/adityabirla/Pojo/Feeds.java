package feedsdetails.com.adityabirla.Pojo;

/**
 * Created by Amaresh on 11/13/17.
 */

public class Feeds {
    String id,user_name,user_id,title,file_type,file_name,no_of_like,no_of_comment,no_of_share,
            no_of_downloads,date,description;

    public Feeds() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getNo_of_like() {
        return no_of_like;
    }

    public void setNo_of_like(String no_of_like) {
        this.no_of_like = no_of_like;
    }

    public String getNo_of_comment() {
        return no_of_comment;
    }

    public void setNo_of_comment(String no_of_comment) {
        this.no_of_comment = no_of_comment;
    }

    public String getNo_of_share() {
        return no_of_share;
    }

    public void setNo_of_share(String no_of_share) {
        this.no_of_share = no_of_share;
    }

    public String getNo_of_downloads() {
        return no_of_downloads;
    }

    public void setNo_of_downloads(String no_of_downloads) {
        this.no_of_downloads = no_of_downloads;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Feeds(String id, String user_name, String user_id, String title, String file_type, String file_name,
                 String no_of_like, String no_of_comment, String no_of_share, String no_of_downloads, String date, String description) {
        this.id=id;
        this.user_id=user_id;
        this.user_name=user_name;
        this.title=title;
        this.file_type=file_type;
        this.file_name=file_name;
        this.no_of_like=no_of_like;
        this.no_of_comment=no_of_comment;
        this.no_of_share=no_of_share;
        this.no_of_downloads=no_of_downloads;
        this.date=date;
        this.description=description;



    }
}
