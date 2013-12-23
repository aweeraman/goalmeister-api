package com.goalmeister.data.impl;

import com.goalmeister.data.ApplicationDao;
import com.goalmeister.data.GoalDao;
import com.goalmeister.data.UserDao;

public class DaoFactory {

  private static DaoFactory daoFactory = new DaoFactory();

  private static UserDao userDao;
  private static GoalDao goalDao;
  private static ApplicationDao applicationDao;

  private DaoFactory() {
    userDao = new UserDaoImpl();
    goalDao = new GoalDaoImpl();
    applicationDao = new ApplicationDaoImpl();
  }

  public static DaoFactory getInstance() {
    return daoFactory;
  }

  public UserDao getUserDao() {
    return userDao;
  }

  public GoalDao getGoalDao() {
    return goalDao;
  }

  public ApplicationDao getApplicationDao() {
    return applicationDao;
  }
}
