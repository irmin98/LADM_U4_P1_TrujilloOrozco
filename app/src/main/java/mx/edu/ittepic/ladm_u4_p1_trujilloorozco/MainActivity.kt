package mx.edu.ittepic.ladm_u4_p1_trujilloorozco

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var siLecturaLlamadas=2
    var siEnvioSMS =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_CALL_LOG),siLecturaLlamadas)
        }

        button.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.SEND_SMS),siEnvioSMS)
            }else{
                Monitorearllamada()
            }



        }

    }

    private fun Monitorearllamada() {
        var resultado =""
        var num =""
        var tam=0
        var cursorllamadas = contentResolver.query(
            Uri.parse("content://call_log/calls"),
            null,null,null,null
        )


        if(cursorllamadas!!.moveToFirst()){
            do {
                if(cursorllamadas.getString(cursorllamadas.getColumnIndex(CallLog.Calls.TYPE)).equals("3")){
                var nombre = cursorllamadas.getString(cursorllamadas.getColumnIndex(CallLog.Calls.CACHED_NAME))
                var numero = cursorllamadas.getString(cursorllamadas.getColumnIndex(CallLog.Calls.NUMBER))
                var tipo = cursorllamadas.getString(cursorllamadas.getColumnIndex(CallLog.Calls.TYPE))
                var fecha = cursorllamadas.getString(cursorllamadas.getColumnIndex(CallLog.Calls.DATE))
                var duracion = cursorllamadas.getString(cursorllamadas.getColumnIndex(CallLog.Calls.DURATION))

                resultado = "NOMBRE: "+nombre+"\nNUMERO: "+numero + "\nTIPO: "+tipo+"\nFECHA :"+fecha+"\nDURACION :"+duracion+"Seg"+"\n-----------\n"
                num=numero
                    tam += 1

                    }
            }while (cursorllamadas.moveToNext())
        }


        if(num.equals("3231182662")){
            resultado=resultado +"\nLLAMADA NO DESEADA"
            envioSMSMalo()
        }
        if(num.equals("3231030836")){
            resultado=resultado +"\nLLAMADA SI DESEADA"
            envioSMSBueno()
        }

        textView.setText(resultado)
        setTitle(tam.toString())
    }

    private fun envioSMSBueno() {
        SmsManager.getDefault().sendTextMessage("3231030836",null,"EN CUANTO PUEDA TE LLAMO IRENE",null,null)
        Toast.makeText(this,"SE ENVIO EL SMS A IRENE ", Toast.LENGTH_LONG).show()
    }
    private fun envioSMSMalo() {
        SmsManager.getDefault().sendTextMessage("3231182662",null,"NO ME ENFADES",null,null)
        Toast.makeText(this,"SE ENVIO EL SMS A ARYN ", Toast.LENGTH_LONG).show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == siLecturaLlamadas) {
            setTitle("PERMISO OTORGADO")
        }
        if (requestCode == siEnvioSMS) {
            setTitle("PERMISO OTORGADO")
        }
    }

}
