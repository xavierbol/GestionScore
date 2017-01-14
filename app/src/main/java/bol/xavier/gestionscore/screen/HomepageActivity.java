package bol.xavier.gestionscore.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bol.xavier.gestionscore.R;
import bol.xavier.gestionscore.asynchronoustask.Async_AfficherUsers;
import bol.xavier.gestionscore.asynchronoustask.Async_game_bestPlayer;

public class HomepageActivity extends AppCompatActivity {
    private final int NUM_REQUETE = 3;
    private int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Button btn_ajoutscore, btn_topGame, btn_listGame, btn_listUser;

        btn_ajoutscore = (Button)findViewById(R.id.btn_ajoutScore);
        btn_topGame = (Button)findViewById(R.id.btn_topGame);
        btn_listGame = (Button)findViewById(R.id.btn_listGame);
        btn_listUser = (Button)findViewById(R.id.btn_listUser);


        if(id_user == 0) {
            Intent intent_recup = getIntent();
            id_user = intent_recup.getIntExtra("iduser", 0);
        }

        btn_ajoutscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, AjoutScoreActivity.class);
                intent.putExtra("id_user", id_user);
                startActivityForResult(intent, NUM_REQUETE);
            }
        });

        btn_topGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, TopGameActivity.class);
                startActivity(intent);
            }
        });

        btn_listGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_game_bestPlayer(HomepageActivity.this).execute();
                //Intent intent = new Intent(HomepageActivity.this, ListGameActivity.class);
                //startActivity(intent);
            }
        });

        btn_listUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_AfficherUsers(HomepageActivity.this).execute();
            }
        });
    }

    protected void onActivityResult(int num_requete, int code_retour, Intent intent) {
        if (num_requete == NUM_REQUETE)
        {
            if (code_retour == RESULT_OK)
            {
                id_user = intent.getIntExtra("id_user", 0);
            }
        }
    }

    public void populate_listUser(String[] result) {
        int code = Integer.parseInt(result[0]);
        String listUsers = result[1];
        TextView tv_error = (TextView)findViewById(R.id.tv_errorHome);

        switch (code)
        {
            case 0:
                Intent intent = new Intent(HomepageActivity.this, ListUserActivity.class);
                intent.putExtra("listUsers", listUsers);
                startActivity(intent);
                break;
            case 300:
                tv_error.setText(R.string.list_pseudo_code_500);
                break;
            case 1000:
                tv_error.setText(R.string.list_pseudo_code_1000);
                break;
            default:
                tv_error.setText(R.string.list_pseudo_code_2000);
        }
    }

    public void populate_afficherJeux(String[] result)
    {
        TextView tv_error = (TextView)findViewById(R.id.tv_errorHome);
        int code;
        String jeux;

        code = Integer.parseInt(result[0]);
        jeux = result[1];

        switch (code){
            case 0:
                if(!jeux.equals(""))
                {
                    Intent intent = new Intent(HomepageActivity.this, WizardActivity.class);
                    intent.putExtra("listjeux", jeux);
                    startActivityForResult(intent, NUM_REQUETE);
                }
                else
                {
                    tv_error.setText("Il existe encore aucun jeu dans la base de données");
                }
                break;
            case 500:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_500);
                break;
            case 1000:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_1000);
                break;
            default:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_2000);
        }
    }

    public void  populate_game_bestPlayer(String[] result)
    {
        TextView tv_error = (TextView)findViewById(R.id.tv_errorHome);
        int code;
        String jeux;

        code = Integer.parseInt(result[0]);
        jeux = result[1];

        switch (code){
            case 0:
                if(!jeux.equals(""))
                {
                    Intent intent = new Intent(HomepageActivity.this, ListGameActivity.class);
                    intent.putExtra("listjeux", jeux);
                    startActivityForResult(intent, NUM_REQUETE);
                }
                else
                {
                    tv_error.setText("Il existe encore aucun jeu dans la base de données");
                }
                break;
            case 500:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_500);
                break;
            case 1000:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_1000);
                break;
            default:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_2000);
        }
    }
}
