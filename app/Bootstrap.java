import play.test.*;
import play.jobs.*;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Load default data if the database is empty
        // Cargar datos de prueba si la base de datos está vacía
        if(Comarca.count() == 0) {
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
        }
    }

}