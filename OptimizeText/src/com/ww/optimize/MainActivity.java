package com.ww.optimize;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	/**
	 * Created  2015/9/20
	 * @author ww
	 */
	
	private ListView mListview;
	
	private StaticLayoutManager mLayoutManager;
	
	private List<Student> mStudentList = new ArrayList<Student>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DisplayUtils.init(this);
		mListview = (ListView) findViewById(android.R.id.list);
		mLayoutManager = new StaticLayoutManager(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		createData();
		loadData();
		//隔两秒 再显示  加载内容 是需要时间的
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				showData();
			}
		}, 2000);
	}
	
	private void createData() {
		for (int i = 0; i < 10; i++) {
			Student student = new Student();
			student.setId(String.valueOf(i%3));
			student.setContent(readFile(String.valueOf(i%3)));
			mStudentList.add(student);
		}
	}

	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mLayoutManager.initLayout(mStudentList, 12);//显示的数据 和 行数
			}
		}).start();
	}
	
	private void showData() {
		TextAdapter adapter = new TextAdapter(this, mStudentList, mLayoutManager);
		mListview.setAdapter(adapter);
	}

	/**
	 * 读取 assets 中的文件
	 * @param fileName
	 * @return
	 */
	private String readFile(String fileName) {
		try {
			InputStream is = getAssets().open(fileName+".txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			return new String(buffer);
		} catch (IOException e) {
			return "读取错误...";
		}
	}
	
	Handler handler = new Handler();
	
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("温馨提示").setMessage("你打开了一个对话框").setIcon(R.drawable.ic_launcher);;
		builder.show();
	}

}
