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
        List<Comarca> c = Comarca.findAll();
        renderArgs.put("comarques", c);
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
        List<Comarca> c = Comarca.findAll();
        renderArgs.put("comarques", c);
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
            session.put("comarca", a.ncomarca.ncomarca);
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

    public static void ocuparCamps(String ncomarca, int ncamps) {
        Comarca comarca = Comarca.find("byNcomarca", ncomarca).first();
        if (comarca != null) {
            long totalCamps = comarca.numerocamps; // Número total de campos en la comarca
            long ocupacioActual = calcularOcupacioCamps(comarca); // Ocupación actual de campos en la comarca

            if (ocupacioActual + ncamps > totalCamps) {
                // Si la ocupación total después de agregar los nuevos campos excede el total de campos disponibles
                renderJSON("{\"message\": \"Error: No es pot ocupar més camps dels disponibles (" + totalCamps + ") en la comarca " + ncomarca + "\"}");
                return;
            }

            Agricultor agricultor = Agricultor.find("byUsuariAndNcomarca", session.get("usuari"), comarca).first();
            if (agricultor != null) {
                agricultor.ncamps = ncamps;
                agricultor.save();
                renderJSON("{\"message\": \"S'han ocupat " + ncamps + " camps a la comarca " + ncomarca + "\"}");
            } else {
                renderJSON("{\"message\": \"Agricultor no trobat a la comarca\"}");
            }
        } else {
            renderJSON("{\"message\": \"Comarca no trobada\"}");
        }
    }

    public static void getComarcaDetails(String ncomarca) {
        Comarca comarca = Comarca.find("byNcomarca", ncomarca).first();
        if (comarca != null) {
            long numAgricultors = Agricultor.count("byNcomarca", comarca);
            long numCamps = comarca.numerocamps;
            //long ocupacioCamps = calcularOcupacioCamps(comarca);

            System.out.println("Comarca: " + ncomarca);
            System.out.println("Num Agricultors: " + numAgricultors);
            System.out.println("Num Camps: " + numCamps);
            //System.out.println("Ocupacio Camps: " + ocupacioCamps);

            renderJSON(new ComarcaDetails(ncomarca, numAgricultors, numCamps));
        } else {
            notFound("Comarca not found");
        }
    }

    public static class ComarcaDetails {
        public String ncomarca;
        public long numAgricultors;
        public long numCamps;
        public long ocupacioCamps;

        public ComarcaDetails(String ncomarca, long numAgricultors, long numCamps) {
            this.ncomarca = ncomarca;
            this.numAgricultors = numAgricultors;
            this.numCamps = numCamps;
            //this.ocupacioCamps = ocupacioCamps;
        }
    }

    public static long calcularOcupacioCamps(Comarca comarca) {
        Long ocupacioCamps = Agricultor.find("select sum(ncamps) from Agricultor where ncomarca = ?", comarca).first();
        System.out.println("Ocupacio Camps calculat: " + ocupacioCamps); // Agrega un mensaje de depuración
        return ocupacioCamps != null ? ocupacioCamps : 0;
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