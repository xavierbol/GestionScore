package bol.xavier.gestionscore.asynchronoustask;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bol.xavier.gestionscore.screen.HomepageActivity;

public class Async_AfficherUsers extends AsyncTask<String, Void, String[]> {
    private HomepageActivity screen = null;

    public Async_AfficherUsers(HomepageActivity s)
    {
        screen = s;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String[] result = new String[2];

        try {
            URL url = new URL("http://xavier.16mb.com/ProjetAndroid/lister_pseudos.php");

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
                        listJeux = recupUsers(json_reader);
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
        screen.populate_listUser(result);
    }

    private String recupUsers(JsonReader jsonReader) throws IOException {
        String nom_champs, nom_users, users;

        nom_champs = nom_users = users = "";

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            nom_champs = jsonReader.nextName();
            if(nom_champs.equals("pseudo")) {
                nom_users = jsonReader.nextString();
                if(!users.contains(nom_users))
                    users = users + nom_users + ";";
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();

        return users;
    }
}
