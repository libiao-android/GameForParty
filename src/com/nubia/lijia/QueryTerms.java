package com.nubia.lijia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nubia.gameforparty.R;

public class QueryTerms extends Activity implements View.OnClickListener{
	
	 private ListView mListView;//声明ListView控件
	 private String[] mTermContentList={"你的初恋是几岁?", "你的初恋对象是谁?","你的初吻是几岁在什么地方被什么人夺去的?", "大学一共挂过几门课？", "大学所有考试中，你考到最低的一门是什么课，考了几分？", 
				"你吻过几个人？", "在现场所有同学中，你看哪位异性同学最舒服？", "如果再给你一次机会，回到高中毕业那天，你最想对某一位异性说什么话？", "第一个喜欢的异性叫什么名字？", "你曾经意淫过在场的哪一位？如果过去没有的话，你现在会选哪一位作为YY对象？", "对梦中情人有什么要求（在一分钟内说出五条）"};
	 private String [] mNumberList={"1","2","3","4","5","6","7","8","9","10","11"};
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_term);
		
		findViewById(R.id.exitbtn).setOnClickListener(this);
		
		List<Map<String, Object>> listmap=new ArrayList<Map<String,Object>>();
        for(int i=0;i<mTermContentList.length;i++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("termContentList", mTermContentList[i]);
            map.put("numberList", mNumberList[i]);

            listmap.add(map);}
        mListView = (ListView) findViewById(R.id.termList);
       
        ItemAdapter adapter = new ItemAdapter(this,listmap);
        mListView.setAdapter(adapter);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exitbtn:
            finish();
            break;
		}
		
	}
	
	private final class  ListItemView{  
		public TextView numberView;  
		public TextView termcontentView;  
		}  
	
	public class ItemAdapter extends BaseAdapter{
		private List<Map<String, Object>> listItems;
		private LayoutInflater inflater;
		public ItemAdapter(Context context, List<Map<String , Object>> listItems){  
			this.listItems=listItems;
			inflater = LayoutInflater.from(context);
		}  

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return  listItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) { 
			ListItemView listItemView=null;
			if(convertView == null){
				listItemView = new ListItemView();
				convertView = inflater.inflate(R.layout.item, null);
				listItemView.numberView = (TextView) convertView.findViewById(R.id.number);  
				listItemView.termcontentView = (TextView) convertView.findViewById(R.id.termcontent); 
				//存放在标志里当作缓存使用  
				convertView.setTag(listItemView);  
				}
			else{  
				listItemView=(ListItemView)convertView.getTag();
			}  
	         listItemView.numberView.setText((String)listItems.get(position).get("numberList"));
	         listItemView.termcontentView.setText((String)listItems.get(position).get("termContentList"));
			return convertView;  

		} 
		
 
	}
	
}
