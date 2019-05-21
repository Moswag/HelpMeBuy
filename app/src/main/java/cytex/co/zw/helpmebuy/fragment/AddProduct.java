package cytex.co.zw.helpmebuy.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cytex.co.zw.helpmebuy.R;
import cytex.co.zw.helpmebuy.model.Product;
import cytex.co.zw.helpmebuy.util.MessageToast;
import cytex.co.zw.helpmebuy.util.VariableConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProduct extends Fragment {

    Button addProduct;
    EditText name,price;
    Spinner category;
    TextView expiry_date;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    public AddProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_product, container, false);


        progressDialog=new ProgressDialog(getActivity());


        progressDialog.setTitle("Adding product");



        databaseReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_PRODUCTS);


        name=(EditText) view.findViewById(R.id.name);
        category=(Spinner) view.findViewById(R.id.category);
        price=(EditText) view.findViewById(R.id.price);
        expiry_date=(TextView) view.findViewById(R.id.expiry_date);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        List<String> cat=new ArrayList<>();
        cat.add("drinks");
        cat.add("sweets");
        cat.add("braids");
        cat.add("toys");
        cat.add("royco");
        cat.add("beer");
        cat.add("clothes");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, cat);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(dataAdapter);

        expiry_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                expiry_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        addProduct=(Button) view.findViewById(R.id.btnAddProduct);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                addProduct();
            }
        });


        return view;
    }


    private void addProduct(){

        String tname=name.getText().toString();
        String tcategory=String.valueOf(category.getSelectedItem());
        String tprice=price.getText().toString();
        String texpiry_date=expiry_date.getText().toString();

        if(!TextUtils.isEmpty(tname)){
            if(!TextUtils.isEmpty(tcategory)){
                if(!TextUtils.isEmpty(tprice)){
                    if(!TextUtils.isEmpty(texpiry_date)){

                        progressBar.setVisibility(View.VISIBLE);

                        String id = databaseReference.push().getKey();

                        Product product=new Product();
                        product.setName(tname);
                        if(product.getName().length()>2){
                            product.setCategory(tcategory);
                            if(product.getCategory().length()>2) {
                                product.setPrice(tprice);
                                product.setExpiryDate(texpiry_date);
                                product.setProductId(id);

                                databaseReference.child(id).setValue(product);
                                clearTextViews();

                                MessageToast.show(getActivity(),"Product successfully added");
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                MessageToast.show(getActivity(),"Please enter a valid category for the product");
                            }

                        }
                        else{
                            MessageToast.show(getActivity(),"Please enter a valid name for the product");
                        }

                    }
                    else{
                        expiry_date.setFocusable(true);
                        expiry_date.setError("Please pick the expiry date");
                    }
                }
                else {
                    price.setFocusable(true);
                    price.setError("Price is required");
                }
            }

        }
        else{
            name.setFocusable(true);
            name.setError("Product name required");
        }

        progressDialog.dismiss();


    }

    public void clearTextViews(){
        name.setText("");
        price.setText("");
        expiry_date.setText("");
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
