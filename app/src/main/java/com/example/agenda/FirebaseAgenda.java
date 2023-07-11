package com.example.agenda;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAgenda extends android.app.Application { //extiende de andriod.app... para tener privilegios y ejecutarse primero

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //habilita la persistencia de datos, evita que los datos se pierdan al realizar acciones si conexiión a internet, detecta los ultmos cambios realizados y los coloca en la bd si no estan ya
    }
}
