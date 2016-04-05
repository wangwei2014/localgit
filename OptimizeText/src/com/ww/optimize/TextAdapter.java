package com.ww.optimize;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TextAdapter extends BaseAdapter{
	
	private Context mContext;

	private List<Student> mStudentList;

	private StaticLayoutManager mLayoutManager;
	
	public TextAdapter(Context context,List<Student> list,StaticLayoutManager manager){
		mContext = context;
		mStudentList = list;
		mLayoutManager = manager;
	}

	@Override
	public int getCount() {
		return mStudentList.size();
	}

	@Override
	public Object getItem(int position) {
		return mStudentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		View view = View.inflate(mContext, R.layout.item, null);
		StaticLayoutView tv_content = (StaticLayoutView)view.findViewById(R.id.static_content);
		tv_content.setLayout(mLayoutManager.getLayout(mStudentList.get(position).getId()));
		return view;
	}

}
