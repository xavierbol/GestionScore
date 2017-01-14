package bol.xavier.gestionscore.asynchronoustask;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bol.xavier.gestionscore.screen.ListGameActivity;
import bol.xavier.gestionscore.screen.TopGameActivity;

public class Async_top10 extends AsyncTask<String, Void, String[]> {
    private Object screen = null;

    public Async_top10(TopGameActivity s)
    {
        screen = s;
    }

    public Async_top10(ListGameActivity s) { screen = s; }

    @Override
    protected String[] doInBackground(String... params) {
        String[] result = new String[2];
        String nom_jeu = params[0];
        nom_jeu = nom_jeu.replaceAll("\\s", "%20");

        try {
            URL url = new URL("http://xavier.16mb.com/ProjetAndroid/afficher_top.php?jeu=" + nom_jeu);

            HttpURLConnection connexion = (HttpURLConnection)url.openConnection();
            connexion.setRequestMethod("GET");

            connexion.connect();
            int responseCode = connexion.getResponseCode();
            if (responseCode == 200) {
                //Récupération du JSON du RPC
                String nom_champs, joueurs;
                int code = -1;

                nom_champs = joueurs = "";

                InputStream inputStream = connexion.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                JsonReader json_reader = new JsonReader(inputStreamReader);
                json_reader.beginObject();
                while (json_reader.hasNext()) {
                    nom_champs = json_reader.nextName();
                    if (nom_champs.equals("code"))
                        code = json_reader.nextInt();
                    else {
                        joueurs = recupJoueurs(json_reader);
                    }
                }
                json_reader.endObject();
                json_reader.close();

                result[0] = Integer.toString(code);
                if (code == 0) {
                    result[1] = joueurs;
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
        if(screen instanceof TopGameActivity)
            ((TopGameActivity)screen).populate_top10(result);
        else
            ((ListGameActivity)screen).populate_top10(result);
    }

    private String recupJoueurs(JsonReader jsonReader) throws IOException {
        String nom_champs, joueurs = "", pseudo;
        int score;

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                nom_champs = jsonReader.nextName();
                if(nom_champs.equals("pseudo")) {
                    pseudo = jsonReader.nextString();
                    joueurs = joueurs + pseudo + ",";
                }
                else{
                    score = jsonReader.nextInt();
                    joueurs = joueurs + Integer.toString(score) + ";";
                }
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();

        return joueurs;
    }
}