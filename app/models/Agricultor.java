package models;
import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
@Entity
public class Agricultor extends Model{
    public String nom;
    public String cognom;
    public int edat;
    public String especialitat;
    public int ncamps;
    public double hectarees;
    public String historiccultius;


    //varios agricultors poden pertànyer a una comarca
    @ManyToOne
    public Comarca ncomarca;


    //un agricultor tindrà una llista de camps
    @OneToMany (mappedBy="nom")
    public List<Camp> llistacamp = new ArrayList<Camp>();


    //per identificar un agricultor tindrem el seu nom, cognom i comarca on treballa
    public Agricultor (String nom, String cognom, int edat/*, Comarca ncomarca*/){
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
        this.ncomarca = null;
    }

    public void AfegeixComarca(Comarca c){
        if(ncomarca==null) {
            ncomarca=c;
            this.save();
        }
    }
}

