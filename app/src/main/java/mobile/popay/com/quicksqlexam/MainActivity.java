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

import static org.xmlpull.v1.XmlPullParserFactory.*;

public class MainActivity extends AppCompatActivity
        implements FragmentTime.OnFragmentInteractionListener
                    ,AnswerQuizz.OnFragmentInteractionListener
                   ,EditQuizz.OnFragmentInteractionListener {
    //private  Questionnaire mQuestionLibrary = new Questionnaire();
    Questionnaire mQuestionLibrary ;
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
        mTimer        = (TextView)findViewById(R.id.timer);

        mButtonChoice1 = (CheckBox)findViewById(R.id.checkBox);
        mButtonChoice2 = (CheckBox)findViewById(R.id.checkBox2);
        mButtonChoice3 = (CheckBox)findViewById(R.id.checkBox3);
        mButtonChoice4 = (CheckBox)findViewById(R.id.checkBox4);

        mButton        = (Button)findViewById(R.id.nextButton);
        mButtonAdd     = (Button)findViewById(R.id.addButton);
        mButtonNxtQuizz= (Button)findViewById(R.id.button);
        mbuttonStart = (Button)findViewById(R.id.startButton);

        mRadio1 = (RadioButton)findViewById(R.id.radioButton1);
        mRadio2 = (RadioButton)findViewById(R.id.radioButton2);
        mRadio3 = (RadioButton)findViewById(R.id.radioButton3);
        mRadio4 = (RadioButton)findViewById(R.id.radioButton4);

        mQuestionLibrary = new Questionnaire();
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

        updateView(question);

        /*spinner3 = (Spinner) findViewById(R.id.spinner3);
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
                Questionnaire question;
                if (iterator.hasNext()){
                    question = iterator.next();
                    if (question.getId() == mQuestionNumber){
                        question = iterator.next();
                    }
                    showLayout(question.getTypeRep());
                    updateView(question);
                } else {
                    Toast.makeText(MainActivity.this, "Fin du Test", Toast.LENGTH_LONG).show();
                }
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

    public void setRadioText(Questionnaire q, ArrayList<RadioButton> radio){
        Iterator<RadioButton> iterator1 = radio.iterator();
        int i = 1;
        while(iterator1.hasNext()){
            switch (i){
                case 1: iterator1.next().setText(q.getmOption1());
                break;
                case 2: iterator1.next().setText(q.getmOption2());
                break;
                case 3: iterator1.next().setText(q.getmOption3());
                    break;
                case 4: iterator1.next().setText(q.getmOption4());
                    break;
            }
            i += 1;
        }
    }

    public void setBoxText(Questionnaire q, ArrayList<CheckBox> radio){
        Iterator<CheckBox> iterator1 = radio.iterator();
        int i = 1;
        while(iterator1.hasNext()){
            switch (i){
                case 1: iterator1.next().setText(q.getmOption1());
                    break;
                case 2: iterator1.next().setText(q.getmOption2());
                    break;
                case 3: iterator1.next().setText(q.getmOption3());
                    break;
                case 4: iterator1.next().setText(q.getmOption4());
                    break;
            }
            i += 1;
        }
    }


    public  void updateView(Questionnaire question){
        if (question.getTypeRep().equals("radio")){
            setRadioText(question, listRadio);
        }else if (question.getTypeRep().equals("checkbox")){
            setBoxText(question, listBox);
        } else Toast.makeText(this, "type de question "+question.getTypeRep()+"non pris en compte"
                , Toast.LENGTH_SHORT).show();

        mquizzQuest.setText(question.getmQuestion());
        mAnswer = question.getmRep();
        mQuestionNumber = question.getId();
        nbQuizz = questionnaires.size();
        updateLevel();
    }
    public void showLayout(String typeRep){
        if (typeRep.equals("radio")) {
            if (checkboxLayout.getVisibility() == View.GONE){
                radioLayout.setVisibility(View.GONE);
                checkboxLayout.setVisibility(View.VISIBLE);
            }
            int selectedRadio = grpRadio.getCheckedRadioButtonId();
            RadioButton rbtn = (RadioButton)findViewById(selectedRadio);
            if (rbtn.getText().equals(mAnswer)){
                Toast.makeText(MainActivity.this, "Good answer", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(MainActivity.this, "Wrong answer", Toast.LENGTH_LONG).show();
            //mQuestionNumber = mQuestionNumber + 1;
        } else if (typeRep.equals("checkbox")){
            if (radioLayout.getVisibility() == View.GONE){
                checkboxLayout.setVisibility(View.GONE);
                radioLayout.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(MainActivity.this, "Choose an option please!", Toast.LENGTH_LONG).show();
        }
        //mQuestionNumber = mQuestionNumber + 1;
    }

    private ArrayList<Questionnaire> ParseXML() throws IOException {
        XmlPullParserFactory parserFactory;

        try {
            parserFactory = newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = null;
            try {
                is = getAssets().open("questions.xml");
                //Toast.makeText(MainActivity.this, "file opened: ", Toast.LENGTH_LONG).show();
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

    private void printQuestions(ArrayList<Questionnaire> questionnaires){
        StringBuilder builder = new StringBuilder();

        for (Questionnaire q: questionnaires){
            builder.append(q.getId()).append("\n").
                    append(q.getmQuestion()).append("\n").
                    append(q.getmOption1()).append("\n").
                    append(q.getmOption2()).append("\n").
                    append(q.getmOption3()).append("\n").
                    append(q.getmOption4()).append("\n");
           // this.questionnaires.add(q) ;
            //mquizzQuest.setText(q.getTypeRep()+"\n"+q.getmQuestion()+"\n"+q.getmOption4());
        }

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
        if (mQuestionNumber == nbQuizz) {
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

    @SuppressLint("SetTextI18n")
    private  void updateLevel(){
        mScoreView.setText(""+mQuestionNumber+"/"+nbQuizz);
    }

    //**
    private void updateQuestion_v2(){
        //mquizzQuest.setText(iterator.next().getTypeRep());

        //mTypeRep = "checkbox"; //iterator.next().getTypeRep();
        if (!iterator.hasNext()) {
            Toast.makeText(MainActivity.this, "Fin du Test", Toast.LENGTH_LONG).show();
            mQuestionNumber = 0;
            updateRadio_v2();
        }
        else if (iterator.next().getTypeRep() == "checkbox"){
            mquizzQuest.setText(""+iterator.next().getmQuestion());
            mButtonChoice1.setText(""+iterator.next().getmOption1());
            mButtonChoice2.setText(""+iterator.next().getmOption2());
            mButtonChoice3.setText(""+iterator.next().getmOption3());
            mButtonChoice4.setText(""+iterator.next().getmOption4());

            mAnswer = iterator.next().getmRep();

            //mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
        }else if (iterator.next().getTypeRep() == "radio"){
            mquizzQuest.setText(""+iterator.next().getmQuestion());
            mRadio1.setText(""+iterator.next().getmOption1());
            mRadio2.setText(""+iterator.next().getmOption2());
            mRadio3.setText(""+iterator.next().getmOption3());
            mRadio4.setText(""+iterator.next().getmOption4());

            mAnswer = iterator.next().getmRep();
                    //mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
        }
        else Toast.makeText(MainActivity.this, "Erreur inconnue", Toast.LENGTH_LONG).show();

    }
    //
    private void updateRadio_v2(){
        if (mQuestionNumber == nbQuizz) {
            //Toast.makeText(MainActivity.this, "Fin du Test", Toast.LENGTH_LONG).show();
            mQuestionNumber = 0;
            updateQuestion_v2();
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
    /*
    private  void updateLevel(){
        mScoreView.setText(""+(mQuestionNumber+1)+"/"+nbQuizz);
    }
    */

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
