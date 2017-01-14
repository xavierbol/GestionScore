package bol.xavier.gestionscore.screen;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bol.xavier.gestionscore.R;
import bol.xavier.gestionscore.asynchronoustask.Async_top10;

public class AfficherTop10Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_top10);

        Intent intent_recup = getIntent();
        String listPseudos = intent_recup.getStringExtra("pseudo");
        String listScores = intent_recup.getStringExtra("score");

        String[] tabPseudos = listPseudos.split(";");
        String[] tabScores = listScores.split(";");

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.activity_afficher_top10);
        LinearLayout layout_ligne;
        TextView tv_pos, tv_pseudo, tv_score, tv_title_pos, tv_title_pseudo, tv_title_score;
        View v_dividerRow, v_dividerColumn;

        tv_title_pos = (TextView)findViewById(R.id.tv_pos);
        tv_title_pseudo = (TextView)findViewById(R.id.tv_pseudo);
        tv_title_score = (TextView)findViewById(R.id.tv_score);

        v_dividerRow = (View)findViewById(R.id.row_divider);
        v_dividerColumn = (View)findViewById(R.id.column_divider);

        if(tabPseudos.length > 0)
        {
            for (int i = 0; i < tabPseudos.length; i++) {
                View divider_row = new View(this);
                View divider_column1 = new View(this);
                View divider_column2 = new View(this);
                layout_ligne = new LinearLayout(this);
                tv_pos = new TextView(this);
                tv_pseudo = new TextView(this);
                tv_score = new TextView(this);

                divider_row.setLayoutParams(v_dividerRow.getLayoutParams());
                divider_row.setBackgroundColor(Color.BLACK);
                divider_column1.setLayoutParams(v_dividerColumn.getLayoutParams());
                divider_column1.setBackgroundColor(Color.BLACK);
                divider_column2.setLayoutParams(v_dividerColumn.getLayoutParams());
                divider_column2.setBackgroundColor(Color.BLACK);
                tv_pos.setLayoutParams(tv_title_pos.getLayoutParams());
                tv_pseudo.setLayoutParams(tv_title_pseudo.getLayoutParams());
                tv_score.setLayoutParams(tv_title_score.getLayoutParams());

                tv_pos.setText(Integer.toString(i+1));
                tv_pseudo.setText(tabPseudos[i]);
                tv_score.setText(tabScores[i]);

                layout_ligne.addView(tv_pos);
                layout_ligne.addView(divider_column1);
                layout_ligne.addView(tv_pseudo);
                layout_ligne.addView(divider_column2);
                layout_ligne.addView(tv_score);
                linearLayout.addView(layout_ligne);
                linearLayout.addView(divider_row);
            }
        }
    }
}
