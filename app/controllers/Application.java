package controllers;

import play.*;
import play.mvc.*;
import play.db.jpa.JPA;

import java.util.*;
import java.lang.String;

import models.*;
import javax.persistence.Query;

public class Application extends Controller {

    //Funcions pels formularis

    public static void index() {
        renderTemplate("Application/Agricultius.html");
    }

    public static void Inici(){
        List<Comarca> c = Comarca.findAll();
        renderArgs.put("comarques", c);
        renderTemplate("Application/Inici.html");
    }

    public void Convidat(){
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

    public static void LoginFormulari (){
        renderTemplate("Application/LoginFormulari.html");
    }

    public static void showComarquesPage() {
        List<Comarca> comarques = Comarca.findAll();
        if (comarques.isEmpty()) {
            renderText("No comarques found");
        } else {
            renderTemplate("/Application/Inici.html");
        }
    }

    //MÈTODES

    //Mètode per donar d'alta/registrar un agricultor
    public static void RegistrarAgricultor(String nom, String cognom, int edat, String usuari, String contrasenya, String comarca) {
        Agricultor a = Agricultor.find("byNomAndCognomAndEdatAndUsuariAndContrasenya", nom, cognom, edat, usuari, contrasenya).first();

        if (a == null) {
            Comarca nc = Comarca.find("byNcomarca", comarca).first();
            new Agricultor(nom, cognom, edat, usuari, contrasenya, nc).save();
            session.put("usuari", usuari);
            session.put("nom", nom);
            session.put("cognom", cognom);
            session.put("edat", edat);
            session.put("comarca", comarca);

            renderTemplate("Application/Agricultius.html");
        } else {
            renderText("Aquest agricultor ja existeix");
        }
    }

    //Mètode per donar d'alta/registrar un agricultor en Android
    public static void RegistrarAgricultorAndroid(String nom, String cognom, int edat, String usuari, String contrasenya, String comarca) {
        Agricultor a = Agricultor.find("byNomAndCognomAndEdatAndUsuariAndContrasenya", nom, cognom, edat, usuari, contrasenya).first();

        if (a == null) {
            Comarca nc = Comarca.find("byNcomarca", comarca).first();
            new Agricultor(nom, cognom, edat, usuari, contrasenya, nc).save();
            session.put("usuari", usuari);
            session.put("nom", nom);
            session.put("cognom", cognom);
            session.put("edat", edat);
            session.put("comarca", comarca);

            renderJSON("{\"status\": \"success\"}");
        } else {
            renderJSON("{\"status\": \"error\", \"message\": \"Aquest agricultor ja existeix\"}");
        }
    }

    //mètode per donar de baixa/eliminar un agricultor
    public static void eliminarAgricultor() {
        String usuari = session.get("usuari");
        if (usuari == null) {
            flash.error("Error: No has iniciat sessió.");
            Application.LoginFormulari();
            return;
        }

        Agricultor agricultor = Agricultor.find("byUsuari", usuari).first();
        if (agricultor != null) {
            agricultor.delete();
            session.clear(); // Limpiar la sesión si el usuario actual se ha eliminado
            //flash.success("L'agricultor ha estat eliminat correctament.");
            Application.Agricultius(); // Redirigir a la pantalla inicial
        } else {
            flash.error("Error: L'agricultor no s'ha trobat.");
            Application.Principal(); // Redirigir a la página principal del usuario
        }
    }

    //Mètode per iniciar sessió
    public static void Login(String usuari, String contrasenya) {
        Agricultor a = Agricultor.find("byUsuariAndContrasenya", usuari, contrasenya).first();
        if (a == null) {
            renderText("Agricultor perdut pel camp, cal que trobis el nord (Registra't!)");
        } else {
            session.put("usuari", usuari);
            session.put("nom", a.nom); // Asegúrate de tener los campos nom y cognom en la clase Agricultor
            session.put("cognom", a.cognom);
            session.put("edat", a.edat);
            session.put("comarca", a.ncomarca.ncomarca);
            renderArgs.put("c", a.ncomarca.ncomarca);
            renderTemplate("/Application/Principal.html");
        }
    }

    //Mètode per iniciar sessió en Android
    public static void LoginAndroid(String usuari, String contrasenya) {
        Agricultor a = Agricultor.find("byUsuariAndContrasenya", usuari, contrasenya).first();

        if (a == null) {
            renderJSON("{\"status\": \"error\", \"message\": \"Aquest agricultor no existeix\"}");
        } else {
            session.put("usuari", usuari);
            session.put("nom", a.nom); // Asegúrate de tener los campos nom y cognom en la clase Agricultor
            session.put("cognom", a.cognom);
            session.put("edat", a.edat);
            session.put("comarca", a.ncomarca.ncomarca);
            renderArgs.put("c", a.ncomarca.ncomarca);

            renderJSON("{\"status\": \"success\"}");
        }
    }

    //Mètode per tancar sessió
    public static void logout() {
        session.clear(); // Limpiar la sesión
        Application.Agricultius(); // Redirigir a la página de inicio
    }

    //Mètode per editar les dades del perfil
    public static void editProfile() {
        String usuari = session.get("usuari");
        if (usuari == null) {
            flash.error("Cal iniciar sessió per editar el perfil.");
            Application.LoginFormulari();
        }
        List<Comarca> comarques = Comarca.findAll();
        render(comarques);
    }

    //Mètode per actualitzar el perfil un cop editat
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

    //Mètode perquè un agricultor pugui ocupar camps a la seva comarca
    public static void ocuparCamps(String ncomarca, int ncamps) {
        Comarca comarca = Comarca.find("byNcomarca", ncomarca).first();
        if (comarca != null) {
            long totalCamps = comarca.numerocamps; // Número total de campos en la comarca
            long ocupacioActual = calcularOcupacioCamps(comarca); // Ocupación actual de campos en la comarca

            if (ocupacioActual + ncamps > totalCamps) {
                // Si la ocupación total después de agregar los nuevos campos excede el total de campos disponibles
                renderText("Error: No es pot ocupar més camps dels disponibles (" + totalCamps + ") en la comarca " + ncomarca);
                return;
            }

            Agricultor agricultor = Agricultor.find("byUsuariAndNcomarca", session.get("usuari"), comarca).first();
            if (agricultor != null) {
                agricultor.ncamps = ncamps;
                agricultor.save();
                renderText("S'han ocupat " + ncamps + " camps a la comarca " + ncomarca );
            } else {
                renderText("Agricultor no trobat a la comarca");
            }
        } else {
            renderText("Comarca no trobada");
        }
    }

    //Mètode per obtenir els detalls d'una comarca
    public static void getComarcaDetails(String ncomarca) {
        Comarca comarca = Comarca.find("byNcomarca", ncomarca).first();
        if (comarca != null) {
            long numAgricultors = Agricultor.count("byNcomarca", comarca);
            long numCamps = comarca.numerocamps;
            long ocupacioCamps = calcularOcupacioCamps(comarca);
            renderJSON(new ComarcaDetails(ncomarca, numAgricultors, numCamps, ocupacioCamps));
        } else {
            notFound("Comarca not found");
        }
    }

    //Mètode per calcular l'ocupació de camps d'una comarca
    public static long calcularOcupacioCamps(Comarca comarca) {
        List<Agricultor> agricultors = Agricultor.find("byNcomarca", comarca).fetch();
        Long ocupacioCamps = 0L;
        for(Agricultor a: agricultors) {
            ocupacioCamps = ocupacioCamps + a.ncamps;
        }
        return ocupacioCamps != null ? ocupacioCamps : 0;
    }

    public static class ComarcaDetails {
        public String ncomarca;
        public long numAgricultors;
        public long numCamps;
        public long ocupacioCamps;

        public ComarcaDetails(String ncomarca, long numAgricultors, long numCamps, long ocupacioCamps) {
            this.ncomarca = ncomarca;
            this.numAgricultors = numAgricultors;
            this.numCamps = numCamps;
            this.ocupacioCamps = ocupacioCamps;
        }
    }

    //Mètode per obtenir la llista dels usuaris registrats
    public static void getUsuaris() {
        List<Agricultor> agricultores = Agricultor.findAll();
        List<String> nombresUsuarios = new ArrayList<>();
        for (Agricultor agricultor : agricultores) {
            nombresUsuarios.add(agricultor.usuari);
        }
        renderJSON(nombresUsuarios);
    }


    //Mètodes extres, sense ús

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

    //Servei que llista les comarques que tenen mínim x Agricultors
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