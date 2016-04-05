package com.ww.optimize;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.util.LruCache;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

/**
 * @author ww 
 * 对Textview的绘制进行优化，优化的核心是 预渲染
 * 由于 Textview 在setText时  需要 进行 计算  以及 一些处理 如果 在表情或文字非常多时 
 * 性能 很不好，可能绘制的时间  超过16ms，从而 会丢帧
 */

public class StaticLayoutManager {

	private LruCache<String,StaticLayout> mCache = new LruCache<>(MAX_ITEM);

	public static final int TEXT_SIZE_DP = 14; //字体大小

	public static final int MAX_ITEM = 100;//最多缓存100条    如果使用listview时  加载更多 可以调用trimToSize 该方法 将进行LRU算法 处理

	private Context mContext;

	public StaticLayoutManager(Context context) {
		mContext = context;
	}

	/**
	 * listview Item 中使用 直接绘制text内容，避开使用Textview的setText
	 * @param width 显示宽度
	 */
	public void initLayout(List<? extends TextProvider> list, int width, int tagLines) {
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.density = mContext.getResources().getDisplayMetrics().density;
		textPaint.setTextSize(DisplayUtils.sp2px(TEXT_SIZE_DP));
		
		Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
		Canvas dummyCanvas = new Canvas();
		
		int length = list.size();
		if (length == 0 || list == null) {
			return;
		}
		synchronized (list) {
			for (int i = 0; i < length; i++) {
				String uniqueKey = list.get(i).getUniqueKey();
				String contentValue = list.get(i).getContentValue();
				if (!TextUtils.isEmpty(contentValue) && mCache.get(uniqueKey) == null) {
					if (tagLines != -1) {
						contentValue = controlLine(contentValue, width, tagLines, textPaint);
					}
					//此处 可对contentValue进行处理，有自己的表情包   或者 需要转码   等等 （如不进行处理  有表情是 将是使用系统表情） 如纯文字 则可不做任何处理
					StaticLayout layout = new StaticLayout(contentValue, textPaint, width, alignment, 1.0f, 0f, true);
					layout.draw(dummyCanvas);
					mCache.put(uniqueKey, layout);
				}
			}
		}
	}

	public void initLayout(List<? extends TextProvider> list,int tagLines) {
		initLayout(list, DisplayUtils.getDisplayWidth()-DisplayUtils.dp2px(13), tagLines);
	}

	public StaticLayout getLayout(String key) {
		return mCache.get(key);
	}

	public void remove(String key){
		mCache.remove(key);
	}

	public void trimToSize(int maxSize){
		mCache.trimToSize(maxSize);
	}

	/**
	 * 控制行数
	 * @param content 要显示的内容
	 * @param width  显示的宽度
	 * @param tagLines 需要显示的行数
	 * @param textPaint 
	 * @return
	 */
	private String controlLine(String content, int width, int tagLines, TextPaint textPaint){
		ArrayList<String> strToList = contentToList(content);
		StringBuilder builder = new StringBuilder();
		int line = 0;
		int tempIndex = 0;
		int enableLine = 0;
		for (String singleStr : strToList) {
			if(line == tagLines){
				builder.setLength(builder.length() - 4);
				builder.append("...");
				break;
			}
			if(singleStr.equals("\n\r")||singleStr.equals("\n")){
				line++;
				builder.append(singleStr);
			}else{
				if(enableLine <= 20){
					builder.append(singleStr);
					enableLine++;
				}else{
					int strWidth = (int) Layout.getDesiredWidth(builder.toString(),tempIndex, builder.length(),textPaint);
					if(strWidth < width){
						enableLine++;
						builder.append(singleStr);
					}else{
						enableLine = 0;
						line++;
						tempIndex = builder.length() -2;//原本为 length - 1，防止每次 计算每行的时候 会多一个文字。减2是为了缓冲。减小误差。可视为容错处理
						builder.append(singleStr);
					}
				}
			}
		}
		return builder.toString();
	}

	
	private ArrayList<String> contentToList(String str) {
		ArrayList<String> list = new ArrayList<>();
		String[] s = str.split("");
		for (String c : s) {
			list.add(c);
		}
		return list;
	}

}