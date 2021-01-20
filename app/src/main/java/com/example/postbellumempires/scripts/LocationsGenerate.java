package com.example.postbellumempires.scripts;

import com.example.postbellumempires.enums.PlaceType;
import com.example.postbellumempires.gameobjects.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LocationsGenerate {
    public static void generatepoints() {
        DatabaseReference LocRef = FirebaseDatabase.getInstance().getReference("places");

        List<Place> places = new ArrayList<>();

        places.add(new Place("place_01","Cafe do C5",38.755909936449385, -9.156900988293799, PlaceType.CAFE));
        places.add(new Place("place_02", "Horta FCUL", 38.755761637420115, -9.156730799678046, PlaceType.PARK));
        places.add(new Place("place_03", "Cobertura Ecológica",38.75610780372063, -9.156741528508004, PlaceType.PARK));
        places.add(new Place("place_04", "FCUL Edificio C5", 38.756083807236365, -9.156948554934361, PlaceType.UNIVERSITY));
        places.add(new Place("place_05", "Bar do C2 ATFCUL", 38.755558348761205, -9.15646373299391, PlaceType.CAFE));
        places.add(new Place("place_06", "100 Montaditos", 38.75594308071998, -9.154713869911246, PlaceType.RESTAURANT));
        places.add(new Place("place_07", "Cafe do C8", 38.757072689123106, -9.156804863926233, PlaceType.CAFE));
        places.add(new Place("place_08", "Museu de Lisboa - Palácio Pimenta", 38.758565898331824, -9.156421360026592, PlaceType.MUSEUM));
        places.add(new Place("place_09", "McDonalds do Campo Grande", 38.75697971813253, -9.154194139510112, PlaceType.RESTAURANT));
        places.add(new Place("place_10", "Jardim Mario Soares", 38.756595020487836, -9.153303098343821, PlaceType.PARK));
        places.add(new Place("place_11", "Farmácia Holon",38.76021624217015, -9.157295750204318, PlaceType.PHARMACY));
        places.add(new Place("place_12", "Metro do Campo Grande", 38.76017644627023, -9.15791136267965, PlaceType.SUBWAY));
        places.add(new Place("place_13", "Metro da Cidade Universitaria", 38.75192679137703, -9.158628882649577, PlaceType.SUBWAY));
        places.add(new Place("place_14", "Faculdade de Ciencias da Universidade de Lisboa", 38.75655971918818, -9.155244567291167, PlaceType.UNIVERSITY));
        places.add(new Place("place_15", "Faculdade de Letras da UL", 38.75351859464719, -9.157584905811975,PlaceType.UNIVERSITY));
        places.add(new Place("place_16", "Faculdade de Direito", 38.75249889356581, -9.15703046102091, PlaceType.UNIVERSITY));
        places.add(new Place("place_17", "Faculdade de Psicologia da UL", 38.75277251937659, -9.155464317316886, PlaceType.UNIVERSITY));
        places.add(new Place("place_18", "Reitoria da UL", 38.75291037068239, -9.15763140315286, PlaceType.UNIVERSITY));
        places.add(new Place("place_19", "Hospital de Santa Maria", 38.74999805660176, -9.160602540174821, PlaceType.HOSPITAL));
        places.add(new Place("place_20", "Biblioteca do C4", 38.75618059908566, -9.156500801708988, PlaceType.LIBRARY));
        places.add(new Place("place_21", "Minicampus", 38.75576288065118, -9.155182344655014, PlaceType.CAFE));
        places.add(new Place("place_22", "Torre do Tombo", 38.754241056816824, -9.156415567293474, PlaceType.LIBRARY));
        places.add(new Place("place_23", "Estatua Jose Pinto Peixoto", 38.7572472563661, -9.155701478670403, PlaceType.MONUMENT));
        places.add(new Place("place_24", "Estatua de D. Joao I", 38.75870404182994, -9.155411297625461, PlaceType.MONUMENT));
        places.add(new Place("place_25", "Chimarrao Campo Grande", 38.759772219959096, -9.156221456624031, PlaceType.RESTAURANT));
        places.add(new Place("place_26", "Estatua de D. Pedro V", 38.753900395769584, -9.157449175824128, PlaceType.MONUMENT));
        places.add(new Place("place_27", "Frankie Hotdogs", 38.753113349761605, -9.153043252579163, PlaceType.RESTAURANT));
        places.add(new Place("place_28", "Casa do Lago", 38.75576419486976, -9.152864181282187, PlaceType.RESTAURANT));
        places.add(new Place("place_29", "Cafe Concerto", 38.757936859237724, -9.154910665031153, PlaceType.CAFE));
        places.add(new Place("place_30", "Estatua D. Afonso Henriques", 38.758742270738885, -9.155260206348824, PlaceType.MONUMENT));
        places.add(new Place("place_31", "Museu Bordalo Pinheiro", 38.75883946608418, -9.15419410775792, PlaceType.MUSEUM));
        places.add(new Place("place_32", "Cantina Velha", 38.75143760336401, -9.15952910725786, PlaceType.RESTAURANT));
        places.add(new Place("place_33", "Parque Canino", 38.75499227311018, -9.15245838857129, PlaceType.PARK));
        places.add(new Place("place_34", "Universidade Lusofona de Humanidades e Tecnologias", 38.75806276543812, -9.15314554257811, PlaceType.UNIVERSITY));
        places.add(new Place("place_35", "Instituto Superior de Gestao", 38.75930598284728, -9.154562120460016, PlaceType.UNIVERSITY));
        places.add(new Place("place_36", "Restaurante - A Horta", 38.75504630991542, -9.153737080376864, PlaceType.RESTAURANT));
        places.add(new Place("place_37", "Jardim Bordallo Pinheiro", 38.75812706257513, -9.15620111386058, PlaceType.PARK));
        places.add(new Place("place_38", "Edificio C3", 38.7564904634016, -9.155539889355437, PlaceType.UNIVERSITY));
        places.add(new Place("place_39", "Edificio C4", 38.75625126099931, -9.156225549157462, PlaceType.UNIVERSITY));
        places.add(new Place("place_40", "Edificio C2", 38.755987433890624, -9.156040601447705, PlaceType.UNIVERSITY));
        places.add(new Place("place_41", "Edificio C1", 38.7565784052597, -9.15646913882397, PlaceType.UNIVERSITY));
        places.add(new Place("place_42", "Edificio C6", 38.75581330745722, -9.157623934326557, PlaceType.UNIVERSITY));
        places.add(new Place("place_43", "Edificio C8", 38.757179924673935, -9.156994661010476, PlaceType.UNIVERSITY));


        for (Place p : places) {
            LocRef.child(p.getId()).setValue(p);
        }
    }
}
