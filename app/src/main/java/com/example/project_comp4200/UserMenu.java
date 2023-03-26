package com.example.project_comp4200;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserMenu extends AppCompatActivity {

    Button createSurveyBtn;
    Button viewSurveysBtn;
    List<surveyEntity> surveys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        createSurveyBtn = findViewById(R.id.createSurveyBtn);

        createSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createSurveyIntent = new Intent(UserMenu.this, CreateSurvey.class);
                startActivity(createSurveyIntent);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = (user.getUid());

            new FetchSurveysTask().execute(userId);

        }


    }

    private class FetchSurveysTask extends AsyncTask<String, Void, List<surveyEntity>> {

        @Override
        protected List<surveyEntity> doInBackground(String... params) {
            String userId = params[0];
            AppDatabase appDatabase = AppController.getInstance().getAppDatabase();
            return appDatabase.surveyDAO().getSurveysByUserId(userId);
        }

        @Override
        protected void onPostExecute(List<surveyEntity> surveys) {
            LinearLayout yourSurveysLayout = findViewById(R.id.yourSurveysLayout);

            // Populate the scroll view with the surveys
            for (surveyEntity survey : surveys) {
                SurveyCard surveyCard = new SurveyCard(UserMenu.this);
                surveyCard.setSurveyTitle(survey.getTitle());
                surveyCard.setSurveyDescription(survey.getDescription());
                yourSurveysLayout.addView(surveyCard);
            }
        }
    }
}