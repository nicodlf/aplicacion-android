package aplicacion.android.prueba.seccion_01;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextWeb;
    private EditText editTextMail;
    private EditText editTextMailComplete;
    private ImageButton imgBtnPhone;
    private ImageButton imgBtnPhone2;
    private ImageButton imgBtnWeb;
    private ImageButton imgBtnCamera;
    private ImageButton imgBtnContact;
    private ImageButton imgBtnMail;
    private ImageButton imgBtnMailComplete;



    //Codigo para permiso de nuevas versiones, se usa para identificar quien manda(camara o telefono, etc)
    private final int PHONE_CALL_CODE = 100;
    private final int PICTURE_FROM_CAMERA = 50;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //Activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextWeb = (EditText) findViewById(R.id.editTextWeb);
        editTextMail = (EditText) findViewById(R.id.editTextEmail);
        editTextMailComplete = (EditText) findViewById(R.id.editTextMailComplete);
        imgBtnPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        imgBtnPhone2 = (ImageButton) findViewById(R.id.imageButtonPhone2);
        imgBtnWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        imgBtnCamera = (ImageButton) findViewById(R.id.imageButtonCamera);
        imgBtnContact = (ImageButton) findViewById(R.id.imageButtonContact);
        imgBtnMail = (ImageButton) findViewById(R.id.imageButtonEmail);
        imgBtnMailComplete = (ImageButton) findViewById(R.id.imageButtonMailComplete);


        //Boton para la llamada
        imgBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhone.getText().toString();
                //Mas control
                if (phoneNumber != null && !phoneNumber.isEmpty() ) {
                    //Comprobar version actual de android que estamos corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        //Comprobar si ha aceptado, ha denegado o nunca se le ha preguntado
                        if(checkPermission(Manifest.permission.CALL_PHONE)){
                            //Ha aceptado
                            Intent i= new Intent(Intent.ACTION_CALL,Uri.parse("tel: "+phoneNumber));
                            if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                return;
                            }
                            startActivity(i);

                        } else{
                            //Ha denegado o es la primera vez que se le pregunta
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                //No se ha preguntado
                                //Comprobar para nuevas versiones
                                //Metodo asincrono-> espera la respuesta
                                 requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            } else {
                                //Ha denegado
                                Toast.makeText(ThirdActivity.this, "Por favor, habilite el permiso", Toast.LENGTH_SHORT).show();
                                //Para mostrar como se hace, habilitar permisos
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package: " + getPackageName()));
                                //Banderas se usa para volver a la tarea que se estaba haciendo
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        }
                    } else {
                        //Version vieja
                        OlderVersion(phoneNumber);
                    }
                }else{
                    Toast.makeText(ThirdActivity.this, "Ingrese un numero", Toast.LENGTH_SHORT).show();

                }
            }

            //Para versiones viejas, no se reutiliza (puede quedar aca)
            public void OlderVersion(String phoneNumber) {
                //Uri-> referencia / este intent es para llamadas
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phoneNumber));

                //Permiso verdadero -> se le pregunta del de llamar
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(ThirdActivity.this, "No se dio permiso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Boton para la direccion web
        imgBtnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editTextWeb.getText().toString();
                if(url != null && !url.isEmpty()){
                    Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
                    //Tambien se puede poner ->
                    //intentWeb.setAction(Intent.ACTION_VIEW);
                    //intentWeb.setData(Uri.parse("http://"+url))

                    startActivity(intentWeb);
                }
            }
        });

        //Boton para contactos
        imgBtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentContact = new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/people"));
                startActivity(intentContact);

            }
        });

        //Boton mail rapido
        imgBtnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextMail.getText().toString();

                Intent intentMailTo = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+email));
                startActivity(intentMailTo);
            }
        });

        //Boton mail completo
        imgBtnMailComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextMailComplete.getText().toString();


                Intent intentMailComplete=new Intent(Intent.ACTION_SEND,Uri.parse(email));//Quien manda
                intentMailComplete.setType("plain/text");//texto plano
                //Usar variables en vez de los string
                intentMailComplete.putExtra(Intent.EXTRA_SUBJECT,"Mail's title");//Titulo
                intentMailComplete.putExtra(Intent.EXTRA_TEXT,"Hi there, I love MyForm app");//Texto
                //Otros mails
                intentMailComplete.putExtra(Intent.EXTRA_EMAIL,new String[]{"nico.dlf1@gmail.com","nico.dlf@outlook.com"});//A quien se le manda
                startActivity(Intent.createChooser(intentMailComplete,"Elige cliente de correo"));//forzar pregunta con que abrir
               // startActivity(intentMailComplete);

            }
        });

        //Boton telefono 2, sin permisos requeridos
        imgBtnPhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPhone= new Intent(Intent.ACTION_DIAL,Uri.parse("tel: 6"));
                startActivity(intentPhone);
            }
        });

        //Camara

        imgBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera=new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intentCamera,PICTURE_FROM_CAMERA);
                //startActivity(intentCamera);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case PICTURE_FROM_CAMERA:
                if(resultCode == Activity.RESULT_OK ){
                    String result = data.toUri(0);
                    Toast.makeText(this,"Resultado: "+result,Toast.LENGTH_SHORT).show();
                    //aca se tendria que manipular result para lo que se necesite
                }else {

                    Toast.makeText(this,"Hubo un error con la foto, intenta nuevamente",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    //Metodo lo necesita para comprobar permisos nueva version, cualquier permiso
    //RequestCode es el PHONE_CALL_CODE
    //Si hay mas llamadas se usa codigo diferente, metodo se aplica varias veces
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Caso del telefono
        switch (requestCode) {
            case PHONE_CALL_CODE:
                //Se puede usar el mismo codigo para distintos permisos-> por eso array de permisos

                //Variables para comprobar
                String permission = permissions[0]; //Seria un for si hay mas permisos
                int result = grantResults[0]; //Resultado del usuario

                if (permission.equals(Manifest.permission.CALL_PHONE)) {

                    //Comprobar si ha sido aceptado o denegado la peticion de permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Concedió su permiso
                        String phoneNumber = editTextPhone.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phoneNumber));

                        //Comrpueba permiso call_phone fue aceptado , si es denegado devuelve return, sino continua
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        //Cuando da error -> alt+intro -> agregar permiso -> aparece lo de arriba
                        startActivity(intentCall);
                    }
                    else{
                        //No concedió su permiso
                        Toast.makeText(ThirdActivity.this,"No se dio permiso",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    //Comprueba si se tiene el permiso, puede ser usado en muchas partes
    //Comprueba que el usuario haya aceptado el permiso
    private boolean checkPermission(String permission){
        //Permiso que le mandamos
        int result = this.checkCallingOrSelfPermission(permission);
        //Permiso granted es un int igual a cero
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
