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
    public static void AfegeixComarca(String ncomarca, int numerocamps){
        new Comarca(ncomarca, numerocamps).save();
        renderText("S'ha afegit correctament");
    }
    public static void consultarComarques() {
        List<Comarca> comarques = Comarca.findAll();
        renderTemplate("Application/Inici.html");
    }
    public static void Principal(){
        renderTemplate("Application/Principal.html");
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
            session.put("usuari", usuari);
            session.put("nom", nom);
            session.put("cognom", cognom);
            session.put("edat", edat);
            session.put("comarca", comarca);
            renderTemplate("Application/Agricultius.html");
        }
        else{
            renderText("Aquest agricultor ja existeix");
        }
    }

    public static void LoginFormulari (){
        renderTemplate("Application/LoginFormulari.html");
    }

    public static void Login (String usuari, String contrasenya){
        Agricultor a = Agricultor.find("byUsuariAndContrasenya", usuari, contrasenya).first();
        if (a == null){
            renderText ("Agricultor perdut pel camp, cal que trobis el nord (Registra't!)");
        }
        else{
            session.put("usuari", usuari);
            session.put("nom", a.nom); // Asegúrate de tener los campos nom y cognom en la clase Agricultor
            session.put("cognom", a.cognom);
            session.put("edat", a.edat);
            //session.put("comarca", a.comarca.ncomarca); // Asegúrate de tener el campo ncomarca en la clase Comarca
            renderTemplate("/Application/Principal.html");
        }
    }

    public static void logout() {
        session.clear(); // Limpiar la sesión
        Application.Agricultius(); // Redirigir a la página de inicio
    }

    public void Convidat(){
        renderTemplate("Application/Inici.html");
    }

    public static void editProfile() {
        String usuari = session.get("usuari");
        if (usuari == null) {
            flash.error("Cal iniciar sessió per editar el perfil.");
            Application.LoginFormulari();
        }
        List<Comarca> comarques = Comarca.findAll();
        render(comarques);
    }
    public static void updateProfile(String nom, String cognom, int edat, String usuari, Long comarcaId) {
        String currentUsuari = session.get("usuari");
        if (currentUsuari == null) {
            flash.error("Cal iniciar sessió per editar el perfil.");
            Application.LoginFormulari();
        }

        Agricultor agricultor = Agricultor.find("byUsuari", currentUsuari).first();
        if (agricultor != null) {
            agricultor.nom = nom;
            agricultor.cognom = cognom;
            agricultor.edat = edat;
            agricultor.usuari = usuari;

            Comarca novaComarca = Comarca.findById(comarcaId);
            if (novaComarca!=null){
                agricultor.ncomarca = novaComarca;
            }
            //agricultor.ncomarca = Comarca.find("byNcomarca", ncomarca).first();
            agricultor.save();

            session.put("usuari", usuari);
            session.put("nom", nom);
            session.put("cognom", cognom);
            session.put("edat", edat);
            session.put("comarca", novaComarca.ncomarca);

            flash.success("Perfil actualitzat correctament.");
            Application.Principal();
        } else {
            flash.error("Error en actualitzar el perfil.");
            editProfile();
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
        new Comarca("Alt Camp", 3580).save();
        new Comarca("Alt Empordà", 28206).save();
        new Comarca("Alt Penedès", 1806).save();
        new Comarca("Alt Urgell", 5808).save();
        new Comarca("Alta Ribagorça", 299).save();
        new Comarca("Anoia", 23375).save();
        new Comarca("Aran", 1140).save();
        new Comarca("Bages", 21323).save();
        new Comarca("Baix Camp", 2177).save();
        new Comarca("Baix Ebre", 10003).save();
        new Comarca("Baix Empordà", 18241).save();
        new Comarca("Baix Llobregat", 1925).save();
        new Comarca("Baix Penedès", 751).save();
        new Comarca("Barcelonès", 31).save();
        new Comarca("Berguedà", 12866).save();
        new Comarca("Cerdanya", 3528).save();
        new Comarca("La Conca de Barberà", 16713).save();
        new Comarca("Garraf", 381).save();
        new Comarca("Garrigues", 8963).save();
        new Comarca("Garrotxa", 7325).save();
        new Comarca("Gironès", 12282).save();
        new Comarca("Lluçanès", 0).save();
        new Comarca("Maresme", 2393).save();
        new Comarca("Moianès", 5110).save();
        new Comarca("Montsià", 13203).save();
        new Comarca("Noguera", 57760).save();
        new Comarca("Osona", 25572).save();
        new Comarca("Pallars Jussà", 13760).save();
        new Comarca("Pallars Sobirà", 1246).save();
        new Comarca("Pla d'Urgell", 19910).save();
        new Comarca("Pla de l'Estany", 8594).save();
        new Comarca("Priorat", 328).save();
        new Comarca("Ribera d'Ebre", 568).save();
        new Comarca("Ripollès", 1740).save();
        new Comarca("Segarra", 42721).save();
        new Comarca("Segrià", 41481).save();
        new Comarca("Selva", 8817).save();
        new Comarca("Solsonès", 18811).save();
        new Comarca("Tarragonès", 1232).save();
        new Comarca("Terra Alta", 1019).save();
        new Comarca("Urgell", 31229).save();
        new Comarca("Vallès Occidental", 4323).save();
        new Comarca("Vallès Oriental", 8118).save();
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

    public static void showComarquesPage() {
        List<Comarca> comarques = Comarca.findAll();
        if (comarques.isEmpty()) {
            renderText("No comarques found");
        } else {
            renderTemplate("/Application/Inici.html");
        }
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