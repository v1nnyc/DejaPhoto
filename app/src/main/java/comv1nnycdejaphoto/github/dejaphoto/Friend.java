package comv1nnycdejaphoto.github.dejaphoto;

import java.util.ArrayList;

/**
 * Created by VINCENT on 5/31/17.
 */

public interface Friend {
    public void add(String Id);
    public void remove(String Id);
    public boolean handleRequest();
    public ArrayList<User> getFriendsList();
}
