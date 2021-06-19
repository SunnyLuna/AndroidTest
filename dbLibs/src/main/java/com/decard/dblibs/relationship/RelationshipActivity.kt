package com.decard.dblibs.relationship

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.dblibs.AppDataBase
import com.decard.dblibs.R
import com.decard.dblibs.relationship.entity.Company
import com.decard.dblibs.relationship.entity.Department
import io.reactivex.schedulers.Schedulers

class RelationshipActivity : AppCompatActivity() {
    private val TAG = "---RelationshipActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relationship)
        dealData()

    }

    private fun dealData() {
        Log.d(TAG, "dealData: 执行次数")
        val list: MutableList<Company> = ArrayList()
        var company = Company(
            "Paul",
            32,
            "California",
            20000.0
        )
        list.add(company)
        company = Company(
            "Allen",
            25,
            "Texas",
            15000.0
        )
        list.add(company)
        company = Company(
            "Teddy",
            23,
            "Norway",
            20000.0
        )
        list.add(company)
        company = Company(
            "Mark",
            25,
            "Rich-Mond",
            65000.0
        )
        list.add(company)
        company = Company(
            "David",
            27,
            "Texas",
            85000.0
        )
        list.add(company)
        company = Company(
            "Kim",
            22,
            "South-Hall",
            45000.0
        )
        list.add(company)
        company = Company(
            "James",
            24,
            "Houston",
            10000.0
        )
        list.add(company)

        val departmentList: MutableList<Department> = ArrayList()
        var department =
            Department("IT Billing", 1)
        departmentList.add(department)
        department =
            Department("Engineerin", 2)
        departmentList.add(department)
        department =
            Department("Finance", 7)
        departmentList.add(department)

//        AppDataBase.getInstance(this).getCompanyDao().insertCompanyList(list)
//            .subscribeOn(Schedulers.io()).subscribe({
//
//            }, {
//                Log.d(TAG, "getCompanyDao: ${it.message}")
//            })
//        AppDataBase.getInstance(this).getDepartmentDao().addDepartments(departmentList)
//            .subscribeOn(Schedulers.io()).subscribe({
//
//            }, {
//                Log.d(TAG, "getDepartmentDao: ${it.message}")
//            })

        AppDataBase.getInstance(this).getCompanyDao().getAllCompany().subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "company: ${it.size}")
                for (company in it) {
                    Log.d(TAG, "company:$company")
                }
            }, {
                Log.d(TAG, "company: ${it.message}")
            })

        AppDataBase.getInstance(this).getDepartmentDao().getAllDepartment()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "department: ${it.size}")
                for (department in it) {
                    Log.d(TAG, "department: $department")
                }
            }, {
                Log.d(TAG, "department: ${it.message}")
            })

        AppDataBase.getInstance(this).getDepartmentDao().getDepartmentFormCompany()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "join: ${it.size}")
                for (join in it) {
                    Log.d(TAG, "join: $join")
                }
            }, {
                Log.d(TAG, "join: ${it.message}")
            })
    }
}