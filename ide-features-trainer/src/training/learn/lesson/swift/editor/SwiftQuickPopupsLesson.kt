package training.learn.lesson.swift.editor

import training.learn.LessonsBundle
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.LessonSample
import training.learn.lesson.kimpl.parseLessonSample

class SwiftQuickPopupsLesson(module: Module) : KLesson("swift.codeassistance.quickpopups", LessonsBundle.message("swift.editor.popups.name"), module, "Swift") {

  private val sample: LessonSample = parseLessonSample("""
import Foundation
import UIKit

class Duplicate: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let x = 0
        let y = 50

        let tableView = UITableView()

        let header = UILabel()
        header.text = "AppCode"
        header.sizeToFit()

        tableView.frame = CGRect(x: x, y: y, width: 320, height: 400)
        tableView.tableHeaderView = header
        self.view.addSubview(tableView)
    }

}""".trimIndent())
  override val lessonContent: LessonContext.() -> Unit = {
    prepareSample(sample)
    task { caret(18, 34) }
    task {
      triggers("ParameterInfo")
      text(LessonsBundle.message("swift.editor.popups.param.info", action("ParameterInfo")))
    }
    task {
      triggers("EditorEscape")
      text(LessonsBundle.message("swift.editor.popups.close.param.info", action("EditorEscape")))
    }
    task { caret(4, 26) }
    task {
      triggers("QuickJavaDoc")
      text(LessonsBundle.message("swift.editor.popups.doc", action("QuickJavaDoc")))
    }
    task { caret(4, 26) }
    task {
      triggers("QuickImplementations")
      text(LessonsBundle.message("swift.editor.popups.impl", action("QuickImplementations")))
    }
  }
}