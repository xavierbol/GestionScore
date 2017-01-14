package bol.xavier.gestionscore.asynchronoustask;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bol.xavier.gestionscore.screen.ConnectionActivity;

public class Async_connection extends AsyncTask<String, Void, int[]> {
    private ConnectionActivity screen = null;

    public Async_connection(ConnectionActivity s)
    {
        screen = s;
    }
    @Override
    protected int[] doInBackground(String... params) {
        int[] result = new int[2];
        String pseudo = params[0];
        String mdp = params[1];

        try {
            URL url = new URL("http://xavier.16mb.com/ProjetAndroid/se_connecter.php");

            HttpURLConnection connexion = (HttpURLConnection)url.openConnection();
            connexion.setRequestMethod("POST");

            //Envoi des paramètres pseudo et mdp pour pouvoir faire la création d'un compte utilisateur
            OutputStream os = connexion.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String parametres_post = "pseudo=" + pseudo + "&mdp=" + mdp;
            writer.write(parametres_post);
            writer.flush();
            writer.close();
            os.close();

            connexion.connect();
            int responseCode = connexion.getResponseCode();
            if (responseCode == 200) {
                //Récupération du JSON du RPC
                String nom_champs = "";
                int code = -1, id = 0;
                InputStream inputStream = connexion.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                /* Script modifié car on a besoin de l'id de l'utilisateur pour que l'utilisateur puisse ajouter un score
                 * Du coup le code mis en commentaire est inutile vu que le script retournera un fichier JSON avec le code et id
                 * comme pour le script "creer_compte.php"
                 * Scanner scanner = new Scanner(inputStream).useDelimiter(" ");
                while (scanner.hasNext())
                {
                    result = scanner.next();
                }*/

                JsonReader json_reader = new JsonReader(inputStreamReader);
                json_reader.beginObject();
                while (json_reader.hasNext()) {
                    nom_champs = json_reader.nextName();
                    if (nom_champs.equals("code"))
                        code = json_reader.nextInt();
                    else
                        id = json_reader.nextInt();
                }

                if (code != 0) {
                    result[0] = code;
                } else {
                    result[0] = code;
                    result[1] = id;
                }
            }
            else
            {
                result[0] = 404;
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
    protected void onPostExecute(int[] result) {
        screen.populate_connexion(result);
    }
}
