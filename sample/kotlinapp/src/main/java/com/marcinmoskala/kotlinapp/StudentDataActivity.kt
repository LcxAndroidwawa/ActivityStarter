package com.marcinmoskala.kotlinapp

import activitystarter.Arg
import activitystarter.KArg
import activitystarter.MakeActivityStarter
import activitystarter.Optional
import activitystarterkotlin.extra
import activitystarterkotlin.extraNullable
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_data.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

@MakeActivityStarter
class StudentDataActivity : BaseActivity() {

    @get:KArg @Optional val name: String by extra("No name provided")
    @get:KArg @Optional val id: Int? by extraNullable()
    @get:KArg val grade: Char? by extraNullable()
    @get:KArg val isPassing: Boolean? by extraNullable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        nameView.text = "Name: " + name
        idView.text = "Id: " + id
        gradeView.text = "Grade: " + grade
        isPassingView.text = "Passing status: " + isPassing
    }


    companion object {

        private val NO_ID = -1

    }
}