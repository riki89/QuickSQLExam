package mobile.popay.com.quicksqlexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(AddQuestion.this,
                R.array.names, R.layout.support_simple_spinner_dropdown_item
        );
        /*ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddQuestion.this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.names)
                );
         */
        myAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
    }
}
