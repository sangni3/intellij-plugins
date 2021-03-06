package training.learn.lesson.swift.navigation

import com.intellij.icons.AllIcons
import training.learn.LessonsBundle
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.LessonSample
import training.learn.lesson.kimpl.parseLessonSample

class SwiftMainWindowsViewsLesson(module: Module) : KLesson("swift.navigation.toolwindows", LessonsBundle.message("swift.navigation.windows.name"), module, "Swift") {

  private val sample: LessonSample = parseLessonSample("""
import UIKit

class Navigation: UITableViewController {

    var detailViewController: DetailViewController? = nil
    var objects = [Any]()


    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        navigationItem.leftBarButtonItem = editButtonItem

        let addButton = UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(insertNewObject(_:)))
        navigationItem.rightBarButtonItem = addButton
        if let split = splitViewController {
            let controllers = split.viewControllers
            detailViewController = (controllers[controllers.count-1] as! UINavigationController).topViewController as? DetailViewController
        }
    }

    override func viewWillAppear(_ animated: Bool) {
        clearsSelectionOnViewWillAppear = splitViewController!.isCollapsed
        super.viewWillAppear(animated)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @objc
    func insertNewObject(_ sender: Any) {
        objects.insert(NSDate(), at: 0)
        let indexPath = IndexPath(row: 0, section: 0)
        tableView.insertRows(at: [indexPath], with: .automatic)
    }

    // MARK: - Segues

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showDetail" {
            if let indexPath = tableView.indexPathForSelectedRow {
                let object = objects[indexPath.row] as! NSDate
                let controller = (segue.destination as! UINavigationController).topViewController as! DetailViewController
                controller.detailItem = object
                controller.navigationItem.leftBarButtonItem = splitViewController?.displayModeButtonItem
                controller.navigationItem.leftItemsSupplementBackButton = true
            }
        }
    }

    // MARK: - Table View

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return objects.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)

        let object = objects[indexPath.row] as! NSDate
        cell.textLabel!.text = object.description
        return cell
    }

    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }

    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            objects.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
        }
    }


}""".trimIndent())
  override val lessonContent: LessonContext.() -> Unit = {
    prepareSample(sample)
    task {
      text(LessonsBundle.message("swift.navigation.windows.intro"))
    }
    task {
      triggers("ActivateProjectToolWindow")
      text(LessonsBundle.message("swift.navigation.windows.project", action("ActivateProjectToolWindow")))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.project2"))
    }
    task {
      triggers("ProjectView.ManualOrder")
      text(LessonsBundle.message("swift.navigation.windows.project.settings", icon(AllIcons.General.GearPlain)))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.project.settings.more"))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.files", code(".xcworkspace"), code(".xcproject")))
    }
    task {
      triggers("com.intellij.ui.content.tabs.TabbedContentAction\$MyNextTabAction")
      text(LessonsBundle.message("swift.navigation.windows.files.activate", action("NextTab")))
    }
    task { caret(1, 1) }
    task { text(LessonsBundle.message("swift.navigation.windows.return.to.editor", action("EditorEscape"))) }
    task {
      text(LessonsBundle.message("swift.navigation.windows.structure", code("//TODO"), code("//FIXME"), code("#pragma mark"), code("//MARK")))
    }
    task {
      triggers("ActivateStructureToolWindow")
      text(LessonsBundle.message("swift.navigation.windows.structure.activate", action("ActivateStructureToolWindow")))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.jump.to.source", action("EditSource")))
    }
    task {
      triggers("FileStructurePopup")
      text(LessonsBundle.message("swift.navigation.windows.structure.popup", action("FileStructurePopup")))
    }
    task { text(LessonsBundle.message("swift.navigation.windows.dismiss.structure.popup", action("EditorEscape"))) }
    task {
      triggers("FindInPath")
      text(LessonsBundle.message("swift.navigation.windows.find", action("FindInPath")))
    }
    task {
      triggers("Build")
      text(LessonsBundle.message("swift.navigation.windows.build", action("Build")))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.build.messages", icon(AllIcons.General.Filter)))
    }
    task {
      triggers("Run")
      text(LessonsBundle.message("swift.navigation.windows.run", action("Run")))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.run.window", action("ActivateRunToolWindow")))
    }
    task {
      triggers("Stop")
      text(LessonsBundle.message("swift.navigation.windows.stop", action("Stop")))
    }
    task {
      triggers("GotoFile", "MasterViewController.swift")
      text(LessonsBundle.message("swift.navigation.windows.go.to.file", code("MasterViewController.swift"), action("GotoFile")))
    }
    task { caret(11, 9) }
    task {
      triggers("ToggleLineBreakpoint", "Debug")
      text(LessonsBundle.message("swift.navigation.windows.toggle.break", action("ToggleLineBreakpoint"), action("Debug")))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.debug", action("ActivateDebugToolWindow")))
    }
    task {
      triggers("Stop")
      text(LessonsBundle.message("swift.navigation.windows.stop.debug", action("Stop")))
    }
    task { caret(16, 9) }
    task {
      triggers("ViewBreakpoints")
      text(LessonsBundle.message("swift.navigation.windows.breakpoints", action("ViewBreakpoints")))
    }
    task {
      triggers("ActivateVersionControlToolWindow")
      text(LessonsBundle.message("swift.navigation.windows.init.git", action("Vcs.QuickListPopupAction"), action("ActivateVersionControlToolWindow")))
    }
    task {
      text(LessonsBundle.message("swift.navigation.windows.vcs.window"))
    }
  }
}