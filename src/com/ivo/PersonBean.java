package com.ivo;


public class PersonBean implements java.io.Serializable{


private static String name;
private int age;
public String company;
private boolean trrr;



public void setAge(int age){
this.age=age;
}

public int getAge(int age){
return age;
}

public void setCompany(String company){
this.company=company;
}

public String getCompany(String company){
return company;
}

public void setTrrr(boolean trrr){
this.trrr=trrr;
}

public boolean getTrrr(boolean trrr){
return trrr;
}

  public String getName(){
  return name;
  }
  public void setName(String name){
  this.name=name;
  }
}
