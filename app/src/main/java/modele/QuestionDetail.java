package modele;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import mobile.popay.com.quicksqlexam.QuestionManager;
import mobile.popay.com.quicksqlexam.R;

/**
 * Created by mac on 27/04/2018.
 */

public class QuestionDetail extends Activity implements View.OnClickListener{
    EditText    title;
    EditText    content;
    TextView    img, quizz_id;
    RadioGroup type;
    Spinner nbReponse;

    Button nextBtn;

    private QuestionManager qm;

    private int _question_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_quizz);

        nextBtn = (Button)findViewById(R.id.nextButton);
        title   = (EditText)findViewById(R.id.QuizzTitle);
        content = (EditText)findViewById(R.id.quizztext);
        img     = (TextView)findViewById(R.id.quizzImg);

        type    = (RadioGroup) findViewById(R.id.type);
        nbReponse = (Spinner)findViewById(R.id.spinner2);

        nextBtn.setOnClickListener(this);

        qm = new QuestionManager(this);
        _question_id = 0;
        Intent intent = getIntent(); // passage de paramètres
        _question_id = intent.getIntExtra("", 0);

        Question q = new Question();
        q = qm.getQuestionById(_question_id);

        title.setText(q.getTitle());
        content.setText(q.getContents());
        //img.setBackground();
        //type.set
        nbReponse.setPromptId(q.getNbOptions());
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.nextButton)){
            Question q = new Question();

            q.setTitle(title.getText().toString());
            q.setContents(content.getText().toString());
            q.setImg(img.getText().toString());
            q.setType(findViewById(type.getCheckedRadioButtonId()).toString());
            q.setNbOptions(Integer.parseInt(findViewById(R.id.spinner2).toString()));

            if (_question_id==0){
                _question_id = qm.insertData(q);
                Toast.makeText(this,"New Question Inserted",Toast.LENGTH_SHORT).show();
            } else{
                qm.update(q);
                Toast.makeText(this,"New Question Updated",Toast.LENGTH_SHORT).show();
            }
        }/*else if (view== findViewById(R.id.btnDelete)){
            qm.delete(_question_id);
            Toast.makeText(this, "Question Record Deleted", Toast.LENGTH_SHORT);
            finish();
        }else if (view== findViewById(R.id.btnClose)){
            finish();
        }*/
    }
}
