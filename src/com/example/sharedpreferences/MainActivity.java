package com.example.sharedpreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Build;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity {

	ListView listView;
	public final static String Tag = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/**!
		 * 保存按钮
		 * */
		Button button = (Button) findViewById(R.id.clickButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText editText = (EditText) findViewById(R.id.inputEditText);
				//获取EditText中的内容
				String inputStr = editText.getText().toString();
				//向sp内添加数据
				addContent(inputStr);
				//刷新tableView上的数据
				reloadTableView(tableDataWith(allContent()));
			}
		});
		
		/**!
		 * 清空按钮
		 * */
		Button clearButton = (Button)findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearContent();
			}
		});

		//初始化tableView
		initTableView(tableDataWith(allContent()));
	}

	/**!
	 * 根据sp中保存的数据转换成我们tableView需要的数据源
	 * */
	public ArrayList<HashMap<String, String>> tableDataWith(ArrayList<String> arr){
		
		ArrayList<HashMap<String, String>> relist = new ArrayList<HashMap<String,String>>();
		
		for (int i = 0; i < arr.size(); i++) {
			String message = arr.get(i);
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("title", "第" + String.valueOf(i) + "条:");
			hashMap.put("message", message);
			relist.add(hashMap);
		}
		
		return relist;
	}
	
	/**!
	 * 获取当前sp内所有的数据
	 * */
	public ArrayList<String> allContent() {
		//初始化一个arrayList
		ArrayList<String> list = new ArrayList<String>();
		//获取sp实例
		SharedPreferences sp = getSp();
		//初始化一个起始value
		int startValue = 0;
		//循环获取出所有的值
		while (null != sp.getString("info" + String.valueOf(startValue), null)) {
			String str = sp.getString("info" + String.valueOf(startValue), null);
			list.add(str);
			startValue ++;
		}
		return list;
	}

	/**!
	 * 向sharedPreferences内添加需要保存的内容
	 * */
	public void addContent(String content) {
		//获取当前已经保存的数量
		int currentValue = allContent().size();
		//获取sp实例
		SharedPreferences sp = getSp();
		//获取sp的Editor
		Editor editor = sp.edit();
		//根据当前已经保存的数量+1向sp内添加数据
		editor.putString("info" + String.valueOf(currentValue), content);
		//提交更新
		editor.commit();
	}
	
	public void clearContent(){
		SharedPreferences sp = getSp();
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
		reloadTableView(tableDataWith(allContent()));
	}

	/**!
	 * sharedPreferences 实例
	 * */
	public SharedPreferences getSp() {
		return getSharedPreferences("content", Context.MODE_WORLD_READABLE);
	}
	
	/**!
	 * 初始化tableView
	 * */
	public void initTableView(ArrayList<HashMap<String, String>> arr){
		listView = (ListView)findViewById(R.id.listView);
		reloadTableView(arr);
	}
	
	/**!
	 * 重新加载SimpleAdapter,刷新tableView
	 * notifyDataSetChanged() 动态刷新数据
	 * */
	public void reloadTableView(ArrayList<HashMap<String, String>> arr){
		//根据数据加载adapter，然后刷新listView
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, arr, R.layout.user, new String[]{"title","message"}, new int[]{R.id.row_title,R.id.row_message});
		listView.setAdapter(simpleAdapter);
	}
}
