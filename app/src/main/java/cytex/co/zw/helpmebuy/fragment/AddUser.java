package cytex.co.zw.helpmebuy.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cytex.co.zw.helpmebuy.R;
import cytex.co.zw.helpmebuy.model.User;
import cytex.co.zw.helpmebuy.util.VariableConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddUser extends Fragment {

    Button addUser;
    EditText name,surname,employeeId,email,password;
    DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ProgressBar progressBar;



    public AddUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_user, container, false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_USER);

        name=(EditText) view.findViewById(R.id.name);
        surname=(EditText) view.findViewById(R.id.surname);
        employeeId=(EditText) view.findViewById(R.id.employee_id);
        email=(EditText) view.findViewById(R.id.email);
        password=(EditText) view.findViewById(R.id.password);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        addUser=(Button) view.findViewById(R.id.btnAddUser);


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });



        return view;
    }

    private void addUser() {

        //getting the values to save
        String name1 = name.getText().toString().trim();
        String surname1 = surname.getText().toString().trim();
        String email1 = email.getText().toString().trim();
        String employee_id1 = employeeId.getText().toString().trim();
        String password1 = password.getText().toString().trim();


        //checking if the value is provided or not Here, you can Add More Validation as you required

        if (!TextUtils.isEmpty(name1)) {
            if (!TextUtils.isEmpty(surname1)) {
                if (!TextUtils.isEmpty(email1)) {
                    if (!TextUtils.isEmpty(employee_id1)) {
                        if (!TextUtils.isEmpty(password1)) {

                            //it will create a unique id and we will use it as the Primary Key for our User
                            String id = databaseReference.push().getKey();
                            //creating an User Object
                            User user = new User();
                            //Saving the User

                            user.setEmployeeId(employee_id1);
                            user.setName(name1);
                            user.setSurname(surname1);
                            user.setEmail(email1);
                            user.setUserId(id);
//                            user.setPassword(password1);

                        //    Toast.makeText(getActivity(), "User is "+user.toString(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.VISIBLE);
                            databaseReference.child(id).setValue(user);


                            //create user
                            auth.createUserWithEmailAndPassword(email1, password1)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Toast.makeText(getActivity(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Authentication failed." + task.getException(),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "User added", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            clearTextFields();


                        } else {
                            password.setFocusable(true);
                            Toast.makeText(getActivity(), "Please enter a password", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        employeeId.setFocusable(true);
                        Toast.makeText(getActivity(), "Please enter an Employee ID", Toast.LENGTH_LONG).show();
                    }
                } else {
                    email.setFocusable(true);
                    Toast.makeText(getActivity(), "Please enter an email", Toast.LENGTH_LONG).show();
                }
            }
            else{
                surname.setFocusable(true);
                Toast.makeText(getActivity(), "Please enter a surname", Toast.LENGTH_LONG).show();
            }
        }
        else{
            name.setFocusable(true);
            Toast.makeText(getActivity(), "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }


    public void clearTextFields(){
        name.setText("");
        surname.setText("");
        employeeId.setText("");
        email.setText("");
        password.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}
