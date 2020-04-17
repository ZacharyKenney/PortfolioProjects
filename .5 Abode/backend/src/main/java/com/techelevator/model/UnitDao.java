package com.techelevator.model;

import java.util.List;

public interface UnitDao { 

//     public List<Unit> getAllUnits();

//     public List<Unit> getUnitsByProperty();

//     public List<User> getRentersByUnit();

    public List<Unit> getUnitByRenter(int userId);

    public List<User> getRenterByUnit(int unitId);

 }