package modele;

import mobile.popay.com.quicksqlexam.MainActivity;
import mobile.popay.com.quicksqlexam.QuestionManager;

/**
 * Created by mac on 26/04/2018.
 */

public class Question {

    //column names
    public static final String KEY_num       = "id";
    public static final String KEY_title     = "title";
    public static final String KEY_contents  = "contents";
    public static final String KEY_img       = "img";
    public static final String KEY_type      = "type";
    public static final String KEY_nb_option = "nb_option";
    //attributs
    private int num = 0;
    private String title;
    private String contents;
    private String img;
    private String type;
    private int nbOptions;

    //methods
    public  Question(){
        super();
        this.num = 0;
        this.title = "";
        this.contents = "";
        this.img = "";
        this.type = "";
        this.nbOptions= 1;
    }

    public Question(int num, String title, String contents, String img, String type, int nbOptions){
        super();
        this.num = num;
        this.title = title;
        this.contents = contents;
        this.img = img;
        this.type = type;
        if (this.type == "Unique"){
            this.nbOptions= 1;
        } else
            this.nbOptions = nbOptions;

    }
    //methods
    public  void addQuestion(QuestionManager qm, Question q){
        qm.insertData(q);
    }

    //getters & setters

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getImg() {
        return img;
    }

    public String getType() {
        return type;
    }

    public int getNbOptions() {
        return nbOptions;
    }
    //setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNbOptions(int nbOptions) {
        this.nbOptions = nbOptions;
    }
}
