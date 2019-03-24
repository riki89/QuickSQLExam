package mobile.popay.com.quicksqlexam;

/**
 * Created by mac on 08/11/2017.
 */

import java.util.ArrayList;
import java.util.Map;

import modele.Question;

public class Questionnaire {
    public static final String TABLE = "Questionnaire";

    public static final String KEY_numquizz = "id";
    public static final String KEY_imgquizz = "imgquizz";
    public static final String KEY_good_rep = "reponse";
    public static final String KEY_question = "question";
    public static final String KEY_typeRep  = "typerep";
    public static final String KEY_option1  = "option1";
    public static final String KEY_option2  = "option2";
    public static final String KEY_option3  = "option3";
    public static final String KEY_option4  = "option4";
    public static final String KEY_option5  = "option5";

    //property
    private int numquizz ;
    private String imgQuizz;
    private String mQuestion;
    private String mRep;
    private String typeRep;
    private String[][] options;
    private String mOption1;
    private String mOption2;
    private String mOption3;
    private String mOption4;
    private String mOption5;

    // getters and setters


    public int getId() { return numquizz; }

    public void setId(int id) { this.numquizz = id; }

    public void setImgQuizz(String img) { this.imgQuizz = img;}

    public String getImgQuizz() { return this.imgQuizz;}

    public String getmRep() { return this.mRep; }

    public void setmRep(String mRep) { this.mRep = mRep; }

    public String getmQuestion() { return this.mQuestion; }

    public void setmQuestion(String mQ1) { this.mQuestion = mQ1; }

    public  String getTypeRep(){return  this.typeRep;}

    public  void  setTypeRep(String typeRep){ this.typeRep = typeRep;}

    public String getmOption1() { return this.mOption1; }

    public void setmOption1(String mQ1) { this.mOption1 = mQ1; }

    public String getmOption2() { return this.mOption2; }

    public void setmOption2(String mQ2) { this.mOption2 = mQ2; }

    public String getmOption3() { return this.mOption3; }

    public void setmOption3(String mQ3) { this.mOption3 = mQ3; }

    public String getmOption4() { return this.mOption4; }

    public void setmOption4(String mQ4) { this.mOption4 = mQ4; }

    public String getmOption5() { return this.mOption5; }

    public void setmOption5(String mQ5) { this.mOption5 = mQ5; }

    /*public String getCorrectAns(String quizz, String[] ops, int idx){

    }*/

    private  String mQuestions []  = {
            "Which of the following SQL statement will SELECT all records with their columns from a table called sales ?",
            "Which of the following SQL statement will SELECT all records with their columns from a view called Order_v ?",
            "You need to load information about new customers from the NEW_CUST table into the tables CUST and CUST_SPECIAL."
                    + "if a new customer has a credit limit greater than 10,000 then the details have to be inserted into CUST_SPECIAL" +
                    "all new details have to be inserted into the CUST table. Wich technique should be used to load the data most efficiently?",
    };

    private  String mChoices [][] = {
            {"select * from sales","select * from sales where 1=1","select from sales","select all from sales_t"},
            {"select * from Order where 1=2", "select * from Order_v", "select from orders_v","select all from sales_v"},
            {"A. External tables", "B. the MERGE command", "the multitable INSERT command", "INSERT using WITH CHECK OPTION"}
    };

    private  String mCorrectAnswers[] =  {"select * from sales", "select * from Order_v", "the multitable INSERT command"};

    public  String getQuestion(int a){
        return mQuestions[a];
    }

    public  String  getChoice(int a, int choice){
        //double b = Math.sqrt(2);
        return  mChoices[a][choice];
    }

    public  String getCorrectAnswer(int a){
        return mCorrectAnswers[a];
    }

    public double racine(float a, float b, float c){
        //ax2 + bx + C = 0
        if (a == 0 ) {
            if (b == 0)
                return 0;
            else
                return -c / b;
        }else {
            double x1, x2,x0;
            double delta = b*b - 4*a*c;
            if (delta >0)
            {
                x1 = ((-b)- Math.sqrt(delta))/(2*a);
                x2 = ((-b)+delta)/(2*a);
                return  x1;
            }
            if (delta ==0)
            {
                x0 =(-b)/(2*a);
                return x0;
            }
            return  0;
        }

    }
}
