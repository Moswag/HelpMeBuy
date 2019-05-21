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
import cytex.co.zw.helpmebuy.adapter.ProductListAdapter;
import cytex.co.zw.helpmebuy.adapter.UserListAdapter;
import cytex.co.zw.helpmebuy.model.Product;
import cytex.co.zw.helpmebuy.model.User;
import cytex.co.zw.helpmebuy.util.VariableConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProducts extends Fragment {

    List<Product> products;
    DatabaseReference databaseReference;
    ListView listViewProducts;

    public ViewProducts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_products, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_PRODUCTS);
        listViewProducts = (ListView) view.findViewById(R.id.listViewProducts);
        products = new ArrayList<>();


        // list item click listener
        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product user = products.get(i);
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
                products.clear();
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting User from firebase console
                    Product product = postSnapshot.getValue(Product.class);
                    //adding User to the list
                    products.add(product);
                }
                //creating Userlist adapter
                ProductListAdapter UserAdapter = new ProductListAdapter(getActivity(), products);
                //attaching adapter to the listview
                listViewProducts.setAdapter(UserAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void CallUpdateAndDeleteDialog(final Product product) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateName = (EditText) dialogView.findViewById(R.id.name);
        final EditText updateCategory = (EditText) dialogView.findViewById(R.id.category);
        final EditText updatePrice = (EditText) dialogView.findViewById(R.id.price);
        updateName.setText(product.getName());
        updateCategory.setText(product.getCategory());
        updatePrice.setText(product.getPrice());
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);
        //username for set dialog title
        dialogBuilder.setTitle(product.getName()+" "+product.getCategory());
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateName.getText().toString().trim();
                String category = updateCategory.getText().toString().trim();
                String price = updatePrice.getText().toString().trim();
                product.setName(name);
                product.setCategory(category);
                product.setPrice(price);
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(category)) {
                        if (!TextUtils.isEmpty(price)) {
                            //Method for update data
                            updateProduct(product);
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
                deleteProduct(product);
                b.dismiss();
            }
        });
    }

    private boolean updateProduct(Product product) {
        //getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_PRODUCTS).child(product.getProductId());

        //update  User  to firebase
        UpdateReference.setValue(product);
        Toast.makeText(getActivity(), "Product Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteProduct(Product product) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_PRODUCTS).child(product.getProductId());
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getActivity(), product.getName()+" "+product.getCategory()+" successfully deleted", Toast.LENGTH_LONG).show();
        return true;
    }

}
