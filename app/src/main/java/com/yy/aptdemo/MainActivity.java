package com.yy.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.yy.lib_annotation.AptTest;
import com.yy.lib_annotation.ReadAnnotation;

@ReadAnnotation("test")
@AptTest("this is apt from annotation")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Class clz = MainActivity.class;
        ReadAnnotation read = (ReadAnnotation) clz.getAnnotation(ReadAnnotation.class);//拿到布局文件id
        TextView textview=findViewById(R.id.txt);
        textview.setText(read.value());

        //
        textview.setOnClickListener(view -> Toast.makeText(MainActivity.this,new APTTest().getString(),Toast.LENGTH_SHORT).show());
    }
}
