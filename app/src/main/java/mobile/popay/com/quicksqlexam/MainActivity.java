package mobile.popay.com.quicksqlexam;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Runnable;

import layout.AnswerQuizz;
import layout.EditQuizz;
import layout.FragmentOne;
import layout.FragmentTime;
import layout.FragmentTwo;

public class MainActivity extends AppCompatActivity
        implements FragmentTime.OnFragmentInteractionListener
                    ,AnswerQuizz.OnFragmentInteractionListener
                   ,EditQuizz.OnFragmentInteractionListener {
    private  Questionnaire mQuestionLibrary = new Questionnaire();
    DBManager my_db;
    private LinearLayout fragmentMain;
    private LinearLayout fragmentAdd;
    private LinearLayout checkboxLayout;
    private LinearLayout radioLayout ;
    private LinearLayout fragmentAnswers;

    private TextView mquizzQuest;
    private CheckBox mButtonChoice1;
    private CheckBox mButtonChoice2;
    private CheckBox mButtonChoice3;
    private CheckBox mButtonChoice4;

    private RadioGroup grpRadio;
    private RadioButton mRadio1;
    private RadioButton mRadio2;
    private RadioButton mRadio3;
    private RadioButton mRadio4;

    private TextView mScoreView;
            TextView mTimer;

    private Button mButton;
    private Button mButtonAdd;
    private Button mButtonNxtQuizz;

    private String   mAnswer;
    private int mScore = 0;
    private int mQuestionNumber = 0;
    private int nbQuizz = 2;
    private String mTypeRep = "radio";

    private Spinner spinner4;
    private Spinner spinner3;
    private Spinner listeEntier;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<CharSequence> adapter2;

    //
    Timer timer;
    //MyTimerTask myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        my_db           = new DBManager(this);
        grpRadio        = (RadioGroup)findViewById(R.id.radioGrp);
        fragmentMain    = (LinearLayout)findViewById(R.id.fragment_main);
        fragmentAdd     = (LinearLayout)findViewById(R.id.quizz_frag);
        checkboxLayout  = (LinearLayout)findViewById(R.id.checkLayout);
        radioLayout     = (LinearLayout)findViewById(R.id.radioLayout);
        fragmentAnswers = (LinearLayout)findViewById(R.id.answers_frag);


        mquizzQuest     = (TextView)findViewById(R.id.quizzQuest);
        mScoreView      = (TextView)findViewById(R.id.score);
        //mTimer        = (TextView)findViewById(R.id.timer);

        mButtonChoice1 = (CheckBox)findViewById(R.id.checkBox);
        mButtonChoice2 = (CheckBox)findViewById(R.id.checkBox2);
        mButtonChoice3 = (CheckBox)findViewById(R.id.checkBox3);
        mButtonChoice4 = (CheckBox)findViewById(R.id.checkBox4);

        mButton        = (Button)findViewById(R.id.nextButton);
        mButtonAdd     = (Button)findViewById(R.id.addButton);
        mButtonNxtQuizz= (Button)findViewById(R.id.button);

        mRadio1 = (RadioButton)findViewById(R.id.radioButton1);
        mRadio2 = (RadioButton)findViewById(R.id.radioButton2);
        mRadio3 = (RadioButton)findViewById(R.id.radioButton3);
        mRadio4 = (RadioButton)findViewById(R.id.radioButton4);

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.questions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter);

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner4.setAdapter(adapter);

        listeEntier = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter2 = ArrayAdapter.createFromResource(this,
                R.array.entier, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        listeEntier.setAdapter(adapter2);

        updateQuestion();
        /*
        lancer le temps
        timer = new Timer();
        myTimerTask = new MyTimerTask();

        if(optSingleShot.isChecked()){
            //singleshot delay 1000 ms
            timer.schedule(myTimerTask, 1000);
        } else{
            //delay 1000ms, repeat in 5000ms
          //  timer.schedule(myTimerTask, 1000, 5000);
        }
        }});
        */

        updateLevel();
        updateRadio();
        //Toast.makeText(MainActivity.this, "X2-2x+1=0; a pour racine: "+mQuestionLibrary.racine(1,-2,1), Toast.LENGTH_LONG).show();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTypeRep == "radio") {
                    if (checkboxLayout.getVisibility() == View.GONE){
                        radioLayout.setVisibility(View.GONE);
                        checkboxLayout.setVisibility(View.VISIBLE);
                    }
                    int selectedRadio = grpRadio.getCheckedRadioButtonId();
                    RadioButton rbtn = (RadioButton)findViewById(selectedRadio);
                    if (rbtn.getText().equals(mAnswer) ){
                        Toast.makeText(MainActivity.this, "Good answer", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Wrong answer", Toast.LENGTH_LONG).show();
                    mQuestionNumber = mQuestionNumber + 1;
                    //updateRadio();
                    mTypeRep = "checkbox";
                    updateQuestion();
                }
                else if (mTypeRep == "checkbox"){
                    if (radioLayout.getVisibility() == View.GONE){
                        checkboxLayout.setVisibility(View.GONE);
                        radioLayout.setVisibility(View.VISIBLE);
                    }
                    mQuestionNumber = mQuestionNumber + 1;
                    //updateQuestion();
                    mTypeRep =  "radio";
                    updateRadio();
                }else{
                    Toast.makeText(MainActivity.this, "Choose an option please!", Toast.LENGTH_LONG).show();
                    updateQuestion();
                }
                updateLevel();

            }
        }); //end nextButton listner
        mButtonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fragmentMain.setVisibility(View.GONE);
                fragmentAnswers.setVisibility(View.GONE);
                fragmentAdd.setVisibility(View.VISIBLE);
            }
        });
        mButtonNxtQuizz.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                fragmentAdd.setVisibility(View.GONE);
                fragmentMain.setVisibility(View.GONE);
                fragmentAnswers.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateQuestion(){
        if (mQuestionNumber == nbQuizz) {
            //Toast.makeText(MainActivity.this, "Fin du Test", Toast.LENGTH_LONG).show();
            mQuestionNumber = 0;
            updateRadio();
        }
        else{
            mquizzQuest.setText(""+mQuestionLibrary.getQuestion(mQuestionNumber));
            mButtonChoice1.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 0));
            mButtonChoice2.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 1));
            mButtonChoice3.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 2));
            mButtonChoice4.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 3));

            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
        }
    }
    //
    private void updateRadio(){
        if (mQuestionNumber == 2) {
            //Toast.makeText(MainActivity.this, "Fin du Test", Toast.LENGTH_LONG).show();
            mQuestionNumber = 0;
            updateQuestion();
        }
        else{
            mquizzQuest.setText(""+mQuestionLibrary.getQuestion(mQuestionNumber));
            mRadio1.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 0));
            mRadio2.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 1));
            mRadio3.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 2));
            mRadio4.setText(""+mQuestionLibrary.getChoice(mQuestionNumber, 3));

            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
        }
    }

    private  void updateLevel(){
        mScoreView.setText(""+(mQuestionNumber+1)+"/"+nbQuizz);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*public void changeFragment(View view){
        Fragment fragment;
        if (view == findViewById(R.id.addButton)) {
            fragment = new FragmentOne();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.edit_quizz, fragment);
            ft.commit();
        }*/
        /*if (view == findViewById(R.id.nextButton)){
            fragment = new FragmentTwo();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.timer, fragment);
            ft.commit();
        }
    }*/
}
/*
class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
        final String strDate = simpleDateFormat.format(calendar.getTime());

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTimer.setText(strDate);
            }
        });
    }
}*/