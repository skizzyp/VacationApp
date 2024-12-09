package com.example.d308.database;

import android.app.Application;

import com.example.d308.dao.ExcursionDao;
import com.example.d308.dao.VacationDao;
import com.example.d308.entity.Excursion;
import com.example.d308.entity.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private VacationDao mVacationDao;
    private ExcursionDao mExcursionDao;
    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabaseBuilder db =VacationDatabaseBuilder.getDatabase(application);
        mExcursionDao=db.excursionDao();
        mVacationDao=db.vacationDao();
    }
    public List<Vacation>getAllVacations(){
        databaseExecutor.execute(()->{
            mAllVacations=mVacationDao.getAllVacations();
        });

        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllVacations;
    }
    public void insert(Vacation vacation) {
        databaseExecutor.execute(()->{
            mVacationDao.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(()->{
            mVacationDao.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(()->{
            mVacationDao.update(vacation);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public List<Excursion> getAllExcursions() {
        databaseExecutor.execute(()->{
            mAllExcursions = mExcursionDao.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcursions;
    }
    public void insert(Excursion excursion) {
        databaseExecutor.execute(()->{
            mExcursionDao.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Excursion excursion) {
        databaseExecutor.execute(()->{
            mExcursionDao.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(()->{
            mExcursionDao.update(excursion);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
