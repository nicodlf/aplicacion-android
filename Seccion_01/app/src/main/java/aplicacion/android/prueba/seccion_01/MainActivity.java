package aplicacion.android.prueba.seccion_01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button btn;
    private final String Greeter="hello from the other side";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Forzar y cargar icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_myicon);

        btn=(Button) findViewById(R.id.buttonMain);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acceder al segundo activity y mandarle string -> intent explicito
                Intent intent= new Intent(MainActivity.this, SecondActivity.class);
                //"greeter" -> id  Greeter-> valor String
                intent.putExtra("greeter",Greeter);
                //Ejecuta
                startActivity(intent);
            }
        });

    }



}
