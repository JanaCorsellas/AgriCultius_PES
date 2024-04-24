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
    public static void RegistrarAgricultor(String nom, String cognom, int edat, String usuari){
        Agricultor a = Agricultor.find("byNomAndCognomAndEdatAndUsuari", nom, cognom, edat, usuari).first();
        if (a == null){
            new Agricultor(nom,cognom,edat,usuari).save();
        }
        else{
            renderText("Ja existeix");
        }
    }

    public static void LoginFormulari (){
        renderTemplate("Application/LoginFormulari.html");
    }

    public static void Inici(){
        renderTemplate("Application/Inici.html");
    }
    public static void Login (String usuari){
        Agricultor a = Agricultor.find("byNomAndCognomAndEdat", usuari).first();
        if (a == null){
            renderText ("Agricultor perdut pel camp, cal que trobis el nord (Registra't!)");
        }
        else{
            renderText("Benvingut/da a AgriCultius");
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
        Comarca c1 = new Comarca("Conca de Barberà");
        Comarca c2 = new Comarca("Garrotxa");
        Agricultor a1 = new Agricultor ("Jana", "Corsellas", 21, "janacorse").save();
        Agricultor a2 = new Agricultor("Ivan", "Garcia", 21, "ivan2003").save();

        c1.AfegeixAgricultor(a1);
        c1.save();
        c2.AfegeixAgricultor(a2);
        c2.save();
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