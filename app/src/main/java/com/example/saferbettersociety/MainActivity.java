package com.example.saferbettersociety;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageView;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Animation topanim, toponeanim, toptwoanim;
    ImageView imageView;
    TextView textView;
    TextView textviewone;


    private static int SPLASH_SCREEN_TIMEOUT=5000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        topanim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        toponeanim = AnimationUtils.loadAnimation(this,R.anim.toponeanim);
        toptwoanim = AnimationUtils.loadAnimation(this,R.anim.toptwoanim);


        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.txt);
        textviewone = findViewById(R.id.txt1);


        imageView.setAnimation(topanim);
        textView.setAnimation(toponeanim);
        textviewone.setAnimation(toptwoanim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.this==null){
                    return;
                }
                Intent intent=new Intent(getApplicationContext(),Landing_Page.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIMEOUT);



    }
}