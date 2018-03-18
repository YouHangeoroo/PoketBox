package floaterr.PoketBoxbykim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import floaterr.PoketBoxbykim.R;

public class MainActivity extends Activity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.btn);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                startService(new Intent(MainActivity.this,FloatingWindow.class));
                onBackPressed();
            }
        });
    }
}