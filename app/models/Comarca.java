package models;
import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
@Entity
public class Comarca extends Model{
    public String ncomarca;
    public String cultiuPrincipal;
    public int numerocamps;


    //una comarca tindrà una llista d'agricultors
    @OneToMany (mappedBy="ncomarca")
    public List<Agricultor> llistaagricultor = new ArrayList <Agricultor>();


    public Comarca (String ncomarca, int numerocamps){
        this.ncomarca = ncomarca;
        this.numerocamps = numerocamps;
    }

    public void AfegeixAgricultor(Agricultor a){
        if(!llistaagricultor.contains(a)) {
            llistaagricultor.add(a);
            this.save();
        }
    }
}

