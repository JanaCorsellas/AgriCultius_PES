package models;
import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
@Entity
public class Comarca extends Model{
    private String ncomarca;
    private String cultiuPrincipal;

    //una comarca tindà una llista de camps
    @OneToMany
    public List<Camp> camp;

    //una comarca tindrà una llista d'agricultors
    @OneToMany
    public List<Agricultor> agricultor;

    public Comarca (String ncomarca){
        this.ncomarca = ncomarca;
    }
}
