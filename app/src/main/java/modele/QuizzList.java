package modele;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import mobile.popay.com.quicksqlexam.QuestionManager;
import modele.QuestionDetail;
import mobile.popay.com.quicksqlexam.R;

/**
 * Created by mac on 28/04/2018.
 */

public class QuizzList extends ListActivity implements android.view.View.OnClickListener{
    QuestionManager qm;
    TextView quizz_id;
    Button   btnAdd, btnGetAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_quizz);

        btnGetAll = (Button) findViewById(R.id.list_quizz);
        btnGetAll.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //if (view == findViewById(R.id.list_quizz)) {
            Toast.makeText(this, "List clicked! ", Toast.LENGTH_SHORT).show();
            qm = new QuestionManager(this);
            ArrayList<HashMap<String, String>> questionList = qm.getQuizzList();
            if (questionList.size() != 0) {
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        quizz_id = (TextView) view.findViewById(R.id.quizzId);
                        String quizzId = quizz_id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(), QuestionDetail.class);
                        objIndent.putExtra("student_Id", Integer.parseInt(quizzId));
                        startActivity(objIndent);
                    }
                });
                ListAdapter adapter = new SimpleAdapter(QuizzList.this, questionList, R.layout.list_quizz,
                        new String[]{"Title", "Type"}, new int[]{R.id.quizzId, R.id.quizztext});
                setListAdapter(adapter); //pour adapter l'affichage
            }
        //}
    }
}
