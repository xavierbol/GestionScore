package bol.xavier.gestionscore.asynchronoustask;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import bol.xavier.gestionscore.screen.AjoutScoreActivity;

public class Async_addScore extends AsyncTask<String, Void, int[]> {
    private AjoutScoreActivity screen = null;

    public Async_addScore(AjoutScoreActivity s)
    {
        screen = s;
    }
    @Override
    protected int[] doInBackground(String... params) {
        String jeu;
        int[] result = new int[2];
        int score, id_user;

        score = Integer.parseInt(params[0]);
        jeu = params[1];
        jeu = jeu.replaceAll("\\s", "%20");
        id_user = Integer.parseInt(params[2]);

        try {
            int code = -1;
            URL url = new URL("http://xavier.16mb.com/ProjetAndroid/ajouter_score.php?score=" + score + "&jeu=" + jeu + "&id_utilisateur=" + id_user);

            HttpURLConnection connexion = (HttpURLConnection)url.openConnection();
            connexion.setRequestMethod("GET");

            connexion.connect();
            int responseCode = connexion.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = connexion.getInputStream();
                Scanner scanner = new Scanner(inputStream).useDelimiter(" ");

                while (scanner.hasNext())
                {
                    code = Integer.parseInt(scanner.next());
                }
                scanner.close();
            }
            else
            {
                code = 404;
            }

            if(code == 0)
            {
                result[0] = code;
                result[1] = id_user;
            }
            else
                result[0] = code;
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
        screen.populate(result);
    }
}
