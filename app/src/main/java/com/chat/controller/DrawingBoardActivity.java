package com.chat.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.chat.View.Draw;
import com.example.myapplication.R;
import com.gc.materialdesign.views.Slider;
import com.github.mummyding.colorpickerdialog.ColorPickerDialog;
import com.github.mummyding.colorpickerdialog.OnColorChangedListener;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DrawingBoardActivity  extends AppCompatActivity {
    Draw draw;
    int color;
    int width =1;
    Slider slider;
    int [] colors = new int[]{Color.YELLOW,Color.BLACK,Color.BLUE,Color.GRAY,
            Color.GREEN,Color.CYAN,Color.RED,Color.DKGRAY, Color.LTGRAY,Color.MAGENTA,
            Color.rgb(100,22,33),Color.rgb(82,182,2), Color.rgb(122,32,12),Color.rgb(82,12,2),
            Color.rgb(89,23,200),Color.rgb(13,222,23), Color.rgb(222,22,2),Color.rgb(2,22,222)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        color= Color.BLACK;
        width=1;
        setContentView(R.layout.drawingboard);
        draw = new Draw(DrawingBoardActivity.this,color,width);
        FilterMenuLayout filterMenuLayout=findViewById(R.id.filter_menu);
        //新建一个Draw类
        //将界面设为Draw类
        RelativeLayout fl = (RelativeLayout) findViewById(R.id.layout_ral);
        attachMenu1(filterMenuLayout);
        fl.addView(draw); //添加Draw类
        filterMenuLayout.bringToFront();
    }
    private FilterMenu attachMenu1(FilterMenuLayout layout){
        return new FilterMenu.Builder(this)
                .addItem(R.drawable.ic_drawboard)
                .addItem(R.drawable.ic_drawpen)
                .addItem(R.drawable.ic_circle)
                .addItem(R.drawable.ic_line)
                .attach(layout)
                .withListener(listener)
                .build();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            if(position==0)
            {
                ColorPickerDialog dialog =
                        // Constructor,the first argv is Context,second one is the colors you want to add
                        new ColorPickerDialog(DrawingBoardActivity.this,colors)
                                // Optional, if you want the dialog dismissed after picking,set it to true,otherwise
                                // false. default true
                                .setDismissAfterClick(false)
                                // Optional, Dialog's title,default "Theme"
                                .setTitle("Custom Theme")
                                //Optional, current checked color
                                .setCheckedColor(Color.BLACK)
                                .setOnColorChangedListener(new OnColorChangedListener() {
                                    @Override
                                    public void onColorChanged(int newColor) {
                                        // do something here
                                        color=newColor;
                                        draw.setPaintColor(color);
                                    }})
                                // build Dialog,argv means width count of Dialog,default value is 4 if you use build()
                                // without argv
                                .build(6)
                                .show();
            }
            if(position==1)
            {
                SweetAlertDialog dialog=new SweetAlertDialog(DrawingBoardActivity.this,SweetAlertDialog.SEEK_TYPE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setOnSweetSeekBarChangeListener(new SweetAlertDialog.OnSweetSeekBarChangeListener() {
                    @Override
                    public void onSeekBarChanged(int range) {
                        width=range+1;
                        draw.setPaintWidth(width);
                    }
                });
                dialog.show();

            }
            if(position==2)
            {
                draw.setType(2);
            }
            if(position==3)
            {
                draw.setType(1);
            }
        }

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };
}

