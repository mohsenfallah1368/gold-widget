package com.mohsenfallah.goldwidget;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
public class MainActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b); TextView t=new TextView(this); t.setText("طلا و ارز TGJU\n\nبرای افزودن ویجت:\nروی صفحه اصلی لمس طولانی → ویجت‌ها → طلا و ارز TGJU"); t.setTextSize(18); t.setPadding(40,60,40,40); t.setTextDirection(TextView.TEXT_DIRECTION_ANY_RTL); setContentView(t);}
}