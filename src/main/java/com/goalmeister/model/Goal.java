package com.goalmeister.model;

import org.mongojack.ObjectId;

public class Goal {

  @ObjectId
  public String _id;
  public String tenant;
  public String title;
  public String description;

}
