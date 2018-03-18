package floaterr.PoketBoxbykim;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;



public class Memo extends Service {

    WindowManager wm;
    TableLayout ll;
    private String id;
    public static final String KEY_MY_PREFERENCE = "my_preference";


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        return null;
    }


    @Override
    public void onCreate() {

              // TODO Auto-generated method stub
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        ll = new TableLayout(this);
        ll.setBackgroundColor(Color.YELLOW);
        TableLayout.LayoutParams layoutParameteres = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        ll.setBackgroundColor(Color.argb(190, 255, 255, 153));
        ll.setLayoutParams(layoutParameteres);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                1000, 1000, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.CENTER | Gravity.CENTER;
        parameters.x = 0;
        parameters.y = 0;


        final EditText edit = new EditText(this);
        edit.setTextColor(Color.BLACK);
        edit.setHint("내용을 입력하세요");
        ll.addView(edit);

        final Button stop = new Button(this);
        stop.setText("닫기");
        ll.addView(stop);

        final Button delete = new Button(this);
        delete.setText("내용지우기");
        ll.addView(delete);


        wm.addView(ll, parameters);



        ll.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = parameters;
            double x;
            double y;
            double pressedX;
            double pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        wm.updateViewLayout(ll, updatedParameters);

                    default:
                        break;
                }

                return false;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setText(null);

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edit.getText().toString();

                // 데이타를저장합니다.
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_MY_PREFERENCE, text);
                editor.commit();

                wm.removeView(ll);
                stopSelf();

            }
        });






    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();

    }
}

