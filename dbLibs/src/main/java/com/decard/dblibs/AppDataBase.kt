package com.decard.dblibs

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.decard.dblibs.column.PeerBean
import com.decard.dblibs.column.PeerDao
import com.decard.dblibs.convert.Manager
import com.decard.dblibs.convert.ManagerDao
import com.decard.dblibs.dao.UserDao
import com.decard.dblibs.entity.UserEntity
import com.decard.dblibs.relationship.dao.CompanyDao
import com.decard.dblibs.relationship.dao.DepartmentDao
import com.decard.dblibs.relationship.entity.Company
import com.decard.dblibs.relationship.entity.Department
import java.io.File

@Database(
    entities = [UserEntity::class, Company::class, Department::class, Manager::class, PeerBean::class],
    version = 5,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    //RoomDatabase提供直接访问底层数据库实现，我们通过定义抽象方法返回具体Dao
    //然后进行数据库增删改查的实现。

    abstract fun getUserDao(): UserDao
    abstract fun getCompanyDao(): CompanyDao
    abstract fun getDepartmentDao(): DepartmentDao
    abstract fun getManagerDao(): ManagerDao
    abstract fun getPeerDao(): PeerDao

    companion object {
        private val TAG = "---TradeDatabase"

        private val dbPath =
            Environment.getExternalStorageDirectory().absolutePath + "/DB_FILE_NAME/"
        private val dbName = "DB_NAME"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            val file = File(dbPath)
            if (!file.exists()) {
                Log.d(TAG, "getInstance: 不存在")
                file.mkdirs()
            }

            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also {
                        instance = it
                    }
            }
        }

        private fun buildDatabase(context: Context): AppDataBase {
            Log.d(TAG, "buildDatabase: ")
            return Room.databaseBuilder(context, AppDataBase::class.java, dbPath + dbName)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE User add COLUMN address TEXT")
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS COMPANY (id INTEGER NOT NULL ,"
                            + "name TEXT NOT NULL,"
                            + "age INTEGER NOT NULL,"
                            + "address TEXT NOT NULL,"
                            + "salary DOUBLE NOT NULL,"
                            + "PRIMARY KEY(id))"
                )

                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS DEPARTMENT (id INTEGER NOT NULL PRIMARY KEY,"
                            + "dept TEXT NOT NULL,"
                            + "empId INTEGER  NOT NULL,"
                            + "FOREIGN KEY(empId) REFERENCES COMPANY(id) ON DELETE CASCADE)"
                )
                database.execSQL("CREATE UNIQUE INDEX index_DEPARTMENT_empId ON DEPARTMENT(empId)")
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE MANAGER (id INTEGER NOT NULL," +
                            "classBeans TEXT NOT NULL," +
                            "date INTEGER NOT NULL," +
                            "name TEXT NOT NULL," +
                            "address TEXT NOT NULL," +
                            "PRIMARY KEY(id))"
                )
            }
        }

        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE PEER (id INTEGER NOT NULL,peerName TEXT NOT NULL,PRIMARY KEY(id))")
            }

        }
    }

}