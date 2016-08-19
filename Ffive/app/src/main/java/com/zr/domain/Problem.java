package com.zr.domain;

public class Problem {
	public String problem_description;//存储问题描述
	public boolean problem_choose;//存储问题是否被选择
	
	public Problem()
	{
		problem_description="";
		problem_choose=false;
	}
	
	public Problem(Problem p)
	{
		problem_description="";
		problem_choose=false;
		
		setProblem_description(p.getProblem_description());
		setProblem_choose(p.isProblem_choose());
	}
	public String getProblem_description() {
		return problem_description;
	}

	public void setProblem_description(String problem_description) {
		this.problem_description = problem_description;
	}

	public boolean isProblem_choose() {
		return problem_choose;
	}

	public void setProblem_choose(boolean problem_choose) {
		this.problem_choose = problem_choose;
	}

}
