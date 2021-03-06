package training.learn.lesson.swift.refactorings

import training.learn.LessonsBundle
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.LessonSample
import training.learn.lesson.kimpl.parseLessonSample

class SwiftExtractVariableLesson(module: Module) : KLesson("swift.refactorings.extract.variable", LessonsBundle.message("swift.refactoring.extract.variable.name"), module, "Swift") {

  private val sample: LessonSample = parseLessonSample("""
import UIKit

class ExtractVariable: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let tableView = UITableView(frame: CGRect(x: 0, y: 50, width: 320, height: 400))

        let header = UILabel()

        header.text = "AppCode"
        header.frame = CGRect(x: 0, y: 50, width: 320, height: 400)
        header.sizeToFit()

        tableView.tableHeaderView = header

        self.view.addSubview(tableView)
    }
}""".trimIndent())
  override val lessonContent: LessonContext.() -> Unit = {
    prepareSample(sample)
    task { caret(8, 61) }
    task {
      triggers("IntroduceVariable", "NextTemplateVariable")
      text(LessonsBundle.message("swift.refactoring.extract.variable.exec", action("IntroduceVariable"), code("50"), code("y")))
    }
    task {
      text(LessonsBundle.message("swift.refactoring.extract.variable.description"))
    }
    task {
      triggers("IntroduceVariable", "NextTemplateVariable")
      text(LessonsBundle.message("swift.refactoring.extract.variable.exec.again", action("IntroduceVariable"), code("CGRect"), code("frame")))
    }
  }
}