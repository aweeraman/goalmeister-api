package com.goalmeister.services;

import com.goalmeister.data.impl.DaoFactory;

public abstract class AbstractResource {

	protected DaoFactory dao = DaoFactory.getInstance();

}
