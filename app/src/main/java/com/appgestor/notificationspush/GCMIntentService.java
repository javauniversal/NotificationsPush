package com.appgestor.notificationspush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMBaseIntentService;

import java.util.HashMap;
import java.util.Map;

public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService(){
        super("notificationspush-1020");
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String msg = intent.getExtras().getString("msg");
        Log.d("GCM", "Mensaje: " + msg);
        notificarMensaje(context, msg);
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.d("GCM", "Error: " + errorId);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        Log.d("GCM", "onRegistered: Registrado OK.");
        //En este punto debeis obtener el usuario donde lo tengais guardado.
        //Si no teneis un sistema de login y los usuarios son anónimos podeis simplemente almacenar el regId
        String usuario = "german";

        registrarUsuario(usuario, regId);
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        Log.d("GCM", "onUnregistered: Desregistrado OK.");
    }

    private void registrarUsuario(final String username, final String regId){

        String url = String.format("http://172.20.32.75/notificaciones");
        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "usersave");
                params.put("username", username);
                params.put("gcmcode", regId);
                return params;
            }
        };
        rq.add(jsonRequest);
    }


    private void notificarMensaje(Context context, String msg){

        String notificationService = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(notificationService);

        //Configuramos la notificación
        int icono = R.mipmap.ic_launcher;
        CharSequence estado = "Has recibido un nuevo mensaje";
        long hora = System.currentTimeMillis();

        Notification notification = new Notification(icono, estado, hora);
        long[] vibrate = {100,100,200,300};
        notification.vibrate = vibrate;

        //Configuramos el Intent
        Context contexto = context.getApplicationContext();
        CharSequence titulo = "Nombre app - nuevo Mensaje";
        CharSequence descripcion = msg;

        Intent intent = new Intent(contexto, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("update", 1);
        intent.putExtra("android.intent.extra.INTENT", b);

        PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, intent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        notification.setLatestEventInfo(contexto, titulo, descripcion, contIntent);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1, notification);
    }
}
