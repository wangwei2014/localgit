package com.ww.optimize;

public class Student implements TextProvider{
	
	private String id;
	
	private String content;
	
	public Student(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getUniqueKey() {
		return id;
	}

	@Override
	public String getContentValue() {
		return content;
	}

}
