// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package training.learn.lesson.java.refactorings

import com.intellij.CommonBundle
import com.intellij.codeInsight.template.impl.TemplateManagerImpl
import com.intellij.java.refactoring.JavaRefactoringBundle
import com.intellij.refactoring.rename.RenameProcessor
import com.intellij.testGuiFramework.impl.button
import com.intellij.util.ui.UIUtil
import training.commands.kotlin.TaskContext
import training.commands.kotlin.TaskRuntimeContext
import training.learn.LessonsBundle
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.*
import training.learn.lesson.kimpl.LessonUtil.checkExpectedStateOfEditor
import javax.swing.JDialog

class JavaRenameLesson(module: Module)
  : KLesson("Refactorings.Rename", LessonsBundle.message("rename.lesson.name"), module, "JAVA") {
  private val initialName = "stylus"

  private val template = """
    import java.lang.String;
    
    class Rename {
        private String <caret id=2/><name>;
        private String deviceName;
    
        public void touchDevice(int x, int y, float strength, Device device) {
            device.touchByStylus(<caret><name>, x, y, strength);
        }

        public boolean checkIPad() {
            if (deviceName.equals("iPad")) return true;
            return false;
        }
    
        public String get<name2>() {
            return <name>;
        }
    
        interface Device {
            void touchByStylus(String stylus, int x, int y, float strength);
        }
    }
    
    class Derived extends Rename {
        @Override
        public String get<name2>() {
            System.err.println("Derived method applied");
            return super.get<name2>();
        }
    }
  """.trimIndent() + '\n'

  private val sample = parseLessonSample(replaceTemplate(initialName))

  private fun replaceTemplate(name: String) =
    template.replace("<name>", name)
      .replace("<name2>", name.capitalize())

  override val lessonContent: LessonContext.() -> Unit = {
    val newNameExample = "pencil"

    prepareSample(sample)
    lateinit var startId: TaskContext.TaskId
    task("RenameElement") {
      startId = taskId
      text(LessonsBundle.message("java.rename.press.rename", action(it), code(initialName)))
      triggers(it)
      proposeRestore {
        checkExpectedStateOfEditor(sample, false)
      }
      test {
        actions(it)
      }
    }

    task("NextTemplateVariable") {
      triggers(it)
      text(LessonsBundle.message("java.rename.type.new.name", code(newNameExample), LessonUtil.rawEnter()))
      restoreAfterStateBecomeFalse {
        TemplateManagerImpl.getTemplateState(editor) == null
      }
      test {
        type(newNameExample)
        actions(it)
      }
    }

    task {
      // wait until dialog will be showed
      // It is not necessary step: just don't go further until correct name is chosen (name correctness is checking by rename handler)
      stateCheck {
        focusOwner?.let { fo -> UIUtil.getParentOfType(JDialog::class.java, fo) }?.title ==
          JavaRefactoringBundle.message("rename.accessors.title")
      }
    }

    task {
      val okButtonText = CommonBundle.getOkButtonText()
      text(LessonsBundle.message("java.rename.confirm.accessors.rename",
                                 LessonUtil.rawEnter(), strong(okButtonText)))
      stateCheck {
        val fieldName = getFieldName()
        val shouldBe = fieldName?.let { replaceTemplate(it).replace("<caret>", "").replace("<caret id=2/>", "") }
        fieldName != initialName && editor.document.text == shouldBe
      }
      restoreAfterStateBecomeFalse(startId) {
        !Thread.currentThread().stackTrace.any {
          it.className.contains(RenameProcessor::class.simpleName!!)
        }
      }
      test {
        ideFrame {
          button(okButtonText).click()
        }
      }
    }
  }

  private fun TaskRuntimeContext.getFieldName(): String? {
    val charsSequence = editor.document.charsSequence
    // get position of declaration because it should not shifted after rename
    val start = sample.getPosition(2).startOffset
    val end = charsSequence.indexOf(';', start).takeIf { it > 0 } ?: return null
    val newName = charsSequence.subSequence(start, end)
    if (newName.isEmpty()) return null
    if (!Character.isJavaIdentifierStart(newName[0]) || newName.any { !Character.isJavaIdentifierPart(it) }) return null
    return newName.toString()
  }
}
