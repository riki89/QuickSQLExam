package mobile.popay.com.quicksqlexam;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Runnable;
import android.os.Handler;
import android.os.SystemClock;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import layout.AnswerQuizz;
import layout.EditQuizz;
import layout.FragmentTime;
import modele.Question;

import static org.xmlpull.v1.XmlPullParserFactory.*;

public class MainActivity extends AppCompatActivity
        implements FragmentTime.OnFragmentInteractionListener
                    ,AnswerQuizz.OnFragmentInteractionListener
                   ,EditQuizz.OnFragmentInteractionListener {
    private  Questionnaire[] tabQuizz; //  = new Questionnaire[2];
    DBManager my_db;
    String[] listQuizz;
    String[] listOptions;
    String[] listIdx;

    private LinearLayout fragmentMain;
    private LinearLayout fragmentAdd;
    private LinearLayout checkboxLayout;
    private LinearLayout radioLayout ;
    private LinearLayout fragmentAnswers;
    private LinearLayout fragmentTimer;

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

    private RadioButton[] mRadio;

    private TextView mScoreView;
    private TextView mTimer;
    private TextView mScoreNet;

    private Button mButton;
    private Button mButtonAdd;
    private Button mButtonNxtQuizz;
    private Button mbuttonStart;

    private String mAnswer;
    private int mScore = 0;
    private int mQuestionNumber = 0;
    private int nbQuizz = 3;
    private String mTypeRep = "radio";

    private Spinner spinner4;
    private Spinner spinner3;
    private Spinner listeEntier;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<CharSequence> adapter2;
    ArrayList<Questionnaire> questionnaires;
    Iterator<Questionnaire> iterator;
    ArrayList<CheckBox> listBox = new ArrayList<>();
    ArrayList<RadioButton> listRadio = new ArrayList<>();
    Iterator<RadioButton> rdIterator;
    Iterator<CheckBox> bxIterator;

    //
    Timer timer;
    MyTimerTask myTimerTask;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

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
        fragmentTimer   = (LinearLayout)findViewById(R.id.timerFrag);

        mquizzQuest     = (TextView)findViewById(R.id.quizzQuest);
        mScoreView      = (TextView)findViewById(R.id.score);
        mTimer          = (TextView)findViewById(R.id.timer);
        mScoreNet       = (TextView)findViewById(R.id.myscore);

        mButtonChoice1 = (CheckBox)findViewById(R.id.checkBox);
        mButtonChoice2 = (CheckBox)findViewById(R.id.checkBox2);
        mButtonChoice3 = (CheckBox)findViewById(R.id.checkBox3);
        mButtonChoice4 = (CheckBox)findViewById(R.id.checkBox4);

        mButton        = (Button)findViewById(R.id.nextButton);
        mButtonAdd     = (Button)findViewById(R.id.addButton);
        mButtonNxtQuizz= (Button)findViewById(R.id.button);
        //mbuttonStart = (Button)findViewById(R.id.startButton);

        mRadio1 = (RadioButton)findViewById(R.id.radioButton1);
        mRadio2 = (RadioButton)findViewById(R.id.radioButton2);
        mRadio3 = (RadioButton)findViewById(R.id.radioButton3);
        mRadio4 = (RadioButton)findViewById(R.id.radioButton4);

        try {
            questionnaires = ParseXML();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iterator = questionnaires.iterator();
        Questionnaire question = new Questionnaire();
        if (iterator.hasNext()){
            question = iterator.next();
        }else
        {
            Toast.makeText(this, "Aucune question trouvée", Toast.LENGTH_SHORT).show();
        }
        listRadio.add(mRadio1);
        listRadio.add(mRadio2);
        listRadio.add(mRadio3);
        listRadio.add(mRadio4);

        listBox.add(mButtonChoice1);
        listBox.add(mButtonChoice2);
        listBox.add(mButtonChoice3);
        listBox.add(mButtonChoice4);

        rdIterator = listRadio.iterator();
        bxIterator = listBox.iterator();

        //showLayout(question);
        updateView(question);
        //questionnaires.remove(0);

        /*
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
*/
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Questionnaire question = null;
                if (iterator.hasNext()){
                    question= iterator.next();
                    updateView(question);
                    initChekedButton();
                    //showLayout(question);
                } else {
                    Toast.makeText(MainActivity.this, "Fin du Test", Toast.LENGTH_LONG).show();
                }

               /* if (question.getTypeRep().equals("radio")){
                    int selectedRadio = grpRadio.getCheckedRadioButtonId();
                    RadioButton rbtn = (RadioButton)findViewById(selectedRadio);
                    if (rbtn.getText().equals(question.getmRep()) ) {
                        Toast.makeText(MainActivity.this, "Good answer", Toast.LENGTH_LONG).show();
                    } else Toast.makeText(MainActivity.this,"Wrong answer", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

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

    public void initChekedButton(){
            int selectedRadio = grpRadio.getCheckedRadioButtonId();
            if (selectedRadio != -1){
                RadioButton rbtn = (RadioButton)findViewById(selectedRadio);
                rbtn.setChecked(false);
            }
            if (mButtonChoice1.isChecked()){ mButtonChoice1.setChecked(false);}
            if (mButtonChoice2.isChecked()){ mButtonChoice2.setChecked(false);}
            if (mButtonChoice3.isChecked()){ mButtonChoice3.setChecked(false);}
            if (mButtonChoice4.isChecked()){ mButtonChoice4.setChecked(false);}
    }

    public void updateScore(Questionnaire q){
        switch (q.getTypeRep()){
            case "radio":
        }
        mScoreNet.setText(mScoreNet.getText()+ " "+10);
    }

    public void setRadioText(Questionnaire q){
        mRadio1.setText(q.getmOption1());
        mRadio2.setText(q.getmOption2());
        mRadio3.setText(q.getmOption3());
        mRadio4.setText(q.getmOption4());
    }

    public void setBoxText(Questionnaire q){
        mButtonChoice1.setText(q.getmOption1());
        mButtonChoice2.setText(q.getmOption2());
        mButtonChoice3.setText(q.getmOption3());
        mButtonChoice4.setText(q.getmOption4());
    }

    public void setRadioText_old(Questionnaire q){
        int i = 1;
        while(rdIterator.hasNext()){
            switch (i){
                case 1: rdIterator.next().setText(q.getmOption1());
                break;
                case 2: rdIterator.next().setText(q.getmOption2());
                break;
                case 3: rdIterator.next().setText(q.getmOption3());
                break;
                case 4: rdIterator.next().setText(q.getmOption4());
                break;
            }
            i += 1;
        }
    }

    public void setBoxText_old(Questionnaire q){
        int i = 1;
        while(bxIterator.hasNext()){
            switch (i){
                case 1: bxIterator.next().setText(q.getmOption1());
                    break;
                case 2: bxIterator.next().setText(q.getmOption2());
                    break;
                case 3: bxIterator.next().setText(q.getmOption3());
                    break;
                case 4: bxIterator.next().setText(q.getmOption4());
                    break;
            }
            i += 1;
        }
    }

    public void setOption(Questionnaire question){
        switch (question.getTypeRep()){
            case "radio": setRadioText(question);
            break;
            case "checkbox": setBoxText(question);
            break;
        }
    }

    public  void updateQuizz(Questionnaire question){
        mquizzQuest.setText(question.getmQuestion());
        mAnswer = question.getmRep();
        mQuestionNumber = question.getId();
        nbQuizz = questionnaires.size();
        updateLevel();
    }

    public void showLayout(Questionnaire question){
        if (question.getTypeRep().equals("checkbox")) {
                radioLayout.setVisibility(View.GONE);
                checkboxLayout.setVisibility(View.VISIBLE);
        } else if (question.getTypeRep().equals("radio")){
                checkboxLayout.setVisibility(View.GONE);
                radioLayout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(MainActivity.this, "Choose an option please!", Toast.LENGTH_LONG).show();
        }

    }

    public  void updateView(Questionnaire question){
        updateQuizz(question);
        setOption(question);
        showLayout(question);
    }

    private ArrayList<Questionnaire> ParseXML() throws IOException {
        XmlPullParserFactory parserFactory;

        try {
            parserFactory = newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = null;
            try {
                is = getAssets().open("questions.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            questionnaires = processParsing(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return questionnaires;
    }

    private ArrayList<Questionnaire> processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        questionnaires = new ArrayList<>();
        int eventType = parser.getEventType();
        Questionnaire currentQuestion = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String eltName = null;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    if ("question".equals(eltName)){
                        currentQuestion = new Questionnaire();
                        questionnaires.add(currentQuestion);
                    } else if (currentQuestion != null){
                        if ("id".equals(eltName)){
                            currentQuestion.setId(Integer.parseInt(parser.nextText().toString()));
                        }else if ("type".equals(eltName)){
                            currentQuestion.setTypeRep(parser.nextText());
                        }else if ("libelle".equals(eltName)){
                            currentQuestion.setmQuestion(parser.nextText());
                        }else if ("good".equals(eltName)){
                            currentQuestion.setmRep(parser.nextText());
                        }else if ("option1".equals(eltName)){
                            currentQuestion.setmOption1(parser.nextText());
                        }else if ("option2".equals(eltName)){
                            currentQuestion.setmOption2(parser.nextText());
                        }else if ("option3".equals(eltName)){
                            currentQuestion.setmOption3(parser.nextText());
                        }else if ("option4".equals(eltName)){
                            currentQuestion.setmOption4(parser.nextText());
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
        //printQuestions(questionnaires);
        return questionnaires;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            mTimer.setText("" + mins + ":"
                            + String.format("%02d", secs)
                    //+ String.format("%03d", milliseconds)
            );
            customHandler.postDelayed(this, 0);
        }
    };

    @SuppressLint("SetTextI18n")
    private  void updateLevel(){
        mScoreView.setText(""+mQuestionNumber+"/"+nbQuizz);
    }

    //**

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    //just a comment
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
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a", Locale.FRANCE);
            final String strDate = simpleDateFormat.format(calendar.getTime());

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mTimer.setText(strDate);
                }
            });
        }
    }

}
