package com.wicare.wistorm.toolkit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 
 * WSql 数据库接口
 * 
 * @author c
 * @date 2015-11-10
 */
public class WSql extends OrmLiteSqliteOpenHelper {
	private static int DATABASE_VERSION = 1;// 数据库版本，若有表更新，需升级版本
	private static final String DB_NAME = "wistrom.db";// 数据库名字
	private static Set<Class> tableSet = new HashSet<Class>();// 存放数据库表映射的实体类

	
	/**
	 * 实例化并创建数据库表
	 * @param context
	 */
	public WSql(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	
	/**
	 * 
	 *addTable 在实例化之前，添加表对应的实体类
	 *@param cls
	 */
	public static void addTable(Class cls) {
		tableSet.add(cls);
	}

	/**
	 * 
	 *addTables 在实例化之前，添加表对应的实体类们
	 *@param tableList 
	 */
	public static void addTables(ArrayList<Class> tableList) {
		for (Class cls : tableList) {
			addTable(cls);
		}
	}

	/**
	 * 创建表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(SQLiteDatabase sql, ConnectionSource connectionSource) {
		try {

			for (Class cls : tableSet) {
				TableUtils.createTable(connectionSource, cls);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 升级数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int arg2, int arg3) {
		try {

			for (Class cls : tableSet) {
				TableUtils.dropTable(connectionSource, cls, true);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		onCreate(sqliteDatabase, connectionSource);

	}

	/**
	 * 设置数据库版本
	 */
	public static void updateVersion(int version) {
		DATABASE_VERSION = version;
	}

}
