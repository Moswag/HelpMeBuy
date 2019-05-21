package cytex.co.zw.helpmebuy;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cytex.co.zw.helpmebuy.adapter.ProductListAdapter;
import cytex.co.zw.helpmebuy.model.Product;
import cytex.co.zw.helpmebuy.util.MessageToast;
import cytex.co.zw.helpmebuy.util.VariableConstants;

public class MainActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    private TextView txvResult;
    Button admin;
    List<Product> products;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvResult = (TextView) findViewById(R.id.txvResult);
        databaseReference = FirebaseDatabase.getInstance().getReference(VariableConstants.TABLE_PRODUCTS);
        products = new ArrayList<>();

        // Init TextToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "This language is not supported!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // btnSpeak.setEnabled(true);
                        textToSpeech.setPitch(0.6f);
                        textToSpeech.setSpeechRate(0.8f);

                        speak();
                    }
                }
            }
        });


        admin = findViewById(R.id.admin);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText("");
                    txvResult.setText(result.get(0));
                    speak();
                    txvResult.setText("");
                    SpeechInput();
                }
                break;
        }
    }

    private void speak() {
        String cat = txvResult.getText().toString();
        MessageToast.show(MainActivity.this, "You said: " + cat);
        List<Product> myProd = getProducts(cat);
        StringBuilder builder = new StringBuilder();
        if (cat.length() > 0) {
            builder.setLength(0);
            if (cat.contains("thank you")) {
                builder.append("It has been good talking to you bye");
            } else if (cat.contains("yes")) {
                builder.append("What is your address");
            } else if (cat.contains("address")) {
                builder.append("Your request has been submitted to OK, please be patient on us");
            } else {
                if (myProd.toString().length() > 3) {
                    builder.append("Under " + cat + " we have ");
                    for (Product prod : myProd) {
                        builder.append(prod.getName() + " costing a price of $" + prod.getPrice() + " and expiry date is " + prod.getExpiryDate() + ",");
                    }
                    builder.append("Do you wish to purchase");

                } else {
                    builder.append("Under " + cat + " we do not have any products ");
                }
            }

        } else {
            builder.append("Welcome to OK App developed by Patience Njiri, how may i help you");

        }






        if (cat.length()>0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(String.valueOf(builder), TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(String.valueOf(builder), TextToSpeech.QUEUE_FLUSH, null);
            }


        } else {
            //nothing
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(String.valueOf(builder), TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(String.valueOf(builder), TextToSpeech.QUEUE_FLUSH, null);
            }
            MessageToast.show(MainActivity.this,"Please say something");

        }

    }


    private List<Product> getProducts(String category) {
        List<Product> myProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                myProducts.add(product);
            }
        }
        return myProducts;
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void SpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
