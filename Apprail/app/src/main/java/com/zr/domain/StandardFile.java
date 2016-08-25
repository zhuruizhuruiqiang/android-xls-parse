package com.zr.domain;

import java.util.ArrayList;
/*
 * 标准库文件domain
 * 	private File file_catalogue;//标准库文件名称
 * private String file_standardfile_version;//文件的版本号
 * private String file_editor;//文件的修订人
 * 
 * private ArrayList<Project> file_projects;//文件中的projects
 */
public class StandardFile {
	private String file_catalogue;//标准库文件目录
	private String file_version;//文件的版本号
	private String file_editor;//文件的修订人

	private ArrayList<Project> file_projects;//文件中的projects	
	/*
	 * 构造函数，根据文件名创建标准库文件
	 */
	public StandardFile(String file_catalogue)
	{
		this.file_catalogue=file_catalogue;
		this.file_version=new String("");
		this.file_editor=new String("");
		this.file_projects=new ArrayList<Project>();
	}
	/*
	 * 文件中包含的项目
	 */	
	public ArrayList<Project> getFile_projects() {
		return file_projects;
	}

	public void setFile_projects(ArrayList<Project> file_projects) {
		this.file_projects = file_projects;
	}

	public void addFile_project(Project file_project) {
		this.file_projects.add(file_project);
	}
	/*
	 * 标准库文件基本信息
	 */
	public String getFile_catalogue() {
		return file_catalogue;
	}

	/*public void setFile_catalogue(String file_catalogue) {
		this.file_catalogue = file_catalogue;
	}*/

	public String getFile_version() {
		return file_version;
	}

	public void setFile_version(String file_version) {
		this.file_version = file_version;
	}

	public String getFile_editor() {
		return file_editor;
	}

	public void setFile_editor(String file_editor) {
		this.file_editor = file_editor;
	}
}
