package com.ww.optimize;

public interface TextProvider {
	
	/**
	 * 使用接口  便于扩展   需要使用的 mode 只需  实现它   提供  显示的内容  和 id
	 * @return
	 */

	String getUniqueKey();
	
	String getContentValue();

}
