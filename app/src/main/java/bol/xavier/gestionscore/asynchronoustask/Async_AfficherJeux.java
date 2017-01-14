package bol.xavier.gestionscore.asynchronoustask;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bol.xavier.gestionscore.screen.AjoutScoreActivity;
import bol.xavier.gestionscore.screen.TopGameActivity;

public class Async_AfficherJeux extends AsyncTask<String, Void, String[]> {
    private Object screen = null;

    public Async_AfficherJeux(AjoutScoreActivity s)
    {
        screen = s;
    }

    public Async_AfficherJeux(TopGameActivity s) {screen = s;}

    @Override
    protected String[] doInBackground(String... params) {
        String[] result = new String[2];

        try {
            URL url = new URL("http://xavier.16mb.com/ProjetAndroid/lister_jeux.php");

            HttpURLConnection connexion = (HttpURLConnection)url.openConnection();

            connexion.connect();
            int responseCode = connexion.getResponseCode();
            if (responseCode == 200) {
                //Récupération du JSON du RPC
                String nom_champs = "";
                int code = -1;
                String listJeux = "";
                InputStream inputStream = connexion.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                JsonReader json_reader = new JsonReader(inputStreamReader);
                json_reader.beginObject();
                while (json_reader.hasNext()) {
                    nom_champs = json_reader.nextName();
                    if (nom_champs.equals("code")){
                        code = json_reader.nextInt();
                    }
                    else {
                        listJeux = recupJeux(json_reader);
                    }
                }
                json_reader.endObject();
                json_reader.close();

                if (code != 0) {
                    result[0] = Integer.toString(code);
                } else {
                    result[0] = Integer.toString(code);
                    result[1] = listJeux;
                }
            }
            else
            {
                result[0] = "404";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if(screen instanceof AjoutScoreActivity)
            ((AjoutScoreActivity)screen).populate_wizard(result);
        else
            ((TopGameActivity)screen).populate_wizard(result);
    }

    private String recupJeux(JsonReader jsonReader) throws IOException {
        String nom_champs, nom_jeux, jeux;

        nom_champs = nom_jeux = jeux = "";

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            nom_champs = jsonReader.nextName();
            if(nom_champs.equals("nom_jeu")) {
                nom_jeux = jsonReader.nextString();
                if(!jeux.contains(nom_jeux))
                    jeux = jeux + nom_jeux + ";";
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();

        return jeux;
    }
}
