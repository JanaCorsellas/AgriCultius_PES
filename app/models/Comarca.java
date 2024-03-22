package models;
import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
@Entity
public class Comarca extends Model{
    public String ncomarca;
    public String cultiuPrincipal;


    //una comarca tindà una llista de camps
    @OneToMany (mappedBy="ncomarca")
    public List<Camp> camp = new ArrayList <Camp>();


    //una comarca tindrà una llista d'agricultors
    @OneToMany (mappedBy="ncomarca")
    public List<Agricultor> llistaagricultor = new ArrayList <Agricultor>();


    public Comarca (String ncomarca){
        this.ncomarca = ncomarca;
    }

    public void AfegeixAgricultor(Agricultor a){
        if(!llistaagricultor.contains(a)) {
            llistaagricultor.add(a);
            this.save();
        }
    }
}

