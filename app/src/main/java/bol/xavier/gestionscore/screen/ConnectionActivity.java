package bol.xavier.gestionscore.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bol.xavier.gestionscore.R;
import bol.xavier.gestionscore.asynchronoustask.Asyn_newAccount;
import bol.xavier.gestionscore.asynchronoustask.Async_connection;

public class ConnectionActivity extends AppCompatActivity {
    private EditText et_login, et_password, et_new_login, et_new_password, et_confirm_password;
    private Button but_logon, but_register, but_save;

    private View.OnClickListener click_butLogon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Async_connection(ConnectionActivity.this).execute(et_login.getText().toString(), et_password.getText().toString());
        }
    };

    private View.OnClickListener click_butRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tv_error = (TextView)findViewById(R.id.tv_error);
            if(et_new_login.getVisibility() == View.INVISIBLE) {
                et_new_login.setVisibility(View.VISIBLE);
                et_new_password.setVisibility(View.VISIBLE);
                et_confirm_password.setVisibility(View.VISIBLE);
                but_save.setVisibility(View.VISIBLE);
            }
            else {
                et_new_login.setVisibility(View.INVISIBLE);
                et_new_password.setVisibility(View.INVISIBLE);
                et_confirm_password.setVisibility(View.INVISIBLE);
                but_save.setVisibility(View.INVISIBLE);
            }
        }
    };
    private View.OnClickListener click_butSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(et_new_login.getText().toString().equals("") && et_new_password.getText().toString().equals("")){
                TextView tv_error = (TextView)findViewById(R.id.tv_error);
                tv_error.setText("Erreur \"confirmer mot de passe\" est différent du champs \"nouveau mot de passe\"");
            }
            else {
                new Asyn_newAccount(ConnectionActivity.this).execute(et_new_login.getText().toString(), et_new_password.getText().toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        et_login = (EditText)findViewById(R.id.et_login);
        et_password = (EditText) findViewById(R.id.et_password);
        et_new_login = (EditText)findViewById(R.id.et_newLogin);
        et_new_password = (EditText)findViewById(R.id.et_newPassword);
        et_confirm_password = (EditText)findViewById(R.id.et_confirmPassword);

        but_logon = (Button)findViewById(R.id.but_logon);
        but_register = (Button)findViewById(R.id.but_register);
        but_save = (Button)findViewById(R.id.but_save);

        but_logon.setOnClickListener(click_butLogon);

        but_register.setOnClickListener(click_butRegister);

        but_save.setOnClickListener(click_butSave);
    }

    //Reprend le résultat de la tâche asynchrone "Async_connection"
    public void populate_connexion (int[] res) {
        TextView tv_error = (TextView)findViewById(R.id.tv_error);
        switch (res[0])
        {
            case 0:
                Intent intent = new Intent(ConnectionActivity.this, HomepageActivity.class);
                intent.putExtra("iduser", res[1]);
                startActivity(intent);
                break;
            case 100:
                tv_error.setText(R.string.connect_code_100);
                break;
            case 110:
                tv_error.setText(R.string.creer_compte_code_110);
                break;
            case 200:
                tv_error.setText(R.string.connect_code_200);
                break;
            case 404:
                tv_error.setText(R.string.error404);
                break;
            case 1000:
                tv_error.setText(R.string.connect_code_1000);
                break;
            default:
                tv_error.setText(R.string.connect_code_2000);
        }
    }

    //Reprend le résultat de la tâche asynchrone "Async_newAccount"
    public void populate_newAccount(int[] res){
        TextView tv_error = (TextView)findViewById(R.id.tv_error);
        switch (res[0])
        {
            case 0:
                Intent intent = new Intent(ConnectionActivity.this, HomepageActivity.class);
                intent.putExtra("iduser", res[1]);
                startActivity(intent);
                break;
            case 100:
                tv_error.setText(R.string.creer_compte_code_100);
                break;
            case 110:
                tv_error.setText(R.string.creer_compte_code_110);
                break;
            case 200:
                tv_error.setText(R.string.creer_compte_code_200);
                break;
            case 404:
                tv_error.setText(R.string.error404);
            case 1000:
                tv_error.setText(R.string.creer_compte_code_1000);
                break;
            default:
                tv_error.setText(res[0] + R.string.creer_compte_code_2000);
        }
    }
}
