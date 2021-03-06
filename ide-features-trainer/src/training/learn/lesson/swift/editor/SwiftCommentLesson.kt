package training.learn.lesson.swift.editor

import training.learn.LessonsBundle
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.LessonSample
import training.learn.lesson.kimpl.parseLessonSample

class SwiftCommentLesson(module: Module) : KLesson("swift.editorbasics.commentline", LessonsBundle.message("swift.editor.comment"), module, "Swift") {


  private val sample: LessonSample = parseLessonSample("""
import Foundation
import UIKit

class Comment: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let x = 0
        let y = 50

        Here we add a table view
        to our view controller
        let tableView = UITableView(frame: CGRect.zero)


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


    task { caret(12, 1) }
    task {
      triggers("CommentByLineComment")
      text(LessonsBundle.message("swift.editor.comment.intro", action("CommentByLineComment")))
    }
    task { caret(12, 1) }
    task {
      triggers("CommentByLineComment")
      text(LessonsBundle.message("swift.editor.comment.uncomment", action("CommentByLineComment")))
    }
    task { caret(12, 1) }
    task {
      triggers("EditorDownWithSelection", "CommentByLineComment")
      text(LessonsBundle.message("swift.editor.comment.several.lines", action("EditorDownWithSelection"), action("CommentByLineComment")))
    }
    task { select(1, 1, 1, 1) }

    task { caret(14, 37) }
    task {
      triggers("EditorSelectWord", "EditorSelectWord", "CommentByBlockComment")
      text(LessonsBundle.message("swift.editor.comment.block", action("EditorSelectWord"), code("frame: CGRect.zero"), action("CommentByBlockComment")))
    }
  }
}