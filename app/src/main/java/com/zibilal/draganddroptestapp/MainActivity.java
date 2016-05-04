package com.zibilal.draganddroptestapp;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.myimage1) TextView myImage1;
    @Bind(R.id.myimage2) TextView myImage2;
    @Bind(R.id.myimage3) TextView myImage3;
    @Bind(R.id.myimage4) TextView myImage4;

    @OnClick(R.id.myimage1) void onImage1() {
        Toast.makeText(this, "Image 1 is clicked!", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.myimage2) void onImage2() {
        Toast.makeText(this, "Image 2 is clicked!", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.myimage3) void onImage3() {
        Toast.makeText(this, "Image 3 is clicked!", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.myimage4) void onImage4() {
        Toast.makeText(this, "Image 4 is clicked!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        myImage1.setOnTouchListener(new MyTouchListener());
        myImage2.setOnTouchListener(new MyTouchListener());
        myImage3.setOnTouchListener(new MyTouchListener());
        myImage4.setOnTouchListener(new MyTouchListener());
        ButterKnife.findById(this, R.id.topleft).setOnDragListener(new MyDragListener(this));
        ButterKnife.findById(this, R.id.topright).setOnDragListener(new MyDragListener(this));
        ButterKnife.findById(this, R.id.bottomleft).setOnDragListener(new MyDragListener(this));
        ButterKnife.findById(this, R.id.bottomright).setOnDragListener(new MyDragListener(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class MyTouchListener implements View.OnTouchListener {
        private static final float MAX_X_MOVE = 70;
        private static final float MAX_Y_MOVE = 70;

        private float mX;
        private float mY;

        public MyTouchListener() {}

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(MainActivity.class.getSimpleName(), "X : " + mX);
                Log.d(MainActivity.class.getSimpleName(), "Y : " + mY);
                Log.d(MainActivity.class.getSimpleName(), "Current X : " + event.getX());
                Log.d(MainActivity.class.getSimpleName(), "Current Y : " + event.getY());
                if (Math.abs(event.getX() - mX) < MAX_X_MOVE || Math.abs(event.getY() - mY) < MAX_Y_MOVE) {
                    Log.d(MainActivity.class.getSimpleName(), "Here we go click event");
                    v.performClick();
                } else {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, shadowBuilder, v, 0);
                    v.setVisibility(View.INVISIBLE);

                    mX = event.getX();
                    mY = event.getY();
                }

                return true;
            }
            return false;
        }
    }

    static class MyDragListener implements View.OnDragListener {
        Context mContext;
        Drawable mEnterShape;
        Drawable mNormalShape;

        public MyDragListener(Context ctx) {
            mContext = ctx;
            mEnterShape = mContext.getResources().getDrawable(R.drawable.shape_droptarget);
            mNormalShape = mContext.getResources().getDrawable(R.drawable.shape);
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(mEnterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(mNormalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundDrawable(mNormalShape);
                default:
                    break;
            }
            return true;
        }
    }
}
