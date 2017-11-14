package feedsdetails.com.adityabirla.Pojo;

/**
 * Created by Amaresh on 11/13/17.
 */

public class User {
    String id,username,email_address,contact_no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public User(String id, String username, String email_address, String contact_no) {
        this.id=id;
        this.username=username;
        this.email_address=email_address;
        this.contact_no=contact_no;

    }
}
