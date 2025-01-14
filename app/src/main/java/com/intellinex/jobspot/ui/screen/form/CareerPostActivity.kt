package com.intellinex.jobspot.ui.screen.form

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intellinex.jobspot.R
import jp.wasabeef.richeditor.RichEditor

class CareerPostActivity : AppCompatActivity() {

    private lateinit var requirement: TextView
    private lateinit var facilities: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_career_post)

        val buttonBack: Button = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

        val requirementRichText: RichEditor = findViewById(R.id.requirementRichText)
        val facilitiesRichText: RichEditor = findViewById(R.id.facilitiesRichText)
        requirementRichText.setEditorHeight(200)
        facilitiesRichText.setEditorHeight(200)
        requirementRichText.setPadding(13, 13, 13, 13)
        facilitiesRichText.setPadding(13, 13, 13, 13)

        requirementRichText.setOnTextChangeListener { text ->

        }

        facilitiesRichText.setOnTextChangeListener { text ->

        }

        findViewById<Button>(R.id.action_undo).setOnClickListener {
            requirementRichText.undo()
        }

        findViewById<Button>(R.id.action_redo).setOnClickListener {
            requirementRichText.redo()
        }

        findViewById<Button>(R.id.action_bold).setOnClickListener {
            requirementRichText.setBold()
        }

        findViewById<Button>(R.id.action_italic).setOnClickListener {
            requirementRichText.setItalic()
        }

        findViewById<Button>(R.id.action_list).setOnClickListener {
            requirementRichText.setBullets()
        }

        findViewById<Button>(R.id.action_undo_f).setOnClickListener {
            facilitiesRichText.undo()
        }

        findViewById<Button>(R.id.action_redo_f).setOnClickListener {
            facilitiesRichText.redo()
        }

        findViewById<Button>(R.id.action_bold_f).setOnClickListener {
            facilitiesRichText.setBold()
        }

        findViewById<Button>(R.id.action_italic_f).setOnClickListener {
            facilitiesRichText.setItalic()
        }

        findViewById<Button>(R.id.action_list_f).setOnClickListener {
            facilitiesRichText.setBullets()
        }

        val jobTitle = findViewById<TextView>(R.id.jobTitle)
        val typeOfWorkspace = findViewById<TextView>(R.id.typeOfWorkspace)
        val jobLocation = findViewById<TextView>(R.id.jobLocation)
        val employmentType = findViewById<TextView>(R.id.employmentType)
        val jobDescription = findViewById<TextView>(R.id.jobDescription)
        val buttonPost = findViewById<Button>(R.id.buttonPost)
        val buttonSaveDraft = findViewById<Button>(R.id.buttonSaveDraft)


    }

}