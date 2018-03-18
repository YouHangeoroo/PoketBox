package floaterr.PoketBoxbykim;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;



public class FloatingWindow extends Service {

    WindowManager wm;
    RelativeLayout ll;
    int Clickcount = 0;


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

        ll = new RelativeLayout(this);
        final RelativeLayout.LayoutParams layoutParameteres = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 400);
        ll.setLayoutParams(layoutParameteres);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                200, 750, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.CENTER | Gravity.CENTER;
        parameters.x = 0;
        parameters.y = 0;

        final ImageView iv0 = new ImageView(this);
        Bitmap orgImage0 = BitmapFactory.decodeResource(getResources(), R.mipmap.gogo);
        Bitmap resize0 = Bitmap.createScaledBitmap(orgImage0, 200, 100, true);
        iv0.setImageBitmap(resize0);
        ll.addView(iv0);


        final ImageView iv = new ImageView(this);
        Bitmap orgImage = BitmapFactory.decodeResource(getResources(), R.mipmap.gift);
        Bitmap resize = Bitmap.createScaledBitmap(orgImage, 200, 200, true);
        iv.setImageBitmap(resize);
        iv.setY(100);
        ll.addView(iv);

        final ImageView iv2 = new ImageView(this);
        Bitmap orgImage2 = BitmapFactory.decodeResource(getResources(), R.mipmap.memo);
        Bitmap resize2 = Bitmap.createScaledBitmap(orgImage2, 150, 150, true);
        iv2.setImageBitmap(resize2);
        iv2.setX(25);
        iv2.setY(300);
        ll.addView(iv2);
        iv2.setVisibility(View.INVISIBLE);

        final ImageView iv3 = new ImageView(this);
        Bitmap orgImage3 = BitmapFactory.decodeResource(getResources(), R.mipmap.camera);
        Bitmap resize3 = Bitmap.createScaledBitmap(orgImage3, 150, 150, true);
        iv3.setImageBitmap(resize3);
        iv3.setX(26);
        iv3.setY(450);
        ll.addView(iv3);
        iv3.setVisibility(View.INVISIBLE);

        final ImageView iv4 = new ImageView(this);
        Bitmap orgImage4 = BitmapFactory.decodeResource(getResources(), R.mipmap.close);
        Bitmap resize4 = Bitmap.createScaledBitmap(orgImage4, 150, 150, true);
        iv4.setImageBitmap(resize4);
        iv4.setX(25);
        iv4.setY(600);
        ll.addView(iv4);
        iv4.setVisibility(View.INVISIBLE);



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

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Clickcount == 0){
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);


                    Clickcount = 1;
                }

                else{
                    iv2.setVisibility(View.INVISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    iv4.setVisibility(View.INVISIBLE);

                    Clickcount = 0;
                }

            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                startService(new Intent(FloatingWindow.this,Memo.class));
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String folder = "포켓박스"; // 폴더 이름

                try {
                    // 현재 날짜로 파일을 저장하기
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    // 년월일시분초
                    Date currentTime_1 = new Date();
                    String dateString = formatter.format(currentTime_1);
                    File sdCardPath = Environment.getExternalStorageDirectory();
                    File dirs = new File(Environment.getExternalStorageDirectory(), folder);

                    if (!dirs.exists()) { // 원하는 경로에 폴더가 있는지 확인
                        dirs.mkdirs(); // Test 폴더 생성
                        Log.d("CAMERA_TEST", "Directory Created");
                    }

                    ll.buildDrawingCache();
                    Bitmap captureView = ll.getDrawingCache();
                    FileOutputStream fos;
                    String save;

                    try {
                        save = sdCardPath.getPath() + "/" + folder + "/" + dateString + ".jpg";
                        // 저장 경로
                        fos = new FileOutputStream(save);
                        captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos); // 캡쳐

                        // 미디어 스캐너를 통해 모든 미디어 리스트를 갱신시킨다.
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), dateString + ".jpg 저장",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("Screen", "" + e.toString());
                }
            }
        });


        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(ll);
                stopSelf();
                System.exit(0);
            }
        });




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

}