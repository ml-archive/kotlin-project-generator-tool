package dk.nodes.generator.views.controllers

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.stage.DirectoryChooser
import dk.nodes.generator.models.ProjectGeneratorListener
import dk.nodes.generator.templating.ProjectGenerator
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter


class MainViewController : ProjectGeneratorListener {
    @FXML
    private lateinit var textFieldProjectName: TextField
    @FXML
    private lateinit var textFieldOutput: TextField
    @FXML
    private lateinit var anchorPane: AnchorPane
    @FXML
    private lateinit var containerGrid: GridPane
    @FXML
    private lateinit var progressBar: ProgressIndicator

    private val isValidPackageName: Boolean
        get() {
            val packageNameValue = textFieldProjectName.text.trim()
            val packageNameArray = packageNameValue.split("")
            return packageNameArray.size == 3
        }

    private val projectGenerator = ProjectGenerator(this)

    @FXML
    fun initialize() {
        setLoading(false)
        updateFields()
    }

    @FXML
    fun onBrowseClicked() {
        val directoryChooser = DirectoryChooser()

        directoryChooser.title = "Select output folder"
        directoryChooser.initialDirectory = projectGenerator.destination

        val file: File? = directoryChooser.showDialog(anchorPane.scene.window)

        file?.let {
            textFieldOutput.text = it.absolutePath
        }
    }

    @FXML
    fun onGenerateClicked(event: ActionEvent) {
        println("onGenerateClicked")

        if (!isValidPackageName) {
            showInvalidPackageNameDialog()
            return
        }

        projectGenerator.packageName = textFieldProjectName.text
        projectGenerator.destination = File(textFieldOutput.text)

        setLoading(true)

        projectGenerator.generate()
    }

    @FXML
    fun onCancelClicked(event: ActionEvent) {
        println("onCancelClicked")
        exitApplication()
    }

    private fun updateFields() {
        textFieldProjectName.text = projectGenerator.packageName
        textFieldOutput.text = projectGenerator.destination.absolutePath
    }

    private fun showSuccessDialog() {
        val alert = Alert(AlertType.INFORMATION)

        alert.title = "Project Generated"
        alert.headerText = null
        alert.contentText = "Project Has been generated successfully"

        val result = alert.showAndWait()

        if (result.get() == ButtonType.OK) {
            exitApplication()
        }
    }

    private fun showInvalidPackageNameDialog() {
        val alert = Alert(AlertType.ERROR)

        alert.title = "Invalid Package Name"
        alert.headerText = null
        alert.contentText = "Package name must follow the following format `com.example.project`"

        alert.showAndWait()
    }

    private fun showErrorDialog(error: Throwable) {
        val alert = Alert(AlertType.ERROR)
        alert.title = "Exception Dialog"
        alert.headerText = "Something happened ¯\\_(ツ)_/¯"
        alert.contentText = "Yell at Brian"

        // Create expandable Exception.
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        error.printStackTrace(pw)
        val exceptionText = sw.toString()

        val label = Label("The exception stacktrace was:")

        val textArea = TextArea(exceptionText)
        textArea.isEditable = false
        textArea.isWrapText = true

        textArea.setMaxWidth(java.lang.Double.MAX_VALUE)
        textArea.setMaxHeight(java.lang.Double.MAX_VALUE)
        GridPane.setVgrow(textArea, Priority.ALWAYS)
        GridPane.setHgrow(textArea, Priority.ALWAYS)

        val expContent = GridPane()
        expContent.maxWidth = java.lang.Double.MAX_VALUE
        expContent.add(label, 0, 0)
        expContent.add(textArea, 0, 1)

        // Set expandable Exception into the dialog pane.
        alert.dialogPane.expandableContent = expContent

        alert.showAndWait()
    }

    private fun setLoading(isLoading: Boolean) {
        progressBar.isVisible = isLoading
        containerGrid.isVisible = !isLoading
    }

    private fun exitApplication() {
        Platform.exit()
        System.exit(0)
    }

    override fun onSuccess() {
        showSuccessDialog()
    }

    override fun onError(error: Throwable) {
        showErrorDialog(error)
    }
}