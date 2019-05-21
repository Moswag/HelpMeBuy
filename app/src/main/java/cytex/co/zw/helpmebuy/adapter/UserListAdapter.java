package cytex.co.zw.helpmebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cytex.co.zw.helpmebuy.R;
import cytex.co.zw.helpmebuy.model.User;

public class UserListAdapter extends ArrayAdapter<User> {
    private Activity context;
    //list of users
    List<User> users;

    public UserListAdapter(Activity context, List<User> users) {
        super(context, R.layout.user_list, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.user_list, null, true);
        //initialize
        TextView textViewName = (TextView) listViewItem.findViewById
                (R.id.name);
        TextView textviewempid = (TextView) listViewItem.findViewById
                (R.id.employee_id);
        TextView textviewemail = (TextView) listViewItem.findViewById
                (R.id.email);
        //getting user at position
        User user = users.get(position);
        String fullname=user.getName()+" "+user.getSurname();
        //set user name
        textViewName.setText(fullname);
        //set user email
        textviewempid.setText(user.getEmployeeId());
        //set user mobilenumber
        textviewemail.setText(user.getEmail());
        return listViewItem;
    }
}
