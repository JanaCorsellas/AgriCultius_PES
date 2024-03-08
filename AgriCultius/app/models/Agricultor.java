package models;
import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
@Entity
public class Agricultor extends Model{
    private String nom;
    private String cognom;
    private int edat;
    private String especialitat;
    private int ncamps;
    private double hectarees;
    private String historiccultius;

    //varios agricultors poden pertànyer a una comarca
    @ManyToOne
    public Comarca ncomarca;

    //un agricultor tindrà una llista de camps
    @OneToMany
    public List<Camp> camp;

    //per identificar un agricultor tindrem el seu nom, cognom i comarca on treballa
    public Agricultor (String nom, String cognom, Comarca ncomarca){
        this.nom = nom;
        this.cognom = cognom;
        this.ncomarca = ncomarca;
    }
}
