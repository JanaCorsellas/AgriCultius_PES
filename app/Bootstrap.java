import play.test.*;
import play.jobs.*;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

    /*public void doJob() {
        // Load default data if the database is empty
        // Cargar datos de prueba si la base de datos está vacía
        if(Agricultor.count() == 0 && Comarca.count() == 0) {
            // Cargar datos de prueba para Agricultor
            Agricultor a1 = new Agricultor("Jana", "Corsellas", 21, "jc", "11", null).save();
            Agricultor a2 = new Agricultor("Ivan", "Garcia", 21, "ivanga", "22", null).save();

            // Cargar datos de prueba para Comarca
            Comarca c1 = new Comarca("La Conca de Barberà", 16713).save();
            Comarca c2 = new Comarca("Garrotxa", 7325).save();

            // Asignar agricultores a comarcas (solo como ejemplo, adapta según tus necesidades)
            a1.setComarca(c1);
            a2.setComarca(c2);

            a1.save();
            a2.save();
        }
    }*/

}