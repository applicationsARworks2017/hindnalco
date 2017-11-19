package feedsdetails.com.adityabirla.Pojo;

/**
 * Created by Amaresh on 11/19/17.
 */

public class Comments {
    String user_name,user_id,file_id,comment,date;

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

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Comments(String user_name, String user_id, String file_id, String comment, String date) {
        this.user_name=user_name;
        this.user_id=user_id;
        this.file_id=file_id;
        this.comment=comment;
        this.date=date;

    }
}
