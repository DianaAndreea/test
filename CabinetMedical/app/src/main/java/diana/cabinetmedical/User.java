package diana.cabinetmedical;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable{

    @SerializedName("UserID")
    public int UserID;
    public void setUserID(int UserID){
        this.UserID = UserID;
    }
    public int getUserID(){
        return UserID;
    }

    @SerializedName("numeUtilizator")
    public String numeUtilizator;
    public String getNumeUtilizator(){return numeUtilizator;}

    @SerializedName("email")
    public String email;

    @SerializedName("parola")
    public String parola;

    @SerializedName("numeDoctor")
    private String numeDoctor;
    public void setNumeDoctor(String numeDoctor){
        this.numeDoctor = numeDoctor;
    }
    public String getNumeDoctor(){
        return numeDoctor;
    }

    @SerializedName("specID")
    public String specID;

    @SerializedName("adresa")
    public String adresa;

    @SerializedName("descriere")
    public String descriere;

    @SerializedName("program")
    public String program;

    @SerializedName("tarif")
    public String tarif;

    @SerializedName("telefon")
    public String telefon;
}
