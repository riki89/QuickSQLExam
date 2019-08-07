package mobile.popay.com.quicksqlexam;

import android.annotation.SuppressLint;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
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
import utils.FileParser;

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
    private CheckBox[] mButtonChoices;
    private CheckBox mButtonChoice1;
    private CheckBox mButtonChoice2;
    private CheckBox mButtonChoice3;
    private CheckBox mButtonChoice4;

    private RadioGroup grpRadio;
    private RadioButton[] mRadios;
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

        mButtonChoices = new CheckBox[4];
        mButtonChoices[0] = (CheckBox)findViewById(R.id.checkBox);
        mButtonChoices[1] = (CheckBox)findViewById(R.id.checkBox2);
        mButtonChoices[2] = (CheckBox)findViewById(R.id.checkBox3);
        mButtonChoices[3] = (CheckBox)findViewById(R.id.checkBox4);


        mButton        = (Button)findViewById(R.id.nextButton);
        mButtonAdd     = (Button)findViewById(R.id.addButton);
        mButtonNxtQuizz= (Button)findViewById(R.id.button);

        mRadios = new RadioButton[4];
        mRadios[0] = (RadioButton)findViewById(R.id.radioButton1);
        mRadios[1] = (RadioButton)findViewById(R.id.radioButton2);
        mRadios[2] = (RadioButton)findViewById(R.id.radioButton3);
        mRadios[3] = (RadioButton)findViewById(R.id.radioButton4);
        try {
            FileParser fp = new FileParser();
            if (fp.equals(null))
            {
                System.out.println("Pas d'objets");
            } else
            {
                System.out.println("Pas d'objets");
            }
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
        for (int i=0; i<mRadios.length; i++){
            listRadio.add(mRadios[i]);
        }

        for (int i=0; i<mButtonChoices.length; i++){
            listBox.add(mButtonChoices[i]);
        }

        rdIterator = listRadio.iterator();
        bxIterator = listBox.iterator();

        //showLayout(question);
        if ( !question.equals(null)){
            updateView(question);
        } else
            Toast.makeText(MainActivity.this, "Quizz nulls", Toast.LENGTH_LONG).show();

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
            for (int i=0; i<mButtonChoices.length; i++){
                if (mButtonChoices[i].isChecked()){ mButtonChoices[i].setChecked(false);}
            }
            /*
            if (mButtonChoice1.isChecked()){ mButtonChoice1.setChecked(false);}
            if (mButtonChoice2.isChecked()){ mButtonChoice2.setChecked(false);}
            if (mButtonChoice3.isChecked()){ mButtonChoice3.setChecked(false);}
            if (mButtonChoice4.isChecked()){ mButtonChoice4.setChecked(false);}
            */
    }

    public void updateScore(Questionnaire q){
        switch (q.getTypeRep()){
            case "radio":
        }
        mScoreNet.setText(mScoreNet.getText()+ " "+10);
    }

    public void setRadioText_old2(Questionnaire q){
        mRadio1.setText(q.getmOption1());
        mRadio2.setText(q.getmOption2());
        mRadio3.setText(q.getmOption3());
        mRadio4.setText(q.getmOption4());
    }
    public void setRadioText(Questionnaire q) {
        for (int i = 0; i < 4; i++) {
            mRadios[i].setText(q.getOption(i));
        }
    }

    public void setBoxText_old2(Questionnaire q){
        mButtonChoice1.setText(q.getmOption1());
        mButtonChoice2.setText(q.getmOption2());
        mButtonChoice3.setText(q.getmOption3());
        mButtonChoice4.setText(q.getmOption4());
    }

    public void setBoxText(Questionnaire q){
        for (int i=0; i < 4; i++){
            mButtonChoices[i].setText(q.getOption(i));
        }
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
        System.out.print("Option 1: "+question.getOption(0));
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
        Questionnaire qst = question;
        updateQuizz(question);
        setOption(qst);
        showLayout(qst);
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
            int i = 0;
            String eltName = null;
            String[] args = new String[5];

            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    if ("question".equals(eltName)){
                        currentQuestion = new Questionnaire();
                        questionnaires.add(currentQuestion);
                    } else if (currentQuestion != null) {
                        switch (eltName) {
                            case "id":
                                currentQuestion.setId(Integer.parseInt(parser.nextText().toString()));
                                break;
                            case "type":
                                currentQuestion.setTypeRep(parser.nextText());
                                break;
                            case "libelle":
                                currentQuestion.setmQuestion(parser.nextText());
                                break;
                            case "good":
                                currentQuestion.setGoodRep(Integer.parseInt(parser.nextText().toString()));
                                break;
                            case "option1":
                                currentQuestion.setOption(0, parser.nextText());
                                break;
                            case "option2":
                                currentQuestion.setOption(1, parser.nextText());
                                break;
                            case "option3":
                                currentQuestion.setOption(2, parser.nextText());
                                break;
                            case "option4":
                                currentQuestion.setOption(3, parser.nextText());
                                break;
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }


        //printQuestions(questionnaires);
        Toast.makeText(this, "nb quizz"+questionnaires.size(), Toast.LENGTH_SHORT).show();
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
