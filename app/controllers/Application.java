package controllers;

import play.*;
import play.mvc.*;
import play.db.jpa.JPA;

import java.util.*;
import java.lang.String;

import models.*;
import javax.persistence.Query;

public class Application extends Controller {

    public static void index() {
        renderTemplate("Application/Agricultius.html");
    }

    public static void Inici(){
        renderTemplate("Application/Inici.html");
    }

    public static void Agricultius(){
        renderTemplate("Application/Agricultius.html");
    }
    public static void AfegeixComarca(String ncomarca){
        new Comarca(ncomarca).save();
        renderText("S'ha afegit correctament");
    }

    public void Registre(){
        renderTemplate("Application/Registre.html");
    }
    public static void RegistrarAgricultor(String nom, String cognom, int edat, String usuari, String contrasenya, String comarca){
        Agricultor a = Agricultor.find("byNomAndCognomAndEdatAndUsuariAndContrasenya", nom, cognom, edat, usuari, contrasenya).first();
        if (a == null){
            Comarca nc = Comarca.find("byNcomarca", comarca).first();
            //renderXml(nc);
            new Agricultor(nom,cognom,edat,usuari,contrasenya,nc).save();
            renderTemplate("Application/Inici.html");
        }
        else{
            renderText("Aquest agricultor ja existeix");
        }
    }

    public void LoginFormulari (){
        renderTemplate("Application/LoginFormulari.html");
    }

    public static void Login (String usuari, String contrasenya){
        Agricultor a = Agricultor.find("byUsuariAndContrasenya", usuari, contrasenya).first();
        if (a == null){
            renderText ("Agricultor perdut pel camp, cal que trobis el nord (Registra't!)");
        }
        else{
            renderTemplate("Application/Inici.html");
        }
    }




    public static void AssignaAgricultorComarca(String nom, String cognom, int edat, String ncomarca){
        Comarca c = Comarca.find("byNcomarca", ncomarca).first();
        if (c!=null){
            Agricultor a = Agricultor.find("byNomAndCognomAndEdat", nom, cognom, edat).first();
            if (a!=null){
                c.AfegeixAgricultor(a);
                a.AfegeixComarca(c);
                a.save();
            }else{
                renderText("Error, agricultor no existeix");
            }
        }else{
            renderText("Error, Comarca no existeix");
        }
        renderText("S'ha completat correctament");
    }

    public static void Test(){
        new Comarca("Alt Camp").save();
        new Comarca("Alt Empordà").save();
        new Comarca("Alt Penedès").save();
        new Comarca("Alt Urgell").save();
        new Comarca("Alta Ribagorça").save();
        new Comarca("Anoia").save();
        new Comarca("Aran").save();
        new Comarca("Bages").save();
        new Comarca("Baix Camp").save();
        new Comarca("Baix Ebre").save();
        new Comarca("Baix Empordà").save();
        new Comarca("Baix Llobregat").save();
        new Comarca("Baix Penedès").save();
        new Comarca("Barcelonès").save();
        new Comarca("Berguedà").save();
        new Comarca("Cerdanya").save();
        new Comarca("La Conca de Barberà").save();
        new Comarca("Garraf").save();
        new Comarca("Garrigues").save();
        new Comarca("Garrotxa").save();
        new Comarca("Gironès").save();
        new Comarca("Lluçanès").save();
        new Comarca("Maresme").save();
        new Comarca("Moianès").save();
        new Comarca("Montsià").save();
        new Comarca("Noguera").save();
        new Comarca("Osona").save();
        new Comarca("Pallars Jussà").save();
        new Comarca("Pallars Sobirà").save();
        new Comarca("Pla d'Urgell").save();
        new Comarca("Pla de l'Estany").save();
        new Comarca("Priorat").save();
        new Comarca("Ribera d'Ebre").save();
        new Comarca("Ripollès").save();
        new Comarca("Segarra").save();
        new Comarca("Segrià").save();
        new Comarca("Selva").save();
        new Comarca("Solsonès").save();
        new Comarca("Tarragonès").save();
        new Comarca("Terra Alta").save();
        new Comarca("Urgell").save();
        new Comarca("Vallès Occidental").save();
        new Comarca("Vallès Oriental").save();
        /*Comarca c1 = new Comarca("La Conca de Barberà");
        c1.save();
        Comarca c2 = new Comarca("Garrotxa");
        c2.save();
        Agricultor a1 = new Agricultor ("Jana", "Corsellas", 21, "janacorse", "1234", c1).save();
        Agricultor a2 = new Agricultor("Ivan", "Garcia", 21, "ivan2003", "1234", c2).save();

        c1.AfegeixAgricultor(a1);
        c1.save();
        c2.AfegeixAgricultor(a2);
        c2.save();*/
    }

    //Servei que llista les comarques que tenen mínim x Agricultors
    //funciona però hem hagut de posar la variable ncomarca pública
    public static void NomComarquesXAgricultors(int nombreMinAgricultors){
        List<Comarca> lc = Comarca.findAll();
        List<String> result = new ArrayList<String>();

        for (Comarca c: lc){
            if (c.llistaagricultor.size() >= nombreMinAgricultors)
                result.add(c.ncomarca);
        }
        if (result.size()>0){
            renderText(result);
        }else{
            renderText("Cap comarca té 20 o més Agricultors");
        }
    }

    //Servei que llista tots els agricultors de la base de dades
    //hem hagut de posar nom en public
    public static void NomAgricultors(){
        List<Agricultor> la = Agricultor.findAll();
        List<String> result2 = new ArrayList<String>();

        for (Agricultor a: la){
            result2.add(a.nom);
        }
        if (result2.size()>0){
            renderText(result2);
        }else{
            renderText("No hi ha cap agricultor registrat");
        }
    }
}