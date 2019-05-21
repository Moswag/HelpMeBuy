package cytex.co.zw.helpmebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cytex.co.zw.helpmebuy.R;
import cytex.co.zw.helpmebuy.model.Product;
import cytex.co.zw.helpmebuy.model.User;

public class ProductListAdapter extends ArrayAdapter<Product> {

    private Activity context;
    //list of users
    private List<Product> products;

    public ProductListAdapter(Activity context, List<Product> products) {
        super(context, R.layout.product_list, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.product_list, null, true);
        //initialize
        TextView textViewName = (TextView) listViewItem.findViewById
                (R.id.name);
        TextView textviewCategory = (TextView) listViewItem.findViewById
                (R.id.category);
        TextView textviewPrice = (TextView) listViewItem.findViewById
                (R.id.price);
        TextView textviewExp = (TextView) listViewItem.findViewById
                (R.id.expiry_date);
        //getting user at position
        Product product = products.get(position);
        String fullname=product.getName();
        //set user name
        textViewName.setText(fullname);
        //set user email
        textviewCategory.setText(product.getCategory());
        //set user mobilenumber
        textviewPrice.setText("$"+product.getPrice());
        textviewExp.setText(product.getExpiryDate());
        return listViewItem;
    }
}
