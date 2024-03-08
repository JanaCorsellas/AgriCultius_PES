package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
@Entity
public class Camp extends Model{
    private String coordenades;
    private String comarca;
    private String cultiu;
    private double hectarees;
    private String historial;

    //varios camps poden pertànyer a una comarca
    @ManyToOne
    public Comarca ncomarca;

    //varios camps poden pertànyer a un agricultor
    @ManyToOne
    public Agricultor nom;

    //per identificar un camp tindrem les coordenades, la comarca i l'agricultor del camp
    public Camp (String coordenades, Comarca ncomarca, Agricultor nom){
        this.coordenades = coordenades;
        this.ncomarca = ncomarca;
        this.nom = nom;
    }
}
