package diana.cabinetmedical;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Review implements Serializable {


    @SerializedName("UserName")
    private String UserName;
    public String getCommentUserName(){
        return UserName;
    }
    public  void setCommentUserName(String UserName){
        this.UserName = UserName;
    }

    @SerializedName("RatingValue")
    private float RatingValue;
    public float getRate(){
        return RatingValue;
    }
    public void setRate(float RatingValue){
        this.RatingValue = RatingValue;
    }

    @SerializedName("Comentariu")
    private String Comentariu;
    public String getComment(){
        return Comentariu;
    }
    public  void setComment(String Comentariu){
        this.Comentariu = Comentariu;
    }

    public Review(){

    }
    public Review(String UserName, float RatingValue, String Comentariu){
        this.UserName = UserName;
        this.RatingValue = RatingValue;
        this.Comentariu = Comentariu;
    }
}
