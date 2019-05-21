package cytex.co.zw.helpmebuy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cytex.co.zw.helpmebuy.R;
import cytex.co.zw.helpmebuy.adapter.UserListAdapter;
import cytex.co.zw.helpmebuy.model.User;
import cytex.co.zw.helpmebuy.util.VariableConstants;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUsers extends Fragment {

    List<User> users;
    DatabaseReference databaseReference;
    ListView listViewUsers;

    public ViewUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_users, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_USER);
        listViewUsers = (ListView) view.findViewById(R.id.listViewUsers);
        users = new ArrayList<>();


        // list item click listener
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                CallUpdateAndDeleteDialog(user);


            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                users.clear();
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting User from firebase console
                    User User = postSnapshot.getValue(User.class);
                    //adding User to the list
                    users.add(User);
                }
                //creating Userlist adapter
                UserListAdapter UserAdapter = new UserListAdapter(getActivity(), users);
                //attaching adapter to the listview
                listViewUsers.setAdapter(UserAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void CallUpdateAndDeleteDialog(final User user) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateName = (EditText) dialogView.findViewById(R.id.name);
        final EditText updateSurname = (EditText) dialogView.findViewById(R.id.surname);
        final EditText updateEmployeeID = (EditText) dialogView.findViewById(R.id.employee_id);
        updateName.setText(user.getName());
        updateSurname.setText(user.getSurname());
        updateEmployeeID.setText(user.getEmployeeId());
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);
        //username for set dialog title
        dialogBuilder.setTitle(user.getName()+" "+user.getSurname());
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateName.getText().toString().trim();
                String surname = updateSurname.getText().toString().trim();
                String employeeId = updateEmployeeID.getText().toString().trim();
                user.setName(name);
                user.setSurname(surname);
                user.setEmployeeId(employeeId);
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(surname)) {
                        if (!TextUtils.isEmpty(employeeId)) {
                            //Method for update data
                            updateUser(user);
                            b.dismiss();
                        }
                    }
                }

            }
        });

        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(user);
                b.dismiss();
            }
        });
    }

    private boolean updateUser(User user) {
        //getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_USER).child(user.getUserId());

        //update  User  to firebase
        UpdateReference.setValue(user);
        Toast.makeText(getActivity(), "User Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(User user) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_USER).child(user.getUserId());
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getActivity(), user.getName()+" "+user.getSurname()+" successfully deleted", Toast.LENGTH_LONG).show();
        return true;
    }

}
