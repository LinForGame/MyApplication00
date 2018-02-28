package com.example.why.myapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    String rootPath;
    File currentParent;

    File[] currentFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = findViewById(R.id.list);
        textView = findViewById(R.id.path);
        rootPath = Environment.getExternalStorageDirectory().getPath();
        Log.d("---","rootPath:"+rootPath);
        File root = new File(rootPath);
        Log.d("---","SD:"+Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
        if(root.exists()){
            currentParent = root;
            currentFiles = root.listFiles();
            Log.d("---",root.getName());
            if(currentFiles == null){
                Log.d("---","currentFiles == null");
                return;
            }
            if(currentFiles.length == 0 ){
                Log.d("---","currentFiles.length == 0");
                return;
            }
            inflateListView(currentFiles);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentFiles[position].isFile()){
                    return;
                }
                File[] temp = currentFiles[position].listFiles();
                if(temp == null){
                    Toast.makeText(MainActivity.this,"当前路径不可访问",Toast.LENGTH_SHORT).show();

                }else{
                    currentParent = currentFiles[position];
                    currentFiles = temp;
                    inflateListView(currentFiles);
                }
            }
        });
        Button parent = findViewById(R.id.parent);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(currentParent.getParentFile()!=null){
                    Toast.makeText(MainActivity.this,currentParent.getParentFile().getCanonicalPath(),Toast.LENGTH_SHORT).show();
                 //   if(!currentParent.getCanonicalPath().equals(rootPath)){
                        currentParent = currentParent.getParentFile();
                        currentFiles = currentParent.listFiles();
                        inflateListView(currentFiles);
                  //  }
                }else{
                        Toast.makeText(MainActivity.this,"currentParent.getParentFile()==null",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void inflateListView(File[] files){
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        if(files==null)
        {
            Log.d("---","files==null");
            Toast.makeText(MainActivity.this,"files==null",Toast.LENGTH_SHORT).show();
            return;
        }
        if(files.length==0)
        {
            Log.d("---","files.length==0");
            Toast.makeText(MainActivity.this,"files.length==0",Toast.LENGTH_SHORT).show();
            //return;
        }else {

        }

        for(int i = 0; i < files.length; i++){
            Map<String ,Object> listItem  = new HashMap<String ,Object>();
            if(files[i].isDirectory()){
                listItem.put("icon",R.drawable.folder);
            }else{
                listItem.put("icon",R.drawable.file);
            }
            listItem.put("fileName",files[i].getName());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,
                R.layout.line,new String[]{"icon","fileName"},new int[]{R.id.icon,R.id.file_name});
        listView.setAdapter(simpleAdapter);

        try{
            textView.setText(currentParent.getCanonicalPath());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
