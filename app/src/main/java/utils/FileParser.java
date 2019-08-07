package utils;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import mobile.popay.com.quicksqlexam.*;

import static org.xmlpull.v1.XmlPullParserFactory.newInstance;

public class FileParser extends MainActivity{
    ArrayList<Questionnaire> questionnaires = new ArrayList<>();
    Iterator<Questionnaire> iterator;

    public ArrayList<Questionnaire> parseXML() throws IOException {
        XmlPullParserFactory parserFactory;

        try {
            parserFactory = newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getApplicationContext().openFileInput("questions.xml"); //null;
            Context ct = getApplicationContext();
            try {
                is = ct.getAssets().open("questions.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            questionnaires = processParsing(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        System.out.print(" Questions: "+questionnaires.size());

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
                    } else if (currentQuestion != null){
                        if ("id".equals(eltName)){
                            currentQuestion.setId(Integer.parseInt(parser.nextText().toString()));
                        }else if ("type".equals(eltName)){
                            currentQuestion.setTypeRep(parser.nextText());
                        }else if ("libelle".equals(eltName)){
                            currentQuestion.setmQuestion(parser.nextText());
                        }else if ("good".equals(eltName)){
                            //currentQuestion.setmRep(parser.nextText());
                            currentQuestion.setGoodRep( Integer.parseInt(parser.nextText().toString()));
                        }else if ("option1".equals(eltName)){
                            //currentQuestion.setmOption1(parser.nextText());
                            currentQuestion.setOption(0, parser.nextText());
                        }else if ("option2".equals(eltName)){
                            //currentQuestion.setmOption2(parser.nextText());
                            currentQuestion.setOption(1, parser.nextText());
                        }else if ("option3".equals(eltName)){
                            //currentQuestion.setmOption3(parser.nextText());
                            currentQuestion.setOption(2, parser.nextText());
                        }else if ("option4".equals(eltName)){
                            //currentQuestion.setmOption4(parser.nextText());
                            currentQuestion.setOption(3, parser.nextText());

                        }
                        // currentQuestion.setOptions(args);

                    }
                    // System.out.printf("\n %s \n %s \n %s \n %s  ", args[0], args[1], args[2], args[3]);
                    break;
            }
            eventType = parser.next();
        }
        //printQuestions(questionnaires);
       // Toast.makeText(this.getParent(), "nb quizz"+questionnaires.size(), Toast.LENGTH_SHORT).show();
        return questionnaires;
    }
}
